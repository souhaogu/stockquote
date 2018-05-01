package com.soustock.stockquote.utils;

/**
 * Created by xuyufei on 2015/06/12.
 */
public class StringUtity {
    /**
     * 将字符串首字母大写
     *
     * @param s
     *            字符串
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
     * @param s
     *            字符串
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
