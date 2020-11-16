package com.jc.pico.utils.customMapper.pos;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosMasterCodeInfo;
import com.jc.pico.utils.bean.PosMasterTableInfo;

/**
 * POS 연동 20. M_TABLE_INFO : 테이블 정보 MyBatis Mapper
 * 2016. 7. 21, green, create
 * 
 * @author green
 *
 */
public interface PosMasterTableInfoMapper {
	/**
	 * 매장 ID 로 매장정보를 가져오는 매퍼 메쏘드
	 * 
	 * @param params
	 *            파라미터...
	 * @return 매장정보
	 */
	public List<PosMasterTableInfo> selectDefault(Map<String, Object> params);

	/**
	 * 테이블 섹션 정보를 CodeInfo 에 프로젝션하여 조회한다.
	 * 
	 * @param params
	 *            companyId : 프랜차이즈 ID
	 *            storeId : 브랜드 ID
	 * 
	 * @return
	 */
	public List<PosMasterCodeInfo> selectTableSectionCodeInfoByStoreId(Map<String, Object> params);

	/**
	 * 셀프 오더 주문 처리를 위한 가상의 테이블 정보 추가
	 * 첫번째 table section에 포함된 테이블 정보를 생성하여 반환한다.
	 * 
	 * @param param
	 *            compayId
	 *            storeId
	 *            tableId 가상 테이블에 부여할 ID
	 * @return
	 */
	public Collection<? extends PosMasterTableInfo> selectSelfOrderTableInfo(Map<String, Object> param);
}
