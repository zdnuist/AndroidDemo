package me.zdnuist.android.demo9.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.zdnuist.android.demo9.db.CourseBean;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CourseParseUtils {
	
	
	public final static SparseArray<String> TIMEARRAY = new SparseArray<String>();
	static{
		TIMEARRAY.put(1, "8点15分");
		TIMEARRAY.put(3, "10点10分");
		TIMEARRAY.put(5, "13点20分");
		TIMEARRAY.put(7, "15点15分");
		TIMEARRAY.put(9, "18点30分");
	}
	


	public static List<List<CourseBean>> parseCourseFromJson(String json,String number,String year,int term) {

		List<List<CourseBean>>  list = new ArrayList<List<CourseBean>>();
		if(json == null) return list;
		
		List<CourseBean> dayList;
		CourseBean bean = null;
		// 解析一个星期的课表
		JSONArray results = JSONArray.parseArray(json);
		if(results == null) return list;
		for (int i = 0; i < results.size(); i++) {
			dayList = new ArrayList<CourseBean>();
			// 获取到每天的课程
			JSONObject dayCourses = (JSONObject) results.get(i);
			String week = "";
			switch (i) {
			case 0:
				week = "星期一";
				break;
			case 1:
				week = "星期二";
				break;
			case 2:
				week = "星期三";
				break;
			case 3:
				week = "星期四";
				break;
			case 4:
				week = "星期五";
				break;
			default:
				break;
			}

			if (!"".equals(week)) {
				// 获取一天中所有的课程
				JSONObject courses = dayCourses.getJSONObject(week);
				for (int j = 1; j <= 10; j++) {

					String classes = "";
					switch (j) {
					case 1:
						classes = "第一节";
						break;
					case 2:
						classes = "第二节";
						break;
					case 3:
						classes = "第三节";
						break;
					case 4:
						classes = "第四节";
						break;
					case 5:
						classes = "第五节";
						break;
					case 6:
						classes = "第六节";
						break;
					case 7:
						classes = "第七节";
						break;
					case 8:
						classes = "第八节";
						break;
					case 9:
						classes = "第九节";
						break;
					case 10:
						classes = "第十节";
						break;
					default:
						break;
					}

					if (!classes.equals("")) {
						String courseString = courses.getString(classes);
						if (!TextUtils.isEmpty(courseString)) {
							String[] courseArray = courseString.split("\\s+");
							if (courseArray.length >= 4) {
								bean = new CourseBean();
								bean.setName(courseArray[0]);
								bean.setWeekDelay(courseArray[1]);
								bean.setTeacher(courseArray[2]);
								bean.setAddress(courseArray[3]);
								bean.setWeekDay(i + 1);
								bean.setClasses(j);
								bean.setNumber(number);
								bean.setYear(year);
								bean.setTerm(term);
								bean.setStatus(CourseConstant.TIP_OFF);
								dayList.add(bean);
							}else if(courseArray.length == 3){
								bean = new CourseBean();
								bean.setName(courseArray[0]);
								bean.setTeacher(courseArray[1]);
								bean.setAddress(courseArray[2]);
								bean.setWeekDay(i + 1);
								bean.setClasses(j);
								bean.setNumber(number);
								bean.setYear(year);
								bean.setTerm(term);
								bean.setStatus(CourseConstant.TIP_OFF);
								dayList.add(bean);
							}else{
								bean = new CourseBean();
								bean.setName(courseString);
								bean.setTeacher("");
								bean.setAddress("");
								bean.setWeekDay(i + 1);
								bean.setClasses(j);
								bean.setNumber(number);
								bean.setYear(year);
								bean.setTerm(term);
								bean.setStatus(CourseConstant.TIP_OFF);
								dayList.add(bean);
							}
						}else{
							bean = new CourseBean();
							bean.setName(null);
							bean.setWeekDay(i + 1);
							bean.setClasses(j);
							bean.setNumber(number);
							bean.setYear(year);
							bean.setTerm(term);
							bean.setStatus(CourseConstant.TIP_OFF);
							dayList.add(bean);
						}
						
						
					}
					
					
				}
				list.add(dayList);
			}
		}

		return list;

	}
	
	public  static List<CourseBean> sort(List<CourseBean> beanList){
		CourseBean[]  array = new CourseBean[]{};
		 array = beanList.toArray(array);
		Arrays.sort(array);
		beanList = new ArrayList<CourseBean>(Arrays.asList(array));
		return beanList;
		
	}

}
