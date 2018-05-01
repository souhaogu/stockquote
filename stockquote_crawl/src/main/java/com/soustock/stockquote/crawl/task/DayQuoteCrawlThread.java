package com.soustock.stockquote.crawl.task;


import com.soustock.stockquote.dao.DayQuoteDao;
import com.soustock.stockquote.dao.StockBasicDao;
import com.soustock.stockquote.po.StockBasicPo;
import com.soustock.stockquote.po.StockQuotePo;
import com.soustock.stockquote.utils.DateUtity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by xuyufei on 2015/9/27.
 * 处理单个股票
 */
public class DayQuoteCrawlThread implements Runnable {

    private final static Log logger = LogFactory.getLog(DayQuoteCrawlThread.class);

    private StockBasicDao stockBasicDao;

    private DayQuoteDao dayQuoteDao;

    /**
     * 股票代码
     */
    private String stockCode;

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    /**
     * 股票总数
     */
    private Integer stockCount;

    /**
     * 当前进度
     */
    private AtomicInteger stockIndex;

    public void setStockIndex(AtomicInteger stockIndex) {
        this.stockIndex = stockIndex;
    }

    public DayQuoteCrawlThread(String stockCode) {
        this.stockCode = stockCode;
    }

    @Override
    public void run() {
        int row = stockIndex.incrementAndGet();
        logger.info(String.format("Day quote crawl: %d/%d, %s...", row, stockCount, stockCode));
        try {
            fetchQuoteForEachStock(stockCode);
            logger.info(String.format("Day quote crawl: %d/%d, %s ...ok.", row, stockCount, stockCode));
        } catch (IOException e) {
            logger.info(String.format("Day quote crawl: %d/%d, %s ...failure, the detail cause is:" + e.getMessage(), row, stockCount, stockCode), e);
        }
    }

    private void fetchQuoteForEachStock(String stockCode) throws IOException {
        String maxDate = dayQuoteDao.getMaxDateOfStock(stockCode);
        StockBasicPo stockBasicPo = stockBasicDao.getStockBasicByStockCode(stockCode);
        getQuoteObjs(stockCode, stockBasicPo.getListDate(), maxDate);
    }

    /**
     * 获取某个股票的行情
     *
     * @param stockCode
     * @param maxDate   返还的行情必须大于maxDate，便于增量下载
     * @return
     * @throws IOException
     */
    public void getQuoteObjs(String stockCode, final String listDate, final String maxDate) throws IOException {
        Date dtNow = new Date();
        if (DateUtity.dateToDateStr(dtNow).equals(maxDate)) {//当天已经更新，则无需下载
            return;
        }

        try {
            int toYear = DateUtity.getYear(dtNow);
            int toJidu = DateUtity.getJidu(dtNow);

            Date dtList = DateUtity.parseDateStrToDate(listDate);
            int fromYear = DateUtity.getYear(dtList);
            int fromJidu = DateUtity.getJidu(dtList);
            if (maxDate != null) {
                Date dtMaxDate = DateUtity.parseDateStrToDate(maxDate);
                fromYear = DateUtity.getYear(dtMaxDate);
                fromJidu = DateUtity.getJidu(dtMaxDate);
            }

            String urlFormat = "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s.phtml?year=%d&jidu=%d";
            while ((fromYear * 4 + fromJidu) <= (toYear * 4 + toJidu)) {
                String url = String.format(urlFormat, stockCode.substring(2), fromYear, fromJidu);
                Document doc = null;
                boolean bTimeOut = false;
                int tryTimes = 0;
                while (doc == null) {
                    try {
                        tryTimes++;
                        doc = Jsoup.connect(url).timeout(300000).get();
                    } catch (IOException ex) {
                        doc = null;
                        if (tryTimes == 5) {
                            logger.error("Timeout error occured when connect the url: " + url + ", the detail cause is：" + ex.getMessage(), ex);
                            bTimeOut = true;
                            break;
                        }
                        Thread.sleep( DateUtity.MS_OF_MINUTE * ( 5 + tryTimes *3 ));
                    }
                }
                //超时退出的，继续退出
                if (bTimeOut) break;

                try {
                    Element table = doc.getElementById("FundHoldSharesTable");
                    if (table != null) {
                        List<StockQuotePo> insertObjs = new ArrayList<>();
                        Elements trs = table.getElementsByTag("tr");
                        for (int index = trs.size() - 1; index >= 2; index--) {
                            Element tr = trs.get(index);
                            Elements tds = tr.getElementsByTag("td");
                            String dtStr = tds.get(0).text().trim().replace("-", "");
                            if ((maxDate == null) || (dtStr.compareTo(maxDate) > 0)) {
                                StockQuotePo insertObj = new StockQuotePo();
                                insertObj.setStockCode(stockCode);
                                insertObj.setTradeDate(dtStr);
                                insertObj.setOpenPrice(Double.parseDouble(tds.get(1).text().trim()));
                                insertObj.setHighPrice(Double.parseDouble(tds.get(2).text().trim()));
                                insertObj.setLowPrice(Double.parseDouble(tds.get(4).text().trim()));
                                insertObj.setClosePrice(Double.parseDouble(tds.get(3).text().trim()));
                                insertObj.setTradeQty(Double.parseDouble(tds.get(5).text().trim()));
                                insertObj.setTradeMoney(Double.parseDouble(tds.get(6).text().trim()));
                                insertObj.setUpdateTime(System.currentTimeMillis());
                                insertObjs.add(insertObj);
                            }
                        }
                        if (!insertObjs.isEmpty()) {
                            dayQuoteDao.insertDayQuotes(insertObjs);
                        }
                    }
                } catch (Exception ex) {
                    logger.error("One error occured when analyzing the page, the url is: " + url +
                            ", the detail cause is：" + ex.getMessage(), ex);
                    throw ex;
                }

                if (fromJidu < 4) {
                    fromJidu++;
                } else {
                    fromJidu = 1;
                    fromYear++;
                }
            }
        } catch (Exception e) {
            logger.error("One error occured when analyzing the code: " + stockCode + ", the detail cause is：" + e.getMessage(), e);
        }
    }

    public void setDayQuoteDao(DayQuoteDao dayQuoteDao) {
        this.dayQuoteDao = dayQuoteDao;
    }

    public void setStockBasicDao(StockBasicDao stockBasicDao) {
        this.stockBasicDao = stockBasicDao;
    }
}
