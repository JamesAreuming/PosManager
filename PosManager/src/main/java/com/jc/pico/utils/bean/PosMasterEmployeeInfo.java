package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 8. M_EMPLOYEE_INFO : 사원 정보
 * 2016. 7. 21, green, create
 */
public class PosMasterEmployeeInfo implements Serializable {
	private final static long serialVersionUID = 1L;

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
	 * 사원코드
	 */
	@JsonProperty(value = "CD_EMPLOYEE")
	private String cdEmployee;
	/**
	 * 사원명
	 */
	@JsonProperty(value = "NM_EMPLOYEE")
	private String nmEmployee;
	/**
	 * 사용자ID
	 */
	@JsonProperty(value = "USER_ID")
	private String userId;
	/**
	 * 사용자암호
	 */
	@JsonProperty(value = "USER_PW")
	private String userPw;

	/**
	 * 휴대전화 번호
	 */
	@JsonProperty(value = "TEL_MOBILE")
	private String telMobile;

	/**
	 * 사용자등급 (1:관리자 0:일반)
	 */
	@JsonProperty(value = "CD_GRADE")
	private String cdGrade;
	/**
	 * 사용유무 (1:사용, 0:미사용)
	 */
	@JsonProperty(value = "YN_USE")
	private Integer ynUse;

	/**
	 * 로그인 시도 실패 횟수
	 */
	@JsonProperty(value = "CNT_LOGIN_FAIL")
	private Integer loginFailCnt;

	/**
	 * 마지막 로그인 성공 일시
	 */
	@JsonProperty(value = "DT_LOGIN_LAST")
	private String loginLastTm;

	/**
	 * 마지막 로그인 시도 일시
	 */
	@JsonProperty(value = "DT_LOGIN_TRY")
	private String loginTryTm;

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

	/**
	 * Getter 회사코드
	 */
	public String getCdCompany() {
		return cdCompany;
	}

	/**
	 * Setter 회사코드
	 * 
	 * @param cdCompany
	 *            회사코드
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
	 * 
	 * @param cdStore
	 *            매장코드
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
	 * 
	 * @param cdEmployee
	 *            사원코드
	 */
	public void setCdEmployee(String cdEmployee) {
		this.cdEmployee = cdEmployee;
	}

	/**
	 * Getter 사원명
	 */
	public String getNmEmployee() {
		return nmEmployee;
	}

	/**
	 * Setter 사원명
	 * 
	 * @param nmEmployee
	 *            사원명
	 */
	public void setNmEmployee(String nmEmployee) {
		this.nmEmployee = nmEmployee;
	}

	/**
	 * Getter 사용자ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter 사용자ID
	 * 
	 * @param userId
	 *            사용자ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter 사용자암호
	 */
	public String getUserPw() {
		return userPw;
	}

	/**
	 * Setter 사용자암호
	 * 
	 * @param userPw
	 *            사용자암호
	 */
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	/**
	 * Getter 사용자등급 (1:관리자 0:일반)
	 */
	public String getCdGrade() {
		return cdGrade;
	}

	/**
	 * Setter 사용자등급 (1:관리자 0:일반)
	 * 
	 * @param cdGrade
	 *            사용자등급 (1:관리자 0:일반)
	 */
	public void setCdGrade(String cdGrade) {
		this.cdGrade = cdGrade;
	}

	/**
	 * Getter 사용유무 (1:사용, 0:미사용)
	 */
	public Integer getYnUse() {
		return ynUse;
	}

	/**
	 * Setter 사용유무 (1:사용, 0:미사용)
	 * 
	 * @param ynUse
	 *            사용유무 (1:사용, 0:미사용)
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
	 * 
	 * @param dtInsert
	 *            등록일시
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
	 * 
	 * @param cdEmployeeInsert
	 *            등록자
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
	 * 
	 * @param dtUpdate
	 *            수정일시
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
	 * 
	 * @param cdEmployeeUpdate
	 *            수정자
	 */
	public void setCdEmployeeUpdate(String cdEmployeeUpdate) {
		this.cdEmployeeUpdate = cdEmployeeUpdate;
	}

	public Integer getLoginFailCnt() {
		return loginFailCnt;
	}

	public void setLoginFailCnt(Integer loginFailCnt) {
		this.loginFailCnt = loginFailCnt;
	}

	public String getLoginLastTm() {
		return loginLastTm;
	}

	public void setLoginLastTm(String loginLastTm) {
		this.loginLastTm = loginLastTm;
	}

	public String getLoginTryTm() {
		return loginTryTm;
	}

	public void setLoginTryTm(String loginTryTm) {
		this.loginTryTm = loginTryTm;
	}

	public String getTelMobile() {
		return telMobile;
	}

	public void setTelMobile(String telMobile) {
		this.telMobile = telMobile;
	}

}
