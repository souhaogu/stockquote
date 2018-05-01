package com.soustock.stockquote.service;


import com.soustock.stockquote.vo.MinuteQuoteVo;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
public interface RealtimeQuoteService {


    List<MinuteQuoteVo> queryQuoteData(String stockCode, int recentlyCount) throws Exception;
}
