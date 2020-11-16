package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

/**
 * POS 연동 8. S_TABLE_STATUS_SAVE : 테이블 상태 변경 MyBatis Mapper
 * 2016. 7. 21, green, create
 * @author green
 *
 */
public interface PosSalesTableStatusSaveMapper {
	/**
	 * 특정 테이블의 상태값들을 읽어오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 특정 테이블의 상태정보
	 */
	public List<Map<String, Object>> selectCurrentTables(Map<String, Object> params);

	/**
	 * 특정 테이블의 데이터를 Update 한다.
	 * @param params 파라미터...
	 * @return Update된 테이블의 Rows Count
	 */
	public int updateCurrentTable(Map<String, Object> params);

	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * @param params 파라미터...
	 * @return 매장정보
	 */
	public List<Object> selectDefault(Map<String, Object> params);
}
