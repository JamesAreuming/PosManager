package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosSalesTableOrderDetail;

/**
 * POS 연동 9. S_TABLE_ORDER : 테이블 주문정보 MyBatis Mapper
 * 2016. 7. 21, green, create
 * @author green
 *
 */
public interface PosSalesTableOrderDetailMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesTableOrderDetail> selectDefault(Map<String, Object> params);
}
