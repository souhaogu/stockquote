package com.soustock.stockquote.service;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.vo.DayQuoteVo;

import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
public interface FuquanFactorService {

    String getMaxDateOfStockFuquan(String stockCode);

    void insertFuquanFactors(List<FuquanFactorPo> fuquanFactorPos);
}
