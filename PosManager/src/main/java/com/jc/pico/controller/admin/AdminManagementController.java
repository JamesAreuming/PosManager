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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.jc.pico.bean.BaseBcode;
import com.jc.pico.bean.BaseBcodeExample;
import com.jc.pico.bean.BaseMcode;
import com.jc.pico.bean.BaseMcodeExample;
import com.jc.pico.bean.Notice;
import com.jc.pico.bean.NoticeExample;
import com.jc.pico.bean.SvcApp;
import com.jc.pico.bean.SvcMailLogWithBLOBs;
import com.jc.pico.bean.SvcSmsLogWithBLOBs;
import com.jc.pico.bean.SvcStoreUser;
import com.jc.pico.bean.SvcStoreUserExample;
import com.jc.pico.bean.SvcTerms;
import com.jc.pico.bean.SvcTermsExample;
import com.jc.pico.bean.SvcUserMapping;
import com.jc.pico.bean.SvcUserMappingExample;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserBackofficeMenu;
import com.jc.pico.bean.UserBackofficeMenuExample;
import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserDeviceExample;
import com.jc.pico.bean.UserExample;
import com.jc.pico.bean.UserGroup;
import com.jc.pico.bean.UserGroupAuth;
import com.jc.pico.bean.UserGroupKey;
import com.jc.pico.bean.UserPermition;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.ext.sms.SMSUtil;
import com.jc.pico.mapper.BaseBcodeMapper;
import com.jc.pico.mapper.BaseMcodeMapper;
import com.jc.pico.mapper.NoticeMapper;
import com.jc.pico.mapper.SvcAppMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcBrandSetMapper;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcFranchiseMapper;
import com.jc.pico.mapper.SvcMailLogMapper;
import com.jc.pico.mapper.SvcSmsLogMapper;
import com.jc.pico.mapper.SvcStampMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcStoreUserMapper;
import com.jc.pico.mapper.SvcTermsMapper;
import com.jc.pico.mapper.SvcUserMappingMapper;
import com.jc.pico.mapper.UserBackofficeMenuMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.mapper.UserGroupAuthMapper;
import com.jc.pico.mapper.UserGroupMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.mapper.UserPermitionMapper;
import com.jc.pico.queue.PushSender;
import com.jc.pico.utils.AdminHandler;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.Config;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.bean.SendPush;
import com.jc.pico.utils.bean.UserBackofficeMenuList;
import com.jc.pico.utils.bean.UserGroupAuthProcedure;
import com.jc.pico.utils.customMapper.admin.CustomManagementMapper;
import com.jc.pico.utils.customMapper.admin.CustomMarketingMapper;
import com.jc.pico.utils.jc.mail.MailSender;

@Controller
@ResponseBody
@RequestMapping("/admin/model/management")
public class AdminManagementController {

  protected static Logger logger = LoggerFactory.getLogger(AdminManagementController.class);

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  CodeUtil codeUtil;
  @Autowired
  SvcFranchiseMapper svcFranchiseMapper;

  @Autowired
  SvcAppMapper svcAppMapper;

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
  UserPermitionMapper userPermitionMapper;
  
  @Autowired
  NoticeMapper noticeMapper;

  @Autowired
  UserMapper userMapper;
  
  @Autowired
  SvcUserMappingMapper svcUserMappingMapper;

  @Autowired
  CustomManagementMapper customManagementMapper;

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Autowired
  private HttpSession session;
  
  @Autowired
  private FileSystemResource fileuploadDirResource;
  
  private Config config = Config.getInstance();

  @RequestMapping(value = "/Codes", method = RequestMethod.POST)
  public Map<String, Object> getBaseCode(
      @RequestParam("codes") String Codes,
      @RequestParam Map<Object, Object> params) {

    Map<String, Object> returnMap = new HashMap<>();
    Map<String, List<BaseBcode>> codeMap = new HashMap<>();
    BaseBcodeExample baseBcodeExample = new BaseBcodeExample();
    BaseMcodeExample baseMcodeExample = new BaseMcodeExample();

    String[] codes = Codes.split(",");

    for (String code : codes) {

      baseMcodeExample.clear();
      baseMcodeExample.createCriteria().andAliasEqualTo(code);

      List<BaseMcode> baseMcode = baseCodeMainMapper.selectByExample(baseMcodeExample);

      if (baseMcode.size() == 1) {

        baseBcodeExample.clear();
        baseBcodeExample.createCriteria().andMainCdEqualTo(baseMcode.get(0).getMainCd());

        codeMap.put(code.toLowerCase(), baseCodeMapper.selectByExample(baseBcodeExample));
      }
    }
    
    if (codeMap.size() == codes.length) {
      returnMap.put("success", true);
      returnMap.put("codes", codeMap);
    } else {
      returnMap.put("success", false);
    }
    logger.debug("code : " + JsonConvert.toJson(returnMap));
    return returnMap;
  }


