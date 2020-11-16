package com.jc.pico.controller.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcDeviceLicenseMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.pos.PosBaseService;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosMasterCodeInfo;
import com.jc.pico.utils.bean.PosMasterConfigInfo;
import com.jc.pico.utils.bean.PosMasterDownloadList;
import com.jc.pico.utils.bean.PosMasterEmployeeAuthority;
import com.jc.pico.utils.bean.PosMasterEmployeeInfo;
import com.jc.pico.utils.bean.PosMasterGoodsClass;
import com.jc.pico.utils.bean.PosMasterGoodsInfo;
import com.jc.pico.utils.bean.PosMasterKitchenMsg;
import com.jc.pico.utils.bean.PosMasterLicenseCheck;
import com.jc.pico.utils.bean.PosMasterMemberInfo;
import com.jc.pico.utils.bean.PosMasterPluClass;
import com.jc.pico.utils.bean.PosMasterPluGoods;
import com.jc.pico.utils.bean.PosMasterPluType;
import com.jc.pico.utils.bean.PosMasterPosInfo;
import com.jc.pico.utils.bean.PosMasterTableInfo;
import com.jc.pico.utils.bean.PosPrinterInfo;
import com.jc.pico.utils.bean.PosResult;
import com.jc.pico.utils.bean.PosResultStoreInfo;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.pos.PosMasterCodeInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterConfigInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterEmployeeAuthorityMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterEmployeeInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterGoodsClassMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterGoodsInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterLicenseCheckMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterMemberInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterPluClassMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterPluGoodsMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterPluTypeMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterPosInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosMasterTableInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosStoreMapper;

/**
 * POS 연동부 중 매장정보와 관련된 컨트롤러
 * 
 * @author green
 *
 */
