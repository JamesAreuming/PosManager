package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 16. M_PLU_TYPE : PLU 타입
 * 2016. 7. 21, green, create
 */
public class PosMasterPluType implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 회사코드
	 */
	@JsonProperty(value="CD_COMPANY")
	private String cdCompany;
	/**
	 * 매장코드
	 */
	@JsonProperty(value="CD_STORE")
	private String cdStore;
	/**
	 * PLU TYPE
	 */
	@JsonProperty(value="CD_PLU_TYPE")
	private Long cdPluType;
	/**
	 * 명칭
	 */
	@JsonProperty(value="NM_PLU_TYPE")
	private String nmPluType;
	/**
	 * 대분류라인수
	 */
	@JsonProperty(value="ROW_GROUP")
	private Integer rowGroup;
	/**
	 * 중분류라인수
	 */
	@JsonProperty(value="ROW_MID")
	private Integer rowMid;
	/**
	 * 소분류라인수
	 */
	@JsonProperty(value="ROW_SMALL")
	private Integer rowSmall;
	/**
	 * 분류단계
	 */
	@JsonProperty(value="CD_DEPTH")
	private Integer cdDepth;
	/**
	 * 팝업PLU사용유무
	 */
	@JsonProperty(value="YN_POPUP")
	private Integer ynPopup;
	/**
	 * 등록일시
	 */
	@JsonProperty(value="DT_INSERT")
	private String dtInsert;
	/**
	 * 등록자
	 */
	@JsonProperty(value="CD_EMPLOYEE_INSERT")
	private String cdEmployeeInsert;
	/**
	 * 수정일시
	 */
	@JsonProperty(value="DT_UPDATE")
	private String dtUpdate;
	/**
	 * 수정자
	 */
	@JsonProperty(value="CD_EMPLOYEE_UPDATE")
	private String cdEmployeeUpdate;
	/**
	 * Getter 회사코드
	 */
	public String getCdCompany() {
		return cdCompany;
	}
	/**
	 * Setter 회사코드
	 * @param cdCompany 회사코드
	 */
	public void setCdCompany(String cdCompany) {
		this.cdCompany = cdCompany;
	}
	/**
	 * Getter 매장코드
	 */
	public String getCdStore() {
		return cdStore;
	}
	/**
	 * Setter 매장코드
	 * @param cdStore 매장코드
	 */
	public void setCdStore(String cdStore) {
		this.cdStore = cdStore;
	}
	/**
	 * Getter PLU TYPE
	 */
	public Long getCdPluType() {
		return cdPluType;
	}
	/**
	 * Setter PLU TYPE
	 * @param cdType PLU TYPE
	 */
	public void setCdType(Long cdType) {
		this.cdPluType = cdType;
	}
	/**
	 * Getter 명칭
	 */
	public String getNmPluType() {
		return nmPluType;
	}
	/**
	 * Setter 명칭
	 * @param nmType 명칭
	 */
	public void setNmPluType(String nmType) {
		this.nmPluType = nmType;
	}
	/**
	 * Getter 대분류라인수
	 */
	public Integer getRowGroup() {
		return rowGroup;
	}
	/**
	 * Setter 대분류라인수
	 * @param rowGroup 대분류라인수
	 */
	public void setRowGroup(Integer rowGroup) {
		this.rowGroup = rowGroup;
	}
	/**
	 * Getter 중분류라인수
	 */
	public Integer getRowMid() {
		return rowMid;
	}
	/**
	 * Setter 중분류라인수
	 * @param rowMid 중분류라인수
	 */
	public void setRowMid(Integer rowMid) {
		this.rowMid = rowMid;
	}
	/**
	 * Getter 소분류라인수
	 */
	public Integer getRowSmall() {
		return rowSmall;
	}
	/**
	 * Setter 소분류라인수
	 * @param rowSmall 소분류라인수
	 */
	public void setRowSmall(Integer rowSmall) {
		this.rowSmall = rowSmall;
	}
	/**
	 * Getter 분류단계
	 */
	public Integer getCdDepth() {
		return cdDepth;
	}
	/**
	 * Setter 분류단계
	 * @param cdDepth 분류단계
	 */
	public void setCdDepth(Integer cdDepth) {
		this.cdDepth = cdDepth;
	}
	/**
	 * Getter 팝업PLU사용유무
	 */
	public Integer getYnPopup() {
		return ynPopup;
	}
	/**
	 * Setter 팝업PLU사용유무
	 * @param ynPopup 팝업PLU사용유무
	 */
	public void setYnPopup(Integer ynPopup) {
		this.ynPopup = ynPopup;
	}
	/**
	 * Getter 등록일시
	 */
	public String getDtInsert() {
		return dtInsert;
	}
	/**
	 * Setter 등록일시
	 * @param dtInsert 등록일시
	 */
	public void setDtInsert(String dtInsert) {
		this.dtInsert = dtInsert;
	}
	/**
	 * Getter 등록자
	 */
	public String getCdEmployeeInsert() {
		return cdEmployeeInsert;
	}
	/**
	 * Setter 등록자
	 * @param cdEmployeeInsert 등록자
	 */
	public void setCdEmployeeInsert(String cdEmployeeInsert) {
		this.cdEmployeeInsert = cdEmployeeInsert;
	}
	/**
	 * Getter 수정일시
	 */
	public String getDtUpdate() {
		return dtUpdate;
	}
	/**
	 * Setter 수정일시
	 * @param dtUpdate 수정일시
	 */
	public void setDtUpdate(String dtUpdate) {
		this.dtUpdate = dtUpdate;
	}
	/**
	 * Getter 수정자
	 */
	public String getCdEmployeeUpdate() {
		return cdEmployeeUpdate;
	}
	/**
	 * Setter 수정자
	 * @param cdEmployeeUpdate 수정자
	 */
	public void setCdEmployeeUpdate(String cdEmployeeUpdate) {
		this.cdEmployeeUpdate = cdEmployeeUpdate;
	}
}
