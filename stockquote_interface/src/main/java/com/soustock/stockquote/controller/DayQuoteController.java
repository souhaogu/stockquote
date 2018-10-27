package com.soustock.stockquote.controller;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.common.ReturnMapFactory;
import com.soustock.stockquote.common.RoundCommon;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.service.DayQuoteService;
import com.soustock.stockquote.utils.DateUtity;
import com.soustock.stockquote.utils.JsonUtity;
import com.soustock.stockquote.utils.StringUtity;
import com.soustock.stockquote.vo.DayQuoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public Map<String, Object> queryQuoteData(HttpServletRequest request) {
        try {
            String stockCode = request.getParameter("stockCode");
            int recentlyCount = Integer.parseInt(request.getParameter("recentlyCount"));
            String fuquanKindStr = request.getParameter("fuquan");
            FuquanKind fuquanKind = StringUtity.isNullOrEmpty(fuquanKindStr) ? FuquanKind.Origin : FuquanKind.getByCode(fuquanKindStr);
            List<DayQuoteVo> dayQuoteVoList = dayQuoteService.queryQuoteData(stockCode, recentlyCount, fuquanKind);
            return ReturnMapFactory.succ("list", RoundCommon.RoundStockQuoteList(dayQuoteVoList));
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/queryQuoteByDate", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryQuoteByDate(HttpServletRequest request) {
        try {
            String stockCode = request.getParameter("stockCode");
            String bgnDate = request.getParameter("bgnDate");
            String endDate = request.getParameter("endDate");
            Date dtToday = new Date();
            Date dtThirtyDaysBefore = new Date(dtToday.getTime() - DateUtity.MS_OF_DAY * 30);
            if (StringUtity.isNullOrEmpty(bgnDate)) {
                bgnDate = DateUtity.dateToDateStr(dtThirtyDaysBefore);
            }
            if (StringUtity.isNullOrEmpty(endDate)) {
                endDate = DateUtity.dateToDateStr(dtToday);
            }

            String fuquanKindStr = request.getParameter("fuquan");
            FuquanKind fuquanKind = StringUtity.isNullOrEmpty(fuquanKindStr) ? FuquanKind.Origin : FuquanKind.getByCode(fuquanKindStr);
            List<DayQuoteVo> dayQuoteVoList = dayQuoteService.queryQuoteByDate(stockCode, bgnDate, endDate, fuquanKind);
            return ReturnMapFactory.succ("list", RoundCommon.RoundStockQuoteList(dayQuoteVoList));
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/getMaxTradeDateOfStock", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMaxTradeDateOfStock(HttpServletRequest request) {
        try {
            String stockCode = request.getParameter("stockCode");
            String maxTradeDateOfStock = dayQuoteService.getMaxTradeDateOfStock(stockCode);
            return ReturnMapFactory.succ("result", maxTradeDateOfStock);
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/insertDayQuotes", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertDayQuotes(HttpServletRequest request) throws Exception {
        try {
            List<StockQuotePo> stockQuotePos = (List<StockQuotePo>) JsonUtity.readValueToList(request.getParameter("list"), StockQuotePo.class);
            dayQuoteService.insertDayQuotes(stockQuotePos);
            return ReturnMapFactory.succ();
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

}
