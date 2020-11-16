package com.jc.pico.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.UserCard;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.utils.mybatis.typehandler.MobileSecurityTypeHandler;

/**
 * POS 연동을 위한 Util 클래스
 * 
 * @author green
 *
 */
@Component
public class PosUtil {
	private Logger logger = LoggerFactory.getLogger(PosUtil.class);

	/**
	 * baseCode 세자리숫자 만들기
	 */
	private NumberFormat codeNumberFormat = new DecimalFormat("000");

	/**
	 * 일자만을 가져올 때 사용하는 포맷
	 */
	public static String DAY_FORMAT = "yyyyMMdd";
	public static SimpleDateFormat DATE_FORAMT = new SimpleDateFormat(DAY_FORMAT);

	/**
	 * 날짜 시간 포맷
	 * 년 월 일 시 분 초
	 * yyyyMMddHHmmss
	 */
	public static SimpleDateFormat DATE_TIME_FORAMT = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 공통코드 체계를 보정하기 위한 값 (APC between LocalPOS)
	 */
	public static int CD_GROUP_CORRECTION = -800;

	/**
	 * oAuth username 에 담기는 값들을 구분짓는 구분자
	 */
	public static String BASE_INFO_DELIMITER = "_-_";
	/**
	 * POS 단말기 ID
	 */
	public static String BASE_INFO_DEVICE_ID = "deviceId";
	/**
	 * 매장 ID
	 */
	public static String BASE_INFO_STORE_ID = "storeId";
	/**
	 * 브랜드 ID
	 */
	public static String BASE_INFO_BRAND_ID = "brandId";
	/**
	 * 회사 ID
	 */
	public static String BASE_INFO_COMPANY_ID = "companyId";
	/**
	 * POS 번호
	 */
	public static String BASE_INFO_POS_NO = "posNo";
	/**
	 * 서비스 가입자 ID
	 */
	public static String BASE_INFO_TENANT_ID = "tenantId";

	/**
	 * 알 수 없는 오류
	 */
	public static String EPO_0000_CODE = "EPO0000";
	public static String EPO_0000_MSG = "Unknown error";

	/**
	 * 타임존 변환 오류
	 */
	public static String EPO_0001_CODE = "EPO0001";
	public static String EPO_0001_MSG = "Timezone conversion error";

	/**
	 * 인증정보에 문제 있음
	 */
	public static String EPO_0002_CODE = "EPO0002";
	public static String EPO_0002_MSG = "Bad credential";

	/**
	 * 테이블에 락이 걸려있음
	 */
	public static String EPO_0003_CODE = "EPO0003";
	public static String EPO_0003_MSG = "SvcTable was locked";

	/**
	 * oAuth 인증정보에서 허용하지 않는 매장/회사 정보 요청
	 */
	public static String EPO_0004_CODE = "EPO0004";
	public static String EPO_0004_MSG = "Invalid request for Company/Store, Check your oAuth";

	/**
	 * 필수 정보 누락, 값의 포맷이 잘못됨
	 */
	public static String EPO_0005_CODE = "EPO0005";
	public static String EPO_0005_MSG = "Required value is empty or Invalid value. Check your data";

	/**
	 * 정보의 갯수가 맞지 않음
	 */
	public static String EPO_0006_CODE = "EPO0006";
	public static String EPO_0006_MSG = "Wrong numbers of expected informations";

	/**
	 * 키 값 오류. 겹침, 잘못된 값. 등
	 */
	public static String EPO_0007_CODE = "EPO0007";
	public static String EPO_0007_MSG = "Invalid key value";

	/**
	 * 데이터를 찾을 수 없음
	 */
	public static String EPO_0008_CODE_DATA_NOT_FOUND = "EPO0008";
	public static String EPO_0008_MSG_DATA_NOT_FOUND = "Data not found";

	/**
	 * 요청을 처리할 수 없는 데이터 상태.
	 * 이미 사용한 쿠폰을 취소 하려거나 등..
	 */
	public static String EPO_0009_CODE_ILLEGAL_STATE = "EPO0009";
	public static String EPO_0009_MSG_ILLEGAL_STATE = "Illegal state.";

