package com.comtop.app.entity;

import java.util.List;

import com.comtop.app.entity.base.BaseContentList;

/**
 * 项目信息列表实体
 * 
 * 2014-04-25
 * 
 * @author by xxx
 * 
 */
public class HolderEmployeeProjectView extends BaseContentList {

	private List<HolderEmployeeProjectVO> items;

	public List<HolderEmployeeProjectVO> getItems() {
		return items;
	}

	public void setItems(List<HolderEmployeeProjectVO> items) {
		this.items = items;
	}

}
