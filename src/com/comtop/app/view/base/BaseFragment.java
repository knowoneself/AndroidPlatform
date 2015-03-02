package com.comtop.app.view.base;

import android.support.v4.app.Fragment;
import android.util.Log;

public class BaseFragment extends Fragment {

	@Override
	public void onResume() {
		super.onResume();
		//友盟屏蔽  MobclickAgent.onPageStart(this.getClass().getName()); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		//友盟屏蔽   MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("BaseFragment", "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("BaseFragment", "onDestroyView");
	}

}
