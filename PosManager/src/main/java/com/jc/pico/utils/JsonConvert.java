/*
 * Filename	: JsonConvert.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.InvalidJsonException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;

public class JsonConvert {

  protected static Logger logger = LoggerFactory.getLogger(JsonConvert.class);

  public static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat(Globals.JSON_DATETIME_FORMAT));
    return mapper;
  }

  @SuppressWarnings({ "unchecked" })
  public static <T> T JsonConvertObject(HttpServletRequest request, TypeReference<T> typeRef) throws InvalidJsonException {
    ObjectMapper mapper = getObjectMapper();
    Object _returnObject = null;

    String jsonString = "";
    try {
      jsonString = IOUtils.toString(getHttpServletRequestBody(request));
      logger.error(jsonString);
      _returnObject = mapper.readValue(jsonString, typeRef);
      
    } catch (Exception e) {
      logger.error(" Json to Object Error : {}", e.getMessage());
      throw new InvalidJsonException(jsonString, typeRef);
    }
    return (T) _returnObject;
  }

  @SuppressWarnings({ "unchecked" })
  public static <T> T JsonConvertObject(HttpServletRequest request, Class<T> classType) throws InvalidJsonException {
    ObjectMapper mapper = getObjectMapper();
    Object _returnObject = null;

    String jsonString = "";
    try {
      jsonString = IOUtils.toString(getHttpServletRequestBody(request));
      logger.error(jsonString);
      _returnObject = mapper.readValue(jsonString, classType);
    } catch (Exception e) {
      logger.error(" Json to Object Error : {}", e.getMessage());
      throw new InvalidJsonException(jsonString, classType);
    }
    return (T) _returnObject;
  }
  
  @SuppressWarnings({ "unchecked" })
  public static <T> T JsonConvertObject(String jsonString, TypeReference<T> typeRef) throws InvalidJsonException {
    ObjectMapper mapper = getObjectMapper();
    Object _returnObject = null;

    try {
      _returnObject = mapper.readValue(jsonString, typeRef);
      
    } catch (Exception e) {
      logger.error(" StringJson to Object Error : {}", e.getMessage());
      throw new InvalidJsonException(jsonString, typeRef);
    }
    return (T) _returnObject;
  }

  @SuppressWarnings({ "unchecked" })
  public static <T> T JsonConvertObject(String jsonString, Class<T> classType) throws InvalidJsonException {
    ObjectMapper mapper = getObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    Object _returnObject = null;

    try {
      _returnObject = mapper.readValue(jsonString, classType);
    } catch (Exception e) {
      logger.error(" StringJson to Object Error : {}", e.getMessage());
      throw new InvalidJsonException(jsonString, classType);
    }
    return (T) _returnObject;
  }

  public static BufferedReader getHttpServletRequestBody(HttpServletRequest request){
    BufferedReader buffer = null;
    try {
      buffer = request.getReader();
    } catch (Exception e) {
      buffer = null;
      logger.error(" Servlet get PostBody Faild : {}", e.getMessage());
    }
    
    return buffer;
  }

  public static String toJson(Object obj) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.setDateFormat(new SimpleDateFormat(Globals.JSON_DATETIME_FORMAT));
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      logger.warn("toJson fail! " + obj.toString(), e);
      return "";
    }
  }
}
