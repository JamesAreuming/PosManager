package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosSalesSaleSaveDiscount;

/**
 * POS 연동 1. S_SALE_SAVE : 매출정보 MyBatis Mapper
 * 2016. 8. 13, green, create
 * @author green
 *
 */
public interface PosSalesSaleSaveDiscountMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesSaleSaveDiscount> selectDefault(Map<String, Object> params);
}
