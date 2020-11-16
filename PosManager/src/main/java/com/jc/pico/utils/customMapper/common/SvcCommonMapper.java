package com.jc.pico.utils.customMapper.common;

import java.util.List;

import com.jc.pico.utils.bean.SingleMap;

public interface SvcCommonMapper {

	/**
	 * 광고정보 가져오기
	 * @param param
	 * @return
	 */
	List<SingleMap> selectAdvertiseList(SingleMap param);
}
