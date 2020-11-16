package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosSalesTableOrder;

/**
 * POS 연동 9. S_TABLE_ORDER : 테이블 주문정보 MyBatis Mapper
 * 2016. 7. 21, green, create
 * 
 * @author green
 *
 */
public interface PosSalesTableOrderMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<PosSalesTableOrder> selectDefault(Map<String, Object> params);

	/**
	 * 테이블에 설정된 주문 정보를 갱신한다.
	 * 
	 * @param params
	 *            id : 테이블id
	 *            orderId : 주문 id
	 *            posNo : 수정 요청한 포스 no
	 */
	public void updateOrderInfoById(Map<String, Object> params);

	/**
	 * 테이블을 리셋한다.
	 * 주문 정보를 제거 한다.
	 * 
	 * @param storeId
	 */
	public void updateOrderInfoResetByStoreId(Long storeId);
}
