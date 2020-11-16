package com.jc.pico.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.jc.pico.utils.bean.StaffUserDetail;

public class AuthenticationUtils {
	/**
	 * token 에서 details 정보 돌려준다
	 * @param <T>
	 * @param type
	 * @return
	 */
	public static <T> T getDetails(Class<T> type) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        
        if(authentication instanceof OAuth2Authentication){
        	final Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
  
       	 	if (userAuthentication instanceof UsernamePasswordAuthenticationToken) {
       	 		try {
       	 			return type.cast(userAuthentication.getDetails());
       	 		}catch(Exception e) {
       	 			
       	 		}
            }
        }
        
        return null;
        
	}
	
	public static void main(String[] args) {
		/**
		 * 호출 샘플
		 */
		final StaffUserDetail userDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		
		System.out.println("userDetail : " + userDetail);
	}
}
