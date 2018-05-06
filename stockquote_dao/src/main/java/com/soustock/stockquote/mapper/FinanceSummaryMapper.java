package com.soustock.stockquote.mapper;


import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.FinanceSummaryPo;
import java.util.List;

/**
 * Created by xuyufei on 2018/5/6.
 * 复权因子dao的mybatis实现
 */
public interface FinanceSummaryMapper {

    /**
     * 获取某个股票的财务摘要的最大日期(yyyyMMdd)
     */
    String getMaxDateOfFinanceSummary(String stockCode);

    void insertFinanceSummarys(List<FinanceSummaryPo> financeSummaryPos);

    List<FinanceSummaryPo> getFinanceSummarysByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo);

}
