package com.iwork.plugs.calender.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
public class CalendarUtil {
	private static Logger logger = Logger.getLogger(CalendarUtil.class);
	/**
	 * 返回起始时间和最终时间之间的所有星期week的具体日期
	 * @param startdate
	 * @param enddate
	 * @param week
	 * @return
	 */
	public List<String> getWeekInfo(Date startdate,Date enddate,int week){
		List<String> list = new ArrayList<String>();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		start.setTime(startdate);
		end.setTime(enddate);
		while(true){
			int i = start.get(Calendar.DAY_OF_WEEK);
			if(i==week){
				list.add(format.format(start.getTime()));
			}
			if(start.equals(end)){
				break;
			}
			start.add(Calendar.DATE, 1);
		}		
		return list;
	}
	/**
	 * 返回起始时间和最终时间之间的每月第dayOfMonth天的具体日期
	 * @param startdate
	 * @param enddate
	 * @param dayOfMonth
	 * @return
	 */
	public List<String> getMonthInfo(Date startdate,Date enddate,int dayOfMonth){
		List<String> list = new ArrayList<String>();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		start.setTime(startdate);
		end.setTime(enddate);
		while(true){
			int i = start.get(Calendar.DAY_OF_MONTH);
			if(i==dayOfMonth){
				list.add(format.format(start.getTime()));
			}
			if(start.equals(end)){
				break;
			}
			start.add(Calendar.DATE, 1);
		}
		return list;
	}
	/**
	 * 返回起始时间和最终时间之间的每年的第monthOfYear月第dayOfMonth日具体日期
	 * @param startdate
	 * @param enddate
	 * @param dayOfMonth
	 * @return
	 */
	public List<String> getYearInfo(Date startdate,Date enddate,int monthOfYear,int dayOfMonth){
		List<String> list = new ArrayList<String>();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		start.setTime(startdate);
		end.setTime(enddate);
		while(true){
			int i = start.get(Calendar.MONTH);
			int j = start.get(Calendar.DAY_OF_MONTH);
			if(i==monthOfYear&&j==dayOfMonth){
				list.add(format.format(start.getTime()));
			}
			if(start.equals(end)){
				break;
			}
			start.add(Calendar.DATE, 1);
		}
		return list;
	}
	/**
	 * 将1970至今的秒数转换成格式日期
	 * @param seconds
	 * @return
	 */
	public String getDateFromSeconds(String seconds){
        if(seconds==null)
            return " ";
        else{
            Date date=new Date();
            try{
                date.setTime(Long.parseLong(seconds)*1000);
                }
            catch(NumberFormatException e){
                logger.error(e,e);
                return "Input string:"+seconds+"not correct,eg:2011-01-20";
                }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }
	/**
	 * 获得当年所有周六日
	 * @return
	 */
	public List<String> getAllWeekDay(){
		List<String> list = new ArrayList<String>();
		
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int nextyear=calendar.get(Calendar.YEAR)+1;
		Calendar nowyear=Calendar.getInstance();
		Calendar nexty=Calendar.getInstance();
		nowyear.set(year, 0, 1);
		nexty.set(nextyear, 0, 1);
		 
		calendar.add(Calendar.DAY_OF_MONTH, -calendar.get(Calendar.DAY_OF_WEEK));//周六
		Calendar c=(Calendar) calendar.clone();
		for(;calendar.before(nexty)&&calendar.after(nowyear);calendar.add(Calendar.DAY_OF_YEAR, -7)){
			list.add(calendar.get(Calendar.YEAR)+"-"+(1+calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DATE));
		}
		for(;c.before(nexty)&&c.after(nowyear);c.add(Calendar.DAY_OF_YEAR, 7)){
			list.add(c.get(Calendar.YEAR)+"-"+(1+c.get(Calendar.MONTH))+"-"+c.get(Calendar.DATE));
		}
		
		Calendar calendar2=Calendar.getInstance();
		int year2=calendar2.get(Calendar.YEAR);
		int nextyear2=calendar2.get(Calendar.YEAR)+1;
		Calendar nowyear2=Calendar.getInstance();
		Calendar nexty2=Calendar.getInstance();
		nowyear2.set(year2, 0, 1);
		nexty2.set(nextyear2, 0, 1);
		 
		calendar2.add(Calendar.DAY_OF_MONTH, -calendar2.get(Calendar.DAY_OF_WEEK)+1);//周日
		Calendar c2=(Calendar) calendar2.clone();
		for(;calendar2.before(nexty2)&&calendar2.after(nowyear2);calendar2.add(Calendar.DAY_OF_YEAR, -7)){
			list.add(calendar2.get(Calendar.YEAR)+"-"+(1+calendar2.get(Calendar.MONTH))+"-"+calendar2.get(Calendar.DATE));
		}
		for(;c2.before(nexty2)&&c2.after(nowyear2);c2.add(Calendar.DAY_OF_YEAR, 7)){
			list.add(c2.get(Calendar.YEAR)+"-"+(1+c2.get(Calendar.MONTH))+"-"+c2.get(Calendar.DATE));
		}
		return list;
	}
	
	/**
	 * 获得设定年所有周六日
	 * @return
	 */
	public List<String> getWeekDayByYear(int year){  
		int i=1;
        Calendar calendar=new GregorianCalendar(year,0,1);
        List<String> weekdays = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while(calendar.get(Calendar.YEAR)<year+1){
            calendar.set(Calendar.WEEK_OF_YEAR,i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if(calendar.get(Calendar.YEAR)==year){
            	weekdays.add(sdf.format(calendar.getTime()));
            }
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
            if(calendar.get(Calendar.YEAR)==year){
            	weekdays.add(sdf.format(calendar.getTime()));
            }
        }
        return weekdays;
    }
}
