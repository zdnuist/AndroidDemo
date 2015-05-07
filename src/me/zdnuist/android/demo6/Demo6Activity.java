package me.zdnuist.android.demo6;

import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Demo6Activity extends BaseActivity implements OnClickListener{
	
	Button screenShot1,show1;
	ImageView toShow;
	
	Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo6);
		
		screenShot1 = (Button) findViewById(R.id.btn_screenshot1);
		show1 = (Button) findViewById(R.id.btn_show1);
		
		toShow = (ImageView) findViewById(R.id.iv_show);
		
		screenShot1.setOnClickListener(this);
		show1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_screenshot1:
			bitmap = ScreenShot.shootScreen(this);
			break;
		case R.id.btn_show1:
			if(bitmap != null){
				toShow.setImageBitmap(bitmap);
			}
			break;
		}
	}

}
