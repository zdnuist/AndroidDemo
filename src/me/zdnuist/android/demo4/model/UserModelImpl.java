package me.zdnuist.android.demo4.model;

import me.zdnuist.android.demo4.dao.IUserDao;
import me.zdnuist.android.demo4.dao.UserDaoImpl;
import android.content.Context;

public class UserModelImpl implements IUserModel{
	
	
	private IUserDao userDao;
	
	public UserModelImpl(Context context){
		userDao = new UserDaoImpl(context);
	}


	@Override
	public UserBean read(int id) {
		return userDao.query(id);
	}


	@Override
	public void save(String name, int age) {
		userDao.save(name,age);
	}

}
