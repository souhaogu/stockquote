package com.soustock.stockquote.sync.task;


import com.soustock.stockquote.dao.FuquanFactorDao;
import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.po.StockBasicPo;
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
 * Created by xuyufei on 2015/9/19.
 * 股票行情抓取
 */
@Component
public class FuquanFactorSyncTask extends BaseSyncTask {

    private final static Log logger = LogFactory.getLog(FuquanFactorSyncTask.class);

    @Autowired
    private StockBasicDao stockBasicDao;
    
    @Autowired
    private FuquanFactorDao fuquanFactorDao;

    @Override
    protected void process() throws BusinessException {
        try {
            List<StockBasicPo> stockBasicPoList = stockBasicDao.getAllStockBasics();
            int stockCount = stockBasicPoList.size();
            int stockIndex = 0;
            for (StockBasicPo stockBasicPo: stockBasicPoList) {
                String stockCode = stockBasicPo.getStockCode();
                stockIndex ++;
                logger.info(String.format("Fuquan factor sync: %d/%d, %s...begin", stockIndex, stockCount, stockCode));
                syncEveryStock(stockCode);
                logger.info(String.format("Fuquan factor sync: %d/%d, %s...done", stockIndex, stockCount, stockCode));
            }
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    private void syncEveryStock(String stockCode) throws Exception {
        String maxTradeDateOfTarget = TargetDataCommon.getMaxDateOfFuquanFactor(stockCode);
        List<FuquanFactorPo> fuquanFactorPoList = null;
        if (StringUtity.isNullOrEmpty(maxTradeDateOfTarget)){
            fuquanFactorPoList = fuquanFactorDao.getAllFuquanFactorsByStockCode(stockCode);
        } else {
            DayQuoteDateCdtVo dayQuoteDateCdtVo = new DayQuoteDateCdtVo();
            dayQuoteDateCdtVo.setStockCode(stockCode);
            dayQuoteDateCdtVo.setBgnDate(DateUtity.getNextDate(maxTradeDateOfTarget));
            dayQuoteDateCdtVo.setEndDate(DateUtity.dateToDateStr(new Date()));
            fuquanFactorPoList = fuquanFactorDao.getFuquanFactorsByStockCodeDate(dayQuoteDateCdtVo);
        }

        if ((fuquanFactorPoList != null)&&(!fuquanFactorPoList.isEmpty())){
            List<FuquanFactorPo> fuquanFactorPoListTemp = new ArrayList<>(100);
            int index = 0;
            for (FuquanFactorPo fuquanFactorPo: fuquanFactorPoList){
                fuquanFactorPoListTemp.add(fuquanFactorPo);
                index ++;
                if (index % 30 == 0){
                    TargetDataCommon.insertFuquanFactors(fuquanFactorPoListTemp);
                    fuquanFactorPoListTemp.clear();
                }
            }
            if (index % 30 > 0){
                TargetDataCommon.insertFuquanFactors(fuquanFactorPoListTemp);
                fuquanFactorPoListTemp.clear();
            }
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
