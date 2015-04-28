package me.zdnuist.android.demo1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class Demo1_3Service extends Service {

	private ExecutorService threadPool;

	boolean flag;

	private CallbackListener listener;

	public void registerListener(CallbackListener listener) {
		this.listener = listener;
	}

	/**
	 * 返回一个Binder对象
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return new MsgBinder();
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public Demo1_3Service getService() {
			return Demo1_3Service.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		threadPool = Executors.newSingleThreadExecutor();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void doTack(){
		threadPool.execute(task);
	}

	int i = 0;

	Runnable task = new Runnable() {

		@Override
		public void run() {
			while (!flag) {
				listener.start(i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
				if (i > 100)
					i = 0;
			}
		}

	};
}
