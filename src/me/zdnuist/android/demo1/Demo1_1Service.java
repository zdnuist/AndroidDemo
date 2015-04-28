package me.zdnuist.android.demo1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 通过广播的方式与Activity交互
 * @author zdnuist
 *
 */
public class Demo1_1Service extends Service{
	
	private ExecutorService threadPool;
	
	boolean flag;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		threadPool = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			String action = intent.getAction();
			if(action.equals(Demo1Constant.ACTION_START)){
				flag = false;
				threadPool.execute(task);
			}else if(action.equals(Demo1Constant.ACTION_PAUSE)){
				flag = true;
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	int i = 0;

	Runnable task = new Runnable(){

		@Override
		public void run() {
			Intent intent = new Intent();
			intent.setAction(Demo1Constant.ACTION_START);
			while(!flag){
				intent.putExtra(Demo1Constant.PROGRESS_INT, i++);
				sendBroadcast(intent);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(i>100) i =0;
			}
		}
		
	};
}
