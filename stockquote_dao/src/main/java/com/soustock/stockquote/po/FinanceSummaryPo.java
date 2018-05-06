package com.soustock.stockquote.po;

/**
 * Created by xuyufei on 2018/5/5.
 */
public class FinanceSummaryPo extends BasePo {

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 财务日期
     */
    private String financeDate;

    /**
     * 每股净资产
     */
    private double naps;

    /**
     * 每股净资产
     */
    private double eps;

    /**
     * 每股现金含量
     */
    private double cps;

    /**
     * 每股资本公积金
     */
    private double cfps;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getFinanceDate() {
        return financeDate;
    }

    public void setFinanceDate(String financeDate) {
        this.financeDate = financeDate;
    }

    public double getNaps() {
        return naps;
    }

    public void setNaps(double naps) {
        this.naps = naps;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getCps() {
        return cps;
    }

    public void setCps(double cps) {
        this.cps = cps;
    }

    public double getCfps() {
        return cfps;
    }

    public void setCfps(double cfps) {
        this.cfps = cfps;
    }
}
