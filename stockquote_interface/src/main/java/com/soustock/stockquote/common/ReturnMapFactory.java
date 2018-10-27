package com.soustock.stockquote.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyufei on 2018/10/27.
 * Controller的返回值工厂类
 */
public class ReturnMapFactory {

    private final static Log logger = LogFactory.getLog(ReturnMapFactory.class);

    public static Map<String, Object> succ(){
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", true);
        return map;
    }


    public static Map<String, Object> succ(String resultKey, Object resultObj){
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", true);
        map.put(resultKey, resultObj);
        return map;
    }

    public static Map<String, Object> fail(String errorMsg){
        logger.error(errorMsg);
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", false);
        map.put("error", errorMsg);
        return map;
    }

    public static Map<String, Object> fail(Exception ex){
        logger.error(ex.getMessage(), ex);
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", false);
        map.put("error", ex.toString());
        return map;
    }
}
