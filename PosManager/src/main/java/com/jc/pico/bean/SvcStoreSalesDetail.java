package com.jc.pico.bean;

import java.util.Date;

public class SvcStoreSalesDetail {
	private String payMethod;		// 결제 방법
	private String payMethodNm;
	private Date payTmLocal;			// 결제 시간
	private String cardNo;				// 카드 번호
	private String cardInfo;				// 카드사
	private int monthlyPlain;			// 할부
	private String tranNo;				// 승인 번호
	private String pgKind;				// PG
	public String getPayMethod() {
		return payMethod;
	}
	public Date getPayTmLocal() {
		return payTmLocal;
	}
	public String getCardNo() {
		return cardNo;
	}
	public String getCardInfo() {
		return cardInfo;
	}
	public int getMonthlyPlain() {
		return monthlyPlain;
	}
	public String getTranNo() {
		return tranNo;
	}
	public String getPgKind() {
		return pgKind;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public void setPayTmLocal(Date payTmLocal) {
		this.payTmLocal = payTmLocal;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public void setCardInfo(String cardInfo) {
		this.cardInfo = cardInfo;
	}
	public void setMonthlyPlain(int monthlyPlain) {
		this.monthlyPlain = monthlyPlain;
	}
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}
	public void setPgKind(String pgKind) {
		this.pgKind = pgKind;
	}
	public String getPayMethodNm() {
		return payMethodNm;
	}
	public void setPayMethodNm(String payMethodNm) {
		this.payMethodNm = payMethodNm;
	}
	@Override
	public String toString() {
		return "SvcStoreSalesDetail [payMethod=" + payMethod + ", payMethodNm=" + payMethodNm + ", payTmLocal="
				+ payTmLocal + ", cardNo=" + cardNo + ", cardInfo=" + cardInfo + ", monthlyPlain=" + monthlyPlain
				+ ", tranNo=" + tranNo + ", pgKind=" + pgKind + "]";
	}
}
