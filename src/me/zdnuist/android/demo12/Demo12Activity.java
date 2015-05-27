package me.zdnuist.android.demo12;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.MainActivity;
import me.zdnuist.android.R;

/**
 * 创建桌面快捷方式 shortCut
 * @author zdnuist
 *
 */
public class Demo12Activity extends BaseActivity implements OnClickListener{
	
	Button make1,delete1,make2,delete2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_demo12);
		
		make1 = (Button) findViewById(R.id.btn_make1);
		make2 = (Button) findViewById(R.id.btn_make2);
		delete1 = (Button) findViewById(R.id.btn_delete1);
		delete2 = (Button) findViewById(R.id.btn_delete2);
		
		make1.setOnClickListener(this);
		make2.setOnClickListener(this);
		
		delete1.setOnClickListener(this);
		delete2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_make1:
			AndroidUtils.makeShortcut(this, MainActivity.class, R.string.app_name);
			break;
		case R.id.btn_make2:
			AndroidUtils.deleteShotCut(this, MainActivity.class, R.string.app_name);
			break;
		case R.id.btn_delete1:
			
			break;
		case R.id.btn_delete2:
			break;
		}
	}

}
