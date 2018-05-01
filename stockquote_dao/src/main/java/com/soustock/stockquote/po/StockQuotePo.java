package com.soustock.stockquote.po;


/**
 * Created by xuyufei on 2016/3/5.
 * 股票行情
 */
public class StockQuotePo extends BasePo {

    /**
     * 股票代码
     */
    private String stockCode;

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
