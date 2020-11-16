package com.jc.pico.service.pos.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcCoupon;
import com.jc.pico.bean.SvcCouponLog;
import com.jc.pico.bean.SvcCouponWithBLOBs;
import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesDiscount;
import com.jc.pico.bean.SvcSalesExample;
import com.jc.pico.bean.SvcStampLog;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcUserCoupon;
import com.jc.pico.bean.SvcUserCouponExample;
import com.jc.pico.bean.SvcUserStamp;
import com.jc.pico.bean.SvcUserStampExample;
import com.jc.pico.bean.User;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.exception.SettingNotFoundException;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcCouponLogMapper;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcSalesMapper;
import com.jc.pico.mapper.SvcStampLogMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcUserCouponMapper;
import com.jc.pico.mapper.SvcUserStampMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.module.CouponProcessor;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.service.pos.CouponValidationService;
import com.jc.pico.service.pos.PosRewardService;
import com.jc.pico.utils.BarcodeUtil;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosCouponInfo;
import com.jc.pico.utils.bean.PosResultStoreInfo;
import com.jc.pico.utils.bean.PosStampSave;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcSalesExtended;
import com.jc.pico.utils.customMapper.pos.PosRewardMapper;

/**
 * 
 * 주문/매출로 부터 발생한 건에 대한
 * 쿠폰 발급/취소/사용 처리
 * 스탬프 발급/취소/사용 처리
 * 
 * 
 */
@Service
public class PosRewardServiceImpl implements PosRewardService {

	protected static final Logger logger = LoggerFactory.getLogger(PosRewardServiceImpl.class);

	/**
	 * 스탬프 적립 : 매출 적립
	 */
	private static final String STAMP_TP_SALES_ADD = "405001";

	/**
	 * 스탬프 상태 : 적립
	 */
	private static final String STAMP_ST_SAVING = "401001";

	/**
	 * 스탬프 상태 : 취소
	 */
	private static final String STAMP_ST_CANCEL = "401002";

	/**
	 * 스탬프 상태 : 사용
	 */
	private static final String STAMP_ST_USED = "401003";

	/**
	 * 스탬프 상태 : 만료
	 */
	private static final String STAMP_ST_EXPIRED = "401004";

	/**
	 * 쿠폰 상태 : 발급
	 */
	private static final String COUPON_ST_ISSUE = "402001";

	/**
	 * 쿠폰 상태: 사용 취소
	 */
	private static final String COUPON_ST_CANCEL = "402002";

	/**
	 * 쿠폰 상태 : 사용함
	 */
	private static final String COUPON_ST_USED = "402003";

	/**
	 * 쿠폰 상태 : 발급 취소
	 */
	private static final String COUPON_ST_ISSUE_CANCEL = "402004";

	/**
	 * 쿠폰 상태 : 만료
	 */
	private static final String COUPON_ST_EXPIRE = "402005";

	/**
	 * 매출 상태 : 정상
	 */
	public static final String SALES_ST_NORMAL = "809001";

	/**
	 * 매출 상태 : 반품
	 */
	public static final String SALES_ST_RETURN = "809002";

	/**
	 * 발급된 스탬프 상태 코드 목록
	 */
	private static final List<String> STAMP_ST_ISSUED_LIST = new ArrayList<>();

	static {
		STAMP_ST_ISSUED_LIST.add(STAMP_ST_SAVING);
		STAMP_ST_ISSUED_LIST.add(STAMP_ST_USED);
		STAMP_ST_ISSUED_LIST.add(STAMP_ST_EXPIRED);
	}

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private SvcUserStampMapper svcUserStampMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PosRewardMapper posRewardMapper;

	@Autowired
	private SvcUserCouponMapper svcUserCouponMapper;

	@Autowired
	private SvcCouponMapper svcCouponMapper;

	@Autowired
	private SvcSalesMapper svcSalesMapper;

	@Autowired
	private PosUtil posUtil;

	@Autowired
	BarcodeUtil barcodeUtil;

	@Autowired
	private SvcStampLogMapper svcStampLogMapper;

	@Autowired
	private SvcCouponLogMapper svcCouponLogMapper;

	@Autowired
	private AppNotificationService appNotificationService;

	@Autowired
	private CouponValidationService couponValidationService;

