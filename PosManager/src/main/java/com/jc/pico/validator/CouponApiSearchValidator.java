/*
 * Filename	: CouponApiSearchValidator.java
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
public class CouponApiSearchValidator implements Validator {
  protected static Logger logger = LoggerFactory.getLogger(CouponApiSearchValidator.class);
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

      if(coupon.get("couponCd") == null) {
        errors.reject("validation.necessary.field", new String[]{"couponCd"}, "");
      }

    }
  }

}
