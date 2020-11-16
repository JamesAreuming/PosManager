package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 2. S_CLOSING_SAVE : 마감정보
 * 2016. 8. 13, green, create
 */
public class PosSalesClosingSaveDetail implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 마감일자
	 */
	@JsonProperty(value = "YMD_CLOSE")
	private String ymdClose;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "NO_CLOSING")
	private Long noClosing;
	/**
	 * 사원코드
	 */
	@JsonProperty(value = "CD_EMPLOYEE")
	private String cdEmployee;
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
	 * 수표금액
	 */
	@JsonProperty(value = "AMT_CHECK")
	private Double amtCheck;
	/**
	 * 10만원
	 */
	@JsonProperty(value = "CNT_100000")
	private Integer cnt100000;
	/**
	 * 5만원
	 */
	@JsonProperty(value = "CNT_50000")
	private Integer cnt50000;
	/**
	 * 1만원
	 */
	@JsonProperty(value = "CNT_10000")
	private Integer cnt10000;
	/**
	 * 5천원
	 */
	@JsonProperty(value = "CNT_5000")
	private Integer cnt5000;
	/**
	 * 1천원
	 */
	@JsonProperty(value = "CNT_1000")
	private Integer cnt1000;
	/**
	 * 500원
	 */
	@JsonProperty(value = "CNT_500")
	private Integer cnt500;
	/**
	 * 100원
	 */
	@JsonProperty(value = "CNT_100")
	private Integer cnt100;
	/**
	 * 50원
	 */
	@JsonProperty(value = "CNT_50")
	private Integer cnt50;
	/**
	 * 10원
	 */
	@JsonProperty(value = "CNT_10")
	private Integer cnt10;
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

	public String getYmdClose() {
		return ymdClose;
	}

	public void setYmdClose(String ymdClose) {
		this.ymdClose = ymdClose;
	}

	/**
	 * Getter 일련번호
	 */
	public Long getNoClosing() {
		return noClosing;
	}

	/**
	 * Setter 일련번호
	 * 
	 * @param seq
	 *            일련번호
	 */
	public void setNoClosing(Long noClosing) {
		this.noClosing = noClosing;
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
	 * Getter 수표금액
	 */
	public Double getAmtCheck() {
		return amtCheck;
	}

	/**
	 * Setter 수표금액
	 * 
	 * @param amtCheck
	 *            수표금액
	 */
	public void setAmtCheck(Double amtCheck) {
		this.amtCheck = amtCheck;
	}

	/**
	 * Getter 10만원
	 */
	public Integer getCnt100000() {
		return cnt100000;
	}

	/**
	 * Setter 10만원
	 * 
	 * @param cnt100000
	 *            10만원
	 */
	public void setCnt100000(Integer cnt100000) {
		this.cnt100000 = cnt100000;
	}

	/**
	 * Getter 5만원
	 */
	public Integer getCnt50000() {
		return cnt50000;
	}

	/**
	 * Setter 5만원
	 * 
	 * @param cnt50000
	 *            5만원
	 */
	public void setCnt50000(Integer cnt50000) {
		this.cnt50000 = cnt50000;
	}

	/**
	 * Getter 1만원
	 */
	public Integer getCnt10000() {
		return cnt10000;
	}

	/**
	 * Setter 1만원
	 * 
	 * @param cnt10000
	 *            1만원
	 */
	public void setCnt10000(Integer cnt10000) {
		this.cnt10000 = cnt10000;
	}

	/**
	 * Getter 5천원
	 */
	public Integer getCnt5000() {
		return cnt5000;
	}

	/**
	 * Setter 5천원
	 * 
	 * @param cnt5000
	 *            5천원
	 */
	public void setCnt5000(Integer cnt5000) {
		this.cnt5000 = cnt5000;
	}

	/**
	 * Getter 1천원
	 */
	public Integer getCnt1000() {
		return cnt1000;
	}

	/**
	 * Setter 1천원
	 * 
	 * @param cnt1000
	 *            1천원
	 */
	public void setCnt1000(Integer cnt1000) {
		this.cnt1000 = cnt1000;
	}

	/**
	 * Getter 500원
	 */
	public Integer getCnt500() {
		return cnt500;
	}

	/**
	 * Setter 500원
	 * 
	 * @param cnt500
	 *            500원
	 */
	public void setCnt500(Integer cnt500) {
		this.cnt500 = cnt500;
	}

	/**
	 * Getter 100원
	 */
	public Integer getCnt100() {
		return cnt100;
	}

	/**
	 * Setter 100원
	 * 
	 * @param cnt100
	 *            100원
	 */
	public void setCnt100(Integer cnt100) {
		this.cnt100 = cnt100;
	}

	/**
	 * Getter 50원
	 */
	public Integer getCnt50() {
		return cnt50;
	}

	/**
	 * Setter 50원
	 * 
	 * @param cnt50
	 *            50원
	 */
	public void setCnt50(Integer cnt50) {
		this.cnt50 = cnt50;
	}

	/**
	 * Getter 10원
	 */
	public Integer getCnt10() {
		return cnt10;
	}

	/**
	 * Setter 10원
	 * 
	 * @param cnt10
	 *            10원
	 */
	public void setCnt10(Integer cnt10) {
		this.cnt10 = cnt10;
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
}