	@Override
	public SingleMap saveStamp(PosStampSave param, Map<String, Object> posInfo) throws SettingNotFoundException, RequestResolveException {

		Integer ynSave = param.getYnSave();

		if (ynSave == null) {
			throw new InvalidParameterException("YN_SAVE value is invalid. 1 or 2 but empty.");
		}

		int stampCount;

		if (ynSave == 1) { // 적립
			stampCount = addStamp(param, posInfo);

		} else if (ynSave == 2) { // 취소			
			stampCount = cancelStamp(param, posInfo);

		} else {
			throw new InvalidParameterException("YN_SAVE is invalid. 1 or 2 but " + ynSave);
		}

		User user = userMapper.selectByPrimaryKey(param.getCdMember());

		SingleMap result = new SingleMap();
		result.put("NM_MEMBER", user == null ? "" : user.getName());
		result.put("CNT_STAMP", stampCount);

		return result;

	}

	/**
	 * 스탬프 발급
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 * @throws SettingNotFoundException
	 * @throws RequestResolveException
	 */
	private int addStamp(PosStampSave param, Map<String, Object> posInfo) throws SettingNotFoundException, RequestResolveException {

		SvcSales sales = getSales(param);
		if (sales == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found sales.");
		}

		SvcUserStamp record = toSvcUserStamp(param, new SingleMap(posInfo), sales);

		final int stampMaxCount = getStampMaxCount(sales.getId());
		final int stampCount = getStampCount(sales.getId());
		final int newStampCount = stampMaxCount - stampCount;

		// 발급 가능한 스탬프가 있는지 확인
		if (newStampCount < 1) {
			throw new RequestResolveException(PosUtil.EPO_0009_CODE_ILLEGAL_STATE,
					"All stamps have been issued. (" + stampCount + "/" + stampMaxCount + ")");
		}

		User user = userMapper.selectByPrimaryKey(record.getUserId());
		SvcStore store = svcStoreMapper.selectByPrimaryKey(record.getStoreId());
		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(record.getBrandId());

		// 만료일 설정
		CouponProcessor couponProcessor = appContext.getBean(CouponProcessor.class);
		couponProcessor.init(user, brand, store);
		record.setExpire(couponProcessor.getStampExpire(new Date()));

		// 스탬프 사용 설정 확인
		if (!couponProcessor.isUseStamp()) {
			return 0;
		}

		// 저장
		for (int i = newStampCount; i > 0; i--) {
			svcUserStampMapper.insertSelective(record);
			addStampLog(record);
		}

		// 쿠폰 발급
		List<Long> couponIds = addCouponByStamp(couponProcessor);

		// 쿠폰 적립, 쿠폰 발급 알림
		notifyRewardAdded(store.getId(), user.getId(), newStampCount, couponIds);

		return newStampCount;
	}

	private void notifyRewardAdded(Long storeId, Long userId, int stampCount, List<Long> couponIds) {
		// 스탬프 적립 알림
		try {
			appNotificationService.notifyStampSaved(storeId, userId, stampCount);
		} catch (Exception e) {
			// 알림에 실패해도 계속 진행
			logger.error("Failed notify stamp saved.", e);
		}

		// 쿠폰 발급 알림
		try {
			appNotificationService.notifyCouponIssued(storeId, userId, couponIds);
		} catch (Exception e) {
			// 알림에 실패해도 계속 진행
			logger.error("Failed notify coupon issued.", e);
		}
	}

	private void notifyRewardCanceled(Long storeId, Long userId, int stampCount, List<Long> couponIds) {
		// 스탬프 적립 알림
		try {
			appNotificationService.notifyStampCanceled(storeId, userId, stampCount);
		} catch (Exception e) {
			// 알림에 실패해도 계속 진행
			logger.error("Failed notify stamp canceled.", e);
		}

		// 쿠폰 발급 알림
		try {
			appNotificationService.notifyCouponIssueCanceled(storeId, userId, couponIds);
		} catch (Exception e) {
			// 알림에 실패해도 계속 진행
			logger.error("Failed notify coupon issue canceled.", e);
		}
	}

	/**
	 * 매출로 부터 발급된 스탬프 갯수 조회
	 * 
	 * @param salesId
	 * @return
	 */
	private int getStampCount(long salesId) {

		SvcUserStampExample example = new SvcUserStampExample();
		example.createCriteria() // 조건
				.andSalesIdEqualTo(salesId) // 해당 매출
				.andStampStIn(STAMP_ST_ISSUED_LIST); // 발급, 사용, 만료
		return svcUserStampMapper.countByExample(example);
	}

	/**
	 * 발급 가능한 스탬프 갯수 조회
	 * 
	 * @param salesId
	 * @return
	 */
	private int getStampMaxCount(long salesId) {
		return posRewardMapper.selectStampIssuableCount(salesId);
	}

