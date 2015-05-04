package me.zdnuist.android.demo4.dao;

import me.zdnuist.android.demo4.model.UserBean;

public interface IUserDao {

	public void save(String name , int age);
	
	public UserBean query(int id);
}
