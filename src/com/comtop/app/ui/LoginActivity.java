package com.comtop.app.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.comtop.app.MyApplication;
import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.entity.User;
import com.comtop.app.https.HttpClient;
import com.comtop.app.https.NetWorkHelper;
import com.comtop.app.ui.base.BaseActivity;
import com.comtop.app.utils.CommonUtil;
import com.comtop.app.utils.StringUtil;
import com.comtop.app.view.ClearEditText;
import com.comtop.app.view.TextURLView;
import com.umeng.update.UmengUpdateAgent;

/**
 * 用户登陆界面acticity
 * 
 * 2014-04-17
 * 
 * @author by xxx
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView thumbnailheadshot;
	private ClearEditText userIdEditText;
	private ClearEditText passwordEditText;
	private Button loginButton;
	private RelativeLayout mRelativeLayout;

	// 用于在本地存储用户信�?
	private SharedPreferences sharedPreferences;

	// 用户账号
	private final String USER_ID = "userId";

	// 用户密码
	private final String USER_PASSWORD = "password";

	private ProgressDialog mProgressAlertDialog;

	/** 省份区域选择 */
	private Spinner mRegionSpinner;

	private ArrayAdapter<String> mSpinnerAdapter;

	/** 省份区域选择icon */
	private ImageButton mSelectImageButton;

	/** 自定�?*/
	private TextURLView mTextURLView;

	/** 选择的省份区域名�?*/
	private String regoinName;

	/** 用户的区域列�?*/
	private String[] lstRegion;

	/** 设置按钮 */
	private Button settingButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		initControl();
		initUserInf();
		isNetWorkAvailable();

		// 仅在WiFi网络环境下才提示进行更新�?
		//友盟屏蔽  UmengUpdateAgent.setUpdateOnlyWifi(true);
		// 程序启动时，自动检测更新�?
		//友盟屏蔽  UmengUpdateAgent.update(this);
	}

	/**
	 * 初始化控�?
	 */
	private void initControl() {

		sharedPreferences = LoginActivity.this.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.myRelativeLayout);
		mRelativeLayout.setOnClickListener(this);

		thumbnailheadshot = (ImageView) findViewById(R.id.thumbnailheadshot);
		thumbnailheadshot.setFocusable(true);
		thumbnailheadshot.setFocusableInTouchMode(true);

		userIdEditText = (ClearEditText) findViewById(R.id.userid);

		passwordEditText = (ClearEditText) findViewById(R.id.password);

		loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(this);

		mTextURLView = (TextURLView) findViewById(R.id.define_self);
		mTextURLView.setText(R.string.definition_self);
		mTextURLView.setOnClickListener(this);

		// 获取区域数据�?
		lstRegion = getResources().getStringArray(R.array.region);

		mRegionSpinner = (Spinner) findViewById(R.id.myRegionSpinner);
		mRegionSpinner.setOnItemSelectedListener(mSpinnerOnItemsListener);

		mSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_checked_text, lstRegion) {
			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.spinner_item_layout, null);

				TextView mTextView = (TextView) view.findViewById(R.id.spinner_item_textview);
				ImageView mImageView = (ImageView) view.findViewById(R.id.spinner_item_checked_image);
				mTextView.setText(lstRegion[position]);
				if (mRegionSpinner.getSelectedItemPosition() == position) {
					mImageView.setVisibility(View.VISIBLE);
				} else {
					mImageView.setVisibility(View.INVISIBLE);
				}

				return view;
			}

		};

		mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item_layout);

		mRegionSpinner.setAdapter(mSpinnerAdapter);
		mRegionSpinner.setSelection(getUserProvinceAreaOffset(), true);

		mSelectImageButton = (ImageButton) findViewById(R.id.select_icon);
		mSelectImageButton.setOnClickListener(this);

		settingButton = (Button) findViewById(R.id.setting);
		settingButton.setOnClickListener(this);

		if (CommonUtil.getIsFirstStart(LoginActivity.this)) {// 如果是程序第一次启�?
			loadIpToLocal();
			// 首次启动，设置默认的数据来源设置为离�?
			Editor mEditor = sharedPreferences.edit();
			mEditor.putBoolean("IS_ONLINE", false);
			mEditor.commit();
			MyApplication.setIsOnLineData(false);
		}

		regoinName = getUserProvinceArea();

	}

	/**
	 * 区域选择监听�?
	 */
	private Spinner.OnItemSelectedListener mSpinnerOnItemsListener = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
			regoinName = parent.getItemAtPosition(position).toString();
			String provinceCode = Constants.dataAreaName.get(regoinName);
			MyApplication.setCurrDataArea(provinceCode);
			saveUserProvinceArea(provinceCode);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	/**
	 * 初始化登陆界面的账号和密�?
	 */
	private void initUserInf() {
		sharedPreferences = getSharedPreferences(Constants.SHARE_NAME, MODE_PRIVATE);
		if (sharedPreferences.contains(USER_ID)) {
			userIdEditText.setText(sharedPreferences.getString(USER_ID, ""));
			// 将sharedPreference中的密码解密
			// String password = new
			// String(EncryUtil.decrypt(sharedPreferences.getString(USER_PASSWORD,
			// "")));
			String password = new String(sharedPreferences.getString(USER_PASSWORD, ""));
			passwordEditText.setText(password);

		}
	}

	/**
	 * 判断网络是否可以
	 */
	private void isNetWorkAvailable() {
		if (!NetWorkHelper.isNetworkAvailable(LoginActivity.this)) {
			showLongToast("哎哟，连不上网络~~");
		}
	}

	/**
	 * 用户登陆成功后，将账号和密码保存到本�?
	 */
	private void saveUserInf(String userId, String password) {
		// 获取sharedPreference的编辑器
		Editor editor = sharedPreferences.edit();
		editor.putString(USER_ID, userId);
		editor.putString(USER_PASSWORD, password);
		editor.commit();

	}

	/**
	 * 保存用户的区域选择
	 * 
	 * @param provinceAreaCode
	 *            省份区域代码
	 */
	private void saveUserProvinceArea(String provinceAreaCode) {
		// 获取sharedPreference的编辑器
		Editor editor = sharedPreferences.edit();
		editor.putString(Constants.USER_PROVINCE_CODE, provinceAreaCode);
		editor.commit();
	}

	/**
	 * 获取用户的省份区域选择
	 * 
	 * @return 省份区域在选择列表中的位置
	 */
	private int getUserProvinceAreaOffset() {
		String areaName = getUserProvinceArea();
		int offset = 0;
		for (int i = 0; i < lstRegion.length; i++) {
			if (areaName.equals(lstRegion[i])) {
				offset = i;
				break;
			}
		}
		return offset;

	}

	/**
	 * 获取用户上一次选择的省份区域，如果没有，则返回默认�?南方电网公司"
	 * 
	 * @return 省份区域名称
	 */
	private String getUserProvinceArea() {
		sharedPreferences = getSharedPreferences(Constants.SHARE_NAME, MODE_PRIVATE);
		String areaCode = sharedPreferences.getString(Constants.USER_PROVINCE_CODE, "00");
		return Constants.dataAreaCode.get(areaCode);

	}

	/**
	 * 用户有无填写用户名密�?
	 * 
	 * @param userId
	 *            用户�?
	 * @param password
	 *            密码
	 */
	private boolean checkUser(String userId, String password) {
		if (StringUtil.isEmpty(userId)) {
			showShortToast("请输入账�?);
			return false;
		} else if (StringUtil.isEmpty(password)) {
			showShortToast("请输入密�?);
			return false;
		}
		return true;
	}

	/**
	 * 用户登陆函数
	 * 
	 * @param userId
	 *            用户账号
	 * @param password
	 *            用户密码
	 */
	private void userLogin(final String userId, final String password) {
		if (StringUtil.isEmpty(userId)) {
			showShortToast("请输入账�?);
		} else if (StringUtil.isEmpty(password)) {
			showShortToast("请输入密�?);
		} else {

			if ("guest".equals(userId) && "hello".equals(password)) {
				//showLongToast("登陆成功�?);

				User currUser = new User();
				currUser.setUserId(userId);
				currUser.setPassword(password);

				MyApplication.setCurrUser(currUser);

				// 将登陆成功的用户信息保存在本�?
				saveUserInf(userId, password);
				openActivity(MainActivity.class);

			} else {
				showLongToast("登陆失败�?);
			}

			// RequestParams params = new RequestParams();
			// params.add("actionType", "holderCertificate");
			// params.add("method", "userLogin");
			// params.add("userId", userId);
			// params.add("password", password);
			//
			// HttpClient.get("", params, new AsyncHttpResponseHandler() {
			// // 开始发起请�?
			// @Override
			// public void onStart() {
			// showAlertDialog("温馨提示", "正在登录请稍等一下~");
			// }
			//
			// // 请求成功
			// @Override
			// public void onSuccess(String response) {
			// mAlertDialog.dismiss();
			// if ("1".equals(response)) {
			// showLongToast("登陆成功�?);
			//
			// User currUser = new User();
			// currUser.setUserId(userId);
			// currUser.setPassword(password);
			//
			// MyApplication.setCurrUser(currUser);
			//
			// // 将登陆成功的用户信息保存在本�?
			// saveUserInf(userId, password);
			// openActivity(MainActivity2.class);
			//
			// } else {
			// showLongToast("登陆失败�?);
			// }
			//
			// }
			//
			// // 请求失败
			// @Override
			// public void onFailure(Throwable e, String data) {
			// mAlertDialog.dismiss();
			// showLongToast("登陆失败�?);
			// }
			//
			// });
		}
	}

	// 分发响应屏幕点击事件
	@Override
	public void onClick(View objView) {
		switch (objView.getId()) {
		// 点击'登陆'按钮
		case R.id.login:
			String provinceCode = Constants.dataAreaName.get(regoinName);
			String currentIP = sharedPreferences.getString(provinceCode + "_IP", "");
			// 为网络访问客户端设置IP地址
			HttpClient.setIP(currentIP);
			// 文件夹目�?
			String fileDirPathPC = CommonUtil.getSdCardPath() + Constants.rootFilePath;

			if (sharedPreferences.getBoolean("IS_ONLINE", false)) {// 用户选择的是在线数据，则直接登录
				MyApplication.setIsOnLineData(true);
				userLogin(userIdEditText.getText().toString(), passwordEditText.getText().toString());
				// userLogin(userIdEditText.getText().toString(),
				// EncryUtil.encrypt(passwordEditText.getText().toString()));
			} else if (CommonUtil.getIsFirstStart(LoginActivity.this)) {// 第一次启动，加载离线�?
				if (checkUser(userIdEditText.getText().toString(), passwordEditText.getText().toString())) {
					new myTask().execute(provinceCode);
				}

			}// 如果用户的SD卡中有更新版本的数据包时，则自动加载最新的数据包到数据库中
			else if (CommonUtil.isLastDataVersion(
					sharedPreferences.getString(Constants.currentVersion + provinceCode, ""), fileDirPathPC)) {
				if (checkUser(userIdEditText.getText().toString(), passwordEditText.getText().toString())) {
					new myTask().execute(provinceCode);
				}
			} else {
				// userLogin(userIdEditText.getText().toString(),
				// EncryUtil.encrypt(passwordEditText.getText().toString()));
				userLogin(userIdEditText.getText().toString(), passwordEditText.getText().toString());
			}

			break;

		case R.id.select_icon:
			mRegionSpinner.performClick();
			break;

		case R.id.define_self:// IP自定�?
			Bundle bundle = new Bundle();
			bundle.putString("areaCode", Constants.dataAreaName.get(regoinName));
			openActivity(SelfDefineIpActivity.class, bundle);
			break;

		case R.id.setting:
			openActivity(SettingActivity.class);
			break;

		// 点击的了空白区域
		default:
			hideKeyboard(mRelativeLayout);

		}
	}

	/**
	 * 用来处理初始化操�?
	 */
	public class myTask extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			mProgressAlertDialog = ProgressDialog.show(LoginActivity.this, "", "拼命加载数据�?..");
		}

		@Override
		protected Boolean doInBackground(String... arg) {
			String provinceCode = arg[0];
			boolean loadOperatFlag = loadOfflineData();
			boolean importOperatFlag = CommonUtil.importData(LoginActivity.this, provinceCode);
			if (importOperatFlag && loadOperatFlag) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressAlertDialog.dismiss();
			Editor mEditor = sharedPreferences.edit();
			if (result) {
				mEditor.putBoolean("IS_FIRST", false);
				showShortToast("数据加载成功�?);
			} else {
				mEditor.putBoolean("IS_FIRST", true);
				showShortToast("数据加载失败�?);
			}
			mEditor.commit();
			// userLogin(userIdEditText.getText().toString(),EncryUtil.encrypt(passwordEditText.getText().toString()));
			userLogin(userIdEditText.getText().toString(), passwordEditText.getText().toString());

		}

	}

	/**
	 * 将程序包中的原有的离线资源文件删除，然后从安装APK中拷贝出�?
	 * 
	 * @return 操作成功，返回true,否则返回false
	 */
	private Boolean loadOfflineData() {
		String currentDataCode = MyApplication.getCurrDataArea();
		// 文件夹目�?
		final String fileDirPath = CommonUtil.getSdCardPath() + Constants.rootFilePath;

		// 文件名称
		final String fileName = currentDataCode + Constants.certificateOfflineDataName;
		// 文件路径
		final String filePath = fileDirPath + fileName;

		// 加入此文件已存在，则无需再拷贝出�?
		if (CommonUtil.isFileExists(filePath)) {
			return true;
		}

		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = LoginActivity.this.getResources().getAssets().open(fileName);
			CommonUtil.createFileDir(fileDirPath);
			out = new FileOutputStream(new File(filePath));
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			in.close();
			out.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 将各个区域的基建系统的IP地址保存在本�?
	 */
	private void loadIpToLocal() {
		Editor mEditor = sharedPreferences.edit();
		mEditor.putString("00_IP", Constants.ZB_IP);
		mEditor.putString("0800_IP", Constants.GZ_IP);
		mEditor.putString("0900_IP", Constants.SZ_IP);
		mEditor.putString("04_IP", Constants.GX_IP);
		mEditor.putString("05_IP", Constants.YN_IP);
		mEditor.putString("07_IP", Constants.HN_IP);
		mEditor.putString("02_IP", Constants.TFTP_IP);
		mEditor.putString("01_IP", Constants.CGY_IP);
		mEditor.putString("06_IP", Constants.GUI_IP);
		mEditor.putString("03_IP", Constants.GD_IP);
		mEditor.commit();
	}
}
