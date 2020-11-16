package com.jc.pico.service.store.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.service.store.StoreRewardService;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.store.StoreBrandRewardMapper;
import com.jc.pico.utils.customMapper.store.StoreCalendarMapper;
import com.jc.pico.utils.customMapper.store.StoreStoreCouponMapper;
import com.jc.pico.utils.customMapper.store.StoreStoreStampMapper;

@Service
public class StoreRewardServiceImpl implements StoreRewardService {

	@Autowired
	private StoreBrandRewardMapper brandRewardMapper;

	@Autowired
	private StoreStoreCouponMapper storeCouponMapper;

	@Autowired
	private StoreStoreStampMapper storeStampMapper;

	@Autowired
	private StoreCalendarMapper calendarMapper;

	@Override
	public SingleMap getBrandRewardSummary(SingleMap param) {
		/*
		 * request
		 * 	brandId, startDate, endDate
		 * 
		 * response
		 * 	amount : 합계
		 * 		- stampTotal
		 * 		- couponIssuedTotal
		 * 		- couponUsedTotal
		 * 	items : 매장별 현황
		 * 		- label : 매장명
		 * 		- stampTotal : 스탬프 적립 
		 * 		- couponIssueTotal : 쿠폰발행
		 * 		- couponUsedTotal : 쿠폰 사용
		 */

		SingleMap result = new SingleMap();
		result.put("amount", brandRewardMapper.selectSummaryTotal(param));
		result.put("items", brandRewardMapper.selectSummaryList(param));

		return result;
	}

	@Override
	public SingleMap getBrandRewardDaily(SingleMap param) {
		/*
		 * request
		 * 	brandId, startDate, endDate
		 * 
		 * response
		 * 	amount : 합계
		 * 		- stampTotal
		 * 		- couponIssuedTotal
		 * 		- couponUsedTotal
		 * 	items : 매장별 현황
		 * 		- label : 매장명
		 * 		- stampTotal : 스탬프 적립 
		 * 		- couponIssueTotal : 쿠폰발행
		 * 		- couponUsedTotal : 쿠폰 사용
		 */

		param.put("pagination", calendarMapper.selectPaginationDaily(param));

		SingleMap result = new SingleMap();
		result.put("items", brandRewardMapper.selectDailyList(param));

		return result;
	}

	@Override
	public SingleMap getBrandRewardWeekly(SingleMap param) {
		/*
		 * request
		 * 	brandId, startDate, endDate
		 * 
		 * response
		 * 	amount : 합계
		 * 		- stampTotal
		 * 		- couponIssuedTotal
		 * 		- couponUsedTotal
		 * 	items : 매장별 현황
		 * 		- label : 매장명
		 * 		- stampTotal : 스탬프 적립 
		 * 		- couponIssueTotal : 쿠폰발행
		 * 		- couponUsedTotal : 쿠폰 사용
		 */

		param.put("pagination", calendarMapper.selectPaginationWeekly(param));

		SingleMap result = new SingleMap();
		result.put("items", brandRewardMapper.selectWeeklyList(param));

		return result;
	}

