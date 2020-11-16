/*
 * Filename	: SmsSender.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   :
 */

package com.jc.pico.queue;

import com.jc.pico.bean.SvcSmsLogWithBLOBs;
import com.jc.pico.mapper.SvcSmsLogMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.SendSms;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeoutException;

@Component
public class SmsSender extends DefaultRabbitClient {
  protected static Logger logger = LoggerFactory.getLogger(SmsSender.class);

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Override
  public void send(String queueName, String message) throws IOException, TimeoutException {
    super.send(queueName, message);
  }

  public void send(final SendSms info) {
    logger.info(String.format("send SMS(start) : %s", info.getTo()));
    SvcSmsLogMapper mapper = sessionTemplate.getMapper(SvcSmsLogMapper.class);
    SvcSmsLogWithBLOBs log = new SvcSmsLogWithBLOBs();
    long id = 0;
    try {
      // save log(before send)
      log.setBrandId(info.getBrandId());
      log.setFrom(info.getFrom());
      log.setTo(info.getTo());
      log.setTitle(info.getSubject());
      log.setContent(info.getMessage());
      log.setSuccess(false);
      log.setSendLog("");

      // insert
      mapper.insertSelective(log); // save to db, before send
      id = log.getId();

      // send sms
      info.setLogId(id);
      logger.debug("send sms - " + JsonConvert.toJson(info));
      send(Queue.SMS.getQueueName(), JsonConvert.toJson(info));

      // update log(after send)
      logger.info(String.format("send SMS(end) : %s", info.getTo()));
    } catch (Exception ex) {
      logger.warn("send sms Fail! - ", ex);
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);

      log = new SvcSmsLogWithBLOBs();
      log.setId(id);
      log.setSuccess(false);
      log.setSendLog(sw.toString());
      mapper.updateByPrimaryKeySelective(log);
    }
  }
}
