package com.soustock.stockquote.po;

/**
 * Created by xuyufei on 2016/3/27.
 */
public class DayQuoteCdtVo {

    /**
     * 股票行情
     */
    private String stockCode;


    /**
     * 取最近多少条
     */
    private int recentlyCount;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }


    public int getRecentlyCount() {
        return recentlyCount;
    }

    public void setRecentlyCount(int recentlyCount) {
        this.recentlyCount = recentlyCount;
    }
}
