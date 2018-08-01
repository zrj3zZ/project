package com.ibpmsoft.project.zqb.util;

import java.util.Calendar;
import java.util.Date;

public class GanttDateUtil {
	
	public static String parseDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int weiMonth = month;
		String monthStr = "";
		String dayStr = "";
		if(weiMonth<10){
			monthStr = "0"+weiMonth;
		}else{
			monthStr = weiMonth+"";
		}
		int day = c.get(Calendar.DATE);
		
		if(day<10){
			dayStr = "0"+day; 
		}else{
			dayStr = ""+day;
		}
		StringBuffer dateStr = new StringBuffer();
		dateStr.append("new Date(").append(year).append(",").append(monthStr).append(",").append(dayStr).append(")");
		return dateStr.toString();
	}

}
