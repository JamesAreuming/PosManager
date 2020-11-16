package com.jc.pico.service.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosCashBookInfo;
import com.jc.pico.utils.bean.PosCashbookSave;
import com.jc.pico.utils.bean.PosStampSave;
import com.jc.pico.utils.bean.SingleMap;

/**
 * 출납부 처리 서비스
 * 
 * @author hyo
 *
 */
public interface PosCashbookService {

	/**
	 * 기간내 출납부 정보 조회
	 * 
	 * @param param
	 *            CD_COMPANY : 회사 코드
	 *            CD_STORE : 매장 코드
	 *            DT_START : 조회 시작일 yyyymmdd
	 *            DT_END : 조회 종료일 yyyymmdd
	 * @return
	 */
	List<PosCashBookInfo> getCashBookInfo(SingleMap param);

	/**
	 * 금전 출납부 저장
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 */
	long saveCashBook(PosCashbookSave param, Map<String, Object> posInfo);

}
