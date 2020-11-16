/*
 * Filename	: InvalidJsonException.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.exception;

import java.util.Locale;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by ruinnel on 2016. 4. 12..
 */
public class InvalidJsonException extends Exception {
  private String json;
  private TypeReference typeRef;
  private Class typeClass;

  public InvalidJsonException(String json, TypeReference typeRef) {
    this.json = json;
    this.typeRef = typeRef;
    this.typeClass = null;
  }
  public InvalidJsonException(String json, Class typeClass) {
    this.json = json;
    this.typeClass = typeClass;
    this.typeRef = null;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public TypeReference getTypeRef() {
    return typeRef;
  }

  public void setTypeRef(TypeReference typeRef) {
    this.typeRef = typeRef;
  }

  public Class getTypeClass() {
    return typeClass;
  }

  public void setTypeClass(Class typeClass) {
    this.typeClass = typeClass;
  }
}
