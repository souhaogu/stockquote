package com.soustock.stockquote.crawl.task;


import com.soustock.stockquote.dao.FinanceSummaryDao;
import com.soustock.stockquote.po.FinanceSummaryPo;
import com.soustock.stockquote.utils.DateUtity;
import com.soustock.stockquote.utils.StringUtity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by xuyufei on 2015/9/27.
 * 处理单个股票的复权因子
 */
public class FinanceSummaryCrawlThread implements Runnable {

    private final static Log logger = LogFactory.getLog(FinanceSummaryCrawlThread.class);

    private String listDate;

    private FinanceSummaryDao financeSummaryDao;

    /**
     * 股票代码
     */
    private String stockCode;

    public FinanceSummaryCrawlThread(String stockCode) {
        this.stockCode = stockCode;
    }

    /**
     * 股票总数
     */
    private Integer stockCount;

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    /**
     * 当前进度
     */
    private AtomicInteger stockIndexAi;

    public void setStockIndexAi(AtomicInteger stockIndexAi) {
        this.stockIndexAi = stockIndexAi;
    }

    @Override
    public void run() {
        int stockIndex = stockIndexAi.incrementAndGet();
        logger.info(String.format("Fuquan factor crawl: %d/%d, %s...", stockIndex, stockCount, stockCode));
        try {
            fetchFuquanFactorForEachStock(stockCode);
            logger.info(String.format("Fuquan factor crawl: %d/%d, %s ...ok.", stockIndex, stockCount, stockCode));
        } catch (IOException e) {
            logger.info(String.format("Fuquan factor crawl: %d/%d, %s ...failure, the detail cause is:" + e.getMessage(), stockIndex, stockCount, stockCode), e);
        }
    }

    private void fetchFuquanFactorForEachStock(String stockCode) throws IOException {
        String maxDate = financeSummaryDao.getMaxDateOfFinanceSummary(stockCode);
        List<FinanceSummaryPo> insertObjs = analysisHtml(maxDate);
        if ((insertObjs != null) && !insertObjs.isEmpty()) {
            financeSummaryDao.insertFinanceSummarys(insertObjs);
        }
    }

    private List<FinanceSummaryPo> analysisHtml(String maxDate){
        String url = String.format("http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/%s.phtml", stockCode.substring(2));
        Document doc = null;
        int tryTimes = 0;
        while (doc == null) {
            try {
                tryTimes++;
                doc = Jsoup.connect(url).timeout(300000).get();
            } catch (IOException ex) {
                doc = null;
                if (tryTimes == 5) {
                    logger.error("Timeout error occured when connect the url: " + url + ", the detail cause is：" + ex.getMessage(), ex);
                    break;
                }
                try {
                    Thread.sleep(DateUtity.MS_OF_MINUTE * (5 + tryTimes * 3));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (doc != null){
            try {
                Element table = doc.getElementById("FundHoldSharesTable");
                if (table != null) {
                    List<FinanceSummaryPo> insertObjs = new ArrayList<>();
                    Elements trs = table.getElementsByTag("tr");
                    int height = 13;
                    int firstRowOfSeason = trs.size() - height;
                    while (firstRowOfSeason >= 0) {
                        Element tr = trs.get(firstRowOfSeason);
                        Elements tds = tr.getElementsByTag("td");
                        String dtStr = tds.get(1).text().trim().replace("-", "");
                        if ((maxDate == null) || (dtStr.compareTo(maxDate) > 0)) {
                            //每股净资产
                            tr = trs.get(firstRowOfSeason + 1);
                            tds = tr.getElementsByTag("td");
                            String td_text = tds.get(1).text();
                            double naps = getTextValue(td_text);

                            //每股收益
                            tr = trs.get(firstRowOfSeason + 2);
                            tds = tr.getElementsByTag("td");
                            td_text = tds.get(1).text();
                            double eps = getTextValue(td_text);

                            //每股现金
                            tr = trs.get(firstRowOfSeason + 3);
                            tds = tr.getElementsByTag("td");
                            td_text = tds.get(1).text();
                            double cps = getTextValue(td_text);

                            //每股资本公积
                            tr = trs.get(firstRowOfSeason + 4);
                            tds = tr.getElementsByTag("td");
                            td_text = tds.get(1).text();
                            double cfps = getTextValue(td_text);

                            FinanceSummaryPo insertObj = new FinanceSummaryPo();
                            insertObj.setStockCode(stockCode);
                            insertObj.setFinanceDate(dtStr);
                            insertObj.setNaps(naps);
                            insertObj.setEps(eps);
                            insertObj.setCps(cps);
                            insertObj.setCfps(cfps);
                            insertObj.setUpdateTime(System.currentTimeMillis());
                            insertObjs.add(insertObj);
                        }
                        firstRowOfSeason -= height;
                    }
                    return insertObjs;
                }
            } catch (Exception ex) {
                logger.error("One error occured when analyzing the page, the url is: " + url +
                        ", the detail cause is：" + ex.getMessage(), ex);
                throw ex;
            }
        }
        return null;
    }

    private static double getTextValue(String textValue){
        if (textValue.endsWith("元")){
            textValue = textValue.substring(0, textValue.length() - 1);
        }
        return StringUtity.isNumeric(textValue) ? Double.parseDouble(textValue): -1.0;
    }


    public void setFinanceSummaryDao(FinanceSummaryDao financeSummaryDao) {
        this.financeSummaryDao = financeSummaryDao;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public static void main(String[] args){
        FinanceSummaryCrawlThread financeSummaryCrawlThread = new FinanceSummaryCrawlThread("SH600000");
        financeSummaryCrawlThread.setStockCount(3000);
        List<FinanceSummaryPo> financeSummaryPoList = financeSummaryCrawlThread.analysisHtml(null);
        System.out.println(financeSummaryPoList.toString());
    }
}