	@Override
	public SingleMap getBrandRewardMonthly(SingleMap param) {
		/*
		 * request
		 * 	brandId, startDate, endDate
		 * 
		 * response
		 * 	amount : 합계
		 * 		- stampTotal
		 * 		- couponIssuedTotal
		 * 		- couponUsedTotal
		 * 	items : 매장별 현황
		 * 		- label : 매장명
		 * 		- stampTotal : 스탬프 적립 
		 * 		- couponIssueTotal : 쿠폰발행
		 * 		- couponUsedTotal : 쿠폰 사용
		 */

		param.put("pagination", calendarMapper.selectPaginationMonthly(param));

		SingleMap result = new SingleMap();
		result.put("items", brandRewardMapper.selectMonthlyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreStampSummary(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * response
		 * 	salesAccumStampCount : 매출적립 스탬프
		 * 	cancelStampCount : 취소 스탬프 
		 * 	stampDailyAccumCount : 일평균 적립  
		 * 	hourlyStamp : 시간대별 적립 스탬프
		 * 		- label : 시간대 (12am, 1, 2, 12pm, 1, 2)
		 * 		- stampCount : 적립 스탬프
		 */

		SingleMap result = new SingleMap();
		result.putAll(storeStampMapper.selectSummaryInfo(param));
		result.put("hourlyStamp", storeStampMapper.selectSummaryHourlyIssuedList(param));

		return result;
	}

	@Override
	public SingleMap getStoreStampDaily(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * 	items : 매장별 현황
		 * 		- label : 날짜 (2016-5-1)
		 * 		- stampCount : 적립 스탬프
		 * 		- cancelStampCount : 취소 스탬프
		 */

		param.put("pagination", calendarMapper.selectPaginationDaily(param));

		SingleMap result = new SingleMap();
		result.put("items", storeStampMapper.selectDailyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreStampWeekly(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * 	items : 매장별 현황
		 * 		- label : 날짜 (5/1 ~ 1/3)
		 * 		- stampCount : 적립 스탬프
		 * 		- cancelStampCount : 취소 스탬프
		 */

		param.put("pagination", calendarMapper.selectPaginationWeekly(param));

		SingleMap result = new SingleMap();
		result.put("items", storeStampMapper.selectWeeklyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreStampMonthly(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * 	items : 매장별 현황
		 * 		- label : 날짜 (2016-1)
		 * 		- stampCount : 적립 스탬프
		 * 		- cancelStampCount : 취소 스탬프
		 */

		param.put("pagination", calendarMapper.selectPaginationMonthly(param));

		SingleMap result = new SingleMap();
		result.put("items", storeStampMapper.selectMonthlyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreCouponSummary(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * response
		 * 	couponIssuedTotal : 쿠폰발행
		 * 	couponIssueCanceledTotal : 쿠폰발행취소 
		 * 	couponUsedTotal : 쿠폰 사용
		 * 	couponUseCanceledTotal : 쿠폰사용취소    
		 * 	hourlyCoupon: 시간대별 쿠폰 발행/사용
		 * 		- label : 시간대 (12am, 1, 2, 12pm, 1, 2)
		 * 		- couponIssuedTotal : 쿠폰 발행
		 * 		- couponUsedTotal : 쿠폰 사용
		 */

		SingleMap result = new SingleMap();
		result.putAll(storeCouponMapper.selectSummaryInfo(param));
		result.put("hourlyCoupon", storeCouponMapper.selectSummaryHourlyIssuedList(param));

		return result;
	}

	@Override
	public SingleMap getStoreCouponDaily(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * 	items :  일별 현황
		 * 		- label : 날짜 (2016-5-1)
		 * 		- couponIssuedCount : 쿠폰발행
		 * 		- couponUsedCount : 쿠폰 사용
		 */

		param.put("pagination", calendarMapper.selectPaginationDaily(param));

		SingleMap result = new SingleMap();
		result.put("items", storeCouponMapper.selectDailyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreCouponWeekly(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * 	items :  주별 현황
		 * 		- label : 날짜 (2016-5-1)
		 * 		- couponIssuedCount : 쿠폰발행
		 * 		- couponUsedCount : 쿠폰 사용
		 */

		param.put("pagination", calendarMapper.selectPaginationWeekly(param));

		SingleMap result = new SingleMap();
		result.put("items", storeCouponMapper.selectWeeklyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreCouponMonthly(SingleMap param) {
		/*
		 * request
		 * 	storeId, startDate, endDate
		 * 
		 * 	items :  월별 현황
		 * 		- label : 날짜 (2016-5-1)
		 * 		- couponIssuedCount : 쿠폰발행
		 * 		- couponUsedCount : 쿠폰 사용
		 */

		param.put("pagination", calendarMapper.selectPaginationMonthly(param));

		SingleMap result = new SingleMap();
		result.put("items", storeCouponMapper.selectMonthlyList(param));

		return result;
	}

}
