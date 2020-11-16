package com.jc.pico.service.pos.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcKitchenPrinter;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.service.pos.OrderInternalService;
import com.jc.pico.service.pos.OrderService;
import com.jc.pico.service.store.StoreNotificationService;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderInfoMapper;

/**
 * 주문 저장
 * 
 * 결제 처리
 * 
 * 알림 처리
 * 
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 주문 경로 : 앱
	 */
	public static final String ORDER_PATH_APP = "606002";

	/**
	 * 주문 상태 : 승인
	 */
	public static final String ORDER_ST_APPROVAL = "607002";

	/**
	 * 주문 상태 : 취소 (승인 취소)
	 */
	public static final String ORDER_ST_CANCEL = "607003";

	/**
	 * 주문 상태 : 완료
	 */
	public static final String ORDER_ST_COMPLETE = "607005";

	@Autowired
	private OrderInternalService orderInternalService;

	@Autowired
	private AppNotificationService appNotificationService;

	@Autowired
	private StoreNotificationService storeNotificationService;

	@Autowired
	private PosSalesOrderInfoMapper posSalesOrderInfoMapper;

	
	@Override
	public SvcOrderExtended saveOrder(SvcOrderExtended newOrder) throws Throwable {

		// 동시에 포스에서 주문 번호가 발생하 경우 동시 입력되는 경우가 있어 락 설정 함
		
		if (!posSalesOrderInfoMapper.lockSaveOrder(newOrder.getStoreId(), newOrder.getOrderNo())) {
			throw new LockedException("Can not get order lock. storeId=" + newOrder.getStoreId() + ", openDt=" + newOrder.getOpenDt() + ", orderNo="
					+ newOrder.getOrderNo());
		}		

		final SvcOrderExtended oldOrder;

		try {

			oldOrder = orderInternalService.getOrderExtended(newOrder.getId(), newOrder.getStoreId(), newOrder.getOpenDt(), newOrder.getOrderNo());

			// 트랜잭션 내에서 처리
			newOrder = orderInternalService.saveOrder(newOrder, oldOrder);

		} finally {
			posSalesOrderInfoMapper.unlockSaveOrder(newOrder.getStoreId(), newOrder.getOrderNo());			
		}

		// 주문 저장에 따른 변경 사항 알림
		notifyOrderChangedToClient(newOrder, oldOrder);

		return newOrder;
	}

	
	/**
	 * 수정 : 
	 * 
	 */
	@Override
	public SvcOrderExtended saveOrderKiosk(SvcOrderExtended newOrder) throws Throwable {

		// 동시에 포스에서 주문 번호가 발생하 경우 동시 입력되는 경우가 있어 락 설정 함
		// 수정 : 
		/*
		if (!posSalesOrderInfoMapper.lockSaveOrder(newOrder.getStoreId(), newOrder.getOrderNo())) {
			throw new LockedException("Can not get order lock. storeId=" + newOrder.getStoreId() + ", openDt=" + newOrder.getOpenDt() + ", orderNo="
					+ newOrder.getOrderNo());
		}
		*/	
		// 수정 : 

		final SvcOrderExtended oldOrder;

		try {
			// 수정 : 
			//oldOrder = orderInternalService.getOrderExtended(newOrder.getId(), newOrder.getStoreId(), newOrder.getOpenDt(), newOrder.getOrderNo());
			oldOrder = null;
			// 수정 : 
			
			// 수정 : 
			// 트랜잭션 내에서 처리
			//newOrder = orderInternalService.saveOrder(newOrder, oldOrder);
			newOrder = orderInternalService.saveOrderKiosk(newOrder, oldOrder);
			// 수정 : 
		} 
		finally {
			// 수정 : 
			//posSalesOrderInfoMapper.unlockSaveOrder(newOrder.getStoreId(), newOrder.getOrderNo());	
			// 수정 : 
		}

		// 주문 저장에 따른 변경 사항 알림
		// 포스로 push 전송
		// 수정 : 
		//notifyOrderChangedToClient(newOrder, oldOrder);
		// 수정 : 

		return newOrder;
	}
	
	
	
	
	/**
	 * 주문 변경에 따른 알림 전송
	 * 
	 * @param newOrder
	 * @param oldOrder
	 */
	public void notifyOrderChangedToClient(SvcOrderExtended newOrder, SvcOrderExtended oldOrder) {

		final boolean isOrderStatusChanged = isOrderStatusChanged(newOrder, oldOrder);

		// 주문 상태가 변경되었으면 앱 사용자에게 알림
		try {
			if (isOrderStatusChanged) {
				appNotificationService.notifyOrderStatusChanged(newOrder);
			}
		} catch (Exception e) {
			// 알림 전송에 실패해도 주문 저장은 계속 처리
			logger.error("Failed notify order status changed to user mobile. orderId=" + newOrder.getId(), e);
		}

		// 스토어 관리자에게 알림
		try {
			storeNotificationService.notifyOrderChanged(newOrder, oldOrder);
		} catch (Exception e) {
			// 알림 전송에 실패해도 주문 저장은 계속 처리
			logger.error("Failed notify order status changed to store manager. orderId=" + newOrder.getId(), e);
		}

	}

	/**
	 * 주문 상태가 변경되었는지 확인한다.
	 * 이전 주문이 없으면 변경된 것으로 처리
	 * 
	 * @param newOrder
	 *            oldOrder
	 * @return
	 */
	private boolean isOrderStatusChanged(SvcOrder newOrder, SvcOrder oldOrder) {
		// oldOrder 가 없으면 신규 주문
		return oldOrder == null || !StringUtils.equals(newOrder.getOrderSt(), oldOrder.getOrderSt());
	}

	@Override
	public void updateTableOrderInfo(long orderId) {
		orderInternalService.updateTableOrderInfo(orderId);
	}


	@Override
	public List<SvcKitchenPrinter> getOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter) {
		return orderInternalService.getOrderKitchenPrinter(svcKitchenPrinter);
	}


	@Override
	public void setOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter) {
		orderInternalService.setOrderKitchenPrinter(svcKitchenPrinter);
	}


	@Override
	public void deleteKitchenPrinterAtOrderCancel(HashMap<String, Object> cancelOrder) {
		orderInternalService.deleteKitchenPrinterAtOrderCancel(cancelOrder);
		
	}
}
