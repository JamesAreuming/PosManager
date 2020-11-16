package com.jc.pico.utils.bean;

import java.util.List;

import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesDiscount;
import com.jc.pico.bean.SvcSalesPay;

/**
 * 매출을 확장한 Bean
 * @author green
 *
 */
public class SvcSalesExtended extends SvcSales {
	/**
	 * 매출 상품 리스트
	 */
	private List<SvcSalesItemExtended> svcSalesItems = null;

	/**
	 * 매출 리스트
	 */
	private List<SvcSalesPay> svcSalesPays = null;

	/**
	 * 매출 할인 리스트
	 */
	private List<SvcSalesDiscount> svcSalesDiscounts = null;

	public List<SvcSalesItemExtended> getSvcSalesItems() {
		return svcSalesItems;
	}

	public void setSvcSalesItems(List<SvcSalesItemExtended> svcSalesItems) {
		this.svcSalesItems = svcSalesItems;
	}

	public List<SvcSalesPay> getSvcSalesPays() {
		return svcSalesPays;
	}

	public void setSvcSalesPays(List<SvcSalesPay> svcSalesPays) {
		this.svcSalesPays = svcSalesPays;
	}

	public List<SvcSalesDiscount> getSvcSalesDiscounts() {
		return svcSalesDiscounts;
	}

	public void setSvcSalesDiscounts(List<SvcSalesDiscount> svcSalesDiscounts) {
		this.svcSalesDiscounts = svcSalesDiscounts;
	}

}
