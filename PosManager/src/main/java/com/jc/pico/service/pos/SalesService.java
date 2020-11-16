package com.jc.pico.service.pos;

import java.util.Date;

import com.jc.pico.bean.SvcSales;
import com.jc.pico.controller.pos.PosSalesController;
import com.jc.pico.utils.bean.SvcClosingExtended;
import com.jc.pico.utils.bean.SvcSalesExtended;

/**
 * 매출정보 저장/수정
 * TODO 취소?
 * 
 * @author green
 *
 */
public interface SalesService {
	/**
	 * 매출정보 저장 (키값이 있으면 수정, 없으면 신규)
	 * 
	 * @param svcSalesExtended
	 *            매출
	 * @return 저장된 매출의 매출번호 (null 인 경우 오류)
	 * @throws Throwable
	 *             오류
	 */
	public String saveSales(SvcSalesExtended svcSalesExtended) throws Throwable;

	/**
	 * 마감정보 저장 (해당 일자에 마감정보가 있으면 마감, 해당 일자에 마감정보가 없으면 개점이 아니라 오류처리 ==> 개점은
	 * {@link PosSalesController#sOpenInfo(String)})
	 * 
	 * @param svcClosingExtended
	 *            마감
	 * @return 저장된 마감의 키값 (null 인 경우 오류)
	 * @throws Throwable
	 *             오류
	 */
	public Long saveClosing(SvcClosingExtended svcClosingExtended) throws Throwable;

	/**
	 * 매출 조회
	 * 파라미터와 일치하는 매출을 조회 한다.
	 * 
	 * @param storeId
	 * @param openDt
	 * @param posNo
	 * @param receiptNo
	 * @return
	 */
	SvcSales getSales(Long storeId, Date openDt, String posNo, String receiptNo);

}
