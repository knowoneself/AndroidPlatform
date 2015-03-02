package com.comtop.app.entity;

/**
 * 证书与项目关联实�?
 * 
 * @author 王见�?
 * 
 */
public class HolderEmployeeProjectVO {

	/** (ID_CARD_NO,VARCHAR2(18),Y),实体id */
	private String holderEmployeeId;

	/** (USER_NAME,VARCHAR2(50),Y),项目id */
	private String projectId;

	/** (CONTRACTOR_NAME,VARCHAR2(500),Y),项目名称 */
	private String projectName;

	/** (SEX,NUMBER(1,0),Y),项目性质 */
	private Integer projectType;

	public String getHolderEmployeeId() {
		return holderEmployeeId;
	}

	public void setHolderEmployeeId(String holderEmployeeId) {
		this.holderEmployeeId = holderEmployeeId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectType() {
		return projectType;
	}

	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}

}
