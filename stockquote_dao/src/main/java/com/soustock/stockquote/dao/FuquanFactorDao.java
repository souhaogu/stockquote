package com.soustock.stockquote.dao;


import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import java.util.List;

/**
 * Created by xuyufei on 2018/5/3.
 */
public interface FuquanFactorDao {

    /**
     * 获取某个股票的复权因子最大日期(yyyyMMdd)
     */
    String getMaxDateOfStockFuquan(String stockCode);

    void insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPos);

    List<FuquanFactorPo> getFuquanFactorsByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo);

    List<FuquanFactorPo> getFuquanFactorsByStockCodeDate(DayQuoteDateCdtVo dayQuoteDateCdtVo);

    List<FuquanFactorPo> getAllFuquanFactorsByStockCode(String stockCode);

}
