package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * 주문 항목 옵션
 * 포스연동
 *
 */
public class PosSalesOrderInfoOption {

	/**
	 * 일련 번호
	 */
	@JsonProperty(value = "SEQ1")
	private Long seq;

	/**
	 * 옵션 번호
	 */
	@JsonProperty(value = "NO_OPTION")
	private Long noOpt;

	/**
	 * 옵션 성세 번호
	 */
	@JsonProperty(value = "NO_OPTION_DTL")
	private Long noOptDtl;

	/**
	 * 옵션 이름
	 */
	@JsonProperty(value = "NM_OPTION")
	private String nmOpt;

	/**
	 * 옵션 상세 이름
	 */
	@JsonProperty(value = "NM_OPTION_DTL")
	private String nmOptDtl;

	/**
	 * 옵션 가격
	 */
	@JsonProperty(value = "PR_OPTION")
	private Double prOpt;

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getNoOpt() {
		return noOpt;
	}

	public void setNoOpt(Long noOpt) {
		this.noOpt = noOpt;
	}

	public Long getNoOptDtl() {
		return noOptDtl;
	}

	public void setNoOptDtl(Long noOptDtl) {
		this.noOptDtl = noOptDtl;
	}

	public String getNmOpt() {
		return nmOpt;
	}

	public void setNmOpt(String nmOpt) {
		this.nmOpt = nmOpt;
	}

	public String getNmOptDtl() {
		return nmOptDtl;
	}

	public void setNmOptDtl(String nmOptDtl) {
		this.nmOptDtl = nmOptDtl;
	}

	public Double getPrOpt() {
		return prOpt;
	}

	public void setPrOpt(Double prOpt) {
		this.prOpt = prOpt;
	}

	@Override
	public String toString() {
		return "PosSalesOrderInfoOption [seq=" + seq + ", noOpt=" + noOpt + ", noOptDtl=" + noOptDtl + ", nmOpt="
				+ nmOpt + ", nmOptDtl=" + nmOptDtl + ", prOpt=" + prOpt + "]";
	}

}
