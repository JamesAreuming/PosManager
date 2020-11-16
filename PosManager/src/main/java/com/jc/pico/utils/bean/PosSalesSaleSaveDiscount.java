package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. S_SALE_SAVE : 매출정보
 * 2016. 8. 13, green, create
 */
public class PosSalesSaleSaveDiscount implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;
	/**
	 * 할인TYPE코드
	 */
	@JsonProperty(value = "CD_DC_TYPE")
	private Integer cdDcType;
	/**
	 * 할인번호
	 */
	@JsonProperty(value = "NO_DC")
	private Long noDc;

	/**
	 * 쿠폰 번호
	 */
	@JsonProperty(value = "NO_COUPON")
	private String noCoupon;

	/**
	 * 금액여부
	 */
	@JsonProperty(value = "DS_APPLY")
	private Integer dsApply;
	/**
	 * 셋팅금액
	 */
	@JsonProperty(value = "AMT_SET")
	private Double amtSet;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 할인부가번호
	 */
	@JsonProperty(value = "DC_NO_ADD")
	private String dcNoAdd;

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

	/**
	 * Getter 할인TYPE코드
	 */
	public Integer getCdDcType() {
		return cdDcType;
	}

	/**
	 * Setter 할인TYPE코드
	 * 
	 * @param cdDcType
	 *            할인TYPE코드
	 */
	public void setCdDcType(Integer cdDcType) {
		this.cdDcType = cdDcType;
	}

	/**
	 * Getter 할인번호
	 */
	public Long getNoDc() {
		return noDc;
	}

	/**
	 * Setter 할인번호
	 * 
	 * @param noDc
	 *            할인번호
	 */
	public void setNoDc(Long noDc) {
		this.noDc = noDc;
	}

	/**
	 * Getter 금액여부
	 */
	public Integer getDsApply() {
		return dsApply;
	}

	/**
	 * Setter 금액여부
	 * 
	 * @param dsApply
	 *            금액여부
	 */
	public void setDsApply(Integer dsApply) {
		this.dsApply = dsApply;
	}

	/**
	 * Getter 셋팅금액
	 */
	public Double getAmtSet() {
		return amtSet;
	}

	/**
	 * Setter 셋팅금액
	 * 
	 * @param amtSet
	 *            셋팅금액
	 */
	public void setAmtSet(Double amtSet) {
		this.amtSet = amtSet;
	}

	/**
	 * Getter 할인금액
	 */
	public Double getAmtDc() {
		return amtDc;
	}

	/**
	 * Setter 할인금액
	 * 
	 * @param amtDc
	 *            할인금액
	 */
	public void setAmtDc(Double amtDc) {
		this.amtDc = amtDc;
	}

	/**
	 * Getter 할인부가번호
	 */
	public String getDcNoAdd() {
		return dcNoAdd;
	}

	/**
	 * Setter 할인부가번호
	 * 
	 * @param dcNoAdd
	 *            할인부가번호
	 */
	public void setDcNoAdd(String dcNoAdd) {
		this.dcNoAdd = dcNoAdd;
	}

	public String getNoCoupon() {
		return noCoupon;
	}

	public void setNoCoupon(String noCoupon) {
		this.noCoupon = noCoupon;
	}

}
