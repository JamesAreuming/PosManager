package com.jc.pico.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStoreSales {
	private Long brandId;
	private Long storeId;
	private int totalItemCount;
	private Double totalSales;
	private int totalPageCount;
	private List<SvcStoreSalesView> list;
	public Long getBrandId() {
		return brandId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public int getTotalItemCount() {
		return totalItemCount;
	}
	public Double getTotalSales() {
		return totalSales;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public List<SvcStoreSalesView> getList() {
		return list;
	}
	public void setList(List<SvcStoreSalesView> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "SvcStoreSales [brandId=" + brandId + ", storeId=" + storeId + ", totalItemCount=" + totalItemCount
				+ ", totalSales=" + totalSales + ", totalPageCount=" + totalPageCount + ", saleList=" + list + "]";
	}

}
