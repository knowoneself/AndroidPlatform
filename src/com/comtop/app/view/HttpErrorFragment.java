package com.comtop.app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comtop.app.R;
import com.comtop.app.view.base.BaseFragment;

public class HttpErrorFragment extends BaseFragment {

	public HttpErrorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.http_error_fragment, null);
		return view;
	}

}

