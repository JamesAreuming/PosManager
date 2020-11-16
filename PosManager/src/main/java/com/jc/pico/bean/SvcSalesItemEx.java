package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcSalesItemEx {
	private Long    id;
	private String  itemNm;
	private Double  price;
	private Double  purchasePrice;
	private Short   count;
	private Long    salesId;
	private Double  sales;
	private Double  discount;
	private Double  netSales;
	private Double  tax;
	private Double  optPrice;
	private Boolean isPacking;
	private Date    orderTmLocal;
	private String  payMethod;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItemNm() {
		return itemNm;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Short getCount() {
		return count;
	}
	public void setCount(Short count) {
		this.count = count;
	}
	public Long getSalesId() {
		return salesId;
	}
	public void setSalesId(Long salesId) {
		this.salesId = salesId;
	}
	public Double getSales() {
		return sales;
	}
	public void setSales(Double sales) {
		this.sales = sales;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getNetSales() {
		return netSales;
	}
	public void setNetSales(Double netSales) {
		this.netSales = netSales;
	}
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public Double getOptPrice() {
		return optPrice;
	}
	public void setOptPrice(Double optPrice) {
		this.optPrice = optPrice;
	}
	public Boolean getIsPacking() {
		return isPacking;
	}
	public void setIsPacking(Boolean isPacking) {
		this.isPacking = isPacking;
	}
	public Date getOrderTmLocal() {
		return orderTmLocal;
	}
	public void setOrderTmLocal(Date orderTmLocal) {
		this.orderTmLocal = orderTmLocal;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	
	
}
