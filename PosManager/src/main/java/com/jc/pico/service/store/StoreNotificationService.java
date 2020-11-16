package com.jc.pico.service.store;

import java.util.List;
import java.util.Locale;

import com.jc.pico.bean.SvcCctvLog;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.User;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.bean.SvcSalesExtended;

public interface StoreNotificationService {

	SingleMap getNotifyList(SingleMap param);

	SingleMap setNotifyReadMark(SingleMap param);

	SingleMap setNotifyDeleteMark(SingleMap param);

	SingleMap getNotifyUnreadCount(SingleMap param);

	SvcCctvLog addSalesNotifyLog(SvcSalesExtended sales, String eventTp, List<Long> deviceIds);

	void notifyOrderChanged(SvcOrder newOrder, SvcOrderExtended oldOrder);

	void notifySalesChanged(SvcSalesExtended sales);

	void notifyVisitVip(SvcStore store, User user, Locale locale);

}
