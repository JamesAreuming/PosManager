package com.jc.pico.controller.pos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jc.pico.service.pos.PosAsyncService;
import com.jc.pico.service.pos.PosOrderSyncService;

/**
 * 포스 관련 스케쥴링 작업
 *
 */
@Component
public class PosScheduler  {

	protected static Logger logger = LoggerFactory.getLogger(PosScheduler.class);

	@Autowired
	private PosOrderSyncService posOrderSyncService;

	@Autowired
	private PosAsyncService posAsyncService;

	/**
	 * POS <-> APC간 주문 동기화를 위해 주기적으로 동기화되지 않은 주문을 포스에게 알린다.
	 * POS는 MQTT를 수신 후 해당 주문 정보를 조회해가고, 이때 서버는 동기화 완료된것으로 기록한다.
	 * 수정 : 
	 * 2020.01.08
	 */
	//@Scheduled(fixedRate = 5 * 60 * 1000) // 5 min
	@Scheduled(fixedRate = 1440 * 60 * 1000)
	public void sendNotConfirmOrderToPos() {
		logger.info("Scheduled send no confirm order. millis=" + System.currentTimeMillis());

		// 수정 : 
		// 2020.01.08
		
		/*
		// 스케줄러의 반응성을 높이기 위해 주문 싱크 처리 쓰레드에서 처리하도록 함.
		posAsyncService.executeOnOrderSyncTask(new Runnable() {

			@Override
			public void run() {
				logger.info("Start send no confirm order. millis=" + System.currentTimeMillis());
				posOrderSyncService.sendNoConfirmOrder();
			}

		});
		*/
		
	}

	/**
	 * 앱으로 부터 들어온 주문 중 장기 미승인 건은 취소 처리한다.
	 * 미승인(607001)을 거부(607004) 상태로 변경하고 app과 pos에 전달 한다.
	 * 수정 : 
	 * 2020.01.08
	 */
	//@Scheduled(fixedRate = 30 * 60 * 1000) // 30 min
	@Scheduled(fixedRate = 1450 * 60 * 1000)
	public void cancelAppOrderByLongTermUnapproved() {
		logger.info("Scheduled cancel long-term unapproved orders. millis=" + System.currentTimeMillis());

		// 수정 : 
		// 2020.01.08
		
		/*
		// 스케줄러의 반응성을 높이기 위해 주문 싱크 처리 쓰레드에서 처리하도록 함.
		posAsyncService.executeOnOrderSyncTask(new Runnable() {
			@Override
			public void run() {
				logger.info("Start cancel long-term unapproved orders. millis=" + System.currentTimeMillis());
				posOrderSyncService.cancelLongTermUnapprovedAppOrder();
			}
		});
		*/
		
	}

}