	/**
	 * 잘못된 라이센스
	 */
	public static String EPO_0010_CODE_INVALID_LICENSE = "EPO0010";
	public static String EPO_0010_MSG_INVALID_LICENSE = "Invalid device license.";

	/**
	 * PG 결제 처리 실패
	 */
	public static String EPO_0011_CODE_FAILED_PG_PAY = "EPO0011";
	public static String EPO_0011_MSG_FAILED_PG_PAY = "Failed pg pay.";

	/**
	 * PG 결제 취소 처리 실패
	 */
	public static String EPO_0012_CODE_FAILED_PG_REFUND = "EPO0012";
	public static String EPO_0012_MSG_FAILED_PG_REFUND = "Failed pg refund.";

	private MobileSecurityTypeHandler mobileSecurityTypeHandler = new MobileSecurityTypeHandler();

	@Autowired
	private SvcStoreMapper svcStoreMapper;

	/**
	 * APC 서버에 저장된 로컬의 TimeZone 읽어오기
	 * 
	 * @param storeId
	 * @return 로컬 TimeZone
	 */
	public TimeZone getStoreTimeZone(Long storeId) {
		SvcStore svcStore = null; // 로컬 매장
		TimeZone timeZone = null; // 로컬 TimeZone
		try {
			svcStore = svcStoreMapper.selectByPrimaryKey(storeId);
			timeZone = TimeZone.getTimeZone(svcStore.getTimezone());
		} catch (Exception e) {
			logger.error("[{}][{}] storeId: {}, Exception: {}", EPO_0001_CODE, EPO_0001_MSG, storeId, e.getMessage());
		}
		return timeZone;
	}

	/**
	 * 서버에 저장된 UTC+0 시간을 로컬타임존 시간으로 변경하여 반환
	 * <br/>
	 * 이 메써드는 단독으로 사용하지 않고 다음 메써드를 참조할 것.{@link #getStoreTimeZone(Long)}
	 * 
	 * @param timeZone
	 *            로컬 TimeZone
	 * @param orgTimeString
	 *            서버에 저장된 UTC+0 시간
	 * @return 로컬타임존 시간
	 * @see PosUtil.getStoreTimeZone(Long storeId)
	 */
	public String getStoreTime(TimeZone timeZone, String orgTimeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date orgDate = null;
		Date newDate = null;
		String result = null;
		try {
			orgDate = sdf.parse(orgTimeString);

			String orgDateString = sdf.format(orgDate);
			sdf.setTimeZone(timeZone);
			newDate = sdf.parse(orgDateString);
			result = sdf.format(newDate);
		} catch (Exception e) {
			logger.error("[{}][{}] LocalTimeZone: {}, orgTimeString: {}, Exception: {}", EPO_0001_CODE, EPO_0001_MSG, timeZone, orgTimeString,
					e.getMessage());
		}
		return result;
	}

	/**
	 * 매장 타임존의 시간을 UTC+0 시간으로 변경하여 반환
	 * <br/>
	 * 이 메써드는 단독으로 사용하지 않고 다음 메써드를 참조할 것.{@link #getStoreTimeZone(Long)}
	 * 
	 * @param timeZone
	 *            로컬 TimeZone
	 * @param orgTimeString
	 *            매장 타임존의 시간
	 * @return UTC 시간
	 */
	public String getUtcTime(TimeZone timeZone, String orgTimeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(timeZone);

		Date orgDate = null;
		Date newDate = null;
		String result = null;
		try {
			orgDate = sdf.parse(orgTimeString);

			String orgDateString = sdf.format(orgDate);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			newDate = sdf.parse(orgDateString);
			result = sdf.format(newDate);
		} catch (Exception e) {
			logger.error("[{}][{}] LocalTimeZone: {}, orgTimeString: {}, Exception: {}", EPO_0001_CODE, EPO_0001_MSG, timeZone, orgTimeString,
					e.getMessage());
		}
		return result;
	}

