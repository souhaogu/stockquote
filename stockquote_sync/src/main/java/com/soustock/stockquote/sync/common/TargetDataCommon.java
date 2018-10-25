package com.soustock.stockquote.sync.common;

import com.alibaba.fastjson.JSON;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.utils.HttpRequester;
import com.soustock.stockquote.utils.JsonUtity;
import com.soustock.stockquote.utils.StringUtity;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2018/10/25.
 */
public class TargetDataCommon {

    private final static String BASE_URL_QUOTE = "https://quote.souhaogu.cn/stock_quote";
    //private final static String BASE_URL_QUOTE = "http://localhost:8080/stock_quote";

    /**
     * 得到目标数据源的最大更新日期
     * @return
     * @throws IOException
     */
    public static Long getMaxUpdateTimeOfStockUpdate() throws IOException {
        String urlString = BASE_URL_QUOTE + "/stockBasic/getMaxUpdatetimeOfStockBasic.do";
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
        String urlString = BASE_URL_QUOTE + "/stockBasic/insertOrUpdateStockBasics.do";
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> params = new HashMap<>();
        params.put("list",  JsonUtity.writeValueAsString(stockBasicPos));
        httpRequester.sendPost(urlString, params);
    }


    /**
     * 得到目标数据源某个股票的最大交易日期
     * @return
     * @throws IOException
     */
    public static String getMaxDateOfStockQuote(String stockCode) throws IOException {
        String urlString = BASE_URL_QUOTE + "/dayQuote/getMaxDateOfStock.do";
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
        String urlString = BASE_URL_QUOTE + "/dayQuote/insertDayQuotes.do";
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> params = new HashMap<>();
        params.put("list", JsonUtity.writeValueAsString(dayQuotePoList));
        httpRequester.sendPost(urlString, params);
    }


    /**
     * 得到目标数据源某个股票复权因子的最大交易日期
     * @return
     * @throws IOException
     */
    public static String getMaxDateOfFuquanFactor(String stockCode) throws IOException {
        String urlString = BASE_URL_QUOTE + "/fuquanFactor/getMaxDateOfStockFuquan.do";
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
     * 往目标数据源插入fuquanFactor
     * @return
     * @throws IOException
     */
    public static void  insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPoList) throws IOException {
        String urlString = BASE_URL_QUOTE + "/fuquanFactor/insertFuquanFactors.do";
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> params = new HashMap<>();
        params.put("list", JsonUtity.writeValueAsString(fuquanFactorPoList));
        httpRequester.sendPost(urlString, params);
    }


}
