package com.jc.pico.utils;

import java.math.BigDecimal;

/**
 * 
 */

public class MathUtil {


    /**
     * 지정한 자릿수로 반올림
     * (1.081, 2) -> 1.08
     * (1.085, 2) -> 1.09
     * (1.081, 1) -> 1.1
     *
     * @param value 숫자
     * @param scale 유지할 소숫점 자리수
     * @return scale 자리로 반올림한 숫자
     */
    public static double round(double value, int scale) {
        try {
            return (new BigDecimal(Double.toString(value))
                    .setScale(scale, BigDecimal.ROUND_HALF_UP))
                    .doubleValue();
        } catch (NumberFormatException ex) {
            if (Double.isInfinite(value)) {
                return value;
            } else {
                return Double.NaN;
            }
        }
    }
}
