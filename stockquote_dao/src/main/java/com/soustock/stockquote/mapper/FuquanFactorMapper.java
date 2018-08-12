package com.soustock.stockquote.mapper;


import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import java.util.List;

/**
 * Created by xuyufei on 2016/3/26.
 * 复权因子dao的mybatis实现
 */
public interface FuquanFactorMapper {

    /**
     * 获取某个股票的复权因子最大日期(yyyyMMdd)
     */
    String getMaxDateOfStockFuquan(String stockCode);

    void insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPos);

    List<FuquanFactorPo> getFuquanFactorsByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo);

    List<FuquanFactorPo> getFuquanFactorsByStockCodeDate(DayQuoteDateCdtVo dayQuoteDateCdtVo);
}
