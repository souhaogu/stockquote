package com.soustock.stockquote.po;

/**
 * Created by xuyufei on 2016/3/27.
 */
public class DayQuotePageCdtVo extends PageCdtVo {

    /**
     * 股票行情
     */
    private String stockCode;

    /**
     * 交易日期的升降序排列
     */
    private boolean isTradeDateAsc;


    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public boolean isTradeDateAsc() {
        return isTradeDateAsc;
    }

    public void setIsTradeDateAsc(boolean isTradeDateAsc) {
        this.isTradeDateAsc = isTradeDateAsc;
    }
}
