package com.jc.pico.service.pos.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemCat;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderDiscount;
import com.jc.pico.bean.SvcOrderItemHistory;
import com.jc.pico.bean.SvcOrderItemOpt;
import com.jc.pico.bean.SvcOrderPay;
import com.jc.pico.mapper.SvcItemCatMapper;
import com.jc.pico.mapper.SvcItemMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.service.pos.OrderService;
import com.jc.pico.service.pos.PosOrderService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosOrderPayment;
import com.jc.pico.utils.bean.PosSalesOrderInfo;
import com.jc.pico.utils.bean.PosSalesOrderInfoDetail;
import com.jc.pico.utils.bean.PosSalesOrderInfoDiscount;
import com.jc.pico.utils.bean.PosSalesOrderInfoHistory;
import com.jc.pico.utils.bean.PosSalesOrderInfoOption;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.bean.SvcOrderItemExtended;

@Service
public class PosOrderServiceImpl implements PosOrderService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 주문 상태 : 미승인
	 */
	private static final int ORDER_ST_UNAPPROVAL = 607001;

	/**
	 * 주문 상태 : 승인
	 */
	private static final int ORDER_ST_APPROVAL = 607002;

	/**
	 * 주문 경로 : clerk
	 */
	private static final int PATH_TP_CLERK = 606003;

	/**
	 * 주문 경로 : tab
	 */
	private static final int PATH_TP_TAB = 606004;

	@Autowired
	private SvcItemMapper svcItemMapper;

	@Autowired
	private SvcItemCatMapper svcItemCatMapper;

	@Autowired
	private OrderService orderService;

	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private PosUtil posUtil;

	@Override
	public SvcOrderExtended saveOrder(Map<String, Object> posBaseKeyMap, PosSalesOrderInfo posOrderInfo) throws Throwable {

		logger.debug("PosOrderService::saveOrder: {}", posOrderInfo);

		final TimeZone storeTimeZone = posUtil.getStoreTimeZone(Long.parseLong(posOrderInfo.getCdStore()));

		// 1. TB_SVC_ORDER 생성
		SvcOrderExtended svcOrderExtended = toSvcOrder(posOrderInfo, posBaseKeyMap, storeTimeZone);

		// 주문 상품 목록
		svcOrderExtended.setSvcOrderItems(toSvcOrderItems(posOrderInfo.getDetail(), storeTimeZone));

		// 결제 내역
		svcOrderExtended.setSvcOrderPays(toSvcOrderPays(posOrderInfo.getPayments(), storeTimeZone));

		// 테이블 락 상태를 확인하여 락이 걸려 있는데 본인이 건게 아니면 오류 처리
		// OrderService에서 처리할 수도 있으나 pos/clerk/tab간의 에러 코드 분리를 위해 여기서 확인 처리 함.
//		throwIfTableLockOwner(svcOrderExtended, (String) posBaseKeyMap.get(PosUtil.BASE_INFO_POS_NO));

		return orderService.saveOrder(svcOrderExtended);
	}

	private List<SvcOrderItemExtended> toSvcOrderItems(List<PosSalesOrderInfoDetail> posDetails, TimeZone storeTimeZone) {
		if (posDetails == null || posDetails.size() == 0) {
			return Collections.emptyList();
		}
		// S: 아이템별 등록
		// DETAIL 로부터 읽어옴
		List<SvcOrderItemExtended> result = new ArrayList<SvcOrderItemExtended>();

		for (PosSalesOrderInfoDetail posDetail : posDetails) {

			// 주문 상품 정보
			SvcOrderItemExtended svcOrderItem = toSvcOrderItem(posDetail, storeTimeZone);

			// 주문별 할인 정보 세팅
			svcOrderItem.setSvcOrderDiscounts(toSvcOrderDiscounts(posDetail.getDiscount()));

			// 주문별 주문변동 이력
			svcOrderItem.setSvcOrderHistories(toSvcOrderItemHistory(posDetail.getHistory()));

			// 주문 옵션 설정
			svcOrderItem.setSvcOrderItemOpts(toSvcOrderItemOpts(posDetail.getOptions()));

			// 주문별 상품 리스트에 상품 추가
			result.add(svcOrderItem);

		} // for posSalesOrderInfoDetails

		// E: 아이템별 등록

		return result;
	}

	/**
	 * PosSalesOrderInfoDetail -> SvcOrderItemExtended 변환
	 * 
	 * @param posDetail
	 * @return
	 */
	private SvcOrderItemExtended toSvcOrderItem(PosSalesOrderInfoDetail posDetail, TimeZone storeTimeZone) {

		final Date orderTmLocal = posUtil.parseDateTime(posDetail.getDtOrder(), null);
		final Date orderTm = posUtil.convertLocaltoUtc(orderTmLocal, storeTimeZone);

		final SvcOrderItemExtended result = new SvcOrderItemExtended();

		result.setOrdinal(posDetail.getSeq()); // 일련번호
		result.setLastSt(posUtil.getBaseCode("951", posDetail.getCdOrderStatus())); // 주문상태코드(1.변동없음, 2.신규, 3.수정(수량변경), 4.삭제)
		result.setPathTp(posUtil.toString(posDetail.getCdOrderPath())); // main pos: 606001, app: 606002,  clerk: 606003, tab: 606004, Order POS: 606005
		result.setSalesDiv(posDetail.getDsSale().byteValue()); // 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
		result.setItemTp(posUtil.getBaseCode("818", posDetail.getCdSaleType())); // 0: 일반, 7: 옵션
//		posDetail.getYnSetSub(); // 세트 구성품 여부
//		posDetail.getNoGroup(); // 세트 코스그룹번호
//		posDetail.getQtyItem(); // 세트 단위 수량
//		posDetail.getCdStoreMng(); // 통계 사업 단위
		result.setItemId(Long.parseLong(posDetail.getCdGoods())); // 품번
		result.setItemNm(posDetail.getNmGoods()); // 품명		
		result.setItemTp(posUtil.getBaseCode("818", posDetail.getCdSaleType()));
		result.setTaxTp(posUtil.getBaseCode("819", posDetail.getDsTax())); // 매출과세구분 		
		result.setPurchasePrice(posDetail.getPrBuy()); // 매출단가
		result.setPrice(posDetail.getPrSale()); // 판매 단가
		result.setCount(posDetail.getQtyOrder().shortValue()); // 주문 수량		
		result.setSales(posDetail.getAmtSupply() + posDetail.getAmtTax()); // 주문 금액
		result.setOptPrice(posDetail.getPrOptions()); // 옵션 단가 합계
		result.setNetSales(posDetail.getAmtSupply()); // 공급가		
		result.setTax(posDetail.getAmtTax()); // 부가세
		result.setDiscount(posDetail.getAmtDc()); // 할인금액
		result.setMemo(posDetail.getMemo()); // 메모
//		posDetail.getCdNoSaleReason(); // 비매출 사유
		result.setOrderTm(orderTm); // 주문 일시
		result.setOrderTmLocal(orderTmLocal); // 주문 일시
		result.setIsPacking(posDetail.getYnPacking() == 1); // 포장여부 1:포장, 0:일반
		posDetail.getNoGiftCard(); // 기프트 카드 번호
		result.setStaffId(posUtil.parseLong(posDetail.getCdEmployeeUpdate(), null));

		// FIXME 아래 항목들은 값을 받아오는 것이 없거나(전문에 누락?) 모호한 것들이므로 반드시 채워넣어야 함...

		result.setServiceCharge(0d);
		result.setOptId(""); // 옵션 ID(여러개일 경우 ":"로 분리)
		result.setOptCd(""); // 옵션 코드(여러개일 경우 ":"로 분리)
		result.setOptNm(""); // 옵션 이름(여러개일 경우 ":"로 분리)

		// 아이템의 카테고리를 DB에서 읽어옴 (아이템을 거쳐 카테고리를 읽어옴)
		// FIXME Green, 서버 부하를 줄이기 위해 로컬 디바이스에서 읽어오는 것이 더 나을지 검토요망

		// itemCd. catId, catCd 설정
		SvcItem svcItem = svcItemMapper.selectByPrimaryKey(result.getItemId());
		if (svcItem != null) {

			result.setIsStamp(svcItem.getIsStamp());

			result.setItemCd(svcItem.getItemCd());

			SvcItemCat svcItemCat = svcItemCatMapper.selectByPrimaryKey(svcItem.getCatId());
			if (svcItemCat != null) {
				result.setCatCd(svcItemCat.getCatCd());
				result.setCatNm(svcItemCat.getName());
			}
		}

		return result;
	}

	/**
	 * DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
	 * PosSalesOrderInfoDiscount -> SvcOrderDiscount 변환
	 * 
	 * @param posDiscounts
	 * @return
	 */
	private List<SvcOrderDiscount> toSvcOrderDiscounts(List<PosSalesOrderInfoDiscount> posDiscounts) {

		if (posDiscounts == null || posDiscounts.size() == 0) {
			return Collections.emptyList();
		}

		List<SvcOrderDiscount> result = new ArrayList<SvcOrderDiscount>();

		for (PosSalesOrderInfoDiscount posDiscount : posDiscounts) {
			// 주문에 상품별 할인을 넣기 위한 리스트(상품코드가 없는 경우는 주문 자체에 대한 할인임을 인지할 것)
			SvcOrderDiscount svcDiscount = new SvcOrderDiscount();
			svcDiscount.setDiscountTp(posUtil.getBaseCode("824", posDiscount.getCdDcType())); // 할인 형태
			svcDiscount.setOrdinal(posDiscount.getNoDc());
			svcDiscount.setIsCanceled(posDiscount.getDsDiscount() == 1); // 0: 정상: 1:취소
			svcDiscount.setCouponCd(posDiscount.getNoCoupon());
//			posDiscount.getAmtSet();
			svcDiscount.setAmount(posDiscount.getAmtDc());
			svcDiscount.setMemo(posDiscount.getRemark());
			svcDiscount.setStaffId(posUtil.parseLong(posDiscount.getCdEmployeeUpdate(), null));

			result.add(svcDiscount);
		}

		return result;
	}

	/**
	 * PosSalesOrderInfoHistory -> SvcOrderItemHistory 으로 변환
	 * 
	 * @param posHistories
	 * @return
	 */
	private List<SvcOrderItemHistory> toSvcOrderItemHistory(List<PosSalesOrderInfoHistory> posHistories) {

		// 주문에 상품별 할인을 넣기 위한 리스트(상품코드가 없는 경우는 주문 자체에 대한 할인임을 인지할 것)
		if (posHistories == null || posHistories.size() == 0) {
			return Collections.emptyList();
		}

		final List<SvcOrderItemHistory> result = new ArrayList<SvcOrderItemHistory>();

		for (PosSalesOrderInfoHistory srcHistory : posHistories) {

			final SvcOrderItemHistory dstHistory = new SvcOrderItemHistory();
			dstHistory.setOrdinal(srcHistory.getSeq1());
			dstHistory.setOrderCnt(srcHistory.getQtyOrder().shortValue());
			dstHistory.setMemo(srcHistory.getMemo());
			dstHistory.setReason(posUtil.getBaseCode("827", srcHistory.getCdCancelReason()));
			dstHistory.setOrderTm(posUtil.parseDateTime(srcHistory.getDtOrder(), null));
			dstHistory.setStaffId(posUtil.parseLong(srcHistory.getCdEmployee(), 1l));

			result.add(dstHistory);
		}

		return result;
	}

	/**
	 * PosSalesOrderInfo(포스) 를 SvcOrderExtended으로 변환한다
	 * 
	 * @param posSalesOrderSave
	 * @param posInfo
	 * @return
	 */
	private SvcOrderExtended toSvcOrder(PosSalesOrderInfo posSalesOrderSave, Map<String, Object> posInfo, TimeZone localTimeZone) {

		final Date openDt = posUtil.getDate(posSalesOrderSave.getYmdOrder(), PosUtil.DAY_FORMAT, null); // 매장 오픈일자(시간은 무의미 함)
		final Date orderTmLocal = posUtil.parseDateTime(posSalesOrderSave.getDtOrder(), null);
		final Date orderTm = posUtil.convertLocaltoUtc(orderTmLocal, localTimeZone);
		final Date admissionTm = posUtil.parseDateTime(posSalesOrderSave.getDtAdmission(), null); // 입장시간		
		final Date exitTm = posUtil.parseDateTime(posSalesOrderSave.getDtExit(), null); // 퇴장시간
		final Date reserveTmLocal = posUtil.parseYmdHHmm(posSalesOrderSave.getYmdBooking(), posSalesOrderSave.getResTimeBooking(), null); // 로컬 예약시간
		final Date reserveTm = posUtil.convertLocaltoUtc(reserveTmLocal, localTimeZone); // UTC 예약 시간
		final Date reserveRegTmLocal = posUtil.parseDateTime(posSalesOrderSave.getResDtInsert(), null); // 로컬예약등록시간
		final Date reserveRegTm = posUtil.convertLocaltoUtc(reserveRegTmLocal, localTimeZone); // 예약등록시간(UTC)		
		final Date acceptTmLocal = posUtil.parseDateTime(posSalesOrderSave.getDtApproval(), null);
		final Date acceptTm = posUtil.convertLocaltoUtc(acceptTmLocal, localTimeZone);

		// apc는 주문과 예약 고객수를 달리 관리하지 않으나 POS는 달리 관리함, cntCutstomer 우선 사용, 차선으로 res cnt customer 사용		
		final Integer customerCnt = posUtil.nvl(posSalesOrderSave.getCntCustomer(), 0) != 0 ? posUtil.nvl(posSalesOrderSave.getCntCustomer(), 0)
				: posUtil.nvl(posSalesOrderSave.getResCntCustomer(), 0);

		final SvcOrderExtended result = new SvcOrderExtended();

		result.setPosNo((String) posInfo.get(PosUtil.BASE_INFO_POS_NO));
		result.setBrandId((Long) posInfo.get(PosUtil.BASE_INFO_BRAND_ID));
		result.setStoreId(Long.parseLong(posSalesOrderSave.getCdStore()));
		result.setOpenDt(openDt);
		result.setOrderNo(Long.toString(posSalesOrderSave.getNoOrder()));
		result.setOrderTp(posSalesOrderSave.getCdOrderType().toString()); // base code 그대로 사용.
		result.setPathTp(posSalesOrderSave.getCdOrderPath().toString()); // base code 그대로 사용.
		result.setLastSt(posUtil.getBaseCode("951", posSalesOrderSave.getCdOrderStatus()));
		result.setOrderSt(posSalesOrderSave.getCdOrderVerify().toString()); // 주문처리코드(607001:미승인, 607002:승인, 607003:취소, 607004:거부, 607005:완료)
		result.setTableNo(posSalesOrderSave.getNoTable().toString()); // 테이블번호
		result.setOrderDiv(posSalesOrderSave.getDsOrder().byteValue()); // 주문형태 (1:테이블, 2:배달, 3:포장)		
		result.setAdmissionTm(admissionTm);
		result.setExitTm(exitTm);
		result.setCustomerTp(posUtil.getBaseCode("855", posSalesOrderSave.getCdMemberType())); // 고객형태코드
		result.setCustomeAgeTp(posUtil.getBaseCode("846", posSalesOrderSave.getCdMemberAge())); // 고객연령대코드
		result.setCustomeGender(posUtil.getBaseCode("101", posSalesOrderSave.getCdMemberSex())); // 고객성별코드
		result.setCustomerCnt(customerCnt.shortValue());
//		posSalesOrderSave.getCdTour(); // 여행사코드, 연동 컬럼 없음
//		posSalesOrderSave.getNoGuide(); // 가이드번호, 연동 컬럼 없음
//		posSalesOrderSave.getYnForeigner(); // 외국인유무, 연동 컬럼 없음
		result.setUserId(posUtil.parseLong(posSalesOrderSave.getCdMember(), null));
		result.setOrdererName(posSalesOrderSave.getNmMember());
		result.setOrdererMb(posSalesOrderSave.getTelMobileMember());
//		posSalesOrderSave.getCdMemberDelivery(); // 배달고객번호
		result.setStaffId(posUtil.parseLong(posSalesOrderSave.getCdEmployee(), null));
		result.setSales(posSalesOrderSave.getAmtSupply() + posSalesOrderSave.getAmtTax());
		result.setSupplyValue(posSalesOrderSave.getAmtSupply()); // 공급가
		result.setTax(posSalesOrderSave.getAmtTax()); // 부가세
		result.setDiscount(posSalesOrderSave.getAmtDc()); // 할인 금액
		result.setServiceCharge(posSalesOrderSave.getAmtTip()); // 봉사료
		result.setMemo(posSalesOrderSave.getMemo()); // 메모
//		posSalesOrderSave.getDsStatus(); // 주문상태(1:주문 2:전체취소 3:합석), 연동 컬럼 없음
		result.setIsSales(posSalesOrderSave.getYnSale() == 1); // 성잔 완료 여부
//		posSalesOrderSave.getCdVendor(); // 거래처 코드, 연동 컬럼 없음		
		result.setReserveTm(reserveTm); // 예약시간
		result.setReserveTmLocal(reserveTmLocal); // 예약시간 YMD_BOOKING + RES_TIME_BOOKING
		result.setReserveNo(posSalesOrderSave.getNoBooking());
		result.setReserveRegTm(reserveRegTm);
		result.setReserveRegTmLocal(reserveRegTmLocal);
//		posSalesOrderSave.getNoOrderPrint(); // 주문번호출력, 연동 컬럼 없음
		result.setOrderTm(orderTm);
		result.setOrderTmLocal(orderTmLocal);
		result.setEstTm(posSalesOrderSave.getTimeWait()); // 예상 대기 시간 hhmm
		result.setCancelTp(posUtil.getBaseCode("827", posSalesOrderSave.getCdCancelReason())); // 취소 사유
		result.setAcceptTm(acceptTm); // 승인시간
		result.setAcceptTmLocal(acceptTmLocal); // 승인시간
		result.setIsConfirm(true); // 포스에서 들어온 주문이니 싱크됨 상태로 저장

		// 이하 포스측 정보가 없어 저장 하지 않는 값
//		svcOrderExtended.setPerson(); // 판매자(직원) 이름		
//		svcOrderExtended.setReceiptId(receiptId); // 영수증 아이디
//		svcOrderExtended.setReceiptNo(receiptNo); // 영수증 번호
//		svcOrderExtended.setUseCoupon(useCoupon); // 쿠폰 사용 여부
//		svcOrderExtended.setPayMethod(payMethod); // 결제 수단
//		svcOrderExtended.setReason(); // 취소 반려 사유

		return result;
	}

	/**
	 * PosSalesOrderInfoOption 을 SvcOrderItemOpt 으로 변환
	 * 
	 * @param srcOptions
	 * @return
	 */
	private List<SvcOrderItemOpt> toSvcOrderItemOpts(List<PosSalesOrderInfoOption> srcOptions) {
		if (srcOptions == null) {
			return Collections.emptyList();
		}
		List<SvcOrderItemOpt> result = new ArrayList<>(srcOptions.size());

		for (PosSalesOrderInfoOption srcOpt : srcOptions) {
			SvcOrderItemOpt dstOpt = new SvcOrderItemOpt();
			dstOpt.setOptId(srcOpt.getNoOpt());
			dstOpt.setOptDtlId(srcOpt.getNoOptDtl());
			dstOpt.setOrdinal(srcOpt.getSeq().longValue());
			dstOpt.setOptNm(srcOpt.getNmOpt());
			dstOpt.setOptDtlNm(srcOpt.getNmOptDtl());
			dstOpt.setOptPrice(srcOpt.getPrOpt());
			result.add(dstOpt);
		}

		return result;
	}

	/**
	 * PosOrderPayment 를 SvcOrderPay으로 변환한다
	 * 
	 * @param posSalesOrderSave
	 * @return
	 */
	private List<SvcOrderPay> toSvcOrderPays(List<PosOrderPayment> srcPayments, TimeZone localTimeZone) {
		if (srcPayments == null || srcPayments.size() == 0) {
			return Collections.emptyList();
		}
		final List<SvcOrderPay> result = new ArrayList<>();

		for (PosOrderPayment srcPay : srcPayments) {
			SvcOrderPay dstPay = new SvcOrderPay();
			dstPay.setAmount(srcPay.getPrAmount());
			dstPay.setCardNo(srcPay.getNoCard());
			dstPay.setMonthlyPlain(srcPay.getNoMonthlyPlain().byteValue());
			dstPay.setOrdinal(srcPay.getSeq());
			dstPay.setPayMethod(posUtil.getBaseCode("810", srcPay.getCdPayMethod()));
			dstPay.setPayTmLocal(posUtil.parseDateTime(srcPay.getDtPayment(), null));
			dstPay.setPayTm(posUtil.convertLocaltoUtc(dstPay.getPayTmLocal(), localTimeZone));
			dstPay.setStaffId(posUtil.parseLong(srcPay.getCdEmployee(), null));
			dstPay.setTranNo(srcPay.getNoTran());
			dstPay.setPgKind(posUtil.toBaseCode(srcPay.getCdPgKind()));

			// 승인 번호가 존재하면 앱에서 선결제 처리된 것
			// 승인 번호가 없으면 포스에서 추가한 결제 정로 결제 처리 대상이 아닌 상태로 저장 처리
			// 포스에 전달되는 결제 정보는 결제 대상 아님(415001), 결제 완료 (415003) 2가지 이며 결제 완료일때 transNo가 설정됨
			if (StringUtils.isEmpty(srcPay.getNoTran())) {
				dstPay.setPaySt("415001"); // 결제 처리 대상 아님
			}

			result.add(dstPay);
		}
		return result;
	}

	@Override
	public void confirmOrder(PosSalesOrderInfo orderInfo) {
		if (orderInfo.getOrderId() == null || orderInfo.getOrderId() == 0) {
			return;
		}

		SvcOrder record = new SvcOrder();
		record.setId(orderInfo.getOrderId());

		// clerk/tab 주문이면 미승인을 승인 상태로 변경
		if (Objects.equals(orderInfo.getCdOrderVerify(), ORDER_ST_UNAPPROVAL)
				&& (Objects.equals(orderInfo.getCdOrderPath(), PATH_TP_CLERK) || Objects.equals(orderInfo.getCdOrderPath(), PATH_TP_TAB))) {
			record.setOrderSt(String.valueOf(ORDER_ST_APPROVAL));
		}

		// 조회해가면 is_confirm 을 true로 변경		
		record.setIsConfirm(true);

		// 서버 정보 갱신 
		int count = svcOrderMapper.updateByPrimaryKeySelective(record);

		// 포스로 가는 주문 정보에 반영
		if (count > 0 && record.getOrderSt() != null) {
			orderInfo.setCdOrderVerify(ORDER_ST_APPROVAL);
		}
	}

}
