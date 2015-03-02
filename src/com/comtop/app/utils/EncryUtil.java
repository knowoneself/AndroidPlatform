package com.comtop.app.utils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.comtop.app.constant.Constants;

/**
 * 字符串加密解�?
 * 
 * @author by xxx
 * @Date 2014-04-14
 */
public class EncryUtil {

	/**
	 * 对字符串进行加密
	 * 
	 * @param source
	 *            需要加密的字符�?
	 * @return 加密后的字符�?
	 */
	public static String encrypt(String source) {

		RSAPrivateKey priKey = RSAHelper.generateRSAPrivateKey(
				RSAHelper.hexStringToByte(Constants.RSA_MODULUS),
				RSAHelper.hexStringToByte(Constants.RSA_PRIVATE_EXPONENT));
		return RSAHelper.encrypt(priKey, source);
	}

	/**
	 * 
	 * @param source
	 *            需要解密的字符�?
	 * @return 解密后的字符�?
	 */
	public static byte[] decrypt(String source) {

		RSAPublicKey priKey = RSAHelper.generateRSAPublicKey(
				RSAHelper.hexStringToByte(Constants.RSA_MODULUS),
				RSAHelper.hexStringToByte(Constants.RSA_PUBLIC_EXPONENT));

		return RSAHelper.decrypt(priKey, RSAHelper.hexStringToByte(source));
	}

}
