package com.jc.pico.controller.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.queue.PushSender;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.DateFormat;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.bean.SendPush;
import com.jc.pico.utils.customMapper.admin.CustomInventoryMapper;
import com.jc.pico.utils.customMapper.admin.CustomReservationMapper;

/**
 * Admin 관련 스케쥴링 작업
 *
 */
@Component
public class AdminScheduler {

	protected static Logger logger = LoggerFactory.getLogger(AdminScheduler.class);
	
	@Autowired 
	CodeUtil codeUtil;

	@Autowired
	SqlSessionTemplate sessionTemplate;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	CustomReservationMapper customReservationMapper;
	@Autowired
	CustomInventoryMapper customInventoryMapper;
	
	/**
	 * 예약계약 건에 대해 일정시간 전에 알림 push발송.
	 */
	@Scheduled( cron = "0 0 15 * * ?")
	public void sendPushReservation() {
		logger.info("[Admin Schedule] sendPushReservation =" + System.currentTimeMillis());
		// 수정 : 
		// 주석 처리
		
/*
		Map<Object,Object> params = null;
		final Locale defaultLocale = Locale.US;
		
		String android = codeUtil.getBaseCodeByAlias("android");
		String ios = codeUtil.getBaseCodeByAlias("ios");
		int totalCnt = 0;
	    int successCnt = 0;
	    int failCnt = 0;
	    
		try {
		     
             List<LinkedHashMap<String, Object>> pushUserList = new ArrayList<LinkedHashMap<String, Object>>();
             
             //발송대상 카운트 조회
             totalCnt = customReservationMapper.getPushReservationUserListCnt(params);
             
             //발송대상 조회
             if( totalCnt > 0 ) {
            	 pushUserList = customReservationMapper.getPushReservationUserList(params);
            	
            	 for(int i =0 ; i < pushUserList.size() ; i++ ) {
            		 
            		 if(pushUserList.get(i).get("OS") != null && !"".equals(pushUserList.get(i).get("OS")) ) {
	            		 Map<String, List<String>> pushIds = Maps.newHashMap();
	            		 List<String> gcmIds = Lists.newArrayList();
	            		 List<String> apnIds = Lists.newArrayList();
	
	            		 String storeNm   =  StrUtils.isNull(pushUserList.get(i).get("STORE_NM").toString());
	            		 Locale locale    =  null;
	            		 
	            		 try {
	            			 locale =  LocaleUtils.toLocale(StrUtils.isNull(pushUserList.get(i).get("LOCALE").toString()));
	            		 } catch (Exception e) {
	            			 locale =  defaultLocale;
	            		 }
	            		 
	            		 String reserveTM = DateFormat.formatDate(pushUserList.get(i).get("RESERVE_TM").toString(),locale.getLanguage());
	            		 
	            		 if (android.equals(pushUserList.get(i).get("OS").toString()) ) {
	                         gcmIds.add(pushUserList.get(i).get("PUSH_ID").toString());
	            		 } else if (ios.equals(pushUserList.get(i).get("OS").toString()) ) {
	                         apnIds.add(pushUserList.get(i).get("PUSH_ID").toString());
	            		 }
	                 
	                     if (gcmIds.size() > 0) {
	                    	 pushIds.put("android",  gcmIds);
	                     }
	                     
	                     if (apnIds.size() > 0) {
	                    	 pushIds.put("ios",  apnIds);
	                     }
	            		 
	                     try{
	                    	//발송
		                     PushSender pushSender = new PushSender();
		                     SendPush push = new SendPush();
		                     push.setPushIds(pushIds);
		                     push.setBrandId(Long.valueOf(pushUserList.get(i).get("BRAND_ID").toString()));   
		                     push.setTitle(messageSource.getMessage("app.push.message-tp-reservation-notification.title", new String[]{}, locale));
		                     push.setContent(messageSource.getMessage("app.push.message-tp-reservation-notification.msg", new String[]{reserveTM,storeNm}, locale));
		                     pushSender.setSessionTemplate(sessionTemplate);
		                     pushSender.send(false,  push);
	                    	 
		                     successCnt++;
	                     }   catch (Exception e) {
	                    	 logger.error("[ERROR] [AdminScheduler][sendPushReservation] send push fail. [ORDER_NO : " + pushUserList.get(i).get("ORDER_NO").toString() + "]");
	                         e.printStackTrace();
	                    	 failCnt++;
	                     } 
            		 }
            		 
            	 }
             }
           } catch (Exception e) {
             logger.error("[ERROR] [AdminScheduler][sendPushReservation] send push fail.");
             e.printStackTrace();
           } finally {
          	 logger.info("[INFO] [AdminScheduler][sendPushReservation] RESULT = TOTAL: "+ totalCnt + " SUCCESS:"+successCnt + " FAIL:" + failCnt);				
			}
	*/
		
	}
	
	
	
	/**
	 * 월말 재고 스케쥴 
	 * - 매월 1일 오전4시.
	 */
	@Scheduled( cron = "0 0 04 1 * *")
	public void insertStockCloing() {
		logger.info("[Admin Schedule] insertStockCloing =" + System.currentTimeMillis());
		// 수정 : 
		// 주석 처리
		
/*
		Map<Object,Object> params = null;
		int resultCnt = 0;
		
		int totalCnt = 0;
	    int successCnt = 0;
	    int failCnt = 0;
	    
		try {
		     
             List<LinkedHashMap<String, Object>> stockCloingList = new ArrayList<LinkedHashMap<String, Object>>();
             
             //월말 재고 카운트 조회
             totalCnt = customInventoryMapper.getStockCloingListCount(params);
             
             //월말 재고 조회
             if( totalCnt > 0 ) {
            	 stockCloingList  = customInventoryMapper.getStockCloingList(params);
            	 
            	 for(int i =0 ; i < stockCloingList.size() ; i++ ) {
            		 try{
            			 //월말 재고 등록
            			 resultCnt = customInventoryMapper.insertStockCloingList(stockCloingList.get(i));
            			 successCnt++;
            		 } catch (Exception e) {
            			 logger.error("[ERROR] [AdminScheduler][insertStockCloing] Insert row fail.");
                         e.printStackTrace();
            			 failCnt++;
            		 }
            	 }
             }
           } catch (Exception e) {
             logger.error("[ERROR] [AdminScheduler][insertStockCloing] Insert StockCloing fail.");
             e.printStackTrace();
           } finally {
          	 logger.info("[INFO] [AdminScheduler][insertStockCloing] RESULT = TOTAL: "+ totalCnt + " SUCCESS:"+successCnt + " FAIL:" + failCnt);				
			}
*/
		
	}


}
