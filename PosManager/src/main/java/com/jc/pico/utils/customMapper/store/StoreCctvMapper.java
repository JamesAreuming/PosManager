package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreCctvMapper {

	SingleMap selectStoreCctvInfo(SingleMap param);

	List<SingleMap> selectStoreCctvList(SingleMap param);

}
