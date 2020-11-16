package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 주문기 문서 2.M_STORE_INFO:매장정보
 * @author green
 */
public class PosResultStoreInfo implements Serializable {
	private static final long serialVersionUID = 1L;

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
     * 회사코드
     */
	@JsonProperty(value="CD_BRAND")
	private int cdBrand;
    /**
     * 회사코드
     */
	@JsonProperty(value="NO_REGIST")
	private String noRegist;
    /**
     * 회사코드
     */
	@JsonProperty(value="NM_COMPANY")
	private String nmCompany;
    /**
     * 회사코드
     */
	@JsonProperty(value="NM_BRAND")
	private String nmBrand;
    /**
     * 회사코드
     */
	@JsonProperty(value="NM_STORE")
	private String nmStore;
    /**
     * 회사코드
     */
	@JsonProperty(value="NM_BOSS")
	private String nmBoss;
    /**
     * 회사코드
     */
	@JsonProperty(value="NM_UPJONG")
	private String nmUpjong;
    /**
     * 회사코드
     */
	@JsonProperty(value="NM_UPTAE")
	private String nmUptae;
    /**
     * 회사코드
     */
	@JsonProperty(value="TEL1")
	private String tel1;
    /**
     * 회사코드
     */
	@JsonProperty(value="TEL2")
	private String tel2;
    /**
     * 회사코드
     */
	@JsonProperty(value="TEL3")
	private String tel3;

	@JsonProperty(value="FAX")
	private String fax;

	@JsonProperty(value="NO_POST")
	private String noPost;

	@JsonProperty(value="ADDR1")
	private String addr1;

	@JsonProperty(value="ADDR2")
	private String addr2;
	
	@JsonProperty(value="TAX_RATE")
	private Float taxRate;

	@JsonProperty(value="CD_CURRENCY")
	private Integer cdCurrency;

	/**
	 * 테이블사용유무 (1:사용 0:미사용)
	 */
	@JsonProperty(value="YN_TABLE")
	private Integer ynTable;

	@JsonProperty(value="STORE_PLU_TYPE")
	private Integer storePluType;

	@JsonProperty(value="STR_CD")
	private String strCd;

	@JsonProperty(value="DT_INSERT")
	private String dtInsert;

	@JsonProperty(value="CD_EMPLOYEE_INSERT")
	private String cdEmployeeInsert;

	@JsonProperty(value="DT_UPDATE")
	private String dtUpdate;

	@JsonProperty(value="CD_EMPLOYEE_UPDATE")
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

	public int getCdBrand() {
		return cdBrand;
	}

	public void setCdBrand(int cdBrand) {
		this.cdBrand = cdBrand;
	}

	public String getNoRegist() {
		return noRegist;
	}

	public void setNoRegist(String noRegist) {
		this.noRegist = noRegist;
	}

	public String getNmCompany() {
		return nmCompany;
	}

	public void setNmCompany(String nmCompany) {
		this.nmCompany = nmCompany;
	}

	public String getNmBrand() {
		return nmBrand;
	}

	public void setNmBrand(String nmBrand) {
		this.nmBrand = nmBrand;
	}

	public String getNmStore() {
		return nmStore;
	}

	public void setNmStore(String nmStore) {
		this.nmStore = nmStore;
	}

	public String getNmBoss() {
		return nmBoss;
	}

	public void setNmBoss(String nmBoss) {
		this.nmBoss = nmBoss;
	}

	public String getNmUpjong() {
		return nmUpjong;
	}

	public void setNmUpjong(String nmUpjong) {
		this.nmUpjong = nmUpjong;
	}

	public String getNmUptae() {
		return nmUptae;
	}

	public void setNmUptae(String nmUptae) {
		this.nmUptae = nmUptae;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getTel3() {
		return tel3;
	}

	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNoPost() {
		return noPost;
	}

	public void setNoPost(String noPost) {
		this.noPost = noPost;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public Integer getCdCurrency() {
		return cdCurrency;
	}

	public void setCdCurrency(Integer cdCurrency) {
		this.cdCurrency = cdCurrency;
	}

	/**
	 * Getter 테이블사용유무 (1:사용 0:미사용)
	 */
	public Integer getYnTable() {
		return ynTable;
	}
	/**
	 * Setter 테이블사용유무 (1:사용 0:미사용)
	 * @param ynTable 테이블사용유무 (1:사용 0:미사용)
	 */
	public void setYnTable(Integer ynTable) {
		this.ynTable = ynTable;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getStorePluType() {
		return storePluType;
	}

	public void setStorePluType(Integer storePluType) {
		this.storePluType = storePluType;
	}

	public String getStrCd() {
		return strCd;
	}

	public void setStrCd(String strCd) {
		this.strCd = strCd;
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

	public Float getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Float taxRate) {
		this.taxRate = taxRate;
	}	

}