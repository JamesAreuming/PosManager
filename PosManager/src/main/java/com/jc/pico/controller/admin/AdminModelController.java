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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.jc.pico.bean.BaseBcode;
import com.jc.pico.bean.BaseBcodeExample;
import com.jc.pico.bean.BaseMcode;
import com.jc.pico.bean.BaseMcodeExample;
import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcBrandExample;
import com.jc.pico.bean.SvcCoupon;
import com.jc.pico.bean.SvcCouponExample;
import com.jc.pico.bean.SvcCouponWithBLOBs;
import com.jc.pico.bean.SvcFranchise;
import com.jc.pico.bean.SvcFranchiseExample;
import com.jc.pico.bean.SvcStamp;
import com.jc.pico.bean.SvcStampExample;
import com.jc.pico.bean.SvcStampWithBLOBs;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.SvcStoreExample;
import com.jc.pico.bean.SvcTerms;
import com.jc.pico.bean.SvcTermsExample;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserBackofficeMenu;
import com.jc.pico.bean.UserExample;
import com.jc.pico.bean.UserGroup;
import com.jc.pico.bean.UserGroupAuth;
import com.jc.pico.bean.UserGroupKey;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.mapper.BaseBcodeMapper;
import com.jc.pico.mapper.BaseMcodeMapper;
import com.jc.pico.mapper.SvcBeaconMapper;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcBrandSetMapper;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcFranchiseMapper;
import com.jc.pico.mapper.SvcStampMapper;
import com.jc.pico.mapper.SvcStoreMapper;
import com.jc.pico.mapper.SvcTermsMapper;
import com.jc.pico.mapper.SvcUserMappingMapper;
import com.jc.pico.mapper.UserBackofficeMenuMapper;
import com.jc.pico.mapper.UserGroupAuthMapper;
import com.jc.pico.mapper.UserGroupMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.utils.AdminHandler;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.UserBackofficeMenuList;
import com.jc.pico.utils.bean.UserGroupAuthProcedure;
import com.jc.pico.utils.customMapper.admin.CustomManagementMapper;
import com.jc.pico.utils.customMapper.admin.CustomRewardMapper;

@Controller
@ResponseBody
@RequestMapping("/admin/model")
public class AdminModelController {

  protected static Logger logger = LoggerFactory.getLogger(AdminModelController.class);

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
  BaseMcodeMapper baseMcodeMapper;

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
  SvcBeaconMapper svcBeaconMapper;

  @Autowired
  CustomRewardMapper customRewardMapper;
  
  @Autowired
  CustomManagementMapper customManagementMapper;

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Autowired
  private HttpSession session;


  @RequestMapping(value="/Codes", method=RequestMethod.POST)
  public Map<String,Object> getBaseCode(
      @RequestParam("codes") String Codes,
      @RequestParam Map<Object,Object> params){

    Map<String,Object> returnMap = new HashMap<>();
    Map<String,List<BaseBcode>> codeMap = new HashMap<>();
    BaseBcodeExample baseBcodeExample = new BaseBcodeExample();
    BaseMcodeExample baseMcodeExample = new BaseMcodeExample();

    String [] codes = Codes.split(",");

    for(String code : codes){

      baseMcodeExample.clear();
      baseMcodeExample.createCriteria().andAliasEqualTo(code);

      List<BaseMcode> baseMcode = baseMcodeMapper.selectByExample(baseMcodeExample);

      if(baseMcode.size() == 1){

        baseBcodeExample.clear();
        baseBcodeExample.createCriteria().andMainCdEqualTo(baseMcode.get(0).getMainCd()).andUseYnEqualTo(true);

        codeMap.put(code.toLowerCase(), baseCodeMapper.selectByExample(baseBcodeExample));
      }
    }

    if(codeMap.size() == codes.length){
      returnMap.put("success", true);
      returnMap.put("codes", codeMap);
    } else {
      returnMap.put("success", false);
    }

    return returnMap;
  }

