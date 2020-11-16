package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosSalesOrderMissing;

/**
 * POS 연동 12. S_ORDER_MISSING : 미 수신 주문 정보 조회 MyBatis Mapper
 * 2016. 8. 10, green, create
 * @author green
 *
 */
public interface PosSalesOrderMissingMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesOrderMissing> selectDefault(Map<String, Object> params);
}
