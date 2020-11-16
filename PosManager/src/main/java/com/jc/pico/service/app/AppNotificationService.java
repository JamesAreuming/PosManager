package com.jc.pico.service.app;

import java.util.List;

import com.jc.pico.bean.SvcOrder;

/**
 * 앱에게 알림 서비스
 * 
 * @author hyo
 *
 */
public interface AppNotificationService {

	/**
	 * 주문 변경 알림
	 * 
	 * @param newOrder
	 */
	void notifyOrderStatusChanged(SvcOrder newOrder);

	/**
	 * 스탬프 적립 취소 알림
	 * 
	 * @param storeId
	 * @param userId
	 * @param size
	 */
	void notifyStampCanceled(Long storeId, Long userId, int stampCount);

	/**
	 * 스탬프 적립 알림
	 * 
	 * @param id
	 * @param id2
	 * @param newStampCount
	 */
	void notifyStampSaved(Long storeId, Long userId, int stampCount);

	/**
	 * 쿠폰 발급 알림
	 * 
	 * @param id
	 * @param id2
	 * @param couponIds
	 */
	void notifyCouponIssued(Long storeId, Long userId, List<Long> userCouponIds);

	/**
	 * 쿠폰 발급 취소 알림
	 * 
	 * @param storeId
	 * @param userId
	 * @param cancelCouponIds
	 */
	void notifyCouponIssueCanceled(Long storeId, Long userId, List<Long> userCouponIds);

	/**
	 * 쿠폰 사용 알림
	 * 
	 * @param storeId
	 * @param userId
	 * @param userCouponId
	 */
	void notifyCouponUsed(Long storeId, Long userId, Long userCouponId);

}
