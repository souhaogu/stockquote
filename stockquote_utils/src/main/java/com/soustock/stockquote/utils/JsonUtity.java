package com.soustock.stockquote.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2015/06/01.
 * Json对象转换器
 */
public class JsonUtity {
    /**
     * 解析对象为json字符串,使用fastjson工具包.
     *
     * @param o 被解析对象
     * @return json字符串
     */
    public static String writeValueAsString(Object o) {
        return JSON.toJSONString(o);
    }

    /**
     * 解析json字符串为指定对象,使用fastjson工具包.
     *
     * @param json  json字符串
     * @param clazz 对象类
     * @return 返回对象
     */
    public static Object readValue(String json, Class<?> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 解析json字符串为List对象,使用fastjson工具包.
     *
     * @param json json字符串
     * @return List对象
     */
    public static List<?> readValueToList(String json, Class<?> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 将数据源结构保留，值设置为空
     *
     * @param jsonObject 需要将值制空的对象
     * @return
     */
    public static JSONObject getJsonObjectValueType(JSONObject jsonObject) {
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getValue() instanceof Long) {
                jsonObject.put(entry.getKey(), System.currentTimeMillis());
            } else if (entry.getValue() instanceof String) {
                jsonObject.put(entry.getKey(), "");
            } else if (entry.getValue() instanceof Integer) {
                if (entry.getKey().equals("debt")){
                    jsonObject.put(entry.getKey(), "未知");
                }else {
                    jsonObject.put(entry.getKey(), 0);
                }
            } else if (entry.getValue() instanceof Double) {
                jsonObject.put(entry.getKey(), 0.0);
            } else if (entry.getValue() instanceof JSONArray) {
                JSONArray value = (JSONArray) entry.getValue();
                JSONArray array = new JSONArray();
                for (int i = 0; i < value.size(); i++) {
                    array.add("");
                }
                jsonObject.put(entry.getKey(), array);
            } else if (entry.getValue() instanceof JSONObject) {
                JSONObject value = (JSONObject) entry.getValue();
                if (entry.getKey().equals("others")) {
                    jsonObject.put("others", new JSONObject());
                }else {
                    for (Map.Entry<String, Object> subEntry : value.entrySet()) {
                        if (subEntry.getValue() instanceof Long) {
                            value.put(subEntry.getKey(), 0l);
                        } else if (subEntry.getValue() instanceof String) {
                            value.put(subEntry.getKey(), "");
                        } else if (subEntry.getValue() instanceof Integer) {
                            value.put(subEntry.getKey(), 0);
                        } else if (subEntry.getValue() instanceof Double) {
                            value.put(subEntry.getKey(), 0.0);
                        } else if (subEntry.getValue() instanceof JSONArray) {
                            JSONArray subValue = (JSONArray) subEntry.getValue();
                            JSONArray array = new JSONArray();
                            for (int i = 0; i < subValue.size(); i++) {
                                array.add("");
                            }
                            jsonObject.put(entry.getKey(), array);
                        } else {
                            System.out.println(subEntry.getValue().getClass().getName() + "其他子类型");
                        }
                    }
                    jsonObject.put(entry.getKey(), value);
                }
            } else if (entry.getValue() instanceof Boolean) {
                jsonObject.put(entry.getKey(), false);
            } else {
                System.out.println(entry.getValue().getClass().getName() + "其他类型");
            }
        }
        return jsonObject;
    }
}

