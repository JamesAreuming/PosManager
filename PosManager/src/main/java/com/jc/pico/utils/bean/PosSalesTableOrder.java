package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 9. S_TABLE_ORDER : 테이블 주문정보
 * 2016. 8. 13, green, create
 */
public class PosSalesTableOrder implements Serializable {
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
	 * 테이블번호
	 */
	@JsonProperty(value="NO_TABLE")
	private Long noTable;
	/**
	 * 파티션번호
	 */
	@JsonProperty(value="NO_PARTITION")
	private Long noPartition;
	/**
	 * 주문일자(yyyymmdd)
	 */
	@JsonProperty(value="YMD_ORDER")
	private String ymdOrder;
	/**
	 * 주문번호
	 */
	@JsonProperty(value="NO_ORDER")
	private Long noOrder;
	
	/**
	 * 주문ID
	 */
	@JsonProperty(value="ORDER_ID")
	private Long orderId;
	
	/**
	 * 단체번호
	 */
	@JsonProperty(value="NO_PARTY")
	private Long noParty;
	/**
	 * 단체색깔
	 */
	@JsonProperty(value="BG_PARTY")
	private String bgParty;
	/**
	 * 단체테이블명
	 */
	@JsonProperty(value="NM_TABLE")
	private String nmTable;
	/**
	 * 최종사용 포스번호
	 */
	@JsonProperty(value="NO_POS_LAST")
	private String noPosLast;
	/**
	 * 상태(0:미사용 1:사용중)
	 */
	@JsonProperty(value="DS_STATUS")
	private Integer dsStatus;
	/**
	 * 입장시간
	 */
	@JsonProperty(value="DT_ADMISSION")
	private String dtAdmission;
	/**
	 * 객수
	 */
	@JsonProperty(value="CNT_CUSTOMER")
	private Integer cntCustomer;
	/**
	 * 주문금액합계
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
	@JsonProperty(value="AMT_TAX")
	private Double amtTax;
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
	 * 상세 내역
	 */
	@JsonProperty(value="DETAIL")
	private List<PosSalesTableOrderDetail> detail;
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
	 * Getter 테이블번호
	 */
	public Long getNoTable() {
		return noTable;
	}
	/**
	 * Setter 테이블번호
	 * @param noTable 테이블번호
	 */
	public void setNoTable(Long noTable) {
		this.noTable = noTable;
	}
	/**
	 * Getter 파티션번호
	 */
	public Long getNoPartition() {
		return noPartition;
	}
	/**
	 * Setter 파티션번호
	 * @param noPartition 파티션번호
	 */
	public void setNoPartition(Long noPartition) {
		this.noPartition = noPartition;
	}
	/**
	 * Getter 주문일자(yyyymmdd)
	 */
	public String getYmdOrder() {
		return ymdOrder;
	}
	/**
	 * Setter 주문일자(yyyymmdd)
	 * @param ymdOrder 주문일자(yyyymmdd)
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
	 * @param noOrder 주문번호
	 */
	public void setNoOrder(Long noOrder) {
		this.noOrder = noOrder;
	}
	/**
	 * Getter 단체번호
	 */
	public Long getNoParty() {
		return noParty;
	}
	/**
	 * Setter 단체번호
	 * @param noParty 단체번호
	 */
	public void setNoParty(Long noParty) {
		this.noParty = noParty;
	}
	/**
	 * Getter 단체색깔
	 */
	public String getBgParty() {
		return bgParty;
	}
	/**
	 * Setter 단체색깔
	 * @param bgParty 단체색깔
	 */
	public void setBgParty(String bgParty) {
		this.bgParty = bgParty;
	}
	/**
	 * Getter 단체테이블명
	 */
	public String getNmTable() {
		return nmTable;
	}
	/**
	 * Setter 단체테이블명
	 * @param nmTable 단체테이블명
	 */
	public void setNmTable(String nmTable) {
		this.nmTable = nmTable;
	}
	/**
	 * Getter 최종사용 포스번호
	 */
	public String getNoPosLast() {
		return noPosLast;
	}
	/**
	 * Setter 최종사용 포스번호
	 * @param noPosLast 최종사용 포스번호
	 */
	public void setNoPosLast(String noPosLast) {
		this.noPosLast = noPosLast;
	}
	/**
	 * Getter 상태(0:미사용 1:사용중)
	 */
	public Integer getDsStatus() {
		return dsStatus;
	}
	/**
	 * Setter 상태(0:미사용 1:사용중)
	 * @param dsStatus 상태(0:미사용 1:사용중)
	 */
	public void setDsStatus(Integer dsStatus) {
		this.dsStatus = dsStatus;
	}
	/**
	 * Getter 입장시간
	 */
	public String getDtAdmission() {
		return dtAdmission;
	}
	/**
	 * Setter 입장시간
	 * @param dtAdmission 입장시간
	 */
	public void setDtAdmission(String dtAdmission) {
		this.dtAdmission = dtAdmission;
	}
	/**
	 * Getter 객수
	 */
	public Integer getCntCustomer() {
		return cntCustomer;
	}
	/**
	 * Setter 객수
	 * @param cntCustomer 객수
	 */
	public void setCntCustomer(Integer cntCustomer) {
		this.cntCustomer = cntCustomer;
	}
	/**
	 * Getter 주문금액합계
	 */
	public Double getAmtOrder() {
		return amtOrder;
	}
	/**
	 * Setter 주문금액합계
	 * @param amtOrder 주문금액합계
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
	public Double getAmtTax() {
		return amtTax;
	}
	/**
	 * Setter 부가세
	 * @param amtTax 부가세
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
	/**
	 * Getter 상세 내역
	 */
	public List<PosSalesTableOrderDetail> getDetail() {
		return detail;
	}
	/**
	 * Setter 상세 내역
	 * @param detail 상세 내역
	 */
	public void setDetail(List<PosSalesTableOrderDetail> detail) {
		this.detail = detail;
	}
}
