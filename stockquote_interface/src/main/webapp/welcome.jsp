<%@ page language="java" import="java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/base.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>souhaogu_interface</title>
    </head>
    <body>
        <h1>welcome to souhaogu.cn</h1>
        <a href="${root}/stockBasic/getStockBasicsOfLikeStr.do?likeStr=fx">包含"fx"的股票信息列表</a><br>
        <a href="${root}/dayQuote/queryQuoteData.do?stockCode=SH600000&recentlyCount=30">浦发银行最近30天的历史行情</a><br>
        <a href="${root}/realtimeQuote/queryQuoteData.do?stockCode=SH600000&recentlyCount=30">浦发银行最近30分钟的实时行情</a>
    </body>
</html>