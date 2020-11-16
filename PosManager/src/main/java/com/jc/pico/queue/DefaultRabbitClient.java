/*
 * Filename	: DefaultRabbitClient.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.queue;

import com.jc.pico.configuration.Globals;
import com.jc.pico.queue.RabbitClient.Queue;
import com.jc.pico.utils.JsonConvert;

/**
 * Created by ruinnel on 2016. 4. 25..
 */
public class DefaultRabbitClient extends RabbitClient {

  public DefaultRabbitClient() {
    String host = Globals.AMQP_HOST;
    int port = Globals.AMQP_PORT;
    String username = Globals.AMQP_USERNAME;
    String password = Globals.AMQP_PASSWORD;
    init(host, port, username, password);
  }
  
  /**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DefaultRabbitClient rc = new DefaultRabbitClient();
		rc.init("95.131.29.205", 5672, "pico", "pico1234"); // 211.237.27.10, 95.131.29.205, 95.131.29.206
		rc.send(Queue.GCM.getQueueName(), "test");
	}

}
