package com.huwei.utils;

import java.io.File;

import org.wind.util.StringHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.huwei.common.Common;

public class FileUtils {
	public static boolean isExistsImg(String filename) {
		if (filename == null) {
			return false;
		}
		if (StringHelper.isEmpty(filename.trim())) {
			return false;
		}
		File file = new File(Environment.getExternalStorageDirectory()
				+ Common.Path_Image, filename);
		return file.exists();
	}

	public static Bitmap getImageBitemap(String filename) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ Common.Path_Image, filename);
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		return bitmap;
	}

	public static String[][] getImageSuffix() {
		// 图片，语音
		String[][] suffix = new String[][] {
				{ ".png", ".jpg", ".bmp", ".jpeg" }, { ".3gp" } };
		return suffix;
	}
}
