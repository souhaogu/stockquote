package com.soustock.stockquote.sync.common;

import com.alibaba.fastjson.JSON;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.utils.HttpRequester;
import com.soustock.stockquote.utils.StringUtity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2018/10/25.
 */
public class TargetDataCommon {

    private final static String BASE_URL_QUOTE = "https://quote.souhaogu.com";

    /**
     * 得到目标数据源的最大更新日期
     * @return
     * @throws IOException
     */
    public static Long getMaxUpdateTimeOfStockUpdate() throws IOException {
        String urlString = BASE_URL_QUOTE + "/stockBasic/getMaxUpdatetimeOfStockBasic";
        HttpRequester httpRequester = new HttpRequester();
        String retStr = httpRequester.sendGet(urlString);
        if (!StringUtity.isNullOrEmpty(retStr)){
            Map<String, Object> retMap = JSON.parseObject(retStr, Map.class);
            return (Long) retMap.get("result");
        } else {
            return null;
        }
    }

    /**
     * 往目标数据源插入stockBasic
     * @return
     * @throws IOException
     */
    public static void  insertOrUpdateStockBasics(List<StockBasicPo> stockBasicPos) throws IOException {
        String urlString = BASE_URL_QUOTE + "/stockBasic/insertOrUpdateStockBasics";
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> params = new HashMap<>();
        params.put("list", JSON.toJSONString(stockBasicPos));
        httpRequester.sendPost(urlString, params);
    }


    /**
     * 得到目标数据源某个股票的最大交易日期
     * @return
     * @throws IOException
     */
    public static String getMaxDateOfStockQuote(String stockCode) throws IOException {
        String urlString = BASE_URL_QUOTE + "/dayQuote/getMaxDateOfStock";
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> params = new HashMap<>();
        params.put("stockCode", stockCode);
        String retStr = httpRequester.sendGet(urlString, params);
        if (!StringUtity.isNullOrEmpty(retStr)){
            Map<String, Object> retMap = JSON.parseObject(retStr, Map.class);
            return (String) retMap.get("result");
        } else {
            return null;
        }
    }

    /**
     * 往目标数据源插入dayQuote
     * @return
     * @throws IOException
     */
    public static void  insertDayQuotes(List<StockQuotePo> dayQuotePoList) throws IOException {
        String urlString = BASE_URL_QUOTE + "/stockBasic/insertOrUpdateStockBasics";
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> params = new HashMap<>();
        params.put("list", JSON.toJSONString(dayQuotePoList));
        httpRequester.sendPost(urlString, params);
    }


}
