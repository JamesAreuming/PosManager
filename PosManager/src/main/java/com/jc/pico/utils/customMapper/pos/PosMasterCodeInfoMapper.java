package com.jc.pico.utils.customMapper.pos;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosMasterCodeInfo;
import com.jc.pico.utils.bean.SingleMap;

/**
 * POS 연동 7. M_CODE_INFO : 공통 코드 MyBatis Mapper
 * 2016. 7. 19, green, create
 * 
 * @author green
 *
 */
public interface PosMasterCodeInfoMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<PosMasterCodeInfo> selectDefault(Map<String, Object> params);

	/**
	 * main cd에 해당하는 코드 정보 조회
	 * 6자리 코드 사용
	 * 
	 * @param params
	 *           mainCodes : 메인코드 array
	 * @return 매장정보
	 */
	public Collection<? extends PosMasterCodeInfo> selectManualList(SingleMap params);

}
