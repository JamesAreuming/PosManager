/*
 * Filename	: AuthSuccessHandler.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  
  public AuthSuccessHandler(String defaultTargetUrl){
    this.setDefaultTargetUrl(defaultTargetUrl);
  }

}
