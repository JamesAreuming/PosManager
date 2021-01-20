package com.jc.pico.service.clerk.impl;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
//import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcDelivery;
import com.jc.pico.bean.SvcKitchenPrinter;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderDiscount;
import com.jc.pico.bean.SvcOrderDiscountExample;
import com.jc.pico.bean.SvcOrderExample;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcOrderItemExample;
import com.jc.pico.bean.SvcOrderItemHistory;
import com.jc.pico.bean.SvcOrderItemHistoryExample;
import com.jc.pico.bean.SvcOrderItemOpt;
import com.jc.pico.bean.SvcOrderItemOptExample;
import com.jc.pico.bean.SvcOrderPay;
import com.jc.pico.bean.SvcOrderPayExample;
import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesList;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcStoreInfo;
import com.jc.pico.bean.SvcStoreInfoPrinter;
import com.jc.pico.bean.SvcStorePrinter;
import com.jc.pico.bean.SvcTable;
import com.jc.pico.bean.SvcTableExample;
import com.jc.pico.bean.User;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcOrderDiscountMapper;
import com.jc.pico.mapper.SvcOrderItemHistoryMapper;
import com.jc.pico.mapper.SvcOrderItemMapper;
import com.jc.pico.mapper.SvcOrderItemOptMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.mapper.SvcOrderPayMapper;
import com.jc.pico.mapper.SvcSalesItemMapper;
import com.jc.pico.mapper.SvcSalesMapper;
import com.jc.pico.mapper.SvcStoreInfoMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcStorePrinterMapper;
import com.jc.pico.mapper.SvcTableMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.clerk.ClerkCommonService;
import com.jc.pico.service.clerk.ClerkOrderService;
import com.jc.pico.service.pos.OrderService;
import com.jc.pico.utils.AuthenticationUtils;
import com.jc.pico.utils.ClerkUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.MQTTClient;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.ClerkResult.ErrorCode;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StaffUserDetail;
import com.jc.pico.utils.bean.StoreParam;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.bean.SvcOrderItemExtended;
import com.jc.pico.utils.bean.SvcTableExtended;
import com.jc.pico.utils.customMapper.clerk.ClerkOrderMapper;

@Service
public class ClerkOrderServiceImpl implements ClerkOrderService {

	private static Logger logger = LoggerFactory.getLogger(ClerkOrderServiceImpl.class);

	/**
	 * 주문 미승인 (최초 주문 등록)
	 */
	public static final String ORDER_ST_UNAPPROVED = "607001";

	/**
	 * 주문 승인 (주문 확인후 주방에 전달 후 승인)
	 */
	public static final String ORDER_ST_APPROVED = "607002";

	/**
	 * 주문 취소 (승인 -> 취소)
	 */
	public static final String ORDER_ST_CANCEL = "607003";

	/**
	 * 주문 거부 (미승인 -> 거부, 재고 없는 등의 사유)
	 */
	public static final String ORDER_ST_REJECT = "607004";

	/**
	 * 주문 완료 (상품이 고객에게 배달 됨)
	 */
	public static final String ORDER_ST_COMPLETE = "607005";

	/**
	 * 주문
	 */
	public static final String ORDER_TP_ORDER = "605001";

	/**
	 * 예약
	 */
	public static final String ORDER_TP_RESERVATION = "605002";

	/**
	 * 파티 계약
	 */
	public static final String ORDER_TP_CONTRACT = "605003";

	/**
	 * 주문 최종 상태 : 신규
	 */
	public static final String LAST_ST_NEW = "951002";

	/**
	 * 주문 최종 상태 : 수정 (수량변경)
	 */
	public static final String LAST_ST_UPDATE = "951003";

	/**
	 * 주문 최종 상태 : 삭제
	 */
	public static final String LAST_ST_DELETE = "951004";

	/**
	 * 주문 경로 : clerk
	 */
	public static final String PATH_TP_CLERK = "606003";

	/**
	 * 기본 날짜 시간 포맷
	 */
	public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 테이블 락 요청 : 처리 하지 않음
	 */
	public static final String TABLE_LOCK_NONE = "none";

	/**
	 * 테이블 락 요청 : 락 설정
	 */
	public static final String TABLE_LOCK_SET = "lock";

	/**
	 * 테이블 락 요청 : 락 해제
	 */
	public static final String TABLE_LOCK_RELEASE = "release";

	@Autowired
	private SvcTableMapper svcTableMapper;

	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private SvcOrderItemMapper svcOrderItemMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private OrderService orderService;

	@Autowired
	private SvcOrderItemOptMapper svcOrderItemOptMapper;

	@Autowired
	private SvcOrderItemHistoryMapper svcOrderItemHistoryMapper;

	@Autowired
	private SvcOrderDiscountMapper svcOrderDiscountMapper;

	@Autowired
	private SvcOrderPayMapper svcOrderPayMapper;

	@Autowired
	private ClerkOrderMapper clerkOrderMapper;

	@Autowired
	private ClerkCommonService clerkCommonService;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private PosUtil posUtil;

	// 추가 : 
	@Autowired
	private SvcSalesItemMapper svcSalesItemMapper;

	@Autowired
	private SvcStorePrinterMapper svcMerchantMapper;
	
	@Autowired
	private SvcStoreInfoMapper svcStoreInfoMapper;
	// 추가 : \
	
