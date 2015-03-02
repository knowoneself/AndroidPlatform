package com.comtop.app.https;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 使用独立于UI主线程之外的android-async-http库支持包完成网络的post和get请求。它内部是采用Android Handler
 * message 机制来传递消息的
 * 
 * 2014-04-15
 * 
 * @author by xxx
 * 
 */
public class HttpClient {

	/** IP地址 */
	private static String IP;
	/** 请求的根路径 */
	private static String BASE_URL;

	/** 连接超时时长 */
	private static final int TIMEOUT = 15000;

	/** 实例化一个对象AsyncHttpClient对象 */
	private static AsyncHttpClient client = new AsyncHttpClient();

	/** 设置请求超时时长 */
	static {
		client.setTimeout(TIMEOUT);
	}

	public static void setIP(String IP) {
		HttpClient.IP = IP;
		BASE_URL = "http://" + HttpClient.IP + "/web/lcam/mobile/mobileServlet";
	}

	/**
	 * 根据请求的相对路径拼装请求权路径
	 * 
	 * @param relativeUrl
	 *            相对请求路径
	 * @return 请求的全路径
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	/**
	 * 根据url相对路径(不带参数)请求资源
	 * 
	 * @param relativeUrl
	 *            相对路径
	 * @param responseHandler
	 *            AsyncHttpResponseHandler
	 */
	public static void get(String relativeUrl, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	/**
	 * 根据url相对路径(带参数)请求资源
	 * 
	 * @param relativeUrl
	 *            相对路径
	 * @param params
	 *            参数
	 * @param responseHandler
	 *            AsyncHttpResponseHandler
	 */
	public static void get(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}

	/**
	 * 根据url相对路径（不带参数）下载，返回byte类型数据
	 * 
	 * @param relativeUrl
	 *            相对路径
	 * @param ResponseHandler
	 *            BinaryHttpResponseHandler
	 */
	public static void get(String relativeUrl, BinaryHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	/**
	 * 根据url相对路径（带参数）下载 ，返回byte类数据
	 * 
	 * @param relativeUrl
	 *            相对路径
	 * @param params
	 *            参数
	 * @param responseHandler
	 *            BinaryHttpResponseHandler
	 */
	public static void get(String relativeUrl, RequestParams params, BinaryHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}
}
