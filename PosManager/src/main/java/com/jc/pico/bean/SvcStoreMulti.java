package com.jc.pico.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStoreMulti {

	private Long id;
	private Long franId;
	private Long brandId;
	private Long storeId;
	private Byte ordinal;
	private Long childrenStoreId;
	private Long insertUser;	
	private String isUsed;
	private Date create;
	private Date update;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFranId() {
		return franId;
	}
	public void setFranId(Long franId) {
		this.franId = franId;
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
	public Byte getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Byte ordinal) {
		this.ordinal = ordinal;
	}
	public Long getChildrenStoreId() {
		return childrenStoreId;
	}
	public void setChildrenStoreId(Long childrenStoreId) {
		this.childrenStoreId = childrenStoreId;
	}
	public Long getInsertUser() {
		return insertUser;
	}
	public void setInsertUser(Long insertUser) {
		this.insertUser = insertUser;
	}
	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
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
