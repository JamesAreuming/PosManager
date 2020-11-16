package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. S_ORDER_INFO : 주문정보
 * 11. S_ORDER_SAVE : 주문정보 저장
 * 2016. 8. 13, green, create
 * 2016. 9. 7, hyo, 주문 조회/저장 bean 통합
 */
public class PosSalesOrderInfoDiscount implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 할인형태
	 */
	@JsonProperty(value = "CD_DC_TYPE")
	private Integer cdDcType;
	/**
	 * 할인번호
	 */
	@JsonProperty(value = "NO_DC")
	private Long noDc;

	/**
	 * 쿠폰 번호
	 */
	@JsonProperty(value = "NO_COUPON")
	private String noCoupon;

	/**
	 * 할인 취소 여부 (0: 정상, 1: 취소)
	 */
	@JsonProperty(value = "DS_DISCOUNT")
	private Long dsDiscount;

	/**
	 * 적용구분
	 */
	@JsonProperty(value = "DS_APPLY")
	private Integer dsApply;
	/**
	 * 설정금액
	 */
	@JsonProperty(value = "AMT_SET")
	private Double amtSet;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 부가번호
	 */
	@JsonProperty(value = "NO_ADD")
	private String noAdd;
	/**
	 * 부가번호2
	 */
	@JsonProperty(value = "NO_ADD2")
	private String noAdd2;
	/**
	 * 비고
	 */
	@JsonProperty(value = "REMARK")
	private String remark;
	/**
	 * 포인트사용여부
	 */
	@JsonProperty(value = "PNT_USE")
	private Integer pntUse;
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
	 * Getter 할인형태
	 */
	public Integer getCdDcType() {
		return cdDcType;
	}

	/**
	 * Setter 할인형태
	 * 
	 * @param cdDcType
	 *            할인형태
	 */
	public void setCdDcType(Integer cdDcType) {
		this.cdDcType = cdDcType;
	}

	/**
	 * Getter 할인번호
	 */
	public Long getNoDc() {
		return noDc;
	}

	/**
	 * Setter 할인번호
	 * 
	 * @param noDc
	 *            할인번호
	 */
	public void setNoDc(Long noDc) {
		this.noDc = noDc;
	}

	/**
	 * Getter 적용구분
	 */
	public Integer getDsApply() {
		return dsApply;
	}

	/**
	 * Setter 적용구분
	 * 
	 * @param dsApply
	 *            적용구분
	 */
	public void setDsApply(Integer dsApply) {
		this.dsApply = dsApply;
	}

	/**
	 * Getter 설정금액
	 */
	public Double getAmtSet() {
		return amtSet;
	}

	/**
	 * Setter 설정금액
	 * 
	 * @param amtSet
	 *            설정금액
	 */
	public void setAmtSet(Double amtSet) {
		this.amtSet = amtSet;
	}

	/**
	 * Getter 할인금액
	 */
	public Double getAmtDc() {
		return amtDc;
	}

	/**
	 * Setter 할인금액
	 * 
	 * @param amtDc
	 *            할인금액
	 */
	public void setAmtDc(Double amtDc) {
		this.amtDc = amtDc;
	}

	/**
	 * Getter 부가번호
	 */
	public String getNoAdd() {
		return noAdd;
	}

	/**
	 * Setter 부가번호
	 * 
	 * @param noAdd
	 *            부가번호
	 */
	public void setNoAdd(String noAdd) {
		this.noAdd = noAdd;
	}

	/**
	 * Getter 부가번호2
	 */
	public String getNoAdd2() {
		return noAdd2;
	}

	/**
	 * Setter 부가번호2
	 * 
	 * @param noAdd2
	 *            부가번호2
	 */
	public void setNoAdd2(String noAdd2) {
		this.noAdd2 = noAdd2;
	}

	/**
	 * Getter 비고
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Setter 비고
	 * 
	 * @param remark
	 *            비고
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * Getter 포인트사용여부
	 */
	public Integer getPntUse() {
		return pntUse;
	}

	/**
	 * Setter 포인트사용여부
	 * 
	 * @param pntUse
	 *            포인트사용여부
	 */
	public void setPntUse(Integer pntUse) {
		this.pntUse = pntUse;
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

	public String getNoCoupon() {
		return noCoupon;
	}

	public void setNoCoupon(String noCoupon) {
		this.noCoupon = noCoupon;
	}

	public Long getDsDiscount() {
		return dsDiscount;
	}

	public void setDsDiscount(Long dsDiscount) {
		this.dsDiscount = dsDiscount;
	}

}