	/**
	 * 컨트롤러로부터 Auth 정보를 받아 필요한 상점/POS 정보의 키를 읽어오는 유틸메쏘드
	 * 
	 * @param username
	 *            authentication 인증의 name 항목 ([deviceId]_-_[storeId]_-_[brandId]_-_[companyId])
	 * @return 상점/POS 정보의 키 맵
	 */
	public Map<String, Object> getPosBasicInfo(String username) {
		Map<String, Object> result = null;
		if (username != null && !username.isEmpty()) {
			String[] usernames = username.split(PosUtil.BASE_INFO_DELIMITER);
			if (usernames != null && usernames.length > 5) {
				String deviceId = usernames[0];
				Long storeId = Long.parseLong(usernames[1]);
				Long brandId = Long.parseLong(usernames[2]);
				Long companyId = Long.parseLong(usernames[3]);
				String posNo = usernames[4];
				Long tenantId = Long.parseLong(usernames[5]);
				result = new HashMap<String, Object>();
				result.put(PosUtil.BASE_INFO_DEVICE_ID, deviceId);
				result.put(PosUtil.BASE_INFO_STORE_ID, storeId);
				result.put(PosUtil.BASE_INFO_BRAND_ID, brandId);
				result.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
				result.put(PosUtil.BASE_INFO_POS_NO, posNo);
				result.put(PosUtil.BASE_INFO_TENANT_ID, tenantId);
			} else {
				throw new AuthenticationCredentialsNotFoundException("Not pos user. username=[" + username + "]");
			}
		}
		return result;
	}

	/**
	 * 암호화되어 저장된 핸드폰번호를 복호화하여 반환하기
	 * 
	 * @param org
	 *            암호화된 핸드폰번호
	 * @return 복호화된 핸드폰번호
	 * @author green, 2016.07.14
	 * @see 실제암호화모듈: {@link MobileSecurityTypeHandler#decrypt(String)}
	 */
	public String decryptMobileNumber(String org) {
		return mobileSecurityTypeHandler.decrypt(org);
	}

	/**
	 * POS 기기로부터 오는 코드를 APC 시스템 코드로 변환하여 리턴
	 * 
	 * @param mainCode
	 *            메인 코드
	 * @param baseCode
	 *            상세 코드
	 * @return 시스템 코드(메인 + 상세)
	 */
	public String getBaseCode(String mainCode, Integer baseCode) {
		String result = null;
		if (mainCode == null || mainCode.isEmpty() || baseCode == null || baseCode < 0) {
			return result;
		}
		result = mainCode + codeNumberFormat.format(baseCode);
		return result;
	}

	public Integer getPosCode(Integer baseCode) {
		return getPosCode(baseCode, null);
	}

	public Integer getPosCode(String baseCode, Integer defaultValue) {
		if (baseCode != null && baseCode.length() == 6) {
			return Integer.valueOf(baseCode.substring(3));
		} else {
			return defaultValue;
		}
	}

	public Integer getPosCode(Integer baseCode, Integer defaultValue) {
		String strCode = String.valueOf(baseCode);
		if (strCode != null && strCode.length() == 6) {
			String subCode = strCode.substring(3);
			return Integer.valueOf(subCode);
		} else {
			return defaultValue;
		}
	}

	public String toString(Object value) {
		return value == null ? null : value.toString();
	}

	public String toString(Object value, String defaultValue) {
		return value == null ? defaultValue : value.toString();
	}

	public Integer parseInt(String value, Integer defaultValue) {
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		return Integer.parseInt(value);
	}

	public Long parseLong(String value, Long defaultValue) {
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (Throwable tw) {
			return defaultValue;
		}
	}

