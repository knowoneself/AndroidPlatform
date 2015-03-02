package com.comtop.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.comtop.app.constant.Constants;
import com.comtop.app.db.HolderEmployeeItem;
import com.comtop.app.db.HolderEmployeeProject;
import com.comtop.app.db.PersonalCertificate;
import com.comtop.app.db.PersonalCertificateInf;

public class CommonUtil {
	private static final String strTag = "ImportActivity";
	private static int successCount = 0;// 导入成功的计�?
	private static int failCount = 0;// 导入失败的计�?

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * Checks if there is enough Space on SDCard
	 * 
	 * @param updateSize
	 *            Size to Check
	 * @return True if the Update will fit on SDCard, false if not enough space
	 *         on SDCard Will also return false, if the SDCard is not mounted as
	 *         read/write
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard());
	}

	/**
	 * get the space is left over on sdcard
	 */
	public static long getRealSizeOnSdcard() {
		File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Checks if there is enough Space on phone self
	 * 
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * get the space is left over on phone self
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 根据手机分辨率从dp转成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�?px(像素) 的单�?转成�?dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f) - 15;
	}

	/**
	 * 判断程序是否是第一次启动。如果是第一次启动，返回true
	 * 
	 * @param mContext
	 *            COntext
	 * @return
	 */
	public static boolean getIsFirstStart(Context mContext) {
		SharedPreferences mShare = mContext.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);

		return mShare.getBoolean("IS_FIRST", true);
	}

	/**
	 * 判断一个文件是否存�?
	 * 
	 * @param filePath
	 *            文件的路�?
	 * @return 创建成功，返回true,否则返回false
	 */
	public static boolean isFileExists(String filePath) {
		try {
			File file = new File(filePath);
			return file.exists();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 删除SD卡中的某个文�?
	 * 
	 * @param filePath
	 *            文件的路�?
	 * @return 删除成功，返回true,否则返回false
	 */
	public static boolean deleteSdCardFile(String filePath) {
		try {
			File file = new File(filePath);
			return file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 创建文件�?
	 * 
	 * @return
	 */
	public static void createFileDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 获取SD卡文件的路径
	 * 
	 * @return SD卡文件的路径
	 */
	public static String getSdCardPath() {
		String path;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getPath();
		} else {
			path = Environment.getDataDirectory().getAbsolutePath();
		}
		return path;
	}

	/**
	 * 获取某个目录下所有的后缀�?data的文件名称（不包括目录名称）
	 * 
	 * @param strPath
	 *            目录路径
	 * @return 文件名list
	 */
	public static List<String> getDirFileList(String strDirPath) {
		List<String> lstFileName = new ArrayList<String>();

		File path = new File(strDirPath);
		if (path != null) {
			File[] files = path.listFiles();
			if (files != null) {// 先判断目录是否为空，否则会报空指�?
				for (File file : files) {
					if (!file.isDirectory()) {
						String fileName = file.getName();
						if (fileName.endsWith(".data")) {
							lstFileName.add(fileName);
						}
					}
				}
			}
		}

		return lstFileName;
	}

	/**
	 * 判断一个名字为fileName的离线资源文件和SD卡中的相比，是否是最新的资源�?
	 * 
	 * @param fileName
	 *            文件名称，eq: 0900_PC_201405151430.data
	 * 
	 * @param strPath
	 *            相对比的SD卡的文件目录路径
	 * @return SD卡中的最新的：true , SD卡中不是最新的：false
	 */

	public static boolean isLastDataVersion(String fileName, String strPath) {
		List<String> lstFileName = getDirFileList(strPath);

		if (StringUtil.isEmpty(fileName)) {
			return true;
		}

		String preFileName = fileName.substring(0, fileName.lastIndexOf("_"));

		// SD卡中此文件的名称
		String sdFileName = "";

		// preFileName 与SD卡中的文件名进行对比,找到SD卡中此文件最新的名称
		for (String strFileName : lstFileName) {
			if (strFileName.indexOf(preFileName) != -1) {
				if (strFileName.compareTo(fileName) >= 0) {
					sdFileName = strFileName;
				}
			}
		}
		// SD卡中不存在以 fileName 命名的文件，则以fileName命名的文件是最新的
		if (StringUtil.isEmpty(sdFileName)) {
			return true;
		} else {// SD卡中存在以fileName命名的文件，则需要和SD卡中的此文件对比一下，以此得出哪个是最新的
			if (fileName.compareTo(sdFileName) >= 0) {// 以fileName命名的文件是最新的
				return false;
			} else {// SD卡中的是最新的
				return true;
			}
		}

	}

	/**
	 * 根据数据区域获取SD卡中某路径下最新的离线数据包名�?
	 * 
	 * @param dataArea
	 *            数据区域
	 * @param strPath
	 *            目录路径
	 * @return 离线包名称，例如�?2_PC_201405151430.data
	 */
	public static String getLastVersionFileName(String dataArea, String strPath) {
		List<String> lstFileName = getDirFileList(strPath);
		String preFileName = dataArea + "_PC_";

		// SD卡中此文件的名称
		String sdFileName = "";
		// preFileName 与SD卡中的文件名进行对比,找到SD卡中此文件最新的名称
		for (String strFileName : lstFileName) {
			if (strFileName.startsWith(preFileName)) {
				if (strFileName.compareTo(sdFileName) >= 0) {
					sdFileName = strFileName;
				}
			}
		}

		return sdFileName;
	}

	/**
	 * 删除filePath目录下，包含前缀preFileName的所有的文件
	 * 
	 * @param preFileName
	 *            前缀�?
	 * @param dirPath
	 *            文件目录路径
	 * @return 删除成功:true 删除失败：false
	 */
	public static void deletePreFileName(String preFileName, String dirPath) {
		List<String> lstFileName = getDirFileList(dirPath);
		for (String strFileName : lstFileName) {
			if (strFileName.indexOf(preFileName) != -1) {
				deleteSdCardFile(dirPath + strFileName);
			}
		}

	}

	/**
	 * 导入数据主入�?
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static boolean importData(Context context, String provinceAreaCode) {
		try {

			String fileDirPathPC = CommonUtil.getSdCardPath() + Constants.rootFilePath;
			String fileNamePC = CommonUtil.getLastVersionFileName(provinceAreaCode, fileDirPathPC);

			// 用于在本地存储用户信�?
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARE_NAME,
					Context.MODE_PRIVATE);
			// 将用户当前使用的离线包名称保存起�?
			Editor editor = sharedPreferences.edit();
			editor.putString(Constants.currentVersion + provinceAreaCode, fileNamePC);
			editor.commit();

			String path = fileDirPathPC + fileNamePC;
			// 调用数据库插入数�?
			DBHelper objDBHelper = DBHelper.getInstance(context, provinceAreaCode);
			objDBHelper.deleteAllHolderEmployeeProject();

			// 执行导入
			doReadFileAndImport(objDBHelper, path);

			Log.d(strTag + "结束----endend�?, String.valueOf(System.currentTimeMillis()));

		} catch (Exception e) {
			Log.e(strTag + "importContact 导入出错", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path
	 * @return
	 */
	private static ArrayList<String> doReadFileAndImport(DBHelper objDBHelper, String path) {
		ZipFile objZipFile = null;
		ZipInputStream objZipInput = null; // 定义压缩输入�?
		BufferedReader objBuReader = null; // 定义行读取流
		ZipEntry objEntry = null; // 压缩实体
		String strImportflag = ""; // 数据库表标识
		String strLineContent = ""; // 读取一行内�?
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			objZipFile = new ZipFile(path);
			objZipInput = new ZipInputStream(new FileInputStream(path));
			while ((objEntry = objZipInput.getNextEntry()) != null) {
				objBuReader = new BufferedReader(new InputStreamReader(objZipFile.getInputStream(objEntry), "GBK"));
				// 按行读取文件 进行操作
				while ((strLineContent = objBuReader.readLine()) != null) {
					if (strLineContent.startsWith("addHolderData")) {
						// 遇到addHolderData标志换表
						addHolderData(objDBHelper, arrayList, strImportflag);
						strImportflag = strLineContent;
						arrayList = new ArrayList<String>();
					} else {
						failCount++;
						arrayList.add(strLineContent);
					}

					// 读到1000条数据时，执行一次导入操�?
					if (arrayList.size() > 999) {
						addHolderData(objDBHelper, arrayList, strImportflag);
						arrayList = new ArrayList<String>();
					}
				}

				objBuReader.close();
			}

			// 剩余的不�?000条数据，执行导入操作
			addHolderData(objDBHelper, arrayList, strImportflag);
		} catch (Exception e) {
			Log.e(strTag + " doReadFileAndImport 关闭文件连接出错", e.getMessage());
		} finally {
			try {
				if (null != objBuReader) {
					objBuReader.close();
				}
				if (null != objZipInput) {
					objZipInput.close();
				}
				if (null != objZipFile) {
					objZipFile.close();
				}
			} catch (IOException e) {
				Log.e(strTag + "doReadFileAndImport 关闭文件连接出错", e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 调用数据库插入数据总入�?
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 * @return
	 */
	private static void addHolderData(DBHelper objDBHelper, ArrayList<String> arrayList, String strImportflag) {
		// addHolderData for HolderEmployeeProject 项目信息
		// addHolderData for HolderEmployeeItem 项目对应证书信息
		// addHolderData for PersonalCertificate 人员信息
		// addHolderData for PersonalCertificateInf 人员持证信息
		if (strImportflag.endsWith("HolderEmployeeProject")) {
			addEmployeeProjectData(objDBHelper, arrayList);
		} else if (strImportflag.endsWith("HolderEmployeeItem")) {
			addEmployeeItemData(objDBHelper, arrayList);
		} else if (strImportflag.endsWith("PersonalCertificate")) {
			addCertificateData(objDBHelper, arrayList);
		} else if (strImportflag.endsWith("PersonalCertificateInf")) {
			addCertificateInfData(objDBHelper, arrayList);
		}

	}

	/**
	 * 导入项目信息
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private static void addEmployeeProjectData(DBHelper objDBHelper, ArrayList<String> arrayList) {

		if (arrayList.size() < 1) {
			return;
		}
		// 组装list
		ArrayList<HolderEmployeeProject> contactsList = new ArrayList<HolderEmployeeProject>();
		HolderEmployeeProject objHolderEmployeeProject = null;
		JSONArray objJSONArray = null;
		JSONObject ojbJson = null;
		try {
			for (int i = 0, j = arrayList.size(); i < j; i++) {
				// json转换成Vo
				objJSONArray = new JSONArray("[" + arrayList.get(i).toString() + "]");
				ojbJson = objJSONArray.getJSONObject(0);
				if (null != ojbJson) {
					/*
					 * ojbJson.getString("projectName");
					 * ojbJson.getString("projectId");
					 * ojbJson.getString("holderEmployeeId");
					 * ojbJson.getInt("projectType");
					 */
					objHolderEmployeeProject = new HolderEmployeeProject(ojbJson.getString("holderEmployeeId"),
							ojbJson.getString("projectId"), ojbJson.getString("projectName"),
							ojbJson.getInt("projectType"));
				}
				contactsList.add(objHolderEmployeeProject);
				// 打印调试信息
				// Log.d(strTag, strsList.get(i).toString());
			}
			// 调用数据库插入数�?
			objDBHelper.insertHolderEmployeeProject(contactsList);
			successCount = successCount + contactsList.size();

		} catch (JSONException e) {
			Log.e(strTag + " addEmployeeProjectData 转换json数据出错", e.getMessage());
		}

		Log.d(strTag + "successCount", String.valueOf(successCount));
		// Log.d(strTag+"组装完vo�?, String.valueOf(System.currentTimeMillis()));
		Log.d(strTag + "readFromFile", String.valueOf(System.currentTimeMillis()));
		// return contactsList;
	}

	/**
	 * 导入项目对应证书信息
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private static void addEmployeeItemData(DBHelper objDBHelper, ArrayList<String> arrayList) {

		if (arrayList.size() < 1) {
			return;
		}
		// 组装list
		ArrayList<HolderEmployeeItem> lstItems = new ArrayList<HolderEmployeeItem>();
		HolderEmployeeItem objHolderEmployeeItem = null;
		JSONArray objJSONArray = null;
		JSONObject ojbJson = null;
		try {
			for (int i = 0, j = arrayList.size(); i < j; i++) {
				// json数据转换成Vo
				objJSONArray = new JSONArray("[" + arrayList.get(i).toString() + "]");
				ojbJson = objJSONArray.getJSONObject(0);
				if (null != ojbJson) {
					/*
					 * ojbJson.getString("holderEmployeeItemId");
					 * ojbJson.getString("holderEmployeeId");
					 * ojbJson.getString("holderCertificateId");
					 * ojbJson.getInt("admissionStatus");
					 */
					Date ojbAdmissionTime = null;
					Date ojbdepartureTime = null;
					String strTemp = ojbJson.getString("admissionTime");
					if (!StringUtil.isEmpty(strTemp)) {
						ojbAdmissionTime = new Date(ojbJson.getLong("admissionTime"));
					}
					strTemp = ojbJson.getString("departureTime");
					if (!StringUtil.isEmpty(strTemp)) {
						ojbdepartureTime = new Date(ojbJson.getLong("departureTime"));
					}
					objHolderEmployeeItem = new HolderEmployeeItem(ojbJson.getString("holderEmployeeItemId"),
							ojbJson.getString("holderEmployeeId"), ojbJson.getString("holderCertificateId"),
							ojbJson.getInt("admissionStatus"), ojbAdmissionTime, ojbdepartureTime);
				}
				lstItems.add(objHolderEmployeeItem);
				// 打印调试信息
				// Log.d(strTag, strsList.get(i).toString());
			}
			// 调用数据库插入数�?
			objDBHelper.insertHolderEmployeeItem(lstItems);
			successCount = successCount + lstItems.size();

		} catch (JSONException e) {
			Log.e(strTag + " addEmployeeItemData 转换json数据出错", e.getMessage());
		}
	}

	/**
	 * 导入人员证书信息
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private static void addCertificateData(DBHelper objDBHelper, ArrayList<String> arrayList) {
		if (arrayList.size() < 1) {
			return;
		}
		// 组装list
		ArrayList<PersonalCertificate> lstItems = new ArrayList<PersonalCertificate>();
		PersonalCertificate objPersonalCertificate = null;
		JSONArray objJSONArray = null;
		JSONObject ojbJson = null;
		try {
			for (int i = 0, j = arrayList.size(); i < j; i++) {
				// json数据转换成Vo
				objJSONArray = new JSONArray("[" + arrayList.get(i).toString() + "]");
				ojbJson = objJSONArray.getJSONObject(0);
				if (null != ojbJson) {
					/*
					 * ojbJson.getString("holderCertificateDetailId");
					 * ojbJson.getString("holderCertificateId");
					 * ojbJson.getString("certificateCode");
					 * ojbJson.getString("jobTypeName");
					 * ojbJson.getString("workTypeName");
					 * ojbJson.getLong("certificateDate");
					 * ojbJson.getLong("certificateVaildDate");
					 * ojbJson.getString("certifyingAuthorityName");
					 */
					Date ojbCerDate = null;
					Date ojbVaildDate = null;
					String strTemp = ojbJson.getString("certificateDate");
					if (!StringUtil.isEmpty(strTemp)) {
						ojbCerDate = new Date(ojbJson.getLong("certificateDate"));
					}
					strTemp = ojbJson.getString("certificateVaildDate");
					if (!StringUtil.isEmpty(strTemp)) {
						ojbVaildDate = new Date(ojbJson.getLong("certificateVaildDate"));
					}
					objPersonalCertificate = new PersonalCertificate(ojbJson.getString("holderCertificateDetailId"),
							ojbJson.getString("holderCertificateId"), ojbJson.getString("certificateCode"),
							ojbJson.getString("jobTypeName"), ojbJson.getString("workTypeName"), ojbCerDate,
							ojbVaildDate, ojbJson.getString("certifyingAuthorityName"));
				}
				lstItems.add(objPersonalCertificate);
				// 打印调试信息
				// Log.d(strTag, strsList.get(i).toString());
			}
			// 调用数据库插入数�?
			objDBHelper.insertPersonalCertificate(lstItems);
			successCount = successCount + lstItems.size();
			Log.d(strTag, "addCertificateData  is end ");
		} catch (JSONException e) {
			Log.e(strTag + " addCertificateData 转换json数据出错", e.getMessage());
		}

	}

	/**
	 * 导入人员信息
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private static void addCertificateInfData(DBHelper objDBHelper, ArrayList<String> arrayList) {
		if (arrayList.size() < 1) {
			return;
		}
		// 组装list
		ArrayList<PersonalCertificateInf> lstItems = new ArrayList<PersonalCertificateInf>();
		PersonalCertificateInf objPersonalCertificateInf = null;
		JSONArray objJSONArray = null;
		JSONObject ojbJson = null;
		try {
			for (int i = 0, j = arrayList.size(); i < j; i++) {
				// json数据转换成Vo
				objJSONArray = new JSONArray("[" + arrayList.get(i).toString() + "]");
				ojbJson = objJSONArray.getJSONObject(0);
				if (null != ojbJson) {
					/*
					 * ojbJson.getString("holderCertificateId");
					 * ojbJson.getString("idCardNo");
					 * ojbJson.getString("userName");
					 * ojbJson.getString("contractorName");
					 * ojbJson.getInt("sex"); ojbJson.getInt("blacked");
					 */
					ojbJson.getString("blackedRemark");
					Date ojbBlackedDate = null;
					String strTemp = ojbJson.getString("blackedDate");
					if (!StringUtil.isEmpty(strTemp)) {
						ojbBlackedDate = new Date(ojbJson.getLong("blackedDate"));
					}

					objPersonalCertificateInf = new PersonalCertificateInf(ojbJson.getString("holderCertificateId"),
							ojbJson.getString("idCardNo"), ojbJson.getString("userName"),
							ojbJson.getString("contractorName"), ojbJson.getInt("sex"), ojbJson.getInt("blacked"),
							ojbBlackedDate, ojbJson.getString("blackedRemark"));
				}
				lstItems.add(objPersonalCertificateInf);
				// 打印调试信息
				// Log.d(strTag, strsList.get(i).toString());
			}
			// 调用数据库插入数�?
			objDBHelper.insertPersonalCertificateInf(lstItems);
			successCount = successCount + lstItems.size();

		} catch (JSONException e) {
			Log.e(strTag + " addCertificateInfData 转换json数据出错", e.getMessage());
		}

	}

}
