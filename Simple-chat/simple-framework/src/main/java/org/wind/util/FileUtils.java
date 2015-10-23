package org.wind.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.R.integer;

public class FileUtils {
	/**
	 * 判断路径是否存在
	 * 
	 * @param path验证路径
	 * @param create创建路径
	 */
	public static boolean createorexistsPath(String path, boolean create) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查文件是否存在
	 */
	public static boolean fileExist(String filepath) {
		File file = new File(filepath);
		if (!file.isFile()) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static byte[] file2ByteArray(String filepath) {
		byte[] array = null;
		File file = new File(filepath);
		int len = 0;
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		byte[] buffere = new byte[1024];
		if (fileExist(filepath)) {
			try {
				fis = new FileInputStream(file);
				baos = new ByteArrayOutputStream();
				while ((len = fis.read(buffere)) != -1) {
					baos.write(buffere, 0, len);
				}
				array = baos.toByteArray();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return array;
	}
}
