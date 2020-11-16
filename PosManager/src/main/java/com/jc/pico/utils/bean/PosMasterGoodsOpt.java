package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. M_GOODS_INFO : 상품 정보 옵션
 * 2016. 8. 29, hyo, create
 */
public class PosMasterGoodsOpt implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 옵션 코드
	 */
	@JsonProperty(value = "NO_OPTION")
	private Long noOption;

	/**
	 * 사용 여부
	 */
	@JsonProperty(value = "YN_USE")
	private Integer ynUse;

	/**
	 * 옵션명
	 */
	@JsonProperty(value = "NM_OPTION")
	private String nmOption;

	/**
	 * 옵션 설명
	 */
	@JsonProperty(value = "MEMO")
	private String memo;

	/**
	 * 필수 여부
	 */
	@JsonProperty(value = "YN_MANDATORY")
	private Integer ynMandatory;

	/**
	 * 선택 가능 갯수
	 */
	@JsonProperty(value = "QTY_OPTION")
	private Integer qtyOption;

	/**
	 * 옵션 상세 정보
	 */
	@JsonProperty(value = "DETAILS")
	private List<PosMasterGoodsOptDtl> details;

	public Long getNoOption() {
		return noOption;
	}

	public void setNoOption(Long noOption) {
		this.noOption = noOption;
	}

	public Integer getYnUse() {
		return ynUse;
	}

	public void setYnUse(Integer ynUse) {
		this.ynUse = ynUse;
	}

	public String getNmOption() {
		return nmOption;
	}

	public void setNmOption(String nmOption) {
		this.nmOption = nmOption;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getYnMandatory() {
		return ynMandatory;
	}

	public void setYnMandatory(Integer ynMandatory) {
		this.ynMandatory = ynMandatory;
	}

	public Integer getQtyOption() {
		return qtyOption;
	}

	public void setQtyOption(Integer qtyOption) {
		this.qtyOption = qtyOption;
	}

	public List<PosMasterGoodsOptDtl> getDetails() {
		return details;
	}

	public void setDetails(List<PosMasterGoodsOptDtl> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "PosMasterGoodsOpt [noOption=" + noOption + ", ynUse=" + ynUse + ", nmOption=" + nmOption + ", memo="
				+ memo + ", ynMandatory=" + ynMandatory + ", qtyOption=" + qtyOption + ", details=" + details + "]";
	}
	
}
