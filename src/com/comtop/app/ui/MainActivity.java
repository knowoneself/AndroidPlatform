package com.comtop.app.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comtop.app.R;
import com.comtop.app.entity.ActionItem;
import com.comtop.app.ui.base.BaseFragmentActivity;
import com.comtop.app.view.CertificateListFragment;
import com.comtop.app.view.FragmentInterface;
import com.comtop.app.view.TitlePopup;
import com.comtop.app.view.TitlePopup.OnItemOnClickListener;
import com.umeng.fb.FeedbackAgent;

public class MainActivity extends BaseFragmentActivity implements OnClickListener, FragmentInterface,
		OnItemOnClickListener {

	// private DrawerLayout mDrawerLayout;
	// private RadioButton certificateRadioButton;
	// private LinearLayout aboveImageView;
	// private TextView aboveTitleTextView;

	// private CustomButton cbFeedback;
	// private CustomButton cbSetting;

	private FragmentManager mFragmentManager;
	//暂时去掉popus只添加设置文�?
	private ImageButton moreImageButton;
	private TitlePopup titlePopup;

	// private TextView loginUserTextView;
	
	//临时添加设置文字按钮  
	private Button aboveSettingTemp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		iniView();
		iniClass();

		CertificateListFragment mFragment = new CertificateListFragment();
		// 清空Fragment的Back�?
		mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		this.replaceFragment(mFragment, CertificateListFragment.TAG);
	}

	private void iniView() {
		// certificateRadioButton = (RadioButton)
		// findViewById(R.id.certificateRadioButton);
		// certificateRadioButton.setOnClickListener(this);

		// mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

		// aboveImageView = (LinearLayout)
		// findViewById(R.id.Linear_above_toHome);
		// aboveImageView.setOnClickListener(this);
		// aboveTitleTextView = (TextView) findViewById(R.id.above_title);

		// cbFeedback = (CustomButton) findViewById(R.id.cbFeedback);
		// cbFeedback.setOnClickListener(this);

		// cbSetting = (CustomButton) findViewById(R.id.cbsetting);
		// cbSetting.setOnClickListener(this);

		// loginUserTextView = (TextView) findViewById(R.id.loginUser);

		//暂时去掉popus只添加设置文�?
		moreImageButton = (ImageButton) findViewById(R.id.more_imageButton);
		moreImageButton.setOnClickListener(this);
		//aboveSettingTemp = (Button)findViewById(R.id.above_setting_but);
		//aboveSettingTemp.setOnClickListener(this); 
		
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "设置", R.drawable.button_setting));
		titlePopup.addAction(new ActionItem(this, "反馈", R.drawable.button_feedback));
		titlePopup.setItemOnClickListener(this);

	}

	private void iniClass() {
		mFragmentManager = getSupportFragmentManager();
		// loginUserTextView.setText("�?" +
		// MyApplication.getCurrUser().getUserId());
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View mView) {
		hideKeyboard(mView);
		switch (mView.getId()) {
		//暂时去掉popus只添加设置文�?
		case R.id.more_imageButton:
			titlePopup.show(mView);
			break;
		/*case R.id.above_setting_but:  
			openActivity(SettingActivity.class);
		break;*/
		}
	}

	@Override
	public void replaceFragment(Fragment mFragment, String Tag) {
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.replace(R.id.contentFrameLayout, mFragment, Tag);
		mTransaction.commit();

	}

	@Override
	public void addFragment(Fragment mFragment, String oldTag, String newTag) {
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		if (oldTag != null) {
			mTransaction.remove(mFragmentManager.findFragmentByTag(oldTag));
			mTransaction.addToBackStack(null);
		}
		mTransaction.add(R.id.contentFrameLayout, mFragment, newTag);
		mTransaction.commit();
	}

	@Override
	public void backFragment() {
		mFragmentManager.popBackStack();

	}

	/**
	 * 连续按两次返回键就退�?
	 */
	private boolean isWaitingExit = false;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
			if(isWaitingExit){
				isWaitingExit = false;
				finish();
			
			}else{
				showShortToast("再按一次退出！");
				isWaitingExit = true;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						isWaitingExit = false;
					}
				}, 3000);
				return true;
				
			}
			
			
			
			
//			if (mFragmentManager.getBackStackEntryCount() > 0) {
//				backFragment();
//				return true;
//			}
			
			
			

		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			// mDrawerLayout.closeDrawers();
			// } else {
			// mDrawerLayout.openDrawer(Gravity.LEFT);
			// }

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(ActionItem item, int position) {
		// 设置功能
		if (position == 0) {
			openActivity(SettingActivity.class);

		} else if (position == 1) {// 反馈功能
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

}
