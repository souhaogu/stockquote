package com.soustock.stockquote.crawl.cache;


import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.po.Constants;
import com.soustock.stockquote.po.StockBasicPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuyufei on 2016/03/07.
 */
@Component
public class StockCodeCache {

    @Autowired
    private StockBasicDao stockBasicDao;

    private Lock lock = new ReentrantLock();

    private List<String> stockCodes = null;
    public List<String> getStockCodes() {
        if (stockCodes==null) {
            lock.lock();
            try {
                if (stockCodes==null) {
                    stockCodes = fetchStockCodes();
                }
            } finally {
                lock.unlock();
            }
        }
        return stockCodes;
    }

    private List<String> fetchStockCodes() {
        List<String> stockCodeList = new ArrayList<>();
        for (String marketName : Constants.MARKET_NAME_ARR) {
            List<StockBasicPo> stockBasicPos = stockBasicDao.getAllStockBasicsOfMarket(marketName);
            for (StockBasicPo vo: stockBasicPos) {
                stockCodeList.add(vo.getStockCode());
            }
        }
        return stockCodeList;
    }

    public void reset(){
        if (stockCodes!=null) {
            lock.lock();
            try {
                if (stockCodes!=null) {
                    stockCodes = null;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
