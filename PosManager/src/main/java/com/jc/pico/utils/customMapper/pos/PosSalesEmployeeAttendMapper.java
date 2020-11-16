package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.bean.SvcStaffAttend;

/**
 * POS 연동 8. M_EMPLOYEE_INFO : 사원 정보 MyBatis Mapper
 * 2016. 7. 21, green, create
 * @author green
 *
 */
public interface PosSalesEmployeeAttendMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public int selectCountDefault(Map<String, Object> params);
}
