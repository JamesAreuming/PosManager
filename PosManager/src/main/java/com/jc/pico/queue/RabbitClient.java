/*
 * Filename	: RabbitClient.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   :
 */

package com.jc.pico.queue;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class RabbitClient {
  public enum Queue {
    EMAIL("pico_email_send"),
    GCM("pico_gcm_send"),
    APN_STORE("pico_store_apn_send"),
    APN_APP("pico_app_apn_send"),
    SMS("pico_sms_send"),
    CALLBACK("pico_send_callback"),
    ;

    private String value;
    Queue(String value) {
      this.value = value;
    }
    public String getQueueName() {
      return value;
    }
    public static Queue getByName(String name) {
      Queue[] vals = values();
      for (Queue queue: vals) {
        if (queue.getQueueName().equals(name)) {
          return queue;
        }
      }
      return null;
    }
  }
  protected String host;
  protected int port;
  protected String username;
  protected String password;
  protected String message;

  protected Connection connection;
  protected Channel channel;

  public RabbitClient() {}

  protected void init(String host, int port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
  }

  public void send(String queueName, String message) throws IOException, TimeoutException {
    connect();
    channel.queueDeclare(queueName, false, false, false, null);
    channel.basicPublish("", queueName, null, message.getBytes());
    close();
  }

  protected void connect() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(host);
    factory.setUsername(username);
    factory.setPassword(password);
    connection = factory.newConnection();
    channel = connection.createChannel();
  }

  protected void close() throws IOException, TimeoutException {
    if (channel != null) {
      channel.close();
    }
    if (connection != null) {
      connection.close();
    }
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
