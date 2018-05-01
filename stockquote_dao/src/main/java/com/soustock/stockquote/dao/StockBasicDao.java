package com.soustock.stockquote.dao;

import com.soustock.stockquote.po.StockBasicPo;

import java.util.List;

/**
 * Created by xuyufei on 2016/3/5.
 * 读写股票基本信息的dao
 */
public interface StockBasicDao {

    /**
     * 根据股票代码获取股票的基本信息
     * @param stockCode
     * @return
     */
    StockBasicPo getStockBasicByStockCode(String stockCode);

    /**
     * 写入一条股票信息
     * @param stockInfo
     */
    void insertStockBasic(StockBasicPo stockInfo);

    /**
     * 修改一条股票信息
     * @param stockInfo
     */
    void updateStockBasic(StockBasicPo stockInfo);

    /**
     * 获取一个市场中的股票数目
     * @param market
     * @return
     */
    long getStockCountOfMarket(String market);


    /**
     * 获取某个市场的所有股票信息，分页处理
     * @param market 市场代码
     * @param pageCdt 分页条件
     * @return
     */
//    PageList<StockBasicPo> getStockInfosOfMarket(String market, PageCdt pageCdt);

    /**
     * 取出所有的股票信息
     * @return
     */
    List<StockBasicPo> getAllStockBasics();

    /**
     * 获得某个市场所有的股票信息
     * @param market
     * @return
     */
    List<StockBasicPo> getAllStockBasicsOfMarket(String market);
}
