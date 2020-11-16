/*
 * Filename	: OAuth2LoginException.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class OAuth2LoginException extends OAuth2Exception {
  public OAuth2LoginException(String msg) {
    super(msg);
  }
}
