package com.soustock.stockquote.po;


/**
 * Created by xuyufei on 2015/10/18.
 * 股票信息
 */
public class StockBasicPo extends BasePo {

    /**
     * 股票代码
     */
    private String stockCode ;

    /**
     * 股票名称
     */
    private String stockName ;

    /**
     * 拼音名次（股票名称的首字母组合）
     */
    private String pyName;

    /**
     * 上市日期
     */
    private String listDate;

    /**
     * 市场
     */
    private String market;


    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

}
