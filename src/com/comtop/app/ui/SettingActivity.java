package com.comtop.app.ui;

import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comtop.app.MyApplication;
import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.https.HttpClient;
import com.comtop.app.ui.base.BaseActivity;
import com.comtop.app.utils.CommonUtil;
import com.comtop.app.utils.DateUtil;
import com.comtop.app.view.CheckSwitchButton;
import com.comtop.app.view.SelectPopupWindow;
import com.comtop.app.view.SettingView;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 用户设置界面acticity
 * 
 * 2014-05-08
 * 
 * @author 王见�?
 * 
 */
public class SettingActivity extends BaseActivity {

	/** 导入离线�?*/
	private SettingView mImportDataSettingView;

	/** 软件说明 */
	private SettingView mAboutSettingView;

	/** 在线更新数据 */
	private SettingView mUpdateDataSettingView;

	/** 安全退�?*/
	private Button exitBootstrapButton;

	/** 返回按钮 */
	private ImageButton backImageButton;

	/** 数据来源切换 */
	private CheckSwitchButton mCheckSwithcButton;
	/** 安全退�?*/
	SelectPopupWindow popupWindow;

	/** 用户信息 */
	private SharedPreferences sharedPreferences;

	/** 进度显示. */
	private ProgressDialog mProgressAlertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		initControl();
		setSettingResource();
	}

	/**
	 * 初始化控�?
	 */
	private void initControl() {
		sharedPreferences = this.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
		mImportDataSettingView = (SettingView) findViewById(R.id.mImportSettingView);
		mImportDataSettingView.setLayoutOnClickListener(mImportDataOnClickListener);
		mAboutSettingView = (SettingView) findViewById(R.id.mAboutSettingView);
		mAboutSettingView.setLayoutOnClickListener(mAboutDataOnClickListener);
		exitBootstrapButton = (Button) findViewById(R.id.exitButton);
		exitBootstrapButton.setOnClickListener(exitButtonOnClickListener);
		backImageButton = (ImageButton) findViewById(R.id.returnImageButton);
		backImageButton.setOnClickListener(mbackImageButtonOnClickListener);

		mUpdateDataSettingView = (SettingView) findViewById(R.id.mUpdateDataSettingView);
		mUpdateDataSettingView.setLayoutOnClickListener(mUpdateDataOnClickListener);

		boolean isOnLineData = sharedPreferences.getBoolean("IS_ONLINE", false);
		mCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.mySwitchButton);
		mCheckSwithcButton.setChecked(isOnLineData);
		mCheckSwithcButton.setOnCheckedChangeListener(mOnCheckedChangeListener);

	}

	/**
	 * 设置相关文字图片资源
	 */
	private void setSettingResource() {
		mImportDataSettingView.setImageViewLeft(R.drawable.set_about_icon);
		mImportDataSettingView.setTextView(R.string.importData);
		mImportDataSettingView.setCommonVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);

		mUpdateDataSettingView.setImageViewLeft(R.drawable.update_data);
		mUpdateDataSettingView.setTextView("在线更新数据" + this.getStrOnTime());
		mUpdateDataSettingView.setCommonVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);

		mAboutSettingView.setImageViewLeft(R.drawable.set_temp_icon);
		mAboutSettingView.setTextView(R.string.button_about);
		mAboutSettingView.setCommonVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
	}

	private OnClickListener mImportDataOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(SettingActivity.this,
			// "mImportDataOnClickListener",
			// Toast.LENGTH_LONG).show();
			openActivity(ImportActivity.class);
		}
	};

	private OnClickListener mAboutDataOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			openActivity(UseSkillActivity.class);
		}
	};

	private OnClickListener mbackImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			finish();
		}
	};

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			Editor mEditor = sharedPreferences.edit();
			if (isChecked) {
				mEditor.putBoolean("IS_ONLINE", true);
				showShortToast("切换至在线数据！");
			} else {
				mEditor.putBoolean("IS_ONLINE", false);
				showShortToast("切换至离线数据！");
			}
			mEditor.commit();
			MyApplication.setIsOnLineData(isChecked);
		}
	};

	private OnClickListener mUpdateDataOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			RequestParams params = new RequestParams();
			params.add("actionType", "holderCertificate");
			params.add("method", "exportData");
			String[] strContentTypes = new String[] { "application/x-zip-compressed" };
			HttpClient.get("", params, new BinaryHttpResponseHandler(strContentTypes) {
				@Override
				public void onStart() {
					super.onStart();
					mProgressAlertDialog = ProgressDialog.show(SettingActivity.this, "", "数据更新�?..");
					// Toast.makeText(SettingActivity.this,"onStart",Toast.LENGTH_LONG).show();;

				}

				@Override
				public void onSuccess(byte[] arg0) {
					super.onSuccess(arg0);
					String strArea = getUserProvinceAreaCode();
					String strTime = DateUtil.getMinute();
					String strFile = strArea + "_PC_" + strTime + ".data";

					String strPathPC = CommonUtil.getSdCardPath() + Constants.rootFilePath;
					// 删除当前区局旧的数据�?
					CommonUtil.deletePreFileName(strArea + "_PC_", strPathPC);
					try {
						FileOutputStream oStream = new FileOutputStream(strPathPC + strFile);
						oStream.write(arg0);
						oStream.flush();
						oStream.close();

						// 导入数据
						CommonUtil.importData(SettingActivity.this, strArea);
						// 更新时间
						String strUpdate = "(" + strTime.substring(0, 4) + "-" + strTime.substring(4, 6) + "-"
								+ strTime.substring(6, 8) + " " + strTime.substring(8, 10) + ":"
								+ strTime.substring(10, 12) + ")";
						Editor mEditor = sharedPreferences.edit();
						mEditor.putString(Constants.onlineUpdateTime, strUpdate);
						mEditor.commit();
						mUpdateDataSettingView.setTextView("在线更新数据" + strUpdate);
						mProgressAlertDialog.dismiss();
						Toast.makeText(SettingActivity.this, "恭喜~数据已更�?, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						mProgressAlertDialog.dismiss();
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(Throwable e, String data) {
					mProgressAlertDialog.dismiss();
					Toast.makeText(SettingActivity.this, "更新失败~请检查网�?, Toast.LENGTH_LONG).show();
				}

			});

		}
	};

	private OnClickListener exitButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View mView) {
			// startActivity(new
			// Intent(SettingActivity.this,LoginActivity.class));
			popupWindow = new SelectPopupWindow(SettingActivity.this, itemsOnClick);
			// 显示窗口
			popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
		}

		// 为弹出窗口实现监听类
		private OnClickListener itemsOnClick = new OnClickListener() {
			public void onClick(View v) {
				popupWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_pop_exit:
					startActivity(new Intent(SettingActivity.this, LoginActivity.class));
					break;
				default:
					break;
				}

			}
		};
	};

	/**
	 * 获取用户上一次选择的省份区域，如果没有
	 * 
	 * @return
	 */
	private String getUserProvinceAreaCode() {
		// sharedPreferences = getSharedPreferences(Constants.SHARE_NAME,
		// MODE_PRIVATE);
		String areaCode = sharedPreferences.getString(Constants.USER_PROVINCE_CODE, "00");
		return areaCode;

	}

	/**
	 * 获取用户上一次选择的省份区域，如果没有
	 * 
	 * @return
	 */
	private String getStrOnTime() {
		// sharedPreferences = getSharedPreferences(Constants.SHARE_NAME,
		// MODE_PRIVATE);
		String strTime = sharedPreferences.getString(Constants.onlineUpdateTime, "");
		return strTime;

	}

}
