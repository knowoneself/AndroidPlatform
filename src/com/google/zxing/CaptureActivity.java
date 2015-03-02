package com.google.zxing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Vector;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comtop.app.MyApplication;
import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.comtop.app.entity.PersonalCertificateListEntity;
import com.comtop.app.entity.PersonalCertificateVO;
import com.comtop.app.https.HttpClient;
import com.comtop.app.ui.PersonCertificateDetailsActivity;
import com.comtop.app.ui.base.BaseActivity;
import com.comtop.app.utils.DBHelper;
import com.comtop.app.utils.EncryUtil;
import com.comtop.app.utils.StringUtil;
import com.comtop.app.utils.ThreadPoolManager;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.view.ViewfinderView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 二维码扫�? * 
 * 2014-05-27
 * 
 * @author by xxx
 * 
 */
public class CaptureActivity extends BaseActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private TextView mTitle;
	private ImageView mGoHome;

	private ImageButton mylightImageButton;

	private boolean isLightOpen;

	private TextView myLightTextView;

	private LinearLayout loadingLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qr_code_scan);

		CameraManager.init(getApplication());
		initControl();

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	private void initControl() {
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		loadingLayout = (LinearLayout) findViewById(R.id.view_loading);
		mTitle = (TextView) findViewById(R.id.details_textview_title);
		mTitle.setText(R.string.scan_title);
		mGoHome = (ImageView) findViewById(R.id.details_imageview_gohome);
		mGoHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mylightImageButton = (ImageButton) findViewById(R.id.mylight);
		myLightTextView = (TextView) findViewById(R.id.mylightText);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		isLightOpen = false;

		mylightImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!isLightAvaliable()) {
					showShortToast("闪光灯不能用�?);
					return;
				} else {
					if (isLightOpen) {// 闪光灯打开了，就关�?						isLightOpen = false;
						myLightTextView.setText(R.string.openLight);
						CameraManager.get().closeLight();

					} else {// 闪光灯关闭了，就打开
						isLightOpen = true;
						myLightTextView.setText(R.string.closeLight);
						CameraManager.get().openLight();

					}

				}
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();

	}

	@Override
	public void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		final String resultString = result.getText();

		byte[] mByte = EncryUtil.decrypt(resultString);

		String personIdNo = "";
		if (mByte != null) {
			personIdNo = new String(mByte);
		}
		String cardNo = personIdNo;
		if (StringUtil.isEmpty(cardNo)) {
			showShortToast("二维码扫描失败！");
			return;
		}
		if (MyApplication.getIsOnLineData()) {// 获取在线数据
			getOnLineContent(cardNo);
		} else {// 读取离线包数�?			getOffLineContent(cardNo);
		}

	}

	/**
	 * 获取在线数据
	 * 
	 * @param cardNo
	 *            身份证号�?	 */
	private void getOnLineContent(String cardNo) {
		RequestParams params = new RequestParams();
		params.add("actionType", "holderCertificate");
		params.add("method", "queryPersonList");
		params.add("pageNo", "1");
		if (!StringUtil.isEmpty(cardNo)) {
			try {
				params.add("queryStr", URLEncoder.encode(cardNo, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		HttpClient.get("", params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// 显示"加载"图标
				loadingLayout.setVisibility(View.VISIBLE);

			}

			@Override
			public void onSuccess(String response) {
				loadingLayout.setVisibility(View.GONE);

				ObjectMapper mObjectMapper = new ObjectMapper();
				try {
					PersonalCertificateListEntity personalCertificateListEntity = mObjectMapper.readValue(response,
							new TypeReference<PersonalCertificateListEntity>() {
							});

					String holderCertificateId = personalCertificateListEntity.getItems().get(0)
							.getHolderCertificateId();

					String personName = personalCertificateListEntity.getItems().get(0).getPersonName();
					Bundle bundle = new Bundle();
					bundle.putString("holderCertificateId", holderCertificateId);
					bundle.putString("holderName", personName);
					openActivity(PersonCertificateDetailsActivity.class, bundle);

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
				loadingLayout.setVisibility(View.GONE);
				showLongToast("二维码扫描失败！");
			}

		});

	}

	/**
	 * 获取离线数据
	 * 
	 * @param cardNo
	 *            身份证号�?	 */
	private void getOffLineContent(final String cardNo) {
		Runnable mRunnable = new Runnable() {
			@Override
			public void run() {
				DBHelper dbHelper = DBHelper.getInstance(CaptureActivity.this, getUserProvinceArea());
				PersonalCertificateVO tempVO = dbHelper.getHolderCertificateId(cardNo);
				Bundle bundle = new Bundle();
				bundle.putString("holderCertificateId", tempVO.getHolderCertificateId());
				bundle.putString("holderName", tempVO.getUserName());
				openActivity(PersonCertificateDetailsActivity.class, bundle);

			}

		};

		ThreadPoolManager.getInstance().addTask(mRunnable);

	}

	/**
	 * 获取当前用户所选择的省份区域编�?	 * 
	 * @return 省份区域编码
	 */
	private String getUserProvinceArea() {
		SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_NAME, MODE_PRIVATE);
		String areaCode = sharedPreferences.getString(Constants.USER_PROVINCE_CODE, "00");
		return areaCode;

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	/**
	 * 扫描正确后的震动声音,如果感觉apk大了,可以删除
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.qrcode_completed);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	/**
	 * 判断闪光灯是否可�?	 * 
	 * @return true 可以使用�?false 不可以使�?	 */
	private boolean isLightAvaliable() {
		boolean flag = false;
		PackageManager pm = this.getPackageManager();
		FeatureInfo[] features = pm.getSystemAvailableFeatures();

		for (FeatureInfo mFeatures : features) {
			if (PackageManager.FEATURE_CAMERA_FLASH.equals(mFeatures.name)) // 判断设备是否支持闪光�?			{
				flag = true;
				break;
			}
		}
		return flag;
	};

}

