package com.jc.pico.service.pos.impl;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.Message;
import com.jc.pico.bean.MessageModel;
import com.jc.pico.bean.Sequence;
import com.jc.pico.bean.SvcDelivery;
import com.jc.pico.bean.SvcItem;
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
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcTable;
import com.jc.pico.bean.SvcTableExample;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.OrderSequenceMapper;
import com.jc.pico.mapper.SvcDeliveryMapper;
import com.jc.pico.mapper.SvcItemMapper;
import com.jc.pico.mapper.SvcKitchenPrinterMapper;
import com.jc.pico.mapper.SvcOrderDiscountMapper;
import com.jc.pico.mapper.SvcOrderItemHistoryMapper;
import com.jc.pico.mapper.SvcOrderItemMapper;
import com.jc.pico.mapper.SvcOrderItemOptMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.mapper.SvcOrderPayMapper;
import com.jc.pico.mapper.SvcOrderSalesMapper;
import com.jc.pico.mapper.SvcSalesMapper;
import com.jc.pico.mapper.SvcTableMapper;
import com.jc.pico.service.clerk.ClerkCommonService;
import com.jc.pico.service.pos.OrderInternalService;
import com.jc.pico.utils.APIInit;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.ClerkResult.ErrorCode;
import com.jc.pico.utils.bean.PosPrinterInfo;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.bean.SvcOrderItemExtended;
import com.jc.pico.utils.customMapper.pos.PosSalesTableOrderMapper;
import com.jc.pico.utils.customMapper.pos.PosStorePrinterMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Service
public class OrderInternalServiceImpl implements OrderInternalService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 주문 상태 : 삭제
	 */
	private static final String LAST_ST_DELETE = "951004";

	/**
	 * 주문 상태 : 미승인
	 */
	public static final String ORDER_ST_DISAPPROVAL = "607001";

	/**
	 * 주문 상태 : 승인
	 */
	public static final String ORDER_ST_APPOVAL = "607002";

	/**
	 * 주문 상태 : 취소 (승인 취소)
	 */
	public static final String ORDER_ST_CANCEL = "607003";

	/**
	 * 주문 상태 : 거절 (미승인 거절)
	 */
	public static final String ORDER_ST_REJECT = "607004";

	/**
	 * 주문 상태 : 완료
	 */
	public static final String ORDER_ST_COMPLETE = "607005";

	/**
	 * 주문 경로 : 앱
	 */
	public static final String ORDER_PATH_APP = "606002";
	
	/**
	 * 매출 상태 : 정상
	 */
	public static final String SALES_ST_NORMAL = "809001";

	/**
	 * 매출 상태 : 반품
	 */
	public static final String SALES_ST_RETURN = "809002";
	
	/* 매출 */
	// 추가 : 
	@Autowired
	private SvcSalesMapper svcSalesMapper;
	
	@Autowired
	private OrderSequenceMapper orderSequenceMapper;
	// 추가 : 
	
	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private SvcOrderItemMapper svcOrderItemMapper;

	@Autowired
	private SvcOrderDiscountMapper svcOrderDiscountMapper;

	@Autowired
	private SvcOrderItemHistoryMapper svcOrderItemHistoryMapper;

	@Autowired
	private SvcOrderItemOptMapper svcOrderItemOptMapper;

	@Autowired
	private SvcTableMapper svcTableMapper;

	@Autowired
	private SvcOrderPayMapper svcOrderPayMapper;

	@Autowired
	private PosSalesTableOrderMapper posSalesTableOrderMapper;
	
	@Autowired
	private PosStorePrinterMapper posStorePrinterMapper;
	
	@Autowired
	private SvcKitchenPrinterMapper svcKitchenPrinterMapper;

	@Autowired
	private SvcItemMapper svcItemMapper;
	
	@Autowired
	private SvcOrderSalesMapper svcOrderSalesMapper;

	@Autowired
	private PosUtil posUtil;
	
	
	@Autowired
	private ClerkCommonService clerkCommonService;
	
	@Autowired
	private SvcDeliveryMapper svcDeliveryMapper;	

	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = JsonConvert.getObjectMapper();
	}
	
	

	/**
	 * saveOrder
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public SvcOrderExtended saveOrder(SvcOrderExtended newOrder, SvcOrderExtended oldOrder) throws Throwable {

		logger.debug("SvcOrderExtended::saveOrder.1 > oldOrder : ");
		 
		// 1. Validation
		throwIfInvalidOrder(newOrder);
		logger.debug("SvcOrderExtended::saveOrder.2 > oldOrder : ");

		// 상태 변경 시간 설정
		if (isOrderStatusChanged(newOrder, oldOrder)) {
			setOrderStatusChangedTimeAtNow(newOrder, oldOrder); // 현재 시간으로 설정 

			logger.debug("SvcOrderExtended::saveOrder.3 > oldOrder : ");
		}

		// 주문 마스터 정보 저장
		saveOrderMaster(newOrder, oldOrder);
		logger.debug("SvcOrderExtended::saveOrder.4 > oldOrder : ");

		// 5. 주문 상품 리스트 저장
		List<SvcOrderItemExtended> svcOrderItemExtendeds = newOrder.getSvcOrderItems();
		if (svcOrderItemExtendeds != null) {

			logger.debug("SvcOrderExtended::saveOrder.5 > oldOrder : ");
			for (SvcOrderItemExtended svcOrderItemExtended : svcOrderItemExtendeds) {

				svcOrderItemExtended.setOrderId(newOrder.getId());
				logger.debug("SvcOrderExtended::saveOrder.6 > oldOrder : ");

				saveOrderItem(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.7 > oldOrder : ");

				// 6. 주문 상품별 할인 리스트 저장
				saveOrderItemDiscount(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.8 > oldOrder : ");

				// 7. 주문 상품별 히스토리 리스트 저장
				saveOrderHistories(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.9 > oldOrder : ");

				// 8. 주문 옵션 저장
				saveOrderItemOpts(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.10 > oldOrder : ");
			}
		}

		// 주문 결제 내역 저장
		saveOrderPays(newOrder);
		logger.debug("SvcOrderExtended::saveOrder.11 > oldOrder : ");

		// 테이블에 주문 할당/제거
		updateTableOrderInfo(newOrder);
		logger.debug("SvcOrderExtended::saveOrder.12 > oldOrder : ");

		return newOrder;
	}

	
	/**
	 * saveOrderKiosk
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public SvcOrderExtended saveOrderKiosk(SvcOrderExtended newOrder, SvcOrderExtended oldOrder, String host) throws Throwable {

		
		logger.debug("SvcOrderExtended::saveOrder.1 > oldOrder : ");

		// 1. Validation
		throwIfInvalidOrder(newOrder);
		logger.debug("SvcOrderExtended::saveOrder.2 > oldOrder : ");

		// 상태 변경 시간 설정
		if (isOrderStatusChanged(newOrder, oldOrder)) {
			setOrderStatusChangedTimeAtNow(newOrder, oldOrder); // 현재 시간으로 설정 

			logger.debug("SvcOrderExtended::saveOrder.3 > oldOrder : ");
		}
		
		// 영수중번호 (ex. 2)
		int receiptNo = createReceiptNo(newOrder);
		
		//업데이트 된 주문번호
		String orderNo = newOrder.getOrderNo();
		// 주문 마스터 정보 저장
		logger.debug("SvcOrderExtended::saveOrder.4 > oldOrder (b) : " + ( newOrder.getId() == null ? 0 : newOrder.getId() ) );
		saveOrderMasterKiosk(newOrder, oldOrder);
		logger.debug("SvcOrderExtended::saveOrder.4 > oldOrder (a) : " + ( newOrder.getId() == null ? 0 : newOrder.getId() ) );

		// 5. 주문 상품 리스트 저장
		List<SvcOrderItemExtended> svcOrderItemExtendeds = newOrder.getSvcOrderItems();
		
		if (svcOrderItemExtendeds != null) {
			logger.debug("SvcOrderExtended::saveOrder.5 > oldOrder(size) : " + svcOrderItemExtendeds.size()); // 1
			
			int ordinal = 0;
			String defaultPrinterNo = "";
			// 주방 프린트 item값 설정 및 print_no default값 설정
			List<SvcKitchenPrinter> svcKitchenPrintList = new ArrayList<SvcKitchenPrinter>();
			List<SvcItem> itemList = new ArrayList<SvcItem>();
			
			logger.debug("IsUsePrinter::::::::::::::: " + newOrder.getIsUsePrinter()); //true
			if(newOrder.getIsUsePrinter()) {
				defaultPrinterNo = generateDefaultPrinterNo(defaultPrinterNo, newOrder, itemList);
			}
			logger.debug("itemList:::::::::::::::::: " + itemList);
			
			String orderItemNm = null;
			int index = 0;
			for (SvcOrderItemExtended svcOrderItemExtended : svcOrderItemExtendeds) {

				if(index == 0) {
					orderItemNm = svcOrderItemExtended.getItemNm();
				}
				
				svcOrderItemExtended.setOrderId(newOrder.getId());
				logger.debug("SvcOrderExtended::saveOrder.6 > oldOrder : ");

				saveOrderItemKiosk(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.7 > oldOrder : ");

				// 6. 주문 상품별 할인 리스트 저장
				saveOrderItemDiscountKiosk(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.8 > oldOrder : ");

				// 7. 주문 상품별 히스토리 리스트 저장
				saveOrderHistoriesKiosk(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.9 > oldOrder : ");

				// 8. 주문 옵션 저장
				saveOrderItemOptsKiosk(svcOrderItemExtended, newOrder.getOrderNo());
				logger.debug("SvcOrderExtended::saveOrder.10 > oldOrder : ");
				
				logger.debug("SvcOrderExtended::saveOrder.10.5 > defaultPrinterNo : " + defaultPrinterNo);
				// 9. 주방 프린트 설정
				if(!defaultPrinterNo.equals("")) {
					ordinal = saveOrderKitchenPrinter(svcOrderItemExtended, newOrder, itemList, svcKitchenPrintList, defaultPrinterNo, ordinal);
				}
				logger.debug("SvcOrderExtended::saveOrder.11 > oldOrder : ");
				
				index++;
			}
			// 주방 프린트 저장
			if(!svcKitchenPrintList.isEmpty()) {
				svcKitchenPrinterMapper.insertList(svcKitchenPrintList);
			}
			

			//System.out.println("찍어라>>>>>>>>>>>>>>>>>>>>>>>>>>"+newOrder.getSvcOrderDelivery().getBrandId());
			
			if(newOrder.getSvcOrderDelivery().getBrandId() == 0) {
				newOrder.getSvcOrderDelivery().setBrandId(null);
				
			}
			
			if(newOrder.getSvcOrderDelivery().getBrandId() != null) {
				
				//배달정보
				SvcDelivery orderDeliveryInfo = new SvcDelivery();
				
				orderDeliveryInfo.setBrandId(newOrder.getSvcOrderDelivery().getBrandId());
				orderDeliveryInfo.setStoreId(newOrder.getSvcOrderDelivery().getStoreId());
				orderDeliveryInfo.setOrderNo(orderNo);
				orderDeliveryInfo.setCusName(newOrder.getSvcOrderDelivery().getCusName());
				orderDeliveryInfo.setCusCellNo(newOrder.getSvcOrderDelivery().getCusCellNo());
				orderDeliveryInfo.setCusTelNo(newOrder.getSvcOrderDelivery().getCusTelNo());
				orderDeliveryInfo.setCusZip(newOrder.getSvcOrderDelivery().getCusZip());
				orderDeliveryInfo.setCusAddr1(newOrder.getSvcOrderDelivery().getCusAddr1());
				orderDeliveryInfo.setCusAddr2(newOrder.getSvcOrderDelivery().getCusAddr2());
				orderDeliveryInfo.setCusMessage(newOrder.getSvcOrderDelivery().getCusMessage());
				System.out.println("확인중--------------------------------------------------------------------1");				
				svcDeliveryMapper.insertOrderDeliveryInfo(orderDeliveryInfo);
				
				//주문시간
				Date AcceptTmLocal = newOrder.getAcceptTmLocal();
				SimpleDateFormat transFormat = new SimpleDateFormat("MM/dd a HH:mm");
				String orderTm = transFormat.format(AcceptTmLocal);
				
				//배달담당가게정보
				SvcStore orderStore = clerkCommonService.getStoreById(newOrder.getSvcOrderDelivery().getStoreId());
				String storeNm = orderStore.getStoreNm();
				
				//배달상품명
				String itemNm = orderItemNm;
				//예외) 여러 상품 주문시
				if(index > 1) {
					String cnt = "등 "+(index-1)+"개";
					itemNm = orderItemNm.concat(cnt);
				}
				
				//문제메세지 보내기
				sendMessage(orderDeliveryInfo, orderTm, storeNm, itemNm, host);
				

			}
			
		}
		
		Long salesId = saveOrderSalesKiosk(newOrder.getId());
		// 추가 : 
		
		if(salesId == null) {
			throw new RequestResolveException(ErrorCode.DATA_NOT_FOUND.code, salesId + " salesId is not found.");
		}
		
		// 주문 결제 내역 저장		
		saveOrderPaysKiosk(newOrder);
		logger.debug("SvcOrderExtended::saveOrder.12 > oldOrder : ");
		
		// 매출 결제 내역 저장		
		saveOrderSalesPayKiosk(newOrder.getId(), salesId);
		logger.debug("SvcOrderExtended::saveOrder.13 > oldOrder : ");




		return newOrder;
	}
	
	private void sendMessage(SvcDelivery orderDeliveryInfo, String orderTm, String storeNm, String itemNm, String host) throws IOException { //배달정보, 담당업체, 주문시간, 상품명
		//이름,우편번호,주소,상세주소
		String cusName = orderDeliveryInfo.getCusName();
		String cusZip = orderDeliveryInfo.getCusZip();
		String cusAddr1 = orderDeliveryInfo.getCusAddr1();
		String cusAddr2 = orderDeliveryInfo.getCusAddr2();
		

        //주문번호
		String orderNo = orderDeliveryInfo.getOrderNo();
		
		
		//주문상세정보
		String orderDetailInfo = host+"/order9/orderInfo?orderNo="+orderNo;
		
		
		//수신&발신번호
		String cusCellNo = orderDeliveryInfo.getCusCellNo();
		String order9Number = "0312030960";
		/*
		 * 안녕하세요 정아름님 주문해 주셔서 감사합니다.
		 * 확인 후 신속히 배달해 드리도록 하겠습니다.
		 * 
		 * ● 주문일시
		 * ● 주문번호
		 * ● 담당업체
		 * ● 주문상품
		 * ● 배달주소
		 */
		String orderDeliveryInfoText = String.format("안녕하세요 %s님%n" // cusName
                                                      + "주문해 주셔서 감사합니다.%n"
				                                      + "신속히 배달해 드리도록 하겠습니다.%n%n"
                                                      
				                                      + "● 주문일시 : %s%n" // orderTm
				                                      + "● 주문번호%n　☞ [%s]%n" // orderNo
                                                      + "● 담당업체 : %s%n" // storeNm
                                                      + "● 주문상품 : %s%n" // itemNm
                                                      + "● 배달주소 : (우)%s %s %s%n%n"// cusZip, cusAddr1, cusAddr2
                                                      + "▼ 주문 상세 정보%n %s"
                                                      ,cusName, orderTm, orderNo, storeNm, itemNm, cusZip, cusAddr1, cusAddr2, orderDetailInfo);
				
		//수신번호, 발신번호, 제목, 내용
		Message message = new Message(cusCellNo, order9Number,orderDeliveryInfoText,"주문 접수 완료");
		

		Call<MessageModel> api = APIInit.getAPI().sendMessage(APIInit.getHeaders(), message);
		 api.enqueue(new Callback<MessageModel>() { //MessageModel 추가
	            @Override
	            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
	                // 성공 시 200이 출력됩니다.
	                if (response.isSuccessful()) {
	                    System.out.println("statusCode : " + response.code());
	                    MessageModel body = response.body();
	                    System.out.println("groupId : " + body.getGroupId());
	                    System.out.println("messageId : " + body.getMessageId());
	                    System.out.println("to : " + body.getTo());
	                    System.out.println("from : " + body.getFrom());
	                    System.out.println("type : " + body.getType());
	                    System.out.println("statusCode : " + body.getStatusCode());
	                    System.out.println("statusMessage : " + body.getStatusMessage());
	                    System.out.println("customFields : " + body.getCustomFields());
	    				System.out.println("확인중--------------------------------------------------------------------4");
	                } else {
	                    try {
	                        System.out.println(response.errorBody().string());
	        				System.out.println("확인중--------------------------------------------------------------------5");
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }

	            @Override
	            public void onFailure(Call<MessageModel> call, Throwable throwable) {
	                throwable.printStackTrace();
	            }
	        });
	    }


	private Long saveOrderSalesKiosk(Long orderId) throws RequestResolveException {
		if(orderId != null) {
			SingleMap map = new SingleMap();
			map.put("orderId", orderId);
			SingleMap order = svcOrderSalesMapper.selectOrder(map);
			
			if(order == null) {
				throw new RequestResolveException(ErrorCode.DATA_NOT_FOUND.code, order.toString() + " order is not found.");
			}
			order.put("orderSt", ORDER_ST_COMPLETE);
			svcOrderSalesMapper.insertOrderToSales(order);
			
			final Long salesId = order.getLong("id");
			if(salesId < 1) {
				throw new RequestResolveException(ErrorCode.DATA_NOT_FOUND.code, salesId + " salesId is not found.");
			}
			
			
			
			if(order.get("items") != null) {
				List<SingleMap> items = (List<SingleMap>) order.get("items");
				for(SingleMap item : items) {
					item.put("salesId", salesId);
					svcOrderSalesMapper.insertOrderItemToSalesItem(item);
					if(item.get("options") != null) {
						List<SingleMap> options = (List<SingleMap>) item.get("options");
						for(SingleMap option : options) {
							option.put("salesId", salesId);
							option.put("itemId", item.getLong("id"));
							svcOrderSalesMapper.insertOrderItemOptionToSalesItemOption(option);
						}
					}
				}
			}
			return salesId;
		}
		return null;
	}
	
	private void saveOrderSalesPayKiosk(Long orderId, Long salesId) {
		SingleMap map = new SingleMap();
		map.put("orderId", orderId);
		map.put("salesId", salesId);
		map.put("salesSt", SALES_ST_NORMAL);
		svcOrderSalesMapper.insertOrderPay(map);
	}


	/**
	 * 영수증번호 / 주문번호 
	 * 
	 * @param newOrder
	 * @return receiptNo
	 * @throws Throwable
	 */
	private int createReceiptNo(SvcOrderExtended newOrder) throws Throwable {
		int receiptNo = 0;
		String seqId  = String.format("%s%s", newOrder.getBrandId(), newOrder.getStoreId());
		Date today    = new Date();
		SimpleDateFormat todayFormat = new SimpleDateFormat("yyyyMMdd");
		String receiptDate = todayFormat.format(today);
		String orderNo = seqId + today.getTime();
		
		Sequence seq = orderSequenceMapper.selectByPrimaryKey(seqId);

		if (seq != null) {
			logger.debug("- Sequence 1 > Id={}, Today={}, OrderNo={}, ReceiptNo={}, receiptDate={}, getSeqToday={}",  
							seq.getSeqId(), seq.getSeqToday(), seq.getSeqOrderNo(), seq.getSeqReceiptNo(), receiptDate,seq.getSeqToday() );

			if (receiptDate.equals(seq.getSeqToday())) {
				// 영업일 같은 날
				receiptNo = seq.getSeqReceiptNo() + 1;
			}
			else {
				// 영업일 다음 날, 영수증 번호 1로 초기화
				receiptNo = 1;
			}

			// 영수증번호 / 주문번호 : 업데이트
			seq.setSeqReceiptNo(receiptNo);
			seq.setSeqToday(receiptDate);
			seq.setSeqOrderNo(seqId + today.getTime());
			
			orderSequenceMapper.updateSequence(seq);
		}
		else {
			// 신규업체 등록
			// 영수증번호 / 주문번호
			seq = new Sequence();

			receiptNo = 1;
			seq.setSeqId(seqId);
			seq.setSeqToday(receiptDate);
			seq.setSeqOrderNo(seqId + today.getTime());
			seq.setSeqReceiptNo(receiptNo);

			orderSequenceMapper.insertSequence(seq);
		}

		// 주문번호, 영수증 번호 업데이트
		String receiptId = String.format("%s-%04d", receiptDate, receiptNo);
		newOrder.setOrderNo(orderNo);
		newOrder.setReceiptId(receiptId);
		newOrder.setReceiptNo(receiptId);

		logger.debug("- Sequence 2 > Id={}, Today={}, OrderNo={}, ReceiptNo={}, receiptDate={}, getSeqToday={}",  
				seq.getSeqId(), seq.getSeqToday(), seq.getSeqOrderNo(), seq.getSeqReceiptNo(), receiptDate,seq.getSeqToday() );

		return receiptNo;
	}

	
	/**
	 * 포스에서 사용하는 주문 정보 PK를 기준으로 저장된 주문 정보를 조회
	 * 
	 * @param storeId
	 * @param openDt
	 * @param orderNo
	 * @return
	 * @throws RequestResolveException
	 */
	private SvcOrder getOrder(Long storeId, Date openDt, String orderNo) throws RequestResolveException {
		/* 수정 : 
		 * 수정 필요함
		 *
		 * select ID, BRAND_ID, STORE_ID, USER_ID, STAFF_ID, ORDER_NO, LAST_ST, PATH_TP, ORDER_TP, ORDER_DIV, OPEN_DT, ORDER_ST, ORDERER_NAME, ORDERER_MB, 
                  RECEIPT_ID, RECEIPT_NO, ORDER_TM_LOCAL, ORDER_TM, RESERVE_NO, RESERVE_TM, RESERVE_TM_LOCAL, RESERVE_END_TM, RESERVE_END_TM_LOCAL, 
                  RESERVE_REG_TM, RESERVE_REG_TM_LOCAL, EST_TM, COMPLETE_TM, COMPLETE_TM_LOCAL, ACCEPT_TM, ACCEPT_TM_LOCAL, REJECT_TM, REJECT_TM_LOCAL, 
                  CANCEL_TM, CANCEL_TM_LOCAL, READY_TM, READY_TM_LOCAL, CANCEL_TP, USE_COUPON, SALES, DISCOUNT, SUPPLY_VALUE, TAX, SERVICE_CHARGE, CUSTOMER_CNT, 
                  PAY_METHOD, TABLE_NO, PERSON, POS_NO, MEMO, REASON, ADMISSION_TM, EXIT_TM, CUSTOMER_TP, CUSTOME_AGE_TP, CUSTOME_GENDER, IS_SALES, IS_CONFIRM, 
                  CREATED, UPDATED 
             from tb_svc_order 
            WHERE ( BRAND_ID = 44 AND ORDER_NO = 1572525533412 and STORE_ID = 89 ) 
            order by ID DESC limit 0, 1
        *
        * 수정 : 
 		*/
		
		// orderNo가 존재하면 기존 데이터가 있는지 확인후
		// 존재하면 갱신, 없으면 새롭게 추가하는데 이용
		// open_dt의 시분초는 0이어야 하나, 0가 아닌 정보가 들어와 아래 처럼 범위로 검색함 0시 ~ 23시 59분 59초..
		SvcOrderExample svcOrderExample = new SvcOrderExample();
		svcOrderExample.createCriteria()    // 검색 조건
				.andOrderNoEqualTo(orderNo) // 주문 번호
				.andStoreIdEqualTo(storeId) // 스토어 ID
				.andOpenDtBetween(posUtil.getDateTime(openDt, 0, 0, 0, 0) // 포스 오픈 시작
						        , posUtil.getDateTime(openDt, 23, 59, 59, 999)); // 포스 오픈 종료

		// 중복으로 데이터가 들어가는 문제가 있어 차선으로 가장 최근에 갱신된 데이터를 사용 하도록 변경
		// 중복으로 저장되지 않도록 개선이 필요함. (mysql에서 제공하는 어플리케이션 락을 걸었으나 뜻대로 동작을 안해서..)
		svcOrderExample.setOrderByClause("UPDATED DESC, ID DESC");

		List<SvcOrder> orders = svcOrderMapper.selectByExample(svcOrderExample);

		if (orders.size() == 0) { // 신규 주문, orderNo는 전달 받은것 사용
			return null;
		} else {
			return orders.get(0);
		}
	}

	/**
	 * 주문과 주문한 상품 정보를 조회한다.
	 * 스토어 알림에서 상품 정보를 필요로해서 해당 정보만 추가 조회
	 * 
	 * @param storeId
	 * @param openDt
	 * @param orderNo
	 * @return
	 * @throws RequestResolveException
	 */
	@Override
	public SvcOrderExtended getOrderExtended(Long storeId, Date openDt, String orderNo) throws RequestResolveException {
		SvcOrder order = getOrder(storeId, openDt, orderNo);
		if (order == null) {
			return null;
		}
		
		SvcOrderExtended result = objectMapper.convertValue(order, SvcOrderExtended.class);
		result.setSvcOrderItems(getSvcOrderItemExtendedListByOrderId(result.getId()));
		
		return result;
	}

	/**
	 * 주문과 주문한 상품 정보를 조회한다.
	 * 스토어 알림에서 상품 정보를 필요로해서 해당 정보만 추가 조회
	 * 
	 * @param storeId
	 * @param openDt
	 * @param orderNo
	 * @return
	 * @throws RequestResolveException
	 */
	@Override
	public SvcOrderExtended getOrderExtended(Long orderId, Long storeId, Date openDt, String orderNo) throws RequestResolveException {
		SvcOrder order;
		if (orderId != null && orderId.longValue() > 0) {
			order = svcOrderMapper.selectByPrimaryKey(orderId);
		} else {
			order = getOrder(storeId, openDt, orderNo);
		}

		if (order == null) {
			return null;
		}

		SvcOrderExtended result = objectMapper.convertValue(order, SvcOrderExtended.class);
		result.setSvcOrderItems(getSvcOrderItemExtendedListByOrderId(result.getId()));
		return result;
	}

	/**
	 * 주문 정보가 올바르지 않으면 예외를 던저 중단함
	 * 
	 * @param svcOrderExtended
	 * @throws Exception
	 */
	private void throwIfInvalidOrder(SvcOrderExtended svcOrderExtended) throws Exception {
		if (svcOrderExtended.getStoreId() == null) {
			logger.error("[{}][{}] StoreID empty. storeId: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, svcOrderExtended.getStoreId());
			throw new Exception("StoreId empty. storeId: " + svcOrderExtended.getStoreId());
		}
		if (svcOrderExtended == null || svcOrderExtended.getSvcOrderItems() == null) {
			logger.error("[{}][{}] Order or Items are empty. order: {}, items: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, svcOrderExtended,
					svcOrderExtended.getSvcOrderItems());
			throw new Exception(
					"Order or Items are empty. order: " + svcOrderExtended.getOrderNo() + ", items: " + svcOrderExtended.getSvcOrderItems());
		}
		if (svcOrderExtended.getOpenDt() == null) {
			throw new InvalidParameterException("openDt is empty.");
		}
	}

	
	/**
	 * 주문 정보중 tb_svc_order에 해당하는 부분 저장 처리
	 * 
	 * @param newOrder
	 * @throws RequestResolveException
	 */
	private void saveOrderMaster(SvcOrder newOrder, SvcOrder oldOrder) throws RequestResolveException {

		if (newOrder.getId() != null && newOrder.getId().longValue() > 0) {
			// 기존 주문 수정
			// order id를 기준으로 갱신
			svcOrderMapper.updateByPrimaryKeySelective(newOrder);
			return;
		}

		if (oldOrder != null) {
			// 기존 주문 수정
			// order id를 기준으로 갱신
			newOrder.setId(oldOrder.getId());
			svcOrderMapper.updateByPrimaryKeySelective(newOrder);
			return;
		}

		// 신규 주문
		newOrder.setId(null); // 0인 경우 null로 설정하여 DB에서 할당되도록 함.
		svcOrderMapper.insertSelective(newOrder);
	}

	
	/**
	 * 주문 정보중 tb_svc_order에 해당하는 부분 저장 처리
	 * 추가 : 
	 * 
	 * @param newOrder
	 * @throws RequestResolveException
	 */
	private long saveOrderMasterKiosk(SvcOrder newOrder, SvcOrder oldOrder) throws RequestResolveException {
		// 추가 : 
		// 기본값 true 설정
		newOrder.setIsSales(true);
		newOrder.setIsConfirm(true);
		// 추가 : 
		
		// 신규 주문
		newOrder.setId(null); // 0인 경우 null로 설정하여 DB에서 할당되도록 함.
		svcOrderMapper.insertSelective(newOrder);

		
		// 매출 저장 추가 
		// 추가 : 
		HashMap<String, Long> orderMap = new HashMap();
		orderMap.put("id", newOrder.getId());

		logger.debug("SvcOrderExtended::saveOrder.12 > getId : " + newOrder.getId());
//		svcSalesMapper.insertIntoSales(orderMap);
		logger.debug("SvcOrderExtended::saveOrder.12 > getId : " + orderMap.get("id"));

		return (orderMap.get("id") != null ? orderMap.get("id") : 0);
		// 추가 : 
	}
	
	
	/**
	 * 상태가 변경된 시간을 설정한다.
	 * FIXME 포스에서 상태 변경시 해당 시간을 설정해서 보내고 그대로 저장하도록 변경 해야함.
	 * 당장에 시연을 위해 오차가 발생하더라도 이렇게 구현 함.
	 * 
	 * @param newOrder
	 * @param date
	 */
	private void setOrderStatusChangedTimeAtNow(SvcOrder newOrder, SvcOrder oldOrder) {

		// 미승인 -> 승인 -> 완료
		// 미승인 -> 승인 -> 취소
		// 미승인 -> 거절

		String orderSt = newOrder.getOrderSt();
		Date date = new Date();

		// 승인
		if (ORDER_ST_APPOVAL.equals(orderSt)) {
			// 승인시는 포스에서 설정해서 보내주기로 함.

			// 준비 (승인 상태에서 테이블이 할당되면 해당)
			if (!StringUtils.isEmpty(newOrder.getTableNo()) && !StringUtils.equals(newOrder.getTableNo(), "0")) {
				newOrder.setReadyTm(date);
				newOrder.setReadyTmLocal(date);
			}

			return;
		}

		// 취소
		if (ORDER_ST_CANCEL.equals(orderSt)) {
			newOrder.setCancelTm(date);
			newOrder.setCancelTmLocal(date);

			// 취소 상태인데 승인 상태 시간이 없으면 설정
			if (oldOrder != null && oldOrder.getAcceptTm() == null) {
				newOrder.setAcceptTm(newOrder.getCancelTm());
				newOrder.setAcceptTmLocal(newOrder.getCancelTmLocal());
			}

			return;
		}

		// 거절
		if (ORDER_ST_REJECT.equals(orderSt)) {
			newOrder.setRejectTm(date);
			newOrder.setRejectTmLocal(date);
			return;
		}

		// 완료
		if (ORDER_ST_COMPLETE.equals(orderSt)) {

			// 완료 시간 기록
			newOrder.setCompleteTm(newOrder.getExitTm());
			newOrder.setCompleteTmLocal(newOrder.getExitTm());

			// 완료 상태인데 승인 상태 시간이 없으면 설정
			if (oldOrder != null && oldOrder.getAcceptTm() == null) {
				newOrder.setAcceptTm(newOrder.getCompleteTm());
				newOrder.setAcceptTmLocal(newOrder.getCompleteTmLocal());
			}

			// 완료  상태인데 준비 상태 시간이 없으면 설정
			if (oldOrder != null && oldOrder.getReadyTm() == null) {
				newOrder.setReadyTm(newOrder.getCompleteTm());
				newOrder.setReadyTmLocal(newOrder.getCompleteTmLocal());
			}

			// 예약 정보가 있으면 예약 만료 시간을 기록
			if (newOrder.getReserveTmLocal() != null) {
				newOrder.setReserveEndTmLocal(newOrder.getCompleteTm());
				newOrder.setReserveEndTm(newOrder.getCompleteTmLocal()); // TODO UTC 시간 처리 필요
			}

			return;
		}
	}


	/**
	 * saveOrderItem
	 * @param orderItem
	 * @param orderNo
	 * @throws RequestResolveException
	 */
	private void saveOrderItem(SvcOrderItemExtended orderItem, String orderNo) throws RequestResolveException {
		if (orderItem.getOrdinal() == null || orderItem.getOrdinal() == 0) {
			throw new InvalidParameterException(
					"Invalid order item ordinal (" + orderItem.getOrdinal() + "), name=" + orderItem.getItemNm() + ", orderNo=" + orderNo);
		}
		SvcOrderItemExample svcOrderItemExample = new SvcOrderItemExample();
		svcOrderItemExample.createCriteria() // 검색 조건
				.andOrderIdEqualTo(orderItem.getOrderId()) // order id
				.andOrdinalEqualTo(orderItem.getOrdinal()); // 2016.08.13. edited by green, ORDINAL 값으로 Unique 가 보장되는 것으로 POS 와 이야기됨(2016.08.12. by 김도형선임)
		List<SvcOrderItem> svcOrderItems = svcOrderItemMapper.selectByExample(svcOrderItemExample);

		if (svcOrderItems.size() == 0) {
			// 0개이면 새로 insert
			svcOrderItemMapper.insertSelective(orderItem);
		} else if (svcOrderItems.size() == 1) {
			// 1개인 경우, 업데이트
			SvcOrderItem svcOrderItem = svcOrderItems.get(0);
			orderItem.setId(svcOrderItem.getId());
			svcOrderItemMapper.updateByPrimaryKeySelective(orderItem);
		} else {
			throw new RequestResolveException(PosUtil.EPO_0006_CODE,
					"OrderNo: " + orderNo + ", Item's Ordinal: " + orderItem.getOrdinal() + ", Too many ordinals");
		}
	}

	
	/**
	 * saveOrderItem
	 * @param orderItem
	 * @param orderNo
	 * @throws RequestResolveException
	 */
	private void saveOrderItemKiosk(SvcOrderItemExtended orderItem, String orderNo) throws RequestResolveException {
		if (orderItem.getOrdinal() == null || orderItem.getOrdinal() == 0) {
			throw new InvalidParameterException(
					"Invalid order item ordinal (" + orderItem.getOrdinal() + "), name=" + orderItem.getItemNm() + ", orderNo=" + orderNo);
		}
		
		// 수정 : 
		svcOrderItemMapper.insertSelective(orderItem);
		// 수정 : 
	}

	
	
	/**
	 * 할인 정보 저장
	 * 
	 * @param svcOrderItemExtended
	 * @param orderNo
	 * @param svcOrderExtended
	 * @param itemId
	 * @param updatedCount
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	private void saveOrderItemDiscount(SvcOrderItemExtended svcOrderItemExtended, String orderNo) throws RequestResolveException {
		List<SvcOrderDiscount> discounts = svcOrderItemExtended.getSvcOrderDiscounts();
		if (discounts == null || discounts.size() == 0) {
			return;
		}

		final long orderId = svcOrderItemExtended.getOrderId();
		final long itemId  = svcOrderItemExtended.getId();

		final SvcOrderDiscountExample example = new SvcOrderDiscountExample();

		for (SvcOrderDiscount discount : discounts) {

			discount.setOrderId(svcOrderItemExtended.getOrderId());
			discount.setItemId(itemId);

			example.clear();
			example.createCriteria() // 조건
					.andOrderIdEqualTo(orderId) // id 
					.andItemIdEqualTo(itemId) // item id
					.andDiscountTpEqualTo(discount.getDiscountTp()); // 2016.08.13. edited by green, discountTp 값으로 Unique 가 보장되는 것으로 POS 와 이야기됨(2016.08.12. by 김도형선임)

			List<SvcOrderDiscount> oldDiscounts = svcOrderDiscountMapper.selectByExample(example);
			if (oldDiscounts == null || oldDiscounts.size() == 0) {
				// 0개이면 새로 insert
				svcOrderDiscountMapper.insertSelective(discount);
			} else if (oldDiscounts.size() == 1) {
				// 1개인 경우, 업데이트
				discount.setId(oldDiscounts.get(0).getId());
				svcOrderDiscountMapper.updateByPrimaryKeySelective(discount);
			} else {
				throw new RequestResolveException(PosUtil.EPO_0006_CODE,
						"OrderNo: " + orderNo + ", Item: " + itemId + ", Discount: " + discount.getDiscountTp() + " Too many discounts");
			}
		}

	}

	
	/**
	 * 할인 정보 저장
	 * 
	 * @param svcOrderItemExtended
	 * @param orderNo
	 * @param svcOrderExtended
	 * @param itemId
	 * @param updatedCount
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	private void saveOrderItemDiscountKiosk(SvcOrderItemExtended svcOrderItemExtended, String orderNo) throws RequestResolveException {
		List<SvcOrderDiscount> discounts = svcOrderItemExtended.getSvcOrderDiscounts();
		if (discounts == null || discounts.size() == 0) {
			return;
		}

		final long orderId = svcOrderItemExtended.getOrderId();
		final long itemId  = svcOrderItemExtended.getId();

		final SvcOrderDiscountExample example = new SvcOrderDiscountExample();

		for (SvcOrderDiscount discount : discounts) {

			discount.setOrderId(svcOrderItemExtended.getOrderId());
			discount.setItemId(itemId);

			example.clear();
			example.createCriteria() // 조건
					.andOrderIdEqualTo(orderId) // id 
					.andItemIdEqualTo(itemId) // item id
					.andDiscountTpEqualTo(discount.getDiscountTp()); // 2016.08.13. edited by green, discountTp 값으로 Unique 가 보장되는 것으로 POS 와 이야기됨(2016.08.12. by 김도형선임)

			List<SvcOrderDiscount> oldDiscounts = svcOrderDiscountMapper.selectByExample(example);
			if (oldDiscounts == null || oldDiscounts.size() == 0) {
				// 0개이면 새로 insert
				svcOrderDiscountMapper.insertSelective(discount);
			} else if (oldDiscounts.size() == 1) {
				// 1개인 경우, 업데이트
				discount.setId(oldDiscounts.get(0).getId());
				svcOrderDiscountMapper.updateByPrimaryKeySelective(discount);
			} else {
				throw new RequestResolveException(PosUtil.EPO_0006_CODE,
						"OrderNo: " + orderNo + ", Item: " + itemId + ", Discount: " + discount.getDiscountTp() + " Too many discounts");
			}
		}

	}
	
	
	
	/**
	 * 테이블 주문 정보 갱신
	 * 
	 * @param tableId
	 * @param order
	 */
	private void updateTableOrderInfo(SvcOrder order) {

		// clerk/tab에서 발생한 미승인 주문건은 테이블에 바로 넣지 않고
		// 포스에서 주문 정보를 조회해 갈때 주문을 승인처리하고 테이블에 넣음.
		if (ORDER_ST_DISAPPROVAL.equals(order.getOrderSt())) {
			logger.debug("Disapproval order is not assigned to table. posNo={}, orderId={}", order.getPosNo(), order.getId());
			return;
		}

		SingleMap params = new SingleMap();

		// 기존 테이블에서 주문 제거
		SvcTableExample example = new SvcTableExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(order.getId()); // order id로 테이블 검색
		List<SvcTable> oldTables = svcTableMapper.selectByExample(example);
		for (SvcTable oldTable : oldTables) {
			oldTable.setOrderId(null);
			params.put("tableId", oldTable.getId());
			params.put("orderId", null);
			posSalesTableOrderMapper.updateOrderInfoById(params);
		}

		final boolean isDelete = LAST_ST_DELETE.equals(order.getLastSt());

		// 새 테이블에 주문 설정	
		params.clear();
		params.put("tableId", order.getTableNo());
		params.put("orderId", isDelete ? null : order.getId());
		params.put("posNo", isDelete ? "" : order.getPosNo());
		posSalesTableOrderMapper.updateOrderInfoById(params);
	}

	
	/**
	 * 테이블 주문 정보 갱신
	 * 수정 : 
	 * @param tableId
	 * @param order
	 */
	private void updateTableOrderInfoKiosk(SvcOrder order) {

		// clerk/tab에서 발생한 미승인 주문건은 테이블에 바로 넣지 않고
		// 포스에서 주문 정보를 조회해 갈때 주문을 승인처리하고 테이블에 넣음.
		if (ORDER_ST_DISAPPROVAL.equals(order.getOrderSt())) {
			logger.debug("Disapproval order is not assigned to table. posNo={}, orderId={}", order.getPosNo(), order.getId());
			return;
		}

		SingleMap params = new SingleMap();

		// 기존 테이블에서 주문 제거
		SvcTableExample example = new SvcTableExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(order.getId()); // order id로 테이블 검색
		List<SvcTable> oldTables = svcTableMapper.selectByExample(example);
		for (SvcTable oldTable : oldTables) {
			oldTable.setOrderId(null);
			params.put("tableId", oldTable.getId());
			params.put("orderId", null);
			posSalesTableOrderMapper.updateOrderInfoById(params);
		}

		final boolean isDelete = LAST_ST_DELETE.equals(order.getLastSt());

		// 새 테이블에 주문 설정	
		params.clear();
		params.put("tableId", order.getTableNo());
		params.put("orderId", isDelete ? null : order.getId());
		params.put("posNo", isDelete ? "" : order.getPosNo());
		posSalesTableOrderMapper.updateOrderInfoById(params);
	}

	
	
	@Override
	public void updateTableOrderInfo(long orderId) {
		SvcOrder order = svcOrderMapper.selectByPrimaryKey(orderId);
		if (order != null) {
			updateTableOrderInfo(order);
		}
	}

	
	/**
	 * saveOrderHistories
	 * 
	 * @param svcOrderItemExtended
	 * @param orderNo
	 * @throws RequestResolveException
	 */
	private void saveOrderHistories(SvcOrderItemExtended svcOrderItemExtended, String orderNo) throws RequestResolveException {
		final List<SvcOrderItemHistory> svcOrderHistories = svcOrderItemExtended.getSvcOrderItemHistories();
		if (svcOrderHistories == null || svcOrderHistories.isEmpty()) {
			return;
		}

		final long orderId = svcOrderItemExtended.getOrderId();
		final long itemId = svcOrderItemExtended.getId();

		for (SvcOrderItemHistory history : svcOrderHistories) {

			history.setOrderId(orderId);
			history.setItemId(itemId);

			final long historyOrdinal = history.getOrdinal();

			SvcOrderItemHistoryExample svcOrderItemHistoryExample = new SvcOrderItemHistoryExample();
			svcOrderItemHistoryExample.createCriteria() // 조건
					.andOrderIdEqualTo(orderId) // 해당 주문
					.andItemIdEqualTo(itemId) // 해당 아이템
					.andOrdinalEqualTo(historyOrdinal); // 2016.08.13. edited by green, ordinal 값으로 Unique 가 보장되는 것으로 POS 와 이야기됨(2016.08.12. by 김도형선임)

			List<SvcOrderItemHistory> svcOrderItemHistories = svcOrderItemHistoryMapper.selectByExample(svcOrderItemHistoryExample);
			if (svcOrderItemHistories == null || svcOrderItemHistories.size() == 0) {
				// 0개이면 새로 insert
				svcOrderItemHistoryMapper.insertSelective(history);
			} else if (svcOrderItemHistories.size() == 1) {
				// 1개인 경우, 업데이트
				svcOrderItemHistoryMapper.updateByExampleSelective(history, svcOrderItemHistoryExample);
			} else {
				logger.error("[{}][{}] OrderNo: {}, Item: {}, History: {}, Too many histories", PosUtil.EPO_0006_CODE, PosUtil.EPO_0006_MSG, orderNo,
						itemId, historyOrdinal);
				throw new RequestResolveException(PosUtil.EPO_0006_MSG,
						"OrderNo: " + orderNo + ", Item: " + itemId + ", History: " + historyOrdinal + " Too many histories");
			}
		}
	}
	
	
	/**
	 * saveOrderHistoriesKiosk
	 * 수정 : 
	 * 
	 * @param svcOrderItemExtended
	 * @param orderNo
	 * @throws RequestResolveException
	 */
	private void saveOrderHistoriesKiosk(SvcOrderItemExtended svcOrderItemExtended, String orderNo) throws RequestResolveException {
		final List<SvcOrderItemHistory> svcOrderHistories = svcOrderItemExtended.getSvcOrderItemHistories();
		if (svcOrderHistories == null || svcOrderHistories.isEmpty()) {
			return;
		}

		final long orderId = svcOrderItemExtended.getOrderId();
		final long itemId = svcOrderItemExtended.getId();

		for (SvcOrderItemHistory history : svcOrderHistories) {

			history.setOrderId(orderId);
			history.setItemId(itemId);

			final long historyOrdinal = history.getOrdinal();

			SvcOrderItemHistoryExample svcOrderItemHistoryExample = new SvcOrderItemHistoryExample();
			svcOrderItemHistoryExample.createCriteria() // 조건
					.andOrderIdEqualTo(orderId) // 해당 주문
					.andItemIdEqualTo(itemId) // 해당 아이템
					.andOrdinalEqualTo(historyOrdinal); // 2016.08.13. edited by green, ordinal 값으로 Unique 가 보장되는 것으로 POS 와 이야기됨(2016.08.12. by 김도형선임)

			List<SvcOrderItemHistory> svcOrderItemHistories = svcOrderItemHistoryMapper.selectByExample(svcOrderItemHistoryExample);
			if (svcOrderItemHistories == null || svcOrderItemHistories.size() == 0) {
				// 0개이면 새로 insert
				svcOrderItemHistoryMapper.insertSelective(history);
			} else if (svcOrderItemHistories.size() == 1) {
				// 1개인 경우, 업데이트
				svcOrderItemHistoryMapper.updateByExampleSelective(history, svcOrderItemHistoryExample);
			} else {
				logger.error("[{}][{}] OrderNo: {}, Item: {}, History: {}, Too many histories", PosUtil.EPO_0006_CODE, PosUtil.EPO_0006_MSG, orderNo,
						itemId, historyOrdinal);
				throw new RequestResolveException(PosUtil.EPO_0006_MSG,
						"OrderNo: " + orderNo + ", Item: " + itemId + ", History: " + historyOrdinal + " Too many histories");
			}
		}
	}
	
	
	/**
	 * 상품 옵션 저장
	 * 
	 * @param orderNo
	 * 
	 * @param svcOrderItemOpts
	 * @throws RequestResolveException
	 *             EPO0007 : ordinal이 중복됨
	 */
	private void saveOrderItemOpts(SvcOrderItemExtended orderItem, String orderNo) throws RequestResolveException {
		logger.debug("saveOrderItemOpts size=" + orderItem.getSvcOrderItemOpts().size());

		// 기존 저장된 옵션 id 조회
		final List<Long> oldOptIds = getSvcOrderItemOptionIdList(orderItem);
		final Set<Long> ordinalSet = new HashSet<>();

		// 최대한 기존 레코드를 활용하여 저장 처리
		for (SvcOrderItemOpt itemOpt : orderItem.getSvcOrderItemOpts()) {

			if (itemOpt.getOrdinal() == null || ordinalSet.contains(itemOpt.getOrdinal())) {
				throw new RequestResolveException(PosUtil.EPO_0007_CODE, "OrderNo: " + orderNo + ", Item: " + itemOpt.getItemId() + ", Option: "
						+ itemOpt.getOrdinal() + " Invalid option ordinal value.");
			}

			// 기존 레코드의 id가 있으면 설정
			itemOpt.setId(oldOptIds.size() > 0 ? oldOptIds.remove(0) : null);

			// 신규 등록인 경우는 order id와 item id가 없으니 항상 설정
			itemOpt.setOrderId(orderItem.getOrderId());
			itemOpt.setItemId(orderItem.getId());

			// created, updated는 db에서 자동 입력 처리
			itemOpt.setUpdated(null);
			itemOpt.setCreated(null);

			if (itemOpt.getId() != null) {
				// 기존 레코드에 갱신 하여 저장
				svcOrderItemOptMapper.updateByPrimaryKeySelective(itemOpt);
			} else {
				// 신규 저장
				svcOrderItemOptMapper.insertSelective(itemOpt);
			}
			ordinalSet.add(itemOpt.getOrderId());
		}

		// 기존 저장된 옵션중 제가된 옵션은 삭제
		// 저장을 요청한 주문 정보에 없으면 삭제된 것으로 간주
		deleteOrderItemOptByOptIds(oldOptIds);
	}

	
	/**
	 * 상품 옵션 저장
	 * 
	 * @param orderNo
	 * 
	 * @param svcOrderItemOpts
	 * @throws RequestResolveException
	 *             EPO0007 : ordinal이 중복됨
	 */
	private void saveOrderItemOptsKiosk(SvcOrderItemExtended orderItem, String orderNo) throws RequestResolveException {
		logger.debug("saveOrderItemOpts size=" + orderItem.getSvcOrderItemOpts().size());

		// 기존 저장된 옵션 id 조회
		final List<Long> oldOptIds = getSvcOrderItemOptionIdList(orderItem);
		final Set<Long> ordinalSet = new HashSet<>();

		// 최대한 기존 레코드를 활용하여 저장 처리
		for (SvcOrderItemOpt itemOpt : orderItem.getSvcOrderItemOpts()) {

			if (itemOpt.getOrdinal() == null || ordinalSet.contains(itemOpt.getOrdinal())) {
				throw new RequestResolveException(PosUtil.EPO_0007_CODE, "OrderNo: " + orderNo + ", Item: " + itemOpt.getItemId() + ", Option: "
						+ itemOpt.getOrdinal() + " Invalid option ordinal value.");
			}

			// 기존 레코드의 id가 있으면 설정
			itemOpt.setId(oldOptIds.size() > 0 ? oldOptIds.remove(0) : null);

			// 신규 등록인 경우는 order id와 item id가 없으니 항상 설정
			itemOpt.setOrderId(orderItem.getOrderId());
			itemOpt.setItemId(orderItem.getId());

			// created, updated는 db에서 자동 입력 처리
			itemOpt.setUpdated(null);
			itemOpt.setCreated(null);

			if (itemOpt.getId() != null) {
				// 기존 레코드에 갱신 하여 저장
				svcOrderItemOptMapper.updateByPrimaryKeySelective(itemOpt);
			} else {
				// 신규 저장
				svcOrderItemOptMapper.insertSelective(itemOpt);
			}
			ordinalSet.add(itemOpt.getOrderId());
		}

		// 기존 저장된 옵션중 제가된 옵션은 삭제
		// 저장을 요청한 주문 정보에 없으면 삭제된 것으로 간주
		deleteOrderItemOptByOptIds(oldOptIds);
	}
	
	/**
	 * 주문 내용을 주방 프린터에 저장
	 * 
	 * @param orderNo
	 * 
	 * @param svcOrderItemOpts
	 * @throws RequestResolveException
	 *             EPO0007 : ordinal이 중복됨
	 */
	private int  saveOrderKitchenPrinter(SvcOrderItemExtended orderItem, SvcOrderExtended order, List<SvcItem> itemList, List<SvcKitchenPrinter> svcKitchenPrintList, String defaultPrinterNo, int ordinal) throws RequestResolveException {
		logger.debug("saveOrderKitchenPrinter size=" + orderItem.getSvcOrderItemOpts().size());
		SvcKitchenPrinter svc = new SvcKitchenPrinter();
		
		svc.setOrderNo(order.getOrderNo());
		svc.setReceiptNo(order.getReceiptNo());
		svc.setBrandId(order.getBrandId());
		svc.setStoreId(order.getStoreId());
		svc.setOrderTmLocal(order.getOrderTmLocal());
		svc.setPosNo(order.getPosNo());
		svc.setOrdinal(++ordinal);
		
		Optional<SvcItem> optional =  itemList.stream().filter( i -> i.getId().equals(orderItem.getItemId())).findFirst();
		
		if(optional.isPresent()) {
			SvcItem item = new SvcItem();
			item  = optional.get();
			
			if(item.getPrinterNo() == null || item.getPrinterNo().equals("YES")) {
				svc.setPrinterNo(defaultPrinterNo);
			} else {
				svc.setPrinterNo(item.getPrinterNo());
			}
		} else {
			svc.setPrinterNo(defaultPrinterNo);
		}
		
		svc.setCount(orderItem.getCount());
		svc.setItemNm(orderItem.getItemNm());
		svcKitchenPrintList.add(svc);
		
		if(orderItem.getSvcOrderItemOpts().size() > 0) {
			for (SvcOrderItemOpt itemOpt : orderItem.getSvcOrderItemOpts()) {
				SvcKitchenPrinter optSvc = new SvcKitchenPrinter();
				optSvc.setOrderNo(svc.getOrderNo());
				optSvc.setReceiptNo(svc.getReceiptNo());
				optSvc.setBrandId(svc.getBrandId());
				optSvc.setStoreId(svc.getStoreId());
				optSvc.setPrinterNo(svc.getPrinterNo());
				optSvc.setOrderTmLocal(svc.getOrderTmLocal());
				optSvc.setItemNm(itemOpt.getOptNm() + "(" + itemOpt.getOptDtlNm() + ")");
				optSvc.setOrdinal(++ordinal);
				optSvc.setIsOption(true);
				optSvc.setPosNo(svc.getPosNo());
				svcKitchenPrintList.add(optSvc);
			}
		}
		
		return ordinal;
	}

	
	
	/**
	 * 옵션 정보 삭제
	 * 
	 * @param oldOptIds
	 *            삭제할 옵션 id
	 */
	private void deleteOrderItemOptByOptIds(List<Long> oldOptIds) {
		if (oldOptIds.size() == 0) {
			return;
		}
		SvcOrderItemOptExample example = new SvcOrderItemOptExample();
		example.createCriteria() // 조건
				.andIdIn(oldOptIds);
		svcOrderItemOptMapper.deleteByExample(example);
	}

	/**
	 * order item의 옵션 id 목록
	 * 
	 * @param orderItem
	 * @return
	 */
	private List<Long> getSvcOrderItemOptionIdList(SvcOrderItemExtended orderItem) {
		List<SvcOrderItemOpt> oldOpts = getSvcOrderItemOptionListByOrderItemId(orderItem.getId());
		List<Long> oldOptIds = new ArrayList<>(oldOpts.size());
		for (SvcOrderItemOpt itemOpt : oldOpts) {
			oldOptIds.add(itemOpt.getId());
		}
		return oldOptIds;
	}

	/**
	 * order item의 옵션 조회
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
	 * 결재 내역 저장
	 * 
	 * @param svcOrderExtended
	 * @throws RequestResolveException
	 */
	private void saveOrderPays(SvcOrderExtended order) throws RequestResolveException {

		if (order.getSvcOrderPays() == null) {
			return;
		}

		final Set<Long> usedOrdinalSet   = new HashSet<>();
		final SvcOrderPayExample example = new SvcOrderPayExample();

		// 최대한 기존 레코드를 활용하여 저장 처리
		for (SvcOrderPay item : order.getSvcOrderPays()) {

			// 없거나, 중복이면 에러 처리
			if (item.getOrdinal() == null || usedOrdinalSet.contains(item.getOrdinal())) {
				throw new RequestResolveException(PosUtil.EPO_0007_CODE,
						"OrderNo: " + order.getOrderNo() + ", Ordinal: " + item.getOrdinal() + " Invalid oridnal value. empty or duplicate.");
			}

			// 신규 등록인 경우는 order id가 없으니 항상 설정
			item.setOrderId(order.getId());

			// created, updated는 db에서 자동 입력 처리
			item.setUpdated(null);
			item.setCreated(null);

			// ID가 없으면 ordinal로 id를 조회하여 설정
			// 조회 결과가 id가 null 이면 신규 항목
			if (item.getId() == null || item.getId() == 0) {
				item.setId(getOldPayIdByOrdinal(example, item));
			}

			// 등록 혹은 갱신
			if (item.getId() == null || item.getId() == 0) {
				// 신규 등록
				svcOrderPayMapper.insertSelective(item);
			} else {
				// 갱신 처리				
				svcOrderPayMapper.updateByPrimaryKeySelective(item);
			}

			usedOrdinalSet.add(item.getOrdinal());
		} // for order pays

	}

	
	/**
	 * 결재 내역 저장
	 * 수정 : 김성워
	 * @param svcOrderExtended
	 * @throws RequestResolveException
	 */
	private void saveOrderPaysKiosk(SvcOrderExtended order) throws RequestResolveException {

		if (order.getSvcOrderPays() == null) {
			return;
		}

		final Set<Long> usedOrdinalSet   = new HashSet<>();
		final SvcOrderPayExample example = new SvcOrderPayExample();

		// 최대한 기존 레코드를 활용하여 저장 처리
		for (SvcOrderPay item : order.getSvcOrderPays()) {

			// 없거나, 중복이면 에러 처리
			if (item.getOrdinal() == null || usedOrdinalSet.contains(item.getOrdinal())) {
				throw new RequestResolveException(PosUtil.EPO_0007_CODE,
						"OrderNo: " + order.getOrderNo() + ", Ordinal: " + item.getOrdinal() + " Invalid oridnal value. empty or duplicate.");
			}

			// 신규 등록인 경우는 order id가 없으니 항상 설정
			item.setOrderId(order.getId());

			// created, updated는 db에서 자동 입력 처리
			item.setUpdated(null);
			item.setCreated(null);

			// ID가 없으면 ordinal로 id를 조회하여 설정
			// 조회 결과가 id가 null 이면 신규 항목
			if (item.getId() == null || item.getId() == 0) {
				item.setId(getOldPayIdByOrdinal(example, item));
			}

			// 등록 혹은 갱신
			if (item.getId() == null || item.getId() == 0) {
				// 신규 등록
				svcOrderPayMapper.insertSelective(item);
			} else {
				// 갱신 처리				
				svcOrderPayMapper.updateByPrimaryKeySelective(item);
			}

			usedOrdinalSet.add(item.getOrdinal());
		} // for order pays

	}
	
	
	/**
	 * orderId와 ordinal로 결제 내역을 조회한다.
	 * 포스에서 전달하는 정보는 두 조합을 pk로 사용
	 * 
	 * @param example
	 * @param pay
	 *            order id와 ordinal이 포함된 order pay
	 * @return
	 */
	private Long getOldPayIdByOrdinal(SvcOrderPayExample example, SvcOrderPay pay) {
		example.clear();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(pay.getOrderId()) // order id
				.andOrdinalEqualTo(pay.getOrdinal()); // ordinal
		List<SvcOrderPay> oldPays = svcOrderPayMapper.selectByExample(example);
		return oldPays.size() > 0 ? oldPays.get(0).getId() : null;
	}

	/**
	 * 주문 상태가 변경되었는지 확인한다.
	 * 이전 주문이 없으면 변경된 것으로 처리
	 * 
	 * @param newOrder
	 * @param oldOrder
	 * @return
	 */
	private boolean isOrderStatusChanged(SvcOrder newOrder, SvcOrder oldOrder) {
		// oldOrder 가 없으면 신규 주문
		return oldOrder == null || !StringUtils.equals(newOrder.getOrderSt(), oldOrder.getOrderSt());
	}

	/**
	 * order id로 주문 항목 조회
	 * 
	 * @param orderId
	 * @return 추가 정보를 담을 수 있는 주문 항목 목록
	 */
	private List<SvcOrderItemExtended> getSvcOrderItemExtendedListByOrderId(long orderId) {
		SvcOrderItemExample example = new SvcOrderItemExample();
		example.createCriteria() // 
				.andLastStNotEqualTo(LAST_ST_DELETE) // 삭제된 항목은 제외
				.andOrderIdEqualTo(orderId);
		List<SvcOrderItem> items = svcOrderItemMapper.selectByExample(example);
		List<SvcOrderItemExtended> result = new ArrayList<SvcOrderItemExtended>(items.size());
		for (SvcOrderItem item : items) {
			result.add(objectMapper.convertValue(item, SvcOrderItemExtended.class));
		}
		return result;
	}

	@Override
	public void cancelOrder(Long orderId) {
		SvcOrder record = new SvcOrder();
		record.setId(orderId);
		record.setOrderSt(ORDER_ST_CANCEL);
		svcOrderMapper.updateByPrimaryKeySelective(record);
	}



	@Override
	public List<SvcKitchenPrinter> getOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter) {
		// TODO Auto-generated method stub
		return svcKitchenPrinterMapper.selectList(svcKitchenPrinter);
	}



	@Override
	public void setOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter) {
		svcKitchenPrinter.setIsPrint(true);
		
		logger.debug("svcKitchenPrinter 확인 >>" + svcKitchenPrinter.getIsPrint());
		
		svcKitchenPrinterMapper.update(svcKitchenPrinter);
		
	}
	

	private String generateDefaultPrinterNo(String defaultPrinterNo, SvcOrderExtended newOrder, List<SvcItem> afterItemList) {
		logger.debug("generateDefaultPrinterNo::");
		List<SvcOrderItemExtended> svcOrderItemExtendeds = newOrder.getSvcOrderItems();
		List<SvcItem> itemList = svcItemMapper.selectByOrderItemList(svcOrderItemExtendeds);
		if(itemList.isEmpty()) {
			List<PosPrinterInfo> posPrinterInfoList = posStorePrinterMapper.selectListWithLong(newOrder.getStoreId());
			defaultPrinterNo = (!posPrinterInfoList.isEmpty())? posPrinterInfoList.get(0).getCdPrinter() : "";
		}else {
			defaultPrinterNo = itemList.get(0).getPrinterNo();
			afterItemList.addAll(itemList);
		}
		return defaultPrinterNo;
	}



	@Override
	public void deleteKitchenPrinterAtOrderCancel(HashMap<String, Object> cancelOrder) {
		if(cancelOrder.get("orderNo") != null) {
			svcKitchenPrinterMapper.deleteKitchenPrinterAtOrderCancel(cancelOrder);
		}
	}
}
