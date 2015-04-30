package me.zdnuist.android.demo3;

import java.util.Calendar;

import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.TimePicker;

/**
 * 通过AlarmManger实现一个闹铃的功能
 *  1.设定未来某个时间的闹铃 
 *  2.设定一个重复响应的闹铃
 * 
 * @author zdnuist
 * 
 */
public class AlarmManagerActivity extends BaseActivity implements
		OnScrollListener {

	public static final String TAG = "AlarmManagerActivity";

	public static final String INTENT_ACTION_1 = "alarm_action_1";

	private AlarmManager alarmManager;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Button set1, set2;
	private PendingIntent mPendingIntent;

	private NumberPicker numberPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_alarm);

		/**************************************************************/
		//初始化
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true); // 使用24小时制

		set1 = (Button) findViewById(R.id.btn_set1);
		set2 = (Button) findViewById(R.id.btn_set2);

		numberPicker = (NumberPicker) findViewById(R.id.numberPicker1);
		numberPicker.setMaxValue(60); //设置数字的最大值
		numberPicker.setMinValue(1); //设置数字的最小值
		numberPicker.setValue(5); //设置数字的默认值
		numberPicker.setOnScrollListener(this); //设定此接口，可以实现numberPicker的滚动选择
		/**************************************************************/
		//注册广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTENT_ACTION_1);
		registerReceiver(alarmReciver, filter);

		Intent intent = new Intent(INTENT_ACTION_1);
		mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
				intent, 0);
		
		/**************************************************************/

		set1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int h = timePicker.getCurrentHour();
				int min = timePicker.getCurrentMinute();
				int mon = datePicker.getMonth();
				int d = datePicker.getDayOfMonth();

				Log.d(TAG, "datetime:" + (mon + 1) + "-" + d + " " + h + ":"
						+ min);

				Calendar calendar1 = Calendar.getInstance();
				calendar1.set(2015, mon, d, h, min);

				//设定指定时间的闹钟
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar1.getTimeInMillis(), mPendingIntent);
			}

		});

		set2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int num = numberPicker.getValue(); // 设置分钟时间
				Log.d(TAG, "num:" + num);

				//设定间隔分钟的闹钟
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(), num * 60 * 1000,
						mPendingIntent);
			}

		});
	}

	/**
	 * 广播接收器
	 */
	BroadcastReceiver alarmReciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent != null && INTENT_ACTION_1.equals(intent.getAction())) {
				Log.d(TAG, "receiver");
			}

		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (alarmReciver != null) {
			unregisterReceiver(alarmReciver);
			alarmReciver = null;
		}

		if (alarmManager != null) {
			alarmManager.cancel(mPendingIntent);
		}

	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {

	};

}
