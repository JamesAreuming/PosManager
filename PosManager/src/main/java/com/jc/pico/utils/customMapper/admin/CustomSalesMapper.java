/*
 * Filename	: CustomSalesMapper.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.customMapper.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;


public interface CustomSalesMapper {

	/*
	 * Brand Summmary
	 */
	// BrandSum
	List<LinkedHashMap<String, Object>> getSalesSummaryList(Map<Object,Object> paramMap);
  
	// 브랜드 섬머리(테이블)
	List<LinkedHashMap<String, Object>> getBrandSummaryList(Map<Object, Object> paramMap);
	
	// 
	List<LinkedHashMap<String, Object>> getSalesDayChart(Map<Object,Object> paramMap);
	
	//
	List<LinkedHashMap<String, Object>> getSalesMonthChart(Map<Object,Object> paramMap);
	
	//
	List<LinkedHashMap<String, Object>> getSalesHourChart(Map<Object,Object> paramMap);
	
	//
	List<LinkedHashMap<String, Object>> getSalesDayofweekChart(Map<Object,Object> paramMap);
	
	/*
	 * Sales Summmary
	 */
	// Sales Summmary Chart
	List<LinkedHashMap<String, Object>> getSalesSummaryChart(Map<Object,Object> paramMap);
	
	// Sales Summmary List
	List<LinkedHashMap<String, Object>> getAllSalesSummaryList(Map<Object,Object> paramMap);
	
	// Sales Summmary Hour Of Day Chart 
	List<LinkedHashMap<String, Object>> getSalesHourOfDayList(Map<Object, Object> paramMap);
		
	// Sales Summmary Day Of Day Month
	List<LinkedHashMap<String, Object>> getSalesDayOfMonthList(Map<Object, Object> paramMap);
	
	// Sales Summmary Day Of Week Chart
	List<LinkedHashMap<String, Object>> getSalesDayOfWeekList(Map<Object, Object> paramMap);
		
	// Sales Summmary Month Of Year Chart
	List<LinkedHashMap<String, Object>> getSalesMonthOfYearList(Map<Object, Object> paramMap);
	
	
	/*
	 * Sales Goal
	 */
	// Sales Goal 매출목표관리 등록수정
	int insertUpdateSalesGoal(Map<Object,Object> paramMap);
  
	// Sales Goal 매출목표관리 조회
	List<LinkedHashMap<String, Object>> getSalesGoalList(Map<Object,Object> paramMap);
  
	/*
	 * Sales Goal Performance
	 */
	// Analyze performance against goals 목표대비실적분석:목표매출
	List<LinkedHashMap<String, Object>> getAPGoalList(Map<Object,Object> paramMap);
  
	// Analyze performance against goals 목표대비실적분석 : 매출실적
	List<LinkedHashMap<String, Object>> getAPSalesList(Map<Object,Object> paramMap);
	
	
	/*
	 * Sales Category 
	 */
	// Category Sales TOP 5 Summary List
	List<LinkedHashMap<String, Object>> getCateSalesSummaryList(Map<Object, Object> paramMap);

	// 카테고리 판매현황 검색(테이블)
	List<LinkedHashMap<String, Object>> getCategorySalesList(Map<Object, Object> paramMap);
		
	// Category Sales TOP 5 Summary Chart
	List<LinkedHashMap<String, Object>> getCateSalesTop5SummaryChart(Map<Object, Object> paramMap);
	  
	// Category Sales -> 카테고리 판매현황 TOP 5 목록 
	List<LinkedHashMap<String, Object>> getCategoryTopList(Map<Object,Object> paramMap);
	
	// Category Sales TOP 5 Day Of Month Chart 
	List<LinkedHashMap<String, Object>> getCateDayOfMonthChart(Map<Object,Object> paramMap);
	
	// Category Sales TOP 5 Day Of Week Chart 
	List<LinkedHashMap<String, Object>> getCateDayOfWeekChart(Map<Object,Object> paramMap);
	
	// Category Sales TOP 5 Month Of Year Chart 
	List<LinkedHashMap<String, Object>> getCateMonthOfYeeaChart(Map<Object,Object> paramMap);
	
	// Category Sales TOP 5 Hour Of Day Chart 
	List<LinkedHashMap<String, Object>> getCateHourOfDayChart(Map<Object,Object> paramMap);
	
	
	/*
	 * Service Sales 
	 */
	// Service Sales Summary List
	List<LinkedHashMap<String, Object>> getServiceSalesSummaryList(Map<Object,Object> paramMap);
	
	// Service Sales 판매현황 검색(테이블)
	List<LinkedHashMap<String, Object>> getServiceSalesList(Map<Object,Object> paramMap);
  
	List<LinkedHashMap<String, Object>> getServiceSummaryChart(Map<Object,Object> paramMap);

	List<LinkedHashMap<String, Object>> getServiceDayChart(Map<Object,Object> paramMap);

	List<LinkedHashMap<String, Object>> getServiceMonthChart(Map<Object,Object> paramMap);

	List<LinkedHashMap<String, Object>> getServiceHourChart(Map<Object,Object> paramMap);

	List<LinkedHashMap<String, Object>> getServiceDayOfWeekChart(Map<Object,Object> paramMap);
	
	
	/*
	 * Item Sales 
	 */
	// 아이템 판매현황 검색(테이블)
	List<LinkedHashMap<String, Object>> getItemSalesList(Map<Object, Object> paramMap);
	
	// Category Sales TOP 5 Summary List
	List<LinkedHashMap<String, Object>> getItemSalesSummaryList(Map<Object, Object> paramMap);

	// Category Sales TOP 5 Summary Chart
	List<LinkedHashMap<String, Object>> getItemSalesTop5SummaryChart(Map<Object, Object> paramMap);
  
	// Category Sales TOP 5 Day Of Month Chart
	List<LinkedHashMap<String, Object>> getItemDayOfMonthChart(Map<Object, Object> paramMap);

	// Category Sales TOP 5 Day Of Week Chart
	List<LinkedHashMap<String, Object>> getItemDayOfWeekChart(Map<Object, Object> paramMap);
  
	// Category Sales TOP 5 Month Of Day Chart
	List<LinkedHashMap<String, Object>> getItemMonthOfYeeaChart(Map<Object, Object> paramMap);
	
	// Category Sales TOP 5 Hour Of Day Chart
	List<LinkedHashMap<String, Object>> getItemHourOfDayChart(Map<Object, Object> paramMap);
  

	/*
	 * Sales Detail
	 */
	List<LinkedHashMap<String, Object>> getDetailList(Map<Object,Object> paramMap);

	Map<String, Object> getDetail(Map<Object, Object> params);
	
	List<LinkedHashMap<String, Object>> getDetailServiceList(Map<Object,Object> paramMap);

	List<LinkedHashMap<String, Object>> getDetailItemList(Map<Object,Object> paramMap);


	/*
	 * Card Approval
	 */
	List<LinkedHashMap<String, String>> getCardApprovalList(Map<String,String> paramMap);
  
	List<LinkedHashMap<String, String>> getCardApprovalList(Map<String,String> paramMap, RowBounds rowBounds);
  
	int getCountCardApprovalList(Map<String,String> paramMap);  
	
	
	// 검색 기간 범위의 날짜(YYYY-MM-DD) 리턴 
	List<LinkedHashMap<String, Object>> getCalendarYMDList(Map<Object, Object> paramMap);
  
	// 검색 기간 범위의 날짜(YYYYWW) 리턴 
	List<LinkedHashMap<String, Object>> getCalendarYearWeekList(Map<Object, Object> paramMap);
  
	// 검색 기간 범위의 날짜(YYYYMM) 리턴 
	List<LinkedHashMap<String, Object>> getCalendarYearMonthList(Map<Object, Object> paramMap);
  
}
