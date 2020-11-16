/*
 * Filename	: AdminStoreController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcBeacon;
import com.jc.pico.bean.SvcBeaconExample;
import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcBrandExample;
import com.jc.pico.bean.SvcBrandSet;
import com.jc.pico.bean.SvcBrandSetExample;
import com.jc.pico.bean.SvcBrandSetWithBLOBs;
import com.jc.pico.bean.SvcCoupon;
import com.jc.pico.bean.SvcDeviceLicense;
import com.jc.pico.bean.SvcDeviceLicenseExample;
import com.jc.pico.bean.SvcFranchise;
import com.jc.pico.bean.SvcFranchiseExample;
import com.jc.pico.bean.SvcStaff;
import com.jc.pico.bean.SvcStaffExample;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcStoreBeacon;
import com.jc.pico.bean.SvcStoreCctv;
import com.jc.pico.bean.SvcStoreExample;
import com.jc.pico.bean.SvcStoreImg;
import com.jc.pico.bean.SvcStoreImgExample;
import com.jc.pico.bean.SvcStoreNvr;
import com.jc.pico.bean.SvcStoreNvrExample;
import com.jc.pico.bean.SvcStorePrinter;
import com.jc.pico.bean.SvcStorePrinterExample;
import com.jc.pico.bean.SvcStoreSet;
import com.jc.pico.bean.SvcStoreSetWithBLOBs;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.mapper.SvcBeaconMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcBrandSetMapper;
import com.jc.pico.mapper.SvcDeviceLicenseMapper;
import com.jc.pico.mapper.SvcFranchiseMapper;
import com.jc.pico.mapper.SvcStaffMapper;
import com.jc.pico.mapper.SvcStampMapper;
import com.jc.pico.mapper.SvcStoreBeaconMapper;
import com.jc.pico.mapper.SvcStoreCctvMapper;
import com.jc.pico.mapper.SvcStoreImgMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcStoreNvrMapper;
import com.jc.pico.mapper.SvcStorePrinterMapper;
import com.jc.pico.mapper.SvcStoreSetMapper;
import com.jc.pico.utils.AES256Cipher;
import com.jc.pico.utils.AdminHandler;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.RandomUtil;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.customMapper.admin.CustomStoreMapper;

import au.com.bytecode.opencsv.CSVReader;

@Controller
@ResponseBody
@RequestMapping("/admin/model/nine")
public class AdminNineController {

  protected static Logger logger = LoggerFactory.getLogger(AdminNineController.class);

  @Autowired 
  CodeUtil codeUtil;

  @Autowired
  SvcFranchiseMapper svcFranchiseMapper;

  @Autowired
  SvcBrandMapper svcBrandMapper;

  @Autowired
  SvcBrandSetMapper svcBrandSetMapper;

  @Autowired
  SvcStoreMapper svcStoreMapper;

  @Autowired
  SvcStoreSetMapper svcStoreSetMapper;

  @Autowired
  SvcStoreBeaconMapper svcStoreBeaconMapper;

  @Autowired
  SvcStoreImgMapper svcStoreImgMapper;

  @Autowired
  SvcDeviceLicenseMapper svcDeviceLicenseMapper;

  @Autowired
  SvcStaffMapper svcStaffMapper;

  @Autowired
  SvcStoreNvrMapper svcStoreNvrMapper;

  @Autowired
  SvcStampMapper svcStampMapper;

  @Autowired
  SvcBeaconMapper svcBeaconMapper;

  @Autowired
  CustomStoreMapper customStoreMapper;
  
  @Autowired
  SvcStoreCctvMapper svcStoreCctvMapper;  

  @Autowired
  SqlSessionTemplate sessionTemplate;

  /**
   * Store - Franchise
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/Franchise2")
  public Map<Object,Object> Franchise2(
		  HttpSession session,
		  @RequestParam(required=false) SvcFranchise franchise,
	      @RequestParam(required=false) Map<Object,String> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws Exception, InvalidJsonException, ParseException {
	  
	    Map<Object,Object> result = new HashMap<>();
	    Map<String, String> search = new HashMap<>();
	    SvcFranchiseExample fran = new SvcFranchiseExample();
	    SvcBrandExample brand = new SvcBrandExample();
	    SvcStoreExample store = new SvcStoreExample();

    result = AdminHandler.setSessionInfo(result, session, "1");
    
	  if (franchise == null && params.get("data") != null) {
		  logger.debug("params ::::: "+params);
	      search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
	  }

    switch(method){
    case GET :
    	HashMap<String, Object> userInfo = AdminHandler.getUserInfo(session); 
    	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
        logger.debug(" userInfo : " + userInfo);
        logger.debug("★★★★★★★★★★★★★★★★★★★★★");
    	
        if(params.get("data") == null && "300004".equals(userInfo.get("type"))){
        	params.put("franId", String.valueOf(userInfo.get("franId")));
    		params.put("brandId", String.valueOf(userInfo.get("brandId")));
    		params.put("storeId", String.valueOf(userInfo.get("storeId")));
    		params.put("type", String.valueOf(userInfo.get("type")));
    	}
        
        SvcBrandExample.Criteria brandCri = brand.createCriteria();
        SvcStoreExample.Criteria storeCri = store.createCriteria();
        
        if("300004".equals(userInfo.get("type"))){
        	if(userInfo.get("franId") != null && userInfo.get("brandId") == null && userInfo.get("storeId") == null){
        		brandCri.andFranIdEqualTo((Long) userInfo.get("franId"));
        		List<SvcBrand> brList = svcBrandMapper.selectByExample(brand);
                result.put("brList", brList);
        	} else if(userInfo.get("franId") != null && userInfo.get("brandId") != null && userInfo.get("storeId") == null){
        		storeCri.andBrandIdEqualTo((Long) userInfo.get("brandId"));
        		List<SvcStore> stList = svcStoreMapper.selectByExample(store);
                result.put("stList", stList);
        	}
        }else if("300003".equals(userInfo.get("type"))){
        	List<SvcFranchise> frList = svcFranchiseMapper.selectByExample(fran);
            result.put("frList", frList);
        }
        
      result.put("success", true);
      break;

    case POST :
      

      break;

    case PUT :
      

      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false);
      //      result.put("errMsg", "");
      //      break;

    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Store - Franchise
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/Franchise")
  public Map<Object,Object> Franchise(
	  HttpSession session,
	  @RequestParam(required=false) SvcFranchise franchise,
      @RequestParam(required=false) Map<Object,String> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws Exception, InvalidJsonException, ParseException {
	  
    Map<Object,Object> result = new HashMap<>();
    Map<String, String> search = new HashMap<>();
    SvcFranchiseExample example = new SvcFranchiseExample();

    result = AdminHandler.setSessionInfo(result, session, "1");
    
	  if (franchise == null && params.get("data") != null) {
		  logger.debug("params ::::: "+params);
	      search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
	  }

    switch(method){
    case GET :
    	HashMap<String, Object> userInfo = AdminHandler.getUserInfo(session); 
    	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
        logger.debug(" userInfo : " + userInfo);
        logger.debug("★★★★★★★★★★★★★★★★★★★★★");
    	
        SvcFranchiseExample.Criteria frCri = example.createCriteria();
        if(userInfo.get("franId") != null){
        	frCri.andIdEqualTo((Long) userInfo.get("franId"));
        }
        
    	if (franchise == null) {
          if (!String.valueOf(params.get("searchKeyword")).equals("null")) {
        	  frCri.andFranNmLike("%"+params.get("searchKeyword")+"%");
	          //.andServiceIdEqualTo(Globals.PICO_SERVICE_ID)
	          //.andTenantIdEqualTo(Globals.PICO_TENANT_ID);
          }
          
          /*
           * DATATABLE 검색 값 세팅
           */
