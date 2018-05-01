package com.soustock.stockquote.service;


import com.soustock.stockquote.vo.DayQuoteVo;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
public interface DayQuoteService {


    List<DayQuoteVo> queryQuoteData(String stockCode, int recentlyCount) throws Exception;
}
