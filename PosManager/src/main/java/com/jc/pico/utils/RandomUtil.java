/**
 * <pre>
 * Filename	: RandomUtil.java
 * Function	: Random 생성
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.utils;

import java.util.UUID;

public class RandomUtil {

	/**
	 * 숫자형 random
	 * 
	 * @param leng - random 길이
	 * @return
	 * @throws Exception
	 */
	public static String numericRandom(int leng) throws Exception {
		String result = "";

		for (int inx = 0; inx < leng; inx++) {
			result += String.valueOf((int) (Math.random() * 10));
		}

		return result;
	}

	/**
	 * License 생성 예) BDF8-4DF0-BE2D-494C
	 * 
	 * @return
	 */
	public static String getLicense() {
		String uuid = UUID.randomUUID().toString();
		String result = uuid.substring(9, 28).toUpperCase();

		return result;
	}

	/**
	 * Barcode 생성 - 12자리 예) 469777629256
	 * 
	 * @return
	 */
	public static String getBarcode() {
		long millis = System.currentTimeMillis();
		String result = Long.toString(millis).substring(1);

		/*
		double random = Math.floor(Math.random() * 1000000) + 100000;
		if (random > 1000000) {
			random = random - 100000;
		}
		System.out.println("r  : " + String.valueOf((int) random));
		*/
		return result;
	}

	/**
	 * for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("randomUUID : " + UUID.randomUUID().toString());
		System.out.println("numericRandom : " + RandomUtil.numericRandom(6));
		System.out.println("getLicense : " + RandomUtil.getLicense());
		System.out.println("getBarcode : " + RandomUtil.getBarcode());
	}
}
