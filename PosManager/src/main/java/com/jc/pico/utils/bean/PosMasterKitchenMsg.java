/*
 * Filename	: PosMasterGoodsClass.java
 * Function	:
 * Comment 	:
 * History	:
 *
 * Version	: 1.0
 * Author   :
 */

package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 33. M_KITCHEN_MSG : 주방 메모
 * 2016. 12. 09
 */
public class PosMasterKitchenMsg implements Serializable {

	private final static long serialVersionUID = 1L;

	/**
	 * 주방 메시지 코드
	 */
	@JsonProperty(value = "CD_KITCHEN_MSG")
	private Long cdKitchenMsg;

	/**
	 * 회사코드
	 */
	@JsonProperty(value = "CD_COMPANY")
	private String cdCompany;

	/**
	 * 매장코드
	 */
	@JsonProperty(value = "CD_STORE")
	private String cdStore;

	/**
	 * 상품 분류 코드
	 */
	@JsonProperty(value = "CD_CLASS")
	private String cdClass;

	/**
	 * 정렬 순서
	 */
	@JsonProperty(value = "ORDINAL")
	private Integer ordinal;

	/**
	 * 메모 내용
	 */
	@JsonProperty(value = "MESSAGE")
	private String message;

	public String getCdCompany() {
		return cdCompany;
	}

	public void setCdCompany(String cdCompany) {
		this.cdCompany = cdCompany;
	}

	public String getCdStore() {
		return cdStore;
	}

	public void setCdStore(String cdStore) {
		this.cdStore = cdStore;
	}

	public String getCdClass() {
		return cdClass;
	}

	public void setCdClass(String cdClass) {
		this.cdClass = cdClass;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
