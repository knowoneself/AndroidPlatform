package com.comtop.app.entity;

/**
 * 用户实体VO
 * 
 * 2014-04-17
 * 
 * @author by xxx
 * 
 */
public class User {
	/** 用户账号 */
	private String userId;
	/** 用户密码 */
	private String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
