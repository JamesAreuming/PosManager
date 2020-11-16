/**
 * <pre>
 * Filename	: PayOnline.java
 * Function	: PG 거래 처리 - PayOnline
 * Comment 	: PayOnline Gateway API Reference vers. 1.0.2 eng.pdf
 *            -> 주의사항 : SecurityKey 앞의 필드는 MD5 Hash 암호화(PrivateSecurityKey 또는 PaymentKey 포함)
 * History	: 
 *
 * Version	: 1.0
 * Author   :
 * </pre>
 */
package com.jc.pico.ext.pg;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class PayOnline {

	private static Logger logger = LoggerFactory.getLogger(PayOnline.class);

	// Merchant Information
	private static final String MERCHANT_ID = "74817";
	private static final String PRIVATE_SECURITY_KEY = "f7c1ba11-b4c6-4028-8475-fc9fc950e076";
	private static final String PAYMENT_KEY = "0243f520-5d57-47b7-b8ed-b5014ec6375e";

	// PayOnline API URL
	private static final String URL_TRANSACTION = "https://secure.payonlinesystem.com/payment/transaction/";
	private static final String URL_AUTH_METHOD = URL_TRANSACTION + "auth/";
	private static final String URL_COMPLETE_METHOD = URL_TRANSACTION + "complete/";
	private static final String URL_VOID_METHOD = URL_TRANSACTION + "void/";
	private static final String URL_REFUND_METHOD = URL_TRANSACTION + "refund/";

	/**
	 * SecurityKey 생성
	 * 
	 * @param params
	 * @return
	 * @throws ExceptionSecurityKey
	 */
	private static String getSecurityKey(PayBean payBean, String params) throws Exception {
		String keyParams = params + "&PrivateSecurityKey=" + payBean.getPrivateKey();
		//String keyParams = params + "&PaymentKey=" + payBean.getPaymentKey();

		return PayUtil.getMD5Hash(keyParams);
	}

	/**
	 * 카드 권한 체크(카드 등록때 호출) - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> checkCardAuth(PayBean payBean) {
		HashMap<String, String> result = Maps.newHashMap();

		try {
			// set bean
			payBean.setMerchantID(MERCHANT_ID);
			payBean.setPrivateKey(PRIVATE_SECURITY_KEY);
			payBean.setOrderId(Long.toString(System.currentTimeMillis()));
			payBean.setAmount(10.00);
			payBean.setCurrency("RUB");
			
			// purchase
			result = PayOnline.purchase(payBean);

			// check result
			String pgCode = (String) result.get("Code");
			if ("200".equals(pgCode)) {
				// cancel
				payBean.setTransactionId(result.get("Id"));
				HashMap<String, String> result2 = PayOnline.cancel(payBean);
				result.put("Code", result2.get("Code"));
			}
			logger.debug("checkCardAuth code : {}", result.get("Code"));			
			
		} catch (Exception ex) {
			result.put("Code", "9999");
			result.put("Message", "System error.");
		}

		return result;
	}

	/**
	 * 결제 - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> purchase(PayBean payBean) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			// make base query : SecurityKey 앞의 필드는 MD5 Hash 암호화
			StringBuffer querySb = new StringBuffer();
			querySb.append("MerchantId=").append(payBean.getMerchantID());
			querySb.append("&OrderId=").append(payBean.getOrderId());
			querySb.append("&Amount=").append(PayUtil.normalizedCurrencyFractionDigits(payBean.getCurrency(), payBean.getAmount()));
			querySb.append("&Currency=").append(payBean.getCurrency());

			// set SecurityKey (parameters -> MD5 hash)
			logger.debug("PG request data(before) : {}", querySb.toString());
			String hash = PayOnline.getSecurityKey(payBean, querySb.toString());
			logger.debug("PG request hash : {}", hash);
			querySb.append("&SecurityKey=").append(hash);

			// make other query
			// querySb.append("&CardHolderName=").append(PayUtil.encodeParam(payBean.getCardHolderName()));
			querySb.append("&CardHolderName=").append(payBean.getCardHolderName());
			querySb.append("&CardNumber=").append(payBean.getCardNumber());
			querySb.append("&CardExpDate=").append(payBean.getCardExpDate());
			querySb.append("&CardCvv=").append(payBean.getCardCvv());

			// get result data
			logger.info("PG request data : {}", querySb.toString());
			String pgData = PayUtil.requestHttps(URL_AUTH_METHOD, querySb.toString(), "POST");
			logger.info("PG respose data(String) : {}", pgData);

			// parse result data
			result = PayUtil.parseParams(pgData);
			logger.debug("PG respose data(Map) : {}", result);

			// TODO, temp result
			String pgCode = (String) result.get("Code");
			logger.debug("PG respose code : {}", pgCode);
//			if ("6004".equals(pgCode)) {
//				result.put("Code", "200");
//			}
		} catch (Exception ex) {
			result.put("Code", "9999");
			result.put("Message", "System error.");
		}

		return result;
	}
	
	/**
	 * 결제 완료 - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> complete(PayBean payBean) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			// make base query : SecurityKey 앞의 필드는 MD5 Hash 암호화
			StringBuffer querySb = new StringBuffer();
			querySb.append("MerchantId=").append(payBean.getMerchantID());
			querySb.append("&TransactionId=").append(payBean.getTransactionId());
			querySb.append("&Amount=").append(PayUtil.normalizedCurrencyFractionDigits(payBean.getCurrency(), payBean.getAmount()));

			// set SecurityKey (parameters -> MD5 hash)
			logger.debug("PG request data(before) : {}", querySb.toString());
			String hash = PayOnline.getSecurityKey(payBean, querySb.toString());
			logger.debug("PG request hash : {}", hash);
			querySb.append("&SecurityKey=").append(hash);

			// get result data
			logger.info("PG request data : {}", querySb.toString());
			String pgData = PayUtil.requestHttps(URL_COMPLETE_METHOD, querySb.toString(), "POST");
			logger.info("PG respose data(String) : {}", pgData);

			// parse result data
			result = PayUtil.parseParams(pgData);
			logger.debug("PG respose data(Map) : {}", result);

			// check result
			String pgResult = (String) result.get("Result"); // Ok or Error
			logger.debug("PG respose result : {}", pgResult);
			if ("Ok".equals(pgResult)) {
				result.put("Code", "200");
			} else {
				result.put("Code", "9999");
			}
		} catch (Exception ex) {
			result.put("Code", "9999");
			result.put("Message", "System error.");
		}

		return result;
	}

	/**
	 * 결제 취소 - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> cancel(PayBean payBean) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			// make base query : SecurityKey 앞의 필드는 MD5 Hash 암호화
			StringBuffer querySb = new StringBuffer();
			querySb.append("MerchantId=").append(payBean.getMerchantID());
			querySb.append("&TransactionId=").append(payBean.getTransactionId());

			// set SecurityKey (parameters -> MD5 hash)
			logger.debug("PG request data(before) : {}", querySb.toString());
			String hash = PayOnline.getSecurityKey(payBean, querySb.toString());
			logger.debug("PG request hash : {}", hash);
			querySb.append("&SecurityKey=").append(hash);

			// get result data
			logger.info("PG request data : {}", querySb.toString());
			String pgData = PayUtil.requestHttps(URL_VOID_METHOD, querySb.toString(), "POST");
			logger.info("PG respose data(String) : {}", pgData);

			// parse result data
			result = PayUtil.parseParams(pgData);
			logger.debug("PG respose data(Map) : {}", result);

			// check result
			String pgResult = (String) result.get("Result"); // Ok or Error
			logger.debug("PG respose result : {}", pgResult);
			if ("Ok".equals(pgResult)) {
				result.put("Code", "200");
			} else {
				result.put("Code", "9999");
			}
		} catch (Exception ex) {
			result.put("Code", "9999");
			result.put("Message", "System error.");
		}

		return result;
	}
	
	/**
	 * pay back - Code 200인 경우 정상
	 * 
	 * @param payBean
	 * @return
	 */
	public static HashMap<String, String> refund(PayBean payBean) {
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			// make base query : SecurityKey 앞의 필드는 MD5 Hash 암호화
			StringBuffer querySb = new StringBuffer();
			querySb.append("MerchantId=").append(payBean.getMerchantID());
			querySb.append("&TransactionId=").append(payBean.getTransactionId());
			querySb.append("&Amount=").append(PayUtil.normalizedCurrencyFractionDigits(payBean.getCurrency(), payBean.getAmount()));

			// set SecurityKey (parameters -> MD5 hash)
			logger.debug("PG request data(before) : {}", querySb.toString());
			String hash = PayOnline.getSecurityKey(payBean, querySb.toString());
			logger.debug("PG request hash : {}", hash);
			querySb.append("&SecurityKey=").append(hash);

			// get result data
			logger.info("PG request data : {}", querySb.toString());
			String pgData = PayUtil.requestHttps(URL_REFUND_METHOD, querySb.toString(), "POST");
			logger.info("PG respose data(String) : {}", pgData);

			// parse result data
			result = PayUtil.parseParams(pgData);
			logger.debug("PG respose data(Map) : {}", result);

			// check result
			String pgResult = (String) result.get("Result"); // Ok or Error
			logger.debug("PG respose result : {}", pgResult);
			if ("Ok".equals(pgResult)) {
				result.put("Code", "200");
			} else {
				result.put("Code", "9999");
			}
		} catch (Exception ex) {
			result.put("Code", "9999");
			result.put("Message", "System error.");
		}

		return result;
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String transactionId = "94408465";
		
		PayBean payBean = new PayBean();
		payBean.setMerchantID(MERCHANT_ID);
		payBean.setPrivateKey(PRIVATE_SECURITY_KEY);

		/* checkCardAuth */
		payBean.setCardHolderName("Test");
		payBean.setCardNumber("4111111111111111");
		payBean.setCardExpDate("1224");
		payBean.setCardCvv("123");

		PayOnline.checkCardAuth(payBean);
		
		/* purchase */
		payBean.setOrderId(Long.toString(System.currentTimeMillis()));
		payBean.setAmount(10.00);
		payBean.setCurrency("RUB");
		payBean.setCardHolderName("Test");
		payBean.setCardNumber("4111111111111111");
		payBean.setCardExpDate("1224");
		payBean.setCardCvv("123");

		//PayOnline.purchase(payBean);

		/* complete */
		payBean.setTransactionId(transactionId);
		payBean.setAmount(10.00);

		//PayOnline.complete(payBean);
		
		/* cancel */
		payBean.setTransactionId(transactionId);
		payBean.setAmount(10.00);

		//PayOnline.cancel(payBean);
		
		/* refund */
		payBean.setTransactionId(transactionId);
		payBean.setAmount(10.00);

		//PayOnline.refund(payBean);
	}
}
