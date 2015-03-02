package com.comtop.app.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.comtop.app.MyApplication;
import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.db.HolderEmployeeItem;
import com.comtop.app.db.HolderEmployeeProject;
import com.comtop.app.db.PersonalCertificate;
import com.comtop.app.db.PersonalCertificateInf;
import com.comtop.app.ui.base.BaseActivity;
import com.comtop.app.utils.CommonUtil;
import com.comtop.app.utils.DBHelper;
import com.comtop.app.utils.StringUtil;

/**
 * 用户导入离线数据acticity
 * 
 * 2014-05-4
 * 
 * @author tanlijun
 *  
 */
public class ImportActivity extends BaseActivity implements OnClickListener {

	private Button importBut;

	private RelativeLayout myImportLayout;

	private static final String strTag = "ImportActivity";

	//private TextView show;// 显示结果的文本框
 
	private Thread objAddThread;// 增加导入线程
	private static final int ADD_FAIL = 0;// 导入失败标识
	private static final int ADD_SUCCESS = 1;// 导入成功标识
	private static int successCount = 0;// 导入成功的计�?
	private static int failCount = 0;// 导入失败的计�?
	// 默认文件路劲，实际情况应作相应修改或从界面输入或浏览选择
	private static String PATH;

	private ImageButton backImageButton;

