package com.comtop.app.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.comtop.app.R;
import com.comtop.app.constant.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ComposeImageUtils {

	/**
	 * 生成文字图片合成操作函数
	 * 
	 * @param headBitmap
	 *            头像图片
	 * @param qcImageUrl
	 * @param constractorName
	 * @param personName
	 */
	public void generationImageOperate(final Context mContext, Bitmap headBitmap, String personId,
			String constractorName, String personName, Bitmap backGroundBitmap, final String imageSavePath) {

		// 创建一个空白Bitmap
		final Bitmap blankBitmap = Bitmap.createBitmap(Constants.backGroundWidth, Constants.backGroundHeight,
				Config.ARGB_8888);
		// 创建一个画�?
		Canvas mCanvas = new Canvas(blankBitmap);
		// 绘制背景图片
		if (backGroundBitmap != null) {
			mCanvas.drawBitmap(backGroundBitmap, 0, 0, null);
		}

		// 绘制承包商名�?
		Paint constractorNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		constractorNamePaint.setTextSize(Constants.constractorName_fontSize);// 字体大小
		constractorNamePaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽�?
		constractorNamePaint.setColor(Color.BLACK);// 采用的颜�?

		int constractorNameTextWidth = Constants.constractorName_fontSize * constractorName.length();
		int constractorName_X = (Constants.backGroundWidth - constractorNameTextWidth) / 2;
		if (constractorName_X < 0) {
			constractorName_X = 0;
		}
		mCanvas.drawText(constractorName, constractorName_X, Constants.constractorName_Y, constractorNamePaint);

		// 获取头像图片
		// Bitmap headBitmap = BitmapFactory.decodeFile(headImageUrl);
		// 绘制头像图片
		if (headBitmap != null) {
			mCanvas.drawBitmap(headBitmap, (Constants.backGroundWidth - Constants.headImageWidth) / 2,
					Constants.headImage_Y, null);
		}

		// 绘制人名
		Paint personNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		personNamePaint.setTextSize(Constants.personName_fontSize);// 字体大小
		personNamePaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽�?
		personNamePaint.setColor(Color.BLACK);// 采用的颜�?

		int personNameTextWidth = Constants.personName_fontSize * personName.length();
		int personName_X = (Constants.backGroundWidth - personNameTextWidth) / 2;
		mCanvas.drawText(personName, personName_X, Constants.personName_Y, personNamePaint);

		// 绘制身份证号码二维码
		try {
			Bitmap qrBitmap = Create2DCode(personId);
			if (qrBitmap != null) {
				mCanvas.drawBitmap(qrBitmap, (Constants.backGroundWidth - Constants.qrCode_Height_Width) / 2,
						Constants.qr_Y, null);
			}

		} catch (WriterException e1) {
			e1.printStackTrace();
		}

		mCanvas.save(Canvas.ALL_SAVE_FLAG);// 保存
		mCanvas.restore();

		View mView = LayoutInflater.from(mContext).inflate(R.layout.pic_view_layout, null);
		ImageView mImageView = (ImageView) mView.findViewById(R.id.picImageView);
		mImageView.setImageBitmap(blankBitmap);
		new AlertDialog.Builder(mContext).setTitle("效果预览").setView(mView)
				.setPositiveButton("保存", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						File bitmapFile = new File(imageSavePath);
						bitmapFile.delete();
						try {
							FileOutputStream bitmapWtriter = new FileOutputStream(bitmapFile);
							blankBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapWtriter);
							Toast.makeText(mContext, "保存成功!", Toast.LENGTH_SHORT).show();
						} catch (FileNotFoundException e) {
							Toast.makeText(mContext, "保存失败!", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}

				}).setNegativeButton("取消", null).show();

	}

	/**
	 * 将字符串转化为二维码
	 * 
	 * @param str
	 *            待转换的二维�?
	 * @return 二维码图片Bitmap
	 * @throws WriterException
	 *             异常
	 */
	public Bitmap Create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大�?不要生成了图片以后再进行缩放,这样会模糊导致识别失�?
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, Constants.qrCode_Height_Width,
				Constants.qrCode_Height_Width);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数�?也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}
