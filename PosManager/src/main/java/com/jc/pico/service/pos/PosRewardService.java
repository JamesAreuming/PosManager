package com.jc.pico.service.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.exception.SettingNotFoundException;
import com.jc.pico.utils.bean.PosCouponInfo;
import com.jc.pico.utils.bean.PosStampSave;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcSalesExtended;

/**
 * 포스 연동 쿠폰/스탬프 처리 서비스
 * 
 * @author hyo
 *
 */
public interface PosRewardService {

	/**
	 * 스탬프 적립 (사후적립)
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 * 		STAMP_COUNT : 저장된 스탬프 갯수
	 * 		
	 * @throws SettingNotFoundException
	 * @throws RequestResolveException
	 */
	SingleMap saveStamp(PosStampSave param, Map<String, Object> posInfo) throws SettingNotFoundException, RequestResolveException;

	/**
	 * 쿠폰 정보 조회 및 유효성 검사
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 * @throws InvalidJsonException
	 * @throws RequestResolveException
	 */
	List<PosCouponInfo> getCouponInfoWithValidation(SingleMap param, Map<String, Object> posInfo)
			throws InvalidJsonException, RequestResolveException;

	/**
	 * 매출로 부터 쿠폰 및 스캠프를 발급 / 취소 / 사용 처리 한다.
	 * 
	 * @param sales
	 * @throws SettingNotFoundException
	 * @throws RequestResolveException
	 */
	void resolveRewardConsume(SvcSalesExtended sales) throws SettingNotFoundException, RequestResolveException;

}
