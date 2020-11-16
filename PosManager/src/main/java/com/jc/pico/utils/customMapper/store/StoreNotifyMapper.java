package com.jc.pico.utils.customMapper.store;

import java.util.List;

import com.jc.pico.bean.UserDevice;
import com.jc.pico.utils.bean.SingleMap;

public interface StoreNotifyMapper {

	/**
	 * 디바이스 별 브랜드 알림 목록
	 * 
	 * @param param
	 *            brandId
	 *            storeId
	 *            deviceId
	 * @return
	 */
	List<SingleMap> selectNotifyList(SingleMap param);

	/**
	 * 읽음 상태로 표시
	 * 
	 * @param param
	 *            brandId
	 *            storeId
	 *            deivceId
	 */
	int updateNotifyRead(SingleMap param);

	/**
	 * 삭제한 상태로 표시
	 * 
	 * @param param
	 *            notifyId
	 *            deviceId
	 */
	int updateNotifyDelete(SingleMap param);

	/**
	 * 알림 읽지 않은 갯수 조회
	 * 
	 * @param param
	 *            brandId
	 *            storeId
	 *            deviceId
	 * @return
	 */
	int selectNotiftyUnreadCount(SingleMap param);

	/**
	 * 
	 * @param params
	 *            osTp : OS 종류
	 *            isPushPayment : 결제 알림 허용 단말을 조회하려면 true
	 * @return
	 */
	List<UserDevice> selectPushUserDeviceListByEventTp(SingleMap params);
}
