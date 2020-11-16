package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 출납부 정보
 * S_CASHBOOK_INFO response bean
 * 
 * 
 * @author hyo 2016.08.30
 *
 */
public class PosCashBookInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 출납부 코드 (신규일 경우 값 없음)
	 */
	@JsonProperty(value = "CD_CASHBOOK")
	private Long cdCashbook;

	/**
	 * 계정 과목 (1 ~ 11, 공통코드 30번 참조)
	 */
	@JsonProperty(value = "CD_ACCOUNT")
	private Integer cdAccount;

	/**
	 * 구분
	 * 1: 입금, 2: 출금
	 */
	@JsonProperty(value = "DS_ACCOUNT")
	private Integer dsAccount;

	/**
	 * 금액
	 */
	@JsonProperty(value = "PR_AMOUNT")
	private Double prAmount;

	/*
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;

	/**
	 * 등록일시
	 */
	@JsonProperty(value = "DT_INSERT")
	private String dtInsert;

	/**
	 * 등록자
	 */
	@JsonProperty(value = "CD_EMPLOYEE_INSERT")
	private String cdEmployeeInsert;

	/**
	 * 수정일시
	 */
	@JsonProperty(value = "DT_UPDATE")
	private String dtUpdate;

	/**
	 * 수정자
	 */
	@JsonProperty(value = "CD_EMPLOYEE_UPDATE")
	private String cdEmployeeUpdate;

	public Long getCdCashbook() {
		return cdCashbook;
	}

	public void setCdCashbook(Long cdCashbook) {
		this.cdCashbook = cdCashbook;
	}

	public Integer getCdAccount() {
		return cdAccount;
	}

	public void setCdAccount(Integer cdAccount) {
		this.cdAccount = cdAccount;
	}

	public Integer getDsAccount() {
		return dsAccount;
	}

	public void setDsAccount(Integer dsAccount) {
		this.dsAccount = dsAccount;
	}

	public Double getPrAmount() {
		return prAmount;
	}

	public void setPrAmount(Double prAmount) {
		this.prAmount = prAmount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDtInsert() {
		return dtInsert;
	}

	public void setDtInsert(String dtInsert) {
		this.dtInsert = dtInsert;
	}

	public String getCdEmployeeInsert() {
		return cdEmployeeInsert;
	}

	public void setCdEmployeeInsert(String cdEmployeeInsert) {
		this.cdEmployeeInsert = cdEmployeeInsert;
	}

	public String getDtUpdate() {
		return dtUpdate;
	}

	public void setDtUpdate(String dtUpdate) {
		this.dtUpdate = dtUpdate;
	}

	public String getCdEmployeeUpdate() {
		return cdEmployeeUpdate;
	}

	public void setCdEmployeeUpdate(String cdEmployeeUpdate) {
		this.cdEmployeeUpdate = cdEmployeeUpdate;
	}

}
