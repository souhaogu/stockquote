package com.soustock.stockquote.crawl.cache;


import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.po.Constants;
import com.soustock.stockquote.po.StockBasicPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuyufei on 2016/03/07.
 */
@Component
public class StockListDateCache {

    @Autowired
    private StockBasicDao stockBasicDao;

    private Lock lock = new ReentrantLock();

    private Map<String, String> stockListDateMap = null;

    public Map<String, String> getListDateOfAllStock() {
        if (stockListDateMap == null) {
            lock.lock();
            try {
                if (stockListDateMap == null) {
                    stockListDateMap = innerGetListDates();
                }
            } finally {
                lock.unlock();
            }
        }
        return stockListDateMap;
    }

    private Map<String, String> innerGetListDates() {
        Map<String, String> stockCodeMap = new HashMap<>();
        for (String marketName : Constants.MARKET_NAME_ARR) {
            List<StockBasicPo> stockBasicPos = stockBasicDao.getAllStockBasicsOfMarket(marketName);
            for (StockBasicPo vo : stockBasicPos) {
                stockCodeMap.put(vo.getStockCode(), vo.getListDate());
            }
        }
        return stockCodeMap;
    }

    public void reset() {
        if (stockListDateMap != null) {
            lock.lock();
            try {
                if (stockListDateMap != null) {
                    stockListDateMap = null;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
