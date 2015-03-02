package com.comtop.app.utils;

public class StringUtil {

	/**
	 * �ж�һ��str�Ƿ�Ϊ�ջ�null
	 * 
	 * @param str
	 *            ���жϵ��ַ�
	 * @return Ϊ�ջ�null������false,���򷵻�true
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}

	}
}
