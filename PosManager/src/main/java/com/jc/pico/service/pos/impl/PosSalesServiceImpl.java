package com.jc.pico.service.pos.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jc.pico.bean.SvcClosingDetail;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderExample;
import com.jc.pico.bean.SvcSalesDiscount;
import com.jc.pico.bean.SvcSalesItemOpt;
import com.jc.pico.bean.SvcSalesPay;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.service.pos.PosSalesService;
import com.jc.pico.service.pos.SalesService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosSalesClosingSave;
import com.jc.pico.utils.bean.PosSalesClosingSaveDetail;
import com.jc.pico.utils.bean.PosSalesOrderInfoOption;
import com.jc.pico.utils.bean.PosSalesSaleSave;
import com.jc.pico.utils.bean.PosSalesSaleSaveDetail;
import com.jc.pico.utils.bean.PosSalesSaleSaveDiscount;
import com.jc.pico.utils.bean.PosSalesSaleSavePay;
import com.jc.pico.utils.bean.SvcClosingExtended;
import com.jc.pico.utils.bean.SvcSalesExtended;
import com.jc.pico.utils.bean.SvcSalesItemExtended;

@Service
public class PosSalesServiceImpl implements PosSalesService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SalesService salesService;

	@Autowired
	private SvcOrderMapper svcOrderMapper;
	
	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private PosUtil posUtil;

	
	/**
	 * saveSale
	 */
	@Override
	public String saveSale(Map<String, Object> posBaseKeyMap, PosSalesSaleSave posSalesSaleSave) throws Throwable {

		logger.debug("pos saveSale: {}", posSalesSaleSave);
		
		final Long storeId = Long.parseLong(posSalesSaleSave.getCdStore());		
		final SvcStore store = svcStoreMapper.selectByPrimaryKey(storeId);
		final Long staffId = posUtil.parseLong(posSalesSaleSave.getCdEmployee(), null);		
		final TimeZone storeTimeZone = posUtil.getStoreTimeZone(storeId);

		
		// 1. TB_SVC_SALE 생성
		SvcSalesExtended svcSalesExtended = new SvcSalesExtended();

		// 주문시간은 OpenDt
		final Date openDt = posUtil.parseDate(posSalesSaleSave.getYmdSale(), null); // 매장 오픈일자(시간은 무의미 함)
		if (openDt == null) {
			// 매출 날짜가 없으면 전혀 의미가 없으므로 오류
			logger.error("[{}][{}] ymdSale is invalid: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, posSalesSaleSave.getYmdSale());
			return null;
		}
		
		// 주문 번호는 단말에서 생성. POS는 1부터 채번, clerk/tab/app 은 System.currentTimeMillis() 사용. 
		String orderNo = null; // 매장내 주문번호
		Long noOrder = posSalesSaleSave.getNoOrder();
		if (noOrder != null && noOrder.longValue() != 0) {
			orderNo = Long.toString(noOrder);
		} else {
			// orderNo = Long.toString(System.nanoTime()); // 주문번호가 앞단에서 들어오지 않으면 뒷단에서 생성해야 한다. OrderService 로 옮김
			logger.error("[{}][{}] noOrder is invalid: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, noOrder);
			return null;
		}		

		// S: 매출 기본정보 등록
		svcSalesExtended.setStoreId(storeId);
		svcSalesExtended.setOpenDt(openDt);
		svcSalesExtended.setPosNo(posSalesSaleSave.getNoPos());
		svcSalesExtended.setReceiptNo(posUtil.toString(posSalesSaleSave.getNoRcp()));
		svcSalesExtended.setUserId(posUtil.parseLong(posSalesSaleSave.getCdMember(), null)); // 고객 번호
		svcSalesExtended.setStaffId(staffId);
		svcSalesExtended.setSalesSt(posUtil.getBaseCode("809", posSalesSaleSave.getDsSale())); // 판매형태코드(1:정상, 2:반품, 3:주문, 4:시식, 5:증정, 6:폐기, 7:접대, 8:손실, 9:Void, 10:점간이동, 11:배달, 12:선입금, 13:외상매출입금, 14:상품교환) (809xxx)
		svcSalesExtended.setOrderTp(posSalesSaleSave.getCdOrderType().toString()); // 주문유형코드(605001:주문,605002:예약,605003:계약)
		svcSalesExtended.setPathTp(posSalesSaleSave.getCdOrderPath().toString()); // 주문 경로 타입(pos: 606001, app: 606002,  cleck: 606003, tab: 606004)
		svcSalesExtended.setOrderSt(posSalesSaleSave.getCdOrderVerify().toString()); // 주문처리코드(6071:미승인, 6072:승인, 6073:취소, 6074:거부, 6075:완료)
		svcSalesExtended.setOrderDiv(posSalesSaleSave.getDsOrder()); // 주문형태 (1:테이블, 2:배달, 3:포장)		
		svcSalesExtended.setSales(posSalesSaleSave.getAmtSale()); // 매출
		svcSalesExtended.setActualSalesAmt(posSalesSaleSave.getAmtSale() - posSalesSaleSave.getAmtDc()); // 실매출
		svcSalesExtended.setSupplyValue(posSalesSaleSave.getAmtSupply()); // 공급가액 (순매출)
		svcSalesExtended.setTax(posSalesSaleSave.getAmtVat()); // 세금
		svcSalesExtended.setDiscount(posSalesSaleSave.getAmtDc()); // 할인
		svcSalesExtended.setServiceCharge(posSalesSaleSave.getAmtTip());
		svcSalesExtended.setCustomerTp(posUtil.getBaseCode("855", posSalesSaleSave.getCdMemberType())); // 고객형태코드
		svcSalesExtended.setCustomeAgeTp(posUtil.getBaseCode("846", posSalesSaleSave.getCdMemberAge())); // 고객연령대코드
		svcSalesExtended.setCustomeGender(posUtil.getBaseCode("101", posSalesSaleSave.getCdMemberSex())); // 고객성별코드
		svcSalesExtended.setCustomerCnt(posSalesSaleSave.getCntCustomer() != null ? posSalesSaleSave.getCntCustomer().shortValue() : null);
		svcSalesExtended.setSalesTmLocal(posUtil.parseDateTime(posSalesSaleSave.getDtSale(), null)); // 매출 일시
		svcSalesExtended.setSalesTm(posUtil.convertLocaltoUtc(svcSalesExtended.getSalesTmLocal(), storeTimeZone)); // 매출 일시 (UTC)
		svcSalesExtended.setOrderTmLocal(posUtil.parseDateTime(posSalesSaleSave.getDtOrder(), null)); // 주문 일시
		svcSalesExtended.setOrderTm(posUtil.convertLocaltoUtc(svcSalesExtended.getOrderTmLocal(), storeTimeZone)); // 주문 일시 (UTC)
		svcSalesExtended.setAdmissionTm(posUtil.parseDateTime(posSalesSaleSave.getDtAdmission(), null)); // 입장 시간
		svcSalesExtended.setExitTm(posUtil.parseDateTime(posSalesSaleSave.getDtExit(), null)); // 퇴장 시간
		svcSalesExtended.setTableNo(posUtil.toString(posSalesSaleSave.getNoTable(), null));
		svcSalesExtended.setMemo(posSalesSaleSave.getMemo());
		svcSalesExtended.setBrandId(store.getBrandId());		
		svcSalesExtended.setReason(posSalesSaleSave.getCdReturnReason() != null ? posSalesSaleSave.getCdReturnReason().toString() : null); // 반품사유코드
		svcSalesExtended.setOrderNo(orderNo);
		svcSalesExtended.setServiceCharge(posSalesSaleSave.getAmtTip());
		svcSalesExtended.setOrderId(getOrderId(storeId, orderNo, openDt));
		svcSalesExtended.setAcceptTmLocal(posUtil.parseDateTime(posSalesSaleSave.getDtApproval(), null)); // 승인시간
		svcSalesExtended.setAcceptTm(posUtil.convertLocaltoUtc(svcSalesExtended.getAcceptTmLocal(), storeTimeZone)); // 승인시간 (UTC)
		// E: 매출 기본정보 등록

		// S: 아이템별 등록
		// DETAIL 로부터 읽어옴
		svcSalesExtended.setSvcSalesItems(toSvcSalesItem(posSalesSaleSave.getDetail(), staffId, storeTimeZone)); // 매출에 매출품목 리스트 세팅
		// E: 아이템별 등록

		// S: 결제내역별 등록
		svcSalesExtended.setSvcSalesPays(toSvcSalesPay(posSalesSaleSave.getPay(), staffId)); // 매출에 결제 리스트 세팅

		// S: 할인내역 변환
		svcSalesExtended.setSvcSalesDiscounts(toSvcSalesDiscount(posSalesSaleSave.getDiscount(), staffId)); // 매출에 할인 리스트 세팅

		return salesService.saveSales(svcSalesExtended);
	}

	
	/**
	 * 포스 주문 상품 정보 서버 상품 정보로 변환
	 * PosSalesSaleSaveDetail -> SvcSalesItem
	 * 
	 * @param posDetails
	 * @param regDt
	 * @param staffId
	 * @return
	 */
	private List<SvcSalesItemExtended> toSvcSalesItem(List<PosSalesSaleSaveDetail> posDetails, Long staffId, TimeZone storeTimeZone) {
		if (posDetails == null || posDetails.isEmpty()) {
			return Collections.emptyList();
		}

		List<SvcSalesItemExtended> result = new ArrayList<SvcSalesItemExtended>();
		for (PosSalesSaleSaveDetail posDetail : posDetails) {			
		
			SvcSalesItemExtended svcSalesItem = new SvcSalesItemExtended();
			svcSalesItem.setSalesSt(posUtil.getBaseCode("809", posDetail.getDsSale()));
			svcSalesItem.setOrdinal(posDetail.getSeq());
			svcSalesItem.setPartialOrdinal(posDetail.getSeq_partial());
			svcSalesItem.setItemId(Long.parseLong(posDetail.getCdGoods()));
			svcSalesItem.setItemNm(posDetail.getNmGoods());
			svcSalesItem.setItemTp(posUtil.getBaseCode("818", posDetail.getCdSaleType()));
			svcSalesItem.setPurchasePrice(posDetail.getPrBuy());
			svcSalesItem.setPrice(posDetail.getPrSale());
			svcSalesItem.setCount(posDetail.getQtySale() != null ? posDetail.getQtySale().shortValue() : null);
			svcSalesItem.setSales(posDetail.getAmtSale());
			svcSalesItem.setActualSalesAmt(posDetail.getAmtSale() - posDetail.getAmtDc());
			svcSalesItem.setNetSales(posDetail.getAmtSupply());
			svcSalesItem.setTax(posDetail.getAmtVat());
			svcSalesItem.setDiscount(posDetail.getAmtDc());
			svcSalesItem.setMemo(posDetail.getMemo());
			svcSalesItem.setIsPacking(posDetail.getYnPacking() == 1 ? true : false);
			svcSalesItem.setStaffId(staffId);
			svcSalesItem.setPathTp(posUtil.toString(posDetail.getCdOrderPath()));
			svcSalesItem.setServiceCharge(0d);
			svcSalesItem.setOrderTmLocal(posUtil.parseDateTime(posDetail.getDtOrder(), null)); // 주문 시간
			svcSalesItem.setOrderTm(posUtil.convertLocaltoUtc(svcSalesItem.getOrderTmLocal(), storeTimeZone)); // 주문시간 UTC
			
			// 매출 상품 옵션
			svcSalesItem.setOptions(toSvcSalesItemOpts(posDetail.getOptions()));

			result.add(svcSalesItem); // 매출품목 리스트에 개별 품목 추가
		}

		return result;
	}

	/**
	 * 포스 결제 정보를 서버 결제 정보로 변환
	 * PosSalesSaleSavePay -> SvcSalesPay
	 * 
	 * @param posPays
	 * @param staffId
	 * @return
	 */
	private List<SvcSalesPay> toSvcSalesPay(List<PosSalesSaleSavePay> posPays, Long staffId) {
		if (posPays == null || posPays.isEmpty()) {
			return Collections.emptyList();
		}
		List<SvcSalesPay> result = new ArrayList<SvcSalesPay>();
		for (PosSalesSaleSavePay posPay : posPays) {
			SvcSalesPay svcPay = new SvcSalesPay();
			svcPay.setSalesSt(posUtil.getBaseCode("809", posPay.getDsSale()));
			svcPay.setOrdinal(posPay.getSeq());
			svcPay.setPartialOrdinal(posPay.getSeq_partial());
			svcPay.setPayMethod(posUtil.getBaseCode("810", posPay.getCdPay()));
			svcPay.setAmount(posPay.getAmtPay());
			svcPay.setStaffId(staffId);
			svcPay.setPgKind(posUtil.toBaseCode(posPay.getCdPgKind()));
			
			result.add(svcPay); // 매출 결제내역 리스트에 개별 결제 추가
		}

		return result;
	}

	private List<SvcSalesDiscount> toSvcSalesDiscount(List<PosSalesSaleSaveDiscount> posDiscounts, Long staffId) {
		if (posDiscounts == null || posDiscounts.size() == 0) {
			return Collections.emptyList();
		}

		List<SvcSalesDiscount> result = new ArrayList<SvcSalesDiscount>(posDiscounts.size());

		for (PosSalesSaleSaveDiscount posDiscount : posDiscounts) {
			SvcSalesDiscount svcDiscount = new SvcSalesDiscount();
			svcDiscount.setOrdinal(posDiscount.getSeq());
			svcDiscount.setDiscountTp(posUtil.getBaseCode("824", posDiscount.getCdDcType()));
			svcDiscount.setAmount(posDiscount.getAmtDc());
			svcDiscount.setCouponCd(posDiscount.getNoCoupon());
			svcDiscount.setStaffId(staffId);

			result.add(svcDiscount); // 매출 할인내역 리스트에 개별 할인 추가
		}

		return result;
	}

	@Override
	public Long saveClosing(Map<String, Object> posBaseKeyMap, PosSalesClosingSave posSalesClosingSave) throws Throwable {

		// 마감 마스터 정보 변환 PosSalesClosingSave -> SvcClosingExtended
		SvcClosingExtended svcClosingExtended = toSvcClosing(posSalesClosingSave, posBaseKeyMap);

		// 마감 상세정보 변환 PosSalesClosingSaveDetail -> SvcClosingDetail
		svcClosingExtended.setSvcClosingDetails(toSvcClosingDetail(posSalesClosingSave.getDetail())); // 마감에 마감상세 리스트 세팅

		return salesService.saveClosing(svcClosingExtended);
	}

	/**
	 * 마감 마스터 정보 변환 PosSalesClosingSave -> SvcClosingExtended
	 * 
	 * @param posSalesClosingSave
	 * @param posInfo
	 * @return
	 * @throws RequestResolveException
	 *             데이터가 invalid 한 경우 발생
	 */
	private SvcClosingExtended toSvcClosing(PosSalesClosingSave posSalesClosingSave, Map<String, Object> posInfo) throws RequestResolveException {

		final Long brandId = (Long) (posInfo != null && posInfo.get(PosUtil.BASE_INFO_BRAND_ID) != null ? posInfo.get(PosUtil.BASE_INFO_BRAND_ID)
				: 0L); // 브랜드

		final boolean isClose = posSalesClosingSave.getYnClose() == 1; // 0: false, 1: true		
		final Date closingDt = posUtil.parseDate(posSalesClosingSave.getYmdClose(), null);
		final Date dtOpenDt = posUtil.parseDateTime(posSalesClosingSave.getDtOpen(), null); // 개점, 마감시 존재
		final Date dtCloseDt = posUtil.parseDateTime(posSalesClosingSave.getDtClose(), null); // 개점시 미존재, 마감시 존재

		// 마감일자는 항상 존재 (포스에서 전달하는 YMD_CLOSE는 개점일이다.)
		if (closingDt == null) {
			throw new RequestResolveException(PosUtil.EPO_0005_CODE, "YMD_CLOSE is empty or invalid foramt. required " + PosUtil.DAY_FORMAT);
		}

		// 개점일자는 항상 존재
		if (dtOpenDt == null) {
			throw new RequestResolveException(PosUtil.EPO_0005_CODE,
					"DT_OPEN is empty or invalid foramt. required " + PosUtil.DATE_TIME_FORAMT.toPattern());
		}

		// 마감 정보인데 dtCloseDt 의 파싱 결과가 없으면 오류
		// 전달 된 마감 시간이 없거나, 포맷 오류
		if (isClose && dtCloseDt == null) {
			throw new RequestResolveException(PosUtil.EPO_0005_CODE,
					"DT_CLOSE is empty or invalid foramt. required " + PosUtil.DATE_TIME_FORAMT.toPattern());
		}

		// 1. TB_SVC_CLOSING 생성
		SvcClosingExtended result = new SvcClosingExtended();
		// S: 마감 기본정보 등록
		result.setBrandId(brandId);
		result.setStoreId(Long.parseLong(posSalesClosingSave.getCdStore()));
		result.setOpenDt(closingDt);
		result.setIsClosing(isClose);
		result.setOpenTm(dtOpenDt);
		result.setCloseTm(dtCloseDt);
		result.setOpenReserve(posSalesClosingSave.getAmtOpenReserve()); // 준비금
		result.setSales(posSalesClosingSave.getAmtSale()); // 판매금액);
		result.setSalesCnt(posSalesClosingSave.getCntSale()); // 판매건수);
		result.setRefund(posSalesClosingSave.getAmtSaleReturn()); // 반품금액);
		result.setRefundCnt(posSalesClosingSave.getCntSaleReturn()); // 반품건수);
		result.setDiscount(posSalesClosingSave.getAmtDc()); // 할인금액
		result.setDiscountCnt(posSalesClosingSave.getCntDc()); // 할인건수
		result.setCustomerCnt(posSalesClosingSave.getCntCustomer()); // 객수
		result.setCashIn(posSalesClosingSave.getAmtCashIn()); // 현금입금액
		result.setCashInCnt(posSalesClosingSave.getCntCashIn()); // 현금입금건수
		result.setCashOut(posSalesClosingSave.getAmtCashOut()); // 현금출금액
		result.setCashOutCnt(posSalesClosingSave.getCntCashOut()); // 현금출금건수
		result.setCashOnHand(posSalesClosingSave.getAmtCashOnHand()); // 현금시재
		result.setCashLack(posSalesClosingSave.getAmtCashLack());
		// posSalesClosingSave.getNoPos(); // 포스번호
		// E: 마감 기본정보 등록
		return result;
	}

	/**
	 * 마감 상세정보 변환 PosSalesClosingSaveDetail -> SvcClosingDetail
	 * 
	 * @param posDetails
	 * @return
	 * @throws RequestResolveException
	 *             데이터가 invalid 한 경우 발생
	 */
	private List<SvcClosingDetail> toSvcClosingDetail(List<PosSalesClosingSaveDetail> posDetails) throws RequestResolveException {

		if (posDetails == null || posDetails.size() == 0) {
			return Collections.emptyList();
		}

		List<SvcClosingDetail> result = new ArrayList<SvcClosingDetail>();
		for (PosSalesClosingSaveDetail posDetail : posDetails) {

			// 개점시간 상세는 dtOpenDetailDt (보조 데이터, 없다고 오류처리할 데이터는 아닌 것으로.)				
			final Date dtOpenDetailDt = posUtil.parseDateTime(posDetail.getDtOpen(), null); // 매장 오픈일시 상세
			if (dtOpenDetailDt == null && !StringUtils.isEmpty(posDetail.getDtOpen())) {
				throw new RequestResolveException(PosUtil.EPO_0005_CODE,
						"Detail DT_OPEN is empty or invalid foramt. required " + PosUtil.DATE_TIME_FORAMT.toPattern());
			}

			// 폐점시간 상세는 dtCloseDetailDt (보조 데이터, 없다고 오류처리할 데이터는 아닌 것으로.)
			final Date dtCloseDetailDt = posUtil.parseDateTime(posDetail.getDtClose(), null); // 매장 마감일시 상세
			if (dtCloseDetailDt == null && !StringUtils.isEmpty(posDetail.getDtClose())) {
				throw new RequestResolveException(PosUtil.EPO_0005_CODE,
						"Detail DT_CLOSE is empty or invalid foramt. required " + PosUtil.DATE_TIME_FORAMT.toPattern());
			}

			SvcClosingDetail svcClosingDetail = new SvcClosingDetail();
			svcClosingDetail.setOrdinal(posDetail.getNoClosing().intValue());
			svcClosingDetail.setStaffId(posUtil.parseLong(posDetail.getCdEmployee(), null));
			svcClosingDetail.setIsClosing(posDetail.getYnClose() == 1);
			svcClosingDetail.setOpenTm(dtOpenDetailDt);
			svcClosingDetail.setCloseTm(dtCloseDetailDt);
			svcClosingDetail.setOpenReserve(posDetail.getAmtOpenReserve());
			svcClosingDetail.setSales(posDetail.getAmtSale());
			svcClosingDetail.setSalesCnt(posDetail.getCntSale());
			svcClosingDetail.setRefund(posDetail.getAmtSaleReturn());
			svcClosingDetail.setRefundCnt(posDetail.getCntSaleReturn());
			svcClosingDetail.setDiscount(posDetail.getAmtDc());
			svcClosingDetail.setDiscountCnt(posDetail.getCntDc());
			svcClosingDetail.setCustomerCnt(posDetail.getCntCustomer());
			svcClosingDetail.setCashIn(posDetail.getAmtCashIn());
			svcClosingDetail.setCashInCnt(posDetail.getCntCashIn());
			svcClosingDetail.setCashOut(posDetail.getAmtCashOut());
			svcClosingDetail.setCashOutCnt(posDetail.getCntCashOut());
			svcClosingDetail.setCheck(posDetail.getAmtCheck());
			svcClosingDetail.setCnt01(posDetail.getCnt100000());
			svcClosingDetail.setCnt02(posDetail.getCnt50000());
			svcClosingDetail.setCnt03(posDetail.getCnt10000());
			svcClosingDetail.setCnt04(posDetail.getCnt5000());
			svcClosingDetail.setCnt05(posDetail.getCnt1000());
			svcClosingDetail.setCnt06(posDetail.getCnt500());
			svcClosingDetail.setCnt07(posDetail.getCnt100());
			svcClosingDetail.setCnt08(posDetail.getCnt50());
			svcClosingDetail.setCnt09(posDetail.getCnt10());
			svcClosingDetail.setCashOnHand(posDetail.getAmtCashOnHand());
			svcClosingDetail.setCashLack(posDetail.getAmtCashLack());

			result.add(svcClosingDetail); // 마감 상세 리스트에 개별 상세 추가
		}
		return result;

	}

	private Long getOrderId(Long storeId, String orderNo, Date openDt) {
		if (storeId == null || orderNo == null || openDt == null) {
			return null;
		}
		SvcOrderExample example = new SvcOrderExample();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(openDt);

		posUtil.setTime(cal, 0, 0, 0, 0);
		Date startOpenDt = cal.getTime();

		posUtil.setTime(cal, 23, 59, 59, 999);
		Date endOpenDt = cal.getTime();

		example.createCriteria() // 검색 조건
				.andOrderNoEqualTo(orderNo) // 주문 번호
				.andStoreIdEqualTo(storeId) // 스토어 ID
				.andOpenDtBetween(startOpenDt, endOpenDt); // 포스 오픈 날짜

		List<SvcOrder> orders = svcOrderMapper.selectByExample(example);
		return orders.isEmpty() ? null : orders.get(0).getId();
	}
	
		/**
	 * PosSalesOrderInfoOption 을 SvcOrderItemOpt 으로 변환
	 * 
	 * @param srcOptions
	 * @return
	 */
	private List<SvcSalesItemOpt> toSvcSalesItemOpts(List<PosSalesOrderInfoOption> srcOptions) {
		if (srcOptions == null) {
			return Collections.emptyList();
		}
		List<SvcSalesItemOpt> result = new ArrayList<>(srcOptions.size());

		for (PosSalesOrderInfoOption srcOpt : srcOptions) {
			SvcSalesItemOpt dstOpt = new SvcSalesItemOpt();
			dstOpt.setOptId(srcOpt.getNoOpt());
			dstOpt.setOptDtlId(srcOpt.getNoOptDtl());
			dstOpt.setOrdinal(srcOpt.getSeq());
			dstOpt.setOptNm(srcOpt.getNmOpt());
			dstOpt.setOptDtlNm(srcOpt.getNmOptDtl());
			dstOpt.setOptPrice(srcOpt.getPrOpt());
			result.add(dstOpt);
		}

		return result;
	}

}
