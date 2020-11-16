/*
 * Filename	: GlobalExceptionController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import com.jc.pico.exception.*;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.*;
import com.jc.pico.utils.bean.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionController {
  protected static Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

  @Autowired
  MessageSource messageSource;

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleError405(HttpServletRequest request, Exception e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    String code = Error.ErrorCodes.METHOD_NOT_SUPPORT.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{}, request.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.METHOD_NOT_SUPPORT.getCode(), messageSource.getMessage("405", new String[]{}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleError415(HttpServletRequest request, Exception e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    String code = Error.ErrorCodes.MEDIA_TYPE_NOT_SUPPORT.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{}, request.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.MEDIA_TYPE_NOT_SUPPORT.getCode(), messageSource.getMessage("415", new String[]{}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoPermissionException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleNoPermission(HttpServletRequest request, NoPermissionException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    String code = Error.ErrorCodes.NO_PERMISSION.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{}, request.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.NO_PERMISSION.getCode(), messageSource.getMessage("no.permission", new String[]{}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SettingNotFoundException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleSettingNotFound(HttpServletRequest request, SettingNotFoundException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    String code = Error.ErrorCodes.NO_PERMISSION.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{}, request.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.NO_PERMISSION.getCode(), messageSource.getMessage("setting.not.found", new String[]{}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CodeNotFoundException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleCodeNotFound(HttpServletRequest request, CodeNotFoundException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    if (e.getLocale() == null) {
      e.setLocale(request.getLocale());
    }
    String code = Error.ErrorCodes.CODE_NOT_FOUND.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{e.getType(), e.getType()}, e.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.CODE_NOT_FOUND.getCode(), messageSource.getMessage("code.not.found", new String[]{e.getType(), e.getType()}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataDuplicateException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleDataDuplicate(HttpServletRequest request, DataDuplicateException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    if (e.getLocale() == null) {
      e.setLocale(request.getLocale());
    }
    String code = Error.ErrorCodes.DATA_DUPLICATE.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{e.getValue()}, e.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.DATA_DUPLICATE.getCode(), messageSource.getMessage("validation.duplicate", new String[]{e.getValue()}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidJsonException.class)
  @ResponseBody
  public ResponseEntity<Object> handleInvalidJson(HttpServletRequest request, InvalidJsonException e) {
    String name;
    if (e.getTypeClass() != null) {
      name = e.getTypeClass().getCanonicalName();
    } else if (e.getTypeRef() != null) {
      name = e.getTypeRef().getType().getClass().getCanonicalName();
    } else {
      name = "unknown";
    }

    String uri = request.getRequestURI();
    if (uri != null && uri.startsWith("/api/pos/")) {
      PosResult result = new PosResult();
      result.setSuccess(false);
      result.setResultMsg("Invalid Json - (" + name + ")");
      logger.error("[{}][{}] {}", Error.ErrorCodes.INVALID_PARAM, "Invalid Json!", e.getMessage(), e);
      return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
    } else {
      JsonResult result = new JsonResult();
      result.setSuccess(false);
      String code = Error.ErrorCodes.INVALID_JSON.getCode();
      result.addError(code, messageSource.getMessage(code, new String[]{name}, request.getLocale()));
//    result.addError(Error.ErrorCodes.INVALID_JSON.getCode(), messageSource.getMessage("validation.invalid.json", new String[]{name}, request.getLocale()));
      return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(DataNotRegisteredException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleDataNotRegistered(HttpServletRequest request, DataNotRegisteredException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    if (e.getLocale() == null) {
      e.setLocale(request.getLocale());
    }
    
    String code = Error.ErrorCodes.DATA_NOT_REGISTERED.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{e.getType(), e.getValue()}, e.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.DATA_NOT_REGISTERED.getCode(), messageSource.getMessage("validation.not.registered", new String[]{e.getType(), e.getValue()}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataNotFoundException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleDataNotFound(HttpServletRequest request, DataNotFoundException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    if (e.getLocale() == null) {
      e.setLocale(request.getLocale());
    }
    String code = Error.ErrorCodes.DATA_NOT_FOUND.getCode();
    header.setErrCd(code);
    header.setErrMsg( messageSource.getMessage(code, new String[]{e.getId()}, e.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.DATA_NOT_FOUND.getCode(), messageSource.getMessage("validation.data.not.found", new String[]{e.getId()}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseBody
  public ResponseEntity<Object> handleMissingServletRequestParameter(HttpServletRequest request, MissingServletRequestParameterException e) {
    String uri = request.getRequestURI();
    if (uri != null && uri.startsWith("/api/pos/")) {
      PosResult result = new PosResult();
      result.setSuccess(false);
      result.setResultMsg(e.getParameterName() + " is necessary");
      logger.error("[{}][{}] {}", Error.ErrorCodes.INVALID_PARAM, "Missing servlet request parameter!", e.getMessage(), e);
      return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
    } else {
      JsonResult result = new JsonResult();
      result.setSuccess(false);
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      result.addError(code, messageSource.getMessage(code, new String[]{e.getParameterName()}, request.getLocale()));
//    result.addError(Error.ErrorCodes.INVALID_PARAM.getCode(), messageSource.getMessage("validation.necessary.field", new String[]{e.getParameterName()}, request.getLocale()));
      return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(InvalidTokenException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleInvalidToken(HttpServletRequest request, InvalidTokenException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    String code = Error.ErrorCodes.DATA_NOT_REGISTERED.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{}, request.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.DATA_NOT_REGISTERED.getCode(), messageSource.getMessage("validation.invalid.token", new String[]{}, request.getLocale()));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleSQLException(HttpServletRequest request, DataIntegrityViolationException e) {
    AppJsonResult result = new AppJsonResult();
    result.setData(false);
    Header header = new Header();
    String code = Error.ErrorCodes.DATA_NOT_REGISTERED.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{}, request.getLocale()));
    result.setHeader(header);
//    result.addError(Error.ErrorCodes.DATA_NOT_REGISTERED.getCode(), messageSource.getMessage("validation.invalid.token", new String[]{}, request.getLocale()));
    logger.equals(e.getCause().getClass().getName() + " ; " + e.getCause().getMessage());
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidParamException.class)
  public ResponseEntity<AppJsonResult> handleInvalidParam(HttpServletRequest request, InvalidParamException e) {
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();
    Errors errors = e.getErrors();
    
    if (e.getLocale() == null) {
      e.setLocale(request.getLocale());
    }
    if (errors != null && errors.hasFieldErrors()) {
      for (FieldError err : errors.getFieldErrors()) {
        String message = messageSource.getMessage(err.getCode(), err.getArguments(), e.getLocale());
//        result.addError(Error.ErrorCodes.INVALID_PARAM.getCode(), message);
        header.setErrCd(Error.ErrorCodes.INVALID_PARAM.getCode());
        header.setErrMsg(message);
      }
    } else if (errors != null) {
      for (ObjectError err : errors.getAllErrors()) {
        String message = messageSource.getMessage(err.getCode(), err.getArguments(), e.getLocale());
        header.setErrCd(Error.ErrorCodes.INVALID_PARAM.getCode());
        header.setErrMsg(message);
      }
    } else if (e.getParams() != null) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      header.setErrCd(code);
      header.setErrMsg(messageSource.getMessage(code, new String[]{e.getParams()[0]}, e.getLocale()));
    }

    result.setHeader(header);
    result.setData(false);
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidValueException.class)
  public ResponseEntity<AppJsonResult> handleInvalidValue(HttpServletRequest request, InvalidValueException e) {
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();
    Errors errors = e.getErrors();
    if (errors != null && errors.hasFieldErrors()) {
      for (FieldError err : errors.getFieldErrors()) {
        logger.debug("field = " + err.getField());
        String message = messageSource.getMessage(err.getCode(), err.getArguments(), request.getLocale());
//        result.addError(Error.ErrorCodes.INVALID_PARAM.getCode(), message);
        header.setErrCd(Error.ErrorCodes.INVALID_VALUE.getCode());
        header.setErrMsg(message);
      }
    } else if (errors != null) {
      for (ObjectError err : errors.getAllErrors()) {
        String message = messageSource.getMessage(err.getCode(), err.getArguments(), request.getLocale());
        header.setErrCd(Error.ErrorCodes.INVALID_VALUE.getCode());
        header.setErrMsg(message);
      }
    } else if (e.getParams() != null) {
      if (e.getLocale() == null) {
        e.setLocale(request.getLocale());
      }
      String code = Error.ErrorCodes.INVALID_VALUE.getCode();
      header.setErrCd(code);
      header.setErrMsg(messageSource.getMessage(code, new String[]{e.getParams()[0]}, e.getLocale()));
    }

    result.setHeader(header);
    result.setData(false);
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidEmailException.class)
  @ResponseBody
  public ResponseEntity<AppJsonResult> handleInvalidEmail(HttpServletRequest request, InvalidEmailException e) {
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();
    if (e.getLocale() == null) {
      e.setLocale(request.getLocale());
    }
    String code = Error.ErrorCodes.INVALID_EMAIL.getCode();
    header.setErrCd(code);
    header.setErrMsg(messageSource.getMessage(code, new String[]{e.getParams()[0]}, e.getLocale()));

    result.setHeader(header);
    result.setData(false);
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }
}
