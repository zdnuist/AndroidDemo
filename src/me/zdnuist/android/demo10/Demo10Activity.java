package me.zdnuist.android.demo10;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;

/**
 * 监控媒体按键
 * @author zdnuist
 *
 */
public class Demo10Activity extends BaseActivity implements OnClickListener{
	
	Button open,close;
	TextView show;
	
	AudioManager audioManager;
	
	ComponentName componentName;
	
	PackageManager pm;
	
	private static Demo10Activity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo10);
		
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		pm = getPackageManager();
		
		open = (Button) findViewById(R.id.btn_open);
		close = (Button) findViewById(R.id.btn_close);
		
		open.setOnClickListener(this);
		close.setOnClickListener(this);
		
		show = (TextView) findViewById(R.id.tv_show_log);
		
		componentName = new ComponentName(this,MediaButtonReceiver.class);
		
		instance = this;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_open:
			audioManager.registerMediaButtonEventReceiver(componentName);
			pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
			break;
		case R.id.btn_close:
			audioManager.unregisterMediaButtonEventReceiver(componentName);
			pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
			break;
		}
		
	}
	
	public static Demo10Activity getInstance(){
		return instance;
	}

	
	static class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			int what = msg.what;
			switch(what){
			case 0x0001:
				String log = (String) msg.obj;
				Demo10Activity.getInstance().show.setText(log);
				break;
			}
		}
		
	}
}
