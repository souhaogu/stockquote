package com.soustock.stockquote.po;


/**
 * Created by xuyufei on 2016/3/5.
 * 复权因子
 */
public class FuquanFactorPo extends BasePo {

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 复权因子
     */
    private double factor;


    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}
