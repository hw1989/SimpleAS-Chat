package org.wind.annotation;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
public class ActivityInject {
	private static ActivityInject instance = null;

	private ActivityInject() {
	}

	public static ActivityInject getInstance() {
		if (instance == null) {
			instance = new ActivityInject();
		}
		return instance;
	}

	/**
	 * 设置的activity的注解 ,使用反射
	 */
	public void setInject(Activity activity) {
		int id = 0;
		ViewInject inject = null;
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				// 判断是否为ViewInject注解
				if (field.isAnnotationPresent(ViewInject.class)) {
					inject = field.getAnnotation(ViewInject.class);
					id = inject.id();
					if (id > 0) {
						try {
							field.setAccessible(true);
							field.set(activity, activity.findViewById(id));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 设置的fragment的注解 ,使用反射
	 */
	public void setInject(Fragment fragment, View view) {
		int id = 0;
		ViewInject inject = null;
		Field[] fields = fragment.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				// 判断是否为ViewInject注解
				if (field.isAnnotationPresent(ViewInject.class)) {
					inject = field.getAnnotation(ViewInject.class);
					id = inject.id();
					if (id > 0) {
						try {
							field.setAccessible(true);
							field.set(view, view.findViewById(id));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
