package com.comtop.app.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.comtop.app.MyApplication;
import com.comtop.app.R;
import com.comtop.app.adapter.CertificateLastAdapter;
import com.comtop.app.adapter.CertificateResultAdapter;
import com.comtop.app.constant.Constants;
import com.comtop.app.db.HolderEmployeeProject;
import com.comtop.app.db.PersonalCertificateInf;
import com.comtop.app.entity.PersonalCertificateContentItem;
import com.comtop.app.entity.PersonalCertificateListEntity;
import com.comtop.app.https.HttpClient;
import com.comtop.app.ui.PersonCertificateDetailsActivity;
import com.comtop.app.utils.DBHelper;
import com.comtop.app.utils.DateUtil;
import com.comtop.app.utils.StringUtil;
import com.comtop.app.view.CustomListView.OnLoadMoreListener;
import com.comtop.app.view.base.BaseFragment;
import com.google.zxing.CaptureActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CertificateListFragment extends BaseFragment {

	private Activity mActivity;

	private FragmentInterface mFragmentInterface;

	private AutoCompleteTextView mAutoCompleteTextView;

	// private Button cancleSearchButton;

	/** 最近搜索title */
	private View mSearchLastTitleView;
	/** 最近搜�?*/
	private CustomListView mSearchLastListView;
	/** 最近搜索适配�?*/
	private CertificateLastAdapter mSearchLastAdapter;

	private LinkedList<String> mLastSearchLst = new LinkedList<String>();

	/** 搜索结果title */
	private View mSearchResultTitleView;
	/** 搜索结果title */
	private TextView mSearchResultTitle;
	/** 搜索结果 */
	private CustomListView mSearchResultListView;
	/** 搜索结果适配�?*/
	private CertificateResultAdapter mSearchResultAdapter;

	private LinkedList<PersonalCertificateContentItem> mResultSearchLst = new LinkedList<PersonalCertificateContentItem>();

	/** 用于在本地存储用户信�?*/
	private SharedPreferences sharedPreferences;

	private int pageNo_onLine;
	private int pageNo_offLine;

	private DBHelper dbHelper;

	private LinearLayout loadingLayout;

	private String countInf = "�?s人，其中黑名�?%d�?;

	public final static String TAG = "CertificateListFragment";

	private ImageView scanImageView;

	private ImageView clearImageView;
	
	private TextView scanTextView;
	
	private static final int CLICK_NO = 0;// 未点�?
	private static final int CLICK_HAV = 1;// 加载�?
	//查询点击控制
	private int iClickFlag;
	
	//查询时是否清除listView原有数据
	private boolean isDeleteFlag;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mFragmentInterface = (FragmentInterface) mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		sharedPreferences = mActivity.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
		View mView = inflater.inflate(R.layout.certificate_fragment_message_search, null);
		mSearchLastListView = (CustomListView) mView.findViewById(R.id.messagesSearchLast);
		mAutoCompleteTextView = (AutoCompleteTextView) mView.findViewById(R.id.searchEditText);
		mAutoCompleteTextView.setOnEditorActionListener(mOnEditorActionListener);
		mAutoCompleteTextView.addTextChangedListener(mTextWatcher);
		mAutoCompleteTextView.setOnItemClickListener(mItemClickListener);
		mSearchLastTitleView = inflater.inflate(R.layout.search_title, null);

		mSearchResultListView = (CustomListView) mView.findViewById(R.id.messagesSearchResult);
		mSearchResultTitleView = inflater.inflate(R.layout.search_title, null);

		// cancleSearchButton = (Button)
		// mView.findViewById(R.id.cancleSearchTextView);
		loadingLayout = (LinearLayout) mView.findViewById(R.id.view_loading);

		scanImageView = (ImageView) mView.findViewById(R.id.scanImageView);
		scanImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View mView) {
				Intent mIntent = new Intent(mActivity, CaptureActivity.class);
				mActivity.startActivity(mIntent);
				// 画面向左切换效果
				mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			}
		});

		clearImageView = (ImageView) mView.findViewById(R.id.clearImageView);
		clearImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mAutoCompleteTextView.setText("");

			}
		});
		
		//搜索按钮操作
		scanTextView = (TextView) mView.findViewById(R.id.scanTextView);
		scanTextView.setOnClickListener(scanOnClickListener);

		intView();

		iniClass();
		return mView;
	}
	
	
	View.OnClickListener scanOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			String queryStr = mAutoCompleteTextView.getText().toString().trim();
			saveLastMessageData(queryStr); 
			doSearch(true); 
		}
	}; 


	private void intView() {

		TextView lastSearchView = (TextView) mSearchLastTitleView.findViewById(R.id.search_titleTextView);
		lastSearchView.setText(R.string.last_search);

		mSearchLastListView.addHeaderView(mSearchLastTitleView);
		mSearchLastListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position != 1) {
					String item = (String) mSearchLastAdapter.getItem(position - 2);
					mAutoCompleteTextView.setText(item);
					saveLastMessageData(item);
					doSearch(true);
				}

			}

		});
		mSearchLastAdapter = new CertificateLastAdapter(mActivity, mLastSearchLst);
		mSearchLastListView.setAdapter(mSearchLastAdapter);

		mSearchResultTitle = (TextView) mSearchResultTitleView.findViewById(R.id.search_titleTextView);
		mSearchResultTitle.setText(R.string.search_result);
		mSearchResultListView.addHeaderView(mSearchResultTitleView);
		mSearchResultListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position != 1) {

					Bundle mBundle = new Bundle();
					mBundle.putString("holderCertificateId", mResultSearchLst.get(position - 2)
							.getHolderCertificateId());
					mBundle.putString("holderName", mResultSearchLst.get(position - 2).getPersonName());

					Intent mIntent = new Intent(mActivity, PersonCertificateDetailsActivity.class);
					mIntent.putExtras(mBundle);
					mActivity.startActivity(mIntent);
					// 画面向左切换效果
					mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

				}

			}

		});

		mSearchResultListView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				if (MyApplication.getIsOnLineData()) {// 获取在线数据
					pageNoPlusOnLine();
				} else {// 读取离线包数�?
					pageNoPlusOffLine();
				}

				doSearch(false);

			}

		});

		mSearchResultAdapter = new CertificateResultAdapter(mActivity, mResultSearchLst);
		mSearchResultListView.setAdapter(mSearchResultAdapter);

		// cancleSearchButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// mFragmentInterface.replaceFragment(new CertificateQueryFragment(),
		// CertificateQueryFragment.TAG);
		//
		// }
		// });
	}

	private void iniClass() {
		mSearchLastListView.setCanLoadMore(false);
		mSearchLastListView.setCanRefresh(false);
		mSearchResultListView.setCanRefresh(false);
		mSearchResultListView.setCanLoadMore(false);
		loadLastMessageData();

		dbHelper = DBHelper.getInstance(mActivity, MyApplication.getCurrDataArea());

		pageNo_onLine = 1;
		pageNo_offLine = 1;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAutoCompleteTextView.setFocusable(true);
		mAutoCompleteTextView.requestFocus();

	}

	/** pageNo自增 */
	private void pageNoPlusOnLine() {
		pageNo_onLine++;
	}

	/** pageNo自增 */
	private void pageNoPlusOffLine() {
		pageNo_offLine++;
	}

	/**
	 * 查询方法
	 * 
	 * @param clearFlag
	 *            查询前，是否要清除原有记�?true 清除
	 */
	private void doSearch(boolean clearFlag) {
		isDeleteFlag = 	clearFlag;
		((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if (clearFlag) { 
			pageNo_onLine = 1;
			pageNo_offLine = 1;
		}

		String queryStr = mAutoCompleteTextView.getText().toString();
		mSearchLastListView.setVisibility(View.GONE);
		mSearchResultListView.setVisibility(View.VISIBLE);
		showContent(queryStr);
	}

	/**
	 * 点击软键盘上的“搜索”键执行查询
	 */
	private OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				String queryStr = mAutoCompleteTextView.getText().toString().trim();
				saveLastMessageData(queryStr);
				doSearch(true);
				return true;
			}

			return false;
		}
	};

	/** 自动补全监听�?*/
	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence queryStr, int arg1, int arg2, int arg3) {
			String str = queryStr.toString().trim();
			if (str.length() == 0) {
				loadLastMessageData();
				mSearchLastListView.setVisibility(View.VISIBLE);
				mSearchResultListView.setVisibility(View.GONE);
				clearImageView.setVisibility(View.GONE);
			} else {
				clearImageView.setVisibility(View.VISIBLE);
				if (MyApplication.getIsOnLineData()) {// 获取在线数据
					getOnLineAutoCompleteQuery(str);
				} else {// 读取离线包数�?
					getOffLineAutoCompleteQuery(str);
				}

			}

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			doSearch(true);
		}

	};

	/**
	 * 人员持证快速查询补�?查询在线数据)
	 */
	public void getOnLineAutoCompleteQuery(String queryStr) {
		RequestParams params = new RequestParams();
		params.add("actionType", "holderCertificate");
		params.add("method", "autoQueryPerson");
		try {
			params.add("queryStr", URLEncoder.encode(queryStr, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpClient.get("", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {

				ObjectMapper mObjectMapper = new ObjectMapper();

				try {
					String[] autoQuery = mObjectMapper.readValue(response, String[].class);

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.auto_query, autoQuery);

					mAutoCompleteTextView.setAdapter(adapter);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/**
	 * 人员持证快速查询补�?查询离线数据)
	 */
	public void getOffLineAutoCompleteQuery(String queryStr) {
		new MyAutoTask().execute(queryStr);
	}

	/**
	 * 自动补全
	 * 
	 * @author by xxx
	 * 
	 */
	public class MyAutoTask extends AsyncTask<String, Integer, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			String queryStr = params[0];
			List<PersonalCertificateInf> lstPersonalCertificateInf = dbHelper.getAutoContractorName(queryStr);
			List<HolderEmployeeProject> lstHolderEmployeeProject = dbHelper.getAutoProjectName(queryStr);

			List<String> lstTemp = new ArrayList<String>();
			for (PersonalCertificateInf objInf : lstPersonalCertificateInf) {
				String contractorName = objInf.getContractorName();
				lstTemp.add(contractorName);
			}

			for (HolderEmployeeProject objInf : lstHolderEmployeeProject) {
				String projectName = objInf.getProjectName();
				lstTemp.add(projectName);

			}
			String[] arrayResult = new String[lstTemp.size()];
			for (int i = 0; i < lstTemp.size(); i++) {
				arrayResult[i] = lstTemp.get(i);
			}
			return arrayResult;
		}

		@Override
		protected void onPostExecute(String[] result) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.auto_query, result);

			mAutoCompleteTextView.setAdapter(adapter);
		}

	}

	/**
	 * 保存用户最近搜索信�?
	 * 
	 * @param lastQueryStr
	 *            待保存的查询字符�?
	 */
	private void saveLastMessageData(String lastQueryStr) {
		if (StringUtil.isEmpty(lastQueryStr)) {
			return;
		}
		LinkedList<String> messageLst = new LinkedList<String>();
		for (int i = 0; i < Constants.QUERY_STR.length; i++) {
			String tempStr = sharedPreferences.getString(Constants.QUERY_STR[i], "");
			messageLst.addLast(tempStr);
		}
		messageLst.remove(lastQueryStr);
		messageLst.addFirst(lastQueryStr);
		messageLst.removeLast();

		if (messageLst != null) {
			Editor mEditor = sharedPreferences.edit();
			for (int i = 0; i < messageLst.size(); i++) {
				mEditor.putString(Constants.QUERY_STR[i], messageLst.get(i));

			}
			mEditor.commit();
		}

	}

	/**
	 * 加载用户最近搜索信�?
	 */
	private void loadLastMessageData() {
		LinkedList<String> messageLst = new LinkedList<String>();
		for (int i = 0; i < Constants.QUERY_STR.length; i++) {
			String lastQueryStr = sharedPreferences.getString(Constants.QUERY_STR[i], "");
			if (!StringUtil.isEmpty(lastQueryStr)) {
				messageLst.addLast(lastQueryStr);
			}
		}

		if (messageLst.size() > 0) {
			mSearchLastListView.setVisibility(View.VISIBLE);
			mLastSearchLst.clear();
			mLastSearchLst.addAll(messageLst);
			mSearchLastAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 显示程序主列表信�?
	 * 
	 * @param objBaseDao
	 *            数据封装dao
	 * @param params
	 *            查询参数
	 */
	private void showContent(String queryStr) {
		if (MyApplication.getIsOnLineData()) {// 获取在线数据
			if(iClickFlag > 0){return;} //多次点击查询时，程序会退出，问题时不时出现，未找到原�?
			iClickFlag = 1;
			getOnLineContent(queryStr);
		} else {// 读取离线包数�?
			getOffLineContent(queryStr);
		}

	}

	/**
	 * 获取人员证书在线数据
	 * 
	 * @param queryStr
	 *            查询字符�?
	 * @param mBasePageAdapter
	 *            页适配�?
	 */
	public void getOnLineContent(String queryStr) {  
		RequestParams params = new RequestParams();
		params.add("actionType", "holderCertificate");
		params.add("method", "queryPersonList");
		params.add("pageNo", String.valueOf(pageNo_onLine));
		if (!StringUtil.isEmpty(queryStr)) {
			try {
				params.add("queryStr", URLEncoder.encode(queryStr, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		HttpClient.get("", params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// 显示"加载"图标
				if (pageNo_onLine == 1) {
					loadingLayout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onSuccess(String response) {
				loadingLayout.setVisibility(View.GONE);
				if (StringUtil.isEmpty(response) && pageNo_onLine == 1) {
					mSearchResultListView.setCanLoadMore(false);
					mSearchResultListView.removeLoadMore();
					mSearchResultTitle.setText(R.string.no_data);
				} else {
					ObjectMapper mObjectMapper = new ObjectMapper();
					try {
						PersonalCertificateListEntity personalCertificateListEntity = mObjectMapper.readValue(response,
								new TypeReference<PersonalCertificateListEntity>() {
								});
						if(isDeleteFlag){  //如果是重新查询，删除原有数据，不能放ui线程中，会引起notifyDataSetChanged报错
					    mResultSearchLst.clear(); 
						}
						mResultSearchLst.addAll(personalCertificateListEntity.getItems());
						mSearchResultAdapter.notifyDataSetChanged();  
						mSearchResultTitle.setText(String.format(countInf, personalCertificateListEntity.getAllCount(),
								personalCertificateListEntity.getBlackCount()));

						if (personalCertificateListEntity.getItems().size() >= 18) {
							mSearchResultListView.setCanLoadMore(true);
						}else{
							mSearchResultListView.setCanLoadMore(false);
							mSearchResultListView.removeLoadMore();
						}

					} catch (Exception e) {
						iClickFlag = 0;
						e.printStackTrace();
					}
				}

				mSearchResultListView.onLoadMoreComplete();
				iClickFlag = 0;
			}

			@Override
			public void onFailure(Throwable e, String data) {
				loadingLayout.setVisibility(View.GONE);
				mSearchResultTitle.setText(R.string.load_fail);
				iClickFlag = 0;
			}

		});

	}

	public void getOffLineContent(String queryStr) {  
		// AsyncTask.THREAD_POOL_EXECUTOR 表示立即执行，因为asyncTask内部是串行的，会产生等待
		new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, queryStr);
		//new MyTask().execute(queryStr);
	}

	/**
	 * 获取离线包数�?
	 * 
	 * @author by xxx
	 * 
	 *         BaseDao参数是执行异步时传入的参�?Integer 是更新进度条的参�?Map<String, Object> �?
	 *         doInBackground返回的值类�?
	 */
	public class MyTask extends AsyncTask<String, Integer, PersonalCertificateListEntity> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pageNo_offLine == 1) {
				loadingLayout.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected PersonalCertificateListEntity doInBackground(String... params) {

			String queryStr = params[0];

			List<PersonalCertificateInf> lstPersonalCertificateInf = dbHelper.getPersonalCertificateInf(queryStr,
					pageNo_offLine);

			if (lstPersonalCertificateInf.size() == 0) {
				return null;
			}

			PersonalCertificateListEntity objPersonalCertificateListEntity = new PersonalCertificateListEntity();

			List<PersonalCertificateContentItem> items = new ArrayList<PersonalCertificateContentItem>();

			String strCurrentDate = DateUtil.getCurrentDate();
			for (PersonalCertificateInf objInf : lstPersonalCertificateInf) {
				PersonalCertificateContentItem objItem = new PersonalCertificateContentItem();
				objItem.setContractorName(objInf.getContractorName());
				objItem.setHolderCertificateId(objInf.getHolderCertificateId());
				Date blackedDate = objInf.getBlackedDate();
				if (blackedDate != null) {
					String strBlackDate = DateUtil.getFormatDate(blackedDate);
					int iTemp = strCurrentDate.compareTo(strBlackDate);
					if (iTemp <= 0) {// 是黑名单
						objItem.setIsBlacked(1);
					} else {// 不是黑名�?
						objItem.setIsBlacked(2);
					}
				} else {
					objItem.setIsBlacked(2);
				}
				objItem.setPersonName(objInf.getUserName());

				items.add(objItem);
			}
			objPersonalCertificateListEntity.setItems(items);
			if (lstPersonalCertificateInf.size() < 18) {
				objPersonalCertificateListEntity.setMoreFlag("0");
			} else {
				objPersonalCertificateListEntity.setMoreFlag("1");
			}
			objPersonalCertificateListEntity.setPageNum(1);
			objPersonalCertificateListEntity.setQueryStr(queryStr);

			objPersonalCertificateListEntity.setAllCount(dbHelper.getAllCountPersonInf(queryStr));

			objPersonalCertificateListEntity.setBlackCount(dbHelper.getAllBlackCountPersonInf(queryStr));

			return objPersonalCertificateListEntity;
		}

		@Override
		protected void onPostExecute(PersonalCertificateListEntity result) {
			super.onPostExecute(result);
			loadingLayout.setVisibility(View.GONE);

			if (result == null && pageNo_offLine == 1) {
				mSearchResultListView.setCanLoadMore(false);
				mSearchResultListView.removeLoadMore();
				mSearchResultTitle.setText(R.string.no_data);

			} else {
				try {
					if(isDeleteFlag){
					    mResultSearchLst.clear(); 
						}
					mResultSearchLst.addAll(result.getItems());
					mSearchResultAdapter.notifyDataSetChanged();

					mSearchResultTitle.setText(String.format(countInf, result.getAllCount(), result.getBlackCount()));

					if (result.getItems().size() >= 18) {
						mSearchResultListView.setCanLoadMore(true);
					} else {
						mSearchResultListView.setCanLoadMore(false);
						mSearchResultListView.removeLoadMore();
					}
				} catch (NullPointerException e) {
					Log.e("CertificateListFragment", e.toString());
				}

			}

			mSearchResultListView.onLoadMoreComplete();

		}
	}

}
