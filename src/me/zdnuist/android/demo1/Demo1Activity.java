package me.zdnuist.android.demo1;

import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

/**
 * Activity与Service相互通信方式 
 * 1.通过广播方式进行通信 
 * 2.通过bindService的 Messenger handler进行通信
 * 3.通过bindService的接口回调方式进行通信
 * @author zdnuist
 * 
 */
public class Demo1Activity extends BaseActivity implements OnClickListener {

	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_demo1);
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_pause).setOnClickListener(this);
		
		findViewById(R.id.btn_bind1).setOnClickListener(this);
		findViewById(R.id.btn_start1).setOnClickListener(this);
		findViewById(R.id.btn_pause1).setOnClickListener(this);
		
		findViewById(R.id.btn_bind2).setOnClickListener(this);
		findViewById(R.id.btn_start2).setOnClickListener(this);
		findViewById(R.id.btn_pause2).setOnClickListener(this);

		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Demo1Constant.ACTION_START);
		registerReceiver(receiver, filter);
	}

	Intent intent;

	boolean isBind1;
	boolean isBind2;
	Message msg;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			intent = new Intent(this, Demo1_1Service.class);
			intent.setAction(Demo1Constant.ACTION_START);
			startService(intent);
			break;
		case R.id.btn_pause:
			intent = new Intent(this, Demo1_1Service.class);
			intent.setAction(Demo1Constant.ACTION_PAUSE);
			startService(intent);
			break;
		case R.id.btn_bind1:
			if (!isBind1) {
				intent = new Intent(this, Demo1_2Service.class);
				bindService(intent, serviceConnection1, BIND_AUTO_CREATE);
				isBind1 = true;
			}
			break;
		case R.id.btn_start1:
			msg = mHandler.obtainMessage();
			msg.what = 0x101;
			try {
				if(remoteMessenger!=null)
				remoteMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			break;
		case R.id.btn_pause1:
			msg = mHandler.obtainMessage();
			msg.what = 0x102;
			try {
				if(remoteMessenger!=null)
				remoteMessenger.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case R.id.btn_bind2:
			if(!isBind2){
				intent = new Intent(this,Demo1_3Service.class);
				bindService(intent, serviceConnection2, BIND_AUTO_CREATE);
				isBind2 = true;
			}
			break;
		case R.id.btn_start2:
			if(service3 != null){
				service3.flag = false;
				service3.doTack();
			}
			break;
		case R.id.btn_pause2:
			if(service3 != null)
				service3.flag = true;
			break;
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (action.equals(Demo1Constant.ACTION_START)) {
					pb.setProgress(intent.getIntExtra(
							Demo1Constant.PROGRESS_INT, 0));
				}
			}
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(receiver);

		if (isBind1)
			unbindService(serviceConnection1);
		
		if(isBind2)
			unbindService(serviceConnection2);
	}

	private Messenger remoteMessenger;
	private Messenger localMessenger;
	ServiceConnection serviceConnection1 = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			remoteMessenger = new Messenger(service);
			localMessenger = new Messenger(mHandler);
			Message msg = mHandler.obtainMessage();
			msg.what = 0x100;
			msg.replyTo = localMessenger;
			try {
				remoteMessenger.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x201:// 开始
				pb.setProgress(msg.arg1);
				break;
			}
		};
	};
	
	Demo1_3Service service3;
	ServiceConnection serviceConnection2 = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			service3 = ((Demo1_3Service.MsgBinder)service).getService();  
			service3.registerListener(new CallbackListener(){

				@Override
				public void start(int progress) {
					pb.setProgress(progress);
				}
				
			});
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};
}
