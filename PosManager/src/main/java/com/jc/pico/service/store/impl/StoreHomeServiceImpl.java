package com.jc.pico.service.store.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.service.store.StoreCommonService;
import com.jc.pico.service.store.StoreHomeService;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.store.StoreHomeBrandSalesMapper;
import com.jc.pico.utils.customMapper.store.StoreHomeStoreSalesMapper;

@Service
public class StoreHomeServiceImpl implements StoreHomeService {

	@Autowired
	private StoreHomeBrandSalesMapper brandSalesMapper;

	@Autowired
	private StoreHomeStoreSalesMapper storeSalesMapeer;

	@Autowired
	private StoreCommonService commonService;
	
	@Override
	public SingleMap getBrandHome(SingleMap param) {

		/*
		 * todaySales
		 * 		- storeCount : 매출이 발생한 점포 갯수
		 * 		- salesTotal : 총 매출액
		 * 		- orderTotal : 총 주문수
		 * todaySalesChange
		 * 		- diffSales : 최고 매출과의 차이 
		 * 		- diffRate : 최고 매출과의 차이 비율
		 * lastDaysSales 지난 5일간 매출
		 * 		- label : 날짜 yyyy-MM-dd
		 * 		- salesTotal : 지난주 총 매출액
		 * todaySalesBestItem
		 * 		- label : 상품명
		 * 		- salesCount : 총 주문수
		 * 		- salesTotal : 매출액
		 * todayTimeSales :
		 * 		- label : 시간대
		 * 		- salesTotal : 총 매출액
		 * todaySalesTopStore : 총 매출이 큰 매장 5개
		 * 		- label : 매장명
		 * 		- salesTotal : 총 매출액
		 * 
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		final int dayCount = 5; // 오늘 포함 5일치 검색
		param.put("topStoreLimit", dayCount);// todaySalesTopStore 조회 건수		
		param.put("lastDaysLimit", dayCount); // lastDaySales, lastDaysSalesChange 조회 건수 (오늘을 포함한 N일)
		param.put("lastDaysLimits", new int[dayCount]); // mybatis에서 dayCount 만큼 foreach를 돌린는데 사용

		SingleMap todaySales = brandSalesMapper.selectTodaySalesSummary(param);
		List<SingleMap> lastDaysSales = brandSalesMapper.selectLastDaysSales(param); // 최근 5일간 매출
		SingleMap yesterDaySales = lastDaysSales.get(lastDaysSales.size() - 2);
		resolveGetBestSales(lastDaysSales);

		SingleMap result = new SingleMap();
		result.put("todaySales", todaySales);
		result.put("todaySalesBestItem", brandSalesMapper.selectTodaySalesBestItem(param));
		result.put("todayTimeSales", brandSalesMapper.selectTodayTimeSales(param));
		result.put("todaySalesTopStore", brandSalesMapper.selectTodaySalesTopStore(param));
		result.put("todaySalesChange", getSalesChange(todaySales, yesterDaySales));
		result.put("lastDaysSales", lastDaysSales);

		return result;
	}

	@Override
	public SingleMap getStoreHome(SingleMap param) {
		/*
		 * todaySales 		
		 * 		- salesTotal : 총 매출액
		 * 		- orderTotal : 총 주문수	  		
		 * todaySalesChange
		 * 		- diffSales : 최고 매출과의 차이 
		 * 		- diffRate : 최고 매출과의 차이 비율
		 * lastDaysSales 지난 5일간 매출
		 * 		- label : 날짜 yyyy-MM-dd
		 * 		- salesTotal : 지난주 총 매출액
		 * todaySalesBestItem
		 * 		- label : 상품명
		 * 		- salesCount : 총 주문수
		 * 		- salesTotal : 매출액
		 * todayTimeSales :
		 * 		- label : 시간대
		 * 		- salesTotal : 총 매출액
		 * 
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		final int dayCount = 5; // 오늘 포함 5일치 검색
		param.put("topStoreLimit", dayCount);// todaySalesTopStore 조회 건수		
		param.put("lastDaysLimit", dayCount); // lastDaySales, lastDaysSalesChange 조회 건수 (오늘을 포함한 N일)
		param.put("lastDaysLimits", new int[dayCount]); // mybatis에서 dayCount 만큼 foreach를 돌린는데 사용

		SingleMap todaySales = storeSalesMapeer.selectTodaySalesSummary(param);
		List<SingleMap> lastDaysSales = storeSalesMapeer.selectLastDaysSales(param); // 최근 5일간 매출
		SingleMap yesterDaySales = lastDaysSales.get(lastDaysSales.size() - 2);
		resolveGetBestSales(lastDaysSales);

		SingleMap result = new SingleMap();
		result.put("todaySales", todaySales);
		result.put("todaySalesBestItem", storeSalesMapeer.selectTodaySalesBestItem(param));
		result.put("todayTimeSales", storeSalesMapeer.selectTodayTimeSales(param));
		result.put("todaySalesChange", getSalesChange(todaySales, yesterDaySales));
		result.put("lastDaysSales", lastDaysSales);

		return result;
	}

	/**
	 * 두 매출간의 총매출액 차이와 증감 비율을 계산
	 * 
	 * @param toSales
	 *            기준 매출
	 * @param fromSales
	 *            이전 매출 (비교 대상)
	 * @return
	 * 		diffSales : 총매출 차이
	 *         diffRate : 변동 비율
	 */
	private SingleMap getSalesChange(SingleMap toSales, SingleMap fromSales) {
		double toSalesTotal = toSales.getDouble("salesTotal");
		double fromSalesTotal = fromSales.getDouble("salesTotal");

		double diffSales = toSalesTotal - fromSalesTotal;
		double diffRate = 0;

		// diffRate 계산
		if (diffSales == 0) { //  기준/이전 매출이 둘 다 0 인 경우 처리
			diffRate = 0;
		} else if (fromSalesTotal == 0) { // 이전 매출이 0 이면 계산이 불가하니 100% 증가로 처리
			diffRate = 100;
		} else { // (기준 매출 - 이전 매출) / 이전 매츨 * 100
			diffRate = diffSales / fromSalesTotal * 100;
		}

		SingleMap result = new SingleMap();
		result.put("diffSales", diffSales);
		result.put("diffRate", diffRate);
		return result;
	}

	/**
	 * 오늘(마지막날)을 제외한 최고 매출 항목 조회
	 * 
	 * @param daysSales
	 *            날짜순으로 정렬된 일별 매출
	 * @return
	 */
	private SingleMap resolveGetBestSales(List<SingleMap> daysSales) {
		SingleMap bestSales = null;

		// 최고 매출인 항목 검색
		// 마지막 항목은 오늘 조회 건이므로 대상에서 제외 
		for (int i = daysSales.size() - 2; i >= 0; i--) {
			SingleMap sales = daysSales.get(i);
			if (bestSales == null) {
				bestSales = sales;
				continue;
			}
			if (bestSales.getDouble("salesTotal") < sales.getDouble("salesTotal")) {
				bestSales = sales;
			}
		}

		// 최고 매출 항목에 isBest를 true로 설정 그외 false로 설정
		for (SingleMap sales : daysSales) {
			sales.put("isBest", sales.equals(bestSales));
		}

		return bestSales;
	}

}
