package com.comtop.app.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.comtop.app.R;
import com.comtop.app.utils.LogUtil;

public class BaseFragmentActivity extends FragmentActivity {

	private static final String TAG = "BaseFragmentActivity";

	protected AlertDialog mAlertDialog;

	/** 异步操作处理 */
	protected AsyncTask mRunningTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//友盟屏蔽   MobclickAgent.onPageEnd(this.getClass().getName());
		//友盟屏蔽   MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//友盟屏蔽   MobclickAgent.onPageStart(this.getClass().getName());
		//友盟屏蔽   MobclickAgent.onResume(this);
	}

	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void defaultFinish() {
		super.finish();
	}

	/**
	 * 回收 mRunningTask、mAlertDialog资源
	 */
	@Override
	public void onDestroy() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onDestroy() invoked!!");
		super.onDestroy();

		if (mRunningTask != null && mRunningTask.isCancelled() == false) {
			mRunningTask.cancel(false);
			mRunningTask = null;
		}
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}

	/******************************** 【Activity LifeCycle For Debug�?*******************************************/

	/******************************** 【Activity 控制显示Toast提示框�?*******************************************/

	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	/******************************** 【Activity 控制显示Toast提示框�?*******************************************/

	/**
	 * 判断Intent中是有包含pExtraKey的键的键值对存在
	 * 
	 * @param pExtraKey
	 *            �?	 * @return 包含true，否�?false
	 */
	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}

	/**
	 * 启动一个activity
	 * 
	 * @param cls
	 *            待启动的activity
	 */
	protected void openActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		// 画面向左切换效果
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 启动一个activity
	 * 
	 * @param cls
	 *            待启动的activity
	 * @param pBundle
	 *            需要绑定的数据Bundle
	 */
	protected void openActivity(Class<?> cls, Bundle... pBundle) {
		Intent intent = new Intent(this, cls);
		if (pBundle != null) {
			for (int i = 0; i < pBundle.length; i++) {
				intent.putExtras(pBundle[i]);
			}
		}
		startActivity(intent);
		// 画面向左切换效果
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 隐藏输入键盘
	 * 
	 * @param view
	 *            待隐藏键盘的view
	 */
	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}

