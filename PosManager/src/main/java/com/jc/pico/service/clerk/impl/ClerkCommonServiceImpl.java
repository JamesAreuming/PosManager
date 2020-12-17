package com.jc.pico.service.clerk.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.jc.pico.bean.SvcApp;
import com.jc.pico.bean.SvcAppExample;
import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcClosingExample;
import com.jc.pico.bean.SvcClosingExample.Criteria;
import com.jc.pico.bean.SvcDeviceLicense;
import com.jc.pico.bean.SvcDeviceLicenseExample;
import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcItemImgExample;
import com.jc.pico.bean.SvcItemOpt;
import com.jc.pico.bean.SvcItemOptDtl;
import com.jc.pico.bean.SvcItemOptDtlExample;
import com.jc.pico.bean.SvcItemOptExample;
import com.jc.pico.bean.SvcKitchenMessage;
import com.jc.pico.bean.SvcKitchenMessageExample;
import com.jc.pico.bean.SvcPluItemExample;
import com.jc.pico.bean.SvcStaff;
import com.jc.pico.bean.SvcStaffExample;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcStoreExample;
import com.jc.pico.bean.SvcTable;
import com.jc.pico.bean.SvcTableExample;
import com.jc.pico.bean.SvcUserMapping;
import com.jc.pico.bean.SvcUserMappingExample;
import com.jc.pico.bean.SvnStoreTemp;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserExample;
import com.jc.pico.exception.DataNotFoundException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.NoPermissionException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcAppMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcClosingMapper;
import com.jc.pico.mapper.SvcDeviceLicenseMapper;
import com.jc.pico.mapper.SvcItemImgMapper;
import com.jc.pico.mapper.SvcItemMapper;
import com.jc.pico.mapper.SvcItemOptDtlMapper;
import com.jc.pico.mapper.SvcItemOptMapper;
import com.jc.pico.mapper.SvcKitchenMessageMapper;
import com.jc.pico.mapper.SvcPluItemMapper;
import com.jc.pico.mapper.SvcStaffMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcTableMapper;
import com.jc.pico.mapper.SvcUserMappingMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.clerk.ClerkCommonService;
import com.jc.pico.service.pos.PosEtcService;
import com.jc.pico.utils.AES256Cipher;
import com.jc.pico.utils.AuthenticationUtils;
import com.jc.pico.utils.ClerkUtil;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.ClerkResult.ErrorCode;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StaffUserDetail;
import com.jc.pico.utils.bean.SvcItemExtended;
import com.jc.pico.utils.bean.SvcItemExtended.SvcItemOptExtended;
import com.jc.pico.utils.customMapper.clerk.ClerkOrderMapper;
import com.jc.pico.utils.customMapper.clerk.ClerkStoreMapper;
import com.jc.pico.utils.customMapper.common.SvcCommonMapper;

@Service
public class ClerkCommonServiceImpl implements ClerkCommonService {

	private static Logger logger = LoggerFactory.getLogger(ClerkCommonServiceImpl.class);

	public static final String PLU_TP_POS = "351001";
	public static final String PLU_TP_TAB = "351003";

	public static final String LICENSE_STATUS_UNUSE = "354001"; //라이센스 미등록
	public static final String LICENSE_STATUS_USE = "354002"; //라이센스 등록
	public static final String LICENSE_STATUS_END = "354003"; //라이센스 만료
	public static final String LICENSE_STATUS_DSUSE = "354004"; //라이센스 폐기

	public static final String LICENSE_DEVICE_TYPE_CLERK = "876003"; // 점원App
	public static final String LICENSE_DEVICE_TYPE_TAB = "876004"; // 테이블 오더

	public static final int INSTALL_TYPE_NEW = 1; // 신규설치
	public static final int INSTALL_TYPE_RE = 2; // 재설치

	public static final String USER_TYPE_AGENT_USER = "300005"; // 설치직원
	public static final String USER_TYPE_STORE_MANAGER = "300006"; // 매장 관리자
	
	public static final String ADVERTISE_TYPE_VIDEO = "V";
	public static final String ADVERTISE_TYPE_PICTURE = "P";

	/**
	 * 직원 상태 : 정상
	 */
	public static final String STAFF_STATUS_NORMAL = "305001";

	/**
	 * 사용자 상태 : 정상
	 */
	private static final String USER_STATUS_NORMAL = "301001";

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

	/**
	 * 테이블 락 요청 : 처리 하지 않음
	 */
	public static final String TABLE_LOCK_NONE = "none";

	/**
	 * 테이블 락 요청 : 락 해제
	 */
	public static final String TABLE_LOCK_RELEASE = "release";

	private static final RowBounds ROW_BOUNDS_JUST_FIRST = new RowBounds(0, 1); //페이징 기법
	
    private static final List<String> DEFAULT_ADVERTISE = new ArrayList<String>();
    static {
        DEFAULT_ADVERTISE.add("/image-resource/ad/store/default/ad_00.jpg");
        DEFAULT_ADVERTISE.add("/image-resource/ad/store/default/ad_01.jpg");
        DEFAULT_ADVERTISE.add("/image-resource/ad/store/default/ad_02.jpg");
    }

	@Autowired
	private ClerkStoreMapper storeMapper;

	@Autowired
	private ClerkOrderMapper orderMapper;

	@Autowired
	private SvcItemImgMapper svcItemImgMapper;

	@Autowired
	private SvcItemOptMapper svcItemOptMapper;

	@Autowired
	private SvcItemOptDtlMapper svcItemOptDtlMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SvcUserMappingMapper svcUserMappingMapper;

//	@Autowired
//	private SvcFranchiseMapper svcFranchiseMapper;

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private SvcDeviceLicenseMapper svcDeviceLicenseMapper;

	@Autowired
	private SvcStaffMapper svcStaffMapper;

	@Autowired
	private SvcAppMapper svcAppMapper;

	@Autowired
	private SvcClosingMapper svcClosingMapper;

	@Autowired
	private SvcKitchenMessageMapper svcKitchenMessageMapper;

	@Autowired
	private PosEtcService posEtcService;

	@Autowired
	private SvcTableMapper svcTableMapper;

	@Autowired
	private SvcItemMapper svcItemMapper;
	
	@Autowired
	private SvcCommonMapper svcCommonMapper;

