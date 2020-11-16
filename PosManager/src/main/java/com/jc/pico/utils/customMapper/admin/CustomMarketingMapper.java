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

import org.apache.ibatis.session.RowBounds;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface CustomMarketingMapper {

  List<LinkedHashMap<String, Object>> getPromotionMarketingList(Map<String, Object> params, RowBounds rowBounds);
  
  int getCountPromotionMarketingList(Map<String, Object> params);

  List<LinkedHashMap<String, Object>> getUserList(Map<Object,Object> paramMap, RowBounds rowBounds);

  int getCountUserList(Map<Object,Object> paramMap);
  
  // User Coupon Status List
  List<LinkedHashMap<String,String>> getCouponList(Map<String,String> paramMap, RowBounds rowBounds);

  // User Coupon Status List Count
  int getCountCouponList(Map<String,String> paramMap);
  
  List<LinkedHashMap<String, Object>> getIssedUserList(Map<String, Object> params, RowBounds rowBounds);
  
  int getCountIssedUserList(Map<String, Object> params);
  
  LinkedHashMap<String, Object> getCouponDetail(Long id);  
  
  // 프로모션 목록조회 
  List<LinkedHashMap<String, Object>> getPromotionList(Map<String, Object> params, RowBounds rowBounds);
  
  // 프로모션 목록 총건수 
  int getCountPromotionList(Map<String, Object> params);
  
  // 프로모션 상세
  LinkedHashMap<String, Object> getPromotionDetail(Long id); 
  
  // 프로모션 쿠폰 목록조회 
  List<LinkedHashMap<String, Object>> getPromotionCouponList(Map<String, Object> params, RowBounds rowBounds);
 
  // 프로모션 쿠폰 총건수 
  int getCountPromotionCouponList(Map<String, Object> params);
  
  // 프로모션 등록 
  int insertPromotionSelective(Map<String, Object> params);
  
  // 프로모션 쿠폰 매핑 등록
  int insertPromotionCouponSelective(Map<String, Object> params);
  
  // 프로모션 정보 수정
  int updatePromotionSelective(Map<String, Object> params);
  
  // 프로모션 쿠폰 매핑 삭제
  int deletePromotionCoupon(Long id);
  
  // 프로모션 발행대상 쿠폰 목록 
  List<LinkedHashMap<String, Object>> getIssueCouponList(Map<String, Object> params, RowBounds rowBounds);
  
  // 프로모션 발행대상 쿠폰 목록 총건수 
  int getCountIssueCouponList(Map<String, Object> params);
  
  // 프로모션 발행대상 쿠폰 목록 
  List<LinkedHashMap<String, Object>> getIssueUserList(Map<String, Object> params, RowBounds rowBounds);
 
  //프로모션 발행대상 쿠폰 목록 
  List<LinkedHashMap<String, Object>> getIssueUserList(Map<String, String> params);
 
  // 프로모션 발행대상 쿠폰 목록 총건수 
  int getCountIssueUserList(Map<String, Object> params); 
  
  // 프로모션 쿠폰 발행 회원등록 
  int insertUserCoupon(Map<String, String> params);
 
  // 프로모션 쿠폰 발행 대상회원 등록 
  int insertTargetUser(Map<String, String> params);
  
  // 프로모션 정보 수정
  int updatePromotionCouponSelective(Map<String, String> params);
  
  // 프로모션 쿠폰 발행 대상회원 등록 
  int insertUserCouponLog(Map<String, String> params);
  
}
