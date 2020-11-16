/*
 * Filename	: DataNotRegisteredException.java
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
public class DataNotRegisteredException extends Exception {
  private String type;
  private String value;
  private Locale locale;

  public DataNotRegisteredException(String type, String value, Locale locale) {
    this.type = type;
    this.value = value;
    this.locale = locale;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  
}
