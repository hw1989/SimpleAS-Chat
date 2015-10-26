package com.huwei.db;

import org.wind.database.TableHelper;
import android.R.integer;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huwei.common.Common;
import com.huwei.entity.ChatMessage;
import com.huwei.entity.ConnectInfo;
import com.huwei.entity.GroupInfo;
import com.huwei.entity.Relationship;
import com.huwei.entity.UserInfo;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public MySQLiteOpenHelper(Context context) {
		this(context, Common.DB_Name, null, 7);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableHelper connect = new TableHelper(ConnectInfo.class);
		TableHelper chatMessage = new TableHelper(ChatMessage.class);
		TableHelper user=new TableHelper(UserInfo.class);
		TableHelper group=new TableHelper(GroupInfo.class);
		TableHelper relationship=new TableHelper(Relationship.class);
//		String sql=user.getSQL();
		db.beginTransaction();
		try {
			db.execSQL(connect.getSQL());
			db.execSQL(chatMessage.getSQL());
			db.execSQL(user.getSQL());
			db.execSQL(group.getSQL());
			db.execSQL(relationship.getSQL());
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			// db.close();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
