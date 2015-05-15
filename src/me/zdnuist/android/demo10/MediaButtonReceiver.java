package me.zdnuist.android.demo10;

import me.zdnuist.android.demo10.Demo10Activity.MyHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * 响应媒体按键的广播
 * @author zdnuist
 *
 */
public class MediaButtonReceiver extends BroadcastReceiver {

	public static final String TAG = "MediaButtonReceiver";
	
	MyHandler handler = new MyHandler();

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent != null
				&& Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			
			String log = "action:" + event.getAction() + " , keyCode:"
					+ event.getKeyCode();
			Log.d(TAG, log);
			
			handler.obtainMessage(0x0001, log).sendToTarget();

			// 拦截，终止广播(不让别的程序收到此广播，免受干扰)
			// 调用该方法，会出现java.lang.RuntimeException: BroadcastReceiver trying to
			// return result during a non-ordered broadcast
			// 保证同时只会收到一个广播，避免同时收到多个广播播
			try {
				this.abortBroadcast();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
