package com.soustock.stockquote.crawl.connect;


import com.soustock.stockquote.utils.HttpRequester;
import com.soustock.stockquote.utils.TxtFileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyufei on 2015/9/19.
 */
public class XueqiuConnector {

    public static String xueqiu_cookie;

    static {
        try {
            xueqiu_cookie = readXueqiuCookieFromFile();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static String readXueqiuCookieFromFile() throws IOException {
        String xueqiu_cookie_file = "/root/stock_quote/xueqiu_cookie.txt";
        TxtFileReader txtFileReader = new TxtFileReader(xueqiu_cookie_file);
        try {
            return txtFileReader.readLine();
        } finally {
            txtFileReader.close();
        }
    }

    /**
     * send a GET request
     *
     * @param urlString
     *            URL String
     * @param params
     *            parameters map
     * @param properties
     *            properties map
     * @return reponsing string
     * @throws IOException
     */
    public static String sendGet(String urlString, Map<String, String> params,
                          Map<String, String> properties) throws IOException {
        HttpRequester httpRequester = new HttpRequester();
        if (properties==null){
            properties = new HashMap<>();
        }
        properties.put("Cookie", xueqiu_cookie);
        return httpRequester.sendGet(urlString, params, properties);
    }

}
