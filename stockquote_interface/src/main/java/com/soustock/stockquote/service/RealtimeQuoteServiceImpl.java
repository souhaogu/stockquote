package com.soustock.stockquote.service;


import com.soustock.stockquote.utils.HttpRequester;
import com.soustock.stockquote.vo.MinuteQuoteVo;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyufei on 2018/4/28.
 */
@Service("realtimeQuoteService")
public class RealtimeQuoteServiceImpl implements RealtimeQuoteService {

    @Override
    public List<MinuteQuoteVo> queryQuoteData(String stockCode, int recentlyCount) throws Exception {
        List<MinuteQuoteVo> minuteQuoteVoList = new ArrayList<>();

        String url = String.format("http://vip.stock.finance.sina.com.cn/quotes_service/view/vML_DataList.php?asc=j&symbol=%s&num=%d", stockCode.toLowerCase(), recentlyCount);
        HttpRequester httpRequester = new HttpRequester();
        String responseStr = httpRequester.sendGet(url);
        int pos = responseStr.indexOf('=');
        String[] arr = responseStr.substring(pos + 1).replaceAll("( |'|\\[|\\]\\];|\n)", "").split("],");
        for (String row: arr){
            String[] col_arr = row.split(",");
            if (col_arr.length == 3) {
                MinuteQuoteVo vo = new MinuteQuoteVo();
                vo.setTradeTime(col_arr[0]);
                vo.setAvgPrice(Double.parseDouble(col_arr[1]));
                vo.setTradeQty(Double.parseDouble(col_arr[2]));
                minuteQuoteVoList.add(vo);
            }
        }
        return minuteQuoteVoList;
    }

    //var minute_data_list = [['15:00:00', '11.61', '0'],['14:59:00', '11.61', '369400'],['14:58:00', '11.61', '134090'],['14:57:00', '11.61', '159443'],['14:56:00', '11.61', '89400'],['14:55:00', '11.61', '95500'],['14:54:00', '11.61', '52500'],['14:53:00', '11.62', '43987'],['14:52:00', '11.61', '31226'],['14:51:00', '11.63', '77074'],['14:50:00', '11.63', '24200'],['14:49:00', '11.61', '129800'],['14:48:00', '11.61', '103408'],['14:47:00', '11.6', '122200'],['14:46:00', '11.6', '19400'],['14:45:00', '11.59', '298000'],['14:44:00', '11.61', '55967'],['14:43:00', '11.6', '162293'],['14:42:00', '11.61', '336400'],['14:41:00', '11.6', '29000'],['14:40:00', '11.59', '170600'],['14:39:00', '11.6', '58000'],['14:38:00', '11.6', '32400'],['14:37:00', '11.6', '235000'],['14:36:00', '11.61', '80200'],['14:35:00', '11.61', '76700'],['14:34:00', '11.61', '242500'],['14:33:00', '11.63', '68000'],['14:32:00', '11.62', '262620'],['14:31:00', '11.6', '68800'],['14:30:00', '11.59', '46300'],['14:29:00', '11.6', '152305'],['14:28:00', '11.58', '125600'],['14:27:00', '11.59', '46700']];
    public static void main(String[] args) throws Exception {
        RealtimeQuoteServiceImpl realtimeQuoteService = new RealtimeQuoteServiceImpl();
        List<MinuteQuoteVo> minuteQuoteVoList = realtimeQuoteService.queryQuoteData("sh600000", 242);
        for (MinuteQuoteVo vo: minuteQuoteVoList){
            System.out.println(String.format("time:%s, price:%f, qty:%f", vo.getTradeTime(), vo.getAvgPrice(), vo.getTradeQty()));
        }
    }


}
