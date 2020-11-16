/*
 * Filename	: OrderUtil.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.module;

import com.jc.pico.bean.*;
import com.jc.pico.mapper.*;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.bean.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class OrderProcessor {
  protected static Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

  @Autowired SvcStoreMapper svcStoreMapper;
  @Autowired SvcOrderMapper svcOrderMapper;
  @Autowired SvcOrderItemMapper svcOrderItemMapper;
  @Autowired SvcOrderItemOptMapper svcOrderItemOptMapper;
  @Autowired SvcOrderPayMapper svcOrderPayMapper;
  @Autowired SvcOrderDiscountMapper svcOrderDiscountMapper;

  @Autowired CodeUtil codeUtil;


  @Transactional
  public void save(Order order) {
  }

  @Transactional
  public void cancel(Order order) {
  }

  /**
   * 서버의 Default TimeZone로 파싱된 Date를 특정(to) TimeZone으로 변경 합니다.
   * (Date는 TimeZone 정보를 갖고 있지 않음.)
   * @param date
   * @param to
   * @return
   */
  public static Date toTimeZone(Date date, TimeZone to) {
    return toTimeZone(date, TimeZone.getDefault(), to);
  }

  /**
   * "from" TimeZone로 파싱된 Date를 특정(to) TimeZone으로 변경 합니다.
   * (Date는 TimeZone 정보를 갖고 있지 않음.)
   * @param src
   * @param to
   * @return
   */
  public static Date toTimeZone(Date src, TimeZone from, TimeZone to) {
    int fromOffset = from.getOffset(src.getTime());
    int toOffset = to.getOffset(src.getTime());

    Date utc = new Date(src.getTime() - fromOffset);
    Date converted = new Date(utc.getTime() + toOffset);

    return converted;
  }

  public static void main(String[] args) throws Exception {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    TimeZone from = TimeZone.getDefault();
    //TimeZone from = TimeZone.getTimeZone("America/Los_Angeles");
    TimeZone to = TimeZone.getTimeZone("America/Los_Angeles");

    Date src = new Date();
    src = format.parse("2016-08-02 04:10:00");


    Date converted = toTimeZone(src, to);
    System.out.println("src       : " + format.format(src));
    System.out.println("converted : " + format.format(converted));
  }
}
