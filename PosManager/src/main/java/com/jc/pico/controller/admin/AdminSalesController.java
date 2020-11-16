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

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcFranchise;
import com.jc.pico.bean.SvcSales;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.mapper.BaseBcodeMapper;
import com.jc.pico.mapper.SvcSalesMapper;
import com.jc.pico.mapper.UserBackofficeMenuMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.admin.AdminCommonService;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.StrUtils;
import com.jc.pico.utils.customMapper.admin.CustomSalesMapper;

@Controller
@ResponseBody
@RequestMapping("/admin/model/sales")
public class AdminSalesController {

	protected static Logger logger = LoggerFactory.getLogger(AdminSalesController.class);

	@Autowired
	CodeUtil codeUtil;

	@Autowired
	BaseBcodeMapper baseCodeMapper;

	@Autowired
	UserBackofficeMenuMapper userBackofficeMenuMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	CustomSalesMapper customSalesMapper;

	@Autowired
	SvcSalesMapper svcSalesMapper;

	@Autowired
	SqlSessionTemplate sessionTemplate;

	@Autowired
	AdminCommonService adminCommonService;

	protected static String dateFormat = "yyyy-MM-dd";

	/**
	 * Sales - FranchiseSum 해당메뉴사용안함
	 * 
	 * @param franchise
	 * @param params
	 * @param start
	 * @param length
	 * @param method
	 * @return
	 * @throws InvalidJsonException
	 */
	@Deprecated
	@RequestMapping("/FranchiseSum")
	public Map<Object, Object> FranchiseSum(@RequestParam(required = false) SvcFranchise franchise,
			@RequestParam(required = false) Map<Object, Object> params,
			@RequestParam(required = false, value = "start", defaultValue = "0") int start,
			@RequestParam(required = false, value = "length", defaultValue = "10") int length, HttpMethod method)
			throws InvalidJsonException {
		Map<Object, Object> result = new HashMap<>();

		if (franchise == null && params.get("data") != null) {
			franchise = JsonConvert.JsonConvertObject(params.get("data").toString(), SvcFranchise.class);
		}

		switch (method) {
		case GET:

			result.put("success", true);

			break;
		case POST:
			break;
		case PUT:

			break;
		// case DELETE :
		// result.put("success",
		// svcFranchiseMapper.deleteByPrimaryKey(corpManage.getCo_cd()) == 1 ?
		// true : false );
		// result.put("errMsg", "");
		// break;
		default:
			result.put("success", false);
			result.put("errMsg", "not supported method.");
		}

		return result;
	}

