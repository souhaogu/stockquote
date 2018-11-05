package com.soustock.stockquote.utils.pinyin4j;


import com.soustock.stockquote.exception.BusinessException;

/**
 * @author Ricky Fung
 */
public interface Converter {

    String[] getPinyin(char ch) throws BusinessException;

    String getPinyin(String chinese) throws BusinessException;
}
