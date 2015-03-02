package com.comtop.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.comtop.app.R;

public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {

	/** 宸︿晶icon */
	private Drawable leftDrawable;
	/** 鍙充晶icon */
	private Drawable rightDrawable;

	private boolean flag = false;

	public ClearEditText(Context context) {
		super(context);
		initClass();
		initView();
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClass();
		initView();
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initClass();
		initView();
	}

	private void initClass() {
		flag = true;
		Drawable[] myDrawable = getCompoundDrawables();
		leftDrawable = myDrawable[0];
		rightDrawable = myDrawable[2];
	}

	private void initView() {
		if (rightDrawable == null) {// 濡傛�?娌℃湁璁剧疆鍙充晶icon锛岄粯璁ゆ槸鍒犻櫎鍥炬爣
			rightDrawable = getResources().getDrawable(R.drawable.abs__ic_clear_search_api_holo_light);
		}
		rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
		if (leftDrawable != null) {
			leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
		}

		setOnFocusChangeListener(this);
		addTextChangedListener(this);
		setRightDrawableVisible(false);

	}

	/**
	 * 璁剧疆宸︿晶鍥炬爣鐨勫彲瑙佹�?
	 * 
	 * @param isVisible
	 */
	private void setLeftDrawableVisible(boolean isVisible) {
		if (flag) {
			if (isVisible) {
				setCompoundDrawables(leftDrawable, getCompoundDrawables()[1], getCompoundDrawables()[2],
						getCompoundDrawables()[3]);
			} else {
				setCompoundDrawables(null, getCompoundDrawables()[1], getCompoundDrawables()[2],
						getCompoundDrawables()[3]);
			}
		}
	}

	/**
	 * 璁剧疆鍙充晶鍥炬爣鐨勫彲瑙佹�?
	 * 
	 * @param isVisible
	 */
	private void setRightDrawableVisible(boolean isVisible) {
		if (flag) {
			if (isVisible) {
				setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], rightDrawable,
						getCompoundDrawables()[3]);
			} else {
				setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null,
						getCompoundDrawables()[3]);
			}
		}
	}

	@Override
	public void onFocusChange(View view, boolean isFocusChange) {
		if (isFocusChange) {
			if (getText().length() > 0) {
				setLeftDrawableVisible(false);
				setRightDrawableVisible(true);
			} else {
				setLeftDrawableVisible(true);
				setRightDrawableVisible(false);
			}
		}

	}

	/**
	 * 褰撹緭鍏ユ閲岄潰鍐呭鍙戠敓鍙樺寲鐨勬椂鍊欏洖璋冪殑鏂规硶
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (s.length() > 0) {
			setLeftDrawableVisible(false);
			setRightDrawableVisible(true);
		} else {
			setLeftDrawableVisible(true);
			setRightDrawableVisible(false);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	/**
	 * 璁剧疆鏅冨姩鍔ㄧ�?
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 鏅冨姩鍔ㄧ敾
	 * 
	 * @param counts
	 *            1绉掗挓鏅冨姩澶氬皯涓�?	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth() - getPaddingRight() - rightDrawable.getIntrinsicWidth())
						&& (event.getX() < getWidth());
				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

}
