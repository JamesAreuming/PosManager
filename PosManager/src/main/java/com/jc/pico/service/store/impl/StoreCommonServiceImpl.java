package com.jc.pico.service.store.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.naming.NoPermissionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcApp;
import com.jc.pico.bean.SvcAppExample;
import com.jc.pico.bean.SvcDeviceLicense;
import com.jc.pico.bean.SvcDevicePushSet;
import com.jc.pico.bean.SvcDevicePushSetExample;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserDeviceExample;
import com.jc.pico.bean.UserExample;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcAppMapper;
import com.jc.pico.mapper.SvcDevicePushSetMapper;
import com.jc.pico.mapper.SvcStorePrinterMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.store.StoreCommonService;
import com.jc.pico.utils.AuthenticationUtils;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StaffUserDetail;
import com.jc.pico.utils.bean.StoreParam;
import com.jc.pico.utils.bean.StoreResult;
import com.jc.pico.utils.customMapper.store.StoreCommonMapper;

@Service
public class StoreCommonServiceImpl implements StoreCommonService {

	private static Logger logger = LoggerFactory.getLogger(StoreCommonServiceImpl.class);

	/**
	 * 앱 종류 : 스토어
	 */
	public static final String APP_TYPE_STORE = "108002";

	/**
	 * 앱 종류 : Clerk
	 */
	public static final String APP_TYPE_CLERK = "108003";

	/**
	 * 앱 종류 : Tab
	 */

	public static final String APP_TYPE_TAB = "108004";

	/**
	 * OS 종류 : Android
	 */
	public static final String OS_TP_ANDROID = "103001";

	/**
	 * OS 종류 : iOS
	 */
	public static final String OS_TP_IOS = "103002";

	/**
	 * 서비스 ID : BAK
	 */
	private static final long SERVICE_ID_BAK = 1;

	private static final RowBounds ROW_BOUNDS_JUST_FIRST = new RowBounds(0, 1);

	@Autowired
	private StoreCommonMapper commonMapper;

	@Autowired
	private UserDeviceMapper userDeviceMapper;

	@Autowired
	private CodeUtil codeUtil;

	@Autowired
	private UserMapper userMapeer;

	@Autowired
	private SvcAppMapper svcAppMapper;

	@Autowired
	private SvcDevicePushSetMapper svcDevicePushMapper;
	
	@Autowired
	private SvcStorePrinterMapper svcStorePrinterMapper;

	private BCryptPasswordEncoder bcryptPasswordEncoder;
	private ObjectMapper objectMapper;

	/**
	 * 브랜드별 소숫점 자리 캐싱
	 */
	private Map<Long, Integer> brandFractionDigitsCache;

	@PostConstruct
	public void init() {
		bcryptPasswordEncoder = new BCryptPasswordEncoder();
		objectMapper = new ObjectMapper();
		brandFractionDigitsCache = Collections.synchronizedMap(new HashMap<Long, Integer>());
	}

