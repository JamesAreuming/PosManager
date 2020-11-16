package com.jc.pico.utils.customMapper.pos;

import java.util.List;

import com.jc.pico.utils.bean.PosOrderPayment;

/**
 * 11. S_ORDER_SAVE : 주문정보 저장
 * 12. S_ORDER_MISSING : 미 수신 주문 정보 조회
 * 
 * 결제 정보
 * 
 * 2016. 9. 7, hyo, create
 * 
 *
 */
public interface PosOrderPaymentMapper {

	/**
	 * 결제 정보 조회
	 * 
	 * @param orderId
	 *            주문 ID
	 * @return
	 */
	List<PosOrderPayment> selectListByOrderId(Long orderId);

}
