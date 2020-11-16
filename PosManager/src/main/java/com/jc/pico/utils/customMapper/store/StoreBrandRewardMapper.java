package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreBrandRewardMapper {

	SingleMap selectSummaryTotal(SingleMap param);

	List<SingleMap> selectSummaryList(SingleMap param);

	List<SingleMap> selectDailyList(SingleMap param);

	List<SingleMap> selectWeeklyList(SingleMap param);

	List<SingleMap> selectMonthlyList(SingleMap param);

}
