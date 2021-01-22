package com.jc.pico.utils.bean;

import java.util.List;

import com.jc.pico.bean.SvcDelivery;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderPay;

/**
 * 주문을 확장한 Bean
 * 
 * @author green
 *
 */
public class SvcOrderExtended extends SvcOrder {
	
	private SvcDelivery svcOrderDelivery;
	
	/**
	 * 주문 상품 리스트
	 */
	private List<SvcOrderItemExtended> svcOrderItems = null;

	/**
	 * 주문 결제 내역
	 */
	private List<SvcOrderPay> svcOrderPays;
	
	private boolean isUsePrinter;

	/**
	 * 주문 상품 리스트 Getter
	 * 
	 * @return 주문 상품 리스트
	 */
	public List<SvcOrderItemExtended> getSvcOrderItems() {
		return svcOrderItems;
	}
	

	/**
	 * 주문 상품 리스트 Setter
	 * 
	 * @param svcOrderItems
	 *            주문 상품 리스트
	 */
	



	
	public void setSvcOrderItems(List<SvcOrderItemExtended> svcOrderItems) {
		this.svcOrderItems = svcOrderItems;
	}



	public SvcDelivery getSvcOrderDelivery() {
		return svcOrderDelivery;
	}


	public void setSvcOrderDelivery(SvcDelivery svcOrderDelivery) {
		this.svcOrderDelivery = svcOrderDelivery;
	}


	public List<SvcOrderPay> getSvcOrderPays() {
		return svcOrderPays;
	}

	public void setSvcOrderPays(List<SvcOrderPay> svcOrderPays) {
		this.svcOrderPays = svcOrderPays;
	}

	public boolean getIsUsePrinter() {
		return isUsePrinter;
	}

	public void setIsUsePrinter(boolean isUsePrinter) {
		this.isUsePrinter = isUsePrinter;
	}

}
