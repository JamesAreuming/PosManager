/*
 * Filename	: StampApiCreateValidator.java
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
public class StampApiCreateValidator implements Validator {
  protected static Logger logger = LoggerFactory.getLogger(StampApiCreateValidator.class);
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

      if(stamp.get("count") != null){
        if(!GenericValidator.isInt(String.valueOf(stamp.get("count")))){
          errors.reject("validation.number.format", new String[]{"count"}, "");
        }
      } else {
        errors.reject("validation.necessary.field", new String[]{"count"}, "");
      }

      if(stamp.get("receipt") != null){
        Map<Object,Object> receipt = (Map<Object, Object>) stamp.get("receipt");

        if(receipt.get("id") == null){
          errors.reject("validation.necessary.field", new String[]{"receipt.id"}, "");
        }
        
        if(receipt.get("date") != null){
          if(!GenericValidator.isDate(String.valueOf(receipt.get("date")), "yyyy-MM-dd HH:mm:ss", true)){
            errors.reject("validation.date.format", new String[]{"receipt.date","yyyy-MM-dd HH:mm:ss"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.date"}, "");
        }
        
        if(receipt.get("useCoupon") != null ){
          if(!"true".equals(String.valueOf(receipt.get("useCoupon"))) && !"false".equals(String.valueOf(receipt.get("useCoupon")))){
            errors.reject("validation.invalid.boolean", new String[]{"receipt.useCoupon"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.useCoupon"}, "");
        }
        
        if(receipt.get("sales") != null){
          if(!GenericValidator.isDouble(String.valueOf(receipt.get("sales")))){
            errors.reject("validation.number.format", new String[]{"receipt.sales"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.sales"}, "");
        }
        
        if(receipt.get("discount") != null){
          if(!GenericValidator.isDouble(String.valueOf(receipt.get("discount")))){
            errors.reject("validation.number.format", new String[]{"receipt.discount"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.discount"}, "");
        }       
        
        if(receipt.get("netSales") != null){
          if(!GenericValidator.isDouble(String.valueOf(receipt.get("netSales")))){
            errors.reject("validation.number.format", new String[]{"receipt.netSales"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.netSales"}, "");
        }
        

        if(receipt.get("tax") != null){
          if(!GenericValidator.isDouble(String.valueOf(receipt.get("tax")))){
            errors.reject("validation.number.format", new String[]{"receipt.tax"}, "");
          }
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.tax"}, "");
        }
        
        if(receipt.get("items") != null){
          List<Map<Object,Object>> items = (List<Map<Object, Object>>) receipt.get("items");
          
          for(Map<Object,Object> item : items){

            if(item.get("menuCd") == null){
              errors.reject("validation.necessary.field", new String[]{"receipt.items menuCd"}, "");
            }
            
            if(item.get("isStamp") != null){
              if(!"true".equals(String.valueOf(item.get("isStamp"))) && !"false".equals(String.valueOf(item.get("isStamp")))){
                errors.reject("validation.invalid.boolean", new String[]{"receipt.items["+item.get("menuCd")+"] isStamp"}, "");
              }
            } else {
              errors.reject("validation.necessary.field", new String[]{"receipt.items["+item.get("menuCd")+"] isStamp"}, "");
            }
            
            if(item.get("sales") != null){
              if(!GenericValidator.isDouble(String.valueOf(item.get("sales")))){
                errors.reject("validation.number.format", new String[]{"receipt.items["+item.get("menuCd")+"] sales"}, "");
              }
            } else {
              errors.reject("validation.necessary.field", new String[]{"receipt.items["+item.get("menuCd")+"] sales"}, "");
            }
            
            if(item.get("discount") != null){
              if(!GenericValidator.isDouble(String.valueOf(item.get("discount")))){
                errors.reject("validation.number.format", new String[]{"receipt.items["+item.get("menuCd")+"] discount"}, "");
              }
            } else {
              errors.reject("validation.necessary.field", new String[]{"receipt.items["+item.get("menuCd")+"] discount"}, "");
            }       
            
            if(item.get("netSales") != null){
              if(!GenericValidator.isDouble(String.valueOf(item.get("netSales")))){
                errors.reject("validation.number.format", new String[]{"receipt.items["+item.get("menuCd")+"] netSales"}, "");
              }
            } else {
              errors.reject("validation.necessary.field", new String[]{"receipt.items["+item.get("menuCd")+"] netSales"}, "");
            }

            if(item.get("tax") != null){
              if(!GenericValidator.isDouble(String.valueOf(item.get("tax")))){
                errors.reject("validation.number.format", new String[]{"receipt.tax"}, "");
              }
            } else {
              errors.reject("validation.necessary.field", new String[]{"receipt.tax"}, "");
            }
          }
          
        } else {
          errors.reject("validation.necessary.field", new String[]{"receipt.items"}, "");
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
