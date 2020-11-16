package com.jc.pico.service.store.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcCctvLog;
import com.jc.pico.bean.SvcCctvLogDeviceMapping;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcSalesItem;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcCctvLogDeviceMappingMapper;
import com.jc.pico.mapper.SvcCctvLogMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.queue.PushSender;
import com.jc.pico.service.store.StoreCommonService;
import com.jc.pico.service.store.StoreNotificationService;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.NumberFormat;
import com.jc.pico.utils.bean.SendPush;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.bean.SvcSalesExtended;
import com.jc.pico.utils.customMapper.store.StoreNotifyMapper;

@Service
public class StoreNotificationServiceImpl implements StoreNotificationService {

	private static Logger logger = LoggerFactory.getLogger(StoreNotificationServiceImpl.class);

	/**
	 * 주문 상태 취소
	 */
	public static final String ORDER_ST_CANCEL = "607003";

	/**
	 * 주문 상태 거부
	 */
	public static final String ORDER_ST_REJECT = "607004";

	/**
	 * 매출 상태 : 정상
	 */
	public static final String SALES_ST_NORMAL = "809001";

	/**
	 * 매출 상태 : 반품
	 */
	public static final String SALES_ST_REFUND = "809002";

	/**
	 * 이벤트 종류 : 주문 취소
	 */
	public static final String EVENT_TP_ORDER_CANCEL = "701001";

	/**
	 * 알림 이벤트 종류 : 결제
	 */
	public static final String EVENT_TP_PAYMENT = "701002";

	/**
	 * 알림 이벤트 종류 : 반품
	 */
	public static final String EVENT_TP_REFUND = "701004";

	/**
	 * 이벤트 종류 : VIP 방문
	 */
	public static final String EVENT_TP_VISIT_VIP = "701005";

	/**
	 * OS 종류 : 안드로이드
	 */
	public static final String OS_TP_ANDROID = "103001";

	/**
	 * OS 종류 : ios
	 */
	public static final String OS_TP_IOS = "103002";

	/**
	 * 주문 경로 : 앱
	 */
	public static final String ORDER_PATH_APP = "606002";

	public static final String MSG_CODE_PREFIX_PUSH = "store.push.";
	public static final String MSG_CODE_POSTFIX_TITLE = ".title";
	public static final String MSG_CODE_POSTFIX_MESSAGE = ".msg";
	public static final String MSG_CODE_POSTFIX_MESSAGE_SINGULAR = ".msg.single";
	public static final String MSG_CODE_PREPIX_BASE_CODE_ALIAS = "basecode.alias.";

	@Autowired
	private StoreNotifyMapper notifyMapper;

	@Autowired
	private SvcCctvLogMapper svcCctvLogMapper;

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SvcCctvLogDeviceMappingMapper svcCctvLogDeviceMappingMapper;