	@Autowired
	private CodeUtil codeUtil;
	
	@Autowired
	private PosUtil posUtil;

	@Autowired
	private SvcPluItemMapper svcPluItemMapper;	

	private ObjectMapper objectMapper;

	private BCryptPasswordEncoder bcryptPasswordEncoder;

	@PostConstruct
	public void init() {
		objectMapper = JsonConvert.getObjectMapper();
		bcryptPasswordEncoder = new BCryptPasswordEncoder();
	}

	
	/**
	 * tableSections
	 * - table section 1
	 * - table section 2
	 * - table1
	 * - table2
	 * - table3
	 * 
	 * @throws RequestResolveException
	 * 
	 */
	@Override
	public List<SingleMap> getStoreTableSections(SingleMap param) throws RequestResolveException {

		List<SingleMap> sections = storeMapper.selectTableSectionListByStoreId(param);

		for (SingleMap section : sections) {
			param.put("sectionId", section.get("id"));

			List<SingleMap> tables = storeMapper.selectTableListBySectionId(param);
			section.put("tables", tables);

			for (SingleMap table : tables) {
				if (table.hasValue("orderId")) {
					table.put("order", getOrderSimpleByOrderId(table));
				}
			}
		}

		return sections;
	}	

	
	@Override
	public List<SingleMap> getCategoriesDetail(SingleMap param) { //param : catId, storeId, brandId	
		List<SingleMap> categories = storeMapper.selectPluCategoryList(param); // 카테고리 리스트 : 인기메뉴, 세트메뉴 ....

		for (SingleMap category : categories) { //for문으로 돌면서 각 카테고리별 메뉴 넣기
			param.put("catId", category.get("id")); //417 - 인기메뉴, 418 - 세트메뉴, 419 - 단품메뉴, 420 - 참숯메뉴, 421 - 사이드메뉴, 422 - 음료수, 423 - 토핑
			category.put("items", getPluItemListByCatId(param));
		}

		return categories;
	}

	
	/**
	 * Plu item 목록 조회, 옵션, 옵션 상세 포함
	 * 
	 * @param param
	 * @return
	 */
	private List<SvcItemExtended> getPluItemListByCatId(SingleMap param) {
		//매출 30일 기준 --> 베스트 5 선정 --> 이전tb_svc_plu_item 지우고, tb_svc_plu_item에 넣어준다(insert)
		// param : {catId=?, brandId=?, storeId=?} --for문
		// 이전 인기메뉴 삭제 : delete from tb_svc_plu_item where brand_id = 44 and store_id = 89 and cat_id = 417
		//svcPluItemMapper.deleteByExample(svcPluItemWhere);
		//svcPluItemMapper.deleteByExample(param.);
		// 매출 30일 기준 --> 베스트 5 선정
		
		// 인기메뉴 넣어주기 : insert into tb_svc_plu_item (brand_id, store_id, cat_id, item_id, ordinal) values (44,89,417,2926,1)
		
		Entry<String, Object> entry = param.entrySet().iterator().next();
		String key = entry.getKey();
		Object value = entry.getValue(); // 417
		String castValue = String.valueOf(value);
		
		 
		 if(castValue.equals("417")) { //인기메뉴
			 // 이전 인기메뉴 삭제 : delete from tb_svc_plu_item where brand_id = ? and store_id = ? and cat_id = ?
			 // SvcPluItemExample example = new SvcPluItemExample();
			 // example.createCriteria();
			 
			        //.andBrandIdEqualTo()
			        //.andStoreIdEqualTo(value)
			 //.andCatIdEqualTo()
			 
			// brand_id : 44
			// store_id : 89
		    // cat_id : 417
			
				
				
			 //svcPluItemMapper.deleteByExample(example);
			 // 30일 기준 판매건수별 :
			 // 인기 메뉴 넣어주기(for문) : insert into tb_svc_plu_item (brand_id, store_id, cat_id, item_id, ordinal) values (?,?,?,?,?);
		 }
		 
		List<SvcItemExtended> items = storeMapper.selectPluItemList(param); // 각 카테고리별 포함된 아이템리스트

		for (SvcItemExtended item : items) {
			item.setImages(new ArrayList<SvcItemImg>());
			item.setOptions(new ArrayList<SvcItemOptExtended>());
		}

		// 세부 항목 검색용 item id 목록 생성
		List<Long> itemIds = new ArrayList<>(items.size());
		for (SvcItemExtended item : items) {
			itemIds.add(item.getId());
		}

		if (!itemIds.isEmpty()) {
			appendItemImages(items, itemIds);
			appendItemOptions(items, itemIds);
		}

		return items;
	}
	
	
	@Override
	public SingleMap getUserDetail(SingleMap param) {

		SingleMap result = new SingleMap();
		User user = getUserByUserName(param.getString("userName"));
		user.setPassword(null); // 보안상 패스워드는 제거
		result.put("user", user);

		return result;
	}
	

