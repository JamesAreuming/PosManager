/*
 * Filename	: AdminInventoryController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcBeaconExample;
import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemExample;
import com.jc.pico.bean.SvcStockAdjust;
import com.jc.pico.bean.SvcStockImport;
import com.jc.pico.bean.SvcStockImportExample;
import com.jc.pico.bean.SvcSupplyCompany;
import com.jc.pico.bean.SvcSupplyCompanyExample;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.mapper.BaseBcodeMapper;
import com.jc.pico.mapper.BaseMcodeMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcBrandSetMapper;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcFranchiseMapper;
import com.jc.pico.mapper.SvcItemMapper;
import com.jc.pico.mapper.SvcStampMapper;
import com.jc.pico.mapper.SvcStockAdjustMapper;
import com.jc.pico.mapper.SvcStockImportMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcSupplyCompanyMapper;
import com.jc.pico.mapper.SvcTermsMapper;
import com.jc.pico.mapper.UserBackofficeMenuMapper;
import com.jc.pico.mapper.UserGroupAuthMapper;
import com.jc.pico.mapper.UserGroupMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.customMapper.admin.CustomInventoryMapper;
import com.jc.pico.utils.mybatis.typehandler.MobileSecurityTypeHandler;

import au.com.bytecode.opencsv.CSVReader;

@Controller
@ResponseBody
@RequestMapping("/admin/model/inventory")
public class AdminInventoryController {

  protected static Logger logger = LoggerFactory.getLogger(AdminInventoryController.class);

  @Autowired
  SvcFranchiseMapper svcFranchiseMapper;

  //  @Autowired
  //  CorpManageMapper corpManageMapper;
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
  BaseMcodeMapper baseCodeMainMapper;

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
  CustomInventoryMapper customInventoryMapper; 

  @Autowired
  CodeUtil codeUtil;
  
  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Autowired
  private HttpSession session;


  /**
   * Inventory - 재고현황
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/StockStatus")
  public Map<Object,Object> StockStatus(
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException, ParseException {
	//Param Log.
	  logger.debug("StockImport : " + JsonConvert.toJson(params));
	  
	  Map<Object,Object> result = new HashMap<>();
	  
	  switch(method){
	    case GET :
	    	List<LinkedHashMap<String, Object>> stockStatusList = Lists.newArrayList();
	    	if(!String.valueOf(params.get("draw")).equals("null")){
	        checkParams(params);
          if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
  	    		if(params.get("order[0][column]") != null){
                      String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                                      +" "+String.valueOf(params.get("order[0][dir]"));
                      params.put("orderby",orderby);
  	    		}
  	    		/****************** 서비스를 호출하기 위한 로직 시작 ******************/
  	    		int count = customInventoryMapper.getStockSatusListCount(params);
  	    		/****************** 서비스를 호출하기 위한 로직 종료 ******************/  
        
  	    		result.put("recordsTotal", count); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
  	    		result.put("recordsFiltered", count); // 총 갯수, 해당 변수로 page row count
        
  	    		if (count > 0) {
  	    			/****************** 서비스를 호출하기 위한 로직 시작 ******************/	
  	    			stockStatusList = customInventoryMapper.getStockSatusList(params);
  	    			/****************** 서비스를 호출하기 위한 로직 종료 ******************/
  	    		}
          } else {
            result.put("recordsTotal", 0); 
            result.put("recordsFiltered", 0);
          }
	    	} else {
      
	    	}
	    	logger.debug("data : " + JsonConvert.toJson(stockStatusList ));
	    	result.put("success", true);
	    	result.put("list",  stockStatusList);
	    	result.put("errMsg", "");  	
	  		
	    	break;
  	
    case POST :
  	  result.put("success", true);
  	  result.put("errMsg", "");
    	break;

    default:
  	  result.put("success", true);
  	  result.put("errMsg", "");    	  
    	break;
    
    }

    return result;
  }
  
  /**
   * Inventory - 실사재고등록
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/ActualStock")
  public Map<Object,Object> ActualStock(
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {
    Map<Object,Object> result = new HashMap<>();
    SvcStockAdjustMapper adjustMapper = sessionTemplate.getMapper(SvcStockAdjustMapper.class);
    SvcStockImportMapper mapper = sessionTemplate.getMapper(SvcStockImportMapper.class);
    SvcStockImportExample example = new SvcStockImportExample();
    
    try {
      logger.debug("method : " + method);
      logger.debug("actualStocks : " + JsonConvert.toJson(params));
      switch(method){
      case GET :  { // 실사 재고 목록 반환 
        List<LinkedHashMap<String, Object>> actualStocks = Lists.newArrayList();
        if(!String.valueOf(params.get("draw")).equals("null")){     // datatables 일 때
          
          checkParams(params);
          if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
            int cnt = customInventoryMapper.getCountActualStockList(params);
            
            result.put("recordsTotal", cnt); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
            result.put("recordsFiltered", cnt); // 총 갯수, 해당 변수로 page row count
            
            if (cnt > 0) {
              if (params.get("order[0][column]") != null){
                String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                    +" "+String.valueOf(params.get("order[0][dir]"));
                params.put("orderby",orderby);
              }
              
              actualStocks = customInventoryMapper.getActualStockList(params);
            } 
          } else {
            result.put("recordsTotal",  0);
            result.put("recordsFiltered",  0);
          }
        } 
        result.put("success", true);
        result.put("list",  actualStocks);
        result.put("errMsg", "");
      }
        break;
      case POST : {   // 실사 재고 업데이트 및, 실사 재고 히스토리 등록
        Map<Object, Object> acData = JsonConvert.JsonConvertObject(params.get("data").toString(),  new TypeReference<Map<Object, Object>>() {});
        List<Map<Object, Object>> checkArray = JsonConvert.JsonConvertObject(JsonConvert.toJson(acData.get("checkArray")),  new TypeReference<List<Map<Object, Object>>>() {});
        
        // 실사 재고 (업데이트, 히스토리 등록)
        for (Map<Object, Object> data : checkArray) {
          example = new SvcStockImportExample();
          example.createCriteria().andIdEqualTo(((Integer)data.get("id")).longValue());
          List<SvcStockImport> stockImports = mapper.selectByExample(example);
          
          /* 실사 재고 리스트 가져온 후 업데이트 */
          if (stockImports != null && stockImports.size() > 0) {
            SvcStockImport stockImport = stockImports.get(0);
            stockImport.setCurrentCnt(Integer.parseInt(String.valueOf(data.get("realCnt")).replaceAll(",",  "")));
            mapper.updateByExampleSelective(stockImport, example);
            /* 업데이트 끝 */
            
            /* 실사 재고 히스토리 등록 */
            logger.debug("stockImport : " + JsonConvert.toJson(stockImport));
            SvcStockAdjust stockAdjust = new SvcStockAdjust();
            stockAdjust.setBrandId(stockImport.getBrandId());
            stockAdjust.setStoreId(stockImport.getStoreId());
            stockAdjust.setItemId(((Integer)data.get("itemId")).longValue());
            stockAdjust.setSafeCnt(Integer.parseInt(String.valueOf(data.get("safeStockCnt")).replaceAll(",",  "")));
            stockAdjust.setCurrentCnt(Integer.parseInt(String.valueOf(data.get("currentCnt")).replaceAll(",",  "")));
            stockAdjust.setActualCnt(Integer.parseInt(String.valueOf(data.get("realCnt")).replaceAll(",",  "")));
            stockAdjust.setGapCnt(Integer.parseInt(String.valueOf(data.get("gap")).replaceAll(",",  "")));
            adjustMapper.insertSelective(stockAdjust);
            /* 등록 끝 */
          }
        }
        
        result.put("success", true);
        result.put("errMsg", "");
        }
        break;
      case PUT : {
          result.put("success", true);
          result.put("errMsg", "");
      }
          break;
      default : {
        result.put("success", false);
        result.put("errMsg", "not supported method.");
        }
      }
  
      return result;
    } catch (Exception e) {
      logger.error("[{}][{}]", "/ActualStock", "param : " + JsonConvert.toJson(params));
      e.printStackTrace();
      throw e;
    }
  }
  
  /**
   * Inventory - 입고등록
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/StockImportReg")
  public Map<Object,Object> StockImportReg(
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException, ParseException {
    Map<Object,Object> result = new HashMap<>();

    switch(method){
    case GET :
      result.put("success", true);
      result.put("errMsg", "");
      break;
    case POST :
    	result.put("success", true);
        result.put("errMsg", "");
        break;
    case PUT :
    	result.put("success", true);
        result.put("errMsg", "");
        break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  /**
   * Inventory - 입고조회
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/StockImport")
  public Map<Object,Object> StockImport(
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException, ParseException {
    Map<Object,Object> result = new HashMap<>();

    try {
	    switch(method){
		    case GET :
		    	logger.debug("StockImport : " + JsonConvert.toJson(params));
		        List<LinkedHashMap<String, Object>> stockImportList = Lists.newArrayList();
		        if(!String.valueOf(params.get("draw")).equals("null")){	          
		          checkParams(params);
		        } else if (!GenericValidator.isBlankOrNull((String)params.get("getTotalPrice"))) {
		          checkParams(params);
		          params.remove("orderby");
		        }
		        
		        if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
              int count = customInventoryMapper.getStockImportListCount(params);            
              result.put("recordsTotal", count); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
              result.put("recordsFiltered", count); // 총 갯수, 해당 변수로 page row count
              
              if (count > 0) {
                stockImportList = customInventoryMapper.getStockImportList(params);
              }
            } else {
              result.put("recordsTotal", 0); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
              result.put("recordsFiltered", 0); // 총 갯수, 해당 변수로 page row count
            }
		        //logger.debug("data : " + JsonConvert.toJson(stockImportList));
		        result.put("success", true);
		        result.put("list",  stockImportList);
		        result.put("errMsg", "");
		        logger.debug("result : " + JsonConvert.toJson(result));
		        break;
		    case POST :
		    	result.put("success", true);
		        result.put("errMsg", "");
		        break;
		    case PUT :
		    	result.put("success", true);
		        result.put("errMsg", "");
		        break;
		    default :
		      result.put("success", false);
		      result.put("errMsg", "not supported method.");
    	}
    } catch (Exception e) {
        logger.error("[{}][{}]", "/StockImport", "param : " + JsonConvert.toJson(params));
        e.printStackTrace();
        throw e;
	}

    return result;
  }
  
  /**
   * Inventory - 입고 CSV 업로드
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
	@RequestMapping(value = "/StockImportCSV", method = RequestMethod.POST)
	public Map<Object,Object> StockImportCSV(MultipartHttpServletRequest req, HttpServletRequest request) throws IOException {
		Map<Object,Object> result = new HashMap<>();

		File serverFile = csvFile(req, request);
	    String[] nextLine;
      //read file
      //CSVReader(fileReader, ';', '\'', 1) means
      //using separator ; and using single quote ' . Skip first line when read
  	//BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(serverFile), "MS949"));
      try (FileReader fileReader = new FileReader(serverFile);
          CSVReader reader = new CSVReader(fileReader, ';', '\'', 1);) {

      	SvcItemMapper itemMapper = sessionTemplate.getMapper(SvcItemMapper.class);
      	SvcSupplyCompanyMapper scMapper = sessionTemplate.getMapper(SvcSupplyCompanyMapper.class);
      	List<SvcStockImport> stockImportList = new ArrayList<SvcStockImport>();
      	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      	
      	long franId = Long.parseLong((String) req.getParameter("franId"));
      	long brandId = Long.parseLong((String) req.getParameter("brandId"));
      	long storeId = Long.parseLong((String) req.getParameter("storeId"));
      	boolean flag = true;

      	while ((nextLine = reader.readNext()) != null) {
      		SvcStockImport stockImport = new SvcStockImport();

        for (int i = 0; i < nextLine.length; i++) {
          try {
            String[] val = nextLine[i].split(",", -1);
            String stockDtStr = val[0];
            String itemCd = val[1]; // 상품 코드
            String bizNo = val[2]; // 사업자 번호
            // item id 추출 > item cd + store id로 
            // supply company id 추출 > biz no+ store id 로
            // item id, supply compnay 둘 중 하나라도 없으면 continue;
            String unitPriceStr = val[3];
            String stockCntStr = val[4];
            Long itemId = null;
            Long supplyId = null;
            
            SvcItemExample itemExample = new SvcItemExample();
            SvcSupplyCompanyExample scExample = new SvcSupplyCompanyExample();
            
            itemExample.createCriteria()
                .andBrandIdEqualTo(brandId)
                .andStoreIdEqualTo(storeId)
                .andItemCdEqualTo(itemCd);
            List<SvcItem> items = itemMapper.selectByExample(itemExample);
            
            if (items != null && items.size() > 0) {
              itemId = items.get(0).getId();
            } else {
              continue;
            }
            
            scExample = new SvcSupplyCompanyExample();
            scExample.createCriteria()
                .andBrandIdEqualTo(brandId)
                .andStoreIdEqualTo(storeId)
                .andCompanyCdEqualTo(bizNo);
            
            List<SvcSupplyCompany> supplys = scMapper.selectByExample(scExample);
             
            if (supplys != null && supplys.size() > 0) {
              supplyId = supplys.get(0).getId();
            } else {
              continue;
            }

            stockImport.setBrandId(brandId);
            stockImport.setStoreId(storeId);
            stockImport.setStockDt((Date) dateFormat.parse(stockDtStr));
            stockImport.setItemId(itemId);
            stockImport.setSupplyId(supplyId);
            stockImport.setUnitPrice(Double.parseDouble(unitPriceStr));
            stockImport.setStockCnt(Integer.parseInt(stockCntStr));
            stockImport.setCurrentCnt(stockImport.getStockCnt());
            stockImport.setTotalPrice(stockImport.getUnitPrice() * stockImport.getStockCnt());

            logger.debug("data : " + JsonConvert.toJson(stockImport));
            stockImportList.add(stockImport);
          } catch (Exception ex) {
            ex.printStackTrace();
            flag = false;
          }
        }
        if (!flag) {
          break;
        }
      }

      		if (flag) {
      		 if (stockImportList != null && stockImportList.size() > 0) {
  	        	Map<String, Object> map = new HashMap<>();
  	        	map.put("list", stockImportList);
  	        	customInventoryMapper.insertStockImportCSV(map);
      		 } else {
      		   flag = false;
      		 }
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
   * Inventory - 출고조회
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/StockExport")
  public Map<Object,Object> StockExport(
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException, ParseException {

	  //Param Log.
	  logger.debug("StockImport : " + JsonConvert.toJson(params));
	  
	  Map<Object,Object> result = new HashMap<>();
	  
	  switch(method){
	    case GET :
	    	List<LinkedHashMap<String, Object>> stockExportList = Lists.newArrayList();
	    	if(!String.valueOf(params.get("draw")).equals("null")){
	    	  
	    	   checkParams(params);
	    	  if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
  	    		if(params.get("order[0][column]") != null){
                      String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                                      +" "+String.valueOf(params.get("order[0][dir]"));
                      params.put("orderby",orderby);
                 }
  	    		/****************** 서비스를 호출하기 위한 로직 시작 ******************/
  	    		int count = customInventoryMapper.getStockExportListCount(params);
  	    		/****************** 서비스를 호출하기 위한 로직 종료 ******************/  
        
  	    		result.put("recordsTotal", count); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
  	    		result.put("recordsFiltered", count); // 총 갯수, 해당 변수로 page row count
        
  	    		if (count > 0) {
  	    			/****************** 서비스를 호출하기 위한 로직 시작 ******************/	
  	    			stockExportList = customInventoryMapper.getStockExportList(params);
  	    			/****************** 서비스를 호출하기 위한 로직 종료 ******************/
  	    		}
	    	  } else {
            result.put("recordsTotal", 0); 
            result.put("recordsFiltered", 0);
	    	  }
	    	} else {
      
	    	}
	    	logger.debug("data : " + JsonConvert.toJson(stockExportList ));
	    	result.put("success", true);
	    	result.put("list",  stockExportList);
	    	result.put("errMsg", "");  	
	  		
	    	break;
  	
    case POST :
  	  result.put("success", true);
  	  result.put("errMsg", "");
    	break;

    default:
  	  result.put("success", true);
  	  result.put("errMsg", "");    	  
    	break;
    
    }

    return result;
  }
  
  /**
   * Inventory - 재고조정조회
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @RequestMapping("/StockAdjust")
  public Map<Object,Object> StockAdjust(
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException, ParseException {
    Map<Object,Object> result = new HashMap<>();

    try {
	    switch(method){
		    case GET :
		    	logger.debug("StockAdjust : " + JsonConvert.toJson(params));
		        List<LinkedHashMap<String, Object>> stockAdjustList = Lists.newArrayList();
		        if(!String.valueOf(params.get("draw")).equals("null")){	          
		          
		          checkParams(params);
		          if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
  		          int count = customInventoryMapper.getStockAdjustListCount(params);	          
  		          result.put("recordsTotal", count); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
  		          result.put("recordsFiltered", count); // 총 갯수, 해당 변수로 page row count
  		          
  		          /*
  		           * DATATABLE 컬럼 정렬값 세팅
  		           */
  		          if(params.get("order[0][column]") != null){
  		            String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
  		                +" "+String.valueOf(params.get("order[0][dir]"));
  		            params.put("orderby",orderby);
  		          }
  		          
  		          if (count > 0) {
  		        	  stockAdjustList = customInventoryMapper.getStockAdjustList(params);
  		          }
		          } else {
		            result.put("list",  "");
		            result.put("recordsTotal",  0);
		            result.put("recordsFiltered",  0);
		          }
		        } else {
		          
		        }
		        result.put("success", true);
		        result.put("list",  stockAdjustList);
		        result.put("errMsg", "");
		        
		        logger.debug("data : " + JsonConvert.toJson(result));
		        break;
		    case POST :
		    	result.put("success", true);
		        result.put("errMsg", "");
		        break;
		    case PUT :
		    	result.put("success", true);
		        result.put("errMsg", "");
		        break;
		    default :
		      result.put("success", false);
		      result.put("errMsg", "not supported method.");
	    }
	} catch (Exception e) {
        logger.error("[{}][{}]", "/StockAdjust", "param : " + JsonConvert.toJson(params));
        e.printStackTrace();
        throw e;
	}

	return result;
  }
  
  /**
   * Inventory - 거래처관리
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws ParseException
   */
  @Transactional
  @RequestMapping("/Supplier")
  public Map<Object,Object> Supplier(
       @RequestParam(required=false) Map<Object,Object> params
      ,HttpMethod method) throws InvalidJsonException, ParseException {
    Map<Object,Object> result = new HashMap<>();
    SvcSupplyCompanyMapper mapper = sessionTemplate.getMapper(SvcSupplyCompanyMapper.class);
    SvcSupplyCompanyExample example = new SvcSupplyCompanyExample();
    
    try {
      logger.debug("method : " + method);
      logger.debug("supplier : " + JsonConvert.toJson(params));
      switch(method){
      case GET :  { // 공급사 목록 반환 
        List<LinkedHashMap<String, Object>> suppliers = Lists.newArrayList();
        if(!String.valueOf(params.get("draw")).equals("null")){     // datatables 일 때
          
          checkParams(params);
          if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
            int cnt = customInventoryMapper.getCountSupplyCompanyList(params);
            
            result.put("recordsTotal", cnt); // 총 갯수, 해당 변수로 데이터를 주면 pagination 구현
            result.put("recordsFiltered", cnt); // 총 갯수, 해당 변수로 page row count
  
            if (cnt > 0) {
              if (params.get("order[0][column]") != null){
                String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                    +" "+String.valueOf(params.get("order[0][dir]"));
                params.put("orderby",orderby);
              }
              
              suppliers = customInventoryMapper.getSupplyCompanyList(params);
            }
          } else {
            result.put("list",  "");
            result.put("recordsTotal", 0);
            result.put("recordsFiltered", 0);
          }
        } else {    // detail 일 때
          Map<Object, Object> scData = JsonConvert.JsonConvertObject(params.get("data").toString(),  new TypeReference<Map<Object, Object>>() {});
          if (scData.get("id") != null) {
            suppliers = customInventoryMapper.getSupplyCompanyList(scData);
            if (suppliers.size() == 1) {
              result.put("data", suppliers.get(0));
            }
          }
        }
        result.put("success", true);
        result.put("list",  suppliers);
        result.put("errMsg", "");
        }
        break;
      case POST : { // 공급사 등록
        Map<Object, Object> scData = JsonConvert.JsonConvertObject(params.get("data").toString(),  new TypeReference<Map<Object, Object>>() {});
        SvcSupplyCompany sc = getSvcSupplyCompany(scData);
        
        mapper.insertSelective(sc);
    	  result.put("success", true);
        result.put("errMsg", "");
        }
        break;
      case PUT : {
        Map<Object, Object> scData = JsonConvert.JsonConvertObject(params.get("data").toString(),  new TypeReference<Map<Object, Object>>() {});
        SvcSupplyCompany sc = getSvcSupplyCompany(scData);
        example.createCriteria().andIdEqualTo(Long.parseLong((String)scData.get("id")));
        mapper.updateByExampleSelective(sc, example);
      	result.put("success", true);
          result.put("errMsg", "");
        }
          break;
      default : {
        result.put("success", false);
        result.put("errMsg", "not supported method.");
        }
      }
  
      return result;
    } catch (Exception e) {
      logger.error("[{}][{}]", "/Supplier", "param : " + JsonConvert.toJson(params));
      e.printStackTrace();
      throw e;
    }
  }
  
  private SvcSupplyCompany getSvcSupplyCompany(Map<Object, Object> scData) {
    MobileSecurityTypeHandler ms = new MobileSecurityTypeHandler();
    SvcSupplyCompany sc = new SvcSupplyCompany();
    if (scData.get("franId") != null) {
      Object data = scData.get("franId");
      if (data instanceof String) {
        sc.setFranId(Long.parseLong((String) data));
      } else if (data instanceof Integer) {
        sc.setFranId(((Integer) data).longValue());
      }
    }
    
    if (scData.get("brandId") != null) {
      Object data = scData.get("brandId");
      if (data instanceof String) {
        sc.setFranId(Long.parseLong((String) data));
      } else if (data instanceof Integer) {
        sc.setFranId(((Integer) data).longValue());
      }
    }
    
    if (scData.get("storeId") != null) {
      Object data = scData.get("storeId");
      if (data instanceof String) {
        sc.setFranId(Long.parseLong((String) data));
      } else if (data instanceof Integer) {
        sc.setFranId(((Integer) data).longValue());
      }
    }
    sc.setBizNo(scData.get("bizNo") != null ? (String)scData.get("bizNo") : "");
    sc.setCompanyCd(scData.get("companyCd") != null ? (String)scData.get("companyCd")  : "");
    sc.setCompanyNm(scData.get("companyNm") != null ? (String)scData.get("companyNm") : "");
    sc.setSvcSt(codeUtil.getBaseCodeByAlias("svc-st-normal"));
    sc.setCeoNm(scData.get("ceoNm") != null ? (String)scData.get("ceoNm") : "");
    sc.setEmail(scData.get("email") != null ? (String)scData.get("email") : "");
    sc.setMb(scData.get("mb") != null ? ms.textEncryptor.encrypt((String)scData.get("mb")) : "");
    sc.setTel(scData.get("tel") != null ? (String)scData.get("tel") : "");
    sc.setFax(scData.get("fax") != null ? (String)scData.get("fax") : "");
    sc.setAddr1(scData.get("addr1") != null ? (String)scData.get("addr1") : "");
    sc.setAddr2(scData.get("addr2") != null ? (String)scData.get("addr2") : "");
    sc.setZip("");                        // 추후 필요할 때 작업
    sc.setMbCountryCd("");    // 추후 필요할 때 작업
    sc.setFaxCountryCd("");   // 추후 필요할 때 작업
    sc.setTelCountryCd("");   // 추후 필요할 때 작업
    return sc;
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

  /**
   * 파라미터 유효 데이터 체크
   * @param params
   */
  private void checkParams(Map<Object, Object> params) {
    // fran, brand 관리자로 첫 화면 볼 때는 해당 데이터들이 ""로 받아와짐.
    clearInvalidParam(params, "franId");
    clearInvalidParam(params, "brandId");
    clearInvalidParam(params, "storeId");
    clearInvalidParam(params, "searchKeyword");
    clearInvalidParam(params, "itemCat");
  }
  
  /**
   * 키는 존재하지만 키에 해당하는 값이 없을 때 해당 키 제거
   * @param params
   * @param key
   */
  private void clearInvalidParam(Map<Object, Object> params, String key) {
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
}
