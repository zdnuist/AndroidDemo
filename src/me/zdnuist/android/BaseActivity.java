package me.zdnuist.android;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState,int layResId) {
		super.onCreate(savedInstanceState);
		setContentView(layResId);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
