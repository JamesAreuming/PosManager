package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 7. M_CODE_INFO : 공통 코드
 * 2016. 7. 19, green, create
 */
public class PosMasterCodeInfo implements Serializable {
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
	 * 공통구분코드 (공통코드 탭 참고)
	 */
	@JsonProperty(value="CD_GROUP")
	private Integer cdGroup;
	/**
	 * 공통상세코드 (공통코드 탭 참고)
	 */
	@JsonProperty(value="CD_CODE")
	private Integer cdCode;
	/**
	 * 명칭
	 */
	@JsonProperty(value="NM_CODE")
	private String nmCode;
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
	 * 관리항목6
	 */
	@JsonProperty(value="DATA6")
	private String data6;
	/**
	 * 관리항목7
	 */
	@JsonProperty(value="DATA7")
	private String data7;
	/**
	 * 관리항목8
	 */
	@JsonProperty(value="DATA8")
	private String data8;
	/**
	 * 관리항목9
	 */
	@JsonProperty(value="DATA9")
	private String data9;
	/**
	 * 관리항목10
	 */
	@JsonProperty(value="DATA10")
	private String data10;
	/**
	 * 사용유무 (1:사용, 0:미사용)
	 */
	@JsonProperty(value="YN_USE")
	private Integer ynUse;
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
	 * Getter 공통구분코드 (공통코드 탭 참고)
	 */
	public Integer getCdGroup() {
		return cdGroup;
	}
	/**
	 * Setter 공통구분코드 (공통코드 탭 참고)
	 * @param cdGroup 공통구분코드 (공통코드 탭 참고)
	 */
	public void setCdGroup(Integer cdGroup) {
		this.cdGroup = cdGroup;
	}
	/**
	 * Getter 공통상세코드 (공통코드 탭 참고)
	 */
	public Integer getCdCode() {
		return cdCode;
	}
	/**
	 * Setter 공통상세코드 (공통코드 탭 참고)
	 * @param cdCode 공통상세코드 (공통코드 탭 참고)
	 */
	public void setCdCode(Integer cdCode) {
		this.cdCode = cdCode;
	}
	/**
	 * Getter 명칭
	 */
	public String getNmCode() {
		return nmCode;
	}
	/**
	 * Setter 명칭
	 * @param nmCode 명칭
	 */
	public void setNmCode(String nmCode) {
		this.nmCode = nmCode;
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
	 * Getter 관리항목6
	 */
	public String getData6() {
		return data6;
	}
	/**
	 * Setter 관리항목6
	 * @param data6 관리항목6
	 */
	public void setData6(String data6) {
		this.data6 = data6;
	}
	/**
	 * Getter 관리항목7
	 */
	public String getData7() {
		return data7;
	}
	/**
	 * Setter 관리항목7
	 * @param data7 관리항목7
	 */
	public void setData7(String data7) {
		this.data7 = data7;
	}
	/**
	 * Getter 관리항목8
	 */
	public String getData8() {
		return data8;
	}
	/**
	 * Setter 관리항목8
	 * @param data8 관리항목8
	 */
	public void setData8(String data8) {
		this.data8 = data8;
	}
	/**
	 * Getter 관리항목9
	 */
	public String getData9() {
		return data9;
	}
	/**
	 * Setter 관리항목9
	 * @param data9 관리항목9
	 */
	public void setData9(String data9) {
		this.data9 = data9;
	}
	/**
	 * Getter 관리항목10
	 */
	public String getData10() {
		return data10;
	}
	/**
	 * Setter 관리항목10
	 * @param data10 관리항목10
	 */
	public void setData10(String data10) {
		this.data10 = data10;
	}
	/**
	 * Getter 사용유무 (1:사용, 0:미사용)
	 */
	public Integer getYnUse() {
		return ynUse;
	}
	/**
	 * Setter 사용유무 (1:사용, 0:미사용)
	 * @param ynUse 사용유무 (1:사용, 0:미사용)
	 */
	public void setYnUse(Integer ynUse) {
		this.ynUse = ynUse;
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
