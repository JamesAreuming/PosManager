package com.jc.pico.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvnStoreTemp {
	private Long storeId;
	private Date created;
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "SvnStoreTemp [storeId=" + storeId + ", created=" + created + "]";
	}
	
}
