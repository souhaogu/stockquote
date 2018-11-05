package com.soustock.stockquote.crawl.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soustock.stockquote.crawl.connect.XueqiuConnector;
import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.utils.pinyin4j.PinyinConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by xuyufei on 2018/11/4.
 */
public class StockBasicUpdateProc {

    private final static Log logger = LogFactory.getLog(StockBasicUpdateProc.class);

    private StockBasicDao stockBasicDao;

    public StockBasicUpdateProc(StockBasicDao stockBasicDao){
        this.stockBasicDao = stockBasicDao;
    }

    public void doProc() throws IOException, InterruptedException, BusinessException {
        List<StockBasicPo> stockBasicPoList = stockBasicDao.getAllStockBasics();
        Map<String, StockBasicPo> stockBasicPoMap = new HashMap<>(stockBasicPoList.size());
        for (StockBasicPo stockBasicPo: stockBasicPoList){
            stockBasicPoMap.put(stockBasicPo.getStockCode(), stockBasicPo);
        }

        boolean bFetalExit = false;
        //若存在更名，请更新股票的代码、名称和简称
        int totalCount = getCountFromWeb();
        int pageSize = 90;
        int pageStart = 1;
        int pageCount = (int) Math.ceil(totalCount * 1.0 / pageSize);
        int pageEnd = pageStart + pageCount;
        logger.info(String.format("stock basic update, found %d stocks，make %d row as one page，so need download %d pages.", totalCount, pageSize, pageCount));
        PinyinConverter pinyinConverter = new PinyinConverter();
        for (int pageNum = pageStart; pageNum <= pageEnd; pageNum++) {
            logger.info(String.format("Now the page %d will be download...", pageNum));
            String requestUrl = "https://xueqiu.com/stock/cata/stocklist.json";
            Map<String, String> paramaters = new HashMap<>();
            paramaters.put("page", String.valueOf(pageNum));
            paramaters.put("size", String.valueOf(pageSize));
            paramaters.put("order", "asc");
            paramaters.put("orderBy", "code");
            paramaters.put("type", "11");
            paramaters.put("_", String.valueOf(System.currentTimeMillis()));
            String jsonStr = XueqiuConnector.sendGet(requestUrl, paramaters, null);
            logger.info(String.format("the %d page has been downloaded.", pageNum));

            logger.info(String.format("第%d页正在解析, 即将写入数据库...", pageNum));
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            boolean succ = Boolean.parseBoolean(jsonObject.getString("success"));
            if (succ) {
                JSONArray stockArr = jsonObject.getJSONArray("stocks");
                for (Object obj : stockArr) {
                    JSONObject stockObj = (JSONObject) obj;
                    String stockCode = stockObj.getString("symbol");
                    String stockName = stockObj.getString("name");
                    String pyName = pinyinConverter.getPinyinFirstSpell(stockName).toLowerCase();
                    if (pyName.startsWith("*")) {
                        pyName = pyName.substring(1);
                    }
                    StockBasicPo stockBasicPo = stockBasicPoMap.get(stockCode);
                    if (stockBasicPo != null){
                        if (!stockBasicPo.getStockName().equals(stockName)||
                                !stockBasicPo.getPyName().equals(pyName)) {
                            stockBasicPo.setStockName(stockName);
                            stockBasicPo.setPyName(pyName);
                            stockBasicPo.setUpdateTime(System.currentTimeMillis());
                            stockBasicDao.updateStockBasic(stockBasicPo);
                            Thread.sleep(10);
                        }
                    }
                }

                if (bFetalExit) {
                    break;
                }

                logger.info(String.format("the page '%d' is analyzed, and written into data store.", pageNum));
            }
        }
    }


    /**
     * 获取雪球上股票的条数
     * @return
     * @throws IOException
     */
    private int getCountFromWeb() throws IOException {
        String requestUrl = "https://xueqiu.com/stock/cata/stocklist.json";
        Map<String, String> paramaters = new HashMap<>();
        paramaters.put("page", "1");
        paramaters.put("size", "30");
        paramaters.put("order", "asc");
        paramaters.put("orderBy", "stock_code");
        paramaters.put("type", "11");
        paramaters.put("_", String.valueOf(System.currentTimeMillis()));

        String jsonStr = XueqiuConnector.sendGet(requestUrl, paramaters, null);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Map countMap = (Map) jsonObject.get("count");
        return (int) countMap.get("count");
    }

}
