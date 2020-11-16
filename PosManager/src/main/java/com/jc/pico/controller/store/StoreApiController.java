package com.jc.pico.controller.store;

import java.io.IOException;

import javax.naming.NoPermissionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.service.store.StoreCommonService;
import com.jc.pico.service.store.StoreHomeService;
import com.jc.pico.service.store.StoreKeeperService;
import com.jc.pico.service.store.StoreNotificationService;
import com.jc.pico.service.store.StoreRewardService;
import com.jc.pico.service.store.StoreSalesService;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StoreParam;
import com.jc.pico.utils.bean.StoreResult;
import com.jc.pico.utils.bean.StoreResult.ErrorCode;

/**
 * Store 앱 서비스를 위한 API
 * 
 *
 */
@RestController
@RequestMapping(value = "/store/api") // , consumes="application/json")
public class StoreApiController {

	private static final int COUNT_DEFAULT = 10;

	private static Logger logger = LoggerFactory.getLogger(StoreApiController.class);

	@Autowired
	private StoreCommonService commonService;

	@Autowired
	private StoreHomeService homeService;

	@Autowired
	private StoreSalesService salesService;

	@Autowired
	private StoreRewardService rewardService;

	@Autowired
	private StoreKeeperService keeperService;

	@Autowired
	private StoreNotificationService notificationService;

	/**
	 * 앱 정보 조회
	 * 
	 * @param param
	 *            디바이스 종류
	 * @return 버전
	 * @throws InvalidParamException
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/appinfo", method = RequestMethod.POST)
	public StoreResult getAppInfo(@RequestBody StoreParam reqParam, Authentication authentication)
			throws InvalidParamException, RequestResolveException {
		logger.debug("getAppInfo : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("os", reqParam.getHeader().get("os"));

		StoreResult result = new StoreResult();
		result.setData(commonService.getAppInfo(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 사용자 정보 조회
	 * 
	 * @param reqParam
	 * @return
	 */
	@RequestMapping(value = "/user/detail", method = RequestMethod.POST)
	public StoreResult getUserDetail(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getUserDetail : " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", (authentication != null ? authentication.getName() : null));

		SingleMap userDetail = commonService.getUserDetail(param);

		StoreResult result = new StoreResult();

		if (userDetail.isEmpty()) { // not found user			
			result.setSuccess(false);
			result.setError(ErrorCode.ERROR, "Forbidden");
			return result;
		}