  @RequestMapping("/Corp")
  public Map<Object,Object> Corp(
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

  @RequestMapping("/Terms")
  public Map<Object,Object> Terms(
      @RequestParam(required=false) SvcTerms terms,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException{
    Map<Object,Object> result = new HashMap<>();


    if(terms == null && params.get("data") != null){
      terms = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcTerms.class);
    }

    switch(method){
    case GET :
      if(!String.valueOf(params.get("draw")).equals("null")){

        SvcTermsExample example = new SvcTermsExample();

        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : "");

        example.createCriteria().andTitleLike("%"+searchString+"%");

        List<SvcTerms> list = svcTermsMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", svcTermsMapper.countByExample(null));
        result.put("recordsFiltered", svcTermsMapper.countByExample(example));
      } else {
        result.put("list", svcTermsMapper.selectByPrimaryKey(terms.getId()));
      }

      result.put("success", true);

      break;
    case POST :
      result.put("success", svcTermsMapper.insertSelective(terms) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcTermsMapper.updateByPrimaryKeySelective(terms) == 1 ? true : false );
      result.put("errMsg", "");
      System.out.println(result.get("success"));
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

  @RequestMapping("/Stamp")
  public Map<Object,Object> Stamp(
      @RequestParam(required=false) SvcStampWithBLOBs store,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
    Map<Object,Object> result = new HashMap<>();


    if(store == null && params.get("data") != null){
      store = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcStampWithBLOBs.class);
    }

    switch(method){
    case GET :

      SvcStampExample example = new SvcStampExample();

      example.createCriteria().andBrandIdEqualTo(store.getBrandId() != null ? store.getBrandId() : -1);

      List<SvcStamp> stamp = svcStampMapper.selectByExample(example);

      if(stamp.size() > 0){
        result.put("data", stamp.get(0));
        result.put("success", true);
      } else {
        result.put("success", false);
      }

      break;
    case POST :
      result.put("success", svcStampMapper.insertSelective(store) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", svcStampMapper.updateByPrimaryKeySelective(store) == 1 ? true : false );
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

  @RequestMapping("/BaseCodeMain")
  public Map<Object,Object> BaseCodeMain(
      @RequestParam(required=false) BaseMcode baseMcode,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {

    Map<Object,Object> result = new HashMap<>();


    if(baseMcode == null && params.get("data") != null){
      baseMcode = JsonConvert.JsonConvertObject(params.get("data").toString(), BaseMcode.class);
    }

    switch(method){
    case GET :
      List<BaseMcode> list = new ArrayList<>();

      if(baseMcode == null || baseMcode.getMainCd() == null){
        list = baseMcodeMapper.selectByExampleWithRowbounds(null, new RowBounds(start, length));
      } else {
        list.add(baseMcodeMapper.selectByPrimaryKey(baseMcode.getMainCd()));
      }

      result.put("success", true);
      result.put("list", list);
      result.put("recordsTotal", baseMcodeMapper.countByExample(null));
      result.put("recordsFiltered", baseMcodeMapper.countByExample(null));

      break;
    case POST :
      result.put("success", baseMcodeMapper.insertSelective(baseMcode) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", baseMcodeMapper.updateByPrimaryKeySelective(baseMcode) == 1 ? true : false );
      result.put("errMsg", "");
      break;
      //    case DELETE :
      //      baseCodeMain = JsonConvert.JsonConvertObject(params.get("data").toString(), BaseCodeMain.class);
      //
      //      result.put("success", baseMcodeMapper.deleteByPrimaryKey(baseCodeMain.getMain_cd()) == 1 ? true : false );
      //      result.put("errMsg", "");
      //      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/BaseCode")
  public Map<Object,Object> BaseCode(
      @RequestParam(required=false) BaseBcode baseBcode,
      @RequestParam(required=false, value="baseCd") String baseBcodeKey,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Map<Object,Object> result = new HashMap<>();

    if(baseBcode == null && params.get("data") != null){
      baseBcode = JsonConvert.JsonConvertObject(params.get("data").toString(), BaseBcode.class);
    }


    switch(method){
    case GET :
      List<BaseBcode> list = new ArrayList<>();
      BaseBcodeExample baseBcodeExample = new BaseBcodeExample();
      if(baseBcode != null && baseBcode.getMainCd() != null){

        baseBcodeExample.createCriteria().andMainCdEqualTo(baseBcode.getMainCd());

        list.addAll(baseCodeMapper.selectByExampleWithRowbounds(baseBcodeExample, new RowBounds(start, length)));
        result.put("recordsTotal", baseCodeMapper.countByExample(baseBcodeExample));
        result.put("recordsFiltered", baseCodeMapper.countByExample(baseBcodeExample));
      } else if (baseBcodeKey != null) {
        list.add(baseCodeMapper.selectByPrimaryKey(baseBcodeKey));
      }

      result.put("success", true);
      result.put("list", list);

      break;
    case POST :
      baseBcode.setBaseCd(baseBcode.getMainCd()+baseBcode.getSubCd());

      result.put("success", baseCodeMapper.insertSelective(baseBcode) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      baseBcode.setBaseCd(baseBcode.getMainCd()+baseBcode.getSubCd());

      result.put("success", baseCodeMapper.updateByPrimaryKeySelective(baseBcode) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/BackOfficeMenu")
  public Map<Object,Object> BackOfficeMenu(
      @RequestParam(required=false) UserBackofficeMenu userBackOfficeMenu,
      @RequestParam(required=false) Map<Object,Object> params,
      HttpMethod method) throws InvalidJsonException {

    Map<Object,Object> result = new HashMap<>();

    if(userBackOfficeMenu == null && params.get("data") != null){
      userBackOfficeMenu = JsonConvert.JsonConvertObject(params.get("data").toString(), UserBackofficeMenu.class);
    }
    
    Map<Object, Object> userInfo = (Map<Object, Object>) session.getAttribute("userInfo");
    
    switch(method){
    case GET :
        if (userBackOfficeMenu == null) {

            
            logger.debug("★★★★★★★★★★★★★★★★★★★★★★");
            logger.debug(userInfo.toString());
            logger.debug("★★★★★★★★★★★★★★★★★★★★★★");
            
          // 권한별 메뉴 규성을 위해 수정 
          //List<UserBackofficeMenu> list = userBackofficeMenuMapper.selectByExample(null);
          List<UserBackofficeMenuList> list = new ArrayList<>(); 
          list = customManagementMapper.selectMenuList2(userInfo.get("userId")); 
          
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
    case POST :
      result.put("success", userBackofficeMenuMapper.insertSelective(userBackOfficeMenu) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", userBackofficeMenuMapper.updateByPrimaryKeySelective(userBackOfficeMenu) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  @RequestMapping("/UserGroup")
  public Map<Object,Object> UserGroup(
      @RequestParam(required=false) UserGroup userGroup,
      @RequestParam(required=false) UserGroupKey userGroupKey,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, defaultValue="") String limit,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException{

    Map<Object,Object> result = new HashMap<>();

    if(userGroup == null && params.get("data") != null){
      userGroup = JsonConvert.JsonConvertObject(params.get("data").toString(), UserGroup.class);
    }

    if(userGroupKey == null && params.get("data") != null){
      userGroupKey = JsonConvert.JsonConvertObject(params.get("data").toString(), UserGroupKey.class);
    }

    switch(method){
    case GET :
      List<UserGroup> list = new ArrayList<>();


      if(userGroup == null){
        if(limit.equals("none")){
          list = userGroupMapper.selectByExample(null);
        } else{
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
    case POST :
      result.put("success", userGroupMapper.insertSelective(userGroup) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :
      result.put("success", userGroupMapper.updateByPrimaryKeySelective(userGroup) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }


  @RequestMapping("/UserGroupAuth")
  public Map<Object,Object> UserGroupAuth(
      @RequestParam(required=false) UserGroupAuth userGroupAuth,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException{

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Map<Object,Object> result = new HashMap<>();


    switch(method){
    case GET :

      if(userGroupAuth == null && params.get("data") != null){
        userGroupAuth = JsonConvert.JsonConvertObject(params.get("data").toString(), UserGroupAuth.class);
      }
/*
      List<UserGroupAuthProcedure> list = customMapper.selectByProcedureUserGroupAuthList(userGroupAuth);

      List<UserGroupAuthProcedure> treeMenu = new ArrayList<>();
      List<UserGroupAuthProcedure> node = new ArrayList<>();


      for(UserGroupAuthProcedure m : list){
        if(m.getUpCd() == null || m.getUpCd().equals("")){
          treeMenu.add(m);
        }
      }

      list.removeAll(treeMenu);

      for(UserGroupAuthProcedure m : treeMenu){
        for(UserGroupAuthProcedure n : list){
          if(m.getMenuCd().equals(n.getUpCd())){
            node.add(n);
          }
        }
        if(node.size() > 0){
          m.setNodes(node);
          node = new ArrayList<>();
        }
      }
*/

      result.put("success", true);
      //result.put("list", treeMenu);

      break;
    case POST :

      result.put("errMsg", "");
      break;
    case PUT :
      List<UserGroupAuth> _list = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<List<UserGroupAuth>>() {});

      if(_list != null){

        for(UserGroupAuth uga : _list){
          if(uga.getId() == null){
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
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

  @RequestMapping("/User")
  public Map<Object,Object> User(
      @RequestParam(required=false) User user,
      @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws InvalidJsonException{

    Map<Object,Object> result = new HashMap<>();

    if(user == null && params.get("data") != null){
      user = JsonConvert.JsonConvertObject(params.get("data").toString(), User.class);
    }

    switch(method){
    case GET :
      if(user == null){

        UserExample example = new UserExample();

        example.createCriteria().andTypeEqualTo("300003");

        List<User> list = userMapper.selectByExampleWithRowbounds(example, new RowBounds(start, length));

        result.put("list", list);
        result.put("recordsTotal", userMapper.countByExample(example));
        result.put("recordsFiltered", userMapper.countByExample(example));
      } else {
        result.put("list", userMapper.selectByPrimaryKey(user.getId()));
      }

      result.put("success", true);

      break;
    case POST :
      user.setPlatformId(Globals.PICO_PLATFORM_ID);;
      user.setType("300003");
      user.setStatus("");

      result.put("success", userMapper.insertSelective(user) == 1 ? true : false );
      result.put("errMsg", "");
      break;
    case PUT :

      user.setMbBk("1234");
      result.put("success", userMapper.updateByPrimaryKeySelective(user) == 1 ? true : false );
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
  
  @RequestMapping("/UserStatus")
  public Map<Object,Object> UserStatus(HttpSession session) throws Exception{
    UserMapper userMapper = sessionTemplate.getMapper(UserMapper.class);
    SvcUserMappingMapper userMappingMapper = sessionTemplate.getMapper(SvcUserMappingMapper.class);

    Map<Object,Object> result = Maps.newHashMap();

    result = AdminHandler.setSessionInfo(result, session, "1");
    

    

    result.put("success", true);
    return result;
  }
  
  

  /**
   * Brand selectBox list
   * @param brand
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   */
    @RequestMapping("/Franchise")
    public Map<Object,Object> Franchise(@RequestParam(required=false) SvcFranchise franchise, @RequestParam(required=false) Map<Object,Object> params, HttpMethod method) throws InvalidJsonException {
      Map<Object,Object> result = new HashMap<>();
      SvcFranchiseExample franchiseExample = new SvcFranchiseExample();

      if (franchise == null && params.get("data") != null) {
    	  franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
      }
     // franchiseExample.createCriteria().andIdEqualTo(franchise.getId() != null ? franchise.getId() : -1);

  	result.put("list", svcFranchiseMapper.selectByExample(franchiseExample));
      result.put("success", true);

      return result;
    }
    
/**
 * Brand selectBox list
 * @param brand
 * @param params
 * @param method
 * @return
 * @throws InvalidJsonException
 */
  @RequestMapping("/Brand")
  public Map<Object,Object> Brand(@RequestParam(required=false) SvcBrand brand, @RequestParam(required=false) Map<Object,Object> params, HttpMethod method) throws InvalidJsonException {
    Map<Object,Object> result = new HashMap<>();
    SvcBrandExample brandExample = new SvcBrandExample();

    if (brand == null && params.get("data") != null) {
    	brand = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcBrand.class);
    }
    brandExample.createCriteria().andFranIdEqualTo(brand.getFranId() != null ? brand.getFranId() : -1);

	result.put("list", svcBrandMapper.selectByExample(brandExample));
    result.put("success", true);

    return result;
  }

  /**
   * Store selectBox list
   * @param store
   * @param params
   * @param method
   * @return
   * @throws InvalidJsonException
   */
  @RequestMapping("/Store")
  public Map<Object,Object> Store(@RequestParam(required=false) SvcStore store, @RequestParam(required=false) Map<Object,Object> params, HttpMethod method) throws InvalidJsonException {
	  Map<Object,Object> result = new HashMap<>();
	  SvcStoreExample storeExample = new SvcStoreExample();

	  if (store == null && params.get("data") != null) {
		  store = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcStore.class);
	  }
	  storeExample.createCriteria().andBrandIdEqualTo(store.getBrandId() != null ? store.getBrandId() : -1);

	  result.put("list", svcStoreMapper.selectByExample(storeExample));
	  result.put("success", true);

	  return result;
  }

 /* @RequestMapping(value = "/ExcelUpload", method = RequestMethod.POST)
  public Map<Object,Object> excelUpload(@RequestParam(required=false) Map<Object,Object> params, MultipartHttpServletRequest req) throws ParseException {
    Map<Object,Object> result = new HashMap<>();

  	MultipartFile file = req.getFile("excel");

  	//ArrayList<CompanyDetailVO> comp_list = new ArrayList<>();
  	if (file != null && file.getSize() > 0) {
  		try {
  			Workbook wb = new HSSFWorkbook(file.getInputStream());
  			Sheet sheet = wb.getSheetAt(0);

  			int last = sheet.getLastRowNum();
  			System.out.println("Last : " + last);
  			for (int i=1; i<=last; i++) {
  				Row row = sheet.getRow(i);
  				SvcBeacon beacon = new SvcBeacon();

  				String uuId = row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

  				if(!uuId.equals("") && uuId != null){
  					//int ss = Integer.parseInt(row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
  					Short major = Short.parseShort(row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
  					Short minor = Short.parseShort(row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());

  					beacon.setServiceId(Globals.PICO_SERVICE_ID);
  					beacon.setTenantId(Globals.PICO_TENANT_ID);
  					beacon.setUuid(uuId);
  					beacon.setMajor(major);
  					beacon.setMinor(minor);
  					beacon.setLabel(row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());

  					result.put("success", svcBeaconMapper.insertSelective(beacon) == 1 ? true : false );
  					//comp_list.add(company);
  				}
  			}
  		}
  		catch (IllegalStateException | IOException e) {
  			result.put("success", false);
  			e.printStackTrace();
  		}
  		//result.put("errMsg", "");
  	}
  	//service.insertCompExcel(comp_list);

  	return result;
  }*/


}