  @RequestMapping("/Terms")
  public Map<Object, Object> Terms(
      @RequestParam(required = false) SvcTerms terms,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {
    Map<Object, Object> result = new HashMap<>();

    if (terms == null && params.get("data") != null) {
      terms = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcTerms.class);
    }

    switch (method) {
      case GET:
        if (!String.valueOf(params.get("draw")).equals("null")) {

          SvcTermsExample example = new SvcTermsExample();

          String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : "");

          //example.createCriteria().andTitleLike("%" + new String(searchString.getBytes("iso-8859-1"), "utf-8") + "%");
          example.createCriteria().andTitleLike("%" + searchString + "%");

          if (params.get("order[0][column]") != null) {
            String orderby = String.valueOf(params.get("columns[" + String.valueOf(params.get("order[0][column]")) + "][name]"));
            String orderByDir = (String) (params.get("order[0][dir]") != null ? params.get("order[0][dir]") : "desc");
            example.setOrderByClause(orderby + " " + orderByDir + " , ID " + orderByDir);
          }

          List<SvcTerms> list = svcTermsMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", svcTermsMapper.countByExample(null));
          result.put("recordsFiltered", svcTermsMapper.countByExample(example));
        } else {
          result.put("list", svcTermsMapper.selectByPrimaryKey(terms.getId()));
        }

        result.put("success", true);

        break;
      case POST:
        terms.setServiceId(Globals.PICO_SERVICE_ID);
        result.put("success", svcTermsMapper.insertSelective(terms) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      case PUT:
        result.put("success", svcTermsMapper.updateByPrimaryKeySelective(terms) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/Notice")
  public Map<Object, Object> Notice(
      @RequestParam(required = false) Notice notice,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {
    Map<Object, Object> result = new HashMap<>();

    if (notice == null && params.get("data") != null) {
      notice = JsonConvert.JsonConvertObject(params.get("data").toString(), Notice.class);
    }

    switch (method) {
      case GET:
        if (!String.valueOf(params.get("draw")).equals("null")) {

          NoticeExample exampleTotalCount = new NoticeExample();
          NoticeExample example = new NoticeExample();

          if (params.get("order[0][column]") != null) {
            String orderby = String.valueOf(params.get("columns[" + String.valueOf(params.get("order[0][column]")) + "][name]"));
            String orderByDir = (String) (params.get("order[0][dir]") != null ? params.get("order[0][dir]") : "desc");
            example.setOrderByClause(orderby + " " + orderByDir + " , ID " + orderByDir);
          }

          String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : "");

          example.createCriteria().andNoticeTpEqualTo(codeUtil.getBaseCodeByAlias("notice"))
              //.andTitleLike("%" + new String(searchString.getBytes("iso-8859-1"), "utf-8") + "%");
              .andTitleLike("%" + searchString + "%");
          
          exampleTotalCount.createCriteria().andNoticeTpEqualTo(codeUtil.getBaseCodeByAlias("notice"));

          List<Notice> list = noticeMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", noticeMapper.countByExample(exampleTotalCount));
          result.put("recordsFiltered", noticeMapper.countByExample(example));
        } else {
          result.put("list", noticeMapper.selectByPrimaryKey(notice.getId()));
        }

        result.put("success", true);

        break;
      case POST:
        notice.setPlatformId(Globals.PICO_PLATFORM_ID);
        notice.setNoticeTp(codeUtil.getBaseCodeByAlias("notice"));
        result.put("success", noticeMapper.insertSelective(notice) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      case PUT:
        result.put("success", noticeMapper.updateByPrimaryKeySelective(notice) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }

  @RequestMapping("/Event")
  public Map<Object, Object> Event(
      @RequestParam(required = false) Notice notice,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {
    Map<Object, Object> result = new HashMap<>();

    if (notice == null && params.get("data") != null) {
      notice = JsonConvert.JsonConvertObject(params.get("data").toString(), Notice.class);
    }

    switch (method) {
      case GET:
        if (!String.valueOf(params.get("draw")).equals("null")) {
          params.put("noticeTp", codeUtil.getBaseCodeByAlias("event"));

          String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : "");

          if (params.get("order[0][column]") != null) {
            String orderby = String.valueOf(params.get("columns[" + String.valueOf(params.get("order[0][column]")) + "][name]"));
            String orderByDir = (String) (params.get("order[0][dir]") != null ? params.get("order[0][dir]") : "desc");
            params.put("orderByClause", orderby + " " + orderByDir + " , ID " + orderByDir);
          }

          //params.put("title", new String(searchString.getBytes("iso-8859-1"), "utf-8"));
          params.put("title", searchString);

          List<LinkedHashMap<String, Object>> list = customManagementMapper.getEventList(params, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", customManagementMapper.getCountEventListTotal(params));
          result.put("recordsFiltered", customManagementMapper.getCountEventList(params));
        } else {
          params.put("id", notice.getId());
          result.put("list", customManagementMapper.getEventList(params, new RowBounds(0, 10)));
        }

        result.put("success", true);

        break;
      case POST:
        notice.setPlatformId(Globals.PICO_PLATFORM_ID);
        notice.setNoticeTp(codeUtil.getBaseCodeByAlias("event"));
        result.put("success", noticeMapper.insertSelective(notice) == 1 ? true : false);
        result.put("id", notice.getId());
        result.put("errMsg", "");
        break;
      case PUT:
        result.put("success", noticeMapper.updateByPrimaryKeySelective(notice) == 1 ? true : false);
        result.put("id", notice.getId());
        result.put("errMsg", "");
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }

  @RequestMapping(value = "/Event/Image", method = RequestMethod.POST)
  public Map<Object, Object> EventImageUpload(
      MultipartHttpServletRequest request) throws IOException, InterruptedException {

    Map<Object, Object> resultMap = Maps.newHashMap();

    resultMap.put("success", false);

    String id = String.valueOf(request.getParameter("id"));
    String type = String.valueOf(request.getParameter("type"));
    String resultUrl = null;
    Notice event = null;

    if (id != "null") {
      if (!GenericValidator.isBlankOrNull(id) && GenericValidator.isInt(id)) {
        event = noticeMapper.selectByPrimaryKey(Long.parseLong(id));
      }
    }

    if (event != null) {
      id = String.valueOf(event.getId());
    } else {
      id = "temps";
      resultMap.put("temp", true);
    }

    if (request.getFile("imagefile") != null) {
      String file_path = Globals.IMG_RESOURCE + "/event/" + type + "/";
      String result_path = "/image-resource/event/" + type + "/";
      long timestamp = Calendar.getInstance().getTimeInMillis();

      MultipartFile file = request.getFile("imagefile");

      String file_name = file.getOriginalFilename();
      String file_type = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1, file.getContentType().length());

      byte[] bytes = file.getBytes();
      File Folder = new File(file_path + id);
      File lOutFile = new File(file_path + id + "/" + timestamp + "_" + file_name);

      if (!Folder.isDirectory()) {
        Folder.mkdirs();
      }

      if (lOutFile.isFile()) {
        if (lOutFile.delete()) {
          FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
          lFileOutputStream.write(bytes);
          lFileOutputStream.close();
          resultUrl = result_path + id + "/" + timestamp + "_" + file_name;
        }
      } else {
        FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
        lFileOutputStream.write(bytes);
        lFileOutputStream.close();
        resultUrl = result_path + id + "/" + timestamp + "_" + file_name;
      }

      if (type.equals("main") && event != null) {
        Notice _event = new Notice();
        _event.setId(event.getId());
        _event.setEventImgPath(resultUrl);
        noticeMapper.updateByPrimaryKeySelective(_event);
      }

      if (System.getProperty("os.name").contains("Linux")) {
        String chmod = "chmod 777 " + file_path + id + "/" + timestamp + "_" + file_name;

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

      resultMap.put("success", true);
      resultMap.put("url", resultUrl);
    } else {

    }

    return resultMap;
  }

  @RequestMapping("/BaseCodeMain")
  public Map<Object, Object> BaseCodeMain(
      @RequestParam(required = false) BaseMcode baseMcode,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException {

    Map<Object, Object> result = new HashMap<>();


    if (baseMcode == null && params.get("data") != null) {
      baseMcode = JsonConvert.JsonConvertObject(params.get("data").toString(), BaseMcode.class);
    }

    switch (method) {
      case GET:
        List<BaseMcode> list = new ArrayList<>();
        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : "");

        if (baseMcode == null || baseMcode.getMainCd() == null) {
          BaseMcodeExample example = new BaseMcodeExample();
          example.createCriteria().andTitleLike("%"+searchString+"%");
          example.or().andMainCdLike("%"+searchString+"%");
          example.or().andDscLike("%"+searchString+"%");
          
          list = baseCodeMainMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));
        } else {
          list.add(baseCodeMainMapper.selectByPrimaryKey(baseMcode.getMainCd()));
        }

        result.put("success", true);
        result.put("list", list);
        result.put("recordsTotal", baseCodeMainMapper.countByExample(null));
        result.put("recordsFiltered", baseCodeMainMapper.countByExample(null));

        break;
      case POST:
        result.put("success", baseCodeMainMapper.insertSelective(baseMcode) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      case PUT:
        result.put("success", baseCodeMainMapper.updateByPrimaryKeySelective(baseMcode) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      //    case DELETE :
      //      baseCodeMain = JsonConvert.JsonConvertObject(params.get("data").toString(), BaseCodeMain.class);
      //
      //      result.put("success", baseCodeMainMapper.deleteByPrimaryKey(baseCodeMain.getMain_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/BaseCode")
  public Map<Object, Object> BaseCode(
      @RequestParam(required = false) BaseBcode baseBcode,
      @RequestParam(required = false, value = "baseCd") String baseBcodeKey,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Map<Object, Object> result = new HashMap<>();

    if (baseBcode == null && params.get("data") != null) {
      baseBcode = JsonConvert.JsonConvertObject(params.get("data").toString(), BaseBcode.class);
    }

    switch (method) {
      case GET:
        List<BaseBcode> list = new ArrayList<>();
        BaseBcodeExample baseBcodeExample = new BaseBcodeExample();
        
        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : "");
        
        if (baseBcode != null && baseBcode.getMainCd() != null) {

          baseBcodeExample.createCriteria().andMainCdEqualTo(baseBcode.getMainCd()).andTitleLike("%"+searchString+"%");

          list.addAll(baseCodeMapper.selectByExampleWithRowbounds(baseBcodeExample, new RowBounds(start, length)));
          result.put("recordsTotal", baseCodeMapper.countByExample(baseBcodeExample));
          result.put("recordsFiltered", baseCodeMapper.countByExample(baseBcodeExample));
        } else if (baseBcodeKey != null) {
          list.add(baseCodeMapper.selectByPrimaryKey(baseBcodeKey));
        }

        result.put("success", true);
        result.put("list", list);

        break;
      case POST:
        baseBcode.setBaseCd(baseBcode.getMainCd() + baseBcode.getSubCd());

        result.put("success", baseCodeMapper.insertSelective(baseBcode) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      case PUT:
        baseBcode.setBaseCd(baseBcode.getMainCd() + baseBcode.getSubCd());

        result.put("success", baseCodeMapper.updateByPrimaryKeySelective(baseBcode) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/BackOfficeMenu")
  public Map<Object, Object> BackOfficeMenu(
	  HttpSession session,
	  @RequestParam(required = false) UserBackofficeMenu userBackOfficeMenu,
      @RequestParam(required = false) Map<Object, Object> params,
      HttpMethod method) throws Exception { 
	  
    Map<Object, Object> result = new HashMap<>();
    Map<Object, Object> userInfo = new HashMap<>();
    result = AdminHandler.setSessionInfo(result, session, "1");

    userInfo = (Map<Object, Object>) session.getAttribute("userInfo");
    
    if (userBackOfficeMenu == null && params.get("data") != null) {
    	userBackOfficeMenu = JsonConvert.JsonConvertObject(params.get("data").toString(), UserBackofficeMenu.class);
    }
    
    UserBackofficeMenuExample example = new UserBackofficeMenuExample();
    String upCd, subCd;
    int existsCount;

    switch (method) {
      case GET:

        if (userBackOfficeMenu == null) {
          // 권한별 메뉴 규성을 위해 수정 
          //List<UserBackofficeMenu> list = userBackofficeMenuMapper.selectByExample(null);
          List<UserBackofficeMenuList> list = new ArrayList<>();
          
          logger.debug("★★★★★★★★★★★★★★★★★★★★★★");
          logger.debug(userInfo.toString());
          logger.debug("★★★★★★★★★★★★★★★★★★★★★★");
          
//          if("1".equals(userInfo.get("userRole"))){
//        	  list = customManagementMapper.selectAllMenuList(userInfo.get("userId"));  
//          }else{
//        	  list = customManagementMapper.selectMenuList2(userInfo.get("userId"));
//          }
          
          list = customManagementMapper.selectAllMenuList(userInfo.get("userId"));
          
          List<UserBackofficeMenuList> treeMenu = new ArrayList<>();
          List<UserBackofficeMenuList> node = new ArrayList<>();

          for (UserBackofficeMenuList m : list) {
            if (m.getUpCd() == null || m.getUpCd().equals("")) {
              treeMenu.add(m);
            }
          }

          list.removeAll(treeMenu);

          for (UserBackofficeMenuList m : treeMenu) {
            for (UserBackofficeMenuList n : list) {
              if (m.getMenuCd().equals(n.getUpCd())) {
                node.add(n);
              }
            }
            if (node.size() > 0) {
              m.setNodes(node);
              node = new ArrayList<>();
            }
          }

          result.put("success", true);
          result.put("list", treeMenu);
        } else {        	
        	UserBackofficeMenu menu = userBackofficeMenuMapper.selectByPrimaryKey(userBackOfficeMenu.getId());

          if (menu != null) {
            result.put("success", true);
            result.put("menu", menu);
          }
        }
        break;
        
      case POST:
        upCd = userBackOfficeMenu.getUpCd();
        subCd = userBackOfficeMenu.getSubCd();
        if (upCd == null || upCd.length() == 0) {
          userBackOfficeMenu.setMenuCd(subCd);
          example.createCriteria().andMenuCdEqualTo(subCd);
        }
        else {
          example.createCriteria().andUpCdEqualTo(upCd).andSubCdEqualTo(subCd);
        }
        existsCount = userBackofficeMenuMapper.countByExample(example);
        if(existsCount > 0){
          result.put("success", false);
          result.put("errMsg", "Exist Same-Code("+ upCd + subCd + ")");
        }
        else {
          result.put("success", userBackofficeMenuMapper.insertSelective(userBackOfficeMenu) == 1 ? true : false);
          result.put("errMsg", "");
        }
        break;
        
      case PUT:
        upCd = userBackOfficeMenu.getUpCd();
        subCd = userBackOfficeMenu.getSubCd();
        if (upCd == null || upCd.length() == 0) {
          example.createCriteria().andMenuCdEqualTo(subCd).andIdNotEqualTo(userBackOfficeMenu.getId());
        }
        else {
          example.createCriteria().andUpCdEqualTo(upCd).andSubCdEqualTo(subCd).andIdNotEqualTo(userBackOfficeMenu.getId());
        }
        existsCount = userBackofficeMenuMapper.countByExample(example);
        if(existsCount > 0){
          result.put("success", false);
          result.put("errMsg", "Exist Same-Code("+ upCd + subCd + ")");
        }
        else {
          result.put("success", userBackofficeMenuMapper.updateByPrimaryKeySelective(userBackOfficeMenu) == 1 ? true : false);
          result.put("errMsg", "");
        }
        break;
        
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
        break;
    }

    return result;
  }

  @RequestMapping("/UserGroup")
  public Map<Object, Object> UserGroup(
      @RequestParam(required = false) UserGroup userGroup,
      @RequestParam(required = false) UserGroupKey userGroupKey,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, defaultValue = "") String limit,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException {

    Map<Object, Object> result = new HashMap<>(); 

    if (userGroup == null && params.get("data") != null) {
      userGroup = JsonConvert.JsonConvertObject(params.get("data").toString(), UserGroup.class);
    }

    if (userGroupKey == null && params.get("data") != null) {
      userGroupKey = JsonConvert.JsonConvertObject(params.get("data").toString(), UserGroupKey.class);
    }
    
	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
    logger.debug(" userGroup : " + userGroup);
    logger.debug(" userGroupKey : " + userGroupKey);
    logger.debug("★★★★★★★★★★★★★★★★★★★★★");

    switch (method) {
      case GET:
        List<UserGroup> list = new ArrayList<>();


        if (userGroup == null) {
          if (limit.equals("none")) {
            list = userGroupMapper.selectByExample(null);
          } else {
            list = userGroupMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));
          }
          result.put("recordsTotal", userGroupMapper.countByExample(null));
          result.put("recordsFiltered", userGroupMapper.countByExample(null));
        } else {
          list.add(userGroupMapper.selectByPrimaryKey(userGroupKey));
        }

        result.put("success", true);
        result.put("list", list);

        break;
      case POST:
        result.put("success", userGroupMapper.insertSelective(userGroup) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      case PUT:
        result.put("success", userGroupMapper.updateByPrimaryKeySelective(userGroup) == 1 ? true : false);
        result.put("errMsg", "");
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/UserGroupAuth")
  public Map<Object, Object> UserGroupAuth(
      @RequestParam(required = false) UserGroupAuth userGroupAuth,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Map<Object, Object> result = new HashMap<>();

	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
    logger.debug(" auth : " + auth); 
    logger.debug(" method : " + method); 
    logger.debug(" params : " + params); 
    logger.debug(" userGroupAuth : " + userGroupAuth); 
    logger.debug("★★★★★★★★★★★★★★★★★★★★★");
    
    switch (method) {
      case GET:

        if (userGroupAuth == null && params.get("data") != null) {
          userGroupAuth = JsonConvert.JsonConvertObject(params.get("data").toString(), UserGroupAuth.class);
        }

        List<UserGroupAuthProcedure> list = customManagementMapper.selectByProcedureUserGroupAuthList(userGroupAuth);
        
        
    	logger.debug("★★★★★★★★★★★★★★★★★★★★★");
        logger.debug(" list : " + list);  
        logger.debug("★★★★★★★★★★★★★★★★★★★★★");
        
        List<UserGroupAuthProcedure> treeMenu = new ArrayList<>();
        List<UserGroupAuthProcedure> node = new ArrayList<>();


        for (UserGroupAuthProcedure m : list) {
          if (m.getUpCd() == null || m.getUpCd().equals("")) {
            treeMenu.add(m);
          }
        }

        list.removeAll(treeMenu);

        for (UserGroupAuthProcedure m : treeMenu) {
          for (UserGroupAuthProcedure n : list) {
            if (m.getMenuCd().equals(n.getUpCd())) {
              node.add(n);
            }
          }
          if (node.size() > 0) {
            m.setNodes(node);
            node = new ArrayList<>();
          }
        }


        result.put("success", true);
        result.put("list", treeMenu);

        break;
      case POST:

        result.put("errMsg", "");
        break;
      case PUT:
        List<UserGroupAuth> _list = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<List<UserGroupAuth>>() {
        });

        if (_list != null) {

          for (UserGroupAuth uga : _list) {
            if (uga.getId() == null) {
              userGroupAuthMapper.insertSelective(uga);
            } else {
              userGroupAuthMapper.updateByPrimaryKeySelective(uga);
            }
          }

          result.put("errMsg", "");
        } else {
          result.put("success", false);
        }
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  @RequestMapping("/UserPermition")
  public Map<Object, Object> UserPermition(
      @RequestParam(required = false) UserPermition userPermition,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Map<Object, Object> result = new HashMap<>();
    
	logger.debug("★★★★★★★★★★★ /UserPermition ★★★★★★★★★★");
    logger.debug(" params : " + params.toString());  
    logger.debug("★★★★★★★★★★★★★★★★★★★★★");
    
    
    switch (method) {
      case GET:

        if (userPermition == null && params.get("data") != null) {
        	userPermition = JsonConvert.JsonConvertObject(params.get("data").toString(), UserPermition.class);
        }

        User user = JsonConvert.JsonConvertObject(params.get("data").toString(), User.class);
        
        user = userMapper.selectByPrimaryKey(user.getId());
        
        // System Admin/Store Admin 인 경우 메뉴 노출 목록 처리
        String whereMenuYn = "store";
        if (user.getType().equals("300003")) {
          whereMenuYn = "system";
        }
        
        List<UserGroupAuthProcedure> list = null;
        String role =  user.getUserRole();
        if(role == null || role.equals("")){  
        	role = "4"; 
        	whereMenuYn = "store";
        } 
        
//        if(customManagementMapper.getCountUserPermitionList(user.getId()) == 0){
//        	userPermition.setGroupId(Long.valueOf(role)); 
//        	userPermition.setUserId(Long.valueOf(user.getId()));
//        	list = customManagementMapper.selectByProcedureUserPermitionList(userPermition, whereMenuYn);
//        }else{
//        	userPermition.setGroupId(Long.valueOf(role));
//        	userPermition.setUserId(Long.valueOf(user.getId()));
//        	list = customManagementMapper.selectByProcedureUserPermitionList(userPermition, whereMenuYn);
//        }  
        
        if(customManagementMapper.getCountUserPermitionList(user.getId()) == 0){
        	userPermition.setGroupId(Long.valueOf(user.getUserRole())); 
        	userPermition.setUserId(Long.valueOf(user.getId()));
        	list = customManagementMapper.selectByProcedureUserPermitionList(userPermition, whereMenuYn);
        }else{
        	userPermition.setGroupId(Long.valueOf(user.getUserRole()));
        	userPermition.setUserId(Long.valueOf(user.getId()));
        	list = customManagementMapper.selectByProcedureUserPermitionList(userPermition, whereMenuYn);
        }        
        
    	logger.debug("★★★★★★★★★★★ /UserPermition ★★★★★★★★★★");
        logger.debug(" list : " + list.toString()); 
        logger.debug(" userPermition : " + userPermition.toString()); 
        logger.debug(" user : " + user.toString()); 
        logger.debug("★★★★★★★★★★★★★★★★★★★★★");
        
        List<UserGroupAuthProcedure> treeMenu = new ArrayList<>();
        List<UserGroupAuthProcedure> node = new ArrayList<>();

        for (UserGroupAuthProcedure m : list) {
          if (m.getUpCd() == null || m.getUpCd().equals("")) {
            treeMenu.add(m);
          }
        }

        list.removeAll(treeMenu);

        for (UserGroupAuthProcedure m : treeMenu) {
          for (UserGroupAuthProcedure n : list) {
            if (m.getMenuCd().equals(n.getUpCd())) {
              node.add(n);
            }
          }
          if (node.size() > 0) {
            m.setNodes(node);
            node = new ArrayList<>();
          }
        }

        result.put("success", true);
        result.put("list", treeMenu);

        break;
      case POST:

        result.put("errMsg", "");
        break;
      case PUT:    	  
    	List<UserPermition> _list = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<List<UserPermition>>() {});
        
        if (_list != null) {
          for (UserPermition uga : _list) {
            if (uga.getId() == null) {
            	userPermitionMapper.insertSelective(uga);
            } else {
            	userPermitionMapper.updateByPrimaryKeySelective(uga);
            }
          }
          result.put("success", true);
          result.put("errMsg", "");
        } else {
          result.put("success", false);
        }
        break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }

  
  /*
   * 사용자 목록
   * 
   * 접속사용자의 분류에 따라 권한 설정
   * 
   * 1. 시스템관리자 : 모든 사용자 관리 가능
   * 2. 에이전트 : 직원, 매장사용자
   * 3. 브랜드 관리자 : 직원, 매장사용자
   * 4. 매장 : 직원
   */
  @Transactional(rollbackFor={Exception.class})
  @RequestMapping("/User")
  public Map<Object, Object> User(
	  HttpSession session,
	  @RequestParam(required = false) User user,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws Exception {
	  
	     
    Map<Object, Object> result = new HashMap<>();
    
    result = AdminHandler.setSessionInfo(result, session, "1");
    
    if (user == null && params.get("data") != null) {
    	user = JsonConvert.JsonConvertObject(params.get("data").toString(), User.class);
    } 

    switch (method) {
      case GET:
        if (!String.valueOf(params.get("draw")).equals("null")) {
        	HashMap<String, Object> userInfo = AdminHandler.getUserInfo(session); 
        	
      	  logger.debug("★★★★★★★★★★★ /management/user ★★★★★★★★★★★ "); 
    	  logger.debug(" params : " + params);
    	  logger.debug(" userInfo : " + userInfo);
    	  logger.debug("★★★★★★★★★★★");
    	  
            if(params.get("data") == null && "300004".equals(userInfo.get("type"))){
        		params.put("franId", userInfo.get("franId"));
        		params.put("brandId", userInfo.get("brandId"));
        		params.put("storeId", userInfo.get("storeId"));
        		params.put("type", userInfo.get("type"));
        	}
            
            // 로그인 사용자가 Store Admin 일 때는 Store Admin, Store Manager 만 표시
            if ("300004".equals(userInfo.get("type"))) {
            	params.put("memberType", "store");
            } else {
            	params.put("memberType", "system");
            }

            /*
             * DATATABLE 검색 값 세팅
             */
            String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
            if (!searchString.isEmpty()) {
            	params.put("searchString", searchString);
            }
            
            /*
             * DATATABLE 컬럼 정렬값 세팅
             */
            if(params.get("order[0][column]") != null){
              String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
                  +" "+String.valueOf(params.get("order[0][dir]"));
              params.put("orderby",orderby);
            }
            
            List<LinkedHashMap<String, Object>> list = customManagementMapper.getUserList(params, new RowBounds(start, length));
	        result.put("list", list);
	        result.put("recordsTotal", customManagementMapper.getTotalCountUserList(params));
	        result.put("recordsFiltered", customManagementMapper.getCountUserList(params));
          
        } else {
        	params.put("id", user.getId());
        	result.put("list", customManagementMapper.getUserInfo(params)); 
        }

        result.put("success", true);

        break;
      case POST:
        user.setPlatformId(Globals.PICO_PLATFORM_ID);
        user.setBarcode(String.valueOf(System.currentTimeMillis()));
        
        //result.put("success", userMapper.insertSelective(user) == 1 ? true : false);
        UserExample userExample = new UserExample();
        UserExample.Criteria userCri = userExample.createCriteria();
        
        userCri.andUsernameEqualTo(user.getUsername());
        
        if(userMapper.countByExample(userExample) == 0){
        	
        	if(!("300004".equals(user.getType()) || "300006".equals(user.getType()))){
        		result.put("success", userMapper.insertSelective(user) == 1 ? true : false );
    			result.put("errMsg", "");
        	}else{
        		if(userMapper.insertSelective(user) == 1){

            		// 매핑테이블에 사용자 정보 세팅
            		SvcUserMapping mapping = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcUserMapping.class);
            		mapping.setUserId(user.getId());
            		
            		svcUserMappingMapper.insertSelective(mapping);
            		
            		user.setBarcode(String.valueOf(user.getId()));
            		
        			result.put("success", userMapper.updateByPrimaryKeySelective(user) == 1 ? true : false );
        			result.put("errMsg", "");
        		}
        	}
        }else{
        	result.put("success",false);
        	result.put("errMsg", "Duplicate ID");
        }
        
        break;
      case PUT:
    	  result.put("success",false);
    	  
    	  // 비밀번호값이 없을경우 null 을 세팅해준다    	  
    	  if(user.getPassword().isEmpty()){
  			user.setPassword(null);
  		  } 
      	
    	  logger.debug("★★★★★★★★★★★ /management/user ★★★★★★★★★★★ "); 
	  	  logger.debug(" params : " + params);
	  	  logger.debug(" user : " + user);
	  	  logger.debug("★★★★★★★★★★★");
	  	  
    	  if(!("300004".equals(user.getType()) || "300006".equals(user.getType()))){
    		  result.put("success", userMapper.updateByPrimaryKeySelective(user) == 1 ? true : false);
    	  }else{
    		  if(userMapper.updateByPrimaryKeySelective(user) > 0){
    			  	// 매핑테이블에 사용자 정보 세팅
					SvcUserMapping mapping = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcUserMapping.class);
					mapping.setUserId(user.getId());
					mapping.setId(null);
					
					SvcUserMappingExample test = new SvcUserMappingExample();
					
					test.createCriteria().andUserIdEqualTo(mapping.getUserId());
					result.put("success", customManagementMapper.updateUserMapping(mapping) == 1 ? true : false);
	    	  }
    	  }    	  
    	  
        result.put("errMsg", "");
        break;
      //    case DELETE :
      //      result.put("success", svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/Review")
  public Map<Object, Object> Review(
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object, Object> result = new HashMap<>();

    switch (method) {
      case GET:
        params.put("serviceId", Globals.PICO_SERVICE_ID);
        params.put("tenantId", Globals.PICO_TENANT_ID);

        if (!String.valueOf(params.get("draw")).equals("null")) {
          String searchString = (String) (params.get("searchKeyword") != null ? params.get("searchKeyword") : "");
          String beginSearch = (String) (params.get("beginSearch") != null ? params.get("beginSearch") : "");
          String endSearch = (String) (params.get("endSearch") != null ? params.get("endSearch") : "");

          //params.put("content", new String(searchString.getBytes("iso-8859-1"), "utf-8"));
          params.put("content", searchString);
          if (StringUtils.hasText(beginSearch)) params.put("begin", beginSearch);
          if (StringUtils.hasText(endSearch)) params.put("end", endSearch);
          
          if (params.get("order[0][column]") != null) {
            String orderby = String.valueOf(params.get("columns[" + String.valueOf(params.get("order[0][column]")) + "][name]"));
            String orderByDir = (String) (params.get("order[0][dir]") != null ? params.get("order[0][dir]") : "desc");
            params.put("orderByClause", orderby + " " + orderByDir + " , A.ID " + orderByDir);
          }

          List<LinkedHashMap<String, Object>> list = customManagementMapper.getReviewList(params, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", customManagementMapper.getCountReviewListTotal(params));
          result.put("recordsFiltered", customManagementMapper.getCountReviewList(params));
        }
        result.put("success", true);
        break;

      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }

  /**
   * Marketing – member
   * @param params
   * @param start
   * @param length
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @Transactional
  @RequestMapping("/Customer")
  public Map<Object,Object> Customer(
      Principal principal,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    try {
      logger.debug("customer : " + JsonConvert.toJson(params));
      Map<Object,Object> result = Maps.newHashMap();
      result.put("success", false);
  
      CustomManagementMapper customManagementMapper = sessionTemplate.getMapper(CustomManagementMapper.class);
      SvcStoreUserMapper storeUserMapper = sessionTemplate.getMapper(SvcStoreUserMapper.class);
      SvcStoreUserExample storeUserExample = new SvcStoreUserExample();
  
      List<LinkedHashMap<String,Object>> userList = Lists.newArrayList();
      int recordsTotal = 0;
      int recordsFiltered = 0;
      
      switch (method) {
      case GET:
        if(!String.valueOf(params.get("draw")).equals("null")){
          if (params.get("franId") != null && params.get("brandId") != null && params.get("storeId") != null) {
            int count = customManagementMapper.getCountCustomerList(params);
            if (count > 0) {
              recordsTotal = count;
              recordsFiltered = count;
              
              List<String> userType = Arrays.asList(codeUtil.getBaseCodeByAlias("user"), codeUtil.getBaseCodeByAlias("associate-user"));
              params.put("types", userType);
              userList = customManagementMapper.getCustomerList(params);
            }
          }
        } else {
          if(params.get("userId") != null){
            userList = customManagementMapper.getCustomerList(params);
            
            if(userList.size() == 1){
              result.put("data", userList.get(0));
            } else {
              result.put("errMsg", "사용자를 찾을수 없습니다.");
            }
          }
        }
        result.put("list", userList);
        result.put("success", true);
        result.put("recordsTotal", recordsTotal);
        result.put("recordsFiltered", recordsFiltered);
        
        logger.debug("customer : " + method + ",  data : " + JsonConvert.toJson(result));
        break;
      case PUT:
          logger.debug("★★★★★★★★★★★★★★");
          logger.debug("PUT");
          logger.debug(params.toString());
          logger.debug("★★★★★★★★★★★★★★");
          
          Map<String, Object> data = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, Object>>() {});
          
          logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
          logger.debug("data : " + JsonConvert.toJson(data));
          logger.debug(data.get("userId").getClass().toString());
          logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
          
          storeUserExample.createCriteria()
            .andUserIdEqualTo(Long.parseLong((String) data.get("userId")))
            .andBrandIdEqualTo(Long.parseLong((String) data.get("brandId")))
            .andStoreIdEqualTo(Long.parseLong((String) data.get("storeId")));
          List<SvcStoreUser> storeUsers = storeUserMapper.selectByExample(storeUserExample);
          
          if (storeUsers!= null && storeUsers.size() > 0) {
            SvcStoreUser storeUser = storeUsers.get(0);
            storeUser.setLevel((String) data.get("membership"));
            storeUserMapper.updateByPrimaryKeySelective(storeUser);
          }
          
          result.put("success", true);
        break;
      default:
        break;
      }
      return result;
    } catch (Exception e) {
      logger.info("[ERROR] [AdminManagementController] [/Customer] [param : " + params + "]");
      e.printStackTrace();
      throw e;
    }
  }
  
  @RequestMapping("/AppVersion")
  public Map<Object, Object> AppVersion(
      @RequestParam(required = false) SvcApp apps,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object, Object> result = new HashMap<>();

    if (apps == null && params.get("data") != null) {
      apps = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcApp.class);
    }

    switch (method) {
      case GET:
        params.put("serviceId", Globals.PICO_SERVICE_ID);
        //params.put("tenantId", Globals.PICO_TENANT_ID);

        if (!String.valueOf(params.get("draw")).equals("null")) {
          //params.put("appType", codeUtil.getBaseCodeByAlias("AppType"));
          //params.put("osType", codeUtil.getBaseCodeByAlias("MobileOSType"));

          params.put("useYn", true);

          params.put("appType", (String) (params.get("appType") != null ? params.get("appType") : ""));
          params.put("osType", (String) (params.get("osType") != null ? params.get("osType") : ""));

          String searchString = (String) (params.get("searchKeyword") != null ? params.get("searchKeyword") : "");

          //params.put("content", new String(searchString.getBytes("iso-8859-1"), "utf-8"));
          params.put("content", searchString);

          if (params.get("order[0][column]") != null) {
            String orderby = String.valueOf(params.get("columns[" + String.valueOf(params.get("order[0][column]")) + "][name]"));
            String orderByDir = (String) (params.get("order[0][dir]") != null ? params.get("order[0][dir]") : "desc");
            params.put("orderByClause", orderby + " " + orderByDir + " , ID " + orderByDir);
          }

          List<LinkedHashMap<String, Object>> list = customManagementMapper.getAppVersionList(params, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", customManagementMapper.getCountAppVersionListTotal(params));
          result.put("recordsFiltered", customManagementMapper.getCountAppVersionList(params));
        }
        else {
          result.put("list", svcAppMapper.selectByPrimaryKey(apps.getId()));
        }
        result.put("success", true);
        break;
        
      case POST:
        apps.setServiceId(Globals.PICO_SERVICE_ID);
        apps.setUseYn(true);
        result.put("success", svcAppMapper.insertSelective(apps) == 1 ? true : false);
        result.put("errMsg", "");
        break;
        
      case PUT:
        result.put("success", svcAppMapper.updateByPrimaryKeySelective(apps) == 1 ? true : false);
        result.put("errMsg", "");
        break;

      case DELETE:
        apps.setUseYn(false);
        result.put("success", svcAppMapper.updateByPrimaryKeySelective(apps) == 1 ? true : false);
        result.put("errMsg", "");
        break;

      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }
  
  @RequestMapping("/AppVersionList")
  public Map<Object, Object> AppVersionList(
      @RequestParam(required = false) SvcApp apps,
      @RequestParam(required = false) Map<Object, Object> params,
      @RequestParam(required = false, value = "start", defaultValue = "0") int start,
      @RequestParam(required = false, value = "length", defaultValue = "10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException {

    Map<Object, Object> result = new HashMap<>();

    if (apps == null && params.get("data") != null) {
      apps = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcApp.class);
    }

    switch (method) {
      case GET:
        params.put("serviceId", Globals.PICO_SERVICE_ID);

        if (!String.valueOf(params.get("draw")).equals("null")) {
          params.put("useYn", true);

          String appType = apps.getAppTp();
          String osType = apps.getOsTp();
//          if (appType == "" || osType == "") {
//            appType = "NONE";
//            osType = "NONE";
//          }
          params.put("appType", appType);
          params.put("osType", osType);

          params.put("orderByClause", " VERSION DESC, ID DESC ");

          List<LinkedHashMap<String, Object>> list = customManagementMapper.getAppVersionList(params, new RowBounds(start, length));

          result.put("list", list);
          result.put("recordsTotal", customManagementMapper.getCountAppVersionList(params));
          result.put("recordsFiltered", customManagementMapper.getCountAppVersionList(params));
        }
        else {
          result.put("list", svcAppMapper.selectByPrimaryKey(apps.getId()));
        }
        result.put("success", true);
        break;

      default:
        result.put("success", false);
        result.put("errMsg", "not supported method.");
    }

    return result;
  }
}
