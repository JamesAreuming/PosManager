package com.jc.pico.service.pos.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcClosingDetail;
import com.jc.pico.bean.SvcClosingDetailExample;
import com.jc.pico.bean.SvcClosingExample;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesExample;
import com.jc.pico.bean.SvcSalesItem;
import com.jc.pico.mapper.SvcClosingDetailMapper;
import com.jc.pico.mapper.SvcClosingMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.mapper.SvcSalesMapper;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.service.pos.PaymentGatewayService;
import com.jc.pico.service.pos.PosRewardService;
import com.jc.pico.service.pos.PosStoreUserService;
import com.jc.pico.service.pos.SalesSaveService;
import com.jc.pico.service.pos.SalesService;
import com.jc.pico.service.store.StoreNotificationService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.SvcClosingExtended;
import com.jc.pico.utils.bean.SvcSalesExtended;
import com.jc.pico.utils.customMapper.pos.PosSalesSaleSaveMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesStockMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesTableOrderMapper;

@Service
public class SalesServiceImpl implements SalesService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 매출 상태 : 정상
	 */
	public static final String SALES_ST_NORMAL = "809001";

	/**
	 * 매출 상태 : 반품
	 */
	public static final String SALES_ST_RETURN = "809002";

	/**
	 * 주문 경로 : 앱
	 */
	public static final String ORDER_PATH_APP = "606002";

	/**
	 * 주문 상태 : 완료
	 */
	public static final String ORDER_ST_COMPLETE = "607005";
	public static final String ORDER_TP_ORDER = "605001";

	@Autowired
	private SvcSalesMapper svcSalesMapper;

	@Autowired
	private SvcClosingMapper svcClosingMapper;

	@Autowired
	private SvcClosingDetailMapper svcClosingDetailMapper;

	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private PosSalesTableOrderMapper posSalesTableOrderMapper;

	@Autowired
	private PosRewardService posRewardService;

	@Autowired
	private AppNotificationService appNotificationService;

	@Autowired
	private StoreNotificationService storeNotificationService;

	@Autowired
	private PosSalesSaleSaveMapper posSalesSaleSaveMapper;

	@Autowired
	private PaymentGatewayService paymentGatewayService;

	@Autowired
	private SalesSaveService salesSaveService;

	@Autowired
	private PosSalesStockMapper posSalesStockMapper;

	@Autowired
	private PosStoreUserService posStoreUserService;


	/**
	 * saveSales
	 * 
	 */
	@Override
	public String saveSales(SvcSalesExtended sales) throws Throwable {

		// 포스에서 연속으로 매출 정보가 올라오는 경우가 있어 락을 걸고 처리 함 			
		if (!posSalesSaleSaveMapper.lockSaveSales(sales.getStoreId(), sales.getReceiptNo())) {
			throw new LockedException("Can not get sales lock. storeId=" + sales.getStoreId() + ", openDt=" + sales.getOpenDt() + ", posNo="
					+ sales.getPosNo() + ", receiptNo=" + sales.getReceiptNo());
		}

		try {

			// 이전 매출 정보 조회 (중복 발생해서는 안되지만 포스에서 중복해서 보내는 문제가 있어 추가함.)		
			SvcSales oldSales = getSales(sales.getStoreId(), sales.getOpenDt(), sales.getPosNo(), sales.getReceiptNo());

			// 트랜잭션 내에서 처리
			salesSaveService.saveSales(sales, oldSales);

			// 재고관리 처리
			salesStockExport(sales);

			// 매출 연계된 주문 조회
			final SvcOrder order = sales.getOrderId() != null ? svcOrderMapper.selectByPrimaryKey(sales.getOrderId()) : null;

			// 사용자 매출 통계 갱신
			posStoreUserService.resolveStoreUserSales(sales);

			// 매출에 따른 스탬프 및 쿠폰을 발급 / 취소 / 사용 처리		
			try {
				posRewardService.resolveRewardConsume(sales);
			} catch (Throwable tw) {
				logger.error("Failed resolve reward. But continue save sales.", tw);
			}

			// 취소 매출이면 앱에서 선결제한 PG 결제분을 취소 처리
			if (SALES_ST_RETURN.equals(sales.getSalesSt()) && order != null) {
				try {					
					paymentGatewayService.refund(order);
				} catch (Throwable tw) {
					logger.error("Failed cancel pg payment. But continue save sales.", tw);
				}
			}

			// 매출 알림 발송
			// 상태가 변경되는 경우만 발송
			if (oldSales == null || !Objects.equals(oldSales.getSalesSt(), sales.getSalesSt())) {
				notifySalesChanged(order, sales);
			} else {
				logger.error("Ignore notify sales changed to store manager. Sales status not changed sales. salesId=" + sales.getId());
			}

			return String.valueOf(sales.getId());

		} finally {

			// 반드시 락 해제
			posSalesSaleSaveMapper.unlockSaveSales(sales.getStoreId(), sales.getReceiptNo());
		}

	}

	/**
	 * 재고 관리 : 아이템 출고/반품
	 * 
	 * @param sales
	 * @throws Throwable
	 */
	private void salesStockExport(SvcSalesExtended sales) throws Throwable {
		// 1. Validation
		if (sales == null || sales.getStoreId() == null) {
			logger.error("[{}][{}] StoreID empty. storeId: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, sales.getStoreId());
			throw new Exception("StoreId empty. storeId: " + sales.getStoreId());
		}
		if (sales == null || sales.getSvcSalesItems() == null) {
			logger.error("[{}][{}] Sales or Items are empty. sales: {}, items: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, sales,
					sales.getSvcSalesItems());
			throw new Exception("Sales or Items are empty. sales: " + sales.getOrderNo() + ", items: " + sales.getSvcSalesItems());
		}

		Map<Object, Object> stockParam = new HashMap<>();

		// 2. 출고/반품 리스트 Param(브랜드,스토어 ) 셋팅
		stockParam.put("brand_id", sales.getBrandId());
		stockParam.put("store_id", sales.getStoreId());
		stockParam.put("sales_id", sales.getId());

		// 3. 아이템 별 출고/반품 Update.
		for (SvcSalesItem item : sales.getSvcSalesItems()) {
			stockParam.put("sales_item_id", item.getId());
			/****************** 서비스를 호출하기 위한 로직 시작 ******************/
			int stockSalesItemCnt = posSalesStockMapper.getStockSalesItemCnt(stockParam);
			/****************** 서비스를 호출하기 위한 로직 시작 ******************/
			//3.0. TB_SVC_STOCK_SALES_ITEM 에  같은 TB_SVC_SALES_ITEM : ID 로 등록 되어있는 건은 PASS(중복 INSERT 방지를 위함)
			if (stockSalesItemCnt < 1) {

				List<LinkedHashMap<String, Object>> stockImportList = null;

				// 3.1. 입고 리스트 Param(아이템ID) 셋팅.
				stockParam.put("item_id", item.getItemId()); //아이템ID

				// 3.2. 결제타입 (809001 : 정상, 809002 : 반품 )  
				if ("809001".equals(sales.getSalesSt())) {
					// 3.2.1. 현재 재고량이 1이상 인 것.
					int count = posSalesStockMapper.getStockImportListCount(stockParam);

					if (count > 0) {
						//3.2.2. 재고량이 1이상 인 입고리스트.(선입선출 이기 때문에 마지막 건 포함) 
						stockImportList = posSalesStockMapper.getStockImportList(stockParam);
					} else {
						//3.2.3. 재고량이 모두 0일경우 마지막 입고 단일 건.
						stockImportList = posSalesStockMapper.getStockImportLastList(stockParam);
					}

				} else if ("809002".equals(sales.getSalesSt())) {
					//3.2.1. 취소일 경우 재고가 모두 0일겨우 마지막 입고 단일 건.
					stockImportList = posSalesStockMapper.getStockImportLastList(stockParam);
				}

				if (stockImportList != null && stockImportList.size() > 0) {
					// 3.3. 계산 (현재 재고량 - 판매 아이템 갯수)

					BigInteger multiplyFlag = new BigInteger("-1");
					BigInteger itemCnt = new BigInteger(Short.toString(item.getCount()));
					BigInteger salesItemCnt = new BigInteger(Short.toString(item.getCount()));
					// 아이템 갯수 * -1
					itemCnt = itemCnt.multiply(multiplyFlag);

					for (int i = 0; i < stockImportList.size(); i++) {
						stockParam.put("id", stockImportList.get(i).get("ID"));
						BigInteger currentCnt = new BigInteger(String.valueOf(stockImportList.get(i).get("CURRENT_CNT")));

						//아이템 갯수 + 현재 재고량
						itemCnt = itemCnt.add(currentCnt);

						//3.4.1 마지막 리스트 일경우 +- 상관없이 값 입력.
						if (i == stockImportList.size() - 1) {
							stockParam.put("newCurrentCnt", itemCnt);
						} else {
							// 마지막 리스트가 아닐경우 0 또는 + 값 입력.
							if (itemCnt.signum() == -1) {
								stockParam.put("newCurrentCnt", "0");
							} else if (itemCnt.signum() == 0) {
								stockParam.put("newCurrentCnt", "0");
							} else if (itemCnt.signum() == 1) {
								stockParam.put("newCurrentCnt", itemCnt);
							}
						}

						// 3.4.2. 재고관리 현재 재고량 업데이트(TB_SVC_STOCK_IMPORT)
						stockParam.put("salesItemCnt", salesItemCnt);
						stockParam.put("currentCnt", currentCnt);
						/****************** 서비스를 호출하기 위한 로직 시작 ******************/
						int resultCnt = posSalesStockMapper.updateStockImport(stockParam);

						// 3.4.3 재고관리맵핑 등록(TB_SVC_STOCK_SALES_ITEM)
						if (resultCnt > 0) {
							posSalesStockMapper.insertStockSalesItem(stockParam);
						}
						/****************** 서비스를 호출하기 위한 로직 종료 ******************/

						// 3.4.4. 현재 재고량이 +,0일경우 BREAK, -일경우는  FOR문 계속
						if (itemCnt.signum() == 1) {
							break;
						} else if (itemCnt.signum() == 0) {
							break;
						}
					}
				}
			}

		}
	}

	/**
	 * 매출 조회
	 * storeId, openDt, posNo, receiptNo로 조회 한다.
	 * 
	 * @param sales
	 * @return
	 */
	@Override
	public SvcSales getSales(Long storeId, Date openDt, String posNo, String receiptNo) {
		SvcSalesExample svcSalesExample = new SvcSalesExample();

		// 기존 매출 정보에서 읽어온다. (바로 그 매장의 매출번호)
		// 포스에서 보내는 매출 데이터는 아래 값을 pk로 사용
		svcSalesExample.createCriteria() // 조건				
				.andStoreIdEqualTo(storeId) // 상점 id 
				.andOpenDtEqualTo(openDt) // 개점일
				.andPosNoEqualTo(posNo) // 포스 번호
				.andReceiptNoEqualTo(receiptNo); // 영수증 번호

		List<SvcSales> svcSalesz = svcSalesMapper.selectByExample(svcSalesExample);
		return svcSalesz.isEmpty() ? null : svcSalesz.get(0);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long saveClosing(SvcClosingExtended svcClosingExtended) throws Throwable {

		// 1. Validation
		if (svcClosingExtended == null || svcClosingExtended.getStoreId() == null) {
			logger.error("[{}][{}] StoreID empty. storeId: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, svcClosingExtended.getStoreId());
			throw new Exception("StoreId empty. storeId: " + svcClosingExtended.getStoreId());
		}

		// 2. TB_SVC_CLOSING 생성 (이미 있는 경우, 수정)
		SvcClosingExample svcClosingExample = new SvcClosingExample();
		Long storeId = svcClosingExtended.getStoreId();
		Date openDt = svcClosingExtended.getOpenDt();

		if (openDt == null) {
			logger.error("[{}][{}] OpenDt empty. storeId: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, svcClosingExtended.getStoreId());
			throw new Exception("OpenDt empty. storeId: " + svcClosingExtended.getStoreId());
		}

		// 기존 마감 정보에서 읽어온다. (바로 그 매장의 마감일자)
		SvcClosingExample.Criteria svcClosingExampleCriteria = svcClosingExample.createCriteria();
		svcClosingExampleCriteria.andStoreIdEqualTo(storeId);
		svcClosingExampleCriteria.andOpenDtEqualTo(openDt);

		List<SvcClosing> svcClosings = svcClosingMapper.selectByExample(svcClosingExample);
		if (svcClosings.size() == 0) {
			// 신규, 오픈 정보
			svcClosingMapper.insertSelective(svcClosingExtended);

		} else {
			// 갱신, 마감 혹은 재오픈
			SvcClosing svcClosing = svcClosings.get(0);
			svcClosingExtended.setId(svcClosing.getId());
			svcClosingMapper.updateByPrimaryKeySelective(svcClosingExtended);
		}

		// 3. 기존에 존재하는 하위 상세 정보를 삭제
		SvcClosingDetailExample svcClosingDetailExample = new SvcClosingDetailExample();
		SvcClosingDetailExample.Criteria svcClosingDetailExampleCriteria = svcClosingDetailExample.createCriteria();

		svcClosingDetailExampleCriteria.andClosingIdEqualTo(svcClosingExtended.getId());
		svcClosingDetailMapper.deleteByExample(svcClosingDetailExample); // 마감에 딸린 상세정보 삭제

		// 4. 마감 상세 리스트 저장
		List<SvcClosingDetail> svcClosingDetails = svcClosingExtended.getSvcClosingDetails();
		if (svcClosingDetails != null) {
			int svcClosingDetailsSize = svcClosingDetails.size();
			final long closingId = svcClosingExtended.getId();
			for (int inx = 0; inx < svcClosingDetailsSize; inx++) {
				SvcClosingDetail svcClosingDetail = svcClosingDetails.get(inx);
				svcClosingDetail.setClosingId(closingId);
				svcClosingDetailMapper.insertSelective(svcClosingDetail);
			}
		}

		// 개점 요청이면 테이블의 주문 정보를 리셋함.
		if (!svcClosingExtended.getIsClosing()) {
			posSalesTableOrderMapper.updateOrderInfoResetByStoreId(storeId);
		}

		return svcClosingExtended.getId();
	}

	/**
	 * 주문 / 매출 발생 알림
	 * 
	 * @param order
	 * @param sales
	 */
	private void notifySalesChanged(final SvcOrder order, SvcSalesExtended sales) {

		// 매출 발생/취소에 상태에 따라 알림		
		try {
			storeNotificationService.notifySalesChanged(sales);
		} catch (Throwable tw) {
			logger.error("Failed notify sales changed to store manager. But continue save sales.", tw);
		}

		// 앱에 완료 알림 
		try {
			System.out
					.println("SalesService notification : order.getOrderTp():" + order.getOrderTp() + " , order.getOrderSt():" + order.getOrderSt());
			if (!ORDER_TP_ORDER.equals(order.getOrderTp()) && !ORDER_ST_COMPLETE.equals(order.getOrderSt())) {
				appNotificationService.notifyOrderStatusChanged(order);
			}
		} catch (Throwable tw) {
			logger.error("Failed notify order status changed to user mobile.. But continue save sales.", tw);
		}
	}

}
