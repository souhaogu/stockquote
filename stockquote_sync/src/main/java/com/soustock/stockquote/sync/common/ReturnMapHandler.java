package com.soustock.stockquote.sync.common;


import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.utils.JsonUtity;
import com.soustock.stockquote.utils.StringUtity;

import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2018/10/27.
 */
public class ReturnMapHandler {

    public static <T> T handleObject(String retMapStr, String resultKey, Class<T> resultClazz) throws BusinessException {
        if (!StringUtity.isNullOrEmpty(retMapStr)) {
            Map<String, Object> retMap = (Map<String, Object>) JsonUtity.readValue(retMapStr, Map.class);
            if (retMap != null) {
                boolean isSucc = (boolean) retMap.get("isSucc");
                if (isSucc) {
                    Object obj = retMap.get(resultKey);
                    if (obj != null) {
                        return (T) JsonUtity.readValue(obj.toString(), resultClazz);
                    }
                } else {
                    String errorMsg = (String) retMap.get("errorMsg");
                    if (!StringUtity.isNullOrEmpty(errorMsg)) {
                        throw new BusinessException(errorMsg);
                    }
                }
            }
        }
        throw new BusinessException("unknown error.");
    }

    public static <T> List<T> handleList(String retMapStr, String resultKey, Class<T> resultElementClazz) throws BusinessException {
        if (!StringUtity.isNullOrEmpty(retMapStr)) {
            Map<String, Object> retMap = (Map<String, Object>) JsonUtity.readValue(retMapStr, Map.class);
            if (retMap != null) {
                boolean isSucc = (boolean) retMap.get("isSucc");
                if (isSucc) {
                    Object obj = retMap.get(resultKey);
                    if (obj != null) {
                        return (List<T>) JsonUtity.readValueToList(obj.toString(), resultElementClazz);
                    }
                } else {
                    String errorMsg = (String) retMap.get("errorMsg");
                    if (!StringUtity.isNullOrEmpty(errorMsg)) {
                        throw new BusinessException(errorMsg);
                    }
                }
            }
        }
        throw new BusinessException("unknown error.");
    }

    public static void handleVoid(String retMapStr) throws BusinessException {
        if (!StringUtity.isNullOrEmpty(retMapStr)) {
            Map<String, Object> retMap = (Map<String, Object>) JsonUtity.readValue(retMapStr, Map.class);
            if (retMap != null) {
                boolean isSucc = (boolean) retMap.get("isSucc");
                if (!isSucc) {
                    String errorMsg = (String) retMap.get("error");
                    if (!StringUtity.isNullOrEmpty(errorMsg)) {
                        throw new BusinessException(errorMsg);
                    }
                }
            }
        }
        throw new BusinessException("unknown error.");
    }

    public static <T> T handleType(String retMapStr, String resultKey) throws BusinessException {
        if (!StringUtity.isNullOrEmpty(retMapStr)) {
            Map<String, Object> retMap = (Map<String, Object>) JsonUtity.readValue(retMapStr, Map.class);
            if (retMap != null) {
                boolean isSucc = (boolean) retMap.get("isSucc");
                if (isSucc) {
                    Object obj = retMap.get(resultKey);
                    if (obj != null) {
                        return (T)obj;
                    }
                } else {
                    String errorMsg = (String) retMap.get("error");
                    if (!StringUtity.isNullOrEmpty(errorMsg)) {
                        throw new BusinessException(errorMsg);
                    }
                }
            }
        }
        throw new BusinessException("unknown error.");
    }
}
