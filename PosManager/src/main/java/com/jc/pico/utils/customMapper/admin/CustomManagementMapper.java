/*
 * Filename	: CustomManagementMapper.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.customMapper.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.jc.pico.bean.SvcUserMapping;
import com.jc.pico.bean.SvcUserMappingExample;
import com.jc.pico.bean.UserBackofficeMenuExample;
import com.jc.pico.bean.UserGroupAuth;
import com.jc.pico.bean.UserPermition;
import com.jc.pico.utils.bean.UserBackofficeMenuList;
import com.jc.pico.utils.bean.UserGroupAuthProcedure;

public interface CustomManagementMapper {
  
  List<LinkedHashMap<String, Object>> getCustomerList(Map<Object,Object> paramMap);
  int getCountCustomerList(Map<Object,Object> paramMap);
  
  List<LinkedHashMap<String, Object>> getEventList(Map<Object,Object> paramMap, RowBounds rowBounds);
  int getCountEventListTotal(Map<Object,Object> paramMap);
  int getCountEventList(Map<Object,Object> paramMap);

  List<LinkedHashMap<String, Object>> getReviewList(Map<Object,Object> paramMap, RowBounds rowBounds);
  int getCountReviewListTotal(Map<Object,Object> paramMap);
  int getCountReviewList(Map<Object,Object> paramMap);
  
  List<LinkedHashMap<String, Object>> getAppVersionList(Map<Object,Object> paramMap, RowBounds rowBounds);
  int getCountAppVersionListTotal(Map<Object,Object> paramMap);
  int getCountAppVersionList(Map<Object,Object> paramMap);

  // admin 관리자일경우 제약없이 젠체 메뉴를 제어한다
  List<UserBackofficeMenuList> selectAllMenuList(Object object);
 
  List<UserBackofficeMenuList> selectMenuList(UserBackofficeMenuExample example);
  List<UserBackofficeMenuList> selectMenuList2(Object object);
  
  List<LinkedHashMap<String, Object>> getUserList(Map<Object,Object> paramMap, RowBounds rowBounds);
  LinkedHashMap<String, Object> getUserInfo(Map<Object,Object> paramMap);
  int getTotalCountUserList(Map<Object,Object> paramMap);
  int getCountUserList(Map<Object,Object> paramMap);
  
  List<UserGroupAuthProcedure> selectByProcedureUserGroupAuthList(@Param("record") UserGroupAuth record);
  List<UserGroupAuthProcedure> selectByProcedureUserPermitionList(@Param("record") UserPermition record, @Param("whereMenuYn") String whereMenuYn);
  List<UserGroupAuthProcedure> selectUserPermitionList(UserPermition userPermition);
  int getCountUserPermitionList(Long userId);
  
  int updateUserMapping(@Param("record") SvcUserMapping record);  
  
}