	/**
	 * headerList Reward Summary HeaderList
	 * 
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
		case "daily":
			for (int i = 0; i < timeLag; i++) {
				cal.setTime(sdformat.parse(fromdate));
				cal.add(Calendar.DATE, 1);
				list.add(fromdate);
				fromdate = sdformat.format(cal.getTime());
			}
			break;

		case "weekly":
			String begin = fromdate;
			Date endDate = sdformat.parse(todate);
			for (int i = 0; i < timeLag; i++) {
				cal.setTime(sdformat.parse(fromdate));
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

				if (i > 0)
					cal.add(Calendar.DATE, 7);

				fromdate = sdformat.format(cal.getTime());

				if (i > 0) {
					cal.add(Calendar.DATE, -6);
					begin = sdformat.format(cal.getTime());
				}

				Date beginDate = sdformat.parse(fromdate);
				int compare = beginDate.compareTo(endDate);

				if (compare > 0)
					fromdate = todate;

				list.add(cal.get(Calendar.WEEK_OF_YEAR) + "/" + begin + " ~ " + fromdate);

				if (compare > 0 || compare == 0)
					break;
			}
			break;

		case "monthly":
			int strtYear = Integer.parseInt(fromdate.substring(0, 4));
			int strtMonth = Integer.parseInt(fromdate.substring(5, 7));

			int endYear = Integer.parseInt(todate.substring(0, 4));
			int endMonth = Integer.parseInt(todate.substring(5, 7));

			int month = (endYear - strtYear) * 12 + (endMonth - strtMonth);

			for (int i = 0; i < month + 1; i++) {
				cal.setTime(sdformat.parse(fromdate));
				list.add(fromdate.substring(0, 7));
				cal.add(Calendar.MONTH, 1);
				fromdate = sdformat.format(cal.getTime());
			}
			break;

		default:
			break;
		}
		return list;
	}

	/**
	 * 날짜 차이 계산
	 * 
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

	public static void main(String[] args) throws Exception {
		System.out.println(headerList("weekly", "2016-07-03", "2016-07-09"));

	}

	/**
	 * Sales Summary List
	 * 
	 * @param dateList
	 * @param logList
	 * @param option1
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<LinkedHashMap<String, Object>> salesSumList(List<String> headerList,
			List<LinkedHashMap<String, Object>> logList, String option1) throws Exception {

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> linkedHeader = new LinkedHashMap<String, Object>();
		int cnt = 1;

		linkedHeader.put("header", "Store");
		for (String val : headerList) {
			linkedHeader.put("header" + cnt++, val);
		}
		list.add(linkedHeader);

		for (int i = 0; i < list.size();) {
			Object obj = list.get(i);
			Map<String, String> map = null;

			if (obj instanceof Map)
				map = (Map) obj;
			else
				map = BeanUtils.describe(obj);

			for (LinkedHashMap<String, Object> log : logList) {
				Set set = map.entrySet();
				Iterator it = set.iterator();
				LinkedHashMap<String, Object> linkedCnt = new LinkedHashMap<String, Object>();

				while (it.hasNext()) {
					Map.Entry fn = (Map.Entry) it.next();

					linkedCnt.put("header", log.get("storeNm"));

					int whenCnt = 0;
					String whenData = (String) log.get("when");

					if (StringUtils.hasText(whenData)) {
						String[] arrData = whenData.split(",");
						List<String> whenList = Arrays.asList(arrData);

						String val = String.valueOf(fn.getValue());
						for (String when : whenList) {
							if ("weekly".equals(option1))
								val = val.split("/", -1)[0];
							if (val.equals(when))
								whenCnt = whenCnt + 1;
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
	 * Sales - BrandSum
	 * 
	 * @param franchise
	 * @param params
	 * @param start
	 * @param length
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/BrandSum")
	public Map<Object, Object> BrandSum(@RequestParam(required = false) Map<Object, Object> params, HttpMethod method)
			throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<Object, Object> search = new HashMap<>();
		Map<String, String> userInfo = new HashMap<>();

		logger.info("★★★★★★★★★ BrandSum ★★★★★★★★★★★");
		logger.info(" params : " + params.toString());
		logger.info("★★★★★★★★★ BrandSum ★★★★★★★★★★★");

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),
					new TypeReference<Map<Object, Object>>() {
					});
		}

		if (params.get("userInfo") != null) {
			userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(),
					new TypeReference<Map<String, String>>() {
					});
		}

		logger.info("★★★★★★★★★ BrandSum ★★★★★★★★★★★");
		logger.info(" search : " + search.toString());
		logger.info(" userInfo : " + userInfo.toString());
		logger.info("★★★★★★★★★ BrandSum ★★★★★★★★★★★");

		// 브랜드의 통화에 따라 소숫점 이하 단위 절삭을 달리 함
		search.put("currencyFraction",
				adminCommonService.getBrandFractionDigits(Long.parseLong(String.valueOf(search.get("brandId")))));

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

		// 브랜드에 속한 매장별 판매 summary
		if ("summary".equals(search.get("option1"))) {
			try {
				list = customSalesMapper.getSalesSummaryList(search);

				result.put("fromdate", (String) search.get("fromdate"));
				result.put("todate", (String) search.get("todate"));
				result.put("list", list);

				result.put("success", true);
				result.put("errMsg", "");

			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("errMsg", "false select getBrandSummaryList !! ");
			}
		} else if ("monthly".equals(search.get("option1"))) {
			try {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

				cal.setTime(sdformat.parse((String) search.get("fromdate")));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				search.put("fromdate", sdformat.format(cal.getTime()));

				cal.setTime(sdformat.parse((String) search.get("todate")));
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				search.put("todate", sdformat.format(cal.getTime()));

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getBrandSummaryList !! ");
			}

		}

		if (StringUtils.hasText((String) search.get("option2"))) {

			List<String> headerList = headerList((String) search.get("option1"), (String) search.get("fromdate"),
					(String) search.get("todate"));
			List<LinkedHashMap<String, Object>> logList = new ArrayList<LinkedHashMap<String, Object>>();
			// 달력 리스트
			List<LinkedHashMap<String, Object>> calendarList = new ArrayList<LinkedHashMap<String, Object>>();

			if ("monthly".equals(search.get("option1"))) {

				try {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

					cal.setTime(sdformat.parse((String) search.get("fromdate")));
					cal.set(Calendar.DAY_OF_MONTH, 1);
					search.put("fromdate", sdformat.format(cal.getTime()));

					cal.setTime(sdformat.parse((String) search.get("todate")));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					search.put("todate", sdformat.format(cal.getTime()));

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getBrandSummaryList !! ");
				}
			}

			// 01.달력 검색(검색 기간 범위의 날짜형식들 리턴: param : daily/weekly/monthly)
			if ("daily".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYMDList(search);
			} else if ("weekly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearWeekList(search);
			} else if ("monthly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearMonthList(search);
			}

			if (calendarList != null) {
				if (!calendarList.isEmpty()) {
					// 달력목록 추가(to search)
					search.put("calendarList", calendarList);
				}
			}

			// 02.브랜드별 판매현황 검색(테이블)
			logList = customSalesMapper.getBrandSummaryList(search);

			list = salesSumList(headerList, logList, (String) search.get("option1"));

			Long timeLag = doDiffOfDate(dateFormat, (String) search.get("fromdate"), (String) search.get("todate"));
			String dayMonth = "";

			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
				dayMonth = "day";
				result.put("dayChart", customSalesMapper.getSalesDayChart(search));
			} else {
				dayMonth = "month";
				result.put("monthChart", customSalesMapper.getSalesMonthChart(search));
			}

			result.put("dayMonth", dayMonth);
			result.put("hourChart", customSalesMapper.getSalesHourChart(search));
			result.put("dayofweekChart", customSalesMapper.getSalesDayofweekChart(search));
			result.put("list", list);

			// 테이블 헤더
			result.put("tableHeader", calendarList);
			// 테이블 데이터
			result.put("tableList", logList);

			result.put("success", true);

		}

		return result;
	}

	/**
	 * Sales - SalesSum
	 * 
	 * @param franchise
	 * @param params
	 * @param start
	 * @param length
	 * @param method
	 * @return
	 * @throws InvalidJsonException
	 */
	@RequestMapping("/SalesSum")
	public Map<Object, Object> SalesSum(@RequestParam(required = false) Map<Object, Object> params, HttpMethod method)
			throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<Object, Object> search = new HashMap<>();
		Map<String, String> userInfo = new HashMap<>();

