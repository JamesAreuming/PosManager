/*
 * Filename	: AppController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.BizPlatform;
import com.jc.pico.bean.BizPlatformExample;
import com.jc.pico.bean.Service;
import com.jc.pico.bean.SvcApp;
import com.jc.pico.bean.SvcAppExample;
import com.jc.pico.bean.SvcMailAccount;
import com.jc.pico.bean.SvcMailTemplate;
import com.jc.pico.bean.SvcMailTemplateExample;
import com.jc.pico.bean.SvcMessageTemplate;
import com.jc.pico.bean.SvcMessageTemplateExample;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcSmsAuth;
import com.jc.pico.bean.SvcSmsAuthExample;
import com.jc.pico.bean.SvcSmsLogWithBLOBs;
import com.jc.pico.bean.SvcTerms;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserCardExample;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserExample;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.DataNotRegisteredException;
import com.jc.pico.exception.InvalidEmailException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.InvalidTokenException;
import com.jc.pico.exception.InvalidValueException;
import com.jc.pico.exception.SettingNotFoundException;
import com.jc.pico.ext.sms.SMSUtil;
import com.jc.pico.mapper.BizPlatformMapper;
import com.jc.pico.mapper.ServiceMapper;
import com.jc.pico.mapper.SvcAppMapper;
import com.jc.pico.mapper.SvcMailTemplateMapper;
import com.jc.pico.mapper.SvcMessageTemplateMapper;
import com.jc.pico.mapper.SvcSmsAuthMapper;
import com.jc.pico.mapper.SvcSmsLogMapper;
import com.jc.pico.mapper.UserCardMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.queue.SmsSender;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.utils.AES256Cipher;
import com.jc.pico.utils.BarcodeUtil;
import com.jc.pico.utils.CertUtil;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.Config;
import com.jc.pico.utils.CustomMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.PagingUtil;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.bean.AppJsonResult;
import com.jc.pico.utils.bean.Error;
import com.jc.pico.utils.bean.Error.ErrorCodes;
import com.jc.pico.utils.bean.Header;
import com.jc.pico.utils.bean.JsonResult;
import com.jc.pico.utils.bean.SendEmail;
import com.jc.pico.utils.jc.mail.MailSender;
import com.jc.pico.validator.UserValidator;

@Controller
// @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST,
// RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
@RequestMapping(value = "/app/public", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
public class PublicController {
  protected static Logger logger = LoggerFactory.getLogger(PublicController.class);
  private static final long BIZ_PLARTFORM_ID = 1;
  
  private static final String REGISTER_TYPE_USER_ADD = "user_add";
  private static final String REGISTER_TYPE_USER_UPDATE_EMAIL= "user_update_email";
  private static final String REGISTER_TYPE_USER_UPDATE_NAME= "user_update_name";
  private static final String REGISTER_TYPE_USER_UPDATE_PHONE_NUMBER= "user_update_phone_number";
  private static final String REGISTER_TYPE_USER_UPDATE_PASSWORD = "user_update_password";
  
 private Config config = Config.getInstance();
 
  @Autowired
  SqlSessionTemplate sessionTemplate;
  @Autowired
  MessageSource messageSource;
  @Autowired
  CodeUtil codeUtil;
  @Autowired
  CertUtil certUtil;
  @Autowired
  SmsSender smsSender;
  @Autowired
  com.jc.pico.queue.MailSender mailSender;
  @Autowired
  UserValidator userValidator;
  @Autowired
  BarcodeUtil barcodeUtil;
  @Autowired
  AppNotificationService appNotificationService;
  @Autowired
  @Qualifier("dataSourceTransactionManager")
  PlatformTransactionManager dataSourceTransactionManager;
  
  private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

  private Errors makeErrors() {
    return new MapBindingResult(Maps.newHashMap(), this.getClass().getName());
  }

  private SvcMailAccount getDefaultMailAccount() {
    SvcMailAccount account = new SvcMailAccount();

    // account.setHost(Global.);
    // account.setUsername(Globals.MAIL_USERNAME);
    return account;
  }

  private SvcMailTemplate getMailTemplate(Long serviceId, String messageTp) {
    SvcMessageTemplateMapper msgMapper = sessionTemplate.getMapper(SvcMessageTemplateMapper.class);
    SvcMessageTemplateExample msgExample = new SvcMessageTemplateExample();
    msgExample.createCriteria().andUsedEqualTo(true).andMessageTpEqualTo(messageTp);

    List<SvcMessageTemplate> messages = msgMapper.selectByExample(msgExample);
    if (messages != null && messages.size() > 0) {
      SvcMailTemplateMapper mapper = sessionTemplate.getMapper(SvcMailTemplateMapper.class);
      SvcMailTemplateExample example = new SvcMailTemplateExample();
      example.createCriteria().andServiceIdEqualTo(serviceId).andMessageIdEqualTo(messages.get(0).getId())
          .andUsedEqualTo(true);

      List<SvcMailTemplate> templates = mapper.selectByExampleWithBLOBs(example);
      if (templates != null && templates.size() > 0) {
        return templates.get(0);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @RequestMapping("/platform")
  @ResponseBody
  public AppJsonResult platform(
      @RequestParam("params") String params) throws Exception {
    try {
      Map<String, Object> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, Object>>() {});
      Locale locale = getLocale((String) param.get("lang"));
      Long id = ((Integer) param.get("id")).longValue();
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      
      BizPlatformMapper mapper = sessionTemplate.getMapper(BizPlatformMapper.class);
      BizPlatformExample example = new BizPlatformExample();
      
      example.createCriteria().andIdEqualTo(id);
      List<BizPlatform> datas = mapper.selectByExample(example);
      
      if (datas != null && datas.size() > 0) {
        result.setData(datas.get(0));
      } else {
        String errCd = ErrorCodes.DATA_NOT_FOUND.getCode();
        result.setData(null);
        header.setErrCd(errCd);
        header.setErrMsg(messageSource.getMessage(errCd, new String[]{}, locale));
      }
      
      result.setHeader(header);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @RequestMapping("/code")
  @ResponseBody
  public AppJsonResult baseCode(@RequestParam(value = "lastUpdated", defaultValue = "0") Long lastUpdated) {
    Map<String, Object> data = Maps.newHashMap();
    data.put("lastUpdated", codeUtil.getLastUpdated());
    logger.debug("last update : " + (new Date(lastUpdated) + " code last update : " + (new Date(codeUtil.getLastUpdated()))));
    if (lastUpdated != 0 && lastUpdated < codeUtil.getLastUpdated()) {
      data.put("codes", codeUtil.getAll());
    }

    AppJsonResult result = new AppJsonResult();
    Header header = new Header();

    header.setErrCd(ErrorCodes.SUCCESS.getCode());
    result.setHeader(header);
    result.setData(data);

    return result;
  }

  @RequestMapping("/baseInfo")
  @ResponseBody
  public AppJsonResult baseInfo(
      @RequestParam("params") String params,
      @RequestHeader("Service-Identifier") Long serviceId) throws Exception {
    try {
      Map<String, Object> param = JsonConvert.JsonConvertObject(params,  new TypeReference<Map<String, Object>>() {});
      SvcAppMapper mapper = sessionTemplate.getMapper(SvcAppMapper.class);
    
      String os = String.valueOf(param.get("os"));
      SvcAppExample example = new SvcAppExample();
      example.createCriteria().andUseYnEqualTo(true).andServiceIdEqualTo(serviceId)
      		.andAppTpEqualTo(codeUtil.getBaseCodeByAlias("app-tp-mobile")) // 앱 종류, base code: 108001
      		.andOsTpEqualTo(codeUtil.getBaseCodeByAlias(os)); // OS 종류, main code : 103, FIXME os 종류에 따라 조건 변경 필요, 
      example.setOrderByClause("VERSION_CODE DESC");
      List<SvcApp> apps = mapper.selectByExampleWithRowbounds(example, new RowBounds(0, 1));
      SvcApp app = (apps != null && apps.size() > 0) ? apps.get(0) : null;
    
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(app);
    
      return result;
    } catch (Exception e) {
      logger.error("[baseInfo] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/check/email")
  @ResponseBody
  public JsonResult checkMail(@RequestHeader("Service-Identifier") Integer serviceId,
      @RequestParam("email") String email) {
    UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);

    String userTpAssociate = codeUtil.getBaseCodeByAlias("associate-user");
    String userTpReular = codeUtil.getBaseCodeByAlias("user");
    String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
    UserExample example = new UserExample();
    example.createCriteria().andTypeIn(Lists.newArrayList(userTpReular, userTpAssociate)).andStatusEqualTo(statusNormal)
        .andEmailEqualTo(email);

    int count = mapper.countByExample(example);

    JsonResult result = new JsonResult();
    result.setSuccess(count == 0);
    return result;
  }

  @RequestMapping("/phone/check")
  @ResponseBody
  public AppJsonResult phoneCheck(
      @RequestHeader("Service-Identifier") Integer serviceId,
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      String mb = "";
      if (param.containsKey("mb")  && param.get("mb").length() > 0) {
        mb = AES256Cipher.decodeAES256(param.get("mb"));
        logger.debug("mb 1: " + mb);
      } else {
        String[] errParam = {"mb"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      UserExample example = new UserExample();
      example.createCriteria()
        .andMbEqualTo(mb)
        .andStatusEqualTo(codeUtil.getBaseCodeByAlias("user-status-normal"));
      
      List<User> users = mapper.selectByExample(example);
  
      header.setErrCd(Error.ErrorCodes.SUCCESS.getCode());
      if (users != null && users.size() > 0) {
        result.setData(true);
      } else {
        result.setData(false);  
      }
  
      result.setHeader(header);
      return result;
    } catch (InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[phoneDuplicateCheck] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[phoneDuplicateCheck] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/terms")
  @ResponseBody
  public AppJsonResult terms(
      @RequestHeader("Service-identifier") Long serviceId,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    try {
      Map<String, Object> params = Maps.newHashMap();
      params.put("serviceId", serviceId);
      params.put("termsSvcTp",  codeUtil.getBaseCodeByAlias("terms-service-tp-bak-app"));
      // limits
      if (rowsPerPage != 0) {
        params.put("offset", pageNo);
        params.put("limit", rowsPerPage);
      }      
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      List<Map<String, Object>> datas = mapper.getTerms(params);
      int totalCnt = mapper.getTermsCount(params);
      
      List<Object> items = new ArrayList<Object>();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> notice = datas.get(i);
        Map<String, Object> item = convertCamelcase(notice);
        items.add(item);
      }
      
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, null);
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(Error.ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      return result;
    } catch (Exception e) {
      logger.error("[terms] fail !! ", e);
      throw e;
    }
  }
  
  @RequestMapping("/password/check")
  @ResponseBody
  public AppJsonResult passwordCheck(
      @RequestHeader("Service-identifier") Long serviceId,
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      String password = "";
      String mb = "";
      
      if (param.containsKey("password") && param.get("password").length() > 0) {
        password = AES256Cipher.decodeAES256(param.get("password"));
      } else {
        String[] errParam = {"password"};
        throw new InvalidParamException(errParam, locale);
      }
      
      if (param.containsKey("mb") && param.get("mb").length() > 0) {
        mb = AES256Cipher.decodeAES256(param.get("mb"));
      } else {
        String[] errParam = {"mb"};
    
        throw new InvalidParamException(errParam, locale);
      }
      
      // andPasswordEqualsTo먹히지 않음, 번호로 유저를 반환하여 비밀번호 확인
      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      UserExample example = new UserExample();
      example.createCriteria()
        .andMbEqualTo(mb)
        .andStatusEqualTo(codeUtil.getBaseCodeByAlias("user-status-normal"));
      List<User> users = mapper.selectByExample(example);

      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      Map<String, Object> data = Maps.newHashMap();
      if (users != null && users.size() > 0) {
        User user = users.get(0);
        
        // 비밀번호 유효성 검사
        CustomMapper customMapper = sessionTemplate.getMapper(CustomMapper.class);
        Map<String, Object> checkParam = Maps.newHashMap();
        int loginRestriction = config.getInt("login.restriction");
        int loginRestrctionFullCount = config.getInt("login.restriction.full.count");
        int loginRetryTerm = config.getInt("login.retry.term");
        byte loginFailCnt = user.getLoginFailCnt();
        
        if (loginFailCnt < loginRestrctionFullCount) {  // 로그인 실패 횟수가 로그인 제한 실패 횟수보다 작을 때
          if (bcryptEncoder.matches(password,  user.getPassword())) {  // 데이터 유효
            checkParam.put("userId",  user.getId());
            
            if (loginFailCnt > 0) {
              checkParam.put("loginFailCnt", 0);
              customMapper.updateLoginFailCount(checkParam);
            }
            
            header.setErrCd(Error.ErrorCodes.SUCCESS.getCode());
            data.put("success", true);
            data.put("username", user.getUsername());
            result.setData(data);
          } else { // 데이터 유효하지 않음
            logger.debug("login check 1 : " + password);
            logger.debug("login check 2 : " +user.getPassword());
            logger.debug("login check 3 : " +bcryptEncoder.matches(password,  user.getPassword()));
            if (loginFailCnt >= loginRestriction) { // 로그인이 로그인 잠시 제한 횟수보다 높을 때
              if (loginFailCnt % loginRestriction == 0) { // 로그인 제한 상태일 경우
                String restrictionTm = "";
                long waitTm = System.currentTimeMillis() - user.getLoginTryTm().getTime();
                if (waitTm > loginRetryTerm) { // 로그인 제한 시간이 지났을 때
                  if (bcryptEncoder.matches(password,  user.getPassword())) {  // 데이터 유효
                    checkParam.put("userId",  user.getId());
                    
                    if (loginFailCnt > 0) {
                      checkParam.put("loginFailCnt", 0);
                      customMapper.updateLoginFailCount(checkParam);
                    }
                    
                    header.setErrCd(Error.ErrorCodes.SUCCESS.getCode());
                    data.put("success", true);
                    result.setData(data);
                  } else { // 데이터 유효하지 않음
                    checkParam.put("userId",  user.getId());
                    checkParam.put("loginFailCnt",  loginFailCnt + 1);
                    customMapper.updateLoginFailCount(checkParam);
                    
                    String errCd = Error.ErrorCodes.LOGIN_FAIL.getCode();
                    header.setErrCd(errCd);
                    header.setErrMsg(messageSource.getMessage(errCd, new String[]{mb}, locale));
                    data.put("success",  false);
                    data.put("loginFailCnt", loginFailCnt);
                    result.setData(data);
                  }
                }  else {  // 로그인 제한 시간 이내일 때
                  String second =  messageSource.getMessage("login.second", new String[]{}, locale);
                  long waitTime = (loginRetryTerm - waitTm) / 1000;
                  waitTime = waitTime == 0 ? 1 : waitTime;
                  restrictionTm = String.format("%s %s", String.valueOf(waitTime), second);
                  
                  String errCd = Error.ErrorCodes.LOGIN_RESTRCTION.getCode();
                  header.setErrCd(errCd);
                  header.setErrMsg(messageSource.getMessage(errCd, new String[]{restrictionTm}, locale));
                  data.put("restrictionTm", waitTime);
                  data.put("success",  false);
                  result.setData(data);
                }
              } else { // 로그인 제한 상태가 아닐 때
                checkParam.put("userId",  user.getId());
                checkParam.put("loginFailCnt",  loginFailCnt + 1);
                customMapper.updateLoginFailCount(checkParam);
                
                String errCd = Error.ErrorCodes.LOGIN_FAIL.getCode();
                header.setErrCd(errCd);
                header.setErrMsg(messageSource.getMessage(errCd, new String[]{mb}, locale));
                data.put("success",  false);
                data.put("loginFailCnt", loginFailCnt + 1);
                result.setData(data);
              }
            } else { // 로그인 제한 상태가 아닐 때
              checkParam.put("userId",  user.getId());
              checkParam.put("loginFailCnt",  loginFailCnt + 1);
              customMapper.updateLoginFailCount(checkParam);
              
              String errCd = Error.ErrorCodes.LOGIN_FAIL.getCode();
              header.setErrCd(errCd);
              header.setErrMsg(messageSource.getMessage(errCd, new String[]{mb}, locale));
              data.put("success",  false);
              data.put("loginFailCnt", loginFailCnt + 1);
              result.setData(data);
            }
          }
        } else { // 로그인 실패 횟수가 로그인 제한을 넘을 때
          String bakServiceEmail = config.getString("bak.service.email");
          String bakServiceContactPhone = config.getString("bak.service.contact.phone");
          header.addExtra("bakServiceEmail", bakServiceEmail);
          header.addExtra("bakServiceContactPhone", bakServiceContactPhone);
          
          String errCd = Error.ErrorCodes.LOGIN_RESTRCTION_FULL.getCode();
          header.setErrCd(errCd);
          header.setErrMsg(messageSource.getMessage(errCd, new String[]{}, locale));
          
          data.put("success",  false);
          result.setData(data);
        }
      } else {
        String errCd = Error.ErrorCodes.LOGIN_FAIL.getCode();
        header.setErrCd(errCd);
        header.setErrMsg(messageSource.getMessage(errCd, new String[]{mb}, locale));
        data.put("success", false);
        result.setData(data);
      }
      result.setHeader(header);
      return result;
    } catch (InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String errParam= invalidParam.getParams()[0];
      logger.error("[passwordCheck] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[passwordCheck] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/password/find")
  @ResponseBody
  public AppJsonResult findPassword(
      @RequestHeader("Service-Identifier") Integer serviceId,
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("locale"));
    try {
     String mb = "";
     String email = "";
     String errCd = "";
     if (param.containsKey("mb") && param.get("mb").length() > 0) {
       mb = AES256Cipher.decodeAES256(param.get("mb"));
     } else {
       String[] errParam = {"mb"};
       throw new InvalidParamException(errParam, locale);
     }
     
     if (param.containsKey("email") && param.get("email").length() > 0) {
       email = AES256Cipher.decodeAES256(param.get("email"));
     } else {
       String[] errParam = {"email"};
       throw new InvalidParamException(errParam, locale);
     }
     
     UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
     UserExample example = new UserExample();
     example.createCriteria()
       .andMbEqualTo(mb)
       .andStatusEqualTo(codeUtil.getBaseCodeByAlias("user-status-normal"));
     
     AppJsonResult result = new AppJsonResult();
     Header header = new Header();
     List<User> users = mapper.selectByExample(example);
     if (users != null && users.size() > 0) {
       /* success. 해당 번호에 맞는 이메일일 때 임시비밀번호 8자리를 이메일로 보낸다.*/
       User user = users.get(0);
       if (!GenericValidator.isBlankOrNull(user.getEmail())) {
         if (GenericValidator.isEmail(user.getEmail())) {
           if (!user.getEmail().equals(email)) {
             /* false, 1. 해당 번호에 맞는 이메일이아닐 때 */
             String[] errParam = {"email"};
             throw new InvalidValueException(errParam, locale);
           } else { /* 임시비밀번호 전송 */
             StringBuffer passwd = new StringBuffer();
             Random rn = new Random();
             
             for (int i = 0; i < 8; i++) {
               passwd.append(Math.round(rn.nextDouble() * 10));
             }
             
             example = new UserExample();
             example.createCriteria().andIdEqualTo(user.getId());
             user.setPassword(passwd.toString());
             user.setLoginFailCnt((byte) 0); // 비밀번호 실패 카운트 초기화
             mapper.updateByExample(user, example);
             
             String host = config.getString("email.host");
             String port = config.getString("email.port");
             String contentType = config.getString("email.content.type");
             String charset = config.getString("email.content.charset");
             String supportAccount = config.getString("email.support.account");
             String password = config.getString("email.support.password");

             String to = user.getEmail();
             String subject = messageSource.getMessage("email.send.subject",  new String[]{}, locale);
             String msg1 = messageSource.getMessage("email.send.msg1",  new String[]{}, locale);
             String msg2 = messageSource.getMessage("email.send.msg2",  new String[]{}, locale);
             String msg3 = messageSource.getMessage("email.send.msg3",  new String[]{}, locale);
             String tempPassword = messageSource.getMessage("email.send.temp.password",  new String[]{passwd.toString()}, locale);
             StringBuffer body = new StringBuffer();
             
             body.append("<div style='width:60%; height:60%;'>");
             body.append("<div align='center'>");
             body.append("<img alt='' src='http://www.order9.co.kr/image-resource/app/order9_app/order9_app.png'  height='20%' width='20%'/>");
             body.append("</div>");
             body.append(String.format("<h2 align='center'><b>%s</b></h2>", subject));
             body.append("<br/>");
             body.append("<h4>");
             body.append(String.format("%s.</h4>", msg1));
             body.append("<h4>");
             body.append(String.format("%s",  msg2));
             body.append("</h4>");
             body.append("<br/>");
             body.append("<h4> ");
             body.append(String.format("%s", tempPassword));
             body.append("</h4>");
             body.append("<br/>");
             body.append("<h4>");
             body.append(String.format("%s",  msg3));
             body.append("</h4>");
             body.append("</div>");
             try {
               MailSender mail = new MailSender(host, supportAccount, password, port);

               mail.connect();
               mail.setFrom(supportAccount);
               mail.setTo(to);
               mail.setSubject(subject);
               mail.setText(body.toString());
               mail.setCharset(charset);
               mail.setType(contentType);
               mail.send();
               
               header.setErrCd(ErrorCodes.SUCCESS.getCode());
             } catch (Exception e) {
               e.printStackTrace();
               errCd = ErrorCodes.TIME_OUT.getCode();
               header.setErrCd(errCd);
               header.setErrMsg(messageSource.getMessage(errCd,  new String[]{"email", email}, locale));
             }
           }
         } else {
           String[] errParam = {"email"};
           throw new InvalidEmailException(errParam, locale);
         }
       } else {
         throw new DataNotRegisteredException("email", "", locale);  
       }
     } else {
       /* false, 2. 해당 번호 데이터가 존재하지 않을 때 */
       throw new DataNotRegisteredException("mb", mb, locale);
     }
     
     result.setHeader(header);
     result.setData(true);
      return result;
    } catch (InvalidEmailException invalidEmail) {
      String code = Error.ErrorCodes.INVALID_EMAIL.getCode();
      String errParam= invalidEmail.getParams()[0];
      logger.error("[findPassword] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidEmail;
    } catch (InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String errParam= invalidParam.getParams()[0];
      logger.error("[findPassword] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (InvalidValueException invalidValue) {
      String code = Error.ErrorCodes.INVALID_VALUE.getCode();
      String errParam= invalidValue.getParams()[0];
      logger.error("[findPassword] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidValue;
    } catch (DataNotRegisteredException notData) {
      String code = Error.ErrorCodes.DATA_NOT_REGISTERED.getCode();
      String errParam= notData.getType();
      logger.error("[findPassword] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw notData;
    } catch (Exception e) {
      logger.error("[findPassword] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/pushtest")
  @ResponseBody
  public AppJsonResult pushTest(
      HttpServletRequest request) throws Exception {
    try {
      
      AppJsonResult result = new AppJsonResult();
      SvcOrder order = new SvcOrder();
      order.setUserId(181l);
      order.setPathTp("606002");
      order.setOrderTp("605001");
      order.setOrderSt("607001");
      order.setBrandId(4l);
      order.setStoreId(4l);
      appNotificationService.notifyOrderStatusChanged(order);
      
      result.setData(true);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }    
  }
  
  @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
  @ResponseBody
  public AppJsonResult register(
      @RequestHeader("Service-Identifier") Integer serviceId,
      @RequestBody User user,
      @RequestParam(value = "params", defaultValue = "{}") String params,
      HttpServletRequest request,
      BindingResult errors) throws Exception {
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();
   try {
    Map<String, Object> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, Object>>() {});
    Locale locale = getLocale(String.valueOf(param.get("lang")));
    user = decodeUser(user);
    logger.debug("user : " + JsonConvert.toJson(user));
    logger.debug("params : " + JsonConvert.toJson(param));
    
    if (param.containsKey("registerType") && String.valueOf(param.get("registerType")).length() > 0) {
      String registerTp = String.valueOf(param.get("registerType"));
      
      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      UserExample example = new UserExample();
      
      if (registerTp.equals(REGISTER_TYPE_USER_ADD) || registerTp.equals(REGISTER_TYPE_USER_UPDATE_PHONE_NUMBER)) {
        CustomMapper customMapper= sessionTemplate.getMapper(CustomMapper.class);
        
        if (registerTp.equals(REGISTER_TYPE_USER_UPDATE_PHONE_NUMBER)) { // 휴대폰 번호 변경
          if (param.containsKey("mb") && String.valueOf(param.get("mb")).length() > 0) {
            user.setMb(AES256Cipher.decodeAES256(String.valueOf(param.get("mb"))));
          } else {
            header.setErrCd(ErrorCodes.INVALID_PARAM.getCode());
            header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_PARAM.getCode(),  new String[]{"phone number"}, locale));
            result.setData(null);
          }
          
          if (param.containsKey("mbCountryCd") && String.valueOf(param.get("mbCountryCd")).length() > 0) {
            user.setMbCountryCd(String.valueOf(param.get("mbCountryCd")));
          } else {
            header.setErrCd(ErrorCodes.INVALID_PARAM.getCode());
            header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_PARAM.getCode(),  new String[]{"country number"}, locale));
            result.setData(null);
          }
          user.setFaxCountryCd(String.valueOf(param.get("mbCountryCd")));
          user.setTelCountryCd(String.valueOf(param.get("mbCountryCd")));
          example = new UserExample();
          example.createCriteria().andIdEqualTo(user.getId());
          user.setPassword(null);
          user.setMbBk(user.getMb().substring(user.getMb().length() - 4, user.getMb().length()));
          logger.debug("user: " + JsonConvert.toJson(user));
          mapper.updateByExampleSelective(user,  example);
        } else { // 신규 등록
          if (param.containsKey("isJoinDuplicate") && (Boolean) param.get("isJoinDuplicate")) {
            UserMapper userMapper = sessionTemplate.getMapper(UserMapper.class);
            UserExample userExample = new UserExample();
            userExample.createCriteria()
              .andUsernameEqualTo(user.getUsername())
              .andStatusEqualTo(codeUtil.getBaseCodeByAlias("user-status-normal"));
            
            List<User> users = userMapper.selectByExample(userExample);
            if (users != null && users.size() > 0) {
              User userDisable = users.get(0);
              userExample = new UserExample();
              userExample.createCriteria().andIdEqualTo(userDisable.getId());
              userDisable.setStatus(codeUtil.getBaseCodeByAlias("user-status-suspend"));
              
              userDisable.setUsername("");
              userDisable.setEmail("");
              userDisable.setMbCountryCd("");
              userDisable.setMb("");
              userDisable.setMbBk("");
              userDisable.setTelCountryCd("");
              userDisable.setFaxCountryCd("");
              userDisable.setCountry("");
              
              UserCardMapper cardMapper = sessionTemplate.getMapper(UserCardMapper.class);
              UserCardExample cardExample = new UserCardExample();
              
              cardExample.createCriteria().andUserIdEqualTo(userDisable.getId());
              cardMapper.deleteByExample(cardExample);
              
              userMapper.updateByPrimaryKeySelective(userDisable);
            }
          }
          String groupCd = barcodeUtil.makeGroupCode(user.getMb());
          Integer maxMemCd = customMapper.getMaxMemCode(groupCd);
          Integer memCd = (maxMemCd != null ? maxMemCd : 0) + 1;
          String barcode = barcodeUtil.makeUserCode(groupCd, memCd);
          // barcode 생성 후 update
          //      user.setId(id);
          user.setPassword(user.getPassword());
          user.setGrpCd(groupCd);
          user.setMemCd(memCd);
          user.setBarcode(barcode);
          // process mobile number
          user.setUsername(barcode);
          user.setMbBk(user.getMb().substring(user.getMb().length() - 4, user.getMb().length()));
          
          /*2. 데이터 유효성 검사*/
          userValidator.request = request;
          userValidator.messageSource = messageSource;
          userValidator.locale = locale;
          userValidator.validate(user, errors);
          
          List<SvcTerms> terms = JsonConvert.JsonConvertObject(JsonConvert.toJson(param.get("terms")),  new TypeReference<List<SvcTerms>>(){});
          if (terms != null && terms.size() > 0) {
            String termsSvcTpBakApp = codeUtil.getBaseCodeByAlias("terms_service_tp_bak_app");
            String termsTpSMS = codeUtil.getBaseCodeByAlias("term-tp-sms");
            String termsTpPush = codeUtil.getBaseCodeByAlias("terms-tp-push");
            for (SvcTerms term : terms) {
              if (termsSvcTpBakApp.equals(term.getTermsSvcTp())) {  // 약관 서비스 유형이 BAK_app일 때
                if (termsTpSMS.equals(term.getTermsTp())) {
                  user.setIsSvcSms(true);
                } 
                
                if (termsTpPush.equals(term.getTermsTp())) {
                  user.setIsSvcPush(true);
                }
              }
            }
          }
          if (errors.hasErrors()) {
            InvalidParamException ex = new InvalidParamException(errors);
            ex.setLocale(locale);
            throw ex;
         }
          
          user.setPlatformId(Globals.PICO_PLATFORM_ID);
          user.setType(codeUtil.getBaseCodeByAlias("user"));
          user.setStatus(codeUtil.getBaseCodeByAlias("user-status-normal"));
          mapper.insertSelective(user);
          Map<String, Object> data = Maps.newHashMap();
          data.put("success",  true);
          data.put("barcode",  user.getBarcode());
          result.setData(data);
        }
      } else {
        if (registerTp.equals(REGISTER_TYPE_USER_UPDATE_EMAIL)) {
          if (param.containsKey("email") && String.valueOf(param.get("email")).length() > 0) {
            logger.debug("email 1: " + param.get("email"));
            String email = AES256Cipher.decodeAES256(String.valueOf(param.get("email")));
            logger.debug("email 2: " + email);
            example.createCriteria()
              .andStatusEqualTo(codeUtil.getBaseCodeByAlias("user-status-normal"))
              .andEmailEqualTo(email);
            List<User> users = mapper.selectByExample(example);
            if (users != null && users.size() > 0) {
              header.setErrCd(ErrorCodes.DATA_DUPLICATE.getCode());
              header.setErrMsg(messageSource.getMessage(ErrorCodes.DATA_DUPLICATE.getCode(),  new String[]{"email"}, locale));
              result.setData(null);
            } else {
              if (!GenericValidator.isEmail(email)) {
                header.setErrCd(ErrorCodes.INVALID_EMAIL.getCode());
                header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_EMAIL.getCode(),  new String[]{"email"}, locale));
                result.setData(null);
              } else {
                example = new UserExample();
                example.createCriteria().andIdEqualTo(user.getId());
                user.setEmail(AES256Cipher.encodeAES256(email));
                user.setPassword(null);
                mapper.updateByExampleSelective(user,  example);
                Map<String, Object> data = Maps.newHashMap();
                data.put("success", true);
                data.put("email",  user.getEmail());
                result.setData(data);
              }
            }
          } else {
            String[] errParam = {"email"};
            throw new InvalidParamException(errParam, locale);
          }
        } else if (registerTp.equals(REGISTER_TYPE_USER_UPDATE_NAME)) {
          if (param.containsKey("name") && String.valueOf(param.get("name")).length() > 0) {
            String name = String.valueOf(param.get("name"));
            example = new UserExample();
            example.createCriteria().andIdEqualTo(user.getId());
            user.setName(name);
            user.setPassword(null);
            mapper.updateByExampleSelective(user,  example);
            result.setData(null);
          } else {
            header.setErrCd(ErrorCodes.INVALID_PARAM.getCode());
            header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_PARAM.getCode(),  new String[]{"name"}, locale));
            result.setData(null);
          }
        } else {  // REGISTER_TYPE_USER_UPDATE_PASSWORD
          String nowPassword;
          String newPassword;
          if (param.containsKey("nowPassword") && String.valueOf(param.get("nowPassword")).length() > 0) {
            nowPassword = AES256Cipher.decodeAES256(String.valueOf(param.get("nowPassword")));
            
            if (param.containsKey("newPassword") && String.valueOf(param.get("newPassword")).length() > 0) {
              newPassword = AES256Cipher.decodeAES256(String.valueOf(param.get("newPassword")));
              
              example.createCriteria()
                .andIdEqualTo(user.getId())
                .andStatusEqualTo(codeUtil.getBaseCodeByAlias("user-status-normal"));
              List<User> users = mapper.selectByExample(example);
              
              if (users != null && users.size() > 0) {
                User u = users.get(0);
                
                if (bcryptEncoder.matches(nowPassword, u.getPassword())) {
                  u.setPassword(newPassword);
                  result.setData(null);
                  mapper.updateByExample(u, example);
                } else {
                  header.setErrCd(ErrorCodes.INVALID_VALUE.getCode());
                  header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_VALUE.getCode(),  new String[]{"nowPassword"}, locale));
                  result.setData(null);
                }
              }
            } else {
              header.setErrCd(ErrorCodes.INVALID_PARAM.getCode());
              header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_PARAM.getCode(),  new String[]{"newPassword"}, locale));
              result.setData(null);
            }

          } else {
            header.setErrCd(ErrorCodes.INVALID_PARAM.getCode());
            header.setErrMsg(messageSource.getMessage(ErrorCodes.INVALID_PARAM.getCode(),  new String[]{"nowPassword"}, locale));
            result.setData(null);
          }
        }
      }
    } else {
      String[] errParam = {"registerType"};
      throw new InvalidParamException(errParam, locale);
    }
    
    if (header.getErrCd() == null) {
      header.setErrCd(ErrorCodes.SUCCESS.getCode());  
    }
    result.setHeader(header);
    
    return result;
   } catch (Exception e) {
     e.printStackTrace();
     throw e;
   }
  }
  
  @Transactional
  @RequestMapping("/cert/phone/req")
  @ResponseBody
  public AppJsonResult certPhoneRequest(
      @RequestHeader("Service-Identifier") Long serviceId,
      @RequestParam(value = "params", defaultValue = "{}") String searchParams,
      HttpServletRequest request) throws Exception {
    logger.debug("user : " + JsonConvert.toJson(searchParams));
    Map<String, String> params = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(params.get("lang"));
    try {
      String mb = "";
      String mbCountryCd = "";
      if (params.containsKey("mb") && params.get("mb").length() > 0) {
        mb = params.get("mb");
      } else {
        String[] errParam = {"mb"};
        throw new InvalidParamException(errParam, locale);
      }
      
      if (params.containsKey("mbCountryCd") && params.get("mbCountryCd").length() > 0) {
        mbCountryCd = params.get("mbCountryCd");
      } else {
        String[] errParam = {"mbCountryCd"};
        throw new InvalidParamException(errParam, locale);
      }
      ServiceMapper serviceMapper = sessionTemplate.getMapper(ServiceMapper.class);
      Service service = serviceMapper.selectByPrimaryKey(serviceId);
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(Error.ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      
//      String certNo = certUtil.putCertNumber(mb);
//      String message = messageSource.getMessage("sms.cert", new String[] { certNo }, request.getLocale());
  
//      double random = Math.floor(Math.random() * 1000000)+100000;
//      if(random > 1000000){
//        random = random - 100000;
//      }
      // 개발서버 테스트 용
      double random = 111111;
      String authCode = String.valueOf((int) random);
      String msg = messageSource.getMessage("sms.cert", new String[]{authCode} ,locale);

      logger.debug("msg : " + msg);
      Date now = new Date();
      StringBuffer from = new StringBuffer();
      from.append(config.getString("sms.surem.reqphone1")); // 발신자 번호1
      from.append(config.getString("sms.surem.reqphone2")); // 발신자 번호2
      from.append(config.getString("sms.surem.reqphone3")); // 발신자 번호3
      SvcSmsLogMapper smsLogMapper = sessionTemplate.getMapper(SvcSmsLogMapper.class);
      SvcSmsLogWithBLOBs smsLog = new SvcSmsLogWithBLOBs();
      smsLog.setPathTp(codeUtil.getBaseCodeByAlias("bak-service-tp-mobile"));
      smsLog.setFrom(from.toString());
      smsLog.setTo(mb);
      smsLog.setContent(msg.toString());
      smsLog.setSuccess(false);
      smsLog.setCreated(now);
      smsLog.setUpdated(now);
      
      smsLogMapper.insertSelective(smsLog);

      SvcSmsAuthMapper smsAuthMapper = sessionTemplate.getMapper(SvcSmsAuthMapper.class);
      SvcSmsAuth smsAuth = new SvcSmsAuth();
      smsAuth.setSmsLogId(smsLog.getId());
      smsAuth.setMbCountryCd(mbCountryCd);
      smsAuth.setMb(mb);
      smsAuth.setAuthCode(authCode);
      smsAuth.setAuthSt(codeUtil.getBaseCodeByAlias("sms_auth_uncertified"));
      smsAuth.setCreated(now);
      smsAuth.setUpdated(now);
      
      smsAuthMapper.insertSelective(smsAuth);
      SMSUtil.sendIMS((int) random, mbCountryCd, StrUtils.toNumberOnly(mb), msg.toString(), StrUtils.toNumberOnly(mb));
//      SendSms info = new SendSms();
//      info.setFrom(service.getTel());
//      info.setTo(mb);
//      info.setContent(message);
//      info.setReplaceMap(Maps.<SendMessage.Token, String> newHashMap());
//      smsSender.send(info);   // TODO 동작 구현 하지 않음.
  
      result.setData(smsAuth.getId());
      return result;
    } catch (InvalidParamException invalidParam) {
      invalidParam.printStackTrace();
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String errParam= invalidParam.getParams()[0];
      logger.error("[certPhoneRequest] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam},locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(searchParams));
      throw invalidParam;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[certPhoneRequest] fail !!", e);
      throw e;
    }
  }
  
//  @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
//  @ResponseBody
//  public JsonResult register(@RequestHeader("Service-Identifier") Integer serviceId, @RequestBody User user,
//      BindingResult errors) throws Exception {
//    DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
//    transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
//    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
//
//    JsonResult result = new JsonResult();
//    String userTpAssociate = codeUtil.getBaseCodeByAlias("associate-user");
//    String userTpReular = codeUtil.getBaseCodeByAlias("user");
//    String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
//    String userLevelNormal = codeUtil.getBaseCodeByAlias("user-level-normal");
//
//    UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
//    CustomMapper customMapper = sessionTemplate.getMapper(CustomMapper.class);
//    String mb = certUtil.getPhoneNumber(user.getMb());
//
//    user.setMb(mb);
//    user.setGender(codeUtil.getBaseCodeByAlias(user.getGender()));
//
//    // check email duplicate
//    UserExample example = new UserExample();
//    example.createCriteria().andTypeIn(Lists.newArrayList(userTpAssociate, userTpReular)).andStatusEqualTo(statusNormal)
//        .andEmailEqualTo(user.getEmail());
//
//    int count = mapper.countByExample(example);
//    if (count > 0) {
//      throw new DataDuplicateException("email", user.getEmail());
//    }
//
//    // check validate
//    userValidator.validate(user, errors);
//
//    if (errors.hasErrors()) {
//      throw new InvalidParamException(errors);
//    }
//
//    // 준회원 존재 여부 확인
//    example = new UserExample();
//    example.createCriteria().andTypeEqualTo(userTpAssociate).andStatusEqualTo(statusNormal).andMbEqualTo(user.getMb());
//
//    User existsUser = null;
//    List<User> existsUsers = mapper.selectByExample(example);
//    if (existsUsers != null && existsUsers.size() > 0) {
//      if (existsUsers.size() == 1) {
//        existsUser = existsUsers.get(0);
//      } else {
//        throw new DataDuplicateException("mb", mb);
//      }
//    }
//
//    // 신규 등록 또는 준회원 -> 정회원 전환
//    BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
//    if (existsUser == null) {
//      user.setPlatformId(Globals.PICO_PLATFORM_ID);
//      user.setType(userTpReular);
//      user.setUsername(user.getEmail());
//      user.setCountry("KR");
//      user.setStatus(statusNormal);
//      user.setLevel(userLevelNormal);
//
//      int id = mapper.insertSelective(user);
//      // barcode 생성 후 update // TODO?
//      // user.setId(id);
//      user.setGrpCd(barcodeUtil.makeGroupCode(user.getMb()));
//      Integer maxMemCd = customMapper.getMaxMemCode(user.getGrpCd());
//      user.setMemCd((maxMemCd != null ? maxMemCd : 0) + 1);
//      user.setBarcode(barcodeUtil.makeUserCode(user.getGrpCd(), user.getMemCd()));
//      user.setPassword(bcrypt.encode(user.getBarcode()));
//      mapper.updateByPrimaryKeySelective(user);
//    } else {
//      user.setId(existsUser.getId());
//      user.setPlatformId(Globals.PICO_PLATFORM_ID);
//      user.setType(userTpReular);
//      // user.setBarcode(BarcodeUtil.makeUserCode(user.getMb(),
//      // existsUser.getId()));
//      // user.setPassword(bcrypt.encode(user.getBarcode()));
//      user.setUsername(user.getEmail());
//      user.setCountry("KR");
//      user.setStatus(statusNormal);
//      user.setLevel(userLevelNormal);
//      mapper.updateByPrimaryKeySelective(existsUser);
//
//      // stamp -> 쿠폰 전환
//      ServiceMapper serviceMapper = sessionTemplate.getMapper(ServiceMapper.class);
//      Service service = serviceMapper.selectByPrimaryKey(serviceId);
//      SvcUserStampMapper userStampMapper = sessionTemplate.getMapper(SvcUserStampMapper.class);
//      SvcUserStampExample userStampExample = new SvcUserStampExample();
//      userStampExample.createCriteria().andUserIdEqualTo(user.getId());
//      userStampExample.setOrderByClause("GROUP BY STORE_ID");
//      List<SvcUserStamp> stamps = userStampMapper.selectByExample(userStampExample);
//      for (int i = 0; (stamps != null && i < stamps.size()); i++) {
//        SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
//        SvcBrand brand = brandMapper.selectByPrimaryKey(stamps.get(i).getBrandId());
//        couponUtil.init(user, brand, null);
//        couponUtil.issue(null);
//      }
//    }
//
//    // commit
//    dataSourceTransactionManager.commit(transactionStatus);
//
//    // login
//    Map<String, Object> bean = Maps.newHashMap();
//    Map<String, String> oauth = ApiUtil.login("password", "mobile", user.getUsername(), user.getBarcode(),
//        user.getMb());
//    bean.put("oauth", oauth);
//    bean.put("user", user);
//    result.setSuccess(true);
//    result.setBean(bean);
//
//    certUtil.removeCertNumber(mb);
//
//    return result;
//  }
  
  @RequestMapping("/cert/phone/check")
  @ResponseBody
  public AppJsonResult certPhoneCheck(
      @RequestHeader("Service-identifier") Long serviceId,
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      String authCode = "";
      Long authKey;
      
      if (param.containsKey("authCode") && param.get("authCode").length() > 0) {
        authCode = param.get("authCode");
      } else {
        String[] errParam = {"authCode"};
        throw new InvalidParamException(errParam, locale);
      }
      
      if (param.containsKey("authKey") && param.get("authKey").length() > 0) {
        authKey = Long.parseLong(param.get("authKey"));
      } else {
        String[] errParam = {"authKey"};
        throw new InvalidParamException(errParam, locale);
      }
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      
      SvcSmsAuthMapper authMapper = sessionTemplate.getMapper(SvcSmsAuthMapper.class);
      SvcSmsAuthExample authExample = new SvcSmsAuthExample();
      authExample.createCriteria().andIdEqualTo(authKey);
      
      List<SvcSmsAuth> smsAuth = authMapper.selectByExample(authExample);
      
      if (smsAuth != null && smsAuth.size() > 0) {
        SvcSmsAuth auth = smsAuth.get(0);
        
        if (!auth.getAuthCode().equals(authCode)) {
          String[] errParam = {"Auth code"};
          throw new InvalidParamException(errParam, locale);
        }
      }
      
      result.setHeader(header);
      return result;
    } catch (InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String errParam= invalidParam.getParams()[0];
      logger.error("[certPhoneCheck] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{errParam}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[certPhoneCheck] fail !!", e);
      throw e;
    }
  }

//  @RequestMapping("/cert/phone/certified")
//  @ResponseBody
//  public JsonResult certPhoneCertified(@RequestHeader("Service-Identifier") Integer serviceId,
//      @RequestParam("token") String token, HttpServletRequest request) {
//
//    JsonResult result = new JsonResult();
//
//    // Cross domain 이슈로 session 사용 불가.
//    String mb = certUtil.getPhoneNumber(token);
//    if (mb != null && mb.length() > 0) {
//      result.setSuccess(true);
//      result.setBean(mb);
//    } else {
//      result.setSuccess(false);
//      // TODO 에러코드 정의 할 것
//      result.addError("001",
//          messageSource.getMessage("sms.certified.phone.number.not.exists", new String[] {}, locale));
//    }
//
//    return result;
//  }

  @RequestMapping("/cert/email/req")
  @ResponseBody
  @Transactional(rollbackFor = Exception.class)
  public JsonResult certMailRequest(@RequestHeader("Service-Identifier") Long serviceId,
      @RequestParam("email") String email) throws Exception {

    String userType = codeUtil.getBaseCodeByAlias("user");
    String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
    String messageTp = codeUtil.getBaseCodeByAlias("message-tp-mb-update");

    UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);

    ServiceMapper serviceMapper = sessionTemplate.getMapper(ServiceMapper.class);
    Service service = serviceMapper.selectByPrimaryKey(serviceId);

    SvcMailAccount account = getDefaultMailAccount();
    SvcMailTemplate template = getMailTemplate(serviceId, messageTp);

    if (account == null || template == null) {
      throw new SettingNotFoundException();
    }

    // TODO app user 코드 가져오기.
    UserExample example = new UserExample();
    example.createCriteria().andPlatformIdEqualTo(Globals.PICO_PLATFORM_ID).andTypeEqualTo(userType)
        .andStatusEqualTo(statusNormal).andEmailEqualTo(email);

    List<User> users = mapper.selectByExample(example);
    if (users == null || users.size() != 1) {
      throw new DataNotRegisteredException("email", email, null);
    }
    User user = users.get(0);

    String secToken = certUtil.encrypt(email + ":" + System.currentTimeMillis()); // throw
                                                                                  // TokenInvalidException

    // TODO replace values
    Map<SendEmail.Token, String> replaceMap = Maps.newHashMap();
    replaceMap.put(SendEmail.Token.SEC_TOKEN, secToken);
    replaceMap.put(SendEmail.Token.USER_EMAIL, user.getEmail());
    replaceMap.put(SendEmail.Token.USER_NAME, user.getName());

    SendEmail sendEmail = new SendEmail();
    sendEmail.setAccount(account);
    sendEmail.setTitle(template.getTitle());
    sendEmail.setContent(template.getContent());
    sendEmail.setReplaceMap(replaceMap);
    sendEmail.setContentType(codeUtil.getTitleByCode(template.getContentTp()));
    sendEmail.setFrom(service.getEmail());
    sendEmail.setTo(Lists.newArrayList(user.getEmail()));
    // List<String> cc = JsonConvert.JsonConvertObject(template.getCc(), new
    // TypeReference<List<String>>() {});
    // sendEmail.setCc(cc);
    // List<String> bcc = JsonConvert.JsonConvertObject(template.getBcc(), new
    // TypeReference<List<String>>() {});
    // sendEmail.setBcc(bcc);
//    mailSender.send(sendEmail);

    user.setSecToken(secToken);
    mapper.updateByPrimaryKey(user);

    JsonResult result = new JsonResult();

    result.setSuccess(true);

    return result;
  }

  @RequestMapping("/cert/email/check")
  @ResponseBody
  public JsonResult certMailCheck(@RequestHeader("Service-Identifier") Integer serviceId,
      @RequestParam("token") String token) throws Exception {
    JsonResult result = new JsonResult();

    String decrypted = certUtil.decrypt(token); // throw TokenInvalidException
    String email = null;
    String timestamp = null;
    if (decrypted != null && decrypted.contains(":")) {
      String[] split = decrypted.split(":");
      email = split[0];
      timestamp = split[1];
    }

    UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
    String userType = codeUtil.getBaseCodeByAlias("user"); // 정회원..
    UserExample example = new UserExample();
    example.createCriteria().andTypeEqualTo(userType).andSecTokenEqualTo(token).andEmailEqualTo(email);

    List<User> users = mapper.selectByExample(example);
    if (users == null || users.size() == 0) {
      throw new InvalidTokenException(token);
    }

    User user = users.get(0);

    // 최소 정보만 반환
    final Map<String, Object> data = Maps.newHashMap();
    data.put("id", user.getId());
    data.put("username", user.getUsername());
    data.put("barcode", user.getBarcode());
    data.put("name", user.getName());
    data.put("mb", user.getMb());

    result.setSuccess(true);
    result.setBean(data);

    return result;
  }

  @RequestMapping("/cert/email/change")
  @ResponseBody
  public JsonResult certMailChange(@RequestHeader("Service-Identifier") Integer serviceId,
      @RequestParam("mb") String mb, @RequestParam("token") String token) throws Exception {
    JsonResult result = new JsonResult();

    String userType = codeUtil.getBaseCodeByAlias("user");
    String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");

    String decrypted = certUtil.decrypt(token);
    String email = null;
    String timestamp = null;
    if (decrypted != null && decrypted.contains(":")) {
      String[] split = decrypted.split(":");
      email = split[0];
      timestamp = split[1];
    }

    // get user from email
    UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
    UserExample example = new UserExample();
    example.createCriteria().andTypeEqualTo(userType).andStatusEqualTo(statusNormal).andSecTokenEqualTo(token)
        .andEmailEqualTo(email);

    List<User> users = mapper.selectByExample(example);
    if (users == null || users.size() == 0) {
      throw new InvalidTokenException(token);
    }

    User user = users.get(0);

    // change phone number
    user.setMb(mb);
    user.setSecToken(""); // clear security token
    mapper.updateByPrimaryKey(user);

    result.setSuccess(true);
    result.setBean(user);

    return result;
  }

  @RequestMapping(value = "/device")
  @ResponseBody
  public AppJsonResult device(@RequestHeader("Service-Identifier") Integer serviceId,
      @RequestParam("params") String params,
      @RequestParam(value = "isAlive", defaultValue = "true") Boolean isAlive,
      HttpServletRequest request) throws Exception {
    
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    UserDeviceMapper mapper = sessionTemplate.getMapper(UserDeviceMapper.class);
    CustomMapper customMapper = sessionTemplate.getMapper(CustomMapper.class);
    UserDevice device = new UserDevice();
    Errors errors = makeErrors();
    try {
      String os = "";
      String uuid = "";
      
      if (param.containsKey("os")  && param.get("os").length() > 0) {
        os = param.get("os");
        // convert to code
        String osCode = codeUtil.getBaseCodeByAlias(os);
        if (!GenericValidator.isBlankOrNull(osCode)) {
          device.setOs(osCode);
        } else {
          device.setOs(""); // 에러 방지. 잘못된 값일 경우(6자 넘을경우)
          errors.rejectValue("os", ErrorCodes.CODE_NOT_FOUND.getCode(), new String[] { "os", os }, "");
          InvalidParamException ex = new InvalidParamException(errors);
          ex.setLocale(locale);
          throw ex;
        }
      } else {
        String[] errParam = {"os"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("uuid")  && param.get("uuid").length() > 0) {
        device.setUuid(param.get("uuid"));
      } else {
        String[] errParam = {"uuid"};
        throw new InvalidParamException(errParam, locale);       
      }
   
      device.setIsAlive(isAlive);
  
      // make device id
      ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
      String seed = String.format("%s-%s", device.getOs(), device.getUuid());
  
      int nextIdx = customMapper.getNextIndex("TB_USER_DEVICE");
      String deviceId = encoder.encodePassword(seed, String.valueOf(nextIdx));
  
      // insert
      device.setDeviceId(deviceId);
      device.setIsLastLogin(true);
      mapper.insertSelective(device);
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(device);
      
      return result;
    } catch (InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , serviceId : " + serviceId + ", " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[app device] fail !!", e);
      throw e;
    }
  }

  private Locale getLocale(String language) throws Exception {
    Locale locale = null;
    // 기본 값 영어
    if (language == null || language.length() == 0) { 
      locale = Locale.US;
    } else {
      language = language.toLowerCase();
      if (language.equals(Locale.US.toString().toLowerCase())) {
        locale = Locale.US;
      } else if (language.equals((new Locale("ru", "RU").toString().toLowerCase()))) {
        locale = new Locale("ru", "RU");
      } else {
        locale = Locale.US;
      }
    }
    
    return locale;
  }
  
  private Map<String, Object> convertCamelcase(Map<String, Object> map) {
    if (map == null || map.size() == 0) {
      return null;
    }

    Map<String, Object> result = Maps.newHashMap();
    Set<String> keys = map.keySet();
    Iterator<String> itr = keys.iterator();
    while (itr.hasNext()) {
      String key = itr.next();
      result.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), map.get(key));
    }

    return result;
  }
  
  // app 쪽에서 암호화된 유저 정보 복호화
  private User decodeUser(User user) throws Exception{
    if (!GenericValidator.isBlankOrNull(user.getPassword())) {
      user.setPassword(AES256Cipher.decodeAES256(user.getPassword()));
    }
    
    if (!GenericValidator.isBlankOrNull(user.getEmail())) {
      user.setEmail(AES256Cipher.decodeAES256(user.getEmail()));
    }
    
    if (!GenericValidator.isBlankOrNull(user.getMb())) {
      user.setMb(AES256Cipher.decodeAES256(user.getMb()));
    }
    
    return user;
  }
}
