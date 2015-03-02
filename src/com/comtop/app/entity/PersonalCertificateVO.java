package com.comtop.app.entity;

import java.util.Date;

/**
 * 人员信息实体
 * 
 * 2014-04-24
 * 
 * @author 王见�?
 * 
 */
public class PersonalCertificateVO {

	/** (HOLDER_CERTIFICATE_ID,VARCHAR2(36),N),持证情况ID */
	private String holderCertificateId;

	/** (ID_CARD_NO,VARCHAR2(18),Y),身份证号�?*/
	private String idCardNo;

	/** (USER_NAME,VARCHAR2(50),Y),持证人姓�?*/
	private String userName;

	/** (CONTRACTOR_NAME,VARCHAR2(300),Y),持证人单位名称（从承包商表取数据�?*/
	private String contractorName;

	/** (SEX,NUMBER(1,0),Y),性别 */
	private int sex;

	/** (BLACKED,NUMBER(1,0),Y),黑名�?*/
	private int blacked;

	/**
	 * 黑名�?private String isBlacked;
	 */
	/** (BLACKED_DATE,DATE,Y),黑名单有效日�?*/
	private Date blackedDate;

	/** BLACKED_REMARK 黑名单原�?*/
	private String blackedRemark;

	public String getHolderCertificateId() {
		return holderCertificateId;
	}

	public void setHolderCertificateId(String holderCertificateId) {
		this.holderCertificateId = holderCertificateId;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public int getBlacked() {
		return blacked;
	}

	public void setBlacked(int blacked) {
		this.blacked = blacked;
	}

	public Date getBlackedDate() {
		return blackedDate;
	}

	public void setBlackedDate(Date blackedDate) {
		this.blackedDate = blackedDate;
	}

	public String getBlackedRemark() {
		return blackedRemark;
	}

	public void setBlackedRemark(String blackedRemark) {
		this.blackedRemark = blackedRemark;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
