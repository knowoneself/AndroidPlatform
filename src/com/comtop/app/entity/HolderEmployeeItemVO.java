package com.comtop.app.entity;

import java.util.Date;

/**
 * 项目录入的人员持证明细实�?
 * 
 * @author tanlijun
 */
public class HolderEmployeeItemVO {

	/** (HOLDER_EMPLOYEE_ITEM_ID,VARCHAR2(36),N),现场人员明细编号 */
	private String holderEmployeeItemId;

	/** (HOLDER_EMPLOYEE_ID,VARCHAR2(36),N),现场人员编号 */
	private String holderEmployeeId;

	/** (HOLDER_CERTIFICATE_ID,VARCHAR2(36),Y),证书编号 */
	private String holderCertificateId;

	/** (ADMISSION_STATUS,NUMBER(1),Y),在场离场时间状�?*/
	private Integer admissionStatus;

	/** (ADMISSION_TIME,DATE,Y),入场时间 */
	private Date admissionTime;

	/** (DEPARTURE_TIME,DATE,Y),离场时间 */
	private Date departureTime;

	public Integer getAdmissionStatus() {
		return admissionStatus;
	}

	public void setAdmissionStatus(Integer admissionStatus) {
		this.admissionStatus = admissionStatus;
	}

	public Date getAdmissionTime() {
		return admissionTime;
	}

	public void setAdmissionTime(Date admissionTime) {
		this.admissionTime = admissionTime;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public String getHolderCertificateId() {
		return holderCertificateId;
	}

	public void setHolderCertificateId(String holderCertificateId) {
		this.holderCertificateId = holderCertificateId;
	}

	public String getHolderEmployeeId() {
		return holderEmployeeId;
	}

	public void setHolderEmployeeId(String holderEmployeeId) {
		this.holderEmployeeId = holderEmployeeId;
	}

	public String getHolderEmployeeItemId() {
		return holderEmployeeItemId;
	}

	public void setHolderEmployeeItemId(String holderEmployeeItemId) {
		this.holderEmployeeItemId = holderEmployeeItemId;
	}

}
