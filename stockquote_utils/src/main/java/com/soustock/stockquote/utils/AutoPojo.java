package com.soustock.stockquote.utils;

/**
 * Created by xuyufei on 2015/06/12.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 将HttpRequest自动转换成pojo类
 */
public class AutoPojo {

    private final static Log logger = LogFactory.getLog(AutoPojo.class);

    /**
     * 绑定参数到POJO
     *
     * @param <T>
     * @param request
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T bindRequestParam(HttpServletRequest request, Class<T> clazz) {
        try {
            T bean = clazz.newInstance();
            // request attribute
            logger.debug("request paramters :");
            for (Field field : Mirror.getAllFields(clazz)) {

                String fieldName = field.getName();
                logger.debug("-----fieldName:"+fieldName);
                Object value = null;
                if(field.getType().isArray()){//如果是数组参数
                    value=request.getParameterValues(fieldName);
                }else if((field.getType())==List.class){//如果是数组参数
                    value=request.getParameterValues(fieldName);
                }else{
                    value= request.getParameter(fieldName);
                }
                if ( value != null ) {
                    try {
                        value = Mirror.parseObj(field.getType(), value);
                        Method method=clazz.getMethod("set"+ StringUtity.firstUpperCase(fieldName),field.getType());
                        method.invoke(bean,value);

                        logger.debug(fieldName + ":" + value + ",");
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("属性设置失败,Name:" + fieldName + "Type:"
                                + field.getType() + "value:" + value, e);
                    }
                }
            }

            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
            logger.error("绑定参数到POJO失败", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error("绑定参数到POJO失败", e);
        }
        logger.debug("绑定参数到POJO失败！");
        return null;
    }
}