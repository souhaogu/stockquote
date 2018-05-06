package com.soustock.stockquote.dao;


import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.FinanceSummaryPo;
import java.util.List;

/**
 * Created by xuyufei on 2018/5/3.
 */
public interface FinanceSummaryDao {

    /**
     * 获取某个股票的财务简报的最大日期(yyyyMMdd)
     */
    String getMaxDateOfFinanceSummary(String stockCode);

    void insertFinanceSummarys(List<FinanceSummaryPo> financeSummaryPos);

    List<FinanceSummaryPo> getFinanceSummarysByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo);

}
