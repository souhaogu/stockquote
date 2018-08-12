package com.soustock.stockquote.dao;

import com.soustock.stockquote.po.*;

import java.util.List;

/**
 * Created by xuyufei on 2016/3/5.
 * 股票行情的dao
 */
public interface DayQuoteDao {

    /**
     * 获取某个股票的行情最大日期(yyyyMMdd)
     */
    String getMaxDateOfStock(String stockCode);

//    /**
//     * 根据股票代码、起止日期查找股票行情
//     * @param stockCode
//     * @param beginDate
//     * @param endDate
//     * @return
//     */
//    List<StockQuotePo> getStockQuoteBetween(String stockCode, String beginDate, String endDate);

    void insertDayQuotes(List<StockQuotePo> stockQuotePos);

    PageList<StockQuotePo> getStockQuotesByStockCode(DayQuotePageCdtVo dayQuotePageCdtVo);


    /**
     * 获取某个股票下所有的日行情数据，以交易日期降序排列
     * @param dayQuoteCdtVo
     * @return
     */
    List<StockQuotePo> getStockQuotesByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo);

    /**
     * 获取某个股票下所有的日行情数据，以交易日期降序排列
     * @param dayQuoteDateCdtVo
     * @return
     */
    List<StockQuotePo> getStockQuotesByStockCodeDate(DayQuoteDateCdtVo dayQuoteDateCdtVo);

}
