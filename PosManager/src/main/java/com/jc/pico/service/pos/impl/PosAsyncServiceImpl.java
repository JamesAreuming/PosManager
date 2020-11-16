package com.jc.pico.service.pos.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jc.pico.service.pos.PosAsyncService;

/**
 * 비동기 작업을 수행한다.
 * 스프링에서는 @Async를 이용한 비동기 실행 호출시
 * 동일 서비스내의 메서드를 호출하는 것은 동작하지 않아 별로 비동기 실행 서비스를 이용한다.
 *
 */
@Service
public class PosAsyncServiceImpl implements PosAsyncService {

	/**
	 * 지정한 시간이 경과되면 결제 처리 실행자로 실행한다
	 */
	@Override
	@Async("paymentExecutor")
	public void executeOnAsyncPayment(Runnable runnable, long startTimeMillis) {
		// 지정한 시간 이후로 실행되도록 함.
		sleepTimeOnThread(startTimeMillis);
		runnable.run();
	}

	/**
	 * 지정한 시간이 경과되면 mqtt 처리 실행자로 실행한다.
	 * 
	 * @param order
	 */
	@Override
	@Async("mqttExecutor")
	public void executeOnAsyncMqtt(Runnable runnable, long startTimeMillis) {
		sleepTimeOnThread(startTimeMillis);
		runnable.run();
	}

	/**
	 * 주문 싱크 처리를 별도 멀티 쓰레드로 처리 한다.
	 * 
	 * @param order
	 */
	@Override
	@Async("orderSyncExecutor")
	public void executeOnOrderSyncTask(Runnable runnable) {
		runnable.run();
	}

	/**
	 * 해당 쓰레드에서 지정한 시간이 될때까지 sleep 하여 대기
	 * 
	 * @param startTimeMillis
	 */
	private void sleepTimeOnThread(long startTimeMillis) {
		final long delay = startTimeMillis - System.currentTimeMillis();
		if (delay > 0) {
			synchronized (Thread.currentThread()) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// ignore..
				}
			}
		}
	}

}
