package com.jc.pico.service.pos;

public interface PosAsyncService {

	void executeOnAsyncPayment(Runnable runnable, long startTimeMillis);

	void executeOnAsyncMqtt(Runnable runnable, long startTimeMillis);

	void executeOnOrderSyncTask(Runnable runnable);

}
