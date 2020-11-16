package com.jc.pico.utils.customMapper.clerk;

import java.util.List;

import com.jc.pico.bean.SvnStoreTemp;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcItemExtended;

public interface ClerkStoreMapper {

	List<SingleMap> selectTableSectionListByStoreId(SingleMap param);

	List<SingleMap> selectTableListBySectionId(SingleMap param);

	List<SingleMap> selectPluCategoryList(SingleMap param); // ex. 44, 44, 89

	List<SvcItemExtended> selectPluItemList(SingleMap param); // 151

	Long saveStoreTemp(SvnStoreTemp record);

}
