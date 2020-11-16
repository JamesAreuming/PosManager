package com.jc.pico.service.pos;

import com.jc.pico.bean.SvcSales;
import com.jc.pico.utils.bean.SvcSalesExtended;

/**
 * 매출 저장 서비스
 * 매출 저장 트랜잭션 처리를 위해 추가
 * 
 * @author hyo
 *
 */
public interface SalesSaveService {

	/**
	 * 매출정보 저장 (키값이 있으면 수정, 없으면 신규)
	 * 
	 * @param svcSalesExtended
	 *            매출
	 * @return 저장된 매출의 매출번호 (null 인 경우 오류)
	 * @throws Throwable
	 *             오류
	 */
	String saveSales(SvcSalesExtended sales, SvcSales oldSales) throws Throwable;

}
