package org.wind.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {
	private static PreferenceUtils utils = null;
	private static SharedPreferences preferences = null;

	private PreferenceUtils(Context context, String name) {
		preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static PreferenceUtils init(Application context, String name) {
		if (utils == null || preferences == null) {
			utils = new PreferenceUtils(context, name);
		}
		return utils;
	}

	public static String getString(String key, String def) {
		return preferences.getString(key, def);
	}

	public static int getInt(String key, int def) {
		return preferences.getInt(key, def);
	}

	public static boolean getBool(String key, Boolean def) {
		return preferences.getBoolean(key, def);
	}

	public static void putString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putBoolen(String key, Boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putInt(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
}
