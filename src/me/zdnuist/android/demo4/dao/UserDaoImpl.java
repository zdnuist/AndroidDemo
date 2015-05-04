package me.zdnuist.android.demo4.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import me.zdnuist.android.demo4.model.UserBean;

public class UserDaoImpl implements IUserDao {

	private UserSQLiteOpenHelperr helper;

	public UserDaoImpl(Context context) {
		helper = new UserSQLiteOpenHelperr(context);
	}

	@Override
	public void save(String name , int age) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into user(name , age) values(?,?)", new Object[] {
				name, age });
	}

	@Override
	public UserBean query(int id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		UserBean bean = null;
		Cursor cursor = null;
		try {
		 cursor = db.query("user", new String[]{"id","name","age"}, "id = " + id, null, null, null, null);
			while(cursor.moveToNext()){
				int id2 = cursor.getInt(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int age = cursor.getInt(cursor.getColumnIndex("age"));
				bean = new UserBean();
				bean.setId(id2);
				bean.setAge(age);
				bean.setName(name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return bean;
	}

}
