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

    private volatile boolean isBusy = false;

    @PostConstruct
    public void init() {
        List<StockSimpleVo> retList = getStockSimpleVoList();

        isBusy = true;
        stockSimpleVoList = retList;
        isBusy = false;
    }

    private List<StockSimpleVo> getStockSimpleVoList(){
        List<StockSimpleVo> retList = new ArrayList<>(4000);
        List<StockBasicPo> stockBasicPoList = stockBasicDao.getAllStockBasics();
        for (StockBasicPo stockBasicPo: stockBasicPoList ){
            StockSimpleVo stockSimpleVo = new StockSimpleVo();
            stockSimpleVo.setStockCode(stockBasicPo.getStockCode());
            stockSimpleVo.setPyName(stockBasicPo.getPyName());
            stockSimpleVo.setStockName(stockBasicPo.getStockName());
            retList.add(stockSimpleVo);
        }
        return retList;
    }

    private final static int FETCH_COUNT_LIKESTR = 10;
    @Override
    public List<StockSimpleVo> getStockSimpleVosOfLikeStr(String likeStr) throws Exception {
        waitForNotBusy();

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

    private void waitForNotBusy(){
        while (isBusy){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public StockSimpleVo getStockBasicByStockCode(String stockCode) throws Exception {
        waitForNotBusy();
        StockBasicPo stockBasicPo = stockBasicDao.getStockBasicByStockCode(stockCode);
        StockSimpleVo stockSimpleVo = new StockSimpleVo();
        stockSimpleVo.setStockCode(stockBasicPo.getStockCode());
        stockSimpleVo.setPyName(stockBasicPo.getPyName());
        stockSimpleVo.setStockName(stockBasicPo.getStockName());
        return stockSimpleVo;
    }

    @Override
    public void refreshCache() {
        this.init();
    }

    @Override
    public void insertOrUpdateStockBasics(List<StockBasicPo> stockInfoList) {
        for (StockBasicPo stockBasicPo: stockInfoList){
            String stockCode = stockBasicPo.getStockCode();
            if (null == stockBasicDao.getStockBasicByStockCode(stockCode)){
                stockBasicDao.insertStockBasic(stockBasicPo);
            } else {
                stockBasicDao.updateStockBasic(stockBasicPo);
            }
        }
    }

    @Override
    public List<StockBasicPo> getStockBasicsAfter(Long updateTime) {
        return stockBasicDao.getStockBasicsAfter(updateTime);
    }

    @Override
    public Long getMaxUpdatetimeOfStockBasic() {
        return stockBasicDao.getMaxUpdatetimeOfStockBasic();
    }
}
