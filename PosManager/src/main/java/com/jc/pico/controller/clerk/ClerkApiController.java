package com.jc.pico.controller.clerk;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jc.pico.exception.DataNotFoundException;
import com.jc.pico.exception.DataNotRegisteredException;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.NoPermissionException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.service.clerk.ClerkCommonService;
import com.jc.pico.service.clerk.ClerkOrderService;
import com.jc.pico.service.clerk.ClerkRewardService;
import com.jc.pico.utils.ClerkUtil;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StoreParam;
import com.jc.pico.utils.customMapper.admin.CustomSalesMapper;
import com.jc.pico.utils.customMapper.pos.PosStorePrinterMapper;

/**
 * Store 앱 서비스를 위한 API
 * 
 *
 */
@RestController //@RestController : Controller가 REST 방식을 처리하기 위한 것임을 명시
@RequestMapping(value = "/clerk/api")
public class ClerkApiController {

	private static Logger logger = LoggerFactory.getLogger(ClerkApiController.class);

	/**
	 * Staff 권한
	 */
	private static final String ROLE_STORE_STAFF = "ROLE_STORE_STAFF";

	@Autowired
	private ClerkCommonService clerkCommonService;

	@Autowired
	private ClerkOrderService clerkOrderService;

	@Autowired
	private ClerkRewardService clerkRewardService;
	
	@Autowired
	private PosStorePrinterMapper posStorePrinterMapper;
	
	
	
	@Autowired
	private CustomSalesMapper customSalesMapper;
	
	@RequestMapping(value = "/appversion", method = RequestMethod.POST)
	public ClerkResult getAppVersion(HttpServletRequest request, @RequestBody StoreParam reqParam,  Authentication authentication)
			throws RequestResolveException {
		
		final SingleMap param = reqParam.getData();
		
		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getAppVersion(request.getServletContext(), param));
		result.setSuccess();
		
