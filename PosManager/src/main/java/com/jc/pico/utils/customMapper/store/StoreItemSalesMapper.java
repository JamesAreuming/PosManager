package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreItemSalesMapper {

	SingleMap selectItemTotal(SingleMap param);

	List<SingleMap> selectItemList(SingleMap param);

	SingleMap selectItemCateTotal(SingleMap param);

	List<SingleMap> selectItemCateList(SingleMap param);

}
