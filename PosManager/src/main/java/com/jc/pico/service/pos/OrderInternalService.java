package com.jc.pico.service.pos;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jc.pico.bean.SvcKitchenPrinter;
import com.jc.pico.bean.SvnStoreTemp;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.SvcOrderExtended;

/**
 * 주문정보 저장/수정
 * 
 * @author green
 *
 */
public interface OrderInternalService {
	/**
	 * 주문정보 저장 (키값이 있으면 수정, 없으면 신규)
	 * 
	 * @param svcOrderExtended
	 *            주문
	 * @param oldOrder
	 * @return 주문 정보
	 * @throws 오류
	 */
	SvcOrderExtended saveOrder(SvcOrderExtended newOrder, SvcOrderExtended oldOrder) throws Throwable;

	
	/**
	 * 주문정보 저장 (키값이 있으면 수정, 없으면 신규)
	 * 추가 : 
	 * @param svcOrderExtended
	 *            주문
	 * @param oldOrder
	 * @return 주문 정보
	 * @throws 오류
	 */
	SvcOrderExtended saveOrderKiosk(SvcOrderExtended newOrder, SvcOrderExtended oldOrder) throws Throwable;

	
	/**
	 * 포스PK로 주문 상세 정보 조회
	 * 
	 * @param storeId
	 * @param openDt
	 * @param orderNo
	 * @return
	 * @throws RequestResolveException
	 */
	SvcOrderExtended getOrderExtended(Long storeId, Date openDt, String orderNo) throws RequestResolveException;

	/**
	 * 주문 ID 혹은 POS 주문 PK로 주문 상세 정보 조회.
	 * 주문 ID를 우선하여 조회. 주문 ID가 없으면 pos pk로 조회
	 * 
	 * @param orderId
	 *            주문 ID
	 * @param storeId
	 *            상점 번호
	 * @param openDt
	 *            개점일
	 * @param orderNo
	 *            주문 번호
	 * 
	 * @return 상세 주문 정보
	 */
	SvcOrderExtended getOrderExtended(Long orderId, Long storeId, Date openDt, String orderNo) throws RequestResolveException;

	/**
	 * 주문을 승인 취소 한다
	 * 
	 * @param id
	 */
	void cancelOrder(Long id);

	/**
	 * 주문을 테이블에 할당하거나 해제한다.
	 * 주문 상태에 따라 동작 한다.
	 * 
	 * @param order
	 */
	void updateTableOrderInfo(long orderId);


	List<SvcKitchenPrinter> getOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter);


	void setOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter);


	void deleteKitchenPrinterAtOrderCancel(HashMap<String, Object> cancelOrder);
}
