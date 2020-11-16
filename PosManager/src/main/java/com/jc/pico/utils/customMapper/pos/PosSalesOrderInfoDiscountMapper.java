package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosSalesOrderInfoDiscount;

/**
 * POS 연동 10. S_ORDER_INFO : 주문정보 MyBatis Mapper
 * 2016. 7. 23, green, create
 * @author green
 *
 */
public interface PosSalesOrderInfoDiscountMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesOrderInfoDiscount> selectDefault(Map<String, Object> params);
}
