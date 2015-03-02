package com.comtop.app.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.comtop.app.R;
import com.comtop.app.view.base.BaseFragment;
import com.google.zxing.CaptureActivity;

public class CertificateQueryFragment extends BaseFragment {

	private Activity mActivity;
	private RelativeLayout mSearchLayout;
	private FragmentInterface mFragmentInterface;
	private ImageView scanImageView;

	public final static String TAG = "CertificateQueryFragment";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mFragmentInterface = (FragmentInterface) mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View mView = inflater.inflate(R.layout.certificate_query_layout, null);

		scanImageView = (ImageView) mView.findViewById(R.id.scanImageView);

		scanImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View mView) {
				Intent mIntent = new Intent(mActivity, CaptureActivity.class);
				mActivity.startActivity(mIntent);
				// 画面向左切换效果
				mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			}
		});

		mSearchLayout = (RelativeLayout) mView.findViewById(R.id.searchLayout);
		mSearchLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View mView) {
				Animation translationAnimation;
				translationAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -mSearchLayout.getTop());

				translationAnimation.setDuration(400);
				translationAnimation.setFillAfter(true);
				translationAnimation.setAnimationListener(mAnimationListener);
				mSearchLayout.startAnimation(translationAnimation);

			}
		});

		return mView;
	}

	private AnimationListener mAnimationListener = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation arg0) {
			mFragmentInterface.addFragment(new CertificateListFragment(), CertificateQueryFragment.TAG,
					CertificateListFragment.TAG);

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}

	};

}
