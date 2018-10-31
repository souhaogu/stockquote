package com.soustock.stockquote.crawl.task;


import com.soustock.stockquote.crawl.cache.StockListDateCache;
import com.soustock.stockquote.crawl.common.BaseCrawlTask;
import com.soustock.stockquote.dao.FinanceSummaryDao;
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
 * 财务摘要抓取
 */
//@Component
public class FinanceSummaryCrawlTask extends BaseCrawlTask {

    private final static Log logger = LogFactory.getLog(FinanceSummaryCrawlTask.class);

    @Autowired
    private StockListDateCache stockListDateCache;

    @Autowired
    private FinanceSummaryDao financeSummaryDao;

    @Override
    protected void process() throws BusinessException {
        try {
            Map<String, String> stockCodeAndListDates = stockListDateCache.getListDateOfAllStock();
            ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            AtomicInteger stockIndex = new AtomicInteger(0);
            int stockCount = stockCodeAndListDates.size();
            for (Map.Entry<String, String> entry: stockCodeAndListDates.entrySet()) {
                String stockCode = entry.getKey();
                String listDate = entry.getValue();
                FinanceSummaryCrawlThread unitTask = new FinanceSummaryCrawlThread(stockCode);
                unitTask.setFinanceSummaryDao(financeSummaryDao);
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
        return "finance_summary";
    }

    @Override
    public int getExecuteOrder() {
        return 3;
    }

}
