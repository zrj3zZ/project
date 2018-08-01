package com.iwork.app.schedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IWorkScheduleUtil {
	public static String[][] FLAG = { { "0", "禁用" }, { "1", "启用" } };
	public static String[][] RULE_TYPE = { { "0", "每天" }, { "1", "每周" }, { "2", "每月" }, { "3", "每季度" }, { "4", "每年" }, { "5", "服务启动时" } };
	public static String[][] STATUS = { {},{ "0", "执行成功" }, { "1", "抛出异常" },{ "2", "执行失败" } };
	public static String[][] EXECUTE_TYPE = { { "0", "自动" }, { "1", "手动" } };

	// insert
	private static final String QUERY_ALL_LIST = "select ID,PLANNAME,ISSYSTEM,ISDISABLED,PLANDESC,CLASSZ,PLANPRI,ADD_TIME from SYS_SCHEDULE";

	public void f1() {

		// 获取当前数据库的日期默认函数 ,sysdate，用于创建的
		// DBSql.getDateDefaultValue();
		// 用于更新
		// DBSql.convertShortDate('2007-1-1');
	}
 
	public static String filterString(String srcString, String[][] filterString) {
		if (srcString == null) {
			srcString = "";
			return srcString;
		}
		String str = filterString[Integer.parseInt(srcString)][1];
		return str;
	}

	/**
	 * 获得 任务
	 * @param type
	 * @param executeTime
	 * @param classz
	 * @param usefulliftStart
	 * @param usefulliftEnd
	 * @param executePoints
	 * @param scheduleModel
	 * @return 
	 * @author Administrator
	 */
//	public static IAWScheduleTask getScheduleTask(String type,
//			String executeTime, String classz, String usefulliftStart,
//			String usefulliftEnd, String executePoints,AWSScheduleModel scheduleModel) {
//		int typeValue = Integer.parseInt(type);
//		long executeTimel = Long.parseLong(executeTime);
//		if (usefulliftStart == null) {
//			usefulliftStart = "190000000000";
//		}
//		if (usefulliftStart.trim().equals("")) {
//			usefulliftStart = "190000000000";
//		}
//		if (usefulliftEnd == null) {
//			usefulliftEnd = "300000000000";
//		}
//		if (usefulliftEnd.trim().equals("")) {
//			usefulliftEnd = "300000000000";
//		}
//
//		switch (typeValue) {
//			case 0: { // 日计划
//				SchedueDayImp task = new SchedueDayImp();
//				task.setClassz(classz);
//				long currentTime = getCurrentDayHHMM();
//				long exeTime = getCurrentDay() + executeTimel;
//				if(currentTime<=exeTime){
//					
//				}else {
//					exeTime = getTomorrowDay() + executeTimel;
//				}
//				task.setExecuteTime(exeTime);
//				task.setUsefulliftStart(usefulliftStart);
//				task.setUsefulliftEnd(usefulliftEnd);
//				task.setScheduleModel(scheduleModel);
//				return task;
//			}
//			case 1: { // 周计划
//				SchedueWeekImp task = new SchedueWeekImp();
//				task.setClassz(classz);
//				task.setExecutePoints(executePoints);
//				long dayOfWeek = getFirstExecuteDayOfWeek(executePoints, executeTimel);
//				long exeTime = dayOfWeek + executeTimel;
//				task.setExecuteTime(exeTime);
//				task.setUsefulliftStart(usefulliftStart);
//				task.setUsefulliftEnd(usefulliftEnd);
//				task.setScheduleModel(scheduleModel);
//				return task;
//			}
//			case 2: {  // 月计划
//				SchedueMonthImp task = new SchedueMonthImp();
//				task.setClassz(classz);
//				task.setExecutePoints(executePoints);
//				long dayOfWeek = getFirstExecuteDayOfMonth(executePoints, executeTimel);
//				long exeTime = dayOfWeek + executeTimel;
//				task.setExecuteTime(exeTime);
//				task.setUsefulliftStart(usefulliftStart);
//				task.setUsefulliftEnd(usefulliftEnd);
//				task.setScheduleModel(scheduleModel);
//				return task;
//			}
//			case 3:{  // 季度计划
//				SchedueQuarterImp task = new SchedueQuarterImp();
//				task.setClassz(classz);
//				task.setExecutePoints(executePoints);
//				long exeTime = getFirstExecuteDayOfQuarterMonth(executePoints, executeTimel);
//				task.setExecuteTime(exeTime);
//				task.setUsefulliftStart(usefulliftStart);
//				task.setUsefulliftEnd(usefulliftEnd);
//				task.setScheduleModel(scheduleModel);
//				return task;
//			}
//			case 4:{  // 年计划
//				SchedueYearImp task = new SchedueYearImp();
//				task.setClassz(classz);
//				task.setExecutePoints(executePoints);
//				long exeTime = getFirstExecuteDayOfYear(executePoints, executeTimel);
//				task.setExecuteTime(exeTime);
//				task.setUsefulliftStart(usefulliftStart);
//				task.setUsefulliftEnd(usefulliftEnd);
//				task.setScheduleModel(scheduleModel);
//				return task;
//			}
//			case 5:{  // AWS启动时
//				SchedueAWSRunImp task = new SchedueAWSRunImp();
//				task.setClassz(classz);
//				task.setUsefulliftStart(usefulliftStart);
//				task.setUsefulliftEnd(usefulliftEnd);
//				task.setScheduleModel(scheduleModel);
//				return task;
//			}
//		}
//		return null;
//	}
	/**
	 * 获得按执行的时间
	 * @param executePoints
	 * @param executeTimel
	 * @return
	 * @author Administrator
	 */
	public static long getFirstExecuteDayOfYear(String executePoints, long executeTimel) {
		String[] points = executePoints.split(",");
		long currentDay = getCurrentDay();
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long month = Long.parseLong(points[0]);
		long day = Long.parseLong(points[1]);
		long exeTime = year * 100000000 + month * 1000000 + day * 10000;
		if(exeTime > currentDay){
			return  exeTime+executeTimel;
		}else{
			return (year+1) * 100000000 + month * 1000000 + day * 10000+executeTimel;
		}
		
		
	}

	public static long getFirstExecuteDayOfQuarterMonth(String executePoints, long executeTimel) {
		String[] points = executePoints.split(",");
		long currentDay = getCurrentDayHHMM();
		return getExecuteTime(points[0],points[1],executeTimel,currentDay); 
	}

	
	private static long getExecuteTime(String num, String day, long executeTimel, long currentDay) {		
		if(num.trim().equals("1")){
			String[] monthArray = {"1","4","7","10"};
			return checkTime(monthArray,day,executeTimel,currentDay);
		}
		if(num.trim().equals("2")){
			String[] monthArray = {"2","5","8","11"};
			return checkTime(monthArray,day,executeTimel,currentDay);
		}
		if(num.trim().equals("3")){
			String[] monthArray = {"3","6","9","12"};
			return checkTime(monthArray,day,executeTimel,currentDay);
		}
		return 0;
	}
	
	private static long checkTime(String[] monthArray, String day, long executeTimel, long currentDay){
		long exeTime = 0;
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long dd = Long.parseLong(day);
		for(int i=0; i<monthArray.length; i++){
			long month = Long.parseLong(monthArray[i]);
			exeTime = year * 100000000 + month * 1000000 + dd * 10000 + executeTimel;
			if(exeTime>currentDay){
				return exeTime;
			}
		}
		exeTime = (year+1) * 100000000 + Long.parseLong(monthArray[0]) * 1000000 + dd * 10000 + executeTimel;
		return exeTime;
	}

	/**
	 * 得到当前天的长整型串 如 ：200708220000
	 * 
	 * @return
	 * @author Administrator
	 */
	public static long getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH) + 1;
		long day = cal.get(Calendar.DATE);
		long exeTime = year * 100000000 + month * 1000000 + day * 10000;
		return exeTime;
	}
	/**
	 * 得到当前天的长整型串 如 ：200708221030
	 * 
	 * @return
	 * @author Administrator
	 */
	public static long getCurrentDayHHMM() {
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH) + 1;
		long day = cal.get(Calendar.DATE);
		long hour = cal.get(Calendar.HOUR_OF_DAY);
		long minute = cal.get(Calendar.MINUTE);
		long exeTime = year * 100000000 + month * 1000000 + day * 10000+hour*100+minute;
		return exeTime;
	}
	/**
	 * 得到当前天的长整型串 如 ：200708221030
	 * 
	 * @return
	 * @author Administrator
	 */
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH) + 1;
		long day = cal.get(Calendar.DATE);
		long hour = cal.get(Calendar.HOUR_OF_DAY);
		long minute = cal.get(Calendar.MINUTE);
		long second = cal.get(Calendar.SECOND);
		long MILLISECOND = cal.get(Calendar.MILLISECOND);
		String exeTime = year+"-" + month+ "-" + day +" "+hour+":"+minute+":"+second+"."+MILLISECOND;
		return exeTime;
	}

	/**
	 * 得到当前天的长整型串 如 ：200708221030
	 * 
	 * @return
	 * @author Administrator
	 */
	public static String getCurrentTimeYYMMDD() {
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH) + 1;
		long day = cal.get(Calendar.DATE);
		String exeTime = year+"-" + month+ "-" + day ;
		return exeTime;
	}
	/**
	 * 任务的一个星期内第一个执行日期 如 ：200708220000
	 * 
	 * @return
	 * @author Administrator
	 * @throws ParseException 
	 */
	public static long getFirstExecuteDayOfWeek(String dayPoint,
			long executeTimel)  {
		String[] points = dayPoint.split(",");
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK);

		long exeTime = 0;
		for (int i = 0; i < points.length; i++) {
			int point = Integer.parseInt(points[i]);
			if (weekday < point) {
				Date date = getDateAfterN(point - weekday);
				exeTime = getYYMMDD(date);
				break;
			}else if(weekday == point) {
				long hour = cal.get(Calendar.HOUR_OF_DAY);
				long minute = cal.get(Calendar.MINUTE);
				long currentHHMM = hour*100+minute;
				if(currentHHMM >= executeTimel){
					if(i+1 < points.length){
						int tempPoint = Integer.parseInt(points[i+1]);
						Date date = getDateAfterN(tempPoint - weekday);
						exeTime = getYYMMDD(date);						
					}else if(points.length == 1){						
						Date date = getDateAfterN(7);
						exeTime = getYYMMDD(date);						
					}
				}else{
					Date date = getDateAfterN(0);
					exeTime = getYYMMDD(date);		
				}
				break;				
			}else{
					continue;	
			}
		}
		// 如果所有的点都比当前时间点小 ，算下一周的第一个点
		if (exeTime == 0) {
			int point = Integer.parseInt(points[0]);
			Date date = getDateAfterN((point+7) - weekday);
			exeTime = getYYMMDD(date);
		}
		return exeTime;
	}


	
	/**
	 * 任务的一个月内第一个执行日期 如 ：200708220000
	 * 
	 * @return
	 * @author Administrator
	 * @throws ParseException 
	 */
	public static long getFirstExecuteDayOfMonth(String dayPoint,
			long executeTimel)  {
		String[] points = dayPoint.split(",");
		Calendar cal = Calendar.getInstance();
		int monthDay = cal.get(Calendar.DAY_OF_MONTH);

		long exeTime = 0;
		for (int i = 0; i < points.length; i++) {
			int point = Integer.parseInt(points[i]);
			if (monthDay < point) {
				Date date = getDateAfterN(point - monthDay);
				exeTime = getYYMMDD(date);
				break;
			}else if(monthDay == point) {
				long hour = cal.get(Calendar.HOUR_OF_DAY);
				long minute = cal.get(Calendar.MINUTE);
				long currentHHMM = hour*100+minute;
				if(currentHHMM >= executeTimel){
					if(i+1 < points.length){
						int tempPoint = Integer.parseInt(points[i+1]);
						Date date = getDateAfterN(tempPoint - monthDay);
						exeTime = getYYMMDD(date);						
					}else if(points.length == 1){	// 如果只有一个点	
						int month = cal.get(Calendar.MONTH)+1;
						int days = 30;
						if(month == 1 ||month == 3 ||month == 5||month == 7 ||month == 8 ||month == 10||month == 12){
							days = 31;
						}else if(month == 2){
							days = 28;
						}
						Date date = getDateAfterN(days);
						exeTime = getYYMMDD(date);						
					}
				}else{
					Date date = getDateAfterN(0);
					exeTime = getYYMMDD(date);
				}
				break;				
			}else{
				continue;
			}
		}
		//如果 当前时间比所有点都大，那么计算下一个月的第一个执行天
		if (exeTime == 0) {
			int month = cal.get(Calendar.MONTH)+1;
			int days = 30;
			if(month == 1 ||month == 3 ||month == 5||month == 7 ||month == 8 ||month == 10||month == 12){
				days = 31;
			}else if(month == 2){
				if(checkYearRun()){
					days = 29;	
				}else{
					days = 28;
				}
			}
			int point = Integer.parseInt(points[0]);
			Date date = getDateAfterN((point+days) - monthDay);
			exeTime = getYYMMDD(date);
		}
		return exeTime;
	}
	/**
	 * 判断润年
	 * @return
	 * @author Administrator
	 */
	private static boolean checkYearRun(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		boolean flag = false;
		if(year%100==0)
		{
		    if(year%400==0)
		       flag=true;
		}else  if(year%4==0){
			flag=true;
		}
		return flag;
	}
	/**
	 * 任务的一个月内第一个执行日期 如 ：200708220000
	 * 
	 * @return
	 * @author Administrator
	 * @throws ParseException 
	 */
	public static long getFirstExecuteDayOfQuarter(String dayPoint,
			long executeTimel)  {
		String[] points = dayPoint.split(",");
		Calendar cal = Calendar.getInstance();
		int monthDay = cal.get(Calendar.DAY_OF_MONTH);

		long exeTime = 0;
		for (int i = 0; i < points.length; i++) {
			int point = Integer.parseInt(points[i]);
			if (monthDay < point) {
				Date date = getDateAfterN(point - monthDay);
				exeTime = getYYMMDD(date);
				break;
			}else if(monthDay == point) {
				long hour = cal.get(Calendar.HOUR);
				long minute = cal.get(Calendar.MINUTE);
				long currentHHMM = hour*100+minute;
				if(currentHHMM > executeTimel){
					if(i+1 < points.length){
						int tempPoint = Integer.parseInt(points[i+1]);
						Date date = getDateAfterN(tempPoint - monthDay);
						exeTime = getYYMMDD(date);						
					}else if(points.length == 1){	// 如果只有一个点	
						int month = cal.get(Calendar.MONTH)+1;
						int days = 30;
						if(month == 1 ||month == 3 ||month == 5||month == 7 ||month == 8 ||month == 10||month == 12){
							days = 31;
						}else if(month == 2){
							days = 28;
						}
						Date date = getDateAfterN(days);
						exeTime = getYYMMDD(date);						
					}
				}
				break;				
			}
		}
		if (exeTime == 0) {
			int month = cal.get(Calendar.MONTH)+1;
			int days = 30;
			if(month == 1 ||month == 3 ||month == 5||month == 7 ||month == 8 ||month == 10||month == 12){
				days = 31;
			}else if(month == 2){
				days = 28;
			}
			int point = Integer.parseInt(points[0]);
			Date date = getDateAfterN((point+days) - monthDay);
			exeTime = getYYMMDD(date);
		}
		return exeTime;
	}

	/**
	 * 获得年月日，如：200709090000  长整型
	 * @param date
	 * @return
	 * @author Administrator
	 */
	private static long getYYMMDD(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH)+1;
		long day = cal.get(Calendar.DATE);
		long executeTime = year*100000000 + month*1000000+day*10000;
		return executeTime;
	}

	/**
	 * 获得从今天开始，N天后的日期
	 * 
	 * @return
	 * @author Administrator
	 * @throws ParseException
	 */
	public static Date getDateAfterN(int n){
		
		Date date = new Date();
		// 加n天
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		Date date1 = format.parse("2002-02-28 10:16:00");
		long Time = (date.getTime() / 1000) + n * 24 * 60 * 60;
		date.setTime(Time * 1000);
		String mydate1 = format.format(date);
		return date;
	}

	/**
	 * 得到下一天的的长整型串 如 ：200708220000
	 * 
	 * @return
	 * @author Administrator
	 */
	public static long getTomorrowDay() {
		Calendar cal = Calendar.getInstance();
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH) + 1;
		long day = cal.get(Calendar.DATE) + 1;
		long tomorrowTime = year * 100000000 + month * 1000000 + day * 10000;

		return tomorrowTime;
	}

	/**
	 * 初始化任务计划列表
	 * 
	 * @author Administrator
	 */

	public static void main(String[] args) {

		Date dd = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dd);
		int mday = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int quarter = cal.get(Calendar.MONTH)+1;
	}
}
