package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcKitchenPrinterItem {
	private int ordinal;
	private String itemNm;
	private int count;
	private boolean isOption;
	public int getOrdinal() {
		return ordinal;
	}
	public String getItemNm() {
		return itemNm;
	}
	public int getCount() {
		return count;
	}
	public boolean getIsOption() {
		return isOption;
	}
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setIsOption(boolean isOption) {
		this.isOption = isOption;
	}
	@Override
	public String toString() {
		return "SvcKitchenPrinterItem [ordinal=" + ordinal + ", itemNm=" + itemNm + ", count=" + count + ", isOption="
				+ isOption + "]";
	}
	
}
