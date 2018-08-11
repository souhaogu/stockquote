package com.soustock.stockquote.service;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.vo.DayQuoteVo;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
public interface DayQuoteService {


    List<DayQuoteVo> queryQuoteData(String stockCode, int recentlyCount, FuquanKind fuquanKind ) throws Exception;
}
