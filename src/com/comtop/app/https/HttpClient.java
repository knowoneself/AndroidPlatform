package com.comtop.app.https;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * ʹ�ö�����UI���߳�֮���android-async-http��֧�ְ���������post��get�������ڲ��ǲ���Android Handler
 * message ������������Ϣ��
 * 
 * 2014-04-15
 * 
 * @author by xxx
 * 
 */
public class HttpClient {

	/** IP��ַ */
	private static String IP;
	/** ����ĸ�·�� */
	private static String BASE_URL;

	/** ���ӳ�ʱʱ�� */
	private static final int TIMEOUT = 15000;

	/** ʵ����һ������AsyncHttpClient���� */
	private static AsyncHttpClient client = new AsyncHttpClient();

	/** ��������ʱʱ�� */
	static {
		client.setTimeout(TIMEOUT);
	}

	public static void setIP(String IP) {
		HttpClient.IP = IP;
		BASE_URL = "http://" + HttpClient.IP + "/web/lcam/mobile/mobileServlet";
	}

	/**
	 * ������������·��ƴװ����Ȩ·��
	 * 
	 * @param relativeUrl
	 *            �������·��
	 * @return �����ȫ·��
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	/**
	 * ����url���·��(��������)������Դ
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param responseHandler
	 *            AsyncHttpResponseHandler
	 */
	public static void get(String relativeUrl, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	/**
	 * ����url���·��(������)������Դ
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param params
	 *            ����
	 * @param responseHandler
	 *            AsyncHttpResponseHandler
	 */
	public static void get(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}

	/**
	 * ����url���·�����������������أ�����byte��������
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param ResponseHandler
	 *            BinaryHttpResponseHandler
	 */
	public static void get(String relativeUrl, BinaryHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	/**
	 * ����url���·���������������� ������byte������
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param params
	 *            ����
	 * @param responseHandler
	 *            BinaryHttpResponseHandler
	 */
	public static void get(String relativeUrl, RequestParams params, BinaryHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}
}
