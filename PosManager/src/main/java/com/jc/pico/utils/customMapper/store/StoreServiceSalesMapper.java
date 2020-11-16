package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreServiceSalesMapper {

	SingleMap selectServiceTotal(SingleMap param);

	List<SingleMap> selectOfflineServiceList(SingleMap param);

	List<SingleMap> selectOnlineServiceList(SingleMap param);

	SingleMap selectOfflineServiceTotal(SingleMap param);

	SingleMap selectOnlineServiceTotal(SingleMap param);

}