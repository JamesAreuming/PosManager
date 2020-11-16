package com.jc.pico.service.app.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcCoupon;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcUserCoupon;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserDeviceExample;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcUserCouponMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.queue.PushSender;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.DateFormat;
import com.jc.pico.utils.bean.SendPush;

@Service
public class AppNotificationServiceImpl implements AppNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(AppNotificationServiceImpl.class);

	/**
	 * 주문 상태 : 미승인
	 */
	public static final String ORDER_ST_DISAPPROVAL = "607001";

	/**
	 * 주문 상태 : 승인
	 */
	public static final String ORDER_ST_APPOVAL = "607002";

	/**
	 * 주문 상태 : 취소 (승인 취소)
	 */
	public static final String ORDER_ST_CANCEL = "607003";

	/**
	 * 주문 상태 : 거절 (미승인 거절)
	 */
	public static final String ORDER_ST_REJECT = "607004";

	/**
	 * 주문 상태 : 완료
	 */
	public static final String ORDER_ST_COMPLETE = "607005";

	public static final String ORDER_TP_ORDER = "605001";
	public static final String ORDER_TP_RESERVATION = "605002";
	public static final String ORDER_TP_CONTRACT = "605003";

	/**
	 * OS 종류 : 안드로이드
	 */
	public static final String OS_TP_ANDROID = "103001";

	/**
	 * OS 종류 : iOS
	 */
	public static final String OS_TP_IOS = "103002";

	public static final String MESSAGE_TP_ORDER_DISAPPROVAL = "203011";
	public static final String MESSAGE_TP_ORDER_APPOVAL = "203012";
	public static final String MESSAGE_TP_ORDER_READY = "203013";
	public static final String MESSAGE_TP_ORDER_CANCEL = "203014";
	public static final String MESSAGE_TP_RESERVATION_DISAPPROVAL = "203015";
	public static final String MESSAGE_TP_RESERVATION_APPOVAL = "203016";
	public static final String MESSAGE_TP_RESERVATION_READY = "203017";
	public static final String MESSAGE_TP_RESERVATION_COMPLETE = "203018";
	public static final String MESSAGE_TP_RESERVATION_CANCEL = "203019";
	public static final String MESSAGE_TP_CONTRACT_DISAPPROVAL = "203020";
	public static final String MESSAGE_TP_CONTRACT_APPOVAL = "203021";
	public static final String MESSAGE_TP_CONTRACT_READY = "203022";
	public static final String MESSAGE_TP_CONTRACT_COMPLETE = "203023";
	public static final String MESSAGE_TP_CONTRACT_CANCEL = "203024";
	public static final String MESSAGE_TP_STAMP_SAVING = "203025";
	public static final String MESSAGE_TP_COUPON_ISSUE = "203005";
	public static final String MESSAGE_TP_STAMP_CANCEL = "203026";
	public static final String MESSAGE_TP_COUPON_ISSUE_CANCEL = "203006";
	public static final String MESSAGE_TP_COUPON_EXPIRE_REMIND = "203027";
	public static final String MESSAGE_TP_NOTICE = "203004";
	public static final String MESSAGE_TP_COUPON_USE = "203007";

	public static final String MSG_CODE_PREFIX_PUSH = "app.push.";
	public static final String MSG_CODE_POSTFIX_TITLE = ".title";
	public static final String MSG_CODE_POSTFIX_MESSAGE = ".msg";

	/**
	 * 주문 경로 : 앱
	 */
	public static final String ORDER_PATH_APP = "606002";

	private static final SimpleDateFormat DATE_FORMAT_HHMM = new SimpleDateFormat("HHhmm");

	/**
	 * 사용자 타입: 정회원
	 */
	private static final Object USER_TYPE_REGULAR_MEMBER = "300001";

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private UserDeviceMapper userDeviceMapper;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SvcUserCouponMapper svcUserCouponMapper;

	@Autowired
	private SvcCouponMapper svcCouponMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PushSender pushSender;

	@Autowired
	private CodeUtil codeUtil;

	/**
	 * 푸시 정보.
	 * 메시지 지역화를 위해 언어별로 메시지 정보를 담도록 함.
	 * 
	 */
	private static class PushInfo {
		public String title;
		public String message;
		public final Map<String, List<String>> pushTokens;

		PushInfo(Map<String, List<String>> pushTokens) {
			this.pushTokens = pushTokens;
		}
	}

	@Override
	public void notifyOrderStatusChanged(SvcOrder order) {
		if (order == null) {
			return;
		}

		// 앱에서 넣은 주문인 경우만 처리
		if (!ORDER_PATH_APP.equals(order.getPathTp())) {
			return;
		}

		// 모바일 앱에서 발생한 주문인데 사용자 정보가 없을 수는 없다. 
		// 여기서 리턴되면 문제 상황!
		if (order.getUserId() == null || order.getUserId() == 0) {
			logger.error("Ignored notify order status changed. Cause by order has not user id.");
			return;
		}

		final String messageTypeCode = getMessageTypeCode(order);

		if (messageTypeCode == null) {
			return;
		}

		// 푸시 토큰 조회 (locale에 따라 분류)
		Map<Locale, PushInfo> pushInfo = getPushInfoByUserId(order.getUserId());

		// 로케일에 맞게 메시지 생성		
		final String messageTypeAlias = codeUtil.getAliasByCode(messageTypeCode);
		buildOrderStatusChangeMessage(pushInfo, messageTypeCode, messageTypeAlias, order);

		// 메시지 전송
		for (PushInfo info : pushInfo.values()) {

			SendPush push = new SendPush();
			push.setPushIds(info.pushTokens);
			push.setTitle(info.title);
			push.setContent(info.message);
			push.setType(messageTypeAlias);
			push.setBrandId(order.getBrandId());

			push.addExtra("orderId", order.getId());

			pushSender.send(false, push);

			logger.info("Sent order status changed. msgTp={}, orderId={}, userId={}, msg=[{}]" // 
					, messageTypeCode, order.getId(), order.getUserId(), info.message);
		}

	}

	/**
	 * 주문 변경 알림 메시지 생성
	 * 
	 * @param pushInfos
	 * @param messageTypeCode
	 * @param order
	 */
	private void buildOrderStatusChangeMessage(Map<Locale, PushInfo> pushInfos, String messageTypeCode, String messageTypeAlias, SvcOrder order) {

		for (Locale locale : pushInfos.keySet()) {
			PushInfo info = pushInfos.get(locale);

			// message_ko_KR.propeties 파일 참조 
			String title = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + messageTypeAlias + MSG_CODE_POSTFIX_TITLE, null, null, locale);
			String message = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + messageTypeAlias + MSG_CODE_POSTFIX_MESSAGE,
					buildOrderStatusChangedMessageArgs(order, messageTypeCode, locale), null, locale);

			if (title == null || message == null) {
				logger.warn("Ignored notifiy order status changed. Cause by Can not build title or message. messageTp={}, locale={}", messageTypeCode,
						locale);
				continue;
			}
			info.title = message; // title; FIXME 미기획/미번역 관계로 제목은 메시지로 대체
			info.message = message;
		}
	}

	/**
	 * 로케일별로 분류한 푸시 토큰 정보 조회
	 * 
	 * @param userId
	 * @return
	 */
	private Map<Locale, PushInfo> getPushInfoByUserId(Long userId) {

		// 푸시 설정이 off 되어 있으면 전송하지 않도록 함
		User user = userMapper.selectByPrimaryKey(userId);

		// push 허용, 정규회원이 아니면 전송하지 않도록 함
		if (user == null || !user.getIsSvcPush() || !USER_TYPE_REGULAR_MEMBER.equals(user.getType())) {
			return Collections.emptyMap();
		}

		UserDeviceExample example = new UserDeviceExample();
		example.createCriteria() // 조건
				.andUserIdEqualTo(userId) // 사용자 아이디로 조회
				.andIsAliveEqualTo(true) // 활성화됨
				.andPushIdIsNotNull() // 토큰 빈값 제외				
				.andPushIdNotEqualTo(""); // 토큰 빈값 제외;				
		List<UserDevice> devices = userDeviceMapper.selectByExample(example);

		// key: 로케일, value: (os별로 분류된 push id map)
		Map<String, Map<String, List<String>>> localizedPushIdMap = new HashMap<>();
		Map<String, List<String>> pushIdMap;

		final Locale defaultLocale = Locale.US;

		List<String> pushIds;
		for (UserDevice device : devices) {
			String locale = device.getLocale();

			// 언어 설정이 없으면 기본 언어 설정
			if (StringUtils.isEmpty(locale)) {
				locale = defaultLocale.toString();
			}

			pushIdMap = localizedPushIdMap.get(locale);
			if (pushIdMap == null) {
				pushIdMap = new HashMap<>();
				localizedPushIdMap.put(locale, pushIdMap);
			}

			String osAlias = codeUtil.getAliasByCode(device.getOs());
			pushIds = pushIdMap.get(osAlias);
			if (pushIds == null) {
				pushIds = new ArrayList<>();
				pushIdMap.put(osAlias, pushIds);
			}
			pushIds.add(device.getPushId());
		}

		Map<Locale, PushInfo> result = new HashMap<>(localizedPushIdMap.size());
		for (String locale : localizedPushIdMap.keySet()) {
			result.put(toLocale(locale, defaultLocale), new PushInfo(localizedPushIdMap.get(locale)));
		}
		localizedPushIdMap.clear();
		return result;
	}

	private Locale toLocale(String locale, Locale defaultLocale) {
		try {
			return LocaleUtils.toLocale(locale);
		} catch (Exception e) {
			return defaultLocale;
		}
	}

	private Object[] buildOrderStatusChangedMessageArgs(SvcOrder order, String messageTypeCode, Locale locale) {

		List<Object> args = new ArrayList<>();

		SvcStore store = svcStoreMapper.selectByPrimaryKey(order.getStoreId());

		if (MESSAGE_TP_ORDER_DISAPPROVAL.equals(messageTypeCode)) {
			// 매장명 
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_ORDER_APPOVAL.equals(messageTypeCode)) {
			// 매장명, 대기시간 (분)
			args.add(store.getStoreNm());
			args.add(toMinutes(order.getEstTm())); // 시분(HHmm)을 분으로 변경
			return toArray(args);
		}

		if (MESSAGE_TP_ORDER_READY.equals(messageTypeCode)) {
			// 매장명
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_ORDER_CANCEL.equals(messageTypeCode)) {
			// 매장명, 취소사유
			args.add(store.getStoreNm());
			args.add(getBaseCodeMessage(order.getCancelTp(), locale));
			return toArray(args);
		}

		if (MESSAGE_TP_RESERVATION_DISAPPROVAL.equals(messageTypeCode)) {
			// 매장명
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_RESERVATION_APPOVAL.equals(messageTypeCode)) {
			// 매장명 
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_RESERVATION_READY.equals(messageTypeCode)) {
			// 매장명 예약일 예약시간			
			args.add(store.getStoreNm());
			args.add(formatDay(order.getReserveTmLocal(), locale));
			args.add(formatTime(order.getReserveTmLocal(), locale));
			return toArray(args);
		}

		if (MESSAGE_TP_RESERVATION_COMPLETE.equals(messageTypeCode)) {
			// 매장명 
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_RESERVATION_CANCEL.equals(messageTypeCode)) {
			// 매장명 취소사유
			args.add(store.getStoreNm());
			args.add(getBaseCodeMessage(order.getCancelTp(), locale));
			return toArray(args);
		}

		if (MESSAGE_TP_CONTRACT_DISAPPROVAL.equals(messageTypeCode)) {
			// 매장명 
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_CONTRACT_APPOVAL.equals(messageTypeCode)) {
			// 매장명 
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_CONTRACT_READY.equals(messageTypeCode)) {
			// 매장명 예약일 예약시간
			args.add(store.getStoreNm());
			args.add(formatDay(order.getReserveTmLocal(), locale));
			args.add(formatTime(order.getReserveTmLocal(), locale));
			return toArray(args);
		}

		if (MESSAGE_TP_CONTRACT_COMPLETE.equals(messageTypeCode)) {
			// 매장명
			args.add(store.getStoreNm());
			return toArray(args);
		}

		if (MESSAGE_TP_CONTRACT_CANCEL.equals(messageTypeCode)) {
			// 매장명 취소사유
			args.add(store.getStoreNm());
			args.add(getBaseCodeMessage(order.getCancelTp(), locale));
			return toArray(args);
		}

		return new Object[0];
	}

	/**
	 * hhmm 시간을 분으로 변경
	 * 0110 -> 70
	 * 
	 * @param hhmm
	 * @return
	 */
	private String toMinutes(String hhmm) {
		if (hhmm == null || hhmm.length() != 4) {
			return "";
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(DATE_FORMAT_HHMM.parse(hhmm));
			return String.valueOf(cal.get(Calendar.MINUTE));

		} catch (Exception e) {
			return "";
		}
	}

	private String formatDay(Date date, Locale locale) {
		try {
			return DateFormat.formatDay(date, locale.getLanguage());
		} catch (Exception e) {
			return "";
		}
	}

	private String formatTime(Date date, Locale locale) {
		try {
			return DateFormat.formatTime(date, locale.getLanguage());
		} catch (Exception e) {
			return "";
		}
	}

	private String getBaseCodeMessage(String baseCode, Locale locale) {
		return messageSource.getMessage("basecode." + baseCode, null, "", locale);

	}

	private static Object[] toArray(List<Object> list) {
		Object[] result = new Object[list.size()];
		list.toArray(result);
		return result;
	}

	/**
	 * 주문 종류, 상태에 따라 보낼 메시지 타입을 선택한다.
	 * 처리할 상태가 아니면 null을 발환
	 * 
	 * @param order
	 * @return 메시지 타입, 처리할 상태가 아니면 null
	 */
	private String getMessageTypeCode(SvcOrder order) {

		final String orderTp = order.getOrderTp();
		final String orderSt = order.getOrderSt();
System.out.println("Appnotification:" + orderTp + " , " + orderSt + "," + order.getOrderNo() );
		// 주문
		if (ORDER_TP_ORDER.equals(orderTp)) {

			if (ORDER_ST_DISAPPROVAL.equals(orderSt)) {
				return MESSAGE_TP_ORDER_DISAPPROVAL;

			} else if (ORDER_ST_APPOVAL.equals(orderSt)) {
				return MESSAGE_TP_ORDER_APPOVAL;

				/*if (hasTableNo(order)) {
					// 테이블 번호가 할당되어 있으면 상품 준비 완료 상태
					return MESSAGE_TP_ORDER_READY;
				} else {
					return MESSAGE_TP_ORDER_APPOVAL;
				}*/

			} else if (ORDER_ST_COMPLETE.equals(orderSt)) {
				//self-order (주문완료,매출정보가 있을 시 push발송.)
				if(order.getIsSales()) {
					return MESSAGE_TP_ORDER_READY;
				}
			} else if (ORDER_ST_CANCEL.equals(orderSt) || ORDER_ST_REJECT.equals(orderSt)) {
				return MESSAGE_TP_ORDER_CANCEL;
			}

			return null;
		}

		// 예약
		if (ORDER_TP_RESERVATION.equals(orderTp)) {
			if (ORDER_ST_DISAPPROVAL.equals(orderSt)) {
				return MESSAGE_TP_RESERVATION_DISAPPROVAL;

			} else if (ORDER_ST_APPOVAL.equals(orderSt)) {

				if (hasTableNo(order)) {
					// 테이블 번호가 할당되어 있으면 상품 준비 완료 상태
					return MESSAGE_TP_RESERVATION_READY;
				} else {
					return MESSAGE_TP_RESERVATION_APPOVAL;
				}

			} else if (ORDER_ST_CANCEL.equals(orderSt) || ORDER_ST_REJECT.equals(orderSt)) {
				return MESSAGE_TP_RESERVATION_CANCEL;

			} else if (ORDER_ST_COMPLETE.equals(orderSt)) {
				return MESSAGE_TP_RESERVATION_COMPLETE;
			}
			return null;
		}

		// 계약
		if (ORDER_TP_CONTRACT.equals(orderTp)) {
			if (ORDER_ST_DISAPPROVAL.equals(orderSt)) {
				return MESSAGE_TP_CONTRACT_DISAPPROVAL;

			} else if (ORDER_ST_APPOVAL.equals(orderSt)) {

				if (hasTableNo(order)) {
					// 테이블 번호가 할당되어 있으면 상품 준비 완료 상태
					return MESSAGE_TP_CONTRACT_READY;
				} else {
					return MESSAGE_TP_CONTRACT_APPOVAL;
				}

			} else if (ORDER_ST_CANCEL.equals(orderSt) || ORDER_ST_REJECT.equals(orderSt)) {
				return MESSAGE_TP_CONTRACT_CANCEL;

			} else if (ORDER_ST_COMPLETE.equals(orderSt)) {
				return MESSAGE_TP_CONTRACT_COMPLETE;
			}
			return null;
		}

		return null;
	}

	private boolean hasTableNo(SvcOrder order) {
		return !StringUtils.isEmpty(order.getTableNo()) && !StringUtils.equals(order.getTableNo(), "0");
	}

	@Override
	public void notifyStampCanceled(Long storeId, Long userId, int stampCount) {

		final String messageTypeCode = MESSAGE_TP_STAMP_CANCEL;
		final String messageTypeAlias = codeUtil.getAliasByCode(messageTypeCode);

		sendStampRewardPush(storeId, userId, stampCount, messageTypeCode, messageTypeAlias);
	}

	@Override
	public void notifyStampSaved(Long storeId, Long userId, int stampCount) {

		final String messageTypeCode = MESSAGE_TP_STAMP_SAVING;
		final String messageTypeAlias = codeUtil.getAliasByCode(messageTypeCode);

		sendStampRewardPush(storeId, userId, stampCount, messageTypeCode, messageTypeAlias);
	}

	@Override
	public void notifyCouponIssued(Long storeId, Long userId, List<Long> userCouponIds) {

		final String messageTypeCode = MESSAGE_TP_COUPON_ISSUE;
		final String messageTypeAlias = codeUtil.getAliasByCode(messageTypeCode);

		for (Long userCouponId : userCouponIds) {
			sendCouponRewardPush(storeId, userId, userCouponId, messageTypeCode, messageTypeAlias);
		}
	}

	@Override
	public void notifyCouponIssueCanceled(Long storeId, Long userId, List<Long> userCouponIds) {
		final String messageTypeCode = MESSAGE_TP_COUPON_ISSUE_CANCEL;
		final String messageTypeAlias = codeUtil.getAliasByCode(messageTypeCode);

		for (Long userCouponId : userCouponIds) {
			sendCouponRewardPush(storeId, userId, userCouponId, messageTypeCode, messageTypeAlias);
		}
	}

	@Override
	public void notifyCouponUsed(Long storeId, Long userId, Long userCouponId) {
		final String messageTypeCode = MESSAGE_TP_COUPON_USE;
		final String messageTypeAlias = codeUtil.getAliasByCode(messageTypeCode);

		sendCouponRewardPush(storeId, userId, userCouponId, messageTypeCode, messageTypeAlias);
	}

	/**
	 * 스탬프 발급/취소 알림
	 * 
	 * @param storeId
	 * @param userId
	 * @param stampCount
	 * @param messageTypeCode
	 * @param messageTypeAlias
	 * @param locale
	 */
	private void sendStampRewardPush(Long storeId, Long userId, int stampCount, final String messageTypeCode, final String messageTypeAlias) {

		// 푸시 토큰 조회 (locale에 따라 분류)
		Map<Locale, PushInfo> pushInfoMap = getPushInfoByUserId(userId);

		// 로케일에 맞게 메시지 생성
		SvcStore store = svcStoreMapper.selectByPrimaryKey(storeId);
		Object[] args = new Object[] { store.getStoreNm(), stampCount };
		for (Locale locale : pushInfoMap.keySet()) {
			PushInfo info = pushInfoMap.get(locale);

			// message_ko_KR.propeties 파일 참조 
			String title = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + messageTypeAlias + MSG_CODE_POSTFIX_TITLE, null, null, locale);
			String message = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + messageTypeAlias + MSG_CODE_POSTFIX_MESSAGE, args, null, locale);

			if (title == null || message == null) {
				logger.warn("Ignored notify stamp canceled. Cause by Can not build title or message from message type. messageTp={}, locale={}",
						messageTypeCode, locale);
				continue;
			}
			info.title = message; // title; FIXME 미기획/미번역 관계로 제목은 메시지로 대체
			info.message = message;
		}

		// 전송
		for (PushInfo info : pushInfoMap.values()) {
			SendPush push = new SendPush();
			push.setPushIds(info.pushTokens);
			push.setTitle(info.title);
			push.setContent(info.message);
			push.setType(messageTypeAlias);
			push.setBrandId(store.getBrandId());

			push.addExtra("storeId", storeId);

			pushSender.send(false, push);

			logger.info("Sent notify stamp reward changed userId={}, msgTpAlias={}, msg=[{}]", userId, messageTypeAlias, info.message);
		}

	}

	private void sendCouponRewardPush(Long storeId, Long userId, Long userCouponId, final String messageTypeCode, final String messageTypeAlias) {

		SvcUserCoupon userCoupon = svcUserCouponMapper.selectByPrimaryKey(userCouponId);
		if (userCoupon == null) {
			logger.warn("Ignored notify coupon changed. Cause by Not found user coupon. userCouponId= " + userCouponId);
			return;
		}

		SvcCoupon coupon = svcCouponMapper.selectByPrimaryKey(userCoupon.getCouponId());
		if (coupon == null) {
			logger.warn("Ignored notify coupon changed. Cause by Not found coupon. couponId= " + userCoupon.getCouponId());
			return;
		}

		SvcStore store = svcStoreMapper.selectByPrimaryKey(storeId);
		if (store == null) {
			logger.warn("Ignored notify coupon changed. Cause by Not found store. storeId= " + storeId);
			return;
		}

		// 푸시 토큰 조회 (locale에 따라 분류)
		Map<Locale, PushInfo> pushInfoMap = getPushInfoByUserId(userId);

		// 로케일에 맞게 메시지 생성		
		Object[] args = new Object[] { coupon.getCouponNm(), store.getStoreNm() };
		for (Locale locale : pushInfoMap.keySet()) {
			PushInfo info = pushInfoMap.get(locale);

			// message_ko_KR.propeties 파일 참조 
			String title = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + messageTypeAlias + MSG_CODE_POSTFIX_TITLE, null, null, locale);
			String message = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + messageTypeAlias + MSG_CODE_POSTFIX_MESSAGE, args, null, locale);

			if (title == null || message == null) {
				logger.warn("Ignored notify coupon changed. Cause by Can not build title or message from message type. messageTp={}, locale={}",
						messageTypeCode, locale);
				continue;
			}
			info.title = message; // title; FIXME 미기획/미번역 관계로 제목은 메시지로 대체
			info.message = message;
		}

		for (PushInfo info : pushInfoMap.values()) {
			SendPush push = new SendPush();
			push.setPushIds(info.pushTokens);
			push.setTitle(info.title);
			push.setContent(info.message);
			push.setType(messageTypeAlias);
			push.setBrandId(store.getBrandId());

			push.addExtra("storeId", storeId);

			pushSender.send(false, push);

			logger.info("Sent notify coupon reward changed userId={}, couponId={}, msgTpAlias={}, msg=[{}]" //
					, userId, userCoupon, messageTypeAlias, info.message);
		}
	}

}
