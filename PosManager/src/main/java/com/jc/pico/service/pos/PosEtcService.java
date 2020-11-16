package com.jc.pico.service.pos;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

/**
 * 포스 관련 기타 처리 서비스
 * 
 * @author hyo
 *
 */
public interface PosEtcService {

	/**
	 * 앱 정보 조회
	 * 
	 * @param param
	 *            VERSION_CODE : 현재 앱 버전 코드
	 * @return
	 * 
	 */
	List<SingleMap> getAppInfo(SingleMap param);

	/**
	 * 스토어 생존 보고
	 * 
	 * @param param
	 *            CD_COMPANY :프랜차이즈 ID
	 *            CD_STORE : 스토어 ID
	 */
	void updateStoreAlive(SingleMap param);

	boolean isAliveStore(long storeId);

}
