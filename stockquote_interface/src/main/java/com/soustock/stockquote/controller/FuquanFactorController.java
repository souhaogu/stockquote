package com.soustock.stockquote.controller;


import com.soustock.stockquote.common.ReturnMapFactory;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.service.FuquanFactorService;
import com.soustock.stockquote.utils.JsonUtity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyufei on 2018/8/11.
 */
@Controller
@RequestMapping("/fuquanFactor")
public class FuquanFactorController {

    @Autowired
    private FuquanFactorService fuquanFactorService;

    @RequestMapping(value = "/getMaxDateOfStockFuquan", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMaxDateOfStockFuquan(HttpServletRequest request) {
        try {
            String stockCode = request.getParameter("stockCode");
            return ReturnMapFactory.succ("result", fuquanFactorService.getMaxDateOfStockFuquan(stockCode));
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/insertFuquanFactors", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertFuquanFactors(HttpServletRequest request) {
        try {
            List<FuquanFactorPo> fuquanFactorPoList = (List<FuquanFactorPo>) JsonUtity.readValueToList(request.getParameter("list"), FuquanFactorPo.class);
            fuquanFactorService.insertFuquanFactors(fuquanFactorPoList);
            return ReturnMapFactory.succ();
        } catch (Exception ex){
            return ReturnMapFactory.fail(ex.getMessage());
        }
    }

}
