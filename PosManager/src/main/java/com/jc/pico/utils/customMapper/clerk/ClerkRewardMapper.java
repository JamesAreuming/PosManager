package com.jc.pico.utils.customMapper.clerk;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jc.pico.utils.bean.SingleMap;

public interface ClerkRewardMapper {

	/**
	 * 상점 정보 조회
	 * 
	 * @param storeIds
	 * @return
	 */
	List<SingleMap> selectCouponStoreInfoList(@Param("storeIds") List<Long> storeIds);

}
