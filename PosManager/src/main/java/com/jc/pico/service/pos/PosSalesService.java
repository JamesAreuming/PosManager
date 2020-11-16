package com.jc.pico.service.pos;

import java.util.Map;

import com.jc.pico.utils.bean.PosSalesClosingSave;
import com.jc.pico.utils.bean.PosSalesClosingSaveDetail;
import com.jc.pico.utils.bean.PosSalesSaleSave;
import com.jc.pico.utils.bean.PosSalesSaleSaveDetail;
import com.jc.pico.utils.bean.PosSalesSaleSaveDiscount;
import com.jc.pico.utils.bean.PosSalesSaleSavePay;

/**
 * POS 로부터 받은 매출정보와 마감정보 저장/수정
 * TODO 취소?
 * @author green
 *
 */
public interface PosSalesService {
	/**
	 * 매출정보 저장/수정
	 * @param posBaseKeyMap oAuth 정보로부터 매장/회사/브랜드의 키값을 읽어올 수 있는 객체
	 * @param posSalesSaleSave POS로부터 받는 데이터구조 {@link PosSalesSaleSave}, {@link PosSalesSaleSaveDetail}, {@link PosSalesSaleSaveDiscount}, {@link PosSalesSaleSavePay}
	 * @return 성공의 경우, 주문번호
	 * @throws Throwable 오류
	 */
	public String saveSale(Map<String, Object> posBaseKeyMap, PosSalesSaleSave posSalesSaleSave) throws Throwable;

	/**
	 * 마감정보 저장/수정
	 * @param posBaseKeyMap oAuth 정보로부터 매장/회사/브랜드의 키값을 읽어올 수 있는 객체
	 * @param posSalesClosingSave POS로부터 받는 데이터구조 {@link PosSalesClosingSave}, {@link PosSalesClosingSaveDetail}
	 * @return 성공의 경우, 마감키
	 * @throws Throwable 오류
	 */
	public Long saveClosing(Map<String, Object> posBaseKeyMap, PosSalesClosingSave posSalesClosingSave) throws Throwable;
}
