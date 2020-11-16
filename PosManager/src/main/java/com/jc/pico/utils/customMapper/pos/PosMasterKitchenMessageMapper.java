/*
 * Filename	: PosMasterGoodsClassMapper.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosMasterKitchenMsg;

/**
 * POS 연동 33. M_KITCHEN_MSG : 주방 메모 조회
 * 2016. 12. 09
 *
 */
public interface PosMasterKitchenMessageMapper {
	/**
	 * 매장 ID 로 주방 메시지를 조회한다.
	 * 
	 * @param params
	 *            storeId
	 * @return 주방 메모 목록
	 */
	public List<PosMasterKitchenMsg> selectList(Map<String, Object> param);
}
