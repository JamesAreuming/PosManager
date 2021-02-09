package com.jc.pico.utils.bean;

public class MainStore {

	private long brandId;
	private long storeId;
	private boolean speciality;
	
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public boolean getSpeciality() {
		return speciality;
	}
	public void setSpeciality(boolean speciality) {
		this.speciality = speciality;
	}
	
	
	@Override
	public String toString() {
		return "MainStore [brandId=" + brandId + ", storeId=" + storeId + ", speciality=" + speciality + "]";
	}

}
