package com.soustock.stockquote.vo;


import java.io.Serializable;

/**
 * Created by xuyufei on 2016/5/1.
 * 分钟行情
 */
public class MinuteQuoteVo implements Serializable {

    /**
     * 交易时间(HH:mm:ss)
     */
    private String tradeTime;

    /**
     * 平均价
     */
    private double avgPrice;

    /**
     * 成交量
     */
    private double tradeQty;

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getTradeQty() {
        return tradeQty;
    }

    public void setTradeQty(double tradeQty) {
        this.tradeQty = tradeQty;
    }
}
