package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 18. M_PLU_GOODS : PLU 상품
 * 2016. 7. 21, green, create
 */
public class PosMasterPluGoods implements Serializable {
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
	 * 분류코드
	 */
	@JsonProperty(value="CD_CLASS")
	private String cdClass;
	/**
	 * 품번
	 */
	@JsonProperty(value="CD_GOODS")
	private String cdGoods;
	/**
	 * 품명
	 */
	@JsonProperty(value="NM_GOODS")
	private String nmGoods;
	/**
	 * 위치
	 */
	@JsonProperty(value="NO_POSITION")
	private Integer noPosition;
	/**
	 * 배경색
	 */
	@JsonProperty(value="BGCOLOR")
	private String bgcolor;
	/**
	 * 전경색
	 */
	@JsonProperty(value="FGCOLOR")
	private String fgcolor;
	/**
	 * 이미지파일명
	 */
	@JsonProperty(value="NM_IMAGE")
	private String nmImage;
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
	public void setCdPluType(Long cdType) {
		this.cdPluType = cdType;
	}
	/**
	 * Getter 분류코드
	 */
	public String getCdClass() {
		return cdClass;
	}
	/**
	 * Setter 분류코드
	 * @param cdClass 분류코드
	 */
	public void setCdClass(String cdClass) {
		this.cdClass = cdClass;
	}
	/**
	 * Getter 품번
	 */
	public String getCdGoods() {
		return cdGoods;
	}
	/**
	 * Setter 품번
	 * @param cdGoods 품번
	 */
	public void setCdGoods(String cdGoods) {
		this.cdGoods = cdGoods;
	}
	/**
	 * Getter 품명
	 */
	public String getNmGoods() {
		return nmGoods;
	}
	/**
	 * Setter 품명
	 * @param nmGoods 품명
	 */
	public void setNmGoods(String nmGoods) {
		this.nmGoods = nmGoods;
	}
	/**
	 * Getter 위치
	 */
	public Integer getNoPosition() {
		return noPosition;
	}
	/**
	 * Setter 위치
	 * @param noPosition 위치
	 */
	public void setNoPosition(Integer noPosition) {
		this.noPosition = noPosition;
	}
	/**
	 * Getter 배경색
	 */
	public String getBgcolor() {
		return bgcolor;
	}
	/**
	 * Setter 배경색
	 * @param bgcolor 배경색
	 */
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	/**
	 * Getter 전경색
	 */
	public String getFgcolor() {
		return fgcolor;
	}
	/**
	 * Setter 전경색
	 * @param fgcolor 전경색
	 */
	public void setFgcolor(String fgcolor) {
		this.fgcolor = fgcolor;
	}
	/**
	 * Getter 이미지파일명
	 */
	public String getNmImage() {
		return nmImage;
	}
	/**
	 * Setter 이미지파일명
	 * @param nmImage 이미지파일명
	 */
	public void setNmImage(String nmImage) {
		this.nmImage = nmImage;
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
