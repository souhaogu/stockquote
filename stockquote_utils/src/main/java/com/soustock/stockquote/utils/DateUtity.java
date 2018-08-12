package com.soustock.stockquote.utils;

import com.soustock.stockquote.exception.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xuyufei on 2016/03/06.
 * 时间工具类
 */
public class DateUtity {

    public final static long MS_OF_SECOND = 1000;

    public final static long MS_OF_MINUTE = MS_OF_SECOND * 60;

    public final static long MS_OF_HOUR = MS_OF_MINUTE * 60;

    public final static long MS_OF_DAY = MS_OF_HOUR * 24;

    private final static Log logger = LogFactory.getLog(DateUtity.class);

    public static Date parseXueqiuFormatToDate(String xueqiuDtStr) throws BusinessException {
        try {
            DateFormat xueqiuFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US );
            return xueqiuFormat.parse(xueqiuDtStr);
        }
        catch (Exception ex){
            logger.error("解析失败，当前字符串为:"+xueqiuDtStr, ex);
            throw new BusinessException("解析失败，当前字符串为:"+xueqiuDtStr, ex);
        }

    }

    public static String dateToDateStr(Date dt){
        DateFormat stdFormat = new SimpleDateFormat("yyyyMMdd");
        return stdFormat.format(dt);
    }

    public static Date parseDateStrToDate(String dtStr) throws BusinessException {
        try {
            DateFormat stdFormat = new SimpleDateFormat("yyyyMMdd");
            return stdFormat.parse(dtStr);
        }
        catch (ParseException ex){
            logger.error("解析失败，当前字符串为:"+dtStr, ex);
            throw new BusinessException("解析失败，当前字符串为:"+dtStr, ex);
        }
    }

    public static int getYear(Date dt){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return calendar.get(Calendar.YEAR);
    }

    public static int getJidu(Date dt){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return calendar.get(Calendar.MONTH) /3 + 1;
    }

    /**
     * 得到某个日期是星期几，从0开始分别是星期天、星期一、星期二、...、星期六
     * @param dt
     * @return
     */
    public static int getDayOfWeek(Date dt){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Date StrToDateTime(String dtStr){
        if (NullCheckUtity.stringIsNull(dtStr)) return null ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dtStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date StrToDate(String dtStr){
        if (NullCheckUtity.stringIsNull(dtStr)) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dtStr);
        } catch (ParseException e) {
            return null;
        }
    }

}
