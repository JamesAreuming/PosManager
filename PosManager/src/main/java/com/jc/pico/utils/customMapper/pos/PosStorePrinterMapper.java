package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosPrinterInfo;

/**
 * 포스 연동 프린터 정보
 * 
 * @author hyo 2016.09.09
 *
 */
public interface PosStorePrinterMapper {

	/**
	 * 스토어의 프린터 정보 조회
	 * 
	 * @param param
	 *            storeId
	 *            companyId
	 * @return
	 */
	List<PosPrinterInfo> selectList(Map<String, Object> param);
	
	List<PosPrinterInfo> selectListWithLong(Long storeId);

}
