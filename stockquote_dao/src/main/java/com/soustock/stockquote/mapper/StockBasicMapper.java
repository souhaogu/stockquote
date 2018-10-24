package com.soustock.stockquote.mapper;

import com.soustock.stockquote.po.StockBasicPo;

import java.util.List;

/**
 * Created by xuyufei on 2016/3/6.
 * stockinfodao的mybatis实现
 */
public interface StockBasicMapper {

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
//    @Options(flushCache = true, timeout = 20000)
    void insertStockBasic(StockBasicPo stockInfo);

    /**
     * 获取某个市场的股票总数
     * @param market
     * @return
     */
    long getStockCountOfMarket(String market);

    /**
     * 修改一条股票信息
     * @param stockInfo
     */
//    @Options(flushCache = true, timeout = 2000)
    void updateStockBasic(StockBasicPo stockInfo);

    /**
     * 获取某个市场的股票列表
     */
    List<StockBasicPo> getAllStockBasicsOfMarket(String market);

    /**
     * 获取所有的股票列表
     * @return
     */
    List<StockBasicPo> getAllStockBasics();

    /**
     * 获取某个时间之后更新过的的股票列表
     * @return
     */
    List<StockBasicPo> getStockBasicsAfter(Long updateTime);

    /**
     * 获取股票基本信息的最大更新时间
     * @return
     */
    Long getMaxUpdatetimeOfStockBasic();

}
