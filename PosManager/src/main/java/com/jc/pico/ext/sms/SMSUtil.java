/**
 * <pre>
 * Filename	: SMSUtil.java
 * Function	: SMS 유틸
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.ext.sms;

public class SMSUtil {
	
	/**
	 * 국제SMS 발송
	 * 
	 * @param smsId - 메시지 고유값
	 * @param mbCountryCd - 국가코드
	 * @param mb - 휴대폰번호
	 * @param smsText - SMS 메시지
	 * @param userName - 휴대폰 사용자명
	 * @return
	 * @throws Exception
	 */
	public static String sendIMS(int smsId, String mbCountryCd, String mb, String smsText,
			String userName) throws Exception {
		String result = SureM.sendIMS(smsId, mbCountryCd, mb, smsText, userName); // 'O': 전송 요청 성공
		
		return result;
	}
	
	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String result = SMSUtil.sendIMS(100, "82", "1082735122", "PICO Test !!!", "테스트");
		System.out.println("sendIMS : " + result);	// 'O': 전송 요청 성공 
	}
}
