package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosMasterGoodsOpt;
import com.jc.pico.utils.bean.PosMasterGoodsOptDtl;

/**
 * POS 연동 10. M_GOODS_INFO : 상품 정보 MyBatis Mapper
 * 2016. 7. 21, green, create
 * 
 * @author green
 *
 */
public interface PosMasterGoodsInfoMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<Object> selectDefault(Map<String, Object> params);

	/**
	 * 상품 ID로 상품의 옵션 정보를 조회
	 * 
	 * @param params
	 *            itemId : 아이템 id
	 * @return 상품 정보
	 */
	public List<PosMasterGoodsOpt> selectOptListByItemId(Map<String, Object> params);

	/**
	 * 상품 옵션 ID로 상세 조회
	 * @param params
	 * 	optId : 옵션 ID
	 * @return
	 */
	public List<PosMasterGoodsOptDtl> getPosMasterGoodsOptDtlByOptId(Map<String, Object> params);
}
