/*
* Filename	: AdminOrderController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcFranchise;
import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemCat;
import com.jc.pico.bean.SvcItemCatExample;
import com.jc.pico.bean.SvcItemExample;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcItemImgExample;
import com.jc.pico.bean.SvcItemOpt;
import com.jc.pico.bean.SvcItemOptDtl;
import com.jc.pico.bean.SvcItemOptDtlExample;
import com.jc.pico.bean.SvcItemOptExample;
import com.jc.pico.bean.SvcKitchenMessage;
import com.jc.pico.bean.SvcKitchenMessageExample;
import com.jc.pico.bean.SvcPluCat;
import com.jc.pico.bean.SvcPluCatExample;
import com.jc.pico.bean.SvcPluItem;
import com.jc.pico.bean.SvcPluItemExample;
import com.jc.pico.bean.SvcTable;
import com.jc.pico.bean.SvcTableExample;
import com.jc.pico.bean.SvcTableSection;
import com.jc.pico.bean.SvcTableSectionExample;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.mapper.SvcFranchiseMapper;
import com.jc.pico.mapper.SvcItemCatMapper;
import com.jc.pico.mapper.SvcItemImgMapper;
import com.jc.pico.mapper.SvcItemMapper;
import com.jc.pico.mapper.SvcItemOptDtlMapper;
import com.jc.pico.mapper.SvcItemOptMapper;
import com.jc.pico.mapper.SvcKitchenMessageMapper;
import com.jc.pico.mapper.SvcPluCatMapper;
import com.jc.pico.mapper.SvcPluItemMapper;
import com.jc.pico.mapper.SvcTableMapper;
import com.jc.pico.mapper.SvcTableSectionMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.RandomUtil;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.customMapper.admin.CustomItemsMapper;

@Controller
@ResponseBody
@RequestMapping("/admin/order")
public class AdminOrdersController {

  protected static Logger logger = LoggerFactory.getLogger(AdminItemsController.class);
  
  @Autowired
  SvcFranchiseMapper svcFranchiseMapper;

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Autowired
  SvcItemCatMapper svcItemCatMapper;

  @Autowired
  SvcItemMapper svcItemMapper;

  @Autowired
  SvcItemImgMapper svcItemImgMapper;

  @Autowired
  SvcPluCatMapper svcPluCatMapper;

  @Autowired
  SvcPluItemMapper svcPluItemMapper;

  @Autowired
  SvcItemOptMapper svcItemOptMapper;

  @Autowired
  SvcItemOptDtlMapper svcItemOptDtlMapper;

  @Autowired
  SvcTableSectionMapper svcTableSectionMapper;

  @Autowired
  SvcTableMapper svcTableMapper;

  @Autowired
  SvcKitchenMessageMapper svcKitchenMessageMapper;


  /**
   * Items - Category
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/Category")
  public Map<Object,Object> Category(
		  @RequestParam(required=false) SvcItemCat itemCat,
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws InvalidJsonException {

	    Map<Object,Object> result = new HashMap<>();

	    if(itemCat == null && params.get("data") != null){
	    	itemCat = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcItemCat.class);
	    }

	    CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);
	    SvcItemCatExample svcItemCatExample = new SvcItemCatExample();

	    switch(method){
	    case GET :
		    	if(itemCat.getBrandId() == null){
	    			itemCat.setBrandId((long) 0);
	    		}

	    	  result.put("list", mapper.getItemCategory(itemCat));
	    	  result.put("success", true);

	      break;
	    case POST :
	    	
	    	SvcItemCatExample.Criteria svcItemCatExampleCriteria = svcItemCatExample.createCriteria();
	    	
	    	svcItemCatExampleCriteria.andBrandIdEqualTo(itemCat.getBrandId());	// 브랜드 ID
	    	svcItemCatExampleCriteria.andStoreIdEqualTo(itemCat.getStoreId());	// 매장 ID
	    	svcItemCatExampleCriteria.andParentEqualTo(itemCat.getParent());	// 부모카테고리 ID
	    	svcItemCatExampleCriteria.andCatCdEqualTo(itemCat.getCatCd());		// 카테고리 코드
	    	
	        if (svcItemCatMapper.countByExample(svcItemCatExample) == 0) {
	        	result.put("success", svcItemCatMapper.insertSelective(itemCat) == 1 ? true : false );
	   	      	result.put("errMsg", "");
	        }else{
	        	result.put("success", false );
	   	      	result.put("errMsg", "이미 등록된 카테고리 코드입니다.");
	        }
	     
	      break;
	    case PUT :

	      result.put("success", svcItemCatMapper.updateByPrimaryKeySelective(itemCat) == 1 ? true : false );
	      result.put("errMsg", "");
	      break;
	    case DELETE :
        	  svcItemCatExample.createCriteria().andParentEqualTo(itemCat.getId());

        	  boolean exists = false;

        	  //1.하위카테고리 존재여부 체크
        	  int existsCount = svcItemCatMapper.countByExample(svcItemCatExample);
        	  if(existsCount > 0){
        		  exists = true;
        		  result.put("errMsg", "하위 카테고리가 존재합니다.");
        	  }else{
        		  //2.해당 카테고리의 상품 존재 여부
        		  SvcItemExample svcItemExample = new SvcItemExample();
	        	  svcItemExample.createCriteria().andCatIdEqualTo(itemCat.getId());

        		  existsCount = svcItemMapper.countByExample(svcItemExample);
        		  if(existsCount > 0){
        			  exists = true;
        			  result.put("errMsg", "카테고리 상품이 존재합니다.");
        		  }
        	  }

          	  //3.하위 카테고리 및 등록상품이 없으면 삭제 실행
        	  if (!exists) {
        		  result.put("success", svcItemCatMapper.deleteByPrimaryKey(itemCat.getId()) == 1 ? true : false );
        		  result.put("errMsg", "");
        	  }

            break;
	    default :
	      result.put("success", false);
	      result.put("errMsg", "not supported method.");
	    }

    return result;
  }

  /**
   * 셀렉트박스용 쿼리
   * @param itemCat
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/CateSelect")
  public Map<Object,Object> CateSelect(
		  @RequestParam(required=false) SvcItemCat itemCat,
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws InvalidJsonException {

	    Map<Object,Object> result = new HashMap<>();

	    if(itemCat == null && params.get("data") != null){
	    	itemCat = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcItemCat.class);
	    }

	    CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);

	    switch(method){
	    case GET :
	    	  result.put("list", mapper.getItemCateSelect(itemCat));
	    	  result.put("success", true);

	      break;
	    default :
	      result.put("success", false);
	      result.put("errMsg", "not supported method.");
	    }

    return result;
  }

  /**
   * Items - Items
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/Items")
  public Map<Object,Object> Items(
		  @RequestParam(required=false) SvcItem item,
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") String start,
	      @RequestParam(required=false, value="length", defaultValue="10") String length,
	      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

		//임시셋팅
	  	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	  	Date now = new Date();
	  	String tmpBcd = format.format(now);

	    Map<Object,Object> result = new HashMap<>();	// 결과데이터
	    Map<String,String> baseParam = new HashMap<>();	// 검색데이터
	    Map<String,String> userInfo = new HashMap<>();	// 로그인 사용자 정보
	    
	    CustomItemsMapper customItemsMapper = sessionTemplate.getMapper(CustomItemsMapper.class);

	    if(item == null && params.get("data") != null){
	    	item = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcItem.class);
	    }

	    if (params.get("data") != null) {
	    	baseParam = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
		}
	    
	    if (params.get("userInfo") != null) {
	    	userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(), new TypeReference<Map<String, String>>() {});
		}
	    
	    switch(method){
	    case GET :

	    	if(!String.valueOf(params.get("draw")).equals("null")){

	    		logger.debug("brandId  >>>> " + baseParam.get("brandId") );

	    		if(baseParam.get("brandId") != null){

    	          /*
    	           * DATATABLE 검색 값 세팅
    	           */
