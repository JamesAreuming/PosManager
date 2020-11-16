/*
 * Filename	: CustomRewardMapper.java
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


public interface CustomRewardMapper {

  List<LinkedHashMap<String, Object>> getRewardSummaryList(Map<Object,Object> paramMap);

  
  
  List<LinkedHashMap<String, Object>> getRewardSummaryStampChart(Map<Object,Object> paramMap);
  
  List<LinkedHashMap<String, Object>> getRewardSummaryCouponChart(Map<Object,Object> paramMap);
  
  List<LinkedHashMap<String, Object>> getRewardSummaryUsedCouponChart(Map<Object,Object> paramMap);
  
  List<LinkedHashMap<String, Object>> getRewardList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getRewardDayChart(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getRewardMonthChart(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getRewardHourChart(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getRewardDayofweekChart(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getStampSummaryList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getStampDailyList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getStampWeeklyList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getStampMonthlyList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getCouponSummaryList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getCouponDailyList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getCouponWeeklyList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getCouponMonthlyList(Map<Object,Object> paramMap);
  
  // Stamp Detail List
  List<LinkedHashMap<String, String>> getStampList(Map<String,String> paramMap, RowBounds rowBounds);

  //Stamp Detail List Count
  int getCountStampList(Map<String,String> paramMap);
  
  List<LinkedHashMap<String, Object>> getCouponList(Map<Object,Object> paramMap, RowBounds rowBounds);
  
  LinkedHashMap<String, String> getCouponList(Map<String, Object> paramMap);

  int getCountCouponList(Map<Object,Object> paramMap);
  
  // 쿠폰발행취소(개인) 수정
  int updateCancleCoupon(Map<String, Object> params);  
  
  // 쿠폰정산
  List<LinkedHashMap<String, String>> getStampAccList(Map<String, String> params, RowBounds rowBounds);

  // 쿠폰정산 엑셀
  List<LinkedHashMap<String, String>> getStampAccList(Map<String, String> params);
 
  // 쿠폰정산 목록 수
  int getCountStampAccList(Map<String, String> params);
  
}
