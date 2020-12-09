/**
 * <pre>
 * Filename	: SureM.java
 * Function	: SMS 유틸
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.ext.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jc.pico.utils.Config;
import com.surem.api.ims.SureIMSAPI;
import com.surem.net.SendReport;
import com.surem.net.ims.SureIMSDeliveryReport;

public class SureM {

	private static Config config = Config.getInstance();
	private static Logger logger = LoggerFactory.getLogger(SureM.class);

	/**
	 * 국제SMS 발송
	 * 
	 * @param smsId       - 메시지 고유값
	 * @param mbCountryCd - 국가코드
	 * @param mb          - 휴대폰번호
	 * @param smsText     - SMS 메시지
	 * @param userName    - 휴대폰 사용자명
	 * @return
	 * @throws Exception
	 */
	public static String sendIMS(int smsId, String mbCountryCd, String mb, String smsText, String userName)
			throws Exception {
		String result = "";

		SureIMSAPI ims = new SureIMSAPI() {
			@Override
			public void report(SureIMSDeliveryReport dr) {
				logger.debug("msgkey=" + dr.getMember()); // 메시지 고유값
				logger.debug("result=" + dr.getResult()); // '2': 전송 결과 성공. '4': 전송 결과 실패
				logger.debug("errorcode=" + dr.getErrorCode()); // 결과 코드
				logger.debug("recvtime=" + dr.getRecvDate() + dr.getRecvTime()); // 단말기 수신 시간
			}
		};

		logger.debug("smsId : " + smsId);
		logger.debug("compId : " + config.getString("sms.surem.userId"));
		logger.debug("compCd : " + config.getString("sms.surem.userCd"));
		logger.debug("compName : " + config.getString("sms.surem.userName"));
		logger.debug("reqphone1 : " + config.getString("sms.surem.reqphone1"));
		logger.debug("reqphone2 : " + config.getString("sms.surem.reqphone2"));
		logger.debug("reqphone3 : " + config.getString("sms.surem.reqphone3"));
		logger.debug("mbCountryCd : " + mbCountryCd);
		logger.debug("mb : " + mb);
		logger.debug("userName : " + userName);
		logger.debug("smsText : " + smsText);
		// send SMS
		SendReport sr = ims.sendMain(smsId, // 메시지 고유값
				config.getString("sms.surem.userId"), // 유저코드
				config.getString("sms.surem.userCd"), // 회사코드
				config.getString("sms.surem.userName"), // 유저명
				mbCountryCd, // 국가코드
				mb, // 수신자 번호
				userName, // 수신자 명
				config.getString("sms.surem.reqphone1"), // 발신자 번호1
				config.getString("sms.surem.reqphone2"), // 발신자 번호2
				config.getString("sms.surem.reqphone3"), // 발신자 번호3
				smsText, // 메시지 내용
				"00000000", // 예약날짜 yyyyMMdd ("00000000" -> 즉시전송)
				"000000", // 예약시간 HHmmss
				"U" // UTF-16 인코딩 여부 "U"
		);
		result = sr.getStatus(); // 'O': 전송 요청 성공
		logger.debug("smsResult : " + result);

		return result;
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SureIMSAPI ims = new SureIMSAPI() {

			@Override
			public void report(SureIMSDeliveryReport dr) {
				System.out.print("msgkey=" + dr.getMember() + "\t"); // 메시지 고유값
				System.out.print("result=" + dr.getResult() + "\t"); // '2': 전송 결과 성공. '4': 전송 결과 실패
				System.out.print("errorcode=" + dr.getErrorCode() + "\t"); // 결과 코드
				System.out.print("recvtime=" + dr.getRecvDate() + dr.getRecvTime() + "\t"); // 단말기 수신 시간
				System.out.println();
			}
		};

		/*
		 * SendReport sr = ims.sendMain( int member, java.lang.String usercode,
		 * java.lang.String deptcode, java.lang.String username, java.lang.String
		 * country, java.lang.String callphone, java.lang.String reqname,
		 * java.lang.String reqphone1, java.lang.String reqphone2, java.lang.String
		 * reqphone3, java.lang.String callmessage, java.lang.String rdate,
		 * java.lang.String rtime, java.lang.String encodeFlag);
		 * 
		 * member - 메시지 고유값 usercode - 유저코드 deptcode - 회사코드 username - 사용자 명 country -
		 * 국가코드 callphone - 수신자 번호 reqname - 수신자 명 reqphone1 - 발신자 번호1 (02) reqphone2 -
		 * 발신자 번호2 (1588) reqphone3 - 발신자 번호3 (4640) callmessage - 메시지 내용 rdate - 예약날짜
		 * yyyyMMdd ("00000000" -> 즉시전송) rtime - 예약시간 HHmmss encodeFlag - UTF-16 인코딩 여부
		 * "U"
		 */
		SendReport sr = ims.sendMain(100, // int member,
				"ORDER9", // java.lang.String usercode,
				"ORDER9", // java.lang.String deptcode,
				"ORDER9", // java.lang.String username,
				"82", // java.lang.String country,
				"12341234", // java.lang.String callphone,
				"테스트", // java.lang.String reqname,
				"02", // java.lang.String reqphone1,
				"1234", // java.lang.String reqphone2,
				"1234", // java.lang.String reqphone3,
				"ORDER9 Test !!!", // java.lang.String callmessage,
				"00000000", // java.lang.String rdate,
				"000000", // java.lang.String rtime,
				"U" // java.lang.String encodeFlag, "U"
		);
		System.out.println(sr.getMember()); // 메시지 고유값
		System.out.println(sr.getStatus()); // 'O': 전송 요청 성공

		// ims.recvReport(/* params */); // 전송 결과 요청
		ims.recvReport("00000000", // java.lang.String rdate,
				"ORDER9", // java.lang.String usercode,
				"ORDER9" // java.lang.String deptcode
		); // 전송 결과 요청
	}
}
