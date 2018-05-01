package com.soustock.stockquote.utils;

/**
 * Created by xuyufei on 2016/3/6.
 */
public class NullCheckUtity {

    public static boolean stringIsNull(String str){
        return (str==null)||(str.trim().length()==0);
    }
}
