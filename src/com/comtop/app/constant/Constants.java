package com.comtop.app.constant;

import java.util.HashMap;

public class Constants {

	/** RSA 加密密钥 */
	public final static String RSA_MODULUS = "009AD5BED30B11E6FB60769DE68558FC69";
	public final static String RSA_PUBLIC_EXPONENT = "010001";
	public final static String RSA_PRIVATE_EXPONENT = "62B65EE041B07A274AF3BC81985CD301";

	/** 当前人员持证信息的离线包版本(APK中包含的人员持证离线数据包版本日�? */
	public final static String PERSON_CERTIFICATE_VERSION_DATE = "201405201730";

	/** 数据区域对应的数据库名称 */
	public final static HashMap<String, String> dataAreaDbName = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0900", "SZ_DB");
			put("0800", "GZ_DB");
			put("01", "CGY_DB");
			put("02", "TFTP_DB");
			put("03", "GD_DB");
			put("04", "GX_DB");
			put("05", "YN_DB");
			put("06", "GUI_DB");
			put("07", "HN_DB");
			put("00", "ZB_DB");
		}
	};

	/** 根据区域名称获取区域编码 */
	public final static HashMap<String, String> dataAreaName = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("广东电网公司", "03");
			put("广西电网公司", "04");
			put("云南电网公司", "05");
			put("贵州电网公司", "06");
			put("海南电网公司", "07");
			put("深圳供电局有限公司", "0900");
			put("广州供电局有限公司", "0800");
			put("超高压输电公�?, "01");
			put("调峰调频发电公司", "02");
			put("南方电网公司", "00");
		}
	};

	/** 根据区域编码获取区域名称 */
	public final static HashMap<String, String> dataAreaCode = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("03", "广东电网公司");
			put("04", "广西电网公司");
			put("05", "云南电网公司");
			put("06", "贵州电网公司");
			put("07", "海南电网公司");
			put("0900", "深圳供电局有限公司");
			put("0800", "广州供电局有限公司");
			put("01", "超高压输电公�?);
			put("02", "调峰调频发电公司");
			put("00", "南方电网公司");
		}
	};

	/** 根据区域编码获取区域服务器的IP地址 */
	public final static HashMap<String, String> dataAreaIP = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0900", "SZ_DB");
			put("0800", "GZ_DB");
			put("01", "CGY_DB");
			put("02", "TFTP_DB");
			put("03", "GD_DB");
			put("04", "GX_DB");
			put("05", "YN_DB");
			put("06", "GUI_DB");
			put("07", "HN_DB");
			put("00", "ZB_DB");
		}
	};
	/** 广州基建系统IP */
	public final static String GZ_IP = "10.150.140.20";
	/** 深圳基建系统IP */
	public final static String SZ_IP = "10.150.34.230";
	/** 超高压基建系统IP */
	public final static String CGY_IP = "10.118.250.8";
	/** 调峰调频基建系统IP */
	public final static String TFTP_IP = "192.168.2.101";
	/** 广电基建系统IP */
	public final static String GD_IP = "10.150.0.218";
	/** 广西基建系统IP */
	public final static String GX_IP = "10.100.83.2";
	/** 云南基建系统IP */
	public final static String YN_IP = "10.111.5.108";
	/** 贵州基建系统IP */
	public final static String GUI_IP = "10.115.153.1";
	/** 海南基建系统IP */
	public final static String HN_IP = "10.94.2.82";
	/** 南网总部基建系统IP */
	public final static String ZB_IP = "10.91.1.169";

	// 程序中保存临时信息的文件�?	public final static String SHARE_NAME = "myShareName";

	// 文件存储的根路径
	public final static String rootFilePath = "/comtop/data/";

	/** 当前的离线包版本 */
	public final static String currentVersion = "currentVersion_";

	/** 当前的离线包版本 */
	public final static String onlineUpdateTime = "ONLINE_UPDATE_TIME";

	// 用户选定的区域代�?	public final static String USER_PROVINCE_CODE = "provinceAreaCode";

	// 人员持证离线资源包的文件�?APK中包含的人员持证离线数据包名)
	public final static String certificateOfflineDataName = "_PC_" + PERSON_CERTIFICATE_VERSION_DATE + ".data";

	/** 头像的宽�?*/
	public static final int headImageWidth = 280;
	/** 头像的高�?*/
	public static final int headImageHeight = 380;
	/** 头像图片的纵坐标 */
	public static final int headImage_Y = 300;
	/** 背景图片的宽�?*/
	public static final int backGroundWidth = 826;
	/** 背景图片的高�?*/
	public static final int backGroundHeight = 1240;
	/** 承包商名称文字的纵坐�?*/
	public static final int constractorName_Y = 1130;

	/** 承包商名称文字的大小 */
	public static final int constractorName_fontSize = 40;

	/** 姓名文字的纵坐标 */
	public static final int personName_Y = 770;
	/** 姓名文字的大�?*/
	public static final int personName_fontSize = 70;

	/** 二维码的长宽 */
	public static final int qrCode_Height_Width = 270;
	/** 二维码图片的纵坐�?*/
	public static final int qr_Y = 780;

	/** 图片的位�?*/
	public static final String rootImagePath = "/comtop/image/";
	/** 系统图片的位�?*/
	public static final String sysImagePath = rootImagePath + "sysImage/";
	/** 生成图片的位�?*/
	public static final String genImagePath = rootImagePath + "genImage/";
	/** 头像图片的位�?*/
	public static final String headImagePath = rootImagePath + "headImage/";
	
	/** 用户最近查询的10个字符串 */
	public final static String[] QUERY_STR = { "queryForwardStr00", "queryForwardStr01", "queryForwardStr02",
			"queryForwardStr03", "queryForwardStr04", "queryForwardStr05", "queryForwardStr06", "queryForwardStr07",
			"queryForwardStr08", "queryForwardStr09" };

}

