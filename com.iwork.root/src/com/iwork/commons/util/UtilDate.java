

package com.iwork.commons.util;
import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class UtilDate {
	private static Logger logger = Logger.getLogger(UtilDate.class);
	/**
	 * 获得指定日期的时间
	 * 
	 * @param text
	 *            日期格式yyyy-MM-dd
	 * @return 时间措
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static long getTimes(String text) {
		return getTimes(text, "HH:mm:ss");
	}
	
	/**
	 * 获得当前日期及时间
	 * @return
	 */
	   public static String getNowDatetime()
	    {
	        Date dt = new Date();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return df.format(dt);
	    }
	   /**
	    * 获得格式化日期及时间
	    * @param format
	    * @return
	    */
	   public static String getNowFormatTime(String format)
	    {
	        Date dt = new Date();
	        SimpleDateFormat df = new SimpleDateFormat(format);
	        return df.format(dt);
	    }
	   /**
		 * 时间间隔计算
		 * 
		 */
		public static String getDaysBeforeNow(Date date) {
			if(date==null)return "";
			long sysTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			long ymdhms = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
			String strYear = "年前";
			String strMonth = "月前";
			String strDay = "天前";
			String strHour = "小时前";
			String strMinute = "分钟前";
			try {
				if (ymdhms == 0) {
					return "";
				}
				long between = (sysTime / 10000000000L) - (ymdhms / 10000000000L);
				if (between > 0) {
					return between + strYear;
				}
				between = (sysTime / 100000000L) - (ymdhms / 100000000L);
				if (between > 0) {
					return between+ strMonth;
				}
				between = (sysTime / 1000000L) - (ymdhms / 1000000L);
				if (between > 0) {
					return between + strDay;
				}
				between = (sysTime / 10000) - (ymdhms / 10000);
				if (between > 0) {
					return between + strHour;
				}
				between = (sysTime / 100) - (ymdhms / 100);
				if (between > 0) {
					return between + strMinute;
				}
				return "1" + strMinute;
			} catch (Exception e) {logger.error(e,e);
				return "";
			}
		}
	   /**
	    * 获得当前年月日
	    * @return
	    */
	    public static String getNowdate()
	    {
	        Date dt = new Date();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        return df.format(dt);
	    }
	   /**
	    * 获得当前时间
	    * @return
	    */
	   public static String getNowTime()
	    {
	        Date dt = new Date();
	        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	        return df.format(dt);
	    }


	/**
	 * 按照指定格式获得指定日期的时间措
	 * 
	 * @param text
	 *            text
	 * @param format
	 *            日期格式
	 * @return 时间措
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static long getTimes(String text, String format) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(format);
		try {
			return datetimeFormat.parse(text).getTime();
		} catch (Exception e) {
			logger.error(e,e);
		}
		return 0;
	}
	/**
	 * 字符串转日期
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date StringToDate(String dateStr,String formatStr){
		SimpleDateFormat dd=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		return date;
	}
	/**
	 * 获得年份
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String yearFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy");
		return datetimeFormat.format(date);
	}

	/**
	 * 获得年份
	 * 
	 * @param timestamp
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String yearFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	/**
	 * 获得月份
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String monthFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM");
		return datetimeFormat.format(date);
	}

	/**
	 * 获得月份
	 * 
	 * @param timestamp
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String monthFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	/**
	 * 获得日
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String dayFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd");
		return datetimeFormat.format(date);
	}

	/**
	 * 获得日
	 * 
	 * @param timestamp
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String dayFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	/**
	 * 算出两个时间的时长(String【XX天，XX.X小时】)
	 * @param startTime
	 * @param endTime
	 */
	public static String[] getTimeSlot(Date startTime, Date endTime){
		String[] time = new String[3];
		long end = endTime.getTime();
		long start = startTime.getTime();
		long times = end - start;
		long day = times/(1000*60*60*24);
		double hour = 0.00;
		if(day==0){
			hour = Double.valueOf(String.valueOf(times))/(1000*60*60);
		}else{
			times = times - (day*1000*60*60*24);
			hour = Double.valueOf(String.valueOf(times))/(1000*60*60);
		}
		BigDecimal d = new BigDecimal(hour);
		hour = d.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		time[0] = String.valueOf(day);
		time[1] = String.valueOf(hour);
		time[2] = String.valueOf(end - start);
		return time;
	}
	
	/**
	 * 算出一个毫秒数的时长(String【XX天，XX.X小时】)
	 * @param millis
	 * @return
	 */
	public static String[] getTimeSlot(Long times){
		String[] time = new String[2];
		long day = times/(1000*60*60*24);
		double hour = 0.00;
		if(day==0){
			hour = Double.valueOf(String.valueOf(times))/(1000*60*60);
		}else{
			times = times - (day*1000*60*60*24);
			hour = Double.valueOf(String.valueOf(times))/(1000*60*60);
		}
		BigDecimal d = new BigDecimal(hour);
		hour = d.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		time[0] = String.valueOf(day);
		time[1] = String.valueOf(hour);
		return time;
	}
	
	 /**
     * 判断2个时间相差多少天、多少小时、多少分<br>
     * <br>
     * @param pBeginTime 请假开始时间<br>
     * @param pEndTime 请假结束时间<br>
     * @return String 计算结果<br>
     * @Exception 发生异常<br>
     */
 public static String TimeDiff(Long pBeginTime, Long pEndTime) {
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	  Long beginL = pBeginTime;
	  Long endL = pEndTime;
	  Long day = (endL - beginL)/86400000;
	  Long hour = ((endL - beginL)%86400000)/3600000;
	  Long min = ((endL - beginL)%86400000%3600000)/60000;
	  StringBuffer str = new StringBuffer();
	  if(day!=0){
		  str.append(day).append("天");
	  }
	  if(day<5){
		  if(hour!=0){
			  str.append(hour).append("小时");
		  }
		  if(min!=0){
			  str.append(min).append("分钟");
		  }
	  }
	  return str.toString();
 }
 /**
  * 判断2个时间相差多少天、多少小时、多少分<br>
  * <br>
  * @param pBeginTime 请假开始时间<br>
  * @param pEndTime 请假结束时间<br>
  * @return String 计算结果<br>
  * @Exception 发生异常<br>
  */
 public static String TimeDiff(Long time) {
	 // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	 Long day = (time)/86400000;
	 Long hour = ((time)%86400000)/3600000;
	 Long min = ((time)%86400000%3600000)/60000;
	 Long ss = ((time)%86400000%3600000%60000)/6000;
	 StringBuffer str = new StringBuffer();
	 if(day!=0){
		 str.append(day).append("天");
	 } 
	 if(day<3&&hour!=0){
		 str.append(hour).append("小时");
	 }
	 if(day==0&&min!=0){
		 str.append(min).append("分钟");
	 }
	 if(str.toString().equals("")){
		 if(ss!=0){
			 str.append(ss).append("秒");
		 } 
	 }
	
	 return str.toString();
 }
	/**
	 * 获得日期时间
	 * 
	 * @param date
	 * @return 日期格式yyyy-MM-dd HH:mm:ss(old:yyyy-MM-dd hh:mm:ss a)
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String datetimeFormat(Date date) {
		if(date==null)return "";
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(date);
	}
	/**
	 * 获得日期时间
	 * 
	 * @param date
	 * @return 日期格式yyyy-MM-dd hh:mm:ss a
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String datetimeFormat24(Date date) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(date);
	}
	
	public static String datetimeFormat2(Date date) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		return datetimeFormat.format(date);
	}
	
	public static String datetimeFormat(Date date,String format) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(format);
		return datetimeFormat.format(date);
	}


	/**
	 * 获得日期时间
	 * 
	 * @param timestamp
	 * @return 格式yyyy-MM-dd hh:mm:ss a
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String datetimeFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
//		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}
	
	/**
	 * 获得日期时间
	 * 
	 * @param timestamp
	 * @return 格式yyyy-MM-dd hh:mm:ss a
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String datetimeFormat24(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
//		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}
	
	/**
	 * 获得日期时间
	 * 
	 * @param timestamp
	 * @return 格式yyyy-MM-dd hh:mm:ss a
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String datetimeFormat2(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
//		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}

	/**
	 * 获得日期
	 * 
	 * @param date
	 * @return 格式yyyy-MM-dd
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String dateFormat(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		return datetimeFormat.format(date);
	}

	/**
	 * 获得日期
	 * 
	 * @param timestamp
	 * @return 格式yyyy-MM-dd
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String dateFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}
	/**
	 * 获得月、日
	 * 
	 * @param timestamp
	 * @return 格式MM-dd
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String monthDayFormat(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM-dd");
		return datetimeFormat.format(new Date(timestamp.getTime()));
	}
	/**
	 * 获得月、日
	 * 
	 * @param timestamp
	 * @return 格式MM-dd
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String monthDayFormat(Date date) { 
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM-dd");
		return datetimeFormat.format(date);
	}

	/**
	 * 获得当前日期是星期几(数值)
	 * 
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getDayOfWeek() {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		return getDayOfWeek(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH) + 1, gregorianCalendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 获得指定日期是星期几(数值)
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getDayOfWeek(int year, int month, int day) {
		month = month - 1; // 从0开始
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.set(year, month, day);
		return gregorianCalendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获得当前日期是星期几(符号)
	 * 
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String getDayOfWeekSymbols() {
		return new DateFormatSymbols().getWeekdays()[getDayOfWeek()];
	}

	/**
	 * 获得指定日期是星期几(符号)
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String getDayOfWeekSymbols(int year, int month, int day) {
		return new DateFormatSymbols().getWeekdays()[getDayOfWeek(year, month, day)];
	}

	/**
	 * 获得当前月份最大天数
	 * 
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getMaxDayOfMonth() {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		return getMaxDayOfMonth(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 获得指定月份最大天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getMaxDayOfMonth(int year, int month) {
		month = month - 1;
		Calendar gregorianCalendar1 = GregorianCalendar.getInstance();
		Calendar gregorianCalendar2 = GregorianCalendar.getInstance();
		for (int i = 31; i > 27; i--) {
			gregorianCalendar1.set(year, month, i);
			gregorianCalendar2.set(year, month, 1);
			if (gregorianCalendar1.get(Calendar.MONTH) == gregorianCalendar2.get(Calendar.MONTH)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 当前是第几周
	 * 
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getWeekOfYear() {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		return getWeekOfYear(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH) + 1, gregorianCalendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 当前指定的日期是第几周
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getWeekOfYear(int year, int month, int day) {
		month = month - 1; // 从0开始
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.set(year, month, day);
		return gregorianCalendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 当前指定的日期是第几个季度
	 * 
	 * @param month
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getQuarterOfYear(int month) {
		if (month > 0 && month < 4)
			return 1;
		else if (month > 3 && month < 7)
			return 2;
		else if (month > 6 && month < 10)
			return 3;
		else
			return 4;
	}

	/**
	 * 当前指定的日期是第几个季度
	 * 
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getQuarterOfYear() {
		return getQuarterOfYear(getMonth(new Date()));
	}

	/**
	 * 得到指定日期的年份
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getYear(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(Calendar.YEAR);
	}

	/**
	 * 得到指定日期的小时
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getHour(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		// gregorianCalendar.setGregorianChange(date);
		return gregorianCalendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到指定日期的月份
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getMonth(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到指定日期的天
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getDay(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到指定日期的小时
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getHourOfDay(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到指定日期的分钟
	 * 
	 * @param date
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static int getMinute(Date date) {
		Calendar gregorianCalendar = GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return gregorianCalendar.get(Calendar.MINUTE);
	}
	
	/**
	 * 格式化时间，返回时间信息
	 * @param oldTime
	 * @return newTime ‘今天HH:mm’或‘昨天HH:mm’或‘MM-dd’或‘yyyy-MM-dd’
	 */
	public static String getShowTime(String oldTime){
		if(oldTime==null || "".equals(oldTime))return "";
		String newTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			oldTime = sdf1.format(sdf1.parse(oldTime));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			try {
				oldTime = sdf1.format(sdf.parse(oldTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}
		}
		try{
			Calendar cal = Calendar.getInstance();
			String jinnianStr = String.valueOf(cal.get(Calendar.YEAR))+"-01-01 00:00:00";
			Date date = cal.getTime();
			String todayStr = sdf.format(date);
			todayStr = todayStr + " 00:00:00";
			cal.add(Calendar.DATE, -1);
			date = cal.getTime();
			String yesterdayStr = sdf.format(date);
			yesterdayStr = yesterdayStr + " 00:00:00";
			
			Date jinnian = sdf1.parse(jinnianStr);
			Date jintian = sdf1.parse(todayStr);
			Date zuotian = sdf1.parse(yesterdayStr);
			
			if(sdf1.parse(oldTime).before(jinnian)){
				newTime = oldTime.substring(0,10);
			}//今年前
			else if(sdf1.parse(oldTime).after(jinnian) && sdf1.parse(oldTime).before(zuotian)){
				newTime = oldTime.substring(5,10);
			}//昨天前，今年后
			else if(sdf1.parse(oldTime).after(zuotian) && sdf1.parse(oldTime).before(jintian)){
				newTime = "昨天 " + oldTime.substring(11,16);
			}//昨天后，今天前
			else{
				newTime = "今天 " + oldTime.substring(11,16);
			}
		}catch(Exception e){
			logger.error(e,e);
		}
		return newTime;
	}
   /**
    * 格式化时间
    * @param oldTime  'yyyy-MM-dd HH:mm:ss'形式
    * @return 返回‘今天-yyyy年MM月dd日星期x’或者‘昨天-yyyy年MM月dd日星期x’或者‘yyyy年MM月dd日星期x’
    */
	public static String timeFormate(String oldTime){
		if(oldTime==null || "".equals(oldTime))return "";
		String newTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayNames[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"}; 
		try{
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			String todayStr = sdf.format(date);
			todayStr = todayStr + " 00:00:00";
			cal.add(Calendar.DATE, -1);
			date = cal.getTime();
			String yesterdayStr = sdf.format(date);
			yesterdayStr = yesterdayStr + " 00:00:00";
			
			Date jintian = sdf1.parse(todayStr);     //今天
			Date zuotian = sdf1.parse(yesterdayStr); //昨天
			Date oldDate = sdf1.parse(oldTime);      //参数
			
			cal.setTime(oldDate);
			String week = dayNames[cal.get(Calendar.DAY_OF_WEEK)];
			
			if(oldDate.before(zuotian)){
				newTime = sdf.format(oldDate)+week;
			}//昨天前
			else if(oldDate.after(zuotian) && oldDate.before(jintian)){
				newTime = "昨天-" + sdf.format(oldDate)+week;
			}//昨天后，今天前
			else{
				newTime = "今天-" + sdf.format(oldDate)+week;
			}
		}catch(Exception e){
			logger.error(e,e);
		}
		return newTime;
	}
	/**
	 * 格式化时间
	 * @param oldTime 'yyyy-MM-dd HH:mm:ss'形式
	 * @return 返回‘上午HH:mm’或者‘下午HH:mm’
	 */
	public static String getHourAndMin(String oldTime){
		if(oldTime==null || "".equals(oldTime))return "";
		String returnStr = "";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String noonName[] = {"上午","下午"};
		try {
			Date oldDate = sdf1.parse(oldTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(oldDate);
			returnStr = noonName[cal.get(Calendar.AM_PM)]+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		return returnStr;
	}
	
	/**
	 * 获取当前日期前后一段时间的格式化时间
	 * @param args
	 */
	public static String getMonthDate(String formatString,Integer num){
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = GregorianCalendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(calendar.MONTH, num);  //设置为前3月
		dBefore = calendar.getTime();   //得到前3月的时间
		SimpleDateFormat sdf=new SimpleDateFormat(formatString); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
		return defaultStartDate;
	}
	public static void main(String args[]) {
		String s = "AAA|BBA|";
	}
	
	public static String getFirstDayTimeOfMonth(int year,int month){  
        Calendar cal = Calendar.getInstance();  
        //设置年份  
        cal.set(Calendar.YEAR,year);  
        //设置月份  
        cal.set(Calendar.MONTH, month-1);  
        //获取某月最小天数  
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数  
        cal.set(Calendar.DAY_OF_MONTH, firstDay);  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String firstDayOfMonth = sdf.format(cal.getTime())+" 00:00:00";  
          
        return firstDayOfMonth;  
    }
	
	public static String getLastDayTimeOfMonth(int year,int month){  
        Calendar cal = Calendar.getInstance();  
        //设置年份  
        cal.set(Calendar.YEAR,year);  
        //设置月份  
        cal.set(Calendar.MONTH, month-1);  
        //获取某月最大天数  
        int LastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数  
        cal.set(Calendar.DAY_OF_MONTH, LastDay);  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String LastDayOfMonth = sdf.format(cal.getTime())+" 23:59:59";  
          
        return LastDayOfMonth;  
    }
}
