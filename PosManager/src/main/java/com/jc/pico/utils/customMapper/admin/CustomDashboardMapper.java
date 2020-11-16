/*
 * Filename	: CustomStoreMapper.java
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

public interface CustomDashboardMapper {
	
	/*
	 * dashboard to franchise
	 */
	// 프랜차이즈 로그인시 브랜드별 매출목록 조회
	List<LinkedHashMap<String, String>> getBrandInfoList(Map<String, String> paramMap);
	
	/*
	 * brand
	 */
	// 브랜드 매출 조회
	LinkedHashMap<String, String> getBrandInfo(Map<String, String> paramMap);
	
	// Sales Trend
	List<LinkedHashMap<String, String>> getSalesTrendList(Map<String, String> paramMap);
	
	// Today Sales Top OR Bottom 5 Chart
	List<LinkedHashMap<String, String>> getSalesChart(Map<String, String> paramMap);
	
	// Today Sales Top OR Bottom 5 List
	List<LinkedHashMap<String, String>> getSalesList(Map<String, String> paramMap);
	
	/*
	 * dashboard to Store
	 */
	// Today Sales
	LinkedHashMap<String, String> getSalesData(Map<String, String> paramMap);
	
	// Today Service
	LinkedHashMap<String, String> getServiceData(Map<String, String> paramMap);
	
	// Today Top5 Item
	List<LinkedHashMap<String, String>> getItemList(Map<String, String> searchData);
	
	// Today Top5 Category
	List<LinkedHashMap<String, String>> getCategoryList(Map<String, String> paramMap);
	
	// Today Payment List
	List<LinkedHashMap<String, String>> getPaymentList(Map<String, String> paramMap);
	
	// This Week Customer List
	List<LinkedHashMap<String, String>> getThisWeekCustomerList(Map<String, String> paramMap);
	
	 // Search Last Customer List
  List<LinkedHashMap<String, String>> getSearchLastCustomerList(Map<String, String> paramMap);
	
	 // This Month Customer List
  List<LinkedHashMap<String, String>> getThisMonthCustomerList(Map<String, String> paramMap);
	
	// Recently Reviews
	List<LinkedHashMap<String, String>> getReviewsList(Map<String, String> paramMap);
	
	int getReviewsListCount(Map<String, String> paramMap);
	
	// Today Stamp
	LinkedHashMap<String, String> getStampData(Map<String, String> paramMap);
	
	// This week Coupon Issue
	List<LinkedHashMap<String, String>> getCouponIssueList(Map<String, String> paramMap);
	
	// This week Coupon use
	List<LinkedHashMap<String, String>> getCouponUseList(Map<String, String> paramMap);
}
