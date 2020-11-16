package com.jc.pico.service.store;

import com.jc.pico.bean.SvcStoreSalesInfo;
import com.jc.pico.utils.Page;
import com.jc.pico.utils.bean.SingleMap;

public interface StoreSalesService {

	SingleMap getBrandSalesSummary(SingleMap param);

	SingleMap getBrandSalesDaily(SingleMap param);

	SingleMap getBrandSalesWeekly(SingleMap param);

	SingleMap getBrandSalesMonthly(SingleMap param);

	SingleMap getStoreSalesSummary(SingleMap param);

	SingleMap getStoreSalesDaily(SingleMap param);

	SingleMap getStoreSalesWeekly(SingleMap param);

	SingleMap getStoreSalesMonthly(SingleMap param);

	SingleMap getStoreItemSales(SingleMap param);

	SingleMap getStoreItemCateSales(SingleMap param);

	SingleMap getStoreServiceSales(SingleMap param);

	SingleMap getStorePaymentSales(SingleMap param);

	SingleMap getStoreSalesTime(SingleMap param);

	SingleMap getStoreSalesWeek(SingleMap param);

	SingleMap getStoreSalesDetails(SingleMap param);

	SingleMap getStoreSalesDetail(SingleMap param);

	Page getStoreSalesList(SingleMap param);

	SvcStoreSalesInfo getStoreSalesDtl(SingleMap param);
}
