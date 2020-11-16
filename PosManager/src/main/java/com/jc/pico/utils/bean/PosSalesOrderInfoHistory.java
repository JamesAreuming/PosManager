package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. S_ORDER_INFO : 주문정보
 * 11. S_ORDER_SAVE : 주문정보 저장
 * 2016. 8. 13, green, create
 * 2016. 9. 7, hyo, 주문 조회/저장 bean 통합
 */
public class PosSalesOrderInfoHistory implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호1
	 */
	@JsonProperty(value = "SEQ1")
	private Long seq1;
	/**
	 * 주문수량
	 */
	@JsonProperty(value = "QTY_ORDER")
	private Integer qtyOrder;
	/**
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;
	/**
	 * 상태(사용안함)
	 */
	@JsonProperty(value = "DS_STATUS")
	private Integer dsStatus;
	/**
	 * 주문순서
	 */
	@JsonProperty(value = "ORDER_SORT")
	private Integer orderSort;
	/**
	 * 취소사유코드
	 */
	@JsonProperty(value = "CD_CANCEL_REASON")
	private Integer cdCancelReason;
	/**
	 * 주문일시
	 */
	@JsonProperty(value = "DT_ORDER")
	private String dtOrder;
	/**
	 * 사원코드
	 */
	@JsonProperty(value = "CD_EMPLOYEE")
	private String cdEmployee;
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
	 * Getter 일련번호1
	 */
	public Long getSeq1() {
		return seq1;
	}

	/**
	 * Setter 일련번호1
	 * 
	 * @param seq1
	 *            일련번호1
	 */
	public void setSeq1(Long seq1) {
		this.seq1 = seq1;
	}

	/**
	 * Getter 주문수량
	 */
	public Integer getQtyOrder() {
		return qtyOrder;
	}

	/**
	 * Setter 주문수량
	 * 
	 * @param qtyOrder
	 *            주문수량
	 */
	public void setQtyOrder(Integer qtyOrder) {
		this.qtyOrder = qtyOrder;
	}

	/**
	 * Getter 메모
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * Setter 메모
	 * 
	 * @param memo
	 *            메모
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * Getter 상태(사용안함)
	 */
	public Integer getDsStatus() {
		return dsStatus;
	}

	/**
	 * Setter 상태(사용안함)
	 * 
	 * @param dsStatus
	 *            상태(사용안함)
	 */
	public void setDsStatus(Integer dsStatus) {
		this.dsStatus = dsStatus;
	}

	/**
	 * Getter 주문순서
	 */
	public Integer getOrderSort() {
		return orderSort;
	}

	/**
	 * Setter 주문순서
	 * 
	 * @param orderSort
	 *            주문순서
	 */
	public void setOrderSort(Integer orderSort) {
		this.orderSort = orderSort;
	}

	/**
	 * Getter 취소사유코드
	 */
	public Integer getCdCancelReason() {
		return cdCancelReason;
	}

	/**
	 * Setter 취소사유코드
	 * 
	 * @param cdCancelReason
	 *            취소사유코드
	 */
	public void setCdCancelReason(Integer cdCancelReason) {
		this.cdCancelReason = cdCancelReason;
	}

	/**
	 * Getter 주문일시
	 */
	public String getDtOrder() {
		return dtOrder;
	}

	/**
	 * Setter 주문일시
	 * 
	 * @param dtOrder
	 *            주문일시
	 */
	public void setDtOrder(String dtOrder) {
		this.dtOrder = dtOrder;
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
}