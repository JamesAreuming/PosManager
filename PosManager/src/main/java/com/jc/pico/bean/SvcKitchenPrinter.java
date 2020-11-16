package com.jc.pico.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcKitchenPrinter {
	private Long brandId;
	private Long storeId;
	private String printerNo;
	private String orderNo;
	private int ordinal;
	private String receiptNo;
	private String itemNm;
	private int count;
	private boolean isPrint;
	private boolean isOption;
	private Date orderTmLocal;
	private String printPort;
	private String printIp;
	private Date created;
	private Date updated;
	private List<SvcKitchenPrinterItem> items;
	public Long getBrandId() {
		return brandId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public String getPrinterNo() {
		return printerNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public int getOrdinal() {
		return ordinal;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public String getItemNm() {
		return itemNm;
	}
	public int getCount() {
		return count;
	}
	public boolean getIsPrint() {
		return isPrint;
	}
	public boolean getIsOption() {
		return isOption;
	}
	public Date getOrderTmLocal() {
		return orderTmLocal;
	}
	public Date getCreated() {
		return created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public void setPrinterNo(String printerNo) {
		this.printerNo = printerNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setIsPrint(boolean isPrint) {
		this.isPrint = isPrint;
	}
	public void setIsOption(boolean isOption) {
		this.isOption = isOption;
	}
	public void setOrderTmLocal(Date orderTmLocal) {
		this.orderTmLocal = orderTmLocal;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	public List<SvcKitchenPrinterItem> getItems() {
		return items;
	}
	public void setItems(List<SvcKitchenPrinterItem> items) {
		this.items = items;
	}
	public String getPrintPort() {
		return printPort;
	}
	public String getPrintIp() {
		return printIp;
	}
	public void setPrintPort(String printPort) {
		this.printPort = printPort;
	}
	public void setPrintIp(String printIp) {
		this.printIp = printIp;
	}
	@Override
	public String toString() {
		return "SvcKitchenPrinter [brandId=" + brandId + ", storeId=" + storeId + ", printerNo=" + printerNo + ", orderNo="
				+ orderNo + ", ordinal=" + ordinal + ", receiptNo=" + receiptNo + ", itemNm=" + itemNm + ", count="
				+ count + ", isPrint=" + isPrint + ", isOption=" + isOption + ", orderTmLocal=" + orderTmLocal
				+ ", printPort=" + printPort + ", printIp=" + printIp + ", created=" + created + ", updated=" + updated
				+ ", items=" + items + "]";
	}
}
