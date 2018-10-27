package com.soustock.stockquote.service;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.vo.MinuteQuoteVo;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
public interface RealtimeQuoteService {


    List<MinuteQuoteVo> queryRealtimeQuotes(String stockCode, int recentlyCount, FuquanKind fuquanKind) throws Exception;
}
