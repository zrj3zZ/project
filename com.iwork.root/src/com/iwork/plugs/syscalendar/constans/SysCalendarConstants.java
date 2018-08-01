package com.iwork.plugs.syscalendar.constans;

import java.util.HashMap;
import java.util.Map;
/**
 * 系统工作日历常量类
 * @author WangJianhui
 *
 */
public class SysCalendarConstants {
	//星期
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDSDAY = 3;
	public static final int TURSDAY = 4;
	public static final int FRIDAY = 5;
	public static final int SATDAY = 6;
	public static final int SUNDAY = 7;
	//月份
	public static final int JANUARY = 1;
	public static final int FEBRUARY = 2;
	public static final int MARCH = 3;
	public static final int APRIL = 4;
	public static final int MAY = 5;
	public static final int JUNE = 6;
	public static final int JULY = 7;
	public static final int AUGUST = 8;
	public static final int SEPTEMBER = 9;
	public static final int OCTOBER = 10;
	public static final int NOVEBER = 11;
	public static final int DECEBER = 12;
	
	//返回错误信息天数
	public static final int ERROR_DAYS = -111111111;
	
	//星期键值对
	public final static Map<String,Integer> WEEK_KEY = new HashMap<String, Integer>(){{
		put("MONDAY",1);
		put("TUESDAY",2);
		put("WEDSDAY",3);
		put("TURSDAY",4);
		put("FRIDAY",5);
		put("SATDAY",6);
		put("SUNDAY",7);
    }};
}
