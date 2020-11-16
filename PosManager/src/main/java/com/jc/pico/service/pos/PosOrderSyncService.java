package com.jc.pico.service.pos;

public interface PosOrderSyncService {

	void sendNoConfirmOrder();

	void cancelLongTermUnapprovedAppOrder();

}
