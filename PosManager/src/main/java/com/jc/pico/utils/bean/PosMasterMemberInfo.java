package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 29. M_MEMBER_INFO : 회원 정보
 * 2016. 7. 21, green, create
 */
public class PosMasterMemberInfo implements Serializable {

	private final static long serialVersionUID = 1L;

	/**
	 * 회원코드
	 */
	@JsonProperty(value = "CD_MEMBER")
	private String cdMember;

	/**
	 * 회원명
	 */
	@JsonProperty(value = "NM_MEMBER")
	private String nmMember;

	/**
	 * 휴대전화
	 */
	@JsonProperty(value = "TEL_MOBILE")
	private String telMobile;

	/**
	 * 생년월일
	 */
	@JsonProperty(value = "YMD_BIRTH")
	private String ymdBirth;

	/**
	 * 바코드 (표시용 회원코드)
	 */
	@JsonProperty(value = "BARCODE")
	private String barcode;

	public String getCdMember() {
		return cdMember;
	}

	public void setCdMember(String cdMember) {
		this.cdMember = cdMember;
	}

	public String getNmMember() {
		return nmMember;
	}

	public void setNmMember(String nmMember) {
		this.nmMember = nmMember;
	}

	public String getTelMobile() {
		return telMobile;
	}

	public void setTelMobile(String telMobile) {
		this.telMobile = telMobile;
	}

	public String getYmdBirth() {
		return ymdBirth;
	}

	public void setYmdBirth(String ymdBirth) {
		this.ymdBirth = ymdBirth;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
