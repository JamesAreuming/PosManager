/*
 * Filename	: PushSender.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.queue;

import com.jc.pico.bean.SvcPushLogWithBLOBs;
import com.jc.pico.mapper.SvcPushLogMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.SendPush;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeoutException;

import static java.awt.SystemColor.info;

@Component
public class PushSender extends DefaultRabbitClient {
  protected static Logger logger = LoggerFactory.getLogger(PushSender.class);

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Override
  public void send(String queueName, String message) throws IOException, TimeoutException {
    super.send(queueName, message);
  }

  public void send(final boolean toStore, final SendPush info) {
    logger.info(String.format("send PUSH(start) : %s", JsonConvert.toJson(info)));
    sendGCM(info);
    if (toStore) {
      sendAPN(Queue.APN_STORE, info);
    } else {
      sendAPN(Queue.APN_APP, info);
    }
  }

  public void sendGCM(final SendPush info) {
    if (!info.hasMessage("android")) { return; }

    info.setOsType("android");
    logger.info(String.format("send GCM(start) : %s", JsonConvert.toJson(info)));
    SvcPushLogMapper mapper = sessionTemplate.getMapper(SvcPushLogMapper.class);
    SvcPushLogWithBLOBs log = new SvcPushLogWithBLOBs();
    long id = 0;
    try {
      // save log(before send)
      log.setBrandId(info.getBrandId());
      log.setOs("android");
      log.setTo(JsonConvert.toJson(info.getPushIds()));
      log.setTitle(info.getSubject());
      log.setContent(info.getMessage());
      log.setSuccess(false);
      log.setExtra("");
      log.setSendLog("");

      // insert
      mapper.insertSelective(log); // save to db, before send
      id = log.getId();

      // send
      info.setLogId(id);
      logger.debug("send push(GCM) - " + JsonConvert.toJson(info));
      send(Queue.GCM.getQueueName(), JsonConvert.toJson(info));

      // update log(after send)
      logger.info(String.format("send GCM(end) : %s", info.getPushIds()));
    } catch (Exception ex) {
      logger.warn("send GCM Fail! - ", ex);
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);

      log = new SvcPushLogWithBLOBs();
      log.setId(id);
      log.setSuccess(false);
      log.setSendLog(sw.toString());
      mapper.updateByPrimaryKeySelective(log);
    }
  }

  public void sendAPN(Queue queue, final SendPush info) {
    if (!info.hasMessage("ios")) { return; }

    info.setOsType("ios");
    logger.info(String.format("send APNs(start) : %s", JsonConvert.toJson(info)));
    SvcPushLogMapper mapper = sessionTemplate.getMapper(SvcPushLogMapper.class);
    SvcPushLogWithBLOBs log = new SvcPushLogWithBLOBs();
    long id = 0;
    try {
      // save log(before send)
      log.setBrandId(info.getBrandId());
      log.setOs("ios");
      log.setTo(JsonConvert.toJson(info.getPushIds()));
      log.setTitle(info.getSubject());
      log.setContent(info.getMessage());
      log.setSuccess(false);
      log.setExtra("");
      log.setSendLog("");

      // insert
      mapper.insertSelective(log); // save to db, before send
      id = log.getId();

      // send
      info.setLogId(id);
      logger.debug("send push(APNs) - " + JsonConvert.toJson(info));
      send(queue.getQueueName(), JsonConvert.toJson(info));

      // update log(after send)
      logger.info(String.format("send APNs(end) : %s", info.getPushIds()));
    } catch (Exception ex) {
      logger.warn("send APNs Fail! - ", ex);
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);

      log = new SvcPushLogWithBLOBs();
      log.setId(id);
      log.setSuccess(false);
      log.setSendLog(sw.toString());
      mapper.updateByPrimaryKeySelective(log);
    }
  }

  public SqlSessionTemplate getSessionTemplate() {
    return sessionTemplate;
  }

  public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
    this.sessionTemplate = sessionTemplate;
  }
}
