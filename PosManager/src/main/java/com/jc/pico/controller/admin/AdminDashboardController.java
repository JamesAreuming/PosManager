/*
 * Filename	: AdminModelController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.jc.pico.utils.AdminHandler;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.customMapper.admin.CustomDashboardMapper;

@Controller
@ResponseBody
@RequestMapping("/admin/model")
public class AdminDashboardController {

  protected static Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  CustomDashboardMapper customDashboardMapper;
  
  @Autowired
  SqlSessionTemplate sessionTemplate;

  @Autowired
  private HttpSession session;

  @RequestMapping("/Dashboard")
  public Map<Object,Object> Corp(
	  @RequestParam(required=false) Map<Object,Object> params,
      @RequestParam(required=false, value="start", defaultValue="0") int start,
      @RequestParam(required=false, value="length", defaultValue="10") int length,
      HttpMethod method) throws Exception {
    Map<Object,Object> result = new HashMap<>();
    Map<String,String> userInfo = new HashMap<>();
    Map<String, String> searchData = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar date = Calendar.getInstance();
    
    // 사용자 정보 저장
    result = AdminHandler.setSessionInfo(result, session, "1");
    
    userInfo = (HashMap<String, String>) session.getAttribute("userInfo");
    
    // 임시셋팅
   	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
   	Date now = new Date();
   	String tmpBcd = format.format(now);
   	
   	searchData.put("franId", String.valueOf(userInfo.get("franId")));
	searchData.put("brandId", String.valueOf(userInfo.get("brandId")));
	
	searchData.put("fromDate", tmpBcd);
	searchData.put("toDate", tmpBcd);
	
    logger.info("★★★★★★★★★ Dashboard ★★★★★★★★★★★★★★");
    logger.info("result : " + result);
    logger.info("userInfo : " + userInfo);
    logger.info("searchData : " + searchData);
    logger.info("★★★★★★★★★  Dashboard ★★★★★★★★★★★★★★");
  	
    switch(method){
    
    case GET :
    	if("2".equals(userInfo.get("userRole")) || "1".equals(userInfo.get("userRole"))){ // franchise
    		
        	List<LinkedHashMap<String, String>> list = customDashboardMapper.getBrandInfoList(searchData);

        	result.put("today",  tmpBcd);
        	result.put("brandlist", list);
        	result.put("success", true);
        	logger.debug("fran : " + JsonConvert.toJson(result));
        	
    	}else if("3".equals(userInfo.get("userRole"))){    // brand
    	  Map<String, String> searchCouponDate = Maps.newHashMap();
    	  Map<String, String> searchCustomerDate = Maps.newHashMap();
    	  
    		// Sales Trend
    		List<LinkedHashMap<String, String>> trendList = customDashboardMapper.getSalesTrendList(searchData);    		
    		
    		// Today Sales, Today Service
    		LinkedHashMap<String, String> brandlist = customDashboardMapper.getBrandInfo(searchData);
        	
    		// Today Sales Top 5 Chart
    		searchData.put("searchSort", "DESC");
    		List<LinkedHashMap<String, String>> salesTopChart = customDashboardMapper.getSalesChart(searchData);
    		
    		// Today Sales Tot 5
    		List<LinkedHashMap<String, String>> salesTopList = customDashboardMapper.getSalesList(searchData);
    		
    		// Today Sales Bottom 5 List
    		searchData.put("searchSort", "ASC");
    		List<LinkedHashMap<String, String>> salesBottomChart = customDashboardMapper.getSalesChart(searchData);
    		
    		// Today Sales Bottom 5
    		List<LinkedHashMap<String, String>> salesBottomList = customDashboardMapper.getSalesList(searchData);
    		
    		// Top5 Item
    		List<LinkedHashMap<String, String>> itemList = customDashboardMapper.getItemList(searchData);
    		
    		// Top5 Category
    		List<LinkedHashMap<String, String>> categoryList = customDashboardMapper.getCategoryList(searchData);
    		searchCouponDate.put("weekToDate", sdf.format(date.getTime()));
    		date.add(Calendar.DATE, -6);
    		searchCouponDate.put("weekFromDate", sdf.format(date.getTime()));
    		
    	  // Today Recently 4 reviews
	        List<LinkedHashMap<String, String>> reviewsList = customDashboardMapper.getReviewsList(searchData);
	        
	        // Today All Reviews count
	        int todayReviewsCount = customDashboardMapper.getReviewsListCount(searchData);
	        
	        // Today Stamp
	        LinkedHashMap<String, String> todayStamp = customDashboardMapper.getStampData(searchData);
	        
	        // Recently 7 days Coupon issue
	        List<LinkedHashMap<String, String>> couponIssueList = customDashboardMapper.getCouponIssueList(searchData);
	        
	     // Recently 7 days Coupon use
	        List<LinkedHashMap<String, String>> couponUseList = customDashboardMapper.getCouponUseList(searchData);
	    		
	        // This week customer (online : bak app,  offline : pos)
	        date = Calendar.getInstance();
	        searchData.put("salesToDate", sdf.format(date.getTime()));
	        date.set(Calendar.DAY_OF_WEEK,  Calendar.SUNDAY);
	        searchData.put("salesFromDate", sdf.format(date.getTime()));
	        searchData.put("calendarFromDate", sdf.format(date.getTime()));
	        date.set(Calendar.DAY_OF_WEEK,  Calendar.SATURDAY);
	        searchData.put("calendarToDate", sdf.format(date.getTime()));
	        searchCustomerDate.put("startDate",  searchData.get("calendarFromDate"));
	        searchCustomerDate.put("endDate",  searchData.get("calendarToDate"));
	        List<LinkedHashMap<String, String>> thisWeekCustomerList = customDashboardMapper.getThisWeekCustomerList(searchData);
	        
	        // Last week customer (online : bak app,  offline : pos)
	        date = Calendar.getInstance();
	        date.add(Calendar.DATE, -7);
	        date.set(Calendar.DAY_OF_WEEK,  Calendar.SUNDAY);
	        searchData.put("salesFromDate", sdf.format(date.getTime()));
	        searchData.put("calendarFromDate", sdf.format(date.getTime()));
	        date.set(Calendar.DAY_OF_WEEK,  Calendar.SATURDAY);
	        searchData.put("salesToDate", sdf.format(date.getTime()));
	        searchData.put("calendarToDate", sdf.format(date.getTime()));
	        
	        List<LinkedHashMap<String, String>> lastWeekCustomerList = customDashboardMapper.getSearchLastCustomerList(searchData);
        
        	result.put("trendList", trendList);					// Sales Trend
        	result.put("brandlist", brandlist);					// Today Sales, Today Service
        	result.put("salesTopChart", salesTopChart);			// Today Sales Top 5 Chart
        	result.put("salesTopList", salesTopList);			// Today Sales Top 5 List
        	result.put("salesBottomChart", salesBottomChart);	// Today Bottom Top 5 Chart
        	result.put("salesBottomList", salesBottomList);		// Today Sales Bottom 5 List
        	result.put("itemList", itemList);					// Item Sales Top 5
        	result.put("categoryList", categoryList);			// Top5 Category
        	result.put("today", tmpBcd);
        	result.put("reviewList",  reviewsList);    // Today Recently 4 reviews
        	result.put("todayReviewsCount", todayReviewsCount);  // Today All Reviews count  
        	result.put("todayStamp",  todayStamp); // Today Stamp
        	result.put("couponIssueList", couponIssueList); // Recently 7 days Coupon issue
        	result.put("couponUseList", couponUseList); // Recently 7 days Coupon use
        	result.put("thisWeekCustomerList", thisWeekCustomerList); // This week customer list
        	result.put("lastWeekCustomerList", lastWeekCustomerList); // Last week customer list
        	result.put("searchCouponDate", searchCouponDate);
        	result.put("searchCustomerDate", searchCustomerDate);
        	result.put("success", true);
        	
    	}else if("4".equals(userInfo.get("userRole"))){    // store
    		Map<String, String> searchingDateForCustomer = Maps.newHashMap();
    		Map<String, String> searchingDateForCoupon = Maps.newHashMap();
    		Map<String, String> searchingDateForsSales = Maps.newHashMap();
    		searchData.put("storeId", String.valueOf(userInfo.get("storeId")));
    		
    		// Today Sales    		
    		LinkedHashMap<String, String> salesData = customDashboardMapper.getSalesData(searchData);
    		
    		// Today Service
    		LinkedHashMap<String, String> serviceData = customDashboardMapper.getServiceData(searchData);
    		
    		date = Calendar.getInstance();
    		searchingDateForsSales.put("endDate", sdf.format(date.getTime()));
	        date.add(Calendar.DATE, -6);
	        searchingDateForsSales.put("startDate", sdf.format(date.getTime()));
    		// This week Top5 Item
    		List<LinkedHashMap<String, String>> itemList = customDashboardMapper.getItemList(searchData);
    		
    		// This week Top5 Category
    		List<LinkedHashMap<String, String>> categoryList = customDashboardMapper.getCategoryList(searchData);
    		
    		// Today Payment List
    		List<LinkedHashMap<String, String>> paymentList = customDashboardMapper.getPaymentList(searchData);
    		
    		// New Customers VS Returning Customers / Last 30 Days (online : bak app,  offline : pos)
	        date = Calendar.getInstance();
	        searchData.put("salesToDate", sdf.format(date.getTime()));
	        date.set(Calendar.DATE, date.getActualMinimum(Calendar.DATE));
	        searchData.put("salesFromDate", sdf.format(date.getTime()));
	        searchData.put("calendarFromDate", sdf.format(date.getTime()));
	        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DATE));
	        searchData.put("calendarToDate", sdf.format(date.getTime()));
	        searchingDateForCustomer.put("thisMonthFromDate", searchData.get("salesFromDate"));
	        searchingDateForCustomer.put("thisMonthToDate", searchData.get("salesToDate"));
	        
	        List<LinkedHashMap<String, String>> thisMonthCustomerList = customDashboardMapper.getThisMonthCustomerList(searchData);
	        
	        date = Calendar.getInstance();
	        date.add(Calendar.MONTH,  -1);
	        date.set(Calendar.DATE, date.getActualMinimum(Calendar.DATE));
	        searchData.put("salesFromDate", sdf.format(date.getTime()));
	        searchData.put("calendarFromDate", sdf.format(date.getTime()));
	        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DATE));
	        searchData.put("salesToDate", sdf.format(date.getTime()));
	        searchData.put("calendarToDate", sdf.format(date.getTime()));
	        searchingDateForCustomer.put("lastMonthFromDate", searchData.get("salesFromDate"));
	        searchingDateForCustomer.put("lastMonthToDate", searchData.get("salesToDate"));
	        
	        List<LinkedHashMap<String, String>> lastMonthCustomerList = customDashboardMapper.getSearchLastCustomerList(searchData);
    		
    		// Recently Reviews
    		List<LinkedHashMap<String, String>> reviewsList = customDashboardMapper.getReviewsList(searchData);
    		
    		// Today Stamp
    		LinkedHashMap<String, String> stampData = customDashboardMapper.getStampData(searchData);
    		
    		// This week Coupon Issue
    		List<LinkedHashMap<String, String>> couponIssueList = customDashboardMapper.getCouponIssueList(searchData);
    		
    		// This week Coupon use
    		List<LinkedHashMap<String, String>> couponUseList = customDashboardMapper.getCouponUseList(searchData);
        	
    		date = Calendar.getInstance();
    		searchingDateForCoupon.put("thisWeekCouponToDate", sdf.format(date.getTime()));
    		date.add(Calendar.DATE,  -6);
    		searchingDateForCoupon.put("thisWeekCouponFromDate", sdf.format(date.getTime()));
    		
    		
        	result.put("salesData", salesData);
        	result.put("serviceData", serviceData);
        	result.put("itemList", itemList);
        	result.put("categoryList", categoryList);
        	result.put("paymentList", paymentList);
//        	result.put("newAndReturnList", newAndReturnList);
        	result.put("thisMonthCustomerList", thisMonthCustomerList);
        	result.put("lastMonthCustomerList", lastMonthCustomerList);
        	result.put("reviewList", reviewsList);
        	result.put("todayStamp", stampData);
        	result.put("couponIssueList", couponIssueList);
        	result.put("couponUseList", couponUseList);
        	result.put("today", tmpBcd);
        	result.put("searchingDateForCustomer", searchingDateForCustomer);
        	result.put("searchingDateForCoupon", searchingDateForCoupon);
        	result.put("searchingDateForsSales",  searchingDateForsSales);
        	result.put("success", true);
    	}

      break;
    case POST :

      break;
    case PUT :

      break;
    default :
      result.put("success", false);
      result.put("errMsg", "not supported method.");
    }

    return result;
  }

}
