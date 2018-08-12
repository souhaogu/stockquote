package com.soustock.stockquote.po;

/**
 * Created by xuyufei on 2018/8/11.
 * 按日期搜索的条件
 */
public class DayQuoteDateCdtVo {

    /**
     * 股票行情
     */
    private String stockCode;

    /**
     * 开始日期
     */
    private String bgnDate;

    /**
     * 结束日期
     */
    private String endDate;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getBgnDate() {
        return bgnDate;
    }

    public void setBgnDate(String bgnDate) {
        this.bgnDate = bgnDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
