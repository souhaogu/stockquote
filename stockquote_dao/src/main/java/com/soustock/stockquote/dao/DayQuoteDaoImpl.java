package com.soustock.stockquote.dao;

import com.soustock.stockquote.mapper.DayQuoteMapper;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.DayQuotePageCdtVo;
import com.soustock.stockquote.po.PageList;
import com.soustock.stockquote.po.StockQuotePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuyufei on 2016/3/26.
 */
@Repository("dayQuoteDao")
public class DayQuoteDaoImpl implements DayQuoteDao {

    @Autowired
    private DayQuoteMapper dayQuoteMapper;

    @Override
    public String getMaxDateOfStock(String stockCode) {
        return dayQuoteMapper.getMaxDateOfStock(stockCode);
    }

    @Override
    public void insertDayQuotes(List<StockQuotePo> stockQuotePos) {
        dayQuoteMapper.insertDayQuotes(stockQuotePos);
    }

    @Override
    public PageList<StockQuotePo> getStockQuotesByStockCode(DayQuotePageCdtVo dayQuotePageCdtVo) {
        PageList<StockQuotePo> stockQuoteVoPageList = new PageList<>();
        long totalRows = dayQuoteMapper.getQuoteCountOfStockCode(dayQuotePageCdtVo.getStockCode());
        int totalPages = (int) Math.ceil(totalRows*1.0/dayQuotePageCdtVo.getPageSize());
        stockQuoteVoPageList.setTotalRows(totalRows);
        stockQuoteVoPageList.setTotalPages(totalPages);
        stockQuoteVoPageList.setList(dayQuoteMapper.getStockQuotesByStockCode(dayQuotePageCdtVo));
        return stockQuoteVoPageList;
    }

    @Override
    public List<StockQuotePo> getStockQuotesByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo){
        return dayQuoteMapper.getStockQuotesByStockCodeCount(dayQuoteCdtVo);
    }
}
