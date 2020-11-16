package com.jc.pico.utils.customMapper.pos;

import org.apache.ibatis.annotations.Param;

import com.jc.pico.utils.bean.SingleMap;

public interface PosEtcMapper {

	/**
	 * 매장 활성화 시간을 갱신한다.
	 * 
	 * @param param
	 * @return
	 */
	public int updateStoreAlive(SingleMap param);

	/**
	 * 새로운 매장 활성화 상태를 추가 한다
	 * 
	 * @param param
	 * @return
	 */
	public int insertStoreAlive(SingleMap param);

	/**
	 * 상점이 해당 현재 시간으로 부터 지정한 시간(초) 이내에 생존 보고를 한 적 있는지 확인한다.
	 * 
	 * @param storeId
	 *            상점
	 * @param period
	 *            유효 시간 (초)
	 * @return 생존보고 한적 있으면 양수, 없으면 0
	 */
	public int selectAliveStoreCount(@Param("storeId") long storeId, @Param("period") int period);

}
