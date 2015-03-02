package com.comtop.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comtop.app.R;

public class CertificateLastAdapter extends BaseAdapter {
	protected static final String TAG = "MessagesLastAdapter";
	private Context mContext;
	private List<String> lists;

	public CertificateLastAdapter(Context context, List<String> lists) {
		this.mContext = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		String strMessage = lists.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_message_last_item, null);
			holder = new Holder();
			holder.lastTextView = (TextView) convertView.findViewById(R.id.lastSearchTextView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.lastTextView.setText(strMessage);

		return convertView;
	}

	class Holder {
		TextView lastTextView;
	}

}
