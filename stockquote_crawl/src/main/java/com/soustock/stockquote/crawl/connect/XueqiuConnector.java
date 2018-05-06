package com.soustock.stockquote.crawl.connect;


import com.soustock.stockquote.utils.HttpRequester;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyufei on 2015/9/19.
 */
public class XueqiuConnector {

    public final static String STR_COOKIE_XUEQIU = "aliyungf_tc=AQAAADIv9D2iBg0AEuz2Os0ROA22piAe; Hm_lvt_1db88642e346389874251b5a1eded6e3=1525504559; __utmc=1; __utmz=1.1525504560.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; device_id=349a3416b726345fff81d17113d03b91; Hm_lvt_39dcd5bd05965dcfa70b1d2457c6dcae=1525504573; __utma=1.515195785.1525504560.1525504560.1525513480.2; __utmt=1; remember=1; remember.sig=K4F3faYzmVuqC0iXIERCQf55g2Y; xq_a_token=59588964d9980cc8e9a0c67e567b1e105bcd7b83; xq_a_token.sig=4cyFK1aS8ENAxvrmGTsKdHkrL-Q; xq_r_token=1c1047ac2978bd94cf2478725768a245462cbd94; xq_r_token.sig=JrBqv7sMoK6ShGjH5__632QDnnI; xq_is_login=1; xq_is_login.sig=J3LxgPVPUzbBg3Kee_PquUfih7Q; u=4330033544; u.sig=ktwo2NB-Bxq8mt6AkOWJ_Bp0TwY; s=gi11srw52t; bid=9b15a2aa90149bcac4ad19830e783e9d_jgt7cl8d; __utmb=1.4.10.1525513480; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1525513562; Hm_lpvt_39dcd5bd05965dcfa70b1d2457c6dcae=1525513563";

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
        properties.put("Cookie", STR_COOKIE_XUEQIU);
        return httpRequester.sendGet(urlString, params, properties);
    }
}