//	    			String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 

    	          /*
    	           * DATATABLE 컬럼 정렬값 세팅
    	           */
    	          if(params.get("order[0][column]") != null){
    	            String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
    	                +" "+String.valueOf(params.get("order[0][dir]"));
    	            baseParam.put("orderby",orderby);
    	          }
	    	          
    	          baseParam.put("start", start);
    	          baseParam.put("length", length);
    	          
    	          int itemCount = customItemsMapper.getItemCount(baseParam);
    	          
		            result.put("list", customItemsMapper.getItemInfo(baseParam));
		            result.put("recordsTotal", itemCount);
		            result.put("recordsFiltered", itemCount);
	    		}else{
	    			result.put("list", "");
	    			result.put("recordsTotal", 0);
		            result.put("recordsFiltered", 0);
	    		}

	          } else {
	        	  SvcItemImgExample svcItemImgWhere = new SvcItemImgExample();
	        	  SvcItemImgExample.Criteria criteria = svcItemImgWhere.createCriteria();
	        	  criteria.andItemIdEqualTo(item.getId());

	              result.put("list", svcItemMapper.selectByPrimaryKey(item.getId()));
	              result.put("itemImg", svcItemImgMapper.selectByExample(svcItemImgWhere));
	              logger.debug("item : " + JsonConvert.toJson(result));
	          }

	    	result.put("success", true);
	      break;
	    case POST :
	    	//임시부여 : ItemCd, Barcode
			item.setItemCd(tmpBcd);
			item.setBarcode(RandomUtil.getBarcode());

			result.put("success", svcItemMapper.insertSelective(item) == 1 ? true : false );
			result.put("itemId", item.getId());
			result.put("errMsg", "");
	      break;
	    case PUT :
	    	//ItemCd 없을경우 임시 부여
	    	if("".equals(item.getItemCd()) || item.getItemCd() == null){
	    		item.setItemCd(tmpBcd);
	    	}

	    	//Barcode 없을경우
	    	if("".equals(item.getBarcode()) || item.getBarcode() == null){
	    		item.setBarcode(RandomUtil.getBarcode());
	    	}
	    	logger.debug("★★★★★★★★★★★★★");
	    	logger.debug(params.toString());
	    	logger.debug("★★★★★★★★★★★★★");
	    	result.put("success", svcItemMapper.updateByPrimaryKeySelective(item) == 1 ? true : false );
	    	result.put("itemId", item.getId());
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
   * Items image
   * @param request
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
  @RequestMapping(value = "/Items/Image", method = RequestMethod.POST)
  public Map<Object, Object> ItemImageUpload(
		  MultipartHttpServletRequest request,
		  HttpMethod method) throws IOException, UnsupportedEncodingException {

    Map<Object, Object> resultMap = Maps.newHashMap();
    try {
      resultMap.put("success", false);
  
      String id = null;
      String itemId = String.valueOf(request.getParameter("itemId"));
      String resultUrl = null;
      SvcItem svcItem = null;
      SvcItemImg svcItemImg = null;
  
      CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);
  
      if (itemId != "null") {
      	  svcItem = svcItemMapper.selectByPrimaryKey(Long.parseLong(itemId));
      	  SvcItemImg tempItemImg  =  mapper.getItemImgId(svcItem.getId());
      	  if(tempItemImg != null){
      		  id = Long.toString(tempItemImg.getId());
      		  svcItemImg = svcItemImgMapper.selectByPrimaryKey(tempItemImg.getId());
      	  }
      }
  
      if (id == null) {
        id = "temps";
        resultMap.put("temp", true);
      }
  
      if (request.getFile("imagefile") != null) {
        MultipartFile file = request.getFile("imagefile");
        String filePath = "";
        String resultPath = "";
        String fileName = file.getOriginalFilename();
        String extension = getExtension(fileName);
        String imgName = "";
        String fileType = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1, file.getContentType().length());
        
        boolean isStore = svcItem.getStoreId() != null ? true : false;
        if (isStore) {
          //   path -->  /img-resource/items/store/{storeId}/{itemId}/it_st_{storeId}_System.CurrentMillisecondes;
          imgName = String.format("it_st_%s_%d.%s",  svcItem.getStoreId(), System.currentTimeMillis(), extension);
          resultPath = "/image-resource"+ Globals.ITEM_RESOURCE + Globals.STORE_RESOURCE + File.separator + svcItem.getStoreId() 
                                          + File.separator + svcItem.getId() + File.separator;
          filePath = Globals.IMG_RESOURCE + Globals.ITEM_RESOURCE + Globals.STORE_RESOURCE + File.separator + svcItem.getStoreId() 
                                          + File.separator + svcItem.getId() + File.separator;
        } else {
          imgName = String.format("it_st_%s_%d.%s",  svcItem.getBrandId(), System.currentTimeMillis(), extension);
          resultPath = "/image-resource" + Globals.ITEM_RESOURCE + Globals.STORE_RESOURCE + File.separator + svcItem.getBrandId() 
                                          + File.separator + svcItem.getId() + File.separator;
          filePath = Globals.IMG_RESOURCE + Globals.ITEM_RESOURCE + Globals.STORE_RESOURCE + File.separator + svcItem.getStoreId() 
                                          + File.separator + svcItem.getId() + File.separator; 
        }
        logger.debug("resultPath : " + resultPath);
        logger.debug("filePath : " + filePath);
  
//        /Users/kimdoyeon/Documents/clone/pico/web/src/main/webapp/userFile/img-resource/items/store/4/115/ 
//        /Users/kimdoyeon/Documents/clone/pico/web/src/main/webapp/userFile/image/items/store/4/115/ 
        
        byte[] bytes = file.getBytes();
        File Folder = new File(filePath);
        File lOutFile = new File(filePath + imgName);
  
        if (!Folder.isDirectory()) {
          Folder.mkdirs();
        }
  
        if (lOutFile.isFile()) {
          if (lOutFile.delete()) {
            FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
            lFileOutputStream.write(bytes);
            lFileOutputStream.close();
          }
        } else {
          FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
          lFileOutputStream.write(bytes);
          lFileOutputStream.close();
        }
  
  	  SvcItemImg _svcItemImg = new SvcItemImg();
  
  	  _svcItemImg.setItemId(svcItem.getId());
  	  _svcItemImg.setBrandId(svcItem.getBrandId());
  	  if(svcItem.getStoreId() != null){
  		  _svcItemImg.setStoreId(svcItem.getStoreId());
  	  }
  	  _svcItemImg.setImage(resultPath + imgName);
  	  _svcItemImg.setImageView(fileName);
  	  _svcItemImg.setSmallImage(resultPath + imgName);
  	  _svcItemImg.setSmallImageView(fileName);
  
        	if (svcItemImg == null) {
        		resultMap.put("success", svcItemImgMapper.insertSelective(_svcItemImg) == 1 ? true : false );
        		resultMap.put("url", resultUrl);
        		resultMap.put("errMsg", "");
        	}
        	else if(svcItemImg != null){
  	    	_svcItemImg.setId(svcItemImg.getId());
  	    	resultMap.put("success", svcItemImgMapper.updateByPrimaryKeySelective(_svcItemImg) == 1 ? true : false );
  	    	resultMap.put("url", resultUrl);
  	    	resultMap.put("errMsg", "");
        	}else{
  	    	resultMap.put("success", false);
  	    	resultMap.put("errMsg", "not supported method.");
  	    }
  
        if (System.getProperty("os.name").contains("Linux")) {
          String chmod = "chmod 777 " + filePath + imgName;
  
          try {
            Process p = Runtime.getRuntime().exec(chmod);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
  
            while ((line = br.readLine()) != null) {
              logger.info("commend line : " + line);
            }
          } catch (Exception e) {
            logger.error(e.getMessage());
          }
        }
  
      } else {
      	resultMap.put("success", false);
      	resultMap.put("url", resultUrl);
      	resultMap.put("errMsg", "not supported method.");
      }
  
      return resultMap;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }


  /**
   * Items - Items
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/ItemsSelect")
  public Map<Object,Object> ItemsSelect(
		  @RequestParam(required=false) SvcItem item,
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

	    Map<Object,Object> result = new HashMap<>();
//	    CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);

	    if(item == null && params.get("data") != null){
	    	item = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcItem.class);
	    }

	    switch(method){
	    case GET :

	    	SvcItemExample svcItemWhere = new SvcItemExample();
       	  	SvcItemExample.Criteria criteria = svcItemWhere.createCriteria();

	    	if(item.getBrandId() != null){
    			criteria.andBrandIdEqualTo(item.getBrandId());
    		}
    		if(item.getStoreId() != null){
    			criteria.andStoreIdEqualTo(item.getStoreId());
    		}
    		if(item.getCatId() != null){
    			criteria.andCatIdEqualTo(item.getCatId());
    		}

    		criteria.andStatusEqualTo("602001");

//    		result.put("list", mapper.selectItems(item));
			result.put("list", svcItemMapper.selectByExample(svcItemWhere));
			result.put("success", true);

	      break;
	    case POST :
	      break;
	    case PUT :
	    break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }


  /**
   * Items - SetItem
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/SetItem")
  public Map<Object,Object> SetItem(
      @RequestParam(required=false) SvcFranchise franchise,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {
    Map<Object,Object> result = new HashMap<>();


    if(franchise == null && params.get("data") != null){
      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
    }

    switch(method){
    case GET :
      if(franchise == null){
        List<SvcFranchise> list = svcFranchiseMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", svcFranchiseMapper.countByExample(null));
        result.put("recordsFiltered", svcFranchiseMapper.countByExample(null));
      } else {
        result.put("list", svcFranchiseMapper.selectByPrimaryKey(franchise.getId()));
      }

      result.put("success", true);
      break;
    case POST :
      franchise.setTenantId(1l);

      result.put("success", svcFranchiseMapper.insertSelective(franchise) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :

      result.put("success", svcFranchiseMapper.updateByPrimaryKeySelective(franchise) == 1 ? true : false );
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
   * ItemOption
   * @param itemOpt
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/ItemOption")
  public Map<Object,Object> ItemOption(
		  @RequestParam(required=false) SvcItemOpt itemOpt,
	      @RequestParam(required=false) Map<Object,Object> params,
	      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object,Object> result = new HashMap<>();

    if(itemOpt == null && params.get("data") != null){
    	itemOpt = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcItemOpt.class);
    }

    switch(method){
    case GET :
      if(itemOpt.getId() == null){

    	SvcItemOptExample svcItemOptWhere = new SvcItemOptExample();
    	SvcItemOptExample.Criteria criteria = svcItemOptWhere.createCriteria();

        if(itemOpt.getItemId() != null){
    		criteria.andItemIdEqualTo(itemOpt.getItemId());
        }

        svcItemOptWhere.setOrderByClause("ORDINAL ASC");

        List<SvcItemOpt> list = svcItemOptMapper.selectByExample(svcItemOptWhere);
        result.put("list", list);
      } else {
    	  // 1. 옵션마스터 정보
    	  result.put("optInfo", svcItemOptMapper.selectByPrimaryKey(itemOpt.getId()));

    	  // 2. 옵션상세 리스트 정보
    	  SvcItemOptDtlExample svcItemOptDtlWhere = new SvcItemOptDtlExample();
    	  SvcItemOptDtlExample.Criteria criteria = svcItemOptDtlWhere.createCriteria();

    	  criteria.andOptIdEqualTo(itemOpt.getId());
    	  svcItemOptDtlWhere.setOrderByClause("ORDINAL ASC");
    	  result.put("optDtlList", svcItemOptDtlMapper.selectByExample(svcItemOptDtlWhere));
      }

      result.put("success", true);

      break;
    case POST :
		itemOpt.setDesc(itemOpt.getName());
		itemOpt.setOrdinal((byte) 99);
		result.put("success", svcItemOptMapper.insertSelective(itemOpt) == 1 ? true : false );
	    result.put("errMsg", "");
      break;
    case PUT :
    	String mode =params.get("mode").toString();
    	String dataList =params.get("dataList").toString();
    	 if(mode.equals("optListUpt")){
        	 try{
	    		List<SvcItemOpt>  _list = JsonConvert.JsonConvertObject(dataList, new TypeReference<List<SvcItemOpt>>() {});

     			// 리스트 정보 입력
     	    	for (SvcItemOpt itemOptList : _list) {
     	            svcItemOptMapper.updateByPrimaryKeySelective(itemOptList);
     	        }

         		result.put("success",  true );
         		result.put("errMsg", "");

         	}catch (Exception e) {
         		result.put("success",  false );
         		result.put("errMsg", "update fail.");
     		}
    	 }
    	 else if(mode.equals("optAndDtlInst")){

    		 try{
	    		 //1. 옵션 마스터 수정
	    		 svcItemOptMapper.updateByPrimaryKeySelective(itemOpt);

	    		 //2. 상세정보 갱신 : 상세옵션 목록 삭제 후 변경된 정보 insert
	    		 SvcItemOptDtlExample svcItemOptDtlWhere = new SvcItemOptDtlExample();
       		  	 SvcItemOptDtlExample.Criteria criteria = svcItemOptDtlWhere.createCriteria();

       		  	 criteria.andOptIdEqualTo(itemOpt.getId());
       		  	 svcItemOptDtlMapper.deleteByExample(svcItemOptDtlWhere);

       		  	 List<SvcItemOptDtl>  _OptDtl = JsonConvert.JsonConvertObject(dataList, new TypeReference<List<SvcItemOptDtl>>() {});
       			// 리스트 정보 입력

      	    	for (SvcItemOptDtl itemOptDtlList : _OptDtl) {
      	    		itemOptDtlList.setBrandId(itemOpt.getBrandId());
      	    		itemOptDtlList.setStoreId(itemOpt.getStoreId());
      	    		itemOptDtlList.setItemId(itemOpt.getItemId());
      	    		svcItemOptDtlMapper.insertSelective(itemOptDtlList);
      	    	}

      	    	result.put("success",  true );
         		result.put("errMsg", "");

    		 }catch (Exception e) {
    			result.put("success",  false );
          		result.put("errMsg", "update fail.");
    		 }

    	 }
    	 else{
    		 result.put("success",  false );
      		 result.put("errMsg", "update fail.");
    	 }

      break;
          case DELETE :
        	  mode =params.get("mode").toString();
        	  if(mode.equals("optListDel")){

        		  SvcItemOptDtlExample svcItemOptDtlWhere = new SvcItemOptDtlExample();
        		  SvcItemOptDtlExample.Criteria criteria = svcItemOptDtlWhere.createCriteria();

        		  if(itemOpt.getId() != null){

        			  criteria.andOptIdEqualTo(itemOpt.getId());

        			  //상세옵션 목록 삭제
	        		  svcItemOptDtlMapper.deleteByExample(svcItemOptDtlWhere);
	        		  //부모옵션 삭제
	        		  svcItemOptMapper.deleteByPrimaryKey(itemOpt.getId());

	        		  result.put("success", true);
	        		  result.put("errMsg", "");

        		  }else{
        			  result.put("success", false);
        			  result.put("errMsg", "id is null.");
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
   * GetItemOptAndDtl
   * @param itemOpt
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   * @throws UnsupportedEncodingException
   */
  @RequestMapping("/GetItemOptAndDtl")
  public Map<Object,Object> GetItemOptAndDtl(
		  @RequestParam(required=false) SvcItemOpt itemOpt,
	      @RequestParam(required=false) Map<Object,Object> params,
	      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object,Object> result = new HashMap<>();

    if(itemOpt == null && params.get("data") != null){
    	itemOpt = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcItemOpt.class);
    }

    switch(method){
    case GET :

    	String mode =params.get("mode").toString();

    	if(mode.equals("getOptData")){

	    	SvcItemOptExample svcItemOptWhere = new SvcItemOptExample();
	    	SvcItemOptExample.Criteria criteria = svcItemOptWhere.createCriteria();

	        criteria.andIsUsedEqualTo(true); //사용가능한 옵션만 가져옴
	        svcItemOptWhere.setOrderByClause("ORDINAL ASC");

	        List<SvcItemOpt> list = svcItemOptMapper.selectByExample(svcItemOptWhere);
	        result.put("list", list);

	        result.put("success", true);
    	}

    	if(mode.equals("getOptDtlData")){
		    if(itemOpt.getId() != null){
		    	SvcItemOptDtlExample svcItemOptDtlWhere = new SvcItemOptDtlExample();
		    	SvcItemOptDtlExample.Criteria dtlCriteria = svcItemOptDtlWhere.createCriteria();

		    	dtlCriteria.andOptIdEqualTo(itemOpt.getId());
		    	svcItemOptDtlWhere.setOrderByClause("ORDINAL ASC");
		    	result.put("optDtlList", svcItemOptDtlMapper.selectByExample(svcItemOptDtlWhere));

		    	result.put("success", true);
		    }
    	}

      break;
    case POST :
		itemOpt.setDesc(itemOpt.getName());
		itemOpt.setOrdinal((byte) 99);
		result.put("success", svcItemOptMapper.insertSelective(itemOpt) == 1 ? true : false );
	    result.put("errMsg", "");
      break;
    case PUT :
    	mode =params.get("mode").toString();

    	if(mode.equals("otherItemOptDataSave")){

	    	SvcItemOptExample svcItemOptWhere = new SvcItemOptExample();
	    	SvcItemOptExample.Criteria criteria = svcItemOptWhere.createCriteria();

	    	criteria.andIdEqualTo(itemOpt.getId());
	    	SvcItemOpt getOptData = svcItemOptMapper.selectByExample(svcItemOptWhere).get(0);

	    	SvcItemOptDtlExample svcItemOptDtlWhere = new SvcItemOptDtlExample();
	    	SvcItemOptDtlExample.Criteria dtlCriteria = svcItemOptDtlWhere.createCriteria();

	    	dtlCriteria.andOptIdEqualTo(itemOpt.getId());
	    	svcItemOptDtlWhere.setOrderByClause("ORDINAL ASC");

	    	List<SvcItemOptDtl> getItemOptDtl = svcItemOptDtlMapper.selectByExample(svcItemOptDtlWhere);

	    	try{

	    		getOptData.setBrandId(itemOpt.getBrandId());
		    	getOptData.setStoreId(itemOpt.getStoreId());
		    	getOptData.setItemId(itemOpt.getItemId());
		    	getOptData.setId(null);

		    	result.put("success", svcItemOptMapper.insertSelective(getOptData) == 1 ? true : false );

//		    	logger.debug("getOptData.getId() >>>>>>>>>>>>>>> " + getOptData.getId())  ;

		    	for (SvcItemOptDtl getItemOptDtlList : getItemOptDtl) {
		    		getItemOptDtlList.setBrandId(itemOpt.getBrandId());
		    		getItemOptDtlList.setStoreId(itemOpt.getStoreId());
		    		getItemOptDtlList.setItemId(itemOpt.getItemId());
		    		getItemOptDtlList.setOptId(getOptData.getId());
		    		getItemOptDtlList.setId(null);
      	    		svcItemOptDtlMapper.insertSelective(getItemOptDtlList);
      	    	}

	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}

    	}
      break;
          case DELETE :

            break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }


  /**
   * CouseItem
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/CouseItem")
  public Map<Object,Object> CouseItem(
      @RequestParam(required=false) SvcFranchise franchise,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {
    Map<Object,Object> result = new HashMap<>();


    if(franchise == null && params.get("data") != null){
      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
    }

    switch(method){
    case GET :
      if(franchise == null){
        List<SvcFranchise> list = svcFranchiseMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", svcFranchiseMapper.countByExample(null));
        result.put("recordsFiltered", svcFranchiseMapper.countByExample(null));
      } else {
        result.put("list", svcFranchiseMapper.selectByPrimaryKey(franchise.getId()));
      }

      result.put("success", true);

      break;
    case POST :
      franchise.setTenantId(1l);

      result.put("success", svcFranchiseMapper.insertSelective(franchise) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :

      result.put("success", svcFranchiseMapper.updateByPrimaryKeySelective(franchise) == 1 ? true : false );
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
   * PosPluCate
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/PosPluCate")
  public Map<Object,Object> PosPluCate(
      @RequestParam(required=false) SvcPluCat pluCat,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {

    Map<Object,Object> result = new HashMap<>();

    if(pluCat == null && params.get("data") != null){
    	pluCat = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcPluCat.class);
    }

    CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);

    switch(method){
    case GET :
	    	if(pluCat.getBrandId() == null){
	    		pluCat.setBrandId((long) 0);
    		}

    	  result.put("list", mapper.getPluCategory(pluCat));
    	  result.put("success", true);

      break;
    case POST :
      result.put("success", svcPluCatMapper.insertSelective(pluCat) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcPluCatMapper.updateByPrimaryKeySelective(pluCat) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case DELETE :
	    	/* */
	    	  SvcPluCatExample svcPluCatExample = new SvcPluCatExample();
	    	  svcPluCatExample.createCriteria().andParentEqualTo(pluCat.getId());

	    	  boolean exists = false;

	    	  //1.하위카테고리 존재여부 체크
	    	  int existsCount = svcPluCatMapper.countByExample(svcPluCatExample);
	    	  if(existsCount > 0){
	    		  exists = true;
	    		  result.put("errMsg", "하위 카테고리가 존재합니다.");
	    	  }else{
	    		  //2.해당 카테고리의 상품 존재 여부
	    		  SvcPluItemExample svcPluItemExample = new SvcPluItemExample();
	    		  svcPluItemExample.createCriteria().andCatIdEqualTo(pluCat.getId());

	    		  existsCount = svcPluItemMapper.countByExample(svcPluItemExample);
	    		  if(existsCount > 0){
	    			  exists = true;
	    			  result.put("errMsg", "카테고리 상품이 존재합니다.");
	    		  }
	    	  }

	      	  //3.하위 카테고리 및 등록상품이 없으면 삭제 실행
	    	  if (!exists) {
	    		  result.put("success", svcPluCatMapper.deleteByPrimaryKey(pluCat.getId()) == 1 ? true : false );
	    		  result.put("errMsg", "");
	    	  }
	        break;
	    default :
	      result.put("success", false);
	      result.put("errMsg", "not supported method.");
	    }

		return result;
	}


  /**
   * PosPluCate
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/PluTypeSelect")
  public Map<Object,Object> pluTypeSelect(
      @RequestParam(required=false) SvcPluCat pluCat,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {

	  Map<Object,Object> result = new HashMap<>();

	  if(pluCat == null && params.get("data") != null){
	  	pluCat = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcPluCat.class);
	  }

	  CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);

	  switch(method){
	  case GET :
      	  if(pluCat.getBrandId() == null){
      		pluCat.setBrandId((long) 0);
  		  }

		  result.put("list", mapper.pluTypeSelect(pluCat));
		  result.put("success", true);

	    break;
	  case POST :
	    break;
	  case PUT :
	    break;
	  case DELETE :
	      break;
	  default :
	    result.put("success", false);
	    result.put("errMsg", "not supported method.");
	  }

	  return result;
  }


  /**
   * PosPluSet
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/PosPluSet")
  public Map<Object,Object> PosPluSet(
      @RequestParam(required=false) SvcPluItem svcPluItem,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {

    Map<Object,Object> result = new HashMap<>();


    if(svcPluItem == null && params.get("data") != null){
    	svcPluItem = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcPluItem.class);
    }

    SvcPluItemExample svcPluItemWhere = new SvcPluItemExample();
    SvcPluItemExample.Criteria criteria = svcPluItemWhere.createCriteria();

    if(svcPluItem.getBrandId() != null){
		criteria.andBrandIdEqualTo(svcPluItem.getBrandId());
	}
	if(svcPluItem.getStoreId() != null){
		criteria.andStoreIdEqualTo(svcPluItem.getStoreId());
	}
	if(svcPluItem.getCatId() != null){
		criteria.andCatIdEqualTo(svcPluItem.getCatId());
	}

	CustomItemsMapper mapper = sessionTemplate.getMapper(CustomItemsMapper.class);

    switch(method){
    case GET :
      if(svcPluItem != null){
    	  result.put("list", mapper.getPluItemGridly(svcPluItem));
      }
      result.put("success", true);

      break;
    case POST :

    	String dataList =params.get("dataList").toString();
    	List<SvcPluItem> _list = JsonConvert.JsonConvertObject(dataList, new TypeReference<List<SvcPluItem>>() {
        });

    	logger.debug("_list " + _list);

    	try{

			//기존정보삭제
			svcPluItemMapper.deleteByExample(svcPluItemWhere);

			// 리스트 정보 다시입력
	    	for (SvcPluItem pluItem : _list) {
	            svcPluItemMapper.insertSelective(pluItem);
	        }

    		result.put("success",  true );
    		result.put("errMsg", "");

    	}catch (Exception e) {
    		result.put("success",  false );
    		result.put("errMsg", "insert fail.");
		}

      break;
    case PUT :

//      result.put("success", svcPluItemMapper.updateByPrimaryKeySelective(svcPluItem) == 1 ? true : false );
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
   * MobileDpCate
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/MobileDpCate")
  public Map<Object,Object> MobileDpCate(
      @RequestParam(required=false) SvcFranchise franchise,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {
    Map<Object,Object> result = new HashMap<>();


    if(franchise == null && params.get("data") != null){
      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
    }

    switch(method){
    case GET :
      if(franchise == null){
        List<SvcFranchise> list = svcFranchiseMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", svcFranchiseMapper.countByExample(null));
        result.put("recordsFiltered", svcFranchiseMapper.countByExample(null));
      } else {
        result.put("list", svcFranchiseMapper.selectByPrimaryKey(franchise.getId()));
      }

      result.put("success", true);

      break;
    case POST :
      franchise.setTenantId(1l);

      result.put("success", svcFranchiseMapper.insertSelective(franchise) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :

      result.put("success", svcFranchiseMapper.updateByPrimaryKeySelective(franchise) == 1 ? true : false );
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
   * MobileDpSet
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/MobileDpSet")
  public Map<Object,Object> MobileDpSet(
      @RequestParam(required=false) SvcFranchise franchise,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {
    Map<Object,Object> result = new HashMap<>();


    if(franchise == null && params.get("data") != null){
      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
    }

    switch(method){
    case GET :
      if(franchise == null){
        List<SvcFranchise> list = svcFranchiseMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", svcFranchiseMapper.countByExample(null));
        result.put("recordsFiltered", svcFranchiseMapper.countByExample(null));
      } else {
        result.put("list", svcFranchiseMapper.selectByPrimaryKey(franchise.getId()));
      }

      result.put("success", true);

      break;
    case POST :
      franchise.setTenantId(1l);

      result.put("success", svcFranchiseMapper.insertSelective(franchise) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :

      result.put("success", svcFranchiseMapper.updateByPrimaryKeySelective(franchise) == 1 ? true : false );
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
   * TabDpCate
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/TabDpCate")
  public Map<Object,Object> TabDpCate(
		  @RequestParam(required=false) SvcPluCat pluCat,
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws InvalidJsonException {

	Map<Object,Object> result = new HashMap<>();

    if(pluCat == null && params.get("data") != null){
    	pluCat = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcPluCat.class);
    }

    switch(method){
    case GET :

	      SvcPluCatExample svcPluCatWhere = new SvcPluCatExample();
	      SvcPluCatExample.Criteria criteria = svcPluCatWhere.createCriteria();

	      if(pluCat.getBrandId() != null){
				criteria.andBrandIdEqualTo(pluCat.getBrandId());
			}else{
				criteria.andBrandIdEqualTo((long) 0);
			}
			if(pluCat.getStoreId() != null){
				criteria.andStoreIdEqualTo(pluCat.getStoreId());
			}

			if(pluCat.getPluTp() != null){
				criteria.andPluTpEqualTo(pluCat.getPluTp());
			}

	      result.put("list", svcPluCatMapper.selectByExample(svcPluCatWhere));
    	  result.put("success", true);

      break;
    case POST :
      result.put("success", svcPluCatMapper.insertSelective(pluCat) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcPluCatMapper.updateByPrimaryKeySelective(pluCat) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case DELETE :

    	boolean exists = false;

		  //1.해당 카테고리의 상품 존재 여부
		  SvcPluItemExample svcPluItemExample = new SvcPluItemExample();
		  svcPluItemExample.createCriteria().andCatIdEqualTo(pluCat.getId());

		  int existsCount = svcPluItemMapper.countByExample(svcPluItemExample);

		  if(existsCount > 0){
			  exists = true;
			  result.put("errMsg", "카테고리 상품이 존재합니다.");
		  }

	  	  //3.등록상품이 없으면 삭제 실행
		  if (!exists) {
			  result.put("success", svcPluCatMapper.deleteByPrimaryKey(pluCat.getId()) == 1 ? true : false );
			  result.put("errMsg", "");
		  }

	    break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

	return result;
	}

  /**
   * TabDpSet
   * @param franchise
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/TabDpSet")
  public Map<Object,Object> TabDpSet(
      @RequestParam(required=false) SvcFranchise franchise,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {
    Map<Object,Object> result = new HashMap<>();


    if(franchise == null && params.get("data") != null){
      franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
    }

    switch(method){
    case GET :
      if(franchise == null){
        List<SvcFranchise> list = svcFranchiseMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", svcFranchiseMapper.countByExample(null));
        result.put("recordsFiltered", svcFranchiseMapper.countByExample(null));
      } else {
        result.put("list", svcFranchiseMapper.selectByPrimaryKey(franchise.getId()));
      }

      result.put("success", true);

      break;
    case POST :
      franchise.setTenantId(1l);

      result.put("success", svcFranchiseMapper.insertSelective(franchise) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :

      result.put("success", svcFranchiseMapper.updateByPrimaryKeySelective(franchise) == 1 ? true : false );
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
   * Sales - Pos Table Set
   * @param tableSection
   * @param table
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/PosTableSet")
  public Map<Object,Object> PosTableSet(
      @RequestParam(required=false) SvcTableSection tableSection,
      @RequestParam(required=false) SvcTable table,
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException {

	Map<Object,Object> result = new HashMap<>();
    Map<String, String> search = new HashMap<>();
    SvcTableSectionExample tsExample = new SvcTableSectionExample();
    SvcTableExample example = new SvcTableExample();

    String mode = "";
    long sectionId = 0l;
	boolean flag = true;

	if (tableSection == null && table == null && params.get("data") != null) {
		tableSection = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcTableSection.class);
		table = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcTable.class);
        search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
        mode = search.get("mode");
        sectionId = StrUtils.parseLong(search.get("sectionId"));
	}

	switch (method) {
		case GET :

	        if (String.valueOf(search.get("detail")).equals("null")) {
	        	tsExample.createCriteria().andBrandIdEqualTo(StrUtils.parseLong(search.get("brandId")))
	        	.andStoreIdEqualTo(StrUtils.parseLong(search.get("storeId")));
	        	List<SvcTableSection> floorList = svcTableSectionMapper.selectByExample(tsExample);

	        	if (floorList.size() > 0) {
	        		if (sectionId == 0) sectionId =	floorList.get(0).getId();
	        	}

	        	example.createCriteria().andBrandIdEqualTo(StrUtils.parseLong(search.get("brandId")))
	        	.andStoreIdEqualTo(StrUtils.parseLong(search.get("storeId")))
	        	.andSectionIdEqualTo(sectionId);
	        	List<SvcTable> tableList = svcTableMapper.selectByExample(example);

	        	result.put("floorList", floorList);
	        	result.put("tableList", tableList);
	        	result.put("sectionId", sectionId);
	        	result.put("tableCnt", svcTableMapper.countByExample(example));
	        }
	        else {
	        	if ("floor-modal".equals(mode)) {
	        		result.put("list", svcTableSectionMapper.selectByPrimaryKey(table.getId()));
	        	}
	        	else {
	        		result.put("list", svcTableMapper.selectByPrimaryKey(table.getId()));
	        	}
	        }
	        result.put("success", true);

			break;

		case POST :

			if ("table-amt-modal".equals(mode)) {
				int tableCnt = StrUtils.parseInt(search.get("tableCnt"));

				example.createCriteria().andSectionIdEqualTo(sectionId);
				svcTableMapper.deleteByExample(example);

				if (flag) {
					for (int i = 1; i <= tableCnt; i++) {
						table.setName(Integer.toString(i));
						table.setOrdinal((short) i);

						flag = svcTableMapper.insertSelective(table) == 1 ? true : false;
		                if (!flag) break;
					}
					result.put("sectionId", sectionId);
				}
			}
			else {
				flag = svcTableSectionMapper.insertSelective(tableSection) == 1 ? true : false;
			}
			result.put("success", flag);
		    result.put("errMsg", "");

		    break;

		case PUT :

        	if ("floor-modal".equals(mode)) {
        		flag = svcTableSectionMapper.updateByPrimaryKeySelective(tableSection) == 1 ? true : false;
        	}
        	else {
        		flag = svcTableMapper.updateByPrimaryKeySelective(table) == 1 ? true : false;
        	}
        	result.put("sectionId", sectionId);
			result.put("success", flag);
			result.put("errMsg", "");

			break;

	    case DELETE :

	    	example.createCriteria().andSectionIdEqualTo(tableSection.getId());
	    	svcTableMapper.deleteByExample(example);

		    result.put("success", svcTableSectionMapper.deleteByPrimaryKey(tableSection.getId()) == 1 ? true : false);
		    result.put("errMsg", "");
		    break;

 	    default :
		    result.put("success", false);
		    result.put("errMsg", "not supported method.");
		    break;
	    }

	return result;
  }

  /**
   * Sales - Pos Message Set
   * @param kitchenMessage
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/PosMessageSet")
  public Map<Object,Object> PosMessageSet(
      @RequestParam(required=false) SvcKitchenMessage kitchenMessage,
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException {

	Map<Object,Object> result = new HashMap<>();
    Map<String, String> search = new HashMap<>();
    SvcKitchenMessageExample example = new SvcKitchenMessageExample();

	if (kitchenMessage == null && params.get("data") != null) {
		kitchenMessage = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcKitchenMessage.class);
        search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
	}

	switch (method) {
		case GET :

        	example.createCriteria().andBrandIdEqualTo(StrUtils.parseLong(search.get("brandId")))
        	.andStoreIdEqualTo(StrUtils.parseLong(search.get("storeId")))
        	.andItemCatIdEqualTo(StrUtils.parseLong(search.get("itemCatId")));
        	List<SvcKitchenMessage> list = svcKitchenMessageMapper.selectByExample(example);

        	result.put("list", list);
            result.put("totCnt", svcKitchenMessageMapper.countByExample(example));
	        result.put("success", true);

			break;

		case POST :

			result.put("success", svcKitchenMessageMapper.insertSelective(kitchenMessage) == 1 ? true : false);
		    result.put("errMsg", "");

		    break;

		case PUT :

			result.put("success", svcKitchenMessageMapper.updateByPrimaryKeySelective(kitchenMessage) == 1 ? true : false);
			result.put("errMsg", "");

			break;

	    case DELETE :

		    result.put("success", svcKitchenMessageMapper.deleteByPrimaryKey(kitchenMessage.getId()) == 1 ? true : false);
		    result.put("errMsg", "");

		    break;

 	    default :
		    result.put("success", false);
		    result.put("errMsg", "not supported method.");
		    break;
	    }

	return result;
  }

  private String getExtension(String fileName) {
    String extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
    return extension;
  }
}
