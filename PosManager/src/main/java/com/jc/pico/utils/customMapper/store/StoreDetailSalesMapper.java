package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreDetailSalesMapper {

	SingleMap selectDetailTotal(SingleMap param);

	List<SingleMap> selectDetailList(SingleMap param);

	SingleMap selectSalesDetail(SingleMap param);

	List<SingleMap> selectSalesDetailPayMethodList(SingleMap param);

	List<SingleMap> selectSalesItemList(SingleMap param);

	SingleMap selectSalesItemTotal(SingleMap param);

}
