/*
 * Filename	: AdminRewardController.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.controller.admin;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jc.pico.bean.SvcFranchiseExample;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.mapper.BaseBcodeMapper;
import com.jc.pico.mapper.SvcCouponLogMapper;
import com.jc.pico.mapper.SvcCouponMapper;
import com.jc.pico.mapper.SvcUserCouponMapper;
import com.jc.pico.mapper.UserBackofficeMenuMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.utils.AdminHandler;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.customMapper.admin.CustomMarketingMapper;
import com.jc.pico.utils.customMapper.admin.CustomRewardMapper;

@Controller
@ResponseBody
@RequestMapping("/admin/model/reward")
public class AdminRewardController {

	protected static Logger logger = LoggerFactory.getLogger(AdminRewardController.class);

	@Autowired CodeUtil codeUtil;

	@Autowired
	BaseBcodeMapper baseCodeMapper;

	@Autowired
    UserBackofficeMenuMapper userBackofficeMenuMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CustomRewardMapper customRewardMapper;

    @Autowired
    SvcUserCouponMapper svcUserCouponMapper;
    
    @Autowired
    SvcCouponMapper svcCouponMapper;
    
    @Autowired
    SvcCouponLogMapper svcCouponLogMapper;
    
    @Autowired
    CustomMarketingMapper customMarketingMapper;
    
    
    @Autowired
    SqlSessionTemplate sessionTemplate;

	protected static String dateFormat = "yyyy-MM-dd";

	/**
	 * Reword - Reward Summary
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/RewardSum")
    public Map<Object,Object> RewardSum(@RequestParam(required=false) Map<Object,Object> params) throws Exception {

    	Map<Object,Object> result = new HashMap<>();
        Map<String, String> search = new HashMap<>();
        Map<String, String> userInfo = new HashMap<>();
        
		logger.info("★★★★★★★★★ CardApproval ★★★★★★★★★★★");
		logger.info(" params : " + params.toString());
	  	logger.info("★★★★★★★★★ CardApproval ★★★★★★★★★★★");
	  	
        if (params.get("data") != null) {
            search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
        }

        if (params.get("userInfo") != null) {
			userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(), new TypeReference<Map<String, String>>() {});
		}
		
		logger.info("★★★★★★★★★ CardApproval ★★★★★★★★★★★");
	  	logger.info(" search : " + search.toString());
	  	logger.info(" userInfo : " + userInfo.toString());
	  	logger.info("★★★★★★★★★ CardApproval ★★★★★★★★★★★");
	  	
        String brandId = search.get("brandId");
        String option1 = search.get("option1");
        String option2 = search.get("option2");
        String fromdate = search.get("fromdate");
        String todate = search.get("todate");
        //String labeldate = search.get("labeldate");

        if (StringUtils.hasText(brandId)) params.put("brandId", brandId);
        if (StringUtils.hasText(fromdate)) params.put("fromdate", fromdate);
    	if (StringUtils.hasText(todate)) params.put("todate", todate);

    	List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
    	List<LinkedHashMap<String, Object>> chartList = new ArrayList<LinkedHashMap<String, Object>>();

        if (StringUtils.hasText(option2)) {
        	params.put("option1", option1);
        	params.put("option2", option2);

        	List<String> headerList = headerList(option1, fromdate, todate);
        	List<LinkedHashMap<String, Object>> logList = new ArrayList<LinkedHashMap<String, Object>>();

	        switch (option2) {
	        case "01" : // 스탬프적립
	        	params.put("tableNm", "TB_SVC_STAMP_LOG");
	        	params.put("logTp", codeUtil.getBaseCodeByAlias("stamp-log-saving"));
	        	break;

	        case "02" : // 쿠폰발행
	        	params.put("tableNm", "TB_SVC_COUPON_LOG");
	        	params.put("logTp", codeUtil.getBaseCodeByAlias("coupon-log-issue"));
	        	break;

	        case "03" : // 쿠폰사용
	        	params.put("tableNm", "TB_SVC_COUPON_LOG");
	        	params.put("logTp", codeUtil.getBaseCodeByAlias("coupon-log-use"));
	        	break;
	        }

	        if ("monthly".equals(option1)) { // monthly은 무조건 월초~월말일
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

				cal.setTime(sdformat.parse(fromdate));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				params.put("fromdate", sdformat.format(cal.getTime()));

				cal.setTime(sdformat.parse(todate));
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		    	params.put("todate", sdformat.format(cal.getTime()));
	        }

	        logList = customRewardMapper.getRewardList(params);
	        list = rewardSumList(headerList, logList, option1, option2);

			Long timeLag = doDiffOfDate(dateFormat, fromdate, todate);
			String dayMonth = "";

			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
				dayMonth = "day";
				result.put("dayChart", customRewardMapper.getRewardDayChart(params));
			}
			else {
				dayMonth = "month";
				result.put("monthChart", customRewardMapper.getRewardMonthChart(params));
			}

			result.put("dayMonth", dayMonth);
    		result.put("hourChart", customRewardMapper.getRewardHourChart(params));
    		result.put("dayofweekChart", customRewardMapper.getRewardDayofweekChart(params));

        }
        else { // Summary
        	list = customRewardMapper.getRewardSummaryList(params);

        	// TOP5 Chart
        	params.put("logTp", codeUtil.getBaseCodeByAlias("stamp-log-saving"));
        	result.put("chartList01", customRewardMapper.getRewardSummaryStampChart(params));
        	
        	params.put("logTp", codeUtil.getBaseCodeByAlias("coupon-log-issue"));
        	result.put("chartList02", customRewardMapper.getRewardSummaryCouponChart(params));
        	
        	params.put("logTp", codeUtil.getBaseCodeByAlias("coupon-log-use"));
        	result.put("chartList03", customRewardMapper.getRewardSummaryUsedCouponChart(params));
        	
        }

        result.put("fromdate", fromdate);
        result.put("todate", todate);
		result.put("list", list);
		result.put("success", true);

		return result;
    }

	@RequestMapping("/StampSum")
    public Map<Object,Object> StampSum(@RequestParam(required=false) Map<Object,Object> params) throws Exception {

    	Map<Object,Object> result = new HashMap<>();
        Map<String, String> search = new HashMap<>();

        if (params.get("data") != null) {
            search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
        }

        String brandId = search.get("brandId");
        String storeId = search.get("storeId");
        String option1 = search.get("option1");
        String fromdate = search.get("fromdate");
        String todate = search.get("todate");

    	List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

        if (StringUtils.hasText(brandId) && StringUtils.hasText(fromdate) && StringUtils.hasText(todate)) {
        	params.put("brandId", brandId);
        	params.put("storeId", storeId);
	        params.put("fromdate", fromdate);
	    	params.put("todate", todate);

	    	if (StringUtils.hasText(storeId)) params.put("storeId", storeId);

	    	if (StringUtils.hasText(option1)) {

	    		if ("summary".equals(option1)) {
    				list = customRewardMapper.getStampSummaryList(params);
	    		}
	    		else {
	    			switch (option1) {
	    			case "daily" :
	    				list = customRewardMapper.getStampDailyList(params);
	    				break;

	    			case "weekly" :
	    				list = customRewardMapper.getStampWeeklyList(params);
	    				break;

	    			case "monthly" :
	    				Calendar cal = Calendar.getInstance();
	    				SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

	    				cal.setTime(sdformat.parse(fromdate));
	    				cal.set(Calendar.DAY_OF_MONTH, 1);
	    				params.put("fromdate", sdformat.format(cal.getTime()));

	    				cal.setTime(sdformat.parse(todate));
	    				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    		    	params.put("todate", sdformat.format(cal.getTime()));

	    				list = customRewardMapper.getStampMonthlyList(params);
	    				break;
	    			}

	    			LinkedHashMap<String, Object> linkedHeader = new LinkedHashMap<String, Object>();
	    			LinkedHashMap<String, Object> linkedIssuedTotal = new LinkedHashMap<String, Object>();
	    			LinkedHashMap<String, Object> linkedCanceledTotal = new LinkedHashMap<String, Object>();
	    			LinkedHashMap<String, Object> linkedSumTotal = new LinkedHashMap<String, Object>();
	    			LinkedHashMap<String, Object> IssuedDailyAvgTotal = new LinkedHashMap<String, Object>();

	    			int cnt = 1;
	    			String header = "header";

	    			linkedHeader.put(header, "Item Name");	// BO2243 항목
	    			linkedIssuedTotal.put(header, "Earn Sales");	// BO2257 매출적립
	    			linkedCanceledTotal.put(header, "Cancel Sales");	// BO2258 매출취소
	    			linkedSumTotal.put(header, "Earn Total");	// BO2260 적립합계
	    			IssuedDailyAvgTotal.put(header, "Daily Average Earn");	// BO2259 일평균적립

    				for (LinkedHashMap<String, Object> val : list) {
    					String label = String.valueOf(val.get("label"));
    					header = "header" + cnt++;

    					if ("weekly".equals(option1)) {
    						String headerLast = "header" + list.size();

    						if ("header1".equals(header)) label = fromdate + " ~ " + label.split(" ~ ", -1)[1];
    						if (headerLast.equals(header)) label = label.split(" ~ ", -1)[0] + " ~ " + todate;
    					}
    					linkedHeader.put(header, label);
    					linkedIssuedTotal.put(header, val.get("stampIssuedTotal"));
    					linkedCanceledTotal.put(header, val.get("stampCanceledTotal"));
    					linkedSumTotal.put(header, val.get("stampSumTotal"));
    					IssuedDailyAvgTotal.put(header, val.get("stampIssuedDailyAvg"));
    				}
    				list.clear();
    				list.add(linkedHeader);
    				list.add(linkedIssuedTotal);
    				list.add(linkedCanceledTotal);
    				list.add(linkedSumTotal);
    				list.add(IssuedDailyAvgTotal);
	    		}

	        	params.put("tableNm", "TB_SVC_STAMP_LOG");
	        	params.put("logTp", codeUtil.getBaseCodeByAlias("stamp-log-saving"));

    			Long timeLag = doDiffOfDate(dateFormat, fromdate, todate);
    			String dayMonth = "";

    			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
    				dayMonth = "day";
    				result.put("dayChart", customRewardMapper.getRewardDayChart(params));
    			}
    			else {
    				dayMonth = "month";
    				result.put("monthChart", customRewardMapper.getRewardMonthChart(params));
    			}
    			result.put("dayMonth", dayMonth);
        		result.put("hourChart", customRewardMapper.getRewardHourChart(params));
        		result.put("dayofweekChart", customRewardMapper.getRewardDayofweekChart(params));

	    	}
        }

        result.put("fromdate", fromdate);
        result.put("todate", todate);
		result.put("list", list);
		result.put("success", true);

		return result;
    }

	@RequestMapping("/CouponSum")
	public Map<Object,Object> CouponSum(@RequestParam(required=false) Map<Object,Object> params) throws Exception {

		Map<Object,Object> result = new HashMap<>();
		Map<String, String> search = new HashMap<>();

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
		}

        String brandId = search.get("brandId");
        String storeId = search.get("storeId");
        String option1 = search.get("option1");
        String fromdate = search.get("fromdate");
        String todate = search.get("todate");

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

		if (StringUtils.hasText(brandId) && StringUtils.hasText(fromdate) && StringUtils.hasText(todate)) {
			params.put("brandId", brandId);
			params.put("storeId", storeId);
			params.put("fromdate", fromdate);
			params.put("todate", todate);

	    	if (StringUtils.hasText(storeId)) params.put("storeId", storeId);

			if (StringUtils.hasText(option1)) {

				if ("summary".equals(option1)) {
					list = customRewardMapper.getCouponSummaryList(params);
				}
				else {
					switch (option1) {
					case "daily" :
						list = customRewardMapper.getCouponDailyList(params);
						break;

					case "weekly" :
						list = customRewardMapper.getCouponWeeklyList(params);
						break;

					case "monthly" :
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

						cal.setTime(sdformat.parse(fromdate));
						cal.set(Calendar.DAY_OF_MONTH, 1);
						params.put("fromdate", sdformat.format(cal.getTime()));

						cal.setTime(sdformat.parse(todate));
						cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
						params.put("todate", sdformat.format(cal.getTime()));

						list = customRewardMapper.getCouponMonthlyList(params);
						break;
					}
					LinkedHashMap<String, Object> linkedHeader = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedIssuedTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedIssuedAccmlTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedIssuedPromotionTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedIssuedCanceledTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedIssuedCanceledAccmlTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedIssuedCanceledPromotionTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedUsedTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedUsedAccmlTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedUsedPromotionTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedUsedCanceledTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedUsedCanceledAccmlTotal = new LinkedHashMap<String, Object>();
					LinkedHashMap<String, Object> linkedUsedCanceledPromotionTotal = new LinkedHashMap<String, Object>();

					int cnt = 1;
					String header = "header";

					linkedHeader.put(header, "Item Name");		// BO2243 항목
					linkedIssuedTotal.put(header, "Coupon Issuing");	// BO2189 쿠폰발행
					linkedIssuedAccmlTotal.put(header, "Earn Coupon");		// BO2401 쿠폰적립
					linkedIssuedPromotionTotal.put(header, "Promotion coupon");		// BO2402 프로모션쿠폰
					linkedIssuedCanceledTotal.put(header, "Cancle Coupon Issuing");	// BO2403 쿠폰발행취소
					linkedIssuedCanceledAccmlTotal.put(header, "Earn Coupon");
					linkedIssuedCanceledPromotionTotal.put(header, "Promotion coupon");
					linkedUsedTotal.put(header, "Use Coupon");	// BO2270 쿠폰사용
					linkedUsedAccmlTotal.put(header, "Earn Coupon");
					linkedUsedPromotionTotal.put(header, "Promotion coupon");
					linkedUsedCanceledTotal.put(header, "Cancle Coupon Issuing");
					linkedUsedCanceledAccmlTotal.put(header, "Earn Coupon");
					linkedUsedCanceledPromotionTotal.put(header, "Promotion coupon");

					for (LinkedHashMap<String, Object> val : list) {
						String label = String.valueOf(val.get("label"));
						int couponIssuedTotal = StrUtils.parseInt(String.valueOf(val.get("couponIssuedTotal")));
						int couponIssuedPromotionTotal = StrUtils.parseInt(String.valueOf(val.get("couponIssuedPromotionTotal")));
						int couponIssuedCanceledTotal = StrUtils.parseInt(String.valueOf(val.get("couponIssuedCanceledTotal")));
						int couponIssuedCanceledPromotionTotal = StrUtils.parseInt(String.valueOf(val.get("couponIssuedCanceledPromotionTotal")));
						int couponUsedTotal = StrUtils.parseInt(String.valueOf(val.get("couponUsedTotal")));
						int couponUsedPromotionTotal = StrUtils.parseInt(String.valueOf(val.get("couponUsedPromotionTotal")));
						int couponUsedCanceledTotal = StrUtils.parseInt(String.valueOf(val.get("couponUsedCanceledTotal")));
						int couponUsedCanceledPromotionTotal = StrUtils.parseInt(String.valueOf(val.get("couponUsedCanceledPromotionTotal")));
						header = "header" + cnt++;

						if ("weekly".equals(option1)) {
							String headerLast = "header" + list.size();

							if ("header1".equals(header)) label = fromdate + " ~ " + label.split(" ~ ", -1)[1];
							if (headerLast.equals(header)) label = label.split(" ~ ", -1)[0] + " ~ " + todate;
						}
						linkedHeader.put(header, label);
						linkedIssuedTotal.put(header, couponIssuedTotal);
						linkedIssuedAccmlTotal.put(header, couponIssuedTotal - couponIssuedPromotionTotal);
						linkedIssuedPromotionTotal.put(header, couponIssuedPromotionTotal);
						linkedIssuedCanceledTotal.put(header, couponIssuedCanceledTotal);
						linkedIssuedCanceledAccmlTotal.put(header, couponIssuedCanceledTotal - couponIssuedCanceledPromotionTotal);
						linkedIssuedCanceledPromotionTotal.put(header, couponIssuedCanceledPromotionTotal);
						linkedUsedTotal.put(header, couponUsedTotal);
						linkedUsedAccmlTotal.put(header, couponUsedTotal - couponUsedPromotionTotal);
						linkedUsedPromotionTotal.put(header, couponUsedPromotionTotal);
						linkedUsedCanceledTotal.put(header, couponUsedCanceledTotal);
						linkedUsedCanceledAccmlTotal.put(header, couponUsedCanceledTotal - couponUsedCanceledPromotionTotal);
						linkedUsedCanceledPromotionTotal.put(header, couponUsedCanceledPromotionTotal);
					}
					list.clear();
					list.add(linkedHeader);
					list.add(linkedIssuedTotal);
					list.add(linkedIssuedAccmlTotal);
					list.add(linkedIssuedPromotionTotal);
					list.add(linkedIssuedCanceledTotal);
					list.add(linkedIssuedCanceledAccmlTotal);
					list.add(linkedIssuedCanceledPromotionTotal);
					list.add(linkedUsedTotal);
					list.add(linkedUsedAccmlTotal);
					list.add(linkedUsedPromotionTotal);
					list.add(linkedUsedCanceledTotal);
					list.add(linkedUsedCanceledAccmlTotal);
					list.add(linkedUsedCanceledPromotionTotal);
				}

    			List<LinkedHashMap<String, Object>> dayMonthList = new ArrayList<LinkedHashMap<String, Object>>();

				params.put("tableNm", "TB_SVC_COUPON_LOG");

    			Long timeLag = doDiffOfDate(dateFormat, fromdate, todate);
    			String logTp = "";

            	for (int i = 1; i < 3; i++) {
            		if (i == 1) logTp = codeUtil.getBaseCodeByAlias("coupon-log-issue");
            		else if (i == 2) logTp = codeUtil.getBaseCodeByAlias("coupon-log-use");

            		params.put("logTp", logTp);

            		result.put("hourValueChart" + i, customRewardMapper.getRewardHourChart(params));
            		result.put("dayofweekValueChart" + i, customRewardMapper.getRewardDayofweekChart(params));
    			}

    			String dayMonth = "";
            	for (int i = 1; i < 4; i++) {
            		if (i == 1) logTp = codeUtil.getBaseCodeByAlias("coupon-log-issue");
            		else if (i == 2) logTp = codeUtil.getBaseCodeByAlias("coupon-log-use");
            		else if (i == 3) logTp = codeUtil.getBaseCodeByAlias("coupon-log-cancel");

            		params.put("logTp", logTp);

            		if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
            			dayMonth = "day";
            			dayMonthList = customRewardMapper.getRewardDayChart(params);
            		}
            		else {
            			dayMonth = "month";
            			dayMonthList = customRewardMapper.getRewardMonthChart(params);
            		}
            		result.put("dayMonthChart" + i, dayMonthList);
            	}
    			result.put("dayMonth", dayMonth);

			}
		}

		result.put("fromdate", fromdate);
		result.put("todate", todate);
		result.put("list", list);
		result.put("success", true);

		return result;
	}

	@RequestMapping("/StampLog")
	  public Map<Object,Object> StampLog(
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
	    Map<Object,Object> result = new HashMap<>();
	    Map<String,String> baseParam = new HashMap<>();	// 검색데이터
	    Map<String,String> userInfo = new HashMap<>();	// 로그인 사용자 정보
	    
	    logger.debug("★★★★★★★★★★★★★★★★★★★★");
	    logger.debug(params.toString());
	    logger.debug("★★★★★★★★★★★★★★★★★★★★");
	    
	    if (params.get("data") != null) {
	    	baseParam = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
		}
	    
	    if (params.get("userInfo") != null) {
	    	userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(), new TypeReference<Map<String, String>>() {});
		}
	    
	    switch(method){
	    case GET :
	      
	      if(!String.valueOf(params.get("draw")).equals("null")){        
	        
	    	logger.debug("★★★★★★★★★★★★★★★★★★★★");
	  	    logger.debug(baseParam.toString());
	  	    logger.debug(userInfo.toString());
	  	    logger.debug("★★★★★★★★★★★★★★★★★★★★");
	  	    
	        /*
	         * DATATABLE 검색 값 세팅
	         */
	        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
	        if (!searchString.isEmpty()) {
	        	params.put("searchString", searchString);
	        }
	        /*
	         * DATATABLE 컬럼 정렬값 세팅
	         */
	        if(params.get("order[0][column]") != null){
	          String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
	              +" "+String.valueOf(params.get("order[0][dir]"));
	          params.put("orderby",orderby);
	        }
	        
	        List<LinkedHashMap<String,String>> list = customRewardMapper.getStampList(baseParam, new RowBounds(start, length));

	        result.put("list", list);
	        result.put("recordsTotal", customRewardMapper.getCountStampList(baseParam));
	        result.put("recordsFiltered", customRewardMapper.getCountStampList(baseParam));
	      } else {
	        result.put("data", customRewardMapper.getStampList(baseParam, new RowBounds(0, 1)));
	      }

	      result.put("success", true);

	      break;
	    default :
	      result.put("success", false);
	      result.put("errMsg", "not supported method.");
	    }

	    return result;
	  }
	
	@Transactional
	@RequestMapping("/CouponLog")
	  public Map<Object,Object> CouponLog(
	      @RequestParam(required=false) Map<Object,Object> params,
	      @RequestParam(required=false, value="start", defaultValue="0") int start,
	      @RequestParam(required=false, value="length", defaultValue="10") int length,
	      HttpMethod method) throws InvalidJsonException, UnsupportedEncodingException{
	    Map<Object,Object> result = new HashMap<>();

	    switch(method){
	    case GET :
	      Map<Object,Object> baseParam = new HashMap<>();
	      try {
	    	  if(!String.valueOf(params.get("draw")).equals("null")){
				
	  	        baseParam.put("brandId", params.get("brandId"));
	  	        baseParam.put("storeId", params.get("storeId"));
	  	        
	  	        /*
	  	         * DATATABLE 검색 값 세팅
	  	         */
	  	        String searchString = (String) (params.get("search[value]") != null ? params.get("search[value]") : ""); 
	  	        if (!searchString.isEmpty()) {
	  	        	params.put("searchString", searchString);
	  	        }
	  	        /*
	  	         * DATATABLE 컬럼 정렬값 세팅
	  	         */
	  	        if(params.get("order[0][column]") != null){
	  	          String orderby = String.valueOf(params.get("columns["+String.valueOf(params.get("order[0][column]"))+"][name]"))
	  	              +" "+String.valueOf(params.get("order[0][dir]"));
	  	          params.put("orderby",orderby);
	  	        }
	  	        
	  	        List<LinkedHashMap<String,Object>> list = customRewardMapper.getCouponList(params, new RowBounds(start, length));

	  	        result.put("list", list);
	  	        result.put("recordsTotal", customRewardMapper.getCountCouponList(baseParam));
	  	        result.put("recordsFiltered", customRewardMapper.getCountCouponList(params));
	  	      } else {
	  	        baseParam.put("id", params.get("id"));
	  	        result.put("data", customRewardMapper.getCouponList(baseParam, new RowBounds(0, 1)));
	  	      }

	  	      result.put("success", true);	
	      } catch (Exception e) {
	    	  result.put("success", false);
	    	  result.put("errMsg", false);
	      }
	      

	      break;
	    case PUT :
	        
	       Map<String,Object> cancleTarget = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, Object>>() {});	       
	       
	       /*
    	    * 푸폰 사용/발행 취소
    	    * 대상테이블 TB_SVC_USER_COUPON - COUPON_ST(402002:사용취소, 402004:발행취소) 변경
    	    *       , TB_SVC_COUPON_LOG  - LOG_TP(402002:사용취소, 402004:발행취소) 로그데이터 신규 등록
    	    */
    	   Map<String,String> originalData = customRewardMapper.getCouponList(cancleTarget);
    	   
    	   cancleTarget.put("adminId", params.get("username"));
    	   
    	   if(customRewardMapper.updateCancleCoupon(cancleTarget) > 0){
    		   
				Map<String, String> userCouponLogData = new HashMap<>();
				
				userCouponLogData.put("userId", String.valueOf(originalData.get("userId"))); 				// 사용자 ID
				userCouponLogData.put("brandId", String.valueOf(originalData.get("brandId")));
				userCouponLogData.put("storeId", String.valueOf(originalData.get("storeId")));    	
				userCouponLogData.put("couponId", String.valueOf(originalData.get("couponId"))); 			// 쿠폰 ID
				userCouponLogData.put("promotionId", originalData.get("promotionId")); 	// 프로모션 ID    				
				userCouponLogData.put("couponNm", originalData.get("couponNm")); 			// 쿠폰 ID			
				userCouponLogData.put("couponCd", originalData.get("couponCd"));			// 쿠폰CD
				userCouponLogData.put("logTp", (String) cancleTarget.get("couponSt"));				// 쿠폰로그 유형 402001 발행
				
				HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		        String ip = req.getHeader("X-FORWARDED-FOR");
		        if (ip == null){
		            ip = req.getRemoteAddr();
		        }
		        userCouponLogData.put("srcIp", ip);
    		   
				customMarketingMapper.insertUserCouponLog(userCouponLogData);
			}
    	   result.put("success", true);
			
	    	break;
	    case POST :
	    	
	    	result.put("success", true);
	    	break; 
	    default :
	      result.put("success", false);
	      result.put("errMsg", "not supported method.");
	    }

	    return result;
	  }
	
	/**
	 * Reward Summary List
	 * @param dateList
	 * @param logList
	 * @param option1
	 * @param option2
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<LinkedHashMap<String, Object>> rewardSumList(
		List<String> headerList, List<LinkedHashMap<String, Object>> logList, String option1, String option2) throws Exception {

    	List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> linkedHeader = new LinkedHashMap<String, Object>();
		int cnt = 1;

		linkedHeader.put("header", "Store");
		for (String val : headerList) {
			linkedHeader.put("header"+cnt++, val);
		}
		list.add(linkedHeader);

		for (int i = 0; i < list.size();) {
			Object obj = list.get(i);
			Map<String, String> map = null;

			if (obj instanceof Map) map = (Map)obj;
			else map = BeanUtils.describe(obj);

			for (LinkedHashMap<String, Object> log : logList) {
				Set set = map.entrySet();
				Iterator it = set.iterator();
				LinkedHashMap<String, Object> linkedCnt = new LinkedHashMap<String, Object>();

				while (it.hasNext()) {
					Map.Entry fn = (Map.Entry)it.next();

					linkedCnt.put("header", log.get("storeNm"));

					int whenCnt = 0;
					String whenData = (String) log.get("when");

					if (StringUtils.hasText(whenData)) {
						String [] arrData = whenData.split(",");
						List<String> whenList = Arrays.asList(arrData);

						String val = String.valueOf(fn.getValue());
						for (String when : whenList) {
							if ("weekly".equals(option1)) val = val.split("/", -1)[0];
							if (val.equals(when)) whenCnt = whenCnt + 1;
						}
					}
					linkedCnt.put((String) fn.getKey(), whenCnt);
				}
				list.add(linkedCnt);
			}
			break;
		}
		return list;
	}

	/**
	 * Reward Summary HeaderList
	 * @param type
	 * @param fromdate
	 * @param todate
	 * @return
	 * @throws Exception
	 */
    public static List<String> headerList(String type, String fromdate, String todate) throws Exception {
    	List<String> list = new ArrayList<String>();

		Long timeLag = doDiffOfDate(dateFormat, fromdate, todate);
  	    Calendar cal = Calendar.getInstance();
  	    SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

        switch (type) {
        case "daily" :
	  	    for (int i = 0; i < timeLag; i++) {
	      		cal.setTime(sdformat.parse(fromdate));
	      		cal.add(Calendar.DATE, 1);
	      		list.add(fromdate);
	      		fromdate = sdformat.format(cal.getTime());
	  	    }
        	break;

        case "weekly" :
        	String begin = fromdate;
      		Date endDate = sdformat.parse(todate);
	  	    for (int i = 0; i < timeLag; i++) {
		  	    cal.setTime(sdformat.parse(fromdate));
		  	    cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

		  	    if (i > 0) cal.add(Calendar.DATE, 7);

	      		fromdate = sdformat.format(cal.getTime());

	      		if (i > 0) {
	      			cal.add(Calendar.DATE, -6);
	      			begin = sdformat.format(cal.getTime());
	      		}

	      		Date beginDate = sdformat.parse(fromdate);
	            int compare = beginDate.compareTo(endDate);

	            if (compare > 0) fromdate = todate;

	      		list.add(cal.get(Calendar.WEEK_OF_YEAR)+"/"+begin+" ~ "+fromdate);

	      		if (compare > 0 || compare == 0) break;
	  	    }
	  	    break;

        case "monthly" :
        	int strtYear = Integer.parseInt(fromdate.substring(0, 4));
        	int strtMonth = Integer.parseInt(fromdate.substring(5, 7));

        	int endYear = Integer.parseInt(todate.substring(0, 4));
        	int endMonth = Integer.parseInt(todate.substring(5, 7));

        	int month = (endYear - strtYear)* 12 + (endMonth - strtMonth);

	  	    for (int i = 0; i < month + 1; i++) {
	      		cal.setTime(sdformat.parse(fromdate));
	      		list.add(fromdate.substring(0, 7));
	      		cal.add(Calendar.MONTH, 1);
	      		fromdate = sdformat.format(cal.getTime());
	  	    }
        	break;

        default :
        	break;
        }
		return list;
    }

    /**
     * Store - StampAcc
     * @param StampAcc
     * @param params
     * @param start
     * @param length
     * @param method
     * @return
     * @throws InvalidJsonException
     * @throws ParseException
     */
    @RequestMapping("/StampAcc")
    public Map<Object,Object> StampAcc(
  	  HttpSession session,
        @RequestParam(required=false) Map<Object,String> params,
        @RequestParam(required=false, value="start", defaultValue="0") int start,
        @RequestParam(required=false, value="length", defaultValue="10") int length,
        HttpMethod method) throws Exception, InvalidJsonException, ParseException {
  	  
      Map<Object,Object> result = new HashMap<>();
      Map<String, String> search = new HashMap<>();
      SvcFranchiseExample example = new SvcFranchiseExample();

      logger.debug("★★★★★★★★★★★★★★★★★★");
      logger.debug(params.toString());
      logger.debug("★★★★★★★★★★★★★★★★★★");
      
      // 사용자 정보 저장
      result = AdminHandler.setSessionInfo(result, session, "1");

      search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
      
      switch(method){
      case GET :
      	HashMap<String, Object> userInfo = AdminHandler.getUserInfo(session); 
      	
      	if(!String.valueOf(params.get("draw")).equals("null")){
      		
            List<LinkedHashMap<String, String>> list = customRewardMapper.getStampAccList(search, new RowBounds(start, length));
      		
            result.put("list",list);
      		result.put("recordsTotal", customRewardMapper.getCountStampAccList(search));
            result.put("recordsFiltered", customRewardMapper.getCountStampAccList(search));
            
      		result.put("success", true);

        } else {
        	List<LinkedHashMap<String, String>> list = new ArrayList<>();
        	// 엑셀출력
        	if("1".equals(params.get("gubun"))){
            	list = customRewardMapper.getStampAccList(search);        		
        	}else{
        		list = customRewardMapper.getStampAccList(search, new RowBounds(start, length));
        	}
        	
        	result.put("list",list);
        	result.put("recordsTotal", customRewardMapper.getCountStampAccList(search));
            result.put("recordsFiltered", customRewardMapper.getCountStampAccList(search));
            result.put("success", true);
        	  
          }
        result.put("success", true);
      	
        break;

      case POST :
    	  
        result.put("sucess", false);
    	result.put("errMsg", "");
        break;

      case PUT :
    	result.put("sucess", false);
      	result.put("errMsg", "");

        break;
      
      case DELETE :
   	    result.put("sucess", false);
        result.put("errMsg", "");

        break;

      default :
        result.put("success", false);
        result.put("errMsg", "not supported method.");
      }

      return result;
    }
    
    /**
     * 날짜 차이 계산
     * @param format
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static long doDiffOfDate(String format, String start, String end) throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat(format);
        Date beginDate = sdformat.parse(start);
        Date endDate = sdformat.parse(end);

        long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays + 1;
    }

}
