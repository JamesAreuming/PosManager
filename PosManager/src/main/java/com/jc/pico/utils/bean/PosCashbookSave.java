package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosCashbookSave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * 날짜
	 */
	@JsonProperty(value = "YMD_SALE")
	private String ymdSale;

	/**
	 * 포스번호
	 */
	@JsonProperty(value = "NO_POS")
	private String noPos;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Integer seq;

	/**
	 * 계정과목(1~11, 공통코드 30번 참조)
	 */
	@JsonProperty(value = "CD_ACCOUNT")
	private Integer cdAccount;

	/**
	 * 출납유형코드(1: 입금, 2: 출금)
	 */
	@JsonProperty(value = "CD_CASHBOOK_TYPE")
	private Integer cdCashbookType;

	/**
	 * 금액
	 */
	@JsonProperty(value = "AMT_CASHBOOK")
	private Double amtCashbook;

	/**
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

	public String getYmdSale() {
		return ymdSale;
	}

	public void setYmdSale(String ymdSale) {
		this.ymdSale = ymdSale;
	}

	public String getNoPos() {
		return noPos;
	}

	public void setNoPos(String noPos) {
		this.noPos = noPos;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getCdAccount() {
		return cdAccount;
	}

	public void setCdAccount(Integer cdAccount) {
		this.cdAccount = cdAccount;
	}

	public Integer getCdCashbookType() {
		return cdCashbookType;
	}

	public void setCdCashbookType(Integer cdCashbookType) {
		this.cdCashbookType = cdCashbookType;
	}

	public Double getAmtCashbook() {
		return amtCashbook;
	}

	public void setAmtCashbook(Double amtCashbook) {
		this.amtCashbook = amtCashbook;
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

	@Override
	public String toString() {
		return "PosCashbookSave [cdCompany=" + cdCompany + ", cdStore=" + cdStore + ", ymdSale=" + ymdSale + ", noPos="
				+ noPos + ", seq=" + seq + ", cdAccount=" + cdAccount + ", cdCashbookType=" + cdCashbookType
				+ ", amtCashbook=" + amtCashbook + ", memo=" + memo + ", dtInsert=" + dtInsert + ", cdEmployeeInsert="
				+ cdEmployeeInsert + ", dtUpdate=" + dtUpdate + ", cdEmployeeUpdate=" + cdEmployeeUpdate + "]";
	}

}
