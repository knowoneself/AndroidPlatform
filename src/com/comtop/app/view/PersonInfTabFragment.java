package com.comtop.app.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.constant.ProjectTypeMapping;
import com.comtop.app.entity.HolderEmployeeProjectVO;
import com.comtop.app.entity.HolderEmployeeProjectView;
import com.comtop.app.entity.PersonalCertificateDataVO;
import com.comtop.app.entity.PersonalCertificateView;
import com.comtop.app.utils.CommonUtil;
import com.comtop.app.utils.ComposeImageUtils;
import com.comtop.app.utils.DateUtil;
import com.comtop.app.utils.EncryUtil;
import com.comtop.app.view.ProjectInfTabFragment.ViewHolder;
import com.comtop.app.view.base.BaseFragment;

/**
 * 人员持证人员信息Fragment
 * 
 * 2014-04-24
 * 
 * @author by xxx
 * 
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class PersonInfTabFragment extends BaseFragment {
	private Activity mActivity;

	public LayoutInflater mInflater;
	/** 是否黑名单标�?*/
	private TextView isblackflagView;
	/** 人员姓名 */
	private TextView personNameTextView;
	/** 身份证Id */
	private TextView personIdTextView;
	/** 承包商名�?*/
	private TextView contractorNameTextView;
	/** 黑名单原因提示文�?*/
	private TextView blackReasonTextTipView;
	/** 黑名单原�?*/
	private TextView blackReasonTextView;
	/** 解除日期提示文字 */
	private TextView relieveDateTextTipView;
	/** 解除日期 */
	private TextView relieveDateTextView;
	/** 资质证书列表 */
	private ListView certificateListView;
	
	/** 项目信息数据 */
	private HolderEmployeeProjectView objHolderEmployeeProjectView;
	/** 项目信息列表 */
	private ListView projectListView;

	/** 制证操作 */
	private ImageView shortImageView;

	private MyAdapter adapter;
	
	private ProjectAdapter projectAdapter;

	/** listview底部空文档，点位�?*/
	private View listViewEmptyBotton;
	
	/** 数据列表 */
	private PersonalCertificateView objPersonalCertificateView;

    /* 屏蔽生成照片功能 代码保留*/ 
	
	private static final int CHOOSE_FROM_CAMERA = 1;

	private static final int CHOOSE_FROM_FILE = 2;

	private static final int COPY_PICTURE = 3;
	
	private AlertDialog dialog;

	private Uri headImageUri; 

	public PersonInfTabFragment() {

	}

	public PersonInfTabFragment(Activity context, PersonalCertificateView objPersonalCertificateView, 
			HolderEmployeeProjectView objHolderEmployeeProjectView) {
		this.mActivity = context;
		this.objPersonalCertificateView = objPersonalCertificateView;
		this.objHolderEmployeeProjectView = objHolderEmployeeProjectView;

	    /* 屏蔽生成照片功能 代码保留*/
		
		CommonUtil.createFileDir(CommonUtil.getSdCardPath() + Constants.headImagePath);
		headImageUri = Uri.parse("file://" + CommonUtil.getSdCardPath() + Constants.headImagePath
				+ this.objPersonalCertificateView.getPersonalInf().getIdCardNo() + ".jpg"); 

	}

	/**
	 * 为人员信息详情fragment设置布局文件
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.person_tab_navigation, container, false);
		personNameTextView = (TextView) view.findViewById(R.id.personName);
		personNameTextView.setText(this.objPersonalCertificateView.getPersonalInf().getUserName());
		listViewEmptyBotton = inflater.inflate(R.layout.my_empty_bottom, null);

		/* 屏蔽生成照片功能 代码保留*/
	
		shortImageView = (ImageView) view.findViewById(R.id.button_shot);
		shortImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog = new AlertDialog.Builder(mActivity).setItems(new String[] { "拍照", "选择本地图片" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = null;
								if (which == 0) {
									intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action
																							// is
																							// capture
									intent.putExtra(MediaStore.EXTRA_OUTPUT, headImageUri);
									startActivityForResult(intent, CHOOSE_FROM_CAMERA);
								} else {
									intent = new Intent(Intent.ACTION_GET_CONTENT, null);
									intent.setType("image/*");
									intent.putExtra("crop", "true");
									intent.putExtra("aspectX", 2);
									intent.putExtra("aspectY", 3);
									intent.putExtra("outputX", Constants.headImageWidth);
									intent.putExtra("outputY", Constants.headImageHeight);
									intent.putExtra("scale", true);
									intent.putExtra("return-data", false);
									intent.putExtra("scaleUpIfNeeded", true);// 去除黑边
									intent.putExtra(MediaStore.EXTRA_OUTPUT, headImageUri);
									intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
									intent.putExtra("noFaceDetection", false); // no
																				// face
																				// detection
									startActivityForResult(intent, CHOOSE_FROM_FILE);
								}

							}

						}).create();

				if (!dialog.isShowing()) {
					dialog.show();
				}

			}
		});  
		
		
		personIdTextView = (TextView) view.findViewById(R.id.personID);
		personIdTextView.setText("(" + this.objPersonalCertificateView.getPersonalInf().getIdCardNo() + ")");

		contractorNameTextView = (TextView) view.findViewById(R.id.contractorName);
		contractorNameTextView.setText(this.objPersonalCertificateView.getPersonalInf().getContractorName());

		if (this.objPersonalCertificateView.getPersonalInf().getBlacked() == 1) {// 这个人是黑名�?
			
			isblackflagView = (TextView) view.findViewById(R.id.isBlackFlag);
			isblackflagView.setVisibility(View.VISIBLE);
			blackReasonTextTipView = (TextView) view.findViewById(R.id.blackReasonTip);
			blackReasonTextTipView.setVisibility(View.VISIBLE);
			
			blackReasonTextView = (TextView) view.findViewById(R.id.blackReason);
			blackReasonTextView.setVisibility(View.VISIBLE);
			blackReasonTextView.setText(this.objPersonalCertificateView.getPersonalInf().getBlackedRemark());

			relieveDateTextTipView = (TextView) view.findViewById(R.id.relieveDateTip);
			relieveDateTextTipView.setVisibility(View.VISIBLE); 
			relieveDateTextView = (TextView) view.findViewById(R.id.relieveDate);
			relieveDateTextView.setVisibility(View.VISIBLE);
			relieveDateTextView.setText(DateUtil.getFormatDate(this.objPersonalCertificateView.getPersonalInf().getBlackedDate()));

			// blackImage = (BootstrapThumbnail)
			// view.findViewById(R.id.blackImage);
			// blackImage.setVisibility(View.VISIBLE);
		}

		certificateListView = (ListView) view.findViewById(R.id.certificateListView);
		
		/*
		//(1)ListView �?ScrolView 滚动条底部遮挡原�? 固增加空文件
		if(null != this.objPersonalCertificateView.getCertificateDatas() && 
				this.objPersonalCertificateView.getCertificateDatas().size() >1){
			certificateListView.addFooterView(listViewEmptyBotton);
		}*/
		
		
		adapter = new MyAdapter();
		adapter.appendToList(this.objPersonalCertificateView.getCertificateDatas());
		certificateListView.setAdapter(adapter);
		
		//(2)ListView �?ScrolView 滚动条底部遮挡原�? 固增加空文件 或者用�?种方�?
		setListViewHeightBasedOnChildren(certificateListView);     
		
		if(null != objHolderEmployeeProjectView){
		projectListView = (ListView) view.findViewById(R.id.projectListView);
		projectAdapter = new ProjectAdapter();
		projectAdapter.appendToList(this.objHolderEmployeeProjectView.getItems());
		projectListView.setAdapter(projectAdapter);
		}
		
		return view;

	}
	
	/**
	 * scrollview与listview共存时，动态获取并设置ListView高度
	 * 可以研究其他好的处理方式，如不用scrollview，整个界面用ListView,或LinearLayout实现
	 * 
	 * @param listView
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;       
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			 
		if(null != this.objPersonalCertificateView.getCertificateDatas() && 
				this.objPersonalCertificateView.getCertificateDatas().size() >1){
			params.height = params.height + 50;
		}else{
			params.height = params.height + 30; 
		}
		//((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}
	

	class MyAdapter extends BaseAdapter {
		List<PersonalCertificateDataVO> mList = new ArrayList<PersonalCertificateDataVO>();

		public MyAdapter() {
		}

		public void appendToList(List<PersonalCertificateDataVO> lists) {
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
			PersonalCertificateDataVO item = mList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.certificate_item_layout, null);
				holder.certificateCodeTextView = (TextView) convertView.findViewById(R.id.certificateIdContent);
				holder.jobTypeNameTextView = (TextView) convertView.findViewById(R.id.jobTypeNameContent);
				holder.workTypeNameTextView = (TextView) convertView.findViewById(R.id.workTypeNameContent);
				holder.certificateDateTextView = (TextView) convertView.findViewById(R.id.certificateDateContent);
				holder.certificateVaildDateTextView = (TextView) convertView
						.findViewById(R.id.certificateVaildDateContent);
				holder.certifyingAuthorityNameTextView = (TextView) convertView
						.findViewById(R.id.certifyingAuthorityNameContent);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			holder.certificateCodeTextView.setText(item.getCertificateCode());
			holder.jobTypeNameTextView.setText(item.getJobTypeName());
			holder.workTypeNameTextView.setText(item.getWorkTypeName());
			holder.certificateDateTextView.setText(DateUtil.getFormatDate(item.getCertificateDate()));
			holder.certificateVaildDateTextView.setText(DateUtil.getFormatDate(item.getCertificateVaildDate()));
			holder.certifyingAuthorityNameTextView.setText(item.getCertifyingAuthorityName().toString());

			return convertView;
		}
	}

	static class ViewHolder {
		/** 证书编码 */
		public TextView certificateCodeTextView;
		/** 作业类别 */
		public TextView jobTypeNameTextView;
		/** 工种 */
		public TextView workTypeNameTextView;
		/** 发证日期 */
		public TextView certificateDateTextView;
		/** 有效日期 */
		public TextView certificateVaildDateTextView;
		/** 发证单位 */
		public TextView certifyingAuthorityNameTextView;
	}
	
	
	
	
	
	
	class ProjectAdapter extends BaseAdapter {
		List<HolderEmployeeProjectVO> mList = new ArrayList<HolderEmployeeProjectVO>();

		public ProjectAdapter() {
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
			ViewProjectHolder holder;
			HolderEmployeeProjectVO item = mList.get(position);
			if (convertView == null) {
				holder = new ViewProjectHolder();
				convertView = mInflater.inflate(R.layout.project_item_layout, null);
				holder.projectNameTextView = (TextView) convertView.findViewById(R.id.projectNameContent);
				holder.projectTypeTextView = (TextView) convertView.findViewById(R.id.projectTypeContent);
				convertView.setTag(holder);
			} else {
				holder = (ViewProjectHolder) convertView.getTag();

			}
			ProjectTypeMapping projectTypeMapping = new ProjectTypeMapping();
			holder.projectNameTextView.setText(item.getProjectName());
			holder.projectTypeTextView.setText(projectTypeMapping.getProjectType(item.getProjectType()));

			return convertView;
		}
	}

	static class ViewProjectHolder {
		/** 项目名称 */
		public TextView projectNameTextView;
		/** 项目类型 */
		public TextView projectTypeTextView;
	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != Activity.RESULT_OK) {
			// Log.e(TAG, "requestCode = " + requestCode);
			// Log.e(TAG, "resultCode = " + resultCode);
			// Log.e(TAG, "data = " + intent);
			return;
		} else {
			
			/* 屏蔽生成照片功能 代码保留*/
			
			switch (requestCode) {
			case CHOOSE_FROM_CAMERA:
				Intent objIntent = new Intent("com.android.camera.action.CROP");
				objIntent.setDataAndType(headImageUri, "image/*");
				objIntent.putExtra("crop", "true");
				objIntent.putExtra("aspectX", 2);
				objIntent.putExtra("aspectY", 3);
				objIntent.putExtra("outputX", Constants.headImageWidth);
				objIntent.putExtra("outputY", Constants.headImageHeight);
				objIntent.putExtra("scale", true);
				objIntent.putExtra(MediaStore.EXTRA_OUTPUT, headImageUri);
				objIntent.putExtra("return-data", false);
				objIntent.putExtra("scaleUpIfNeeded", true);// 去除黑边
				objIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				objIntent.putExtra("noFaceDetection", true); // no face
																// detection
				startActivityForResult(objIntent, COPY_PICTURE);
				break;
			case COPY_PICTURE:
				// Log.d(TAG, "COPY_PICTURE : intent="+intent);
				showSelectPicture(headImageUri);
				break;
			case CHOOSE_FROM_FILE:
				// Log.d(TAG, "CHOOSE_PICTURE : intent="+intent);
				showSelectPicture(headImageUri);
				break;
			} 
		}
	}

	/**
	 * 
	 * @param uri
	 */
	private void showSelectPicture(Uri uri) {
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(uri));

			String saveImagePath = CommonUtil.getSdCardPath() + Constants.genImagePath
					+ this.objPersonalCertificateView.getPersonalInf().getIdCardNo() + ".jpg";
			CommonUtil.createFileDir(CommonUtil.getSdCardPath() + Constants.genImagePath);
			ComposeImageUtils ComposeImageUtils = new ComposeImageUtils();

			// 获取背景图片资源
			loadBackgroundImage(mActivity);
			// 获取背景图片
			Bitmap backGroundBitmap = BitmapFactory.decodeFile(CommonUtil.getSdCardPath() + Constants.sysImagePath
					+ "background.jpg");

			String personIdNo = EncryUtil.encrypt(this.objPersonalCertificateView.getPersonalInf().getIdCardNo());

			ComposeImageUtils.generationImageOperate(mActivity, bmp, personIdNo, this.objPersonalCertificateView
					.getPersonalInf().getContractorName(), this.objPersonalCertificateView.getPersonalInf()
					.getUserName(), backGroundBitmap, saveImagePath);
		} catch (FileNotFoundException e) {
		}
	}

	/**
	 * 将程序包中的原有的离线资源文件删除，然后从安装APK中拷贝出�?
	 * 
	 * @return 操作成功，返回true,否则返回false
	 */
	private Boolean loadBackgroundImage(Context mContext) {
		// 文件夹目�?
		final String fileDirPath = CommonUtil.getSdCardPath() + Constants.sysImagePath;
		// 文件路径
		final String filePath = fileDirPath + "background.jpg";
		// 加入此文件已存在，则无需再拷贝出�?
		if (CommonUtil.isFileExists(filePath)) {
			return true;
		}

		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = mContext.getResources().getAssets().open("background.jpg");
			CommonUtil.createFileDir(fileDirPath);
			out = new FileOutputStream(new File(filePath));
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			in.close();
			out.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
}
