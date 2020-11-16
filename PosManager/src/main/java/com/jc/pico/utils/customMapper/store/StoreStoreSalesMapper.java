package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.bean.SvcStoreSales;
import com.jc.pico.bean.SvcStoreSalesInfo;
import com.jc.pico.bean.SvcStoreSalesView;
import com.jc.pico.utils.bean.SingleMap;

public interface StoreStoreSalesMapper {


	SingleMap selectSalesSummary(SingleMap param);

	List<SingleMap> selectPayMethodSales(SingleMap param);

	List<SingleMap> selectHourlySalesList(SingleMap param);

	List<SingleMap> selectDailyList(SingleMap param);

	List<SingleMap> selectWeeklyList(SingleMap param);

	List<SingleMap> selectMonthlyList(SingleMap param);

	Long selectSalesBrandIdSalesId(SingleMap param);

	SvcStoreSales getStoreSales(SingleMap param);

	SvcStoreSalesInfo getStoreSalesDtl(SingleMap param);
	
	SvcStoreSales getStoreSalesTotal(SingleMap param);

	List<SvcStoreSalesView> getStoreSalesList(SingleMap param);
}
