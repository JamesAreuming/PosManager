package com.jc.pico.utils.customMapper.clerk;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface ClerkOrderMapper {

	SingleMap selectOrderSimpleByOrderId(SingleMap param);

	List<SingleMap> selectOrderItemSimpleListByOrderId(SingleMap param);

	String selectOrderStById(long orderId);

}