	/**
	 * 디바이스 라이센스 등록 처리
	 * 
	 * @throws NoPermissionException
	 */
	@Override
	public SingleMap registerDeviceLicense(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException {

		String storeCode = param.getString("storeCode");
		String licenseKey = param.getString("licenseKey");
		String posNo = param.getString("posNo");
		String deviceType = param.getString("deviceType");
		int installType = param.getInt("installType"); // 1: 신규설치, 2: 재설치
		String userName = param.getString("userName");
		String hwInfo = param.getString("hwInfo");

		Date nowDate = new Date();
		SvcStore store = getStoreByStoreCode(storeCode);
		User user = getAgentUserByUserName(userName);

		if (store == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code, storeCode + " code store is not found.");
		}

		if (user == null) {
			logger.warn("[{}] Unknown user is {}", ErrorCode.NO_PERMISSION.code, userName);
			throw new NoPermissionException();
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
			logger.warn("[{}] Invalid device license.", ErrorCode.LICENSE_INVALID.code);
			throw new RequestResolveException(ErrorCode.LICENSE_INVALID.code, "Invalid device license.");
		}

		// 라이센스 검증
		if (installType != INSTALL_TYPE_NEW && installType != INSTALL_TYPE_RE) {
			logger.warn("[{}] Invalid installType paramter. {}", ErrorCode.LICENSE_INVALID.code, installType);
			throw new InvalidParamException(new String[] { "installType" }, null);
		}
		SvcDeviceLicense license = licenses.get(0);
		// 신규 설치인데 라이센스가 사용된 미사용 상태가 아니면 에러
		if (installType == INSTALL_TYPE_NEW && !LICENSE_STATUS_UNUSE.equals(license.getStatus())) {
			logger.warn("[{}] Used license.", ErrorCode.LICENSE_USED.code);
			throw new RequestResolveException(ErrorCode.LICENSE_USED.code, "Used license.");
		}
		// 재설치 인데 라이센스가 사용한 상태가 아니면 에러
		if (installType == INSTALL_TYPE_RE && !LICENSE_STATUS_USE.equals(license.getStatus())) {
			logger.warn("[{}] Unused license.", ErrorCode.LICENSE_UNUSED.code);
			throw new RequestResolveException(ErrorCode.LICENSE_UNUSED.code, "Unused license.");
		}

		// 포스번호, 등록자, 상태 갱신
		SvcDeviceLicense record = new SvcDeviceLicense();
		record.setId(license.getId());
		record.setBrandId(store.getBrandId());
		record.setStoreId(store.getId());
		record.setPosNo(posNo);
		record.setUserId(user.getId());
		record.setStatus(LICENSE_STATUS_USE); // 사용 상태로 변경
		record.setHwInfo(hwInfo);
		record.setCertTm(installType == INSTALL_TYPE_NEW ? new Date() : null); // 신규 셜치이면 등록일 기록

		svcDeviceLicenseMapper.updateByPrimaryKeySelective(record);

		// 포스 개점 시간
		SvcClosing closing = getLatestClosingByStoreId(store.getId());
		license = svcDeviceLicenseMapper.selectByPrimaryKey(license.getId()); // 재조회 해서 전달

		SingleMap result = new SingleMap();
		result.put("license", license);
		result.put("store", store);
		result.put("brand", svcBrandMapper.selectByPrimaryKey(store.getBrandId()));
		result.put("openDt", closing != null ? closing.getOpenDt() : null);

		return result;
	}
	

	/**
	 * 키오스크 디바이스 라이센스 등록 처리
	 * 
	 * @throws NoPermissionException
	 */
	@Override
	public SingleMap registerDeviceLicenseKiosk(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException {

//		String storeCode = param.getString("storeCode");
//		String posNo = param.getString("posNo");
		boolean isMain = param.getBoolean("isMain", false);
		String licenseKey = param.getString("licenseKey");
		String deviceType = param.getString("deviceType");
		int installType = param.getInt("installType"); // 1: 신규설치, 2: 재설치
		String userName = param.getString("userName");
		String hwInfo = param.getString("hwInfo");

		Date nowDate = new Date();
		// 수정 : 
		//		SvcStore store = getStoreByStoreCode(storeCode);
		// 수정 : 
		User user = getAgentUserByUserName(userName);

		// 수정 : 
		//	if (store == null) {
		//		throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code, storeCode + " code store is not found.");
		//	}
		// 수정 : 

		if (user == null) {
			logger.warn("[{}] Unknown user is {}", ErrorCode.NO_PERMISSION.code, userName);
			throw new NoPermissionException();
		}

		// 라이센스 조회 
		SvcDeviceLicenseExample example = new SvcDeviceLicenseExample();
		example.createCriteria() //			
				.andLicenseKeyEqualTo(licenseKey)            // 라이센스 코드
				.andBeginLessThanOrEqualTo(nowDate)    // 유효 기간 확인 (시작일 <= 현재 <= 만료일) 
				.andEndGreaterThanOrEqualTo(nowDate)  // 유효 기간 확인
				.andDeviceTpEqualTo(deviceType)             // 디바이스 종류
				//.andHwInfoEqualTo(hwInfo)                     // 시리얼번호 > 추가 : 다른 장비에서 시리얼번호를 사용중이면 사용하지 못하도록
				.andStatusIn(Arrays.asList(LICENSE_STATUS_UNUSE, LICENSE_STATUS_USE)); // 사용, 미사용 상태의 라이센스만 허용

		List<SvcDeviceLicense> licenses = svcDeviceLicenseMapper.selectByExample(example);
		if (licenses.size() == 0) {
			logger.warn("[{}] Invalid device license.", ErrorCode.LICENSE_INVALID.code); //유효하지 않은 라이센스
			throw new RequestResolveException(ErrorCode.LICENSE_INVALID.code, "Invalid device license.");
		}

		// 라이센스 검증
		if (installType != INSTALL_TYPE_NEW && installType != INSTALL_TYPE_RE) {
			logger.warn("[{}] Invalid installType paramter. {}", ErrorCode.LICENSE_INVALID.code, installType);
			throw new InvalidParamException(new String[] { "installType" }, null);
		}

		SvcDeviceLicense license = licenses.get(0);
		// 신규 설치인데 라이센스가 사용된 미사용 상태가 아니면 에러
		if (installType == INSTALL_TYPE_NEW && !LICENSE_STATUS_UNUSE.equals(license.getStatus())) {
			logger.warn("[{}] Used license.", ErrorCode.LICENSE_USED.code);
			throw new RequestResolveException(ErrorCode.LICENSE_USED.code, "Used license.");
		}
		
		// 재설치 인데 라이센스가 사용한 상태가 아니면 에러
		if (installType == INSTALL_TYPE_RE && !LICENSE_STATUS_USE.equals(license.getStatus())) {
			logger.warn("[{}] Unused license.", ErrorCode.LICENSE_UNUSED.code);
			throw new RequestResolveException(ErrorCode.LICENSE_UNUSED.code, "Unused license.");
		}

		// 추가 : 
		// 2020.02.01
		// 재설치 이면서 다른 장비에서 사용중인 라이선스를 사용할 경우 오류
		// 다른 장비의 라이선스를 사용할 경우
		if  (installType == INSTALL_TYPE_RE  &&  LICENSE_STATUS_USE.equals(license.getStatus()) && 
			 !hwInfo.equals(license.getHwInfo())) {
			logger.warn("[{}] Used license.", ErrorCode.LICENSE_USED.code);
			throw new RequestResolveException(ErrorCode.LICENSE_USED.code, "Used license.");
		}
		
		// 수정 : 
		// 2020.01.05
		SvcStore store = getStoreById(license.getStoreId());
		if (store == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code,  " store is not found.");
		}
		// 수정 : 
		
		
		// 포스번호, 등록자, 상태 갱신
		SvcDeviceLicense record = new SvcDeviceLicense();
		record.setId(license.getId());
		record.setBrandId(store.getBrandId());
		record.setStoreId(store.getId());
//		record.setPosNo(posNo);
		record.setMain(isMain);
		record.setUserId(user.getId());
		record.setStatus(LICENSE_STATUS_USE); // 사용 상태로 변경
		record.setHwInfo(hwInfo);
		record.setCertTm(installType == INSTALL_TYPE_NEW ? new Date() : null); // 신규 셜치이면 등록일 기록
		
		//TODO STORE_ID 해당 포스들중 하나만 메인으로 설정
		
		if(isMain) { //false
			svcDeviceLicenseMapper.updateIsMainFalse(record);			
		}

		svcDeviceLicenseMapper.updateByPrimaryKeySelective(record);

		license = svcDeviceLicenseMapper.selectByPrimaryKey(license.getId()); // 재조회 해서 전달
		
		SingleMap result = new SingleMap();
		result.put("license", license);
		result.put("store",    store);
		result.put("brand",   svcBrandMapper.selectByPrimaryKey(store.getBrandId()));
		
		System.out.println("확인 >>>>>"+store.toString());
		System.out.println("확인 >>>>>"+svcBrandMapper.selectByPrimaryKey(store.getBrandId()));
		
		return result;
	} // ---------------------------- registerDeviceLicenseKiosk
	

