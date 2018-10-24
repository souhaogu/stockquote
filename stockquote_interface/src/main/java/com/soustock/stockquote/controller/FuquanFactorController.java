package com.soustock.stockquote.controller;


import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.service.FuquanFactorService;
import com.soustock.stockquote.utils.AutoPojo;
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
 * Created by xuyufei on 2018/8/11.
 */
@Controller
@RequestMapping("/dayQuote")
public class FuquanFactorController {

    @Autowired
    private FuquanFactorService fuquanFactorService;

    @RequestMapping(value = "/getMaxDateOfStockFuquan", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMaxDateOfStockFuquan(HttpServletRequest request) throws Exception {
        String stockCode = request.getParameter("stockCode");
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        map.put("list", fuquanFactorService.getMaxDateOfStockFuquan(stockCode));
        return map;
    }

    @RequestMapping(value = "/insertFuquanFactors", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertFuquanFactors(HttpServletRequest request) throws Exception {
        List<FuquanFactorPo> fuquanFactorPoList = AutoPojo.bindRequestParam(request, List.class);
        fuquanFactorService.insertFuquanFactors(fuquanFactorPoList);
        Map<String, Object> map = new HashMap<>();
        map.put("isSucc", "true");
        return map;
    }

}
