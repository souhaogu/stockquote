package com.soustock.stockquote.controller;


import com.soustock.stockquote.service.DayQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyufei on 2016/3/26.
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
        Map<String, Object> map = new HashMap<>();
        map.put("list", dayQuoteService.queryQuoteData(stockCode, recentlyCount));
        return map;
    }
}
