package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. M_GOODS_INFO : 상품 정보 옵션 상세
 * 2016. 8. 29, hyo, create
 */
public class PosMasterGoodsOptDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "NO_OPTION_DTL")
	private Long noOptionDtl;

	@JsonProperty(value = "NM_OPTION_DTL")
	private String nmOptionDtl;

	@JsonProperty(value = "PR_OPTION")
	private Double prOption;

	public Long getNoOptionDtl() {
		return noOptionDtl;
	}

	public void setNoOptionDtl(Long noOptionDtl) {
		this.noOptionDtl = noOptionDtl;
	}

	public String getNmOptionDtl() {
		return nmOptionDtl;
	}

	public void setNmOptionDtl(String nmOptionDtl) {
		this.nmOptionDtl = nmOptionDtl;
	}

	public Double getPrOption() {
		return prOption;
	}

	public void setPrOption(Double prOption) {
		this.prOption = prOption;
	}

	@Override
	public String toString() {
		return "PosMasterGoodsOptDtl [noOptionDtl=" + noOptionDtl + ", nmOptionDtl=" + nmOptionDtl + ", prOption="
				+ prOption + "]";
	}

}
