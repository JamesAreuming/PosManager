package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 5. M_CONFIG_INFO : 환경설정
 * 2016. 7. 21, green, create
 */
public class PosMasterConfigInfo implements Serializable {
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
	 * 포스번호
	 */
	@JsonProperty(value="NO_POS")
	private String noPos;
	/**
	 * 항목코드 (환경설정 항목코드 탭 참고)
	 */
	@JsonProperty(value="CD_CONFIG")
	private Integer cdConfig;
	/**
	 * 설정값
	 */
	@JsonProperty(value="NM_VALUE")
	private String nmValue;
	/**
	 * 관리항목1
	 */
	@JsonProperty(value="DATA1")
	private String data1;
	/**
	 * 관리항목2
	 */
	@JsonProperty(value="DATA2")
	private String data2;
	/**
	 * 관리항목3
	 */
	@JsonProperty(value="DATA3")
	private String data3;
	/**
	 * 관리항목4
	 */
	@JsonProperty(value="DATA4")
	private String data4;
	/**
	 * 관리항목5
	 */
	@JsonProperty(value="DATA5")
	private String data5;
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
	 * Getter 포스번호
	 */
	public String getNoPos() {
		return noPos;
	}
	/**
	 * Setter 포스번호
	 * @param noPos 포스번호
	 */
	public void setNoPos(String noPos) {
		this.noPos = noPos;
	}
	/**
	 * Getter 항목코드 (환경설정 항목코드 탭 참고)
	 */
	public Integer getCdConfig() {
		return cdConfig;
	}
	/**
	 * Setter 항목코드 (환경설정 항목코드 탭 참고)
	 * @param cdConfig 항목코드 (환경설정 항목코드 탭 참고)
	 */
	public void setCdConfig(Integer cdConfig) {
		this.cdConfig = cdConfig;
	}
	/**
	 * Getter 설정값
	 */
	public String getNmValue() {
		return nmValue;
	}
	/**
	 * Setter 설정값
	 * @param nmValue 설정값
	 */
	public void setNmValue(String nmValue) {
		this.nmValue = nmValue;
	}
	/**
	 * Getter 관리항목1
	 */
	public String getData1() {
		return data1;
	}
	/**
	 * Setter 관리항목1
	 * @param data1 관리항목1
	 */
	public void setData1(String data1) {
		this.data1 = data1;
	}
	/**
	 * Getter 관리항목2
	 */
	public String getData2() {
		return data2;
	}
	/**
	 * Setter 관리항목2
	 * @param data2 관리항목2
	 */
	public void setData2(String data2) {
		this.data2 = data2;
	}
	/**
	 * Getter 관리항목3
	 */
	public String getData3() {
		return data3;
	}
	/**
	 * Setter 관리항목3
	 * @param data3 관리항목3
	 */
	public void setData3(String data3) {
		this.data3 = data3;
	}
	/**
	 * Getter 관리항목4
	 */
	public String getData4() {
		return data4;
	}
	/**
	 * Setter 관리항목4
	 * @param data4 관리항목4
	 */
	public void setData4(String data4) {
		this.data4 = data4;
	}
	/**
	 * Getter 관리항목5
	 */
	public String getData5() {
		return data5;
	}
	/**
	 * Setter 관리항목5
	 * @param data5 관리항목5
	 */
	public void setData5(String data5) {
		this.data5 = data5;
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
