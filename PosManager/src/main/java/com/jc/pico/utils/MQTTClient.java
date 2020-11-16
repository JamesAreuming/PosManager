/*
 * Filename	: MQTTClient.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.bean.User;
import com.jc.pico.configuration.Globals;
import com.jc.pico.utils.bean.SyncNotice;
//import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.Nullable;

public class MQTTClient {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private static final String CLIENT_ID = MQTTClient.class.getName();

  public enum Division {
    ORDER("order"),
    TABLE("table");

    String name;
    Division(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
  private final String host;
  private final int port;
  public MQTTClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  private static MQTTClient getDefaultClient() {
    return new MQTTClient(Globals.MQTT_HOST, Globals.MQTT_PORT);
  }

  public static void sendOrder(SvcStore store, SvcOrder order, @Nullable SvcOrderItem firstItem, User user, String deviceTp, String posNo) {
	  sendOrder(store.getId(), order, firstItem, user, deviceTp, posNo);
  }  
    
  public static void sendOrder(Long storeId, SvcOrder order, String deviceTp, String posNo) {
    sendOrder(storeId, order, null, null, deviceTp, posNo);
  }
  
  public static void sendOrder(Long storeId, SvcOrder order, @Nullable SvcOrderItem firstItem, User user, String deviceTp, String posNo) {
    MQTTClient client = getDefaultClient();

    SyncNotice notice = new SyncNotice();
    notice.setDivision(Division.ORDER.getName());

    SyncNotice.User syncUser = new SyncNotice.User();
    if(user != null) {
	    syncUser.setId(user.getId());
	    syncUser.setName(user.getName());
	    syncUser.setTel(user.getMb());
    }

    SyncNotice.Item item = new SyncNotice.Item();
    if (firstItem != null) {
      item.setId(firstItem.getId());
      item.setName(firstItem.getItemNm());
      item.setPrice(firstItem.getPrice());
      item.setCount(firstItem.getCount().intValue());
    }

    SyncNotice.Order syncOrder = new SyncNotice.Order();
    syncOrder.setId(order.getId());
    syncOrder.setOrderNo(order.getOrderNo());
    syncOrder.setOrderTm(order.getOrderTmLocal());
    syncOrder.setOrderTp(order.getOrderTp());
    syncOrder.setReserveTm(order.getReserveTmLocal());
    syncOrder.setSales(order.getSales());
    syncOrder.setOpenDt(order.getOpenDt());
    syncOrder.setCustomerCnt(order.getCustomerCnt() != null ? order.getCustomerCnt().intValue() : 0);
    if (firstItem != null ) {
      syncOrder.setItem(item);
    }

    SyncNotice.OrderData data = new SyncNotice.OrderData();
    data.setStatus(order.getOrderSt()); // 신규 주문
    data.setUser(syncUser);
    data.setOrder(syncOrder);

    SyncNotice.From from = new SyncNotice.From();
    from.setType(deviceTp);
    from.setNo(posNo);

    notice.setData(data);
    notice.setFrom(from);

    client.sendToStore(storeId, notice);
  }

  
  public void sendToStore(Long storeId, SyncNotice notice) {    
    send(String.format("/bak/store/%d", storeId), notice);
  }
    
  public void send(SvcStore store, SyncNotice notice) {
	sendToStore(store.getId(), notice);
  }

  public void send(SvcStore store, SyncNotice notice, String posNo) {
    String topic = String.format("/bak/store/%d/%s", store.getId(), posNo);
    send(topic, notice);
  }

  public void send(String topic, SyncNotice notice) {
    String broker = String.format("tcp://%s:%d", host, port);
    MemoryPersistence persistence = new MemoryPersistence();

    try {
      MqttClient client = new MqttClient(broker, CLIENT_ID + System.nanoTime(), persistence);
      MqttConnectOptions connectOptions = new MqttConnectOptions();
      connectOptions.setCleanSession(true);
      logger.debug("Connecting to broker : {}", broker);
      client.connect(connectOptions);
      logger.debug("Connected");
      MqttMessage message = new MqttMessage(JsonConvert.toJson(notice).getBytes("UTF-8"));
      client.publish(topic, message);
      logger.debug("published : {}, / {}", topic, JsonConvert.toJson(notice));
      client.disconnect();
      client.close();
    } catch (MqttException e) {
      logger.error("MQTT send fail! - topic: {}, data : {}", topic, JsonConvert.toJson(notice));
      logger.error("MQTT send fail!", e);
    } catch (UnsupportedEncodingException e) {
      logger.error("MQTT send fail! - JsonData.getByte() fail!\n{}", JsonConvert.toJson(notice));
    }
  }

  public static void main(String[] args) throws Exception {
    String host = "95.131.29.205"; // 211.237.27.10, 95.131.29.205, 95.131.29.206
    int port = 1883;

    SvcStore store = new SvcStore();
    store.setId(23l);
    SyncNotice notice = new SyncNotice();
    notice.setDivision("order");

    SyncNotice.User user = new SyncNotice.User();
    user.setId(100l);
    user.setName("박광은");
    user.setTel("01082735122");

    SyncNotice.Item item = new SyncNotice.Item();
    item.setId(1124l);
    item.setName("Coffe");
    item.setPrice(2500.0);
    item.setCount(4);

    SyncNotice.Order order = new SyncNotice.Order();
    order.setId(400l);
    order.setOrderTp("605001"); // 주문
    order.setReserveTm(new Date());
    order.setSales(10000.0);
    order.setCustomerCnt(3);
    order.setItem(item);

    SyncNotice.OrderData data = new SyncNotice.OrderData();
    data.setStatus("607001"); // 신규 주문
    data.setUser(user);
    data.setOrder(order);

    SyncNotice.From from = new SyncNotice.From();
    from.setType("606001"); // pos
    from.setNo("POS-001");

    notice.setData(data);
    notice.setFrom(from);


    MQTTClient client = new MQTTClient(host, port);
    client.send(store, notice);
  }
}
