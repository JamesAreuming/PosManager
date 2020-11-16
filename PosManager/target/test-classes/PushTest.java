
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.LocaleUtils;
//import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.controller.admin.AdminScheduler;
import com.jc.pico.queue.PushSender;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.DateFormat;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.bean.SendPush;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class PushTest {

	@Autowired 
	CodeUtil codeUtil;
	
	protected static Logger logger = LoggerFactory.getLogger(AdminScheduler.class);
	protected Connection connection;
	protected Channel channel;
	
	//@Test
	public void pushTest() throws IOException, TimeoutException {
			    
		try {
		     String host 	    = "localhost";
		     int port 	            = 5672;
		     String username = "master";
		     String password = "master1234";
		    		 
             String queueName = "master_gcm_send";
             
             //TODO:시간날때.MAP형식으로.
             //Map<String, Object> pushInfo = new HashMap<>();
             //message = JsonConvert.toJson(pushInfo);
             String message_title ="푸쉬 타이틀 입니다.";
             String message_content ="푸쉬 내용입니다.";
             String pushId = "3htR_PSXTy0NAoyT_k_8VvbugM7LLK4rPt5h6N9Joa872j7-dHYvFVASOBVbGIISUAE_NCu";
             
             String message = "{\"logId\":11761,\"os\":\"android\",\"type\":\"message-tp-order-approval\",\"extra\":{\"orderId\":6725},"
             		          + "\"pushIds\":[\" " + pushId +"\"],"
             		          + "\"message\":{\"content\":\" " + message_content + "\","
             		          + "\"title\":\" " + message_title + "\","
             		          + "\"extra\":{\"orderId\":6725},\"type\":\"message-tp-order-approval\"}}";
             
             connect(host,username,password);
             
             channel.queueDeclare(queueName, false, false, false, null);
             channel.basicPublish("", queueName, null, message.getBytes());
 
       } catch (Exception e) {
         logger.error("[ERROR] : " + e);
       } finally {
    	   close();	
       }
	}
	
	
	
	protected void connect(String host, String username,String password) throws IOException, TimeoutException {
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

}
