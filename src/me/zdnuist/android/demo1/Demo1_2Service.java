package me.zdnuist.android.demo1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public class Demo1_2Service extends Service {
	
	ExecutorService threadPool = Executors.newSingleThreadExecutor();
	

	private Messenger messenger;

	private Messenger cMessenger;

	private Message toMessage;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return messenger.getBinder();
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x100://初始化
				cMessenger = msg.replyTo;
				break;
			case 0x101: //开始
				flag = false;
				threadPool.execute(task);
				break;
			case 0x102://暂停
				flag = true;
				break;
			}

		};
	};
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		
		messenger = new Messenger(handler);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}
	
	int i = 0;
	boolean flag;
	Runnable task = new Runnable(){

		@Override
		public void run() {
			while(!flag){
				toMessage = handler.obtainMessage();
				toMessage.what = 0x201;
				toMessage.arg1 = i;
				
				try {
					cMessenger.send(toMessage);
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
				if(i>100) i =0;
			}
		}
		
	};
}
