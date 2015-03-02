package com.comtop.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comtop.app.R;

/**
 * 设置组件
 * 
 * @author WJB
 * 
 */
public class SettingView extends RelativeLayout {

	private static final String TAG = "SettingView";
	private Context mContext;

	private RelativeLayout myRelativeLayout;
	private ImageView mLeftImageView;
	private TextView mTextView;
	private ImageView mRightImageView;

	public SettingView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.set_button, this);
		myRelativeLayout = (RelativeLayout) findViewById(R.id.set_layout);
		mLeftImageView = (ImageView) findViewById(R.id.set_icon);
		mRightImageView = (ImageView) findViewById(R.id.set_more_icon);
		mTextView = (TextView) findViewById(R.id.set_text);
		myRelativeLayout = (RelativeLayout) findViewById(R.id.set_layout);

	}

	public void setCommonVisible(int mSetLayoutVisibility, int mLeftVisibility,
			int mCenterVisibility, int mRightVisibility) {
		myRelativeLayout.setVisibility(mSetLayoutVisibility);
		mLeftImageView.setVisibility(mLeftVisibility);
		mTextView.setVisibility(mCenterVisibility);
		mRightImageView.setVisibility(mRightVisibility);

	}

	public void setImageViewLeft(int txtRes) {
		Drawable img = mContext.getResources().getDrawable(txtRes);
		mLeftImageView.setImageDrawable(img);
	}

	public void setTextView(int txtRes) {
		mTextView.setText(txtRes);
	}
	
	public void setTextView(String txtRes) {
		mTextView.setText(txtRes);
	}

	public void setLeftImageViewOnclickListener(OnClickListener listener) {
		mLeftImageView.setOnClickListener(listener);
	}

	public void setRightImageViewOnclickListener(OnClickListener listener) {
		mRightImageView.setOnClickListener(listener);
	}

	public void setLayoutOnClickListener(OnClickListener listenet) {
		myRelativeLayout.setOnClickListener(listenet);
	}

	public ImageView getLeftImageView() {
		return mLeftImageView;
	}

	public ImageView getRightImageView() {
		return mRightImageView;
	}

	public TextView getTextView() {
		return mTextView;
	}

}