    @Autowired
	private SvcSalesMapper svcSalesMapper;
    
	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = JsonConvert.getObjectMapper();
	}

	@Override
	public SvcOrderExtended getOrderDetailOrThrow(SingleMap param) throws RequestResolveException {
		return getOrderDetailOrThrowByOrderId(param.getLong("orderId"));
	}

	@Override
	public SvcOrderExtended getOrderDetailOrThrowByOrderId(long orderId) throws RequestResolveException {

		// 주문 정보 조회
		SvcOrderExtended order = getOrderDetailByOrderId(orderId);
		if (order == null) {
			throw new RequestResolveException(ErrorCode.ORDER_NOT_FOUND.code, orderId + " order id is not found.");
		}
		return order;
	}

	
	@Override
	@Nullable
	public SvcOrderExtended getOrderDetail(SingleMap param) {
		return getOrderDetailByOrderId(param.getLong("orderId"));
	}


	@Override
	@Nullable
	public SvcOrderExtended getOrderDetailByOrderId(long orderId) {

		// 주문 정보 조회
		SvcOrderExtended order = getSvcOrderExtendedByOrderId(orderId);
		if (order == null) {
			return null;
		}

		// 주문 항목 조회
		order.setSvcOrderItems(getSvcOrderItemExtendedListByOrderId(order.getId()));

		// 주문 상세 정보
		for (SvcOrderItemExtended item : order.getSvcOrderItems()) {
			// 주문 옵션
			item.setSvcOrderItemOpts(getSvcOrderItemOptionListByOrderItemId(item.getId()));

			// 주문 옵션 히스토리
			item.setSvcOrderHistories(getSvcOrderItemHistoryListByOrderItemId(item.getId()));

			// 주문 할인
			item.setSvcOrderDiscounts(getSvcOrderDiscountListByOrderItemId(item.getId()));
		}

		return order;
	}

	
	/**
	 * getOrderDetailEx
	 * 추가 : 
	 * @param param
	 * @return
	 */
	@Override
	@Nullable
	public SvcOrderExtended getOrderDetailEx(SingleMap param) {
		return getOrderDetailByOrderIdEx(param.getLong("storeId"), param.getLong("brandId"), param.getString("orderId"));
	}

	/**
	 * getOrderDetailByOrderIdEx
	 * 추가 : 
	 * 
	 */
	@Override
	@Nullable
	public SvcOrderExtended getOrderDetailByOrderIdEx(long storeId, long brandId, String orderId) {

		// 주문 정보 조회
		SvcOrderExtended order = getSvcOrderExtendedByOrderIdEx(storeId, brandId, orderId);
		if (order == null) {
			return null;
		}

		// 결제 정보
		List<SvcOrderPay> svcOrderPayList = new ArrayList<>();
		svcOrderPayList.add( svcOrderPayMapper.selectByOrderId( order.getId()) );
		order.setSvcOrderPays(svcOrderPayList);

		/*
		// 주문 항목 조회
		order.setSvcOrderItems(getSvcOrderItemExtendedListByOrderId(order.getId()));

		// 주문 상세 정보
		for (SvcOrderItemExtended item : order.getSvcOrderItems()) {
			// 주문 옵션
			item.setSvcOrderItemOpts(getSvcOrderItemOptionListByOrderItemId(item.getId()));

			// 주문 옵션 히스토리
			item.setSvcOrderHistories(getSvcOrderItemHistoryListByOrderItemId(item.getId()));

			// 주문 할인
			item.setSvcOrderDiscounts(getSvcOrderDiscountListByOrderItemId(item.getId()));
		}
		*/

		return order;
	}

	
	
	/**
	 * 키오스크 일별 매출 검색
	 * 추가 : 
	 * @param param
	 * @return
	 */	
	@Override
	@Nullable
	//public List<SvcSalesItemEx> getOrderDetailKioskEx(SingleMap param) {
	//public List<SvcSalesItem> getOrderDetailKioskEx(SingleMap param) {	
	public SvcSalesList getOrderDetailKioskEx(SingleMap param) {		
		Long   STORE_ID  = param.getLong("storeId");
		Long   BRAND_ID = param.getLong("brandId");
		String startString  = param.getString("startDate");
		String endString   = param.getString("endDate");
		
		// 매출 리스트 
		SvcSalesList svcSalesList = new SvcSalesList();
		
		HashMap<String, Object> searchItem = new HashMap();
		searchItem.put("storeId",    STORE_ID);
		searchItem.put("brandId",   BRAND_ID);
		searchItem.put("startDate", startString);
		searchItem.put("endDate",  endString);
		
		logger.debug("detailKiosk > storeId : " + STORE_ID);
		logger.debug("detailKiosk > brandId : " + BRAND_ID);
		logger.debug("detailKiosk > startDate : " + startString);
		logger.debug("detailKiosk > endDate : " + endString);
		
		//List<SvcSalesItemEx> salesItemList = svcSalesItemMapper.selectBySalesItemList(searchItem);
		//List<SvcSalesItem> salesItemList = svcSalesItemMapper.selectBySalesItemList(searchItem);
		
		svcSalesList.setSvcSalesList(svcSalesItemMapper.selectBySalesItemList(searchItem));
		
		
		
		return svcSalesList;
	}
	
	@Override
	public SvcSalesList getSalesDetailKiosk(SingleMap param) {
		Long   STORE_ID  = param.getLong("storeId");
		Long   BRAND_ID = param.getLong("brandId");
		String startString  = param.getString("startDate");
		String endString   = param.getString("endDate");
		
		//일별 리스트
		SvcSalesList svcSalesList = new SvcSalesList();
		
		HashMap<String, Object> searchItem = new HashMap();
		searchItem.put("storeId",    STORE_ID);
		searchItem.put("brandId",   BRAND_ID);
		searchItem.put("startDate", startString);
		searchItem.put("endDate",  endString);

		
		svcSalesList.setSvcSalesList(svcSalesItemMapper.selectBySalesDetailKioskTest(searchItem)); //영수증 리스트
		
//		svcSalesList.getSvcSalesList().get(0).getSvcSales().getReceiptNo();
//		svcSalesList.getSvcSalesList().size();
//		

/* 보류		for(int i=0;i<svcSalesList.getSvcSalesList().size();i++) {
			String receiptString = svcSalesList.getSvcSalesList().get(i).getSvcSales().getReceiptNo();
			System.out.println("영수증번호 >> "+receiptString);
			
			HashMap<String, Object> searchTest = new HashMap();
			searchTest.put("storeId",    STORE_ID);
			searchTest.put("brandId",   BRAND_ID);			
			searchTest.put("receiptNo", receiptString);
			
			System.out.println("제발 >>"+searchTest.toString());
			SvcSales test = new SvcSales();
				     test = svcSalesMapper.selectBySalesDetailKioskTest2(searchTest);
				     			
			
		}	
		//svcSalesList.setSvcSalesList(svcSalesItemMapper.selectBySalesDetailKioskTest2(param));
*/				
		return svcSalesList;
	}	

	
	/**
	 * 키오스크 일별 매출 검색
	 * 추가 : 
	 * @param param
	 * @return
	 */	
	@Override
	@Nullable
	public SvcOrderExtended getOrderDetailKiosk(SingleMap param) {
		logger.debug("detailKiosk > getOrder : " + 2);
		return getOrderDetailByOrderKiosk(param.getLong("orderId"));
	}

	
	@Override
	@Nullable
	/**
	 * 키오스크 일별 매출 검색
	 * 추가 : 
	 * @param orderId
	 * @return
	 */
	public SvcOrderExtended getOrderDetailByOrderKiosk(long orderId) {

		logger.debug("detailKiosk > getOrder : " + 3);

		// 주문 정보 조회
		//SvcOrderExtended order = getSvcOrderExtendedByOrderId(orderId);
		SvcOrderExtended order = getSvcOrderExtendedByOrderKiosk(orderId);
		if (order == null) {
			logger.debug("detailKiosk > getOrder : " + 4);
			return null;
		}

		logger.debug("detailKiosk > getOrder : " + 5);
	
		
		// 주문 항목 조회
		order.setSvcOrderItems(getSvcOrderItemExtendedListByOrderId(order.getId()));

		// 주문 상세 정보
		for (SvcOrderItemExtended item : order.getSvcOrderItems()) {
			// 주문 옵션
			item.setSvcOrderItemOpts(getSvcOrderItemOptionListByOrderItemId(item.getId()));

			// 주문 옵션 히스토리
			item.setSvcOrderHistories(getSvcOrderItemHistoryListByOrderItemId(item.getId()));

			// 주문 할인
			item.setSvcOrderDiscounts(getSvcOrderDiscountListByOrderItemId(item.getId()));
		}

		logger.debug("detailKiosk > getOrder : " + 6);
		logger.debug("detailKiosk > getOrder > getSvcOrderItems : " + order.getSvcOrderItems().size());
		logger.debug("detailKiosk > getOrder > getSvcOrderPays  : " + order.getSvcOrderPays().size());
		
		return order;
	}

	
	
	/**
	 * 테이블에서 주문 처리중인 고객수 변경
	 * 
	 * @throws RequestResolveException
	 */
	@Override
	public SingleMap setTableCustomerCount(SingleMap param) throws RequestResolveException {

		long tableId = param.getLong("tableId");
		short customerCount = param.getShort("customerCnt");

		SvcTable table = svcTableMapper.selectByPrimaryKey(tableId);
		if (table == null) {
			throw new RequestResolveException(ErrorCode.TABLE_NOT_FOUND.code, tableId + " id table is not found.");
		}
		if (table.getOrderId() == null) {
			throw new RequestResolveException(ErrorCode.ORDER_NOT_FOUND.code, tableId + " id table has not order.");
		}

		SvcOrder record = new SvcOrder();
		record.setId(table.getOrderId());
		record.setCustomerCnt(customerCount);
		record.setIsConfirm(false);

		svcOrderMapper.updateByPrimaryKeySelective(record);

		SingleMap tableDetail = objectMapper.convertValue(table, SingleMap.class);
		SvcOrderExtended order = getOrderDetailOrThrowByOrderId(table.getOrderId());
		tableDetail.put("order", order);

		SvcStore store = svcStoreMapper.selectByPrimaryKey(order.getStoreId());
		sendMqtt(store, order);

		return tableDetail;
	}

	@Override
	public List<SingleMap> moveTableOrder(SingleMap param) throws RequestResolveException {

		final TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			long fromTableId = param.getLong("fromTableId");
			long toTableId = param.getLong("toTableId");
			String posNo = param.getString("posNo");

			SvcTable fromTable = svcTableMapper.selectByPrimaryKey(fromTableId);
			SvcTable toTable = svcTableMapper.selectByPrimaryKey(toTableId);

			if (fromTable == null) {
				throw new RequestResolveException(ErrorCode.TABLE_NOT_FOUND.code, fromTableId + " id table is not found.");
			}
			if (toTable == null) {
				throw new RequestResolveException(ErrorCode.TABLE_NOT_FOUND.code, toTableId + " id table is not found.");
			}
			if (fromTable.getOrderId() == null || fromTable.getOrderId() == 0) {
				throw new RequestResolveException(ErrorCode.ORDER_NOT_FOUND.code, fromTableId + " id table has not order.");
			}
			if (toTable.getOrderId() != null && toTable.getOrderId() != 0) {
				throw new RequestResolveException(ErrorCode.ORDER_EXIST.code, toTableId + " id table had order.");
			}

			// 테이블 락 상태 확인
			throwIfTableLocked(fromTableId, posNo);
			throwIfTableLocked(toTableId, posNo);

			// fromTable을 주문을 toTable로 이동
			toTable.setOrderId(fromTable.getOrderId());
			svcTableMapper.updateByPrimaryKey(toTable);

			// 주문의 테이블 ID 변경		
			SvcOrder order = svcOrderMapper.selectByPrimaryKey(fromTable.getOrderId());
			order.setTableNo(String.valueOf(toTable.getId()));
			svcOrderMapper.updateByPrimaryKey(order);

			// 기존 테이블 비움
			fromTable.setOrderId(null);
			svcTableMapper.updateByPrimaryKey(fromTable);

			// Build result
			// 이동 처리 요청한 두 테이블의 상세 정보를 조회하여 반환
			List<SingleMap> result = new ArrayList<SingleMap>();

			// from table detail
			SingleMap tableDetail = objectMapper.convertValue(fromTable, SingleMap.class);
			// 테이블 이동시 fromTable에는 주문이 제거되므로 조회 할 필요 없음
			result.add(tableDetail);

			// to table detail
			tableDetail = objectMapper.convertValue(toTable, SingleMap.class);
			tableDetail.put("order", getOrderDetailOrThrow(tableDetail));
			result.add(tableDetail);

			platformTransactionManager.commit(transactionStatus);

			// send mqtt (주문 변경 알림)
			SvcStore store = svcStoreMapper.selectByPrimaryKey(toTable.getStoreId());
			sendMqtt(store, order);

			return result;

		} catch (Exception e) {
			platformTransactionManager.rollback(transactionStatus);
			throw e;
		}
	}

	
	/**
	 * 공통 주문 처리 모듈 호출
	 * 
	 * @throws RequestResolveException
	 */
	@Override
	public SvcTableExtended saveOrder(SingleMap param) throws RequestResolveException {

		SvcOrderExtended order = objectMapper.convertValue(param.get("order"), SvcOrderExtended.class);
		final long tableId = param.getLong("tableId");
		final String withTableLock = param.getString("withTableLock", TABLE_LOCK_NONE);
		final String posNo = param.getString("posNo", null); // FIXME posNo를 헤더에 전달하면 clerk/tabl이 릴리즈 되면 두번째 파라미터를 제거해야 empty 체크 하도록 해야함

		// 설정된 user id가 없으면 null로 변경. 0으로는 입력될수 없음
		if (order.getUserId() != null && order.getUserId() == 0) {
			order.setUserId(null);
		}

		clerkCommonService.throwIfPosNotReady(order.getStoreId());

		throwIfTableLocked(ClerkUtil.parseLong(order.getTableNo(), 0l), posNo);

		// 주문 저장
		try {
			// 내부에서 주문 정보를 다시 반환함.
			// 신규 주문등은 order id 등이 할당 됨.
			order = orderService.saveOrder(order);
			logger.info("Saved order. posNo={}, orderId={}", posNo, order.getId());
		} catch (Throwable e) {
			logger.error("Can not save order on common order service.", e);
			throw new RequestResolveException(ErrorCode.ORDER_SAVE_FAILED.code, "Failed save order.", e);
		}

		// MQTT로 주문 변경 알림
		SvcStore store = svcStoreMapper.selectByPrimaryKey(order.getStoreId());
		User user = order.getUserId() != null ? userMapper.selectByPrimaryKey(order.getUserId()) : null;
		MQTTClient.sendOrder(store, order, order.getSvcOrderItems().get(0), user, order.getOrderTp(), posNo);

		// 신규 주문시 미승인 상태로 주문이 들어오며 
		// 포스에서 주문 정보를 조회하여 승인 상태로 변경할때까지 대기
		if (ORDER_ST_UNAPPROVED.equals(order.getOrderSt())) {
			boolean isApproved = waitApprovalOrder(order.getId(), 10000 /* millis */);

			// 승인 상태로 변경되지 못했으면 주문을 삭제하고 에러 처리
			if (!isApproved) {
				deleteOrder(order.getId());
				throw new RequestResolveException(ErrorCode.ORDER_CAN_NOT_APPROVED.code, "Can not approved order. orderId=" + order.getId());
			}
		}

		// 승인 처리되었으면 테이블에 할당 한다		
		orderService.updateTableOrderInfo(order.getId());

		// 테이블 락 해제 요청이 있는 경우 해제 (가능 하면)
		if (TABLE_LOCK_RELEASE.equals(withTableLock)) {
			logger.info("Release table lock tableId={}, posNo={}", tableId, posNo);
			setTableLockOffIfPossible(tableId, posNo);
		}

		// 완료된 테이블 주문 정보를 전달
		return getTableDetailByTableId(tableId);
	}


	
	/**
	 * 키오스크 주문 저장
	 * 수정 : 
	 * 
	 * 공통 주문 처리 모듈 호출
	 * 
	 * @throws RequestResolveException
	 */
	@Override
	public SvcTableExtended saveOrderKiosk(SingleMap param) throws RequestResolveException {

		SvcOrderExtended order = objectMapper.convertValue(param.get("order"), SvcOrderExtended.class);
		final long   tableId = param.getLong("tableId");
		final String withTableLock = param.getString("withTableLock", TABLE_LOCK_NONE);
		final String posNo = param.getString("posNo", null); // FIXME posNo를 헤더에 전달하면 clerk/tabl이 릴리즈 되면 두번째 파라미터를 제거해야 empty 체크 하도록 해야함
		final boolean isUsePrinter = param.getBoolean("isUsePrinter", false);
		//SvcDelivery orderDelivery = objectMapper.convertValue(param.get("orderDelivery"), SvcDelivery.class);
		
		logger.debug("확인>>>>>>>>>>"+order.getUserId()); //0
		logger.debug("확인 >>>>>>>>>>"+param); 
		//{withTableLock=release, os=android, tableId=1, isUsePrinter=true, posNo=103, userName=kiosk1_-_89_-_2668, lang=ko, order={acceptTm=2020-11-20 06:34:25, acceptTmLocal=2020-11-20 15:34:25, brandId=44, customerCnt=1, discount=0.0, id=0, isReserve=false, lastSt=951002, openDt=2020-11-17 00:00:00, orderDiv=1, orderNo=44891605854065469, orderSt=607002, orderTm=2020-11-20 06:34:25, orderTmLocal=2020-11-20 15:34:25, orderTp=605001, pathTp=606004, posNo=103, sales=1004.0, serviceCharge=0.0, staffId=82, storeId=89, supplyValue=904.0, svcOrderItems=[{catCd=1001, catNm=식사류, count=1, discount=0.0, id=0, image=/image-resource/items/store/89/348/it_st_89_1577128826420.jpg, isCanceled=false, isPacking=false, isStamp=false, itemCd=20191212095230, itemId=348, itemNm=김치찌개, itemTp=818000, lastSt=951002, netSales=904.0, optPrice=0.0, orderAmount=1004.0, orderId=0, orderTm=2020-11-20 06:34:25, orderTmLocal=2020-11-20 15:34:25, ordinal=1605854065468, orgCount=0, orgId=0, pathTp=606004, price=1004.0, purchasePrice=0.0, sales=1004.0, salesDiv=0, salesTypeDiv=0, serviceCharge=0.0, shortName=김치찌개, staffId=82, svcOrderDiscounts=[], svcOrderHistories=[], svcOrderItemOpts=[], tax=100.0, taxTp=819001}], svcOrderPays=[{acceptorCd=09, acceptorNm=현대카드, amount=1004.0, cardInfo=현대비자, cardInfoCd=09, cardNo=4172-33**-****-****, created=2020-11-20 15:34:25, id=0, monthlyPlain=0, orderId=0, ordinal=1, payMethod=810002, paySt=415003, payTm=2020-11-20 06:34:25, payTmLocal=2020-11-20 15:34:25, pgKind=, staffId=82, tranNo=00754768, updated=2020-11-20 15:34:25}], tableNo=1, tax=100.0, useCoupon=false, userId=0}}  
		
		// 추가 : 
		// SvcOrderPay
		//SvcOrderPay paymentInfo = objectMapper.convertValue(param.get("paymentInfo"), SvcOrderPay.class);
		// 추가 : 
		
		// 설정된 user id가 없으면 null로 변경. 0으로는 입력될수 없음
		if (order.getUserId() != null && order.getUserId() == 0) {
			order.setUserId(null);
		}

		/**
		 * 매장 포스가 주문을 받을 준비가 되어 있는지 확인한다.
		 * 매장 포스가 open 되어 있지 않거나 마감되어 있으면 예외를 던짐.
		 * 포스가 살아 있는 상태가 아니면 예외를 던짐
		 */
		// 수정 : 
		// 포스를 확인하지 않는다
		//clerkCommonService.throwIfPosNotReady(order.getStoreId());
		//throwIfTableLocked(ClerkUtil.parseLong(order.getTableNo(), 0l), posNo);
		// 수정 : 


		// 주문 저장
		try {
			// 내부에서 주문 정보를 다시 반환함.
			// 신규 주문등은 order id 등이 할당 됨.
			// 수정 : 
			// saveOrder --> saveOrderKiosk
			
			// 추가 : 2020.02.01
			// 신용카드, 현금 구분.
			if (!order.getSvcOrderPays().isEmpty()) {
				// 신용카드(810002) , 현금 (810001)
				order.setPayMethod(order.getSvcOrderPays().get(0).getPayMethod());
			}
			order.setIsUsePrinter(isUsePrinter);
			order = orderService.saveOrderKiosk(order);

			logger.info("Kiosk Saved order. posNo={}, orderId={}, orderNo={}, receiptId={}, receiptNo={},",  //posNo=103, orderId=36676, orderNo=44891605854073931, receiptId=20201120-0004, receiptNo=20201120-0004
						 			posNo, order.getId(), order.getOrderNo(), order.getReceiptId(), order.getReceiptNo());
		} 
		catch (Throwable e) {
			logger.error("Can not save order on common Kiosk order service.", e); // 결제가 안될시 이쪽으로 넘어감
			throw new RequestResolveException(ErrorCode.ORDER_SAVE_FAILED.code, "Failed save order.", e);
		}
 
		
		// 수정 : 
		// 키오스크 주문은 MQTT 전송을 하지 않는다
		// MQTT로 주문 변경 알림
		/*
		SvcStore store = svcStoreMapper.selectByPrimaryKey(order.getStoreId());
		User user = order.getUserId() != null ? userMapper.selectByPrimaryKey(order.getUserId()) : null;
		MQTTClient.sendOrder(store, order, order.getSvcOrderItems().get(0), user, order.getOrderTp(), posNo);
		*/
		// 수정 : 

		// 수정 : 
		// 신규 주문시 미승인 상태로 주문이 들어오며 
		// 포스에서 주문 정보를 조회하여 승인 상태로 변경할때까지 대기
		/*
		if (ORDER_ST_UNAPPROVED.equals(order.getOrderSt())) {
			boolean isApproved = waitApprovalOrder(order.getId(), 10000);

			// 승인 상태로 변경되지 못했으면 주문을 삭제하고 에러 처리
			if (!isApproved) {
				deleteOrder(order.getId());
				throw new RequestResolveException(ErrorCode.ORDER_CAN_NOT_APPROVED.code, "Can not approved order. orderId=" + order.getId());
			}
		}

		// 승인 처리되었으면 테이블에 할당 한다		
		orderService.updateTableOrderInfo(order.getId());

		// 테이블 락 해제 요청이 있는 경우 해제 (가능 하면)
		if (TABLE_LOCK_RELEASE.equals(withTableLock)) {
			logger.info("Release table lock tableId={}, posNo={}", tableId, posNo);
			setTableLockOffIfPossible(tableId, posNo);
		}

		// 완료된 테이블 주문 정보를 전달
		return getTableDetailByTableId(tableId);

		*/
		// 수정 : 

		// 수정 : 		
		// 완료된 테이블 주문 정보를 전달
		// getTableDetailByTableId --> getTableDetailByTableIdKiosk
		return getTableDetailByTableIdKiosk(tableId, order);
		// 수정 : 
	}
	
	
	
	/**
	 * 주문상태가 승인으로 변경될때까지 대기한다.
	 * 최대 지정한 밀리초까지 대기하며 1초 단위로 확인한다.
	 * 
	 *
	 * 
	 * @param maxWaitInMs
	 *            최대 대기시간
	 */
	private boolean waitApprovalOrder(long orderId, int maxWaitInMs) {

		logger.info("Wait approval order orderId={}, maxWaitInMs={}", orderId, maxWaitInMs);

		final Integer sleepInMs = 1000; // 1s		

		for (int i = maxWaitInMs; i > 0; i -= sleepInMs) {

			try {
				synchronized (sleepInMs) {
					Thread.sleep(sleepInMs);
				}
			} catch (InterruptedException e) {
				break;
			}

			final String orderSt = clerkOrderMapper.selectOrderStById(orderId);

			if (ORDER_ST_APPROVED.equals(orderSt)) {
				// 승인 상태로 변경되면 정상
				// 포스에서 주문 변경 mqtt를 받은후 정보를 조회하갈때 승인 상태로 변경 처리됨.
				return true;

			} else if (!ORDER_ST_UNAPPROVED.equals(orderSt)) {
				// 승인/미승인 이외의 상태로 가면 실패 처리 
				return false;
			}
			// else 미승인 상태면 계속 대기
		}

		return false;
	}

	/**
	 * 테이블과 테이블의 주문 정보를 조회
	 * 
	 * @param tableId
	 * @return 테이블 정보와 상세 주문 정보
	 */
	private SvcTableExtended getTableDetailByTableId(long tableId) {
		SvcTableExtended table = getSvcTableExtendedByTableId(tableId);
		if (table == null) {
			return null;
		}
		
		// 포함된 주문이 있는 경우 조회
		if (table.getOrderId() != null) {
			table.setOrder(getOrderDetailByOrderId(table.getOrderId()));
		}
		
		return table;
	}

	
	/**
	 * 추가 : 
	 * Kiosk 버전
	 * 테이블과 테이블의 주문 정보를 조회
	 * 
	 * @param tableId
	 * @return 테이블 정보와 상세 주문 정보
	 */
	private SvcTableExtended getTableDetailByTableIdKiosk(long tableId, SvcOrderExtended order) {
		SvcTableExtended table = getSvcTableExtendedByTableId(tableId);
		
		if (table == null) {
			// 수정 : 
			// 키오스크는 테이블을 사용하지 않기 때문에 데이터가 존재하지 않기 때문에
			// 기본 정보로 채운다
			table = new SvcTableExtended();
			table.setBrandId(order.getBrandId());
			table.setStoreId(order.getStoreId());
			table.setSectionId(0L);
			table.setOrdinal((short)1);
			table.setName("1");

			//return null;
		}

		// 포함된 주문이 있는 경우 조회
		if (table.getOrderId() != null) {
			// 수정 : 
			//table.setOrder(getOrderDetailByOrderId(table.getOrderId()));
			// 수정 : 

			// 추가 : 
			// 키오스크 주문은 한번으로 끝나기 때문에 더 이상 서버의 주문을 키오스트로 보내지 않는다
			table.setOrder(null);
			// 추가 : 
		}


		// 추가 : 
		// 서버에서 주문번호 및 영수증 번호를 생성해서 클라이언트로 전달
		if (table.getOrder() == null) {
			// 오더 정보를 찾지 못한 경우
			table.setAdminId(order.getReceiptId());
			table.setOrderNo(order.getOrderNo());
			table.setReceiptId(order.getReceiptId());
			table.setReceiptNo(order.getReceiptNo());

			/*
			table.setOrder(new SvcOrderExtended());
			table.getOrder().setOrderNo(order.getOrderNo()); 
			table.getOrder().setReceiptId(order.getReceiptId()); 
			table.getOrder().setReceiptNo(order.getReceiptNo());

			logger.info("*** SvcTableExtended null(2) : orderNo={}, receiptId={}, receiptNo={}", 
					table.getOrder().getOrderNo(),
					table.getOrder().getReceiptId(),
					table.getOrder().getReceiptNo());
			*/
		}
		else if (table.getOrder() != null) {
			table.getOrder().setOrderNo(order.getOrderNo()); 
			table.getOrder().setReceiptId(order.getReceiptId()); 
			table.getOrder().setReceiptNo(order.getReceiptNo());
		}
		// 추가 : 

		
		return table;
	}
	
	
	/**
	 * table id로 테이블 정보 조회
	 * 
	 * @param tableId
	 * @return 상세 정보를 담을 수 있는 테이블 정보
	 */
	private SvcTableExtended getSvcTableExtendedByTableId(long tableId) {
		SvcTable table = svcTableMapper.selectByPrimaryKey(tableId);
		return table != null ? objectMapper.convertValue(table, SvcTableExtended.class) : null;
	}

	/**
	 * order id로 주문 조회
	 * 
	 * @param orderId
	 * @return 상세 정보를 담을 수 있는 주문 정보
	 */
	private SvcOrderExtended getSvcOrderExtendedByOrderId(long orderId) {
		SvcOrder order = getSvcOrderByOrderId(orderId);
		return order != null ? objectMapper.convertValue(order, SvcOrderExtended.class) : null;
	}

	
	/**
	 * order id로 주문 조회
	 * 
	 * @param orderId
	 * @return 주문 정보
	 */
	private SvcOrder getSvcOrderByOrderId(long orderId) {
		SvcOrderExample orderExample = new SvcOrderExample();
		orderExample.createCriteria() // 검색 조건
				.andIdEqualTo(orderId) // 주문 번호
				.andOrderStNotEqualTo(ORDER_ST_CANCEL);
		List<SvcOrder> orders = svcOrderMapper.selectByExample(orderExample);
		return orders.size() > 0 ? orders.get(0) : null;
	}

	
	/**
	 * getSvcOrderExtendedByOrderIdEx
	 * 추가 : 
	 * 
	 * order id로 주문 조회
	 * 
	 * @param orderId
	 * @return 상세 정보를 담을 수 있는 주문 정보
	 */
	private SvcOrderExtended getSvcOrderExtendedByOrderIdEx(long storeId, long brandId, String orderId) {
		SvcOrder order = getSvcOrderByOrderIdEx(storeId, brandId, orderId);
		return order != null ? objectMapper.convertValue(order, SvcOrderExtended.class) : null;
	}
	
	
	/**
	 * order id로 주문 조회
	 * 
	 * @param orderId
	 * @return 주문 정보
	 */
	private SvcOrder getSvcOrderByOrderIdEx(long storeId, long brandId, String orderId) {
		SvcOrderExample orderExample = new SvcOrderExample();
		orderExample.createCriteria()        // 검색 조건
				.andBrandIdEqualTo(brandId)  // 브랜드 
				.andStoreIdEqualTo(storeId)  // 상점
				.andOrderNoEqualTo(orderId); // 주문 번호
		List<SvcOrder> orders = svcOrderMapper.selectByExample(orderExample);
		return orders.size() > 0 ? orders.get(0) : null;
	}

	
	
	
	/**
	 * order id로 주문 조회(키오스크)
	 * 추가 : 
	 * @param 
	 * 
	 * @return 상세 정보를 담을 수 있는 주문 정보
	 */
	private SvcOrderExtended getSvcOrderExtendedByOrderKiosk(long orderId) {
		SvcOrder order = getSvcOrderByOrderKiosk(orderId);
		return order != null ? objectMapper.convertValue(order, SvcOrderExtended.class) : null;
	}

	
	/**
	 * order id로 주문 조회(키오스크)
	 * 
	 * @param 
	 * 
	 * @return 주문 정보
	 */
	private SvcOrder getSvcOrderByOrderKiosk(long orderId) {
		Long   BRAND_ID = 44L;
		Long   STORE_ID = 89L;
		String startString = "2019-12-09 00:00:00.0";
		Date   startDate   = null;
		String endString   = "2019-12-09 23:59:59.0";
		Date   endDate     = null;
		
		try {
			startDate   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startString);
			endDate     = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endString);
		}
		catch (Exception e) {
		}
		
		SvcOrderExample orderExample = new SvcOrderExample();
		orderExample.createCriteria()  // 검색 조건
		 	    .andBrandIdEqualTo(BRAND_ID)
		 	    .andStoreIdEqualTo(STORE_ID)
		 	    .andOrderTmLocalBetween(startDate, endDate);
				//.andIdEqualTo(orderId) // 주문 번호
				//.andOrderStNotEqualTo(ORDER_ST_CANCEL);
		List<SvcOrder> orders = svcOrderMapper.selectByExample(orderExample);
		return orders.size() > 0 ? orders.get(0) : null;
	}

	
	
	/**
	 * order id로 주문 항목 조회
	 * 
	 * @param orderId
	 * @return 추가 정보를 담을 수 있는 주문 항목 목록
	 */
	private List<SvcOrderItemExtended> getSvcOrderItemExtendedListByOrderId(long orderId) {
		List<SvcOrderItem> items = getSvcOrderItemListByOrderId(orderId);
		List<SvcOrderItemExtended> result = new ArrayList<SvcOrderItemExtended>(items.size());
		for (SvcOrderItem item : items) {
			result.add(objectMapper.convertValue(item, SvcOrderItemExtended.class));
		}
		return result;
	}

	/**
	 * order id로 주문 항목 조회
	 * 
	 * @param orderId
	 * @return 주문 항목 목록
	 */
	private List<SvcOrderItem> getSvcOrderItemListByOrderId(long orderId) {
		SvcOrderItemExample example = new SvcOrderItemExample();
		example.createCriteria() // 
				.andLastStNotEqualTo(LAST_ST_DELETE) // 삭제된 항목은 제외
				.andOrderIdEqualTo(orderId);
		return svcOrderItemMapper.selectByExample(example);
	}

	/**
	 * orderItemId에 해당하는 주문 항목 옵션 조회
	 * 
	 * @param orderItemId
	 * @return 주문 항목 옵션
	 */
	private List<SvcOrderItemOpt> getSvcOrderItemOptionListByOrderItemId(Long orderItemId) {
		SvcOrderItemOptExample example = new SvcOrderItemOptExample();
		example.createCriteria() // 조건
				.andItemIdEqualTo(orderItemId);
		return svcOrderItemOptMapper.selectByExample(example);
	}

	/**
	 * orderItemId에 해당하는 주문 항목 히스토리 조회
	 * 
	 * @param orderItemId
	 * @return 주문 항목 히스토리
	 */
	private List<SvcOrderItemHistory> getSvcOrderItemHistoryListByOrderItemId(Long orderItemId) {
		SvcOrderItemHistoryExample example = new SvcOrderItemHistoryExample();
		example.createCriteria() // 조건
				.andItemIdEqualTo(orderItemId);
		return svcOrderItemHistoryMapper.selectByExample(example);
	}

	/**
	 * orderItemId에 해당하는 주문 할인 정보
	 * 
	 * @param orderItemId
	 * @return 주문 할인 정보
	 */
	private List<SvcOrderDiscount> getSvcOrderDiscountListByOrderItemId(Long orderItemId) {
		SvcOrderDiscountExample example = new SvcOrderDiscountExample();
		example.createCriteria() // 조건
				.andItemIdEqualTo(orderItemId);
		return svcOrderDiscountMapper.selectByExample(example);
	}

	/**
	 * 테이블 ID에 해당하는 테이블과 주문 정보를 조회
	 */
	@Override
	@Transactional
	public SingleMap getTableDetail(SingleMap param) throws RequestResolveException {
		long tableId = param.getLong("tableId");
		String withTableLock = param.getString("withTableLock", TABLE_LOCK_NONE);
		String posNo = param.getString("posNo", null); // FIXME posNo를 헤더에 전달하면 clerk/tabl이 릴리즈 되면 두번째 파라미터를 제거해야 empty 체크 하도록 해야함 

		if (TABLE_LOCK_SET.equals(withTableLock)) {
			setTableLockOnIfPossible(tableId, posNo);
		} else if (TABLE_LOCK_RELEASE.equals(withTableLock)) {
			setTableLockOffIfPossible(tableId, posNo);
		}
		// else TABLE_LOCK_NONE do nothing.. 

		SvcTableExtended table = getTableDetailByTableId(tableId);
		if (table == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.TABLE_NOT_FOUND.code, tableId + " table id is not found.");
		}

		SvcClosing closing = clerkCommonService.getLatestClosingByStoreId(param.getLong("storeId"));
		SingleMap result = new SingleMap();
		result.put("table", table);
		result.put("openDt", closing != null ? closing.getOpenDt() : null);

		return result;
	}

	/**
	 * 테이블에 락을 걸 수 있는 상태면 락을 설정 한다
	 * 
	 * @param tableId
	 * @param posNo
	 */
	private void setTableLockOnIfPossible(long tableId, String posNo) {
		SvcTable table = svcTableMapper.selectByPrimaryKey(tableId);
		if (table == null || posNo == null) {
			return;
		}

		// 이미 락이 걸린 상태면 무시
		if (table.getIsUsed()) {
			return;
		}

		// 락 설정
		SvcTable record = new SvcTable();
		record.setId(tableId);
		record.setIsUsed(true);
		record.setPosNo(posNo);
		svcTableMapper.updateByPrimaryKeySelective(record);

		// 락은 포스당 한 테이블만 가능
		SvcTableExample example = new SvcTableExample();
		example.createCriteria() // 락을 해제할 테이블 조건
				.andStoreIdEqualTo(table.getStoreId()) // 동일 매장
				.andPosNoEqualTo(posNo) // 해당 포스의 락				
				.andIdNotEqualTo(tableId) // 락을 요청한 테이블 이외에
				.andIsUsedEqualTo(true); // 사용중인 것을
		record.setId(null);
		record.setIsUsed(false); // 락 해제 
		record.setPosNo(null);
		svcTableMapper.updateByExampleSelective(record, example);
	}

	/**
	 * 테이블에 락을 걸 수 있는 상태면 락을 해제 한다
	 * 
	 * @param tableId
	 * @param posNo
	 */
	private void setTableLockOffIfPossible(long tableId, String posNo) {
		SvcTable table = svcTableMapper.selectByPrimaryKey(tableId);
		if (table == null || posNo == null) {
			return;
		}

		// 락이 해제 상태면 무시, 락을 건 포스와 해제를 요청한 포스가 다르면 무시 
		if (!table.getIsUsed() || !StringUtils.equals(table.getPosNo(), posNo)) {
			return;
		}

		// 락 설정
		SvcTable record = new SvcTable();
		record.setId(tableId);
		record.setIsUsed(false);
		svcTableMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SingleMap> mergeTableOrder(SingleMap param) throws RequestResolveException {

		final TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

		try {

			long fromTableId = param.getLong("fromTableId");
			long toTableId = param.getLong("toTableId");
			String pathTp = param.getString("pathType");
			String posNo = param.getString("posNo");

			SvcTable fromTable = svcTableMapper.selectByPrimaryKey(fromTableId);
			SvcTable toTable = svcTableMapper.selectByPrimaryKey(toTableId);

			if (fromTable == null) {
				throw new RequestResolveException(ErrorCode.TABLE_NOT_FOUND.code, fromTableId + " id table is not found.");
			}
			if (toTable == null) {
				throw new RequestResolveException(ErrorCode.TABLE_NOT_FOUND.code, toTableId + " id table is not found.");
			}

			if (fromTable.getOrderId() == null || fromTable.getOrderId() == 0) {
				throw new RequestResolveException(ErrorCode.ORDER_NOT_FOUND.code, fromTableId + " id table has not order.");
			}
			if (toTable.getOrderId() == null || toTable.getOrderId() == 0) {
				throw new RequestResolveException(ErrorCode.ORDER_NOT_FOUND.code, toTableId + " id table has not order.");
			}

			// 테이블 락 상태 확인
			throwIfTableLocked(fromTableId, posNo);
			throwIfTableLocked(toTableId, posNo);

			SvcOrder fromOrder = svcOrderMapper.selectByPrimaryKey(fromTable.getOrderId());
			SvcOrder toOrder = svcOrderMapper.selectByPrimaryKey(toTable.getOrderId());
			SvcStore store = svcStoreMapper.selectByPrimaryKey(fromOrder.getStoreId());
			TimeZone storeTimezone = TimeZone.getTimeZone(store.getTimezone());

			// fromOrder를 toOrder에 합침
			toOrder.setSales(toOrder.getSales() + fromOrder.getSales());
			toOrder.setDiscount(toOrder.getDiscount() + fromOrder.getDiscount());
			toOrder.setCustomerCnt((short) (toOrder.getCustomerCnt() + fromOrder.getCustomerCnt()));
			toOrder.setIsConfirm(false);
			toOrder.setTax(toOrder.getTax() + fromOrder.getTax());
			toOrder.setServiceCharge(toOrder.getServiceCharge() + fromOrder.getServiceCharge());
			toOrder.setSupplyValue(toOrder.getSupplyValue() + fromOrder.getSupplyValue());
			toOrder.setUseCoupon(toOrder.getUseCoupon() | fromOrder.getUseCoupon());
			toOrder.setLastSt(LAST_ST_UPDATE);
			toOrder.setPathTp(pathTp);
			svcOrderMapper.updateByPrimaryKey(toOrder);

			// fromTable의 주문은 취소 처리
			// 주문의 table_no는 포스에서 사용하므로 유지. 
			fromOrder.setOrderSt(ORDER_ST_CANCEL);
			fromOrder.setLastSt(LAST_ST_DELETE);
			fromOrder.setPathTp(pathTp);
			fromOrder.setCancelTmLocal(new Date());
			fromOrder.setCancelTm(posUtil.convertLocaltoUtc(fromOrder.getCancelTmLocal(), storeTimezone));
			svcOrderMapper.updateByPrimaryKey(fromOrder);

			// 테이블에 할당된 주문 제거
			fromTable.setOrderId(null);
			svcTableMapper.updateByPrimaryKey(fromTable);

			// fromOrder의 order item을 toOrder로 이동
			moveOrderItem(fromOrder.getId(), toOrder.getId(), pathTp);

			// fromOrder의 order item opt를 toOrder 로 이동
			moveOrderItemOpt(fromOrder.getId(), toOrder.getId(), pathTp);

			// fromOrder의 order item history를 toOrder로 이동
			moveOrderItemHistory(fromOrder.getId(), toOrder.getId(), pathTp);

			// fromOrder의 order discount 를 toOrde로 이동
			moveOrderDiscount(fromOrder.getId(), toOrder.getId(), pathTp);

			// fromOrder의 order pay를 toOrde로 이동
			moveOrderPay(fromOrder.getId(), toOrder.getId(), pathTp);

			// Build result
			// 이동 처리 요청한 두 테이블의 상세 정보를 조회하여 반환
			List<SingleMap> result = new ArrayList<SingleMap>();

			// from table detail
			SingleMap tableDetail = objectMapper.convertValue(fromTable, SingleMap.class);
			// 테이블을 합치면 fromTable에는 주문이 제거되므로 상세는 조회 할 필요 없음
			result.add(tableDetail);

			// to table detail
			tableDetail = objectMapper.convertValue(toTable, SingleMap.class);
			tableDetail.put("order", getOrderDetailOrThrow(tableDetail));
			result.add(tableDetail);

			platformTransactionManager.commit(transactionStatus);

			// send mqtt			
			sendMqtt(store, fromOrder);
			sendMqtt(store, toOrder);

			return result;

		} catch (Exception e) {
			platformTransactionManager.rollback(transactionStatus);
			throw e;
		}
	}

	/**
	 * fromOrderId의 order item을 toOrderId로 이동
	 * 
	 * @param fromOrderId
	 * @param toOrderId
	 */
	private void moveOrderItem(Long fromOrderId, Long toOrderId, String pathTp) {

		// fromOrder에 있는 order item을 toOrder로 이동
		// order item 의 ordinal 은 유니크 해야하니 
		// from order items의 ordinal은 to order items 이후의 ordinal 을 부여함

		List<SvcOrderItem> fromOrderItems = getSvcOrderItemListByOrderId(fromOrderId);
		List<SvcOrderItem> toOrderItems = getSvcOrderItemListByOrderId(toOrderId);

		// to order item의 마지막 ordinal 검색		
		long ordinal = 0;
		for (SvcOrderItem item : toOrderItems) {
			if (ordinal < item.getOrdinal()) {
				ordinal = item.getOrdinal();
			}
		}

		// order item
		for (SvcOrderItem item : fromOrderItems) {
			item.setOrderId(toOrderId);
			item.setOrdinal(++ordinal);
			item.setLastSt(LAST_ST_NEW);
			item.setPathTp(pathTp);
			svcOrderItemMapper.updateByPrimaryKey(item);
		}
	}

	/**
	 * fromOrder의 order item opt를 toOrder로 이동
	 * 
	 * @param fromOrderId
	 * @param toOrderId
	 */
	private void moveOrderItemOpt(Long fromOrderId, Long toOrderId, String pathTp) {

		// fromOrder에 있는 order item opt 를  toOrder로 이동
		// order item opt 의 ordinal 은 유니크 해야하니 
		// from order item opt 의 ordinal 은 to order item opt 이후의 ordinal 을 부여함

		// order item opt
		List<SvcOrderItemOpt> fromOrderItemOpts = getSvcOrderItemOptListByOrderId(fromOrderId);
		List<SvcOrderItemOpt> toOrderItemOpts = getSvcOrderItemOptListByOrderId(toOrderId);

		// to order item opt의 마지막 ordinal검색
		long ordinal = 0;
		for (SvcOrderItemOpt item : toOrderItemOpts) {
			if (ordinal < item.getOrdinal()) {
				ordinal = item.getOrdinal();
			}
		}

		// order item opt 의 order id를 to order id로 변경
		SvcOrderItemOpt optRecord = new SvcOrderItemOpt();
		optRecord.setOrderId(toOrderId);
		for (SvcOrderItemOpt item : fromOrderItemOpts) {
			optRecord.setId(item.getId());
			optRecord.setOrdinal(++ordinal);
			svcOrderItemOptMapper.updateByPrimaryKeySelective(optRecord);
		}
	}

	/**
	 * fromOrder의 order item history를 toOrder로 이동
	 * 
	 * @param fromOrderId
	 * @param toOrderId
	 */
	private void moveOrderItemHistory(Long fromOrderId, Long toOrderId, String pathTp) {

		List<SvcOrderItemHistory> fromOrderItemHistories = getSvcOrderItemHistoryListByOrderId(fromOrderId);
		List<SvcOrderItemHistory> toOrderItemHistories = getSvcOrderItemHistoryListByOrderId(toOrderId);

		// to order item opt의 마지막 ordinal검색
		long ordinal = 0;
		for (SvcOrderItemHistory item : toOrderItemHistories) {
			if (ordinal < item.getOrdinal()) {
				ordinal = item.getOrdinal();
			}
		}

		// order item opt 의 order id를 to order id로 변경
		SvcOrderItemHistory record = new SvcOrderItemHistory();
		record.setOrderId(toOrderId);
		for (SvcOrderItemHistory item : fromOrderItemHistories) {
			record.setId(item.getId());
			record.setOrdinal(++ordinal);
			svcOrderItemHistoryMapper.updateByPrimaryKeySelective(record);
		}
	}

	/**
	 * fromOrder의 order pay 를 toOrder로 이동
	 * 
	 * @param fromOrderId
	 * @param toOrderId
	 */
	private void moveOrderPay(Long fromOrderId, Long toOrderId, String pathTp) {

		List<SvcOrderPay> fromOrderPays = getSvcOrderPayListByOrderId(fromOrderId);
		List<SvcOrderPay> toOrderPays = getSvcOrderPayListByOrderId(toOrderId);

		// to order item opt의 마지막 ordinal검색
		long ordinal = 0;
		for (SvcOrderPay item : toOrderPays) {
			if (ordinal < item.getOrdinal()) {
				ordinal = item.getOrdinal();
			}
		}

		// order item opt 의 order id를 to order id로 변경
		SvcOrderPay record = new SvcOrderPay();
		record.setOrderId(toOrderId);
		for (SvcOrderPay item : fromOrderPays) {
			record.setId(item.getId());
			record.setOrdinal(++ordinal);
			svcOrderPayMapper.updateByPrimaryKeySelective(record);
		}
	}

	/**
	 * fromOrder의 order discount 를 toOrder로 이동
	 * 
	 * @param fromOrderId
	 * @param toOrderId
	 */
	private void moveOrderDiscount(Long fromOrderId, Long toOrderId, String pathTp) {

		List<SvcOrderDiscount> fromOrderDiscounts = getSvcOrderDiscountListByOrderId(fromOrderId);
		List<SvcOrderDiscount> toOrderDiscounts = getSvcOrderDiscountListByOrderId(toOrderId);

		// to order item opt의 마지막 ordinal검색
		long ordinal = 0;
		for (SvcOrderDiscount item : toOrderDiscounts) {
			if (ordinal < item.getOrdinal()) {
				ordinal = item.getOrdinal();
			}
		}

		// order item opt 의 order id를 to order id로 변경
		SvcOrderDiscount record = new SvcOrderDiscount();
		record.setOrderId(toOrderId);
		for (SvcOrderDiscount item : fromOrderDiscounts) {
			record.setId(item.getId());
			record.setOrdinal(++ordinal);
			svcOrderDiscountMapper.updateByPrimaryKeySelective(record);
		}
	}

	private List<SvcOrderItemOpt> getSvcOrderItemOptListByOrderId(Long orderId) {
		SvcOrderItemOptExample example = new SvcOrderItemOptExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(orderId);
		return svcOrderItemOptMapper.selectByExample(example);
	}

	private List<SvcOrderItemHistory> getSvcOrderItemHistoryListByOrderId(Long orderId) {
		SvcOrderItemHistoryExample example = new SvcOrderItemHistoryExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(orderId);
		return svcOrderItemHistoryMapper.selectByExample(example);
	}

	private List<SvcOrderPay> getSvcOrderPayListByOrderId(Long orderId) {
		SvcOrderPayExample example = new SvcOrderPayExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(orderId);
		return svcOrderPayMapper.selectByExample(example);
	}

	private List<SvcOrderDiscount> getSvcOrderDiscountListByOrderId(Long orderId) {
		SvcOrderDiscountExample example = new SvcOrderDiscountExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(orderId);
		return svcOrderDiscountMapper.selectByExample(example);
	}

	private void sendMqtt(SvcStore store, SvcOrder order) {
		final User user = order.getUserId() != null && order.getUserId() != 0 ? userMapper.selectByPrimaryKey(order.getUserId()) : null;
		List<SvcOrderItem> orderItems = getSvcOrderItemListByOrderId(order.getId());
		MQTTClient.sendOrder(store, order, orderItems.isEmpty() ? null : orderItems.get(0), user, order.getOrderTp(), order.getPosNo());
	}

	private void deleteOrder(Long orderId) {
		SvcOrder record = new SvcOrder();
		record.setId(orderId);
		record.setLastSt(LAST_ST_DELETE); // 주문 삭제
		record.setIsConfirm(false);
		svcOrderMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 테이블의 락상태를 확인하여 접근 권한이 없으면 예외를 던진다.
	 * 락이 걸려 있지 않으면 통과.
	 * 락이 걸려 있는데 건 포스가 현재 요청자 포스 번호와 일치하면 통과.
	 * 
	 * @param tableId
	 *            테이블 번호
	 * 
	 * @param posNo
	 *            요청자 포스 번호
	 * @throws RequestResolveException
	 */
	private void throwIfTableLocked(long tableId, String posNo) throws RequestResolveException {

		if (tableId == 0) {
			// 지정된 테이블이 없으면 처리 하지 않음
			return;
		}

		SvcTable table = svcTableMapper.selectByPrimaryKey(tableId);
		if (table == null || !table.getIsUsed()) {
			// 테이블 정보가 없으면 무시
			// 테이블 잠금 상태가 아니면 통과
			return;
		}

		if (!StringUtils.equals(table.getPosNo(), posNo)) {
			throw new RequestResolveException(ClerkResult.ErrorCode.TABLE_NOT_PERMISSION.code,
					tableId + " table is locked from [" + table.getPosNo() + "]");
		}
	}
	
	
	/**
	 * setOrderCancelComplete
	 *   주문, 매출 취소
	 *   
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public boolean setOrderCancelComplete(SingleMap param)  {

		Long   ID             = param.getLong("Id");
		Long   STORE_ID = param.getLong("storeId");
		Long   BRAND_ID = param.getLong("brandId");
		String ORDER_ID = param.getString("orderId");
		
		HashMap<String, Object> cancelOrder = new HashMap();
		cancelOrder.put("id",          ID);
		cancelOrder.put("storeId",  STORE_ID);
		cancelOrder.put("brandId", BRAND_ID);
		cancelOrder.put("orderId",  ORDER_ID);
		cancelOrder.put("orderNo",  ORDER_ID);
		cancelOrder.put("orderSt", ORDER_ST_CANCEL); // 주문, 매출  취소

		int orderUpdate = 0;
		int salesUpdate = 0;
		try {
			// 주문 취소
			orderUpdate = svcOrderMapper.updateByOrderCancel(cancelOrder);
			// 주문 취소 시 매장 프린터 미 출력된 영수증 삭제
			if(orderUpdate > 0)  {
				orderService.deleteKitchenPrinterAtOrderCancel(cancelOrder);
			}
			
			// 매출 취소 
			salesUpdate = svcOrderMapper.updateBySalesCancel(cancelOrder);
			
		} catch (Exception e) {
			throw new InvalidParameterException(e.getMessage());
		}

		return (orderUpdate > 0 && salesUpdate > 0 ? true : false);
	}
	
	
	/**
	 * getMerchantAndPrinter
	 *   결재  ID,  주방 프린터 정보 
	 *   
	 */
	@Override
	public SvcStorePrinter getMerchantAndPrinter(SingleMap param)  {

		SvcStorePrinter merchantInfo;

		try  {
			Long   STORE_ID  = param.getLong("storeId");
			Long   BRAND_ID = param.getLong("brandId");

			HashMap<String, Object> storeInfo = new HashMap();
			storeInfo.put("storeId",  STORE_ID);
			storeInfo.put("brandId", BRAND_ID);
			merchantInfo = svcMerchantMapper. selectByMerchantAndPrinter(storeInfo);
		
			logger.debug("getMerchantAndPrinter > IP  :  " + ( merchantInfo.getIp() != null ? merchantInfo.getIp() : "") );
			logger.debug("getMerchantAndPrinter > MerchantId  :  " + ( merchantInfo.getPgMerchantId() != null ? merchantInfo.getPgMerchantId() : "") );
			
		} catch (Exception e) {
			throw new InvalidParameterException(e.getMessage());
		}

		return merchantInfo;
	}

	@Override
	public List<SvcKitchenPrinter> getOrderKitchenPrinter() {
		StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		// 포스번호, 등록자, 상태 갱신
		SvcKitchenPrinter svcKitchenPrinter = new SvcKitchenPrinter();
		svcKitchenPrinter.setStoreId(staffUserDetail.getStoreId());
		svcKitchenPrinter.setBrandId(staffUserDetail.getBrandId());
		
		List<SvcKitchenPrinter> list = orderService.getOrderKitchenPrinter(svcKitchenPrinter);
		
		return list;
	}

	@Override
	public List<SvcKitchenPrinter> setOrderKitchenPrinter(SingleMap param) {
		StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		// 포스번호, 등록자, 상태 갱신
		SvcKitchenPrinter svcKitchenPrinter = new SvcKitchenPrinter();
		
		svcKitchenPrinter.setStoreId(staffUserDetail.getStoreId());
		svcKitchenPrinter.setBrandId(staffUserDetail.getBrandId());
		svcKitchenPrinter.setOrderNo(param.getString("orderNo"));
		svcKitchenPrinter.setPrinterNo(param.getString("printerNo"));
		
		orderService.setOrderKitchenPrinter(svcKitchenPrinter);
		
		List<SvcKitchenPrinter> list = orderService.getOrderKitchenPrinter(svcKitchenPrinter);
		
		return list;
	}

	@Override
	public SingleMap storeInfo(SingleMap param) throws RequestResolveException {
		final StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		// 포스번호, 등록자, 상태 갱신
		SingleMap map = new SingleMap();
		HashMap<String, Object> storeInfo = new HashMap<String, Object>();
		try  {
			List<SvcStoreInfoPrinter> printerList = new ArrayList<SvcStoreInfoPrinter>();
			storeInfo.put("storeId",  staffUserDetail.getStoreId());
			storeInfo.put("brandId", staffUserDetail.getBrandId());
			storeInfo.put("licenseId", staffUserDetail.getLicenseId());
			SvcStoreInfo svcStoreInfo = svcStoreInfoMapper. selectMerchantAndPrinter(storeInfo);
			SingleMap deviceLicenseMap = svcStoreInfoMapper.selectDeviceLicenseIsMain(storeInfo);
			
			SvcStore store = clerkCommonService.getStoreById(staffUserDetail.getStoreId());
			if (store == null) {
				throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code,  " store is not found.");
			}
			
			if(deviceLicenseMap != null) {
				svcStoreInfo.setIsMain(deviceLicenseMap.getBoolean("IS_MAIN", false));
			}
			
			for(SvcStoreInfoPrinter printer : svcStoreInfo.getPrinters()) {
				if(StrUtils.toIpRegex(printer.getIp())) {
					printerList.add(printer);
				}
			}
			
			svcStoreInfo.setPrinters(printerList);
			map = objectMapper.convertValue(svcStoreInfo, SingleMap.class);
			map.put("store", store);
			
		} catch (Exception e) {
			throw new RequestResolveException(ErrorCode.STORE_INFO_NOT_FOUND.code, storeInfo + " storeInfo is not found.");
		}
		return map;
	}







}
