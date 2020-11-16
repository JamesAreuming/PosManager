package com.jc.pico.service.pos;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.PosMasterCodeInfo;
import com.jc.pico.utils.bean.PosMasterGoodsOpt;
import com.jc.pico.utils.bean.PosMasterKitchenMsg;
import com.jc.pico.utils.bean.PosMasterTableInfo;
import com.jc.pico.utils.bean.PosPrinterInfo;
import com.jc.pico.utils.bean.SingleMap;

public interface PosBaseService {

	List<PosMasterGoodsOpt> getPosMasterGoodsOpts(String cdGoods);

	Map<String, Object> checkPermissionAndGetPosInfo(Authentication authentication, String cdCompany, String cdStore) throws RequestResolveException;

	void replaceTableSectionInfoTo(List<PosMasterCodeInfo> posMasterCodeInfos, Long companyId, Long storeId);

	/**
	 * 셀프 오더 주문 처리용 테이블정보 추가 (가상의 테이블
	 * 
	 * @param posMasterTableInfos
	 * @param companyId
	 * @param storeId
	 */
	void appendSelfOrderTableInfoTo(List<PosMasterTableInfo> posMasterTableInfos, Long companyId, Long storeId);

	/**
	 * 포스에서 사용할 코드 정보를 조회한다.
	 * 800~999 번대가 아닌 코드를 조회
	 * 
	 * @param storeId
	 * @param companyId
	 * @return
	 */
	List<PosMasterCodeInfo> getCodeInfoList(Long storeId, Long companyId);

	/**
	 * 프린터 정보 조회
	 * 
	 * @param param
	 *            update_num
	 *            username
	 * @return
	 * @throws RequestResolveException
	 */
	List<PosPrinterInfo> getPrinterInfoList(SingleMap param) throws RequestResolveException;

	/**
	 * 포스 라이센스 등록
	 * 
	 * @param param
	 * @return 
	 * @throws RequestResolveException 
	 */
	SingleMap registerDeviceLicense(SingleMap param) throws RequestResolveException;

	/**
	 * 포스가 설정한 락을 해제 한다
	 * @param storeId
	 * @param posNo
	 */
	void releaseTableLock(Long storeId, String posNo);

	/**
	 * 상점별 주방 메모를 조회 한다.
	 * 
	 * @param storeId
	 * @return
	 */
	List<PosMasterKitchenMsg> getKitchenMessageByStoreId(Long storeId);

}
