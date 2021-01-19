package com.jc.pico.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcDelivery {
	private Long id;
	private Long brandId;
	private Long storeId;
	private String orderNo;
	private String cusName;
	private String cusCellNo;
	private String cusTelNo;
	private String cusZip;
	private String cusAddr1;
	private String cusAddr2;
	private String cusMessage;
	private String cusExpress;
	private String cusExpressNo;
	private Date create;
	private Date update;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getCusCellNo() {
		return cusCellNo;
	}
	public void setCusCellNo(String cusCellNo) {
		this.cusCellNo = cusCellNo;
	}
	public String getCusTelNo() {
		return cusTelNo;
	}
	public void setCusTelNo(String cusTelNo) {
		this.cusTelNo = cusTelNo;
	}
	public String getCusZip() {
		return cusZip;
	}
	public void setCusZip(String cusZip) {
		this.cusZip = cusZip;
	}
	public String getCusAddr1() {
		return cusAddr1;
	}
	public void setCusAddr1(String cusAddr1) {
		this.cusAddr1 = cusAddr1;
	}
	public String getCusAddr2() {
		return cusAddr2;
	}
	public void setCusAddr2(String cusAddr2) {
		this.cusAddr2 = cusAddr2;
	}
	public String getCusMessage() {
		return cusMessage;
	}
	public void setCusMessage(String cusMessage) {
		this.cusMessage = cusMessage;
	}
	public String getCusExpress() {
		return cusExpress;
	}
	public void setCusExpress(String cusExpress) {
		this.cusExpress = cusExpress;
	}
	public String getCusExpressNo() {
		return cusExpressNo;
	}
	public void setCusExpressNo(String cusExpressNo) {
		this.cusExpressNo = cusExpressNo;
	}
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
	}
	public Date getUpdate() {
		return update;
	}
	public void setUpdate(Date update) {
		this.update = update;
	}
	
	
	
}
