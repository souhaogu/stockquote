package com.soustock.stockquote.po;


import java.io.Serializable;

public class PageCdtVo implements Serializable{

	/**
	 * 翻到第几页, 从1开始
	 */
	private Integer pageNum;

	/**
	 * 每页多少行
	 */
	private Integer pageSize;

	public Integer getPageNum() {
		return pageNum==null?1:pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize==null?20:pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getRowNum(){
		return (pageNum - 1) * pageSize;
	}

}
