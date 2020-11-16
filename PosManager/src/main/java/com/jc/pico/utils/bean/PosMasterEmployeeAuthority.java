package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 8. M_EMPLOYEE_AUTHORITY : 사원 권한
 * 2016. 7. 21, green, create
 */
public class PosMasterEmployeeAuthority implements Serializable {
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
	 * 사원코드
	 */
	@JsonProperty(value="CD_EMPLOYEE")
	private String cdEmployee;
	/**
	 * 포스접근차단기능코드
	 */
	@JsonProperty(value="CD_AUTHORITY")
	private Integer cdAuthority;
	/**
	 * 포스접근차단가능유무
	 */
	@JsonProperty(value="YN_ENABLE")
	private Integer ynEnable;
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
	 * Getter 사원코드
	 */
	public String getCdEmployee() {
		return cdEmployee;
	}
	/**
	 * Setter 사원코드
	 * @param cdEmployee 사원코드
	 */
	public void setCdEmployee(String cdEmployee) {
		this.cdEmployee = cdEmployee;
	}
	/**
	 * Getter 포스접근차단기능코드
	 */
	public Integer getCdAuthority() {
		return cdAuthority;
	}
	/**
	 * Setter 포스접근차단기능코드
	 * @param cdAuthority 포스접근차단기능코드
	 */
	public void setCdAuthority(Integer cdAuthority) {
		this.cdAuthority = cdAuthority;
	}
	/**
	 * Getter 포스접근차단가능유무
	 */
	public Integer getYnEnable() {
		return ynEnable;
	}
	/**
	 * Setter 포스접근차단가능유무
	 * @param ynEnable 포스접근차단가능유무
	 */
	public void setYnEnable(Integer ynEnable) {
		this.ynEnable = ynEnable;
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
