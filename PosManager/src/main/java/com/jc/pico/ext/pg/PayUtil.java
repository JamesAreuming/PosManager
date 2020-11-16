/**
 * <pre>
 * Filename	: PayUtil.java
 * Function	: Pay 공통 유틸
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.ext.pg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayUtil {

	private static Logger logger = LoggerFactory.getLogger(PayUtil.class);
	public static String runMode = (GenericValidator.isBlankOrNull(System.getProperty("config.run.mode")) ? "real" : System.getProperty("config.run.mode"));

	/**
	 * 
	 * @param urlString
	 * @return
	 * @throws Exception
	 */
	public static String requestHttps(String urlString, String query, String method) throws Exception {
		StringBuffer result = new StringBuffer();

		URL url = null;
		HttpsURLConnection urlConn = null;
		int responseCode = 200;

		// output
		OutputStream os = null;
		BufferedWriter writer = null;

		// input
		InputStream is = null;
		BufferedReader reader = null;

		try {
			// URL
			url = new URL(urlString);

			// set connection
			urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);

			// set request property
			// urlConn.setRequestProperty("Host", "order9.co.kr/");
			// urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7");
			// urlConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// urlConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			// urlConn.setRequestProperty("Connection", "keep-alive");
			// urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// urlConn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

			// set HostnameVerifier
			urlConn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					// Ignore host name verification. It always returns true.
					return true;
				}
			});

			// set SSL
			SSLContext context = SSLContext.getInstance("SSL"); // TLS, SSL
			// context.init(null, null, new SecureRandom());
			context.init(null, null, null);
			urlConn.setSSLSocketFactory(context.getSocketFactory());

			// set request method
			if ("POST".equals(method)) {
				urlConn.setRequestMethod(method);
				try {
					os = urlConn.getOutputStream();
					writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
					logger.debug("request query : {}", query);
					writer.write(query);
					writer.flush();
				} finally {
					if (writer != null) {
						writer.close();
					}
				}

				responseCode = urlConn.getResponseCode();
				logger.debug("respose Code : {}", responseCode);
			} else {
				urlConn.setRequestMethod("GET");
			}

			// connect to host
			// urlConn.connect();
			// urlConn.setInstanceFollowRedirects(true);

			// get response
			try {
				is = urlConn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}

		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}

		return result.toString();
	}
	
	/**
	 * 패터미터 값을 encode
	 * 
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public static String encodeParam(String val) throws Exception {
		if (val == null || val.trim().length() == 0) {return "";}
		
		return URLEncoder.encode(val, "UTF-8");
	}
	
	/**
	 * 패터미터를 파싱하여 Map으로 반환 (response data 파싱에 사용)
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> parseParams(String params) throws Exception {
		HashMap<String, String> result = new HashMap<String, String>();
		if (params == null || params.trim().length() == 0) {return result;}
		
		// parse param
		String[] paramArr = params.split("&");
		for (int inx = 0; inx < paramArr.length; inx++) {
			String[] pairArr = paramArr[inx].split("=");
			if (pairArr.length == 2) {
				result.put(pairArr[0], pairArr[1]);
			}
		}
		
		return result;
	}
	
	/**
	 * MD5 Hash 변환
	 * 
	 * @param val
	 * @return
	 */
	public static String getMD5Hash(String val) {
		if (val == null || val.trim().length() == 0) {return val;}
		
	     MessageDigest md = null;
	     String hash = null;
		
	     try {
	    	 md = MessageDigest.getInstance("MD5");
	    	 md.update(val.getBytes(), 0, val.length());
		     hash = new BigInteger(1, md.digest()).toString(16);
	     } catch (NoSuchAlgorithmException ex) {
	    	 ex.printStackTrace();
	     }
		
	     return hash;
	}
	
	/**
	 * 통화에 해당하는 소수점 자리를 가진 숫자로 변환한다. (지정 소수점 이하 버림)
	 * RUB 인경우 소숫점 2자리이며 32425.0 이면 32425.00 으로 출력.
	 * @param currencyCode
	 * @param amount
	 * @return
	 */
	public static final String normalizedCurrencyFractionDigits(String currencyCode, double amount) {
	  /*
	   * 가격을 10.00으로 던질 때
	   * 서버 언어가 러시아인 곳에서 실행 시 가격 포맷이 ,로 만들어져 5203 format error 발생
	   * 기존 Locale 저장 후 en-US로 변경,  포맷이 끝나면 다시 언어 셋팅
	   * */ 
	  Locale preLocale = Locale.getDefault(); 
	  Locale.setDefault(new Locale("en", "US"));
		Currency currency = Currency.getInstance(currencyCode);		
		DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(currency.getDefaultFractionDigits());
		format.setMinimumFractionDigits(currency.getDefaultFractionDigits());
		format.setGroupingUsed(false);
		Locale.setDefault(preLocale);
		return format.format(amount);
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//String query = "MerchantId=74817&OrderId=56789&Amount=9.99&Currency=RUB&SecurityKey=3844908d4c2a42e19be091bb5d068d22&CardHolderName=test&CardNumber=4111111111111111&CardExpDate=1224&CardCvv=123";
		//String query = "MerchantId=74817&OrderId=56789&Amount=1.01&Currency=RUB&SecurityKey=3844908d4c2a42e19be091bb5d068d22&CardHolderName=KWANG EUN PARK&CardNumber=5243356118382615&CardExpDate=1017&CardCvv=162";
		//query = URLEncoder.encode(query, "UTF-8");
		//String urlString = "https://secure.payonlinesystem.com/payment/transaction/auth/";
		//String urlString = "https://www.google.com";

		//String result = PayUtil.requestHttps(urlString + "?" + query, "", "GET");
		//String result = PayUtil.requestHttps(urlString, query, "POST");
		//System.out.println("requestHttps : " + result);
		//System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("sAXigwAMu36"));
		//System.out.println("getMD5Hash : " + "3844908d-4c2a-42e1-9be0-91bb5d068d22".replaceAll("-", ""));
		//System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("3844908d-4c2a-42e1-9be0-91bb5d068d22".replaceAll("-", "")));
		
		//System.out.println("encodeParam : " + PayUtil.encodeParam("3844908d-4c2a-42e1-9be0-91bb5d068d22"));
		
		//System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=12345&OrderId=56789&Amount=9.99&Currency=USD&PrivateSecurityKey=3844908d-4c2a-42e1-9be0-91bb5d068d22"));
		System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=12345&OrderId=56789&Amount=9.99&Currency=USD&ValidUntil=2010-01-29 16:10:00&OrderDescription= Buying phone &PrivateSecurityKey=3844908d-4c2a-42e1-9be0-91bb5d068d22"));
		
		//System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=12345&OrderId=56789&Amount=9.99&Currency=USD&ValidUntil=" + PayUtil.encodeParam("2010-01-29 16:10:00") + "&OrderDescription=" + PayUtil.encodeParam("Buying phone") + "&PrivateSecurityKey=3844908d-4c2a-42e1-9be0-91bb5d068d22"));
		System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=12345&OrderId=56789&Amount=9.99&Currency=USD&ValidUntil=2010-01-29 16:10:00&OrderDescription=" + PayUtil.encodeParam(" Buying phone ") + "&PrivateSecurityKey=3844908d-4c2a-42e1-9be0-91bb5d068d22"));
		
		System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=12345&OrderId=56789&Amount=9.99&Currency=USD&ValidUntil=2010-01-29 16:10:00&PrivateSecurityKey=3844908d-4c2a-42e1-9be0-91bb5d068d22"));
		System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=74817&OrderId=1471938109279&Amount=1.01&Currency=RUB&CardHolderName=Test&CardNumber=4111111111111111&CardExpDate=1224&CardCvv=123&PrivateSecurityKey=f7c1ba11-b4c6-4028-8475-fc9fc950e076"));
	
		// PayOnline
		System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=74817&OrderId=125687&Amount=2.00&Currency=RUB&PrivateSecurityKey=f7c1ba11-b4c6-4028-8475-fc9fc950e076"));
		System.out.println("getMD5Hash : " + PayUtil.getMD5Hash("MerchantId=74817&OrderId=125687&Amount=2.00&Currency=USD&PrivateSecurityKey=f7c1ba11-b4c6-4028-8475-fc9fc950e076"));
	}
}
