package com.soustock.stockquote.controller;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.common.ReturnMapFactory;
import com.soustock.stockquote.common.RoundCommon;
import com.soustock.stockquote.service.RealtimeQuoteService;
import com.soustock.stockquote.utils.StringUtity;
import com.soustock.stockquote.vo.MinuteQuoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2016/3/26.
 */
@Controller
@RequestMapping("/realtimeQuote")
public class RealtimeQuoteController {

    @Autowired
    private RealtimeQuoteService realtimeQuoteService;

    @RequestMapping(value = "/queryRealtimeQuotes", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryRealtimeQuotes(HttpServletRequest request) {
        try {
            String stockCode = request.getParameter("stockCode");
            int recentlyCount = (request.getParameter("recentlyCount") == null) ? 242 : Integer.parseInt(request.getParameter("recentlyCount"));
            String fuquanKindStr = request.getParameter("fuquan");
            FuquanKind fuquanKind = StringUtity.isNullOrEmpty(fuquanKindStr) ? FuquanKind.Origin : FuquanKind.getByCode(fuquanKindStr);
            List<MinuteQuoteVo> minuteQuoteVoList = realtimeQuoteService.queryRealtimeQuotes(stockCode, recentlyCount, fuquanKind);
            return ReturnMapFactory.succ("list", RoundCommon.RoundMinuteQuoteList(minuteQuoteVoList));
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }
}
