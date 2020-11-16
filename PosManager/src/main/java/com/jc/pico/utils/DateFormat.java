/**
 * <pre>
 * Filename	: DateFormat.java
 * Function	: 언어별 날짜 format
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DateFormat {

	/**
	 * default language
	 */
	private static final String DEFAULT_LANG = "ru";
	/**
	 * default day format
	 */
	private static final SimpleDateFormat DEFAULT_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * default day format
	 */
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 언어별 day format map
	 */
	private static HashMap<String, String> dayFormatMap = new HashMap<String, String>();
	/**
	 * 언어별 time format map
	 */
	private static HashMap<String, String> timeFormatMap = new HashMap<String, String>();
	/**
	 * 언어별 date format map
	 */
	private static HashMap<String, String> dateFormatMap = new HashMap<String, String>();
	
	static {
		// 한국어
		dayFormatMap.put("ko", "yyyy.MM.dd");
		timeFormatMap.put("ko", "HH:mm:ss");
		dateFormatMap.put("ko", "yyyy.MM.dd HH:mm:ss");
		
		// 영어
		dayFormatMap.put("en", "dd.MM.yyyy");
		timeFormatMap.put("en", "HH:mm:ss");
		dateFormatMap.put("en", "dd.MM.yyyy HH:mm:ss");
		
		// 러이사어
		dayFormatMap.put("ru", "dd.MM.yy");
		timeFormatMap.put("ru", "HH:mm:ss");
		dateFormatMap.put("ru", "dd.MM.yy HH:mm:ss");
	}
	
	/**
	 * 언어별 day format
	 * 
	 * @param lang
	 * @return
	 */
	public static String getDayFormat(String lang) {
		if (lang == null || lang.trim().length() == 0) {lang = DEFAULT_LANG;}
		String format = dayFormatMap.get(lang);
		if (format == null) {format = dayFormatMap.get(DEFAULT_LANG);}
		
		return format;
	}
	
	/**
	 * 언어별 time format
	 * 
	 * @param lang
	 * @return
	 */
	public static String getTimeFormat(String lang) {
		if (lang == null || lang.trim().length() == 0) {lang = DEFAULT_LANG;}
		String format = timeFormatMap.get(lang);
		if (format == null) {format = timeFormatMap.get(DEFAULT_LANG);}
		
		return format;
	}
	
	/**
	 * 언어별 date format
	 * 
	 * @param lang
	 * @return
	 */
	public static String getDateFormat(String lang) {
		if (lang == null || lang.trim().length() == 0) {lang = DEFAULT_LANG;}
		String format = dateFormatMap.get(lang);
		if (format == null) {format = dateFormatMap.get(DEFAULT_LANG);}
		
		return format;
	}
	
	/**
	 * 언어별 day 변환
	 * 
	 * @param date
	 * @param lang
	 * @return
	 */
	public static String formatDay(Date date, String lang) throws Exception {
		if (date == null) {return "";}
		String format = DateFormat.getDayFormat(lang);
		String result = new SimpleDateFormat(format).format(date);
		
		return result;
	}
	
	/**
	 * 언어별 day 변환
	 * 
	 * @param val - YYYY-MM-DD
	 * @param lang
	 * @return
	 */
	public static String formatDay(String val, String lang) throws Exception {
		if (val == null || val.length() != 10) {return val;}
		String result = DateFormat.formatDay(DEFAULT_DAY_FORMAT.parse(val), lang);
		
		return result;
	}
	
	/**
	 * 언어별 time 변환
	 * 
	 * @param date
	 * @param lang
	 * @return
	 */
	public static String formatTime(Date date, String lang) throws Exception {
		if (date == null) {return "";}
		String format = DateFormat.getTimeFormat(lang);
		String result = new SimpleDateFormat(format).format(date);
		
		return result;
	}
	
	/**
	 * 언어별 date 변환
	 * 
	 * @param date
	 * @param lang
	 * @return
	 */
	public static String formatDate(Date date, String lang) throws Exception {
		if (date == null) {return "";}
		String format = DateFormat.getDateFormat(lang);
		String result = new SimpleDateFormat(format).format(date);
		
		return result;
	}
	
	/**
	 * 언어별 date 변환
	 * 
	 * @param val - YYYY-MM-DD HH:mm:ss
	 * @param lang
	 * @return
	 */
	public static String formatDate(String val, String lang) throws Exception {
		if (val == null || val.length() != 19) {return val;}
		String result = DateFormat.formatDate(DEFAULT_DATE_FORMAT.parse(val), lang);
		
		return result;
	}
	
	/**
	 * for test
	 * @param args
	 */
	public static void main(String[] args) throws Exception {		
		String lang = "ru"; // ko, en, ru, null
		
		// get format
		System.out.println("getDayFormat : " + DateFormat.getDayFormat(lang));
		System.out.println("getTimeFormat : " + DateFormat.getTimeFormat(lang));
		System.out.println("getDateFormat : " + DateFormat.getDateFormat(lang));
		
		// format for Date
		System.out.println("formatDay : " + DateFormat.formatDay(new Date(), lang));
		System.out.println("formatTime : " + DateFormat.formatTime(new Date(), lang));
		System.out.println("formatDate : " + DateFormat.formatDate(new Date(), lang));
		
		// format for String
		System.out.println("formatDay : " + DateFormat.formatDay("2016-07-13", lang));
		System.out.println("formatDate : " + DateFormat.formatDate("2016-07-13 10:13:25", lang));
	}
}
