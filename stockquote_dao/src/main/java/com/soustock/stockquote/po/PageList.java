package com.soustock.stockquote.po;

import java.util.List;

/**
 * Created by xuyufei on 2016/03/24.
 * 分页列表
 */
public class PageList<T> {

    /**
     * 总行数
     */
    private long totalRows;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 当前页的数据
     */
    private List<T> list;

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
