package com.soustock.stockquote.common;


import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.utils.RoundUtity;
import com.soustock.stockquote.vo.DayQuoteVo;
import com.soustock.stockquote.vo.MinuteQuoteVo;

import java.util.List;

/**
 * Created by xuyufei on 2018/10/27.
 */
public class RoundCommon {

    public static DayQuoteVo RoundStockQuote(DayQuoteVo dayQuoteVo){
        dayQuoteVo.setOpenPrice(RoundUtity.RoundDouble(dayQuoteVo.getOpenPrice(), 2));
        dayQuoteVo.setHighPrice(RoundUtity.RoundDouble(dayQuoteVo.getHighPrice(), 2));
        dayQuoteVo.setLowPrice(RoundUtity.RoundDouble(dayQuoteVo.getLowPrice(), 2));
        dayQuoteVo.setClosePrice(RoundUtity.RoundDouble(dayQuoteVo.getClosePrice(), 2));
        dayQuoteVo.setTradeQty(RoundUtity.RoundDouble(dayQuoteVo.getTradeQty(), 2));
        dayQuoteVo.setTradeMoney(RoundUtity.RoundDouble(dayQuoteVo.getTradeMoney(), 2));
        return dayQuoteVo;
    }

    public static List<DayQuoteVo> RoundStockQuoteList(List<DayQuoteVo> dayQuoteVoList){
        for (DayQuoteVo dayQuoteVo: dayQuoteVoList){
            RoundStockQuote(dayQuoteVo);
        }
        return dayQuoteVoList;
    }

    public static MinuteQuoteVo RoundMinuteQuote(MinuteQuoteVo minuteQuoteVo){
        minuteQuoteVo.setAvgPrice(RoundUtity.RoundDouble(minuteQuoteVo.getAvgPrice(), 2));
        minuteQuoteVo.setTradeQty(RoundUtity.RoundDouble(minuteQuoteVo.getTradeQty(), 2));
        return minuteQuoteVo;
    }

    public static List<MinuteQuoteVo> RoundMinuteQuoteList(List<MinuteQuoteVo> minuteQuoteVoList){
        for (MinuteQuoteVo minuteQuoteVo: minuteQuoteVoList){
            RoundMinuteQuote(minuteQuoteVo);
        }
        return minuteQuoteVoList;
    }



}