		return result;
	}
	
	/******     (1) url : /clerk/api/advertiseInfo     ******/
	
	@RequestMapping(value = "/advertiseInfo", method = RequestMethod.POST)
	public ClerkResult getAdvertise(HttpServletRequest request, @RequestBody StoreParam reqParam, OAuth2Authentication authentication)
			throws RequestResolveException {

		final SingleMap param = reqParam.getData();
		
		logger.debug("getAdvertise  : " + reqParam); //StoreParam [header={os=android, posNo=003, lang=ko}, data={}]

		param.put("host", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort())); //http://192.168.0.166:포트번호 -- 내 ip주소
		param.put("storeId", ClerkUtil.getStaffStoreId(authentication));
		
		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getAdvertiseInfo(request.getServletContext(), param)); //{host=http://192.168.0.166, storeId=89}
		result.setSuccess();
		
		logger.debug("getAdvertise  : " + result); //StoreParam [header={os=android, posNo=003, lang=ko}, data={host=http://192.168.0.166, storeId=89}]

		
		return result;
	}
	
	/******     (2) url : /clerk/api/appinfo     ******/
	
	/**
	 * 앱 정보 조회
	 * 
	 * @param param
	 *        storeId : 스토어 Id
	 * 
	 * @return 버전
	 * @throws RequestResolveException
	 * 
	 */
	@RequestMapping(value = "/appinfo", method = RequestMethod.POST) //@RequestBody : JSON 데이터를 원하는 타입으로 바인딩 처리
	public ClerkResult getAppInfo(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getAppInfo : " + reqParam);

		SingleMap param = reqParam.getData();
		param.putAll(reqParam.getHeader()); // putAll()메소드를 이용해서 복사
		
		System.out.println("앱버전>>>>>>>>>>>"+param); //withTableLock=release, os=android, appType=108004, posNo=105, storeId=89, lang=ko, versionCode=28

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getAppInfo(param));
		result.setSuccess(); //ECL0000 -- 성공
		return result;
	}
	
	/******     (3) url : /clerk/api/coupon     ******/
	
	/**
	 * 쿠폰 정보 조회
	 * 
	 * @param param
	 *            couponCd : 쿠폰 코드
	 * 
	 * 
	 * @return 쿠폰 정보
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 * @throws InvalidJsonException
	 * 
	 */
	@RequestMapping(value = "/coupon/detail", method = RequestMethod.POST)
	public ClerkResult getCouponDetail(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException, RequestResolveException, InvalidJsonException {
		logger.debug("getCouponDetail " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", (authentication != null ? authentication.getName() : ""));

		ClerkResult result = new ClerkResult();
		result.setData(clerkRewardService.getCouponDetail(param));
		result.setSuccess();

		return result;
	}
	
	/******     (4) url : /clerk/api/license     ******/
	
	/**
	 * 디바이스 라이센스 등록
	 * 
	 * @param param
	 * 
	 * 
	 * @return
	 * @throws RequestResolveException
	 *             - ECL0004 : 존재하지 않는 레이센스
	 *             - ECL0005 : 사용중인 라이센스
	 *             - ECL0006 : 미사용 라이센스
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/license/register", method = RequestMethod.POST)
	public ClerkResult registerDeviceLicense(@RequestBody StoreParam reqParam, Authentication authentication)
			throws RequestResolveException, InvalidParamException, NoPermissionException {

		logger.debug("registerDeviceLicense : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", ClerkUtil.getAgentUserName(authentication));
		//param.putIfEmpty("deviceType", "876003"); // Clerk FIXME 제거 필요

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.registerDeviceLicense(param));
		result.setSuccess();

		return result;
	}	
	
	
	/**
	 * 키오스크 디바이스 라이센스 등록
	 * 
	 * @param param
	 * 
	 * 
	 * @return
	 * @throws RequestResolveException
	 *             - ECL0004 : 존재하지 않는 레이센스
	 *             - ECL0005 : 사용중인 라이센스
	 *             - ECL0006 : 미사용 라이센스
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/license/registerKiosk", method = RequestMethod.POST)
	public ClerkResult registerDeviceLicenseKiosk(@RequestBody StoreParam reqParam, Authentication authentication)
			throws RequestResolveException, InvalidParamException, NoPermissionException {

		logger.debug("registerDeviceLicenseKiosk : " + reqParam); // ▶ StoreParam [header={os=android, posNo=, lang=ko}, data={deviceType=876004, licenseKey=9D4A-4772-B39E-E870, isMain=true, installType=1, hwInfo=G7FXOJYDBD}] 
		
		SingleMap param = reqParam.getData();
		param.put("userName", ClerkUtil.getAgentUserName(authentication));
		
		logger.debug(authentication.toString());
		
		logger.debug("param : " + param); // ▶  {deviceType=876004, licenseKey=11CE-4865-9C83-8802, isMain=true, installType=1, hwInfo=G7FXOJYDBD, userName=installman} 

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.registerDeviceLicenseKiosk(param));
		result.setSuccess();
		
		return result;
	}	
	
	/**
	 * 포스 번호 수정
	 * 
	 * @param param
	 * 
	 * 
	 * @return
	 * @throws RequestResolveException
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/license/register/posno", method = RequestMethod.POST)
	public ClerkResult updateLicensePosNo(@RequestBody StoreParam reqParam, Authentication authentication)
			throws RequestResolveException, InvalidParamException, NoPermissionException {

		logger.debug("updateLicensePosNo : " + reqParam);

		SingleMap param = reqParam.getData();

		param.put("userName", ClerkUtil.getAgentUserName(authentication));
		param.putIfEmpty("deviceType", "876003"); // KIOSK DEVICE

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.updateLicensePosNo(param));
		result.setSuccess();

		return result;
	}	
	
	/**
	 * getDeviceAndStoreInfoKiosk
	 * 
	 *   키오스크 디바이스  라이선스 정보
	 *   
	 * @param param
	 * 
	 * 
	 * @return
	 * @throws RequestResolveException
	 *             - ECL0004 : 존재하지 않는 레이센스
	 *             - ECL0005 : 사용중인 라이센스
	 *             - ECL0006 : 미사용 라이센스
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/license/getDeviceInfoKiosk", method = RequestMethod.POST)
	public ClerkResult getDeviceInfoKiosk(@RequestBody StoreParam reqParam, Authentication authentication)
			throws RequestResolveException, InvalidParamException, NoPermissionException {

		logger.debug("getDeviceInfoKiosk   : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", ClerkUtil.getAgentUserName(authentication));

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getDeviceInfoKiosk(param));
		result.setSuccess();

		return result;
	}
	
	/******     (5) url : /clerk/api/open     ******/
	
	/**
	 * pos 의 S_OPEN_INFO 프로세스를 가져왔음
	 * @param request
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/open", method = RequestMethod.POST )
	public ClerkResult sOpenInfo(HttpServletRequest request, @RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException{
		
		final SingleMap param = reqParam.getData();
		
		logger.debug("sOpenInfo  : " + reqParam); //StoreParam [header={os=android, posNo=002, lang=ko}, data={openDateApc=20201208}]
		
		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.sOpenInfo(param));
		result.setSuccess();
	
		return result;
	}	
	
	/******     (6) url : /clerk/api/order     ******/
	
	/**
	 * 주문 정보 조회
	 * 
	 * @param param
	 *            orderId : order id
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/order/detail", method = RequestMethod.POST)
	public ClerkResult getOrderDetail(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getOrder : " + reqParam);

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getOrderDetail(param));
		result.setSuccess();

		return result;
	}


	
	/**
	 * getOrderDetailEx
	 * 
	 * 주문 정보 조회(취소를 위해서 주문 정보 조회)
	 * 
	 * @param param
	 * 			storeId : store Id
	 *          brandId : brand Id
	 *          orderId : order id
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/order/detailOrderKiosk", method = RequestMethod.POST)
	public ClerkResult getOrderDetailEx(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getOrder : " + reqParam);

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getOrderDetailEx(param));
		result.setSuccess();

		return result;
	}

	
	
	/**
	 * 매출  정보 조회(키오스크)
	 * 추가 : 
	 * @param param
	 *            법인
	 *            브랜드
	 *            스토어
	 *            시작일
	 *            종료일
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	//@RequestMapping(value = "/order/detailKiosk", method = RequestMethod.POST)
	//public ClerkResult getOrderDetailKiosk(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
	@RequestMapping(value = "/order/detailKiosk", method = RequestMethod.POST)
	public ClerkResult getOrderDetailKiosk(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("detailKiosk > getOrder : " + reqParam); 
		//StoreParam [header={os=android, posNo=002, lang=ko}, data={endDate=2020-12-16 23:59:59, brandId=44, tableId=1, storeId=89, startDate=2020-12-16 00:00:00}] 

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getOrderDetailKioskEx(param));
		result.setSuccess();
		
		return result;
	}

	
	
	/**
	 * 주문 정보 저장
	 * 
	 * @param param
	 *            order : 주문 정보
	 *            tableId : 테이블 ID
	 * 
	 * 
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/order/save", method = RequestMethod.POST)
	public ClerkResult saveOrder(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("saveOrder : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", (authentication != null ? authentication.getName() : ""));
		param.putAll(reqParam.getHeader());

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.saveOrder(param));
		result.setSuccess();

		return result;
	}


	
	/**
	 * 주문 정보 저장 (키오스크)
	 * 수정 : 
	 * 
	 * @param param
	 *            order : 주문 정보
	 *            tableId : 테이블 ID
	 * 
	 * @return
	 * 
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/order/saveKiosk", method = RequestMethod.POST)
	public ClerkResult saveOrderKiosk(HttpServletRequest request, @RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("주문정보 >>> saveOrderKiosk : " + reqParam);
		
		//매인매장 주문시
		//특산품 - 배송지입력O
		//특산품 - 배송지입력X
		                
		
		SingleMap param = reqParam.getData();
		param.put("host", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
		
		
		param.put("userName", (authentication != null ? authentication.getName() : ""));
		logger.debug("userName : " + authentication.getName()); //kiosk1_-_89_-_2332 
		param.putAll(reqParam.getHeader());

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.saveOrderKiosk(request.getServletContext(), param));
		result.setSuccess();

		return result;
	}	
	
	/**
	 * setOrderCancelComplete
	 * 주문, 매출 취소
	 * 
	 * @param param
	 * 			  Id
	 *            storeId
	 *            brandId
	 *            orderId
	 * 
	 * @return true(성공) / false(실패)
	 * 
	 * @throws InvalidParamException
	 * 
	 */
	@RequestMapping(value = "/order/setOrderCancelComplete", method = RequestMethod.POST)
	public ClerkResult setOrderCancelComplete(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException {

		logger.debug("setOrderCancelComplete : " + reqParam);

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.setOrderCancelComplete(param));
		result.setSuccess();

		return result;
	}
	

	/**
	 * getMerchantAndPrinter
	 * 결재  ID,  주방 프린터 정보 
	 * 
	 * @param param
	 *            brandId
	 *            storeId
	 * 
	 * @return 
	 * 
	 * @throws InvalidParamException
	 * 
	 */
	@RequestMapping(value = "/order/getMerchantAndPrinter", method = RequestMethod.POST)
	public ClerkResult getMerchantAndPrinter(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException {

		logger.debug("getMerchantAndPrinter : " + reqParam);

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getMerchantAndPrinter(param));
		result.setSuccess();

		return result;
	}	
	
	/**
	 * 주방프린터 리스트
	 * @param request
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/order/kitchenprinter", method = RequestMethod.POST )
	public ClerkResult orderKitchenPrinter(HttpServletRequest request, @RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException{
		logger.debug("tempOrder : " + reqParam); //StoreParam [header={os=android, posNo=103, lang=ko}, data={}] 
		
		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getOrderKitchenPrinter());
		result.setSuccess();
	
		return result;
	}
		
	/**
	 * 매출  정보 조회(키오스크)
	 * 추가 : 
	 * @param param
	 *            브랜드
	 *            스토어
	 *            시작일
	 *            종료일
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	
	
	@RequestMapping(value = "/order/salesDetailKiosk", method = RequestMethod.POST)
	public ClerkResult getSalesDetailKiosk(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("SalesDtailKiosk : " + reqParam); 

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getSalesDetailKiosk(param));
		result.setSuccess();

		return result;
	}
	

	
	
	
	/******     (7) url : /clerk/api/plu     ******/
	
	/**
	 * 카테고리별 상품 조회
	 * 
	 * @param param
	 *            storeId : 스토어 id
	 * 
	 * @return
	 * @throws DataNotFoundException
	 * @throws DataNotRegisteredException
	 */
	@RequestMapping(value = "/plu/categories", method = RequestMethod.POST)
	public ClerkResult getPluCategories(@RequestBody StoreParam reqParam, Authentication authentication)
			throws DataNotFoundException, DataNotRegisteredException {
		
		logger.debug("getPluCategories >>>>>>>>>>>>>>>>>>>>>>> : " + reqParam); //StoreParam [header={os=android, posNo=002, lang=ko}, data={brandId=44, storeId=89}
		
		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getCategoriesDetail(param));
		result.setSuccess();

		return result;
	}
	
	//특산품 가게
	@RequestMapping(value = "/plu/categories/specialtyInfo", method = RequestMethod.POST)
	public ClerkResult getPluCategoriesSpecialtyInfo(@RequestBody StoreParam reqParam, Authentication authentication)
			throws DataNotFoundException, DataNotRegisteredException {	
		
		logger.debug("getPluCategoriesSpecialtyInfo : " + reqParam); //StoreParam [header={os=android, posNo=002, lang=ko}, data={brandId=44, storeId=89}
		
		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getPluCategoriesSpecialtyInfo(param));
		result.setSuccess();
		
		return result;
	}
	
	//배달가게 - 리스트(보류)
	@RequestMapping(value = "/plu/categories/deliveryInfo", method = RequestMethod.POST)
	public ClerkResult getPluCategoriesDeliveryInfo(@RequestBody StoreParam reqParam, Authentication authentication)
			throws DataNotFoundException, DataNotRegisteredException {	
		
		logger.debug("getPluCategoriesDeliveryInfo : " + reqParam); //StoreParam [header={os=android, posNo=002, lang=ko}, data={brandId=44, storeId=89}
		
		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getPluCategoriesDeliveryInfo(param));
		result.setSuccess();
		
		return result;
	}	
	
	/******     (8) url : /clerk/api/staff     ******/
	
	/**
	 * 매장 직원 정보 조회
	 * 
	 * 
	 * 
	 * @return
	 * @throws DataNotFoundException
	 * @throws NoPermissionException
	 * @throws DataNotRegisteredException
	 */
	@RequestMapping(value = "/staff/detail", method = RequestMethod.POST)
	public ClerkResult getStaffDetail(HttpServletRequest reqeust, @RequestBody StoreParam reqParam, OAuth2Authentication authentication)
			throws DataNotFoundException, NoPermissionException {

		logger.debug("getUserDetail : " + reqParam); //[header={os=android, posNo=103, lang=ko}, data={}

		if (!reqeust.isUserInRole(ROLE_STORE_STAFF)) {
			throw new NoPermissionException();
		}

		SingleMap param = reqParam.getData();
		param.put("staffDetail", authentication.getUserAuthentication().getDetails());

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getStaffDetail(param));
		result.setSuccess();

		return result;
	}
	
	/**
	 * 지점의 직원 유효성 검색
	 * 
	 * @param param
	 *            username 직원 ID
	 *            password 패스워드
	 * 
	 * 
	 * @return 직원 목록
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/staff/validation", method = RequestMethod.POST)
	public ClerkResult checkStaffValidation(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException {

		logger.debug("checkStaffAuthority");

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.checkStaffValidation(param));
		result.setSuccess();

		return result;
	}
	
		
	/******     (9) url : /clerk/api/store     ******/
	
	/**
	 * 스토어의 테이블 섹션 목록 및 섹션의 테이블 목록, 테이블의 간단한 주문 정보 조회
	 * 포스가 오픈전, 마감후 이면 예외를 던짐
	 * 
	 * @param param
	 *            stordId : 스토어 아이디
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/store/tables", method = RequestMethod.POST)
	public ClerkResult getStoreSections(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getStoreSections : " + reqParam);

		SingleMap param = reqParam.getData();

		clerkCommonService.throwIfPosNotReady(param.getLong("storeId"));

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getStoreTableSections(param));
		result.setSuccess();

		return result;
	}

	/**
	 * 스토어의 개점 정보, 테이블 섹션 목록 및 섹션의 테이블 목록, 테이블의 간단한 주문 정보 조회
	 * 포스가 오픈전, 마감후 이면 예외를 던짐
	 * 
	 * @param param
	 *            stordId : 스토어 아이디
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/store/status", method = RequestMethod.POST)
	public ClerkResult getStoreStatus(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getStoreStatus : " + reqParam);

		SingleMap param = reqParam.getData();

		clerkCommonService.throwIfPosNotReady(param.getLong("storeId"));

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getStoreStatus(param));
		result.setSuccess();

		return result;
	}
	
	/**
	 * 지점의 직원 조회
	 * 
	 * @param param
	 *            storeId 스토어 id
	 * 
	 * 
	 * @return 직원 목록
	 * 
	 * @throws InvalidParamException
	 * 
	 */
	@RequestMapping(value = "/store/staffs", method = RequestMethod.POST)
	public ClerkResult getStaffList(@RequestBody StoreParam reqParam, Authentication authentication) throws InvalidParamException {

		logger.debug("getStaffList : " + reqParam);

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getStaffList(param));
		result.setSuccess();

		return result;
	}
	
	/**
	 * 주방 메모 템플핏 메시지 조회
	 * 
	 * @param param
	 *            storeId : 상점 번호
	 *            itemId : 상품 ID
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/kitchen/message", method = RequestMethod.POST)
	public ClerkResult getKitchenMessage(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getKitchenMessage : " + reqParam);

		SingleMap param = reqParam.getData();
		param.putAll(reqParam.getHeader());

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getKichenMessage(param));
		result.setSuccess();

		return result;
	}

	
	

	
	
	/**
	 * 키오스크 상정 정보
	 * 
	 * @param param
	 * 
	 * 
	 * @return
	 * @throws RequestResolveException
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/store/getStoreInfoKiosk", method = RequestMethod.POST)
	public ClerkResult getStoreInfoKiosk(@RequestBody StoreParam reqParam, Authentication authentication)
			throws RequestResolveException, InvalidParamException, NoPermissionException {

		logger.debug("getStoreInfoKiosk : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", ClerkUtil.getAgentUserName(authentication));

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getStoreInfoKiosk(param));
		result.setSuccess();

		return result;
	}
	
	

	

	


	/**
	 * pos test
	 * @param request
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/store/temp", method = RequestMethod.POST )
	public ClerkResult sStoreTemp(HttpServletRequest request, @RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException{
		logger.debug("tempOrder : " + reqParam);
		
		clerkCommonService.saveStoreTemp();
		
		ClerkResult result = new ClerkResult();
		result.setData(null);
		result.setSuccess();
	
		return result;
	}
	
	/**
	 * getMerchantAndPrinter
	 * 결재  ID,  주방 프린터 정보 
	 * 
	 * @param param
	 *            brandId
	 *            storeId
	 * 
	 * @return 
	 * 
	 * @throws InvalidParamException
	 * 
	 */
	
	@RequestMapping(value = "/store/info", method = RequestMethod.POST)
	public ClerkResult storeInfo(@RequestBody StoreParam reqParam, Authentication authentication)
			 throws RequestResolveException {

		logger.debug("storeInfo : " + reqParam); // StoreParam [header={os=android, posNo=111, lang=ko}, data={hwInfo=G7FXOJYDBD, version=1.0.30}]
//		logger.debug("인증1>>>>>>>>> : " + authentication.getAuthorities());
//
//		logger.debug("인증1>>>>>>>>> : " + authentication.toString());
//		logger.debug("인증1>>>>>>>>> : " + authentication.getName());
		
		
		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.storeInfo(param));
		result.setSuccess();

		return result;
	}
	
	
	/******     (10) url : /clerk/api/table     ******/
	
	/**
	 * 테이블 고객수 변경
	 * 
	 * @param param
	 *            tableId 테이블 번호
	 *            customerCnt 고객수
	 * 
	 * 
	 * @return 직원 목록
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 * 
	 */
	@RequestMapping(value = "/table/customercnt", method = RequestMethod.POST)
	public ClerkResult updateTableCustomerCount(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException, RequestResolveException {
		logger.debug("updateTableCustomerCount");

		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.setTableCustomerCount(param));
		result.setSuccess();

		return result;
	}

	/**
	 * fromTable의 주문을 toTable로 이동 이동 처리
	 * 
	 * @param param
	 *            fromTableId 이동한 주문이 존재하는 테이블 ID
	 *            toTableId 이동을 완료할 테이블 ID
	 * 
	 * 
	 * @return 직원 목록
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 * 
	 */
	@RequestMapping(value = "/table/move", method = RequestMethod.POST)
	public ClerkResult moveTable(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException, RequestResolveException {
		logger.debug("moveTable " + reqParam);

		SingleMap param = reqParam.getData();
		param.putAll(reqParam.getHeader());

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.moveTableOrder(param));
		result.setSuccess();

		return result;
	}

	/**
	 * fromTable의 주문을 toTable로 이동 이동 처리
	 * 
	 * @param param
	 *            fromTableId 합치기할 주문이 존재하는 테이블 ID
	 *            toTableId 주문을 합쳐넣을 테이블 ID
	 * 
	 * 
	 * @return 변경된 테이블 정보 목록
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 * 
	 */
	@RequestMapping(value = "/table/merge", method = RequestMethod.POST)
	public ClerkResult mergeTable(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException, RequestResolveException {
		logger.debug("mergeTable " + reqParam);

		SingleMap param = reqParam.getData();
		param.putAll(reqParam.getHeader());

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.mergeTableOrder(param));
		result.setSuccess();

		return result;
	}



	/**
	 * 테이블 상세 정보 조회
	 * 
	 * @param param
	 *            tableId : table id
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/table/detail", method = RequestMethod.POST)
	public ClerkResult getTableDetail(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getTableDetail : " + reqParam);

		SingleMap param = reqParam.getData();
		param.putAll(reqParam.getHeader()); // posNo 번호를 사용함

		clerkCommonService.throwIfPosNotReady(param.getLong("storeId"));

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getTableDetail(param));
		result.setSuccess();

		return result;
	}

	
	/**
	 * 추가 : 
	 * 테이블 상세 정보 조회(키오스크)
	 * 
	 * @param param
	 *            tableId : table id
	 * 
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/table/detailKiosk", method = RequestMethod.POST)
	public ClerkResult getTableDetailKiosk(@RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException {
		logger.debug("getTableDetail : " + reqParam);

		SingleMap param = reqParam.getData();
		param.putAll(reqParam.getHeader()); // posNo 번호를 사용함

		// 수정 : 
		// 키오스크는 포스 상테 확인 하지 않음 
		//clerkCommonService.throwIfPosNotReady(param.getLong("storeId"));
		// 수정 : 

		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.getTableDetail(param));
		result.setSuccess();

		return result;
	}
	
	/******     (11) url : /clerk/api/update     ******/
	
	/**
	 * 주방프린터 프린트시 업데이트
	 * @param request
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/update/kitchenprinter", method = RequestMethod.POST )
	public ClerkResult orderKitchenPrinterUpdate(HttpServletRequest request, @RequestBody StoreParam reqParam, Authentication authentication) throws RequestResolveException{
		logger.debug("tempOrder : " + reqParam); //StoreParam [header={os=android, posNo=103, lang=ko}, data={orderNo=44891605854073931, printerNo=101, receiptNo=20201120-0004}] 
		
		ClerkResult result = new ClerkResult();
		result.setData(clerkOrderService.setOrderKitchenPrinter(reqParam.getData()));
		result.setSuccess();
	
		return result;
	}
	
	/******     (12) url : /clerk/api/user     ******/

	/**
	 * 사용자 정보 조회
	 * 
	 * @param param
	 * 
	 * 
	 * @return
	 * @throws DataNotFoundException
	 * @throws DataNotRegisteredException
	 */
	@RequestMapping(value = "/user/detail", method = RequestMethod.POST)
	public ClerkResult getUserDetail(@RequestBody StoreParam reqParam, Authentication authentication) {

		logger.debug("getUserDetail : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", (authentication != null ? authentication.getName() : ""));

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.getUserDetail(param));
		result.setSuccess();

		return result;
	}


	/**
	 * 스토어 사용자 유효성 검새
	 * 
	 * @param param
	 *            username 직원 ID
	 *            password 패스워드
	 * 
	 * 
	 * @return 직원 목록
	 * 
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 * 
	 */
	@RequestMapping(value = "/user/validation", method = RequestMethod.POST)
	public ClerkResult checkUserValidation(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, NoPermissionException {
		logger.debug("checkUserValidation");

		SingleMap param = reqParam.getData();
		param.put("userName", (authentication != null ? authentication.getName() : ""));

		ClerkResult result = new ClerkResult();
		result.setData(clerkCommonService.checkUserValidation(param));
		result.setSuccess();

		return result;
	}



	
	

	
	
}