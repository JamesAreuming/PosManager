/*
 * Filename	: BarcodeUtil.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;


import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class BarcodeUtil {
  public static final int OFFSET = 0135246; //

  public static final int DEFAULT_COUPON_CODE_LENGTH = 16;

  public String makeUserCode(String grpCd, int memCd) {
    return makeUserCode(grpCd, memCd, 7);
  }

  public String makeUserCode(String grpCd, int memCd, int memCdLength) {
    return String.format("%s%0" + memCdLength + "d", grpCd, (memCd + OFFSET));
  }

  public String makeGroupCode(String mb) {
    byte[] bytes = mb.getBytes();
    int code = bytes[0];

    for (int i = 1; i < bytes.length; i++) {
      code = code ^ bytes[i];
      code = code << 1;
    }

    code = code % 1000;
    if (code < 100) { // 최소 3자리
      code = code + 100;
    }
    return String.valueOf(code);
  }

  public String makeCouponCode(String brandCd) {
    return makeCouponCode(brandCd, DEFAULT_COUPON_CODE_LENGTH);
  }

  /**
   * @param brandCd - brand code(3) + issue type(1)
   * @param length - default = 16
   * @return
   */
  public String makeCouponCode(String brandCd, int length) {
    int randomNumberLength = length - (brandCd.length() + 1);

    StringBuilder builder = new StringBuilder(brandCd);
    builder.append(makeCouponSerial(randomNumberLength));

    // Do the Luhn algorithm to makeUserCode the check digit.
    int checkDigit = this.getCheckDigit(builder.toString());
    builder.append(checkDigit);

    return builder.toString();
  }

  private int getCheckDigit(String number) {

    // Get the sum of all the digits, however we need to replace the value
    // of the first digit, and every other digit, with the same digit
    // multiplied by 2. If this multiplication yields a number greater
    // than 9, then add the two digits together to get a single digit
    // number.
    //
    // The digits we need to replace will be those in an even position for
    // card numbers whose length is an even number, or those is an odd
    // position for card numbers whose length is an odd number. This is
    // because the Luhn algorithm reverses the card number, and doubles
    // every other number starting from the second number from the last
    // position.
    int sum = 0;
    for (int i = 0; i < number.length(); i++) {

      // Get the digit at the current position.
      int digit = Integer.parseInt(number.substring(i, (i + 1)));

      if ((i % 2) == 0) {
        digit = digit * 2;
        if (digit > 9) {
          digit = (digit / 10) + (digit % 10);
        }
      }
      sum += digit;
    }

    // The check digit is the number required to make the sum a multiple of
    // 10.
    int mod = sum % 10;
    return ((mod == 0) ? 0 : 10 - mod);
  }

  private static int last = 124;
//  private String makeCouponSerial(int length) {
//    Random random = new Random(System.currentTimeMillis());
//    StringBuilder builder = new StringBuilder();
//    for (int i = 0; i < length; i++) {
//      int digit = random.nextInt(100);
//      builder.append(digit);
//    }
//
//    return builder.toString();
//  }

  private String makeCouponSerial(int length) {
    Random random = new Random(System.currentTimeMillis());
    last = last + random.nextInt(100);

    return String.format("%0" + length + "d", last);
  }

  public static final void main(String[] args) throws Exception {
    BarcodeUtil util = new BarcodeUtil();

//    System.out.println(util.makeUserCode(util.makeGroupCode("0104158664"), 12312));
//    System.out.println(util.makeCouponCode("12", 16));
//    for (int i = 0; i < 10; i++) {
//      System.out.println(util.makeCouponCode("4911", 16));
//    }

    System.out.println(Arrays.toString("yy/mm/dd".split("/")));
  }
}
