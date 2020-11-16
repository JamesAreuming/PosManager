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

import org.apache.ibatis.session.RowBounds;


public interface CustomStoreMapper {

  Map<String, Object> getBrand(Map<String, Object> params);

  List<LinkedHashMap<String, Object>> getBrandList(Map<Object, Object> paramMap, RowBounds rowBounds);

  int getCountBrandList(Map<Object, Object> paramMap);

  List<LinkedHashMap<String, Object>> getStoreList(Map<String, String> paramMap, RowBounds rowBounds);

  int getCountStoreList(Map<String, String> paramMap);
  
  int getCheckStore(Map<String, String> search);

  Map<String, Object> getStoreSet(Map<String, Object> params);

  Map<String, Object> getBrandSet(Map<String, Object> params);

  List<LinkedHashMap<String, Object>> getStoreBeaconList(Map<String, Object> params);

  List<LinkedHashMap<String, Object>> getBeaconList(Map<Object, Object> paramMap, RowBounds rowBounds);

  int getCountBeaconList(Map<Object, Object> paramMap);

  void insertBeaconCsv(Map<String, Object> params);

  List<LinkedHashMap<String, Object>> getPosLicenseList(Map<Object, Object> paramMap, RowBounds rowBounds);
  
  List<LinkedHashMap<String, Object>> getPosLicenseList(Map<String, Object> paramMap);

  int getCountPosLicenseList(Map<Object, Object> paramMap);

  void insertPosLicenseCsv(Map<String, Object> params);
  
  // CCTV 목록 조회
  List<LinkedHashMap<String, Object>> getCctvList(Map<String, String> params, RowBounds rowBounds);
  
  // CCTV 목록 총 건수
  int getCountCctvList(Map<Object, Object> params);
  
  // Printer  목록 조회
  List<LinkedHashMap<String, Object>> getPrinterList(Map<String, String> params, RowBounds rowBounds);
  
  // Printer  목록 조회
  List<LinkedHashMap<String, Object>> getPrinterList(Map<String, String> params);
 
  // Printer 목록 총 건수
  int getPrinterListCount(Map<Object, Object> params);
  
  // Stamp 조회 brand_set
  LinkedHashMap<String, String> getStampBrandInfo(Map<Object, Object> params);
 
  // Stamp 조회 store_set
  LinkedHashMap<String, String> getStampStoreInfo(Map<Object, Object> params);
  
  // 스템프적립 쿠폰 등록
  int insertStampCoupon(Map<String, String> params);  
  
  // 스템프적립 쿠폰 수정
  int updateStampCoupon(Map<String, String> params);
  
}
