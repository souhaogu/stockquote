package com.soustock.stockquote.utils;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2015/5/19.
 */
public class JSTLFunction {

    /**
     * @param key
     * @param json
     * @return
     */
    public static String get(String key, String json) {
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(json), Map.class);
        return map.get(key);
    }

    /**
     * 将List转换成MultiselectValue控件认可的值形式s
     *
     * @param value
     * @return
     */
    public static String toMultiselectValue(List<Object> value) {
        if (value == null)
            return "[]";
        String retValue = "[";//["103", "104"]
        for (int i = 0; i < value.size(); i++) {
            retValue = retValue + "\"" + value.get(i).toString() + "\"";
            if (i != value.size() - 1) {
                retValue = retValue + ",";
            }
        }
        retValue = retValue + "]";
        return retValue;
    }

    /**
     * 时间戳转换成字符串
     *
     * @param timeStamp
     * @return
     */
    public static String toDateStr(long timeStamp) {
        Date dt = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(dt);
    }

    public static String secondsFormate(long seconds) {
        String viewTime = "";
        long tempTime = seconds / 60;
        if (tempTime > 1 && tempTime < 60) {
            viewTime = (int) tempTime + "分" + (int) (seconds % 60) + "秒";
        } else if (tempTime > 60 && tempTime < (60 * 24)) {
            viewTime = (int) (tempTime / 60) + "小时" + (int) (tempTime % 60) + "分" + (int) (seconds % 60 % 60) + "秒";
        } else if (tempTime > (60 * 24)) {
            viewTime = (int)(tempTime / (60 * 24)) + "天" + (int)(tempTime / 60 % 24) + "小时" + (int)(tempTime % 60 / 24) + "分" + (int)(seconds / 24 % 60 % 60) + "秒";
        } else {
            String format = String.format("%.2f", (seconds / 1.0));
            viewTime = format + "秒";
        }

        return viewTime;
    }

}
