package com.jc.pico.service.pos;

import com.jc.pico.bean.SvcOrder;

public interface PaymentGatewayService {

	/**
	 * 결제 처리
	 * 
	 * @return
	 * 
	 */
	boolean purchase(SvcOrder order);

	/**
	 * 주문으로 부터 결제 취소 처리
	 * 
	 * @param order
	 */
	boolean refund(SvcOrder order);

}
