package com.iwork.plugs.meeting.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
public class UtilDate {
	private static Logger logger = Logger.getLogger(UtilDate.class);
	public static long getTimes(String text) {
		return getTimes(text, "yyyy-MM-dd");
	}

	public static long getTimes(String text, String format) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(format);
		try {
			return datetimeFormat.parse(text).getTime();
		} catch (Exception e) {logger.error(e,e);
		}
		return 0L;
	}

	public static String yearFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy");
		return datetimeFormat.format(date);
	}

	public static String yearFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String monthFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM");
		return datetimeFormat.format(date);
	}

	public static String monthFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String dayFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd");
		return datetimeFormat.format(date);
	}

	public static String dayFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String datetimeFormat(Date date) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(date);
	}

	public static String datetimeFormat24(Date date) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(date);
	}

	public static String datetimeFormat2(Date date) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		return datetimeFormat.format(date);
	}

	public static String datetimeFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}

		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String datetimeFormat24(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}

		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String datetimeFormat2(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}

		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String dateFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		return datetimeFormat.format(date);
	}

	public static String dateFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static String monthDayFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM-dd");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	public static int getDayOfWeek() {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		return getDayOfWeek(gregorianCalendar.get(1),
				gregorianCalendar.get(2) + 1, gregorianCalendar.get(5));
	}

	public static int getDayOfWeek(int year, int month, int day) {
		month--;
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.set(year, month, day);
		return gregorianCalendar.get(7);
	}

	public static String getDayOfWeekSymbols() {
		return new java.text.DateFormatSymbols().getWeekdays()[getDayOfWeek()];
	}

	public static String getDayOfWeekSymbols(int year, int month, int day) {
		return new java.text.DateFormatSymbols().getWeekdays()[getDayOfWeek(
				year, month, day)];
	}

	public static int getMaxDayOfMonth() {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		return getMaxDayOfMonth(gregorianCalendar.get(1),
				gregorianCalendar.get(2) + 1);
	}

	public static int getMaxDayOfMonth(int year, int month) {
		month--;
		Calendar gregorianCalendar1 = GregorianCalendar.getInstance();
		Calendar gregorianCalendar2 = GregorianCalendar.getInstance();
		for (int i = 31; i > 27; i--) {
			gregorianCalendar1.set(year, month, i);
			gregorianCalendar2.set(year, month, 1);
			if (gregorianCalendar1.get(2) == gregorianCalendar2.get(2)) {
				return i;
			}
		}
		return 0;
	}

	public static int getWeekOfYear() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		return getWeekOfYear(gregorianCalendar.get(1),
				gregorianCalendar.get(2) + 1, gregorianCalendar.get(5));
	}

	public static int getWeekOfYear(int year, int month, int day) {
		month--;
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.set(year, month, day);
		return gregorianCalendar.get(3);
	}

	public static int getQuarterOfYear(int month) {
		if ((month > 0) && (month < 4))
			return 1;
		if ((month > 3) && (month < 7))
			return 2;
		if ((month > 6) && (month < 10)) {
			return 3;
		}
		return 4;
	}

	public static int getQuarterOfYear() {
		return getQuarterOfYear(getMonth(new Date()));
	}

	public static int getYear(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(1);
	}

	public static int getHour(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);

		return gregorianCalendar.get(11);
	}

	public static int getMonth(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(2) + 1;
	}

	public static int getDay(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(5);
	}

	public static int getHourOfDay(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(11);
	}

	public static int getMinute(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(12);
	}

	public static void main(String[] args) {
		String s = "AAA|BBA|";
	}
}