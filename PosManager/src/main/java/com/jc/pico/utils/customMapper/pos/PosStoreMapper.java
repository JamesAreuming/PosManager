/*
 * Filename	: CustomStoreMapper.java
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

import com.jc.pico.bean.SvcStoreSet;

/**
 * POS 연동을 위한 커스텀 Mybatis 매퍼
 * 
 * @author green
 *
 */
public interface PosStoreMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<Object> selectStoreInfos(Map<String, Object> params);

	/**
	 * 해당 매장의 셀프 오더 사용 활성화 여부
	 * 
	 * @param storeId
	 * @return
	 */
	public Boolean selectUseSelfOrder(Long storeId);

	/**
	 * 서비스 중인 상점 ID 목록
	 * 
	 * @return
	 */
	List<Long> selectStoreIdListByActivated();

	
	/**
	 * 매장의 PG 결제 설정 정보 조회
	 * 매장 PG 설정을 사용하도록 되어 있으면 매장의 설정 정보를 아니면 브랜드 PG 정보를 반환. 
	 * @param storeId
	 * @return PG 관련 설정
	 */
	SvcStoreSet selectPaymentGatewayStoreSet(Long storeId);
}
