package com.soustock.stockquote.dao;


import com.soustock.stockquote.mapper.FinanceSummaryMapper;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.FinanceSummaryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by xuyufei on 2018/5/3.
 */
@Repository("financeSummaryDao")
public class FinanceSummaryDaoImpl implements FinanceSummaryDao {

    @Autowired
    private FinanceSummaryMapper financeSummaryMapper;

    @Override
    public String getMaxDateOfFinanceSummary(String stockCode) {
        return financeSummaryMapper.getMaxDateOfFinanceSummary(stockCode);
    }

    @Override
    public void insertFinanceSummarys(List<FinanceSummaryPo> financeSummaryPos) {
        financeSummaryMapper.insertFinanceSummarys(financeSummaryPos);
    }

    @Override
    public List<FinanceSummaryPo> getFinanceSummarysByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo) {
        return financeSummaryMapper.getFinanceSummarysByStockCodeCount(dayQuoteCdtVo);
    }
}
