package com.comtop.app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comtop.app.R;
import com.comtop.app.view.base.BaseFragment;

public class NoDataFragment extends BaseFragment {
	public NoDataFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.no_data_fragment, null);
		return view;
	}


}

