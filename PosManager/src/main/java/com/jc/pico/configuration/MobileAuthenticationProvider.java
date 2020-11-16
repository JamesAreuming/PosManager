/*
 * Filename	: MobileAuthenticationProvider.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   :
 */

package com.jc.pico.configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("authenticationProvider")
public class MobileAuthenticationProvider implements AuthenticationProvider {
  
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    List<GrantedAuthority> grantedAuths = new ArrayList<>();

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    String userid = authentication.getName();
    String userpass = authentication.getCredentials().toString();

    if(request.getParameter("grant_type") == null){

      Map<Object, Object> user = null;

      if (user == null) {
        throw new BadCredentialsException("Username not found.");
      }

      if (!userpass.equals(user.get("password").toString())) {
        throw new BadCredentialsException("Wrong password.");
      }

      grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

    }
    
    return new UsernamePasswordAuthenticationToken(userid, userpass, grantedAuths);

  }

  @Override
  public boolean supports(Class<?> authentication) {
    // TODO Auto-generated method stub
    return true;
  }

}
