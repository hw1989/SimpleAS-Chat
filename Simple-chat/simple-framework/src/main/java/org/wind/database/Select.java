package org.wind.database;

import java.util.List;

import org.wind.annotation.Table;
import org.wind.util.StringHelper;

import android.database.sqlite.SQLiteDatabase;

/**
 * 查询
 * 
 * @author 作者 :wei.hu
 * @version 创建时间:2015-5-13 下午2:19:18
 */
public class Select {
	// sql语句
	private StringBuilder sql = null;
	private String table = null;
	private String column = null;
	private StringBuilder where = null;
	private StringBuilder and = null;
	private String page = null;
	private StringBuilder order = null;
	private String[] args = null;

	private Select(Class<?> clazz) {
		sql = new StringBuilder(" select ");
		and = new StringBuilder();
		table = clazz.getAnnotation(Table.class).DTname();
	};

	public synchronized static Select from(Class<?> clazz) {
		return new Select(clazz);
	}

	public Select column(String str) {
		column = str;
		return this;
	}

	/**
	     * 
	     */
	public Select where(String str, String... args) {
		if (!StringHelper.isNullorEmpty(str.trim())) {
			if (str != null) {
				int count = 0;
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '?') {
						count++;
					}
				}
				if (count != 0 && args == null) {
					throw new RuntimeException("sql语句的参数数量不对应!");
				}
				if (count != 0 && args.length < count) {
					throw new RuntimeException("sql语句的参数数量不对应!");
				}
			}
			where = new StringBuilder(" where ").append(str);
			this.args = args;
		}
		return this;
	}

	public Select and(String str) {
		if (!StringHelper.isNullorEmpty(str.trim())) {
			and.append(" and( ").append(str).append(" ) ");
		}
		return this;
	}

	public List<?> exec(SQLiteDatabase db) {
		return null;
	}

	public String getSQL() {
		if (column == null) {
			sql.append(" * ");
		} else {
			sql.append(column);
		}
		sql.append(" from ").append(table);
		if (where != null) {
			sql.append(where);
			if (and != null) {
				sql.append(and);
			}
		}
		if (order != null) {
			sql.append(order);
		}
		return sql.toString();
	}

	public Select orderAsc(String str) {
		if (!StringHelper.isNullorEmpty(str.trim())) {
			order = new StringBuilder(" order by ").append(str).append(" asc ");
		}
		return this;
	}

	public Select orderDesc(String str) {
		if (!StringHelper.isNullorEmpty(str.trim())) {
			order = new StringBuilder(" order by ").append(str)
					.append(" desc ");
		}
		return this;
	}
}
