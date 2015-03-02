package com.comtop.app.entity;

import java.util.Date;

/**
 * 人员证书信息实体
 * 
 * 2014-04-24
 * 
 * @author 王见�?
 */
public class PersonalCertificateDataVO {

	/** (HOLDER_CERTIFICATE_DETAIL_ID,VARCHAR2(36),N),持证情况明细表ID */
	private String holderCertificateDetailId;

	/** (HOLDER_CERTIFICATE_ID,VARCHAR2(36),N),持证情况ID */
	private String holderCertificateId;

	/** (CERTIFICATE_CODE,VARCHAR2(20),Y),证书编码 */
	private String certificateCode;

	/** 作业类别 */
	private String jobTypeName;

	/** 工种 */
	private String workTypeName;

	/** (CERTIFICATE_DATE,DATE,Y),发证日期 */
	private Date certificateDate;

	/** (CERTIFICATE_VAILD_DATE,DATE,Y),有效日期 */
	private Date certificateVaildDate;

	/** (CERTIFYING_AUTHORITY_NAME,VARCHAR2(128),Y),发证单位名称 */
	private String certifyingAuthorityName;

	public String getCertificateCode() {
		return certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	public Date getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(Date certificateDate) {
		this.certificateDate = certificateDate;
	}

	public Date getCertificateVaildDate() {
		return certificateVaildDate;
	}

	public void setCertificateVaildDate(Date certificateVaildDate) {
		this.certificateVaildDate = certificateVaildDate;
	}

	public String getCertifyingAuthorityName() {
		return certifyingAuthorityName;
	}

	public void setCertifyingAuthorityName(String certifyingAuthorityName) {
		this.certifyingAuthorityName = certifyingAuthorityName;
	}

	public String getHolderCertificateDetailId() {
		return holderCertificateDetailId;
	}

	public void setHolderCertificateDetailId(String holderCertificateDetailId) {
		this.holderCertificateDetailId = holderCertificateDetailId;
	}

	public String getHolderCertificateId() {
		return holderCertificateId;
	}

	public void setHolderCertificateId(String holderCertificateId) {
		this.holderCertificateId = holderCertificateId;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public String getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}

}
