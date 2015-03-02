package com.comtop.app.entity.base;

/**
 * 主列表基�?
 * 
 * 2014-04-21
 * 
 * @author by xxx
 * 
 */
public abstract class BaseContentList {

	/** 页码 */
	private int pageNum;

	/** 相对应的加载更多url */
	private String moreFlag;

	/** 查询的字符串 */
	private String queryStr;
	
	public String getMoreFlag() {
		return moreFlag;
	}

	public void setMoreFlag(String moreFlag) {
		this.moreFlag = moreFlag;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}