//          String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
//            if (!searchString.isEmpty()) {
//				criteria.andUsernameLike("%"+searchString+"%");
//			}
          /*
           * DATATABLE 컬럼 정렬값 세팅
           */
          if(params.get("order[0][column]") != null){
            String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                +" "+String.valueOf(params.get("order[0][dir]"));
            example.setOrderByClause(orderby);
          }
          
          List<SvcFranchise> list = svcFranchiseMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", svcFranchiseMapper.countByExample(null));
          result.put("recordsFiltered", svcFranchiseMapper.countByExample(example));
      }
      else {
          result.put("list", svcFranchiseMapper.selectByPrimaryKey(franchise.getId()));
      }
      result.put("success", true);
      break;

    case POST :
      franchise.setServiceId(Globals.PICO_SERVICE_ID);
      franchise.setTenantId(Globals.PICO_TENANT_ID);

      example.createCriteria().andFranCdEqualTo(franchise.getFranCd());

      if (svcFranchiseMapper.countByExample(example) == 0) {
    	  franchise.setOpenDt(franchise.getOpenDt());
    	  result.put("success", svcFranchiseMapper.insertSelective(franchise) == 1 ? true : false);
      }
      else {
    	  result.put("sucess", false);
    	  result.put("errMsg", franchise.getFranCd());
      }

      break;

    case PUT :
      example.createCriteria().andFranCdEqualTo(franchise.getFranCd()).andFranCdNotEqualTo(search.get("franCdEx"));

      if (svcFranchiseMapper.countByExample(example) == 0) {
    	  result.put("success", svcFranchiseMapper.updateByPrimaryKeySelective(franchise) == 1 ? true : false);
      }
      else {
      	  result.put("sucess", false);
      	  result.put("errMsg", franchise.getFranCd());
      }

      break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false);
      //      result.put("errMsg", "");
      //      break;

    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  /**
   * Store - brand
   * @param brand
   * @param brandSet
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
 * @throws Exception 
   */
  @RequestMapping("/Brand")
  public Map<Object,Object> Brand(
		  HttpSession session,
      @RequestParam(required=false) SvcBrand brand,
      @RequestParam(required=false) SvcBrandSetWithBLOBs brandSet,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws Exception {

	  	HashMap<String, Object> userInfo = AdminHandler.getUserInfo(session); 
	  
	    Map<Object, Object> result = new HashMap<>();
	    Map<String, String> search = new HashMap<>();

	    if (brand == null && brandSet == null && params.get("data") != null) {
	        brand = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcBrand.class);
	        brandSet = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcBrandSetWithBLOBs.class);
	        search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	    }
	    
	    logger.debug("-------------- order 9 brand ---------------------");
	    logger.debug("brand : " + JsonConvert.toJson(brand));
	    logger.debug("brandSet : " + JsonConvert.toJson(brandSet));
	    logger.debug("params : " + JsonConvert.toJson(params));
	    logger.debug("userInfo : " + JsonConvert.toJson(userInfo));
    
	  	Map<String, Object> param = Maps.newHashMap();
	  	Map<String, Object> map = Maps.newHashMap();
	  	SvcBrandExample example = new SvcBrandExample();
	    String errMsg = "";

    switch(method){
    case GET :

      if (!String.valueOf(params.get("draw")).equals("null")) {
          if (brand == null) {
        	  params.put("franId", -1l);
              /*String searchString = (String) (params.get("searchKeyword") != null ? params.get("searchKeyword") : "");
              String svcStSearch = (String) (params.get("svcStSearch") != null ? params.get("svcStSearch") : "");
              SvcBrandExample.Criteria criteria = example.createCriteria();
              criteria.andFranIdEqualTo(brand.getFranId() != null ? brand.getFranId() : -1);
              criteria.andBrandNmLike("%"+new String(searchString.getBytes("iso-8859-1"), "utf-8")+"%");
              if (StringUtils.hasText(svcStSearch)) criteria.andSvcStEqualTo(svcStSearch);*/
          }

          /*
           * DATATABLE 검색 값 세팅
           */
//          String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
//        if (!searchString.isEmpty()) {
//			criteria.andUsernameLike("%"+searchString+"%");
//		}
          /*
           * DATATABLE 컬럼 정렬값 세팅
           */
          if(params.get("order[0][column]") != null){
            String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                +" "+String.valueOf(params.get("order[0][dir]"));
              params.put("orderby",orderby);
          }else{
        	  params.put("orderby", "id desc");
          }

          if(params.get("brandId") != null){
        	  params.put("id", params.get("brandId")); 
          }
	      List<LinkedHashMap<String, Object>> list = customStoreMapper.getBrandList(params, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", customStoreMapper.getCountBrandList(params));
          result.put("recordsFiltered", customStoreMapper.getCountBrandList(params));
          result.put("success", true);
      }
      else {
    	  param.put("id", brand.getId());
    	  map = (Map<String, Object>) customStoreMapper.getBrand(param);
    	  map = StrUtils.convertCamelcase(map);

    	  result.put("list", map);
          result.put("success", true);
      }
      break;

    case POST :
    	
    	logger.debug(params.toString());
      brand.setServiceId(Globals.PICO_SERVICE_ID);
      brand.setTenantId(Globals.PICO_TENANT_ID);

      boolean flagPOST = false;
      SvcBrandExample.Criteria svcBrandExampleCriteria = example.createCriteria();
      svcBrandExampleCriteria.andFranIdEqualTo(brand.getFranId());
      svcBrandExampleCriteria.andBrandCdEqualTo(brand.getBrandCd());

      if (svcBrandMapper.countByExample(example) == 0) {
    	  svcBrandMapper.insertSelective(brand);

    	  flagPOST = brand.getId() > 0 ? true : false;
    	  if (flagPOST) {
    		  brandSet.setBrandId(brand.getId());
    		  brandSet.setPgKind(codeUtil.getBaseCodeByAlias("pg-payonline"));
    		  brandSet.setStampDesc("null");
    		  brandSet.setStampNotice("null");
    		  flagPOST = svcBrandSetMapper.insertSelective(brandSet) == 1 ? true : false;
    	  }
      }
      else {
    	  errMsg = brand.getBrandCd();
      }

      result.put("success", flagPOST);
      result.put("errMsg", errMsg);
      break;

    case PUT :
      boolean flagPUT = false;
      example.createCriteria().andBrandCdEqualTo(brand.getBrandCd()).andBrandCdNotEqualTo(search.get("brandCdEx"));

      if (svcBrandMapper.countByExample(example) == 0) {
    	  flagPUT = svcBrandMapper.updateByPrimaryKeySelective(brand) == 1 ? true : false;
    	  if (flagPUT) {
    		  long bsId = StrUtils.parseLong((String) search.get("bsId"));
    		  brandSet.setBrandId(brand.getId());
    		  if (bsId > 0) {
    			  brandSet.setId(bsId);
    			  flagPUT = svcBrandSetMapper.updateByPrimaryKeySelective(brandSet) == 1 ? true : false;
    		  }
    		  else {
        		  brandSet.setStampDesc("null");
        		  brandSet.setStampNotice("null");
    			  flagPUT = svcBrandSetMapper.insertSelective(brandSet) == 1 ? true : false;
    		  }
    	  }
    	  result.put("id", brand.getId());
      }
      else {
      	  errMsg = brand.getBrandCd();
      }

	  result.put("success", flagPUT);
      result.put("errMsg", errMsg);
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
   * Store - Store
   * @param store
   * @param storeSet
   * @param deviceLicense
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
 * @throws Exception 
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/Store")
  public Map<Object,Object> Store( 
	  HttpSession session,
      @RequestParam(required=false) SvcStore store,
      @RequestParam(required=false) SvcStoreSetWithBLOBs storeSet,
      @RequestParam(required=false) SvcDeviceLicense deviceLicense,
      @RequestParam(required=false) Map<Object, Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      
      HttpMethod method) throws Exception {

	  HashMap<String, Object> userInfo = AdminHandler.getUserInfo(session); 
	  
	    Map<Object, Object> result = new HashMap<>();
	    Map<String, String> search = new HashMap<>();
	    String mode = "";
	    String errMsg = "";
	
	    if (store == null && storeSet == null && params.get("data") != null) {
	    	store = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcStore.class);
	    	storeSet = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcStoreSetWithBLOBs.class);
	    	search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	    	mode = search.get("mode");
	    }    
	  
	    logger.debug("------------------------------------------------- Nine9 /store -------------------------");
	    logger.debug("store : " + JsonConvert.toJson(store));
	    logger.debug("storeSet: " + JsonConvert.toJson(storeSet));
	    logger.debug("search : " + JsonConvert.toJson(search));
	    logger.debug("method : " + method);
	    logger.debug("params : " + JsonConvert.toJson(params));
    
    switch (method) {
    case GET :
      if (!String.valueOf(params.get("draw")).equals("null")) {

        //if (store == null) params.put("brandId", -1l);

    	  logger.debug("★★★★★★★★★★★★★★★★★★★★★★★★");
    	  logger.debug(params.toString());
    	  logger.debug("★★★★★★★★★★★★★★★★★★★★★★★★");
        /*
         * DATATABLE 컬럼 정렬값 세팅
         */
        if(params.get("order[0][column]") != null){
        	String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
        					+" "+String.valueOf(params.get("order[0][dir]"));
          	search.put("orderby",orderby);
        }else{
        	search.put("orderby", "id desc");
        } 
        
        checkParams(search);
	    List<LinkedHashMap<String, Object>> list = customStoreMapper.getStoreList(search, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", customStoreMapper.getCountStoreList(search));
        result.put("recordsFiltered", customStoreMapper.getCountStoreList(search));
      }
      else {
    	  
		  logger.debug("★★★★★★★★★★★★★ GET Info★★★★★★★★★★★★★★★");
		  logger.debug("★★★★★★★★★★★★★ MODE =>>> "+mode+"★★★★★★★★★★★★★★★");
		  
    	  Map<String, Object> param = Maps.newHashMap();

    	  if ("service".equals(mode)) {
    		  Map<String, Object> storeMap = Maps.newHashMap();

    		  storeMap.put("id", store.getId());
    		  storeMap = (Map<String, Object>) customStoreMapper.getStoreSet(storeMap);

    		  param.put("brandId", store.getBrandId());
    		  param = (Map<String, Object>) customStoreMapper.getBrandSet(param);

    		  boolean allowStoreSet = (boolean) param.get("allowStoreSet");
			  if (allowStoreSet) {
				  storeMap = StrUtils.convertCamelcase(storeMap);
				  storeMap.put("allowStoreSet", allowStoreSet);
				  result.put("list", storeMap);
    		  }
			  else {
				  param = StrUtils.convertCamelcase(param);
				  param.put("allowStoreSet", allowStoreSet);
				  param.put("svcSt", storeMap.get("svcSt"));
				  result.put("list", param);
			  }
    	  }
    	  else if ("beacon".equals(mode)) {
    		  param.put("id", store.getId());

    	      List<LinkedHashMap<String,Object>> list = customStoreMapper.getStoreBeaconList(param);
        	  result.put("list", list);
    	  }
    	  else if ("pos".equals(mode)) {
    		  param.put("storeId", store.getId());

	          List<LinkedHashMap<String,Object>> list = customStoreMapper.getPosLicenseList(param);
        	  result.put("list", list);
    	  }
    	  else if ("staff".equals(mode)) {
    		  SvcStaffExample example = new SvcStaffExample();
    		  example.createCriteria().andStoreIdEqualTo(store.getId());
    		  List<SvcStaff> svcStaff = new ArrayList<>();

    		  svcStaff = svcStaffMapper.selectByExample(example);
    		  result.put("list", svcStaff);
    	  }
    	  else if ("nvr".equals(mode)) {
    		  SvcStoreNvrExample example = new SvcStoreNvrExample();
    		  example.createCriteria().andStoreIdEqualTo(store.getId());
    		  List<SvcStoreNvr> svcStoreNvr = new ArrayList<>();

    		  svcStoreNvr = svcStoreNvrMapper.selectByExample(example);
    		  result.put("list", svcStoreNvr);
    	  } 
    	  else if ("printer".equals(mode)) {
    	    // 리스트 불러올 때 id를 store id로 key변경 해야함
    	    if (!GenericValidator.isBlankOrNull(search.get("id"))) {
    	      search.put("storeId", search.get("id"));  
    	    }
    	    logger.debug("mode : " + mode + " " + JsonConvert.toJson(search));
    	    List<LinkedHashMap<String, Object>> printers = customStoreMapper.getPrinterList(search);
    	    result.put("list",  printers);
    	  }
    	  else {

    		  SvcStore storeInfo = svcStoreMapper.selectByPrimaryKey(store.getId());
    		  String timezone = "";
    		  if (storeInfo != null) {
    		    timezone = storeInfo.getTimezone();
    		  } else {
    		    timezone = search.get("timezone");
    		  }
    		  result.put("list", storeInfo);
    		  result.put("timezone", getCountryTimezone(timezone, search.get("lang")));
    	  }

      }
      result.put("success", true);
      break;

    case POST : 
		  
      if ("beacon".equals(mode)) {

		Map<String, String> map = StrUtils.getStartWithParam(search, "bId");
		if (MapUtils.isNotEmpty(map)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				SvcStoreBeacon storeBeacon = new SvcStoreBeacon();
				SvcBeacon beacon = new SvcBeacon();
				//String ids = map.get(fn);
				String fn = it.next();
				String idx = StringUtils.replace(fn, "bId", "");
				String beaconIU = search.get("beaconIU"+idx);

				storeBeacon.setLabel(search.get("label"+idx));

				if ("I".equals(beaconIU)) {
					if (StringUtils.hasText(search.get("bId"+idx))) {
						long beaconId = StrUtils.parseLong(search.get("beaconId"+idx));

						storeBeacon.setServiceId(Globals.PICO_SERVICE_ID);
						storeBeacon.setBrandId(store.getBrandId());
						storeBeacon.setStoreId(store.getId());
						storeBeacon.setBeaconId(beaconId);

						int flag = svcStoreBeaconMapper.insertSelective(storeBeacon);

						if (flag == 1) {
							beacon.setId(beaconId);
							beacon.setBrandId(store.getBrandId());
							beacon.setStoreId(store.getId());
							beacon.setStatus(codeUtil.getBaseCodeByAlias("beacon-status-use")); // 선택된 비콘 사용상태로 변경

							flag = svcBeaconMapper.updateByPrimaryKeySelective(beacon);

			        		result.put("success", flag == 1 ? true : false);
						}
					}
				}
				else {
					storeBeacon.setId(StrUtils.parseLong(idx));
					result.put("success", svcStoreBeaconMapper.updateByPrimaryKeySelective(storeBeacon) == 1 ? true : false);
				}
			}
		}

      } else if ("pos".equals(mode)) {

    	
  		Map<String, String> map = StrUtils.getStartWithParam(search, "pId");
  		if (MapUtils.isNotEmpty(map)) {
  			Iterator<String> it = map.keySet().iterator();
  			while (it.hasNext()) {
  				SvcDeviceLicense license = new SvcDeviceLicense();
  				String fn = it.next();
  				String idx = StringUtils.replace(fn, "pId", "");
  				String posIU = search.get("posIU"+idx);

  				license.setServiceId(Globals.PICO_SERVICE_ID);
  				license.setId(StrUtils.parseLong(search.get("pId"+idx)));
  				license.setPosNo(search.get("posNo"+idx));
  				
  				if ("I".equals(posIU)) {
					license.setBrandId(store.getBrandId());
					license.setStoreId(store.getId());
					license.setStatus(codeUtil.getBaseCodeByAlias("poslicense-status-unuse"));
					
					result.put("success", svcDeviceLicenseMapper.updateByPrimaryKeySelective(license) == 1 ? true : false);
  				}else{
  					if(search.get("ckl"+idx) != null){
  	  			    	result.put("success", svcDeviceLicenseMapper.updateByPrimaryKeySelective(license) == 1 ? true : false);
  					}
  				}
  			}
  		}

      } else if ("staff".equals(mode)) {

  		Map<String, String> map = StrUtils.getStartWithParam(search, "staffId");
  		
  		if (MapUtils.isNotEmpty(map)) {
  			Iterator<String> it = map.keySet().iterator();
  			while (it.hasNext()) {
  				SvcStaff staff = new SvcStaff();
  				String fn = it.next();
  				String idx = StringUtils.replace(fn, "staffId", "");
  				String staffIU = search.get("staffIU"+idx);
  				
  				staff.setUsername(search.get("username"+idx));
  				staff.setPassword(search.get("password"+idx));
  				staff.setName(search.get("name"+idx));
  				staff.setMb(search.get("mb"+idx));

				if ("I".equals(staffIU)) {
  					staff.setBrandId(store.getBrandId());
  					staff.setStoreId(store.getId());
  					staff.setUsername(search.get("username"+idx));
  					staff.setStatus("305001");
					result.put("success", svcStaffMapper.insertSelective(staff) == 1 ? true : false);
  				}
  				else {
	  				if(search.get("chk"+idx) != null){
	  					staff.setId(StrUtils.parseLong(idx));	  					
						result.put("success", svcStaffMapper.updateByPrimaryKeySelective(staff) == 1 ? true : false);
	  				}
  				}  				
  			}
  		}
      } else if ("nvr".equals(mode)) {

    	  logger.debug("★★★★★★★★★★★");
    	  logger.debug(params.get("data").toString());
    	  logger.debug(store.getBrandId().toString());
    	  logger.debug("★★★★★★★★★★★");
    	  
    	  Map<String, String> map = StrUtils.getStartWithParam(search, "nvrId");
    	  if (MapUtils.isNotEmpty(map)) {
    		  Iterator<String> it = map.keySet().iterator();
    		  while (it.hasNext()) {
    			  SvcStoreNvr storeNvr = new SvcStoreNvr();
    			  String fn = it.next();
    			  String idx = StringUtils.replace(fn, "nvrId", "");
    			  String cctvIU = search.get("nvrIU"+idx);

    			  storeNvr.setHost(search.get("host"+idx));
    			  storeNvr.setPort(StrUtils.parseInt(search.get("port"+idx)));
    			  storeNvr.setUsername(search.get("username"+idx));
    			  storeNvr.setPassword(search.get("password"+idx));

    			  if ("I".equals(cctvIU)) {
    				  storeNvr.setBrandId(store.getBrandId());
    				  storeNvr.setStoreId(store.getId());
    				  result.put("success", svcStoreNvrMapper.insertSelective(storeNvr) == 1 ? true : false);
    			  }
    			  else {
    				  storeNvr.setId(StrUtils.parseLong(idx));
    				  result.put("success", svcStoreNvrMapper.updateByPrimaryKeySelective(storeNvr) == 1 ? true : false);
    			  }
    		  }
    	  }
      } else if ("printer".equals(mode)) {
        try {
          logger.debug(params.get("data").toString());
          SvcStorePrinterMapper printerMapper = sessionTemplate.getMapper(SvcStorePrinterMapper.class);
          SvcStorePrinter printer = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<SvcStorePrinter>() {});
          logger.debug("printer : " + JsonConvert.toJson(printer));
          
          if (printer.getAccessPwd() != null) {
            printer.setAccessPwd(AES256Cipher.encodeAES256(printer.getAccessPwd()));
          }
          
          if (printer.getUserPwd() != null) {
            printer.setUserPwd(AES256Cipher.encodeAES256(printer.getUserPwd()));
          }
          
          if (printer.getId() != null) {
            result.put("success", printerMapper.updateByPrimaryKeySelective(printer));
          } else {
            result.put("success", printerMapper.insertSelective(printer));            
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        try {
      	  store.setServiceId(Globals.PICO_SERVICE_ID);
      	  store.setTenantId(Globals.PICO_TENANT_ID);
      	  if (GenericValidator.isBlankOrNull(store.getTimezone())) {
      	    store.setTimezone("UTC");      	    
      	  } 
      	  
	      	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
	        logger.debug(" userInfo : " + userInfo);
	        logger.debug("★★★★★★★★★★★★★★★★★★★★★"); 
  	  
		  
		  //브랜드 ID
		  store.setBrandId(Long.valueOf(String.valueOf(userInfo.get("brandId"))));
		  store.setStoreCd("NINESTORE");
		  
          boolean flag = false;
            
      	  if ("info".equals(mode) || !StringUtils.hasText(mode)) {
      		  
      		  if (customStoreMapper.getCheckStore(search) == 0) {
      			  store.setTip("{\"beacon\": false, \"parking\": false, \"babyChair\": false, \"smallRoom\": false, \"party\": false, \"outdoor\": false}");
      			  
      			  if(svcStoreMapper.insertSelective(store) == 1 ? true : false){    				  
      				  
      				  SvcBrandSetExample exaample = new SvcBrandSetExample();
      				  SvcBrandSetExample.Criteria exCriteria = exaample.createCriteria();
      				  exCriteria.andBrandIdEqualTo(store.getBrandId());
      				  
      				  List<SvcBrandSet> brandSet = svcBrandSetMapper.selectByExample(exaample);

      				
      				  if(brandSet != null){ 
      					  storeSet.setBrandId(store.getBrandId());
      					  storeSet.setStoreId(store.getId());
      					  storeSet.setUseStamp(brandSet.get(0).getUseStamp());
      					  storeSet.setUseReview(brandSet.get(0).getUseReview());
      					  storeSet.setUseSelforder(brandSet.get(0).getUseSelforder());
      					  storeSet.setUseReserve(brandSet.get(0).getUseReserve());
      					  storeSet.setUseParty(brandSet.get(0).getUseParty());
      					  storeSet.setOrderBegin(brandSet.get(0).getOrderBegin());
      					  storeSet.setOrderEnd(brandSet.get(0).getOrderEnd());
      					  storeSet.setBudget(brandSet.get(0).getBudget());
      					  storeSet.setRsvDeposit(brandSet.get(0).getRsvDeposit());
      					  storeSet.setPrtDeposit(brandSet.get(0).getPrtDeposit());
      					  storeSet.setStampCnt(brandSet.get(0).getStampCnt());
      					  storeSet.setStampIcon(brandSet.get(0).getStampIcon());
      					  storeSet.setStampBg(brandSet.get(0).getStampBg());
      					  storeSet.setStampCoupon(brandSet.get(0).getStampCoupon());
      					  storeSet.setStampTerm(brandSet.get(0).getStampTerm());
      					  storeSet.setStampDesc("null");
      			    	  storeSet.setStampNotice("null");
      					  
      					  if(svcStoreSetMapper.insertSelective(storeSet) == 1 ? true : false){
      						  flag = svcStoreMapper.updateByPrimaryKeySelective(store) == 1 ? true : false;
      					  }
      				  }
      			  }
      		  } else {
      			  errMsg = store.getStoreCd();
      		  }
      	  } else {    		  
      		  store.setTip("{\"beacon\": false, \"parking\": false, \"babyChair\": false, \"smallRoom\": false, \"party\": false, \"outdoor\": false}");
      		  if(svcStoreMapper.insertSelective(store) == 1 ? true : false){
      			  SvcBrandSetExample exaample = new SvcBrandSetExample();
  				  SvcBrandSetExample.Criteria exCriteria = exaample.createCriteria();
  				  exCriteria.andBrandIdEqualTo(store.getBrandId());
  				  
  				  List<SvcBrandSet> brandSet = svcBrandSetMapper.selectByExample(exaample);
  				  
  				  if(brandSet != null){
  					  storeSet.setBrandId(store.getBrandId());
  					  storeSet.setStoreId(store.getId());
  					  storeSet.setUseStamp(brandSet.get(0).getUseStamp());
  					  storeSet.setUseReview(brandSet.get(0).getUseReview());
  					  storeSet.setUseSelforder(brandSet.get(0).getUseSelforder());
  					  storeSet.setUseReserve(brandSet.get(0).getUseReserve());
  					  storeSet.setUseParty(brandSet.get(0).getUseParty());
  					  storeSet.setOrderBegin(brandSet.get(0).getOrderBegin());
  					  storeSet.setOrderEnd(brandSet.get(0).getOrderEnd());
  					  storeSet.setBudget(brandSet.get(0).getBudget());
  					  storeSet.setRsvDeposit(brandSet.get(0).getRsvDeposit());
  					  storeSet.setPrtDeposit(brandSet.get(0).getPrtDeposit());
  					  storeSet.setStampCnt(brandSet.get(0).getStampCnt());
  					  storeSet.setStampIcon(brandSet.get(0).getStampIcon());
  					  storeSet.setStampBg(brandSet.get(0).getStampBg());
  					  storeSet.setStampCoupon(brandSet.get(0).getStampCoupon());
  					  storeSet.setStampTerm(brandSet.get(0).getStampTerm());
  					  storeSet.setStampDesc("null");
  			    	  storeSet.setStampNotice("null");
  					  
  					  if(svcStoreSetMapper.insertSelective(storeSet) == 1 ? true : false){
  						  flag = svcStoreMapper.updateByPrimaryKeySelective(store) == 1 ? true : false;
  					  }
  				  }
      		  }
      	  }
      	  result.put("success", flag);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      result.put("errMsg", errMsg);
      break;

    case PUT :
      try {
        if ("service".equals(mode)) {
        	
	      	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
	        logger.debug(" storeSet : " + storeSet);
	        logger.debug("★★★★★★★★★★★★★★★★★★★★★"); 
	        
          if ("true".equals(storeSet.getPgUseTp())) {
            storeSet.setPgUseTp(codeUtil.getBaseCodeByAlias("use-brand-pg"));
          } else {
            storeSet.setPgUseTp("");
          }
          storeSet.setId(Long.parseLong(search.get("storeSetId")));
          
      	  boolean flag = true;
      	  String allowStoreSet = search.get("allowStoreSet");
      	  
      	  if ("true".equals(allowStoreSet)) {
      		  flag = svcStoreSetMapper.updateByPrimaryKeySelective(storeSet) == 1 ? true : false;
      	  }
      	  if (flag) {
      		  store.setId(storeSet.getStoreId());
      		  flag = svcStoreMapper.updateByPrimaryKeySelective(store) == 1 ? true : false;
      	  }
      	  result.put("success", flag);
      	  
        } else if ("printer".equals(mode)) {
          SvcStorePrinterMapper printerMapper = sessionTemplate.getMapper(SvcStorePrinterMapper.class);
          SvcStorePrinter printer = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<SvcStorePrinter>() {});
          logger.debug("printer : " + JsonConvert.toJson(printer));
          
          if (printer.getAccessPwd() != null) {
            printer.setAccessPwd(AES256Cipher.encodeAES256(printer.getAccessPwd()));
          }
          
          if (printer.getUserPwd() != null) {
            printer.setUserPwd(AES256Cipher.encodeAES256(printer.getUserPwd()));
          }
          logger.debug("printer : " + JsonConvert.toJson(printer));
          result.put("success", printerMapper.updateByPrimaryKeySelective(printer) == 1 ? true : false);
          result.put("data", printer);
        }
        else {
            boolean flag = false;
  
      	  if ("info".equals(mode) || !StringUtils.hasText(mode)) {
      		  SvcStoreExample example = new SvcStoreExample();
      	      example.createCriteria().andStoreCdEqualTo(store.getStoreCd()).andStoreCdNotEqualTo(search.get("storeCdEx"));
  
      	      if (svcStoreMapper.countByExample(example) == 0) {
      	    	  flag = svcStoreMapper.updateByPrimaryKeySelective(store) == 1 ? true : false;
      	      }
      	      else {
      	    	  errMsg = store.getStoreCd();
      	      }
      	  }
      	  else {
      		  flag = svcStoreMapper.updateByPrimaryKeySelective(store) == 1 ? true : false;
      	  }
      	  result.put("success", flag);
      	  result.put("id", store.getId());
        }
        result.put("errMsg", errMsg);
      } catch (Exception e) {
        e.printStackTrace();
      }
      break;

    case DELETE :
      SvcStorePrinterMapper printerMapper = sessionTemplate.getMapper(SvcStorePrinterMapper.class);
        if ("beacon".equals(mode)) {
        	int flag = svcStoreBeaconMapper.deleteByPrimaryKey(store.getId());

        	if (flag == 1) {
        		SvcBeacon beacon = new SvcBeacon();
        		beacon.setId(StrUtils.parseLong(search.get("beaconId")));
        		beacon.setBrandId(0l);
        		beacon.setStoreId(0l);
        		beacon.setStatus(codeUtil.getBaseCodeByAlias("beacon-status-in")); // 선택된 비콘 재고상태로 변경

        		flag = svcBeaconMapper.updateByPrimaryKeySelective(beacon);

        		result.put("success", flag == 1 ? true : false);
        	}
        }
        else if ("pos".equals(mode)) {
    		SvcDeviceLicense license = new SvcDeviceLicense();
    		license.setId(StrUtils.parseLong(search.get("id")));
    		license.setBrandId(0l);
    		license.setStoreId(0l);
    		license.setStatus(codeUtil.getBaseCodeByAlias("poslicense-status-unuse"));
        	result.put("success", svcDeviceLicenseMapper.updateByPrimaryKeySelective(license) == 1 ? true : false);
        }
        else if ("staff".equals(mode)) {
        	result.put("success", svcStaffMapper.deleteByPrimaryKey(store.getId()) == 1 ? true : false);
        }
        else if ("cctv".equals(mode)) {
        	result.put("success", svcStoreNvrMapper.deleteByPrimaryKey(store.getId()) == 1 ? true : false);
        }
        else if ("printer".equals(mode)) {
          result.put("success", printerMapper.deleteByPrimaryKey(StrUtils.parseLong(search.get("id")))== 1 ? true : false);
        }
        result.put("errMsg", "");
      break;

    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  /**
   * Multi File List/Delete
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/Files")
  public Map<Object,Object> Files(@RequestParam(required=false) Map<Object,String> params, HttpMethod method) throws InvalidJsonException {

    Map<Object,Object> result = new HashMap<>();
    Map<String,String> param = new HashMap<>();
    SvcStoreImgExample example = new SvcStoreImgExample();

    long id = 0;

	if (params.get("data") != null) {
		param = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
		id = StrUtils.parseLong((String) param.get("id"));
	}

    switch (method) {
    case GET :
		example.createCriteria().andStoreIdEqualTo(id);
    	List<SvcStoreImg> list = svcStoreImgMapper.selectByExample(example);
    	result.put("list", list);
    	result.put("success", true);
    	break;

    case DELETE :
    	svcStoreImgMapper.deleteByPrimaryKey(id);
    	result.put("success", true);
    	break;

    default :
	    result.put("success", false);
	    result.put("errMsg", "not supported method.");
    }

    return result;
  }

  /**
   * Multi File Upload
   * @param request
   * @param files
   * @return
   * @throws IOException
   */
  @RequestMapping(value="/FilesUpload", method=RequestMethod.POST)
  public Map<Object,Object> FilesUpload(MultipartHttpServletRequest request, @RequestParam("files") MultipartFile[] files) throws IOException {
	  Map<Object, Object> resultMap = Maps.newHashMap();

	  String id = String.valueOf(request.getParameter("id"));
	  String brandId = String.valueOf(request.getParameter("brandId"));
	  //String gubun = String.valueOf(request.getParameter("mode"));
	  String file_path = Globals.IMG_RESOURCE+"/store/store/";
	  String result_path = "/image-resource/store/store/";

	  
	  
	  for (int i = 0; i < files.length; i++) {
		  SvcStoreImg storeImg = new SvcStoreImg();
          String fileName = files[i].getOriginalFilename();
          String save_file_name = UUID.randomUUID().toString().replaceAll("-", "") + fileName.substring(fileName.lastIndexOf("."));
          
    	  String resultUrl = null;
		  long timestamp = Calendar.getInstance().getTimeInMillis();

		  byte[] bytes = files[i].getBytes();
		  File Folder = new File(file_path+id);
		  File lOutFile = new File(file_path+id+"/"+timestamp+"_"+save_file_name);

		  if (!Folder.isDirectory()) Folder.mkdirs();

		  if (lOutFile.isFile()) {
			  if (lOutFile.delete()) {
				  FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
				  lFileOutputStream.write(bytes);
				  lFileOutputStream.close();
				  resultUrl = result_path+id+"/"+timestamp+"_"+save_file_name;
			  }
		  }
		  else {
			  FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
			  lFileOutputStream.write(bytes);
			  lFileOutputStream.close();
			  resultUrl = result_path+id+"/"+timestamp+"_"+save_file_name;
		  }

		  storeImg.setStoreId(StrUtils.parseLong(id));
		  storeImg.setBrandId(StrUtils.parseLong(brandId));
		  storeImg.setOrdinal((byte) i);
		  storeImg.setImage(resultUrl);
		  storeImg.setImageView(fileName);
		  svcStoreImgMapper.insertSelective(storeImg);

		  if (System.getProperty("os.name").contains("Linux")) {
			  String chmod = "chmod 777 "+file_path+id+"/"+timestamp+"_"+save_file_name;

			  try {
				  Process p = Runtime.getRuntime().exec(chmod);
				  BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				  String line = null;

				  while ((line = br.readLine()) != null) {
					  logger.info("commend line : "+line);
				  }
			  }
			  catch (Exception e) {
				  logger.error(e.getMessage());
			  }
		  }
		  resultMap.put("success", true);
	  }

	  return resultMap;
  }

  /**
   * Store - Stamp
   * @param store
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/Stamp")
  public Map<Object,Object> Stamp(
	  @RequestParam(required=false) SvcCoupon coupon,
      @RequestParam(required=false) SvcBrand brand,
      @RequestParam(required=false) SvcBrandSetWithBLOBs brandSet,
      @RequestParam(required=false) SvcStore store,
      @RequestParam(required=false) SvcStoreSetWithBLOBs storeSet,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object,Object> result = new HashMap<>();
    Map<String, String> couponData = new HashMap<>();
    
    String gubun = "";
    if (brandSet == null && storeSet == null && params.get("data") != null) {
    	brandSet = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcBrandSetWithBLOBs.class);
    	storeSet = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcStoreSetWithBLOBs.class);
    	couponData = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
    	
    	gubun = String.valueOf(params.get("gubun"));
    }

    switch (method) {
    case GET :
      boolean flag = true;
      String rqMethod = "POST";
      long brandId = StrUtils.parseLong(String.valueOf(params.get("brandId")));

      brand = svcBrandMapper.selectByPrimaryKey(brandId);

      if (StringUtils.hasText((String) params.get("storeId"))) {

    	  if (codeUtil.getBaseCodeByAlias("stamp-tp-all").equals(brand.getStampTp())) { // 매장통합적립
    		  flag = false;
    	  }
    	  else {
    		  result.put("data", customStoreMapper.getStampStoreInfo(params));
    		  
    	  }
          result.put("gubun", "storeSet");
      }
      else {
    	  result.put("data", customStoreMapper.getStampBrandInfo(params));

    	  result.put("gubun", "brandSet");
      }

      result.put("rqMethod", result.get("data") == null ? "POST" : "PUT");
      result.put("success", flag);
      break;

    case POST :
    	
    	logger.debug("★★★★★★★★★★POST★★★★★★★★★★★★");
    	logger.debug(params.toString());
    	logger.debug(couponData.toString());
    	logger.debug("★★★★★★★★★★★★★★★★★★★★★★");
    	
    	if(couponData.get("couponId").isEmpty()){
    		
    		couponData.put("id", couponData.get("couponId"));
    		couponData.put("couponTp", "406001");
    		couponData.put("isDefault", "1");
    		couponData.put("term", couponData.get("couponTerm"));
    		couponData.put("expireTp", "404001");
    		couponData.put("isAll", "1");
    		
    		// 브랜드 설정인 경우 STORE_ID 값이 없으므로 0으로 세팅
    		if("brandSet".equals(gubun)){
    			couponData.put("storeId", "0");
    		}
    		
    		if(customStoreMapper.insertStampCoupon(couponData) > 0){
        		if ("brandSet".equals(gubun)) {

        			// TB_SVC_COUPON 테이블에 등록후 해당 ID 값을 TB_SCV
        			brandSet.setStampCoupon(Long.parseLong(String.valueOf(couponData.get("id"))));
        			brandSet.setUseStamp(true);
        			if(brandSet.getId() == null){
        				result.put("success", svcBrandSetMapper.insertSelective(brandSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcBrandSetMapper.updateByPrimaryKeySelective(brandSet) == 1 ? true : false);
        			}
        			
        			 result.put("id", brandSet.getId());
        		}else{
        			storeSet.setStampCoupon(Long.valueOf(couponData.get("id")));
        			storeSet.setUseStamp(true);
        			if(storeSet.getId() == null){
        				result.put("success", svcStoreSetMapper.insertSelective(storeSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcStoreSetMapper.updateByPrimaryKeySelective(storeSet) == 1 ? true : false);
        			}
        			result.put("id", storeSet.getId());
        		}
        	}
    	}else{
    		
    		couponData.put("id", couponData.get("couponId"));
    		couponData.put("term", couponData.get("couponTerm"));
    		
    		if(customStoreMapper.updateStampCoupon(couponData) > 0){
    			if ("brandSet".equals(gubun)) {

        			// TB_SVC_COUPON 테이블에 등록후 해당 ID 값을 TB_SCV
        			brandSet.setStampCoupon(Long.parseLong(String.valueOf(couponData.get("id"))));
        			brandSet.setUseStamp(true);
        			if(brandSet.getId() == null){
        				result.put("success", svcBrandSetMapper.insertSelective(brandSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcBrandSetMapper.updateByPrimaryKeySelective(brandSet) == 1 ? true : false);
        			}
        			
        			 result.put("id", brandSet.getId());
        		}else{
        			storeSet.setStampCoupon(Long.valueOf(couponData.get("id")));
        			storeSet.setUseStamp(true);
        			if(storeSet.getId() == null){
        				result.put("success", svcStoreSetMapper.insertSelective(storeSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcStoreSetMapper.updateByPrimaryKeySelective(storeSet) == 1 ? true : false);
        			}
        			result.put("id", storeSet.getId());
        		}        		
        	}
    	}

      break;

    case PUT :
    	
    	logger.debug("★★★★★★★★★ PUT ★★★★★★★★★★★★★");
    	logger.debug(params.toString());
    	logger.debug("★★★★★★★★★★★★★★★★★★★★★★");
    	
    	if(couponData.get("couponId").isEmpty()){
    		
    		couponData.put("id", couponData.get("couponId"));
    		couponData.put("couponTp", "406001");
    		couponData.put("isDefault", "1");
    		couponData.put("term", couponData.get("couponTerm"));
    		couponData.put("expireTp", "404001");
    		couponData.put("isAll", "1");
    		
    		// 브랜드 설정인 경우 STORE_ID 값이 없으므로 0으로 세팅
    		if("brandSet".equals(gubun)){
    			couponData.put("storeId", "0");
    		}
    		
    		if(customStoreMapper.insertStampCoupon(couponData) > 0){
        		if ("brandSet".equals(gubun)) {

        			// TB_SVC_COUPON 테이블에 등록후 해당 ID 값을 TB_SCV
        			brandSet.setStampCoupon(Long.parseLong(String.valueOf(couponData.get("id"))));
        			brandSet.setUseStamp(true);
        			if(brandSet.getId() == null){
        				result.put("success", svcBrandSetMapper.insertSelective(brandSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcBrandSetMapper.updateByPrimaryKeySelective(brandSet) == 1 ? true : false);
        			}
        			
        			 result.put("id", brandSet.getId());
        		}else{
        			storeSet.setStampCoupon(Long.valueOf(couponData.get("id")));
        			storeSet.setUseStamp(true);
        			if(storeSet.getId() == null){
        				result.put("success", svcStoreSetMapper.insertSelective(storeSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcStoreSetMapper.updateByPrimaryKeySelective(storeSet) == 1 ? true : false);
        			}
        			result.put("id", storeSet.getId());
        		}
        	}
    	}else{
    		
    		couponData.put("id", couponData.get("couponId"));
    		couponData.put("term", couponData.get("couponTerm"));
    		
    		if(customStoreMapper.updateStampCoupon(couponData) > 0){
    			if ("brandSet".equals(gubun)) {

        			// TB_SVC_COUPON 테이블에 등록후 해당 ID 값을 TB_SCV
        			brandSet.setStampCoupon(Long.parseLong(String.valueOf(couponData.get("id"))));
        			brandSet.setUseStamp(true);
        			if(brandSet.getId() == null){
        				result.put("success", svcBrandSetMapper.insertSelective(brandSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcBrandSetMapper.updateByPrimaryKeySelective(brandSet) == 1 ? true : false);
        			}
        			
        			 result.put("id", brandSet.getId());
        		}else{
        			storeSet.setStampCoupon(Long.valueOf(couponData.get("id")));
        			storeSet.setUseStamp(true);
        			if(storeSet.getId() == null){
        				result.put("success", svcStoreSetMapper.insertSelective(storeSet) == 1 ? true : false);
        			}else{
        				result.put("success", svcStoreSetMapper.updateByPrimaryKeySelective(storeSet) == 1 ? true : false);
        			}
        			result.put("id", storeSet.getId());
        		}        		
        	}
    	}

      break;

    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  /**
   * Store - Beacon
   * @param beacon
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
 * @throws UnsupportedEncodingException
   */
  @RequestMapping("/Beacon")
  public Map<Object,Object> Beacon(
      @RequestParam(required=false) SvcBeacon beacon,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object,Object> result = new HashMap<>();

    if (beacon == null && params.get("data") != null) {
    	beacon = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcBeacon.class);
    }

    SvcBeaconExample example = new SvcBeaconExample();
    String errMsg = "";
    
    switch (method) {
    case GET :

    	if (!String.valueOf(params.get("draw")).equals("null")) {
            String searchString = (String) (params.get("searchKeyword") != null ? params.get("searchKeyword") : "");
            String statusSearch = (String) (params.get("statusSearch") != null ? params.get("statusSearch") : "");

            if (StringUtils.hasText(statusSearch)) params.put("status", statusSearch);
            if (StringUtils.hasText(searchString)) params.put("uuid", searchString);

            /*
             * DATATABLE 검색 값 세팅
             */
//            String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
//          if (!searchString.isEmpty()) {
//				criteria.andUsernameLike("%"+searchString+"%");
//			}
            /*
             * DATATABLE 컬럼 정렬값 세팅
             */
            if(params.get("order[0][column]") != null){
              String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                  +" "+String.valueOf(params.get("order[0][dir]"));
                params.put("orderby", orderby);
            } 
            
	        List<LinkedHashMap<String,Object>> list = customStoreMapper.getBeaconList(params, new RowBounds(start, length));

	        result.put("list", list);
	        result.put("recordsTotal", customStoreMapper.getCountBeaconList(params));
	        result.put("recordsFiltered", customStoreMapper.getCountBeaconList(params));
		}
		else {
			params.put("id", beacon.getId());
	        result.put("list", customStoreMapper.getBeaconList(params, new RowBounds(0, 1)));
		}
		result.put("success", true);
        break;

    case POST :
    	beacon.setServiceId(Globals.PICO_SERVICE_ID);
    	beacon.setTenantId(Globals.PICO_TENANT_ID);
    	example.clear();
    	example.createCriteria().andUuidEqualTo(beacon.getUuid())
    	.andMajorEqualTo(beacon.getMajor())
    	.andMinorEqualTo(beacon.getMinor());

    	if (svcBeaconMapper.countByExample(example) > 0) {
    		result.put("success", false);
    	}
    	else {
    		result.put("success", svcBeaconMapper.insertSelective(beacon) == 1 ? true : false);
    	}
    	result.put("errMsg", errMsg);
        break;

    case PUT :
    	beacon.setServiceId(Globals.PICO_SERVICE_ID);
    	beacon.setTenantId(Globals.PICO_TENANT_ID);
    	result.put("success", svcBeaconMapper.updateByPrimaryKeySelective(beacon) == 1 ? true : false );
        result.put("errMsg", errMsg);
        break;

    case DELETE :
        result.put("success", svcBeaconMapper.deleteByPrimaryKey(beacon.getId()) == 1 ? true : false );
        result.put("errMsg", errMsg);
        break;

    default :
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }

	@RequestMapping(value = "/BeaconCsv", method = RequestMethod.POST)
	public Map<Object,Object> BeaconCsv(MultipartHttpServletRequest req, HttpServletRequest request) throws IOException {
		Map<Object,Object> result = new HashMap<>();

		File serverFile = csvFile(req, request);
	    String[] nextLine;
        //read file
        //CSVReader(fileReader, ';', '\'', 1) means
        //using separator ; and using single quote ' . Skip first line when read
    	//BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(serverFile), "MS949"));
        try (FileReader fileReader = new FileReader(serverFile);
            CSVReader reader = new CSVReader(fileReader, ';', '\'', 1);) {

        	SvcBeaconExample example = new SvcBeaconExample();
        	List<SvcBeacon> beaconList = new ArrayList<SvcBeacon>();
        	boolean flag = true;

        	while ((nextLine = reader.readNext()) != null) {
        		SvcBeacon beacon = new SvcBeacon();
        		beacon.setServiceId(Globals.PICO_SERVICE_ID);
        		beacon.setTenantId(Globals.PICO_TENANT_ID);

        		for (int i = 0; i < nextLine.length; i++) {
                	String [] val = nextLine[i].split(",", -1);
                	String uuid = val[0];
                	String str1 = val[1];
                	String str2 = val[2];
                	int major = 0;
                	int minor = 0;
                	if (!"".equals(str1)) major = Integer.parseInt(str1);
                	if (!"".equals(str2)) minor = Integer.parseInt(str2);

                	example.clear();
                	example.createCriteria().andUuidEqualTo(uuid)
                	.andServiceIdEqualTo(beacon.getServiceId())
                	.andTenantIdEqualTo(beacon.getTenantId());

                	if (svcBeaconMapper.countByExample(example) > 0) {
            	        result.put("errMsg", uuid);
            	        flag = false;
            	        break;
            	        //throw new IOException();
            	        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                	}
                	else {
                		flag = true;
	                	beacon.setUuid(uuid);
	            		beacon.setMajor(major);
	            		beacon.setMinor(minor);
	            		beacon.setLabel(val[3]);
	            		beacon.setStatus(codeUtil.getBaseCodeByAlias("beacon-status-in")); // 기본 재고상태
	            		beaconList.add(beacon);
                	}
                }
                if (!flag) break;
            }

        	if (flag) {
	        	Map<String, Object> map = new HashMap<>();
	        	map.put("list", beaconList);
	        	customStoreMapper.insertBeaconCsv(map);
        	}
        	result.put("success", flag);
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
			result.put("success", false);
	    }

	  	return result;
	}

	/**
	   * Store - CCTV
	   * @param license
	   * @param params
	   * @param start
	   * @param length
	   * @param method
	   * @return
	   * @throws InvalidJsonException
	   */
	  @RequestMapping("/Cctv")
	  public Map<Object,Object> Cctv(
			  @RequestParam(required=false) SvcStoreCctv svcStoreCctv,
			  @RequestParam(required=false) Map<Object,Object> params,
			  @RequestParam(required=false, value="start", defaultValue="0") int start,
			  @RequestParam(required=false, value="length", defaultValue="10") int length,
			  HttpMethod method) throws InvalidJsonException {

		  Map<Object,Object> result = new HashMap<>();
		  Map<String, String> search = new HashMap<>();
		  
		  if (svcStoreCctv == null && params.get("data") != null) {
			  svcStoreCctv = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcStoreCctv.class);
	          search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
		  }
		  
		  switch(method) {
		  case GET :
	    	  if (!String.valueOf(params.get("draw")).equals("null")) {

	              String searchString = (String) (params.get("searchKeyword") != null ? params.get("searchKeyword") : "");
	              String deviceTpSearch = (String) (params.get("deviceTpSearch") != null ? params.get("deviceTpSearch") : "");
	              String statusSearch = (String) (params.get("statusSearch") != null ? params.get("statusSearch") : "");

	              params.put("licenseKey", searchString);
	              
	              if (StringUtils.hasText(deviceTpSearch)) params.put("deviceTp", deviceTpSearch);
	              if (StringUtils.hasText(statusSearch)) params.put("status", statusSearch);

	              if(params.get("order[0][column]") != null){
	                String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
	                    +" "+String.valueOf(params.get("order[0][dir]"));
	                params.put("orderby", orderby);
	              }
	              
		          List<LinkedHashMap<String,Object>> list = customStoreMapper.getCctvList(search, new RowBounds(start, length));

		          result.put("list", list);
		          result.put("recordsTotal", customStoreMapper.getCountCctvList(params));
			      result.put("recordsFiltered", customStoreMapper.getCountCctvList(params));
			  }
			  else {
				  params.put("id", svcStoreCctv.getId());
		          result.put("list", customStoreMapper.getCctvList(search, new RowBounds(start, length)));
			  }

			  result.put("success", true);
			  break;

		  case POST :

			  Map<String, String> map = StrUtils.getStartWithParam(search, "cctvId");
			  
		  		if (MapUtils.isNotEmpty(map)) {
		  			Iterator<String> it = map.keySet().iterator();
		  			while (it.hasNext()) {
		  				String fn = it.next();
		  				String idx = StringUtils.replace(fn, "cctvId", "");
		  				String cctvIU = search.get("cctvIU"+idx);
		  				
		  				svcStoreCctv.setNvrId(Long.valueOf(search.get("nvrId")));
		  				svcStoreCctv.setNo(Short.valueOf((String) search.get("no"+idx)));
		  				svcStoreCctv.setName(search.get("name"+idx));
		  				svcStoreCctv.setIp(search.get("ip"+idx));
		  				svcStoreCctv.setModel(search.get("model"+idx));
		  				
		  				// 임시셋팅
	    			  	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	    			  	Date now = new Date();
	        			
	    			  	svcStoreCctv.setSetDt(now); 	// 발행일자

						if ("I".equals(cctvIU)) {
							svcStoreCctv.setBrandId(Long.valueOf(search.get("brandId")));
							svcStoreCctv.setStoreId(Long.valueOf(search.get("storeId")));
							
							result.put("success", svcStoreCctvMapper.insertSelective(svcStoreCctv) == 1 ? true : false);
		  				}
		  				else {
			  				if(search.get("chk"+idx) != null){
			  					svcStoreCctv.setId(StrUtils.parseLong(idx));	  					
								result.put("success", svcStoreCctvMapper.updateByPrimaryKeySelective(svcStoreCctv) == 1 ? true : false);
			  				}
		  				}  				
		  			}
		  		}
			  break;

		  case PUT :
//			  result.put("success", svcStoreCctvMapper.updateByPrimaryKeySelective(svcStoreCctv) == 1 ? true : false );
//			  result.put("errMsg", "");
			  break;

		  case DELETE :
			  result.put("success", svcStoreCctvMapper.deleteByPrimaryKey(svcStoreCctv.getId()) == 1 ? true : false );
			  result.put("errMsg", "");
			  break;

		  default :
			  result.put("success", false);
			  result.put("errMsg", "not supported method.");
		  }

		  return result;
	  }
	  
  /**
   * Store - POS License
   * @param license
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/PosLicense")
  public Map<Object,Object> PosLicense(
		  @RequestParam(required=false) SvcDeviceLicense license,
		  @RequestParam(required=false) Map<Object,Object> params,
		  @RequestParam(required=false, value="start", defaultValue="0") int start,
		  @RequestParam(required=false, value="length", defaultValue="10") int length,
		  HttpMethod method) throws InvalidJsonException {

	  Map<Object,Object> result = new HashMap<>();
	  Map<String, String> search = new HashMap<>();
	  
	  if (license == null && params.get("data") != null) {
		  license = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcDeviceLicense.class);
          search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	  }

	  switch(method) {
	  case GET :
    	  if (!String.valueOf(params.get("draw")).equals("null")) {

              String searchString = (String) (params.get("searchKeyword") != null ? params.get("searchKeyword") : "");
              String deviceTpSearch = (String) (params.get("deviceTpSearch") != null ? params.get("deviceTpSearch") : "");
              String statusSearch = (String) (params.get("statusSearch") != null ? params.get("statusSearch") : "");

              params.put("licenseKey", searchString);
              if (StringUtils.hasText(deviceTpSearch)) params.put("deviceTp", deviceTpSearch);
              if (StringUtils.hasText(statusSearch)) params.put("status", statusSearch);

              if(params.get("order[0][column]") != null){
                String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                    +" "+String.valueOf(params.get("order[0][dir]"));
                params.put("orderby", orderby);
              }
              
	          List<LinkedHashMap<String,Object>> list = customStoreMapper.getPosLicenseList(params, new RowBounds(start, length));

	          result.put("list", list);
	          result.put("recordsTotal", customStoreMapper.getCountPosLicenseList(params));
		      result.put("recordsFiltered", customStoreMapper.getCountPosLicenseList(params));
		  }
		  else {
			  params.put("id", license.getId());
	          result.put("list", customStoreMapper.getPosLicenseList(params, new RowBounds(0, 1)));
		  }

		  result.put("success", true);
		  break;

	  case POST :
		  license.setServiceId(Globals.PICO_SERVICE_ID);
	      license.setTenantId(Globals.PICO_TENANT_ID);

	      boolean flag = true;
	      int licenseCnt = StrUtils.parseInt(search.get("licenseCnt"));

	      if (licenseCnt > 0) {
	    	  for (int i = 0; i < licenseCnt; i++) {
	    		  license.setLicenseKey(RandomUtil.getLicense());
	    		  license.setStatus(codeUtil.getBaseCodeByAlias("poslicense-status-unuse"));
	    		  flag = svcDeviceLicenseMapper.insertSelective(license) == 1 ? true : false;
			  }
	      }

		  result.put("success", flag);
		  result.put("errMsg", "");
		  break;

	  case PUT :
		  result.put("success", svcDeviceLicenseMapper.updateByPrimaryKeySelective(license) == 1 ? true : false );
		  result.put("errMsg", "");
		  break;

	  case DELETE :
		  result.put("success", svcDeviceLicenseMapper.deleteByPrimaryKey(license.getId()) == 1 ? true : false );
		  result.put("errMsg", "");
		  break;

	  default :
		  result.put("success", false);
		  result.put("errMsg", "not supported method.");
	  }

	  return result;
  }

	@RequestMapping(value = "/PosLicenseCsv", method = RequestMethod.POST)
	public Map<Object,Object> PosLicenseCsv(MultipartHttpServletRequest req, HttpServletRequest request) throws IOException, ParseException {
		Map<Object,Object> result = new HashMap<>();

		File serverFile = csvFile(req, request);
	    String[] nextLine;

        //read file
        try (FileReader fileReader = new FileReader(serverFile);
	        CSVReader reader = new CSVReader(fileReader, ';', '\'', 1);) {

        	SvcDeviceLicenseExample example = new SvcDeviceLicenseExample();
	      	List<SvcDeviceLicense> licenseList = new ArrayList<SvcDeviceLicense>();
	      	boolean flag = true;

	      	while ((nextLine = reader.readNext()) != null) {
	      		SvcDeviceLicense license = new SvcDeviceLicense();
	      		license.setServiceId(Globals.PICO_SERVICE_ID);
	      		license.setTenantId(Globals.PICO_TENANT_ID);

	      		for (int i = 0; i < nextLine.length; i++) {
	              	String [] val = nextLine[i].split(",", -1);
	              	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	              	String str1 = val[2];
	              	String str2 = val[3];
	              	Date begin = format.parse(str1);
	              	Date end = format.parse(str2);
	              	String licenseKey = val[4];

	              	example.clear();
	              	example.createCriteria().andLicenseKeyEqualTo(licenseKey)
	              	.andServiceIdEqualTo(license.getServiceId())
	              	.andTenantIdEqualTo(license.getTenantId());

	              	if (svcDeviceLicenseMapper.countByExample(example) > 0) {
	          	        result.put("errMsg", licenseKey);
	          	        flag = false;
	          	        break;
	              	}
	              	else {
	              		flag = true;
	              		license.setDeviceTp(val[0]);
	                	license.setName(val[1]);
	            		license.setBegin(begin);
	            		license.setEnd(end);
	            		license.setLicenseKey(licenseKey);
	            		licenseList.add(license);
	              	}
	      		}
                if (!flag) break;
            }

	      	if (flag) {
	        	Map<String, Object> map = new HashMap<>();
	        	map.put("list", licenseList);
	        	customStoreMapper.insertPosLicenseCsv(map);
	      	}
	      	result.put("success", flag);
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
			result.put("success", false);
	    }

	  	return result;
	}

	public File csvFile(MultipartHttpServletRequest req, HttpServletRequest request) throws IOException {
	  	MultipartFile file = req.getFile("csv");
	    //if (file.isEmpty())

	    String rootPath = request.getSession().getServletContext().getRealPath("/");
	    File dir = new File(rootPath + File.separator + "uploadedfile");
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }
	    File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());

	    //write file to server
	    try {
	        try (InputStream is = file.getInputStream();
	            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
	        	int i;
	            while ((i = is.read()) != -1) stream.write(i);
	            stream.flush();
	        }
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return serverFile;
	}

	/**
	 * Brand, Stamp Setting ImageUpload
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
  @RequestMapping(value="/ImageUpload", method=RequestMethod.POST)
  public Map<Object,Object> ImageUpload(MultipartHttpServletRequest request) throws IOException, InterruptedException {

	  Map<Object, Object> resultMap = Maps.newHashMap();
	  resultMap.put("success", false);

	  String id = String.valueOf(request.getParameter("id"));
	  String type = request.getParameter("type") == null ? "" : request.getParameter("type");
	  String gubun = String.valueOf(request.getParameter("gubun"));
	  String resultUrl = null;
	  SvcBrand brand = null;
	  SvcBrandSet brandSet = null;
	  SvcStoreSet storeSet = null;
	  boolean flag = false;

	  if (id != "null") {
		  if (!GenericValidator.isBlankOrNull(id) && GenericValidator.isInt(id)) {
			  if ("brand".equals(gubun)) {
				  brand = svcBrandMapper.selectByPrimaryKey(StrUtils.parseLong(id));

				  if (brand != null) id = String.valueOf(brand.getId());
				  else flag = true;
			  }
			  else if ("brandSet".equals(gubun)) {
				  brandSet = svcBrandSetMapper.selectByPrimaryKey(StrUtils.parseLong(id));

				  if (brandSet != null) id = String.valueOf(brandSet.getId());
				  else flag = true;
			  }
			  else if ("storeSet".equals(gubun)) {
				  storeSet = svcStoreSetMapper.selectByPrimaryKey(StrUtils.parseLong(id));

				  if (storeSet != null) id = String.valueOf(storeSet.getId());
				  else flag = true;
			  }
		  }
	  }

	  if (flag) {
		  id = "temps";
		  resultMap.put("temp", true);
	  }

	  String files = "_file_"+gubun+"_"+type;

	  if (request.getFile(files) != null) {
		  String file_path = Globals.IMG_RESOURCE+"/store/"+gubun+"/";
		  String result_path = "/image-resource/store/"+gubun+"/";
		  long timestamp = Calendar.getInstance().getTimeInMillis();

		  MultipartFile file = request.getFile("_file_"+gubun+"_"+type);

		  String file_name = file.getOriginalFilename();
		  //String file_type = file.getContentType().substring(file.getContentType().lastIndexOf("/")+1, file.getContentType().length());

		  byte[] bytes = file.getBytes();
		  File Folder = new File(file_path+id);
		  File lOutFile = new File(file_path+id+"/"+timestamp+"_"+file_name);

		  if (!Folder.isDirectory()) Folder.mkdirs();

		  if (lOutFile.isFile()) {
			  if (lOutFile.delete()) {
				  FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
				  lFileOutputStream.write(bytes);
				  lFileOutputStream.close();
				  resultUrl = result_path+id+"/"+timestamp+"_"+file_name;
			  }
		  }
		  else {
			  FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
			  lFileOutputStream.write(bytes);
			  lFileOutputStream.close();
			  resultUrl = result_path+id+"/"+timestamp+"_"+file_name;
		  }

		  if ("brand".equals(gubun)) {
			  if (brand != null) {
				  SvcBrand _brand = new SvcBrand();
				  _brand.setId(brand.getId());
				  _brand.setLogoImg(resultUrl);
				  svcBrandMapper.updateByPrimaryKeySelective(_brand);
			  }
		  }
		  else if ("brandSet".equals(gubun)) {
			  if (brandSet != null) {
				  SvcBrandSetWithBLOBs _brandSet = new SvcBrandSetWithBLOBs();
				  _brandSet.setId(brandSet.getId());
				  if ("stampBg".equals(type)) _brandSet.setStampBg(resultUrl);
				  else _brandSet.setStampIcon(resultUrl);

				  svcBrandSetMapper.updateByPrimaryKeySelective(_brandSet);
			  }
		  }
		  else if ("storeSet".equals(gubun)) {
			  if (storeSet != null) {
				  SvcStoreSetWithBLOBs _storeSet = new SvcStoreSetWithBLOBs();
				  _storeSet.setId(storeSet.getId());
				  if ("stampBg".equals(type)) _storeSet.setStampBg(resultUrl);
				  else _storeSet.setStampIcon(resultUrl);

				  svcStoreSetMapper.updateByPrimaryKeySelective(_storeSet);
			  }
		  }

		  if (System.getProperty("os.name").contains("Linux")) {
			  String chmod = "chmod 777 "+file_path+id+"/"+timestamp+"_"+file_name;
			  try {
				  Process p = Runtime.getRuntime().exec(chmod);
				  BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				  String line = null;

				  while((line = br.readLine()) != null){
					  logger.info("commend line : "+line);
				  }
			  }
			  catch (Exception e) {
				  logger.error(e.getMessage());
			  }
		  }
		  resultMap.put("success", true);
		  resultMap.put("url", resultUrl);
	  }
	  else {}

	  return resultMap;
  }

  /**
   * 러시아어로는 변환이 안됨(영어로 받아옴)
   * @param timezoneId 매장 타임존 아이디(없을 경우 브라우저 타임존 아이디)
   * @param language 타임존 나라 표시 언어 값
   * @return List(타임존 아이디, 타임존 나라, 타임존 시간)
   * */
  private List<LinkedHashMap<String, Object>> getCountryTimezone(String timezoneId, String language) {
    
    /* 서버 Locale 저장  */
    Locale systemLocale = Locale.getDefault();
    /* 타임존 나라 명을 클라이언트 언어설정 값에 맞게  변환  */
    Locale clientLocale = getLocale(language);
    /* 서버 Locale을 잠시 Client Locale로 변경  */
    Locale.setDefault(clientLocale);
    logger.debug("client language : " + language + ", systemLocale : " + systemLocale + ", clientLocale : " + clientLocale);
    List<LinkedHashMap<String, Object>> datas = Lists.newArrayList();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    String[] ids = TimeZone.getAvailableIDs();
    
    if (GenericValidator.isBlankOrNull(timezoneId)) {
      timezoneId = TimeZone.getDefault().getID();
    }
    
    TimeZone time;
    for (String id : ids) {
      LinkedHashMap<String, Object> data = Maps.newLinkedHashMap();
      if (id.equals(timezoneId)) {
        data.put("selected",  true);
      } else {
        data.put("selected",  false);
      }
      
      time = TimeZone.getTimeZone(id);
      sdf.setTimeZone(time);
      data.put("countryTm", sdf.format(date));
      data.put("id", id);
      data.put("country", time.getDisplayName());
      
      datas.add(data);
    }
    /* 서버 Locale 설정 */
    Locale.setDefault(systemLocale);
    
    /* list 오름차순 정렬 (기준 : 타임존 나라명) */
    Collections.sort(datas, new Comparator<LinkedHashMap<String, Object>>() {
      @Override
      public int compare(LinkedHashMap<String, Object> first, LinkedHashMap<String, Object> second) {
        int firstValue = (int) first.get("country").toString().charAt(0);
        int secondValue = (int) second.get("country").toString().charAt(0);
        
        if (firstValue > secondValue) {
            return 1;         // 오름차순 정렬 1, 내림차순 -1
        } else if (firstValue < secondValue) {
            return -1;        // 오름차순 정렬 -1, 내림차순 1
        } else /* if (firstValue == secondValue) */ {
            return 0;
        }
      }
      
    });
    
    return datas;
  }
  
  /**
   * string language로 Locale 반환
   * @param language client 언어 설정 값 ('en', 'ru', 'ko')
   * @return 언어에 맞는 Locale 반환
   * */
  private Locale getLocale(String language) {
    Locale locale;
    if ("en".equals(language)) {
      locale = Locale.US;
    } else if ("ru".equals(language)){
      locale = new Locale("ru", "RU");
    } else if ("ko".equals(language)) {
      locale = Locale.KOREA;
    } else {
      locale = Locale.US;
    }
    return locale;
  }
  
  /**
   * 파라미터 유효 데이터 체크
   * @param params
   */
  private void checkParams(Map<String, String> params) {
    // fran, brand 관리자로 첫 화면 볼 때는 해당 데이터들이 ""로 받아와짐.
    clearInvalidParam(params, "franId");
    clearInvalidParam(params, "brandId");
    clearInvalidParam(params, "storeId");
  }
  
  /**
   * 키는 존재하지만 키에 해당하는 값이 없을 때 해당 키 제거
   * @param params
   * @param key
   */
  private void clearInvalidParam(Map<String, String> params, String key) {
    if (params.get(key) != null) {
      if (!GenericValidator.isBlankOrNull(params.get(key).toString())) {
        // skip
      } else {
        params.remove(key);
      }
    } else {
      params.remove(key);
    }
  }
  
	  /*@RequestMapping("/Excel")
	  public ModelAndView excel(
		  @RequestParam(required=false) SvcBeacon beacon,
		  ModelMap model,
		  //@RequestParam(required=false) Map<String,Object> model,
		  @RequestParam(required=false) Map<Object,Object> params,
		  @RequestParam(required=false, value="start", defaultValue="0") int start,
		  @RequestParam(required=false, value="length", defaultValue="10") int length,
		  HttpMethod method) throws InvalidJsonException {

		List<LinkedHashMap<String,Object>> list = customMapper.getBeaconList(params, new RowBounds(start, length));

		String[] titles = {"회사구분", "회사명", "담당자명", "연락처", "팩스", "이메일", "기타"};

		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		String day = date.format(today);
		ExcelGenerateVO vo = new ExcelGenerateVO("목록_"+day, titles, list);

		model.put("excel", vo);

		return new ModelAndView(new ExcelGenerateView(), model );
	  }*/

}
