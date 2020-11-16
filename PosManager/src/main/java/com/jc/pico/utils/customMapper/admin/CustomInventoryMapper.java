/*
 * Filename	: CustomManagementMapper.java
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

public interface CustomInventoryMapper {
  
  List<LinkedHashMap<String, Object>> getActualStockList(Map<Object,Object> paramMap);
  int getCountActualStockList(Map<Object,Object> paramMap);
  
	// Stock Import List
	List<LinkedHashMap<String, Object>> getStockImportList(Map<Object,Object> paramMap);
	int getStockImportListCount(Map<Object,Object> paramMap);
	void insertStockImportCSV(Map<String, Object> params);
	
	// Stock Adjust List
	List<LinkedHashMap<String, Object>> getStockAdjustList(Map<Object,Object> paramMap);
	int getStockAdjustListCount(Map<Object,Object> paramMap);
	
	
	List<LinkedHashMap<String, Object>> getSupplyCompanyList(Map<Object,Object> paramMap);
	int getCountSupplyCompanyList(Map<Object,Object> paramMap);
	
	
	
	// Stock Export 리스트 카운트
	int getStockExportListCount(Map<Object,Object> paramMap);
	
	// Stock Export 리스트 조회
	List<LinkedHashMap<String, Object>> getStockExportList(Map<Object,Object> paramMap);
	
	
	// Stock Status 리스트 카운트
	int getStockSatusListCount(Map<Object,Object> paramMap);
	
	// Stock Status 리스트 조회
	List<LinkedHashMap<String, Object>> getStockSatusList(Map<Object,Object> paramMap);
	
	// Stock Cloing 리스트 카운트
	int getStockCloingListCount(Map<Object,Object> paramMap);
		
	// Stock Cloing 리스트 조회
	List<LinkedHashMap<String, Object>> getStockCloingList(Map<Object,Object> paramMap);
	
	// Stock Cloing insert
	int insertStockCloingList(LinkedHashMap<String,Object> paramMap);    
}
