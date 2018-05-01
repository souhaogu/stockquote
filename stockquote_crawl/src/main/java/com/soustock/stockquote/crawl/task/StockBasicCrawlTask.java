package com.soustock.stockquote.crawl.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soustock.stockquote.crawl.cache.StockCodeCache;
import com.soustock.stockquote.crawl.common.BaseCrawlTask;
import com.soustock.stockquote.crawl.connect.XueqiuConnector;
import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.exception.BusinessException;
import com.soustock.stockquote.po.Constants;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.utils.ChineseToEnglish;
import com.soustock.stockquote.utils.DateUtity;
import com.soustock.stockquote.utils.NullCheckUtity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyufei on 2016/03/06.
 */
@Component
public class StockBasicCrawlTask extends BaseCrawlTask {

    private final static Log logger = LogFactory.getLog(StockBasicCrawlTask.class);

    @Autowired
    private StockBasicDao stockBasicDao;

    @Autowired
    private StockCodeCache stockCodeCache;

    @Override
    protected void process() throws BusinessException {
        try {
            for (String marketName : Constants.MARKET_NAME_ARR) {
                procMarket(marketName);
            }
            //更新缓存
            stockCodeCache.reset();
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    @Override
    public String getTaskName() {
        return "抓取股票基本信息";
    }

    @Override
    public int getExecuteOrder() {
        return 0;
    }

    private void procMarket(String marketName) throws IOException, BusinessException {
        //目前只处理两个市场，沪A和深A
        long countInDb = stockBasicDao.getStockCountOfMarket(marketName);
        long countFromWeb = getCountFromWeb(marketName);
        if (countFromWeb == countInDb) {
            logger.info(String.format("市场：%s，未发现新的股票代码，抓取任务不需执行.", marketName));
        } else {
            logger.info(String.format("市场：%s，新的股票代码被发现，抓取任务开始执行...", marketName));
            fetchDataFromWeb(marketName, countInDb, countFromWeb - countInDb);
            logger.info(String.format("市场：%s, 抓取任务执行完毕.", marketName));
        }
    }

    private void fetchDataFromWeb(String marketName, long fetchStart, long fetchCount) throws IOException, BusinessException {
        if (fetchCount <= 0) return;

        boolean bFetalExit = false;
        int pageSize = 90;
        int pageStart = (int) Math.ceil((fetchStart + 1.0) * 1.0 / pageSize);
        int pageEnd = (int) Math.ceil((fetchStart + fetchCount) * 1.0 / pageSize);
        int pageCount = pageEnd - pageStart + 1;
        logger.info(String.format("发现%d条记录，以%d条为一页，需要下载%d页.", fetchCount, pageSize, pageCount));
        for (int pageNum = pageStart; pageNum <= pageEnd; pageNum++) {
            logger.info(String.format("第%d页正在下载...", pageNum));
            String requestUrl = "https://xueqiu.com/proipo/query.json";
            Map<String, String> paramaters = new HashMap<>();
            paramaters.put("page", String.valueOf(pageNum)); //先下载上市日期早的
            paramaters.put("size", String.valueOf(pageSize));
            paramaters.put("order", "asc");
            paramaters.put("column", "symbol,name,list_date");
            paramaters.put("orderBy", "list_date");
            paramaters.put("type", "quote");
            paramaters.put("stockType", marketName);
            String jsonStr = XueqiuConnector.sendGet(requestUrl, paramaters, null);
            logger.info(String.format("第%d页已经完成下载.", pageNum));

            logger.info(String.format("第%d页正在解析, 即将写入数据库...", pageNum));
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (Object obj : jsonArray) {
                JSONArray fieldArr = (JSONArray) obj;
                if (NullCheckUtity.stringIsNull(fieldArr.getString(0)) ||
                        NullCheckUtity.stringIsNull(fieldArr.getString(1)) ||
                        NullCheckUtity.stringIsNull(fieldArr.getString(2))) {
                    bFetalExit = true;
                    logger.error(String.format("第%d页解析时发现空值，异常退出，原数据为%s.", pageNum, fieldArr.toString()));
                    break;
                }

                String stockCode = fieldArr.getString(0);
                StockBasicPo stockBasicPo = stockBasicDao.getStockBasicByStockCode(stockCode);
                if (stockBasicPo == null) {
                    stockBasicPo = new StockBasicPo();
                    stockBasicPo.setStockCode(stockCode);
                    stockBasicPo.setStockName(fieldArr.getString(1));
                    String pyName = ChineseToEnglish.getPinYinHeadChar(fieldArr.getString(1)).toLowerCase();
                    if (pyName.startsWith("*")) {
                        pyName = pyName.substring(1);
                    }
                    stockBasicPo.setPyName(pyName);
                    Date listDate = DateUtity.parseXueqiuFormatToDate(fieldArr.get(2).toString());
                    stockBasicPo.setListDate(DateUtity.dateToDateStr(listDate));
                    stockBasicPo.setMarket(marketName);
                    stockBasicPo.setUpdateTime(new Date().getTime());
                    stockBasicDao.insertStockBasic(stockBasicPo);
                }
            }

            if (bFetalExit) {
                break;
            }

            logger.info(String.format("第%d页解析完毕, 并写入数据库.", pageNum));
        }
    }

    private long getCountFromWeb(String marketName) throws IOException {
        String requestUrl = "https://xueqiu.com/proipo/query.json";
        Map<String, String> paramaters = new HashMap<>();
        paramaters.put("page", "1");
        paramaters.put("size", "30");
        paramaters.put("order", "asc");
        paramaters.put("column", "symbol");
        paramaters.put("orderBy", "list_date");
        paramaters.put("type", "quote");
        paramaters.put("stockType", marketName);

        String jsonStr = XueqiuConnector.sendGet(requestUrl, paramaters, null);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject.getDouble("count").longValue();
    }


}
