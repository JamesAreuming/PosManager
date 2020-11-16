package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. S_ORDER_INFO : 주문정보
 * 11. S_ORDER_SAVE : 주문정보 저장
 * 2016. 8. 13, green, create
 * 2016. 9. 7, hyo, 주문 정보 조회/저장 bean 통합
 */
public class PosSalesOrderInfo implements Serializable {
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
	 * 주문일자(yyyymmdd)
	 */
	@JsonProperty(value = "YMD_ORDER")
	private String ymdOrder;

	/**
	 * 주문번호
	 */
	@JsonProperty(value = "NO_ORDER")
	private Long noOrder;

	/**
	 * 주문ID
	 */
	@JsonProperty(value = "ORDER_ID")
	private Long orderId;

	/**
	 * 주문 경로 타입(pos: 606001, app: 606002, cleck: 606003, tab: 606004)
	 */
	@JsonProperty(value = "CD_ORDER_TYPE")
	private Integer cdOrderType;

	/**
	 * 주문유형(605001:주문,605002:예약,605003:계약)
	 */
	@JsonProperty(value = "CD_ORDER_PATH")
	private Integer cdOrderPath;

	/**
	 * 주문상태코드(1.변동없음,2.신규,3.수정(수량변경),4.삭제)
	 */
	@JsonProperty(value = "CD_ORDER_STATUS")
	private Integer cdOrderStatus;

	/**
	 * 주문처리코드(1:미승인, 2:승인, 3:취소, 4:거부, 5:완료)
	 */
	@JsonProperty(value = "CD_ORDER_VERIFY")
	private Integer cdOrderVerify;

	/**
	 * 테이블번호
	 */
	@JsonProperty(value = "NO_TABLE")
	private Long noTable;

	/**
	 * 주문형태 (1:테이블, 2:배달, 3:포장)
	 */
	@JsonProperty(value = "DS_ORDER")
	private Integer dsOrder;

	/**
	 * 입장시간(yyyymmddhhnnss)
	 */
	@JsonProperty(value = "DT_ADMISSION")
	private String dtAdmission;
	/**
	 * 퇴장시간(yyyymmddhhnnss)
	 */
	@JsonProperty(value = "DT_EXIT")
	private String dtExit;
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
	 * 회원코드
	 */
	@JsonProperty(value = "CD_MEMBER")
	private String cdMember;
	/**
	 * 회원명
	 */
	@JsonProperty(value = "NM_MEMBER")
	private String nmMember;

	/**
	 * 주문자 전화 번호
	 */
	@JsonProperty(value = "TEL_MOBILE_MEMBER")
	private String telMobileMember;

	/**
	 * 배달고객번호
	 */
	@JsonProperty(value = "CD_MEMBER_DELIVERY")
	private String cdMemberDelivery;
	/**
	 * 사원코드
	 */
	@JsonProperty(value = "CD_EMPLOYEE")
	private String cdEmployee;
	/**
	 * 주문금액
	 */
	@JsonProperty(value = "AMT_ORDER")
	private Double amtOrder;
	/**
	 * 공급가
	 */
	@JsonProperty(value = "AMT_SUPPLY")
	private Double amtSupply;
	/**
	 * 부가세
	 */
	@JsonProperty(value = "AMT_TAX")
	private Double amtTax;
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
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;
	/**
	 * 주문상태(1:주문 2:전체취소 3:합석)
	 */
	@JsonProperty(value = "DS_STATUS")
	private Integer dsStatus;
	/**
	 * 정산 완료 여부(1:예 0:아니오)
	 */
	@JsonProperty(value = "YN_SALE")
	private Integer ynSale;
	/**
	 * 거래처 코드
	 */
	@JsonProperty(value = "CD_VENDOR")
	private String cdVendor;
	/**
	 * 주문번호출력
	 */
	@JsonProperty(value = "NO_ORDER_PRINT")
	private Long noOrderPrint;
	/**
	 * 예약일자 yyyyMMdd
	 */
	@JsonProperty(value = "YMD_BOOKING")
	private String ymdBooking;
	/**
	 * 예약번호
	 */
	@JsonProperty(value = "NO_BOOKING")
	private Long noBooking;
	/**
	 * 예약 시간(HHmm)
	 */
	@JsonProperty(value = "RES_TIME_BOOKING")
	private String resTimeBooking;

