package com.comtop.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.https.HttpClient;
import com.comtop.app.ui.base.BaseActivity;
import com.comtop.app.utils.StringUtil;
import com.comtop.app.view.ClearEditText;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 用户自定义IP Acticity
 * 
 * 2014-05-15
 * 
 * @author 王见�?
 * 
 */
public class SelfDefineIpActivity extends BaseActivity implements OnClickListener {

	private TextView dataAreaTextView;
	/** 返回按钮 */
	private ImageButton backImageButton;
	private ClearEditText ipBootstrapEditText;
	/** 测试 按钮 */
	private Button testBootstrapButton;
	/** 保存 按钮 */
	private Button saveBootstrapButton;
	/** 省份区域编码 */
	private String areaCode;
	private SharedPreferences mShare;

	private ProgressDialog mProgressAlertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_define_activity);
		initControl();
	}

	/**
	 * 初始化控�?
	 */
	private void initControl() {
		Bundle mBundle = getIntent().getExtras();
		/** 省份区域编码 */
		areaCode = mBundle.getString("areaCode");

		mShare = this.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
		String defaultIP = mShare.getString(areaCode + "_IP", "");

		String dataAreaName = Constants.dataAreaCode.get(areaCode);

		dataAreaTextView = (TextView) findViewById(R.id.dataAreaName);
		dataAreaTextView.setText(dataAreaName);
		dataAreaTextView.setFocusable(true);
		dataAreaTextView.setFocusableInTouchMode(true);
		ipBootstrapEditText = (ClearEditText) findViewById(R.id.myIP);
		ipBootstrapEditText.setText(defaultIP);
		testBootstrapButton = (Button) findViewById(R.id.test_IP);
		testBootstrapButton.setOnClickListener(this);
		saveBootstrapButton = (Button) findViewById(R.id.save_IP);
		saveBootstrapButton.setOnClickListener(this);
		backImageButton = (ImageButton) findViewById(R.id.returnButton);
		backImageButton.setOnClickListener(this);

	}

	// 分发响应屏幕点击事件
	@Override
	public void onClick(View objView) {
		String strIP = ipBootstrapEditText.getText().toString();
		switch (objView.getId()) {
		case R.id.test_IP:// 测试IP地址的连通�?
			testIP(strIP);
			break;

		case R.id.save_IP:// 将IP地址保存在本�?
			saveIP(strIP);
			break;

		case R.id.returnButton:// 返回按钮
			finish();
			break;

		}
	}

	/**
	 * 测试填写的IP地址是否能连�?
	 */
	private void testIP(String strIP) {
		if (!isIPEmpty(strIP)) {
			RequestParams params = new RequestParams();
			params.add("actionType", "holderCertificate");
			params.add("method", "testIP");
			HttpClient.setIP(strIP);
			HttpClient.get("", params, new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					mProgressAlertDialog = ProgressDialog.show(SelfDefineIpActivity.this, "", "测试网络是否连通中...");
				}

				@Override
				public void onSuccess(String response) {
					mProgressAlertDialog.dismiss();
					showShortToast("网络畅通~~");
				}

				@Override
				public void onFailure(Throwable e, String data) {
					mProgressAlertDialog.dismiss();
					showShortToast("网络不连通~~");

				}
			});

		}
	}

	/**
	 * 将IP地址保存在本�?
	 */
	private void saveIP(String strIP) {
		if (!isIPEmpty(strIP)) {
			Editor mEditor = mShare.edit();
			mEditor.putString(areaCode + "_IP", strIP);
			mEditor.commit();
			showShortToast("保存成功啦~~");
		}
	}

	/**
	 * 判断IP地址是否为空
	 */
	private boolean isIPEmpty(String strIP) {
		if (StringUtil.isEmpty(strIP)) {
			showShortToast("请输入IP地址~~");
			return true;
		}
		return false;

	}

}
