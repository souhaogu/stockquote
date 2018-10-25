package com.soustock.stockquote.controller;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.service.DayQuoteService;
import com.soustock.stockquote.utils.DateUtity;
import com.soustock.stockquote.utils.JsonUtity;
import com.soustock.stockquote.utils.StringUtity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2018/8/11.
 */
@Controller
@RequestMapping("/dayQuote")
public class DayQuoteController {

    @Autowired
    private DayQuoteService dayQuoteService;

    @RequestMapping(value = "/queryQuoteData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryQuoteData(HttpServletRequest request) throws Exception {
        String stockCode = request.getParameter("stockCode");
        int recentlyCount = Integer.parseInt(request.getParameter("recentlyCount"));
        String fuquanKindStr = request.getParameter("fuquan");
        FuquanKind fuquanKind = StringUtity.isNullOrEmpty(fuquanKindStr)? FuquanKind.Origin: FuquanKind.getByCode(fuquanKindStr);

        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        map.put("list", dayQuoteService.queryQuoteData(stockCode, recentlyCount, fuquanKind));
        return map;
    }

    @RequestMapping(value = "/queryQuoteByDate", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryQuoteByDate(HttpServletRequest request) throws Exception {
        String stockCode = request.getParameter("stockCode");
        String bgnDate = request.getParameter("bgnDate");
        String endDate = request.getParameter("endDate");
        Date dtToday = new Date();
        Date dtThirtyDaysBefore = new Date(dtToday.getTime() - DateUtity.MS_OF_DAY * 30);
        if (StringUtity.isNullOrEmpty(bgnDate)){
            bgnDate = DateUtity.dateToDateStr(dtThirtyDaysBefore);
        }
        if (StringUtity.isNullOrEmpty(endDate)){
            endDate = DateUtity.dateToDateStr(dtToday);
        }

        String fuquanKindStr = request.getParameter("fuquan");
        FuquanKind fuquanKind = StringUtity.isNullOrEmpty(fuquanKindStr)? FuquanKind.Origin: FuquanKind.getByCode(fuquanKindStr);

        Map<String, Object> map = new HashMap<>();
        map.put("list", dayQuoteService.queryQuoteByDate(stockCode, bgnDate, endDate, fuquanKind));
        return map;
    }

    @RequestMapping(value = "/getMaxDateOfStock", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMaxDateOfStock(HttpServletRequest request) throws Exception {
        String stockCode = request.getParameter("stockCode");
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        map.put("result", dayQuoteService.getMaxDateOfStock(stockCode));
        return map;
    }

    @RequestMapping(value = "/insertDayQuotes", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertDayQuotes(HttpServletRequest request) throws Exception {
        List<StockQuotePo> stockQuotePos = (List<StockQuotePo>) JsonUtity.readValueToList(request.getParameter("list"), StockQuotePo.class);
        dayQuoteService.insertDayQuotes(stockQuotePos);
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        return map;
    }

}
