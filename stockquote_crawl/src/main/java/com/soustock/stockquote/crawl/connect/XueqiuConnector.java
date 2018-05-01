package com.soustock.stockquote.crawl.connect;

import com.soustock.stockquote.crawl.connect.HttpRequester;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyufei on 2015/9/19.
 */
public class XueqiuConnector {

    public final static String STR_COOKIE_XUEQIU = "device_id=5d4a242940ca204f0d69d155dce60440; s=fv186drwf8; bid=9b15a2aa90149bcac4ad19830e783e9d_jcga7fgp; remember=1; remember.sig=K4F3faYzmVuqC0iXIERCQf55g2Y; xq_a_token=bf195d08bfc2ff375eaa5017cb8e953b26b93b4d; xq_a_token.sig=n_fYffwlVSHJKPZU-Z5Oe24XSSI; xq_r_token=577999701c47bd1550f1bebc1d539eee54080ad4; xq_r_token.sig=pM6SLRoQBE7h09s4PazRFVHQDaU; xq_is_login=1; xq_is_login.sig=J3LxgPVPUzbBg3Kee_PquUfih7Q; u=4330033544; u.sig=ktwo2NB-Bxq8mt6AkOWJ_Bp0TwY; aliyungf_tc=AQAAAHrv7l0XUw0ARJnii0Dqu44yuKEr; Hm_lvt_1db88642e346389874251b5a1eded6e3=1522594343,1524317260; __utmc=1; __utmz=1.1524317260.4.3.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; Hm_lvt_39dcd5bd05965dcfa70b1d2457c6dcae=1522594343,1524317260; snbim_minify=true; __utma=1.1243880463.1484395216.1524317260.1524370441.5; __utmt=1; __utmb=1.2.10.1524370441; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1524370449; Hm_lpvt_39dcd5bd05965dcfa70b1d2457c6dcae=1524370450";

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
