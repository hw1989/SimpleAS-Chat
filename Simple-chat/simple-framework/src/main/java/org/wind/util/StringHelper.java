package org.wind.util;

import java.io.IOException;

import Decoder.BASE64Decoder;
import android.util.Base64;

public class StringHelper {
	@Deprecated
	public static boolean isNullorEmpty(String s) {
		if (s == null || "".equals(s)) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String s) {
		if (s == null || "".equals(s)) {
			return true;
		}
		return false;
	}

	/**
	 * 字节数组转为base64字符
	 */
	public static String byteArray2Base64(byte[] buffere) {
		if(buffere==null){
			return "";
		}
		return Base64
				.encodeToString(buffere, 0, buffere.length, Base64.DEFAULT);
	}

	public static byte[] base642byteArray(String str) {
		// 对base64数据进行解码 生成 字节数组，不能直接用Base64.decode（）；进行解密
		byte[] buffere = null;
		try {
			buffere = new BASE64Decoder().decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffere;
	}
}
