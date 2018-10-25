package com.soustock.stockquote.dao;


import com.soustock.stockquote.mapper.FuquanFactorMapper;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuyufei on 2018/5/3.
 */
@Repository("fuquanFactorDao")
public class FuquanFactorDaoImpl implements FuquanFactorDao {

    @Autowired
    private FuquanFactorMapper fuquanFactorMapper;

    @Override
    public String getMaxDateOfStockFuquan(String stockCode) {
        return fuquanFactorMapper.getMaxDateOfStockFuquan(stockCode);
    }

    @Override
    public void insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPos) {
        fuquanFactorMapper.insertFuquanFactors(fuquanFactorPos);
    }

    @Override
    public List<FuquanFactorPo> getFuquanFactorsByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo) {
        return fuquanFactorMapper.getFuquanFactorsByStockCodeCount(dayQuoteCdtVo);
    }

    @Override
    public List<FuquanFactorPo> getFuquanFactorsByStockCodeDate(DayQuoteDateCdtVo dayQuoteDateCdtVo){
        return fuquanFactorMapper.getFuquanFactorsByStockCodeDate(dayQuoteDateCdtVo);
    }

    @Override
    public List<FuquanFactorPo> getAllFuquanFactorsByStockCode(String stockCode) {
        return fuquanFactorMapper.getAllFuquanFactorsByStockCode(stockCode);
    }
}
