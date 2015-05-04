package me.zdnuist.android.demo4;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import me.zdnuist.android.demo4.presenter.IUserPresenter;
import me.zdnuist.android.demo4.presenter.UserPresenterImpl;
import me.zdnuist.android.demo4.view.IUserView;

public class Demo4Activity extends BaseActivity implements IUserView,OnClickListener{
	
	EditText id,name,age;
	
	TextView show;
	
	private IUserPresenter userPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo4);
		
		id = (EditText) findViewById(R.id.et_id);
		name = (EditText) findViewById(R.id.et_name);
		age = (EditText) findViewById(R.id.et_age);
		
		show = (TextView) findViewById(R.id.tv_result);
		
		userPresenter = new UserPresenterImpl(this, Demo4Activity.this);
		findViewById(R.id.btn_insert).setOnClickListener(this);
		findViewById(R.id.btn_query).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_query:
			userPresenter.loadUser(getId());
			break;
		case R.id.btn_insert:
			userPresenter.saveUser(getUserName(), getUserAge());
			break;
		}
	}

	@Override
	public String getUserName() {
		String n = name.getText().toString();
		return n;
	}

	@Override
	public int getUserAge() {
		int a = Integer.valueOf(age.getText().toString());
		return a;
	}

	@Override
	public void setUserInfo(String n, int a) {
		show.setText(n + "  "+ a);
	}

	@Override
	public int getId() {
		int d = Integer.valueOf(id.getText().toString());
		return d;
	}


}