	/**
	 * 키오스크 상점 정보
	 * 
	 * @throws NoPermissionException
	 */
	@Override
	public SingleMap getStoreInfoKiosk(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException {

		Long storeId = param.getLong("storeId");

		// 수정 : 
		// 2020.01.16
		SvcStore store = getStoreById(storeId);
		if (store == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code,  storeId + " code store is not found.");
		}
		// 수정 : 
		
		SingleMap result = new SingleMap();
		result.put("store", store);

		return result;
	}
	

	/**
	 * getDeviceInfoKiosk
	 * 
	 * 키오스크 디바이스와 상점정보 리턴
	 * 
	 * @throws NoPermissionException
	 */
	@Override
	public SingleMap getDeviceInfoKiosk(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException {

		String licenseKey = param.getString("licenseKey");
		String hwInfo      = param.getString("hwInfo");

		// 라이센스 조회 
		SvcDeviceLicenseExample example = new SvcDeviceLicenseExample();
		example.createCriteria() 		
				.andLicenseKeyEqualTo(licenseKey);    // 라이센스 코드

		List<SvcDeviceLicense> licenses = svcDeviceLicenseMapper.selectByExample(example);
		if (licenses.size() == 0) {
			logger.warn("[{}] Invalid device license.", ErrorCode.LICENSE_INVALID.code);
			throw new RequestResolveException(ErrorCode.LICENSE_INVALID.code, "Invalid device license.");
		}

		// 라이선스 검색
		SvcDeviceLicense license = licenses.get(0);

		// 수정 : 
		// 2020.01.05
		SvcStore store = getStoreById(license.getStoreId());
		if (store == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code,  licenseKey + " key store is not found.");
		}
		// 수정 : 
		
		// 재조회 해서 전달
		//if (license != null)
		//	license = null;
		//license = svcDeviceLicenseMapper.selectByPrimaryKey(license.getId());

		SingleMap result = new SingleMap();
		result.put("license", license);
		result.put("store",    store);
		result.put("brand",   svcBrandMapper.selectByPrimaryKey(store.getBrandId()));
		result.put("openDt", null);

		return result;
	}
	
	
	@Override
	public SingleMap updateLicensePosNo(SingleMap param) throws InvalidParamException, RequestResolveException, NoPermissionException {

		String storeCode = param.getString("storeCode");
		String licenseKey = param.getString("licenseKey");
		String posNo = param.getString("posNo");
		String deviceType = param.getString("deviceType");
		String userName = param.getString("userName");
		Date nowDate = new Date();

		//SvcStore store = getStoreByStoreCode(storeCode);
		User user = getAgentUserByUserName(userName);

		//if (store == null) {
		//	throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code, storeCode + " code store is not found.");
		//}

		if (user == null) {
			logger.warn("[{}] Unknown user is {}", ErrorCode.NO_PERMISSION.code, userName);
			throw new NoPermissionException();
		}

		// 라이센스 조회 
		SvcDeviceLicenseExample example = new SvcDeviceLicenseExample();
		example.createCriteria() //			
				.andLicenseKeyEqualTo(licenseKey) // 라이센스 코드
				.andDeviceTpEqualTo(deviceType) // 디바이스 종류
				.andBeginLessThanOrEqualTo(nowDate) // 유효 기간 확인 (시작일 <= 현재 <= 만료일) 
				.andEndGreaterThanOrEqualTo(nowDate) // 유효 기간 확인
				.andStatusIn(Arrays.asList(LICENSE_STATUS_USE)); // 사용 상태의 라이센스만 허용

		
		// 포스번호, 등록자, 상태 갱신
		SvcDeviceLicense record = new SvcDeviceLicense();
		record.setPosNo(posNo);
		record.setUserId(user.getId());
		int count = svcDeviceLicenseMapper.updateByExampleSelective(record, example);

		if (count == 0) {
			throw new RequestResolveException(ErrorCode.LICENSE_INVALID.code, "Invalid device license.");
		}

		SingleMap result = new SingleMap();
		return result;
	}	
	
	
	@Override
	public List<SvcStaff> getStaffList(SingleMap param) throws InvalidParamException {
		if (!param.hasValue("storeId")) {
			throw ClerkUtil.newInvalidParamException("storeId is empty.");
		}

		SvcStaffExample example = new SvcStaffExample();
		example.createCriteria() // 검색 조건
				.andStoreIdEqualTo(param.getLong("storeId")) // 지점 
				.andStatusEqualTo(STAFF_STATUS_NORMAL); // 상태 정상
		List<SvcStaff> staffs = svcStaffMapper.selectByExample(example);

		// 패스워드 제거
		for (SvcStaff staff : staffs) {
			staff.setPassword(null);
		}
		return staffs;
	}	
	