	@Override
	public SingleMap getAppInfo(SingleMap param) throws RequestResolveException {

		String osType = codeUtil.getBaseCodeByAlias(param.getString("os"));
		String appType = APP_TYPE_STORE;
		int versionCode = param.getInt("versionCode", 1); // FIXME 앱 수정 완료되면 default value 제거

		if (StringUtils.isEmpty(osType)) {
			throw new InvalidParameterException("Unknown os type. " + param.get("os"));
		}

		if (StringUtils.isEmpty(appType)) {
			throw new InvalidParameterException("Unknown app type. app-tp-store");
		}

		return getAppUpdateInfo(appType, osType, versionCode);
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

	private SingleMap getAppUpdateInfo(String appType, String osType, int versionCode) {
		SvcApp appInfo = getAppInfo(appType, osType, versionCode);
		if (appInfo == null) {
			appInfo = new SvcApp();
			appInfo.setVersion("Unknown");
			appInfo.setVersionCode(-1);
			appInfo.setIsStrictUpdate(false);
			appInfo.setUseSwitchLang(false);
		}

		int latestVersionCode = appInfo.getVersionCode();

		SingleMap updateInfo = new SingleMap();
		updateInfo.put("versionName", appInfo.getVersion());
		updateInfo.put("versionCode", latestVersionCode);
		updateInfo.put("isUpdatable", latestVersionCode > versionCode);
		updateInfo.put("isLangSwitch", appInfo.getUseSwitchLang());
		updateInfo.put("isStrict", getHasStrictUpdate(appType, osType, versionCode, latestVersionCode)); // 강제 유무, true이면 반드시 업데이트 해야 함.

		return updateInfo;
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
	public SingleMap getBrandListByFranId(SingleMap param) {
		SingleMap result = new SingleMap();
		result.put("items", commonMapper.selectBrandListByFrndId(param));
		return result;
	}

	@Override
	public SingleMap getStoreListByBrandId(SingleMap param) {
		SingleMap result = new SingleMap();
		result.put("items", commonMapper.selectStoreListByBrandId(param));
		return result;
	}

	@Override
	public SingleMap getStoreServiceListByStoreId(SingleMap param) {
		SingleMap result = new SingleMap();
		result.put("items", commonMapper.getStoreServiceListByStoreId(param));
		return result;
	}

	@Override
	public SingleMap getUserDetail(SingleMap param) {

		SingleMap result = new SingleMap();

		SingleMap userMapping = commonMapper.selectUserMappingByUserName(param);

		if (userMapping == null) {
			return result;
		}

		final String role = userMapping.getString("role");
		result.put("role", role);

		// 프랜차이즈 권한 
		if ("fran".equals(role)) {
			result.put("franchise", commonMapper.selectFranchiseInfoByFranId(userMapping));
			return result;
		}

		// 브랜드 권한 
		if ("brand".equals(role)) {
			result.put("franchise", commonMapper.selectFranchiseInfoByFranId(userMapping));
			result.put("brand", commonMapper.selectBrandInfoByBrandId(userMapping));
			result.put("currency", commonMapper.selectCurrencyByBrandId(userMapping));
			return result;
		}

		// 스토어 권한
		if ("store".equals(role)) {
			result.put("franchise", commonMapper.selectFranchiseInfoByFranId(userMapping));
			result.put("brand", commonMapper.selectBrandInfoByBrandId(userMapping));
			result.put("store", commonMapper.selectStoreInfoByStoreId(userMapping));
			result.put("currency", commonMapper.selectCurrencyByStoreId(userMapping));
			return result;
		}

		return result;
	}

	@Override
	public SingleMap addUserDevice(SingleMap param) throws NoPermissionException {

		// 기존 push id 중복 제거
		if (param.hasValue("pushId")) {
			setDisablePushDievce(param.getString("pushId"));
		}

		param.put("userId", getUserIdByUserName(param.getString("userName")));

		if (!param.hasValue("userId")) {
			logger.warn("No permission user request. " + param.getString("userName"));
			throw new NoPermissionException("Only use the store manager.");
		}

		// deviceId가 없으면 생성
		if (StringUtils.isEmpty(param.getString("deviceId", null))) {
			// make device id
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			String deviceId = encoder.encodePassword(String.format("%s-%s", param.getString("os"), param.getString("uuid")), "store");
			param.put("deviceId", deviceId);
		}

		// 등록된 디바이스인지 확인
		UserDevice userDevice = getUserDeviceByDeviceId(param.getString("deviceId"), false);

		if (userDevice == null) {
			// 신규 디바이스 등록
			return addPushDeviceNewer(param);
		} else {
			// 기존 디바이스 갱신
			return updatePushDevice(param, userDevice);
		}
	}

	/**
	 * UserName으로 UserId를 조회
	 * 
	 * @param userName
	 * @return
	 */
	private Long getUserIdByUserName(String userName) {
		User user = getUserByUserName(userName);
		return user != null ? user.getId() : null;
	}

	/**
	 * UserName으로 사용자 정보를 조회한다.
	 * 
	 * @param userName
	 * @return
	 */
	private User getUserByUserName(String userName) {
		UserExample args = new UserExample();
		args.createCriteria() // 검색 조건 
				.andUsernameEqualTo(userName) // 사용자 로그인
				.andTypeEqualTo("300006") // 스토어 앱 사용자 
				.andStatusEqualTo("301001"); // 계정 상태 정상
		List<User> users = userMapeer.selectByExample(args);
		return users.size() > 0 ? users.get(0) : null;
	}

	/**
	 * 디바이스 정보를 갱신한다. (deviceId를 기준으로 갱신 처리함)
	 * 
	 * @param param
	 * @param oldUserDevice
	 * @return
	 */
	private SingleMap updatePushDevice(SingleMap param, UserDevice oldUserDevice) {
		UserDevice updateDevice = new UserDevice();
		updateDevice.setId(oldUserDevice.getId());
		updateDevice.setOs(codeUtil.getBaseCodeByAlias(param.getString("os")));
		updateDevice.setUserId(param.getLong("userId"));
		updateDevice.setUuid(param.getString("uuid"));
		updateDevice.setPushId(param.getString("pushId"));
		updateDevice.setIsAlive(true);

		userDeviceMapper.updateByPrimaryKeySelective(updateDevice);

		SingleMap result = new SingleMap();
		result.put("deviceId", oldUserDevice.getDeviceId());
		return result;
	}

	/**
	 * 디바이스ID 발급 후 디바이스 정보를 저장함.
	 * 
	 * @param param
	 * @return
	 */
	private SingleMap addPushDeviceNewer(SingleMap param) {
		UserDevice device = new UserDevice();
		device.setOs(codeUtil.getBaseCodeByAlias(param.getString("os")));
		device.setUserId(param.getLong("userId"));
		device.setUuid(param.getString("uuid"));
		device.setPushId(param.getString("pushId"));
		device.setDeviceId(param.getString("deviceId"));
		device.setIsAlive(true);

		userDeviceMapper.insertSelective(device);
		insertDevicePushSet(device.getId());

		SingleMap result = new SingleMap();
		result.put("deviceId", device.getDeviceId());
		return result;
	}

	private void insertDevicePushSet(Long deviceId) {
		SvcDevicePushSet record = new SvcDevicePushSet();
		record.setDeviceId(deviceId);
		record.setIsPushOrderCancel(true);
		record.setIsPushPayment(true);
		record.setIsPushPaymentCancel(true);
		record.setIsPushRefund(true);
		svcDevicePushMapper.insertSelective(record);
	}

	/**
	 * 해당 푸시 디바이스를 비활성화 한다
	 * 
	 * @param pushId
	 * @param uuid
	 */
	private void setDisablePushDievce(String pushId) {
		UserDeviceExample example = new UserDeviceExample();
		example.createCriteria().andPushIdEqualTo(pushId);

		UserDevice record = new UserDevice();
		record.setIsAlive(false);
		record.setPushId("");
		userDeviceMapper.updateByExampleSelective(record, example);
	}

	@Override
	public SingleMap getUserSettings(SingleMap param) throws NoPermissionException, RequestResolveException {

		UserDevice userDevice = getUserDeviceByDeviceId(param.getString("deviceId"), true);
		if (userDevice == null) {
			throw new RequestResolveException(StoreResult.ErrorCode.NOT_REGISTERED_DEVICE.code,
					"Not registered device. [" + param.getString("deviceId") + "]");
		}

		Long userId = getUserIdByUserName(param.getString("userName"));
		if (!Objects.equals(userDevice.getUserId(), userId)) {
			throw new NoPermissionException("Dose not match user. deviceUserId=" + userDevice.getUserId() + " userId=" + userId);
		}

		SvcDevicePushSetExample example = new SvcDevicePushSetExample();
		example.createCriteria() // 조건
				.andDeviceIdEqualTo(userDevice.getId()); // 해당 디바이스
		List<SvcDevicePushSet> settingList = svcDevicePushMapper.selectByExample(example);

		SingleMap settings = settingList.size() > 0 ? objectMapper.convertValue(settingList.get(0), SingleMap.class) : new SingleMap();
		settings.remove("id");
		settings.remove("deviceId");
		settings.remove("created");
		settings.remove("updated");
		settings.put("lang", userDevice.getLocale());

		SingleMap result = new SingleMap();
		result.put("settings", settings);
		return result;
	}

	@Override
	public SingleMap setUserSettings(SingleMap param) throws NoPermissionException, RequestResolveException {

		UserDevice userDevice = getUserDeviceByDeviceId(param.getString("deviceId"), true);
		if (userDevice == null) {
			throw new RequestResolveException(StoreResult.ErrorCode.NOT_REGISTERED_DEVICE.code,
					"Not registered device. [" + param.getString("deviceId") + "]");
		}

		Long userId = getUserIdByUserName(param.getString("userName"));
		if (!Objects.equals(userDevice.getUserId(), userId)) {
			throw new NoPermissionException("Dose not match user. deviceUserId=" + userDevice.getUserId() + " userId=" + userId);
		}

		SvcDevicePushSet record = toSvcDevicePushSet(param);

		SvcDevicePushSetExample example = new SvcDevicePushSetExample();
		example.createCriteria() // 조건
				.andDeviceIdEqualTo(userDevice.getId());

		List<SvcDevicePushSet> settingList = svcDevicePushMapper.selectByExample(example);
		if (settingList.size() == 0) {
			// 최초 설정시 insert
			record.setDeviceId(userDevice.getId());
			svcDevicePushMapper.insertSelective(record);
		} else {
			// 존재하면 갱신
			svcDevicePushMapper.updateByExampleSelective(record, example);
		}

		// 언어 설정 변경
		String lang = param.getString("lang", null);
		if (!StringUtils.isEmpty(lang)) {
			UserDevice userDeviceRecord = new UserDevice();
			userDeviceRecord.setId(userDevice.getId());
			userDeviceRecord.setLocale(lang);
			userDeviceMapper.updateByPrimaryKeySelective(userDeviceRecord);
			userDevice.setLocale(lang);
		}

		// 다시 조회 하여 전달
		settingList = svcDevicePushMapper.selectByExample(example);

		SingleMap settings = settingList.size() > 0 ? objectMapper.convertValue(settingList.get(0), SingleMap.class) : new SingleMap();
		settings.remove("id");
		settings.remove("deviceId");
		settings.remove("created");
		settings.remove("updated");
		settings.put("lang", userDevice.getLocale());

		SingleMap result = new SingleMap();
		result.put("settings", settings);
		return result;
	}

	private static SvcDevicePushSet toSvcDevicePushSet(SingleMap param) {
		SvcDevicePushSet result = new SvcDevicePushSet();

		if (param.hasValue("isPushOrderCancel")) {
			result.setIsPushOrderCancel(param.getBoolean("isPushOrderCancel", null));
		}

		if (param.hasValue("isPushPayment")) {
			result.setIsPushPayment(param.getBoolean("isPushPayment", null));
		}

		if (param.hasValue("isPushPaymentCancel")) {
			result.setIsPushPaymentCancel(param.getBoolean("isPushPaymentCancel", null));
		}

		if (param.hasValue("isPushRefund")) {
			result.setIsPushRefund(param.getBoolean("isPushRefund", null));
		}

		if (param.hasValue("isPushVisitVip")) {
			result.setIsPushVisitVip(param.getBoolean("isPushVisitVip", null));
		}

		return result;
	}

	private UserDevice getUserDeviceByDeviceId(String deviceId, boolean onlyAlive) {
		if (StringUtils.isEmpty(deviceId)) {
			return null;
		}
		UserDeviceExample example = new UserDeviceExample();
		UserDeviceExample.Criteria criteria = example.createCriteria() // 조건
				.andDeviceIdEqualTo(deviceId); // device id를 기준으로 검색
		if (onlyAlive) {
			criteria.andIsAliveEqualTo(true); // 활성화된것 확인
		}
		example.setOrderByClause("UPDATED DESC"); // 최신 정보 조회를 위해 정렬
		List<UserDevice> devices = userDeviceMapper.selectByExample(example);
		return devices.size() > 0 ? devices.get(0) : null;
	}

	@Override
	public SingleMap setUserPassword(SingleMap param) throws NoPermissionException {
		if (!param.hasValue("password")) {
			throw new InvalidParameterException("password is empty.");
		}
		if (!param.hasValue("newPassword")) {
			throw new InvalidParameterException("newPassword is empty.");
		}
		if (!param.hasValue("userName")) {
			throw new NoPermissionException("Not auth user.");
		}
		User user = getUserByUserName(param.getString("userName"));
		if (user == null) {
			throw new NoPermissionException("Invalid username or password.");
		}

		String password = param.getString("password");
		String newPassword = param.getString("newPassword");

		if (!bcryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new NoPermissionException("Invalid username or password.");
		}

		User record = new User();
		record.setId(user.getId());
		record.setPassword(newPassword); // 어디선가 암호화 처리 하는듯.. plain으로 넘겨야 함.

		userMapeer.updateByPrimaryKeySelective(record);

		SingleMap result = new SingleMap();
		return result;
	}

	@Override
	public void updateUserDeviceOwner(String deviceId, long userId) {

		UserDeviceExample example = new UserDeviceExample();
		example.createCriteria() // 조건
				.andDeviceIdEqualTo(deviceId); // 서버에서 발급한 전체 유니크의 디바이스ID

		// user id 만 갱신
		UserDevice record = new UserDevice();
		record.setUserId(userId);

		userDeviceMapper.updateByExampleSelective(record, example);
	}

	@Override
	public int getBrandFractionDigits(SingleMap param) {
		long brandId = param.getLong("brandId", 0l);
		long storeId = param.getLong("storeId", 0l);

		// 스토어 ID로 부터 브랜드ID 조회
		if (brandId == 0 && storeId != 0) {
			Long storeBrandId = commonMapper.selectBrandIdByStoreId(storeId);
			if (storeBrandId != null) {
				brandId = storeBrandId;
			}
		}

		// 브랜드 ID가 없으면 기본 0 반환
		if (brandId == 0) {
			return 0;
		}

		// 캐싱된게 있으면 반환
		Integer result = brandFractionDigitsCache.get(brandId);
		if (result != null) {
			return result;
		}

		// 브랜드 통화를 조회하여 캐싱후 반환
		SingleMap currencyParam = new SingleMap();
		currencyParam.put("brandId", brandId);
		String currencyCode = commonMapper.selectCurrencyByBrandId(currencyParam);
		Currency currency = Currency.getInstance(currencyCode);
		brandFractionDigitsCache.put(brandId, currency.getDefaultFractionDigits());

		return currency.getDefaultFractionDigits();
	}

	@Override
	public boolean uploadLogFile(MultipartHttpServletRequest req, MultipartFile[] files) throws IOException {
		StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		SingleMap map = new SingleMap();
		
		map.put("brandId", staffUserDetail.getBrandId());
		map.put("storeId", staffUserDetail.getStoreId());
		map.put("licenseId", staffUserDetail.getLicenseId());
		SvcDeviceLicense svcDeviceLicense = commonMapper.selectPosNoByLicenseId(map);
		
		Date date = new Date();
		String file_path = Globals.LOG_RESOURCE + "/";
		
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getOriginalFilename();
			String folderName = fileName.substring(fileName.indexOf("_")+1, fileName.indexOf("_")+7);
			String save_file_name = fileName.substring(0, fileName.indexOf(".")) + "_" + svcDeviceLicense.getStoreId() + "_" + svcDeviceLicense.getPosNo() + fileName.substring(fileName.lastIndexOf("."));
			
			byte[] bytes = files[i].getBytes();
			File Folder = new File(file_path + folderName);
			File lOutFile = new File(file_path + folderName + "/" + save_file_name);

			if (!Folder.isDirectory())
				Folder.mkdirs();

			if (lOutFile.isFile()) {
				if (lOutFile.delete()) {
					FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
					lFileOutputStream.write(bytes);
					lFileOutputStream.close();
				}
			} else {
				FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
				lFileOutputStream.write(bytes);
				lFileOutputStream.close();
			}

//			if (System.getProperty("os.name").contains("Linux")) {
//				String chmod = "chmod 777 " + file_path + folderName + "/" + save_file_name;
//
//				try {
//					Process p = Runtime.getRuntime().exec(chmod);
//					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//					String line = null;
//
//					while ((line = br.readLine()) != null) {
//						logger.info("commend line : " + line);
//					}
//				} catch (Exception e) {
//					logger.error(e.getMessage());
//				}
//			}
		}
		return true;
	}

	@Override
	public boolean updateStorePrinterIp(SingleMap data) {
		final StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		SingleMap map = new SingleMap();
		map.put("storeId",  staffUserDetail.getStoreId());
		map.put("brandId", staffUserDetail.getBrandId());
		map.put("ip", data.getString("ip"));
		return svcStorePrinterMapper.updateBySingleMap(map);
	}

}
