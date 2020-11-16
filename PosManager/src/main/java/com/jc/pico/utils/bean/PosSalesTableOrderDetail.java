package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 9. S_TABLE_ORDER : 테이블 주문정보
 * 2016. 8. 13, green, create
 */
public class PosSalesTableOrderDetail implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 주문번호
	 */
	@JsonProperty(value="NO_ORDER")
	private Long noOrder;
	/**
	 * 일련번호
	 */
	@JsonProperty(value="SEQ")
	private Long seq;
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
	 * 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 */
	@JsonProperty(value="DS_SALE")
	private Integer dsSale;
	/**
	 * 판매단가
	 */
	@JsonProperty(value="PR_SALE")
	private Double prSale;
	/**
	 * 주문수량
	 */
	@JsonProperty(value="QTY_ORDER")
	private Integer qtyOrder;
	/**
	 * 주문금액
	 */
	@JsonProperty(value="AMT_ORDER")
	private Double amtOrder;
	/**
	 * 공급가
	 */
	@JsonProperty(value="AMT_SUPPLY")
	private Double amtSupply;
	/**
	 * 부가세
	 */
	@JsonProperty(value="AMT_VAT")
	private Double amtVat;
	/**
	 * 할인금액
	 */
	@JsonProperty(value="AMT_DC")
	private Double amtDc;
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
	 * Getter 주문번호
	 */
	public Long getNoOrder() {
		return noOrder;
	}
	/**
	 * Setter 주문번호
	 * @param noOrder 주문번호
	 */
	public void setNoOrder(Long noOrder) {
		this.noOrder = noOrder;
	}
	/**
	 * Getter 일련번호
	 */
	public Long getSeq() {
		return seq;
	}
	/**
	 * Setter 일련번호
	 * @param seq 일련번호
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
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
	 * Getter 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 */
	public Integer getDsSale() {
		return dsSale;
	}
	/**
	 * Setter 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 * @param dsSale 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 */
	public void setDsSale(Integer dsSale) {
		this.dsSale = dsSale;
	}
	/**
	 * Getter 판매단가
	 */
	public Double getPrSale() {
		return prSale;
	}
	/**
	 * Setter 판매단가
	 * @param prSale 판매단가
	 */
	public void setPrSale(Double prSale) {
		this.prSale = prSale;
	}
	/**
	 * Getter 주문수량
	 */
	public Integer getQtyOrder() {
		return qtyOrder;
	}
	/**
	 * Setter 주문수량
	 * @param qtyOrder 주문수량
	 */
	public void setQtyOrder(Integer qtyOrder) {
		this.qtyOrder = qtyOrder;
	}
	/**
	 * Getter 주문금액
	 */
	public Double getAmtOrder() {
		return amtOrder;
	}
	/**
	 * Setter 주문금액
	 * @param amtOrder 주문금액
	 */
	public void setAmtOrder(Double amtOrder) {
		this.amtOrder = amtOrder;
	}
	/**
	 * Getter 공급가
	 */
	public Double getAmtSupply() {
		return amtSupply;
	}
	/**
	 * Setter 공급가
	 * @param amtSupply 공급가
	 */
	public void setAmtSupply(Double amtSupply) {
		this.amtSupply = amtSupply;
	}
	/**
	 * Getter 부가세
	 */
	public Double getAmtVat() {
		return amtVat;
	}
	/**
	 * Setter 부가세
	 * @param amtVat 부가세
	 */
	public void setAmtVat(Double amtVat) {
		this.amtVat = amtVat;
	}
	/**
	 * Getter 할인금액
	 */
	public Double getAmtDc() {
		return amtDc;
	}
	/**
	 * Setter 할인금액
	 * @param amtDc 할인금액
	 */
	public void setAmtDc(Double amtDc) {
		this.amtDc = amtDc;
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
