/*
 * Filename	: CouponApiDeleteValidator.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.validator;

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
public class CouponApiDeleteValidator implements Validator {
  protected static Logger logger = LoggerFactory.getLogger(CouponApiDeleteValidator.class);
  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (target instanceof Map) {
      Map<Object,Object> coupon = (Map<Object,Object>) target;


      if(coupon.get("userId") != null){
        if(!GenericValidator.isDouble(String.valueOf(coupon.get("userId")))){
          errors.reject("validation.number.format", new String[]{"userId"}, "");
        }
      } else {
        errors.reject("validation.necessary.field", new String[]{"userId"}, "");
      }

      if(coupon.get("ids") != null){
        errors.reject("validation.necessary.field", new String[]{"ids"}, "");
      }


      if(coupon.get("receipt") != null){

        Map<Object,Object> receipt = (Map<Object, Object>) coupon.get("receipt");

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

}
