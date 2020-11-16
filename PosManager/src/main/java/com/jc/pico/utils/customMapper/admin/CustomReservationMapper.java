/*
 * Filename	: CustomReservationMapper.java
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

import org.apache.ibatis.session.RowBounds;


public interface CustomReservationMapper {
  // 예약발송건 push
  int getPushReservationUserListCnt(Map<Object,Object> paramMap);
  
  // Sales Goal 매출목표관리 조회
  List<LinkedHashMap<String, Object>> getPushReservationUserList(Map<Object,Object> paramMap);
 
}
