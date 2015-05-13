package me.zdnuist.android.demo9.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "Course")
public class CourseBean implements Comparable<CourseBean>{

	public CourseBean() {
	};

	public CourseBean(String name, String teacher, int weekDay,
			String address,int classes,String weekDelay) {
		this.name = name;
		this.teacher = teacher;
		this.weekDay = weekDay;
		this.address = address;
		this.weekDelay = weekDelay;
		this.classes = classes;
	}

	private int id;

	@Column(column = "name")
	private String name; // 课程名称

	@Column(column = "teacher")
	private String teacher;// 授课老师

	@Column(column = "start_time")
	private String startTime; // 上课时间

	@Column(column = "end_time")
	private String endTime; // 下课时间

	@Column(column = "week_delay")
	private String weekDelay; //持续周,比如：1-9

	@Column(column = "week_day")
	private int weekDay; // 周几上课

	@Column(column = "address")
	private String address; // 上课地点

	@Column(column = "tip_time")
	private String tipTime; // 提醒时间
	
	@Column(column = "classes")
	private int classes; //第几节课
	
	@Column(column = "number")
	private String number; //学号
	
	@Column(column = "year")
	private String year; //学年
	
	@Column(column = "term")
	private int term; //学期
	
	@Column(column = "status")
	private int status; //设置提醒状态
	
	@Column(column = "ring_url")
	private String ringUrl; //设置铃声url
	
	@Column(column = "repeat")
	private int  repeat; //设置是否重复提醒  (0:提醒一次    ；；；1:每周提醒)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTipTime() {
		return tipTime;
	}

	public void setTipTime(String tipTime) {
		this.tipTime = tipTime;
	}

	public String getWeekDelay() {
		return weekDelay;
	}

	public void setWeekDelay(String weekDelay) {
		this.weekDelay = weekDelay;
	}

	public int getClasses() {
		return classes;
	}

	public void setClasses(int classes) {
		this.classes = classes;
	}

	
	
	

	@Override
	public int compareTo(CourseBean another) {
		if(this.classes > another.classes){
			return 1;
		}else if(this.classes < another.classes){
			return -1;
		}
		return 0;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRingUrl() {
		return ringUrl;
	}

	public void setRingUrl(String ringUrl) {
		this.ringUrl = ringUrl;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	
	
}
