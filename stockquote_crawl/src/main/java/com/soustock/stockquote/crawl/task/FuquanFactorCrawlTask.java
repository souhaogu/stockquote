package com.soustock.stockquote.crawl.task;


import com.soustock.stockquote.crawl.cache.StockListDateCache;
import com.soustock.stockquote.crawl.common.BaseCrawlTask;
import com.soustock.stockquote.dao.FuquanFactorDao;
import com.soustock.stockquote.exception.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuyufei on 2015/9/19.
 * 股票行情抓取
 */
@Component
public class FuquanFactorCrawlTask extends BaseCrawlTask {

    private final static Log logger = LogFactory.getLog(FuquanFactorCrawlTask.class);

    @Autowired
    private StockListDateCache stockListDateCache;

    @Autowired
    private FuquanFactorDao fuquanFactorDao;

    @Override
    protected void process() throws BusinessException {
        try {
            Map<String, String> stockCodeAndListDateMap = stockListDateCache.getListDateOfAllStock();
            ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            AtomicInteger stockIndex = new AtomicInteger(0);
            int stockCount = stockCodeAndListDateMap.size();
            for (Map.Entry<String, String> entry: stockCodeAndListDateMap.entrySet()) {
                String stockCode = entry.getKey();
                String listDate = entry.getValue();
                FuquanFactorCrawlThread unitTask = new FuquanFactorCrawlThread(stockCode);
                unitTask.setFuquanFactorDao(fuquanFactorDao);
                unitTask.setListDate(listDate);
                unitTask.setStockIndexAi(stockIndex);
                unitTask.setStockCount(stockCount);
                executorService.execute(unitTask);
            }

            while (executorService.getActiveCount() > 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getTaskName() {
        return "fuquan_factor";
    }

    @Override
    public int getExecuteOrder() {
        return 4;
    }

}
