package com.soustock.stockquote.service;


import com.soustock.stockquote.common.FuquanKind;
import com.soustock.stockquote.dao.DayQuoteDao;
import com.soustock.stockquote.dao.FuquanFactorDao;
import com.soustock.stockquote.po.DayQuoteCdtVo;
import com.soustock.stockquote.po.DayQuoteDateCdtVo;
import com.soustock.stockquote.po.FuquanFactorPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.vo.DayQuoteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        DayQuoteCdtVo dayQuoteCdtVo = new DayQuoteCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setRecentlyCount(recentlyCount);
        List<StockQuotePo> stockQuotePoList = dayQuoteDao.getStockQuotesByStockCodeCount(dayQuoteCdtVo);

        if (fuquanKind == FuquanKind.Origin){
            return turnOrigin(stockQuotePoList);
        } else {
            List<FuquanFactorPo> fuquanFactorPoList = fuquanFactorDao.getFuquanFactorsByStockCodeCount(dayQuoteCdtVo);
            TreeMap<String, Double> fuquanFactorMap = turnFuquanFactorMap(fuquanFactorPoList);
            if (fuquanKind == FuquanKind.Front){
                return turnFuquanOfFront(stockQuotePoList, fuquanFactorMap);
            } else {
                return turnFuquanOfBehind(stockQuotePoList, fuquanFactorMap);
            }
        }
    }

    public List<DayQuoteVo> queryQuoteByDate(String stockCode, String bgnDate, String endDate, FuquanKind fuquanKind ) throws Exception{
        DayQuoteDateCdtVo dayQuoteCdtVo = new DayQuoteDateCdtVo();
        dayQuoteCdtVo.setStockCode(stockCode);
        dayQuoteCdtVo.setBgnDate(bgnDate);
        dayQuoteCdtVo.setEndDate(endDate);
        List<StockQuotePo> stockQuotePoList = dayQuoteDao.getStockQuotesByStockCodeDate(dayQuoteCdtVo);
        if (fuquanKind == FuquanKind.Origin){
            return turnOrigin(stockQuotePoList);
        } else {
            List<FuquanFactorPo> fuquanFactorPoList = fuquanFactorDao.getFuquanFactorsByStockCodeDate(dayQuoteCdtVo);
            TreeMap<String, Double> fuquanFactorMap = turnFuquanFactorMap(fuquanFactorPoList);
            if (fuquanKind == FuquanKind.Front){
                return turnFuquanOfFront(stockQuotePoList, fuquanFactorMap);
            } else {
                return turnFuquanOfBehind(stockQuotePoList, fuquanFactorMap);
            }
        }
    }

    @Override
    public String getMaxTradeDateOfStock(String stockCode) {
        return dayQuoteDao.getMaxDateOfStock(stockCode);
    }

    @Override
    public void insertDayQuotes(List<StockQuotePo> stockQuotePos) {
        dayQuoteDao.insertDayQuotes(stockQuotePos);
    }


    private TreeMap<String, Double> turnFuquanFactorMap(List<FuquanFactorPo> fuquanFactorPoList){
        TreeMap<String, Double> fuquanFactorMap = new TreeMap<>();
        String lastDate = "";
        for (int index = fuquanFactorPoList.size() - 1; index >= 0; index--) {
            FuquanFactorPo fuquanFactorPo = fuquanFactorPoList.get(index);
            if (!fuquanFactorPo.getTradeDate().equals(lastDate)) {
                fuquanFactorMap.put(fuquanFactorPo.getTradeDate(), fuquanFactorPo.getFactor());
            }
        }
        return fuquanFactorMap;
    }


    /**
     * 获取不复权行情
     * @return
     * @throws Exception
     */
    private List<DayQuoteVo> turnOrigin(List<StockQuotePo> stockQuotePoList) throws Exception {
        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockQuotePoList.size());
        for (StockQuotePo po : stockQuotePoList) {
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
     * 获取前复权行情
     * @return
     * @throws Exception
     */
    private List<DayQuoteVo> turnFuquanOfFront(List<StockQuotePo> stockQuotePoList, TreeMap<String, Double> fuquanFactorMap) throws Exception {
        //最后一天的复权因子
        double currentFactor = fuquanFactorMap.lastEntry().getValue();

        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockQuotePoList.size());
        for (int index = 0; index < stockQuotePoList.size(); index++) {
            StockQuotePo stockQuotePo = stockQuotePoList.get(index);
            double factor = 1.0;
            Map.Entry<String, Double> entry = fuquanFactorMap.floorEntry(stockQuotePo.getTradeDate());
            if (entry!=null){
                factor = entry.getValue();
            }
            DayQuoteVo vo = new DayQuoteVo();
            vo.setTradeDate(stockQuotePo.getTradeDate());
            vo.setHighPrice(stockQuotePo.getHighPrice() * factor / currentFactor);
            vo.setOpenPrice(stockQuotePo.getOpenPrice() * factor / currentFactor);
            vo.setLowPrice(stockQuotePo.getLowPrice() * factor / currentFactor);
            vo.setClosePrice(stockQuotePo.getClosePrice() * factor / currentFactor);
            vo.setTradeQty(stockQuotePo.getTradeQty());
            vo.setTradeMoney(stockQuotePo.getTradeMoney());
            dayQuoteVos.add(vo);
        }
        return dayQuoteVos;
    }


    /**
     * 获取后复权行情（强制以上市第一天为基础复权)
     * @return
     * @throws Exception
     */
    private List<DayQuoteVo> turnFuquanOfBehind(List<StockQuotePo> stockQuotePoList, TreeMap<String, Double> fuquanFactorMap) throws Exception {
        List<DayQuoteVo> dayQuoteVos = new ArrayList<>(stockQuotePoList.size());
        for (int index = 0; index < stockQuotePoList.size(); index++) {
            StockQuotePo stockQuotePo = stockQuotePoList.get(index);
            double factor = 1.0;
            Map.Entry<String, Double> entry = fuquanFactorMap.floorEntry(stockQuotePo.getTradeDate());
            if (entry!=null){
                factor = entry.getValue();
            }
            DayQuoteVo vo = new DayQuoteVo();
            vo.setTradeDate(stockQuotePo.getTradeDate());
            vo.setHighPrice(stockQuotePo.getHighPrice() * factor);
            vo.setOpenPrice(stockQuotePo.getOpenPrice() * factor);
            vo.setLowPrice(stockQuotePo.getLowPrice() * factor);
            vo.setClosePrice(stockQuotePo.getClosePrice() * factor);
            vo.setTradeQty(stockQuotePo.getTradeQty());
            vo.setTradeMoney(stockQuotePo.getTradeMoney());
            dayQuoteVos.add(vo);
        }
        return dayQuoteVos;
    }
}
