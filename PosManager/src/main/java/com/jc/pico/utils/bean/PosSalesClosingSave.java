package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 2. S_CLOSING_SAVE : 마감정보
 * 2016. 8. 13, green, create
 */
public class PosSalesClosingSave implements Serializable {
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
	 * 판매일자
	 */
	@JsonProperty(value = "YMD_CLOSE")
	private String ymdClose;
	/**
	 * 포스번호
	 */
	@JsonProperty(value = "NO_POS")
	private String noPos;
	/**
	 * 마감여부
	 */
	@JsonProperty(value = "YN_CLOSE")
	private Integer ynClose;
	/**
	 * 개점일시
	 */
	@JsonProperty(value = "DT_OPEN")
	private String dtOpen;
	/**
	 * 마감일시
	 */
	@JsonProperty(value = "DT_CLOSE")
	private String dtClose;
	/**
	 * 준비금
	 */
	@JsonProperty(value = "AMT_OPEN_RESERVE")
	private Double amtOpenReserve;
	/**
	 * 판매금액
	 */
	@JsonProperty(value = "AMT_SALE")
	private Double amtSale;
	/**
	 * 판매건수
	 */
	@JsonProperty(value = "CNT_SALE")
	private Integer cntSale;
	/**
	 * 반품금액
	 */
	@JsonProperty(value = "AMT_SALE_RETURN")
	private Double amtSaleReturn;
	/**
	 * 반품건수
	 */
	@JsonProperty(value = "CNT_SALE_RETURN")
	private Integer cntSaleReturn;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 할인건수
	 */
	@JsonProperty(value = "CNT_DC")
	private Integer cntDc;
	/**
	 * 객수
	 */
	@JsonProperty(value = "CNT_CUSTOMER")
	private Integer cntCustomer;
	/**
	 * 현금입금액
	 */
	@JsonProperty(value = "AMT_CASH_IN")
	private Double amtCashIn;
	/**
	 * 현금입금건수
	 */
	@JsonProperty(value = "CNT_CASH_IN")
	private Integer cntCashIn;
	/**
	 * 현금출금액
	 */
	@JsonProperty(value = "AMT_CASH_OUT")
	private Double amtCashOut;
	/**
	 * 현금출금건수
	 */
	@JsonProperty(value = "CNT_CASH_OUT")
	private Integer cntCashOut;
	/**
	 * 현금시재
	 */
	@JsonProperty(value = "AMT_CASH_ON_HAND")
	private Double amtCashOnHand;
	/**
	 * 과부족
	 */
	@JsonProperty(value = "AMT_CASH_LACK")
	private Double amtCashLack;
	/**
	 * 상세 내역
	 */
	@JsonProperty(value = "DETAIL")
	private List<PosSalesClosingSaveDetail> detail;

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
	 * Getter 판매일자
	 */
	public String getYmdClose() {
		return ymdClose;
	}

	/**
	 * Setter 판매일자
	 * 
	 * @param ymdClose
	 *            판매일자
	 */
	public void setYmdClose(String ymdClose) {
		this.ymdClose = ymdClose;
	}

	/**
	 * Getter 포스번호
	 */
	public String getNoPos() {
		return noPos;
	}

	/**
	 * Setter 포스번호
	 * 
	 * @param noPos
	 *            포스번호
	 */
	public void setNoPos(String noPos) {
		this.noPos = noPos;
	}

	/**
	 * Getter 마감여부
	 */
	public Integer getYnClose() {
		return ynClose;
	}

	/**
	 * Setter 마감여부
	 * 
	 * @param ynClose
	 *            마감여부
	 */
	public void setYnClose(Integer ynClose) {
		this.ynClose = ynClose;
	}

	/**
	 * Getter 개점일시
	 */
	public String getDtOpen() {
		return dtOpen;
	}

	/**
	 * Setter 개점일시
	 * 
	 * @param dtOpen
	 *            개점일시
	 */
	public void setDtOpen(String dtOpen) {
		this.dtOpen = dtOpen;
	}

	/**
	 * Getter 마감일시
	 */
	public String getDtClose() {
		return dtClose;
	}

	/**
	 * Setter 마감일시
	 * 
	 * @param dtClose
	 *            마감일시
	 */
	public void setDtClose(String dtClose) {
		this.dtClose = dtClose;
	}

	/**
	 * Getter 준비금
	 */
	public Double getAmtOpenReserve() {
		return amtOpenReserve;
	}

	/**
	 * Setter 준비금
	 * 
	 * @param amtOpenReserve
	 *            준비금
	 */
	public void setAmtOpenReserve(Double amtOpenReserve) {
		this.amtOpenReserve = amtOpenReserve;
	}

