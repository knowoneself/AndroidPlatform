package com.comtop.app.entity;

import java.util.List;

import com.comtop.app.entity.base.BaseContentList;

/**
 * 人员持证查询的数据实�?
 * 
 * @author by xxx
 * 
 */
public class PersonalCertificateListEntity extends BaseContentList {

	private List<PersonalCertificateContentItem> items;

	private int allCount;

	private int blackCount;

	public List<PersonalCertificateContentItem> getItems() {
		return items;
	}

	public void setItems(List<PersonalCertificateContentItem> items) {
		this.items = items;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public int getBlackCount() {
		return blackCount;
	}

	public void setBlackCount(int blackCount) {
		this.blackCount = blackCount;
	}

}
