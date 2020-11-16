package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 포스 연동 결제 정보
 * 
 * @author hyo
 *
 */
public class PosOrderPayment {

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;

	/**
	 * 결제를 처리한 직원 번호
	 */
	@JsonProperty(value = "CD_EMPLOYEE")
	private String cdEmployee;

	/**
	 * 결제 방법 (810001: 현금, 810002: 카드, 810009: 현금+카드 그외 기타)
	 */
	@JsonProperty(value = "CD_PAY_METHOD")
	private Integer cdPayMethod;

	/**
	 * 결제 카드 번호 (암호화됨)
	 */
	@JsonProperty(value = "NO_CARD")
	private String noCard;

	/**
	 * 결제 카드 번호 (암호화됨)
	 */
	@JsonProperty(value = "PR_AMOUNT")
	private Double prAmount;

	/**
	 * 결제 시간 (yyyyMMddHHmmss)
	 */
	@JsonProperty(value = "DT_PAYMENT")
	private String dtPayment;

	/**
	 * 결제 Transaction 번호
	 */
	@JsonProperty(value = "NO_TRAN")
	private String noTran;

	/**
	 * 할부 개월수
	 */
	@JsonProperty(value = "NO_MONTHLY_PLAIN")
	private Integer noMonthlyPlain;

	/**
	 * PG 종류 (356xxx)
	 */
	@JsonProperty(value = "CD_PG_KIND")
	private Integer cdPgKind;

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getCdEmployee() {
		return cdEmployee;
	}

	public void setCdEmployee(String cdEmployee) {
		this.cdEmployee = cdEmployee;
	}

	public Integer getCdPayMethod() {
		return cdPayMethod;
	}

	public void setCdPayMethod(Integer cdPayMethod) {
		this.cdPayMethod = cdPayMethod;
	}

	public String getNoCard() {
		return noCard;
	}

	public void setNoCard(String noCard) {
		this.noCard = noCard;
	}

	public Double getPrAmount() {
		return prAmount;
	}

	public void setPrAmount(Double prAmount) {
		this.prAmount = prAmount;
	}

	public String getDtPayment() {
		return dtPayment;
	}

	public void setDtPayment(String dtPayment) {
		this.dtPayment = dtPayment;
	}

	public String getNoTran() {
		return noTran;
	}

	public void setNoTran(String noTran) {
		this.noTran = noTran;
	}

	public Integer getNoMonthlyPlain() {
		return noMonthlyPlain;
	}

	public void setNoMonthlyPlain(Integer noMonthlyPlain) {
		this.noMonthlyPlain = noMonthlyPlain;
	}

	public Integer getCdPgKind() {
		return cdPgKind;
	}

	public void setCdPgKind(Integer cdPgKind) {
		this.cdPgKind = cdPgKind;
	}

	@Override
	public String toString() {
		return "PosOrderPayment [seq=" + seq + ", cdEmployee=" + cdEmployee + ", cdPayMethod=" + cdPayMethod + ", noCard=" + noCard + ", prAmount="
				+ prAmount + ", dtPayment=" + dtPayment + ", noTran=" + noTran + ", noMonthlyPlain=" + noMonthlyPlain + ", cdPgKind=" + cdPgKind
				+ "]";
	}

}