	@Override
	public boolean checkStaffValidation(SingleMap param) throws NoPermissionException {

		String password;
		try {
			password = AES256Cipher.decodeAES256(param.getString("password"));
		} catch (Exception e) {
			throw new InvalidParameterException("Can not decrypt password.");
		}

		SvcStaff staff = svcStaffMapper.selectByPrimaryKey(param.getLong("staffId"));

		// 해당 staff가 없음
		if (staff == null) {
			throw new NoPermissionException();
		}

		// 직원 상태가 정상이 아님. (휴직, 퇴직)
		if (!STAFF_STATUS_NORMAL.equals(staff.getStatus())) {
			throw new NoPermissionException();
		}

		// 패스워드 틀림
		if (!bcryptPasswordEncoder.matches(password, staff.getPassword())) {
			throw new NoPermissionException();
		}

		return true;
	}
	
	
	/**
	 * 사용자 패스워드 확인 (로그인 상태로만 요청해야함)
	 * 
	 * @param param
	 *            userName : 사용자 id
	 *            password : 패스워드
	 * 
	 * @exception NoPermissionException
	 *                권한 없음
	 * 
	 */
	@Override
	public boolean checkUserValidation(SingleMap param) throws NoPermissionException {

		String password = param.getString("password");

		User user = getUserByUserName(param.getString("userName"));

		// 해당 user가 없음
		if (user == null) {
			throw new NoPermissionException();
		}

		// user 상태가 정상이 아님. (휴직, 퇴직)		
		if (!USER_STATUS_NORMAL.equals(user.getStatus())) {
			throw new NoPermissionException();
		}

		// 패스워드 틀림
		if (!bcryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new NoPermissionException();
		}

		return true;
	}
	
	
	@Override
	public SingleMap getAppInfo(SingleMap param) throws RequestResolveException {

		Long storeId = param.getLong("storeId", 0L); // 0 or null 이면 설정된 storeId가 없는 것임.
		String osType = codeUtil.getBaseCodeByAlias(param.getString("os"));
		String appType = param.getString("appType");
		int versionCode = param.getInt("versionCode");
		String withTableLock = param.getString("withTableLock", TABLE_LOCK_NONE);
		String posNo = param.getString("posNo", null);

		// 앱에서 선택한 지점이 없는 경우는 경우는 storeId를 전달하지 않음. 
		// 단 전달한 storeId가 존재하는데 조회 결과 해당 매장이 없으면 에러
		if (storeId != 0L) {
			SvcStore store = svcStoreMapper.selectByPrimaryKey(storeId);
						
			if (store == null) {
				throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code, "Not found store");
			}
			
			logger.debug("store VanType 확인 >>>" + store.getVanType()); // Null/0 : KIS, 1 : JT NET, 2 : 다우데이터
		}

		if (StringUtils.isEmpty(osType)) {
			throw new InvalidParameterException("Unknown os type. " + param.get("os"));
		}

		if (StringUtils.isEmpty(appType)) {
			throw new InvalidParameterException("Unknown app type. " + param.get("appType"));
		}

		final SvcClosing closing = getLatestOpenByStoreId(storeId); //tb_svc_closing 
		
		// 앱 최초 실행시 호출 되는데 이때 요청이 있으면 해당 포스가 설정한 테이블 락을 해제 한다.
		if (storeId != 0 && posNo != null && TABLE_LOCK_RELEASE.equals(withTableLock)) {
			releaseTableLock(storeId, posNo);
		}

		SingleMap result = new SingleMap();
		result.put("openDt", closing != null ? closing.getOpenDt() : null);
		result.put("isClosing", closing != null ? closing.getIsClosing(): false);
		logger.debug("openDt(개점날짜) 상태 : " + closing.getOpenDt());
		logger.debug("IsClosing(영업오픈(false:0) / 영업종료(true:1)) 상태 : " + closing.getIsClosing()); // false(0 - 영업오픈) / true(1 - 영업종료)
		result.put("appUpdateInfo", getAppUpdateInfo(appType, osType, versionCode));
		