	/**
	 * 예약 등록 일시 (yyyyMMddHHmmss)
	 */
	@JsonProperty(value = "RES_DT_INSERT")
	private String resDtInsert;

	/**
	 * 예약 인원
	 */
	@JsonProperty(value = "RES_CNT_CUSTOMER")
	private Integer resCntCustomer;

	/**
	 * 취소 사유 (827)
	 */
	@JsonProperty(value = "CD_CANCEL_REASON")
	private Integer cdCancelReason;

	/**
	 * 주문일시 (yyyyMMddHHmmss)
	 */
	@JsonProperty(value = "DT_ORDER")
	private String dtOrder;

	/**
	 * 승인시간 (yyyyMMddHHmmss)
	 */
	@JsonProperty(value = "DT_APPROVAL")
	private String dtApproval;

	/**
	 * 
	 * 대기 시간 (hhmm)
	 */
	@JsonProperty(value = "TIME_WAIT")
	private String timeWait;

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
	 * 상세 내역
	 */
	@JsonProperty(value = "DETAIL")
	private List<PosSalesOrderInfoDetail> detail;

	/**
	 * 결제 내역
	 */
	@JsonProperty(value = "PAYMENTS")
	private List<PosOrderPayment> payments;

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
	 * Getter 주문일자(yyyymmdd)
	 */
	public String getYmdOrder() {
		return ymdOrder;
	}

