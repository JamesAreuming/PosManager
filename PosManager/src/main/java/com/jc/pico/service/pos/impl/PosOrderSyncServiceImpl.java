package com.jc.pico.service.pos.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcClosingExample;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderExample;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.User;
import com.jc.pico.mapper.SvcClosingMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.service.pos.PosAsyncService;
import com.jc.pico.service.pos.PosEtcService;
import com.jc.pico.service.pos.PosOrderSyncService;
import com.jc.pico.utils.MQTTClient;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.customMapper.pos.PosStoreMapper;

@Service
public class PosOrderSyncServiceImpl implements PosOrderSyncService {

	protected static Logger logger = LoggerFactory.getLogger(PosOrderSyncServiceImpl.class);

	private static final RowBounds ROW_BOUNDS_JUST_FIRST = new RowBounds(0, 1);

	@Autowired
	private PosAsyncService posAsyncService;

	@Autowired
	private PosStoreMapper posStoreMapper;

	@Autowired
	private SvcClosingMapper svcClosingMapper;

	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private PosEtcService posEtcService;

	@Autowired
	private AppNotificationService appNotificationService;

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PosUtil posUtil;

	/**
	 * 매장단위로 미승인 주문건을 조회하여 MQTT로 전달한다
	 * 마지막 개점일을 포함한 미래 날짜만 조회하여 전송 한다.
	 */
	@Override
	public void sendNoConfirmOrder() {
		// 수정 : 
		// 2020.01.08
		// 모두 주석 처리 : 키오스크는 확인 필요 없음

		/*
		// 매장단위로 별도 쓰레드를 생성하여 전송 처리
		List<Long> storeIds = posStoreMapper.selectStoreIdListByActivated();
		for (final long storeId : storeIds) {
			posAsyncService.executeOnOrderSyncTask(new Runnable() {

				@Override
				public void run() {
					// 싱크 처리 쓰레드에서 호출 됨
					sendNoConfirmOrder(storeId);
				}

			});
		}
		*/
		
	}

	/**
	 * 상점으로 동기화되지 않은 주문 정보를 mqtt로 전달 한다
	 * 
	 * @param storeId
	 */
	private void sendNoConfirmOrder(long storeId) {

		// 수정 : 
		// 2020.01.08
		// 모두 주석 처리 : 키오스크는 확인 필요 없음
		
		/*
		// 마지막 개점일이 너무 오래됐으면 싱크 동작하지 않도록 함
		if (!posEtcService.isAliveStore(storeId)) {
			logger.info("Ignore send no confirm order on mqtt. Store is not alive. storeId={}", storeId);
			return;
		}

		Date openDt = getLatestOpenDtByStoreId(storeId);
		if (openDt == null) {
			logger.info("Ignore send no confirm order on mqtt. Store is not opened. storeId={}", storeId);
			return;
		}

		// 마지막 개점일을 포함한 이후 개점날짜로 들어온 주문 중에 포스에서 가져가지 않은 주문 조회
		List<SvcOrder> orders = getNotConfirmOrderList(storeId, openDt);

		logger.info("Start send no confirm order on mqtt. storeId={}, orderCount={}, openDt={}", storeId, orders.size(), posUtil.formatDate(openDt));


		for (SvcOrder order : orders) {
			MQTTClient.sendOrder(storeId, order, order.getPathTp(), order.getPosNo());
			logger.debug("Sent no confirm order on mqtt. storeId={}, orderId={}", storeId, order.getId());
		}
		*/
		
	}

	/**
	 * 상점의 동기화 되지 않은 주문 정보를 조회한다.
	 * 개점일을 포함한 이후 개점일 주문만 조회한다. (오래된 주문은 동기화할 필요 없음)
	 * 
	 * @param storeId
	 * @param openDt
	 *            개점일
	 * @return 동기화가 필요한 주문 목록
	 */
	private List<SvcOrder> getNotConfirmOrderList(long storeId, Date openDt) {
		SvcOrderExample example = new SvcOrderExample();
		example.createCriteria() // 검색 조건
				.andStoreIdEqualTo(storeId) // 해당 매장
				.andOpenDtGreaterThanOrEqualTo(openDt) // 가장 마지막 개점일 포함 이후~
				.andIsConfirmEqualTo(false); // 싱크되지 않음
		return svcOrderMapper.selectByExample(example);
	}

