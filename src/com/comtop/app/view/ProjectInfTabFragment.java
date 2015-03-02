package com.comtop.app.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comtop.app.R;
import com.comtop.app.constant.ProjectTypeMapping;
import com.comtop.app.entity.HolderEmployeeProjectVO;
import com.comtop.app.entity.HolderEmployeeProjectView;
import com.comtop.app.view.base.BaseFragment;

/**
 * 项目详情列表TabFragmnet
 * 
 * 2014-04-25
 * 
 * @author by xxx
 * 
 */

@SuppressLint("ValidFragment")
public class ProjectInfTabFragment extends BaseFragment {

	private Activity mActivity;

	public LayoutInflater mInflater;

	private MyAdapter adapter;

	private HolderEmployeeProjectView objHolderEmployeeProjectView;

	private ListView projectListView;

	public ProjectInfTabFragment(Activity context, HolderEmployeeProjectView objHolderEmployeeProjectView) {
		this.mActivity = context;
		this.objHolderEmployeeProjectView = objHolderEmployeeProjectView;
	}

	/**
	 * 为人员信息详情fragment设置布局文件
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mInflater = inflater;
		View view = inflater.inflate(R.layout.project_tab_navigation, container, false);
		projectListView = (ListView) view.findViewById(R.id.projectListView);
		adapter = new MyAdapter();
		adapter.appendToList(this.objHolderEmployeeProjectView.getItems());
		projectListView.setAdapter(adapter);
		return view;

	}

	class MyAdapter extends BaseAdapter {
		List<HolderEmployeeProjectVO> mList = new ArrayList<HolderEmployeeProjectVO>();

		public MyAdapter() {
		}

		public void appendToList(List<HolderEmployeeProjectVO> lists) {
			if (lists == null) {
				return;
			}
			mList.addAll(lists);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			HolderEmployeeProjectVO item = mList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.project_item_layout, null);
				holder.projectNameTextView = (TextView) convertView.findViewById(R.id.projectNameContent);
				holder.projectTypeTextView = (TextView) convertView.findViewById(R.id.projectTypeContent);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			ProjectTypeMapping projectTypeMapping = new ProjectTypeMapping();
			holder.projectNameTextView.setText(item.getProjectName());
			holder.projectTypeTextView.setText(projectTypeMapping.getProjectType(item.getProjectType()));

			return convertView;
		}
	}

	static class ViewHolder {
		/** 项目名称 */
		public TextView projectNameTextView;
		/** 项目类型 */
		public TextView projectTypeTextView;
	}

}
