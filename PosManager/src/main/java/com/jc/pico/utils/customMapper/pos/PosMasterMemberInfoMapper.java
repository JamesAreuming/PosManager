package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosMasterMemberInfo;

/**
 * POS 연동 29. M_MEMBER_INFO : 회원 정보 MyBatis Mapper
 * 2016. 7. 21, green, create
 * 
 * @author green
 *
 */
public interface PosMasterMemberInfoMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<PosMasterMemberInfo> selectDefault(Map<String, Object> params);
}
