/*
 * Filename	: Error.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

public class Error {
  public enum ErrorCodes {
    /******************************************************/
    /*** ESO ***/
    /******************************************************/
    SUCCESS("ESO0000"),
    NO_PERMISSION("ESO0001"),
    DATA_DUPLICATE("ESO0002"),
    DATA_NOT_REGISTERED("ESO0003"),
    DATA_NOT_NUMBER_FORMAT("ESO0004"),
    DATA_NOT_FOUND("ESO0006"),
    INVALID_EMAIL("ESO0007"),
    INVALID_JSON("ESO0008"),
    INVALID_VALUE("ESO0009"),
    CODE_NOT_FOUND("ESO0010"),
    INVALID_PARAM("ESO0011"),
    INVALID_TOKEN("ESO0012"),
    METHOD_NOT_SUPPORT("ESO0013"),
    MEDIA_TYPE_NOT_SUPPORT("ESO0014"),
    UNAUTHORIZED("ESO0015"),
    SETTING_NOT_FOUND("ESO0016"),
    LOGIN_FAIL("ESO0017"),
    LOGIN_RESTRCTION("ESO0018"),
    LOGIN_RESTRCTION_FULL("ESO0019"),
    TIME_OUT("ESO0020"),
    
    UNKOWN("ESO9999");

    String code;
    
    ErrorCodes(String code) {
      this.code = code;
    }
    
    public String getCode() {
      return code;
    }
  }

  String code;
  String field;
  String message;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}