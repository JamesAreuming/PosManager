package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreWeekTimeSalesMapper {

	SingleMap selectTimeTotal(SingleMap param);

	List<SingleMap> selectTimeList(SingleMap param);

	List<SingleMap> selectWeekList(SingleMap param);

}