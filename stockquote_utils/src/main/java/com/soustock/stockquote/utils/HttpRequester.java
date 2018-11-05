package com.soustock.stockquote.utils;


import javafx.util.converter.CharacterStringConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * HTTP请求对象
 *
 * @author xuyufei
 */
public class HttpRequester {

    /**
     * 发送GET请求
     *
     * @param urlString
     *            URL地址
     * @return 响应的字符串
     * @throws IOException
     */
    public String sendGet(String urlString) throws IOException {
        return this.send(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @return 响应对象
     * @throws IOException
     */
    public String sendGet(String urlString, Map<String, String> params)
            throws IOException {
        return this.send(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @param propertys
     *            请求属性
     * @return 响应对象
     * @throws IOException
     */
    public String sendGet(String urlString, Map<String, String> params,
                               Map<String, String> propertys) throws IOException {
        return this.send(urlString, "GET", params, propertys);
    }

    /**
     * 发送POST请求
     *
     * @param urlString
     *            URL地址
     * @return 响应对象
     * @throws IOException
     */
    public String sendPost(String urlString) throws IOException {
        return this.send(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @return 响应对象
     * @throws IOException
     */
    public String sendPost(String urlString, Map<String, String> params)
            throws IOException {
        return this.send(urlString, "POST", params, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @param propertys
     *            请求属性
     * @return 响应对象
     * @throws IOException
     */
    public String sendPost(String urlString, Map<String, String> params,
                                Map<String, String> propertys) throws IOException {
        return this.send(urlString, "POST", params, propertys);
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    private String send(String urlString, String method, Map<String, String> parameters, Map<String, String> propertys)
            throws IOException    {
        HttpURLConnection urlConnection = null;

        if (method.equalsIgnoreCase("GET") && parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
            urlString += param;
        }
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        if (propertys != null)
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }

        if (method.equalsIgnoreCase("POST") && parameters != null) {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet()) {
                param.append("&");
                param.append(key).append("=").append(parameters.get(key));
            }
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 得到响应对象
     *
     * @param urlConnection
     * @return 响应对象
     * @throws IOException
     */
    private String makeContent(String urlString,
                                    HttpURLConnection urlConnection) throws IOException {
        try {
            String encoding = "UTF-8";
//            urlConnection.getContentEncoding();
//            if (encoding == null)
//                encoding = this.defaultContentEncoding;
            InputStream in = urlConnection.getInputStream();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int bytesCount ;
            byte[] bytes = new byte[1024];
            while ((bytesCount=in.read(bytes, 0, bytes.length))>0){
                out.write(bytes, 0, bytesCount);
            }
            return new String(out.toByteArray(), encoding);
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    public static void main(String[] args) throws IOException {
        String url = String.format("http://vip.stock.finance.sina.com.cn/quotes_service/view/vML_DataList.php?asc=j&symbol=%s&num=%d", "sh600000", 34);
        HttpRequester httpRequester = new HttpRequester();
        String responseStr = httpRequester.sendGet(url);
        System.out.println(responseStr);
    }
}
