package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

/**
 * POS 연동 0.M_LICENSE_CHECK:라이선스 체크 MyBatis Mapper
 * 2016. 7. 19, green, create
 * @author green
 *
 */
public interface PosMasterLicenseCheckMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public List<Object> selectDefault(Map<String, Object> params);
}
