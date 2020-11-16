/**
 * <pre>
 * Filename : DateUtil.java
 * Function : 
 * Comment  :
 * History  : 
 *
 * Version  : 1.0
 * Author   :
 * </pre>
 */
package com.jc.pico.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jc.pico.controller.app.PublicController;

public class DateUtil {

  protected static Logger logger = LoggerFactory.getLogger(DateUtil.class);
  
  
  public static Date toTimeZone(Date date, TimeZone to) {
    return toTimeZone(date, to, false);
  }
  
  /**
   * 서버의 Default TimeZone로 파싱된 Date를 특정(to) TimeZone으로 변경 합니다.
   * (Date는 TimeZone 정보를 갖고 있지 않음.)
   * @param date
   * @param to 
   * @return
   */
  public static Date toTimeZone(Date date, TimeZone to, boolean isUTC) {
    return toTimeZone(date, TimeZone.getDefault(), to, isUTC);
  }

  /**
   * "from" TimeZone로 파싱된 Date를 특정(to) TimeZone으로 변경 합니다.
   * (Date는 TimeZone 정보를 갖고 있지 않음.)
   * @param src
   * @param to
   * @return
   */
  public static Date toTimeZone(Date src, TimeZone from, TimeZone to, boolean isUTC) {
	  if(src == null) {
		  return null; // FIXME HYO. 당장에 주문이 들어가지 않아 null 처리 합니다. 호출 단에서 상황에 맞게 처리후 제거하세요.
	  }
    int fromOffset = from.getOffset(src.getTime());
    int toOffset = to.getOffset(src.getTime());
    
    Date utc = new Date(src.getTime() - fromOffset);
    Date local = new Date(utc.getTime() + toOffset);

    if (isUTC) {
      return utc;
    } else {
      return local;
    }
  }
  

}
