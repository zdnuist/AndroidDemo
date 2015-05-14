package me.zdnuist.android;

import me.zdnuist.android.demo1.Demo1Activity;
import me.zdnuist.android.demo2.NorthFinderActivity;
import me.zdnuist.android.demo3.AlarmManagerActivity;
import me.zdnuist.android.demo4.Demo4Activity;
import me.zdnuist.android.demo5.Demo5Activity;
import me.zdnuist.android.demo6.Demo6Activity;
import me.zdnuist.android.demo7.Demo7Activity;
import me.zdnuist.android.demo8.Demo8Activity;
import me.zdnuist.android.demo9.Demo9Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_main);
		findViewById(R.id.btn_1).setOnClickListener(this);
		findViewById(R.id.btn_2).setOnClickListener(this);
		findViewById(R.id.btn_3).setOnClickListener(this);
		findViewById(R.id.btn_4).setOnClickListener(this);
		findViewById(R.id.btn_5).setOnClickListener(this);
		findViewById(R.id.btn_6).setOnClickListener(this);
		findViewById(R.id.btn_7).setOnClickListener(this);
		findViewById(R.id.btn_8).setOnClickListener(this);
		findViewById(R.id.btn_9).setOnClickListener(this);
	}

	Intent intent;
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_1:
			intent = new Intent(this,Demo1Activity.class);
			startActivity(intent);
			break;
		case R.id.btn_2:
			intent = new Intent(this,NorthFinderActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_3:
			intent = new Intent(this,AlarmManagerActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_4:
			intent = new Intent(this,Demo4Activity.class);
			startActivity(intent);
			break;
		case R.id.btn_5:
			intent = new Intent(this,Demo5Activity.class);
			startActivity(intent);
			break;
		case R.id.btn_6:
			intent = new Intent(this,Demo6Activity.class);
			startActivity(intent);
			break;
		case R.id.btn_7:
			intent = new Intent(this,Demo7Activity.class);
			startActivity(intent);
			break;
		case R.id.btn_8:
			intent = new Intent(this,Demo8Activity.class);
			startActivity(intent);
			break;
		case R.id.btn_9:
			intent = new Intent(this,Demo9Activity.class);
			startActivity(intent);
			break;
		}
	}

}
