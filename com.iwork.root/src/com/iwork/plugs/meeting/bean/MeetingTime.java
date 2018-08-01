package com.iwork.plugs.meeting.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
public class MeetingTime {
	private static Logger logger = Logger.getLogger(MeetingTime.class);
	private int year;
	private int month;
	private int day;
	private String week;
	private String weekinmonth;
	private String hour;
	private String minute;
	private String second;
	private String curdate;
	private String nextdate;
	private String predate;
	private String time1;
	private String time2;
	private String time3;

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return this.day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getWeek() {
		return this.week;
	}

	public void setWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.year, this.month - 1, this.day);
		String time = calendar.getTime().toLocaleString();
		this.week = getDayOfweek(calendar.get(7));
		setTime2();
	}

	public String getHour() {
		return this.hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return this.minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getSecond() {
		return this.second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getCurdate() {
		return this.curdate;
	}

	public void setCurdate(String curdate) {
		this.curdate = curdate;
		String[] dayString = curdate.split("-");
		setYear(Integer.parseInt(dayString[0]));
		setMonth(Integer.parseInt(dayString[1]));
		setDay(Integer.parseInt(dayString[2]));
		setWeek();
		setNextdate();
		setPredate();
	}

	public String getNextdate() {
		return this.nextdate;
	}

	public void setNextdate() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse(this.curdate);
			c.setTime(date);
		} catch (ParseException e) {
			logger.error(e,e);
		}

		c.add(5, 1);
		Date d2 = c.getTime();
		String nextdate = df.format(d2);
		this.nextdate = nextdate;
	}

	public String getPredate() {
		return this.predate;
	}

	public void setPredate() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse(this.curdate);
			c.setTime(date);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		c.add(5, -1);
		Date d2 = c.getTime();
		String predate = df.format(d2);
		this.predate = predate;
	}

	public String getTime1() {
		return this.time1;
	}

	public void setTime1(String time1) {
		this.time1 = time1;
		String[] timeString = time1.split(":");
		setHour(timeString[0]);
		setMinute(timeString[1]);
		setSecond(timeString[2]);
	}

	public String getTime2() {
		return this.time2;
	}

	public void setTime2() {
		this.time2 = (this.curdate + " " + this.week);
	}

	public String getTime3() {
		return this.time3;
	}

	public void setTime3(String time3) {
		this.time3 = time3;
		String[] dayString = time3.split(" ");
		setCurdate(dayString[0]);
		setTime1(dayString[1]);
		setWeek();
		setNextdate();
		setPredate();
	}

	public static String getDayOfweek(int i) {
		String week = "";
		switch (i) {
		case 1:
			week = "星期日";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
		}
		return week;
	}

	public String getNextWeek() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse(this.curdate);
			c.setTime(date);
		} catch (ParseException e) {
			logger.error(e,e);
		}

		c.add(5, 7);
		Date d2 = c.getTime();
		String nextdate = df.format(d2);
		return nextdate;
	}

	public String getNextMonth() {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse(this.curdate);
			c.setTime(date);
		} catch (ParseException e) {
			logger.error(e,e);
		}

		c.add(2, 1);
		c.set(5, 1);
		Date d2 = c.getTime();
		String nextmonth = df.format(d2);
		return nextmonth;
	}
}