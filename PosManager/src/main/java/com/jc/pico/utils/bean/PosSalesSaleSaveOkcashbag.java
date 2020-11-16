package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. S_SALE_SAVE : 매출정보
 * 2016. 8. 13, green, create
 */
public class PosSalesSaleSaveOkcashbag implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;
	/**
	 * 기관코드
	 */
	@JsonProperty(value = "CD_AGENCY")
	private String cdAgency;
	/**
	 * 가맹점번호
	 */
	@JsonProperty(value = "NO_AFFLIATE")
	private String noAffliate;
	/**
	 * 거래구분
	 */
	@JsonProperty(value = "DS_TRADE")
	private Integer dsTrade;
	/**
	 * 승인유무
	 */
	@JsonProperty(value = "YN_APPROVAL")
	private Integer ynApproval;
	/**
	 * PosEntryCode
	 */
	@JsonProperty(value = "DS_INPUT")
	private String dsInput;
	/**
	 * 카드번호
	 */
	@JsonProperty(value = "NO_CARD")
	private String noCard;
	/**
	 * 승인번호
	 */
	@JsonProperty(value = "NO_APPROVAL")
	private String noApproval;
	/**
	 * 거래금액
	 */
	@JsonProperty(value = "AMT_APPROVAL")
	private Double amtApproval;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 할인후잔여금액
	 */
	@JsonProperty(value = "AMT_AFTER_DC")
	private Double amtAfterDc;
	/**
	 * 할인실차감포인트
	 */
	@JsonProperty(value = "PNT_DC_REAL")
	private Double pntDcReal;
	/**
	 * 할인후잔액적립포인트
	 */
	@JsonProperty(value = "PNT_AFTER_DC")
	private Double pntAfterDc;
	/**
	 * 발생포인트
	 */
	@JsonProperty(value = "PNT_OCCUR")
	private Double pntOccur;
	/**
	 * 가용포인트
	 */
	@JsonProperty(value = "PNT_AVAVIL")
	private Double pntAvavil;
	/**
	 * 누적포인트
	 */
	@JsonProperty(value = "PNT_TOTAL")
	private Double pntTotal;
	/**
	 * 승인일
	 */
	@JsonProperty(value = "YMD_APPROVAL")
	private String ymdApproval;
	/**
	 * 거래시간
	 */
	@JsonProperty(value = "DT_TRADE")
	private String dtTrade;
	/**
	 * 원승인일자
	 */
	@JsonProperty(value = "YMD_APPROVAL_ORG")
	private String ymdApprovalOrg;
	/**
	 * 원승인번호
	 */
	@JsonProperty(value = "NO_APPROVAL_ORG")
	private String noApprovalOrg;
	/**
	 * 출력메시지1
	 */
	@JsonProperty(value = "OUTPUT_MSG1")
	private String outputMsg1;
	/**
	 * 출력메시지2
	 */
	@JsonProperty(value = "OUTPUT_MSG2")
	private String outputMsg2;
	/**
	 * 출력메시지3
	 */
	@JsonProperty(value = "OUTPUT_MSG3")
	private String outputMsg3;
	/**
	 * 출력메시지4
	 */
	@JsonProperty(value = "OUTPUT_MSG4")
	private String outputMsg4;

	/**
	 * Getter 일련번호
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * Setter 일련번호
	 * 
	 * @param seq
	 *            일련번호
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}

	/**
	 * Getter 기관코드
	 */
	public String getCdAgency() {
		return cdAgency;
	}

	/**
	 * Setter 기관코드
	 * 
	 * @param cdAgency
	 *            기관코드
	 */
	public void setCdAgency(String cdAgency) {
		this.cdAgency = cdAgency;
	}

	/**
	 * Getter 가맹점번호
	 */
	public String getNoAffliate() {
		return noAffliate;
	}

	/**
	 * Setter 가맹점번호
	 * 
	 * @param noAffliate
	 *            가맹점번호
	 */
	public void setNoAffliate(String noAffliate) {
		this.noAffliate = noAffliate;
	}

	/**
	 * Getter 거래구분
	 */
	public Integer getDsTrade() {
		return dsTrade;
	}

	/**
	 * Setter 거래구분
	 * 
	 * @param dsTrade
	 *            거래구분
	 */
	public void setDsTrade(Integer dsTrade) {
		this.dsTrade = dsTrade;
	}

	/**
	 * Getter 승인유무
	 */
	public Integer getYnApproval() {
		return ynApproval;
	}

	/**
	 * Setter 승인유무
	 * 
	 * @param ynApproval
	 *            승인유무
	 */
	public void setYnApproval(Integer ynApproval) {
		this.ynApproval = ynApproval;
	}

	/**
	 * Getter PosEntryCode
	 */
	public String getDsInput() {
		return dsInput;
	}

	/**
	 * Setter PosEntryCode
	 * 
	 * @param dsInput
	 *            PosEntryCode
	 */
	public void setDsInput(String dsInput) {
		this.dsInput = dsInput;
	}

	/**
	 * Getter 카드번호
	 */
	public String getNoCard() {
		return noCard;
	}

	/**
	 * Setter 카드번호
	 * 
	 * @param noCard
	 *            카드번호
	 */
	public void setNoCard(String noCard) {
		this.noCard = noCard;
	}

	/**
	 * Getter 승인번호
	 */
	public String getNoApproval() {
		return noApproval;
	}

	/**
	 * Setter 승인번호
	 * 
	 * @param noApproval
	 *            승인번호
	 */
	public void setNoApproval(String noApproval) {
		this.noApproval = noApproval;
	}

	/**
	 * Getter 거래금액
	 */
	public Double getAmtApproval() {
		return amtApproval;
	}

	/**
	 * Setter 거래금액
	 * 
	 * @param amtApproval
	 *            거래금액
	 */
	public void setAmtApproval(Double amtApproval) {
		this.amtApproval = amtApproval;
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
	 * Getter 할인후잔여금액
	 */
	public Double getAmtAfterDc() {
		return amtAfterDc;
	}

	/**
	 * Setter 할인후잔여금액
	 * 
	 * @param amtAfterDc
	 *            할인후잔여금액
	 */
	public void setAmtAfterDc(Double amtAfterDc) {
		this.amtAfterDc = amtAfterDc;
	}

	/**
	 * Getter 할인실차감포인트
	 */
	public Double getPntDcReal() {
		return pntDcReal;
	}

	/**
	 * Setter 할인실차감포인트
	 * 
	 * @param pntDcReal
	 *            할인실차감포인트
	 */
	public void setPntDcReal(Double pntDcReal) {
		this.pntDcReal = pntDcReal;
	}

	/**
	 * Getter 할인후잔액적립포인트
	 */
	public Double getPntAfterDc() {
		return pntAfterDc;
	}

	/**
	 * Setter 할인후잔액적립포인트
	 * 
	 * @param pntAfterDc
	 *            할인후잔액적립포인트
	 */
	public void setPntAfterDc(Double pntAfterDc) {
		this.pntAfterDc = pntAfterDc;
	}

	/**
	 * Getter 발생포인트
	 */
	public Double getPntOccur() {
		return pntOccur;
	}

	/**
	 * Setter 발생포인트
	 * 
	 * @param pntOccur
	 *            발생포인트
	 */
	public void setPntOccur(Double pntOccur) {
		this.pntOccur = pntOccur;
	}

	/**
	 * Getter 가용포인트
	 */
	public Double getPntAvavil() {
		return pntAvavil;
	}

	/**
	 * Setter 가용포인트
	 * 
	 * @param pntAvavil
	 *            가용포인트
	 */
	public void setPntAvavil(Double pntAvavil) {
		this.pntAvavil = pntAvavil;
	}

	/**
	 * Getter 누적포인트
	 */
	public Double getPntTotal() {
		return pntTotal;
	}

	/**
	 * Setter 누적포인트
	 * 
	 * @param pntTotal
	 *            누적포인트
	 */
	public void setPntTotal(Double pntTotal) {
		this.pntTotal = pntTotal;
	}

	/**
	 * Getter 승인일
	 */
	public String getYmdApproval() {
		return ymdApproval;
	}

	/**
	 * Setter 승인일
	 * 
	 * @param ymdApproval
	 *            승인일
	 */
	public void setYmdApproval(String ymdApproval) {
		this.ymdApproval = ymdApproval;
	}

	/**
	 * Getter 거래시간
	 */
	public String getDtTrade() {
		return dtTrade;
	}

	/**
	 * Setter 거래시간
	 * 
	 * @param dtTrade
	 *            거래시간
	 */
	public void setDtTrade(String dtTrade) {
		this.dtTrade = dtTrade;
	}

	/**
	 * Getter 원승인일자
	 */
	public String getYmdApprovalOrg() {
		return ymdApprovalOrg;
	}

	/**
	 * Setter 원승인일자
	 * 
	 * @param ymdApprovalOrg
	 *            원승인일자
	 */
	public void setYmdApprovalOrg(String ymdApprovalOrg) {
		this.ymdApprovalOrg = ymdApprovalOrg;
	}

	/**
	 * Getter 원승인번호
	 */
	public String getNoApprovalOrg() {
		return noApprovalOrg;
	}

	/**
	 * Setter 원승인번호
	 * 
	 * @param noApprovalOrg
	 *            원승인번호
	 */
	public void setNoApprovalOrg(String noApprovalOrg) {
		this.noApprovalOrg = noApprovalOrg;
	}

	/**
	 * Getter 출력메시지1
	 */
	public String getOutputMsg1() {
		return outputMsg1;
	}

	/**
	 * Setter 출력메시지1
	 * 
	 * @param outputMsg1
	 *            출력메시지1
	 */
	public void setOutputMsg1(String outputMsg1) {
		this.outputMsg1 = outputMsg1;
	}

	/**
	 * Getter 출력메시지2
	 */
	public String getOutputMsg2() {
		return outputMsg2;
	}

	/**
	 * Setter 출력메시지2
	 * 
	 * @param outputMsg2
	 *            출력메시지2
	 */
	public void setOutputMsg2(String outputMsg2) {
		this.outputMsg2 = outputMsg2;
	}

	/**
	 * Getter 출력메시지3
	 */
	public String getOutputMsg3() {
		return outputMsg3;
	}

	/**
	 * Setter 출력메시지3
	 * 
	 * @param outputMsg3
	 *            출력메시지3
	 */
	public void setOutputMsg3(String outputMsg3) {
		this.outputMsg3 = outputMsg3;
	}

	/**
	 * Getter 출력메시지4
	 */
	public String getOutputMsg4() {
		return outputMsg4;
	}

	/**
	 * Setter 출력메시지4
	 * 
	 * @param outputMsg4
	 *            출력메시지4
	 */
	public void setOutputMsg4(String outputMsg4) {
		this.outputMsg4 = outputMsg4;
	}
}
