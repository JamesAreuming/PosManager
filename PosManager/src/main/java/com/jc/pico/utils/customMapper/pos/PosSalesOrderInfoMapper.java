package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jc.pico.utils.bean.PosSalesOrderInfo;

/**
 * POS 연동 10. S_ORDER_INFO : 주문정보 MyBatis Mapper
 * 2016. 7. 23, green, create
 * 
 * @author green
 *
 */
public interface PosSalesOrderInfoMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesOrderInfo> selectDefault(Map<String, Object> params);

	/**
	 * Order 기록 쓰기락 설정
	 */
	public boolean lockSaveOrder(@Param("storeId") Long storeId, @Param("orderNo") String orderNo);

	/**
	 * Order 기록 쓰기락 해제
	 */
	public boolean unlockSaveOrder(@Param("storeId") Long storeId, @Param("orderNo") String orderNo);

}
