package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 7. S_TABLE_INFO : 테이블 정보
 * 2016. 7. 21, green, create
 */
public class PosSalesTableInfo implements Serializable {
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
