package com.soustock.stockquote.sync.task;


import com.soustock.stockquote.dao.DayQuoteDao;
import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.sync.common.BaseSyncTask;
import com.soustock.stockquote.sync.common.TargetDataCommon;
import com.soustock.stockquote.utils.DateUtity;
import com.soustock.stockquote.utils.StringUtity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuyufei on 2018/10/25.
 * 股票行情同步
 */
@Component
public class DayQuoteSyncTask extends BaseSyncTask {

    private final static Log logger = LogFactory.getLog(DayQuoteSyncTask.class);

    @Autowired
    private StockBasicDao stockBasicDao;

    @Autowired
    private DayQuoteDao dayQuoteDao;

    @Override
    protected void process() throws BusinessException {
        try {
            List<StockBasicPo> stockBasicPoList = stockBasicDao.getAllStockBasics();
            int stockCount = stockBasicPoList.size();
            int stockIndex = 0;
            for (StockBasicPo stockBasicPo: stockBasicPoList) {
                String stockCode = stockBasicPo.getStockCode();
                syncEveryStock(stockCode);
                stockIndex ++;
                logger.info(String.format("Day quote sync: %d/%d, %s...", stockIndex, stockCount, stockCode));
            }
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getTaskName() {
        return "day_quote";
    }

    @Override
    public int getExecuteOrder() {
        return 2;
    }

    private void syncEveryStock(String stockCode) throws Exception {
        String maxTradeDateOfTarget = TargetDataCommon.getMaxDateOfStockQuote(stockCode);

        List<StockQuotePo> stockQuotePoList = null;
        if (StringUtity.isNullOrEmpty(maxTradeDateOfTarget)){
            stockQuotePoList = dayQuoteDao.getAllStockQuotesByStockCode(stockCode);
        } else {
            DayQuoteDateCdtVo dayQuoteDateCdtVo = new DayQuoteDateCdtVo();
            dayQuoteDateCdtVo.setStockCode(stockCode);
            dayQuoteDateCdtVo.setBgnDate(DateUtity.getNextDate(maxTradeDateOfTarget));
            dayQuoteDateCdtVo.setEndDate(DateUtity.dateToDateStr(new Date()));
            stockQuotePoList = dayQuoteDao.getStockQuotesByStockCodeDate(dayQuoteDateCdtVo);
        }

        if ((stockQuotePoList != null)&&(!stockQuotePoList.isEmpty())){
            List<StockQuotePo> stockQuotePoListTemp = new ArrayList<>(100);
            int index = 0;
            for (StockQuotePo stockQuotePo: stockQuotePoList){
                stockQuotePoListTemp.add(stockQuotePo);
                index ++;
                if (index % 30 == 0){
                    TargetDataCommon.insertDayQuotes(stockQuotePoListTemp);
                    stockQuotePoListTemp.clear();
                }
            }
            if (index % 30 > 0){
                TargetDataCommon.insertDayQuotes(stockQuotePoListTemp);
                stockQuotePoListTemp.clear();
            }
        }
    }

}
