package org.wind.database;

import java.lang.reflect.Field;
import java.security.PublicKey;
import java.util.ArrayList;

import org.wind.annotation.Table;

import android.content.ContentValues;

public class Insert {
	// sql语句
	private StringBuilder sql = null;
	private ArrayList<String> list = null;
	private String table = null;
	// 记录需要插入的字段数
	private int count = 0;
	private StringBuilder r_fields = new StringBuilder();
	private StringBuilder r_values = new StringBuilder();
	private Class<?> clazz = null;

	private Insert(Class<?> clazz) {
		this.clazz = clazz;
		sql = new StringBuilder(" insert into ");
		table = clazz.getAnnotation(Table.class).DTname();
		list = new ArrayList<String>();
		sql.append(" ").append(table).append(" ");
		// 获取数据库里的字段
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(org.wind.annotation.Field.class)) {
				// 自增的除外
				boolean add = field.getAnnotation(
						org.wind.annotation.Field.class).add();
				if (!add) {
					count++;
				}
			}
		}
	}

	public synchronized static Insert into(Class<?> clazz) {

		return new Insert(clazz);
	}

	@Deprecated
	public Insert field() {
		String name = "";
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(org.wind.annotation.Field.class)) {
				try {
					name = field.getAnnotation(org.wind.annotation.Field.class)
							.name();
					r_fields.append(",").append(name);
					r_values.append(",").append("?");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		if (count > 0) {
			r_fields.deleteCharAt(r_fields.indexOf(","));
			r_values.deleteCharAt(r_values.indexOf(","));
			r_fields.insert(0, "(");
			r_values.insert(0, "(");
			r_fields.append(")");
			r_values.append(")");
		}
		return this;
	}

	public Insert field(Object obj, ContentValues values) {
		if (obj == null || values == null) {
			throw new IllegalArgumentException("Insert对象不能为空!");
		} else {
			Field[] fields = obj.getClass().getDeclaredFields();
			String name = "";
			// 判断有没有null
			boolean flag = false;
			Object value = null;
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(org.wind.annotation.Field.class)) {
					try {
						name = field.getAnnotation(org.wind.annotation.Field.class)
								.name();
						value = field.get(obj);
						if (value != null) {
							r_fields.append(",").append(name);
							r_values.append(",").append("?");
							isTxtType(field, name, value, values);
						} else {
							flag = true;
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
			if (!flag) {
				r_fields.replace(0, r_fields.length(), " ");
			} else {
				if (r_fields.length() > 0 && r_values.length() > 0) {
					r_fields.deleteCharAt(r_fields.indexOf(","));
					r_values.deleteCharAt(r_values.indexOf(","));
					r_fields.insert(0, "(");
					r_values.insert(0, "(");
					r_fields.append(")");
					r_values.append(")");
				}
			}
		}
		return this;
	}

	// 判断是否为文本类型
	private void isTxtType(Field field, String key, Object obj,
			ContentValues values) {
		if (Long.class.equals(field.getType())
				|| Long.TYPE.equals(field.getType())) {
			values.put(key, (Long) obj);
		} else if (Float.class.equals(field.getType())
				|| Float.TYPE.equals(field.getType())) {
			values.put(key, (Float) obj);
		} else if (Double.class.equals(field.getType())
				|| Double.TYPE.equals(field.getType())) {
			values.put(key, (Float) obj);
		} else if (Integer.class.equals(field.getType())
				|| Integer.TYPE.equals(field.getType())) {
			values.put(key, (Integer) obj);
		} else if (Boolean.class.equals(field.getType())
				|| Boolean.TYPE.equals(field.getType())) {
			values.put(key, (Boolean) obj);
		} else if (Byte.class.equals(field.getType())
				|| Byte.TYPE.equals(field.getType())) {
			values.put(key, (Byte) obj);
		} else {
			values.put(key, String.valueOf(obj));
		}
	}

	public String getSQL() {

		// if (list.size() > 0) {
		// for (String str : list) {
		// fields.append(",").append(str);
		// values.append(",").append("?");
		// }
		// fields.deleteCharAt(fields.indexOf(","));
		// // 确定字段
		// sql.append(" (").append(fields).append(")");
		// } else {
		// for (int i = 0; i < count; i++) {
		// values.append(",").append("?");
		// }
		// }
		// fields.deleteCharAt(fields.indexOf(","));
		sql.append(r_fields).append(" values ").append(r_values);
		return sql.toString();
	}
}
