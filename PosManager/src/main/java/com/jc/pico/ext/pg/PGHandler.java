/**
 * <pre>
 * Filename	: PGHandler.java
 * Function	: PG 거래 처리
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.ext.pg;

import java.util.HashMap;

public class PGHandler {

	/**
	 * 카드 권한 체크(카드 등록때 호출)
	 * - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> checkCardAuth(PayBean payBean) throws Exception {
		HashMap<String, String> result = null;
		
		// TODO, real mode
		//if ("TODO".equals(PayUtil.runMode)) {
//				result = PayOnline.checkCardAuth(payBean);
//		} else {
			result = new HashMap<String, String>();
			result.put("Code", "200");
			result.put("Id", Long.toString(System.currentTimeMillis()));
//		}
				
					
		return result;
	}
	
	/**
	 * 결제
	 * - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> purchase(PayBean payBean) throws Exception {
		HashMap<String, String> result = null;
		
		// TODO, real mode
		//if ("TODO".equals(PayUtil.runMode)) {
//			result = PayOnline.purchase(payBean);
//		} else {
			result = new HashMap<String, String>();
			result.put("Code", "200");
			result.put("Id", Long.toString(System.currentTimeMillis()));
//		}
		
		return result;
	}
	
	/**
	 * 결제 취소
	 * - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> cancel(PayBean payBean) throws Exception {
		HashMap<String, String> result = PayOnline.cancel(payBean);
		
		return result;
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("key : " + System.currentTimeMillis());
		System.out.println("test : " + PGHandler.purchase(null));
	}
}
