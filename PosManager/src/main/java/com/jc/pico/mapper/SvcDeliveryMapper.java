package com.jc.pico.mapper;

import com.jc.pico.bean.SvcDelivery;

public interface SvcDeliveryMapper{
	int insertOrderDeliveryInfo(SvcDelivery orderDeliveryInfo);
	
	SvcDelivery selectDeliveryCustomerInfo(String orderNo);

	int updateDeliveryCustomerInfo();
}
	