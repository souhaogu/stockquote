package com.soustock.stockquote.controller;

import com.soustock.stockquote.service.StockBasicService;
import com.soustock.stockquote.vo.StockSimpleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2016/3/26.
 */
@Controller
@RequestMapping("/stockBasic")
public class StockBasicController {

    @Autowired
    private StockBasicService stockBasicService;

    @RequestMapping(value = "/getStockBasicsOfLikeStr", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getStockBasicsOfLikeStr(HttpServletRequest request) throws Exception {
        String likeStr = request.getParameter("likeStr");
        List<StockSimpleVo> stockBasicPos = stockBasicService.getStockSimpleVosOfLikeStr(likeStr);

        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        map.put("result", stockBasicPos);
        return map;
    }

    @RequestMapping(value = "/getStockBasicByStockCode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getStockBasicByStockCode(HttpServletRequest request) throws Exception {
        String stockCode = request.getParameter("stockCode");
        StockSimpleVo stockSimpleVo = stockBasicService.getStockBasicByStockCode(stockCode);

        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        map.put("result", stockSimpleVo);
        return map;
    }
}
