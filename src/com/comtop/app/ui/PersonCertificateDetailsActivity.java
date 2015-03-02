package com.comtop.app.ui;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.db.HolderEmployeeProject;
import com.comtop.app.db.PersonalCertificate;
import com.comtop.app.db.PersonalCertificateInf;
import com.comtop.app.entity.HolderEmployeeProjectVO;
import com.comtop.app.entity.HolderEmployeeProjectView;
import com.comtop.app.entity.PersonalCertificateDataVO;
import com.comtop.app.entity.PersonalCertificateVO;
import com.comtop.app.entity.PersonalCertificateView;
import com.comtop.app.entity.base.BaseContentList;
import com.comtop.app.https.HttpClient;
import com.comtop.app.ui.base.BaseFragmentActivity;
import com.comtop.app.utils.DBHelper;
import com.comtop.app.utils.FormConvertUtil;
import com.comtop.app.utils.StringUtil;
import com.comtop.app.view.HttpErrorFragment;
import com.comtop.app.view.NoDataFragment;
import com.comtop.app.view.PersonInfTabFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 人员持证情况详情界面activity
 * 
 * 2014-04-21
 * 
 * @author by xxx
 * 
 */
public class PersonCertificateDetailsActivity extends BaseFragmentActivity implements OnClickListener {

	/** 人员信息详情参数 */
	private RequestParams personInfRequestParams;

	/** 项目详情参数 */
	private RequestParams projectInfRequestParams;

	/** 人员持证ID */
	private String holderCertificateId;

	/** loading加载布局 */
	private LinearLayout loadLayout;

	private DBHelper dbHelper;

	private FragmentManager mFragmentManager;
	
	
	/** 人员信息数据 - 用于明细列表显示 */
	private PersonalCertificateView objPersonalCertificateView;
	/** 项目信息数据 - 用于明细列表显示*/
	private HolderEmployeeProjectView objHolderEmployeeProjectView;


	// 用于在本地存储用户信�?	private SharedPreferences sharedPreferences;

	private String currDataArea;


	private boolean isOnlineFlag;

