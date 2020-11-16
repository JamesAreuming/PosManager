/*
 * Filename	: InvalidTokenException.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.exception;

/**
 * Created by ruinnel on 2016. 4. 12..
 */
public class InvalidTokenException extends Exception {
  private String token;

  public InvalidTokenException(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
