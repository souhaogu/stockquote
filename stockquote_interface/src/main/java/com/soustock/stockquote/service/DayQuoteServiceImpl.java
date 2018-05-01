package com.soustock.stockquote.service;


import com.soustock.stockquote.dao.DayQuoteDao;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.vo.DayQuoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
@Service("dayQuoteService")
public class DayQuoteServiceImpl implements DayQuoteService {

    @Autowired
    private DayQuoteDao dayQuoteDao;

    @Override
    public List<DayQuoteVo> queryQuoteData(String stockCode, int recentlyCount) throws Exception {
        DayQuoteCdtVo dayQuoteCdtVo = new DayQuoteCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setRecentlyCount(recentlyCount);
        List<StockQuotePo> stockBasicPoList = dayQuoteDao.getStockQuotesByStockCodeCount(dayQuoteCdtVo);
        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockBasicPoList.size());
        for (StockQuotePo po: stockBasicPoList){
            DayQuoteVo vo = new DayQuoteVo();
            vo.setTradeDate(po.getTradeDate());
            vo.setHighPrice(po.getHighPrice());
            vo.setOpenPrice(po.getOpenPrice());
            vo.setLowPrice(po.getLowPrice());
            vo.setClosePrice(po.getClosePrice());
            vo.setTradeQty(po.getTradeQty());
            vo.setTradeMoney(po.getTradeMoney());
            dayQuoteVos.add(vo);
        }
        return dayQuoteVos;
    }
}
