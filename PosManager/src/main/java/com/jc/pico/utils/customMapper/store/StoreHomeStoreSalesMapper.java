package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreHomeStoreSalesMapper {

	SingleMap selectTodaySalesSummary(SingleMap param);

	List<SingleMap> selectLastDaysSales(SingleMap param);

	SingleMap selectTodaySalesBestItem(SingleMap param);

	List<SingleMap> selectTodayTimeSales(SingleMap param);

}
