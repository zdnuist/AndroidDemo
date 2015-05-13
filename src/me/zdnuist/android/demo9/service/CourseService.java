package me.zdnuist.android.demo9.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;






import me.zdnuist.android.demo9.db.CourseBean;
import me.zdnuist.android.demo9.notification.CourseNofificationManager;
import me.zdnuist.android.demo9.util.CourseConstant;
import me.zdnuist.android.demo9.util.CourseParseUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class CourseService extends Service {
	
	public static final String TAG = "CourseService";

	private TimeChangeReceiver timeChangeReceiver;
	
	private Handler mHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		registTimeChange();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(intent!=null)
		treateIntent(intent);
		return START_STICKY;
	}
	
	private void treateIntent(Intent intent){
		int perating = intent.getIntExtra(CourseConstant.OPERATING, -1);
		switch(perating){
		case CourseConstant.NOTIFICATION_CANCEL:
			CourseNofificationManager.getInstance().cancelNotifycation(CourseNofificationManager.NOTIFYCATION_TIP_ID);
			break;
		}
	}

	/**
	 * 注册计时广播
	 */
	private void registTimeChange() {
		timeChangeReceiver = new TimeChangeReceiver();
		IntentFilter interFilter = new IntentFilter(Intent.ACTION_TIME_TICK);

		interFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

		registerReceiver(timeChangeReceiver, interFilter);
	}

	/**
	 * 注销计时广播
	 */
	private void unRegisterTimeChange() {
		if (timeChangeReceiver != null) {
			unregisterReceiver(timeChangeReceiver);
			timeChangeReceiver = null;
		}
	}

	@Override
	public void onDestroy() {
		unRegisterTimeChange();
		super.onDestroy();

	}

	
	/**
	 * 计时广播
	 * @author zdnuist
	 *
	 */
	class TimeChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null && intent.getAction().equals(Intent.ACTION_TIME_TICK)){
				Calendar calendar = Calendar.getInstance();
				if(calendar == null) return;
				
				int weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
				if(weekDay == 0) weekDay = 7;
				int h = calendar.get(Calendar.HOUR_OF_DAY);
		    	int m = calendar.get(Calendar.MINUTE);
		    	int s = calendar.get(Calendar.SECOND);
				StringBuilder hour = new StringBuilder();
				StringBuilder min = new StringBuilder();
				StringBuilder sec = new StringBuilder();
				String ringUrl = null;
				if(h<10){
					hour.append("0").append(h);
				}else{
					hour.append(h);
				}
				
				if(m<10){
					min.append("0").append(m);
				}else{
					min.append(m);
				}
				
				if(s<10){
					sec.append("0").append(s);
				}else{
					sec.append(s);
				}
				String time = hour.append(min).append(sec).toString();
				Log.d(TAG,"time:"+ time);
				DbUtils db = DbUtils.create(context, CourseConstant.DB_NAME);
				List<CourseBean> list = new ArrayList<CourseBean>();
				try {
					List<CourseBean> list1 = db.findAll(Selector.from(CourseBean.class).where("week_day","=",weekDay)
							.and("tip_time","=",time)
							.and("status", "=", CourseConstant.TIP_ON)
							.and("repeat","=",CourseConstant.REPEAT_ON)
							);
					List<CourseBean> list2 = db.findAll(Selector.from(CourseBean.class).where("week_day","=",weekDay)
							.and("tip_time","=",time)
							.and("status", "=", CourseConstant.TIP_ON)
							.and("repeat","=",CourseConstant.REPEAT_OFF)
							);
					if(list1!=null) list.addAll(list1);
					if(list2!=null) list.addAll(list2);
					if(list.size()>0){
						StringBuilder sb = new StringBuilder();
						for(CourseBean bean : list){
							sb.append(CourseParseUtils.TIMEARRAY.get(bean.getClasses())+"、"+bean.getName()+"、"+bean.getTeacher()+"、"+bean.getAddress() +"\n");
						}
						ringUrl = list.get(list.size()-1).getRingUrl();
						CourseNofificationManager.getInstance().showCourseNotifycation(context, sb.toString(),ringUrl);
						mHandler.postDelayed(new Runnable(){
							
							@Override
							public void run() {
								CourseNofificationManager.getInstance().stopAlarmRing();
							}
							
						}, 60*1000);//默认闹钟提醒时长1分钟
						
						//如果只设定一次提醒，则闹铃提醒之后把闹铃状态设置为关闭
						if(list2!=null && list2.size() > 0){
							
							for(CourseBean bean : list2){
								bean.setStatus(CourseConstant.TIP_OFF);
							}
							
							db.updateAll(list2, "status");
						}
					}
					
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}

}
