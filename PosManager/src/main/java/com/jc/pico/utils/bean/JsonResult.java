/*
 * Filename	: JsonResult.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.jc.pico.utils.bean.Error.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

public class JsonResult {

  private boolean success;
  private Object bean;
  private List<com.jc.pico.utils.bean.Error> errors;

  public JsonResult() {}

  public JsonResult(Object bean) {
    this.success = true;
    this.bean = bean;
    this.errors = null;
  }

  public JsonResult(List<com.jc.pico.utils.bean.Error> errors) {
    this.success = false;
    this.errors = errors;
    this.bean = null;
  }

  public JsonResult(boolean success, Object bean, List<com.jc.pico.utils.bean.Error> errors) {
    this.success = success;
    this.bean = bean;
    this.errors = errors;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Object getBean() {
    return bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }

  public List<com.jc.pico.utils.bean.Error> getErrors() {
    return errors;
  }

  public void setErrors(List<com.jc.pico.utils.bean.Error> errors) {
    this.errors = errors;
  }


  public void addError(final String code) {
    com.jc.pico.utils.bean.Error error = new com.jc.pico.utils.bean.Error();
    error.code = code;
    this.errors.add(error);
  }

  public void addError(final String code, final String msg) {
    com.jc.pico.utils.bean.Error error = new com.jc.pico.utils.bean.Error();
    error.code = code;
    error.message = msg;
    this.addError(error);
  }

  public void addError(final ErrorCodes code, final String msg) {
    com.jc.pico.utils.bean.Error error = new com.jc.pico.utils.bean.Error();
    error.code = code.getCode();
    error.message = msg;
    this.addError(error);
  }

  public void addError(final String code, final String msg, final String field) {
    com.jc.pico.utils.bean.Error error = new com.jc.pico.utils.bean.Error();
    error.code = code;
    error.message = msg;
    error.field = field;
    this.addError(error);
  }

  public void addError(final com.jc.pico.utils.bean.Error error) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(error);
  }
}