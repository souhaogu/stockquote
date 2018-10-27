package com.soustock.stockquote.exception;

/**
 * Created by xuyufei on 2016/03/06.
 * 异常类，本系统的基本异常
 */
public class BusinessException extends Exception {

    public BusinessException(Throwable t){
        super(t);
    }

    public BusinessException(String msg){
        super(msg);
    }

    public BusinessException(String msg, Throwable t){
        super(msg, t);
    }
}
