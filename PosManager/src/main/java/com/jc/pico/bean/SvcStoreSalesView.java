package com.jc.pico.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStoreSalesView {
	private Long orderId;
	private String receiptNo;
	private Date orderTmLocal;
	private int itemCount;
	private Double sales;
	private String cardNo;
	private Date openDt;
	public String getReceiptNo() {
		return receiptNo;
	}
	public Date getOrderTmLocal() {
		return orderTmLocal;
	}
	public int getItemCount() {
		return itemCount;
	}
	public Double getSales() {
		return sales;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public void setOrderTmLocal(Date orderTmLocal) {
		this.orderTmLocal = orderTmLocal;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public void setSales(Double sales) {
		this.sales = sales;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Date getOpenDt() {
		return openDt;
	}
	public void setOpenDt(Date openDt) {
		this.openDt = openDt;
	}
	@Override
	public String toString() {
		return "SvcStoreSalesView [orderId=" + orderId + ", receiptNo=" + receiptNo + ", orderTmLocal="
				+ orderTmLocal + ", itemCount=" + itemCount + ", sales=" + sales + "]";
	}
}
