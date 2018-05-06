package com.soustock.stockquote.service;


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
}
