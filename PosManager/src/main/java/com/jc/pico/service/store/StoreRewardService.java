package com.jc.pico.service.store;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreRewardService {

	SingleMap getBrandRewardSummary(SingleMap param);

	SingleMap getBrandRewardDaily(SingleMap param);

	SingleMap getBrandRewardWeekly(SingleMap param);

	SingleMap getBrandRewardMonthly(SingleMap param);

	SingleMap getStoreStampSummary(SingleMap param);

	SingleMap getStoreStampDaily(SingleMap param);

	SingleMap getStoreStampWeekly(SingleMap param);

	SingleMap getStoreStampMonthly(SingleMap param);

	SingleMap getStoreCouponSummary(SingleMap param);

	SingleMap getStoreCouponDaily(SingleMap param);

	SingleMap getStoreCouponWeekly(SingleMap param);

	SingleMap getStoreCouponMonthly(SingleMap param);
}
