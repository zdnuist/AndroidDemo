package me.zdnuist.android;

import me.zdnuist.android.demo1.Demo1Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_main);
		findViewById(R.id.btn_1).setOnClickListener(this);
	}

	Intent intent;
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_1:
			intent = new Intent(this,Demo1Activity.class);
			startActivity(intent);
			break;
		}
	}

}
