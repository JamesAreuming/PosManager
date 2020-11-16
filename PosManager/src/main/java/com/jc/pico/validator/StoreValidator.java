/*
 * Filename	: StoreValidator.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.validator;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jc.pico.bean.SvcStore;

/**
 * Created by ruinnel on 2016. 4. 14..
 */
@Component
public class StoreValidator implements Validator {
  protected static Logger logger = LoggerFactory.getLogger(StoreValidator.class);
  @Override
  public boolean supports(Class<?> clazz) {
    return SvcStore.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (target instanceof SvcStore) {
      SvcStore store = (SvcStore) target;
      
      if(GenericValidator.isBlankOrNull(store.getStoreCd())){
        errors.rejectValue("storeCd", "validation.necessary.field", new String[]{"storeCd"}, "");
      }
      
      if(GenericValidator.isBlankOrNull(store.getStoreNm())){
        errors.rejectValue("storeNm", "validation.necessary.field", new String[]{"storeNm"}, "");
      }
      
      if(GenericValidator.isBlankOrNull(store.getBizNo())){
        errors.rejectValue("bizNo", "validation.necessary.field", new String[]{"bizNo"}, "");
      }
      
      if(store.getTenantId() == null){
        errors.rejectValue("TenantId", "validation.necessary.field", new String[]{"TenantId"}, "");
      }
      
      if(store.getBrandId() == null){
        errors.rejectValue("BrandId", "validation.necessary.field", new String[]{"BrandId"}, "");
      }
      
    }
  }

}
