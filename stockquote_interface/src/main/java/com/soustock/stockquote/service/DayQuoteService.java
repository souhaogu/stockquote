package com.soustock.stockquote.service;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.vo.DayQuoteVo;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
public interface DayQuoteService {


    List<DayQuoteVo> queryQuoteData(String stockCode, int recentlyCount, FuquanKind fuquanKind ) throws Exception;


    List<DayQuoteVo> queryQuoteByDate(String stockCode, String bgnDate, String endDate, FuquanKind fuquanKind ) throws Exception;

    /**
     * 获取某个股票的行情最大日期(yyyyMMdd)
     */
    String getMaxDateOfStock(String stockCode);

    void insertDayQuotes(List<StockQuotePo> stockQuotePos);
}