	public void setTime(Calendar cal, int hour, int min, int sec, int millis) {
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, millis);
	}

	public Date getDate(String dateString, String dateFormat, Date defaultValue) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			return format.parse(dateString);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * yyyyMMddHHmmss 포맷을 Date 형으로 파싱
	 * 
	 * @param dateTimeString
	 * @param defaultValue
	 *            파싱 오류시 기본 값
	 * @return
	 */
	public Date parseDateTime(String dateTimeString, Date defaultValue) {
		try {
			return DATE_TIME_FORAMT.parse(dateTimeString);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * yyyyMMdd 포맷을 Date 형으로 파싱
	 * 
	 * @param dateTimeString
	 * @param defaultValue
	 *            파싱 오류시 기본 값
	 * @return
	 */
	public Date parseDate(String dateString, Date defaultValue) {
		try {
			return DATE_FORAMT.parse(dateString);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * yyyyMMddHHmmss 형식으로 포매팅 한다.
	 * 
	 * @param date
	 * @return yyyyMMddHHmmss
	 */
	public String foramtDateTime(Date date) {
		return date != null ? DATE_TIME_FORAMT.format(date) : null;
	}

	/**
	 * yyyyMMdd 형식으로 포매팅
	 * 
	 * @param date
	 * @return yyyyMMddHHmmss
	 */
	public String formatDate(Date date) {
		return date != null ? DATE_FORAMT.format(date) : null;
	}

	/**
	 * 날짜를 비교한다 (년 월 일)
	 * 
	 * @param date1
	 * @param date2
	 * @return - 0이면 동일한 날짜
	 *         - 0보다 작으면 date1 < date2 (date1 과거, date2 미래)
	 *         - 0보다 크면 date1 > date2 (date1 미래, date 과거)
	 */
	public int compareYmdDate(Date date1, Date date2) {
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);

		int compare = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (compare != 0) {
			return compare;
		}

		return cal1.get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * yyyyMMdd 날짜와 HHmm 시간 정보를 Date로 변환
	 */
	public Date parseYmdHHmm(String ymd, String hhmm, Date defaultValue) {
		if (ymd == null || ymd.isEmpty() || hhmm == null || hhmm.isEmpty()) {
			return defaultValue;
		}
		// yyyyMMddHHmmss
		try {
			return DATE_TIME_FORAMT.parse(ymd + hhmm + "00" /* ss */);
		} catch (ParseException e) {
			return defaultValue;
		}
	}

	public int nvl(Integer value, int defaultValue) {
		return value != null ? value : defaultValue;
	}

	/**
	 * 설정한 시분초를 가진 날짜를 반환한다.
	 *
	 * @param date
	 * @param hour
	 * @param min
	 * @param sec
	 * @param millis
	 * @return
	 */
	public Date getDateTime(Date date, int hour, int min, int sec, int millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, millis);
		return cal.getTime();
	}

	/**
	 * Agent 인증 정보에서 user id 획득
	 * 
	 * @param auth
	 * @return
	 */
	public Long getAgentUserId(Authentication auth) {
		if (auth == null) {
			return null;
		}
		String name = auth.getName();
		return parseLong(name.split(BASE_INFO_DELIMITER)[1], null);
	}

	/**
	 * 암호화된 카드 정보를 복호화 한다.
	 * 
	 * @param encryptedCardno
	 * @return
	 * @throws Exception
	 */
	public UserCard parseCardInfo(String encryptedCardno) throws Exception {

		// 카드 정보는 두 번 암호화 되어있어 두번 복호화.
		String decryptedCardInfo = AES256Cipher.decodeAES256(encryptedCardno);
		String[] cardInfos = decryptedCardInfo.split("\\|");
		for (int i = 0, len = cardInfos.length; i < len; i++) {
			cardInfos[i] = AES256Cipher.decodeAES256(cardInfos[i]);
		}

		// 0 : 카드번호 (하이픈)
		// 1: cvv
		// 2: 월
		// 3: 연도
		// 4: 홀더 네임
		UserCard card = new UserCard();
		card.setCardNo(cardInfos[0].replaceAll("-", "")); // 카드 번호의 하이픈은 제거
		card.setSecretCode(cardInfos[1]);
		card.setExpireMonth(cardInfos[2]);
		card.setExpireYear(cardInfos[3]);
		card.setOwnerName(cardInfos[4]);
		return card;
	}

	/**
	 * 날짜를 UTC 기준으로 변환한다.
	 * 
	 * @param date
	 * @param sourceTimeZone
	 * @return
	 */
	public Date convertLocaltoUtc(Date date, TimeZone sourceTimeZone) {
		if (date == null) {
			return null;
		}
		return new Date(date.getTime() - sourceTimeZone.getRawOffset());
	}

	/**
	 * 6자리 숫자형 코드를 문자 코드로 변환한다.
	 * POS 코드는 숫자형, APC 코드는 문자형 이다.
	 * 
	 * @param posCode
	 * @return
	 */
	public String toBaseCode(Integer posCode) {
		if (posCode == null) {
			return null;
		}
		if(posCode.intValue() == 0) {
			return "";
		}
		return posCode.toString();
	}

}