	/**
	 * Getter 판매금액
	 */
	public Double getAmtSale() {
		return amtSale;
	}

	/**
	 * Setter 판매금액
	 * 
	 * @param amtSale
	 *            판매금액
	 */
	public void setAmtSale(Double amtSale) {
		this.amtSale = amtSale;
	}

	/**
	 * Getter 판매건수
	 */
	public Integer getCntSale() {
		return cntSale;
	}

	/**
	 * Setter 판매건수
	 * 
	 * @param cntSale
	 *            판매건수
	 */
	public void setCntSale(Integer cntSale) {
		this.cntSale = cntSale;
	}

	/**
	 * Getter 반품금액
	 */
	public Double getAmtSaleReturn() {
		return amtSaleReturn;
	}

	/**
	 * Setter 반품금액
	 * 
	 * @param amtSaleReturn
	 *            반품금액
	 */
	public void setAmtSaleReturn(Double amtSaleReturn) {
		this.amtSaleReturn = amtSaleReturn;
	}

	/**
	 * Getter 반품건수
	 */
	public Integer getCntSaleReturn() {
		return cntSaleReturn;
	}

	/**
	 * Setter 반품건수
	 * 
	 * @param cntSaleReturn
	 *            반품건수
	 */
	public void setCntSaleReturn(Integer cntSaleReturn) {
		this.cntSaleReturn = cntSaleReturn;
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
	 * Getter 할인건수
	 */
	public Integer getCntDc() {
		return cntDc;
	}

	/**
	 * Setter 할인건수
	 * 
	 * @param cntDc
	 *            할인건수
	 */
	public void setCntDc(Integer cntDc) {
		this.cntDc = cntDc;
	}

	/**
	 * Getter 객수
	 */
	public Integer getCntCustomer() {
		return cntCustomer;
	}

	/**
	 * Setter 객수
	 * 
	 * @param cntCustomer
	 *            객수
	 */
	public void setCntCustomer(Integer cntCustomer) {
		this.cntCustomer = cntCustomer;
	}

	/**
	 * Getter 현금입금액
	 */
	public Double getAmtCashIn() {
		return amtCashIn;
	}

	/**
	 * Setter 현금입금액
	 * 
	 * @param amtCashIn
	 *            현금입금액
	 */
	public void setAmtCashIn(Double amtCashIn) {
		this.amtCashIn = amtCashIn;
	}

	/**
	 * Getter 현금입금건수
	 */
	public Integer getCntCashIn() {
		return cntCashIn;
	}

	/**
	 * Setter 현금입금건수
	 * 
	 * @param cntCashIn
	 *            현금입금건수
	 */
	public void setCntCashIn(Integer cntCashIn) {
		this.cntCashIn = cntCashIn;
	}

	/**
	 * Getter 현금출금액
	 */
	public Double getAmtCashOut() {
		return amtCashOut;
	}

	/**
	 * Setter 현금출금액
	 * 
	 * @param amtCashOut
	 *            현금출금액
	 */
	public void setAmtCashOut(Double amtCashOut) {
		this.amtCashOut = amtCashOut;
	}

	/**
	 * Getter 현금출금건수
	 */
	public Integer getCntCashOut() {
		return cntCashOut;
	}

	/**
	 * Setter 현금출금건수
	 * 
	 * @param cntCashOut
	 *            현금출금건수
	 */
	public void setCntCashOut(Integer cntCashOut) {
		this.cntCashOut = cntCashOut;
	}

	/**
	 * Getter 현금시재
	 */
	public Double getAmtCashOnHand() {
		return amtCashOnHand;
	}

	/**
	 * Setter 현금시재
	 * 
	 * @param amtCashOnHand
	 *            현금시재
	 */
	public void setAmtCashOnHand(Double amtCashOnHand) {
		this.amtCashOnHand = amtCashOnHand;
	}

	/**
	 * Getter 과부족
	 */
	public Double getAmtCashLack() {
		return amtCashLack;
	}

	/**
	 * Setter 과부족
	 * 
	 * @param amtCashLack
	 *            과부족
	 */
	public void setAmtCashLack(Double amtCashLack) {
		this.amtCashLack = amtCashLack;
	}

	/**
	 * Getter 상세 내역
	 */
	public List<PosSalesClosingSaveDetail> getDetail() {
		return detail;
	}

	/**
	 * Setter 상세 내역
	 * 
	 * @param detail
	 *            상세 내역
	 */
	public void setDetail(List<PosSalesClosingSaveDetail> detail) {
		this.detail = detail;
	}
}
