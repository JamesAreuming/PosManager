/*
 * Filename	: MailSender.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.queue;

import com.jc.pico.bean.SvcMailAccount;
import com.jc.pico.bean.SvcMailLogWithBLOBs;
import com.jc.pico.mapper.SvcMailLogMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.SendEmail;
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
public class MailSender extends DefaultRabbitClient {
  protected static Logger logger = LoggerFactory.getLogger(MailSender.class);

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Override
  public void send(String queueName, String message) throws IOException, TimeoutException {
    super.send(queueName, message);
  }

  public void send(final SendEmail info, final SqlSessionTemplate sst) {
    logger.info(String.format("send Mail(start) : %s", JsonConvert.toJson(info)));
    SvcMailAccount account = info.getAccount();
    SvcMailLogMapper mapper = sst.getMapper(SvcMailLogMapper.class);
    SvcMailLogWithBLOBs log = new SvcMailLogWithBLOBs();
    long id = 0;
    try {
      // save log(before send)
      log.setBrandId(account.getBrandId());
      log.setHost(account.getHost());
      log.setPort(account.getPort());
      log.setUsername(account.getUsername());
      log.setFrom(info.getFrom());
      log.setTo(JsonConvert.toJson(info.getTo()));
      log.setBcc(JsonConvert.toJson(info.getBcc()));
      log.setCc(JsonConvert.toJson(info.getCc()));
      log.setTitle(info.getSubject());
      log.setContent(info.getMessage());
      log.setSuccess(false);
      log.setSendLog("");

      // insert
      mapper.insertSelective(log); // save to db, before send
      id = log.getId();

      // send mail
      info.setLogId(id);
      logger.debug("send mail - " + JsonConvert.toJson(info));
      send(Queue.EMAIL.getQueueName(), JsonConvert.toJson(info));

      logger.info(String.format("send Mail(end) : %s", info.getTo()));
    } catch (Exception ex) {
      logger.warn("send Mail Fail! - ", ex);
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);

      log = new SvcMailLogWithBLOBs();
      log.setId(id);
      log.setSuccess(false);
      log.setSendLog(sw.toString());
      mapper.updateByPrimaryKeySelective(log);
    }
  }
}
