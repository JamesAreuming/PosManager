package com.jc.pico.service.pos.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcDeviceLicense;
import com.jc.pico.bean.SvcDeviceLicenseExample;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcStoreExample;
import com.jc.pico.bean.SvcTable;
import com.jc.pico.bean.SvcTableExample;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcDeviceLicenseMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcTableMapper;
import com.jc.pico.service.pos.PosBaseService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosMasterCodeInfo;
import com.jc.pico.utils.bean.PosMasterGoodsOpt;
import com.jc.pico.utils.bean.PosMasterKitchenMsg;
import com.jc.pico.utils.bean.PosMasterTableInfo;
import com.jc.pico.utils.bean.PosPrinterInfo;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.pos.PosMasterCodeInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterGoodsInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterKitchenMessageMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterTableInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosStoreMapper;
import com.jc.pico.utils.customMapper.pos.PosStorePrinterMapper;

@Service
public class PosBaseServiceImpl implements PosBaseService {

	public static final Logger logger = LoggerFactory.getLogger(PosBaseServiceImpl.class);

	public static final String LICENSE_STATUS_UNUSE = "354001";
	public static final String LICENSE_STATUS_USE = "354002";
	public static final String LICENSE_STATUS_END = "354003";
	public static final String LICENSE_STATUS_DSUSE = "354004";

	public static final int INSTALL_TYPE_NEW = 1;
	public static final int INSTALL_TYPE_RE = 2;

	@Autowired
	private PosMasterGoodsInfoMapper posMasterGoodsInfoMapper;

	@Autowired
	private PosMasterTableInfoMapper posMasterTableInfoMapper;

	@Autowired
	private PosStoreMapper posStoreMapper;

	@Autowired
	private PosMasterCodeInfoMapper posMasterCodeInfoMapper;

	@Autowired
	private PosStorePrinterMapper posStorePrinterMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Autowired
	private SvcDeviceLicenseMapper svcDeviceLicenseMapper;

	@Autowired
	private SvcTableMapper svcTableMapper;

	@Autowired
	private PosMasterKitchenMessageMapper posMasterKitchenMessageMapper;

	@Autowired
	private PosUtil posUtil;

	/**
	 * 포스에서 사용하는 공통 코드 중 미리 예약된 800 ~ 999 범위 밖의 코드
	 */
	private static final int[] POS_BASE_CODES = { 605, 606, 607 };

	/**
	 * 상품의 옵션과 옵션 상세를 조회하여 반환
	 */
	@Override
	public List<PosMasterGoodsOpt> getPosMasterGoodsOpts(String cdGoods) {
		Map<String, Object> params = new HashMap<>();
		params.put("itemId", cdGoods);

		List<PosMasterGoodsOpt> result = posMasterGoodsInfoMapper.selectOptListByItemId(params);

		params.clear();
		for (PosMasterGoodsOpt opt : result) {
			params.put("optId", opt.getNoOption());
			opt.setDetails(posMasterGoodsInfoMapper.getPosMasterGoodsOptDtlByOptId(params));
		}
		return result;
	}

	@Override
	public Map<String, Object> checkPermissionAndGetPosInfo(Authentication authentication, String cdCompany, String cdStore)
			throws RequestResolveException {

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RequestResolveException(PosUtil.EPO_0002_CODE, PosUtil.EPO_0002_MSG);
		}

		// 요청자와 요청 파라미터를 비교하여 권한이 있는지 확인한다.
		// franchise, store 가 일치하는지 확인
		String username = (authentication != null ? authentication.getName() : null);
		Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵
		Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
		Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

		final boolean hasPermission = storeId != null && storeId != 0 && storeId.toString().equals(cdStore) // store 확인  
				&& companyId != null && companyId != 0 && companyId.toString().equals(cdCompany); // brand 확인

		if (!hasPermission) {
			throw new RequestResolveException(PosUtil.EPO_0004_CODE, PosUtil.EPO_0004_MSG + " requested company=" + cdCompany + ", store=" + cdStore
					+ ", authenticated company=" + companyId + ", store=" + storeId);
		}

