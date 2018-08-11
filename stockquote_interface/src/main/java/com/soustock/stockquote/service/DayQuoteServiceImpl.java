package com.soustock.stockquote.service;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.dao.DayQuoteDao;
import com.soustock.stockquote.dao.FuquanFactorDao;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.vo.DayQuoteVo;
import com.sun.management.VMOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
@Service("dayQuoteService")
public class DayQuoteServiceImpl implements DayQuoteService {

    @Autowired
    private DayQuoteDao dayQuoteDao;

    @Autowired
    private FuquanFactorDao fuquanFactorDao;

    @Override
    public List<DayQuoteVo> queryQuoteData(String stockCode, int recentlyCount, FuquanKind fuquanKind) throws Exception {
        switch (fuquanKind) {
            case Front:
                return queryQuoteDataOfFront(stockCode, recentlyCount);
            case Behind:
                return queryQuoteDataOfBehind(stockCode, recentlyCount);
            default:
                return queryQuoteDataOfOrigin(stockCode, recentlyCount);
        }
    }

    /**
     * 获取不复权行情
     *
     * @param stockCode
     * @param recentlyCount
     * @return
     * @throws Exception
     */
    private List<DayQuoteVo> queryQuoteDataOfOrigin(String stockCode, int recentlyCount) throws Exception {
        DayQuoteCdtVo dayQuoteCdtVo = new DayQuoteCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setRecentlyCount(recentlyCount);
        List<StockQuotePo> stockBasicPoList = dayQuoteDao.getStockQuotesByStockCodeCount(dayQuoteCdtVo);
        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockBasicPoList.size());
        for (StockQuotePo po : stockBasicPoList) {
            DayQuoteVo vo = new DayQuoteVo();
            vo.setTradeDate(po.getTradeDate());
            vo.setHighPrice(po.getHighPrice());
            vo.setOpenPrice(po.getOpenPrice());
            vo.setLowPrice(po.getLowPrice());
            vo.setClosePrice(po.getClosePrice());
            vo.setTradeQty(po.getTradeQty());
            vo.setTradeMoney(po.getTradeMoney());
            dayQuoteVos.add(vo);
        }
        return dayQuoteVos;
    }

    /**
     * 获取复权行情
     *
     * @param stockCode
     * @param recentlyCount
     * @return
     * @throws Exception
     */
    private List<DayQuoteVo> queryQuoteDataOfFront(String stockCode, int recentlyCount) throws Exception {
        DayQuoteCdtVo dayQuoteCdtVo = new DayQuoteCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setRecentlyCount(recentlyCount);
        List<StockQuotePo> stockQuotePoList = dayQuoteDao.getStockQuotesByStockCodeCount(dayQuoteCdtVo);
        List<FuquanFactorPo> fuquanFactorPoList = fuquanFactorDao.getFuquanFactorsByStockCodeCount(dayQuoteCdtVo);
        if (stockQuotePoList.size() != fuquanFactorPoList.size()) {
            throw new Exception("复权因子数据与行情数据不一致，获取失败.");
        }

        //最后一天的复权因子
        double currentFactor = fuquanFactorPoList.get(0).getFactor();
        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockQuotePoList.size());
        for (int index = 0; index < stockQuotePoList.size(); index++) {
            StockQuotePo stockQuotePo = stockQuotePoList.get(index);
            FuquanFactorPo fuquanFactorPo = fuquanFactorPoList.get(index);
            if (!stockQuotePo.getTradeDate().equals(fuquanFactorPo.getTradeDate())){
                throw new Exception("复权因子数据与行情数据不一致，获取失败.");
            }
            DayQuoteVo vo = new DayQuoteVo();
            vo.setTradeDate(stockQuotePo.getTradeDate());
            vo.setHighPrice(stockQuotePo.getHighPrice() * fuquanFactorPo.getFactor() / currentFactor);
            vo.setOpenPrice(stockQuotePo.getOpenPrice() * fuquanFactorPo.getFactor() / currentFactor);
            vo.setLowPrice(stockQuotePo.getLowPrice() * fuquanFactorPo.getFactor() / currentFactor);
            vo.setClosePrice(stockQuotePo.getClosePrice() * fuquanFactorPo.getFactor() / currentFactor);
            vo.setTradeQty(stockQuotePo.getTradeQty());
            vo.setTradeMoney(stockQuotePo.getTradeMoney());
            dayQuoteVos.add(vo);
        }
        return dayQuoteVos;
    }

    /**
     * 获取后复权行情（强制以上市第一天为基础复权)
     *
     * @param stockCode
     * @param recentlyCount
     * @return
     * @throws Exception
     */
    private List<DayQuoteVo> queryQuoteDataOfBehind(String stockCode, int recentlyCount) throws Exception {
        DayQuoteCdtVo dayQuoteCdtVo = new DayQuoteCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setRecentlyCount(recentlyCount);
        List<StockQuotePo> stockQuotePoList = dayQuoteDao.getStockQuotesByStockCodeCount(dayQuoteCdtVo);
        List<FuquanFactorPo> fuquanFactorPoList = fuquanFactorDao.getFuquanFactorsByStockCodeCount(dayQuoteCdtVo);
        if (stockQuotePoList.size() != fuquanFactorPoList.size()) {
            throw new Exception("复权因子数据与行情数据不一致，获取失败.");
        }

        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockQuotePoList.size());
        for (int index = 0; index < stockQuotePoList.size(); index++) {
            StockQuotePo stockQuotePo = stockQuotePoList.get(index);
            FuquanFactorPo fuquanFactorPo = fuquanFactorPoList.get(index);
            if (!stockQuotePo.getTradeDate().equals(fuquanFactorPo.getTradeDate())){
                throw new Exception("复权因子数据与行情数据不一致，获取失败.");
            }
            DayQuoteVo vo = new DayQuoteVo();
            vo.setTradeDate(stockQuotePo.getTradeDate());
            vo.setHighPrice(stockQuotePo.getHighPrice() * fuquanFactorPo.getFactor());
            vo.setOpenPrice(stockQuotePo.getOpenPrice() * fuquanFactorPo.getFactor());
            vo.setLowPrice(stockQuotePo.getLowPrice() * fuquanFactorPo.getFactor());
            vo.setClosePrice(stockQuotePo.getClosePrice() * fuquanFactorPo.getFactor());
            vo.setTradeQty(stockQuotePo.getTradeQty());
            vo.setTradeMoney(stockQuotePo.getTradeMoney());
            dayQuoteVos.add(vo);
        }
        return dayQuoteVos;
    }


}