	@Autowired
	private StoreCommonService commonService;

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
		public final Map<String, List<String>> pushIds = new HashMap<String, List<String>>();
		public final List<Long> deviceIds = new ArrayList<>();
	}

	@Override
	public SingleMap getNotifyList(SingleMap param) {

		if (!param.hasValue("startId")) {
			throw new InvalidParameterException("startId is empty");
		}

		if (!param.hasValue("count")) {
			throw new InvalidParameterException("count is empty");
		}

		if (!param.hasValue("brandId")) {
			throw new InvalidParameterException("brandId is empty");
		}

		if (!param.hasValue("deviceId")) {
			throw new InvalidParameterException("deviceId is empty");
		}

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();
		result.put("items", notifyMapper.selectNotifyList(param));
		result.putAll(getNotifyUnreadCount(param)); // unreadCount 추가
		return result;
	}

	@Override
	public SingleMap setNotifyReadMark(SingleMap param) {
		if (!param.hasValue("brandId")) {
			throw new InvalidParameterException("brandId is empty");
		}

		if (!param.hasValue("deviceId")) {
			throw new InvalidParameterException("deviceId is empty");
		}

		param.put("isRead", true);
		int count = notifyMapper.updateNotifyRead(param);

		SingleMap result = new SingleMap();
		result.put("readCount", count);
		return result;
	}

	@Override
	public SingleMap setNotifyDeleteMark(SingleMap param) {

		if (!param.hasValue("brandId")) {
			throw new InvalidParameterException("brandId is empty");
		}

		if (!param.hasValue("deviceId")) {
			throw new InvalidParameterException("deviceId is empty");
		}

		param.put("isDel", true);
		int count = notifyMapper.updateNotifyDelete(param);

		SingleMap result = new SingleMap();
		result.put("deleteCount", count);
		return result;
	}

	@Override
	public SingleMap getNotifyUnreadCount(SingleMap param) {

		if (!param.hasValue("brandId")) {
			throw new InvalidParameterException("brandId is empty");
		}

		if (!param.hasValue("deviceId")) {
			throw new InvalidParameterException("deviceId is empty");
		}

		int count = notifyMapper.selectNotiftyUnreadCount(param);

		SingleMap result = new SingleMap();
		result.put("unreadCount", count);
		return result;
	}

	@Override
	public SvcCctvLog addSalesNotifyLog(SvcSalesExtended sales, String eventTp, List<Long> deviceIds) {
		SvcCctvLog record = new SvcCctvLog();
		record.setBrandId(sales.getBrandId());
		record.setStoreId(sales.getStoreId());
		record.setSalesId(sales.getId());
		record.setOrderId(sales.getOrderId());
		record.setEventTm(sales.getSalesTm());
		record.setEventTmLocal(sales.getSalesTmLocal());
		record.setEventTp(eventTp);
		svcCctvLogMapper.insertSelective(record);

		// 조회 디바이스 설정
		addNotifyDeviceMapping(deviceIds, record.getId());

		return record;
	}

	/**
	 * 해당 로그를 조회 할 수 있는 디바이스 기록
	 * 
	 * @param deviceIds
	 * @param logId
	 */
	private void addNotifyDeviceMapping(List<Long> deviceIds, Long logId) {
		SvcCctvLogDeviceMapping mappingRecord = new SvcCctvLogDeviceMapping();
		mappingRecord.setIsDel(false);
		mappingRecord.setIsRead(false);
		mappingRecord.setLogId(logId);
		for (Long deviceId : deviceIds) {
			mappingRecord.setDeviceId(deviceId);
			svcCctvLogDeviceMappingMapper.insertSelective(mappingRecord);
		}
	}

	@Override
	public void notifyOrderChanged(SvcOrder newOrder, SvcOrderExtended oldOrder) {

		// 취소 상태를 스토어 매니저에게 알림
		// 2016.11.04 주문 취소 알림 보내지 않도록 변경
//		if (ORDER_ST_CANCEL.equals(newOrder.getOrderSt()) || ORDER_ST_REJECT.equals(newOrder.getOrderSt())) {
//			sendOrderPushToStoreManager(newOrder, oldOrder, EVENT_TP_ORDER_CANCEL);
//		}

	}

	@Override
	public void notifySalesChanged(SvcSalesExtended sales) {

		if (SALES_ST_NORMAL.equals(sales.getSalesSt())) { // 신규 매출 발생
			sendSalesPushToStoreManager(sales, EVENT_TP_PAYMENT);

		} else if (SALES_ST_REFUND.equals(sales.getSalesSt())) { // 매출 취소 발생
			sendSalesPushToStoreManager(sales, EVENT_TP_REFUND);

		} else {
			// 외에 발송하지 않음
			return;
		}

	}

	@Override
	public void notifyVisitVip(SvcStore store, User user, Locale locale) {

		if (store == null || user == null) {
			return;
		}

		// 푸시 토큰 조회 (locale에 따라 분류)
		Map<Locale, PushInfo> pushInfos = getPushInfoByStoreId(store.getId(), EVENT_TP_VISIT_VIP);

		// 로케일에 맞게 메시지 생성		
		final String eventTpAlias = codeUtil.getAliasByCode(EVENT_TP_VISIT_VIP);
		buildVisitVipMessage(pushInfos, store, user);

		// 로그 먼저 기록
		List<Long> deviceIds = new ArrayList<>();
		for (PushInfo info : pushInfos.values()) {
			deviceIds.addAll(info.deviceIds);
		}
		SvcCctvLog ccgvLog = addVipVisitNotifyLog(store, user, deviceIds);

		// 발송
		for (PushInfo info : pushInfos.values()) {
		  String name = "";
		  if (GenericValidator.isBlankOrNull(user.getName())) {
		    name =messageSource.getMessage("store.push.event-type-visit-vip", new String[]{}, locale);
		  } else {
		    name = user.getName();
		  }
		  
			SendPush push = new SendPush();
			push.setPushIds(info.pushIds);
			push.setTitle(messageSource.getMessage("store.push.event-type-visit-vip.title", new String[]{}, locale));
			push.setContent(messageSource.getMessage("store.push.event-type-visit-vip.msg", new String[]{name}, locale));
			push.setType(eventTpAlias);
			push.setBrandId(store.getBrandId());

			push.addExtra("userId", user.getId());
			push.addExtra("userNm", name);
			push.addExtra("eventTm", ccgvLog.getEventTm());

			pushSender.send(true, push);

			// 로그 기록용
			deviceIds.addAll(info.deviceIds);

			logger.debug("Sent notify sales changed to store manager. msg=[{}]", info.message);
		}
		logger.debug("Sent device ids " + deviceIds);
	}

	/**
	 * 해당 이벤트 메시지를 수신 하도록 허용한 사용자에게 알림을 보낸다.
	 * 
	 * @param sales
	 * @param eventTp
	 * @return
	 */
	private List<Long> sendSalesPushToStoreManager(SvcSalesExtended sales, String eventTp) {

		SvcStore store = svcStoreMapper.selectByPrimaryKey(sales.getStoreId());

		if (store == null) {
			return Collections.emptyList();
		}

		// 푸시 토큰 조회 (locale에 따라 분류)
		Map<Locale, PushInfo> pushInfos = getPushInfoByStoreId(store.getId(), eventTp);

		// 로케일에 맞게 메시지 생성		
		final String eventTpAlias = codeUtil.getAliasByCode(eventTp);
		buildSaleItemPushMessage(pushInfos, sales, eventTp, eventTpAlias);

		// 로그 먼저 기록
		List<Long> deviceIds = new ArrayList<>();
		for (PushInfo info : pushInfos.values()) {
			deviceIds.addAll(info.deviceIds);
		}
		SvcCctvLog ccgvLog = addSalesNotifyLog(sales, eventTp, deviceIds);

		// 발송
		for (PushInfo info : pushInfos.values()) {
			SendPush push = new SendPush();
			push.setPushIds(info.pushIds);
			push.setTitle(info.title);
			push.setContent(info.message);
			push.setType(eventTpAlias);
			push.setBrandId(store.getBrandId());

			push.addExtra("storeNm", store.getStoreNm());
			push.addExtra("eventTm", ccgvLog.getEventTm());

			pushSender.send(true, push);

			// 로그 기록용
			deviceIds.addAll(info.deviceIds);

			logger.debug("Sent notify sales changed to store manager. msg=[{}]", info.message);
		}
		logger.debug("Sent device ids " + deviceIds);
		return deviceIds;
	}

	private void buildSaleItemPushMessage(Map<Locale, PushInfo> pushInfos, SvcSalesExtended sales, String eventTp, String eventTpAlias) {
		int totalCount = 0;
		String firstItemName = null;
		for (SvcSalesItem item : sales.getSvcSalesItems()) {
			totalCount += item.getCount();
			if (firstItemName == null) {
				firstItemName = item.getItemNm();
			}
		}
		if(firstItemName == null) {
			firstItemName = "";
		}

		final String currency = getCurrency(sales.getBrandId());
		final String totalSales = formatCurrency(sales.getSales(), currency);

		final boolean isSingular = totalCount < 2;

		final String messageCode = MSG_CODE_PREFIX_PUSH + "sales-changed"
				+ (isSingular ? MSG_CODE_POSTFIX_MESSAGE_SINGULAR : MSG_CODE_POSTFIX_MESSAGE);

		for (Locale locale : pushInfos.keySet()) {
			PushInfo info = pushInfos.get(locale);

			final String eventTpName = getBaseCodeAliasMessage(eventTpAlias, locale);
			final Object[] messageArgs;
			if (isSingular) {
				messageArgs = new Object[] { eventTpName, firstItemName, totalSales, currency };
			} else {
				messageArgs = new Object[] { eventTpName, firstItemName, (totalCount - 1), totalSales, currency };
			}

			// message_ko_KR.propeties 파일 참조
			String title = eventTpName;
			String message = messageSource.getMessage(messageCode, messageArgs, null, locale);

			if (title == null || message == null) {
				logger.warn("Ignored notifiy sales status changed. Cause by Can not build title or message. eventTp={}, locale={}", eventTp, locale);
				continue;
			}
			info.title = title;
			info.message = message;
		}

	}

	private void buildOrderItemPushMessage(Map<Locale, PushInfo> pushInfos, SvcOrderExtended order, String eventTp, String eventTpAlias) {

		int totalCount = 0;
		String firstItemName = null;
		for (SvcOrderItem item : order.getSvcOrderItems()) {
			totalCount += item.getCount();
			if (firstItemName == null) {
				firstItemName = item.getItemNm();
			}
		}
		if(firstItemName == null) {
			firstItemName = "";
		}

		final String currency = getCurrency(order.getBrandId());
		final String totalSales = formatCurrency(order.getSales(), currency);

		final boolean isSingular = totalCount < 2;

		final String messageCode = MSG_CODE_PREFIX_PUSH + "order-changed"
				+ (isSingular ? MSG_CODE_POSTFIX_MESSAGE_SINGULAR : MSG_CODE_POSTFIX_MESSAGE);

		for (Locale locale : pushInfos.keySet()) {
			PushInfo info = pushInfos.get(locale);

			final String eventTpName = getBaseCodeAliasMessage(eventTpAlias, locale);
			final Object[] messageArgs;
			if (isSingular) {
				messageArgs = new Object[] { eventTpName, firstItemName, totalSales, currency };
			} else {
				messageArgs = new Object[] { eventTpName, firstItemName, (totalCount - 1), totalSales, currency };
			}

			// message_ko_KR.propeties 파일 참조
			String title = eventTpName;
			String message = messageSource.getMessage(messageCode, messageArgs, null, locale);

			if (title == null || message == null) {
				logger.warn("Ignored notifiy order status changed. Cause by Can not build title or message. eventTpAlias={}, locale={}", eventTpAlias,
						locale);
				continue;
			}
			info.title = title;
			info.message = message;
		}

	}

	private String getCurrency(long brandId) {
		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(brandId);
		return brand.getCurrency();
	}

	/**
	 * 금액 포맷팅
	 * 
	 * @param value
	 * @param currency
	 * @return
	 */
	private String formatCurrency(double value, String currency) {
		try {
			if ("RUB".equals(currency)) {
				return NumberFormat.formatPrice2(String.valueOf(value), "ru");
			}
			if ("KRW".equals(currency)) {
				return NumberFormat.formatPrice(String.valueOf(value), "ko");
			}
			if ("USD".equals(currency)) {
				return NumberFormat.formatPrice2(String.valueOf(value), "en");
			}
		} catch (Exception e) {
			logger.warn("Failed currency formatting.", e);
		}
		return String.valueOf(value);
	}

	/**
	 * 
	 * @param order
	 * @param eventTpOrderCancel
	 * @return
	 */
	@SuppressWarnings("unused") // 주문 취소 알림 보내지 않기로 했지만 남겨둠.
	private void sendOrderPushToStoreManager(SvcOrder newOrder, SvcOrderExtended oldOrder, String eventTp) {

		SvcStore store = svcStoreMapper.selectByPrimaryKey(newOrder.getStoreId());

		if (store == null) {
			return;
		}

		// 푸시 토큰 조회 (locale에 따라 분류)
		Map<Locale, PushInfo> pushInfos = getPushInfoByStoreId(store.getId(), eventTp);

		// 로케일에 맞게 메시지 생성		
		final String eventTpAlias = codeUtil.getAliasByCode(eventTp);
		buildOrderItemPushMessage(pushInfos, oldOrder, eventTp, eventTpAlias); // 이전 주문으로 부터 생성

		// 로그 먼저 기록
		List<Long> deviceIds = new ArrayList<>();
		for (PushInfo info : pushInfos.values()) {
			deviceIds.addAll(info.deviceIds);
		}
		SvcCctvLog ccgvLog = addOrderCancelNotify(newOrder, deviceIds);

		// 발송
		for (PushInfo info : pushInfos.values()) {
			SendPush push = new SendPush();
			push.setPushIds(info.pushIds);
			push.setTitle(info.title);
			push.setContent(info.message);
			push.setType(eventTpAlias);
			push.setBrandId(store.getBrandId());

			push.addExtra("storeNm", store.getStoreNm());
			push.addExtra("eventTm", ccgvLog.getEventTm());

			pushSender.send(true, push);

			// 로그 기록용
			deviceIds.addAll(info.deviceIds);

			logger.debug("Sent notify order changed to store manager. eventTpAlias={} msg=[{}]", eventTpAlias, info.message);
		}

		logger.debug("Sent device ids " + deviceIds);
	}

	private SvcCctvLog addOrderCancelNotify(SvcOrder order, List<Long> deviceIds) {

		Date eventTm = null;
		Date eventTmLocal = null;
		if (ORDER_ST_CANCEL.equals(order.getOrderSt())) {
			eventTm = order.getCancelTm();
			eventTmLocal = order.getCancelTmLocal();
		} else if (ORDER_ST_REJECT.equals(order.getOrderSt())) {
			eventTm = order.getRejectTm();
			eventTmLocal = order.getRejectTmLocal();
		}

		SvcCctvLog record = new SvcCctvLog();
		record.setBrandId(order.getBrandId());
		record.setStoreId(order.getStoreId());
		record.setOrderId(order.getId());
		record.setEventTp(EVENT_TP_ORDER_CANCEL);
		record.setEventTm(eventTm);
		record.setEventTmLocal(eventTmLocal);

		svcCctvLogMapper.insertSelective(record);

		addNotifyDeviceMapping(deviceIds, record.getId());

		return record;
	}

	/**
	 * 로케일별로 분류한 푸시 토큰 정보 조회
	 * 
	 * @param userId
	 * @return
	 */
	private Map<Locale, PushInfo> getPushInfoByStoreId(Long storeId, String eventTp) {

		final Locale defaultLocale = Locale.US;

		SingleMap params = new SingleMap();
		params.put("eventTp", eventTp);
		params.put("storeId", storeId);

		List<UserDevice> devices = notifyMapper.selectPushUserDeviceListByEventTp(params);

		// key: 로케일, value: (os별로 분류된 push id map)
		Map<String, PushInfo> localizedPushInfos = new HashMap<>();
		for (UserDevice device : devices) {
			String locale = device.getLocale();

			// 언어 설정이 없으면 기본 언어 설정
			if (StringUtils.isEmpty(locale)) {
				locale = defaultLocale.toString();
			}

			PushInfo info = localizedPushInfos.get(locale);
			if (info == null) {
				info = new PushInfo();
				localizedPushInfos.put(locale, info);
			}

			String osAlias = codeUtil.getAliasByCode(device.getOs());
			List<String> pushIds = info.pushIds.get(osAlias);
			if (pushIds == null) {
				pushIds = new ArrayList<>();
				info.pushIds.put(osAlias, pushIds);
			}

			info.deviceIds.add(device.getId());

			pushIds.add(device.getPushId());
		}

		Map<Locale, PushInfo> result = new HashMap<>(localizedPushInfos.size());
		for (String locale : localizedPushInfos.keySet()) {
			result.put(toLocale(locale, defaultLocale), localizedPushInfos.get(locale));
		}
		localizedPushInfos.clear();
		return result;
	}

	private Locale toLocale(String locale, Locale defaultLocale) {
		try {
			return LocaleUtils.toLocale(locale);
		} catch (Exception e) {
			return defaultLocale;
		}
	}

	private String getBaseCodeAliasMessage(String baseCodeAlias, Locale locale) {
		return messageSource.getMessage("basecode.alias." + baseCodeAlias, null, "", locale);
	}

	/**
	 * VIP 방문 메시지 생성
	 * 
	 * @param pushInfos
	 * @param store
	 * @param user
	 */
	private void buildVisitVipMessage(Map<Locale, PushInfo> pushInfos, SvcStore store, User user) {

		final String eventTp = EVENT_TP_VISIT_VIP;
		final String eventTpAlias = codeUtil.getAliasByCode(eventTp);

		final Object[] messageArgs = new Object[] { user.getName() };

		for (Locale locale : pushInfos.keySet()) {

			// message_ko_KR.propeties 파일 참조
			String title = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + eventTpAlias + MSG_CODE_POSTFIX_TITLE, null, null, locale);
			String message = messageSource.getMessage(MSG_CODE_PREFIX_PUSH + eventTpAlias + MSG_CODE_POSTFIX_MESSAGE, messageArgs, null, locale);

			if (title == null || message == null) {
				logger.warn("Ignored notify visit vip. Cause by Can not build title or message. eventTp={}, locale={}", eventTp, locale);
				continue;
			}

			PushInfo info = pushInfos.get(locale);
			info.title = title;
			info.message = message;
		}
	}

	private SvcCctvLog addVipVisitNotifyLog(SvcStore store, User user, List<Long> deviceIds) {
		SvcCctvLog record = new SvcCctvLog();
		record.setBrandId(store.getBrandId());
		record.setStoreId(store.getId());
		record.setEventTm(new Date());
		record.setEventTmLocal(new Date());
		record.setEventTp(EVENT_TP_VISIT_VIP);
		record.setUserId(user.getId());

		svcCctvLogMapper.insertSelective(record);

		addNotifyDeviceMapping(deviceIds, record.getId());

		return record;
	}

}
