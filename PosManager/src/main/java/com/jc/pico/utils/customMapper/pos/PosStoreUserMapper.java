package com.jc.pico.utils.customMapper.pos;

import java.util.Map;

import com.jc.pico.utils.bean.SingleMap;

public interface PosStoreUserMapper {

	int updateSales(Map<String, Object> param);

	int insertSales(SingleMap param);

}
