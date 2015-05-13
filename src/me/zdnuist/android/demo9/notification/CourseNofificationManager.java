package me.zdnuist.android.demo9.notification;

import java.io.IOException;

import me.zdnuist.android.R;
import me.zdnuist.android.demo9.service.CourseService;
import me.zdnuist.android.demo9.util.CourseConstant;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class CourseNofificationManager {

	private static final String TAG = "CourseNofificationManager";
	private static CourseNofificationManager instance = null;
	public static final int NOTIFYCATION_TIP_ID = 0x0001;

	private Notification notification;
	private NotificationManager mNotificationManager;

	private RemoteViews remoteViews;

	private MediaPlayer mMediaPlayer;

	private CourseNofificationManager() {
		mMediaPlayer = new MediaPlayer();
	}

	public static CourseNofificationManager getInstance() {
		if (null == instance) {
			synchronized (CourseNofificationManager.class) {
				if (null == instance) {
					instance = new CourseNofificationManager();

				}
			}
		}
		return instance;
	}

	/**
	 * 短信解码
	 * 
	 * @param mContext
	 * @param tip
	 */
	public void showCourseNotifycation(Context mContext, String tip,
			String ringUrl) {
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		remoteViews = new RemoteViews(mContext.getPackageName(),
				R.layout.course_notification);

		remoteViews.setTextViewText(R.id.course_tip, tip);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);
		mBuilder.setSmallIcon(R.drawable.tips_ico).setContentIntent(null)
				.setContent(remoteViews);

		// 取消通知栏
		Intent cancelIntent = new Intent(mContext, CourseService.class);
		cancelIntent.putExtra(CourseConstant.OPERATING,
				CourseConstant.NOTIFICATION_CANCEL);
		PendingIntent cancelPi = PendingIntent.getService(mContext, 1,
				cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.course_cancel, cancelPi);

		notification = mBuilder.build();
		notification.contentView = remoteViews;
		notification.ledARGB = 0xFFFFFF; // 这里是颜色，我们可以尝试改变，理论上0xFF0000是红色
		notification.ledOnMS = 100;
		notification.ledOffMS = 100;
		notification.flags = Notification.FLAG_SHOW_LIGHTS;
		mNotificationManager.notify(NOTIFYCATION_TIP_ID, notification);

		playAlarmVoice(mContext, ringUrl);
	}

	/**
	 * 取消某个notifycation
	 * 
	 * @param notifycationId
	 */
	public void cancelNotifycation(int notifycationId) {
		if (null != mNotificationManager) {
			mNotificationManager.cancel(notifycationId);
		}
		stopAlarmRing();
	}

	public Notification getNotification() {
		return notification;
	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	/**
	 * 播放闹铃
	 * 
	 * @param context
	 */
	private void playAlarmVoice(Context context, String ringUrl) {
		Uri alert = null;
		try {
			if (ringUrl == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_ALARM);
			} else {
				alert = Uri.parse(ringUrl);
			}
			Log.d(TAG, alert.toString() + "...");
			mMediaPlayer.reset();
			try {
				mMediaPlayer.setDataSource(context, alert);
			} catch (Exception e) {
				alert = Uri.parse("content://settings/system/ringtone");
				try {
					mMediaPlayer.setDataSource(context, alert);
				} catch (Exception e1) {
					alert = RingtoneManager.getValidRingtoneUri(context);
					mMediaPlayer.setDataSource(context, alert);
				}
			}
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setLooping(false);
				mMediaPlayer.prepare();
			}
			mMediaPlayer.start();
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	/**
	 * 停止闹钟铃声
	 */
	public void stopAlarmRing() {
		try {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.setLooping(false);
				mMediaPlayer.stop();
			}
		} catch (Exception e) {
			System.out.println("mMediaPlayer can not stop");
		} finally {
			// mMediaPlayer.release();
		}
	}
}
