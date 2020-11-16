/*
 * Filename	: CouponUtil.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.module;

import com.google.common.collect.Lists;
import com.jc.pico.bean.*;
import com.jc.pico.bean.SvcUserStampExample.Criteria;
import com.jc.pico.exception.SettingNotFoundException;
import com.jc.pico.mapper.*;
import com.jc.pico.utils.BarcodeUtil;
import com.jc.pico.utils.CodeUtil;
//import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.Nullable;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class CouponProcessor {

	protected static Logger logger = LoggerFactory.getLogger(CouponProcessor.class);

	/**
	 * 서비스 적립 방식 : 개별 적립 (스토어)
	 */
	private static final String STAMP_TP_STORE = "410002";

	private static final int DEFAULT_TRIGGER_COUNT = 12;
	private static final String DEFAULT_STAMP_TERM = "01/00/00"; // yy/mm/dd

	private int triggerCount = DEFAULT_TRIGGER_COUNT;
	private String stampTerm = DEFAULT_STAMP_TERM;
	private boolean isUseStamp = false;

	private SvcStore store;
	private User user;

	private SvcBrand brand;
	private SvcCoupon coupon;

	@Autowired
	private SqlSessionTemplate sessionTemplate;

	@Autowired
	private CodeUtil codeUtil;

	@Autowired
	private BarcodeUtil barcodeUtil;

	@Autowired
	private SvcCouponMapper svcCouponMapper;

	@Autowired
	private SvcStampLogMapper svcStampLogMapper;

	@Autowired
	private SvcStoreSetMapper svcStoreSetMapper;

	@Autowired
	private SvcBrandSetMapper svcBrandSetMapper;

	@Autowired
	private SvcUserStampMapper svcUserStampMapper;

	public void init(User user, SvcBrand brand, @Nullable SvcStore store) throws SettingNotFoundException {
		this.brand = brand;
		this.store = store;
		this.user = user;

		try {

			// 통합 적립, 개별 적립 상태에 따라 분할 처리			 
			if (STAMP_TP_STORE.equals(brand.getStampTp())) { // 개별 적립 사용

				SvcStoreSetExample example = new SvcStoreSetExample();
				example.createCriteria() //
						.andBrandIdEqualTo(brand.getId()) //
						.andStoreIdEqualTo(store.getId());

				SvcStoreSet setting = svcStoreSetMapper.selectByExample(example).get(0);
				triggerCount = setting.getStampCnt();
				stampTerm = setting.getStampTerm();
				isUseStamp = setting.getUseStamp();
				coupon = svcCouponMapper.selectByPrimaryKey(setting.getStampCoupon());

			} else { // 통합 적립 사용 STAMP_TP_ALL : 410001

				SvcBrandSetExample example = new SvcBrandSetExample();
				example.createCriteria() //
						.andBrandIdEqualTo(brand.getId());

				SvcBrandSet setting = svcBrandSetMapper.selectByExample(example).get(0);
				triggerCount = setting.getStampCnt();
				stampTerm = setting.getStampTerm();
				isUseStamp = setting.getUseStamp();
				coupon = svcCouponMapper.selectByPrimaryKey(setting.getStampCoupon());
			}
		} catch (Exception e) {
			logger.warn("coupon config load fail!", e);
			throw new SettingNotFoundException();
		}
	}

	/**
	 * 스탬프 발행
	 * 
	 * @param order
	 *            주문 정보(Nullable)
	 * @param store
	 *            매장 정보(Nullable)
	 * @param additional
	 *            로그 저장용 추가 정보
	 * @throws SettingNotFoundException
	 */
	@Transactional
	@Deprecated // 오더나인 넘어온 코드로 사용하지 않음.
	public void addStamp(@Nullable SvcOrder order, @Nullable SvcStore store, @Nullable SvcStampLog additional) throws SettingNotFoundException {

		SvcUserStamp stamp = new SvcUserStamp();
		stamp.setUserId(user.getId());
		stamp.setBrandId(brand.getId());
		stamp.setCouponId(0l);
		stamp.setExpire(getStampExpire(new Date()));
		stamp.setStampTp(codeUtil.getBaseCodeByAlias("stamp-order-add"));
		stamp.setStampSt(codeUtil.getBaseCodeByAlias("stamp-log-saving"));
		if (store != null) {
			stamp.setStoreId(store.getId());
		}
		if (order != null) {
			stamp.setOrderId(order.getId());
		}

		svcUserStampMapper.insertSelective(stamp);

		SvcCouponLog log = new SvcCouponLog();
		if (additional != null) {
			log.setUserAgent(additional.getUserAgent());
			log.setSrcIp(additional.getSrcIp());
			log.setClientId(additional.getClientId());
			log.setDeviceId(additional.getDeviceId());
			log.setGrantType(additional.getClientId());
		}

		issue(log);
	}

	/**
	 * 쿠폰 발행
	 * 
	 * @param additional
	 *            로그 저장용 추가 정보
	 * @throws SettingNotFoundException
	 */
	@Transactional
	public List<Long> issue(@Nullable SvcCouponLog additional) throws SettingNotFoundException {
		String userTpReular = codeUtil.getBaseCodeByAlias("user");

		if (!userTpReular.equals(user.getType())) {
			return Collections.emptyList(); // 준회원은 쿠폰 생성 하지 않음.
		}

		if (coupon == null) {
			// 스탬프 쿠폰이 없음
			return Collections.emptyList();
		}

		SvcUserStampMapper userStampMapper = sessionTemplate.getMapper(SvcUserStampMapper.class);
		SvcUserCouponMapper couponMapper = sessionTemplate.getMapper(SvcUserCouponMapper.class);

		SvcUserStampExample userStampExample = new SvcUserStampExample();
		Criteria userStampExampleCriteria = userStampExample.createCriteria(); //
		userStampExampleCriteria.andBrandIdEqualTo(brand.getId()) //
				.andUserIdEqualTo(user.getId()) //
				.andStampStEqualTo(codeUtil.getBaseCodeByAlias("stamp-log-saving")) // 유효한 스탬프
				.andCouponIdEqualTo(0l); // 쿠폰 미발행 스템프.

		// 개별 적립 설정이면 해당 매장에 적립된 스탬프 확인
		if (STAMP_TP_STORE.equals(brand.getStampTp())) {
			userStampExampleCriteria.andStoreIdEqualTo(store.getId());
		}

		userStampExample.setOrderByClause("CREATED ASC");

		// stamp count 확인
		int stampCnt = userStampMapper.countByExample(userStampExample);
		if (stampCnt < this.triggerCount) {
			return Collections.emptyList();
		}
		int couponCnt = stampCnt / this.triggerCount;

		List<Long> couponIds = new ArrayList<>();
		String logType = codeUtil.getBaseCodeByAlias("coupon-log-issue"); // 발행 상태
		String couponTp = codeUtil.getBaseCodeByAlias("stamp-complete-publish"); // 스탬프로 부터 발행
		// 쿠폰 생성(준회원 -> 정회원 전환시 한번에 여러개 생성 될 수 있음)
		for (int i = 0; i < couponCnt; i++) {
			SvcUserCoupon userCoupon = new SvcUserCoupon();
			userCoupon.setBrandId(brand.getId());
			userCoupon.setUserId(user.getId());
			userCoupon.setStoreId(coupon.getStoreId());
			userCoupon.setCouponId(coupon.getId());
			userCoupon.setCouponCd(barcodeUtil.makeCouponCode(brand.getBrandCd()));
			userCoupon.setExpire(getCouponExpire(true));
			userCoupon.setCouponSt(logType); // 발행
			userCoupon.setCouponTp(couponTp);
			userCoupon.setUsed(false);
			userCoupon.setStampCnt(triggerCount);
			userCoupon.setDiscountTp(coupon.getDiscountTp());
			userCoupon.setDiscount(coupon.getDiscount());

			couponMapper.insertSelective(userCoupon);
			couponIds.add(userCoupon.getId());

			// insert coupon log
			saveLog(logType, userCoupon, null, additional);

			// update stamp log(set coupon id)      
			List<SvcUserStamp> userStamp = userStampMapper.selectByExampleWithRowbounds(userStampExample, new RowBounds(0, triggerCount));
			for (SvcUserStamp stampLog : userStamp) {
				stampLog.setCouponId(userCoupon.getId());
				stampLog.setStampSt(codeUtil.getBaseCodeByAlias("stamp-log-trans")); // 사용한 상태로 변경
				userStampMapper.updateByPrimaryKeySelective(stampLog);
				addStampLog(stampLog);
			}
		}
		return couponIds;
	}

	/**
	 * 스탬프 취소
	 * 
	 * @param stamps
	 *            삭제할 스템프 정보
	 * @param order
	 *            주문 정보(Nullable)
	 * @param additional
	 *            로그 저장용 추가 정보
	 * @throws SettingNotFoundException
	 */
	@Transactional
	@Deprecated
	public void cancel(List<SvcUserStamp> stamps, @Nullable SvcOrder order, @Nullable SvcStampLog additional) throws SettingNotFoundException {
		if (stamps == null || stamps.size() == 0)
			return;
		SvcUserCouponMapper couponMapper = sessionTemplate.getMapper(SvcUserCouponMapper.class);
		SvcUserStampMapper stampMapper = sessionTemplate.getMapper(SvcUserStampMapper.class);

		for (SvcUserStamp stamp : stamps) {
			if (stamp.getCouponId() > 0) { // 스탬프로 발행된 쿠폰이 있을 경우 발행 취소. 단 미사용인 경우만.
				SvcUserCouponExample example = new SvcUserCouponExample();
				example.createCriteria().andBrandIdEqualTo(brand.getId()).andUserIdEqualTo(user.getId()).andUsedEqualTo(false)
						.andCouponIdEqualTo(stamp.getCouponId());

				List<SvcUserCoupon> coupons = couponMapper.selectByExample(example);
				if (coupons.size() > 0) {
					SvcUserCoupon coupon = coupons.get(0);
					couponMapper.deleteByPrimaryKey(coupon.getId());
				}
			} else {
				stampMapper.deleteByPrimaryKey(stamp.getId());
			}

			saveLog(true, stamp, order, additional);
		}

	}

	/**
	 * 쿠폰 사용
	 * 
	 * @param coupon
	 *            취소할 쿠폰 정보
	 * @param order
	 *            주문 정보(Nullable)
	 * @param additional
	 *            로그 저장용 추가 정보
	 * @throws SettingNotFoundException
	 */
	@Transactional
	@Deprecated
	public void use(SvcUserCoupon coupon, @Nullable SvcOrder order, @Nullable SvcCouponLog additional) throws SettingNotFoundException {
		SvcUserCouponMapper mapper = sessionTemplate.getMapper(SvcUserCouponMapper.class);

		coupon.setUsed(true);
		mapper.updateByPrimaryKeySelective(coupon);

		String logTp = codeUtil.getBaseCodeByAlias("coupon-log-use");
		saveLog(logTp, coupon, order, additional);
	}

	/**
	 * 쿠폰 취소
	 * 
	 * @param coupon
	 *            취소할 쿠폰 정보
	 * @param order
	 *            주문 정보(Nullable)
	 * @param additional
	 *            로그 저장용 추가 정보
	 * @throws SettingNotFoundException
	 */
	@Transactional
	@Deprecated
	public void cancel(SvcUserCoupon coupon, @Nullable SvcOrder order, @Nullable SvcCouponLog additional) throws SettingNotFoundException {
		SvcUserCouponMapper mapper = sessionTemplate.getMapper(SvcUserCouponMapper.class);

		coupon.setUsed(false);
		mapper.updateByPrimaryKeySelective(coupon);

		String logTp = codeUtil.getBaseCodeByAlias("coupon-log-cancel");
		saveLog(logTp, coupon, order, additional);
	}

	/**
	 * stamp log 저장
	 * 
	 * @param logTypeCode
	 *            로그 타입(coupon-log-issue, coupon-log-cancel, coupon-log-use)
	 * @param userCoupon
	 *            취소할 쿠폰 객체
	 * @param order
	 *            취소된 주문 객체(Nullable)
	 * @param log
	 *            로그 정보(요청자 user-agent , IP등
	 */
	private void saveLog(String logTypeCode, SvcUserCoupon userCoupon, @Nullable SvcOrder order, @Nullable SvcCouponLog log) {
		SvcCouponLogMapper couponLogMapper = sessionTemplate.getMapper(SvcCouponLogMapper.class);
		if (log == null) {
			log = new SvcCouponLog();
		}

		log.setLogTp(logTypeCode);
		log.setBrandId(brand.getId());
		log.setUserId(user.getId());
		if (order != null) {
			log.setOrderId(order.getId());
		}
		log.setCouponNm(coupon.getCouponNm());
		log.setCouponId(userCoupon.getCouponId());
		log.setCouponCd(userCoupon.getCouponCd());

		couponLogMapper.insertSelective(log);
	}

	/**
	 * stamp log 저장
	 * 
	 * @param isCancel
	 *            취소일 경우 true, 적립일 경우 false
	 * @param userStamp
	 *            취소할 스템프 객체
	 * @param order
	 *            취소된 주문 객체(Nullable)
	 * @param log
	 *            로그 정보(요청자 user-agent , IP등
	 */
	private void saveLog(boolean isCancel, SvcUserStamp userStamp, @Nullable SvcOrder order, @Nullable SvcStampLog log) {
		String logTp;
		if (isCancel) {
			logTp = codeUtil.getBaseCodeByAlias("stamp-log-cancel");
		} else {
			logTp = codeUtil.getBaseCodeByAlias("stamp-log-saving");
		}

		if (log == null) {
			log = new SvcStampLog();
		}

		SvcStampLogMapper mapper = sessionTemplate.getMapper(SvcStampLogMapper.class);

		log.setLogTp(logTp);
		log.setBrandId(brand.getId());
		log.setUserId(user.getId());
		if (order != null) {
			log.setOrderId(order.getId());
		}
		;

		mapper.insertSelective(log);
	}

	/**
	 * 만료일 계산
	 * 
	 * @param isFuture
	 *            true (만료 예정일) / false (유효 발급 시점)
	 * @return
	 * @throws SettingNotFoundException
	 */
	private Date getCouponExpire(boolean isFuture) throws SettingNotFoundException {
		String expireTpTerm = codeUtil.getBaseCodeByAlias("expire-tp-term");
		String expireTpFixed = codeUtil.getBaseCodeByAlias("expire-tp-fixed-term");

		if (expireTpFixed.equals(coupon.getExpireTp())) {
			return coupon.getExpire();
		} else if (expireTpTerm.equals(coupon.getExpireTp())) {
			try {
				String term = coupon.getTerm();
				String[] splits = term.split("/");
				Calendar cal = Calendar.getInstance();
				if (isFuture) {
					cal.add(Calendar.YEAR, Integer.valueOf(splits[0]));
					cal.add(Calendar.MONTH, Integer.valueOf(splits[1]));
					cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(splits[2]));
				} else {
					cal.add(Calendar.YEAR, -Integer.valueOf(splits[0]));
					cal.add(Calendar.MONTH, -Integer.valueOf(splits[1]));
					cal.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(splits[2]));
				}
				return cal.getTime();
			} catch (Exception e) {
				logger.warn("get expire date error.", e);
				throw new SettingNotFoundException();
			}
		} else {
			throw new SettingNotFoundException();
		}
	}

	public Date getStampExpire(Date issueDate) throws SettingNotFoundException {
		try {
			String[] splits = stampTerm.split("/");
			Calendar cal = Calendar.getInstance();
			cal.setTime(issueDate);
			cal.add(Calendar.YEAR, Integer.valueOf(splits[0]));
			cal.add(Calendar.MONTH, Integer.valueOf(splits[1]));
			cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(splits[2]));
			return cal.getTime();
		} catch (Exception e) {
			logger.warn("get expire date error.", e);
			throw new SettingNotFoundException();
		}
	}

	/**
	 * 스탬프 발급/취소 로그 기록
	 * 
	 * @param userStamp
	 */
	private void addStampLog(SvcUserStamp userStamp) {
		SvcStampLog log = new SvcStampLog();
		log.setBrandId(userStamp.getBrandId());
		log.setClientId("system");
		log.setDeviceId("system");
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

	public List<SvcUserStamp> getCurrentStamps(long brandId) throws SettingNotFoundException {
		SvcUserStampMapper mapper = sessionTemplate.getMapper(SvcUserStampMapper.class);
		SvcUserStampExample example = new SvcUserStampExample();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);

		example.createCriteria().andBrandIdEqualTo(brandId).andUserIdEqualTo(user.getId()).andExpireGreaterThanOrEqualTo(cal.getTime())
				.andCouponIdEqualTo(0l);

		List<SvcUserStamp> stamps = mapper.selectByExample(example);

		return (stamps != null ? stamps : Lists.<SvcUserStamp>newArrayList());
	}

	public int getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(int triggerCount) {
		this.triggerCount = triggerCount;
	}

	public SvcStore getStore() {
		return store;
	}

	public void setStore(SvcStore store) {
		this.store = store;
	}

	public SvcBrand getBrand() {
		return brand;
	}

	public void setBrand(SvcBrand brand) {
		this.brand = brand;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isUseStamp() {
		return isUseStamp;
	}	
	
}