	private ProgressDialog mProgressAlertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_activity);
		initControl();
	}

	/**
	 * 初始化控�?
	 */
	private void initControl() {

		String fileDirPathPC = CommonUtil.getSdCardPath() + Constants.rootFilePath;
		String fileNamePC = CommonUtil.getLastVersionFileName(MyApplication.getCurrDataArea(), fileDirPathPC);
		PATH = fileDirPathPC + fileNamePC;
		myImportLayout = (RelativeLayout) findViewById(R.id.myImportLayout);
		importBut = (Button) findViewById(R.id.Import);
		//show = (TextView) findViewById(R.id.main_tv);
		importBut.setOnClickListener(this);
		backImageButton = (ImageButton) findViewById(R.id.returnImageButton);
		backImageButton.setOnClickListener(this);

	}

	// 分发响应屏幕点击事件
	@Override
	public void onClick(View objView) {
		switch (objView.getId()) {
		// 点击'导入'按钮
		case R.id.Import:
			addHolder();
			break;

		case R.id.returnImageButton:// 返回按钮
			finish();
			// 点击的了空白区域
		default:
			hideKeyboard(myImportLayout);

		}
	}

	/**
	 * 导入持证信息入口
	 */
	private void addHolder() {
		if (!new File(PATH).isFile()) {
			Toast.makeText(this, "文件不存�?", Toast.LENGTH_SHORT).show();
			//show.setText("文件不存�?");
			return;
		}
		if (objAddThread != null) {
			objAddThread.interrupt();
			objAddThread = null;
		}
		objAddThread = new Thread(new AddRunnable(this, PATH));
		createDialog(this, "警告", "使用旧数据包导入会覆盖现有离线数据，请慎用！");
	}

	/**
	 * 创建提示对话�?
	 * 
	 * @param context
	 * @param title
	 * @param message
	 */
	private void createDialog(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mProgressAlertDialog = ProgressDialog.show(ImportActivity.this, "", "数据导入�?..");
				startAddHolder();
			}
		});

		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/**
	 * 开启导入线�?
	 */
	private void startAddHolder() {
		//show.setText("正在导入持证信息数据...");
		if (objAddThread != null) {
			objAddThread.start();
		}
	}

	class AddRunnable implements Runnable {
		private Context context;
		private String path;

		public AddRunnable(Context context, String path) {
			this.path = path;
			this.context = context;
		}

		@Override
		public void run() {
			boolean result = importData(context, path);
			if (result) {
				objHandler.sendEmptyMessage(ADD_SUCCESS);
			} else {
				objHandler.sendEmptyMessage(ADD_FAIL);
			}
		}
	}

	/**
	 * 处理UI相关的handler
	 */
	private final Handler objHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADD_FAIL:
				//show.setText("导入联系人失�?);
				Toast.makeText(ImportActivity.this, String.format("导入联系人失�?), Toast.LENGTH_SHORT).show();  
				mProgressAlertDialog.dismiss();
				break;
			case ADD_SUCCESS:
				//show.setText();
				Toast.makeText(ImportActivity.this, String.format("导入成功 %d 条，失败 %d �?, successCount, failCount - successCount), Toast.LENGTH_SHORT).show();
				mProgressAlertDialog.dismiss();
				break;
			}
		}
	};


	/**
	 * 导入联系�?
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	private boolean importData(Context context, String path) { 
		successCount = 0;
		failCount = 0;
		try {

			// 调用数据库插入数�?
			DBHelper objDBHelper = DBHelper.getInstance(context, MyApplication.getCurrDataArea());
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
	private ArrayList<String> doReadFileAndImport(DBHelper objDBHelper, String path) {
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
				objBuReader = new BufferedReader(new InputStreamReader(objZipFile.getInputStream(objEntry),"GBK"));
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
	 * strImportflag 参数对应导入表：
	 * addHolderData for HolderEmployeeProject --项目信息
	 * addHolderData for HolderEmployeeItem --项目对应证书信息
	 * addHolderData for PersonalCertificate --人员信息
	 * addHolderData for PersonalCertificateInf --人员持证信息
	 * @param objDBHelper
	 * @param arrayList
	 * @return
	 */
	private void addHolderData(DBHelper objDBHelper, ArrayList<String> arrayList, String strImportflag) {
		
		Log.d(strTag + "successCount", String.valueOf(successCount)+"---------"+ String.valueOf(failCount));
		
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
	 * 导入项目信息 :
	 * 
	 * String holderEmployeeId; String projectId; String projectName; Integer
	 * projectType;
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private void addEmployeeProjectData(DBHelper objDBHelper, ArrayList<String> arrayList) {

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

		
		// Log.d(strTag+"组装完vo�?, String.valueOf(System.currentTimeMillis()));
		Log.d(strTag + "readFromFile", String.valueOf(System.currentTimeMillis()));
		// return contactsList;
	}

	/**
	 * 导入人员信息�?
	 * 
	 * String holderCertificateId; String idCardNo; String userName; String
	 * contractorName; Integer sex; Integer blacked; java.util.Date blackedDate;
	 * String blackedRemark;
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private void addCertificateInfData(DBHelper objDBHelper, ArrayList<String> arrayList) {
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
					objPersonalCertificateInf = new PersonalCertificateInf(ojbJson.getString("holderCertificateId"),
							ojbJson.getString("idCardNo"), ojbJson.getString("userName"),
							ojbJson.getString("contractorName"), ojbJson.getInt("sex"), ojbJson.getInt("blacked"),
							this.convertDate(ojbJson, "blackedDate"), ojbJson.getString("blackedRemark"));
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

	/**
	 * 导入人员证书信息:
	 * 
	 * String holderCertificateDetailId; String holderCertificateId; String
	 * certificateCode; String jobTypeName; String workTypeName; java.util.Date
	 * certificateDate; java.util.Date certificateVaildDate; String
	 * certifyingAuthorityName;
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private void addCertificateData(DBHelper objDBHelper, ArrayList<String> arrayList) {
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
					objPersonalCertificate = new PersonalCertificate(ojbJson.getString("holderCertificateDetailId"),
							ojbJson.getString("holderCertificateId"), ojbJson.getString("certificateCode"),
							ojbJson.getString("jobTypeName"), ojbJson.getString("workTypeName"), this.convertDate(
									ojbJson, "certificateDate"), this.convertDate(ojbJson, "certificateVaildDate"),
							ojbJson.getString("certifyingAuthorityName"));
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
	 * 导入项目对应证书信息: String holderEmployeeItemId; String holderEmployeeId; String
	 * holderCertificateId; Integer admissionStatus; java.util.Date
	 * admissionTime; java.util.Date departureTime;
	 * 
	 * @param objDBHelper
	 * @param arrayList
	 */
	private void addEmployeeItemData(DBHelper objDBHelper, ArrayList<String> arrayList) {

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
					objHolderEmployeeItem = new HolderEmployeeItem(ojbJson.getString("holderEmployeeItemId"),
							ojbJson.getString("holderEmployeeId"), ojbJson.getString("holderCertificateId"),
							ojbJson.getInt("admissionStatus"), this.convertDate(ojbJson, "admissionTime"),
							this.convertDate(ojbJson, "departureTime"));
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
	 * 
	 * @param objJSON
	 * @param flag
	 * @return
	 * @throws JSONException
	 */
	private Date convertDate(JSONObject objJSON, String flag) throws JSONException {
		String strTemp = objJSON.getString(flag);
		if (!StringUtil.isEmpty(strTemp)) {
			return new Date(objJSON.getLong(flag));
		}
		return null;
	}

}
