package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreStoreStampMapper {

	SingleMap selectSummaryTotal(SingleMap param);

	SingleMap selectSummaryInfo(SingleMap param);
	
	List<SingleMap> selectSummaryHourlyIssuedList(SingleMap param);

	List<SingleMap> selectDailyList(SingleMap param);

	List<SingleMap> selectWeeklyList(SingleMap param);

	List<SingleMap> selectMonthlyList(SingleMap param);



}
