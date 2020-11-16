package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. S_SALE_SAVE : 매출정보
 * 2016. 8. 13, green, create
 */
public class PosSalesSaleSave implements Serializable {
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
	 * 날짜
	 */
	@JsonProperty(value = "YMD_SALE")
	private String ymdSale;
	/**
	 * 포스번호
	 */
	@JsonProperty(value = "NO_POS")
	private String noPos;

	/**
	 * 전표번호
	 */
	@JsonProperty(value = "NO_RCP")
	private Long noRcp;

	/**
	 * 판매형태코드(1:정상, 2:반품, 3:주문, 4:시식, 5:증정, 6:폐기, 7:접대, 8:손실, 9:Void, 10:점간이동, 11:배달, 12:선입금,
	 * 13:외상매출입금, 14:상품교환)
	 */
	@JsonProperty(value = "DS_SALE")
	private Integer dsSale;

	/**
	 * 주문형태 (1:테이블, 2:배달, 3:포장)
	 */
	@JsonProperty(value = "DS_ORDER")
	private Byte dsOrder;

	/**
	 * 주문유형코드(605001:주문,605002:예약,605003:계약)
	 */
	@JsonProperty(value = "CD_ORDER_TYPE")
	private Integer cdOrderType;

	/**
	 * 주문 경로 타입(pos: 606001, app: 606002, cleck: 606003, tab: 606004)
	 */
	@JsonProperty(value = "CD_ORDER_PATH")
	private Integer cdOrderPath;

	/**
	 * 주문처리코드(1:미승인, 2:승인, 3:취소, 4:거부, 5:완료)
	 */
	@JsonProperty(value = "CD_ORDER_VERIFY")
	private Integer cdOrderVerify;

	/**
	 * 고객번호
	 */
	@JsonProperty(value = "CD_MEMBER")
	private String cdMember;

	/**
	 * 고객명
	 */
	@JsonProperty(value = "NM_MEMBER")
	private String nmMember;
	/**
	 * 주문 처리 직원 ID
	 */
	@JsonProperty(value = "CD_EMPLOYEE")
	private String cdEmployee;
	/**
	 * 금액
	 */
	@JsonProperty(value = "AMT_SALE")
	private Double amtSale;
	/**
	 * 공급가액
	 */
	@JsonProperty(value = "AMT_SUPPLY")
	private Double amtSupply;
	/**
	 * 부가세
	 */
	@JsonProperty(value = "AMT_VAT")
	private Double amtVat;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 봉사료
	 */
	@JsonProperty(value = "AMT_TIP")
	private Double amtTip;
	/**
	 * 고객형태코드
	 */
	@JsonProperty(value = "CD_MEMBER_TYPE")
	private Integer cdMemberType;
	/**
	 * 고객연령대코드
	 */
	@JsonProperty(value = "CD_MEMBER_AGE")
	private Integer cdMemberAge;
	/**
	 * 고객성별코드
	 */
	@JsonProperty(value = "CD_MEMBER_SEX")
	private Integer cdMemberSex;
	/**
	 * 객수
	 */
	@JsonProperty(value = "CNT_CUSTOMER")
	private Integer cntCustomer;
	/**
	 * 매출일시
	 */
	@JsonProperty(value = "DT_SALE")
	private String dtSale;
	/**
	 * 입장일시
	 */
	@JsonProperty(value = "DT_ADMISSION")
	private String dtAdmission;
	/**
	 * 퇴장일시
	 */
	@JsonProperty(value = "DT_EXIT")
	private String dtExit;
	/**
	 * 테이블번호
	 */
	@JsonProperty(value = "NO_TABLE")
	private Long noTable;
	/**
	 * 테이블섹션코드
	 */
	@JsonProperty(value = "CD_SECTION")
	private Long cdSection;
	/**
	 * 여행사코드
	 */
	@JsonProperty(value = "CD_TOUR")
	private String cdTour;
	/**
	 * 가이드번호
	 */
	@JsonProperty(value = "NO_GUIDE")
	private Long noGuide;
	/**
	 * 외국인유무
	 */
	@JsonProperty(value = "YN_FOREIGNER")
	private Integer ynForeigner;
	/**
	 * 통화단위코드
	 */
	@JsonProperty(value = "CD_CURRENCY")
	private Integer cdCurrency;
	/**
	 * 환율
	 */
	@JsonProperty(value = "RT_EXCHANGE")
	private Double rtExchange;
	/**
	 * 외화금액
	 */
	@JsonProperty(value = "AMT_FOREIGN_CURRENCY")
	private Double amtForeignCurrency;
	/**
	 * 달러환율
	 */
	@JsonProperty(value = "RT_EXCHANGE_USD")
	private Double rtExchangeUsd;
	/**
	 * 달러금액
	 */
	@JsonProperty(value = "AMT_USD")
	private Double amtUsd;
	/**
	 * 적립 포인트
	 */
	@JsonProperty(value = "PNT_OCCUR")
	private Double pntOccur;
	/**
	 * 잔여 포인트
	 */
	@JsonProperty(value = "PNT_REST")
	private Double pntRest;
	/**
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;
	/**
	 * 브랜드코드
	 */
	@JsonProperty(value = "CD_BRAND")
	private Long cdBrand;
	/**
	 * 원 매출일자
	 */
	@JsonProperty(value = "YMD_SALE_ORG")
	private String ymdSaleOrg;
	/**
	 * 원 포스번호
	 */
	@JsonProperty(value = "NO_POS_ORG")
	private String noPosOrg;
	/**
	 * 원 영수증번호
	 */
	@JsonProperty(value = "NO_RCP_ORG")
	private Long noRcpOrg;
	/**
	 * 마감번호
	 */
	@JsonProperty(value = "NO_CLOSING")
	private Long noClosing;
	/**
	 * 반품사유코드
	 */
	@JsonProperty(value = "CD_RETURN_REASON")
	private Integer cdReturnReason;
	/**
	 * 주문일자
	 */
	@JsonProperty(value = "YMD_ORDER")
	private String ymdOrder;
	/**
	 * 주문번호
	 */
	@JsonProperty(value = "NO_ORDER")
	private Long noOrder;

