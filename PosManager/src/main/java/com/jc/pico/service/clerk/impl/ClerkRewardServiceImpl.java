package com.jc.pico.service.clerk.impl;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcCouponWithBLOBs;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcUserCoupon;
import com.jc.pico.bean.SvcUserCouponExample;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcUserCouponMapper;
import com.jc.pico.service.clerk.ClerkRewardService;
import com.jc.pico.service.pos.CouponValidationService;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.ClerkResult.ErrorCode;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.clerk.ClerkRewardMapper;

@Service
public class ClerkRewardServiceImpl implements ClerkRewardService {

	private static Logger logger = LoggerFactory.getLogger(ClerkRewardServiceImpl.class);

	@Autowired
	private SvcUserCouponMapper svcUserCouponMapper;

	@Autowired
	private SvcCouponMapper svcCouponMapper;

	@Autowired
	private ClerkRewardMapper clerkRewardMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private CouponValidationService couponValidationService;

	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = JsonConvert.getObjectMapper();
	}

	@Override
	public SingleMap getCouponDetail(SingleMap param) throws RequestResolveException, InvalidJsonException {
		if (!param.hasValue("couponCd")) {
			throw new InvalidParameterException("couponCd is empty.");
		}

		final String couponCode = param.getString("couponCd");
		final long itemId = param.getLong("itemId");
		final long storeId = param.getLong("storeId");

		SvcUserCoupon userCoupon = getUserCouponByCode(couponCode);

		if (userCoupon == null) {
			throw new RequestResolveException(ErrorCode.COUPON_NOT_FOUND.code, couponCode + " code coupon is not found.");
		}

		SvcCouponWithBLOBs coupon = svcCouponMapper.selectByPrimaryKey(userCoupon.getCouponId());

		if (coupon == null) {
			throw new RequestResolveException(ErrorCode.COUPON_NOT_FOUND.code, couponCode + " code coupon is not found.");
		}

		if (!coupon.getUsed()) {
			throw new RequestResolveException(ErrorCode.COUPON_DISABLED.code, couponCode + " code coupon is disabled.");
		}

		SvcStore store = svcStoreMapper.selectByPrimaryKey(storeId);

		if (store == null) {
			throw new RequestResolveException(ErrorCode.STORE_NOT_FOUND.code, "Not found store.");
		}

		// 결과 생성
		SingleMap result = objectMapper.convertValue(coupon, SingleMap.class);

		// user coupon 정보 추가
		result.put("couponTp", userCoupon.getCouponTp());
		result.put("couponCd", userCoupon.getCouponCd());
		result.put("isUsed", userCoupon.getUsed());
		result.put("validCode", couponValidationService.validCoupon(userCoupon, coupon, store, itemId));

		// 특정 지점 사용 쿠폰이면 해당 값 파싱하여 전달
		result.put("storeIds", getCouponStoreInfoList(coupon));

		return result;
	}

	private SvcUserCoupon getUserCouponByCode(String couponCd) {
		SvcUserCouponExample example = new SvcUserCouponExample();
		example.createCriteria() // 조건
				.andCouponCdEqualTo(couponCd); // 쿠폰 코드
		List<SvcUserCoupon> coupons = svcUserCouponMapper.selectByExample(example);
		return coupons.size() > 0 ? coupons.get(0) : null;
	}

	/**
	 * 쿠폰에 지정된 사용가능한 매장의 상세 정보 조회
	 * 
	 * @param coupon
	 * @return
	 * @throws RequestResolveException
	 */
	private List<SingleMap> getCouponStoreInfoList(SvcCouponWithBLOBs coupon) throws RequestResolveException {
		// 전체 사용 이거나 사용 가능 지점 정보가 없으면 빈 리스트 반환
		if (coupon.getIsAll() || StringUtils.isEmpty(coupon.getStoreIds())) {
			return Collections.<SingleMap>emptyList();
		}

		List<Long> storeIds;
		try {
			storeIds = couponValidationService.parseCouponStoreIds(coupon.getStoreIds());
		} catch (Exception e) {
			// 서버 사이드 오류. 쿠폰을 사용 할 수 없는 상태가 되도록 처리 
			logger.error("Failed parse coupon store ids. couponId={}, storeIdsJson={}", coupon.getId(), coupon.getStoreIds(), e);
			return Collections.<SingleMap>emptyList();
		}

		if (storeIds.isEmpty()) {
			return Collections.<SingleMap>emptyList();
		}

		return clerkRewardMapper.selectCouponStoreInfoList(storeIds);
	}

}
