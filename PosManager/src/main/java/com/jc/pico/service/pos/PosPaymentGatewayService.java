package com.jc.pico.service.pos;

import java.util.List;

import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.PosOrderPayment;
import com.jc.pico.utils.bean.SingleMap;

public interface PosPaymentGatewayService {

	/**
	 * 주문에 있는 PG 결제를 처리
	 * 
	 * @param param
	 *            - CD_COMPANY 회사ID (franchise id)
	 *            - CD_STORE 매장 ID
	 *            - YMD_ORDER 개점일
	 *            - NO_ORDER 주문 번호
	 * @return
	 */
	public List<PosOrderPayment> purchase(SingleMap param) throws RequestResolveException;

	/**
	 * 주문에 있는 PG 결제를 취소 처리
	 * 
	 * @param param
	 *            - CD_COMPANY 회사ID (franchise id)
	 *            - CD_STORE 매장 ID
	 *            - YMD_ORDER 개점일
	 *            - NO_ORDER 주문 번호
	 * @return
	 * @throws RequestResolveException
	 */
	public List<PosOrderPayment> refundFromOrder(SingleMap param) throws RequestResolveException;

	/**
	 * 매출 정보로 부터 PG 결제를 취소 처리 한다.
	 * 
	 * @param param
	 *            - CD_COMPANY 회사ID (franchise id)
	 *            - CD_STORE 매장 ID
	 *            - YMD_ORDER 개점일
	 *            - NO_POS 포스 번호
	 *            - NO_RCP 영수증 번호
	 * @throws RequestResolveException 
	 * 
	 */
	public void refundFromSales(SingleMap param) throws RequestResolveException;

}