		// else 권한 있음!
		return posBaseKeyMap;
	}

	/**
	 * 코드 정보에에서 테이블그룹 정보를 갱신한다.
	 * 테이블 그룹 정보는 tb_svc_table_section에 존재하는 값을 사용한다.
	 * 
	 */
	@Override
	public void replaceTableSectionInfoTo(List<PosMasterCodeInfo> posMasterCodeInfos, Long companyId, Long storeId) {

		// 테이블 그룹 정보 삭제
		Iterator<PosMasterCodeInfo> iter = posMasterCodeInfos.iterator();
		while (iter.hasNext()) {
			PosMasterCodeInfo info = iter.next();
			if (info.getCdGroup() == 878) {
				iter.remove();
			}
		}

		// 테이블 섹션 정보로 교체 입력
		Map<String, Object> param = new HashMap<>();
		param.put("companyId", companyId);
		param.put("storeId", storeId);
		posMasterCodeInfos.addAll(posMasterTableInfoMapper.selectTableSectionCodeInfoByStoreId(param));
	}

	@Override
	public void appendSelfOrderTableInfoTo(List<PosMasterTableInfo> posMasterTableInfos, Long companyId, Long storeId) {
		if (!isUseSelfOrder(storeId)) {
			return;
		}
		Map<String, Object> param = new HashMap<>();
		param.put("companyId", companyId);
		param.put("storeId", storeId);
		param.put("tableId", Integer.MAX_VALUE);
		posMasterTableInfos.addAll(posMasterTableInfoMapper.selectSelfOrderTableInfo(param));
	}

	/**
	 * 셀프 오더를 사용하도록 설정된 매장인지 확인한다.
	 * 브랜드의 상점 설정 허용이 false면 브랜드의 셀프오더 설정을 사용
	 * true이면 삼성의 셀프 오더 설정 사용
	 * 
	 * @param storeId
	 * @return 사용하는 매장이면 true, 아니면 false
	 */
	private boolean isUseSelfOrder(Long storeId) {
		Boolean useSelftOrder = posStoreMapper.selectUseSelfOrder(storeId);
		return useSelftOrder != null && useSelftOrder;
	}

	@Override
	public List<PosMasterCodeInfo> getCodeInfoList(Long storeId, Long companyId) {

		SingleMap params = new SingleMap();
		params.put("storeId", storeId);
		params.put("companyId", companyId);
		List<PosMasterCodeInfo> posMasterCodeInfos = posMasterCodeInfoMapper.selectDefault(params);

		// 테이블 섹션 정보를 교체 함
		replaceTableSectionInfoTo(posMasterCodeInfos, companyId, storeId);

		// 그룹 코드를 포스용으로 전환, 800 ~ 999 => 0 ~ 199
		// CD_CODE == SUB_CD
		final TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId);
		for (PosMasterCodeInfo posMasterCodeInfo : posMasterCodeInfos) {
			posMasterCodeInfo.setCdGroup(posMasterCodeInfo.getCdGroup() + PosUtil.CD_GROUP_CORRECTION);
			posMasterCodeInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterCodeInfo.getDtInsert()));
			posMasterCodeInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterCodeInfo.getDtUpdate()));
		}

		//800 ~ 999 이외의 코드 중 포스에서 사용하는 코드
		// 서버의 코드를 그대로 사용, CD_CODE == BASE_CD
		params.put("mainCodes", POS_BASE_CODES);
		posMasterCodeInfos.addAll(posMasterCodeInfoMapper.selectManualList(params));

		return posMasterCodeInfos;
	}

	@Override
	public List<PosPrinterInfo> getPrinterInfoList(SingleMap param) throws RequestResolveException {
		Map<String, Object> posInfo = posUtil.getPosBasicInfo(param.getString("username")); // 매장/POS 정보키 맵
		if (posInfo == null) {
			throw new RequestResolveException(PosUtil.EPO_0004_CODE, "Invalid pos user. [" + param.getString("username") + "]");
		}

		Long storeId = (Long) (posInfo != null ? posInfo.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
		Long companyId = (Long) (posInfo != null ? posInfo.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
		param.put("storeId", storeId);
		param.put("companyId", companyId);
		return posStorePrinterMapper.selectList(param);
	}

	@Override
	public SingleMap registerDeviceLicense(SingleMap param) throws RequestResolveException {

		long userId = param.getLong("userId", 0l);

		String storeCode = param.getString("STR_CD");
		String licenseKey = param.getString("LICENSE_KEY");
		String posNo = param.getString("NO_POS");
		String deviceType = "876001"; // BAK_pos_pay 876001, BAK_pos_order 876002
		int installType = param.getInt("DS_INSTALL"); // 1: 신규설치, 2: 재설치
		String hwInfo = param.getString("HW_INFO");
//		String timeZone = param.getString("TIMEZONE"); 사용하지 않아 주석처리, 전달은 받고 있음.
//		int version = param.getInt("VERSION"); 사용하지 않아 주석 처리, 전달은 받고 있음

		Date nowDate = new Date();
		SvcStore store = getStoreByStoreCode(storeCode);

		if (store == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, storeCode + " code store is not found.");
		}

		if (userId == 0) {
			throw new RequestResolveException(PosUtil.EPO_0004_CODE, "Not agent user.");
		}

		// 라이센스 조회 
		SvcDeviceLicenseExample example = new SvcDeviceLicenseExample();
		example.createCriteria() //			
				.andLicenseKeyEqualTo(licenseKey) // 라이센스 코드
				.andBeginLessThanOrEqualTo(nowDate) // 유효 기간 확인 (시작일 <= 현재 <= 만료일) 
				.andEndGreaterThanOrEqualTo(nowDate) // 유효 기간 확인
				.andDeviceTpEqualTo(deviceType) // 디바이스 종류
				.andStatusIn(Arrays.asList(LICENSE_STATUS_UNUSE, LICENSE_STATUS_USE)); // 사용, 미사용 상태의 라이센스만 허용

		List<SvcDeviceLicense> licenses = svcDeviceLicenseMapper.selectByExample(example);
		if (licenses.size() == 0) {
			throw new RequestResolveException(PosUtil.EPO_0010_CODE_INVALID_LICENSE, "Invalid device license.");
		}

		// 라이센스 검증
		if (installType != INSTALL_TYPE_NEW && installType != INSTALL_TYPE_RE) {
			throw new RequestResolveException(PosUtil.EPO_0005_CODE, "Invalid installType paramter.");

		}
		SvcDeviceLicense license = licenses.get(0);
		// 신규 설치인데 라이센스가 사용된 미사용 상태가 아니면 에러
		if (installType == INSTALL_TYPE_NEW && !LICENSE_STATUS_UNUSE.equals(license.getStatus())) {
			throw new RequestResolveException(PosUtil.EPO_0010_CODE_INVALID_LICENSE, "Used license.");
		}
		// 재설치 인데 라이센스가 사용한 상태가 아니면 에러
		if (installType == INSTALL_TYPE_RE && !LICENSE_STATUS_USE.equals(license.getStatus())) {
			throw new RequestResolveException(PosUtil.EPO_0010_CODE_INVALID_LICENSE, "Unused license.");
		}

		// 포스번호, 등록자, 상태 갱신
		SvcDeviceLicense record = new SvcDeviceLicense();
		record.setId(license.getId());
		record.setBrandId(store.getBrandId());
		record.setStoreId(store.getId());
		record.setPosNo(posNo);
		record.setUserId(userId);
		record.setStatus(LICENSE_STATUS_USE); // 사용 상태로 변경
		record.setHwInfo(hwInfo);
		record.setCertTm(installType == INSTALL_TYPE_NEW ? new Date() : null); // 신규 셜치이면 등록일 기록

		// 갱신 처리
		svcDeviceLicenseMapper.updateByPrimaryKeySelective(record);

		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(store.getBrandId());

		SingleMap result = new SingleMap();
		result.put("CD_COMPANY", brand.getFranId());
		result.put("CD_STORE", store.getId());
		return result;
	}

	private SvcStore getStoreByStoreCode(String storeCode) {
		SvcStoreExample example = new SvcStoreExample();
		example.createCriteria() //
				.andStoreCdEqualTo(storeCode) //
				.andStoreStNotEqualTo("350003"); // 폐점 제외
		List<SvcStore> storeList = svcStoreMapper.selectByExample(example);
		return storeList.size() > 0 ? storeList.get(0) : null;
	}

	@Override
	public void releaseTableLock(Long storeId, String posNo) {
		SvcTableExample example = new SvcTableExample();
		example.createCriteria() // 해제 조건
				.andStoreIdEqualTo(storeId) // 해당 상점의
				.andPosNoEqualTo(posNo) // 포스가 설정한
				.andIsUsedEqualTo(true); // 락이 활성화된 것

		SvcTable record = new SvcTable();
		record.setIsUsed(false);

		svcTableMapper.updateByExampleSelective(record, example);
	}

	@Override
	public List<PosMasterKitchenMsg> getKitchenMessageByStoreId(Long storeId) {
		SingleMap param = new SingleMap();
		param.put("storeId", storeId);
		return posMasterKitchenMessageMapper.selectList(param);
	}

}
