package com.jc.pico.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.utils.bean.StaffUserDetail;

public final class ClerkUtil {

	private ClerkUtil() {

	}

	public static InvalidParamException newInvalidParamException(String message) {
		return new InvalidParamException(new String[] { message }, null);
	}

	public static String getAgentUserName(Authentication auth) {
		if (auth == null) {
			return "";
		}
		String name = auth.getName();
		return name.split(PosUtil.BASE_INFO_DELIMITER)[0];
	}
	
	
	public static Long getStaffStoreId(OAuth2Authentication auth) {
		if (auth == null) {
			return null;
		}
		
		if(auth.getUserAuthentication() == null) {
			return null;
		}
				
		if(auth.getUserAuthentication().getDetails() instanceof StaffUserDetail) {
			StaffUserDetail userDetail = (StaffUserDetail)auth.getUserAuthentication().getDetails();
			return userDetail.getStoreId();
			
		}else {
			return null;
		}
		
	}

	public static Long parseLong(String value, Long defaultValue) {
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (Throwable tw) {
			return defaultValue;
		}
	}

}
