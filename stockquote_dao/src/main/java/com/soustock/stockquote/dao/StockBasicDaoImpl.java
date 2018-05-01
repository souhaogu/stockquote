package com.soustock.stockquote.dao;

import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.mapper.StockBasicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuyufei on 2016/3/6.
 */
@Repository("stockBasicDao")
public class StockBasicDaoImpl implements StockBasicDao {

    @Autowired
    private StockBasicMapper stockBasicMapper;


    @Override
    public StockBasicPo getStockBasicByStockCode(String stockCode) {
        return stockBasicMapper.getStockBasicByStockCode(stockCode);
    }

    @Override
    public void insertStockBasic(StockBasicPo stockInfo) {
        stockBasicMapper.insertStockBasic(stockInfo);
    }

    /**
     * 修改一条股票信息
     * @param stockInfo
     */
    public void updateStockBasic(StockBasicPo stockInfo){
        stockBasicMapper.updateStockBasic(stockInfo);
    }

    /**
     * 获取一个市场中的股票数目
     * @param market
     * @return
     */
    public long getStockCountOfMarket(String market){
        return stockBasicMapper.getStockCountOfMarket(market);
    }

//    @Override
//    public PageList<StockBasicPo> getStockInfosOfMarket(String market, PageCdt pageCdt) {
//        long total = stockBasicMapper.getStockCountOfMarket(market);
//        int pageSize = (int)Math.ceil((total * 1.0) / pageCdt.getPageSize());
//        int startRow = (pageCdt.getPageNum()-1)* pageCdt.getPageSize();
//        List<StockBasicPo> stockInfoVos = stockBasicMapper.getStockInfosOfMarket(market,
//                startRow, pageCdt.getPageSize());
//        PageList<StockBasicPo> result = new PageList<>();
//        result.setPageCdt(pageCdt);
//        result.setPages(pageSize);
//        result.setResult(stockInfoVos);
//        result.setTotal(total);
//        return result;
//    }

    @Override
    public List<StockBasicPo> getAllStockBasics() {
        return stockBasicMapper.getAllStockBasics();
    }

    @Override
    public List<StockBasicPo> getAllStockBasicsOfMarket(String market) {
        return stockBasicMapper.getAllStockBasicsOfMarket(market);
    }


}