		logger.info("★★★★★★★★★ SalesSum ★★★★★★★★★★★");
		logger.info(" params : " + params.toString());
		logger.info("★★★★★★★★★ SalesSum ★★★★★★★★★★★");

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),
					new TypeReference<Map<Object, Object>>() {
					});
		}

		if (params.get("userInfo") != null) {
			userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(),
					new TypeReference<Map<String, String>>() {
					});
		}

		logger.info("★★★★★★★★★ SalesSum ★★★★★★★★★★★");
		logger.info(" search : " + search.toString());
		logger.info(" userInfo : " + userInfo.toString());
		logger.info("★★★★★★★★★ SalesSum ★★★★★★★★★★★");

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

		// 브랜드에 속한 매장별 판매 summary
		if ("summary".equals(search.get("option1"))) {

			try {
				list = customSalesMapper.getAllSalesSummaryList(search);

				result.put("list", list);

				result.put("success", true);
				result.put("errMsg", "");

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getSalesSummaryList !! ");
			}
		}
		//
		else {
			if ("daily".equals(search.get("option1"))) {
				try {

					list = customSalesMapper.getSalesDayOfMonthList(search);

					result.put("list", list);

					result.put("success", true);
					result.put("errMsg", "");

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getSalesDayOfWeekChart !! ");
				}
			} else if ("weekly".equals(search.get("option1"))) {
				try {

					list = customSalesMapper.getSalesDayOfWeekList(search);

					result.put("list", list);

					result.put("success", true);
					result.put("errMsg", "");

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getSalesDayOfWeekChart !! ");
				}
			} else if ("monthly".equals(search.get("option1"))) {
				try {

					list = customSalesMapper.getSalesMonthOfYearList(search);

					result.put("list", list);

					result.put("success", true);
					result.put("errMsg", "");

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getSalesMonthOfYearList !! ");
				}
			}

			Long timeLag = doDiffOfDate(dateFormat, (String) search.get("fromdate"), (String) search.get("todate"));

			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
				result.put("dayMonth", "day");
				result.put("dayChart", customSalesMapper.getSalesDayOfMonthList(search));
			} else {
				result.put("dayMonth", "month");
				result.put("monthChart", customSalesMapper.getSalesMonthOfYearList(search));
			}

			result.put("hourChart", customSalesMapper.getSalesHourOfDayList(search));
			result.put("dayofweekChart", customSalesMapper.getSalesDayOfWeekList(search));
		}

		return result;
	}

	/**
	 * SalesGoal - 매출목표관리
	 * 
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/salesGoal")
	public Map<Object, Object> SalesGoal(@RequestParam(required = false) Map<Object, Object> params, HttpMethod method)
			throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<String, String> search = new HashMap<>();
		List<LinkedHashMap<String, Object>> salesGoalList = new ArrayList<LinkedHashMap<String, Object>>();
		int resultCnt = 1;

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),
					new TypeReference<Map<String, String>>() {
					});
		}

		// Setting Parameter
		String brandId = search.get("brandId");
		String storeId = search.get("storeId");
		String year = search.get("year");

		params.put("brandId", brandId);
		params.put("storeId", storeId);
		params.put("year", year);

		switch (method) {
		case POST:

			Double month1GoalAmt = Double.parseDouble(String.valueOf(search.get("month1GoalAmt")));
			Double month2GoalAmt = Double.parseDouble(String.valueOf(search.get("month2GoalAmt")));
			Double month3GoalAmt = Double.parseDouble(String.valueOf(search.get("month3GoalAmt")));
			Double month4GoalAmt = Double.parseDouble(String.valueOf(search.get("month4GoalAmt")));
			Double month5GoalAmt = Double.parseDouble(String.valueOf(search.get("month5GoalAmt")));
			Double month6GoalAmt = Double.parseDouble(String.valueOf(search.get("month6GoalAmt")));
			Double month7GoalAmt = Double.parseDouble(String.valueOf(search.get("month7GoalAmt")));
			Double month8GoalAmt = Double.parseDouble(String.valueOf(search.get("month8GoalAmt")));
			Double month9GoalAmt = Double.parseDouble(String.valueOf(search.get("month9GoalAmt")));
			Double month10GoalAmt = Double.parseDouble(String.valueOf(search.get("month10GoalAmt")));
			Double month11GoalAmt = Double.parseDouble(String.valueOf(search.get("month11GoalAmt")));
			Double month12GoalAmt = Double.parseDouble(String.valueOf(search.get("month12GoalAmt")));

			params.put("month1_goal_amt", month1GoalAmt);
			params.put("month2_goal_amt", month2GoalAmt);
			params.put("month3_goal_amt", month3GoalAmt);
			params.put("month4_goal_amt", month4GoalAmt);
			params.put("month5_goal_amt", month5GoalAmt);
			params.put("month6_goal_amt", month6GoalAmt);
			params.put("month7_goal_amt", month7GoalAmt);
			params.put("month8_goal_amt", month8GoalAmt);
			params.put("month9_goal_amt", month9GoalAmt);
			params.put("month10_goal_amt", month10GoalAmt);
			params.put("month11_goal_amt", month11GoalAmt);
			params.put("month12_goal_amt", month12GoalAmt);

			/****************** 서비스를 호출하기 위한 로직 시작 ******************/
			resultCnt = customSalesMapper.insertUpdateSalesGoal(params);
			/****************** 서비스를 호출하기 위한 로직 종료 ******************/

			break;
		case GET:

			/****************** 서비스를 호출하기 위한 로직 시작 ******************/
			salesGoalList = customSalesMapper.getSalesGoalList(params);
			/****************** 서비스를 호출하기 위한 로직 종료 ******************/

			break;
		default:
			break;
		}

		result.put("salesGoalList", salesGoalList);
		if (resultCnt > 0) {
			result.put("success", true);
		} else {
			result.put("success", false);
		}

		return result;
	}

	/**
	 * Sales > Analyze performance against goals - 매출목표관리
	 * 
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/anlsGoals")
	public Map<Object, Object> AnlsGoals(@RequestParam(required = false) Map<Object, Object> params, HttpMethod method)
			throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<String, String> search = new HashMap<>();
		List<LinkedHashMap<String, Object>> apGoalList = new ArrayList<LinkedHashMap<String, Object>>();
		List<LinkedHashMap<String, Object>> apSalesList = new ArrayList<LinkedHashMap<String, Object>>();

		int resultCnt = 1;

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),
					new TypeReference<Map<String, String>>() {
					});
		}

		// Setting Parameter
		String brandId = search.get("brandId");
		String storeId = search.get("storeId");
		String year = search.get("year");

		params.put("brandId", brandId);
		params.put("storeId", storeId);
		params.put("year", year);

		switch (method) {
		case POST:
			break;
		case GET:

			/****************** 서비스를 호출하기 위한 로직 시작 ******************/
			apGoalList = customSalesMapper.getAPGoalList(params);
			apSalesList = customSalesMapper.getAPSalesList(params);
			/****************** 서비스를 호출하기 위한 로직 종료 ******************/

			break;
		default:
			break;
		}

		result.put("apGoalList", apGoalList);
		result.put("apSalesList", apSalesList);
		if (resultCnt > 0) {
			result.put("success", true);
		} else {
			result.put("success", false);
		}

		return result;
	}

	/**
	 * Sales - ItemSales
	 * 
	 * @param franchise
	 * @param params
	 * @param start
	 * @param length
	 * @param method
	 * @return
	 * @throws InvalidJsonException
	 */
	@RequestMapping("/ItemSales")
	public Map<Object, Object> ItemSales(@RequestParam(required = false) Map<Object, Object> params, HttpMethod method)
			throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<Object, Object> search = new HashMap<>();
		Map<String, String> userInfo = new HashMap<>();

		logger.info("★★★★★★★★★ ItemSales ★★★★★★★★★★★");
		logger.info(" params : " + params.toString());
		logger.info("★★★★★★★★★ ItemSales ★★★★★★★★★★★");

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),
					new TypeReference<Map<Object, Object>>() {
					});
		}

		if (params.get("userInfo") != null) {
			userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(),
					new TypeReference<Map<String, String>>() {
					});
		}

		logger.info("★★★★★★★★★ ItemSales ★★★★★★★★★★★");
		logger.info(" search : " + search.toString());
		logger.info(" userInfo : " + userInfo.toString());
		logger.info("★★★★★★★★★ ItemSales ★★★★★★★★★★★");

		// 브랜드의 통화에 따라 소숫점 이하 단위 절삭을 달리 함
		search.put("currencyFraction", adminCommonService.getBrandFractionDigits(Long.parseLong(String.valueOf(search.get("brandId")))));

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

		// 브랜드에 속한 매장별 판매 summary
		if ("summary".equals(search.get("option1"))) {
			try {
				list = customSalesMapper.getItemSalesSummaryList(search);

				result.put("fromdate", search.get("fromdate"));
				result.put("todate", search.get("todate"));
				result.put("list", list);
				result.put("ItemSalesTop5Chart", customSalesMapper.getItemSalesTop5SummaryChart(search));

				result.put("success", true);
				result.put("errMsg", "");

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getItemSalesSummaryList !! ");
			}
		} else if ("monthly".equals(search.get("option1"))) {

			try {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

				cal.setTime(sdformat.parse((String) search.get("fromdate")));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				search.put("fromdate", sdformat.format(cal.getTime()));

				cal.setTime(sdformat.parse((String) search.get("todate")));
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				search.put("todate", sdformat.format(cal.getTime()));

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getItemSalesSummaryList !! ");
			}
		}

		if (StringUtils.hasText((String) search.get("option2"))) {

			List<String> headerList = headerList((String) search.get("option1"), (String) search.get("fromdate"),
					(String) search.get("todate"));
			List<LinkedHashMap<String, Object>> logList = new ArrayList<LinkedHashMap<String, Object>>();
			// 달력 리스트
			List<LinkedHashMap<String, Object>> calendarList = new ArrayList<LinkedHashMap<String, Object>>();

			if ("monthly".equals(search.get("option1"))) {

				try {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

					cal.setTime(sdformat.parse((String) search.get("fromdate")));
					cal.set(Calendar.DAY_OF_MONTH, 1);
					search.put("fromdate", sdformat.format(cal.getTime()));

					cal.setTime(sdformat.parse((String) search.get("todate")));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					search.put("todate", sdformat.format(cal.getTime()));

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getSalesSummaryList !! ");
				}
			}

			// 01.달력 검색(검색 기간 범위의 날짜형식들 리턴: param : daily/weekly/monthly)
			if ("daily".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYMDList(search);
			} else if ("weekly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearWeekList(search);
			} else if ("monthly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearMonthList(search);
			}

			if (calendarList != null) {
				if (!calendarList.isEmpty()) {
					// 달력목록 추가(to params)
					search.put("calendarList", calendarList);
				}
			}

			// 02.상품별 판매현황 검색(테이블)
			logList = customSalesMapper.getItemSalesList(search);

			list = salesSumList(headerList, logList, (String) search.get("option1"));

			Long timeLag = doDiffOfDate(dateFormat, (String) search.get("fromdate"), (String) search.get("todate"));

			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
				result.put("dayMonth", "day");
				result.put("dayChart", customSalesMapper.getItemDayOfMonthChart(search));
			} else {
				result.put("dayMonth", "month");
				result.put("monthChart", customSalesMapper.getItemMonthOfYeeaChart(search));
			}

			result.put("hourChart", customSalesMapper.getItemHourOfDayChart(search));
			result.put("dayofweekChart", customSalesMapper.getItemDayOfWeekChart(search));
			result.put("list", list);

			// 테이블 헤더
			result.put("tableHeader", calendarList);
			// 테이블 데이터
			result.put("tableList", logList);

			result.put("success", true);
		}
		return result;
	}

	/**
	 * Sales - CategorySales
	 * 
	 * @param franchise
	 * @param params
	 * @param start
	 * @param length
	 * @param method
	 * @return
	 * @throws InvalidJsonException
	 */
	@RequestMapping("/CategorySales")
	public Map<Object, Object> CategorySales(@RequestParam(required = false) Map<Object, Object> params,
			HttpMethod method) throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<Object, Object> search = new HashMap<>();
		Map<String, String> userInfo = new HashMap<>();

		logger.info("★★★★★★★★★ CategorySales ★★★★★★★★★★★");
		logger.info(" params : " + params.toString());
		logger.info("★★★★★★★★★ CategorySales ★★★★★★★★★★★");

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),
					new TypeReference<Map<Object, Object>>() {
					});
		}

		if (params.get("userInfo") != null) {
			userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(),
					new TypeReference<Map<String, String>>() {
					});
		}

		logger.info("★★★★★★★★★ CategorySales ★★★★★★★★★★★");
		logger.info(" search : " + search.toString());
		logger.info(" userInfo : " + userInfo.toString());
		logger.info("★★★★★★★★★ CategorySales ★★★★★★★★★★★");

		// 브랜드의 통화에 따라 소숫점 이하 단위 절삭을 달리 함
		search.put("currencyFraction", adminCommonService.getBrandFractionDigits(Long.parseLong(String.valueOf(search.get("brandId")))));

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

		// 브랜드에 속한 매장별 판매 summary
		if ("summary".equals(search.get("option1"))) {
			try {
				list = customSalesMapper.getCateSalesSummaryList(search);

				result.put("fromdate", search.get("fromdate"));
				result.put("todate", search.get("todate"));
				result.put("list", list);
				result.put("CateSalesTop5Chart", customSalesMapper.getCateSalesTop5SummaryChart(search));

				result.put("success", true);
				result.put("errMsg", "");

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getCategorySalesSummaryList !! ");
			}
		} else if ("monthly".equals(search.get("option1"))) {

			try {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

				cal.setTime(sdformat.parse((String) search.get("fromdate")));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				search.put("fromdate", sdformat.format(cal.getTime()));

				cal.setTime(sdformat.parse((String) search.get("todate")));
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				search.put("todate", sdformat.format(cal.getTime()));

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getCategorySalesSummaryList !! ");
			}
		}

		if (StringUtils.hasText((String) search.get("option2"))) {

			List<String> headerList = headerList((String) search.get("option1"), (String) search.get("fromdate"),
					(String) search.get("todate"));
			List<LinkedHashMap<String, Object>> logList = new ArrayList<LinkedHashMap<String, Object>>();
			// 달력 리스트
			List<LinkedHashMap<String, Object>> calendarList = new ArrayList<LinkedHashMap<String, Object>>();

			if ("monthly".equals(search.get("option1"))) {

				try {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

					cal.setTime(sdformat.parse((String) search.get("fromdate")));
					cal.set(Calendar.DAY_OF_MONTH, 1);
					search.put("fromdate", sdformat.format(cal.getTime()));

					cal.setTime(sdformat.parse((String) search.get("todate")));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					search.put("todate", sdformat.format(cal.getTime()));

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getCategorySalesSummaryList !! ");
				}
			}

			// 01.달력 검색(검색 기간 범위의 날짜형식들 리턴: param : daily/weekly/monthly)
			if ("daily".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYMDList(search);
			} else if ("weekly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearWeekList(search);
			} else if ("monthly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearMonthList(search);
			}

			if (calendarList != null) {
				if (!calendarList.isEmpty()) {
					// 달력목록 추가(to search)
					search.put("calendarList", calendarList);
				}
			}

			// 02.상품별 판매현황 검색(테이블)
			logList = customSalesMapper.getCategorySalesList(search);

			list = salesSumList(headerList, logList, (String) search.get("option1"));

			Long timeLag = doDiffOfDate(dateFormat, (String) search.get("fromdate"), (String) search.get("todate"));
			String dayMonth = "";

			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
				dayMonth = "day";
				result.put("dayChart", customSalesMapper.getCateDayOfMonthChart(search));
			} else {
				dayMonth = "month";
				result.put("monthChart", customSalesMapper.getCateMonthOfYeeaChart(search));
			}

			result.put("dayMonth", dayMonth);
			result.put("hourChart", customSalesMapper.getCateHourOfDayChart(search));
			result.put("weekChart", customSalesMapper.getCateDayOfWeekChart(search));

			result.put("list", list);
			result.put("success", true);

			// 테이블 헤더
			result.put("tableHeader", calendarList);
			// 테이블 데이터
			result.put("tableList", logList);

			logger.debug("**************************************");
			logger.debug(result.get("hourChart").toString());
			logger.debug("**************************************");
		}
		return result;
	}

	/**
	 * Sales - ServiceSales
	 * 
	 * @param franchise
	 * @param params
	 * @param start
	 * @param length
	 * @param method
	 * @return
	 * @throws InvalidJsonException
	 */
	@RequestMapping("/ServiceSales")
	public Map<Object, Object> ServiceSales(@RequestParam(required = false) Map<Object, Object> params,
			HttpMethod method) throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<Object, Object> search = new HashMap<>();

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(),new TypeReference<Map<Object, Object>>() {});
		}

		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		List<LinkedHashMap<String, Object>> chartList = new ArrayList<LinkedHashMap<String, Object>>();

		// 서비스별 판매 summary
		if ("summary".equals(search.get("option1"))) {

			try {

				list = customSalesMapper.getServiceSalesSummaryList(search);
				chartList = customSalesMapper.getServiceSummaryChart(search);
				result.put("fromdate", search.get("fromdate"));
				result.put("todate", search.get("todate"));
				result.put("list", list);
				result.put("chartList", chartList);

				result.put("success", true);
				result.put("errMsg", "");

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getServiceSalesSummaryList !! ");
			}
		} else if ("monthly".equals(search.get("option1"))) {

			try {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

				cal.setTime(sdformat.parse((String) search.get("fromdate")));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				search.put("fromdate", sdformat.format(cal.getTime()));

				cal.setTime(sdformat.parse((String) search.get("todate")));
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				search.put("todate", sdformat.format(cal.getTime()));

			} catch (Exception e) {
				e.printStackTrace();

				result.put("success", false);
				result.put("errMsg", "false select getServiceSalesSummaryList !! ");
			}

		}

		if (StringUtils.hasText((String) search.get("option2"))) {

			List<String> headerList = headerList((String) search.get("option1"), (String) search.get("fromdate"), (String) search.get("todate"));
			List<LinkedHashMap<String, Object>> logList = new ArrayList<LinkedHashMap<String, Object>>();
			// 달력 리스트
			List<LinkedHashMap<String, Object>> calendarList = new ArrayList<LinkedHashMap<String, Object>>();

			if ("monthly".equals(search.get("option1"))) {

				try {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);

					cal.setTime(sdformat.parse((String) search.get("fromdate")));
					cal.set(Calendar.DAY_OF_MONTH, 1);
					search.put("fromdate", sdformat.format(cal.getTime()));

					cal.setTime(sdformat.parse((String) search.get("todate")));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					search.put("todate", sdformat.format(cal.getTime()));

				} catch (Exception e) {
					e.printStackTrace();

					result.put("success", false);
					result.put("errMsg", "false select getServiceSalesSummaryList !! ");
				}
			}

			// 01.달력 검색(검색 기간 범위의 날짜형식들 리턴: param : daily/weekly/monthly)
			if ("daily".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYMDList(search);
			} else if ("weekly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearWeekList(search);
			} else if ("monthly".equals(search.get("option1"))) {
				calendarList = customSalesMapper.getCalendarYearMonthList(search);
			}

			if (calendarList != null) {
				if (!calendarList.isEmpty()) {
					// 달력목록 추가(to search)
					search.put("calendarList", calendarList);
				}
			}

			// 02.서비스별별 판매현황 검색(테이블)
			logList = customSalesMapper.getServiceSalesList(search);
			list = salesSumList(headerList, logList, (String) search.get("option1"));

			Long timeLag = doDiffOfDate(dateFormat, (String) search.get("fromdate"), (String) search.get("todate"));
			String dayMonth = "";

			if (timeLag < 90) { // 90일 이내는 일별, 이후는 월별
				dayMonth = "day";
				result.put("dayChart", customSalesMapper.getServiceDayChart(search));
			} else {
				dayMonth = "month";
				result.put("monthChart", customSalesMapper.getServiceMonthChart(search));
			}

			result.put("dayMonth", dayMonth);
			result.put("hourChart", customSalesMapper.getServiceHourChart(search));
			result.put("dayofweekChart", customSalesMapper.getServiceDayOfWeekChart(search));
			result.put("list", list);

			// 테이블 헤더
			result.put("tableHeader", calendarList);
			// 테이블 데이터
			result.put("tableList", logList);

			result.put("success", true);

		}

		return result;
	}

	/**
	 * Sales - Sales detail
	 * 
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/SalesDetail")
	public Map<Object, Object> SalesDetail(@RequestParam(required = false) Map<Object, Object> params,
			HttpMethod method) throws Exception {

		Map<Object, Object> result = new HashMap<>();
		Map<Object, Object> search = new HashMap<>();
		Map<String, String> userInfo = new HashMap<>();

		logger.info("★★★★★★★★★ SalesDetail ★★★★★★★★★★★");
		logger.info(" params : " + params.toString());
		logger.info("★★★★★★★★★ SalesDetail ★★★★★★★★★★★");

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<Object, Object>>() {});
		}

		if (params.get("userInfo") != null) {
			userInfo = JsonConvert.JsonConvertObject(params.get("userInfo").toString(), new TypeReference<Map<String, String>>() {});
		}

		logger.info("★★★★★★★★★ SalesDetail ★★★★★★★★★★★");
		logger.info(" search 1: " + search.toString());
		logger.info(" userInfo : " + userInfo.toString());
		logger.info("★★★★★★★★★ SalesDetail ★★★★★★★★★★★");

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<Object, Object>>() {});
		}

		long id = StrUtils.parseLong(String.valueOf(search.get("id")));
		// String storeId = (String) search.get("storeId");
		// String fromdate = (String) search.get("fromdate");
		// String todate = (String) search.get("todate");
		// String serviceSt = (String) search.get("serviceSt");
		// String statusSt = (String) search.get("statusSt");
		//
		// if (StringUtils.hasText(storeId)) params.put("storeId", storeId);
		// if (StringUtils.hasText(fromdate)) params.put("fromdate", fromdate);
		// if (StringUtils.hasText(todate)) params.put("todate", todate);

		boolean flag = false;

		switch (method) {
		case GET:

			if (id > 0) {
				params.put("id", id);

				Map<String, Object> map = Maps.newHashMap();
				map = (Map<String, Object>) customSalesMapper.getDetail(search);
				map = StrUtils.convertCamelcase(map);

				boolean orderCancel = String.valueOf(map.get("orderSt")) .equals(codeUtil.getBaseCodeByAlias("order-st-cancel")) ? true : false;

				result.put("orderCancel", orderCancel);
				result.put("list", map);
				result.put("itemList", customSalesMapper.getDetailItemList(search));
			} else {
				List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

				// if (StringUtils.hasText(serviceSt)) {
				// if
				// (serviceSt.equals(codeUtil.getBaseCodeByAlias("order-type-order"))
				// ||
				// serviceSt.equals(codeUtil.getBaseCodeByAlias("order-type-reservation"))
				// ||
				// serviceSt.equals(codeUtil.getBaseCodeByAlias("order-type-contract")))
				// {
				// search.put("orderTp", serviceSt);
				// }
				// else {
				// search.put("posNo", serviceSt);
				// search.put("pathTp",
				// codeUtil.getBaseCodeByAlias("order-path-tp-pos"));
				// }
				// }

				// if (StringUtils.hasText(statusSt)) {
				// if (statusSt.equals("normality")) search.put("normality",
				// statusSt);
				// else if (statusSt.equals("canceled")) search.put("canceled",
				// statusSt);
				// }

				list = customSalesMapper.getDetailList(search);
				result.put("list", list);
			}
			flag = true;

			break;

		case PUT:

			SvcSales svcSales = new SvcSales();

			svcSales.setId(id);
			svcSales.setOrderSt(codeUtil.getBaseCodeByAlias("order-st-cancel"));
			flag = svcSalesMapper.updateByPrimaryKeySelective(svcSales) == 1 ? true : false;

			break;

		default:
			break;
		}

		result.put("success", flag);

		return result;
	}

	/**
	 * SalesDetailServiceList
	 * 
	 * @param params
	 * @return
	 * @throws InvalidJsonException
	 */
	@RequestMapping("/SalesDetailServiceList")
	public Map<Object, Object> SalesDetailServiceList(@RequestParam(required = false) Map<Object, Object> params)
			throws InvalidJsonException {
		Map<Object, Object> result = new HashMap<>();
		Map<String, String> search = new HashMap<>();

		if (params.get("data") != null) {
			search = JsonConvert.JsonConvertObject(params.get("data").toString(), new TypeReference<Map<String, String>>() {});
		}

		if (StringUtils.hasText(search.get("storeId"))){
			params.put("storeId", search.get("storeId"));
		}
		result.put("list", customSalesMapper.getDetailServiceList(params));
		result.put("success", true);

		return result;
	}

	/**
	 * Sales - Card approval
	 * 
	 * @param params
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/CardApproval")
	public Map<Object, Object> CardApproval(HttpSession session,
			@RequestParam(required = false) Map<Object, Object> params,
			@RequestParam(required = false, value = "start", defaultValue = "0") int start,
			@RequestParam(required = false, value = "length", defaultValue = "10") int length, HttpMethod method)
			throws Exception {

		Map<Object, Object> result = new HashMap<>();
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

		/**
		 * 결제 방법 (810001: 현금, 810002: 카드, 810009: 현금+카드 그외 기타)
		 */
		search.put("payMethod", "810002");//카드
		
		logger.info("★★★★★★★★★ CardApproval ★★★★★★★★★★★");
		logger.info(" search : " + search.toString());
		logger.info(" userInfo : " + userInfo.toString());
		logger.info("★★★★★★★★★ CardApproval ★★★★★★★★★★★");

		switch (method) {
		case GET:

			if (!String.valueOf(params.get("draw")).equals("null")) {

				if (String.valueOf(params.get("draw")).equals("excel")) {
					List<LinkedHashMap<String, String>> list = customSalesMapper.getCardApprovalList(search);
					result.put("list", list);
					result.put("recordsTotal", customSalesMapper.getCountCardApprovalList(search));
					result.put("recordsFiltered", customSalesMapper.getCountCardApprovalList(search));
				} else {
					/*
					 * DATATABLE 컬럼 정렬값 세팅
					 */
					if (params.get("order[0][column]") != null) {
						String orderby = String.valueOf(
								params.get("columns[" + String.valueOf(params.get("order[0][column]")) + "][name]"))
								+ " " + String.valueOf(params.get("order[0][dir]"));
						search.put("orderby", orderby);
					}

					List<LinkedHashMap<String, String>> list = customSalesMapper.getCardApprovalList(search,
							new RowBounds(start, length));

					result.put("list", list);
					result.put("recordsTotal", customSalesMapper.getCountCardApprovalList(search));
					result.put("recordsFiltered", customSalesMapper.getCountCardApprovalList(search));
				}

				result.put("success", true);

			} else {

				result.put("success", false);
				result.put("errMsg", "not supported method");
			}

			break;

		case POST:

			result.put("sucess", false);
			result.put("errMsg", "not supported method");
			break;

		case PUT:
			result.put("sucess", false);
			result.put("errMsg", "not supported method");

			break;

		case DELETE:
			result.put("sucess", false);
			result.put("errMsg", "not supported method");

			break;

		default:
			result.put("success", false);
			result.put("errMsg", "not supported method.");
		}

		return result;
	}

}
