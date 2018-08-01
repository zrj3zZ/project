package com.iwork.plugs.appointment.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
public class AppointmentUtil {
	private static Logger logger = Logger.getLogger(AppointmentUtil.class);
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
}
