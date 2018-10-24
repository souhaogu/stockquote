package com.soustock.stockquote.service;


import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.vo.StockSimpleVo;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 *
 */
public interface StockBasicService {


    List<StockSimpleVo> getStockSimpleVosOfLikeStr(String likeStr) throws Exception;


    StockSimpleVo getStockBasicByStockCode(String stockCode) throws Exception;

    /**
     * 重新刷新缓存
     */
    void refreshCache();


    void insertOrUpdateStockBasics(List<StockBasicPo> stockInfoList);

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