@RestController
@RequestMapping(value = "/api/pos/store")
public class PosBaseController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PosMasterLicenseCheckMapper posMasterLicenseCheckMapper;

	@Autowired
	private PosStoreMapper posStoreMapper;

	@Autowired
	private PosMasterConfigInfoMapper posMasterConfigInfoMapper;

	@Autowired
	private PosMasterCodeInfoMapper posMasterCodeInfoMapper;

	@Autowired
	private PosMasterPosInfoMapper posMasterPosInfoMapper;

	@Autowired
	private PosMasterEmployeeInfoMapper posMasterEmployeeInfoMapper;

	@Autowired
	private PosMasterEmployeeAuthorityMapper posMasterEmployeeAuthorityMapper;

	@Autowired
	private PosMasterGoodsInfoMapper posMasterGoodsInfoMapper;

	@Autowired
	private PosMasterGoodsClassMapper posMasterGoodsClassMapper;

	@Autowired
	private PosMasterPluTypeMapper posMasterPluTypeMapper;

	@Autowired
	private PosMasterPluClassMapper posMasterPluClassMapper;

	@Autowired
	private PosMasterPluGoodsMapper posMasterPluGoodsMapper;

	@Autowired
	private PosMasterTableInfoMapper posMasterTableInfoMapper;

	@Autowired
	private PosMasterMemberInfoMapper posMasterMemberInfoMapper;

	@Autowired
	private SvcDeviceLicenseMapper svcDeviceLicenseMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PosBaseService posBaseService;

	@Autowired
	private PosUtil posUtil;

	@Autowired
	private CodeUtil codeUtil;

	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
	}

	/**
	 * POS 연동 (0.M_LICENSE_CHECK:라이선스 체크)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">LICENSE_KEY</b> String <b>라이센스번호</b></li>
	 *            <li><b style="color:#FF0F65;">VERSION</b> Integer <b>프로그램 버전</b></li>
	 *            <li><b style="color:#FF0F65;">HW_INFO</b> String <b>하드웨어 정보(맥주소)</b></li>
	 *            <li><b style="color:#FF0F65;">STR_CD</b> String <b>가맹점코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>포스번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_INSTALL</b> Integer <b>설치구분 (1:신규,
	 *            2:재설치)</b></li>
	 *            <li><b style="color:#FF0F65;">TIMEZONE</b> String <b>시간대</b></li>
	 *            </ol>
	 * @example {"LICENSE_KEY": "ABCD-EFGH-IJKL-MNOP","VERSION": 1,"HW_INFO": "HWiNFoGooD","STR_CD":
	 *          "VOOVO_STORE_1","NO_POS": "POS-001","DS_INSTALL": 1,"TIMEZONE": "UTC"}
	 * @return {@link PosResult} DATAS: {@link PosMasterLicenseCheck}
	 */
	@RequestMapping(value = "/M_LICENSE_CHECK", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mLiceseCheck(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		String licenseKey = null; // 라이센스번호
		Integer version = null; // 프로그램 버전
		String hwInfo = null; // 하드웨어 정보(맥주소)
		String strCd = null; // 가맹점코드
		String noPos = null; // 포스번호
		Integer dsInstall = null; // 설치구분 (1:신규, 2:재설치)
		String timezone = null; // 시간대
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			licenseKey = (String) jsonObject.get("LICENSE_KEY"); // 라이센스번호
			version = (Integer) jsonObject.get("VERSION"); // 프로그램 버전
			hwInfo = (String) jsonObject.get("HW_INFO"); // 하드웨어 정보(맥주소)
			strCd = (String) jsonObject.get("STR_CD"); // 가맹점코드
			noPos = (String) jsonObject.get("NO_POS"); // 포스번호
			dsInstall = (Integer) jsonObject.get("DS_INSTALL"); // 설치구분 (1:신규, 2:재설치)
			timezone = (String) jsonObject.get("TIMEZONE"); // 시간대

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵
			logger.debug("username: {}", username);

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId); // 인증에서 받아옴
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId); // 인증에서 받아옴
			params.put("licenseKey", licenseKey); // 라이센스키
			params.put("noPos", noPos); // 포스번호
			params.put("strCd", strCd); // 가맹점코드
			List<Object> posMasterLicenseChecks = posMasterLicenseCheckMapper.selectDefault(params);
			if (posMasterLicenseChecks != null && posMasterLicenseChecks.size() > 0) {
				int beansSize = posMasterLicenseChecks.size();

				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterLicenseChecks);
			} else {
				posResult.setSuccess(false);
				posResult.setResultMsg(PosMasterLicenseCheck.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterLicenseCheck.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (1.M_DOWNLOAD_LIST:자료수신(변경수신목록))
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterDownloadList}
	 */
	@RequestMapping(value = "/M_DOWNLOAD_LIST", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mDownloadList(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			List<Object> posMasterDownloadLists = new ArrayList<Object>();
			PosMasterDownloadList posMasterDownloadList = new PosMasterDownloadList();
			posMasterDownloadList.setCdStore(Long.toString(storeId));
			posMasterDownloadList.setTableName("");
			posMasterDownloadList.setUpdateNum(0);
			posMasterDownloadList.setYnDelete(1);
			posMasterDownloadLists.add(posMasterDownloadList);
			int beansSize = posMasterDownloadLists.size();

			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(beansSize);
			posResult.setDatas(posMasterDownloadLists);

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterDownloadList.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (2.M_STORE_INFO:매장 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosResultStoreInfo}
	 */
	@RequestMapping(value = "/M_STORE_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mStoreInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			List<Object> posResultStoreInfos = posStoreMapper.selectStoreInfos(params);
			if (posResultStoreInfos != null && posResultStoreInfos.size() > 0) {
				int posResultStoreInfosSize = posResultStoreInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < posResultStoreInfosSize; inx++) {
					PosResultStoreInfo posResultStoreInfo = (PosResultStoreInfo) posResultStoreInfos.get(inx);
					posResultStoreInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posResultStoreInfo.getDtInsert()));
					posResultStoreInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posResultStoreInfo.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(posResultStoreInfosSize);
				posResult.setDatas(posResultStoreInfos);
			} else {
				posResult.setSuccess(false);
				posResult.setResultMsg(PosResultStoreInfo.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosResultStoreInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (3. M_POS_INFO : 포스 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterPosInfo}
	 */
	@RequestMapping(value = "/M_POS_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mPosInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterPosInfos = posMasterPosInfoMapper.selectDefault(params);
			if (posMasterPosInfos != null && posMasterPosInfos.size() > 0) {
				int beansSize = posMasterPosInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterPosInfo posMasterPosInfo = (PosMasterPosInfo) posMasterPosInfos.get(inx);
					posMasterPosInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterPosInfo.getDtInsert()));
					posMasterPosInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterPosInfo.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterPosInfos);
			} else {
				logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterPosInfos is Null or Size=0");
				posResult.setSuccess(false);
				posResult.setResultMsg(PosMasterPosInfo.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterPosInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (5. M_CONFIG_INFO : 환경설정)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterConfigInfo}
	 */
	@RequestMapping(value = "/M_CONFIG_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mConfigInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			List<Object> posMasterConfigInfos = posMasterConfigInfoMapper.selectDefault(params);
			if (posMasterConfigInfos != null && posMasterConfigInfos.size() > 0) {
				int beansSize = posMasterConfigInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterConfigInfo posMasterConfigInfo = (PosMasterConfigInfo) posMasterConfigInfos.get(inx);
					posMasterConfigInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterConfigInfo.getDtInsert()));
					posMasterConfigInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterConfigInfo.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterConfigInfos);
			} else {
				logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterConfigInfos is Null or Size=0");
				posResult.setSuccess(false);
				posResult.setResultMsg(PosMasterConfigInfo.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterConfigInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (6. M_PRINTER_INFO : 프린터 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosPrinterInfo}
	 */
	@RequestMapping(value = "/M_PRINTER_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mPrinterInfo(@RequestParam(value = "PARAM") String jsonString, Authentication authentication) {

		PosResult posResult = new PosResult();
		try {
			SingleMap param = objectMapper.readValue(jsonString, SingleMap.class);

			String username = (authentication != null ? authentication.getName() : null);
			param.put("username", username);

			List<PosPrinterInfo> priters = posBaseService.getPrinterInfoList(param);
			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(priters.size());
			posResult.setDatas(priters);

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg("Failed get printer info. cause by " + e.getMessage());
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterCodeInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (7. M_CODE_INFO : 공통 코드)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterCodeInfo}
	 */
	@RequestMapping(value = "/M_CODE_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mCodeInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			List<PosMasterCodeInfo> posMasterCodeInfos = posBaseService.getCodeInfoList(storeId, companyId);
			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(posMasterCodeInfos.size());
			posResult.setDatas(posMasterCodeInfos);

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterCodeInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (8. M_EMPLOYEE_INFO : 사원 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterEmployeeInfo}
	 */
	@RequestMapping(value = "/M_EMPLOYEE_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mEmployeeInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterEmployeeInfos = posMasterEmployeeInfoMapper.selectDefault(params);
			if (posMasterEmployeeInfos != null && posMasterEmployeeInfos.size() > 0) {
				int beansSize = posMasterEmployeeInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterEmployeeInfo posMasterEmployeeInfo = (PosMasterEmployeeInfo) posMasterEmployeeInfos.get(inx);
					Integer normal = Integer.valueOf(codeUtil.getBaseCodeByAlias("staff-st-normal"));
					posMasterEmployeeInfo.setYnUse(normal.equals(posMasterEmployeeInfo.getYnUse()) ? 1 : 0);
					posMasterEmployeeInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterEmployeeInfo.getDtInsert()));
					posMasterEmployeeInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterEmployeeInfo.getDtUpdate()));
				}

				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterEmployeeInfos);
			} else {
				logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterEmployeeInfos is Null or Size=0");
				posResult.setSuccess(false);
				posResult.setResultMsg(PosMasterEmployeeInfo.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterEmployeeInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (9. M_EMPLOYEE_AUTHORITY : 사원 권한)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterEmployeeAuthority}
	 */
	@RequestMapping(value = "/M_EMPLOYEE_AUTHORITY", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mEmployeeAuthority(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterEmployeeAuthoritys = posMasterEmployeeAuthorityMapper.selectDefault(params);
			if (posMasterEmployeeAuthoritys != null && posMasterEmployeeAuthoritys.size() > 0) {
				int beansSize = posMasterEmployeeAuthoritys.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterEmployeeAuthority posMasterEmployeeAuthority = (PosMasterEmployeeAuthority) posMasterEmployeeAuthoritys.get(inx);
					posMasterEmployeeAuthority.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterEmployeeAuthority.getDtInsert()));
					posMasterEmployeeAuthority.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterEmployeeAuthority.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterEmployeeAuthoritys);
			} else {
				logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterEmployeeAuthoritys is Null or Size=0");
				posResult.setSuccess(false);
				posResult.setResultMsg(PosMasterEmployeeAuthority.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterEmployeeAuthority.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (10. M_GOODS_INFO : 상품 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterGoodsInfo}
	 */
	@RequestMapping(value = "/M_GOODS_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mGoodsInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterGoodsInfos = posMasterGoodsInfoMapper.selectDefault(params);
			if (posMasterGoodsInfos != null && posMasterGoodsInfos.size() > 0) {
				int beansSize = posMasterGoodsInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterGoodsInfo posMasterGoodsInfo = (PosMasterGoodsInfo) posMasterGoodsInfos.get(inx);
					Integer use = Integer.valueOf(codeUtil.getBaseCodeByAlias("item-st-Y"));
					posMasterGoodsInfo.setYnUse(use.equals(posMasterGoodsInfo.getYnUse()) ? 1 : 0);
					posMasterGoodsInfo.setOptions(posBaseService.getPosMasterGoodsOpts(posMasterGoodsInfo.getCdGoods()));
					posMasterGoodsInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterGoodsInfo.getDtInsert()));
					posMasterGoodsInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterGoodsInfo.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterGoodsInfos);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterGoodsInfos is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posMasterGoodsInfos);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterGoodsInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (16. M_PLU_TYPE : PLU 타입)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterPluType}
	 */
	@RequestMapping(value = "/M_PLU_TYPE", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mPluType(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterPluTypes = posMasterPluTypeMapper.selectDefault(params);
			if (posMasterPluTypes != null && posMasterPluTypes.size() > 0) {
				int beansSize = posMasterPluTypes.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterPluType posMasterPluType = (PosMasterPluType) posMasterPluTypes.get(inx);
					posMasterPluType.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterPluType.getDtInsert()));
					posMasterPluType.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterPluType.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterPluTypes);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterPluTypes is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posMasterPluTypes);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterPluType.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (17. M_PLU_CLASS : PLU 분류)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterPluClass}
	 */
	@RequestMapping(value = "/M_PLU_CLASS", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mPluClass(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterPluClasss = posMasterPluClassMapper.selectDefault(params);
			if (posMasterPluClasss != null && posMasterPluClasss.size() > 0) {
				int beansSize = posMasterPluClasss.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterPluClass posMasterPluClass = (PosMasterPluClass) posMasterPluClasss.get(inx);
					posMasterPluClass.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterPluClass.getDtInsert()));
					posMasterPluClass.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterPluClass.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterPluClasss);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterPluClasss is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posMasterPluClasss);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterPluClass.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (18. M_PLU_GOODS : PLU 상품)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterPluGoods}
	 */
	@RequestMapping(value = "/M_PLU_GOODS", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mPluGoods(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterPluGoodss = posMasterPluGoodsMapper.selectDefault(params);
			if (posMasterPluGoodss != null && posMasterPluGoodss.size() > 0) {
				int beansSize = posMasterPluGoodss.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterPluGoods posMasterPluGoods = (PosMasterPluGoods) posMasterPluGoodss.get(inx);
					posMasterPluGoods.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterPluGoods.getDtInsert()));
					posMasterPluGoods.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterPluGoods.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterPluGoodss);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterPluGoodss is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posMasterPluGoodss);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterPluGoods.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (20. M_TABLE_INFO : 테이블 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterTableInfo}
	 */
	@RequestMapping(value = "/M_TABLE_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mTableInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<PosMasterTableInfo> posMasterTableInfos = posMasterTableInfoMapper.selectDefault(params);

			// 셀프 오더 주문 처리용 테이블 추가
			// FIXME 포스 요청으로 임시로 막음 (오작동이 있어 차단)
//			posBaseService.appendSelfOrderTableInfoTo(posMasterTableInfos, companyId, storeId);

			if (posMasterTableInfos != null && posMasterTableInfos.size() > 0) {
				int beansSize = posMasterTableInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterTableInfo posMasterTableInfo = posMasterTableInfos.get(inx);
					posMasterTableInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterTableInfo.getDtInsert()));
					posMasterTableInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterTableInfo.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterTableInfos);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterTableInfos is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posMasterTableInfos);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterTableInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (29. M_MEMBER_INFO : 회원 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            <li><b style="color:#FF0F65;">SEARCH_WORD</b> Long
	 *            <b>검색조건(고객코드,고객명,휴대폰번호)</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterMemberInfo}
	 */
	@RequestMapping(value = "/M_MEMBER_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mMemberInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {

		PosResult posResult = new PosResult();
		try {
			SingleMap params = objectMapper.readValue(jsonString, SingleMap.class);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);

			List<PosMasterMemberInfo> posMasterMemberInfos = posMasterMemberInfoMapper.selectDefault(params);
			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(posMasterMemberInfos.size());
			posResult.setDatas(posMasterMemberInfos);
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterMemberInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (31. M_GOODS_CLASS : 상품 분류)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">UPDATE_NUM</b> Long <b>업데이트 번호</b></li>
	 *            </ol>
	 * @Example {"UPDATE_NUM": 0}
	 * @return {@link PosResult} DATAS: {@link PosMasterGoodsClass}
	 */
	@RequestMapping(value = "/M_GOODS_CLASS", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mGoodsClass(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long updateNum = null; // 업데이트 번호
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			updateNum = jsonObject.get("UPDATE_NUM").getClass().equals(Long.class) ? (Long) jsonObject.get("UPDATE_NUM")
					: (Integer) jsonObject.get("UPDATE_NUM"); // 업데이트 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posMasterGoodsClasss = posMasterGoodsClassMapper.selectDefault(params);
			if (posMasterGoodsClasss != null && posMasterGoodsClasss.size() > 0) {
				int beansSize = posMasterGoodsClasss.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosMasterGoodsClass posMasterGoodsClass = (PosMasterGoodsClass) posMasterGoodsClasss.get(inx);
					posMasterGoodsClass.setDtInsert(posUtil.getStoreTime(localTimeZone, posMasterGoodsClass.getDtInsert()));
					posMasterGoodsClass.setDtUpdate(posUtil.getStoreTime(localTimeZone, posMasterGoodsClass.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posMasterGoodsClasss);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posMasterGoodsClasss is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posMasterGoodsClasss);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosMasterGoodsClass.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (32.M_LICENSE_SAVE:라이선스 등록)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">LICENSE_KEY</b> String <b>라이센스번호</b></li>
	 *            <li><b style="color:#FF0F65;">VERSION</b> Integer <b>프로그램 버전</b></li>
	 *            <li><b style="color:#FF0F65;">HW_INFO</b> String <b>하드웨어 정보(맥주소)</b></li>
	 *            <li><b style="color:#FF0F65;">STR_CD</b> String <b>가맹점코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>포스번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_INSTALL</b> Integer <b>설치구분 (1:신규,
	 *            2:재설치)</b></li>
	 *            <li><b style="color:#FF0F65;">TIMEZONE</b> String <b>시간대</b></li>
	 *            </ol>
	 * @example {"LICENSE_KEY": "ABCD-EFGH-IJKL-MNOP","VERSION": 1,"HW_INFO": "HWiNFoGooD","STR_CD":
	 *          "VOOVO_STORE_1","NO_POS": "POS-001","DS_INSTALL": 1,"TIMEZONE": "UTC"}
	 * @return {@link PosResult} DATAS: {@link PosMasterLicenseCheck}
	 */
	@RequestMapping(value = "/M_LICENSE_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mLiceseSave(@RequestParam(value = "PARAM") String jsonString) {

		PosResult posResult = new PosResult();

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			SingleMap param = objectMapper.readValue(jsonString, SingleMap.class);
			param.put("userId", posUtil.getAgentUserId(authentication));

			SingleMap result = posBaseService.registerDeviceLicense(param);
			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(1);
			posResult.setDatas(Lists.newArrayList(result));

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(SvcDeviceLicenseMapper.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(SvcDeviceLicenseMapper.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * 주방 메모
	 * 
	 * @param param
	 * 
	 */
	@RequestMapping(value = "/M_KITCHEN_MSG", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult mKitchenMsg(@RequestParam(value = "PARAM") String jsonString) {
		PosResult posResult = new PosResult();

		try {
//			SingleMap param = objectMapper.readValue(jsonString, SingleMap.class);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);

			List<PosMasterKitchenMsg> kitchenMsgList = posBaseService.getKitchenMessageByStoreId(storeId);

			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(kitchenMsgList.size());
			posResult.setDatas(kitchenMsgList);

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg("Failed get kitchen message. Cause by " + e.getMessage());
		}
		return posResult;
	}

}
