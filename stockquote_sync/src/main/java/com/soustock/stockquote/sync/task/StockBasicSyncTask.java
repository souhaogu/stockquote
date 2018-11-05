package com.soustock.stockquote.sync.task;


import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.sync.common.BaseSyncTask;
import com.soustock.stockquote.sync.common.TargetDataCommon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyufei on 2016/03/06.
 */
@Component
public class StockBasicSyncTask extends BaseSyncTask {

    private final static Log logger = LogFactory.getLog(StockBasicSyncTask.class);

    @Autowired
    private StockBasicDao stockBasicDao;

    @Override
    protected void process() throws BusinessException {
        try {
            procSync();
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getTaskName() {
        return "stock_basic";
    }

    @Override
    public int getExecuteOrder() {
        return 1;
    }

    private void procSync() throws Exception {
        //获得目标数据库的最大更新日期
        Long maxUpdateTime = TargetDataCommon.getMaxUpdateTimeOfStockUpdate();
        List<StockBasicPo> stockBasicPoList = (maxUpdateTime == null)?
                stockBasicDao.getAllStockBasics():stockBasicDao.getStockBasicsAfter(maxUpdateTime);

        List<StockBasicPo> stockBasicPosTemp = new ArrayList<>(100);
        int index = 0;
        for (StockBasicPo stockBasicPo: stockBasicPoList){
            stockBasicPosTemp.add(stockBasicPo);
            index ++;
            if (index % 100 == 0){
                TargetDataCommon.insertOrUpdateStockBasics(stockBasicPosTemp);
                stockBasicPosTemp.clear();
            }
        }
        if (index % 100 > 0){
            TargetDataCommon.insertOrUpdateStockBasics(stockBasicPosTemp);
            stockBasicPosTemp.clear();
        }

        TargetDataCommon.refreshCache();
    }

}
