package com.jc.pico.utils.customMapper.pos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jc.pico.utils.bean.PosSalesSaleSave;
import com.jc.pico.utils.bean.SingleMap;

/**
 * POS 연동 1. S_SALE_SAVE : 매출정보 MyBatis Mapper
 * 2016. 8. 13, green, create
 * 
 * @author green
 *
 */
public interface PosSalesStockMapper {
	
	// TB_SVC_STOCK_SALES_ITEM 테이블 조회(히스토리 조회) 
	int getStockSalesItemCnt(Map<Object,Object> paramMap);

	// 현재 재고량이 1이상 인 것.
	int getStockImportListCount(Map<Object,Object> paramMap);
		
	// 현재 재고량이 1이상 인 입고리스트.(선입선출 이기 때문에 마지막 건 포함이라고 가정) 
	List<LinkedHashMap<String, Object>> getStockImportList(Map<Object,Object> paramMap);
	
	// 현재 재고가 모두 0일겨우 마지막 입고 단일 건.
	List<LinkedHashMap<String, Object>> getStockImportLastList(Map<Object,Object> paramMap);
	
	// 재고관리 현재 재고량 업데이트(TB_SVC_STOCK_IMPORT)
	int updateStockImport(Map<Object,Object> paramMap);
	
	// 재고관리맵핑 등록(TB_SVC_STOCK_SALES_ITEM)
	int insertStockSalesItem(Map<Object,Object> paramMap);
	
}
