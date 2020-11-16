/*
 * Filename	: AdminModelController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcCoupon;
import com.jc.pico.bean.SvcCouponExample;
import com.jc.pico.bean.SvcCouponPromotion;
import com.jc.pico.bean.SvcCouponWithBLOBs;
import com.jc.pico.bean.SvcMailLogWithBLOBs;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcOrderItemExample;
import com.jc.pico.bean.SvcSmsLogWithBLOBs;
import com.jc.pico.bean.SvcStampLog;
import com.jc.pico.bean.SvcUserStamp;
import com.jc.pico.bean.SvcUserStampExample;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserDeviceExample;
import com.jc.pico.bean.UserExample;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.ext.sms.SMSUtil;
import com.jc.pico.mapper.BaseBcodeMapper;
import com.jc.pico.mapper.BaseMcodeMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcBrandSetMapper;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcCouponPromotionMapper;
import com.jc.pico.mapper.SvcFranchiseMapper;
import com.jc.pico.mapper.SvcMailLogMapper;
import com.jc.pico.mapper.SvcOrderItemMapper;
import com.jc.pico.mapper.SvcSmsLogMapper;
import com.jc.pico.mapper.SvcStampLogMapper;
import com.jc.pico.mapper.SvcStampMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcTermsMapper;
import com.jc.pico.mapper.SvcUserStampMapper;
import com.jc.pico.mapper.UserBackofficeMenuMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.mapper.UserGroupAuthMapper;
import com.jc.pico.mapper.UserGroupMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.queue.PushSender;
import com.jc.pico.queue.SmsSender;
import com.jc.pico.utils.BarcodeUtil;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.Config;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.bean.SendPush;
import com.jc.pico.utils.customMapper.admin.CustomMarketingMapper;
import com.jc.pico.utils.customMapper.admin.CustomRewardMapper;
import com.jc.pico.utils.jc.mail.MailSender;

@Controller
@ResponseBody
@RequestMapping("/admin/model/marketing")
public class AdminMarketingController {

  public static final String MESSAGE_TP_COUPON_ISSUE = "203005";
  
  protected static Logger logger = LoggerFactory.getLogger(AdminMarketingController.class);

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  MessageSource messageSource;
  
  @Autowired 
  CodeUtil codeUtil;

  @Autowired
  SvcFranchiseMapper svcFranchiseMapper;

  @Autowired
  SvcTermsMapper svcTermsMapper;

  @Autowired
  SvcBrandMapper svcBrandMapper;

  @Autowired
  SvcBrandSetMapper svcBrandSetMapper;

  @Autowired
  SvcStoreMapper svcStoreMapper;

  @Autowired
  SvcStampMapper svcStampMapper;

  @Autowired
  SvcCouponMapper svcCouponMapper;

  @Autowired
  SvcCouponPromotionMapper svcCouponPromotionMapper;
  
  @Autowired
  BaseMcodeMapper baseCodeMainMapper;
  
  @Autowired
  SvcOrderItemMapper svcOrderItemMapper;
  
  @Autowired
  SvcUserStampMapper svcUserStampMapper;
  
  @Autowired
  SvcStampLogMapper svcStampLogMapper;

  @Autowired
  BaseBcodeMapper baseCodeMapper;

  @Autowired
  UserBackofficeMenuMapper userBackofficeMenuMapper;

  @Autowired
  UserGroupMapper userGroupMapper;

  @Autowired
  UserGroupAuthMapper userGroupAuthMapper;

  @Autowired
  UserMapper userMapper;

  @Autowired
  CustomRewardMapper customRewardMapper;
  
  @Autowired
  CustomMarketingMapper customMarketingMapper;

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Autowired
  private HttpSession session;

  @Autowired
  PushSender pushSender;
  
  @Autowired
  SmsSender smsSender;
  
  @Autowired 
  BarcodeUtil barcodeUtil;
  
  private Config config = Config.getInstance();
  
  /**
   * Marketing – member
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/Member")
  public Map<Object,Object> Member(
      Principal principal,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    try {
      logger.debug("member : " + JsonConvert.toJson(params));
      Map<Object,Object> result = Maps.newHashMap();
      result.put("success", false);
  
      UserExample example = new UserExample();
      example.createCriteria().andUsernameEqualTo(principal != null ? principal.getName() : "");
  
      List<User> user = userMapper.selectByExample(example);
      
      switch (method) {
      case GET:
        if(!String.valueOf(params.get("draw")).equals("null")){
          List<String> userType = Arrays.asList(codeUtil.getBaseCodeByAlias("user"), codeUtil.getBaseCodeByAlias("associate-user"));
          
          Map<Object,Object> defaultParam = Maps.newHashMap();
  
          defaultParam.put("types", userType);
          params.put("types", userType);
  
          List<LinkedHashMap<String,Object>> userList = customMarketingMapper.getUserList(params, new RowBounds(start, length));
  
          result.put("list", userList);
          result.put("recordsTotal", customMarketingMapper.getCountUserList(params));
          result.put("recordsFiltered", customMarketingMapper.getCountUserList(params));
        } else {
          if(params.get("userId") != null){
            List<LinkedHashMap<String,Object>> userList = customMarketingMapper.getUserList(params, new RowBounds(start, length));
            
            if(userList.size() == 1){
              result.put("data", userList.get(0));
              result.put("success", true);
            } else {
              result.put("errMsg", "사용자를 찾을수 없습니다.");
            }
          }
        }
        logger.debug("member : " + JsonConvert.toJson(result));
        break;
      case PUT:
        	logger.debug("★★★★★★★★★★★★★★");
        	logger.debug("PUT");
        	logger.debug(params.toString());
        	logger.debug("★★★★★★★★★★★★★★");
        	
        	Map<String, Object> data = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, Object>>() {});
        	List<Map<String, Object>> userInfos = JsonConvert.JsonConvertObject(JsonConvert.toJson(data.get("checkArray")), new TypeReference<List<Map<String, Object>>>() {});
        	
        	logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
      		logger.debug("data : " + JsonConvert.toJson(data));
      		logger.debug("userInfos : " + JsonConvert.toJson(userInfos));
      		logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
      		
      		/*
      		 * - 유저 마케팅
      		 * case1 : Email 보내기
      		 * case2 : SMS 보내기
      		 * case3 : Push 보내기
      		 * */
      		if (data != null && data.get("status") != null) {
      		  String marketingMemberSendType= (String) data.get("status");
      		  String title = (String) data.get("title");
      		  String content = (String) data.get("content");
      		  
      		  // 이메일 발신
      		  switch (marketingMemberSendType) {
      		    case "email": {
                String host = config.getString("email.host");
                String port = config.getString("email.port");
                String contentType = config.getString("email.content.type");
                String charset = config.getString("email.content.charset");
                String supportAccount = config.getString("email.support.account");
                String password = config.getString("email.support.password");

                MailSender sendEmail = new MailSender(host, supportAccount, password, port);
                SvcMailLogMapper mailLogMapper = sessionTemplate.getMapper(SvcMailLogMapper.class);
                try {
                  sendEmail.connect();
                  sendEmail.setFrom(supportAccount);
                  sendEmail.setType(contentType);
                  sendEmail.setCharset(charset);
                  sendEmail.setSubject(title);
                  sendEmail.setText(content);
                  
                  for (Map<String, Object> userInfo : userInfos) {
                    sendEmail.setTo((String) userInfo.get("email"));
                    sendEmail.send();
                    
                    SvcMailLogWithBLOBs mailLog = new SvcMailLogWithBLOBs();
                    mailLog.setFrom(supportAccount);
                    mailLog.setTo((String) userInfo.get("email"));
                    mailLog.setHost(host);
                    mailLog.setPort(Integer.parseInt(port));
                    mailLog.setUsername(supportAccount);
                    mailLog.setTitle(title);
                    mailLog.setContent(content);
                    mailLog.setSuccess(true);
                    
                    mailLogMapper.insertSelective(mailLog);
                  }
                } catch (Exception e) {
                  logger.info("[ERROR] [AdminMarketingController] [/Member] send email fail !! [param : " + JsonConvert.toJson(sendEmail));
                  e.printStackTrace();
                }
              
      		    }
      		    break;
      		    case "sms": {
      		      // sms 발신
                StringBuffer from = new StringBuffer();
                from.append(config.getString("sms.surem.reqphone1")); // 발신자 번호1
                from.append(config.getString("sms.surem.reqphone2")); // 발신자 번호2
                from.append(config.getString("sms.surem.reqphone3")); // 발신자 번호3

                SvcSmsLogMapper smsLogMapper = sessionTemplate.getMapper(SvcSmsLogMapper.class);
                
                SvcSmsLogWithBLOBs smsLog = new SvcSmsLogWithBLOBs();
                try {
                  for (Map<String, Object> userInfo : userInfos) {
                    if ((Boolean) userInfo.get("isSvcSMS")) {
                      String mb = (String) userInfo.get("mb");
                      String mbCountryCd = (String) userInfo.get("mbCountryCd");
                      
                      smsLog = new SvcSmsLogWithBLOBs();
                      smsLog.setPathTp(codeUtil.getBaseCodeByAlias("app-tp-mobile"));
                      smsLog.setFrom(from.toString());
                      smsLog.setTo(mb);
                      smsLog.setTitle(title);
                      smsLog.setContent(content);
                      smsLog.setSuccess(false);
                      
                      smsLogMapper.insertSelective(smsLog);
                      
                      double random = Math.floor(Math.random() * 1000000)+100000;
                      if(random > 1000000){
                        random = random - 100000;
                      }
                      SMSUtil.sendIMS((int) random, mbCountryCd, StrUtils.toNumberOnly(mb), smsLog.getContent(), StrUtils.toNumberOnly(mb));
                    } 
                  }
                } catch (Exception e) {
                  logger.info("[ERROR] [AdminMarketingController] [/Member] send sms fail !! [param : " + JsonConvert.toJson(smsLog));
                  e.printStackTrace();
                }
      		    }
      		    break;
      		    case "push": {
      		      // push 발신
                try {
                  UserDeviceMapper userDeviceMapper = sessionTemplate.getMapper(UserDeviceMapper.class);
                  UserDeviceExample userDeviceExample = new UserDeviceExample();
                  
                  List<Long> ids = Lists.newArrayList();
                  for (Map<String, Object> userInfo : userInfos) {
                    if ((Boolean) userInfo.get("isSvcPush")) {
                      Long id = ((Integer) userInfo.get("id")).longValue();
                      ids.add(id);
                    }
                  }
                  userDeviceExample.createCriteria()
                      .andUserIdIn(ids)
                      .andIsAliveEqualTo(true)
                      .andPushIdIsNotNull()
                      .andPushIdNotEqualTo("");
                  
                  List<UserDevice> userDevices = userDeviceMapper.selectByExample(userDeviceExample);
                  if (userDevices != null && userDevices.size() > 0) {
                    String android = codeUtil.getBaseCodeByAlias("android");
                    String ios = codeUtil.getBaseCodeByAlias("ios");
                    Map<String, List<String>> pushIds = Maps.newHashMap();
                    List<String> gcmIds = Lists.newArrayList();
                    List<String> apnIds = Lists.newArrayList();
                    
                    logger.debug("push : " + JsonConvert.toJson(userDevices));
                    for (UserDevice userDevice : userDevices) {
                      if (android.equals(userDevice.getOs())) {
                        gcmIds.add(userDevice.getPushId());
                      } else if (ios.equals(userDevice.getOs())) {
                        apnIds.add(userDevice.getPushId());
                      }
                    }
                    
                    if (gcmIds.size() > 0) {
                      pushIds.put("android",  gcmIds);
                    }
                    
                    if (apnIds.size() > 0) {
                      pushIds.put("ios",  apnIds);
                    }
                    
                    PushSender pushSender = new PushSender();
                    SendPush push = new SendPush();
                    push.setPushIds(pushIds);
                    push.setBrandId(0l);   // admin
                    push.setTitle(title);
                    push.setContent(content);
                    pushSender.setSessionTemplate(sessionTemplate);
                    pushSender.send(false,  push);
                    
                  }
                } catch (Exception e) {
                  logger.info("[ERROR] [AdminMarketingController] [/Member] send push fail !! [param : " + JsonConvert.toJson(params));
                  e.printStackTrace();
                }
              
      		    }
      		    break;
      		    default :
      		      break;
      		  }
      		}
      		result.put("success", true);
      	break;
      default:
        break;
      }
      return result;
    } catch (Exception e) {
      logger.info("[ERROR] [AdminMarketingController] [/Member] [param : " + params + "]");
      e.printStackTrace();
      throw e;
    }
  }
  
  /**
   * Marketing – member => details 버튼 클릭시
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/StampLog")
  public Map<Object,Object> StampLog(
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
	  
    Map<Object,Object> result = Maps.newHashMap();
    Map<String,String> baseParam = new HashMap<>();	// 검색데이터
    Map<String,String> userInfo = new HashMap<>();	// 로그인 사용자 정보
    
    if (params.get("data") != null) {
    	baseParam = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	}
    
    if (params.get("userInfo") != null) {
    	userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(), new TypeReference<Map<String, String>>() {});
	}
    
    logger.info("★★★★★★★★★  Member Detail => StampLog ★★★★★★★★★★★");
    logger.info("params : " + JsonConvert.toJson(params));
    logger.info("baseParam : " + baseParam.toString());
    logger.info("userInfo : " + userInfo.toString());
    logger.info("★★★★★★★★★ Member Detail => StampLog ★★★★★★★★★★★");
    
    switch(method){
    case GET : 
      
      if(!String.valueOf(params.get("draw")).equals("null")){

        List<LinkedHashMap<String,String>> list = customRewardMapper.getStampList(baseParam, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", customRewardMapper.getCountStampList(baseParam));
        result.put("recordsFiltered", customRewardMapper.getCountStampList(baseParam));
      } else {

        List<LinkedHashMap<String, String>> stampLog = customRewardMapper.getStampList(baseParam, new RowBounds(0, 1));
        
        SvcOrderItemExample example = new SvcOrderItemExample();
        example
          .createCriteria()
          .andOrderIdEqualTo(stampLog.get(0).get("orderId") != null ? Long.parseLong(stampLog.get(0).get("orderId").toString()) : null);
        
        List<SvcOrderItem> orderItems = svcOrderItemMapper.selectByExample(example);
        result.put("data", stampLog);
        result.put("orders", orderItems);
      }

      result.put("success", true);

      break;
    case DELETE :
      String orderId = params.get("id") != null ? params.get("id").toString() : null;
      
      if(orderId != null){
        List<LinkedHashMap<String, String>> stampLog = customRewardMapper.getStampList(baseParam, new RowBounds(0, 1));

        if(stampLog.size() == 1){
          if(stampLog.get(0).get("logTp") != null && stampLog.get(0).get("logTp").toString().equals("401001")){

            SvcUserStampExample example = new SvcUserStampExample();
            example
              .createCriteria()
              .andOrderIdEqualTo(Long.parseLong(orderId));
            
            List<SvcUserStamp> stamps = svcUserStampMapper.selectByExample(example);
            
            for(SvcUserStamp _stamp : stamps){
              
              SvcStampLog _stampLog = new SvcStampLog();
              _stampLog.setStampId(_stamp.getId());
              _stampLog.setUserId(_stamp.getUserId());
              _stampLog.setBrandId(_stamp.getBrandId());
              _stampLog.setOrderId(_stamp.getOrderId());
              _stampLog.setStoreId(_stamp.getStoreId());
              _stampLog.setExpire(_stamp.getExpire());
              _stampLog.setLogTp("401002");
              _stampLog.setUserAgent("");
              _stampLog.setDeviceId("");
              _stampLog.setClientId("");
              _stampLog.setGrantType("");

              svcStampLogMapper.insertSelective(_stampLog);
            }
            
            result.put("success", true);

          } else {
            result.put("errMsg", "이미 적립 취소된 항목입니다.");
          }
        } else {
          result.put("errMsg", "orderId not found");
        }
        
        
      } else {
        result.put("errMsg", "orderId not null");
      }
      
      break;
    default : 
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Marketing – member => details 버튼 클릭시
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  
  @RequestMapping("/CouponLog")
  public Map<Object,Object> CouponLog(
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {
    Map<Object,Object> result = Maps.newHashMap();
    Map<String,String> baseParam = new HashMap<>();	// 검색데이터
    Map<String,String> userInfo = new HashMap<>();	// 로그인 사용자 정보
    
    if (params.get("data") != null) {
    	baseParam = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	}
    
    if (params.get("userInfo") != null) {
    	userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(), new TypeReference<Map<String, String>>() {});
	}

    logger.info("★★★★★★★★★  Member Detail =>  CouponLog ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("baseParam : " + baseParam.toString());
    logger.info("userInfo : " + userInfo.toString());
    logger.info("★★★★★★★★★  Member Detail =>  CouponLog ★★★★★★★★★★★");
    
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){
  
        List<LinkedHashMap<String,String>> list = customMarketingMapper.getCouponList(baseParam, new RowBounds(start, length));
  
        result.put("list", list);
        result.put("recordsTotal", customMarketingMapper.getCountCouponList(baseParam));
        result.put("recordsFiltered", customMarketingMapper.getCountCouponList(baseParam));
        result.put("success", true);
        
      } else {

        result.put("success", false);        
        result.put("errMsg", "not supported method.");
      }
      break;
    default : 
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }
    
    return result;
  }

  /**
   * Marketing => Promotion Coupon 
   *              쿠폰리스트, 쿠폰상세
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/Coupon")
  public Map<Object,Object> Coupon(
      @RequestParam(required=false) SvcCouponWithBLOBs coupon,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();

    if(coupon == null && params.get("data") != null){
      coupon = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcCouponWithBLOBs.class);
    }

    logger.info("★★★★★★★★★  Promotion Coupon ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("★★★★★★★★★  Promotion Coupon ★★★★★★★★★★★");    
    
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        SvcCouponExample example = new SvcCouponExample();

        if(coupon != null){
          example.createCriteria().andBrandIdEqualTo(coupon.getBrandId() != null ? coupon.getBrandId() : -1);
        } else {
          example.createCriteria().andBrandIdEqualTo(-1l);
        }

        /*
         * DATATABLE 검색 값 세팅
         */
        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
        if (!searchString.isEmpty()) {
        	example.createCriteria().andCouponNmLike(searchString);
        }
        
        /*
         * DATATABLE 컬럼 정렬값 세팅
         */
        if(params.get("order[0][column]") != null){
          String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
              +" "+String.valueOf(params.get("order[0][dir]"));
          example.setOrderByClause(orderby);
        }
        
        List<SvcCoupon> list = null;
        		
        if(params.get("draw").equals("select")){
        	list = svcCouponMapper.selectByExampleWithRowbounds(example, new RowBounds(0, 10000));
        }else{
        	list = svcCouponMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));
        }

        result.put("recordsTotal", svcCouponMapper.countByExample(example));
        result.put("recordsFiltered", svcCouponMapper.countByExample(example));
        result.put("list", list);
        result.put("success", true);

      } else {

        LinkedHashMap<String,Object> couponDetail = customMarketingMapper.getCouponDetail(coupon.getId());
        result.put("data", couponDetail);
        result.put("success", true);
      }

      break;
    case POST :
      result.put("success", svcCouponMapper.insertSelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcCouponMapper.updateByPrimaryKeySelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  /**
   * Marketing => Promotion Coupon 
   *              쿠폰 상세
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/CouponDetail")
  public Map<Object,Object> CouponDetail(
      @RequestParam(required=false) Map<String,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();
	  
    logger.info("★★★★★★★★★  CouponDetail ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("★★★★★★★★★  CouponDetail ★★★★★★★★★★★");
    
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        List<LinkedHashMap<String, Object>> list = customMarketingMapper.getPromotionMarketingList(params, new RowBounds(start, length));

        result.put("recordsTotal", customMarketingMapper.getCountPromotionMarketingList(params));
        result.put("recordsFiltered", customMarketingMapper.getCountPromotionMarketingList(params));
        result.put("list", list);
        result.put("success", true);

      } else {
    	  
    	  Map<String, String> search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
    	  
    	  SvcCouponPromotion promotion = svcCouponPromotionMapper.selectByPrimaryKey(Long.parseLong((String) search.get("id")));
    	  
          result.put("promotion", promotion);
          result.put("success", true);

      }

      break;
    case PUT :
    	result.put("success", false);
        result.put("errMsg", "not supported method.");  	  
    	break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Marketing => Promotion Coupon 
   *              쿠폰 발행내역
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/IssueDetail")
  public Map<Object,Object> IssueDetail(
      @RequestParam(required=false) Map<String,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();

    logger.info("★★★★★★★★★  IssueDetail ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("★★★★★★★★★  IssueDetail ★★★★★★★★★★★");
    
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){
    	  
    	  Map<String, Object> search = new HashMap<>();
    	  
    	  if(params.get("data") != null){
    		  search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, Object>>() {});  
    	  }
    	  
    	  List<LinkedHashMap<String, Object>> list = customMarketingMapper.getIssedUserList(search, new RowBounds(start, length));

		  result.put("recordsTotal", customMarketingMapper.getCountIssedUserList(search));
	      result.put("recordsFiltered", customMarketingMapper.getCountIssedUserList(search));
	      result.put("list", list);
	      result.put("success", true);

      } else {
    	  result.put("success", false);
          result.put("errMsg", "not supported method.");
      }

      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Marketing – IssueCoupon
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/IssueCoupon")
  public Map<Object,Object> IssueCoupon(
      @RequestParam(required=false) SvcCouponWithBLOBs coupon,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();

    logger.info("★★★★★★★★★  IssueCoupon ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("★★★★★★★★★  IssueCoupon ★★★★★★★★★★★");
    
    if(coupon == null && params.get("data") != null){
      coupon = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcCouponWithBLOBs.class);
    }

    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        SvcCouponExample example = new SvcCouponExample();

        if(coupon != null){
          example.createCriteria().andBrandIdEqualTo(coupon.getBrandId() != null ? coupon.getBrandId() : -1);
        } else {
          example.createCriteria().andBrandIdEqualTo(-1l);
        }

        List<SvcCoupon> list = svcCouponMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));


        result.put("recordsTotal", svcCouponMapper.countByExample(example));
        result.put("recordsFiltered", svcCouponMapper.countByExample(example));
        result.put("list", list);
        result.put("success", true);

      } else {
        SvcCoupon data = svcCouponMapper.selectByPrimaryKey(coupon.getId());

        result.put("data", data);
        result.put("success", true);
      }

      break;
    case POST :
      result.put("success", svcCouponMapper.insertSelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcCouponMapper.updateByPrimaryKeySelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  @RequestMapping("/Message")
  public Map<Object,Object> Message(
      @RequestParam(required=false) SvcCouponWithBLOBs coupon,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();

    if(coupon == null && params.get("data") != null){
      coupon = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcCouponWithBLOBs.class);
    }

    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        SvcCouponExample example = new SvcCouponExample();

        if(coupon != null){
          example.createCriteria().andBrandIdEqualTo(coupon.getBrandId() != null ? coupon.getBrandId() : -1);
        } else {
          example.createCriteria().andBrandIdEqualTo(-1l);
        }

        List<SvcCoupon> list = svcCouponMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));


        result.put("recordsTotal", svcCouponMapper.countByExample(example));
        result.put("recordsFiltered", svcCouponMapper.countByExample(example));
        result.put("list", list);
        result.put("success", true);

      } else {
        SvcCoupon data = svcCouponMapper.selectByPrimaryKey(coupon.getId());

        result.put("data", data);
        result.put("success", true);
      }

      break;
    case POST :
      result.put("success", svcCouponMapper.insertSelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcCouponMapper.updateByPrimaryKeySelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  @RequestMapping("/PushMarketing")
  public Map<Object,Object> PushMarketing(
      @RequestParam(required=false) SvcCouponWithBLOBs coupon,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();

    if(coupon == null && params.get("data") != null){
      coupon = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcCouponWithBLOBs.class);
    }

    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        SvcCouponExample example = new SvcCouponExample();

        if(coupon != null){
          example.createCriteria().andBrandIdEqualTo(coupon.getBrandId() != null ? coupon.getBrandId() : -1);
        } else {
          example.createCriteria().andBrandIdEqualTo(-1l);
        }

        List<SvcCoupon> list = svcCouponMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));


        result.put("recordsTotal", svcCouponMapper.countByExample(example));
        result.put("recordsFiltered", svcCouponMapper.countByExample(example));
        result.put("list", list);
        result.put("success", true);

      } else {
        SvcCoupon data = svcCouponMapper.selectByPrimaryKey(coupon.getId());

        result.put("data", data);
        result.put("success", true);
      }

      break;
    case POST :
      result.put("success", svcCouponMapper.insertSelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcCouponMapper.updateByPrimaryKeySelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Marketing => Promotion 
   *              프로모션 목록, 프로모션 상세
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/Promotion")
  public Map<Object,Object> Promotion(
      @RequestParam(required=false) SvcCouponPromotion svcCouponPromotion,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();
    Map<String, Object> search = new HashMap<>();
    
    logger.info("★★★★★★★★★  Promotion ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("★★★★★★★★★  Promotion ★★★★★★★★★★★");
    
    result.put("success", true);
    result.put("errMsg", "");
    
    if(svcCouponPromotion == null && params.get("data") != null){    	
        
    	svcCouponPromotion = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcCouponPromotion.class);
    }
    
    if(params.get("data") != null){
    	search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, Object>>() {});
    }
    
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){
        /*
         * DATATABLE 검색 값 세팅
         */
        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
        if (!searchString.isEmpty()) {
        	search.put("searchString", searchString);
        }
        
        /*
         * DATATABLE 컬럼 정렬값 세팅
         */
        if(params.get("order[0][column]") != null){
          String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
              +" "+String.valueOf(params.get("order[0][dir]"));
          search.put("orderby", orderby);
        }
        
        if(search.get("franId") == null){
        	search.put("franId", -1);
        }
        
        List<LinkedHashMap<String, Object>> list = customMarketingMapper.getPromotionList(search, new RowBounds(start, length));

        result.put("recordsTotal", customMarketingMapper.getCountPromotionList(search));
        result.put("recordsFiltered", customMarketingMapper.getCountPromotionList(search));
        result.put("list", list);
        result.put("success", true);

      } else {

        LinkedHashMap<String,Object> promotionDetail = customMarketingMapper.getPromotionDetail(Long.valueOf(search.get("id").toString()));
        result.put("data", promotionDetail);
        result.put("success", true);
      }

      break;
    case POST :   
    	search.put("issueTp", "409001");	// 409001 : 즉시발행    409002 : 예약발행
    	search.put("issueSt", "416002");	// 416001 : 발행         416002 : 미발행
    	search.put("promotionTp", "409001");	// 416001 : 발행         416002 : 미발행
    	
    	int insertCnt = customMarketingMapper.insertPromotionSelective(search);
    	
    	if(search.get("coupon").toString().indexOf(",") > -1){
    		String[] myObjects = JsonConvert.JsonConvertObject(search.get("coupon").toString(), String[].class);
        	
        	for(int i=0; i<myObjects.length; i++){
        		
        		Map<String, Object> insertData = new HashMap<>();
        		insertData.put("id", "");
        		insertData.put("promotionId", search.get("id"));
        		insertData.put("couponId", myObjects[i]);
        		insertData.put("issueTp", "409001");	// 409001 : 즉시발행    409002 : 예약발행
        		insertData.put("issueSt", "416002");	// 416001 : 발행         416002 : 미발행
        		int resultCnt = customMarketingMapper.insertPromotionCouponSelective(insertData);
        	
        	}
    	}else{
    		Map<String, Object> insertData = new HashMap<>();
    		insertData.put("id", "");
    		insertData.put("promotionId", search.get("id"));
    		insertData.put("couponId", search.get("coupon"));
    		insertData.put("issueTp", "409001");	// 409001 : 즉시발행    409002 : 예약발행
    		insertData.put("issueSt", "416002");	// 416001 : 발행         416002 : 미발행
    		int resultCnt = customMarketingMapper.insertPromotionCouponSelective(insertData);
    	}
    	
    	
    	result.put("success", true);
        result.put("errMsg", "");
      break;
    case PUT :
    	
    	if(customMarketingMapper.updatePromotionSelective(search) > 0){
    		if(customMarketingMapper.deletePromotionCoupon(Long.valueOf(search.get("id").toString())) > 0){
    			
    			if(search.get("coupon").toString().indexOf(",") > -1){
    				String[] updateData = JsonConvert.JsonConvertObject(search.get("coupon").toString(), String[].class);
        			
        			for(int i=0; i<updateData.length; i++){    	    		
        	    		Map<String, Object> insertData = new HashMap<>();
        	    		insertData.put("id", "");
        	    		insertData.put("promotionId", search.get("id"));
        	    		insertData.put("couponId", updateData[i]);    	    		
        	    		insertData.put("issueTp", "409001");	// 409001 : 즉시발행    409002 : 예약발행
        	    		insertData.put("issueSt", "416002");	// 416001 : 발행         416002 : 미발행    	    		
        	    		int resultCnt = customMarketingMapper.insertPromotionCouponSelective(insertData);
        	    	}
    	    	}else{
    	    		Map<String, Object> insertData = new HashMap<>();
    	    		insertData.put("id", "");
    	    		insertData.put("promotionId", search.get("id"));
    	    		insertData.put("couponId", search.get("coupon"));    	    		
    	    		insertData.put("issueTp", "409001");	// 409001 : 즉시발행    409002 : 예약발행
    	    		insertData.put("issueSt", "416002");	// 416001 : 발행         416002 : 미발행    	    		
    	    		int resultCnt = customMarketingMapper.insertPromotionCouponSelective(insertData);
    	    	}
    		}
    	}
    	
    	result.put("success", true);
        result.put("errMsg", "");
    	
      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  
  /**
   * Marketing => Promotion Coupon 
   *              쿠폰 상세
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/PromotionCoupon")
  public Map<Object,Object> PromotionCoupon(
      @RequestParam(required=false) Map<String,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();
	  
    logger.info("★★★★★★★★★  PromotionCoupon ★★★★★★★★★★★");
    logger.info("params : " + params.toString());
    logger.info("★★★★★★★★★  PromotionCoupon ★★★★★★★★★★★");
    
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        List<LinkedHashMap<String, Object>> list = customMarketingMapper.getPromotionCouponList(params, new RowBounds(start, length));

        result.put("recordsTotal", customMarketingMapper.getCountPromotionCouponList(params));
        result.put("recordsFiltered", customMarketingMapper.getCountPromotionCouponList(params));
        result.put("list", list);
        result.put("success", true);

      } else {
    	  
    	  Map<String, String> search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
    	  
    	  SvcCouponPromotion promotion = svcCouponPromotionMapper.selectByPrimaryKey(Long.parseLong((String) search.get("id")));
    	  
          result.put("promotion", promotion);
          result.put("success", true);
    	  
      }

      break;
    case PUT :
    	result.put("success", false);
        result.put("errMsg", "not supported method.");
    	break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Marketing => Promotion 
   *              프로모션 쿠폰을 발행받은 희원의 목록을 조회한다
   * @param Promotion ID
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/IssuedUser")
  public Map<Object,Object> IssuedUser(
      @RequestParam(required=false) SvcCouponWithBLOBs coupon,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();

    if(coupon == null && params.get("data") != null){
      coupon = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcCouponWithBLOBs.class);
    }

    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        SvcCouponExample example = new SvcCouponExample();

        if(coupon != null){
          example.createCriteria().andBrandIdEqualTo(coupon.getBrandId() != null ? coupon.getBrandId() : -1);
        } else {
          example.createCriteria().andBrandIdEqualTo(-1l);
        }

        /*
         * DATATABLE 검색 값 세팅
         */
        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
        if (!searchString.isEmpty()) {
        	example.createCriteria().andCouponNmLike(searchString);
        }
        
        /*
         * DATATABLE 컬럼 정렬값 세팅
         */
        if(params.get("order[0][column]") != null){
          String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
              +" "+String.valueOf(params.get("order[0][dir]"));
          example.setOrderByClause(orderby);
        }
        
        List<SvcCoupon> list = svcCouponMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));


        result.put("recordsTotal", svcCouponMapper.countByExample(example));
        result.put("recordsFiltered", svcCouponMapper.countByExample(example));
        result.put("list", list);
        result.put("success", true);

      } else {

        LinkedHashMap<String,Object> couponDetail = customMarketingMapper.getCouponDetail(coupon.getId());
        result.put("data", couponDetail);
        result.put("success", true);
      }

      break;
    case POST :
      result.put("success", svcCouponMapper.insertSelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcCouponMapper.updateByPrimaryKeySelective(coupon) == 1 ? true : false );
      result.put("errMsg", "");
      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Marketing => Coupon Issue
   *              발행대상 쿠폰
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/IssueCoupon2")
  public Map<Object,Object> IssueCoupon2(
      @RequestParam(required=false) Map<String,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();
	  
    try {
      switch(method){
      case GET :
        if(!String.valueOf(params.get("draw")).equals("null")){
  
          logger.debug("★★★★★★★★★★★★★★");
          logger.debug(params.toString());
          logger.debug("★★★★★★★★★★★★★★");
          
          /*
           * DATATABLE 컬럼 정렬값 세팅
           */
          if(params.get("order[0][column]") != null){
            String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                +" "+String.valueOf(params.get("order[0][dir]"));
            params.put("orderby",orderby);
          }
          
          List<LinkedHashMap<String, Object>> list = customMarketingMapper.getIssueCouponList(params, new RowBounds(start, length));
  
          result.put("recordsTotal", customMarketingMapper.getCountIssueCouponList(params));
          result.put("recordsFiltered", customMarketingMapper.getCountIssueCouponList(params));
          result.put("list", list);
          result.put("success", true);
  
        } else {
      	  
      	  Map<String, String> search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
      	  
      	  SvcCouponPromotion promotion = svcCouponPromotionMapper.selectByPrimaryKey(Long.parseLong((String) search.get("id")));
      	  
            result.put("promotion", promotion);
            result.put("success", true);
      	  
        }
  
        break;
      case POST :
      	Map<String, String> data = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
      	
      	logger.debug("★★★★★★★★★★★★★★★★★★★");
      	logger.debug(data.toString());
      	logger.debug("★★★★★★★★★★★★★★★★★★★");
        
      	UserDeviceMapper userDeviceMapper = sessionTemplate.getMapper(UserDeviceMapper.class);
      	UserDeviceExample userDeviceExample = new UserDeviceExample();
      	List<LinkedHashMap<String, Object>> userlist = customMarketingMapper.getIssueUserList(data);
      	List<Long> ids = Lists.newArrayList();
  
      	if(userlist.size() > 0){
      		for (LinkedHashMap<String, Object> target : userlist) {    			
      			
      			// TB_SVC_PROMOTION_TARGET 에 발행대상 사용자 정보를 저장한다.
      			Map<String, String> targetUserData = new HashMap<>();   
      			targetUserData.put("userId", String.valueOf(target.get("userId"))); 	// 사용자 ID
      			targetUserData.put("couponId", (String) data.get("couponId")); 			// 쿠폰 ID
      			targetUserData.put("promotionId", (String) data.get("promotionId")); 	// 프로모션 ID
      			
      			// 즉시발행인경우  발행상태 발행, 발행일자에 현재일시를 저장 
      			if(("409001").equals(data.get("issueTp"))){
      				targetUserData.put("isIssued", "1"); 		// 발행여부 
      				
      				//임시셋팅
      			  	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
      			  	Date now = new Date();
      			  	String tmpBcd = format.format(now);
          			
      				targetUserData.put("issuedDt", tmpBcd); 	// 발행일자
      			}
      			// 예약발행인경우  발행상태 미발행, 발행일자에 입력받은 예약일시저장  
      			else if(("409002").equals(data.get("issueTp"))){
      				targetUserData.put("isIssued", "0"); 		// 발행여부
      				targetUserData.put("issuedDt", (String) data.get("issuedDt")); 	// 발행일자
      			}
      			
      			// 쿠폰 발행 타겟 회원을 tb_svc_promotion_target 에 저장 
      			// 예약발행시에 사용
      			customMarketingMapper.insertTargetUser(targetUserData);
      			
      			// 즉시발행인 경우 tb_svc_user_coupon에 저장
      			if(("409001").equals(data.get("issueTp"))){
      				Map<String, String> userCouponData = new HashMap<>();
      				
      				userCouponData.put("userId", String.valueOf(target.get("userId"))); 	// 사용자 ID
      				userCouponData.put("couponId", (String) data.get("couponId")); 			// 쿠폰 ID
      				userCouponData.put("promotionId", (String) data.get("promotionId")); 	// 프로모션 ID    				
      				userCouponData.put("brandId", (String) data.get("brandId"));
      				userCouponData.put("storeId", (String) data.get("storeId"));    				
      				userCouponData.put("couponCd", barcodeUtil.makeCouponCode((String) data.get("brandCd")));
      				userCouponData.put("couponTp", "406002");	// 쿠폰발행 유형 406002 이벤트 발행
      				userCouponData.put("couponSt", "402001");	// 쿠폰로그 유형 402001 발행
      				userCouponData.put("used", "0");			
      				userCouponData.put("discountTp", (String) data.get("discountTp"));
      				
      				// 만료유형이 발행일 로부터 기간을 지정할경우 term 값으로 계산
      				if("404001".equals(data.get("exipreTp"))){
      					String test[] = data.get("term").split("/");
      					
      					Calendar temp=Calendar.getInstance ( );
      					StringBuffer sbDate=new StringBuffer ( );
      					
      					temp.add ( Calendar.YEAR, Integer.valueOf(test[0]));
      					temp.add ( Calendar.MONTH, Integer.valueOf(test[1]) );
      					temp.add ( Calendar.DAY_OF_MONTH, Integer.valueOf(test[2]) );
      					
      					int nYear = temp.get ( Calendar.YEAR );
      					int nMonth = temp.get ( Calendar.MONTH ) + 1;
      					int nDay = temp.get ( Calendar.DAY_OF_MONTH );
      					
      					sbDate.append ( nYear );
      					if ( nMonth < 10 ) { 
      					  sbDate.append ( "0" );
      					  sbDate.append ( nMonth );
      					} else {
      					  sbDate.append ( nMonth );
      					}
      					if ( nDay < 10 ) {
      					  sbDate.append ( "0" );
      					  sbDate.append ( nDay );
      					} else {
      					  sbDate.append ( nDay );
      					}
      					
      					userCouponData.put("expire", sbDate.toString());
      				}else if("404002".equals(data.get("exipreTp"))){
      					userCouponData.put("expire", (String) data.get("expire"));
      				}    				
      				
      				logger.debug("data: " + JsonConvert.toJson(data));
      				logger.debug("user coupon : " + JsonConvert.toJson(userCouponData));
      				customMarketingMapper.insertUserCoupon(userCouponData);
      				
      				Map<String, String> userCouponLogData = new HashMap<>();
      				
      				userCouponLogData.put("userId", String.valueOf(target.get("userId"))); 	// 사용자 ID
      				userCouponLogData.put("brandId", (String) data.get("brandId"));
      				userCouponLogData.put("storeId", (String) data.get("storeId"));    	
      				userCouponLogData.put("couponId", (String) data.get("couponId")); 			// 쿠폰 ID
      				userCouponLogData.put("promotionId", (String) data.get("promotionId")); 	// 프로모션 ID    				
      				userCouponLogData.put("couponNm", (String) data.get("couponNm")); 			// 쿠폰 ID			
      				userCouponLogData.put("couponCd", userCouponData.get("couponCd"));			// 쿠폰CD
      				userCouponLogData.put("logTp", "402001");	// 쿠폰로그 유형 402001 발행
      				userCouponLogData.put("discountTp", (String) data.get("discountTp"));
      				
      				HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
      		        String ip = req.getHeader("X-FORWARDED-FOR");
      		        if (ip == null){
      		            ip = req.getRemoteAddr();
      		        }
      		        userCouponLogData.put("srcIp", ip);
      		        
      				customMarketingMapper.insertUserCouponLog(userCouponLogData);
      			}
      			
      			// TB_SVC_PROMOTION_COUPON 프로모션 쿠폰 매핑테이블에 발행구분, 발행상태, 발행일자를 업데이트 한다.
      			Map<String, String> promotionCouponData = new HashMap<>();   
      			
      			promotionCouponData.put("couponId", (String) data.get("couponId")); 		// 쿠폰 ID
      			promotionCouponData.put("promotionId", (String) data.get("promotionId")); 	// 프로모션 ID    	
      			promotionCouponData.put("issueCnt", String.valueOf(userlist.size()));
      			promotionCouponData.put("issueTp", (String) data.get("issueTp"));		// 발행구분 유형 409001 즉시발행, 409002 예약발행    			
      			promotionCouponData.put("issueSt", "416001");	// 발행상태 416001 발행, 416002 미발행
      			
      			//임시셋팅
  			  	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
  			  	Date now = new Date();
  			  	String tmpBcd = format.format(now);
      			
  			  	promotionCouponData.put("issuedDt", tmpBcd); 	// 발행일자
  				
      			if(customMarketingMapper.updatePromotionCouponSelective(promotionCouponData) > 0){
      				
      				// TB_SVC_PROMOTION 프로모션 발행일자, 발행시간을  업데이트 한다.
          			Map<String, Object> promotionData = new HashMap<>();   
          			
          			promotionData.put("issuedDt", (String) data.get("couponId")); 		// 쿠폰 ID
          			promotionData.put("id", (String) data.get("promotionId")); 	// 프로모션 ID    	
      			  	
          			// 임시
          			SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd" );
          			SimpleDateFormat formatter1 = new SimpleDateFormat ( "HHmmss" );
          			Date currentTime = new Date ( );
          			
          			String issuedDt = formatter.format ( currentTime );
          			String issuedTm = formatter1.format ( currentTime );
          			
          			promotionData.put("issuedDt", issuedDt); 	// 발행일자
          			promotionData.put("issuedTm", issuedTm); 	// 발행시간
          			
      				customMarketingMapper.updatePromotionSelective(promotionData);
      			}    	
      			
      			Long userId = ((BigInteger)target.get("userId")).longValue();
      			ids.add(userId);
      			
      			UserDeviceMapper deviceMapper = sessionTemplate.getMapper(UserDeviceMapper.class);
                UserDeviceExample deviceExample = new UserDeviceExample();
                deviceExample.createCriteria()
                .andUserIdEqualTo(userId)
                .andIsAliveEqualTo(true)
                .andPushIdIsNotNull()
                .andPushIdNotEqualTo("");
                List<UserDevice> userDevices = userDeviceMapper.selectByExample(deviceExample);
                
                logger.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★");
                logger.debug("userId : " + userId);
                logger.debug("userDevices : " + userDevices.size());
                logger.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★");
                
                if (userDevices != null && userDevices.size() > 0) {
                    String android = codeUtil.getBaseCodeByAlias("android");
                    String ios = codeUtil.getBaseCodeByAlias("ios");
                    Map<String, List<String>> pushIds = Maps.newHashMap();
                    List<String> gcmIds = Lists.newArrayList();
                    List<String> apnIds = Lists.newArrayList();
          
                    for (UserDevice userDevice : userDevices) {
                      if (android.equals(userDevice.getOs())) {
                        gcmIds.add(userDevice.getPushId());
                      } else if (ios.equals(userDevice.getOs())) {
                        apnIds.add(userDevice.getPushId());
                      }
                    }
          
                    if (gcmIds.size() > 0) {
                      pushIds.put("android", gcmIds);
                    }
          
                    if (apnIds.size() > 0) {
                      pushIds.put("ios", apnIds);
                    }
          
                    /*
                     * 사용자별 언어 설정
                     */
                    Locale locale = null;
                    
                    userDevices.get(0).setLocale(userDevices.get(0).getLocale().toLowerCase());
                    if (userDevices.get(0).getLocale().equals(Locale.US.toString().toLowerCase())) {
                      locale = Locale.US;
                    } else if (userDevices.get(0).getLocale().equals((new Locale("ru", "RU").toString().toLowerCase()))) {
                      locale = new Locale("ru", "RU");
                    } else {
                      locale = Locale.US;
                    }
                    
                    PushSender pushSender = new PushSender();
                    SendPush push = new SendPush();
                    push.setPushIds(pushIds);
                    push.setBrandId(Long.parseLong(data.get("brandId"))); // admin
                    
                    push.setTitle(messageSource.getMessage("app.push.message-tp-coupon-issue.title", new String[]{}, locale));
                    push.setContent(messageSource.getMessage("app.push.message-tp-coupon-issue.msg",  new String[]{(String) data.get("couponNm")}, locale));
                    
                    push.setType(MESSAGE_TP_COUPON_ISSUE);
                    push.addExtra("storeId",  Long.parseLong(data.get("storeId")));
                    pushSender.setSessionTemplate(sessionTemplate);
                    pushSender.send(false, push);
          
                  }
          }
          
      		result.put("success", true);
      	}else{
      		result.put("success", false);
      		result.put("errMsg", "No target Customer");
      	}
          
      	break;
      default :
        result.put("success", false);
        result.put("errMsg", "not supported method.");
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.put("success", false);
      result.put("errMsg", "No target Customer.");
    }
    return result;
  }
  
  /**
	 * @date	: 2016. 11. 10. 오후 4:49:32
	 * @author	: 
	 * @method	: PromotionTargetUpload
	 * @return	: Map<Object,Object>
	 * @desc	: 엑셀파일을 통하여 프로모션대상자리스트를 읽어들인다.
	 */
	@RequestMapping("/PromotionTarget/Upload")
	public Map<Object,Object> PromotionTargetUpload(MultipartHttpServletRequest request){
		
		Map<Object,Object> resultMap = Maps.newHashMap();
		resultMap.put("success", false);

		if(request.getFile("targetUser") != null){

			CustomMarketingMapper customMarketingMapper = sessionTemplate.getMapper(CustomMarketingMapper.class);

			try {
				Workbook workbook = WorkbookFactory.create(request.getFile("targetUser").getInputStream());
				Sheet sheet = workbook.getSheetAt(0);
				List<String> barcodeList = Lists.newArrayList();
				Row row = null;

				for(int i = 0 ; i <= sheet.getLastRowNum() ; i++){
					row = sheet.getRow(i);
					barcodeList.add(String.valueOf(row.getCell(0)));
				}

				Map<String,Object> paramMap = Maps.newHashMap();
				paramMap.put("barcodes", barcodeList);

				RowBounds rowBounds = new RowBounds(0, barcodeList.size());
				paramMap.put("offset", rowBounds.getOffset());
				paramMap.put("limit", rowBounds.getLimit());
				List<LinkedHashMap<String,Object>> userList = customMarketingMapper.getIssueUserList(paramMap, new RowBounds(0, barcodeList.size()));

				if(userList.size() > 0){
					resultMap.put("success", true);
					resultMap.put("list", userList);
					if(userList.size() != barcodeList.size()){
						resultMap.put("alert", "업로드 하신내역에 존재하지 않는 유저 정보가 있습니다.");
					}
				} else {
					resultMap.put("errMsg", "회원정보가 존재하지 않습니다.");
				}

			} catch (Exception e) {
				resultMap.put("errMsg", "올바르지 않은 파일 형식입니다.");
				e.printStackTrace();
				logger.error(e.getMessage());
			}

		} else {
			resultMap.put("errMsg", "업로드된 파일이 존재하지 않습니다.");
		}

		return resultMap;
	}
  /**
   * Marketing => Coupon Issue
   *              발행대상 쿠폰
   * @param coupon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/IssueCouponUser")
  public Map<Object,Object> IssueCouponUser(
      @RequestParam(required=false) Map<String,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();
//    Map<String,Object> data = new HashMap<>();
	  
    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

//    	data = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, Object>>() {});
    	
    	/*
         * DATATABLE 컬럼 정렬값 세팅
         */
        if(params.get("order[0][column]") != null){
            String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                +" "+String.valueOf(params.get("order[0][dir]"));
            params.put("orderby",orderby);
        }
          
        List<LinkedHashMap<String, Object>> list = customMarketingMapper.getIssueUserList(params, new RowBounds(start, length));

        result.put("recordsTotal", customMarketingMapper.getCountIssueUserList(params));
        result.put("recordsFiltered", customMarketingMapper.getCountIssueUserList(params));
        result.put("list", list);
        result.put("success", true);

      } else {
    	  
    	  Map<String, String> search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
    	  
    	  SvcCouponPromotion promotion = svcCouponPromotionMapper.selectByPrimaryKey(Long.parseLong((String) search.get("id")));
    	  
          result.put("promotion", promotion);
          result.put("success", true);
    	  
      }

      break;
    case PUT :
    	result.put("success", false);
        result.put("errMsg", "not supported method.");
    	break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
}
