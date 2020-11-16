package com.jc.pico.utils.customMapper.pos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jc.pico.utils.bean.PosResultStoreInfo;

/**
 * 포스 연동 쿠폰 정보 조회
 * PosRewardMapper.xml
 * 
 * 2016. 09. 06 create, hyo
 * 
 * @author hyo
 *
 */
public interface PosRewardMapper {

	/**
	 * 상점 정보 조회
	 * 
	 * @param storeIds
	 * @return
	 */
	List<PosResultStoreInfo> selectCouponStoreInfoList(@Param("storeIds") List<Long> storeIds);

	/**
	 * 매출로 부터 발급 가능한 스탬프 갯수
	 * 
	 * @param salesId
	 * @return
	 */
	int selectStampIssuableCount(long salesId);

}
