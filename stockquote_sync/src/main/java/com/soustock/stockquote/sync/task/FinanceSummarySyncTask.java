package com.soustock.stockquote.sync.task;


import com.soustock.stockquote.dao.FinanceSummaryDao;
import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.sync.common.BaseSyncTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by xuyufei on 2015/9/19.
 * 股票行情抓取
 */
@Component
public class FinanceSummarySyncTask extends BaseSyncTask {

    private final static Log logger = LogFactory.getLog(FinanceSummarySyncTask.class);

    @Autowired
    private StockBasicDao stockBasicDao;

    @Autowired
    private FinanceSummaryDao financeSummaryDao;

    @Override
    protected void process() throws BusinessException {
        try {
            List<StockBasicPo> stockBasicPoList = stockBasicDao.getAllStockBasics();
            for (StockBasicPo stockBasicPo: stockBasicPoList) {
                String stockCode = stockBasicPo.getStockCode();
                syncEveryStock(stockCode);
            }
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    private void syncEveryStock(String stockCode) {

    }

    @Override
    public String getTaskName() {
        return "finance_summary";
    }

    @Override
    public int getExecuteOrder() {
        return 6;
    }

}
