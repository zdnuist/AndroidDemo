package me.zdnuist.android.demo9;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import me.zdnuist.android.demo9.db.CourseBean;
import me.zdnuist.android.demo9.fragment.CourseTipFragment;
import me.zdnuist.android.demo9.fragment.CourseTipFragment.TipListener;
import me.zdnuist.android.demo9.service.CourseService;
import me.zdnuist.android.demo9.util.CourseConstant;
import me.zdnuist.android.demo9.util.CourseParseUtils;
import me.zdnuist.android.demo9.util.MediaUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class Demo9Activity extends BaseActivity implements TipListener {
	public static final String TAG = "NewCourseTableActivity";

	private int colors[] = { Color.rgb(0xee, 0xff, 0xff),
			Color.rgb(0xf0, 0x96, 0x09), Color.rgb(0x8c, 0xbf, 0x26),
			Color.rgb(0x00, 0xab, 0xa9), Color.rgb(0x99, 0x6c, 0x33),
			Color.rgb(0x3b, 0x92, 0xbc), Color.rgb(0xd5, 0x4d, 0x34),
			Color.rgb(0xcc, 0xcc, 0xcc) };

	LinearLayout ll1;
	LinearLayout ll2;
	LinearLayout ll3;
	LinearLayout ll4;
	LinearLayout ll5;
	LinearLayout ll6;
	LinearLayout ll7;

	DbUtils db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_course_table);
		db = DbUtils.create(this, CourseConstant.DB_NAME);
		db.configAllowTransaction(true);
		// 分别表示周一到周日
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll4 = (LinearLayout) findViewById(R.id.ll4);
		ll5 = (LinearLayout) findViewById(R.id.ll5);
		ll6 = (LinearLayout) findViewById(R.id.ll6);
		ll7 = (LinearLayout) findViewById(R.id.ll7);

		initIntent();
		initData();

		Intent intent = new Intent(this, CourseService.class);
		startService(intent);

		MediaUtils.scanMediaFiles(Demo9Activity.this);
	}

	private String name;
	private String num;
	private String method;
	private String year;
	private String term;

	private void initIntent() {
		// TODO Auto-generated method stub
		Intent intent = this.getIntent();
		year = intent.getStringExtra("year");
		term = intent.getStringExtra("term");
		method = intent.getStringExtra("method");
		if ("GetClassByWhere".equals(method)) {
			num = intent.getStringExtra("number");
		} else if ("GetTeacherClassByWhere".equals(method)) {
			name = intent.getStringExtra("name");
		}
		String character = "学生";
		if ("学生".equals(character)) {
			params.put("number", num);
		} else if ("教师".equals(character)) {
			params.put("name", name);
		}
		params.put("year", year);
		params.put("term", term);
	}

	private void initData() {
		// TODO Auto-generated method stub
		try {
			if (db.count(Selector.from(CourseBean.class)
					.where("number", "=", params.get("number"))
					.and("year", "=", params.get("year"))
					.and("term", "=", params.get("term"))) > 0) {
				for (int i = 1; i <= 5; i++) {
					List<CourseBean> dayList = db.findAll(Selector
							.from(CourseBean.class).where("week_day", "=", i)
							.and("number", "=", params.get("number"))
							.and("year", "=", params.get("year"))
							.and("term", "=", params.get("term")));
					setCourseInView(dayList, dayList.size());
				}

			} else {
				getZYandKB(method);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Map<String, String> params = new HashMap<String, String>();

	List<List<CourseBean>> courseList;

	/** 获取专业和课表 */
	private void getZYandKB(String method) {

		String result = "[{'星期一':{'第六节':'外贸英语函电 01-13,15-18 朱莉蓉 敏德楼311','第八节':'国际商法 10-13,15-18 江静波 敏德楼401','第一节':'','第二节':'','第三节':'','第四节':'','第五节':'','第七节':'','第九节':'','第十节':'','第十一节':'','第十二节':''}},{'星期二':{'第一节':'国际金融 01-09 薛芳 G101','第三节':'报关实务 01-13,15-18 庄国娅 G102','第六节':'毛泽东思想和中国特色理论 01-13,15-16 杨玲 敏德楼314','第六节':'毛泽东思想和中国特色理论 01-13,15-16 杨玲 敏德楼314','第九节':'瑜珈  李丽 形体馆','第二节':'','第四节':'','第五节':'','第七节':'','第八节':'','第十节':'','第十一节':'','第十二节':''}},{'星期三':{'第一节':'国际经济合作 01-13,15-18 张伯辉 敏德楼408','第三节':'国际金融 01-09 薛芳 敏德楼409','第三节':'国际商法 10-13,15-18 江静波 敬德楼409','第二节':'','第四节':'','第五节':'','第六节':'','第七节':'','第八节':'','第九节':'','第十节':'','第十一节':'','第十二节':''}},{'星期四':{'第一节':'国际商法 10-13,15-18 江静波 G201','第一节':'国际经济合作 01-13,15-18 张伯辉 敏德楼311','第六节':'外贸英语函电 01-13,15-18 朱莉蓉 敏德楼405','第六节':'国际金融 01-09 薛芳 敏德楼313','第二节':'','第三节':'','第四节':'','第五节':'','第七节':'','第八节':'','第九节':'','第节':'','第十一节':'','第十二节':''}},{'星期五':{'第一节':'国际结算 01-13,15-18 王慧谦 敏德楼410','第三节':'报关实务 01-13,15-18 庄国娅 敏德楼313','第二节':'','第四节':'','第五节':'','第六节':'','第七节':'','第八节':'','第九节':'','第十节':'','第十一节':'','第十二节':''}},{'星期六':{'第二节':'','第三节':'','第四节':'','第五节':'','第六节':'','第七节':'','第八节':'','第九节':'','第十节':'','第十一节':'','第十二节':''}},{'星期七':{'第二节':'','第三节':'','第四节':'','第五节':'','第六节':'','第七节':'','第八节':'','第九节':'','第十节':'','第十一节':'','第十二节':''}}]";
		// 回调处理请求返回数据
		courseList = CourseParseUtils.parseCourseFromJson(result,
				params.get("number"), params.get("year"),
				Integer.parseInt(params.get("term")));
		if (courseList.size() > 0) {
			// 一天有10节课 有课的填充 没课的为空
			for (List<CourseBean> dayList : courseList) {
				// 设置每天课程
				int size = dayList.size();
				if (size > 0) {
					try {
						db.saveAll(dayList);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// setCourseInView(dayList,size);
			}
		}

		try {
			if (db.count(Selector.from(CourseBean.class)
					.where("number", "=", params.get("number"))
					.and("year", "=", params.get("year"))
					.and("term", "=", params.get("term"))) > 0) {
				for (int i = 1; i <= 5; i++) {
					List<CourseBean> dayList = db.findAll(Selector
							.from(CourseBean.class).where("week_day", "=", i)
							.and("number", "=", params.get("number"))
							.and("year", "=", params.get("year"))
							.and("term", "=", params.get("term")));
					setCourseInView(dayList, dayList.size());
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	int classes;

	/**
	 * 设置课程的方法
	 * 
	 * @param ll
	 * @param title
	 *            课程名称
	 * @param place
	 *            地点
	 * @param last
	 *            周次
	 * @param time
	 *            时间
	 * @param classes
	 *            节数
	 * @param color
	 *            背景色
	 */
	void setClass(LinearLayout ll, String title, String place, String last,
			String time, int classes, int color, String teacher, int id,
			int status, int repeatStatus, String ringUrl) {
		View view = LayoutInflater.from(this).inflate(R.layout.course_item,
				null);
		view.setMinimumHeight(dip2px(this, classes * 48));
		view.setBackgroundColor(colors[color]);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.place)).setText(place);
		((TextView) view.findViewById(R.id.last)).setText(last);
		((TextView) view.findViewById(R.id.time)).setText(time);
		((TextView) view.findViewById(R.id.teacher)).setText(teacher);
		// 为课程View设置点击的监听器
		view.setTag(id + "#" + status + "#" + repeatStatus + "#" + ringUrl);

		view.setOnClickListener(new OnClickClassListener());
		TextView blank1 = new TextView(this);
		TextView blank2 = new TextView(this);
		blank1.setHeight(dip2px(this, classes));
		blank2.setHeight(dip2px(this, classes));
		ll.addView(blank1);
		ll.addView(view);
		ll.addView(blank2);
	}

	/**
	 * 设置无课（空百）
	 * 
	 * @param ll
	 * @param classes
	 *            无课的节数（长度）
	 * @param color
	 */
	void setNoClass(LinearLayout ll, int classes, int color) {
		TextView blank = new TextView(this);
		if (color == 0)
			blank.setMinHeight(dip2px(this, classes * 50));
		// blank.setBackgroundColor(colors[color]);
		ll.addView(blank);
	}

	// 点击课程的监听器
	class OnClickClassListener implements OnClickListener {

		public void onClick(View v) {
			String tag = v.getTag() + "";

			CourseTipFragment framgent = new CourseTipFragment(
					Integer.valueOf(tag.split("#")[0]), db, Integer.valueOf(tag
							.split("#")[1]),
					Integer.valueOf(tag.split("#")[2]), tag.split("#")[3]);
			framgent.show(getFragmentManager(), "tip_frament");
		}
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	private LinearLayout parseWeekDay(int weekDay) {
		switch (weekDay) {
		case 1:
			return ll1;
		case 2:
			return ll2;
		case 3:
			return ll3;
		case 4:
			return ll4;
		case 5:
			return ll5;
		case 6:
			return ll6;
		case 7:
			return ll7;
		}
		return null;
	}

	/**
	 * 界面上显示课表
	 * 
	 * @param dayList
	 * @param size
	 */
	private void setCourseInView(List<CourseBean> dayList, int size) {
		for (int i = 0; i < size; i++) { // 设置10次
			final CourseBean bean = dayList.get(i);
			final int j = i;
			if (null == bean.getName()) {

				// 如果前一节课为有课，则不画
				if (classes != 0 && bean.getClasses() == classes + 1) {
					classes = 0;
					continue;
				}

				Demo9Activity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setNoClass(parseWeekDay(bean.getWeekDay()), 1, 0);

					}

				});
			} else {

				Log.d(TAG, "name:" + bean.getName());
				classes = bean.getClasses();
				Demo9Activity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setClass(parseWeekDay(bean.getWeekDay()),
								bean.getName(), bean.getAddress(),
								bean.getWeekDelay(), "", 2,
								j > colors.length - 1 || j == 0 ? 4 : j,
								bean.getTeacher(), bean.getId(),
								bean.getStatus(), bean.getRepeat(),
								bean.getRingUrl());
					}

				});
			}
		}
	}

	@Override
	public void onComplete() {
		ll1.removeAllViewsInLayout();
		ll2.removeAllViewsInLayout();
		ll3.removeAllViewsInLayout();
		ll4.removeAllViewsInLayout();
		ll5.removeAllViewsInLayout();
		ll6.removeAllViewsInLayout();
		ll7.removeAllViewsInLayout();

		try {
			if (db.count(Selector.from(CourseBean.class)
					.where("number", "=", params.get("number"))
					.and("year", "=", params.get("year"))
					.and("term", "=", params.get("term"))) > 0) {
				for (int i = 1; i <= 5; i++) {
					List<CourseBean> dayList = db.findAll(Selector
							.from(CourseBean.class).where("week_day", "=", i)
							.and("number", "=", params.get("number"))
							.and("year", "=", params.get("year"))
							.and("term", "=", params.get("term")));
					setCourseInView(dayList, dayList.size());
				}

			} else {
				getZYandKB("GetClassByWhere");
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
