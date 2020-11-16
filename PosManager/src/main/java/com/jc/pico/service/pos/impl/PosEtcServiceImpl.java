package com.jc.pico.service.pos.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jc.pico.bean.SvcApp;
import com.jc.pico.bean.SvcAppExample;
import com.jc.pico.mapper.SvcAppMapper;
import com.jc.pico.service.pos.PosEtcService;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.pos.PosEtcMapper;

@Service
public class PosEtcServiceImpl implements PosEtcService {

	/**
	 * 서비스 ID : BAK
	 */
	private static final long SERVICE_ID_BAK = 1;

	private static final RowBounds ROW_BOUNDS_JUST_FIRST = new RowBounds(0, 1);

	@Autowired
	private CodeUtil codeUtil;

	@Autowired
	private SvcAppMapper svcAppMapper;

	@Autowired
	private PosEtcMapper posEtcMapper;

	@Override
	public List<SingleMap> getAppInfo(SingleMap param) {
		final String osType = codeUtil.getBaseCodeByAlias(param.getString("OS"));
		final int versionCode = param.getInt("VERSION_CODE");
		final String appType = param.getString("APP_TP");

		if (StringUtils.isEmpty(osType)) {
			throw new InvalidParameterException("Unknown os. " + param.get("OS"));
		}

		List<SingleMap> result = new ArrayList<>();

		SingleMap info = new SingleMap();
		info.put("APP_UPDATE_INFO", getAppUpdateInfo(appType, osType, versionCode));
		// info.put("추가정보", 추가정보 조회);

		result.add(info);
		return result;
	}

	private SingleMap getAppUpdateInfo(String appType, String osType, int versionCode) {
		SvcApp appInfo = getAppInfo(appType, osType, versionCode);
		if (appInfo == null) {
			appInfo = new SvcApp();
			appInfo.setVersion("Unknown");
			appInfo.setVersionCode(-1);
			appInfo.setIsStrictUpdate(false);
			appInfo.setUseSwitchLang(false);
			appInfo.setPath("");
		}

		int latestVersionCode = appInfo.getVersionCode();

		SingleMap updateInfo = new SingleMap();
		updateInfo.put("VERSION_NAME", appInfo.getVersion());
		updateInfo.put("VERSION_CODE", latestVersionCode);
		updateInfo.put("IS_UPDATABLE", latestVersionCode > versionCode);
		updateInfo.put("IS_STRICT", getHasStrictUpdate(appType, osType, versionCode, latestVersionCode)); // 강제 유무, true이면 반드시 업데이트 해야 함.
		updateInfo.put("URL_APK", appInfo.getPath());

		return updateInfo;
	}

	private SvcApp getAppInfo(String appType, String osType, int versionCode) {
		SvcAppExample example = new SvcAppExample();
		example.createCriteria() // 검색 조건
				.andAppTpEqualTo(appType) // 앱 종류
				.andOsTpEqualTo(osType) // os 종류
				.andUseYnEqualTo(true) // 활성화된 버전 정보
				.andServiceIdEqualTo(SERVICE_ID_BAK); // 서비스 번호
		example.setOrderByClause("VERSION_CODE DESC"); // 버전이 가장 높은 것

		List<SvcApp> apps = svcAppMapper.selectByExampleWithRowbounds(example, ROW_BOUNDS_JUST_FIRST);
		return apps.size() > 0 ? apps.get(0) : null;
	}

	/**
	 * 현재 버전 이후로 강제 업데이트 버전이 있는지 확인
	 * 
	 * 
	 * @param appType
	 *            앱 종류
	 * @param osType
	 *            os 종류
	 * @param versionCode
	 *            앱 현재 버전 코드
	 * @param latestVersionCode
	 *            최신 버전 코드
	 * @return 강제 업데이트가 존재하면 true, 아니면 false
	 */
	private boolean getHasStrictUpdate(String appType, String osType, int versionCode, int latestVersionCode) {

		SvcAppExample example = new SvcAppExample();
		example.createCriteria() // 검색 조건
				.andAppTpEqualTo(appType) // 앱 종류
				.andOsTpEqualTo(osType) // os 종류
				.andVersionCodeGreaterThan(versionCode) // 현재 앱 버전 초과 ~
				.andVersionCodeLessThanOrEqualTo(latestVersionCode) // ~ 최신 버전 이하
				.andIsStrictUpdateEqualTo(true) // 강제 업데이트 존재?
				.andServiceIdEqualTo(SERVICE_ID_BAK); // 서비스 번호

		// 조회 결과가 있으면 강제 업데이트가 있음
		return svcAppMapper.selectByExample(example).size() > 0;
	}

	@Override
	public void updateStoreAlive(SingleMap param) {
		// 갱신 해보고 없으면 새로 입력.
		if (posEtcMapper.updateStoreAlive(param) == 0) {
			posEtcMapper.insertStoreAlive(param);
		}
	}

	/**
	 * 포스가 활성화 되어 있는지 확인한다.
	 */
	@Override
	public boolean isAliveStore(long storeId) {
		// TODO 현재 1분 주기로 포스에서 생존 보고함. 주기 + 30초까지 유효한것을 보도록 함
		// 추후에 설정으로 조정할 수 있애 해야함.
		
		return true;
		
		// 수정 : 
		//  2020.01.08
		// 포스 상태를 검사하지 않는다. 키오스크
		//return posEtcMapper.selectAliveStoreCount(storeId, 60 + 30 /* sec */) > 0;
	}

}
