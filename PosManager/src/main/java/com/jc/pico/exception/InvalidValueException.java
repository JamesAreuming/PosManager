/*
 * Filename	: InvalidParamException.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.exception;

import java.util.Locale;

import org.springframework.validation.Errors;

/**
 * Created by kdy on 2016. 7. 7..
 */
public class InvalidValueException extends Exception {
  private Errors errors;
  private String type;
  private String[]  params;
  private Locale locale;

  public InvalidValueException(String type, Locale locale) {
    this.type = type;
    this.locale = locale;
  }
  
  public InvalidValueException(Errors errors, Locale locale) {
    this.errors = errors;
    this.locale = locale;
  }
  
  public InvalidValueException(String[] params, Locale locale) {
    this.params = params;
    this.locale = locale;
  }

  public Errors getErrors() {
    return errors;
  }

  public void setErrors(Errors errors) {
    this.errors = errors;
  }

  public String[] getParams() {
    return params;
  }

  public void setParams(String[] params) {
    this.params = params;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }
}
