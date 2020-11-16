package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. S_SALE_SAVE : 매출정보
 * 2016. 8. 13, green, create
 */
/**
 * @author hyo
 *
 */
public class PosSalesSaleSavePay implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;

	/**
	 * 분할 결제 일련번호
	 */
	@JsonProperty(value = "SEQ_PARTIAL")
	private Long seq_partial;

	/**
	 * 판매형태코드(일반, 폐기, 서비스, 자가소비, 증정, 접대, 손실, 스탬프, 상품교환(단품반품))
	 */
	@JsonProperty(value = "DS_SALE")
	private Integer dsSale;

	/**
	 * 결제형태코드
	 */
	@JsonProperty(value = "CD_PAY")
	private Integer cdPay;

	/**
	 * 금액
	 */
	@JsonProperty(value = "AMT_PAY")
	private Double amtPay;

	/**
	 * 거스름 여부
	 */
	@JsonProperty(value = "YN_CHANGE")
	private Integer ynChange;

	/**
	 * 바코드
	 */
	@JsonProperty(value = "BARCODE")
	private String barcode;

	/**
	 * PG 종류 (356xxx)
	 */
	@JsonProperty(value = "CD_PG_KIND")
	private Integer cdPgKind;

	/**
	 * Getter 일련번호
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * Setter 일련번호
	 * 
	 * @param seq
	 *            일련번호
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getSeq_partial() {
		return seq_partial;
	}

	public void setSeq_partial(Long seq_partial) {
		this.seq_partial = seq_partial;
	}

	public Integer getDsSale() {
		return dsSale;
	}

	public void setDsSale(Integer dsSale) {
		this.dsSale = dsSale;
	}

	/**
	 * Getter 결제형태코드
	 */
	public Integer getCdPay() {
		return cdPay;
	}

	/**
	 * Setter 결제형태코드
	 * 
	 * @param cdPay
	 *            결제형태코드
	 */
	public void setCdPay(Integer cdPay) {
		this.cdPay = cdPay;
	}

	/**
	 * Getter 금액
	 */
	public Double getAmtPay() {
		return amtPay;
	}

	/**
	 * Setter 금액
	 * 
	 * @param amtPay
	 *            금액
	 */
	public void setAmtPay(Double amtPay) {
		this.amtPay = amtPay;
	}

	/**
	 * Getter 거스름 여부
	 */
	public Integer getYnChange() {
		return ynChange;
	}

	/**
	 * Setter 거스름 여부
	 * 
	 * @param ynChange
	 *            거스름 여부
	 */
	public void setYnChange(Integer ynChange) {
		this.ynChange = ynChange;
	}

	/**
	 * Getter 바코드
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * Setter 바코드
	 * 
	 * @param barcode
	 *            바코드
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getCdPgKind() {
		return cdPgKind;
	}

	public void setCdPgKind(Integer cdPgKind) {
		this.cdPgKind = cdPgKind;
	}

}
