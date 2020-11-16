package com.jc.pico.service.pos.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcCoupon;
import com.jc.pico.bean.SvcCouponWithBLOBs;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcUserCoupon;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.service.pos.CouponValidationService;
import com.jc.pico.utils.JsonConvert;

@Service
public class CouponValidationServiceImpl implements CouponValidationService {

	private static Logger logger = LoggerFactory.getLogger(CouponValidationServiceImpl.class);

	/**
	 * 쿠폰 상태 : 발급
	 */
//	private static final String COUPON_TP_ISSUE = "402001";

	/**
	 * 쿠폰 상태: 사용 취소
	 */
	private static final String COUPON_TP_CANCEL = "402002";

	/**
	 * 쿠폰 상태 : 사용함
	 */
	private static final String COUPON_TP_USED = "402003";

	/**
	 * 쿠폰 상태 : 발급 취소
	 */
	private static final String COUPON_TP_ISSUE_CANCEL = "402004";

	/**
	 * 쿠폰의 enable days 값을 사용하지 않도록 설정한 상태
	 */
	private static final String ENABLE_DAYS_DISABLED = "0000000";

	/**
	 * begin 부터 expire 까지 유효
	 */
	public static final String EXPIRE_TP_FIXED_TERM = "404002";

	/**
	 * 쿠폰에 있는 STORE_IDS Json string 파싱용
	 * {"storeIds":["6","5"]}
	 *
	 */
	private static class StoreIds {
		List<Long> storeIds;
	}

	/**
	 * 유효한 쿠폰인지 확인한다
	 * 
	 * @param userCoupon
	 * @param coupon
	 * @param posStoreId
	 * @param itemId
	 * @return
	 * @throws RequestResolveException
	 */
	@Override
	public int validCoupon(SvcUserCoupon userCoupon, SvcCouponWithBLOBs coupon, SvcStore store, long itemId) {
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		final Date now = cal.getTime();

		// store id가 설정되어 있으면 해당 매장에서만 사용 가능
		if (coupon.getStoreId() != null && coupon.getStoreId().longValue() != 0 && !Objects.equals(coupon.getStoreId(), store.getId())) {
			return VALID_CAN_NOT_USE_STORE;
		}

		// 해당 브랜드 산하 매장 에서만 사용 가능
		if (!Objects.equals(coupon.getBrandId(), store.getBrandId())) {
			return VALID_CAN_NOT_USE_STORE;
		}

		// 전체 매장에서 사용하도록 설정된 것이 아니면 storeIds 에 설정된 매장에서만 사용 가능
		if (!coupon.getIsAll()) {
			List<Long> storeIds = parseCouponStoreIds(coupon);
			if (!storeIds.contains(store.getId())) {
				return VALID_CAN_NOT_USE_STORE;
			}
		}

		String couponSt = userCoupon.getCouponSt();

		if (COUPON_TP_CANCEL.equals(couponSt)) { // 사용 취소 (재발행됨)
			return VALID_USE_CANCEL;
		} else if (COUPON_TP_USED.equals(couponSt)) { // 사용함
			return VALID_USED;
		} else if (COUPON_TP_ISSUE_CANCEL.equals(couponSt)) { // 발행 취소 
			return VALID_ISSUE_CANCEL;
		}
		// else 402001 발행, 아래 유효성 체크

		// 유효기간 확인 : 만료 기간이 지났는지 확인, expire tp에 상관 없이 만료기간은 확인함.
		if (compareYmdDate(now, userCoupon.getExpire()) > 0) {
			return VALID_EXPIRED; // 유효기간 지남
		}

		// 유효기간 확인: 만료 타입이 기간 지정이면 현재가 begin 이후 날짜인지 확인
		if (EXPIRE_TP_FIXED_TERM.equals(coupon.getExpireTp()) && compareYmdDate(now, coupon.getBegin()) < 0) {
			return VALID_NOT_PERIOD; // 사용 가능한 기간이 아님
		}

		// 적용 가능한 메뉴인지 확인
		if (coupon.getTargetMenuId() != null && coupon.getTargetMenuId() != 0 && itemId != 0 && itemId != coupon.getTargetMenuId()) {
			return VALID_NOT_TARGET_MENU; // 사용 가능한 메뉴가 아님
		}

		// 이하 제한 조건 사용 안함
		if (!coupon.getHasUseLimit()) {
			return VALID_OK;
		}

		// 사용 가능 요일 확인		
		if (!isEnabledDayOfWeek(coupon, cal)) {
			return VALID_NOT_ENABLE_DAY_OF_WEEK; // 사용 가능한 요일이 아님
		}

		// 사용 가능 시간대 확인
		if (!isEnabledTime(coupon, cal)) {
			return VALID_NOT_ENABLE_TIME;
		}

		return VALID_OK;
	}

