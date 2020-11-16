package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreEventLogMapper {

	List<SingleMap> selectStoreEventList(SingleMap param);
	
}
