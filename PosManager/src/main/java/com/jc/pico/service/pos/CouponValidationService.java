package com.jc.pico.service.pos;

import java.util.List;

import com.jc.pico.bean.SvcCouponWithBLOBs;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcUserCoupon;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.RequestResolveException;

public interface CouponValidationService {

	/**
	 * 사용 가능한 쿠폰
	 */
	public static final int VALID_OK = 1;

	/**
	 * 사용 취소한 쿠폰 (재발행됨)
	 */
	public static final int VALID_USE_CANCEL = 2;

	/**
	 * 사용한 쿠폰
	 */
	public static final int VALID_USED = 3;

	/**
	 * 발행 취소한 쿠폰
	 */
	public static final int VALID_ISSUE_CANCEL = 4;

	/**
	 * 유효 기간 지남
	 */
	public static final int VALID_EXPIRED = 5;

	/**
	 * 사용 할 수 있는 기간이 아님
	 */
	public static final int VALID_NOT_PERIOD = 6;

	/**
	 * 사용 가능한 메뉴가 아님
	 */
	public static final int VALID_NOT_TARGET_MENU = 7;

	/**
	 * 사용 가능한 요일이 아님
	 */
	public static final int VALID_NOT_ENABLE_DAY_OF_WEEK = 8;

	/**
	 * 사용 가능한 시간대가 아님
	 */
	public static final int VALID_NOT_ENABLE_TIME = 9;

	/**
	 * 사용 가능한 매장이 아님
	 */
	public static final int VALID_CAN_NOT_USE_STORE = 10;

	/**
	 * 쿠폰을 사용 가능한지 유효성을 검사합니다.
	 * 
	 * @param userCoupon
	 *            사용자 쿠폰 정보
	 * @param coupon
	 *            쿠폰 정보
	 * @param useStore
	 *            사용할 매장
	 * @param useItemId
	 *            사용할 상품 (없으면 0)
	 * @return
	 * @throws RequestResolveException
	 */
	int validCoupon(SvcUserCoupon userCoupon, SvcCouponWithBLOBs coupon, SvcStore useStore, long useItemId) throws RequestResolveException;

	/**
	 * 쿠폰 정보중 json string 으로된 store_ids 를 파싱 한다
	 * 
	 * @param storeIds
	 * @return 파싱된 store ids 목록
	 * @throws InvalidJsonException
	 */
	List<Long> parseCouponStoreIds(String storeIds) throws InvalidJsonException;

}
