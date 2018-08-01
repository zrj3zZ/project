package com.iwork.plugs.syscalendar.sdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.syscalendar.constans.SysCalendarConstants;
import com.iwork.plugs.syscalendar.dao.SysCalendarDao;
import com.iwork.plugs.syscalendar.model.SysCalendarBaseInfoModel;
import com.iwork.plugs.syscalendar.model.SysCalendarDetailInfoModel;
import com.iwork.plugs.syscalendar.util.SysCalendarUtil;

import org.apache.log4j.Logger;
/**
 * 系统日历API
 * @author WangJianhui
 *
 */
public class SysCalendarAPI {
	private static Logger logger = Logger.getLogger(SysCalendarAPI.class);
	private static SysCalendarAPI instance;  
	private static Object lock = new Object(); 
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private SysCalendarDao syscalendardao;
	public static SysCalendarAPI getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysCalendarAPI();  
                }
            }  
        }  
        return instance;  
	}
	public SysCalendarAPI() {
		super();
		if(syscalendardao==null){
			syscalendardao = (SysCalendarDao)SpringBeanUtil.getBean("syscalendardao");
		}
		if(sessionFactory==null){
			sessionFactory = (SessionFactory)SpringBeanUtil.getBean("sessionFactory");
		}
		if(hibernateTemplate==null){
			this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}
	/**
	 * 判断指定用户的指定日期是否为工作日
	 * @param userid
	 * @return 1 工作日,0 非工作日,若返回 -111111111,则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭
	 */
	public int isWorkDay(String userId,Date _date){
		int result = 0;
		boolean flag = true;//工作日
		//查询该人当年当月的节假日数组
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		//指定日期的几号
		String day = SysCalendarUtil.getInstance().getDayfromDate(_date);
		Long cal_id = SysCalendarUtil.getInstance().getUserCalendar(userId);
		if(cal_id!=null){
			
			SysCalendarBaseInfoModel baseInfoModel  = syscalendardao.queryCalendarById(cal_id);
			int[] week = new int[7];
			if(baseInfoModel!=null){
				int[] temp = SysCalendarUtil.getInstance().getHolidaysArray(baseInfoModel);
				for(int i=0;i<temp.length;i++){ 
					week[i] = temp[i];
				}
			}
			String holidays = SysCalendarUtil.getInstance().getHolidayContainSeted(week, year, month, cal_id);
			//增加设置后的节假日
			//指定日期是否被包含在内,不包含则为工作日
			if(holidays!=null&&!"".equals(holidays)){
				
				String[] tempArr = holidays.split(",");
				for(int i=0;i<tempArr.length;i++){
					if(tempArr[i].equals(day)||tempArr[i]==day){
						flag = false;
						break;
					}
				}
			}
			result = (flag==false)?0:1;
		}else{
			result = SysCalendarConstants.ERROR_DAYS;
		}
		return result;
	}
	/**
	 * 判断指定人两个日期之间的工作天数
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return 若返回 -111111111,则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭
	 */
	public int betweenStartAndEndDays(Date startDate,Date endDate,String userId){
		int workDays = 0;
		if(startDate!=null&&endDate!=null&&userId!=null&&!"".equals(userId)){
			int holidays = SysCalendarUtil.getInstance().getHolidaysByBetweenDates(startDate, endDate, userId);
			int countDays = SysCalendarUtil.getInstance().getDaysBetweenDates(startDate, endDate);
			if(holidays!=SysCalendarConstants.ERROR_DAYS){
				if(countDays>=holidays){
					workDays = countDays - holidays;
				}else{
					workDays = 0;
				}
			}else{
				workDays = SysCalendarConstants.ERROR_DAYS;
			}
		}
		return workDays;
	}
	/**
	 * 根据起始日期和工作天数获得结束日期
	 * @param startDate
	 * @param days
	 * @param userId
	 * @return 若返回null,则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭
	 */
	public Date getEndDateByStartDayAndDays(Date startDate,int days,String userId){
		Date endDate = new Date();
		int countDays = 0;
		boolean flag = false;
		//减少对数据库的操作,并提取数据尽量少些,根据天数来确定查询出区间段数据
		double splitPoint = 360;//分割点
		int offset = 1;//偏移量(可能开始日期接近年底，所以多加一年)
		Long calendarId = 0L;
		int addCountYear = (int)Math.ceil(days/splitPoint);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		int startYear = cal.get(Calendar.YEAR);
		int endYear = startYear + offset + addCountYear;
		String endDateStr = endYear +"-"+"12-31";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date endDateTemp = new Date();
		calendarId = SysCalendarUtil.getInstance().getUserCalendar(userId);
		if(calendarId!=null){
			
			int[] week = SysCalendarUtil.getInstance().getHolidayToWeek(calendarId);
			try {
				endDateTemp = df.parse(endDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}
			Map<String,List<SysCalendarDetailInfoModel>> map = SysCalendarUtil.getInstance().getDatesSetedByCalendarId(calendarId, startDate, endDateTemp);
			List<SysCalendarDetailInfoModel> listHol = new ArrayList<SysCalendarDetailInfoModel>();
			List<SysCalendarDetailInfoModel> listWork = new ArrayList<SysCalendarDetailInfoModel>();
			if(map!=null&&map.size()>0){
				listHol = map.get("0");
				listWork = map.get("1");
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			while(flag==false){
				int dayOfWeek = SysCalendarUtil.getInstance().getDayForWeek(calendar.getTime());
				if(//1)若不是周末且设置周末list中不包含此日   、2)若是周末且设置工作list中包含此日                  1)和2)符合一点即可
						((SysCalendarUtil.getInstance().isContainWeek(week, dayOfWeek)==false) && (SysCalendarUtil.getInstance().isContainDate(listHol,calendar.getTime())==false))
						||((SysCalendarUtil.getInstance().isContainWeek(week, dayOfWeek)==true)&& (SysCalendarUtil.getInstance().isContainDate(listWork,calendar.getTime())==true))
				){
					countDays ++;
				}
				if(countDays==days){
					flag = true;
					endDate = calendar.getTime();
				}else{
					calendar.add(Calendar.DATE, 1);
				}
			}
		}else{
			//若日历都没开启或已经超出有效时间则返回空
			endDate = null;
		}
		return endDate;
	}
	
	/**
	 * 根据结束日期和工作天数获得起始日期
	 * @param startDate
	 * @param days
	 * @param userId
	 * @return 若返回null,则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭
	 */
	public Date getStartDateByEndDayAndDays(Date endDate,int days,String userId){
		Date startDate = new Date();
		int countDays = days;
		boolean flag = false;
		//减少对数据库的操作,并提取数据尽量少些,根据天数来确定查询出区间段数据
		double splitPoint = 360;//分割点
		int offset = 1;//偏移量(可能开始日期接近年底，所以多加一年)
		Long calendarId = 0L;
		int addCountYear = (int)Math.ceil(days/splitPoint);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		int endYear = cal.get(Calendar.YEAR);
		int startYear = endYear - offset - addCountYear;
		String startDateStr = startYear +"-"+"1-1";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDateTemp = new Date();
		calendarId = SysCalendarUtil.getInstance().getUserCalendar(userId);
		if(calendarId!=null){
			
			int[] week = SysCalendarUtil.getInstance().getHolidayToWeek(calendarId);
			try {
				startDateTemp = df.parse(startDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}
			Map<String,List<SysCalendarDetailInfoModel>> map = SysCalendarUtil.getInstance().getDatesSetedByCalendarId(calendarId, endDate, startDateTemp);
			List<SysCalendarDetailInfoModel> listHol = new ArrayList<SysCalendarDetailInfoModel>();
			List<SysCalendarDetailInfoModel> listWork = new ArrayList<SysCalendarDetailInfoModel>();
			if(map!=null&&map.size()>0){
				listHol = map.get("0");
				listWork = map.get("1");
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			while(flag==false){
				int dayOfWeek = SysCalendarUtil.getInstance().getDayForWeek(calendar.getTime());
				if(//1)若不是周末且设置周末list中不包含此日   、2)若是周末且设置工作list中包含次日                  1)和2)符合一点即可
						((SysCalendarUtil.getInstance().isContainWeek(week, dayOfWeek)==false) && (SysCalendarUtil.getInstance().isContainDate(listHol,calendar.getTime())==false))
						||((SysCalendarUtil.getInstance().isContainWeek(week, dayOfWeek)==true)&& (SysCalendarUtil.getInstance().isContainDate(listWork,calendar.getTime())==true))
				){
					countDays --;
				}
				if(countDays==0){
					flag = true;
					startDate = calendar.getTime();
				}else{
					calendar.add(Calendar.DATE, -1);
				}
			}
		}else{
			//若日历都没开启或已经超出有效时间则返回空
			startDate = null;
		}
		return startDate;
	}
	
	/**
	 * 判断指定用户的指定时间是否为工作时间
	 * @param userid 
	 * @param _time必须格式为 yyyy-MM-dd HH:mm:ss
	 * @return 返回0不包含在内,返回1包含在内,返回-111111111表示没有找到日历,日历被关闭,或是过期
	 */
	public int isWorkTime(String userId,String _time){
		int result = 0;
		boolean flag = false;
		Long calendarId = 0L;
		SimpleDateFormat  sdf = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//SimpleDateFormat  sdf_date = new  SimpleDateFormat("yyyy-MM-dd");
		Date date_to = new Date();
		Date date_from = new Date();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		String _mDate = "";
		if(userId!=null&&_time!=null&&!"".equals(_time)&&!"".equals(userId)){
			calendarId = SysCalendarUtil.getInstance().getUserCalendar(userId);
			if(calendarId!=null){
				
				SysCalendarBaseInfoModel baseInfoModel  = syscalendardao.queryCalendarById(calendarId);
				Long workTimeFrom = baseInfoModel.getWorkTimeFrom();
				Long workTimeTo = baseInfoModel.getWorkTimeTo();
				try {
					date = sdf.parse(_time);
					calendar.setTime(sdf.parse(_time));
					_mDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
					if(workTimeFrom!=null){
						_mDate = _mDate + " " + workTimeFrom + ":00:00";
					}
					date_from = sdf.parse(_mDate);
					//若开始时间大于结束时间,则结束时间为次日
					if(workTimeFrom > workTimeTo){
						calendar.add(Calendar.DATE, 1);
					}
					_mDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
					if(workTimeTo!=null){
						_mDate = _mDate + " "+workTimeTo+":00:00";
					}
					date_to = sdf.parse(_mDate);
					flag = SysCalendarUtil.getInstance().isBetweenTwoDates(date_to, date_from, date);
					if(flag == false){
						result = 0;
					}else{
						result = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}else{
				result = SysCalendarConstants.ERROR_DAYS;
			}
		}
		return result;
	}
	
	/**
	 * 判断指定用户的当前时间是否为工作时间
	 * @param userid
	 * @return 返回0不包含在内,返回1包含在内,返回-111111111表示没有找到日历,日历被关闭,或是过期
	 */
	public int isWorkTime(String userId){
		int result = 0;
		boolean flag = false;
		Long calendarId = 0L;
		SimpleDateFormat  sdf = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date_to = new Date();
		Date date_from = new Date();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		String _mDate = "";
		if(userId!=null&&!"".equals(userId)){
			calendarId = SysCalendarUtil.getInstance().getUserCalendar(userId);
			if(calendarId!=null){
				
				SysCalendarBaseInfoModel baseInfoModel  = syscalendardao.queryCalendarById(calendarId);
				Long workTimeFrom = baseInfoModel.getWorkTimeFrom();
				Long workTimeTo = baseInfoModel.getWorkTimeTo();
				try {
					calendar.setTime(date);
					_mDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
					if(workTimeFrom!=null){
						_mDate = _mDate + " " + workTimeFrom + ":00:00";
					}
					date_from = sdf.parse(_mDate);
					//若开始时间大于结束时间,则结束时间为次日
					if(workTimeFrom > workTimeTo){
						calendar.add(Calendar.DATE, 1);
					}
					_mDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
					if(workTimeTo!=null){
						_mDate = _mDate + " "+workTimeTo+":00:00";
					}
					date_to = sdf.parse(_mDate);
					calendar.setTime(date);
					flag = SysCalendarUtil.getInstance().isBetweenTwoDates(date_to, date_from, calendar.getTime());
					if(flag == false){
						result = 0;
					}else{
						result = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}else{
				result = SysCalendarConstants.ERROR_DAYS;
			}
		}
		return result;
	}
	/**
	 * API 测试类
	 * @throws ParseException 
	 */
	public void test_API() {
		Calendar calendar = Calendar.getInstance();
		Date date2 = SysCalendarAPI.getInstance().getEndDateByStartDayAndDays(calendar.getTime(), 20, "YANGDAYONG");
	}
	
}
