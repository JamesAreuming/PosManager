package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 스탬프 적립
 * 
 * @author hyo
 *
 */
public class PosStampSave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 회원코드
	 */
	@JsonProperty(value = "CD_MEMBER")
	private Long cdMember;

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
	 * 전표번호
	 */
	@JsonProperty(value = "NO_RCP")
	private Long noRcp;

	/**
	 * 정립 여부 적립여부 (1.적립, 2.취소)
	 */
	@JsonProperty(value = "YN_SAVE")
	private Integer ynSave;

	public Long getCdMember() {
		return cdMember;
	}

	public void setCdMember(Long cdMember) {
		this.cdMember = cdMember;
	}

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

	public Long getNoRcp() {
		return noRcp;
	}

	public void setNoRcp(Long noRcp) {
		this.noRcp = noRcp;
	}

	public Integer getYnSave() {
		return ynSave;
	}

	public void setYnSave(Integer ynSave) {
		this.ynSave = ynSave;
	}

	@Override
	public String toString() {
		return "PosStampSave [cdMember=" + cdMember + ", cdCompany=" + cdCompany + ", cdStore=" + cdStore + ", ymdSale=" + ymdSale + ", noPos="
				+ noPos + ", noRcp=" + noRcp + ", ynSave=" + ynSave + "]";
	}

}
