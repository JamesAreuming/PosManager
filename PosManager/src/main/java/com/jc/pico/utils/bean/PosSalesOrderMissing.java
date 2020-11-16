package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 12. S_ORDER_MISSING : 미 수신 주문 정보 조회
 * 2016. 8. 10, green, create
 */
public class PosSalesOrderMissing implements Serializable {
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
}
