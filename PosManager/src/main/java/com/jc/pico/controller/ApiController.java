/*
 * Filename	: ApiController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.*;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.DataNotFoundException;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.mapper.*;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.CustomMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.JsonResult;
import com.jc.pico.validator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/api", consumes="application/json")
public class ApiController {
  protected static Logger logger = LoggerFactory.getLogger(ApiController.class);
  @Autowired CodeUtil codeUtil;
  @Autowired
  SvcStoreMapper svcStoreMapper;
  @Autowired
  SvcOrderMapper svcOrderMapper;
  @Autowired SvcOrderItemMapper svcOrderItemMapper;
  @Autowired SvcUserStampMapper svcUserStampMapper;
  @Autowired SvcUserCouponMapper svcUserCouponMapper;
  @Autowired SvcCouponLogMapper svcCouponLogMapper;
  @Autowired
  UserMapper userMapper;
  @Autowired CustomMapper customMapper;
  @Autowired StoreValidator svcStoreValidator;
  @Autowired
  StampApiCreateValidator stampApiCreateValidator;
  @Autowired StampApiDeleteValidator stampApiDeleteValidator;
  @Autowired
  CouponApiSearchValidator couponApiSearchValidator;
  @Autowired CouponApiUseValidator couponApiUseValidator;
  @Autowired
  CouponApiDeleteValidator couponApiDeleteValidator;

  @RequestMapping(value="/pos/syncStoreInfo")
  public JsonResult syncStoreInfo(
      HttpServletRequest request,
      HttpMethod method) throws InvalidJsonException, DataNotFoundException, InvalidParamException {
    JsonResult result = new JsonResult();


    Map<Object,Object> store = JsonConvert.JsonConvertObject(request, new TypeReference<Map<Object,Object>>(){});
    if(store.get("stores") != null){
      List<Map<Object,Object>> stores = (List<Map<Object, Object>>) store.get("stores");

      List<SvcStore> storeList = Lists.transform(stores, new Function<Map<Object,Object>, SvcStore>() {
        @Override
        public SvcStore apply(Map<Object,Object> store) {
          SvcStore _store = new SvcStore();

          _store.setTenantId(1l);
          _store.setBrandId(Globals.PICO_BRAND_ID);
          _store.setStoreCd(store.get("storeCd").toString());
          _store.setStoreNm(store.get("storeNm") != null ? store.get("storeNm").toString() : null);
          _store.setBizNo(store.get("bizNum") != null ? store.get("bizNum").toString() : null);
          _store.setZip(store.get("zip1") != null ? store.get("zip1").toString() : null);
//          _store.setZipOld(store.get("zip2") != null ? store.get("zip2").toString() : null);
          _store.setAddr1(store.get("addr1-new") != null ? store.get("addr1-new").toString() : null);
          _store.setAddr2(store.get("addr2-new") != null ? store.get("addr2-new").toString() : null);
//          _store.setAddr1Old(store.get("addr1-old") != null ? store.get("addr1-old").toString() : null);
//          _store.setAddr2Old(store.get("addr2-old") != null ? store.get("addr2-old").toString() : null);
          _store.setTel(store.get("tel") != null ? store.get("tel").toString() : null);
          _store.setOwnerNm(store.get("ownNm") != null ? store.get("ownNm").toString() : null);

          return _store;
        }
      });

      for(SvcStore st : storeList){
        Errors errors = new BeanPropertyBindingResult(st, "store");
        svcStoreValidator.validate(st, errors);

        if(errors.hasErrors()){
          throw new InvalidParamException(errors);
        }
      }

      List<String> storeCds = Lists.transform(storeList, new Function<SvcStore, String>() {
        @Override
        public String apply(SvcStore store) {
          return store.getStoreCd();
        }
      }); 

      SvcStoreExample example = new SvcStoreExample();
      example.createCriteria().andStoreCdIn(storeCds);

      List<SvcStore> _storeCds = svcStoreMapper.selectByExample(example);

      storeCds = Lists.transform(_storeCds, new Function<SvcStore, String>() {
        @Override
        public String apply(SvcStore store) {
          return store.getStoreCd();
        }
      }); 

      for(SvcStore _store : storeList){
//        Collection<String> res = Collections2.filter(storeCds, Predicates.isEqualTo(_store.getStoreCd()));

//        if(res.size() <= 0){
//          svcStoreMapper.insertSelective(_store);
//        } else {
//          example.clear();
//          example.createCriteria().andStoreCdEqualTo(_store.getStoreCd());
//          svcStoreMapper.updateByExampleSelective(_store, example);
//        } 
      }

      result.setSuccess(true);;
      result.setBean("OK");
    } else {
      throw new DataNotFoundException("stores");
    }

    return result;
  }

  @RequestMapping("/pos/user")
  public JsonResult userSearch(
      HttpServletRequest request) throws InvalidJsonException, InvalidParamException, DataNotFoundException {
    JsonResult result = new JsonResult();

    Map<String,User> maps = JsonConvert.JsonConvertObject(request, new TypeReference<Map<String,User>>(){});
    User user = maps.get("user");

    if(user != null){
      if((user.getBarcode() != null || user.getMb() != null)){

        String userTpAssociate = codeUtil.getBaseCodeByAlias("associate-user");
        String userTpReular = codeUtil.getBaseCodeByAlias("user");

        List<LinkedHashMap<String,Object>> users = customMapper.getPosUserSearchList(user, Lists.newArrayList(userTpAssociate, userTpReular));

        if(users.size() > 0){

          result.setSuccess(true);
          result.setBean(users);

        } else {
          String message = (user.getMb() != null ? user.getMb()+"  " : "  ") + (user.getBarcode() != null ? user.getBarcode() : " ");
          throw new DataNotFoundException(message);
        }

      } else {
        throw new InvalidParamException(new String[] { "barcode or mb" }, request.getLocale());
      }
    } else {
      throw new InvalidParamException(new String[] { "user" }, request.getLocale());
    }

    return result;
  }

  @Transactional
  @RequestMapping(value = "/pos/stamp", method = { RequestMethod.POST , RequestMethod.DELETE})
  public JsonResult stampIssue(
      Principal principal,
      HttpServletRequest request, 
      HttpMethod method) throws InvalidJsonException, DataNotFoundException, InvalidParamException, ParseException {
    JsonResult result = new JsonResult();

    Map<Object,Object> stamp = 
        (Map<Object, Object>) JsonConvert.JsonConvertObject(request, new TypeReference<Map<Object,Object>>(){});

    String[] store = principal.getName().trim().split("_-_");
    SvcStore _store = null;
    if(store.length == 3){
      _store = svcStoreMapper.selectByPrimaryKey(Long.parseLong(store[1]));

      if(_store == null){
        throw new DataNotFoundException("store not found");
      }
    } else {
      throw new DataNotFoundException("token store code not null");
    }

    Errors errors = new BeanPropertyBindingResult(stamp, "stamp");

    switch(method){
    case POST :
      stampApiCreateValidator.validate(stamp, errors);

      if(!errors.hasErrors()){
        User user = userMapper.selectByPrimaryKey(Long.parseLong(String.valueOf(stamp.get("userId"))));

        if(user != null){
          List<String> stampIds = Lists.newArrayList();
          int stampCount = Integer.parseInt(stamp.get("count").toString());
          Map<Object,Object> _order = (Map<Object, Object>) stamp.get("receipt");

          SvcOrder order = new SvcOrder(); 
          order.setBrandId(Globals.PICO_BRAND_ID);
          order.setStoreId(_store.getId());
          order.setUserId(user.getId());
          order.setReceiptId(_order.get("id").toString());
          order.setReceiptNo(_order.get("no").toString());
          order.setOrderTm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_order.get("date").toString()));
          order.setUseCoupon(Boolean.valueOf(_order.get("useCoupon").toString()));
          order.setSales(Double.parseDouble(_order.get("sales").toString()));
          order.setDiscount(Double.parseDouble(_order.get("discount").toString()));
          order.setTax(Double.valueOf(_order.get("tax").toString()));
          order.setTableNo(_order.get("tableNo") != null ? _order.get("tableNo").toString() : null);
          order.setPerson(_order.get("person") != null ? _order.get("person").toString() : null);

          svcOrderMapper.insertSelective(order);

          List<Map<Object,Object>> items = (List<Map<Object, Object>>) _order.get("items");

          for(Map<Object,Object> _item : items){
            SvcOrderItem item = new SvcOrderItem();
            item.setOrderId(order.getId());
            item.setIsStamp(Boolean.valueOf(_item.get("isStamp").toString()));
            item.setCatCd(_item.get("categoryCd") != null ? _item.get("categoryCd").toString() : null);
            item.setCatNm(_item.get("categoryNm") != null ? _item.get("categoryNm").toString() : null);
            item.setItemNm(_item.get("menuNm").toString());
            item.setItemCd(_item.get("menuCd").toString());
            item.setPrice(Double.parseDouble(_item.get("price").toString()));
            item.setCount(Short.valueOf(_item.get("count").toString()));
            item.setSales(Double.parseDouble(_item.get("sales").toString()));
            item.setDiscount(Double.parseDouble(_item.get("discount").toString()));
            item.setNetSales(Double.parseDouble(_item.get("netSales").toString()));
            item.setTax(Double.parseDouble(_item.get("tax").toString()));

            svcOrderItemMapper.insertSelective(item);
          }

          for(int i = 0 ; i < stampCount ; i++){
            SvcUserStamp userStamp = new SvcUserStamp();
            userStamp.setUserId(user.getId());
            userStamp.setBrandId(Globals.PICO_BRAND_ID);
            userStamp.setOrderId(order.getId());
            userStamp.setStoreId(_store.getId());
            userStamp.setStampTp("405001");

            svcUserStampMapper.insertSelective(userStamp);

            stampIds.add(userStamp.getId().toString());
          }

          result.setSuccess(true);
          result.setBean(stampIds);

        } else {
          throw new DataNotFoundException(String.valueOf(stamp.get("userId")));
        }
      } else {
        throw new InvalidParamException(errors);
      }
      break;
    case DELETE : 
      stampApiDeleteValidator.validate(stamp, errors);
      if(!errors.hasErrors()){
        User user = userMapper.selectByPrimaryKey(Long.parseLong(String.valueOf(stamp.get("userId"))));

        if(user != null){
          Map<Object,Object> _order = (Map<Object, Object>) stamp.get("receipt");
          SvcOrderExample _order_example = new SvcOrderExample();
          _order_example
          .createCriteria()
          .andReceiptIdEqualTo(String.valueOf(_order.get("id")))
          .andReceiptNoEqualTo(String.valueOf(_order.get("no")))
          .andOrderTmEqualTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_order.get("date").toString()))
          .andSalesEqualTo(Double.parseDouble(_order.get("sales").toString()));

          List<SvcOrder> order = svcOrderMapper.selectByExample(_order_example);
          List<Long> ids = Lists.transform((List<Object>) stamp.get("ids"), new Function<Object, Long>() {
            @Override
            public Long apply(Object id) {
              return Long.parseLong(String.valueOf(id));
            }
          });

          if(order.size() == 1){

            SvcUserStampExample example = new SvcUserStampExample();
            example.createCriteria()
            .andUserIdEqualTo(user.getId())
            .andBrandIdEqualTo(Globals.PICO_BRAND_ID)
            .andOrderIdEqualTo(order.get(0).getId())
            .andIdIn(ids);

            List<SvcUserStamp> stamps = svcUserStampMapper.selectByExample(example);

            if(stamps.size() == ids.size()){
              example.clear();
              example.createCriteria().andIdIn(ids);

              svcUserStampMapper.deleteByExample(example);

              Map<String,Object> _ids = Maps.newHashMap();
              _ids.put("ids", ids);

              result.setSuccess(true);
              result.setBean(_ids);
            } else {
              throw new DataNotFoundException("stamp ids");
            }

          } else {
            throw new DataNotFoundException("order Data");
          }

        }else {
          throw new DataNotFoundException(String.valueOf(stamp.get("userId")));
        }
      } else {
        throw new InvalidParamException(errors);
      }
      break;
    default:
      break;
    }

    return result;
  }

  @Transactional
  @RequestMapping("/pos/coupon")
  public JsonResult couponIssue(
      Principal principal,
      HttpServletRequest request, 
      HttpMethod method) throws InvalidJsonException, DataNotFoundException, InvalidParamException, ParseException {
    JsonResult result = new JsonResult();

    Map<Object,Object> coupon = 
        (Map<Object, Object>) JsonConvert.JsonConvertObject(request, new TypeReference<Map<Object,Object>>(){});

    String[] store = principal.getName().trim().split("_-_");
    SvcStore _store = null;
    if(store.length == 3){
      _store = svcStoreMapper.selectByPrimaryKey(Long.parseLong(store[1]));

      if(_store == null){
        throw new DataNotFoundException("store not found");
      }
    } else {
      throw new DataNotFoundException("token store code not null");
    }

    Errors errors = new BeanPropertyBindingResult(coupon, "coupon");
    switch (method) {
    case GET:
      couponApiSearchValidator.validate(coupon, errors);
      if(!errors.hasErrors()){ 
        User user = userMapper.selectByPrimaryKey(Long.parseLong(String.valueOf(coupon.get("userId"))));

        if(user != null){
          LinkedHashMap<String, Object> _coupon = customMapper.getUnUsedCoupon(coupon);

          if(_coupon != null){
            Map<String, String> userMap = Maps.newHashMap();
            userMap.put("name", user.getName());
            userMap.put("barcode", user.getBarcode());
            userMap.put("mb", user.getMb());

            _coupon.put("member", userMap);
            result.setSuccess(true);
            result.setBean(_coupon);
          } else {
            throw new DataNotFoundException(String.valueOf(coupon.get("couponCd")));
          }

        } else {
          throw new DataNotFoundException(String.valueOf(coupon.get("userId")));
        }
      } else {
        throw new InvalidParamException(errors);
      }
      break;
    case POST:
      couponApiUseValidator.validate(coupon, errors);

      if(!errors.hasErrors()){ 
        Map<Object,Object> _order = (Map<Object, Object>) coupon.get("receipt");
        SvcOrderExample _order_example = new SvcOrderExample();
        _order_example
        .createCriteria()
        .andReceiptIdEqualTo(String.valueOf(_order.get("id")))
        .andReceiptNoEqualTo(String.valueOf(_order.get("no")))
        .andOrderTmEqualTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_order.get("date").toString()))
        .andSalesEqualTo(Double.parseDouble(_order.get("sales").toString()));

        List<SvcOrder> order = svcOrderMapper.selectByExample(_order_example);

        if(order.size() == 1){

          List<Map<Object,Object>> items = (List<Map<Object, Object>>) _order.get("items");

          for(Map<Object,Object> _item : items){
            SvcUserCouponExample example = new SvcUserCouponExample();
            example
            .createCriteria().andCouponCdEqualTo(_item.get("id").toString());

            List<SvcUserCoupon>_userCoupon = svcUserCouponMapper.selectByExample(example); 

            SvcUserCoupon userCoupon = new SvcUserCoupon();

            userCoupon.setStoreId(_store.getId());
            userCoupon.setUsed(true);

            SvcCouponLog userCouponLog = new SvcCouponLog();
            userCouponLog.setBrandId(Globals.PICO_BRAND_ID);
            userCouponLog.setUserId(_userCoupon.get(0).getUserId());
            userCouponLog.setStoreId(_store.getId());
            userCouponLog.setCouponId(_userCoupon.get(0).getId());
            userCouponLog.setCouponCd(_userCoupon.get(0).getCouponCd());
            userCouponLog.setLogTp("402003");
            userCouponLog.setOrderId(order.get(0).getId());

            svcUserCouponMapper.updateByExampleSelective(userCoupon, example);
            svcCouponLogMapper.insertSelective(userCouponLog);
          }

          result.setSuccess(true);
          result.setBean("OK");

        } else {
          throw new DataNotFoundException(" order ID : "+ String.valueOf(_order.get("id")));
        }
      } else {
        throw new InvalidParamException(errors);
      }
      break;
    case DELETE : 
      couponApiDeleteValidator.validate(coupon, errors);

      if(!errors.hasErrors()){
        User user = userMapper.selectByPrimaryKey(Long.parseLong(String.valueOf(coupon.get("userId"))));

        Map<Object,Object> _order = (Map<Object, Object>) coupon.get("receipt");

        List<String> ids = Lists.transform((List<Object>) coupon.get("ids"), new Function<Object, String>() {
          @Override
          public String apply(Object id) {
            return String.valueOf(id);
          }
        });

        SvcOrderExample _order_example = new SvcOrderExample();
        _order_example
        .createCriteria()
        .andReceiptIdEqualTo(String.valueOf(_order.get("id")))
        .andReceiptNoEqualTo(String.valueOf(_order.get("no")))
        .andOrderTmEqualTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_order.get("date").toString()))
        .andSalesEqualTo(Double.parseDouble(_order.get("sales").toString()));

        List<SvcOrder> order = svcOrderMapper.selectByExample(_order_example);

        SvcCouponLogExample example = new SvcCouponLogExample();
        example
        .createCriteria()
        .andUserIdEqualTo(user.getId())
        .andLogTpEqualTo("402003")
        .andOrderIdEqualTo(order.get(0).getId())
        .andCouponCdIn(ids);

        List<SvcCouponLog> couponLog = svcCouponLogMapper.selectByExample(example); 

        if(ids.size() == couponLog.size()){
          SvcUserCoupon updateCoupon = new SvcUserCoupon();

          updateCoupon.setUsed(false);
          updateCoupon.setStoreId(0l);

          for( SvcCouponLog _couponLog : couponLog ){
            SvcUserCouponExample _example = new SvcUserCouponExample();
            _example.createCriteria().andIdEqualTo(_couponLog.getCouponId());

            svcUserCouponMapper.updateByExample(updateCoupon, _example);
            _couponLog.setId(null);
            _couponLog.setLogTp("402002");
            _couponLog.setWhen(null);

            svcCouponLogMapper.insertSelective(_couponLog);
          }

          Map<Object,Object> returnIds = Maps.newHashMap();
          returnIds.put("ids", ids);

          result.setSuccess(true);
          result.setBean(returnIds);
        } else {
          throw new DataNotFoundException("coupon ids");
        }

      } else {
        throw new InvalidParamException(errors);
      }
      break;
    default:
      break;
    }

    return result;
  }

  @RequestMapping(value = "/solbi/order/confirm", method = {RequestMethod.GET, RequestMethod.POST}, consumes = "*")
  @ResponseBody
  public Map<String, Object> solbiConfirm(@RequestParam("order_trans_no") String orderNo,
                                          @RequestParam("status") int status,
                                          @RequestParam(value = "ret_msg", defaultValue = "") String rejectMsg) {
    Map<String, Object> result = Maps.newHashMap();

    logger.info("SOLBI ORDER CONFIRM : orderNo = {}, status = {}, rejectMsg = {}", orderNo, status, rejectMsg);

    result.put("ret_code", 0);
    result.put("ret_msg", "OK");
    return result;
  }
}
