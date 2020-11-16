/*
 * Filename	: CodeNotFoundException.java
 * Function	:
 * Comment 	:
 * History	:  
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.exception;

import java.util.Locale;

/**
 * Created by ruinnel on 2016. 4. 12..
 */
public class CodeNotFoundException extends Exception {
  private String type;
  private String key;
  private Locale locale;

  public CodeNotFoundException(String type, String key, Locale locale) {
    this.type = type;
    this.key = key;
    this.locale = locale;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }
  
}
