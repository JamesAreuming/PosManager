/*
 * Filename	: ModelController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.app;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.BaseBcode;
import com.jc.pico.bean.Notice;
import com.jc.pico.bean.NoticeExample;
import com.jc.pico.bean.SvcBeacon;
import com.jc.pico.bean.SvcBeaconExample;
import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcBrandSet;
import com.jc.pico.bean.SvcBrandSetExample;
import com.jc.pico.bean.SvcCctvLog;
import com.jc.pico.bean.SvcCctvLogExample;
import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcClosingExample;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcItemImgExample;
import com.jc.pico.bean.SvcItemOpt;
import com.jc.pico.bean.SvcItemOptDtl;
import com.jc.pico.bean.SvcItemOptDtlExample;
import com.jc.pico.bean.SvcItemOptExample;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderExample;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcOrderItemExample;
import com.jc.pico.bean.SvcOrderItemOpt;
import com.jc.pico.bean.SvcOrderItemOptExample;
import com.jc.pico.bean.SvcOrderPay;
import com.jc.pico.bean.SvcOrderPayExample;
import com.jc.pico.bean.SvcPluCat;
import com.jc.pico.bean.SvcPluCatExample;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcStoreBeacon;
import com.jc.pico.bean.SvcStoreBeaconExample;
import com.jc.pico.bean.SvcStoreBeaconLog;
import com.jc.pico.bean.SvcStoreBeaconLogExample;
import com.jc.pico.bean.SvcStoreExample;
import com.jc.pico.bean.SvcStoreImg;
import com.jc.pico.bean.SvcStoreImgExample;
import com.jc.pico.bean.SvcStoreReview;
import com.jc.pico.bean.SvcStoreSet;
import com.jc.pico.bean.SvcStoreSetExample;
import com.jc.pico.bean.SvcStoreSetWithBLOBs;
import com.jc.pico.bean.SvcStoreUser;
import com.jc.pico.bean.SvcStoreUserExample;
import com.jc.pico.bean.SvcTerms;
import com.jc.pico.bean.SvcUserCouponExample;
import com.jc.pico.bean.SvcUserMapping;
import com.jc.pico.bean.SvcUserMappingExample;
import com.jc.pico.bean.SvcUserStamp;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserBookmark;
import com.jc.pico.bean.UserBookmarkExample;
import com.jc.pico.bean.UserCard;
import com.jc.pico.bean.UserCardExample;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserDeviceExample;
import com.jc.pico.bean.UserExample;
import com.jc.pico.bean.UserNoti;
import com.jc.pico.bean.UserNotiExample;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.CodeNotFoundException;
import com.jc.pico.exception.DataDuplicateException;
import com.jc.pico.exception.DataNotFoundException;
import com.jc.pico.exception.DataNotRegisteredException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.InvalidValueException;
import com.jc.pico.exception.NoPermissionException;
import com.jc.pico.ext.pg.PGHandler;
import com.jc.pico.ext.pg.PayBean;
import com.jc.pico.mapper.NoticeMapper;
import com.jc.pico.mapper.SvcBeaconMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcBrandSetMapper;
import com.jc.pico.mapper.SvcCctvLogMapper;
import com.jc.pico.mapper.SvcClosingMapper;
import com.jc.pico.mapper.SvcItemImgMapper;
import com.jc.pico.mapper.SvcItemOptDtlMapper;
import com.jc.pico.mapper.SvcItemOptMapper;
import com.jc.pico.mapper.SvcOrderItemMapper;
import com.jc.pico.mapper.SvcOrderItemOptMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.mapper.SvcOrderPayMapper;
import com.jc.pico.mapper.SvcPluCatMapper;
import com.jc.pico.mapper.SvcStoreBeaconLogMapper;
import com.jc.pico.mapper.SvcStoreBeaconMapper;
import com.jc.pico.mapper.SvcStoreImgMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcStoreReviewMapper;
import com.jc.pico.mapper.SvcStoreSetMapper;
import com.jc.pico.mapper.SvcStoreUserMapper;
import com.jc.pico.mapper.SvcTermsMapper;
import com.jc.pico.mapper.SvcUserCouponMapper;
import com.jc.pico.mapper.SvcUserMappingMapper;
import com.jc.pico.mapper.UserBookmarkMapper;
import com.jc.pico.mapper.UserCardMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.mapper.UserNotiMapper;
import com.jc.pico.module.CouponProcessor;
import com.jc.pico.queue.PushSender;
import com.jc.pico.queue.SmsSender;
import com.jc.pico.service.app.AppNotificationService;
import com.jc.pico.service.store.StoreNotificationService;
import com.jc.pico.utils.AES256Cipher;
import com.jc.pico.utils.ApiUtil;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.Config;
import com.jc.pico.utils.CustomMapper;
import com.jc.pico.utils.DateUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.MQTTClient;
import com.jc.pico.utils.MaskUtil;
import com.jc.pico.utils.PagingUtil;
import com.jc.pico.utils.bean.AppJsonResult;
import com.jc.pico.utils.bean.Beacon;
import com.jc.pico.utils.bean.Error;
import com.jc.pico.utils.bean.Error.ErrorCodes;
import com.jc.pico.utils.bean.Header;
import com.jc.pico.utils.bean.JsonResult;
import com.jc.pico.utils.bean.SendPush;
import com.jc.pico.utils.mybatis.typehandler.MobileSecurityTypeHandler;

@Controller
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping(value = "/app/model", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModelController {
  protected static Logger logger = LoggerFactory.getLogger(ModelController.class);
  
  // json data format
  public static final SimpleDateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private Config config = Config.getInstance();
  
  @Autowired
  MessageSource messageSource;
  @Autowired
  SqlSessionTemplate sessionTemplate;
  @Autowired
  SvcTermsMapper svcTermsMapper;
  @Autowired
  CodeUtil codeUtil;
  @Autowired
  TokenStore tokenStore;
  @Autowired
  PushSender pushSender;
  @Autowired
  SmsSender smsSender;
  @Autowired
  CouponProcessor couponProcessor;
  @Autowired
   AppNotificationService appNotificationService;
  
  @Autowired
  StoreNotificationService storeNotificationService;

  private RowBounds getRowBounds(int pageNo, int rowsPerPage) {
    logger.debug("getRowBounds - " + pageNo + ", " + rowsPerPage);
    return new RowBounds(Math.abs(((pageNo - 1) * rowsPerPage)), rowsPerPage);
  }

  private User getCurrentUser(String idAttribute, Locale locale) throws DataNotRegisteredException, NoPermissionException {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      logger.debug("auth : " + JsonConvert.toJson(authentication));
//      String username = (authentication != null ? AES256Cipher.decodeAES256(authentication.getName()) : null);
      String username = (authentication != null ? authentication.getName() : null);
      String userType = codeUtil.getBaseCodeByAlias("user");
      String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
      // default where...
      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      UserExample example = new UserExample();
      example.createCriteria()
          .andTypeEqualTo(userType)
          .andStatusEqualTo(statusNormal)
          .andUsernameEqualTo(username);
  
      List<User> users = mapper.selectByExample(example);
  
      if (users != null && users.size() == 1) {
        User user = users.get(0);
        if ("me".equals(idAttribute)
            || GenericValidator.isBlankOrNull(idAttribute)
            || idAttribute.equals(String.valueOf(user.getId()))) {
          return user;
        } else {
          throw new NoPermissionException();
        }
      } else {
        throw new DataNotRegisteredException("user", idAttribute, locale);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new DataNotRegisteredException("user", idAttribute, locale);
    }
  }

  private Errors makeErrors() {
    return new MapBindingResult(Maps.newHashMap(), this.getClass().getName());
  }

  private SimpleDateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd");
  }

  private SimpleDateFormat getDateTimeFormat() {
    return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
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

  private void revokeAccessToken(HttpServletRequest request) {
    // logout(revoke token)
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null) {
      String tokenValue = authHeader.toLowerCase().replace("bearer", "").trim();
      OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
      tokenStore.removeAccessToken(accessToken);
    }
  }

  @RequestMapping(value = "/device", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
  @ResponseBody
  public AppJsonResult device(@RequestBody UserDevice device,
      HttpServletRequest request) throws Exception {
    try {
      User user = getCurrentUser("me", null);
      
      UserDeviceMapper mapper = sessionTemplate.getMapper(UserDeviceMapper.class);
      UserDeviceExample example = new UserDeviceExample();
      example.createCriteria()
          .andDeviceIdEqualTo(device.getDeviceId());
  
      logger.debug("UUID : " + device.getUuid());
      // 기존 push id 중복 제거
      if (!GenericValidator.isBlankOrNull(device.getUuid())) {
        UserDeviceExample pushExample = new UserDeviceExample();
        pushExample.createCriteria()
            .andUuidEqualTo(device.getUuid());
        UserDevice record = new UserDevice();
        record.setIsAlive(false);
        record.setPushId("");
        mapper.updateByExampleSelective(record, pushExample);
      }
  
      List<UserDevice> devices = mapper.selectByExample(example);
      if (devices != null && devices.size() > 0) {
        
        // update
        device.setId(devices.get(0).getId());
        device.setIsAlive(true);
        device.setUserId(user.getId());
        device.setIsLastLogin(true);
        mapper.updateByPrimaryKeySelective(device);
        
      } else {
        throw new DataNotRegisteredException("deviceId", device.getDeviceId(), null);
      }
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(device);
      return result;
    } catch (DataNotRegisteredException notData) {
      String code = Error.ErrorCodes.DATA_NOT_REGISTERED.getCode();
      String err = notData.getValue();
      logger.error("[model device] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, request.getLocale()) + "]" +" , device :  " + JsonConvert.toJson(device));
      throw notData;
    } catch (Exception e) {
      logger.error("[model device] fail !!", e);
      throw e;
    }
  }

  @RequestMapping(value = "/summary", method = RequestMethod.GET)
  @ResponseBody
  public JsonResult summary() throws Exception {
    User user = getCurrentUser("me", null);

    Map<String, Object> datas = Maps.newHashMap();

    Date now = new Date();
    NoticeMapper noticeMapper = sessionTemplate.getMapper(NoticeMapper.class);
    NoticeExample noticeWhere = new NoticeExample();
    NoticeExample.Criteria criteria = noticeWhere.createCriteria();
    //criteria.andNoticeTpEqualTo(noticeType);
    criteria.andPlatformIdEqualTo(Globals.PICO_PLATFORM_ID);
    criteria.andOpenLessThanOrEqualTo(now);
    criteria.andCloseGreaterThanOrEqualTo(now);
    criteria.andCloseIsNotNull();
    noticeWhere.setOrderByClause("`OPEN` DESC");

    List<Notice> notices = noticeMapper.selectByExampleWithRowbounds(noticeWhere, getRowBounds(1, 1));

    if (notices != null && notices.size() > 0) {
      datas.put("lastNotice", notices.get(0));
    }

    SvcUserCouponMapper couponMapper = sessionTemplate.getMapper(SvcUserCouponMapper.class);
    SvcUserCouponExample couponWhere = new SvcUserCouponExample();
    couponWhere.createCriteria()
        .andUserIdEqualTo(user.getId())
        .andUsedEqualTo(false)
        .andExpireGreaterThan(now);

    int couponCnt = couponMapper.countByExample(couponWhere);

    datas.put("unusedCoupon", couponCnt);

    JsonResult result = new JsonResult();
    result.setSuccess(true);
    result.setBean(datas);
    return result;
  }

  @RequestMapping(value = "/region", method = RequestMethod.GET)
  @ResponseBody
  public JsonResult region() {
    List<BaseBcode> codes = codeUtil.getByMainCode("113"); // 113 == 지역
    List<Map<String, String>> datas = Lists.newArrayList();
    for (BaseBcode code : codes) {
      HashMap<String, String> map = Maps.newHashMap();
      map.put("title", code.getTitle());
      map.put("value", code.getAlias());
      datas.add(map);
    }

    JsonResult result = new JsonResult();
    result.setSuccess(true);
    List<Object> items = Lists.newArrayList();
    items.addAll(datas);
    Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, datas.size(), null);
    result.setBean(obj);
    return result;
  }
  
  @RequestMapping(value = "/set/service/terms")
  @ResponseBody
  public AppJsonResult setServicetTerm(
      @RequestParam(value = "params", defaultValue = "{}") String params,
      HttpServletRequest request) throws Exception {
    try {
      Map<String, Object> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, Object>>() {});
      Locale locale = getLocale(String.valueOf(param.get("lang")));
      User user = getCurrentUser("me", locale);
      
      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      List<SvcTerms> terms = JsonConvert.JsonConvertObject(JsonConvert.toJson(param.get("terms")),  new TypeReference<List<SvcTerms>>(){});
      if (terms != null && terms.size() > 0) {
        String termsSvcTpBakApp = codeUtil.getBaseCodeByAlias("terms-service-tp-bak-app");
        String termsTpSMS = codeUtil.getBaseCodeByAlias("term-tp-sms");
        String termsTpPush = codeUtil.getBaseCodeByAlias("term-tp-push");
        String termSvcTp = (String) param.get("termSvcTp");
        boolean isAllTrue = (Boolean) param.get("isAllTrue");
        
        logger.debug("param : " + JsonConvert.toJson(param));
        if (isAllTrue) {
          if (termSvcTp.equals(termsSvcTpBakApp)) {  // 약관 서비스 유형이 BAK_app일 때
            user.setIsSvcSms(true);
            user.setIsSvcPush(true);
            user.setIsSvcBluetooth(true); 
          }
        } else {
          for (SvcTerms term : terms) {
            if (termSvcTp.equals(termsSvcTpBakApp)) {   // 약관 서비스 유형이 BAK_app일 때
              if (termsTpSMS.equals(term.getTermsTp())) {
                user.setIsSvcSms(!user.getIsSvcSms());
              } 
              
              if (termsTpPush.equals(term.getTermsTp())) {
                user.setIsSvcPush(!user.getIsSvcPush());
              }
              
              if ("bluetooth".equals(term.getTitle())) { // 
                user.setIsSvcBluetooth(!user.getIsSvcBluetooth());
              }
            }
          }
        }
      }
      
      user.setPassword(null);
      mapper.updateByPrimaryKeySelective(user);
      
      logger.debug("user : " + JsonConvert.toJson(user));
      Map<String, Boolean> data = Maps.newHashMap();
      data.put("sms",  user.getIsSvcSms());
      data.put("push",  user.getIsSvcPush());
      data.put("bluetooth",  user.getIsSvcBluetooth());
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(data);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
 @RequestMapping("/user/set/locale")
 @ResponseBody
 public AppJsonResult setUserLocale(
     @RequestParam(value = "params", defaultValue = "{}") String params,
     HttpServletRequest request) throws Exception {
   Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
   Locale locale = getLocale(param.get("lang"));
   try {
     AppJsonResult result = new AppJsonResult();
     Header header = new Header();
     header.setErrCd(ErrorCodes.SUCCESS.getCode());
     User user = getCurrentUser("me",  locale);
     UserDeviceMapper mapper = sessionTemplate.getMapper(UserDeviceMapper.class);
     UserDeviceExample example = new UserDeviceExample();
     
     example.createCriteria()
        .andUserIdEqualTo(user.getId())
        .andUuidEqualTo(param.get("uuid"))
        .andIsAliveEqualTo(true);
     
     List<UserDevice> users = mapper.selectByExample(example);
     
     if (users != null && users.size() > 0) {
       UserDevice userDevice = users.get(0);
       userDevice.setLocale(locale.toString());
       example = new UserDeviceExample();
       example.createCriteria().andIdEqualTo(userDevice.getId());
       mapper.updateByExample(userDevice,  example);
     }
     result.setData(locale);
     result.setHeader(header);
     return result;
   } catch (Exception e) {
     e.printStackTrace();
     logger.error("[setUserLocale] " + ", param : " + JsonConvert.toJson(param));
     throw e;
   }
 }
  
  @RequestMapping("/withdraw")
  @ResponseBody
  public AppJsonResult withdraw(
      @RequestParam(value = "params", defaultValue = "{}") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      String withdraw = codeUtil.getBaseCodeByAlias("user-status-withdraw");
      
      if (!param.containsKey("id") && param.get("id").length() == 0) {
        String[] errParam = {"userId"};
        throw new InvalidParamException(errParam, locale);
      }

      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      UserExample example = new UserExample();
      example.createCriteria().andIdEqualTo(Long.valueOf(param.get("id")));
      
      List<User> users = mapper.selectByExample(example);
      
      if (users != null && users.size() > 0) {
        User u = users.get(0);
        // username, name, email, mbCountryCd, mb, mbBk, TelCountryCd, FaxCountryCd, country
        // user card delete
        u.setStatus(withdraw);
        u.setUsername("");
        u.setEmail("");
        u.setMbCountryCd("");
        u.setMb("");
        u.setMbBk("");
        u.setTelCountryCd("");
        u.setFaxCountryCd("");
        u.setCountry("");
        u.setUpdated(new Date());
        
        UserCardMapper cardMapper = sessionTemplate.getMapper(UserCardMapper.class);
        UserCardExample cardExample = new UserCardExample();
        
        cardExample.createCriteria().andUserIdEqualTo(u.getId());
        cardMapper.deleteByExample(cardExample);
        
        mapper.updateByExampleSelective(u,  example);
      }
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(true);
      
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[withdraw] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[withdraw] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/notice")
  @ResponseBody
  public AppJsonResult notice(
      @RequestParam(value = "params", defaultValue = "{}") String searchParams,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    Map<String, String> params = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {});
    logger.debug("param : " + JsonConvert.toJson(params));
    Locale locale = getLocale(params.get("lang"));
    try {
      Map<String, Object> mapperParam = Maps.newHashMap();
      
      if (params.containsKey("noticeTp")) {
        mapperParam.put("noticeTp", params.get("noticeTp"));
      }
      if (params.containsKey("noticeId")) {
        mapperParam.put("noticeId", params.get("noticeId"));
      }
      
      // limits
      if (rowsPerPage != 0) {
        mapperParam.put("offset", pageNo);
        mapperParam.put("limit", rowsPerPage);
      }

      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      List<Map<String, Object>> datas = mapper.getNotice(mapperParam);
      int totalCnt = mapper.getNoticeCount(mapperParam);
  
      List<Object> items = Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> notice  = datas.get(i);
        Map<String, Object> item = convertCamelcase(notice);
        items.add(item);
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, params);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      
      return result;
    } catch (Exception e) {
      logger.error("[notice] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/history/order/detail")
  @ResponseBody
  public AppJsonResult historyOrderDetail(
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      Map<String, Object> mapperParams = Maps.newHashMap();
      
      Long orderId;
      if (param.containsKey("orderId") && param.get("orderId").length() > 0) {
        mapperParams.put("orderId",  param.get("orderId"));
        orderId = Long.valueOf(param.get("orderId"));
      } else {
        String[] errParam = {"orderId"};
        throw new InvalidParamException(errParam, locale);
      }
      
      SvcOrderItemMapper orderItemMapper = sessionTemplate.getMapper(SvcOrderItemMapper.class);
      SvcOrderItemExample orderItemExample = new SvcOrderItemExample();
      
      SvcItemOptMapper itemOptMapper = sessionTemplate.getMapper(SvcItemOptMapper.class);
      SvcItemOptExample itemOptExample = new SvcItemOptExample();
      
      SvcItemOptDtlMapper itemOptDtlMapper = sessionTemplate.getMapper(SvcItemOptDtlMapper.class);
      SvcItemOptDtlExample itemOptDtlExample = new SvcItemOptDtlExample();
      
      SvcItemImgMapper imgMapper = sessionTemplate.getMapper(SvcItemImgMapper.class);
      SvcItemImgExample imgExample;
      
      SvcOrderItemOptMapper optMapper = sessionTemplate.getMapper(SvcOrderItemOptMapper.class);
      
      orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
      List<SvcOrderItem> datas = orderItemMapper.selectByExample(orderItemExample);
      List<Map<String, Object>> orderItems = Lists.newArrayList();
      
      logger.debug("item : size : " + datas.size());
      logger.debug("item : data : " + JsonConvert.toJson(datas));
      if (datas != null && datas.size() > 0) {
        for (int i = 0; i < datas.size(); i++) {
          Map<String, Object> orderItem = Maps.newHashMap();
          SvcOrderItem item = datas.get(i);
          orderItem.put("orderItem", item);
          logger.debug("item : " + JsonConvert.toJson(item));
          /* 주문 아이템 이미지 정보 */
          imgExample = new SvcItemImgExample();
         imgExample.createCriteria().andItemIdEqualTo(item.getItemId());
         List<SvcItemImg> imgs = imgMapper.selectByExample(imgExample);
         orderItem.put("itemImage", imgs.get(0));
          
          /* 주문 아이템 옵션 정보 */
          List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
          List<String> optionId = Lists.newArrayList();
          if (item.getOptId().contains(":")) {
            optionId = Arrays.asList(item.getOptId().split(":"));
          } else {
            if (!GenericValidator.isBlankOrNull(item.getOptId())) {
              optionId.add(item.getOptId());
            }
          }
          for (int k = 0; k < optionId.size(); k++) {
//            itemOptExample = new SvcItemOptExample();
//            itemOptExample.createCriteria().andIdEqualTo(Long.valueOf(optionId.get(k)));
//            List<SvcItemOpt> opts = itemOptMapper.selectByExample(itemOptExample);
            Map<String, Object> opt = Maps.newHashMap();
            opt.put("id", optionId.get(k));
            
            List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
            
            // 주문 아이텝 옵션 상세 정보
            SvcOrderItemOptExample optExample = new SvcOrderItemOptExample();
            optExample.createCriteria()
              .andOrderIdEqualTo(orderId)
              .andOptIdEqualTo(Long.parseLong(optionId.get(k)));
            List<SvcOrderItemOpt> opts = optMapper.selectByExample(optExample);
            
            if (opts != null && opts.size() > 0) {
              for (int j = 0; j < opts.size(); j++) {
                Map<String, Object> detail = Maps.newHashMap();
                SvcOrderItemOpt itemOpt = opts.get(j);
                detail.put("id",  itemOpt.getOptDtlId());
                detail.put("name",itemOpt.getOptDtlNm());
                detail.put("price", itemOpt.getOptPrice());
                details.add(detail);
              }
              opt.put("selectItemOptionDetail", details);
            }
            options.add(opt);
          }
          orderItem.put("selectItemOptions", options);
          orderItems.add(orderItem);
        }
      }
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(orderItems);
      
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[historyOrderDetail] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[historyOrderDetail] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/history/order")
  @ResponseBody
  public AppJsonResult historyOrder(
      @RequestParam("params") String params,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    logger.debug("param : " + JsonConvert.toJson(param));
    Locale locale = getLocale(param.get("lang"));
    try {
      User user = getCurrentUser("me", locale);
      Map<String, Object> mapperParams = Maps.newHashMap();
      
      if (param.containsKey("orderTp") && param.get("orderTp").length() > 0) {
        mapperParams.put("orderTp",  param.get("orderTp"));
      } 
      
      if (param.containsKey("orderId") && param.get("orderId").length() > 0) {
        mapperParams.put("orderId",  param.get("orderId"));
      }
      
      // limits
      mapperParams.put("offset", pageNo);
      mapperParams.put("limit", rowsPerPage);
      
      mapperParams.put("userId", user.getId());
      
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      SvcOrderPayMapper payMapper = sessionTemplate.getMapper(SvcOrderPayMapper.class);
      List<Map<String, Object>> datas = null;
      int totalCnt = 0;
      List<Object> items = Lists.newArrayList();
      
      datas = mapper.getOrderHistory(mapperParams);
      totalCnt = mapper.getOrderHistoryCount(mapperParams);
      List<Long> orderIds =Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> history  = datas.get(i);
        Map<String, Object> item = convertCamelcase(history);
        
//        item.put("orderTm", item.get("orderTmLocal"));
//        item.put("completeTm", item.get("completeTmLocal"));
//        item.put("acceptTm", item.get("acceptTmLocal"));
//        item.put("rejectTm", item.get("rejectTmLocal"));
//        item.put("cancelTm", item.get("cancelTmLocal"));
//        item.put("reserveTm", item.get("reserveTmLocal"));
//        item.put("reserveEndTm", item.get("reserveEndTmLocal"));
        
        Long orderId = ((BigInteger) item.get("id")).longValue();
        SvcOrderPayExample example = new SvcOrderPayExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<SvcOrderPay> pay = payMapper.selectByExample(example);
        if (pay != null && pay.size() > 0) {
          item.put("cardNo", pay.get(0).getCardNo());
        }
        
        if (orderIds.contains(orderId)) {
          // skip
        } else {
          orderIds.add(orderId);
          items.add(item);
        }
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, param);
     
      logger.debug("historyOrder : " + JsonConvert.toJson(obj));
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[historyOrder] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/store/coupon/history")
  @ResponseBody
  public AppJsonResult couponHistory(
      @RequestParam("params") String params,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      Map<String, Object> mapperParam = Maps.newHashMap();
      User user = getCurrentUser("me", locale);
      mapperParam.put("userId", user.getId());
      
      if (param.containsKey("brandId")  && param.get("brandId").length() > 0) {
        mapperParam.put("brandId", param.get("brandId"));
      } else {
        String[] errParam = {"brandId"};
        throw new InvalidParamException(errParam, locale);       
      } 
       
      if (param.containsKey("storeId")  && param.get("storeId").length() > 0) {
        mapperParam.put("storeId", param.get("storeId"));
      } else {
        String[] errParam = {"storeId"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("start")  && param.get("start").length() > 0) {
        SimpleDateFormat sdf = getDateTimeFormat();
        Date now = sdf.parse(param.get("start"));
        mapperParam.put("start", now);
      } else {
        String[] errParam = {"start"};
        throw new InvalidParamException(errParam, locale);       
      } 
      
      if (param.containsKey("end")  && param.get("end").length() > 0) {
        SimpleDateFormat sdf = getDateTimeFormat();
        Date now = sdf.parse(param.get("end"));
        mapperParam.put("end", now);
      } else {
        String[] errParam = {"end"};
        throw new InvalidParamException(errParam, locale);       
      } 
      
      // limits
      mapperParam.put("offset", pageNo);
      mapperParam.put("limit", rowsPerPage);
      
      List<Map<String, Object>> datas = mapper.getHistoryCoupon(mapperParam);
      int totalCnt = mapper.getHistoryCouponCount(mapperParam);
  
      List<Object> items = Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> stamp  = datas.get(i);
        Map<String, Object> item = convertCamelcase(stamp);
        items.add(item);
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, param);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[couponHistory] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[couponHistory] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/store/stamp/history")
  @ResponseBody
  public AppJsonResult stampHistory(
      @RequestParam("params") String params,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      Map<String, Object> mapperParam = Maps.newHashMap();
      User user = getCurrentUser("me", locale);
      mapperParam.put("userId", user.getId());
      
      if (param.containsKey("brandId")  && param.get("brandId").length() > 0) {
        mapperParam.put("brandId", param.get("brandId"));
      } else {
        String[] errParam = {"brandId"};
        throw new InvalidParamException(errParam, locale);       
      } 
       
      if (param.containsKey("storeId")  && param.get("storeId").length() > 0) {
        mapperParam.put("storeId", param.get("storeId"));
      } else {
        String[] errParam = {"storeId"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("start")  && param.get("start").length() > 0) {
        SimpleDateFormat sdf = getDateTimeFormat();
        Date now = sdf.parse(param.get("start"));
        mapperParam.put("start", now);
      } else {
        String[] errParam = {"start"};
        throw new InvalidParamException(errParam, locale);       
      } 
      
      if (param.containsKey("end")  && param.get("end").length() > 0) {
        SimpleDateFormat sdf = getDateTimeFormat();
        Date now = sdf.parse(param.get("end"));
        mapperParam.put("end", now);
      } else {
        String[] errParam = {"end"};
        throw new InvalidParamException(errParam, locale);       
      } 
      
      
      // limits
      mapperParam.put("offset", pageNo);
      mapperParam.put("limit", rowsPerPage);
      
      List<Map<String, Object>> datas = mapper.getHistoryStamp(mapperParam);
      int totalCnt = mapper.getHistoryStampCount(mapperParam);
  
      List<Object> items = Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> stamp  = datas.get(i);
        Map<String, Object> item = convertCamelcase(stamp);
        items.add(item);
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, param);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[stampHistory] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[stampHistory] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/store/stamp")
  @ResponseBody
  public AppJsonResult storeStamp(
      @RequestParam("params") String params,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      Map<String, Object> mapperParam = Maps.newHashMap();
      
      if (param.containsKey("userId")  && param.get("userId").length() > 0) {
        mapperParam.put("userId", param.get("userId"));
      } else {
        String[] errParam = {"userId"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("barcode")  && param.get("barcode").length() > 0) {
        mapperParam.put("barcode", param.get("barcode"));
      } else {
        String[] errParam = {"barcode"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("storeId")  && param.get("storeId").length() > 0) {
        mapperParam.put("storeId", param.get("storeId"));
      }
      
      if (param.containsKey("isHistory")  && param.get("isHistory").length() > 0) {
        mapperParam.put("isHistory", param.get("isHistory"));
      }
      
      if (rowsPerPage != 0) {
        // limits
        mapperParam.put("offset", pageNo);
        mapperParam.put("limit", rowsPerPage);
      }
      
      List<Map<String, Object>> datas = mapper.getStoreStampList(mapperParam);
      int totalCnt = mapper.getStoreStampCount(mapperParam);
  
      List<Object> items = Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> stamp  = datas.get(i);
        Map<String, Object> item = convertCamelcase(stamp);
        SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
        Long brandId = ((BigInteger) item.get("brandId")).longValue();
        Long storeId = ((BigInteger) item.get("storeId")).longValue();
        SvcBrand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand.getAllowStoreSet()) {
          SvcStoreSetMapper setMapper = sessionTemplate.getMapper(SvcStoreSetMapper.class);
          SvcStoreSetExample example = new SvcStoreSetExample();
          example.createCriteria()
              .andBrandIdEqualTo(brandId)
              .andStoreIdEqualTo(storeId);

          SvcStoreSet setting = setMapper.selectByExample(example).get(0);
          item.put("stampCnt", setting.getStampCnt());
        } else {
          SvcBrandSetMapper setMapper = sessionTemplate.getMapper(SvcBrandSetMapper.class);
          SvcBrandSetExample example = new SvcBrandSetExample();
          example.createCriteria()
              .andBrandIdEqualTo(brand.getId());

          SvcBrandSet setting = setMapper.selectByExample(example).get(0);
          item.put("stampCnt", setting.getStampCnt());
        }
        item.put("logoImg", brand.getLogoImg());
        
        items.add(item);
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, param);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[storeStamp] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[storeStamp] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/store/set/bookmark")
  @ResponseBody
  public AppJsonResult setBookmark(
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      Long brandId;
      Long storeId;
      int bookmark;
      
      if (param.containsKey("brandId")  && param.get("brandId").length() > 0) {
        brandId = Long.parseLong(param.get("brandId"));
      } else {
        String[] errParam = {"brandId"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("storeId")  && param.get("storeId").length() > 0) {
        storeId = Long.parseLong(param.get("storeId"));
      } else {
        String[] errParam = {"storeId"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("bookmark")) {
        bookmark = Integer.parseInt(param.get("bookmark"));
      } else {
        String[] errParam = {"bookmark"};
        throw new InvalidParamException(errParam, locale);       
      }
      User user = getCurrentUser("me", locale);
      UserBookmarkMapper mapper = sessionTemplate.getMapper(UserBookmarkMapper.class);
      UserBookmark bm = new UserBookmark();
  
      if (bookmark == 1) {
        bm.setUserId(user.getId());
        bm.setBrandId(brandId);
        bm.setStoreId(storeId);
        mapper.insertSelective(bm);
      } else {
        UserBookmarkExample example = new UserBookmarkExample();
        example.createCriteria().andStoreIdEqualTo(storeId).andUserIdEqualTo(user.getId());
        mapper.deleteByExample(example);
      }
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(bookmark);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[setBookmark] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[setBookmark] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/store/delete/review")
  @ResponseBody
  public AppJsonResult deleteReview(
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    try {
      Long reviewId;
      if (param.containsKey("reviewId")  && param.get("reviewId").length() > 0) {
        reviewId = Long.parseLong(param.get("reviewId"));
      } else {
        String[] errParam = {"reviewId"};
        Locale locale = getLocale(null);
        throw new InvalidParamException(errParam, locale);       
      }
      SvcStoreReviewMapper mapper = sessionTemplate.getMapper(SvcStoreReviewMapper.class);
      mapper.deleteByPrimaryKey(reviewId);
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(true);
      
      return result;
    } catch (Exception e) {
      logger.error("[deleteReview] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/store/write/review")
  @ResponseBody
  public AppJsonResult writeReview(
      @RequestBody SvcStoreReview review,
      HttpServletRequest request) throws Exception {

    try {
      SvcStoreReviewMapper mapper = sessionTemplate.getMapper(SvcStoreReviewMapper.class);
      review.setCreated(new Date());
      review.setUpdated(new Date());
      mapper.insertSelective(review);
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(true);
      return result;
    } catch (Exception e) {
      logger.error("[writeReview] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/store/review")
  @ResponseBody
  public AppJsonResult review(
      @RequestParam("params") String params,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    Map<String, Object> mapperParam = Maps.newHashMap();
    try {
      Long brandId;
      Long storeId;
      if (param.containsKey("brandId")  && param.get("brandId").length() > 0) {
        brandId = Long.parseLong(param.get("brandId"));
        mapperParam.put("brandId",  brandId);
      } else {
        String[] errParam = {"brandId"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      if (param.containsKey("storeId")  && param.get("storeId").length() > 0) {
        storeId = Long.parseLong(param.get("storeId"));
        mapperParam.put("storeId",  storeId);
      } 
      
      if (rowsPerPage != 0) { // 매장 상세에서는 전체 리뷰를 보여줌
        // limits
        mapperParam.put("offset", pageNo);
        mapperParam.put("limit", rowsPerPage);
      }
      
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      List<Map<String, Object>> totalCnt = mapper.getReviewCount(mapperParam);
      List<Map<String, Object>> datas = mapper.getReview(mapperParam);
      
      float rating = 0.0f;
      double totalRating  = 0.0f;
      if (totalCnt != null && totalCnt.size() > 0) {
        for (int i = 0; i < totalCnt.size(); i++) {
          Map<String, Object> review = convertCamelcase(totalCnt.get(i));
          rating += (Integer) review.get("rating");
          logger.debug("rating : " + rating + " " + rating /(double)totalCnt.size());
        }
        totalRating = rating / (double) totalCnt.size();
      }
      
      MobileSecurityTypeHandler ms = new MobileSecurityTypeHandler();
      List<Object> items = Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> review = datas.get(i);
        Map<String, Object> item = convertCamelcase(review);
        logger.debug("review : " + JsonConvert.toJson(item));
        if (!GenericValidator.isBlankOrNull((String) item.get("mbBk"))) {
          item.put("mbBk",  ms.textEncryptor.decrypt((String) item.get("mbBk")));
          items.add(item);
        }
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt.size(), pageNo, param);
      obj.put("totalRating", totalRating);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[review] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[review] fail !!", e);
      throw e;
    }
  }

  @RequestMapping(value = {"/user/{idAttribute}"})
  @ResponseBody
  public AppJsonResult user(@PathVariable("idAttribute") String idAttribute,
                         @RequestBody(required = false) User modData,
                         HttpServletRequest request) throws Exception {

    try {
      User user = getCurrentUser(idAttribute, null);
      UserMapper mapper = sessionTemplate.getMapper(UserMapper.class);
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
  
      String userType = codeUtil.getBaseCodeByAlias("user");
      String statusWithdraw = codeUtil.getBaseCodeByAlias("user-status-withdraw");
  
      HttpMethod method = HttpMethod.valueOf(request.getMethod());
      switch (method) {
        case GET: {
          // skip
        }
        break;
        case PUT:
        case PATCH: {  // update
          // chekc email duplicate
          UserExample emailWhere = new UserExample();
          emailWhere.createCriteria()
              .andIdNotEqualTo(user.getId())
              .andTypeEqualTo(userType)
              .andEmailEqualTo(modData.getEmail());
  
          int count = mapper.countByExample(emailWhere);
          if (count > 0) {
            throw new DataDuplicateException("email", modData.getEmail(), null);
          } else {
            user.setEmail(modData.getEmail());
            user.setUsername(modData.getEmail());
  
            mapper.updateByPrimaryKey(user);
  
            // logout(revoke token)
            revokeAccessToken(request);
  
            // request oauth(login)
            Map<String, String> oauth = ApiUtil.login("password", "mobile", user.getUsername(), user.getBarcode(), user.getMb());
            user.setSecToken(oauth.get("access_token"));
          }
        }
        break;
        case DELETE: { // withdraw
          user.setStatus(statusWithdraw);
          mapper.updateByPrimaryKey(user);
  
          // logout(revoke token)
          revokeAccessToken(request);
        }
        break;
        default: {
          throw new HttpRequestMethodNotSupportedException(method.toString());
        }
      }
  
      // convert code -> title
//      user.setLevel(codeUtil.getTitleByCode(user.getLevel()));
  
      if (!GenericValidator.isBlankOrNull(user.getPassword())) {
        user.setPassword(AES256Cipher.encodeAES256(user.getPassword()));
      }
      
      if (!GenericValidator.isBlankOrNull(user.getMb())) {
        user.setMb(AES256Cipher.encodeAES256(user.getMb()));
      }
      
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(user);
  
      logger.debug("user : " + JsonConvert.toJson(result));
      return result;
    } catch (Exception e) {
      logger.error("[user] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/history")
  @ResponseBody
  public JsonResult history(@RequestParam(value = "params", defaultValue = "{}") String searchParams,
                            @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
                            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) throws Exception {
    User user = getCurrentUser("me", null);
    Map<String, String> search = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {
    });
    logger.debug("history - " + search);

    RowBounds rowBound = getRowBounds(pageNo, rowsPerPage);
    CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
    Map<String, Object> params = Maps.newHashMap();
    SimpleDateFormat format = getDateFormat();
    List<Map<String, Object>> datas;
    int totalCnt = 0;

    // where
    params.put("userId", user.getId());
    if (search.containsKey("start") && search.containsKey("end")) {
      try {
        format.parse(search.get("start"));
      } catch (ParseException e) {
        Errors errors = makeErrors();
        errors.rejectValue("start", "validation.invalid.date.format", new String[]{format.toPattern()}, "");
        throw new InvalidParamException(errors);
      }
      try {
        format.parse(search.get("end"));
      } catch (ParseException e) {
        Errors errors = makeErrors();
        errors.rejectValue("end", "validation.invalid.date.format", new String[]{format.toPattern()}, "");
        throw new InvalidParamException(errors);
      }

      params.put("start", search.get("start"));
      params.put("end", search.get("end"));
    }

    // limits
    params.put("offset", rowBound.getOffset());
    params.put("limit", rowBound.getLimit());

    // order by
    if (search.containsKey("orderBy") && "asc".equals(search.get("orderBy"))) {
      params.put("orderBy", "ASC");
    } else {
      params.put("orderBy", "DESC");
    }

    if (search.containsKey("mode") && "coupon".equals(search.get("mode"))) {
      // coupon
      datas = mapper.getCouponHistory(params);
      totalCnt = mapper.getCouponHistoryCount(params);
    } else {
      // stamp
      datas = mapper.getStampHistory(params);
      totalCnt = mapper.getStampHistoryCount(params);
    }

    JsonResult result = new JsonResult();
    result.setSuccess(true);
    List<Object> items = Lists.newArrayList();
    for (int i = 0; (datas != null && i < datas.size()); i++) {
      Map<String, Object> history = datas.get(i);
      Map<String, Object> item = convertCamelcase(history);
      item.put("logTp", codeUtil.getTitleByCode((String) item.get("logTp")));
      items.add(item);
    }
    Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, search);
    result.setBean(obj);
    return result;
  }

  @RequestMapping("/stamp")
  @ResponseBody
  public JsonResult stamp(HttpServletRequest request) throws Exception {
    User user = getCurrentUser("me", null);
    SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
    SvcBrand brand = brandMapper.selectByPrimaryKey(Globals.PICO_BRAND_ID); // TODO 브랜드 상관없이 처리되어야 됨

    couponProcessor.init(user, brand, null);
    List<SvcUserStamp> stamps = couponProcessor.getCurrentStamps(Globals.PICO_BRAND_ID);  // TODO 브랜드 상관없이 처리되어야 됨

    List<Object> items = Lists.newArrayList();
    logger.debug("stampCount = " + couponProcessor.getTriggerCount());
    for (int i = 0; i < couponProcessor.getTriggerCount(); i++) {
      Map<String, Object> map = Maps.newHashMap();
      SvcUserStamp stamp = (i < stamps.size() ? stamps.get(i) : null);

      map.put("exists", stamp != null);
      if (stamp != null) {
        map.put("expire", couponProcessor.getStampExpire(stamp.getCreated()));
      }
      items.add(map);
    }

    JsonResult result = new JsonResult();
    result.setSuccess(true);
    Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, items.size(), null);
    result.setBean(obj);

    return result;
  }

  @RequestMapping("/store/coupon")
  @ResponseBody
  public AppJsonResult coupon(
      @RequestParam(value = "params", defaultValue = "{}") String params,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      HttpServletRequest request) throws Exception {
    try {
      Map<String, String> search = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
      Locale locale = getLocale(search.get("lang"));
      User user = getCurrentUser("me", locale);
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      Map<String, Object> mapperParams = Maps.newHashMap();
      List<Map<String, Object>> datas;
      int totalCnt = 0;
  
      // where
      mapperParams.put("userId", user.getId());
//      if (search.containsKey("start") && search.containsKey("end")) {
//        try {
//          format.parse(search.get("start"));
//        } catch (ParseException e) {
//          String[] errParam = {"start"};
//          throw new InvalidParamException(errParam);
//        }
//        try {
//          format.parse(search.get("end"));
//        } catch (ParseException e) {
//          String[] errParam = {"end"};
//          throw new InvalidParamException(errParam);
//        }
//  
//        mapperParams.put("start", search.get("start"));
//        mapperParams.put("end", search.get("end"));
//      } else {
//        String[] errParam = {"start, end"};
//        throw new InvalidParamException(errParam);
//      }
  
      if (search.containsKey("used")) {
        mapperParams.put("issue", codeUtil.getBaseCodeByAlias("coupon-log-issue"));
        mapperParams.put("reIssue", codeUtil.getBaseCodeByAlias("coupon-log-cancel"));
        mapperParams.put("used", search.get("used"));
      } 
      if (search.containsKey("brandId")) {
        mapperParams.put("brandId", search.get("brandId"));
      } else {
        // 모든 매장의 쿠폰을 가져올 수도 있으므로 예외처리 안함
//        String[] errParam = {"brandId"};
//        throw new InvalidParamException(errParam);
      }
  
      if (search.containsKey("storeId")) {
        mapperParams.put("storeId", search.get("storeId"));
      } else {
        // 모든 매장의 쿠폰을 가져올 수도 있으므로 예외처리 안함
//        String[] errParam = {"storeId"};
//        throw new InvalidParamException(errParam);
      }
  
      if (search.containsKey("now")) {
        mapperParams.put("now", String.valueOf(search.get("now")));
      } 
      
      // limits
      mapperParams.put("offset", pageNo);
      mapperParams.put("limit", rowsPerPage);
  
      // order by
      
      
      if (search.containsKey("sort") && "created".equals(search.get("sort"))) {
        mapperParams.put("sort", "created");
      } else {
        logger.debug("sort : " + search.get("sort"));
        mapperParams.put("sort", "expire");
      }
      if (search.containsKey("orderBy") && "asc".equals(search.get("orderBy"))) {
        mapperParams.put("orderBy", "ASC");
      } else {
        mapperParams.put("orderBy", "DESC");
      }
  
      datas = mapper.getCoupon(mapperParams);
      totalCnt = mapper.getCouponCount(mapperParams);
      logger.debug("totalCnt : " + totalCnt);
  
      List<Object> items = Lists.newArrayList();
      SvcStoreMapper storeMapper = sessionTemplate.getMapper(SvcStoreMapper.class);
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> history = datas.get(i);
        Map<String, Object> item = convertCamelcase(history);
        item.put("discountTp", codeUtil.getTitleByCode((String) item.get("discountTp")));
  
        if (item.containsKey("storeIds") && !GenericValidator.isBlankOrNull((String) item.get("storeIds"))) {
          String val = (String) item.get("storeIds");
          if (GenericValidator.isBlankOrNull(val) || "[]".equals(val)) {
            item.put("store", "ALL");
            item.put("storeCount", 0);
            item.put("storeIds", Lists.<String>newArrayList());
          } else {
            Map<String, Object> data = JsonConvert.JsonConvertObject(val, new TypeReference<Map<String, Object>>(){});
            List<Long> storeIds = JsonConvert.JsonConvertObject(JsonConvert.toJson(data.get("storeIds")), new TypeReference<List<Long>>() {});
            SvcStoreExample storeExample = new SvcStoreExample();
            storeExample.createCriteria()
                .andIdIn(storeIds);
            int count = storeMapper.countByExample(storeExample);
            SvcStore store = storeMapper.selectByPrimaryKey(storeIds.get(0));
            item.put("store", (store != null ? store.getStoreNm() : "UNKNOWN"));
            item.put("storeCount", count);
            item.put("storeIds", storeIds);
          }
        }
        items.add(item);
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, search);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      
      logger.debug("coupon result : " + JsonConvert.toJson(result));
      return result;
    } catch (Exception e) {
      logger.error("[coupon] fail !!", e);
      throw e;
    }
  }

  @RequestMapping(value = {"/coupon/{idAttribute}"})
  @ResponseBody
  public JsonResult coupon(@PathVariable("idAttribute") String idAttribute) throws Exception {
    User user = getCurrentUser("me", null);
    logger.debug("coupon - " + idAttribute);

    CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
    Map<String, Object> params = Maps.newHashMap();
    SimpleDateFormat format = getDateFormat();
    List<Map<String, Object>> datas;
    // where
    params.put("userId", user.getId());
    params.put("couponId", idAttribute);

    datas = mapper.getCoupon(params);

    if (datas == null || datas.size() != 1) {
      throw new DataNotRegisteredException("coupon", idAttribute, null);
    }

    JsonResult result = new JsonResult();
    result.setSuccess(true);

    SvcStoreMapper storeMapper = sessionTemplate.getMapper(SvcStoreMapper.class);
    Map<String, Object> data = datas.get(0);
    Map<String, Object> coupon = convertCamelcase(data);
    coupon.put("discountTp", codeUtil.getTitleByCode((String) coupon.get("discountTp")));

    if (coupon.containsKey("storeIds")) {
      String val = (String) coupon.get("storeIds");
      if (GenericValidator.isBlankOrNull(val) || "[]".equals(val)) {
        coupon.put("store", "ALL");
        coupon.put("storeCount", 0);
        coupon.put("storeIds", Lists.<String>newArrayList());
      } else {
        List<Long> storeIds = JsonConvert.JsonConvertObject(val, new TypeReference<List<Long>>() {
        });
        SvcStoreExample storeExample = new SvcStoreExample();
        storeExample.createCriteria()
            .andIdIn(storeIds);
        int count = storeMapper.countByExample(storeExample);
        SvcStore store = storeMapper.selectByPrimaryKey(storeIds.get(0));
        coupon.put("store", (store != null ? store.getStoreNm() : "UNKNOWN"));
        coupon.put("storeCount", count);
        coupon.put("storeIds", storeIds);
      }
    }
    result.setBean(coupon);
    return result;
  }

  @RequestMapping("/store/category")
  @ResponseBody
  public AppJsonResult storeCategory() throws Exception {
    try {
        CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
        List<Map<String, Object>> storeCategorys = mapper.selectByDistinctStoreTp();
    
        List<BaseBcode> codes = codeUtil.getByMainCode("353"); // 353== 매장 타입
        List<Map<String, String>> datas = Lists.newArrayList();
        
        for (Map<String, Object> sc : storeCategorys) {
          sc = convertCamelcase(sc);
          for (BaseBcode code : codes) {
            HashMap<String, String> map = Maps.newHashMap();
            if (sc.get("storeTp").equals(code.getBaseCd())) {
              map.put("title", code.getTitle()); // 로컬 언어가 영어일 때 해당 데이터를 카테고리로 보여줌
              map.put("value", code.getAlias());
              map.put("desc", code.getDsc());  // 로컬 언어가 러시일 때 해당 데이터를 카테고리로 보여줌
              if (!datas.contains(map)) {
                logger.debug("map : " + JsonConvert.toJson(map));
                if ("ALL".equals(code.getTitle())) {
                  datas.add(0, map);
                } else {
                  datas.add(map);
                }
                break;
              }
            }
          }
        }
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(datas);
      
      return result;
    } catch (Exception e) {
      logger.error("[storeCategory] fail !!", e);
      throw e;
    }
  }

  @RequestMapping("/store")
  @ResponseBody
  public AppJsonResult store(@RequestHeader("Service-Identifier") Long serviceId,
                          @RequestParam(value = "params", defaultValue = "{}") String searchParams,
                          @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
                          @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                          HttpServletRequest request) throws Exception {
    Map<String, String> search = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(search.get("lang"));
    try {
      CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
      Map<String, Object> params = Maps.newHashMap();
  
      String svcStNormal = codeUtil.getBaseCodeByAlias("svc-st-normal");
      String storeStNormal = codeUtil.getBaseCodeByAlias("store-st-normal");
  
      // where
      User user = getCurrentUser("me", locale);
      params.put("userId", user.getId());
      params.put("serviceId", serviceId);
      params.put("svcSt", svcStNormal);
      params.put("storeSt", storeStNormal);
      
      if (search.containsKey("isBookmark") && !GenericValidator.isBlankOrNull(search.get("isBookmark"))) {
        params.put("isBookmark", Double.valueOf(search.get("isBookmark")));
      }
  
      if (search.containsKey("distance") && !GenericValidator.isBlankOrNull(search.get("distance"))) {
        params.put("distance", Double.valueOf(search.get("distance")));
      }
      
      if (search.containsKey("storeId") && !GenericValidator.isBlankOrNull(search.get("storeId"))) {
        params.put("storeId", Long.valueOf(search.get("storeId")));
      }
      
      if (search.containsKey("userId") && !GenericValidator.isBlankOrNull(search.get("userId"))) {
        params.put("userId", Long.valueOf(search.get("userId")));
      }
      
  
      if (search.containsKey("storeTp") && !GenericValidator.isBlankOrNull(search.get("storeTp"))) {
        String storeTp = (String) search.get("storeTp");
        String storeTpAll = codeUtil.getBaseCodeByAlias("store-tp-all");
        if (!storeTp.equals(storeTpAll)) {
          params.put("storeTp", storeTp);
        }
      }
  
      if (search.containsKey("keyword") && !GenericValidator.isBlankOrNull(search.get("keyword"))) {
        params.put("keyword", search.get("keyword"));
      }
  
      if (search.containsKey("area") && !GenericValidator.isBlankOrNull(search.get("area"))) {
        BaseBcode region = codeUtil.getByAlias(search.get("area"));
        if (region == null) {
          throw new CodeNotFoundException("area", search.get("area"), locale);
        } else {
          params.put("reigon", region.getAlias());
        }
      }
  
      if (search.containsKey("lat") && search.containsKey("lng")) {
        if (search.get("lat").length() > 0 && search.get("lng").length() > 0) {
          try {
            params.put("lat", Double.valueOf(search.get("lat")));
            params.put("lng", Double.valueOf(search.get("lng")));
          } catch (NumberFormatException e) {
            throw new InvalidValueException("latlng", locale);
          }
        }
      }
  
      // limits
      if (rowsPerPage != 0) { // reowsPerPage == 0 , 지도에 매장 전체, 매장 상세 페이지
        params.put("offset", pageNo);
        params.put("limit", rowsPerPage);
      }
  
      if (search.containsKey("allNearBy") && search.get("allNearBy").length() > 0) {
        params.put("orderBy", search.get("allNearBy"));
      }
  
      List<Map<String, Object>> datas = mapper.getStore(params);
      int totalCnt = mapper.getStoreCount(params);
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      
      List<Object> items = Lists.newArrayList();
      for (int i = 0; (datas != null && i < datas.size()); i++) {
        Map<String, Object> store = datas.get(i);
        Map<String, Object> item = convertCamelcase(store);
        item.put("svcSt", codeUtil.getAliasByCode((String) item.get("svcSt")));
        item.put("storeSt", codeUtil.getAliasByCode((String) item.get("storeSt")));
        item.put("storeTp", codeUtil.getAliasByCode((String) item.get("storeTp")));
  
        // get setting
        // 매장별 쿠폰 설정을 허용하더라도 매장의 설정 정보가 없을 경우에는 brand 설정 우선.
        ObjectMapper objectMapper = JsonConvert.getObjectMapper();
        Long brandId = Long.valueOf(item.get("brandId").toString());
        Long storeId = Long.valueOf(item.get("id").toString());
        SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
        SvcBrand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand.getAllowStoreSet()) {
          SvcStoreSetMapper setMapper = sessionTemplate.getMapper(SvcStoreSetMapper.class);
          SvcStoreSetExample example = new SvcStoreSetExample();
          example.createCriteria()
              .andBrandIdEqualTo(brandId)
              .andStoreIdEqualTo(storeId);
  
          logger.debug("brand : " + brandId + ", store : " + storeId);
          SvcStoreSetWithBLOBs setting = setMapper.selectByExampleWithBLOBs(example).get(0);
          item.put("setting", objectMapper.convertValue(setting, Maps.<String, Object>newHashMap().getClass()));
        } else {
          SvcBrandSetMapper setMapper = sessionTemplate.getMapper(SvcBrandSetMapper.class);
          SvcBrandSetExample example = new SvcBrandSetExample();
          example.createCriteria()
              .andBrandIdEqualTo(brand.getId());
  
          SvcBrandSet setting = setMapper.selectByExample(example).get(0);
          item.put("setting", objectMapper.convertValue(setting, Maps.<String, Object>newHashMap().getClass()));
        }
  
        // images
        item.put("logo", brand.getLogoImg());
        SvcStoreImgMapper imgMapper = sessionTemplate.getMapper(SvcStoreImgMapper.class);
        SvcStoreImgExample imgExample = new SvcStoreImgExample();
        imgExample.createCriteria()
            .andBrandIdEqualTo(brandId)
            .andStoreIdEqualTo(storeId);
        imgExample.setOrderByClause("ORDINAL ASC");
        List<SvcStoreImg> images = imgMapper.selectByExample(imgExample);
  
        item.put("images", images);
        items.add(item);
      }
      Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, search);
      
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(obj);
      return result;
    } catch (CodeNotFoundException notCode) {
      String code = Error.ErrorCodes.CODE_NOT_FOUND.getCode();
      String err = notCode.getType();
      logger.error("[" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , " + err + " : " + notCode.getKey());
      throw notCode;
    } catch (InvalidValueException invalidValue) {
      String code = Error.ErrorCodes.INVALID_VALUE.getCode();
      String err = invalidValue.getType();
      logger.error("[" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , " + err);
      throw invalidValue;
    } catch (Exception e) {
      logger.error("[store] fail !!", e);
      throw e;
    }
  }

  @RequestMapping(value = {"/store/{idAttribute}"})
  @ResponseBody
  public JsonResult store(@PathVariable("idAttribute") String idAttribute,
                          HttpServletRequest request) throws Exception {
    JsonResult result = new JsonResult();

    long id;
    try {
      id = Long.valueOf(idAttribute);
    } catch (NumberFormatException e) {
      //bindingResult.rejectValue("noticeId", "validation.number.format", new String[]{idAttribute}, "");
      throw new DataNotFoundException(idAttribute);
    }
    SvcStoreMapper mapper = sessionTemplate.getMapper(SvcStoreMapper.class);
    SvcStore store = mapper.selectByPrimaryKey(id);

    if (store == null) {
      throw new DataNotFoundException(idAttribute);
    } else {
      result.setSuccess(true);
      store.setSvcSt(codeUtil.getAliasByCode(store.getSvcSt()));
      store.setStoreSt(codeUtil.getAliasByCode(store.getStoreSt()));

      ObjectMapper objectMapper = JsonConvert.getObjectMapper();
      Map<String, Object> data = objectMapper.convertValue(store, Maps.<String, Object>newHashMap().getClass());

      // get setting
      // 매장별 쿠폰 설정을 허용하더라도 매장의 설정 정보가 없을 경우에는 brand 설정 우선.
      SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
      SvcBrand brand = brandMapper.selectByPrimaryKey(store.getBrandId());
      if (brand.getAllowStoreSet()) {
        SvcStoreSetMapper setMapper = sessionTemplate.getMapper(SvcStoreSetMapper.class);
        SvcStoreSetExample example = new SvcStoreSetExample();
        example.createCriteria()
            .andBrandIdEqualTo(store.getBrandId())
            .andStoreIdEqualTo(store.getId());

        SvcStoreSet setting = setMapper.selectByExample(example).get(0);
        data.put("setting", objectMapper.convertValue(setting, Maps.<String, Object>newHashMap().getClass()));
      } else {
        SvcBrandSetMapper setMapper = sessionTemplate.getMapper(SvcBrandSetMapper.class);
        SvcBrandSetExample example = new SvcBrandSetExample();
        example.createCriteria()
            .andBrandIdEqualTo(brand.getId());

        SvcBrandSet setting = setMapper.selectByExample(example).get(0);
        data.put("setting", objectMapper.convertValue(setting, Maps.<String, Object>newHashMap().getClass()));
      }

      // images
      data.put("logo", brand.getLogoImg());
      SvcStoreImgMapper imgMapper = sessionTemplate.getMapper(SvcStoreImgMapper.class);
      SvcStoreImgExample imgExample = new SvcStoreImgExample();
      imgExample.createCriteria()
          .andBrandIdEqualTo(store.getBrandId())
          .andStoreIdEqualTo(store.getId());
      imgExample.setOrderByClause("ORDINAL ASC");
      List<SvcStoreImg> images = imgMapper.selectByExample(imgExample);

      data.put("images", images);

      result.setBean(data);
    }

    return result;
  }

  @RequestMapping("/category")
  @ResponseBody
  public AppJsonResult category(@RequestParam("brandId") long brandId,
                             @RequestParam("storeId") long storeId) throws Exception {
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();

    String pluTp = codeUtil.getBaseCodeByAlias("plu-mobile");
    SvcPluCatMapper mapper = sessionTemplate.getMapper(SvcPluCatMapper.class);
    SvcPluCatExample example = new SvcPluCatExample();
    example.createCriteria()
        .andPluTpEqualTo(pluTp)
        .andBrandIdEqualTo(brandId)
        .andStoreIdIsNull();
    example.or()
        .andPluTpEqualTo(pluTp)
        .andBrandIdEqualTo(brandId)
        .andStoreIdEqualTo(storeId);

    List<SvcPluCat> categories = mapper.selectByExample(example);
    header.setErrCd(ErrorCodes.SUCCESS.getCode());
    result.setHeader(header);
    result.setData(categories);

    return result;
  }

  @RequestMapping("/item")
  @ResponseBody
  public AppJsonResult item(
        @RequestParam(value = "params", defaultValue = "{}") String searchParams,
        @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) throws Exception {
    Map<String, String> search = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {
    });
    logger.debug("item - " + JsonConvert.toJson(search));

    RowBounds rowBound = getRowBounds(pageNo, rowsPerPage);
    CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
    Map<String, Object> params = Maps.newHashMap();

    // where
    params.put("pluTp",  codeUtil.getBaseCodeByAlias("plu-mobile"));
    params.put("brandId", search.get("brandId"));
    params.put("storeId", search.get("storeId"));
    if (!GenericValidator.isBlankOrNull(search.get("catId"))) {
      params.put("catId", search.get("catId"));
    }

    // item status
    params.put("status",  codeUtil.getBaseCodeByAlias("item-st-Y"));
    // limits
    params.put("offset", rowBound.getOffset());
    params.put("limit", rowBound.getLimit());

    // order by
    if (search.containsKey("orderBy") && "asc".equals(search.get("orderBy"))) {
      params.put("orderBy", "ASC");
    } else {
      params.put("orderBy", "DESC");
    }

    List<Map<String, Object>> datas = mapper.getItem(params);
    int totalCnt = mapper.getItemCount(params);
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();
    header.setErrCd(ErrorCodes.SUCCESS.getCode());
    List<Object> items = Lists.newArrayList();
    SimpleDateFormat format = getDateFormat();
    
    if (datas != null && datas.size() > 0) {
      SvcItemImgMapper imgMapper = sessionTemplate.getMapper(SvcItemImgMapper.class);
      SvcItemOptMapper optMapper = sessionTemplate.getMapper(SvcItemOptMapper.class);
      SvcItemOptDtlMapper dtlMapper = sessionTemplate.getMapper(SvcItemOptDtlMapper.class);
      List<Long> itemIds = Lists.newArrayList();
      for (int i = 0; i < datas.size(); i++) {
        Map<String, Object> dt = datas.get(i);
        Map<String, Object> item = convertCamelcase(dt);
        
        // 상품 판매 기간이 없거나, 판매 기간이 유효한 상품 추출
        Long itemId = Long.valueOf(item.get("id").toString());
        if (!GenericValidator.isBlankOrNull((String)item.get("salesBegin"))) {
          Date salesBegin = format.parse((String)item.get("salesBegin"));
          if (!GenericValidator.isBlankOrNull((String)item.get("salesEnd"))) {
            Date salesEnd = format.parse((String)item.get("salesEnd"));
            Date now = format.parse(format.format(new Date(System.currentTimeMillis())));
            int beginCompare = now.compareTo(salesBegin);
            int endCompare = now.compareTo(salesEnd);
            if (beginCompare >= 0 && endCompare <= 0) {
              itemIds.add(itemId);
              items.add(item);
            }
          }
        } else {
          itemIds.add(itemId);
          items.add(item);
        }
      }

      // get Items
      if (items != null && items.size() > 0) {
        SvcItemImgExample imgExample = new SvcItemImgExample();
        imgExample.createCriteria()
            .andItemIdIn(itemIds);
        imgExample.setOrderByClause("ITEM_ID, ORDINAL ASC");
  
        List<SvcItemImg> images = imgMapper.selectByExample(imgExample);
        Map<Long, List<SvcItemImg>> imageMap = Maps.newHashMap();
        if (images != null) {
          for (SvcItemImg img : images) {
            List<SvcItemImg> imageList = imageMap.get(img.getItemId());
            if (imageList == null) {
              imageList = Lists.newArrayList();
            }
            imageList.add(img);
            imageMap.put(img.getItemId(), imageList);
          }
  
          // set to items..
          for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = (Map<String, Object>) items.get(i);
            Long itemId = Long.valueOf(item.get("id").toString());
            item.put("images", imageMap.get(itemId));
          }
        }
  
//        // get options
//        SvcItemOptExample optExample = new SvcItemOptExample();
//        optExample.createCriteria()
//            .andItemIdIn(itemIds)
//            .andIsUsedEqualTo(true);
//        optExample.setOrderByClause("ITEM_ID, ORDINAL ASC");
//        List<SvcItemOpt> options = optMapper.selectByExample(optExample);
//  
//        // get option details
//        SvcItemOptDtlExample dtlExample = new SvcItemOptDtlExample();
//        dtlExample.createCriteria()
//            .andItemIdIn(itemIds);
//        dtlExample.setOrderByClause("ITEM_ID, ORDINAL ASC");
//  
//        List<SvcItemOptDtl> details = dtlMapper.selectByExample(dtlExample);
//        Map<Long, List<SvcItemOpt>> optMap = Maps.newHashMap();
//        Map<Long, List<SvcItemOptDtl>> dtlMap = Maps.newHashMap();
//        if (options != null) {
//          for (SvcItemOpt opt : options) {
//            List<SvcItemOpt> optList = optMap.get(opt.getItemId());
//            if (optList == null) {
//              optList = Lists.newArrayList();
//            }
//            optList.add(opt);
//            optMap.put(opt.getItemId(), optList);
//          }
//  
//          if (details != null) {
//            for (SvcItemOptDtl dtl : details) {
//              List<SvcItemOptDtl> dtlList = dtlMap.get(dtl.getOptId());
//              if (dtlList == null) {
//                dtlList = Lists.newArrayList();
//              }
//              dtlList.add(dtl);
//              dtlMap.put(dtl.getOptId(), dtlList);
//            }
//          }
//  
//          // set to items..
//          ObjectMapper objectMapper = JsonConvert.getObjectMapper();
//          for (int i = 0; i < items.size(); i++) {
//            Map<String, Object> item = (Map<String, Object>) items.get(i);
//            Long itemId = Long.valueOf(item.get("id").toString());
//            List<SvcItemOpt> tmpList = optMap.get(itemId);
//            List<Map<String, Object>> optionList = Lists.newArrayList();
//            for (int index = 0; (tmpList != null && index < tmpList.size()); index++) {
//              SvcItemOpt option = tmpList.get(index);
//              Map<String, Object> optionMap = objectMapper.convertValue(option, Maps.<String, Object>newHashMap().getClass());
//  
//              optionMap.put("details", dtlMap.get(option.getId()));
//              optionList.add(optionMap);
//            }
//            item.put("options", optionList);
//          }
//        }
      }
    }
    Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, search);
    result.setHeader(header);
    result.setData(obj);
    return result;
  }

  @RequestMapping(value = "/order", method = RequestMethod.GET)
  @ResponseBody
  public JsonResult order(@RequestParam(value = "params", defaultValue = "{}") String searchParams,
                          @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
                          @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) throws Exception {
    Map<String, String> search = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {
    });
    logger.debug("order - " + search);

    User user = getCurrentUser("me", null);
    SimpleDateFormat format = getDateFormat();
    ObjectMapper objectMapper = JsonConvert.getObjectMapper();
    Errors errors = makeErrors();

    RowBounds rowBound = getRowBounds(pageNo, rowsPerPage);
    SvcOrderMapper mapper = sessionTemplate.getMapper(SvcOrderMapper.class);
    SvcOrderItemMapper itemMapper = sessionTemplate.getMapper(SvcOrderItemMapper.class);
    SvcOrderPayMapper payMapper = sessionTemplate.getMapper(SvcOrderPayMapper.class);
    SvcOrderExample example = new SvcOrderExample();

    // where
    SvcOrderExample.Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(user.getId());

    if (!GenericValidator.isBlankOrNull(search.get("brandId"))) {
      criteria.andBrandIdEqualTo(Long.valueOf(search.get("brandId")));
    }
    if (!GenericValidator.isBlankOrNull(search.get("storeId"))) {
      criteria.andStoreIdEqualTo(Long.valueOf(search.get("storeId")));
    }

    if (search.containsKey("start") && search.containsKey("end")) {
      Date start, end;
      try {
        start = format.parse(search.get("start"));
      } catch (ParseException e) {
        errors.rejectValue("start", "validation.invalid.date.format", new String[]{format.toPattern()}, "");
        throw new InvalidParamException(errors);
      }
      try {
        end = format.parse(search.get("end"));
      } catch (ParseException e) {
        errors.rejectValue("end", "validation.invalid.date.format", new String[]{format.toPattern()}, "");
        throw new InvalidParamException(errors);
      }

      criteria.andOrderTmBetween(start, end);
    }

    List<SvcOrder> datas = mapper.selectByExampleWithRowbounds(example, rowBound);
    int totalCnt = mapper.countByExample(example);
    JsonResult result = new JsonResult();
    result.setSuccess(true);
    List<Object> items = Lists.newArrayList();
    for (int i = 0; (datas != null && i < datas.size()); i++) {
      SvcOrder order = datas.get(i);
      order.setOrderSt(codeUtil.getTitleByCode(order.getOrderSt()));
      Map<String, Object> item = objectMapper.convertValue(order, Maps.<String, Object>newHashMap().getClass());

      // get items
      SvcOrderItemExample itemExample = new SvcOrderItemExample();
      itemExample.createCriteria()
          .andOrderIdEqualTo(order.getId());
      item.put("items", itemMapper.selectByExample(itemExample));

      // get payment
      SvcOrderPayExample payExample = new SvcOrderPayExample();
      payExample.createCriteria()
          .andOrderIdEqualTo(order.getId());
      item.put("payment", payMapper.selectByExample(payExample));

      items.add(item);
    }
    Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, search);
    result.setBean(obj);
    return result;
  }

  @RequestMapping(value = {"/order/{idAttribute}"}, method = RequestMethod.GET)
  @ResponseBody
  public JsonResult order(@PathVariable("idAttribute") String idAttribute) throws Exception {
    JsonResult result = new JsonResult();
    ObjectMapper objectMapper = JsonConvert.getObjectMapper();
    SvcOrderItemMapper itemMapper = sessionTemplate.getMapper(SvcOrderItemMapper.class);
    SvcOrderPayMapper payMapper = sessionTemplate.getMapper(SvcOrderPayMapper.class);

    long id;
    try {
      id = Long.valueOf(idAttribute);
    } catch (NumberFormatException e) {
      //bindingResult.rejectValue("noticeId", "validation.number.format", new String[]{idAttribute}, "");
      throw new DataNotFoundException(idAttribute);
    }
    SvcOrderMapper mapper = sessionTemplate.getMapper(SvcOrderMapper.class);
    SvcOrder order = mapper.selectByPrimaryKey(id);

    if (order == null) {
      throw new DataNotFoundException(idAttribute);
    } else {
      order.setOrderSt(codeUtil.getTitleByCode(order.getOrderSt()));
      Map<String, Object> item = objectMapper.convertValue(order, Maps.<String, Object>newHashMap().getClass());

      // get items
      SvcOrderItemExample itemExample = new SvcOrderItemExample();
      itemExample.createCriteria()
          .andOrderIdEqualTo(order.getId());
      item.put("items", itemMapper.selectByExample(itemExample));

      // get payment
      SvcOrderPayExample payExample = new SvcOrderPayExample();
      payExample.createCriteria()
          .andOrderIdEqualTo(order.getId());
      item.put("payment", payMapper.selectByExample(payExample));

      result.setSuccess(true);
      result.setBean(order);
    }

    return result;
  }


  @Transactional
  @RequestMapping(value = "/order", method = {RequestMethod.POST})
  @ResponseBody
  public AppJsonResult orderSave(HttpServletRequest request) throws Exception {
    try{
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      Map<String, Object> data = Maps.newHashMap();
  
      User user = getCurrentUser("me", null);
      CustomMapper customMapper = sessionTemplate.getMapper(CustomMapper.class);
      SvcOrderMapper mapper = sessionTemplate.getMapper(SvcOrderMapper.class);
      SvcOrderItemMapper itemMapper = sessionTemplate.getMapper(SvcOrderItemMapper.class);
      SvcOrderPayMapper payMapper = sessionTemplate.getMapper(SvcOrderPayMapper.class);
      SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
      SvcStoreMapper storeMapper = sessionTemplate.getMapper(SvcStoreMapper.class);
      SvcOrderItemOptMapper itemOptMapper = sessionTemplate.getMapper(SvcOrderItemOptMapper.class);
      UserCardMapper cardMapper = sessionTemplate.getMapper(UserCardMapper.class);
      UserCardExample cardExample = new UserCardExample();
  
      ObjectMapper objectMapper = JsonConvert.getObjectMapper().setDateFormat(JSON_DATE_FORMAT);
      JsonNode rootNode = objectMapper.readTree(request.getReader());
      JsonNode cardNode =  rootNode.get("card");
      JsonNode orderNode = rootNode.get("order");
      JsonNode headerNode = rootNode.get("header");
      ArrayNode itemsNode = (ArrayNode) orderNode.get("items");
//      ArrayNode payNode = (ArrayNode) orderNode.get("payment");
//      String os = orderNode.get("path").toString();
  
      logger.debug("itemNode : " + JsonConvert.toJson(rootNode));
      Errors errors = makeErrors();
      UserCard card = objectMapper.treeToValue(cardNode,  UserCard.class);
      SvcOrder order = objectMapper.treeToValue(orderNode, SvcOrder.class);
      Header requestHeader = objectMapper.treeToValue(headerNode, Header.class);
      List<SvcOrderItem> items = Lists.newArrayList();
      List<SvcOrderPay> payment = Lists.newArrayList();
  
      // get brand & store
      SvcBrand brand = brandMapper.selectByPrimaryKey(order.getBrandId());
      SvcStore store = storeMapper.selectByPrimaryKey(order.getStoreId());
      
      // 매장 개점 날짜 받아옴
      Map<String, Object> param = Maps.newHashMap();
      param.put("storeId",  order.getStoreId());
      LinkedHashMap<String, Object> closing = customMapper.getClosing(param);
      if (closing != null) {
        order.setOpenDt(Timestamp.valueOf(closing.get("openDt").toString()));
    
        for (int i = 0; (itemsNode != null && i < itemsNode.size()); i++) {
          SvcOrderItem item = objectMapper.treeToValue(itemsNode.get(i), SvcOrderItem.class);
          items.add(item);
        }
    
        // TODO Validation check
        // run validator
        if (errors != null && errors.hasErrors()) {
          new InvalidParamException(errors);
        }
    
        String status = codeUtil.getBaseCodeByAlias("order-st-receive");
        boolean orderTpOrder = codeUtil.getBaseCodeByAlias("order-type-order").equals(order.getOrderTp()) ? true : false;
        boolean orderTpReserve = codeUtil.getBaseCodeByAlias("order-type-reservation").equals(order.getOrderTp()) ? true : false;
        boolean orderTpContract = codeUtil.getBaseCodeByAlias("order-type-contract").equals(order.getOrderTp()) ? true : false;
      
        Date now = new Date();
        boolean isUTC = true;
        logger.debug("timezone : " + requestHeader.getTimezone());
        logger.debug("[orderTpOrder] : " + orderTpOrder + ",  [orderTpReserve] : " + orderTpReserve + ",  [orderTpContract] : " + orderTpContract);
        if (orderTpReserve || orderTpContract) {
    //      if (items.size() == 0) {
    //        order.setSales(0.0);
    //        order.setSupplyValue(0.0);
    //      }
          order.setReserveTm(DateUtil.toTimeZone(order.getReserveTm(), TimeZone.getTimeZone(requestHeader.getTimezone()), isUTC));
          order.setReserveTmLocal(DateUtil.toTimeZone(order.getReserveTmLocal(), TimeZone.getTimeZone(requestHeader.getTimezone())));
          order.setReserveRegTm(DateUtil.toTimeZone(order.getReserveRegTm(), TimeZone.getTimeZone(requestHeader.getTimezone()), isUTC));
          order.setReserveRegTmLocal(DateUtil.toTimeZone(order.getReserveRegTmLocal(), TimeZone.getTimeZone(requestHeader.getTimezone())));
        } else if (orderTpOrder) {
    //      Calendar openDt = Calendar.getInstance();
    //      openDt.setTime(order.getOpenDt());
    //      openDt.set(Calendar.HOUR_OF_DAY,  0);
    //      order.setOpenDt(openDt.getTime());
          
          order.setOrderTm(DateUtil.toTimeZone(order.getOrderTm(), TimeZone.getTimeZone(requestHeader.getTimezone()), isUTC));
          order.setOrderTmLocal(DateUtil.toTimeZone(order.getOrderTmLocal(), TimeZone.getTimeZone(requestHeader.getTimezone())));
        }
      
        order.setOrdererMb(user.getMb());
        order.setUserId(user.getId());
        order.setOrderSt(status);
        
        String orderNo = String.valueOf(System.currentTimeMillis());
        // order NO 13자리 데이터 생성 후 앱에서는 10자리만 주문번호로 사용 
        order.setOrderNo(orderNo);
        order.setOrderSt(codeUtil.getBaseCodeByAlias("order-st-new"));
        order.setLastSt(codeUtil.getBaseCodeByAlias("951001"));
        
        mapper.insertSelective(order);
    
        for (int a = 0; a < items.size(); a++) {
          SvcOrderItem item = items.get(a);
          StringBuffer itemOptIds = new StringBuffer();
          StringBuffer itemOptNames= new StringBuffer();
          item.setOrderId(order.getId());
          item.setOrdinal(((Integer) (a + 1)).longValue());
          item.setLastSt(order.getLastSt());
          item.setPathTp(order.getPathTp());
          item.setSalesTp(codeUtil.getBaseCodeByAlias("809003"));
          itemMapper.insertSelective(item);
          
          logger.debug("order item : " + item.getItemNm() + " " + item.getItemId() + " " + item.getId());
          logger.debug("itemsNocde : " + JsonConvert.toJson(itemsNode));
          for (int i = 0; (itemsNode != null && i < itemsNode.size()); i++) {
            ArrayNode itemOptsNode = (ArrayNode) itemsNode.get(i).get("selectItemOptions");
            SvcOrderItem selectItem = objectMapper.treeToValue(itemsNode.get(i),  SvcOrderItem.class);
            if (item.getItemId() == selectItem.getItemId()) {
              logger.debug("item id : " + item.getItemId() + " select : " + selectItem.getItemId());
              logger.debug("items : " + JsonConvert.toJson(itemsNode.get(i)));
              if (itemOptsNode != null && itemOptsNode.size() > 0) {
                for (int k = 0; k < itemOptsNode.size(); k++) {
                  logger.debug("opt : " + JsonConvert.toJson(itemOptsNode.get(k)));
                  ArrayNode itemOptDetails = (ArrayNode) itemOptsNode.get(k).get("details");
                  ArrayNode selectOptDetails = (ArrayNode) itemOptsNode.get(k).get("selectItemOptionDetail");
                  SvcItemOpt opt = objectMapper.treeToValue(itemOptsNode.get(k),  SvcItemOpt.class);
                  if (itemOptDetails != null && itemOptDetails.size() > 0) {
                    for (int n = 0; n < itemOptDetails.size(); n++) {
                      SvcItemOptDtl optDtl = objectMapper.treeToValue(itemOptDetails.get(n),  SvcItemOptDtl.class);
                      if (selectOptDetails != null && selectOptDetails.size() > 0) {
                        for (int j = 0; j < selectOptDetails.size(); j++) {
                         Long itemOptOrdinal = 0l;
                          SvcItemOptDtl selectDtl = objectMapper.treeToValue(selectOptDetails.get(j),  SvcItemOptDtl.class);
                          logger.debug("optDtl : " + JsonConvert.toJson(optDtl));
                          logger.debug("selectDtl" + JsonConvert.toJson(selectDtl));
                          if (optDtl.getId() == selectDtl.getId() && selectDtl.getItemId() == item.getItemId()) {
                            logger.debug("optDtl : " + optDtl.getId() + " seledtdtl : " + selectDtl.getId());
                            SvcOrderItemOpt itemDtl = new SvcOrderItemOpt();
                            itemOptIds.append(opt.getId());
                            itemOptNames.append(opt.getName() + " (" + optDtl.getName() + ")");
                            logger.debug("opt :" + k + " " + (k+1) + " " + opt.getId());
                            // 옵션이 여러개일 때 혹은 한 옵션에 여러개의 디테일이 있을 때
                            if (j + 1 < selectOptDetails.size() || k + 1 < itemOptsNode.size()) {
                              itemOptIds.append(":");
                              itemOptNames.append(":");
                            }
                            itemDtl.setOrderId(order.getId());
                            itemDtl.setOrdinal(itemOptOrdinal++);
                            itemDtl.setItemId(item.getId());
                            itemDtl.setOptId(opt.getId());
                            itemDtl.setOptDtlId(optDtl.getId());
                            itemDtl.setOptNm(opt.getName());
                            itemDtl.setOptDtlNm(optDtl.getName());
                            itemDtl.setOptPrice(optDtl.getPrice());
                            itemDtl.setUpdated(new Date());
                            itemDtl.setCreated(new Date());
                            
                            itemOptMapper.insertSelective(itemDtl);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          if (itemOptIds.length() > 0) {
            SvcOrderItemExample itemExample = new SvcOrderItemExample();
            itemExample.createCriteria().andIdEqualTo(item.getId());
            
            List<SvcOrderItem> updateItems = itemMapper.selectByExample(itemExample);
            if (updateItems != null && updateItems.size() > 0) {
              item = updateItems.get(0);
              item.setOptId(itemOptIds.toString());
              item.setOptNm(itemOptNames.toString());
              itemMapper.updateByExampleSelective(item, itemExample);
            }
          }
        }
    
        cardExample.createCriteria().andIdEqualTo(card.getId());
        List<UserCard> cards = cardMapper.selectByExample(cardExample);
        String cardInfo = "";
        String cardNo = "";
        if (cards != null && cards.size() > 0) {
          logger.debug("card : " + JsonConvert.toJson(cards));
          cardInfo = card.getCardInfo() + cards.get(0).getCardInfo();
          logger.debug("order pay card info : " + cardInfo);
          String decryptCardInfo = AES256Cipher.decodeAES256(cardInfo);
          logger.debug("order pay decode : " + decryptCardInfo);
          String[] cardInfos = decryptCardInfo.split("\\|");
          logger.debug("card : " + cardInfo);
          for (String d : cardInfos) {
            logger.debug("card 1 : " + d);
          }
          cardNo = MaskUtil.getCardNumberFromEncryptedCardNumber(cardInfos[0]);
        } 
        
        SvcOrderPay orderPay = new SvcOrderPay();
        orderPay.setOrderId(order.getId());
        orderPay.setOrdinal(1l);
        orderPay.setPayMethod(codeUtil.getBaseCodeByAlias("810002"));
        orderPay.setCardInfo(cardInfo);
        orderPay.setCardNo(cardNo);
        orderPay.setAmount(order.getSales());
        orderPay.setPaySt(codeUtil.getBaseCodeByAlias("payment-st-pending"));
        orderPay.setPayTm(now);
        orderPay.setPayTmLocal(order.getCreated());
        orderPay.setCreated(now);
        orderPay.setUpdated(now);
        
        payMapper.insertSelective(orderPay);
        
    //    for (SvcOrderPay pay : payment) {
    //      pay.setOrderId(order.getId());
    //      String method = codeUtil.getBaseCodeByAlias(pay.getPayMethod());
    //      pay.setPayMethod(method);
    //      payMapper.insertSelective(pay);
    //    }
        data = objectMapper.convertValue(order, Maps.<String, Object>newHashMap().getClass());
        data.put("items", items);
        data.put("payment", payment);
        data.put("isOpenStore",  true);
    
        logger.debug("order  : " + JsonConvert.toJson(order));
        // Noti to POS and etc..
        MQTTClient.sendOrder(store, order, (!items.isEmpty() ? items.get(0) : null), user, codeUtil.getBaseCodeByAlias("order-path-tp-mobile"), null);
      } else {
        data.put("isOpenStore",  false);
      }
      
      result.setHeader(header);
      result.setData(data);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @RequestMapping("/item/option")
  @ResponseBody
  public AppJsonResult itemOption(
      @RequestParam(value = "params", defaultValue = "{}") String searchParams,
      HttpServletRequest request) throws Exception {
    try {
      Map<String, String> params = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {});
      
      SvcItemOptMapper itemOptMapper = sessionTemplate.getMapper(SvcItemOptMapper.class);
      SvcItemOptDtlMapper itemOptDtlMapper = sessionTemplate.getMapper(SvcItemOptDtlMapper.class);
      SvcItemOptExample itemOptExample = new SvcItemOptExample();
      SvcItemOptDtlExample itemOptDtlExample = new SvcItemOptDtlExample();
      
      long itemId = Long.parseLong(params.get("itemId"));
      itemOptExample.createCriteria()
          .andIsUsedEqualTo(true)
          .andItemIdEqualTo(itemId);
      itemOptExample.setOrderByClause("ITEM_ID, ORDINAL ASC");
      itemOptDtlExample.createCriteria()
          .andItemIdEqualTo(itemId);
      itemOptDtlExample.setOrderByClause("ITEM_ID, ORDINAL ASC");
      List<Object> optsDatas = Lists.newArrayList();      
      List<SvcItemOpt> opts = itemOptMapper.selectByExample(itemOptExample);
      List<SvcItemOptDtl> dtls = itemOptDtlMapper.selectByExample(itemOptDtlExample);
      Map<String, Object> optsData = Maps.newHashMap();
      
      for (int i = 0; (opts != null && i < opts.size()); i++) {
        SvcItemOpt opt = opts.get(i);
        List<SvcItemOptDtl> dtlsDatas = Lists.newArrayList();
        Map<String, Object> dtlsData = Maps.newHashMap();
        
        for (int j = 0; (dtls != null && j < dtls.size()); j++) {
          SvcItemOptDtl dtl = dtls.get(j);
          // 옵션 상세가 사용 가능할 때
          if (dtl.getIsUsed()) {
            if (opt.getId() == dtl.getOptId()) {
              dtlsDatas.add(dtl);
            }
          }
        }
        
        // 사용 가능한 옵션 상세가 1개 이상일 떄
        if (dtlsDatas.size() > 0) {
          dtlsData.put("details", dtlsDatas);
          Map<String, Object> history  = dtlsData;
          Map<String, Object> item = convertCamelcase(history);
          item.put("name", opt.getName());
          item.put("id",  opt.getId());
          item.put("isMandatory", opt.getIsMandatory());
          item.put("desc", opt.getDesc());
          item.put("ordinal", opt.getOrdinal());
          item.put("storeId", opt.getStoreId());
          item.put("isUsed", opt.getIsUsed());
          item.put("optCount", opt.getOptCount());
          item.put("itemId", opt.getItemId());
          item.put("brandId", opt.getBrandId());
          item.put("updated", opt.getUpdated());
          item.put("created", opt.getCreated());
          optsDatas.add(item);
          optsData.put("options", optsDatas);
        }
      }
     
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(optsData);
      return result;
    } catch (Exception e) {
      logger.error("[itemOption] fail !!", e);
      throw e;
    }
  }
  
  @Transactional
  @RequestMapping("/card")
  @ResponseBody
  public AppJsonResult card(
      @RequestParam(value = "params", defaultValue = "{}") String searchParams,
      @RequestParam(value = "rowsPerPage", defaultValue = "10") int rowsPerPage,
      @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
      HttpServletRequest request) throws Exception {
    try {
        Map<String, String> params = JsonConvert.JsonConvertObject(searchParams, new TypeReference<Map<String, String>>() {});
        Locale locale = getLocale(params.get("lang"));
        User user = getCurrentUser("me", locale);
        CustomMapper mapper = sessionTemplate.getMapper(CustomMapper.class);
        Map<String, Object> mapperParam = Maps.newHashMap();
        
        mapperParam.put("userId", user.getId());
        mapperParam.put("offset",  pageNo);
        mapperParam.put("limit", rowsPerPage);
        
        logger.debug("card info 1: " + !GenericValidator.isBlankOrNull(params.get("cardInfo")));
        logger.debug("card info 2 : " + JsonConvert.toJson(params.get("cardInfo")));

        List<Map<String, Object>> cardInfos = checkAnotherDeviceCard(user, params);
        List<Map<String, Object>> cards = mapper.getCard(mapperParam);
        int totalCnt = mapper.getCardCount(mapperParam);
        
        List<Object> items = Lists.newArrayList();
        
        /*
         * 앱 카드 데이터 와 서버 카드 데이터가 연결되면 저장 
         *
         * */
        if (cards != null && cards.size() > 0) {
          for (int i = 0; i < cards.size(); i++) {
            logger.debug("card : " + JsonConvert.toJson(cards.get(i)));
            Map<String, Object> data = convertCamelcase(cards.get(i));
            Long cardId = ((BigInteger) data.get("id")).longValue();
            
            for (int j = 0; (cardInfos != null && j < cardInfos.size()); j++) {
              Object id = cardInfos.get(j).get("id");
              Long cardInfoId = 0l;
              if (id instanceof Integer) {
                cardInfoId = ((Integer) cardInfos.get(j).get("id")).longValue();
              } else if (id instanceof Double) {
                cardInfoId = ((Double) cardInfos.get(j).get("id")).longValue();
              }
              logger.debug("card 2: " + JsonConvert.toJson(cardInfos.get(j)));  
              if (cardId.equals(cardInfoId)) {
                Map<String, Object> item = Maps.newHashMap();
                String cardInfo = String.valueOf(cardInfos.get(j).get("cardInfo")) + String.valueOf(data.get("cardInfo"));

                logger.debug("app card : " + JsonConvert.toJson(cardInfos.get(j).get("cardInfo")));
                logger.debug("server card : " + JsonConvert.toJson(data.get("cardInfo")));

                item.put("id",  cardId);
                item.put("cardInfo",  cardInfo);
                item.put("name",  data.get("name"));
                
                items.add(item);
                break;
              } 
            }
          }
        }
        
        Map<String, Object> obj = PagingUtil.setDefaultValuesForList(items, totalCnt, pageNo, params);
        
        AppJsonResult result = new AppJsonResult();
        Header header = new Header();
        
        header.setErrCd(ErrorCodes.SUCCESS.getCode());
        result.setHeader(header);
        result.setData(obj);
        return result;
      } catch (Exception e) {
        logger.error("[card] fail !!", e);
        throw e;
      }
    }
  
  @Transactional
  @RequestMapping("/card/create")
  @ResponseBody
  public AppJsonResult createCard(
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, Object> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, Object>>() {});
    Map<String, String> cardParam = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    
    logger.debug("param : " + JsonConvert.toJson(param));
    Locale locale = getLocale(String.valueOf(param.get("lang")));
    try {
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      
      UserCardMapper mapper = sessionTemplate.getMapper(UserCardMapper.class);
      UserCardExample example = new UserCardExample();
      
      Map<String, Object> data = Maps.newHashMap();
      User user = getCurrentUser("me", locale);
      List<Map<String, Object>> cardInfos = checkAnotherDeviceCard(user, cardParam);
      StringBuffer cardInfo = new StringBuffer();
      String cardNo = AES256Cipher.decodeAES256((String)param.get("cardNo"));
      String encryptCardInfo = "";
      String decryptCardInfo = "";
      boolean isDuplicate = false;
      
      example.createCriteria()
        .andUserIdEqualTo(user.getId());
      List<UserCard> userCards = mapper.selectByExample(example);
      
      /*
       * 디비에 등록된 유저 카드 정보와 앱에서 보내준 유저 카드가 매칭이 되면 중복
       * */
      if (userCards != null && userCards.size() > 0) {
        if (!GenericValidator.isBlankOrNull((String) param.get("cardInfo"))) {
          for (UserCard userCard : userCards) {
            for (int i = 0; (cardInfos != null && i < cardInfos.size()); i++) {
              try {
                Map<String, Object> cardData = cardInfos.get(i);
                logger.debug("1 : " + (String) cardData.get("cardInfo") + ", 2 : " + userCard.getCardInfo());
                decryptCardInfo = AES256Cipher.decodeAES256((String) cardData.get("cardInfo") + userCard.getCardInfo());
                String[] info = decryptCardInfo.split("\\|");
                if (cardNo.equals(AES256Cipher.decodeAES256(info[0]))) {
                  isDuplicate = true;
                  break;
                }
              } catch (javax.crypto.IllegalBlockSizeException e) {
                // 카드정보가 매칭이 안되면 해당 에러 발생됨..
              }
            }
            if (isDuplicate) {
              break;
            }
          }
        } 
      }
      
      if (isDuplicate) {
        throw new DataDuplicateException("userCard", cardNo, locale);
      }
      
      cardInfo.append(param.get("cardNo"));
      cardInfo.append("|");
      cardInfo.append(param.get("secretCode"));
      cardInfo.append("|");
      cardInfo.append(param.get("expireMonth"));
      cardInfo.append("|");
      cardInfo.append(param.get("expireYear"));
      cardInfo.append("|");
      cardInfo.append(param.get("cardOwnerName"));
      
      encryptCardInfo = AES256Cipher.encodeAES256(cardInfo.toString());
      
      UserCard card = new UserCard();
      card.setUserId(user.getId());
      card.setName((String)param.get("cardName"));
      card.setCreated(new Date());
      card.setUpdated(new Date());
      mapper.insertSelective(card);
      
      logger.debug("card : " + encryptCardInfo);
      logger.debug("card 2: " + JsonConvert.toJson(card));
      example = new UserCardExample();
      example.createCriteria().andIdEqualTo(card.getId());
      List<UserCard> cards = mapper.selectByExample(example);
      if (cards != null && cards.size() > 0) {
        card = cards.get(0);
        String[] cardEncrypt = {
            encryptCardInfo.substring(0,  encryptCardInfo.length() / 2),
            encryptCardInfo.substring(encryptCardInfo.length() / 2, encryptCardInfo.length())
        };
        
        card.setCardInfo(cardEncrypt[1]);
        mapper.updateByExample(card,  example);
        
        data.put("id",  card.getId());
        data.put("cardInfo", encryptCardInfo);
        data.put("cardName", card.getName());
        result.setData(data);
      } else {
        String[] errParam = {"cardNo"};
        throw new InvalidParamException(errParam, locale);
      }

      result.setHeader(header);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[createCard] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(params));
      throw invalidParam;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[createCard] fail !! [{}]", params);
      throw e;
    }
  }
  
  @RequestMapping("/card/delete")
  @ResponseBody
  public AppJsonResult deleteCard(
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, String> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, String>>() {});
    Locale locale = getLocale(param.get("lang"));
    try {
      Long cardId;
      if (param.containsKey("cardId")  && param.get("cardId").length() > 0) {
        cardId = Long.parseLong(param.get("cardId"));
       logger.debug("delete card id : " + cardId);
      } else {
        String[] errParam = {"cardId"};
        throw new InvalidParamException(errParam, locale);       
      }
      UserCardMapper mapper = sessionTemplate.getMapper(UserCardMapper.class);
      mapper.deleteByPrimaryKey(cardId);
  
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(true);
      
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[deleteCard] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(param));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[deleteCard] fail !!", e);
      throw e;
    }
  }
  
  @RequestMapping("/card/check")
  @ResponseBody
  public AppJsonResult validCard(
      @RequestParam("params") String params,
      HttpServletRequest request) throws Exception {
    Map<String, Object> param = JsonConvert.JsonConvertObject(params, new TypeReference<Map<String, Object>>() {});
    Locale locale = getLocale(String.valueOf(param.get("lang")));
    try {
      User user = getCurrentUser("me",  locale);
      UserCard card = null;
      if (param.containsKey("card")) {
        card = JsonConvert.JsonConvertObject(JsonConvert.toJson(param.get("card")), new TypeReference<UserCard>() {});
      } else {
        String[] errParam = {"card"};
        throw new InvalidParamException(errParam, locale);       
      }
      
      PayBean payBean = new PayBean();
//      payBean.setTransactionId("");
//    payBean.setCurrency("");
      payBean.setCardHolderName(card.getOwnerName());
      payBean.setCardNumber(card.getCardNo());
      payBean.setCardExpDate(card.getExpireMonth() + card.getExpireYear());
      payBean.setCardCvv(card.getSecretCode());
      payBean.setCountry(user.getCountry());
      
     Map<String, String> response = PGHandler.checkCardAuth(payBean);
      
      AppJsonResult result = new AppJsonResult();
      Header header = new Header();
      header.setErrCd(ErrorCodes.SUCCESS.getCode());
      result.setHeader(header);
      result.setData(response);
      return result;
    } catch(InvalidParamException invalidParam) {
      String code = Error.ErrorCodes.INVALID_PARAM.getCode();
      String err = invalidParam.getParams()[0];
      logger.error("[validCard] [" + code + "] " + "[" + messageSource.getMessage(code, new String[]{err}, locale) + "]" +" , param : " + JsonConvert.toJson(params));
      throw invalidParam;
    } catch (Exception e) {
      logger.error("[validCard] fail !! [{}]", params);
      throw e;
    }
  }
  
  @RequestMapping(value = {"/setting/{idAttribute}"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
  @ResponseBody
  public JsonResult setting(@PathVariable("idAttribute") String idAttribute,
                            @RequestBody(required = false) Map<String, Object> modData,
                            HttpServletRequest request) throws Exception {
    logger.debug("setting - " + idAttribute + ", data = " + JsonConvert.toJson(modData));
    User user = getCurrentUser("me", null);
    SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
    SvcBrand brand = brandMapper.selectByPrimaryKey(Globals.PICO_BRAND_ID);  // TODO 브랜드 상관없이 처리되어야 됨

    HashMap<String, String> notiTypes = Maps.newHashMap();
    notiTypes.put(codeUtil.getBaseCodeByAlias("noti-tp-sms"), "noti-tp-sms");
    notiTypes.put(codeUtil.getBaseCodeByAlias("noti-tp-push"), "noti-tp-push");

    UserNotiMapper mapper = sessionTemplate.getMapper(UserNotiMapper.class);
    UserNotiExample example = new UserNotiExample();
    example.createCriteria()
        .andUserIdEqualTo(user.getId())
        .andBrandIdEqualTo(brand.getId())
        .andNotiTpIn(Lists.newArrayList(notiTypes.keySet()));

    List<UserNoti> settings = mapper.selectByExample(example);
    Map<String, Boolean> map = Maps.newHashMap();

    JsonResult result = new JsonResult();

    HttpMethod method = HttpMethod.valueOf(request.getMethod());
    switch (method) {
      case GET: {
        Set<String> keys = notiTypes.keySet();
        for (String code : keys) {
          String alias = notiTypes.get(code);
          boolean exists = false;
          if (settings != null && settings.size() > 0) {
            for (UserNoti noti : settings) {
              if (code.equals(noti.getNotiTp())) {
                map.put(alias, noti.getUsed());
                exists = true;
                break;
              }
            }
          }

          if (!exists) {
            map.put(alias, false);
          }
        }
      }
      break;
      case POST:
      case PUT:
      case PATCH: {
        Set<String> keys = notiTypes.keySet();
        for (String code : keys) {
          String alias = notiTypes.get(code);
          boolean exists = false;
          if (settings != null && settings.size() > 0) {
            for (UserNoti noti : settings) {
              if (code.equals(noti.getNotiTp())) {
                Object obj = modData.get(alias);
                boolean used = false;
                if (obj != null && "true".equals(obj.toString())) {
                  used = true;
                }
                noti.setUsed(used);
                mapper.updateByPrimaryKey(noti);
                exists = true;
                break;
              }
            }
          }
          if (!exists) {
            UserNoti noti = new UserNoti();
            noti.setUserId(user.getId());
            noti.setBrandId(brand.getId());
            noti.setUsed(false);
            noti.setNotiTp(code);
            mapper.insertSelective(noti);
          }
        }
      }
      break;
      default: {
        throw new HttpRequestMethodNotSupportedException(method.toString());
      }
    }

    result.setSuccess(true);
    result.setBean(map);

    return result;
  }

  @Transactional
  @RequestMapping(value = "/beacon")
  @ResponseBody
  public AppJsonResult beacon(@RequestHeader("Service-Identifier") Long serviceId,
                           @RequestHeader("Device-Identifier") String deviceId,
      @RequestBody Beacon beacon, BindingResult errors) throws Exception {
    CustomMapper customMapper = sessionTemplate.getMapper(CustomMapper.class);
    SvcStoreBeaconMapper mapper = sessionTemplate.getMapper(SvcStoreBeaconMapper.class);
    SvcBeaconMapper beaconMapper = sessionTemplate.getMapper(SvcBeaconMapper.class);
    SvcStoreMapper storeMapper = sessionTemplate.getMapper(SvcStoreMapper.class);
    SvcBrandMapper brandMapper = sessionTemplate.getMapper(SvcBrandMapper.class);
    SvcStoreUserMapper storeUserMapper = sessionTemplate.getMapper(SvcStoreUserMapper.class);
    SvcStoreUserExample storeUserExample = new SvcStoreUserExample();

    User user = getCurrentUser("me", null);
    AppJsonResult result = new AppJsonResult();
    Header header = new Header();
    Map<String, Object> data = Maps.newHashMap();
    String vip = codeUtil.getBaseCodeByAlias("user-level-vip");
    String vvip = codeUtil.getBaseCodeByAlias("user-level-vvip");
    int beaconPushTerm = config.getInt("beacon.push.term");
    long curTime = System.currentTimeMillis();

    SvcBeaconExample beaconExample = new SvcBeaconExample();
    beaconExample.createCriteria()
        .andServiceIdEqualTo(serviceId)
        .andUuidEqualTo(beacon.getUUID())
        .andMajorEqualTo(beacon.getMajor())
        .andMinorEqualTo(beacon.getMinor());

    // 유저 휴대폰에 매칭된 비콘이 데이터베이스에 등록된 비콘인지 확인
    List<SvcBeacon> svcBeacons = beaconMapper.selectByExample(beaconExample);
    SvcBeacon svcBeacon = null;
    if (svcBeacons != null && svcBeacons.size() > 0) {
      svcBeacon = svcBeacons.get(0);
    } else {
      logger.warn("beacon not registered! - uuid: {}, major : {}, minor : {}", beacon.getUUID(), beacon.getMajor(),
          beacon.getMinor());
      throw new DataNotRegisteredException("beacon",
          String.format("uuid:%s, major: %d, minor: %d", beacon.getUUID(), beacon.getMajor(), beacon.getMinor()), null);
    }

    // 비콘이 매장과 매핑이 된 비콘인지 확인
    // find store
    SvcStoreBeaconExample example = new SvcStoreBeaconExample();
    example.createCriteria()
          .andServiceIdEqualTo(serviceId)
          .andBeaconIdEqualTo(svcBeacon.getId())
          .andStoreIdEqualTo(svcBeacon.getStoreId());

    List<SvcStoreBeacon> stores = mapper.selectByExample(example);
    // 매장과 매핑된 비콘일 떄
    if (stores != null && stores.size() > 0) {
      logger.debug("stores : " + JsonConvert.toJson(stores.get(0)));
      SvcStore store = storeMapper.selectByPrimaryKey(stores.get(0).getStoreId());
      SvcBrand brand = brandMapper.selectByPrimaryKey(stores.get(0).getBrandId());

      storeUserExample.createCriteria()
          .andBrandIdEqualTo(brand.getId())
          .andStoreIdEqualTo(store.getId())
          .andUserIdEqualTo(user.getId());
      
      // 매장에 등록된 유저인지 확인
      List<SvcStoreUser> storeUsers = storeUserMapper.selectByExample(storeUserExample);
      if (storeUsers != null && storeUsers.size() > 0) {
        SvcStoreUser storeUser = storeUsers.get(0);
        logger.debug("stores : " + JsonConvert.toJson(storeUsers));
        logger.debug("user : " + JsonConvert.toJson(user));
        
        // user status (vip, vvip) and isBluetooth or isPush
        if ((vip.equals(storeUser.getLevel()) || vvip.equals(storeUser.getLevel()))
            && (user.getIsSvcBluetooth() && user.getIsSvcPush())) {
          // beacon push term
          Map<String, Object> beaconLogParam = Maps.newHashMap();
          beaconLogParam.put("brandId", storeUser.getBrandId());
          beaconLogParam.put("storeId", storeUser.getStoreId());
          beaconLogParam.put("userId", storeUser.getUserId());

          List<Map<String, Object>> beaconLogs = customMapper.getStoreBeaconLog(beaconLogParam);
          // store beacon log 체크
          SvcStoreBeaconLogMapper beaconLogMapper = sessionTemplate.getMapper(SvcStoreBeaconLogMapper.class);
          SvcStoreBeaconLog beaconLog = null;
          boolean isBeaconPush = false;
          if (beaconLogs == null || beaconLogs.size() == 0) {  // 유저의 beaconLog 데이터가 없을 때
            isBeaconPush = true;
          } else if (beaconLogs != null && beaconLogs.size() > 0) {
              beaconLog = JsonConvert.JsonConvertObject(JsonConvert.toJson(convertCamelcase(beaconLogs.get(0))),new TypeReference<SvcStoreBeaconLog>() {});
              if (beaconLog.getLastBeaconPush() != null && curTime > beaconLog.getLastBeaconPush().getTime() + beaconPushTerm) {
                isBeaconPush = true;
              } else {
                data.put("isBeaconPush", false);
                data.put("ex", "beacon push term is exceed");
              }
          }
          logger.debug("isBeaconPush : " + isBeaconPush);
          logger.debug("user : " + JsonConvert.toJson(beaconLog));
          
          if (isBeaconPush) {
            Locale locale = getUserDeviceLocale();
            logger.debug("beacon logs : locale : " + locale + "   brand : " + brand.getId() + ", store id : " + store.getId() + ", logs : " + JsonConvert.toJson(beaconLog));
            sendPushToUser(brand, store, svcBeacon, beaconLog, locale);
            sendPushToOwner(brand, store, locale);
            
            data.put("isBeaconPush", true);
            data.put("ex", "");
          }
        } else {
          data.put("isBeaconPush", false);
          data.put("ex", "maybe.. user is not vip or disable bluetooth or disable push");
        }
      } else {
        data.put("isBeaconPush", false);
        data.put("ex", "store user is not register");
      }

    } else {
        data.put("isBeaconPush", false);
        data.put("ex", "store beacon is not register");
      }  

    header.setErrCd(Error.ErrorCodes.SUCCESS.getCode());
    result.setHeader(header);
    result.setData(data);
    logger.debug("beacon result : " + JsonConvert.toJson(result));
    return result;
  }

  @RequestMapping("/push/test")
  @ResponseBody
  public AppJsonResult sendPushToStoreOwner(@RequestParam("userId") Long userId, @RequestParam("orderId") Long orderId) throws Exception {
    UserMapper userMapper = sessionTemplate.getMapper(UserMapper.class);
    UserExample userExample = new UserExample();
    userExample.createCriteria().andIdEqualTo(userId);
    List<User> users = userMapper.selectByExample(userExample);
    
    if (users != null && users.size() > 0) {
      User user = users.get(0);
      SvcUserMappingMapper mappingMapper = sessionTemplate.getMapper(SvcUserMappingMapper.class);
      SvcUserMappingExample mappingExample = new SvcUserMappingExample();
      mappingExample.createCriteria().andUserIdEqualTo(user.getId());
      List<SvcUserMapping> maps = mappingMapper.selectByExample(mappingExample);
      
      if (maps != null && maps.size() > 0) {
        String android = codeUtil.getBaseCodeByAlias("android");
        String ios = codeUtil.getBaseCodeByAlias("ios");
        SvcUserMapping userMap = maps.get(0);
        
        UserDeviceMapper deviceMapper = sessionTemplate.getMapper(UserDeviceMapper.class);
        UserDeviceExample deviceExample = new UserDeviceExample();
        deviceExample.createCriteria()
        .andUserIdEqualTo(userId)
        .andIsAliveEqualTo(true)
        .andPushIdIsNotNull()
        .andPushIdNotEqualTo("");
        
        List<UserDevice> userDevices = deviceMapper.selectByExample(deviceExample);
        
        if (userDevices != null && userDevices.size() > 0) {
          UserDevice dev = userDevices.get(0);
          Map<String, List<String>> pushIds = new HashMap<String, List<String>>();
          List<String> gcmIds = Lists.newArrayList();
          List<String> apnIds = Lists.newArrayList();
          SendPush push = new SendPush();
          push.setBrandId(userMap.getBrandId());
          if (android.equals(dev.getOs())) {
            gcmIds.add(dev.getPushId());
          } else if (ios.equals(dev.getOs())) {
            apnIds.add(dev.getPushId());
          }
          
          logger.debug("gcmIds(owner) = " + gcmIds);
          logger.debug("apnIds(owner) = " + apnIds);
          if (gcmIds.size() > 0) {
            pushIds.put("android", gcmIds);
          }
          if (apnIds.size() > 0) {
            pushIds.put("ios", apnIds);
          }
          
          push.setPushIds(pushIds);
          
          SvcOrderMapper orderMapper = sessionTemplate.getMapper(SvcOrderMapper.class);
          SvcOrderExample orderExample = new SvcOrderExample();
          orderExample.createCriteria().andIdEqualTo(orderId);
          
          List<SvcOrder> orders = orderMapper.selectByExample(orderExample);
          if (orders != null && orders.size() > 0) {
            SvcOrder order = orders.get(0);
            push.addExtra("order",  order);
           
            SvcCctvLogMapper logMapper = sessionTemplate.getMapper(SvcCctvLogMapper.class);
            SvcCctvLogExample logExample = new SvcCctvLogExample();
            logExample.createCriteria().andOrderIdEqualTo(order.getId());
            
            List<SvcCctvLog> logs = logMapper.selectByExample(logExample);
            if (logs != null && logs.size() > 0) {
              push.addExtra("event", logs.get(0));
              push.setType(logs.get(0).getEventTp());
              String eventTp = codeUtil.getTitleByCode(logs.get(0).getEventTp());
              
              SvcStoreMapper storeMapper = sessionTemplate.getMapper(SvcStoreMapper.class);
              SvcStoreExample storeExample = new SvcStoreExample();
              storeExample.createCriteria().andIdEqualTo(order.getStoreId());
              
              List<SvcStore> stores = storeMapper.selectByExample(storeExample);
              if (stores != null && stores.size() > 0) {
                SvcStore store = stores.get(0);
                push.setTitle("[BAK_store] event");
                push.setContent(String.format("[%s] %s", store.getStoreNm(), eventTp));
                pushSender.send(true,  push);
                logger.debug("push info : " + JsonConvert.toJson(push));
                AppJsonResult result = new AppJsonResult();
                Map<String, Object> data = Maps.newHashMap();
                data.put("data",  push);
                result.setData(data);
                return result;
              } else {
                logger.info("sendPushToStoreOwner - store is null");
                AppJsonResult result = new AppJsonResult();
                Map<String, Object> data = Maps.newHashMap();
                data.put("data",  push);
                result.setData(data);
                return result;
              }
            } else {
              logger.info("sendPushToStoreOwner - cctv logs is null");
              AppJsonResult result = new AppJsonResult();
              Map<String, Object> data = Maps.newHashMap();
              data.put("data",  "cctv logs is null");
              result.setData(data);
              return result;
            }
          } else {
            logger.info("sendPushToStoreOwner - order is null");
            AppJsonResult result = new AppJsonResult();
            Map<String, Object> data = Maps.newHashMap();
            data.put("data",  "order is null");
            result.setData(data);
            return result;
          }
        } else {
          logger.info("sendPushToStoreOwner - user device is null");
          
          AppJsonResult result = new AppJsonResult();
          Map<String, Object> data = Maps.newHashMap();
          data.put("data",  "user device is null");
          result.setData(data);
          return result;
        }
      } else {
        logger.info("sendPushToStoreOwner - user mapping is null");
        AppJsonResult result = new AppJsonResult();
        Map<String, Object> data = Maps.newHashMap();
        data.put("data",  "user mapping is null");
        result.setData(data);
        return result;
      }
    } else {
      logger.info("sendPushToStoreOwner - user is null");
      AppJsonResult result = new AppJsonResult();
      Map<String, Object> data = Maps.newHashMap();
      data.put("data",  "user is null");
      result.setData(data);
      return result;
    }
  }
  
  private Locale getUserDeviceLocale() throws Exception {
    User user = getCurrentUser("me", null);
    Locale locale = null;
    UserDeviceMapper mapper = sessionTemplate.getMapper(UserDeviceMapper.class);
    UserDeviceExample example = new UserDeviceExample();
    example.createCriteria()
      .andIsAliveEqualTo(true)
      .andUserIdEqualTo(user.getId());
    example.setOrderByClause("CREATED DESC");
    List<UserDevice> devices = mapper.selectByExample(example);
    if (devices != null && devices.size() > 0) {
      String language = devices.get(0).getLocale();
      if (language.equals(Locale.US.toString())) {
        locale = Locale.US;
      } else if (language.equals((new Locale("ru", "RU").toString()))) {
        locale = new Locale("ru", "RU");
      } else {
        locale = Locale.US;
      }
    } else {
      locale = Locale.US;
    }
    return locale;
  }
  
  private Locale getLocale(String language) throws Exception {
    Locale locale = null;
    if (language == null || language.length() == 0) {
      locale = getUserDeviceLocale();
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
  
  private void sendPushToOwner(SvcBrand brand, SvcStore store, Locale locale) throws Exception {
    User user = getCurrentUser("me", null);

    if (user == null) {
      logger.info("sendPushToUser - user is null");
      return;
    }
    if (brand == null) {
      logger.info("sendPushToUser - brand is null");
      return;
    }
    if (store == null) {
      logger.info("sendPushToUser - store is null");
      return;
    }

    // message-tp-customer-comming -> event-type-visit-vip 로 변경    
    storeNotificationService.notifyVisitVip(store, user, locale);    
  }

  private void sendPushToUser(SvcBrand brand, SvcStore store, SvcBeacon beacon, SvcStoreBeaconLog beaconLog, Locale locale) throws Exception {
    SvcStoreBeaconLogMapper beaconLogMapper = sessionTemplate.getMapper(SvcStoreBeaconLogMapper.class);
    SvcStoreBeaconLogExample beaconLogExample = new SvcStoreBeaconLogExample();
    User user = getCurrentUser("me", null);

    if (user == null) {
      logger.info("sendPushToUser - user is null");
      return;
    }
    if (store == null) {
      logger.info("sendPushToUser - store is null");
      return;
    }

    String android = codeUtil.getBaseCodeByAlias("android");
    String ios = codeUtil.getBaseCodeByAlias("ios");

    UserDeviceMapper mapper = sessionTemplate.getMapper(UserDeviceMapper.class);
    UserDeviceExample example = new UserDeviceExample();
    example.createCriteria()
        .andIsAliveEqualTo(true)
        .andUserIdEqualTo(user.getId())
        .andPushIdIsNotNull()
        .andPushIdNotEqualTo("");

    List<UserDevice> devices = mapper.selectByExample(example);
    if (devices != null && devices.size() > 0) {
      // send push
      SendPush push = new SendPush();
      push.setBrandId(brand.getId());
      Map<String, List<String>> pushIds = Maps.newHashMap();
      List<String> gcmIds = Lists.newArrayList();
      List<String> apnIds = Lists.newArrayList();
      logger.debug("devices = " + devices);
      for (UserDevice dev : devices) {
        if (android.equals(dev.getOs())) {
          gcmIds.add(dev.getPushId());
        } else if (ios.equals(dev.getOs())) {
          apnIds.add(dev.getPushId());
        }
      }

      logger.debug("gcmIds(user) = " + gcmIds);
      logger.debug("apnIds(user) = " + apnIds);
      if (gcmIds.size() > 0) {
        pushIds.put("android", gcmIds);
      }
      if (apnIds.size() > 0) {
        pushIds.put("ios", apnIds);
      }

      push.setPushIds(pushIds);
      push.setTitle(messageSource.getMessage("app.push.event-type-visit-vip.title",  new String[]{}, locale));
      push.setContent(messageSource.getMessage("app.push.event-type-visit-vip.msg",  new String[]{store.getStoreNm()}, locale));
      push.setType(codeUtil.getBaseCodeByAlias("message-tp-welcome"));
      push.addExtra("storeId", store.getId());
      push.addExtra("storeNm", store.getStoreNm());
      pushSender.send(false, push);

      if (beaconLog == null) {
        beaconLog = new SvcStoreBeaconLog();
        beaconLog.setBeaconId(beacon.getId());
        beaconLog.setBrandId(brand.getId());
        beaconLog.setStoreId(store.getId());
        beaconLog.setUserId(user.getId());
        beaconLog.setCreated(new Date());
        beaconLog.setUpdated(new Date());
        beaconLog.setLastBeaconPush(new Date());
        beaconLogMapper.insertSelective(beaconLog);
      } else {
        beaconLogExample.createCriteria().andIdEqualTo(beaconLog.getId());
        beaconLog.setLastBeaconPush(new Date());
        beaconLogMapper.updateByExampleSelective(beaconLog, beaconLogExample);
        
        beaconLogExample = new SvcStoreBeaconLogExample();
        beaconLogExample.createCriteria().andIdNotEqualTo(beaconLog.getId());
        beaconLogMapper.deleteByExample(beaconLogExample);
      }
     
    } else {
      logger.warn("sendPushToUser - user device not exists");
      throw new DataNotRegisteredException("device", "me", null);
    }
  }
  
  /**
   * if 앱에 등록된 카드 id 추출
   * else 앱에서 보내준 카드 정보가 없는데 디비에 카드 정보가 있으면 앱 재설치로 판단, 디비에 있는 유저 카드 정보삭제
   * */
  private List<Map<String, Object>> checkAnotherDeviceCard(User user, Map<String, String> params ) {
    List<Map<String, Object>> cardInfos = null;
    try {
      UserCardMapper cardMapper = sessionTemplate.getMapper(UserCardMapper.class);
      UserCardExample example = new UserCardExample();
      StringBuffer ids = new StringBuffer();
  
      example.createCriteria().andUserIdEqualTo(user.getId());
      if (!GenericValidator.isBlankOrNull(params.get("cardInfo"))) { // 앱에서 보내준 카드정보가 있을 때
        List<UserCard> userCards = cardMapper.selectByExample(example);
        
        cardInfos = JsonConvert.JsonConvertObject(params.get("cardInfo"), new TypeReference<List<Map<String, Object>>>() {});
        logger.debug("card : " + JsonConvert.toJson(cardInfos));
        if (cardInfos != null && cardInfos.size() > 0) {
          // 앱에서 보내준 카드 아이디 추출
          for (int i = 0; i < cardInfos.size(); i++) {
            Map<String, Object> data = cardInfos.get(i);
            String id = String.valueOf(data.get("id"));
            if (id.contains(".")) {
              id = id.replace(id.substring(id.indexOf("."), id.length()), "");
            }
            logger.debug("card : " + i + " " + JsonConvert.toJson(cardInfos.get(i)));
            ids.append(id);
            if (i + 1 < cardInfos.size()) {
              ids.append(",");
            }
          }
          // 앱에서 보낸 카드 아이디와 다른 카든 아이디가 있을 때 삭제
          String[] cardIds = ids.toString().split(",");
          logger.debug("user id : " + user.getId());
          logger.debug("ids : " + ids.toString());
          logger.debug("cardIds size : " + cardIds.length);
          logger.debug("user card size : " + userCards.size());
          for (int j = 0; (userCards != null && j < userCards.size()); j++) {
            UserCard card = userCards.get(j);
            boolean isMatched = false;
  
            for (int i = 0; (cardIds != null && i < cardIds.length); i++) {
              Long cardId = Long.parseLong(cardIds[i]);
              // 앱 카드 아이디와 서버 카드 아이디가 같을 때
              logger.debug("UserCard : " + card.getId() + ", cardId : " + cardId);
              if (cardId.equals(card.getId())) {
                isMatched = true;
                break;
              }
            }
  
            // 앱 카드 아이디와 서버 카드 아이디가 매칭이 되는 것이 없을 때 (쓰레기 데이터 삭제)
            if (!isMatched) {
              example = new UserCardExample();
              example.createCriteria().andIdEqualTo(card.getId());
              cardMapper.deleteByExample(example);
            }
          }
        }
        return cardInfos;
      } else { // 앱에서 보낸 카드정보가 없을 때, 유저의 모든 카드 삭제
        cardMapper.deleteByExample(example);
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("[{}][{}][{}]", "checkAnotherDeviceCard", "user : " + JsonConvert.toJson(user), "cardInfos : " + cardInfos);
      return null;
    }
  }
}
