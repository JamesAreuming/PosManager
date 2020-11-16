/*
 * Filename	: UserValidator.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.validator;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.jc.pico.bean.User;
import com.jc.pico.utils.AES256Cipher;
import com.jc.pico.utils.bean.Error.ErrorCodes;

/**
 * Created by ruinnel on 2016. 4. 14..
 */
@Component
public class UserValidator implements Validator {
  protected static Logger logger = LoggerFactory.getLogger(UserValidator.class);
  
  public HttpServletRequest request;
  public MessageSource messageSource;
  public Locale locale;
  
  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (target instanceof User) {
      User user = (User) target;
      PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
      String errCd = "";
      // phone number
      try {
        String mb = AES256Cipher.decodeAES256(user.getMb());
        String password = AES256Cipher.decodeAES256(user.getPassword());
        if (GenericValidator.isBlankOrNull(mb)) {
          errCd = ErrorCodes.INVALID_PARAM.getCode();
          errors.rejectValue("mb",  errCd, new String[]{"mb"}, "");
          logger.error("[" + errCd + "] " + "[" + messageSource.getMessage(errCd, new String[]{"mb"}, locale) + "]" +" , mb : " + user.getMb());
        } else {
          try {
            if (GenericValidator.isBlankOrNull(user.getCountry())) {
              errCd = ErrorCodes.INVALID_PARAM.getCode();
              errors.rejectValue("country", ErrorCodes.INVALID_PARAM.getCode(), new String[]{"country"}, "");
              logger.error("[" + errCd + "] " + "[" + messageSource.getMessage(errCd, new String[]{"country"}, locale) + "]" +" , country : " + user.getCountry());
            } else {
              phoneNumberUtil.parse(mb, user.getCountry());
            }
          } catch (NumberParseException e) {
            errCd = ErrorCodes.DATA_NOT_NUMBER_FORMAT.getCode();
            errors.rejectValue("mb", ErrorCodes.DATA_NOT_NUMBER_FORMAT.getCode(), new String[]{"mb"}, "");
            logger.error("[" + errCd + "] " + "[" + messageSource.getMessage(errCd, new String[]{"mb"}, locale) + "]" +" , mb : " + user.getMb());
          }
        }
        if (GenericValidator.isBlankOrNull(password)) {
          errCd = ErrorCodes.INVALID_PARAM.getCode();
          errors.rejectValue("password",  ErrorCodes.INVALID_PARAM.getCode(), new String[]{"password"}, "");
          logger.error("[" + errCd + "] " + "[" + messageSource.getMessage(errCd, new String[]{"password"}, locale) + "]" +" , password : " + user.getPassword());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      // email
//      if (!GenericValidator.isEmail(user.getEmail())) {
//        errors.rejectValue("email", "validation.invalid.email", new String[]{"email"}, "");
//      }
    }
  }

  public static final void main(String[] args) throws Exception {
    System.out.println(GenericValidator.isEmail(null));


    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    System.out.println(format.toPattern());

    BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
    System.out.println(bcrypt.encode("7120047783"));
    System.out.println(13%12);

    System.out.println(String.format("%029d", 1));
  }
}
