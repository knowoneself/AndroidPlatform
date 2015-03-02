package com.comtop.app.ui.base;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.comtop.app.R;
import com.comtop.app.utils.LogUtil;

/**
 * Activity的基类，所有的Activity都要集成自这个类
 * 
 * 2014-04-14
 * 
 * @author by xxx
 * 
 */
public class BaseActivity extends Activity {

	private static final String TAG = "BaseActivity";

	/** '�?�?�? 提示�?*/
	protected AlertDialog mAlertDialog;

	/** 异步操作处理 */
	protected AsyncTask mRunningTask;

	/******************************** 【Activity LifeCycle For Debug�?*******************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	@Override
	protected void onStart() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStart() invoked!!");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onRestart() invoked!!");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onResume() invoked!!");
		super.onResume();
		//友盟屏蔽   MobclickAgent.onPageStart(this.getClass().getName());
		//友盟屏蔽   MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onPause() invoked!!");
		super.onPause();
		try {
			//友盟屏蔽   MobclickAgent.onPageEnd(this.getClass().getName());
			//友盟屏蔽   MobclickAgent.onPause(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStop() invoked!!");
		super.onStop();
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
	 *            �?
	 * @return 包含true，否�?false
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
	 * 通过反射来设置对话框是否要关闭，在表单校验时很管用， 因为在用户填写出错时点确定时默认Dialog会消失， 所以达不到校验的效�?
	 * 而mShowing字段就是用来控制是否要消失的，而它在Dialog中是私有变量�?所有只有通过反射去解决此问题
	 * 
	 * @param pDialog
	 * @param pIsClose
	 */
	public void setAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field field = pDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(pDialog, pIsClose);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(TitleID).setMessage(Message).show();
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pOkClickListener, null, null);
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			DialogInterface.OnClickListener pOkClickListener, DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage, String pPositiveButtonLabel,
			String pNegativeButtonLabel, DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener, DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected ProgressDialog showProgressDialog(int pTitelResID, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = getResources().getString(pTitelResID);
		return showProgressDialog(title, pMessage, pCancelClickListener);
	}

	protected ProgressDialog showProgressDialog(String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(this, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
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

	/******************************** 【Activity 异常时，使用Toast显示相关信息�?*******************************************/
	/**
	 * 内存不足时，调用此方法，用Toast显示提示信息（在非UI线程中调用）
	 */
	protected void handleOutmemoryError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "内存空间不足�?, Toast.LENGTH_SHORT).show();
				// finish();
			}
		});
	}

	private int network_err_count = 0;

	/**
	 * 网络连接异常时，调用此方法，用Toast显示提示信息（在非UI线程中调用）
	 */
	protected void handleNetworkError() {
		network_err_count++;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (network_err_count < 3) {
					Toast.makeText(BaseActivity.this, "网速好像不怎么给力啊！", Toast.LENGTH_SHORT).show();
				} else if (network_err_count < 5) {
					Toast.makeText(BaseActivity.this, "网速真的不给力�?, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(BaseActivity.this, "唉，今天的网络怎么这么差劲�?, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 程序解析数据格式出错时，调用此方法，用Toast显示提示信息（在非UI线程中调用）
	 */
	protected void handleMalformError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "数据格式错误�?, Toast.LENGTH_SHORT).show();
				// finish();
			}
		});
	}

	/**
	 * 程序意外终止时，调用此方法，用Toast显示提示信息（在非UI线程中调用）
	 */
	protected void handleFatalError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "发生了一点意外，程序终止�?, Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	/******************************** 【Activity 异常时，使用Toast显示相关信息�?*******************************************/
	/**
	 * 为Activity的finish()增加动画效果
	 */
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/**
	 * activity自带的finish()方法
	 */
	public void defaultFinish() {
		super.finish();
	}

}
