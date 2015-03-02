package com.comtop.app.entity.base;

/**
 * 主列表内容基�? * 
 * 2014-04-21
 * 
 * @author by xxx
 * 
 */
public abstract class BaseContentItem {

	/** 明细Id */
	public int id;

	/** 详情url */
	public String detailUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

}

