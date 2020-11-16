package com.jc.pico.utils.customMapper.pos;

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
public interface PosSalesSaleSaveMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesSaleSave> selectDefault(Map<String, Object> params);

	/**
	 * 매출 상품의 아이템 및 카테고리 코드 정보를 조회 한다ㅣ
	 * 
	 * @param itemId
	 * @return
	 */
	public SingleMap selectItemAndCateCodeInfo(long itemId);

	/**
	 * 아이템의 코드 정보를 조회한다.
	 * 
	 * @param itemIds
	 * @return
	 */
	public List<SingleMap> selectItemAndCatInfos(@Param("itemIds") List<Long> itemIds);

	/**
	 * sales 쓰기 락 설정
	 * 
	 * @param storeId
	 *            상점 ID
	 * @param receiptNo
	 *            영수증 번호
	 */
	public boolean lockSaveSales(@Param("storeId") Long storeId, @Param("receiptNo") String receiptNo);

	/**
	 * sales 락 해제
	 * 
	 * @param storeId
	 *            상점 ID
	 * @param receiptNo
	 *            영수증 번호
	 */
	public boolean unlockSaveSales(@Param("storeId") Long storeId, @Param("receiptNo") String receiptNo);

}
