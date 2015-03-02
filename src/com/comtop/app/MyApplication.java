package com.comtop.app;

import android.app.Application;

import com.comtop.app.entity.User;

/**
 * MyApplication是Android程序的真正入口， 每个Android程序和Application之间使一一对应的，并且是单例形式存在的
 * 
 * 2014-04-15
 * 
 * @author by xxx
 * 
 */
public class MyApplication extends Application {

	private static MyApplication mInstance;

	/** 当前登陆用户 */
	private static User currUser;

	/** 读取的是离线包数�?OR 在线数据标识�?true读取的是在线数据，false读取的是离线包数�?*/
	private static boolean isOnLineData = false;

	/** 当前的数据区�?00总部 0900深圳.... */
	private static String currDataArea;

	public static MyApplication getInstance() {
		return mInstance;
	}

	/**
	 * Android程序的真正入�?
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// 禁止友盟默认的Activity统计方式
		//友盟屏蔽   MobclickAgent.openActivityDurationTrack(false);

	}

	/**
	 * 程序导致内存不足时，调用的方�?
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	public static User getCurrUser() {
		return MyApplication.currUser;
	}

	public static void setCurrUser(User currUser) {
		MyApplication.currUser = currUser;
	}

	public static boolean getIsOnLineData() {
		return MyApplication.isOnLineData;
	}

	public static void setIsOnLineData(boolean isOnLineData) {
		MyApplication.isOnLineData = isOnLineData;
	}

	public static String getCurrDataArea() {
		return MyApplication.currDataArea;
	}

	public static void setCurrDataArea(String currDataArea) {
		MyApplication.currDataArea = currDataArea;
	}

}
