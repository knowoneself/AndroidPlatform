package com.comtop.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comtop.app.R;
import com.comtop.app.entity.PersonalCertificateContentItem;

public class CertificateResultAdapter extends BaseAdapter {
	protected static final String TAG = "MessagesLastAdapter";
	private Context mContext;
	private List<PersonalCertificateContentItem> mList;

	public CertificateResultAdapter(Context context, List<PersonalCertificateContentItem> lists) {
		this.mContext = context;
		this.mList = lists;
	}

	@Override
	public int getCount() {
		if (mList != null) {
			return mList.size();
		}
		return 0;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.personal_item_layout, null);
			holder = new ViewHolder();
			holder.personNameTextView = (TextView) convertView.findViewById(R.id.persona_name);
			holder.contractorTextView = (TextView) convertView.findViewById(R.id.contractor_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.personNameTextView.setText(mList.get(position).getPersonName());
		holder.contractorTextView.setText(mList.get(position).getContractorName());
		ImageView blackFlagImageView = (ImageView) convertView.findViewById(R.id.blackFlag);

		if (mList.get(position).getIsBlacked() == 1) {
			blackFlagImageView.setVisibility(View.VISIBLE);
		} else {
			blackFlagImageView.setVisibility(View.GONE);
		}

		
		return convertView;

	}

	static class ViewHolder {
		/** 人员名称 */
		public TextView personNameTextView;
		/** 所属的承包商名�?*/
		public TextView contractorTextView;
	}

}
