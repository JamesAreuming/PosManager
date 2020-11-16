/**
 * <pre>
 * Filename	: NumberFormat.java
 * Function	: 언어별 날짜 format
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

public class NumberFormat {

	/**
	 * default language
	 */
	private static final String DEFAULT_LANG = "ru";

	/**
	 * 언어별 format map
	 */
	private static HashMap<String, Object> formatMap = new HashMap<String, Object>();

	/**
	 * setup
	 */
	static {
		/* setup special DecimalFormatSymbols */
		// CIS
		DecimalFormatSymbols dfs1 = new DecimalFormatSymbols();
		dfs1.setGroupingSeparator(' ');
		dfs1.setDecimalSeparator(',');

		/* setup DecimalFormat */
		// common
		DecimalFormat df1 = new DecimalFormat("#,##0");
		DecimalFormat df2 = new DecimalFormat("#,##0.00");

		// CIS
		DecimalFormat df11 = new DecimalFormat("#,##0", dfs1);
		DecimalFormat df12 = new DecimalFormat("#,##0.00", dfs1);

		/* setup format for language */
		// 한국어
		HashMap<String, Object> koMap = new HashMap<String, Object>();
		koMap.put("NUMBER", df1);
		koMap.put("FLOAT", df2);
		koMap.put("PRICE", df1);
		koMap.put("PRICE2", df2);
		formatMap.put("ko", koMap);

		// 영어
		HashMap<String, Object> enMap = new HashMap<String, Object>();
		enMap.put("NUMBER", df1);
		enMap.put("FLOAT", df2);
		enMap.put("PRICE", df1);
		enMap.put("PRICE2", df2);
		formatMap.put("en", enMap);

		// 러이사어
		HashMap<String, Object> ruMap = new HashMap<String, Object>();
		ruMap.put("NUMBER", df11);
		ruMap.put("FLOAT", df12);
		ruMap.put("PRICE", df11);
		ruMap.put("PRICE2", df12);
		formatMap.put("ru", ruMap);
	}

	/**
	 * 언어별 number 변환
	 * 
	 * @param val
	 * @param lang
	 * @return
	 */
	public static String formatNumber(String val, String lang) throws Exception {
		return formatDecimal(val, "NUMBER", lang);
	}

	/**
	 * 언어별 float 변환
	 * 
	 * @param val
	 * @param lang
	 * @return
	 */
	public static String formatFloat(String val, String lang) throws Exception {
		return formatDecimal(val, "FLOAT", lang);
	}

	/**
	 * 언어별 price 변환
	 * 
	 * @param val
	 * @param lang
	 * @return
	 */
	public static String formatPrice(String val, String lang) throws Exception {
		return formatDecimal(val, "PRICE", lang);
	}

	/**
	 * 언어별 price2 변환
	 * 
	 * @param val
	 * @param lang
	 * @return
	 */
	public static String formatPrice2(String val, String lang) throws Exception {
		return formatDecimal(val, "PRICE2", lang);
	}

	/**
	 * number 변환
	 * 
	 * @param val
	 * @param type
	 * @param lang
	 * @return
	 */
	private static String formatDecimal(String val, String type, String lang) throws Exception {
		if (val == null || val.trim().length() == 0) {
			return "";
		}
		return formatDecimal(Double.parseDouble(val), type, lang);
	}

	/**
	 * number 변환
	 * 
	 * @param val
	 * @param type
	 * @param lang
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String formatDecimal(double val, String type, String lang) throws Exception {
		if (lang == null || lang.trim().length() == 0) {
			lang = DEFAULT_LANG;
		}

		HashMap<String, Object> langMap = (HashMap<String, Object>) formatMap.get(lang);
		if (langMap == null) {
			langMap = (HashMap<String, Object>) formatMap.get(DEFAULT_LANG);
		}

		DecimalFormat df = (DecimalFormat) langMap.get(type);
		String result = df.format(val);

		return result;
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String lang = "ru"; // ko, en, ru, null

		// format
		System.out.println("formatNumber : " + NumberFormat.formatNumber("123456", lang));
		System.out.println("formatFloat : " + NumberFormat.formatFloat("123456.78", lang));
		System.out.println("formatPrice : " + NumberFormat.formatPrice("123456", lang));
		System.out.println("formatPrice2 : " + NumberFormat.formatPrice2("123456.78", lang));
	}
}