	/**
	 * 발급된 스탬프중 1개를 적립 취소 처리 한다.
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 * @throws RequestResolveException
	 */
	private int cancelStamp(PosStampSave param, Map<String, Object> posInfo) throws RequestResolveException {

		SvcSales sales = getSales(param);
		if (sales == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found sales.");
		}

		// 적립된 스탭프가 있는지 조회
		SvcUserStampExample example = new SvcUserStampExample();
		example.setOrderByClause("STAMP_ST ASC"); // 취소 우선 순위: 발행(401001) > 사용(402002)
		example.createCriteria() // 검색 조건
				.andSalesIdEqualTo(sales.getId()) // 매출 ID
				.andUserIdEqualTo(param.getCdMember()) // 동일 소유자 확인
				.andStampStIn(Arrays.asList(STAMP_ST_SAVING, STAMP_ST_USED)); // 적립된 상태 (취소, 만료된 것들은 취소 불가)

		List<SvcUserStamp> stamps = svcUserStampMapper.selectByExample(example);

		// 취소가능한 적립된 스탭프가 없으면 에러
		if (stamps.isEmpty()) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "No stamp has been issued.");
		}

		Set<Long> couponSet = new HashSet<>();

		for (SvcUserStamp targetStamp : stamps) {

			final String stampSt = targetStamp.getStampSt();

			// 취소 처리, 스탬프 상태만 갱신 취소 처리
			targetStamp.setStampSt(STAMP_ST_CANCEL); // 취소

			svcUserStampMapper.updateByPrimaryKeySelective(targetStamp);

			addStampLog(targetStamp);

			// 쿠폰이 있으면 쿠폰도 취소 처리		
			if (STAMP_ST_USED.equals(stampSt) && targetStamp.getCouponId() != null && targetStamp.getCouponId() != 0) {
				couponSet.add(targetStamp.getCouponId());

			}
		}

		// 쿠폰 취소 및 관련 스캠프 발행 상태로 변경
		for (Long couponId : couponSet) {

			boolean canceled = cancelCouponIfPossible(couponId, sales.getOrderId(), true);

			// 쿠폰이 취소되었으면 연관된 스탬프의 쿠폰 정보 제거, 발행 상태로 원복
			if (canceled) {
				resetStampCouponId(couponId);
			}
		}

		SvcUserStamp stamp = stamps.get(0);
		notifyRewardCanceled(stamp.getStoreId(), stamp.getUserId(), stamps.size(), new ArrayList<>(couponSet));

		return stamps.size();
	}

	/**
	 * target coupon id 가 설정된 스탬프의 coupon id를 새로운 coupon id로 변경
	 * 
	 * @param targetCouponId
	 * @param newCouponId
	 */
	private void resetStampCouponId(long targetCouponId) {
		SvcUserStampExample example = new SvcUserStampExample();
		example.createCriteria() // 조건
				.andStampStEqualTo(STAMP_ST_USED) // 사용한 쿠폰 중에서 처리
				.andCouponIdEqualTo(targetCouponId);

		List<SvcUserStamp> stamps = svcUserStampMapper.selectByExample(example);

		// 쿠폰 번호 리셋하고 적립 상태로 변경
		SvcUserStamp record = new SvcUserStamp();
		record.setCouponId(0l);
		record.setStampSt(STAMP_ST_SAVING);
		svcUserStampMapper.updateByExampleSelective(record, example);

		// 로그 기록
		for (SvcUserStamp stamp : stamps) {
			stamp.setStampSt(STAMP_ST_SAVING);
			addStampLog(stamp);
		}
	}

	/**
	 * 쿠폰을 취소 처리한다.
	 * 
	 * @param couponId
	 * @param isStrict
	 *            true로 설정하면 쿠폰이 사용되었거나 만료된 경우 예외 발생. false로 설정하면 그냥 통과 한다. (false 반환)
	 * @return
	 * @throws RequestResolveException
	 */
	private boolean cancelCouponIfPossible(long couponId, Long orderId, boolean isStrict) throws RequestResolveException {
		SvcUserCoupon coupon = svcUserCouponMapper.selectByPrimaryKey(couponId);

		// 삭제되었으면 무시
		if (coupon == null) {
			return false;
		}

		final String couponSt = coupon.getCouponSt();

		// 쿠폰이 발행된 상태면 취소 처리
		if (COUPON_ST_ISSUE.equals(couponSt)) {

			coupon.setCouponSt(COUPON_ST_ISSUE_CANCEL);
			coupon.setUpdated(null); // db에서 자동 갱신 처리
			coupon.setCreated(null); // 변경하지 않음
			svcUserCouponMapper.updateByPrimaryKeySelective(coupon);

			addCouponLog(coupon, orderId);

			return true;
		}

		// 스탬프를 쌓아서 발급한 쿠폰을 사용했거나, 만료됐으면 취소 불가
		if (COUPON_ST_USED.equals(couponSt) || COUPON_ST_EXPIRE.equals(couponSt)) {
			if (isStrict) {
				throw new RequestResolveException(PosUtil.EPO_0009_CODE_ILLEGAL_STATE, "Coupon issued to earn stamps used or expired.");
			}
			// 에러 없이 리턴 처리
			return false;
		}

		// 쿠폰이 취소 상태면 무시
		if (COUPON_ST_CANCEL.equals(couponSt) || COUPON_ST_ISSUE_CANCEL.equals(couponSt)) {
			// 정상적으로 동작하고 있으면 해당 경우는 발생해서는 안됨
			// 쿠폰을 취소 처리하면 연결된 stamp에서는 값이 비워져야 하는 함.  
			return false;
		}

		return false;
	}

	/**
	 * 매출 조회
	 * 
	 * @param param
	 * @return
	 */
	private SvcSales getSales(PosStampSave param) {

		Date ymdSale = posUtil.parseDate(param.getYmdSale(), null);
		if (ymdSale == null) {
			throw new InvalidParameterException("Invalid YMD_SALE. " + param.getYmdSale());
		}

		Date openDtStart = posUtil.getDateTime(ymdSale, 0, 0, 0, 0);
		Date openDtEnd = posUtil.getDateTime(ymdSale, 23, 59, 59, 999);

		SvcSalesExample example = new SvcSalesExample();
		example.createCriteria() // 검색 조건
				.andStoreIdEqualTo(posUtil.parseLong(param.getCdStore(), -1l)).andOpenDtBetween(openDtStart, openDtEnd) // 시간 정보가 잘못 들어가 있는 경우도 있어 범위 검색 본래 시간은 0 이어야 함.
				.andPosNoEqualTo(param.getNoPos()) // 포스 번호
				.andReceiptNoEqualTo(posUtil.toString(param.getNoRcp(), "-1")); // 영수증 번호

		List<SvcSales> salesList = svcSalesMapper.selectByExample(example);

		return salesList.isEmpty() ? null : salesList.get(0);
	}

	/**
	 * PosStampSave 를 SvcUserStamp 으로 변환.
	 * 포스 bean -> apc bean
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 */
	private SvcUserStamp toSvcUserStamp(PosStampSave param, SingleMap posInfo, SvcSales sales) {
		// # ID, USER_ID, BRAND_ID, STORE_ID, ORDER_ID, SALES_ID, COUPON_ID, STAMP_TP, STAMP_ST, EXPIRE, CREATED, UPDATED
		SvcUserStamp result = new SvcUserStamp();
		result.setUserId(param.getCdMember());
		result.setBrandId(posInfo.getLong(PosUtil.BASE_INFO_BRAND_ID, null));
		result.setStoreId(posInfo.getLong(PosUtil.BASE_INFO_STORE_ID, null));
		result.setStampSt(STAMP_ST_SAVING);
		result.setStampTp(STAMP_TP_SALES_ADD);
		result.setSalesId(sales.getId());
		result.setOrderId(sales.getOrderId());
		// result.setExpire(); // CouponProcessor 내에서 브랜드/상점 설정에 따라 부여
		return result;
	}

	@Override
	public List<PosCouponInfo> getCouponInfoWithValidation(SingleMap param, Map<String, Object> posInfo) throws RequestResolveException {

		SingleMap posInfoWrap = new SingleMap(posInfo);
		String couponCode = param.getString("NO_COUPON");
		long itemId = param.getLong("CD_GOODS", 0l);
		long posStoreId = posInfoWrap.getLong(PosUtil.BASE_INFO_STORE_ID);
		SvcUserCoupon userCoupon = getUserCouponByCode(couponCode);

		if (userCoupon == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, couponCode + " no coupon is not found.");
		}

		SvcCouponWithBLOBs coupon = svcCouponMapper.selectByPrimaryKey(userCoupon.getCouponId());

		if (coupon == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, couponCode + " no coupon is not found.");
		}

		List<PosCouponInfo> result = new ArrayList<>();
		result.add(buildPosCouponInfo(userCoupon, coupon, posStoreId, itemId));
		return result;
	}

	private PosCouponInfo buildPosCouponInfo(SvcUserCoupon userCoupon, SvcCouponWithBLOBs coupon, long posStoreId, long itemId)
			throws RequestResolveException {

		// 특정 지점 사용 쿠폰이면 해당 값 파싱하여 전달

		Date expireDate = userCoupon.getExpire();
		SvcBrand couponBrand = svcBrandMapper.selectByPrimaryKey(coupon.getBrandId());
		SvcStore useStore = svcStoreMapper.selectByPrimaryKey(posStoreId);

		PosCouponInfo result = new PosCouponInfo();
		result.setCdCoupon(userCoupon.getId());
		result.setNoCoupon(userCoupon.getCouponCd());
		result.setCdValid(couponValidationService.validCoupon(userCoupon, coupon, useStore, itemId));
		result.setDtIssue(posUtil.foramtDateTime(userCoupon.getCreated()));
		result.setCdCouponStatus(posUtil.parseInt(userCoupon.getCouponTp(), 0)); // 쿠폰 상태 (402001:발행, 402002:재발행(사용취소), 402003:사용, 402004:취소(발행취소))
		result.setCdMember(userCoupon.getUserId());
		result.setCdCompany(couponBrand.getFranId());
		result.setCdStore(userCoupon.getStoreId());
		result.setNmCoupon(coupon.getCouponNm());
		result.setPrDiscount(coupon.getDiscount());
		result.setCdDiscountType(posUtil.parseInt(coupon.getDiscountTp(), -1)); // 할인타입(403001: 비율 할인, 403002: 금액 할인, 403003: 1+1, 403004: 공짜 메뉴 제공)
		result.setPrDiscountLimit(coupon.getDiscountLimit());
		result.setCdExpireType(posUtil.parseInt(coupon.getExpireTp(), -1)); // 만료타입 (404001:발행일로부터, 404002:기간지정)
		result.setYmdBegin(posUtil.formatDate(coupon.getBegin()));
		result.setYmdExpire(posUtil.formatDate(expireDate));
		result.setCdTargetMenuId(coupon.getTargetMenuId());
		result.setCdSupplyMenuId(coupon.getSupplyMenuId());
		result.setYnUseLimit(coupon.getHasUseLimit() ? 1 : 0);
		result.setCdEnableDays(coupon.getEnableDays());
		result.setTimeOpen(coupon.getOpenTm());
		result.setTimeClose(coupon.getCloseTm());
		result.setNmImg(coupon.getImg());
		result.setNmSmallImg(coupon.getSmallImg());
		result.setYnAll(coupon.getIsAll() ? 1 : 0);
		result.setStores(getCouponStoreInfoList(coupon));
		result.setNmNotice(coupon.getNotice());

		return result;
	}

	/**
	 * 쿠폰에 지정된 사용가능한 매장의 상세 정보 조회
	 * 
	 * @param coupon
	 * @return
	 * @throws RequestResolveException
	 */
	private List<PosResultStoreInfo> getCouponStoreInfoList(SvcCouponWithBLOBs coupon) throws RequestResolveException {
		// 전체 사용 이거나 사용 가능 지점 정보가 없으면 빈 리스트 반환
		if (coupon.getIsAll() || StringUtils.isEmpty(coupon.getStoreIds())) {
			return Collections.<PosResultStoreInfo>emptyList();
		}

		List<Long> storeIds;
		try {
			// storeIds는 json array string 형식
			storeIds = couponValidationService.parseCouponStoreIds(coupon.getStoreIds());
		} catch (Exception e) {
			// 서버 사이드 오류. 쿠폰을 사용 할 수 없는 상태가 되도록 처리
			logger.error("Failed parse coupon store ids. couponId={}, storeIdsJson={}", coupon.getId(), coupon.getStoreIds(), e);
			return Collections.<PosResultStoreInfo>emptyList();
		}

		return storeIds.isEmpty() ? Collections.<PosResultStoreInfo>emptyList() : posRewardMapper.selectCouponStoreInfoList(storeIds);
	}

	/**
	 * coupon cd로 쿠폰 정보 조회
	 * 
	 * @param couponCd
	 * @return
	 */
	private SvcUserCoupon getUserCouponByCode(String couponCd) {
		SvcUserCouponExample example = new SvcUserCouponExample();
		example.createCriteria() // 조건
				.andCouponCdEqualTo(couponCd); // 쿠폰 코드
		List<SvcUserCoupon> coupons = svcUserCouponMapper.selectByExample(example);
		return coupons.size() > 0 ? coupons.get(0) : null;
	}

	/**
	 * SvcSalesExtended -> SvcUserStamp 생성
	 * 
	 * @param sales
	 * @param processor
	 * @return
	 * @throws SettingNotFoundException
	 */
	private SvcUserStamp toSvcUserStamp(SvcSalesExtended sales, CouponProcessor processor) throws SettingNotFoundException {
		SvcUserStamp record = new SvcUserStamp();
		record.setUserId(sales.getUserId());
		record.setBrandId(sales.getBrandId());
		record.setStoreId(sales.getStoreId());
		record.setStampSt(STAMP_ST_SAVING);
		record.setStampTp(STAMP_TP_SALES_ADD);
		record.setSalesId(sales.getId());
		record.setOrderId(sales.getOrderId());
		record.setExpire(processor.getStampExpire(new Date()));
		return record;
	}

	/**
	 * 현재 사용자에게 발급 가능한 쿠폰이 있는지 확인하여 발급
	 * 
	 * @param processor
	 * @param orderId
	 * @throws SettingNotFoundException
	 */
	private List<Long> addCouponByStamp(CouponProcessor processor) throws SettingNotFoundException {

		User user = processor.getUser();
		SvcBrand brand = processor.getBrand();
		SvcStore store = processor.getStore();

		SvcCouponLog log = new SvcCouponLog();
		log.setStoreId(store.getId());
		log.setBrandId(brand.getId());
		log.setUserId(user.getId());
		log.setWhen(new Date());
		log.setClientId("pos");
		log.setSrcIp("");
		log.setDeviceId("");
		log.setGrantType("");
		log.setUserAgent("system");

		return processor.issue(log);
	}

	/**
	 * 스탬프 발급/취소 로그 기록
	 * 
	 * @param userStamp
	 */
	private void addStampLog(SvcUserStamp userStamp) {
		SvcStampLog log = new SvcStampLog();
		log.setBrandId(userStamp.getBrandId());
		log.setClientId("pos");
		log.setDeviceId("");
		log.setExpire(userStamp.getExpire());
		log.setGrantType("");
		log.setLogTp(userStamp.getStampSt());
		log.setOrderId(userStamp.getOrderId());
		log.setSrcIp("");
		log.setStampId(userStamp.getId());
		log.setStoreId(userStamp.getStoreId());
		log.setUserAgent("");
		log.setUserId(userStamp.getUserId());
		log.setWhen(new Date());
		svcStampLogMapper.insertSelective(log);
	}

	/**
	 * 쿠폰 발급 로그
	 * 
	 * @param coupon
	 * @param couponTp
	 * @param orderId
	 */
	private void addCouponLog(SvcUserCoupon coupon, Long orderId) {

		SvcCoupon couponInfo = svcCouponMapper.selectByPrimaryKey(coupon.getCouponId());

		SvcCouponLog record = new SvcCouponLog();
		record.setBrandId(coupon.getBrandId());
		record.setClientId("");
		record.setCouponCd(coupon.getCouponCd());
		record.setCouponId(coupon.getId());
		record.setCouponNm(couponInfo.getCouponNm());
		record.setDeviceId("");
		record.setGrantType("");
		record.setLogTp(coupon.getCouponSt());
		record.setOrderId(orderId);
		record.setPromotionId(0l);
		record.setSrcIp("");
		record.setStoreId(coupon.getStoreId());
		record.setUserAgent("system");
		record.setUserId(coupon.getUserId());
		record.setWhen(new Date());

		svcCouponLogMapper.insertSelective(record);
	}

	/**
	 * 쿠폰 및 스탭프 발급 / 취소 / 사용 처리
	 * 
	 * @throws RequestResolveException
	 */
	@Override
	public void resolveRewardConsume(SvcSalesExtended sales) throws SettingNotFoundException, RequestResolveException {

		// 쿠폰 사용 / 취소 처리
		logger.debug("resolveCouponUseBySales");
		resolveCouponUseBySales(sales);

		// 스탬프 발급 / 취소 처리 
		// 스탬프 변경에 따른 쿠폰 발급/취소 처리
		logger.debug("resolveStampIssueBySales");
		resolveStampIssueBySales(sales);
	}

	/**
	 * 매출 상태에 따라 쿠폰 사용 및 취소 처리
	 * 
	 * @param sales
	 */
	private void resolveCouponUseBySales(SvcSalesExtended sales) {
		// 쿠폰 사용처리
		if (SALES_ST_NORMAL.equals(sales.getSalesSt())) {
			for (SvcSalesDiscount item : sales.getSvcSalesDiscounts()) {
				if (!StringUtils.isEmpty(item.getCouponCd())) {
					useCouponBySales(item.getCouponCd(), sales);
				}
			}
			return;
		}

		// 쿠폰 사용 취소 처리
		if (SALES_ST_RETURN.equals(sales.getSalesSt())) {
			for (SvcSalesDiscount item : sales.getSvcSalesDiscounts()) {
				if (!StringUtils.isEmpty(item.getCouponCd())) {
					cancelUsedCouponAndReIssuseCouponBySales(item.getCouponCd(), sales);
				}
			}
		}
	}

	/**
	 * 매출로 부터 쿠폰 사용 처리
	 * 
	 * @param couponCd
	 * @param sales
	 */
	private void useCouponBySales(String couponCd, SvcSales sales) {
		SvcUserCoupon coupon = getUserCouponByCode(couponCd);
		if (coupon == null) {
			return;
		}

		coupon.setUsed(true);
		coupon.setCouponSt(COUPON_ST_USED);
		coupon.setUseTm(sales.getSalesTm());
		coupon.setUseTmLocal(sales.getSalesTmLocal());
		coupon.setUseStoreId(sales.getStoreId());
		coupon.setUpdated(null); // db 에서 갱신 처리하도록
		svcUserCouponMapper.updateByPrimaryKeySelective(coupon);

		addCouponLog(coupon, sales.getOrderId());

		try {
			appNotificationService.notifyCouponUsed(sales.getStoreId(), sales.getUserId(), coupon.getId());
		} catch (Exception e) {
			// 알림에 실패새도 쿠폰 사용 처리는 계속
			logger.error("Failed notify coupon used. userCouponId=" + coupon.getId() + " Continue user coupon by sales.", e);
		}
	}

	/**
	 * 매출로 부터 쿠폰 사용 취소 처리 후 재발급
	 *
	 * @param couponCd
	 * @param sales
	 */
	private void cancelUsedCouponAndReIssuseCouponBySales(String couponCd, SvcSales sales) {
		SvcUserCoupon coupon = getUserCouponByCode(couponCd);
		if (coupon == null) {
			return;
		}
		final long oldCouopnId = coupon.getId();

		// 기존 쿠폰 취소 처리		
		coupon.setId(coupon.getId());
		coupon.setCouponSt(COUPON_ST_CANCEL);
		coupon.setUpdated(null); // db에서 갱신 처리
		svcUserCouponMapper.updateByPrimaryKeySelective(coupon);

		// 취소 로그
		addCouponLog(coupon, sales.getOrderId());

		// 기존 쿠폰과 동일한 조건으로 새 쿠폰 발급
		SvcUserCoupon newCoupon = new SvcUserCoupon();
		final SvcBrand brand = svcBrandMapper.selectByPrimaryKey(coupon.getBrandId());
		newCoupon.setCouponId(coupon.getCouponId());
		newCoupon.setCouponCd(barcodeUtil.makeCouponCode(brand.getBrandCd()));
		newCoupon.setCouponSt(COUPON_ST_ISSUE);
		newCoupon.setCouponTp(coupon.getCouponTp());
		newCoupon.setExpire(coupon.getExpire());
		newCoupon.setStoreId(coupon.getStoreId());
		newCoupon.setUsed(false);
		newCoupon.setUserId(coupon.getUserId());
		svcUserCouponMapper.insertSelective(newCoupon);

		// 발급 로그
		addCouponLog(newCoupon, sales.getOrderId());

		// 스탬프를 통해 발급된 쿠폰이면 해당 스탬프와 연결된 쿠폰을 새로운 쿠폰으로 변경
		SvcUserStampExample stampExample = new SvcUserStampExample();
		stampExample.createCriteria() // 조건
				.andStampStEqualTo(STAMP_ST_USED) // 쿠폰 발급을 위해 사용된 스탬프 
				.andCouponIdEqualTo(oldCouopnId); // 해당 쿠폰과 연결됨

		// 쿠폰 번호 변경
		// 쿠폰 번호만 변경하니 스탬프 로그는 기록할 필요 없음
		SvcUserStamp stampRecord = new SvcUserStamp();
		stampRecord.setCouponId(newCoupon.getId()); // 새로운 쿠폰 ID
		svcUserStampMapper.updateByExampleSelective(stampRecord, stampExample);

		// 쿠폰 발행 알림
		List<Long> couponIds = new ArrayList<>();
		couponIds.add(newCoupon.getId());
		try {
			appNotificationService.notifyCouponIssued(newCoupon.getStoreId(), newCoupon.getUserId(), couponIds);
		} catch (Exception e) {
			logger.warn("Failed notify coupon issued by canceled sales (Reissue).  userId={}, couponId={}", newCoupon.getUserId(), newCoupon.getId());
		}
	}

	/**
	 * 매출 적립/취소 상태에 따라 스탬프를 발급 취소 처리 한다.
	 * 
	 * @param sales
	 * @throws SettingNotFoundException
	 * @throws RequestResolveException
	 */
	private void resolveStampIssueBySales(SvcSalesExtended sales) throws SettingNotFoundException, RequestResolveException {

		// 스탬프 발급 처리
		if (SALES_ST_NORMAL.equals(sales.getSalesSt())) {
			addStampBySales(sales);
			return;
		}

		// 스탬프 취소 처리
		if (SALES_ST_RETURN.equals(sales.getSalesSt())) {
			cancelStampBySales(sales);
		}
	}

	/**
	 * 매출로 부터 스탬프 발급 처리.
	 * 스탬프 발급시 쿠폰 발급 조건에 부합되면 쿠폰 발급
	 * 
	 * @param sales
	 * @throws SettingNotFoundException
	 */
	private void addStampBySales(SvcSalesExtended sales) throws SettingNotFoundException {

		// 적립할 대상(사용자)가 없으면 처리하지 않음.
		if (sales.getUserId() == null || sales.getUserId() == 0) {
			return;
		}

		// 발급 가능한 스탬프가 있는지 확인
		final int stampMaxCount = getStampMaxCount(sales.getId()); // 발급 가능한 최대 갯수
		final int stampCurrentCount = getStampCount(sales.getId()); // 현재 발급된 갯수		
		final int stampIssueCount = stampMaxCount - stampCurrentCount; // 발급해야할 스탬프 갯수

		// 모두 발급 되거었거나, 발급할 것이 없음
		if (stampIssueCount <= 0) {
			return;
		}

		User user = userMapper.selectByPrimaryKey(sales.getUserId());
		SvcStore store = svcStoreMapper.selectByPrimaryKey(sales.getStoreId());
		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(sales.getBrandId());

		// 쿠폰 / 스탬프 발급 처리 초기화
		CouponProcessor couponProcessor = appContext.getBean(CouponProcessor.class);
		couponProcessor.init(user, brand, store);

		// 스탬프 사용 설정 확인
		if (!couponProcessor.isUseStamp()) {
			return;
		}

		// 스탬프 발급 및 로그 기록
		SvcUserStamp record = toSvcUserStamp(sales, couponProcessor);
		for (int i = 0; i < stampIssueCount; i++) {
			svcUserStampMapper.insertSelective(record);
			addStampLog(record);
		}

		// 쿠폰 발급
		List<Long> couponIds = addCouponByStamp(couponProcessor);

		// 스탬프 적립, 쿠폰 발급 알림
		notifyRewardAdded(store.getId(), user.getId(), stampIssueCount, couponIds);
	}

	/**
	 * 매출로 부터 스탬프 취소 처리
	 * 스탬프 취소시 관련된 쿠폰이 있으면 함께 취소 처리
	 * 
	 * @param sales
	 * @throws RequestResolveException
	 */
	private void cancelStampBySales(SvcSalesExtended sales) throws RequestResolveException {

		// 적립된 스탭프가 있는지 조회
		SvcUserStampExample example = new SvcUserStampExample();
		example.setOrderByClause("STAMP_ST ASC"); // 취소 우선 순위: 발행(401001) > 사용(402002)
		example.createCriteria() // 검색 조건
				.andSalesIdEqualTo(sales.getId()) // 매출로 부터 발생한 스탬프
				.andStampStIn(Arrays.asList(STAMP_ST_SAVING, STAMP_ST_USED)); // 적립된 상태 (취소, 만료된 것들은 취소 불가)

		List<SvcUserStamp> stamps = svcUserStampMapper.selectByExample(example);
		logger.debug("Cancel {} stamps from sales refund.", stamps.size());

		// 취소 가능한 적립된 스탭프가 없으면 통과
		if (stamps.isEmpty()) {
			return;
		}

		Set<Long> couponSet = new HashSet<>();
		for (SvcUserStamp targetStamp : stamps) {

			// 스탬프를 통해 발급된 쿠폰이 있으면 쿠폰 취소 하도록 기록	
			if (STAMP_ST_USED.equals(targetStamp.getStampSt()) && targetStamp.getCouponId() != null && targetStamp.getCouponId() != 0) {
				couponSet.add(targetStamp.getCouponId());
			}

			// 취소 처리, 스탬프 상태만 갱신 취소 처리			
			targetStamp.setStampSt(STAMP_ST_CANCEL); // 취소
			targetStamp.setCreated(null); // db에서 자동 처리
			targetStamp.setUpdated(null); // db에서 자동 처리
			svcUserStampMapper.updateByPrimaryKeySelective(targetStamp);
			addStampLog(targetStamp);
		}

		// 쿠폰 취소 처리 및 연관된 스탬프 상태를 발행으로 변경
		logger.debug("Cancel {} coupons from sales refund.", couponSet.size());
		for (Long couponId : couponSet) {
			boolean canceled = cancelCouponIfPossible(couponId, sales.getOrderId(), false); // isStrict를 false로 설정하여 쿠폰이 사용/만료되었으면 무시하도록 함.

			// 쿠폰 취소에 따른 관련 사용된 스탬프들을 적립 상태로 변경
			if (canceled) {
				resetStampCouponId(couponId);
			}
		}

		// 스탬프, 쿠폰 취소 알림
		SvcUserStamp stamp = stamps.get(0);
		notifyRewardCanceled(stamp.getId(), stamp.getUserId(), stamps.size(), new ArrayList<>(couponSet));
	}

}
