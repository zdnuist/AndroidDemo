package me.zdnuist.android.demo4.presenter;

import android.content.Context;
import me.zdnuist.android.demo4.model.IUserModel;
import me.zdnuist.android.demo4.model.UserBean;
import me.zdnuist.android.demo4.model.UserModelImpl;
import me.zdnuist.android.demo4.view.IUserView;

public class UserPresenterImpl implements IUserPresenter{
	
	private IUserModel userModel;
	private IUserView userView;
	
	public UserPresenterImpl(IUserView userView,Context context){
		this.userView = userView;
		userModel = new UserModelImpl(context);
	}

	@Override
	public void saveUser(String name, int age) {
		userModel.save(name, age);
	}

	@Override
	public void loadUser(int id) {
		UserBean bean = userModel.read(id);
		userView.setUserInfo(bean.getName(), bean.getAge());
	}

}
