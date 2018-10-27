package com.soustock.stockquote.controller;


import com.soustock.stockquote.common.ReturnMapFactory;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.service.StockBasicService;
import com.soustock.stockquote.utils.JsonUtity;
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
        try {
            String likeStr = request.getParameter("likeStr");
            List<StockSimpleVo> stockBasicPos = stockBasicService.getStockSimpleVosOfLikeStr(likeStr);
            return ReturnMapFactory.succ("result", stockBasicPos);
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/getStockBasicByStockCode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getStockBasicByStockCode(HttpServletRequest request) {
        try {
            String stockCode = request.getParameter("stockCode");
            StockSimpleVo stockSimpleVo = stockBasicService.getStockBasicByStockCode(stockCode);
            return ReturnMapFactory.succ("result", stockSimpleVo);
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/refreshCache", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> refreshCache(HttpServletRequest request) {
        try {
            stockBasicService.refreshCache();
            return ReturnMapFactory.succ();
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/insertOrUpdateStockBasics", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertOrUpdateStockBasics(HttpServletRequest request) {
        try {
            List<StockBasicPo> stockBasicPoList = (List<StockBasicPo>) JsonUtity.readValueToList(request.getParameter("list"), StockBasicPo.class);
            stockBasicService.insertOrUpdateStockBasics(stockBasicPoList);
            return ReturnMapFactory.succ();
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/getMaxUpdatetimeOfStockBasic", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMaxUpdatetimeOfStockBasic(HttpServletRequest request) {
        try {
            Long maxUpdatetime = stockBasicService.getMaxUpdatetimeOfStockBasic();
            return ReturnMapFactory.succ("result", maxUpdatetime);
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

}