	/**
	 * Setter 주문일자(yyyymmdd)
	 * 
	 * @param ymdOrder
	 *            주문일자(yyyymmdd)
	 */
	public void setYmdOrder(String ymdOrder) {
		this.ymdOrder = ymdOrder;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	 * Getter 주문유형(605001:주문,605002:예약,605003:계약)
	 */
	public Integer getCdOrderType() {
		return cdOrderType;
	}

	/**
	 * Setter 주문유형(605001:주문,605002:예약,605003:계약)
	 * 
	 * @param cdOrder
	 *            주문유형(605001:주문,605002:예약,605003:계약)
	 */
	public void setCdOrderType(Integer cdOrderType) {
		this.cdOrderType = cdOrderType;
	}

	/**
	 * Getter 주문형태 (1:테이블, 2:배달, 3:포장)
	 */
	public Integer getDsOrder() {
		return dsOrder;
	}

	/**
	 * Setter 주문형태 (1:테이블, 2:배달, 3:포장)
	 * 
	 * @param dsOrder
	 *            주문형태 (1:테이블, 2:배달, 3:포장)
	 */
	public void setDsOrder(Integer dsOrder) {
		this.dsOrder = dsOrder;
	}

	/**
	 * Getter 입장시간(yyyymmddhhnnss)
	 */
	public String getDtAdmission() {
		return dtAdmission;
	}

	/**
	 * Setter 입장시간(yyyymmddhhnnss)
	 * 
	 * @param dtAdmission
	 *            입장시간(yyyymmddhhnnss)
	 */
	public void setDtAdmission(String dtAdmission) {
		this.dtAdmission = dtAdmission;
	}

	/**
	 * Getter 퇴장시간(yyyymmddhhnnss)
	 */
	public String getDtExit() {
		return dtExit;
	}

	/**
	 * Setter 퇴장시간(yyyymmddhhnnss)
	 * 
	 * @param dtExit
	 *            퇴장시간(yyyymmddhhnnss)
	 */
	public void setDtExit(String dtExit) {
		this.dtExit = dtExit;
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
	 * Getter 회원코드
	 */
	public String getCdMember() {
		return cdMember;
	}

	/**
	 * Setter 회원코드
	 * 
	 * @param cdMember
	 *            회원코드
	 */
	public void setCdMember(String cdMember) {
		this.cdMember = cdMember;
	}

	/**
	 * Getter 회원명
	 */
	public String getNmMember() {
		return nmMember;
	}

	/**
	 * Setter 회원명
	 * 
	 * @param nmMember
	 *            회원명
	 */
	public void setNmMember(String nmMember) {
		this.nmMember = nmMember;
	}

	/**
	 * Getter 배달고객번호
	 */
	public String getCdMemberDelivery() {
		return cdMemberDelivery;
	}

	/**
	 * Setter 배달고객번호
	 * 
	 * @param cdMemberDelivery
	 *            배달고객번호
	 */
	public void setCdMemberDelivery(String cdMemberDelivery) {
		this.cdMemberDelivery = cdMemberDelivery;
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
	 * Getter 주문금액
	 */
	public Double getAmtOrder() {
		return amtOrder;
	}

	/**
	 * Setter 주문금액
	 * 
	 * @param amtOrder
	 *            주문금액
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
	 * 
	 * @param amtSupply
	 *            공급가
	 */
	public void setAmtSupply(Double amtSupply) {
		this.amtSupply = amtSupply;
	}

	/**
	 * Getter 부가세
	 */
	public Double getAmtTax() {
		return amtTax;
	}

	/**
	 * Setter 부가세
	 * 
	 * @param amtTax
	 *            부가세
	 */
	public void setAmtTax(Double amtTax) {
		this.amtTax = amtTax;
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
	 * Getter 주문상태(1:주문 2:전체취소 3:합석)
	 */
	public Integer getDsStatus() {
		return dsStatus;
	}

	/**
	 * Setter 주문상태(1:주문 2:전체취소 3:합석)
	 * 
	 * @param dsStatus
	 *            주문상태(1:주문 2:전체취소 3:합석)
	 */
	public void setDsStatus(Integer dsStatus) {
		this.dsStatus = dsStatus;
	}

	/**
	 * Getter 정산 완료 여부(1:예 0:아니오)
	 */
	public Integer getYnSale() {
		return ynSale;
	}

	/**
	 * Setter 정산 완료 여부(1:예 0:아니오)
	 * 
	 * @param ynSale
	 *            정산 완료 여부(1:예 0:아니오)
	 */
	public void setYnSale(Integer ynSale) {
		this.ynSale = ynSale;
	}

	/**
	 * Getter 거래처 코드
	 */
	public String getCdVendor() {
		return cdVendor;
	}

	/**
	 * Setter 거래처 코드
	 * 
	 * @param cdVendor
	 *            거래처 코드
	 */
	public void setCdVendor(String cdVendor) {
		this.cdVendor = cdVendor;
	}

	/**
	 * Getter 주문번호출력
	 */
	public Long getNoOrderPrint() {
		return noOrderPrint;
	}

	/**
	 * Setter 주문번호출력
	 * 
	 * @param noOrderPrint
	 *            주문번호출력
	 */
	public void setNoOrderPrint(Long noOrderPrint) {
		this.noOrderPrint = noOrderPrint;
	}

	/**
	 * Getter 예약일자
	 */
	public String getYmdBooking() {
		return ymdBooking;
	}

	/**
	 * Setter 예약일자
	 * 
	 * @param ymdBooking
	 *            예약일자
	 */
	public void setYmdBooking(String ymdBooking) {
		this.ymdBooking = ymdBooking;
	}

	/**
	 * Getter 예약번호
	 */
	public Long getNoBooking() {
		return noBooking;
	}

	/**
	 * Setter 예약번호
	 * 
	 * @param noBooking
	 *            예약번호
	 */
	public void setNoBooking(Long noBooking) {
		this.noBooking = noBooking;
	}

	/**
	 * Getter 예약 시간(HHMM)
	 */
	public String getResTimeBooking() {
		return resTimeBooking;
	}

	/**
	 * Setter 예약 시간(HHMM)
	 * 
	 * @param resTimeBooking
	 *            예약 시간(HHMM)
	 */
	public void setResTimeBooking(String resTimeBooking) {
		this.resTimeBooking = resTimeBooking;
	}

	/**
	 * Getter 예약 인원
	 */
	public Integer getResCntCustomer() {
		return resCntCustomer;
	}

	/**
	 * Setter 예약 인원
	 * 
	 * @param resCntCustomer
	 *            예약 인원
	 */
	public void setResCntCustomer(Integer resCntCustomer) {
		this.resCntCustomer = resCntCustomer;
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

	/**
	 * Getter 상세 내역
	 */
	public List<PosSalesOrderInfoDetail> getDetail() {
		return detail;
	}

	/**
	 * Setter 상세 내역
	 * 
	 * @param detail
	 *            상세 내역
	 */
	public void setDetail(List<PosSalesOrderInfoDetail> detail) {
		this.detail = detail;
	}

	public Integer getCdOrderPath() {
		return cdOrderPath;
	}

	public void setCdOrderPath(Integer cdOrderPath) {
		this.cdOrderPath = cdOrderPath;
	}

	public Long getNoTable() {
		return noTable;
	}

	public void setNoTable(Long noTable) {
		this.noTable = noTable;
	}

	public Integer getCdOrderStatus() {
		return cdOrderStatus;
	}

	public void setCdOrderStatus(Integer cdOrderStatus) {
		this.cdOrderStatus = cdOrderStatus;
	}

	public String getDtOrder() {
		return dtOrder;
	}

	public void setDtOrder(String dtOrder) {
		this.dtOrder = dtOrder;
	}

	public Integer getCdOrderVerify() {
		return cdOrderVerify;
	}

	public void setCdOrderVerify(Integer cdOrderVerify) {
		this.cdOrderVerify = cdOrderVerify;
	}

	public List<PosOrderPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<PosOrderPayment> payments) {
		this.payments = payments;
	}

	public String getTimeWait() {
		return timeWait;
	}

	public void setTimeWait(String timeWait) {
		this.timeWait = timeWait;
	}

	public String getTelMobileMember() {
		return telMobileMember;
	}

	public void setTelMobileMember(String telMobileMember) {
		this.telMobileMember = telMobileMember;
	}

	public Integer getCdCancelReason() {
		return cdCancelReason;
	}

	public void setCdCancelReason(Integer cdCancelReason) {
		this.cdCancelReason = cdCancelReason;
	}

	public String getResDtInsert() {
		return resDtInsert;
	}

	public void setResDtInsert(String resDtInsert) {
		this.resDtInsert = resDtInsert;
	}

	public String getDtApproval() {
		return dtApproval;
	}

	public void setDtApproval(String dtApproval) {
		this.dtApproval = dtApproval;
	}

	@Override
	public String toString() {
		return "PosSalesOrderInfo [cdCompany=" + cdCompany + ", cdStore=" + cdStore + ", ymdOrder=" + ymdOrder + ", orderId=" + orderId + ", noOrder="
				+ noOrder + ", cdOrderType=" + cdOrderType + ", cdOrderPath=" + cdOrderPath + ", cdOrderStatus=" + cdOrderStatus + ", cdOrderVerify="
				+ cdOrderVerify + ", noTable=" + noTable + ", dsOrder=" + dsOrder + ", dtAdmission=" + dtAdmission + ", dtExit=" + dtExit
				+ ", cdMemberType=" + cdMemberType + ", cdMemberAge=" + cdMemberAge + ", cdMemberSex=" + cdMemberSex + ", cntCustomer=" + cntCustomer
				+ ", cdTour=" + cdTour + ", noGuide=" + noGuide + ", ynForeigner=" + ynForeigner + ", cdMember=" + cdMember + ", nmMember=" + nmMember
				+ ", telMobileMember=" + telMobileMember + ", cdMemberDelivery=" + cdMemberDelivery + ", cdEmployee=" + cdEmployee + ", amtOrder="
				+ amtOrder + ", amtSupply=" + amtSupply + ", amtTax=" + amtTax + ", amtDc=" + amtDc + ", amtTip=" + amtTip + ", memo=" + memo
				+ ", dsStatus=" + dsStatus + ", ynSale=" + ynSale + ", cdVendor=" + cdVendor + ", noOrderPrint=" + noOrderPrint + ", ymdBooking="
				+ ymdBooking + ", noBooking=" + noBooking + ", resTimeBooking=" + resTimeBooking + ", resDtInsert=" + resDtInsert
				+ ", resCntCustomer=" + resCntCustomer + ", cdCancelReason=" + cdCancelReason + ", dtOrder=" + dtOrder + ", dtApproval=" + dtApproval
				+ ", timeWait=" + timeWait + ", dtInsert=" + dtInsert + ", cdEmployeeInsert=" + cdEmployeeInsert + ", dtUpdate=" + dtUpdate
				+ ", cdEmployeeUpdate=" + cdEmployeeUpdate + ", detail=" + detail + ", payments=" + payments + "]";
	}

}
