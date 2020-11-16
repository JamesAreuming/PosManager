/*
 * Filename	: CustomMapper.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.User;


/**
 * Created by ruinnel on 2016. 2. 4..
 */
public interface CustomMapper {

  List<Map<String, Object>> getOrderDetailHistory(Map<String, Object> params);

  List<Map<String, Object>> getStoreBeaconLog(Map<String, Object> params);
  
  int getHistoryCouponCount(Map<String, Object> params);
  
  List<Map<String, Object>> getHistoryCoupon(Map<String, Object> params);
  
  int getHistoryStampCount(Map<String, Object> params);
  
  List<Map<String, Object>> getHistoryStamp(Map<String, Object> params);
  
  List<LinkedHashMap<String, Object>> getUserAuthMenuList(String userGrp);
	
  LinkedHashMap<String, Object>  getClosing(Map<String, Object> params);
  
  int getCardCount(Map<String, Object> params);
  
  List<Map<String, Object>> getCard(Map<String, Object> params);
  
  void updateLoginFailCount(Map<String, Object> params);
  
  int getTermsCount(Map<String, Object> params);
  
  List<Map<String, Object>> getTerms(Map<String, Object> params);
  
  int getNoticeCount(Map<String, Object> params);
  
  List<Map<String, Object>> getNotice(Map<String, Object> params);
  
  int getRCHistoryCount(Map<String, Object> params);
  
  List<Map<String, Object>> getRCHistory(Map<String, Object> params);
  
  int getOrderHistoryCount(Map<String, Object> params);
  
  List<Map<String, Object>> getOrderHistory(Map<String, Object> params);
  
  List<Map<String, Object>> getReviewCount(Map<String, Object> params);
  
  int getStoreStampCount(Map<String, Object> params);
  
  List<Map<String, Object>> getStoreStampList(Map<String, Object> params);
  
  List<Map<String, Object>> getReview(Map<String, Object> params);
  
  int getNextIndex(String tableName);

  Integer getMaxMemCode(String grpCd);

  List<Map<String, Object>> getStampHistory(Map<String, Object> params);

  int getStampHistoryCount(Map<String, Object> params);

  List<Map<String, Object>> getCouponHistory(Map<String, Object> params);

  int getCouponHistoryCount(Map<String, Object> params);
  
  List<Map<String, Object>> selectByDistinctStoreTp();

  List<Map<String, Object>> getStore(Map<String, Object> params);

  int getStoreCount(Map<String, Object> params);

  List<Map<String, Object>> getItem(Map<String, Object> params);

  int getItemCount(Map<String, Object> params);

  List<Map<String, Object>> getCoupon(Map<String, Object> params);

  int getCouponCount(Map<String, Object> params);

  List<LinkedHashMap<String, Object>> getPosUserSearchList(@Param("user") User user, @Param("types") List<String> Type);

  LinkedHashMap<String, Object> getUnUsedCoupon(Map<Object,Object> paramMap);

}
