package com.jc.pico.utils.bean;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Staff 사용자 정보
 * 
 * @author hyo
 *
 */
public class StaffUserDetail extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private long brandId;
	private long storeId;
	private long staffId;
	private long licenseId;

	public StaffUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

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

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(long licenseId) {
		this.licenseId = licenseId;
	}
	

	@Override
	public String toString() {
		return "StaffUserDetail [brandId=" + brandId + ", storeId=" + storeId + ", staffId=" + staffId + ", licenseId=" + licenseId + "]";
	}

}
