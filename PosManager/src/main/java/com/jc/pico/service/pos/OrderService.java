package com.jc.pico.service.pos;

import java.util.HashMap;
import java.util.List;

import com.jc.pico.bean.SvcKitchenPrinter;
import com.jc.pico.bean.SvnStoreTemp;
import com.jc.pico.utils.bean.SvcOrderExtended;

public interface OrderService {

	public SvcOrderExtended saveOrder(SvcOrderExtended svcOrderExtended) throws Throwable;

	
	// 수정 : 키오스크 주문 정보
	public SvcOrderExtended saveOrderKiosk(SvcOrderExtended svcOrderExtended) throws Throwable;

	
	/**
	 * 주문을 테이블에 할당하거나 해제한다.
	 * 
	 * @param order
	 */
	public void updateTableOrderInfo(long orderId);


	public List<SvcKitchenPrinter> getOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter);


	public void setOrderKitchenPrinter(SvcKitchenPrinter svcKitchenPrinter);


	public void deleteKitchenPrinterAtOrderCancel(HashMap<String, Object> cancelOrder);

}
