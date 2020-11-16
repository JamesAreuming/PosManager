/*
 * Filename	: CallbackReceiver.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.queue;

import com.jc.pico.bean.*;
import com.jc.pico.mapper.SvcMailLogMapper;
import com.jc.pico.mapper.SvcPushLogMapper;
import com.jc.pico.mapper.SvcSmsLogMapper;
import com.jc.pico.mapper.UserDeviceMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.SendResult;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CallbackReceiver extends DefaultRabbitClient implements InitializingBean, DisposableBean {
  protected static Logger logger = LoggerFactory.getLogger(CallbackReceiver.class);

  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Override
  public void afterPropertiesSet() throws Exception {
	  try {
		  super.connect();
		  logger.info("CallbackReceiver start! (afterPropertiesSet called)");
	  } catch (Exception e) {
          logger.warn("afterPropertiesSet : connect error!", e);
        }
	  
	  try {
		  channel.queueDeclare(Queue.CALLBACK.getQueueName(), false, false, false, null);
	  } catch (Exception e) {
          logger.warn("afterPropertiesSet : queueDeclare error!", e);
        }
    
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        String message = new String(body, "UTF-8");
        try {
          SendResult result = JsonConvert.JsonConvertObject(message, SendResult.class);
          processResult(result);
          logger.debug("callback - " + message);
        } catch (Exception e) {
          logger.warn("SendResult parse error!", e);
        }
      }
    };
    channel.basicConsume(Queue.CALLBACK.getQueueName(), true, consumer);
  }

  private void processResult(SendResult result) throws Exception {
    Queue queue = RabbitClient.Queue.getByName(result.getQueueName());
    switch (queue) {
      case EMAIL:
        processEmail(result);
        break;
      case GCM:
      case APN_APP:
      case APN_STORE:
        processPush(result);
        break;
      case SMS:
        processSms(result);
        break;
    }
  }

  private void processSms(SendResult result) {
    // update log
    if (result.getLogId() > 0) {
      SvcSmsLogMapper mapper = sessionTemplate.getMapper(SvcSmsLogMapper.class);
      SvcSmsLogWithBLOBs log = new SvcSmsLogWithBLOBs();
      log.setId(result.getLogId());
      log.setSuccess(result.isSuccess());
      log.setSendLog(result.getLogs());
      mapper.updateByPrimaryKeySelective(log);
    }
  }

  private void processPush(SendResult result) {
    // update log
    if (result.getLogId() > 0) {
      SvcPushLogMapper mapper = sessionTemplate.getMapper(SvcPushLogMapper.class);
      SvcPushLogWithBLOBs log = new SvcPushLogWithBLOBs();
      log.setId(result.getLogId());
      log.setSuccess(result.isSuccess());
      log.setSendLog(result.getLogs());
      if (result.getExtra() != null) {
        log.setExtra(JsonConvert.toJson(result.getExtra()));
      } else {
        log.setExtra("{}");
      }
      mapper.updateByPrimaryKeySelective(log);
    }

    // update push id
    Map<String, Object> extra = result.getExtra();
    if (extra != null) {
      UserDeviceMapper mapper = sessionTemplate.getMapper(UserDeviceMapper.class);
      if (extra.containsKey("removeIds")) {
        List<String> removeIds = (List<String>) extra.get("removeIds");
        UserDevice device = new UserDevice();
        device.setIsAlive(false);
        UserDeviceExample example = new UserDeviceExample();
        example.createCriteria()
          .andPushIdIn(removeIds);

        mapper.updateByExampleSelective(device, example);
        logger.debug("remove invalidated gcmIds = " + removeIds);
      } else if (extra.containsKey("updateIds")) {
        Map<String, String> updateIds = (Map<String, String>) extra.get("updateIds");
        Set<String> keys = updateIds.keySet();
        Iterator<String> itr = keys.iterator();
        while(itr.hasNext()) {
          String prev = itr.next();
          String changed = updateIds.get(prev);
          UserDevice device = new UserDevice();
          device.setPushId(changed);
          UserDeviceExample example = new UserDeviceExample();
          example.createCriteria()
            .andPushIdEqualTo(prev);

          mapper.updateByExampleSelective(device, example);
        }

        logger.debug("update gcmIds = " + updateIds);
      }
    }
  }

  private void processEmail(SendResult result) {
    if (result.getLogId() > 0) {
      SvcMailLogMapper mapper = sessionTemplate.getMapper(SvcMailLogMapper.class);
      SvcMailLogWithBLOBs log = new SvcMailLogWithBLOBs();
      log.setId(result.getLogId());
      log.setSuccess(result.isSuccess());
      log.setSendLog(result.getLogs());
      mapper.updateByPrimaryKeySelective(log);
    }
  }

  @Override
  public void destroy() throws Exception {
    super.close();
  }
}