		result.setData(userDetail);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 프랜차이즈의 브랜드 목록
	 * 
	 * @param param
	 *            franId : 프랜차이즈 ID
	 * @return
	 * 		brandNm : 브랜드 이름
	 *         brandId브랜드 ID
	 */
	@RequestMapping(value = "/fran/brands", method = RequestMethod.POST)
	public StoreResult getFranchiseBrands(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getFranchiseBrandList " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(commonService.getBrandListByFranId(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 홈 정보
	 * 
	 * @param param
	 *            brandId : 브랜드ID
	 *            today : 조회날짜 yyyy-mm-dd (요청 지역의 현재 날짜)
	 * @return
	 */
	@RequestMapping(value = "/home/brand", method = RequestMethod.POST)
	public StoreResult getBrandHome(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandHome " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(homeService.getBrandHome(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 스토어 홈 정보
	 * 
	 * @param param
	 *            storeId : 상점 아이디
	 *            today: today : 조회날짜 yyyy-mm-dd (요청 지역의 현재 날짜)
	 * @return
	 */
	@RequestMapping(value = "/home/store", method = RequestMethod.POST)
	public StoreResult getStoreHome(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreHome " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(homeService.getStoreHome(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 매장 목록 (검색)
	 * 
	 * @param param
	 *            brandId: 브랜드 ID
	 *            keyword: 검색어 (스토어명칭)
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * 
	 * @return 매장 이름, 매장 ID
	 */
	@RequestMapping(value = "/brand/stores", method = RequestMethod.POST)
	public StoreResult getBrandStoreList(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandStoreList " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(commonService.getStoreListByBrandId(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 서비스 목록
	 * 
	 * @param reqParam
	 *            storeId : 스토어 ID
	 * @return
	 */
	@RequestMapping(value = "/store/services", method = RequestMethod.POST)
	public StoreResult getStoreServices(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreServices " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(commonService.getStoreServiceListByStoreId(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 매출 요약
	 * 
	 * @param param
	 *            brandId: 브랜드ID
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/sales/summary", method = RequestMethod.POST)
	public StoreResult getBrandSalesSummary(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandStoreList " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getBrandSalesSummary(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 일별 매출
	 * 
	 * @param param
	 *            brandId: 브랜드ID
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/sales/daily", method = RequestMethod.POST)
	public StoreResult getBrandSalesDaily(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandStoreList " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getBrandSalesDaily(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 주별 매출
	 * 
	 * @param param
	 *            brandId: 브랜드ID
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/sales/weekly", method = RequestMethod.POST)
	public StoreResult getBrandSalesWeekly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandStoreList " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getBrandSalesWeekly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 월별 매출
	 * 
	 * @param param
	 *            brandId: 브랜드ID
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/sales/monthly", method = RequestMethod.POST)
	public StoreResult getBrandSalesMonthly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandStoreList " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getBrandSalesMonthly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 매출 요약
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 * @return
	 */
	@RequestMapping(value = "/store/sales/summary", method = RequestMethod.POST)
	public StoreResult getStoreSalesSummary(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesSummary " + reqParam);
		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesSummary(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 일별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/sales/daily", method = RequestMethod.POST)
	public StoreResult getStoreSalesDaily(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesDaily " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesDaily(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 주별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/sales/weekly", method = RequestMethod.POST)
	public StoreResult getStoreSalesWeekly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesWeekly " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesWeekly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 월별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/sales/monthly", method = RequestMethod.POST)
	public StoreResult getStoreSalesMonthly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesMonthly " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesMonthly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 항목별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/item/sales", method = RequestMethod.POST)
	public StoreResult getStoreItemSales(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreItemSales " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreItemSales(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 항목 분류별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/category/sales", method = RequestMethod.POST)
	public StoreResult getStoreItemCateSales(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreItemCateSales " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreItemCateSales(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 서비스별 매출
	 * 
	 * @param param
	 *            storeId : 스토어ID
	 *            startDate : 조회 시작일
	 *            endDate : 조회 종료일
	 * @return
	 * 
	 */
	@RequestMapping(value = "/store/service/sales", method = RequestMethod.POST)
	public StoreResult getStoreServiceSales(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreServiceSales " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreServiceSales(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 결제수단별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/payment/sales", method = RequestMethod.POST)
	public StoreResult getStorePaymentSales(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStorePaymentSales " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(salesService.getStorePaymentSales(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 시간대별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/sales/time", method = RequestMethod.POST)
	public StoreResult getStoreSalesTime(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesTime " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesTime(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 주별 매출
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/sales/week", method = RequestMethod.POST)
	public StoreResult getStoreSalesWeek(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesWeek " + reqParam);
		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesWeek(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 매출 상세 목록
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            currentDate: 조회일 yyyy-MM-dd
	 *            startTime: 조회 시작 시간 hh:mm
	 *            endDate: 조회 종료 시간 hh:mm
	 *            serviceType : 서비스 종류 코드 (전체: all, 서비스ID)
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/sales/details", method = RequestMethod.POST)
	public StoreResult getStoreSalesDetails(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesDetails " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesDetails(param));
		result.setSuccess(true);

		return result;
	}

	/**
	 * 매장 매출 상세 조회
	 * 
	 * @param param
	 *            salesId: 매출 아이디
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/sales/detail", method = RequestMethod.POST)
	public StoreResult getStoreSalesDetail(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesDetail " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(salesService.getStoreSalesDetail(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 리워드 요약
	 * 
	 * @param param
	 *            brandId: 브랜드 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/reward/summary", method = RequestMethod.POST)
	public StoreResult getBrandRewardSummary(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandRewardSummary " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getBrandRewardSummary(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 일별 리워드
	 * 
	 * @param param
	 *            brandId: 브랜드 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/reward/daily", method = RequestMethod.POST)
	public StoreResult getBrandRewardDaily(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandRewardDaily " + reqParam);
		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getBrandRewardDaily(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 주별 리워드
	 * 
	 * @param param
	 *            brandId: 브랜드 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/reward/weekly", method = RequestMethod.POST)
	public StoreResult getBrandRewardWeekly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandRewardWeekly " + reqParam);
		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getBrandRewardWeekly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 브랜드 월별 리워드
	 * 
	 * @param param
	 *            brandId: 브랜드 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/brand/reward/monthly", method = RequestMethod.POST)
	public StoreResult getBrandRewardMonthly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getBrandRewardMonthly " + reqParam);
		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getBrandRewardMonthly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 스탬프 요약
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 * @return
	 */
	@RequestMapping(value = "/store/stamp/summary", method = RequestMethod.POST)
	public StoreResult getStoreStampSummary(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreStampSummary " + reqParam);
		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreStampSummary(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 일별 스탬프
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/stamp/daily", method = RequestMethod.POST)
	public StoreResult getStoreStampDaily(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreStampDaily " + reqParam);
		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreStampDaily(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 주별 스탬프
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/stamp/weekly", method = RequestMethod.POST)
	public StoreResult getStoreStampWeekly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreStampWeekly " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreStampWeekly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 월별 스탬프
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/stamp/monthly", method = RequestMethod.POST)
	public StoreResult getStoreStampMonthly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreStampMonthly " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreStampMonthly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 쿠폰 요약
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/coupon/summary", method = RequestMethod.POST)
	public StoreResult getStoreCouponSummary(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreCouponSummary " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreCouponSummary(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 일별 쿠폰
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/coupon/daily", method = RequestMethod.POST)
	public StoreResult getStoreCouponDaily(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreCouponDaily " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreCouponDaily(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 주별 쿠폰
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/coupon/weekly", method = RequestMethod.POST)
	public StoreResult getStoreCouponWeekly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreCouponWeekly " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreCouponWeekly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 월별 쿠폰
	 * 
	 * @param param
	 *            storeId: 매장 아이디
	 *            startDate: 조회 시작일 yyyy-MM-dd
	 *            endDate: 조회 종료일 yyyy-MM-dd
	 *            start: 조회 시작 위치
	 *            count: 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/coupon/monthly", method = RequestMethod.POST)
	public StoreResult getStoreCouponMonthly(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreCouponMonthly " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(rewardService.getStoreCouponMonthly(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 CCTV 정보 조회
	 * 
	 * @param reqParam
	 *            storeId : 상점 아이디
	 * @return
	 */
	@RequestMapping(value = "/store/cctvs", method = RequestMethod.POST)
	public StoreResult getStoreCctvList(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreCctvList " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(keeperService.getStoreCctvList(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 매장 이벤트 로그 조회
	 * 
	 * @param reqParam
	 *            storeId: 상점 아이디
	 *            eventType : 이벤트 종류 (all, order-cancel, payment, payment-cancel, refund)
	 *            startDate : 조회 시작 날짜 시간, yyyy-MM-dd hh:mm
	 *            endDate : 조회 종료 날짜 시간, yyyy-MM-dd hh:mm
	 *            orderNo : 주문번호
	 *            start : 페이징 조회 시작
	 *            count : 페이징 조회 갯수
	 * @return
	 */
	@RequestMapping(value = "/store/events", method = RequestMethod.POST)
	public StoreResult getStoreEventList(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreEventList " + reqParam);

		SingleMap param = reqParam.getData();

		param.putIfEmpty("start", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);

		StoreResult result = new StoreResult();
		result.setData(keeperService.getStoreEventList(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 사용자 디바이스 등록
	 * 푸시토큰 저장
	 * 
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws NoPermissionException
	 */
	@RequestMapping(value = "/user/device", method = RequestMethod.POST)
	public StoreResult addUserDevice(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException {
		logger.debug("addUserDevice " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", authentication.getName());

		StoreResult result = new StoreResult();
		result.setData(commonService.addUserDevice(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 사용자 설정 조회
	 * 
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public StoreResult getSettings(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("getUserSettings " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", authentication.getName());

		StoreResult result = new StoreResult();
		result.setData(commonService.getUserSettings(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 사용자 설정 저장
	 * 
	 * @param reqParam
	 * @param authentication
	 * @return
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/setting/save", method = RequestMethod.POST)
	public StoreResult setSettings(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("setUserSettings " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", authentication.getName());

		StoreResult result = new StoreResult();
		result.setData(commonService.setUserSettings(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 사용자 패스워드 변경
	 * 
	 * @param reqParam
	 *            password 현재 패스워드
	 *            newPassword 새로운 패스워드
	 * 
	 * @param authentication
	 * @return
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/user/password/change", method = RequestMethod.POST)
	public StoreResult setUserPassword(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("setUserPassword " + reqParam);

		SingleMap param = reqParam.getData();
		param.put("userName", authentication.getName());

		StoreResult result = new StoreResult();
		result.setData(commonService.setUserPassword(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 알림 목록
	 * 
	 * @param reqParam
	 *            brandId 브랜드 ID
	 *            storeId 스토어 ID
	 *            deviceId 디바이스 id
	 *            startId 조회 시작 아이디
	 *            count 조회 갯수
	 * 
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/notify/list", method = RequestMethod.POST)
	public StoreResult getNotifyList(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("getEventList " + reqParam);

		SingleMap param = reqParam.getData();

		// 중간에 항목을 삭제하는 경우가 있어 페이징 처리 기준을 id로 함.
		param.putIfEmpty("startId", 0);
		param.putIfEmpty("count", COUNT_DEFAULT);
		
		StoreResult result = new StoreResult();
		result.setData(notificationService.getNotifyList(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 알림 읽음 표시
	 * 
	 * @param reqParam
	 *            brandId 브랜드 ID
	 *            storeId 스토어 ID
	 *            deviceId 디바이스 id
	 * 
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/notify/read", method = RequestMethod.POST)
	public StoreResult setNotifyReadMark(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("setNotifyReadMark " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(notificationService.setNotifyReadMark(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 알림 삭제
	 * 
	 * @param reqParam
	 *            notifyId 알림 ID
	 *            deviceId 디바이스 id
	 * 
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/notify/del", method = RequestMethod.POST)
	public StoreResult setNotifyDelete(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("setNotifyReadMark " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(notificationService.setNotifyDeleteMark(param));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 알림 읽지 않은 갯수 조회
	 * 
	 * @param reqParam
	 *            brandId 브랜드 ID
	 *            storeId 스토어 ID
	 *            deviceId 디바이스 id
	 * 
	 * @param authentication
	 * @return
	 * @throws RequestResolveException
	 */
	@RequestMapping(value = "/notify/unread/count", method = RequestMethod.POST)
	public StoreResult getNotifyUnreadCount(@RequestBody StoreParam reqParam, Authentication authentication)
			throws NoPermissionException, RequestResolveException {
		logger.debug("getNotifyUnreadCount " + reqParam);

		SingleMap param = reqParam.getData();

		StoreResult result = new StoreResult();
		result.setData(notificationService.getNotifyUnreadCount(param));
		result.setSuccess(true);
		return result;
	}
	
  @RequestMapping(value="/log/upload", method=RequestMethod.POST)
  public ClerkResult uploadLogFile(MultipartHttpServletRequest req, @RequestParam("files") MultipartFile[] files) throws IOException, InterruptedException {
	  
	  	ClerkResult result = new ClerkResult();
		result.setData(commonService.uploadLogFile(req, files));
		result.setSuccess();
	  return result;
  }
  
	@RequestMapping(value = "/printer/update", method = RequestMethod.POST)
	public ClerkResult printerUptate(@RequestBody StoreParam reqParam, Authentication authentication)
			 throws RequestResolveException {

		logger.debug("storeInfo : " + reqParam);
		
		boolean updateResult = commonService.updateStorePrinterIp(reqParam.getData());

		ClerkResult result = new ClerkResult();
		result.setData(updateResult);
		result.setSuccess();

		return result;
	}
	
	@RequestMapping(value = "/sales/list", method = RequestMethod.POST)
	public ClerkResult storeSalesList(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesSummary " + reqParam);
		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(salesService.getStoreSalesList(param));
		result.setSuccess();
		return result;
	}
	
	
	@RequestMapping(value = "/sales/detail", method = RequestMethod.POST)
	public ClerkResult storeSalesDtl(@RequestBody StoreParam reqParam, Authentication authentication) {
		logger.debug("getStoreSalesSummary " + reqParam);
		SingleMap param = reqParam.getData();

		ClerkResult result = new ClerkResult();
		result.setData(salesService.getStoreSalesDtl(param));
		result.setSuccess();
		return result;
	}
}
