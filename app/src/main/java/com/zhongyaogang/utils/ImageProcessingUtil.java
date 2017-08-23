package com.zhongyaogang.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zhongyaogang.utils.FileDirectory;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class ImageProcessingUtil {
	/** 创建存储文件 */
	@SuppressLint("SimpleDateFormat")
	public static File getAlbumDir(String path) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "img_" + timeStamp + ".jpg";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File image = new File(dir, imageFileName);
		return image;
	}
	public static void save(String mCurrentPhotoPath, int siz) {

		if (mCurrentPhotoPath != null) {

			try {

				Bitmap bm = getSmallBitmap(mCurrentPhotoPath);

				FileOutputStream fos = new FileOutputStream(mCurrentPhotoPath);

				bm.compress(Bitmap.CompressFormat.JPEG, siz, fos);
				if (bm != null && !bm.isRecycled()) {
					bm.recycle();
				}
			} catch (Exception e) {
				Log.e("图片压缩处理.............", "error", e);
			}

		}
	}
	/** 根据路径获得突破并压缩返回bitmap用于显示 */
	public static Bitmap getSmallBitmap(String filePath) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int dx = calculateInSampleSize(options, 600, 600);
		options.inSampleSize = dx;

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/** 计算图片的缩放值 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);
			if (widthRatio > heightRatio) {
				inSampleSize = widthRatio;
			} else {
				inSampleSize = heightRatio;
			}
		}
		return inSampleSize;
	}
	/** 获取图库图片路径 */
	public static String selectImage(Context context, Intent data, int siz) {
		String path = null;
		Bitmap bitmap = null;
		Uri uri = data.getData();
		ContentResolver cr = context.getContentResolver();
		try {
			InputStream input = cr.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		/* 存储到本地进行压缩 */
		File fileName = getAlbumDir(FileDirectory.BD);
		FileOutputStream b = null;
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
			path = fileName.getPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int y = (w > h) ? w : h;
		if (y > 2300) {
			save(path, siz);
		}

		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return path;
	}
}
