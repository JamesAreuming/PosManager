package com.jc.pico.utils.customMapper.pos;

import java.util.List;

import com.jc.pico.utils.bean.PosCashBookInfo;
import com.jc.pico.utils.bean.SingleMap;

/**
 * 
 * POS 연동	
 * 금전 출납부
 * PosCashbookMapper.xml
 * 
 * 2016. 8. 31 create, hyo
 * 
 * @author hyo
 *
 */
public interface PosCashbookMapper {

	/**
	 * 금전 출납 내역 조회
	 * 
	 * @param param
	 *            branchId : 브랜드 ID
	 *            storeId : 스토어 ID
	 *            startDt : 조회 시작일 yyyymmdd
	 *            endDt : 조회 종료일 yyyymmdd
	 * @return
	 */
	List<PosCashBookInfo> selectCashbookInfoList(SingleMap param);

}