	/**
	 * 상점의 마지막 개점일을 조회한다.
	 * 마지막 개점일이 마감 상태면 null을 반환하여 개점일 없는 것으로 간주 한다
	 * 
	 * @param storeId
	 * @return 개점일
	 */
	private Date getLatestOpenDtByStoreId(long storeId) {

		SvcClosingExample example = new SvcClosingExample();
		example.createCriteria() // 조건
				.andStoreIdEqualTo(storeId);
		example.setOrderByClause("OPEN_DT DESC"); // 최근 순서

		List<SvcClosing> result = svcClosingMapper.selectByExampleWithRowbounds(example, ROW_BOUNDS_JUST_FIRST);
		if (result.isEmpty()) {
			return null;
		}
		SvcClosing closing = result.get(0);

		if (closing.getIsClosing()) {
			// 마감 상태면 전송하지 않음
			return null;
		}
		return closing.getOpenDt();
	}

	/**
	 * 앱에서 들어온 장기 미승인 주문을 거절 처리함
	 */
	@Override
	public void cancelLongTermUnapprovedAppOrder() {

		// 스케쥴러 테스크에서 호출되며 스케줄러의 반응성을 높이기 위해
		// 주문 싱크 처리 쓰레드에서 처리
		posAsyncService.executeOnOrderSyncTask(new Runnable() {

			@Override
			public void run() {
				cancelLongTermUnapprovedAppOrderInOrderSyncTask();
			}

		});

	}

	/**
	 * 앱에서 들어온 장기 미승인 주문을 거절 처리함
	 */
	private void cancelLongTermUnapprovedAppOrderInOrderSyncTask() {

		final Calendar cal = Calendar.getInstance();
		final Date nowUtc = new Date(cal.getTimeInMillis() - cal.getTimeZone().getRawOffset());

		cal.setTime(nowUtc);
		cal.add(Calendar.HOUR_OF_DAY, -24); // 24시간 전		
		final Date expired = cal.getTime();

		// 취소 대상 조회
		// 앱으로 부터 발생한 장기 미승은 주문 건
		SvcOrderExample example = new SvcOrderExample();
		example.createCriteria() // 조회 대상
				.andPathTpEqualTo("606002") // 앱 주문
				.andOrderStEqualTo("607001") // 미승인
				.andOrderTmLessThan(expired); // 주문시간이 24시간 초과
		final List<SvcOrder> orders = svcOrderMapper.selectByExample(example);

		logger.info("Long term unapproved app order count {}. Set reject.", orders.size());

		// storeId, timezone offset
		Map<Long, Integer> localTimeOffsetMap = new HashMap<>();

		// 주문 거부 상태로 변경

		SvcOrder record = new SvcOrder();
		record.setOrderSt("607004"); // 거부 (승인 거절)			
		record.setCancelTp("827003"); // 장시간 대기
		record.setCancelTm(nowUtc);

		Integer localTimeOffset;
		for (SvcOrder order : orders) {

			record.setId(order.getId());

			// 취소 로컬 타입 설정
			localTimeOffset = localTimeOffsetMap.get(order.getStoreId());
			if (localTimeOffset == null) {
				TimeZone timeZone = posUtil.getStoreTimeZone(order.getStoreId());
				localTimeOffset = timeZone != null ? timeZone.getRawOffset() : 0;
				localTimeOffsetMap.put(order.getStoreId(), localTimeOffset);
			}
			record.setCancelTmLocal(new Date(nowUtc.getTime() + localTimeOffset));

			// 주문 갱신
			svcOrderMapper.updateByPrimaryKeySelective(record);

			// 앱으로 알림 발송			
			order.setOrderSt(record.getOrderSt());
			order.setCancelTp(record.getCancelTp());

			// 앱으로 알림 발송
			try {
				appNotificationService.notifyOrderStatusChanged(order);
			} catch (Exception e) {
				// 알림 전송에 실패해도 계속 처리
				logger.error("Failed notify cancel long-term unapproved app order to user mobile. orderId=" + order.getId(), e);
			}

			// 포스로 알림 발송
			SvcStore store = svcStoreMapper.selectByPrimaryKey(order.getStoreId());
			User user = order.getUserId() != null ? userMapper.selectByPrimaryKey(order.getUserId()) : null;
			MQTTClient.sendOrder(store, order, null, user, order.getOrderTp(), "system");
		}
	}

}
