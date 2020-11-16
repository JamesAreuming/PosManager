package com.jc.pico.utils.bean;

import com.jc.pico.bean.SvcOrderDiscount;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcOrderItemHistory;
import com.jc.pico.bean.SvcOrderItemOpt;

import java.util.List;

/**
 * 주문 상품에 대한 확장 Bean
 * @author green
 *
 */
public class SvcOrderItemExtended extends SvcOrderItem {
	/**
	 * 주문 상품의 옵션 리스트
	 */
	private List<SvcOrderItemOpt> svcOrderItemOpts = null;

	/**
	 * 주문 상품의 할인 리스트
	 */
	private List<SvcOrderDiscount> svcOrderDiscounts = null;

	/**
	 * 주문 상품의 주문변경 리스트
	 */
	private List<SvcOrderItemHistory> svcOrderHistories = null;

	/**
	 * 주문상품 옵션리스트 Getter
	 * @return 주문상품 옵션리스트
	 */
	public List<SvcOrderItemOpt> getSvcOrderItemOpts() {
		return svcOrderItemOpts;
	}

	/**
	 * 주문상품 옵션리스트 Setter
	 * @param svcOrderItemOpts 주문상품 옵션리스트
	 */
	public void setSvcOrderItemOpts(List<SvcOrderItemOpt> svcOrderItemOpts) {
		this.svcOrderItemOpts = svcOrderItemOpts;
	}

	/**
	 * 주문상품 할인리스트 Getter
	 * @return
	 */
	public List<SvcOrderDiscount> getSvcOrderDiscounts() {
		return svcOrderDiscounts;
	}

	/**
	 * 주문상품 할인리스트 Setter
	 * @param svcOrderDiscounts 주문상품 할인리스트
	 */
	public void setSvcOrderDiscounts(List<SvcOrderDiscount> svcOrderDiscounts) {
		this.svcOrderDiscounts = svcOrderDiscounts;
	}

	/**
	 * 주문 상품의 주문변경 리스트 Getter
	 * @return 주문 상품의 주문변경 리스트
	 */
	public List<SvcOrderItemHistory> getSvcOrderItemHistories() {
		return svcOrderHistories;
	}

	/**
	 * 주문 상품의 주문변경 리스트 Setter
	 * @param svcOrderItemHistories 주문 상품의 주문변경 리스트
	 */
	public void setSvcOrderHistories(List<SvcOrderItemHistory> svcOrderItemHistories) {
		this.svcOrderHistories = svcOrderItemHistories;
	}

}
