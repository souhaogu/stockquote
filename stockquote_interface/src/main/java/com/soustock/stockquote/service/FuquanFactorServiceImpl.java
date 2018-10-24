package com.soustock.stockquote.service;


import com.soustock.stockquote.dao.FuquanFactorDao;
import com.soustock.stockquote.po.FuquanFactorPo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by xuyufei on 2018/10/24.
 */
public class FuquanFactorServiceImpl implements FuquanFactorService {

    @Autowired
    private FuquanFactorDao fuquanFactorDao;

    @Override
    public String getMaxDateOfStockFuquan(String stockCode) {
        return fuquanFactorDao.getMaxDateOfStockFuquan(stockCode);
    }

    @Override
    public void insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPos) {
        fuquanFactorDao.insertFuquanFactors(fuquanFactorPos);
    }
}
