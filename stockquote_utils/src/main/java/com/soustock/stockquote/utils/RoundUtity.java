package com.soustock.stockquote.utils;

/**
 * Created by xuyufei on 2018/10/27.
 */
public class RoundUtity {

    public static double RoundDouble(double value, int numDigits) {
        int times = 1;
        for (int index = 0; index < numDigits; index++) {
            times *= 10;
        }
        return (double)Math.round(value * times)/times;
    }
}
