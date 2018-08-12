package com.soustock.stockquote.mapper;

import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.DayQuotePageCdtVo;
import com.soustock.stockquote.po.StockQuotePo;

import java.util.List;

/**
 * Created by xuyufei on 2016/3/26.
 * 日行情dao的mybatis实现
 */
public interface DayQuoteMapper {

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

    List<StockQuotePo> getStockQuotesByStockCode(DayQuotePageCdtVo dayQuotePageCdtVo);

    int getQuoteCountOfStockCode(String stockCode);

    /**
     * 获取最近多少条行情数据
     * @param dayQuoteCdtVo
     * @return 降序排列返回结果
     */
    List<StockQuotePo> getStockQuotesByStockCodeCount(DayQuoteCdtVo dayQuoteCdtVo);

    /**
     * 按日期获取行情数据
     * @param dayQuoteDateCdtVo
     * @return 升序排列返回结果
     */
    List<StockQuotePo> getStockQuotesByStockCodeDate(DayQuoteDateCdtVo dayQuoteDateCdtVo);
}
