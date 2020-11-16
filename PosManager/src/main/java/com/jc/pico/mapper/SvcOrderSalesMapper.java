package com.jc.pico.mapper;

import com.jc.pico.utils.bean.SingleMap;

public interface SvcOrderSalesMapper {
	SingleMap selectOrder(SingleMap param);

	void insertOrderToSales(SingleMap order);
	void insertOrderItemToSalesItem(SingleMap order);
	void insertOrderItemOptionToSalesItemOption(SingleMap order);

	void insertOrderPay(SingleMap map);
}