	/**
	 * 주문번호
	 */
	@JsonProperty(value = "DT_ORDER")
	private String dtOrder;

	/**
	 * 승인시간 (yyyyMMddHHmmss)
	 */
	@JsonProperty(value = "DT_APPROVAL")
	private String dtApproval;

	/**
	 * 거래처코드
	 */
	@JsonProperty(value = "CD_VENDOR")
	private String cdVendor;
	/**
	 * 진동벨번호
	 */
	@JsonProperty(value = "NO_PAGER")
	private Long noPager;
	/**
	 * 발주번호
	 */
	@JsonProperty(value = "CD_BALJU")
	private String cdBalju;
	/**
	 * 상세 내역
	 */
	@JsonProperty(value = "DETAIL")
	private List<PosSalesSaleSaveDetail> detail;
	/**
	 * 결제 내역
	 */
	@JsonProperty(value = "PAY")
	private List<PosSalesSaleSavePay> pay;
	/**
	 * 할인 내역
	 */
	@JsonProperty(value = "DISCOUNT")
	private List<PosSalesSaleSaveDiscount> discount;
	/**
	 * 현금영수증 내역
	 */
	@JsonProperty(value = "CASH")
	private List<PosSalesSaleSaveCash> cash;
	/**
	 * 신용카드 내역
	 */
	@JsonProperty(value = "CARD")
	private List<PosSalesSaleSaveCard> card;
	/**
	 * 캐쉬백 내역
	 */
	@JsonProperty(value = "OKCASHBAG")
	private List<PosSalesSaleSaveOkcashbag> okcashbag;

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
	 * Getter 날짜
	 */
	public String getYmdSale() {
		return ymdSale;
	}

