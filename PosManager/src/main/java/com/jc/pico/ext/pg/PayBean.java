/**
 * <pre>
 * Filename	: PayBean.java
 * Function	: payment bean
 * Comment 	:
 * History	:
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.ext.pg;

public class PayBean {

	private String pgKind = null; // PG사 종류(공통코드 356), PayOnline, BAK_pg, etc.
	
	private String merchantID = null; // PG 정보, merchant ID
	private String privateKey = null; // PG 정보, private key
	private String paymentKey = null; // PG 정보, payment key
	
	private String payMethod = null; // 결제수단, CARD
	private String transactionId = null; // PG사 거래 ID (결제취소용)
	private String orderId = null; // 주문 ID
	private double amount = 0; // 금액
	private String currency = null; // 통화기호, ISO 3자리
	private String cardHolderName = null; // 카드 소유자명 (카드에 명기된 성명)
	private String cardNumber = null; // 카드번호
	private String cardExpDate = null; // 유효기한(MMYY)
	private String cardCvv = null; // CVC/CVV 3자리
	private String country = null; // 고객 거주 나라 (ISO 3116 alphabet 2)
	
	public void setPgKind(String pgKind) {this.pgKind = pgKind;}
	public String getPgKind() {return this.pgKind;}
	
	public void setMerchantID(String merchantID) {this.merchantID = merchantID;}
	public String getMerchantID() {return this.merchantID;}
	
	public void setPrivateKey(String privateKey) {this.privateKey = privateKey;}
	public String getPrivateKey() {return this.privateKey;}
	
	public void setPaymentKey(String paymentKey) {this.paymentKey = paymentKey;}
	public String getPaymentKey() {return this.paymentKey;}
	
	public void setPayMethod(String payMethod) {this.payMethod = payMethod;}
	public String getPayMethod() {return this.payMethod;}
	
	public void setTransactionId(String transactionId) {this.transactionId = transactionId;}
	public String getTransactionId() {return this.transactionId;}
	
	public void setOrderId(String orderId) {this.orderId = orderId;}
	public String getOrderId() {return this.orderId;}
	
	public void setAmount(double amount) {this.amount = amount;}
	public double getAmount() {return this.amount;}
	
	public void setCurrency(String currency) {this.currency = currency;}
	public String getCurrency() {return this.currency;}
	
	public void setCardHolderName(String cardHolderName) {this.cardHolderName = cardHolderName;}
	public String getCardHolderName() {return this.cardHolderName;}
	
	public void setCardNumber(String cardNumber) {this.cardNumber = cardNumber;}
	public String getCardNumber() {return this.cardNumber;}
	
	public void setCardExpDate(String cardExpDate) {this.cardExpDate = cardExpDate;}
	public String getCardExpDate() {return this.cardExpDate;}
	
	public void setCardCvv(String cardCvv) {this.cardCvv = cardCvv;}
	public String getCardCvv() {return this.cardCvv;}
	
	public String getCountry() {return country;}
	public void setCountry(String country) {this.country = country;}
	
}
