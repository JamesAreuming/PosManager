/*
 * Filename	: StampApiDeleteValidator.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.validator;

import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.pico.bean.User;

/**
 * Created by ruinnel on 2016. 4. 14..
 */
@Component
public class StampApiDeleteValidator implements Validator {
  protected static Logger logger = LoggerFactory.getLogger(StampApiDeleteValidator.class);
  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (target instanceof Map) {
      Map<Object,Object> stamp = (Map<Object,Object>) target;

      if(stamp.get("userId") != null){
        if(!GenericValidator.isDouble(String.valueOf(stamp.get("userId")))){
          errors.reject("validation.number.format", new String[]{"userId"}, "");
        }
      } else {
        errors.reject("validation.necessary.field", new String[]{"userId"}, "");
      }

      if(stamp.get("ids") != null){
        List<Object> ids = (List<Object>) stamp.get("ids");
        
        for(Object id : ids){
          if(!GenericValidator.isDouble(String.valueOf(id))){
            errors.reject("validation.number.format", new String[]{"ids['"+id+"']"}, "");
          }
        }
      } else {
        errors.reject("validation.necessary.field", new String[]{"ids"}, "");
      }

      if(stamp.get("receipt") != null){
        Map<Object,Object> receipt = (Map<Object, Object>) stamp.get("receipt");

        if(receipt.get("id") == null){
          errors.reject("validation.necessary.field", new String[]{"receipt.id"}, "");
        }
        
        if(receipt.get("no") == null){
          errors.reject("validation.necessary.field", new String[]{"receipt.no"}, "");
        }
        
        if(receipt.get("date") != null){
          if(!GenericValidator.isDate(String.valueOf(receipt.get("date")), "yyyy-MM-dd HH:mm:ss", true)){
            errors.reject("validation.date.format", new String[]{"receipt.date","yyyy-MM-dd HH:mm:ss"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.date"}, "");
        }
        
        if(receipt.get("sales") != null){
          if(!GenericValidator.isDouble(String.valueOf(receipt.get("sales")))){
            errors.reject("validation.number.format", new String[]{"receipt.sales"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.sales"}, "");
        }
        
      } else {
        errors.reject("validation.necessary.field", new String[]{"receipt"}, "");
      }

    }
  }


  public static final void main(String[] args) throws Exception {
    System.out.println(GenericValidator.isDate(String.valueOf("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", true));
  }
}