	private ImageView backPreImageView;
	private TextView personNameTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_details_activity);

		backPreImageView = (ImageView) findViewById(R.id.backPreImageView);
		backPreImageView.setOnClickListener(this);
		personNameTextView = (TextView) findViewById(R.id.above_personTextView);

		sharedPreferences = getSharedPreferences(Constants.SHARE_NAME, MODE_PRIVATE);
		currDataArea = getUserProvinceArea();

		dbHelper = DBHelper.getInstance(this, currDataArea);

		this.mFragmentManager = getSupportFragmentManager();

		holderCertificateId = getIntent().getStringExtra("holderCertificateId");
		if (StringUtil.isEmpty(holderCertificateId)) {
			personNameTextView.setText("持证详情");
		} else {
			personNameTextView.setText(getIntent().getStringExtra("holderName"));
		}

		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		isOnlineFlag = sharedPreferences.getBoolean("IS_ONLINE", false);

	}

	@Override
	public void onResume() {
		super.onResume();
		//personInfButton.performClick();
		if (isOnlineFlag) {
			getPersonOnlineData();
		} else {
			getPersonOfflineData();
		}
	}

	/**
	 * 获取人员信息网络数据
	 * 
	 * @param transaction
	 *            FragmentTransaction
	 */
	public void getPersonOnlineData() {

		personInfRequestParams = new RequestParams();
		personInfRequestParams.put("actionType", "holderCertificate");
		personInfRequestParams.put("method", "queryPersonDetail");
		personInfRequestParams.put("queryStr", holderCertificateId);

		if (StringUtil.isEmpty(holderCertificateId)) {
			FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
			mTransaction.replace(R.id.myPersonDetail, new NoDataFragment());
			mTransaction.commit();
			return;
		}

		HttpClient.get("", personInfRequestParams, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				loadLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(String response) {
				loadLayout.setVisibility(View.GONE);
				try {
					ObjectMapper mObjectMapper = new ObjectMapper();
					objPersonalCertificateView  = mObjectMapper.readValue(response,
							new TypeReference<PersonalCertificateView>() {});
					
					/*
					 * 查询项目列表数据
					 *  
					 *  后续可以优化合并，与人员信息同一请求，需要修改服务器请求返回数据
					 */
					PersonCertificateDetailsActivity.this.getProjectOnlineData();
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable e, String data) {
				loadLayout.setVisibility(View.GONE);
				FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
				mTransaction.replace(R.id.myPersonDetail, new HttpErrorFragment());
				mTransaction.commit();
			}

		});

	}

	/**
	 * 人员信息获取离线包数�?	 */
	public void getPersonOfflineData() {
		if (StringUtil.isEmpty(holderCertificateId)) {
			FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
			mTransaction.replace(R.id.myPersonDetail, new NoDataFragment());
			mTransaction.commit();
			return;
		}
		new MyTask().execute("人员信息");
	}

	/**
	 * 获取项目信息网络数据
	 * 
	 * @param transaction
	 *            FragmentTransaction
	 */
	public void getProjectOnlineData() {
		if (StringUtil.isEmpty(holderCertificateId)) {
			FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
			mTransaction.replace(R.id.myPersonDetail, new NoDataFragment());
			mTransaction.commit();
			return;
		}
		
		projectInfRequestParams = new RequestParams();
		projectInfRequestParams.put("actionType", "holderCertificate");
		projectInfRequestParams.put("method", "queryProjectDetail");
		projectInfRequestParams.put("queryStr", holderCertificateId);
		HttpClient.get("", projectInfRequestParams, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				loadLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(String response) {
				loadLayout.setVisibility(View.GONE);

				if (StringUtil.isEmpty(response)) {
					PersonInfTabFragment myPersonInfTabFragment = new PersonInfTabFragment(
							PersonCertificateDetailsActivity.this, objPersonalCertificateView,objHolderEmployeeProjectView);
					myPersonInfTabFragment.onAttach(PersonCertificateDetailsActivity.this);
					FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
					mTransaction.replace(R.id.myPersonDetail, myPersonInfTabFragment);
					mTransaction.commit();
					
				} else {
					try {
						ObjectMapper mObjectMapper = new ObjectMapper();
						objHolderEmployeeProjectView = mObjectMapper.readValue(response,
								new TypeReference<HolderEmployeeProjectView>() {
								});
						
						PersonInfTabFragment myPersonInfTabFragment = new PersonInfTabFragment(
								PersonCertificateDetailsActivity.this, objPersonalCertificateView,objHolderEmployeeProjectView);
						myPersonInfTabFragment.onAttach(PersonCertificateDetailsActivity.this);
						FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
						mTransaction.replace(R.id.myPersonDetail, myPersonInfTabFragment);
						mTransaction.commit();
						
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onFailure(Throwable e, String data) {
				loadLayout.setVisibility(View.GONE);
				PersonInfTabFragment myPersonInfTabFragment = new PersonInfTabFragment(
						PersonCertificateDetailsActivity.this, objPersonalCertificateView,objHolderEmployeeProjectView);
				myPersonInfTabFragment.onAttach(PersonCertificateDetailsActivity.this);
				FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
				mTransaction.replace(R.id.myPersonDetail, myPersonInfTabFragment);
				mTransaction.commit();
				
			}

		});

	}
	
	

	/**
	 * 项目信息获取离线包数�?	 */
	public void getProjectOfflineData() {
		if (StringUtil.isEmpty(holderCertificateId)) {
			FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
			mTransaction.replace(R.id.myPersonDetail, new NoDataFragment());
			mTransaction.commit();
			return;
		}
		new MyTask().execute("项目信息");
	}

	@Override
	public void onClick(View objView) {
		switch (objView.getId()) {
		case R.id.backPreImageView:
			finish();
		}

	}

	public class MyTask extends AsyncTask<String, Integer, BaseContentList> {

		@Override
		protected void onPreExecute() {
			loadLayout.setVisibility(View.VISIBLE);
		}

		@Override
		protected BaseContentList doInBackground(String... params) {
			if ("人员信息".equals(params[0])) {
				
				/*
				 * 查询人员信息及证�?				 */
				objPersonalCertificateView = new PersonalCertificateView();
				// 获取人员基本信息
				List<PersonalCertificateInf> lstPersonalCertificateInf = dbHelper
						.getPersonalCertificateInfById(holderCertificateId);

				PersonalCertificateVO personalInf = new PersonalCertificateVO();

				if (lstPersonalCertificateInf != null && lstPersonalCertificateInf.size() > 0) {
					FormConvertUtil.personalCertificateInfToPersonalCertificateVO(lstPersonalCertificateInf.get(0),
							personalInf);
				} else {
					return null;
				}

				objPersonalCertificateView.setPersonalInf(personalInf);

				// 获取资质证书列表
				List<PersonalCertificate> lstPersonalCertificate = dbHelper
						.getPersonalCertificateById(holderCertificateId);

				List<PersonalCertificateDataVO> lstPersonalCertificateDataVO = FormConvertUtil
						.personalCertificateLstTopersonalCertificateDataVOLst(lstPersonalCertificate);

				objPersonalCertificateView.setCertificateDatas(lstPersonalCertificateDataVO);

				
				
				/*
				 * 查询项目信息
				 */
				 objHolderEmployeeProjectView = new HolderEmployeeProjectView();
				List<HolderEmployeeProject> lstProject = dbHelper.getProjectInfByCertificateId(holderCertificateId);

				if (lstProject.size() == 0) {
					objHolderEmployeeProjectView = null;
				} else {
					List<HolderEmployeeProjectVO> tempLst = FormConvertUtil.getlstFromHolderEmployeeProject(lstProject);
					objHolderEmployeeProjectView.setItems(tempLst);

				}
				
				return objPersonalCertificateView;

			} 

			return null;
		}

		@Override
		protected void onPostExecute(BaseContentList result) {
			loadLayout.setVisibility(View.GONE);

			if (result == null) {
				FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
				mTransaction.replace(R.id.myPersonDetail, new NoDataFragment());
				mTransaction.commit();
			} else {
				FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
				if (result instanceof PersonalCertificateView) {// 获取的人员信�?					
					PersonInfTabFragment myPersonInfTabFragment = new PersonInfTabFragment(
							PersonCertificateDetailsActivity.this, objPersonalCertificateView,objHolderEmployeeProjectView);
					myPersonInfTabFragment.onAttach(PersonCertificateDetailsActivity.this);
					//FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
					mTransaction.replace(R.id.myPersonDetail, myPersonInfTabFragment);
					mTransaction.commit();
				} 
			}

		}

	}

	/**
	 * 获取当前用户所选择的省份区域编�?	 * 
	 * @return 省份区域编码
	 */
	private String getUserProvinceArea() {
		String areaCode = sharedPreferences.getString(Constants.USER_PROVINCE_CODE, "00");
		return areaCode;

	}

}

