package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 3. M_POS_INFO : 포스 정보
 * 2016. 7. 21, green, create
 */
public class PosMasterPosInfo implements Serializable {
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
	 * 포스번호
	 */
	@JsonProperty(value="NO_POS")
	private String noPos;
	/**
	 * 포스명
	 */
	@JsonProperty(value="NM_POS")
	private String nmPos;
	/**
	 * 포스TYPE코드 (공통코드 76)
	 */
	@JsonProperty(value="DS_POS")
	private Integer dsPos;
	/**
	 * 포스 IP 주소
	 */
	@JsonProperty(value="IP")
	private String ip;
	/**
	 * 메모
	 */
	@JsonProperty(value="MEMO")
	private String memo;
	/**
	 * 사용유무 (1:사용, 0:미사용)
	 */
	@JsonProperty(value="YN_USE")
	private Integer ynUse;
	/**
	 * VAN일련번호
	 */
	@JsonProperty(value="CD_VAN")
	private Long cdVan;
	/**
	 * 현금영수증VAN
	 */
	@JsonProperty(value="CD_VAN_CASH_RCP")
	private Integer cdVanCashRcp;
	/**
	 * 주방프린터 인쇄유무 (1:사용, 0:미사용)
	 */
	@JsonProperty(value="YN_KITCHEN_PRINT")
	private Integer ynKitchenPrint;
	/**
	 * 주방프린터 정보
	 */
	@JsonProperty(value="KITCHEN_PRINTER_INFO")
	private String kitchenPrinterInfo;
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
	 * Getter 포스번호
	 */
	public String getNoPos() {
		return noPos;
	}
	/**
	 * Setter 포스번호
	 * @param noPos 포스번호
	 */
	public void setNoPos(String noPos) {
		this.noPos = noPos;
	}
	/**
	 * Getter 포스명
	 */
	public String getNmPos() {
		return nmPos;
	}
	/**
	 * Setter 포스명
	 * @param nmPos 포스명
	 */
	public void setNmPos(String nmPos) {
		this.nmPos = nmPos;
	}
	/**
	 * Getter 포스TYPE코드 (공통코드 76)
	 */
	public Integer getDsPos() {
		return dsPos;
	}
	/**
	 * Setter 포스TYPE코드 (공통코드 76)
	 * @param dsPos 포스TYPE코드 (공통코드 76)
	 */
	public void setDsPos(Integer dsPos) {
		this.dsPos = dsPos;
	}
	/**
	 * Getter 포스 IP 주소
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * Setter 포스 IP 주소
	 * @param ip 포스 IP 주소
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * Getter 메모
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * Setter 메모
	 * @param memo 메모
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * Getter 사용유무 (1:사용, 0:미사용)
	 */
	public Integer getYnUse() {
		return ynUse;
	}
	/**
	 * Setter 사용유무 (1:사용, 0:미사용)
	 * @param ynUse 사용유무 (1:사용, 0:미사용)
	 */
	public void setYnUse(Integer ynUse) {
		this.ynUse = ynUse;
	}
	/**
	 * Getter VAN일련번호
	 */
	public Long getCdVan() {
		return cdVan;
	}
	/**
	 * Setter VAN일련번호
	 * @param cdVan VAN일련번호
	 */
	public void setCdVan(Long cdVan) {
		this.cdVan = cdVan;
	}
	/**
	 * Getter 현금영수증VAN
	 */
	public Integer getCdVanCashRcp() {
		return cdVanCashRcp;
	}
	/**
	 * Setter 현금영수증VAN
	 * @param cdVanCashRcp 현금영수증VAN
	 */
	public void setCdVanCashRcp(Integer cdVanCashRcp) {
		this.cdVanCashRcp = cdVanCashRcp;
	}
	/**
	 * Getter 주방프린터 인쇄유무 (1:사용, 0:미사용)
	 */
	public Integer getYnKitchenPrint() {
		return ynKitchenPrint;
	}
	/**
	 * Setter 주방프린터 인쇄유무 (1:사용, 0:미사용)
	 * @param ynKitchenPrint 주방프린터 인쇄유무 (1:사용, 0:미사용)
	 */
	public void setYnKitchenPrint(Integer ynKitchenPrint) {
		this.ynKitchenPrint = ynKitchenPrint;
	}
	/**
	 * Getter 주방프린터 정보
	 */
	public String getKitchenPrinterInfo() {
		return kitchenPrinterInfo;
	}
	/**
	 * Setter 주방프린터 정보
	 * @param kitchenPrinterInfo 주방프린터 정보
	 */
	public void setKitchenPrinterInfo(String kitchenPrinterInfo) {
		this.kitchenPrinterInfo = kitchenPrinterInfo;
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
