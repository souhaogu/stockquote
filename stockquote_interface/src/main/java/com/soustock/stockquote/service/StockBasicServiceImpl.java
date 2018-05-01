package com.soustock.stockquote.service;


import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.vo.StockSimpleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 *
 */
@Service("stockBasicService")
public class StockBasicServiceImpl implements StockBasicService {

    @Autowired
    private StockBasicDao stockBasicDao;

    private List<StockSimpleVo> stockSimpleVoList = new ArrayList<>(4000);

    @PostConstruct
    public void init() throws Exception {
        List<StockBasicPo> stockBasicPoList = stockBasicDao.getAllStockBasics();
        for (StockBasicPo stockBasicPo: stockBasicPoList ){
            StockSimpleVo stockSimpleVo = new StockSimpleVo();
            stockSimpleVo.setStockCode(stockBasicPo.getStockCode());
            stockSimpleVo.setPyName(stockBasicPo.getPyName());
            stockSimpleVo.setStockName(stockBasicPo.getStockName());
            stockSimpleVoList.add(stockSimpleVo);
        }
    }

    private final static int FETCH_COUNT_LIKESTR = 10;
    @Override
    public List<StockSimpleVo> getStockSimpleVosOfLikeStr(String likeStr) throws Exception {
        likeStr = likeStr.toUpperCase();

        List<StockSimpleVo> retList = new ArrayList<>(FETCH_COUNT_LIKESTR);
        int index = 0;
        for (StockSimpleVo stockSimpleVo: stockSimpleVoList ){
            if (stockSimpleVo.getStockCode().toUpperCase().contains(likeStr)||
                    stockSimpleVo.getStockName().toUpperCase().contains(likeStr)||
                    stockSimpleVo.getPyName().toUpperCase().contains(likeStr)){
                retList.add(stockSimpleVo);
                index ++;
                if (index >= FETCH_COUNT_LIKESTR)
                    break;
            }
        }
        return retList;
    }

    @Override
    public StockSimpleVo getStockBasicByStockCode(String stockCode) throws Exception {
        StockBasicPo stockBasicPo = stockBasicDao.getStockBasicByStockCode(stockCode);
        StockSimpleVo stockSimpleVo = new StockSimpleVo();
        stockSimpleVo.setStockCode(stockBasicPo.getStockCode());
        stockSimpleVo.setPyName(stockBasicPo.getPyName());
        stockSimpleVo.setStockName(stockBasicPo.getStockName());
        return stockSimpleVo;
    }
}