	@Override
	public List<Long> parseCouponStoreIds(String storeIds) throws InvalidJsonException {
		StoreIds storeIdsObj = JsonConvert.JsonConvertObject(storeIds, StoreIds.class);
		return storeIdsObj.storeIds != null ? storeIdsObj.storeIds : Collections.<Long>emptyList();
	}

	/**
	 * 사용 가능한 시간대인지 확인
	 * 사용 가능한 시간대 설정이 비활성화이면 사용 가능한 시간대로 처리
	 * 
	 * @param coupon
	 *            쿠폰 저보
	 * @param nowCal
	 *            현재 날짜 캘린더
	 * @return 사용 가능한 시간대이면 true, 아니면 false
	 * @throws RequestResolveException
	 */
	private boolean isEnabledTime(SvcCoupon coupon, Calendar nowCal) {
		Calendar openCal;
		Calendar closeCal;
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
			openCal = Calendar.getInstance();
			openCal.setTime(timeFormat.parse(coupon.getOpenTm()));
			closeCal = Calendar.getInstance();
			closeCal.setTime(timeFormat.parse(coupon.getCloseTm()));
		} catch (ParseException e) {
			// 서버 사이드 오류로 파싱 불가시 쿠폰을 사용하지 못하도록 함
			logger.error("Server side error. Invalid open / close tm format. couponId={}, openTm={}, closeTm={}", coupon.getId(), coupon.getOpenTm(),
					coupon.getCloseTm(), e);
			return false;
		}
		final int openHour = openCal.get(Calendar.HOUR_OF_DAY);
		final int openMin = openCal.get(Calendar.MINUTE);
		final int closeHour = closeCal.get(Calendar.HOUR_OF_DAY);
		final int closeMin = closeCal.get(Calendar.MINUTE);

		// 비활성화 상태인지 확인
		if (openHour == 0 && openMin == 0 && closeHour == 0 && closeMin == 00) {
			// 모두 0이면 사용하지 않는 상태
			return true;
		}

		final int nowHour = nowCal.get(Calendar.HOUR_OF_DAY);
		final int nowMin = nowCal.get(Calendar.MINUTE);

		// open <= now <= close
		return openHour <= nowHour && openMin <= nowMin // open
				&& nowHour <= closeHour && nowMin <= closeMin; // close
	}

	/**
	 * 사용 가능한 요일인지 확인한다.
	 * 요일 제한을 사용하지 않으면 항상 true 반환
	 * 
	 * @param enableDays
	 *            요일 정보가 있는 7자리 문자열 (0000000)
	 * @param nowCal
	 *            현재 날짜
	 * @return 사용가능한 요일이면 true, 아니면 false
	 */
	private boolean isEnabledDayOfWeek(SvcCoupon coupon, Calendar nowCal) {
		final String enableDays = coupon.getEnableDays();
		final int nowDayOfWeek = nowCal.get(Calendar.DAY_OF_WEEK);

		if (StringUtils.isEmpty(enableDays) // 비어 있으면 요일 검증 하지 않음 
				|| enableDays.length() != 7 // 7자리여야함 잘못된 정보 있으면 요일 검증 하지 않음
				|| ENABLE_DAYS_DISABLED.equals(enableDays)) { // 모두 비활성화 상태면 요일 검증 하지 않음
			return true;
		}

		// enable days = 0000000 = 일월화수목금토
		// DAY_OF_WEEK = 일=1,월=2 .. 토=7
		// 예) 1000001 이면 일요일, 토요일에만 사용 가능
		return enableDays.charAt(nowDayOfWeek - 1) == '1';
	}

	/**
	 * json array string으로 저장된 store id 목록을 파싱
	 * 
	 * @param storeIds
	 * @return
	 * @throws InvalidJsonException
	 */
	private List<Long> parseCouponStoreIds(SvcCouponWithBLOBs coupon) {

		String storeIds = coupon.getStoreIds();

		if (StringUtils.isEmpty(storeIds)) {
			return Collections.emptyList();
		}
		// {"storeIds":["6","5"]}
		try {
			return parseCouponStoreIds(storeIds);
		} catch (Exception e) {
			// 서버 사이드 오류. 쿠폰을 사용 할 수 없는 상태가 되도록 처리 
			logger.error("Failed parse coupon store ids. couponId={}, storeIdsJson={}", coupon.getId(), coupon.getStoreIds(), e);
			return Collections.<Long>emptyList();
		}
	}

	/**
	 * 날짜를 비교한다 (년 월 일)
	 * 
	 * @param date1
	 * @param date2
	 * @return - 0이면 동일한 날짜
	 *         - 0보다 작으면 date1 < date2 (date1 과거, date2 미래)
	 *         - 0보다 크면 date1 > date2 (date1 미래, date 과거)
	 */
	private static int compareYmdDate(Date date1, Date date2) {
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);

		int compare = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (compare != 0) {
			return compare;
		}

		return cal1.get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR);
	}

}