		return result;
	}
	
	/**
	 * 매장 포스가 주문을 받을 준비가 되어 있는지 확인한다.
	 * 매장 포스가 open 되어 있지 않거나 마감되어 있으면 예외를 던짐.
	 * 포스가 살아 있는 상태가 아니면 예외를 던짐
	 */
	@Override
	public void throwIfPosNotReady(long storeId) throws RequestResolveException {

		SvcStore store = svcStoreMapper.selectByPrimaryKey(storeId);
		if (store == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_FOUND.code, "Not found store");
		}

		// 포스가 살아 있지 않음. (POS에서 APC로 정기적으로 ALIVE 보고함)
		if (!posEtcService.isAliveStore(storeId)) {
			throw new RequestResolveException(ClerkResult.ErrorCode.STORE_NOT_ALIVE.code, "Not alive store.");
		}

		// 포스가 개점 상태인지 확인
		SvcClosing latestClosing = getLatestClosingByStoreId(storeId);
		if (latestClosing == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.POS_NOT_OPENED.code, "Not opened pos.");
		}

		// 마감된 상태~
		if (latestClosing.getIsClosing()) {

			if (DateUtils.isSameDay(new Date() /* now */, latestClosing.getOpenDt())) {
				// 마감이 당일 처리된 경우 "마감됨" 에러
				throw new RequestResolveException(ClerkResult.ErrorCode.POS_CLOSED.code, "Closed pos.");
			} else {
				// 다른날 마감처리된 경우 처리 "오픈되지 않음" 에러
				throw new RequestResolveException(ClerkResult.ErrorCode.POS_NOT_OPENED.code, "Not opened pos.");
			}
		}

		// POS ready~			
	}	
	

	/**
	 * 매장 지원 정보 조회
	 * 인증된 상태로 요청해야 한다.
	 * 
	 * @throws DataNotFoundException
	 */
	@Override
	public SingleMap getStaffDetail(SingleMap param) throws DataNotFoundException {
		StaffUserDetail staffDetail = (StaffUserDetail) param.get("staffDetail");

		if (staffDetail == null) {

		}

		SvcStaff staff = svcStaffMapper.selectByPrimaryKey(staffDetail.getStaffId());
		if (staff == null) {
			throw new DataNotFoundException("Staff not found.");
		}

		staff.setPassword(null); // 보안상 삭제		

		SingleMap result = new SingleMap();
		result.put("staff", staff);

		return result;
	}	

	
	@Override
	public List<String> getKichenMessage(SingleMap param) throws RequestResolveException {
		long storeId = param.getLong("storeId");
		long itemId = param.getLong("itemId", 0L); // FIXME 호환성을 위해 임시로 설정함. 앱 배포 완료되면 기본값 제거해야함.

		SvcItem svcItem = svcItemMapper.selectByPrimaryKey(itemId);

		if (svcItem == null) {
			throw new RequestResolveException(ClerkResult.ErrorCode.DATA_NOT_FOUND.code, "Not found item. itemId=" + itemId);
		}

		SvcKitchenMessageExample example = new SvcKitchenMessageExample();
		example.createCriteria() // 조회 조건
				.andStoreIdEqualTo(storeId) // 해당 상점의 키친 메시지 조회
				.andItemCatIdEqualTo(svcItem.getCatId()); // 상품의 카테고리에 설정된 내용 
		example.setOrderByClause("ORDINAL ASC, ID ASC");

		List<SvcKitchenMessage> messages = svcKitchenMessageMapper.selectByExample(example);
		List<String> result = new ArrayList<>(messages.size());
		for (SvcKitchenMessage message : messages) {
			result.add(message.getMessage());
		}
		return result;
	}	
	

	/**
	 * 상점의 개점 정보 및 테이블별 주문 현황을 조회한다.
	 * 
	 * @param
	 * 			storeId
	 *            : 상점 번호
	 * 
	 * @return
	 * 		- openDt: 개점일
	 *         - sections : 섹션 정보 및 테이블 별 주문 현황
	 */
	@Override
	public SingleMap getStoreStatus(SingleMap param) throws RequestResolveException {

		SvcClosing closing = getLatestClosingByStoreId(param.getLong("storeId"));

		SingleMap result = new SingleMap();
		result.put("openDt", closing != null ? closing.getOpenDt() : null);
		result.put("sections", getStoreTableSections(param));

		return result;
	}
	
	
	@Override
	public SvcClosing getLatestClosingByStoreId(long storeId) {

		SvcClosingExample example = new SvcClosingExample();
		example.createCriteria() // 조건
				.andStoreIdEqualTo(storeId);
		example.setOrderByClause("OPEN_DT DESC"); // 최근 순서

		List<SvcClosing> result = svcClosingMapper.selectByExampleWithRowbounds(example, ROW_BOUNDS_JUST_FIRST);
		return result.size() > 0 ? result.get(0) : null;
	}
	
	
	@Override
	public SingleMap getAdvertiseInfo(ServletContext context, SingleMap param) throws RequestResolveException {
		final InputStream is = context.getResourceAsStream("/admin/_static/ad/ad.data");
		try {
			final String adInfo = IOUtils.toString(is);
			final SingleMap defaultAdvertise = objectMapper.readValue(adInfo, SingleMap.class);
			
			final Long storeId = param.getLong("storeId", 0L);
			if(storeId == 0) {
				return defaultAdvertise;
			}
			
			final List<SingleMap> dataList = svcCommonMapper.selectAdvertiseList(param);
			
			if(dataList.isEmpty()) {
				return defaultAdvertise;
			}
			
			final List<String> videoList = dataList.stream().filter(data -> data.getString("FORMAT").equals(ADVERTISE_TYPE_VIDEO)).map(data -> String.format("%s%s", param.getString("host"), data.getString("URL"))).collect(Collectors.toList());
			final List<String> imageList = dataList.stream().filter(data -> data.getString("FORMAT").equals(ADVERTISE_TYPE_PICTURE)).map(data -> String.format("%s%s", param.getString("host"), data.getString("URL"))).collect(Collectors.toList());
			
			if(!imageList.isEmpty()) {
				imageList.addAll(DEFAULT_ADVERTISE.stream().map(data -> String.format("%s%s", param.getString("host"), data)).collect(Collectors.toList())); //기존의 광고 이미지 + 기본 이미지
				
			}
			
			if(!videoList.isEmpty()) {
				System.out.println("확인 -------------------------->@@@@");
			}
			
			defaultAdvertise.put("image", imageList);
			defaultAdvertise.put("video", videoList);
			
			logger.debug("확인1"+videoList.toString());
			logger.debug("확인2"+imageList.toString());
			
		
			return defaultAdvertise; //안드로이드 쪽에서 이미지를 아예 고정시켜 놓은듯하다 : initImageview()
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * pos 의 S_OPEN_INFO 프로세스를 가져왔음
	 */
	@Override
	public SingleMap sOpenInfo(SingleMap param) throws RequestResolveException {
			//오늘날짜
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Calendar time = Calendar.getInstance();
		    
		    String format_today = format.format(time.getTime());
		
		try {
			final String openDateApcStr = param.getString("openDateApc", null); // 20201208
			final Date openDateApc = StringUtils.isEmpty(openDateApcStr) ? null : param.getDate("openDateApc", "yyyyMMdd"); // Tue Dec 08 00:00:00 KST 2020	

			final StaffUserDetail userDetail = AuthenticationUtils.getDetails(StaffUserDetail.class); // StaffUserDetail [brandId=44, storeId=89, staffId=82, licenseId=320]
			
			Long storeId = userDetail.getStoreId(); // ex. 89
			Long brandId = userDetail.getBrandId(); // ex. 44


			// 해당 매장의 개점/폐점 정보를 읽어오기
			String orderByClause = "OPEN_DT DESC";
			SvcClosingExample svcClosingExample = new SvcClosingExample();
			svcClosingExample.setOrderByClause(orderByClause);
			Criteria svcClosingExampleCriteria = svcClosingExample.createCriteria() // 검색 조건
					.andBrandIdEqualTo(brandId) // 브랜드
					.andStoreIdEqualTo(storeId); // 상점
            
			logger.debug("확인+++"+openDateApc);
			
			// <오늘날짜O 개점 처리>
			if (openDateApc != null) { 

				svcClosingExampleCriteria.andOpenDtBetween(posUtil.getDateTime(openDateApc, 0, 0, 0, 0),      //날짜 ex. 당일 2020-12-07 00:00:00.0(Timestamp) ~ 2020-12-07 23:59:59.999(Timestamp) 까지
						posUtil.getDateTime(openDateApc, 23, 59, 59, 999));

				List<SvcClosing> svcClosings = svcClosingMapper.selectByExampleWithRowbounds(svcClosingExample, new RowBounds(0, 1));
				
				/*
				 * (QUERY문)
				 * select ID, BRAND_ID, STORE_ID, OPEN_DT, OPEN_TM, CLOSE_TM, IS_CLOSING, OPEN_RESERVE, SALES, SALES_CNT, REFUND, REFUND_CNT, DISCOUNT, DISCOUNT_CNT, CUSTOMER_CNT, CASH_IN, CASH_IN_CNT, CASH_OUT, CASH_OUT_CNT, CASH_ON_HAND, CASH_LACK, CREATED, UPDATED from tb_svc_closing WHERE ( BRAND_ID = ? and STORE_ID = ? and OPEN_DT between ? and ? ) order by OPEN_DT DESC
				 * 
				 * (적용문)
				 * select ID, BRAND_ID, STORE_ID, OPEN_DT, OPEN_TM, CLOSE_TM, IS_CLOSING, OPEN_RESERVE, SALES, SALES_CNT, REFUND, REFUND_CNT, DISCOUNT, DISCOUNT_CNT, CUSTOMER_CNT, CASH_IN, CASH_IN_CNT, CASH_OUT, CASH_OUT_CNT, CASH_ON_HAND, CASH_LACK, CREATED, UPDATED from tb_svc_closing WHERE ( BRAND_ID = 44 and STORE_ID = 89 and OPEN_DT between 2020-12-08 00:00:00.0(Timestamp) and 2020-12-08 23:59:59.999(Timestamp) ) order by OPEN_DT DESC
				 *    
				 */
				
				
				if (svcClosings.isEmpty()) { // 신규 개점 처리 				
						
					SvcClosing record = new SvcClosing();
					record.setBrandId(brandId);
					record.setStoreId(storeId);
					record.setOpenDt(openDateApc);
					record.setOpenTm(new Date());
					record.setIsClosing(false);
					svcClosingMapper.insertSelective(record);					

				} else { // 재개점 처리					

					SvcClosing record = new SvcClosing();
					record.setId(svcClosings.get(0).getId());
					record.setIsClosing(false);
					//record.setOpenTm(new Date());
					svcClosingMapper.updateByPrimaryKeySelective(record);
								
				}

			} else { // <오늘날짜X -- 마감 처리> : 오늘날짜X 영업종료되지 않은 경우 → 영업종료상태(true - 0) update → 결과 result 던져주기

				// 오늘날짜X, 영업종료되지 않은 경우 찾기
				List<SvcClosing> svcClosings = svcClosingMapper.selectByExampleWithRowbounds(svcClosingExample, new RowBounds(0, 1));
				
				
				if (!svcClosings.isEmpty()) { // 신규 개점 처리					
					SvcClosing record = new SvcClosing();
					record.setId(svcClosings.get(0).getId());
					record.setIsClosing(true);//영업종료(0 → 1)
					record.setCloseTm(new Date());
					svcClosingMapper.updateByPrimaryKeySelective(record);
					
				} else { // 미개점 상태에서 마감 요청이 들어오면 무시 (이미 마감 처리되어 있거나, 새로운 개점이 없음)

					logger.warn("Invalid close request. Cause by not opened. storeId={}", storeId);						
				}
			}
			

			//결과(SingelMap - result) 던져주기
			final SvcClosing closing = getLatestOpenByStoreId(storeId); //getLatesOpenByStoreId : 
			
			SingleMap result = new SingleMap();
			result.put("openDt", closing != null ? closing.getOpenDt() : null); //개점날짜
			result.put("isClosing", closing != null ? closing.getIsClosing(): false);
			
			logger.debug("오늘 날짜 : " + format_today);
			logger.debug("openDt(개점날짜) 상태 : " + closing.getOpenDt());
			logger.debug("IsClosing(영업오픈(false:0) / 영업종료(true:1)) 상태 : " + closing.getIsClosing()); // false(0 - 영업오픈) / true(1 - 영업종료)
			return result; // {openDt=Mon Dec 07 00:00:00 KST 2020, isClosing=false} // 영업종료 : openDt = null
			
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			return null;
		}
	}

	
	@Override
	public SingleMap saveStoreTemp() throws RequestResolveException {
		StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		// 포스번호, 등록자, 상태 갱신
		SvnStoreTemp record = new SvnStoreTemp();
		record.setStoreId(staffUserDetail.getStoreId());
		Long count = storeMapper.saveStoreTemp(record);

		SingleMap result = new SingleMap();
		result.put("result", count);
		return result;
	}
	
	
	@Override
	public SvcStore getStoreById(Long id) {
		SvcStoreExample example = new SvcStoreExample();
		example.createCriteria() 
				.andIdEqualTo(id);
		List<SvcStore> storeList = svcStoreMapper.selectByExample(example);
		
		
		return storeList.size() > 0 ? storeList.get(0) : null;
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
		updateInfo.put("isUpdatable", latestVersionCode > versionCode);
		updateInfo.put("isStrict", getHasStrictUpdate(appType, osType, versionCode, latestVersionCode)); // 강제 유무, true이면 반드시 업데이트 해야 함.
		updateInfo.put("versionName", appInfo.getVersion());
		updateInfo.put("versionCode", latestVersionCode);


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


	private SingleMap getOrderSimpleByOrderId(SingleMap param) {
		SingleMap result = orderMapper.selectOrderSimpleByOrderId(param);
		if (result == null) {
			return null;
		}
		result.put("svcOrderItems", orderMapper.selectOrderItemSimpleListByOrderId(param));
		return result;
	}




	
	/**
	 * ExtSvcItem에 Option을 검색하여 추가
	 * 
	 * @param items
	 * @param itemIds
	 */
	private void appendItemOptions(List<SvcItemExtended> items, List<Long> itemIds) {

		List<SvcItemOptExtended> allOptions = getItemOptionListByItemIds(itemIds);
		List<SvcItemOptDtl> allDetails = getItemOptionDetailListByItemIds(itemIds);

		// 옵션에 옵션 상세 추가		
		List<SvcItemOptDtl> details;
		for (SvcItemOptExtended option : allOptions) {
			details = option.getDetails();
			for (SvcItemOptDtl dtl : allDetails) {
				if (Objects.equal(option.getId(), dtl.getOptId())) {
					details.add(dtl);
				}
			}
		}

		// 상품에 옵션 설정
		List<SvcItemOptExtended> options;
		for (SvcItemExtended item : items) {
			options = item.getOptions();
			for (SvcItemOptExtended opt : allOptions) {
				if (Objects.equal(item.getId(), opt.getItemId())) {
					options.add(opt);
				}
			}
		}
	}

	
	private List<SvcItemOptExtended> getItemOptionListByItemIds(List<Long> itemIds) {
		// 옵션 조회 조건
		SvcItemOptExample optExample = new SvcItemOptExample();
		optExample.createCriteria() //
				.andItemIdIn(itemIds) //
				.andIsUsedEqualTo(true);
		optExample.setOrderByClause("ITEM_ID, ORDINAL ASC");

		// 옵션 조회
		List<SvcItemOpt> options = svcItemOptMapper.selectByExample(optExample);

		// 옵션 상세를 담을수 있는 타입으로 변환
		List<SvcItemOptExtended> result = new ArrayList<>(options.size());
		for (SvcItemOpt opt : options) {
			SvcItemOptExtended extOpt = objectMapper.convertValue(opt, SvcItemOptExtended.class);
			extOpt.setDetails(new ArrayList<SvcItemOptDtl>());
			result.add(extOpt);
		}
		return result;
	}

	
	private List<SvcItemOptDtl> getItemOptionDetailListByItemIds(List<Long> itemIds) {
		SvcItemOptDtlExample example = new SvcItemOptDtlExample();
		example.createCriteria() //
				.andItemIdIn(itemIds);
		example.setOrderByClause("ITEM_ID, OPT_ID, ORDINAL ASC");
		return svcItemOptDtlMapper.selectByExample(example);
	}

	
	private void appendItemImages(List<SvcItemExtended> items, List<Long> itemIds) {
		List<SvcItemImg> allImages = getItemImageListByItemIds(itemIds);
		List<SvcItemImg> images;

		for (SvcItemExtended item : items) {
			images = item.getImages();
			for (SvcItemImg opt : allImages) {
				if (Objects.equal(item.getId(), opt.getItemId())) {
					images.add(opt);
				}
			}
		}
	}

	
	private List<SvcItemImg> getItemImageListByItemIds(List<Long> itemIds) {
		SvcItemImgExample example = new SvcItemImgExample();
		example.createCriteria() //
				.andItemIdIn(itemIds);
		example.setOrderByClause("ITEM_ID, ORDINAL ASC");
		return svcItemImgMapper.selectByExample(example);
	}


	public SvcUserMapping getUserMappingByUserId(Long userId) {
		SvcUserMappingExample example = new SvcUserMappingExample();
		example.createCriteria() // 검색 조건
				.andUserIdEqualTo(userId);
		List<SvcUserMapping> userMappings = svcUserMappingMapper.selectByExample(example);
		return userMappings.size() > 0 ? userMappings.get(0) : null;
	}

	
	/**
	 * UserName으로 UserId를 조회
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserByUserName(String userName) {
		UserExample args = new UserExample();
		args.createCriteria() // 검색 조건 
				.andUsernameEqualTo(userName) // 사용자 로그인
//				.andTypeEqualTo("300004") // 매장 사용자 FIXME 실 사용자 타입으로 설정
				.andTypeEqualTo("300006") // 매장 관리자				
				.andStatusEqualTo("301001"); // 계정 상태 정상
		List<User> users = userMapper.selectByExample(args);
		return users.size() > 0 ? users.get(0) : null;
	}

	
	public User getAgentUserByUserName(String userName) {
		UserExample args = new UserExample();
		args.createCriteria() // 검색 조건 
				.andUsernameEqualTo(userName) // 사용자 로그인				
				.andTypeEqualTo(USER_TYPE_AGENT_USER) // 매장 관리자				
				.andStatusEqualTo("301001"); // 계정 상태 정상
		List<User> users = userMapper.selectByExample(args);
		return users.size() > 0 ? users.get(0) : null;
	}


	private SvcStore getStoreByStoreCode(String storeCode) {
		SvcStoreExample example = new SvcStoreExample();
		example.createCriteria() //
				.andStoreCdEqualTo(storeCode) //
				.andStoreStNotEqualTo("350003"); // 폐점;
		List<SvcStore> storeList = svcStoreMapper.selectByExample(example);
		return storeList.size() > 0 ? storeList.get(0) : null;
	}
	
	
	/**
	 * 마감되지 않은 최종 openDt
	 * @param storeId
	 * @return
	 */
	public SvcClosing getLatestOpenByStoreId(long storeId) {

		SvcClosingExample example = new SvcClosingExample();
		example.createCriteria() // 조건
				.andStoreIdEqualTo(storeId);
				//.andIsClosingEqualTo(false);
		
		
		example.setOrderByClause("OPEN_DT DESC"); // 최근 순서

		List<SvcClosing> result = svcClosingMapper.selectByExampleWithRowbounds(example, ROW_BOUNDS_JUST_FIRST); //해당 tb_svc_closing의 가장 최근 순서 select
		
		return result.size() > 0 ? result.get(0) : null;
	}
	

	/**
	 * 해당 상점의 포스가 설정한 락을 해제한다.
	 * 
	 * @param storeId
	 *            해제할 상점 ID
	 * @param posNo
	 *            해제할 포스 ID
	 */
	private void releaseTableLock(long storeId, String posNo) {

		// 락은 포스당 한 테이블만 가능
		SvcTableExample example = new SvcTableExample();
		example.createCriteria() // 락을 해제할 테이블 조건
				.andStoreIdEqualTo(storeId) // 동일 매장
				.andPosNoEqualTo(posNo) // 해당 포스의 락				
				.andIsUsedEqualTo(true); // 사용중인 것을
		SvcTable record = new SvcTable();
		record.setIsUsed(false); // 락 해제		
		svcTableMapper.updateByExampleSelective(record, example);
	}
	
}
