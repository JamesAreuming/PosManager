package com.jc.pico.bean;

import java.util.List;

public class SvcStoreSalesInfo {
	private Long id;
	private Long orderId;			//주문 ID
	private String orderNo;			// 주문 코드
	private Long storeId; // 추가
	private int totalCount;				// 주문 수량
	private String orderSt;				// 주문 상태
	private String orderStNm;
	private String staffNm;			// 담당자
	private String posNo;				// 주문기기
	private Double amount;	// 결제 금액
	private String salesSt;				// 결제 상태
	private String salesStNm;

	private List<SvcStoreSalesDetail> pays;
	private List<SvcStoreSalesDetailItem> items;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public Long getstoreId() { // 추가
		return storeId;
	}
	public void setstoreId(Long storeId) {
		this.storeId = storeId;
	}	
	public int getTotalCount() {
		return totalCount;
	}
	public String getOrderSt() {
		return orderSt;
	}
	public String getStaffNm() {
		return staffNm;
	}
	public String getPosNo() {
		return posNo;
	}
	public Double getAmount() {
		return amount;
	}
	public List<SvcStoreSalesDetail> getPays() {
		return pays;
	}
	public List<SvcStoreSalesDetailItem> getItems() {
		return items;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public void setOrderSt(String orderSt) {
		this.orderSt = orderSt;
	}
	public void setStaffNm(String staffNm) {
		this.staffNm = staffNm;
	}
	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setPays(List<SvcStoreSalesDetail> pays) {
		this.pays = pays;
	}
	public void setItems(List<SvcStoreSalesDetailItem> items) {
		this.items = items;
	}
	public String getOrderStNm() {
		return orderStNm;
	}
	public void setOrderStNm(String orderStNm) {
		this.orderStNm = orderStNm;
	}
	public String getSalesSt() {
		return salesSt;
	}
	public String getSalesStNm() {
		return salesStNm;
	}
	public void setSalesSt(String salesSt) {
		this.salesSt = salesSt;
	}
	public void setSalesStNm(String salesStNm) {
		this.salesStNm = salesStNm;
	}
	@Override
	public String toString() {
		return "SvcStoreSalesInfo [id=" + id + ", orderId=" + orderId + ", orderNo=" + orderNo + ", storeId=" + storeId
				+ ", totalCount=" + totalCount + ", orderSt=" + orderSt + ", orderStNm=" + orderStNm + ", staffNm="
				+ staffNm + ", posNo=" + posNo + ", amount=" + amount + ", salesSt=" + salesSt + ", salesStNm="
				+ salesStNm + ", pays=" + pays + ", items=" + items + "]";
	}
		
	
//	@Override
//	public String toString() {
//		return "SvcStoreSalesInfo [id=" + id + ", orderId=" + orderId + ", orderNo=" + orderNo + ", totalCount="
//				+ totalCount + ", orderSt=" + orderSt + ", orderStNm=" + orderStNm + ", staffNm=" + staffNm + ", posNo="
//				+ posNo + ", amount=" + amount + ", salesSt=" + salesSt + ", salesStNm=" + salesStNm + ", pays=" + pays
//				+ ", items=" + items + "]";
//	}
}
