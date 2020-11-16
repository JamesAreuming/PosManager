package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StorePaymentSalesMapper {

	SingleMap selectPaymentTotal(SingleMap param);

	List<SingleMap> selectPaymentList(SingleMap param);

}