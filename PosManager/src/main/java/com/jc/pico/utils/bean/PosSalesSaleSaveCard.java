package com.jc.pico.utils.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. S_SALE_SAVE : 매출정보
 * 2016. 8. 13, green, create
 */
public class PosSalesSaleSaveCard implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;
	/**
	 * VAN사코드
	 */
	@JsonProperty(value = "CD_VAN")
	private Long cdVan;
	/**
	 * 인터넷승인유무
	 */
	@JsonProperty(value = "YN_INTERNET")
	private Integer ynInternet;
	/**
	 * 승인유무
	 */
	@JsonProperty(value = "YN_APPROVAL")
	private Integer ynApproval;
	/**
	 * 단말기번호
	 */
	@JsonProperty(value = "TERMINAL_ID")
	private String terminalId;
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
	 * 할부개월
	 */
	@JsonProperty(value = "INSTALLMENT")
	private Integer installment;
	/**
	 * 거래금액
	 */
	@JsonProperty(value = "AMT_APPROVAL")
	private Double amtApproval;
	/**
	 * 봉사료
	 */
	@JsonProperty(value = "AMT_TIP")
	private Double amtTip;
	/**
	 * 부가세액
	 */
	@JsonProperty(value = "AMT_VAT")
	private Double amtVat;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 원거래일자
	 */
	@JsonProperty(value = "YMD_APPROVAL_ORG")
	private String ymdApprovalOrg;
	/**
	 * 원승인번호
	 */
	@JsonProperty(value = "NO_APPROVAL_ORG")
	private String noApprovalOrg;
	/**
	 * 응답코드
	 */
	@JsonProperty(value = "CD_ANSWER")
	private String cdAnswer;
	/**
	 * 거래고유번호
	 */
	@JsonProperty(value = "NO_TRADE")
	private String noTrade;
	/**
	 * 거래일시
	 */
	@JsonProperty(value = "DT_TRADE")
	private String dtTrade;
	/**
	 * 승인번호
	 */
	@JsonProperty(value = "NO_APPROVAL")
	private String noApproval;
	/**
	 * 발급사코드
	 */
	@JsonProperty(value = "CD_CARD_ISSUE")
	private String cdCardIssue;
	/**
	 * 발급사명
	 */
	@JsonProperty(value = "NM_CARD_ISSUE")
	private String nmCardIssue;
	/**
	 * 매입구분
	 */
	@JsonProperty(value = "DS_BUY")
	private String dsBuy;
	/**
	 * 매입사코드
	 */
	@JsonProperty(value = "CD_CARD_BUY")
	private String cdCardBuy;
	/**
	 * 매입사명
	 */
	@JsonProperty(value = "NM_CARD_BUY")
	private String nmCardBuy;
	/**
	 * 가맹점사업자번호
	 */
	@JsonProperty(value = "NO_AFFILIATE")
	private String noAffiliate;
	/**
	 * VAN승인일자
	 */
	@JsonProperty(value = "YMD_APPROVAL")
	private String ymdApproval;
	/**
	 * 출력제어코드
	 */
	@JsonProperty(value = "CD_OUTPUT_CONTROL")
	private String cdOutputControl;
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
	 * 테스트 승인 여부
	 */
	@JsonProperty(value = "YN_TEST")
	private Integer ynTest;

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
	 * Getter VAN사코드
	 */
	public Long getCdVan() {
		return cdVan;
	}

	/**
	 * Setter VAN사코드
	 * 
	 * @param cdVan
	 *            VAN사코드
	 */
	public void setCdVan(Long cdVan) {
		this.cdVan = cdVan;
	}

	/**
	 * Getter 인터넷승인유무
	 */
	public Integer getYnInternet() {
		return ynInternet;
	}

	/**
	 * Setter 인터넷승인유무
	 * 
	 * @param ynInternet
	 *            인터넷승인유무
	 */
	public void setYnInternet(Integer ynInternet) {
		this.ynInternet = ynInternet;
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
	 * Getter 단말기번호
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * Setter 단말기번호
	 * 
	 * @param terminalId
	 *            단말기번호
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
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
	 * Getter 할부개월
	 */
	public Integer getInstallment() {
		return installment;
	}

	/**
	 * Setter 할부개월
	 * 
	 * @param installment
	 *            할부개월
	 */
	public void setInstallment(Integer installment) {
		this.installment = installment;
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
	 * Getter 봉사료
	 */
	public Double getAmtTip() {
		return amtTip;
	}

	/**
	 * Setter 봉사료
	 * 
	 * @param amtTip
	 *            봉사료
	 */
	public void setAmtTip(Double amtTip) {
		this.amtTip = amtTip;
	}

	/**
	 * Getter 부가세액
	 */
	public Double getAmtVat() {
		return amtVat;
	}

	/**
	 * Setter 부가세액
	 * 
	 * @param amtVat
	 *            부가세액
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
	 * 
	 * @param amtDc
	 *            할인금액
	 */
	public void setAmtDc(Double amtDc) {
		this.amtDc = amtDc;
	}

	/**
	 * Getter 원거래일자
	 */
	public String getYmdApprovalOrg() {
		return ymdApprovalOrg;
	}

	/**
	 * Setter 원거래일자
	 * 
	 * @param ymdApprovalOrg
	 *            원거래일자
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
	 * Getter 응답코드
	 */
	public String getCdAnswer() {
		return cdAnswer;
	}

	/**
	 * Setter 응답코드
	 * 
	 * @param cdAnswer
	 *            응답코드
	 */
	public void setCdAnswer(String cdAnswer) {
		this.cdAnswer = cdAnswer;
	}

	/**
	 * Getter 거래고유번호
	 */
	public String getNoTrade() {
		return noTrade;
	}

	/**
	 * Setter 거래고유번호
	 * 
	 * @param noTrade
	 *            거래고유번호
	 */
	public void setNoTrade(String noTrade) {
		this.noTrade = noTrade;
	}

	/**
	 * Getter 거래일시
	 */
	public String getDtTrade() {
		return dtTrade;
	}

	/**
	 * Setter 거래일시
	 * 
	 * @param dtTrade
	 *            거래일시
	 */
	public void setDtTrade(String dtTrade) {
		this.dtTrade = dtTrade;
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
	 * Getter 발급사코드
	 */
	public String getCdCardIssue() {
		return cdCardIssue;
	}

	/**
	 * Setter 발급사코드
	 * 
	 * @param cdCardIssue
	 *            발급사코드
	 */
	public void setCdCardIssue(String cdCardIssue) {
		this.cdCardIssue = cdCardIssue;
	}

	/**
	 * Getter 발급사명
	 */
	public String getNmCardIssue() {
		return nmCardIssue;
	}

	/**
	 * Setter 발급사명
	 * 
	 * @param nmCardIssue
	 *            발급사명
	 */
	public void setNmCardIssue(String nmCardIssue) {
		this.nmCardIssue = nmCardIssue;
	}

	/**
	 * Getter 매입구분
	 */
	public String getDsBuy() {
		return dsBuy;
	}

	/**
	 * Setter 매입구분
	 * 
	 * @param dsBuy
	 *            매입구분
	 */
	public void setDsBuy(String dsBuy) {
		this.dsBuy = dsBuy;
	}

	/**
	 * Getter 매입사코드
	 */
	public String getCdCardBuy() {
		return cdCardBuy;
	}

	/**
	 * Setter 매입사코드
	 * 
	 * @param cdCardBuy
	 *            매입사코드
	 */
	public void setCdCardBuy(String cdCardBuy) {
		this.cdCardBuy = cdCardBuy;
	}

	/**
	 * Getter 매입사명
	 */
	public String getNmCardBuy() {
		return nmCardBuy;
	}

	/**
	 * Setter 매입사명
	 * 
	 * @param nmCardBuy
	 *            매입사명
	 */
	public void setNmCardBuy(String nmCardBuy) {
		this.nmCardBuy = nmCardBuy;
	}

	/**
	 * Getter 가맹점사업자번호
	 */
	public String getNoAffiliate() {
		return noAffiliate;
	}

	/**
	 * Setter 가맹점사업자번호
	 * 
	 * @param noAffiliate
	 *            가맹점사업자번호
	 */
	public void setNoAffiliate(String noAffiliate) {
		this.noAffiliate = noAffiliate;
	}

	/**
	 * Getter VAN승인일자
	 */
	public String getYmdApproval() {
		return ymdApproval;
	}

	/**
	 * Setter VAN승인일자
	 * 
	 * @param ymdApproval
	 *            VAN승인일자
	 */
	public void setYmdApproval(String ymdApproval) {
		this.ymdApproval = ymdApproval;
	}

	/**
	 * Getter 출력제어코드
	 */
	public String getCdOutputControl() {
		return cdOutputControl;
	}

	/**
	 * Setter 출력제어코드
	 * 
	 * @param cdOutputControl
	 *            출력제어코드
	 */
	public void setCdOutputControl(String cdOutputControl) {
		this.cdOutputControl = cdOutputControl;
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

	/**
	 * Getter 테스트 승인 여부
	 */
	public Integer getYnTest() {
		return ynTest;
	}

	/**
	 * Setter 테스트 승인 여부
	 * 
	 * @param ynTest
	 *            테스트 승인 여부
	 */
	public void setYnTest(Integer ynTest) {
		this.ynTest = ynTest;
	}
}
