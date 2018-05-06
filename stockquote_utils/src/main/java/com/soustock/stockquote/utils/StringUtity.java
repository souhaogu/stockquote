package com.soustock.stockquote.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuyufei on 2015/06/12.
 */
public class StringUtity {

    public static boolean isNullOrEmpty(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    private static Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");

    public static boolean isNumeric(String str) {
        if (str != null) {
            if (str.startsWith("-")){
                str = str.substring(1);
            }
            return pattern.matcher(str).matches();
        }
        return false;
    }

    /**
     * 将字符串首字母大写
     *
     * @param s 字符串
     * @return 首字母大写后的新字符串
     */
    public static String firstUpperCase(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char char0 = s.charAt(0);
        if (Character.isUpperCase(char0))
            return s.toString();
        return new StringBuilder(len).append(Character.toUpperCase(char0))
                .append(s.subSequence(1, len)).toString();
    }

    /**
     * 将字符串首字母小写
     *
     * @param s 字符串
     * @return 首字母小写后的新字符串
     */
    public static String firstLowerCase(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char char0 = s.charAt(0);
        if (Character.isLowerCase(char0))
            return s.toString();
        return new StringBuilder(len).append(Character.toLowerCase(char0))
                .append(s.subSequence(1, len)).toString();
    }
}
