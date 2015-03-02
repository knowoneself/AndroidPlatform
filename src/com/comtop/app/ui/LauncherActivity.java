package com.comtop.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.comtop.app.R;
import com.comtop.app.ui.base.BaseActivity;
import com.umeng.update.UmengUpdateAgent;

public class LauncherActivity extends BaseActivity {

	private Handler mHandler = new Handler() {
		public void handleMessage(Message objMessage) {
			openLoginActivity();
			   UmengUpdateAgent.setUpdateOnlyWifi(false);

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 欢迎界面全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 设置欢迎界面背景图片
		this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.splash_load));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 为了防止消息队列中含有多�?的消息，先移�?
		mHandler.removeMessages(0);
		// 欢迎界面显示2s
		mHandler.sendEmptyMessageDelayed(0, 2000);
	}

	/**
	 * 进入登陆界面
	 */
	private void openLoginActivity() {
		openActivity(LoginActivity.class);
		defaultFinish();

	}

}
