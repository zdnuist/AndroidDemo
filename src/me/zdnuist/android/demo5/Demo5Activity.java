package me.zdnuist.android.demo5;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.MainActivity;
import me.zdnuist.android.R;

/**
 * 通过PackageManager.setComponentEnabledSetting() 
 * 显示或隐藏应用图标 
 * 
 * 如何关闭组件？
 * 关闭组件其实并不难，只要创建packageManager对象和ComponentName对象
 * ，并调用packageManager对象的setComponentEnabledSetting方法。
 * 
 * 
 * public void setComponentEnabledSetting (ComponentName componentName, int
 * newState, int flags)
 * 
 * componentName：组件名称 newState：组件新的状态，可以设置三个值，分别是如下：
 * 不可用状态：COMPONENT_ENABLED_STATE_DISABLED 可用状态：COMPONENT_ENABLED_STATE_ENABLED
 * 默认状态：COMPONENT_ENABLED_STATE_DEFAULT flags:行为标签，值可以是DONT_KILL_APP或者0。
 * 0说明杀死包含该组件的app public int getComponentEnabledSetting(ComponentName
 * componentName)
 * 
 * 获取组件的状态
 * 
 * 参考:http://blog.csdn.net/mingli198611/article/details/17269355/
 * 
 * @author zdnuist
 * 
 */
public class Demo5Activity extends BaseActivity implements OnClickListener {

	PackageManager pm;

	ComponentName componentName;

	Button show, hide;
	
	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo5);

		show = (Button) findViewById(R.id.app_show);
		hide = (Button) findViewById(R.id.app_hide);

		show.setOnClickListener(this);
		hide.setOnClickListener(this);

		pm = getPackageManager();

		componentName = new ComponentName(this,MainActivity.class);
		int res = pm.getComponentEnabledSetting(componentName);

		if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
				|| res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			// 图标已经显示,不需要再次显示
			show.setClickable(false);
		} else {
			hide.setClickable(false);
		}
		
		/**
		 * 20s之后自动显示
		 */
		mHandler.postDelayed(new Runnable(){

			@Override
			public void run() {
				pm.setComponentEnabledSetting(componentName,
						PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
						PackageManager.DONT_KILL_APP);
				show.setClickable(false);
				hide.setClickable(true);
			}
			
		}, 20*1000);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.app_show:
			pm.setComponentEnabledSetting(componentName,
					PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
					PackageManager.DONT_KILL_APP);
			show.setClickable(false);
			hide.setClickable(true);
			
			break;
		case R.id.app_hide:
			pm.setComponentEnabledSetting(componentName,
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
			show.setClickable(true);
			hide.setClickable(false);
			break;
		}
	}

}
