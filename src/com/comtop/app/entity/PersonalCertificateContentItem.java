package com.comtop.app.entity;

import com.comtop.app.entity.base.BaseContentItem;

/**
 * 人员持证信息item实体�?
 */
public class PersonalCertificateContentItem extends BaseContentItem {

	/** 持证的人员Id */
	private String holderCertificateId;

	/** 人员姓名 */
	private String personName;

	/** 所属的承包商名�?*/
	private String contractorName;

	/** 是否黑名�?*/
	private int isBlacked;

	public String getHolderCertificateId() {
		return holderCertificateId;
	}

	public void setHolderCertificateId(String holderCertificateId) {
		this.holderCertificateId = holderCertificateId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public int getIsBlacked() {
		return isBlacked;
	}

	public void setIsBlacked(int isBlacked) {
		this.isBlacked = isBlacked;
	}

}
