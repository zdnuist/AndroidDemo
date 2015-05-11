package me.zdnuist.android.demo7;

import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Demo7Activity extends BaseActivity implements OnClickListener{
	
	private Button add;
	private Button reduce;
	
	int nowBrightnessValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo7);
		
		add = (Button) findViewById(R.id.btn_add);
		reduce = (Button) findViewById(R.id.btn_reduce);
		
		add.setOnClickListener(this);
		reduce.setOnClickListener(this);
		
		//设置为手动调节亮度
		if(BrightnessUtil.isAutoBrightness(this)){
			BrightnessUtil.stopAutoBrightness(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_add:
			nowBrightnessValue = BrightnessUtil.getScreenBrightness(this);
			nowBrightnessValue += 15;
			BrightnessUtil.setBrightness(this, nowBrightnessValue);
			break;
		case R.id.btn_reduce:
			nowBrightnessValue = BrightnessUtil.getScreenBrightness(this);
			nowBrightnessValue -= 15;
			BrightnessUtil.setBrightness(this, nowBrightnessValue);
			break;
		}
	}

}
