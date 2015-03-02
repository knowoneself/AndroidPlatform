package com.comtop.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.comtop.app.R;
import com.comtop.app.ui.base.BaseActivity;

/**
 * 用户使用说明�?
 * 
 * 2014-05-19
 * 
 * @author by xxx
 * 
 */
public class UseSkillActivity extends BaseActivity {

	/** 返回按钮 */
	private ImageButton backImageButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_skill_layout);

		backImageButton = (ImageButton) findViewById(R.id.returnImageButton);
		backImageButton.setOnClickListener(backImageButtonOnClickListener);
	}

	private OnClickListener backImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

}
