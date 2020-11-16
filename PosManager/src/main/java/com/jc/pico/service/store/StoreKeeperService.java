package com.jc.pico.service.store;

import com.jc.pico.utils.bean.SingleMap;

public interface StoreKeeperService {

	/**
	 * 매장 CCTV 목록 조회
	 * 
	 * @param param
	 *            storeId : 스토어ID
	 * @return
	 */
	SingleMap getStoreCctvList(SingleMap param);

	/**
	 * 매장 이벤트 로그 목록 조회
	 * 
	 * @param param
	 * @return
	 */
	SingleMap getStoreEventList(SingleMap param);

}
