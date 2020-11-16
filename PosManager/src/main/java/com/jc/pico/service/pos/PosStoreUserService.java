package com.jc.pico.service.pos;

import com.jc.pico.utils.bean.SvcSalesExtended;

public interface PosStoreUserService {

	/**
	 * 사용자의 매장별 매출 통계 갱신
	 * 
	 * @param sales
	 */
	void resolveStoreUserSales(SvcSalesExtended sales);

}
