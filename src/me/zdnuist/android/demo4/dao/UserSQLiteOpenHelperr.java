package me.zdnuist.android.demo4.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//https://github.com/VectorYi/MVPSample/blob/master/src/com/mvp/model/UserModel.java
public class UserSQLiteOpenHelperr extends SQLiteOpenHelper{
	
	public static final String DB_NAME = "user.db";
	public static final int VERSION_CODE = 1;
	
	public static final String CREATE_TABLE_SQL = 
			" create table user ("
			+ " id integer primary key autoincrement, name string, age integer);";
	public static final String DROP_TABLE_SQL = 
			"drop table user ;";
	
	public UserSQLiteOpenHelperr(Context context){
		this(context,DB_NAME,null,VERSION_CODE);
	}

	public UserSQLiteOpenHelperr(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_SQL);
		db.execSQL(CREATE_TABLE_SQL);
	}

}
