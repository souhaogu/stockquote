package com.soustock.stockquote.vo;


import java.io.Serializable;

/**
 * Created by xuyufei on 2016/3/5.
 * 日行情，也可以作为周、月、季行情
 */
public class DayQuoteVo implements Serializable {

    /**
     * 交易日期（日、周、月行情为日期字符串８位，5、10、15、30、60分钟为时间节点字符串14位
     */
    private String tradeDate;

    /**
     * 开盘价
     */
    private double openPrice;

    /**
     * 最高价
     */
    private double highPrice;

    /**
     * 最低价
     */
    private double lowPrice;

    /**
     * 收盘价
     */
    private double closePrice;

    /**
     * 成交量
     */
    private double tradeQty;

    /**
     * 成交金额
     */
    private double tradeMoney;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getTradeQty() {
        return tradeQty;
    }

    public void setTradeQty(double tradeQty) {
        this.tradeQty = tradeQty;
    }

    public double getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(double tradeMoney) {
        this.tradeMoney = tradeMoney;
    }
}
