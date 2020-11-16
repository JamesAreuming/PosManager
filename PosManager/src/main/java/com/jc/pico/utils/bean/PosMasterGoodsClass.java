/*
 * Filename	: PosMasterGoodsClass.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 17. M_PLU_CLASS : PLU 분류
 * 2016. 7. 21, green, create
 */
public class PosMasterGoodsClass implements Serializable {
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
   * PLU TYPE
   */
  @JsonProperty(value = "DS_CLASS")
  private Long dsClass;
  /**
   * 분류코드
   */
  @JsonProperty(value = "CD_CLASS")
  private String cdClass;
  /**
   * 명칭
   */
  @JsonProperty(value = "NM_CLASS")
  private String nmClass;
  /**
   * 위치
   */
  @JsonProperty(value = "YN_USE")
  private Integer ynUse;
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
   *
   * @param cdStore 매장코드
   */
  public void setCdStore(String cdStore) {
    this.cdStore = cdStore;
  }

  /**
   * Getter 분류코드
   */
  public String getCdClass() {
    return cdClass;
  }

  /**
   * Setter 분류코드
   *
   * @param cdClass 분류코드
   */
  public void setCdClass(String cdClass) {
    this.cdClass = cdClass;
  }

  /**
   * Getter 명칭
   */
  public String getNmClass() {
    return nmClass;
  }

  /**
   * Setter 명칭
   *
   * @param nmClass 명칭
   */
  public void setNmClass(String nmClass) {
    this.nmClass = nmClass;
  }


  /**
   * Getter 후방 분류코드
   * @return
   */
  public Long getDsClass() {
    return dsClass;
  }

  /**
   * Setter 후방 분류코드
   * @param dsClass
   */
  public void setDsClass(Long dsClass) {
    this.dsClass = dsClass;
  }

  /**
   * Getter 사용 유무
   * @return
   */
  public Integer getYnUse() {
    return ynUse;
  }

  /**
   * Setter 사용 유무
   * @param ynUse
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
   *
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
   *
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
   *
   * @param cdEmployeeUpdate 수정자
   */
  public void setCdEmployeeUpdate(String cdEmployeeUpdate) {
    this.cdEmployeeUpdate = cdEmployeeUpdate;
  }
}