	/**
	 * Setter 날짜
	 * 
	 * @param ymdSale
	 *            날짜
	 */
	public void setYmdSale(String ymdSale) {
		this.ymdSale = ymdSale;
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
	 * Getter 전표번호
	 */
	public Long getNoRcp() {
		return noRcp;
	}

	/**
	 * Setter 전표번호
	 * 
	 * @param noRcp
	 *            전표번호
	 */
	public void setNoRcp(Long noRcp) {
		this.noRcp = noRcp;
	}

	/**
	 * Getter 판매형태코드(1:정상, 2:반품, 3:주문, 4:시식, 5:증정, 6:폐기, 7:접대, 8:손실, 9:Void, 10:점간이동, 11:배달, 12:선입금,
	 * 13:외상매출입금, 14:상품교환)
	 */
	public Integer getDsSale() {
		return dsSale;
	}

	/**
	 * Setter 판매형태코드(1:정상, 2:반품, 3:주문, 4:시식, 5:증정, 6:폐기, 7:접대, 8:손실, 9:Void, 10:점간이동, 11:배달, 12:선입금,
	 * 13:외상매출입금, 14:상품교환)
	 * 
	 * @param dsSale
	 *            판매형태코드(1:정상, 2:반품, 3:주문, 4:시식, 5:증정, 6:폐기, 7:접대, 8:손실, 9:Void, 10:점간이동, 11:배달,
	 *            12:선입금, 13:외상매출입금, 14:상품교환)
	 */
	public void setDsSale(Integer dsSale) {
		this.dsSale = dsSale;
	}

	/**
	 * Getter 고객번호
	 */
	public String getCdMember() {
		return cdMember;
	}

	/**
	 * Setter 고객번호
	 * 
	 * @param cdMember
	 *            고객번호
	 */
	public void setCdMember(String cdMember) {
		this.cdMember = cdMember;
	}

	/**
	 * Getter 고객명
	 */
	public String getNmMember() {
		return nmMember;
	}

	/**
	 * Setter 고객명
	 * 
	 * @param nmMember
	 *            고객명
	 */
	public void setNmMember(String nmMember) {
		this.nmMember = nmMember;
	}

	/**
	 * Getter 결제자
	 */
	public String getCdEmployee() {
		return cdEmployee;
	}

	/**
	 * Setter 결제자
	 * 
	 * @param cdEmployee
	 *            결제자
	 */
	public void setCdEmployee(String cdEmployee) {
		this.cdEmployee = cdEmployee;
	}

	/**
	 * Getter 금액
	 */
	public Double getAmtSale() {
		return amtSale;
	}

	/**
	 * Setter 금액
	 * 
	 * @param amtSale
	 *            금액
	 */
	public void setAmtSale(Double amtSale) {
		this.amtSale = amtSale;
	}

	/**
	 * Getter 공급가액
	 */
	public Double getAmtSupply() {
		return amtSupply;
	}

	/**
	 * Setter 공급가액
	 * 
	 * @param amtSupply
	 *            공급가액
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
	 * 
	 * @param amtTax
	 *            부가세
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
	 * Getter 고객형태코드
	 */
	public Integer getCdMemberType() {
		return cdMemberType;
	}

	/**
	 * Setter 고객형태코드
	 * 
	 * @param cdMemberType
	 *            고객형태코드
	 */
	public void setCdMemberType(Integer cdMemberType) {
		this.cdMemberType = cdMemberType;
	}

	/**
	 * Getter 고객연령대코드
	 */
	public Integer getCdMemberAge() {
		return cdMemberAge;
	}

	/**
	 * Setter 고객연령대코드
	 * 
	 * @param cdMemberAge
	 *            고객연령대코드
	 */
	public void setCdMemberAge(Integer cdMemberAge) {
		this.cdMemberAge = cdMemberAge;
	}

	/**
	 * Getter 고객성별코드
	 */
	public Integer getCdMemberSex() {
		return cdMemberSex;
	}

	/**
	 * Setter 고객성별코드
	 * 
	 * @param cdMemberSex
	 *            고객성별코드
	 */
	public void setCdMemberSex(Integer cdMemberSex) {
		this.cdMemberSex = cdMemberSex;
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
	 * Getter 매출일시
	 */
	public String getDtSale() {
		return dtSale;
	}

	/**
	 * Setter 매출일시
	 * 
	 * @param dtSale
	 *            매출일시
	 */
	public void setDtSale(String dtSale) {
		this.dtSale = dtSale;
	}

	/**
	 * Getter 입장일시
	 */
	public String getDtAdmission() {
		return dtAdmission;
	}

	/**
	 * Setter 입장일시
	 * 
	 * @param dtAdmission
	 *            입장일시
	 */
	public void setDtAdmission(String dtAdmission) {
		this.dtAdmission = dtAdmission;
	}

	/**
	 * Getter 퇴장일시
	 */
	public String getDtExit() {
		return dtExit;
	}

	/**
	 * Setter 퇴장일시
	 * 
	 * @param dtExit
	 *            퇴장일시
	 */
	public void setDtExit(String dtExit) {
		this.dtExit = dtExit;
	}

	/**
	 * Getter 테이블번호
	 */
	public Long getNoTable() {
		return noTable;
	}

	/**
	 * Setter 테이블번호
	 * 
	 * @param noTable
	 *            테이블번호
	 */
	public void setNoTable(Long noTable) {
		this.noTable = noTable;
	}

	/**
	 * Getter 테이블섹션코드
	 */
	public Long getCdSection() {
		return cdSection;
	}

	/**
	 * Setter 테이블섹션코드
	 * 
	 * @param cdSection
	 *            테이블섹션코드
	 */
	public void setCdSection(Long cdSection) {
		this.cdSection = cdSection;
	}

	/**
	 * Getter 여행사코드
	 */
	public String getCdTour() {
		return cdTour;
	}

	/**
	 * Setter 여행사코드
	 * 
	 * @param cdTour
	 *            여행사코드
	 */
	public void setCdTour(String cdTour) {
		this.cdTour = cdTour;
	}

	/**
	 * Getter 가이드번호
	 */
	public Long getNoGuide() {
		return noGuide;
	}

	/**
	 * Setter 가이드번호
	 * 
	 * @param noGuide
	 *            가이드번호
	 */
	public void setNoGuide(Long noGuide) {
		this.noGuide = noGuide;
	}

	/**
	 * Getter 외국인유무
	 */
	public Integer getYnForeigner() {
		return ynForeigner;
	}

	/**
	 * Setter 외국인유무
	 * 
	 * @param ynForeigner
	 *            외국인유무
	 */
	public void setYnForeigner(Integer ynForeigner) {
		this.ynForeigner = ynForeigner;
	}

	/**
	 * Getter 통화단위코드
	 */
	public Integer getCdCurrency() {
		return cdCurrency;
	}

	/**
	 * Setter 통화단위코드
	 * 
	 * @param cdCurrency
	 *            통화단위코드
	 */
	public void setCdCurrency(Integer cdCurrency) {
		this.cdCurrency = cdCurrency;
	}

	/**
	 * Getter 환율
	 */
	public Double getRtExchange() {
		return rtExchange;
	}

	/**
	 * Setter 환율
	 * 
	 * @param rtExchange
	 *            환율
	 */
	public void setRtExchange(Double rtExchange) {
		this.rtExchange = rtExchange;
	}

	/**
	 * Getter 외화금액
	 */
	public Double getAmtForeignCurrency() {
		return amtForeignCurrency;
	}

	/**
	 * Setter 외화금액
	 * 
	 * @param amtForeignCurrency
	 *            외화금액
	 */
	public void setAmtForeignCurrency(Double amtForeignCurrency) {
		this.amtForeignCurrency = amtForeignCurrency;
	}

	/**
	 * Getter 달러환율
	 */
	public Double getRtExchangeUsd() {
		return rtExchangeUsd;
	}

	/**
	 * Setter 달러환율
	 * 
	 * @param rtExchangeUsd
	 *            달러환율
	 */
	public void setRtExchangeUsd(Double rtExchangeUsd) {
		this.rtExchangeUsd = rtExchangeUsd;
	}

	/**
	 * Getter 달러금액
	 */
	public Double getAmtUsd() {
		return amtUsd;
	}

	/**
	 * Setter 달러금액
	 * 
	 * @param amtUsd
	 *            달러금액
	 */
	public void setAmtUsd(Double amtUsd) {
		this.amtUsd = amtUsd;
	}

	/**
	 * Getter 적립 포인트
	 */
	public Double getPntOccur() {
		return pntOccur;
	}

	/**
	 * Setter 적립 포인트
	 * 
	 * @param pntOccur
	 *            적립 포인트
	 */
	public void setPntOccur(Double pntOccur) {
		this.pntOccur = pntOccur;
	}

	/**
	 * Getter 잔여 포인트
	 */
	public Double getPntRest() {
		return pntRest;
	}

	/**
	 * Setter 잔여 포인트
	 * 
	 * @param pntRest
	 *            잔여 포인트
	 */
	public void setPntRest(Double pntRest) {
		this.pntRest = pntRest;
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
	 * Getter 브랜드코드
	 */
	public Long getCdBrand() {
		return cdBrand;
	}

	/**
	 * Setter 브랜드코드
	 * 
	 * @param cdBrand
	 *            브랜드코드
	 */
	public void setCdBrand(Long cdBrand) {
		this.cdBrand = cdBrand;
	}

	/**
	 * Getter 원 매출일자
	 */
	public String getYmdSaleOrg() {
		return ymdSaleOrg;
	}

	/**
	 * Setter 원 매출일자
	 * 
	 * @param ymdSaleOrg
	 *            원 매출일자
	 */
	public void setYmdSaleOrg(String ymdSaleOrg) {
		this.ymdSaleOrg = ymdSaleOrg;
	}

	/**
	 * Getter 원 포스번호
	 */
	public String getNoPosOrg() {
		return noPosOrg;
	}

	/**
	 * Setter 원 포스번호
	 * 
	 * @param noPosOrg
	 *            원 포스번호
	 */
	public void setNoPosOrg(String noPosOrg) {
		this.noPosOrg = noPosOrg;
	}

	/**
	 * Getter 원 영수증번호
	 */
	public Long getNoRcpOrg() {
		return noRcpOrg;
	}

	/**
	 * Setter 원 영수증번호
	 * 
	 * @param noRcpOrg
	 *            원 영수증번호
	 */
	public void setNoRcpOrg(Long noRcpOrg) {
		this.noRcpOrg = noRcpOrg;
	}

	/**
	 * Getter 마감번호
	 */
	public Long getNoClosing() {
		return noClosing;
	}

	/**
	 * Setter 마감번호
	 * 
	 * @param noClosing
	 *            마감번호
	 */
	public void setNoClosing(Long noClosing) {
		this.noClosing = noClosing;
	}

	/**
	 * Getter 반품사유코드
	 */
	public Integer getCdReturnReason() {
		return cdReturnReason;
	}

	/**
	 * Setter 반품사유코드
	 * 
	 * @param cdReturnReason
	 *            반품사유코드
	 */
	public void setCdReturnReason(Integer cdReturnReason) {
		this.cdReturnReason = cdReturnReason;
	}

	/**
	 * Getter 주문일자
	 */
	public String getYmdOrder() {
		return ymdOrder;
	}

	/**
	 * Setter 주문일자
	 * 
	 * @param ymdOrder
	 *            주문일자
	 */
	public void setYmdOrder(String ymdOrder) {
		this.ymdOrder = ymdOrder;
	}

	/**
	 * Getter 주문번호
	 */
	public Long getNoOrder() {
		return noOrder;
	}

	/**
	 * Setter 주문번호
	 * 
	 * @param noOrder
	 *            주문번호
	 */
	public void setNoOrder(Long noOrder) {
		this.noOrder = noOrder;
	}

	/**
	 * Getter 거래처코드
	 */
	public String getCdVendor() {
		return cdVendor;
	}

	/**
	 * Setter 거래처코드
	 * 
	 * @param cdVendor
	 *            거래처코드
	 */
	public void setCdVendor(String cdVendor) {
		this.cdVendor = cdVendor;
	}

	/**
	 * Getter 진동벨번호
	 */
	public Long getNoPager() {
		return noPager;
	}

	/**
	 * Setter 진동벨번호
	 * 
	 * @param noPager
	 *            진동벨번호
	 */
	public void setNoPager(Long noPager) {
		this.noPager = noPager;
	}

	/**
	 * Getter 발주번호
	 */
	public String getCdBalju() {
		return cdBalju;
	}

	/**
	 * Setter 발주번호
	 * 
	 * @param cdBalju
	 *            발주번호
	 */
	public void setCdBalju(String cdBalju) {
		this.cdBalju = cdBalju;
	}

	/**
	 * Getter 상세 내역
	 */
	public List<PosSalesSaleSaveDetail> getDetail() {
		return detail;
	}

	/**
	 * Setter 상세 내역
	 * 
	 * @param detail
	 *            상세 내역
	 */
	public void setDetail(List<PosSalesSaleSaveDetail> detail) {
		this.detail = detail;
	}

	/**
	 * Getter 결제 내역
	 */
	public List<PosSalesSaleSavePay> getPay() {
		return pay;
	}

	/**
	 * Setter 결제 내역
	 * 
	 * @param pay
	 *            결제 내역
	 */
	public void setPay(List<PosSalesSaleSavePay> pay) {
		this.pay = pay;
	}

	/**
	 * Getter 할인 내역
	 */
	public List<PosSalesSaleSaveDiscount> getDiscount() {
		return discount;
	}

	/**
	 * Setter 할인 내역
	 * 
	 * @param discount
	 *            할인 내역
	 */
	public void setDiscount(List<PosSalesSaleSaveDiscount> discount) {
		this.discount = discount;
	}

	/**
	 * Getter 현금영수증 내역
	 */
	public List<PosSalesSaleSaveCash> getCash() {
		return cash;
	}

	/**
	 * Setter 현금영수증 내역
	 * 
	 * @param cash
	 *            현금영수증 내역
	 */
	public void setCash(List<PosSalesSaleSaveCash> cash) {
		this.cash = cash;
	}

	/**
	 * Getter 신용카드 내역
	 */
	public List<PosSalesSaleSaveCard> getCard() {
		return card;
	}

	/**
	 * Setter 신용카드 내역
	 * 
	 * @param card
	 *            신용카드 내역
	 */
	public void setCard(List<PosSalesSaleSaveCard> card) {
		this.card = card;
	}

	/**
	 * Getter 캐쉬백 내역
	 */
	public List<PosSalesSaleSaveOkcashbag> getOkcashbag() {
		return okcashbag;
	}

	/**
	 * Setter 캐쉬백 내역
	 * 
	 * @param okcashbag
	 *            캐쉬백 내역
	 */
	public void setOkcashbag(List<PosSalesSaleSaveOkcashbag> okcashbag) {
		this.okcashbag = okcashbag;
	}

	public Integer getCdOrderPath() {
		return cdOrderPath;
	}

	public void setCdOrderPath(Integer cdOrderPath) {
		this.cdOrderPath = cdOrderPath;
	}

	public Byte getDsOrder() {
		return dsOrder;
	}

	public void setDsOrder(Byte dsOrder) {
		this.dsOrder = dsOrder;
	}

	public String getDtOrder() {
		return dtOrder;
	}

	public void setDtOrder(String dtOrder) {
		this.dtOrder = dtOrder;
	}

	public Integer getCdOrderType() {
		return cdOrderType;
	}

	public void setCdOrderType(Integer cdOrderType) {
		this.cdOrderType = cdOrderType;
	}

	public Integer getCdOrderVerify() {
		return cdOrderVerify;
	}

	public void setCdOrderVerify(Integer cdOrderVerify) {
		this.cdOrderVerify = cdOrderVerify;
	}

	public String getDtApproval() {
		return dtApproval;
	}

	public void setDtApproval(String dtApproval) {
		this.dtApproval = dtApproval;
	}

	@Override
	public String toString() {
		return "PosSalesSaleSave [cdCompany=" + cdCompany + ", cdStore=" + cdStore + ", ymdSale=" + ymdSale + ", noPos=" + noPos + ", noRcp=" + noRcp
				+ ", dsSale=" + dsSale + ", dsOrder=" + dsOrder + ", cdOrderType=" + cdOrderType + ", cdOrderPath=" + cdOrderPath + ", cdOrderVerify="
				+ cdOrderVerify + ", cdMember=" + cdMember + ", nmMember=" + nmMember + ", cdEmployee=" + cdEmployee + ", amtSale=" + amtSale
				+ ", amtSupply=" + amtSupply + ", amtVat=" + amtVat + ", amtDc=" + amtDc + ", amtTip=" + amtTip + ", cdMemberType=" + cdMemberType
				+ ", cdMemberAge=" + cdMemberAge + ", cdMemberSex=" + cdMemberSex + ", cntCustomer=" + cntCustomer + ", dtSale=" + dtSale
				+ ", dtAdmission=" + dtAdmission + ", dtExit=" + dtExit + ", noTable=" + noTable + ", cdSection=" + cdSection + ", cdTour=" + cdTour
				+ ", noGuide=" + noGuide + ", ynForeigner=" + ynForeigner + ", cdCurrency=" + cdCurrency + ", rtExchange=" + rtExchange
				+ ", amtForeignCurrency=" + amtForeignCurrency + ", rtExchangeUsd=" + rtExchangeUsd + ", amtUsd=" + amtUsd + ", pntOccur=" + pntOccur
				+ ", pntRest=" + pntRest + ", memo=" + memo + ", cdBrand=" + cdBrand + ", ymdSaleOrg=" + ymdSaleOrg + ", noPosOrg=" + noPosOrg
				+ ", noRcpOrg=" + noRcpOrg + ", noClosing=" + noClosing + ", cdReturnReason=" + cdReturnReason + ", ymdOrder=" + ymdOrder
				+ ", noOrder=" + noOrder + ", dtOrder=" + dtOrder + ", dtApproval=" + dtApproval + ", cdVendor=" + cdVendor + ", noPager=" + noPager
				+ ", cdBalju=" + cdBalju + ", detail=" + detail + ", pay=" + pay + ", discount=" + discount + ", cash=" + cash + ", card=" + card
				+ ", okcashbag=" + okcashbag + "]";
	}

}
