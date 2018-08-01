package com.iwork.plugs.syscalendar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.syscalendar.constans.SysCalendarConstants;
import com.iwork.plugs.syscalendar.model.SysCalendarBaseInfoModel;
import com.iwork.plugs.syscalendar.model.SysCalendarDetailInfoModel;
import com.iwork.plugs.syscalendar.service.SysWorkCalendarService;

/**
 * 系统日历工具类
 * @author WangJianhui
 *
 */
public class SysCalendarUtil extends HibernateDaoSupport{
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private SysWorkCalendarService sysworkcalendarservice;
	private static SysCalendarUtil instance;  
    private static Object lock = new Object();  
	public static SysCalendarUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new SysCalendarUtil();  
	                }
	            }
	        }  
	        return instance;  
	 }
	 
	public SysCalendarUtil() {
		super();
		if(sysworkcalendarservice==null){
			sysworkcalendarservice = (SysWorkCalendarService)SpringBeanUtil.getBean("sysworkcalendarservice");
		}
		if(sessionFactory==null){
			sessionFactory = (SessionFactory)SpringBeanUtil.getBean("sessionFactory");
		}
		if(hibernateTemplate==null){
			this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}
	
	/**
	 * 获得假期字符串,不考虑手动设置的节假日和工作日
	 * @param week 星期（1-7）依次为星期一、二...等.数组
	 * @param year 年份
	 * @param month 月份(1-12)一月、二月....等
	 * @return 返回某年某月的节假日字符串,用','分割
	 */
	public  String getHolidays(int[] week,int year,int month){
		String result = "";
		StringBuffer datesStr = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);	
		//calendar.get(Calendar.DAY_OF_WEEK)求出当月的第一天是星期几,减1去除周日0
		//如fistDayOfWeek = 4 为 星期四
		int fistDayOfWeek =calendar.get(Calendar.DAY_OF_WEEK)-1;
		if(fistDayOfWeek==0){
			fistDayOfWeek = 7;
		}
		//Calendar.DAY_OF_MONTH是一个月的某天,getActualMaximum()求出某天可能的最大值 ：5月最大的可能是31
		int allDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		int day = 0;
		for(int j=0;j<week.length;j++){
			//每个月可能存在最多5周
			if(week[j]!=0){
				for(int i=0;i<6;i++){
					day = 1+(week[j]-fistDayOfWeek)+i*7;
					if(day<1 || day >allDays){
						continue;
					}
					datesStr.append(day).append(",");
				}
			}
		}
		if(datesStr!=null&&!"".equals(datesStr.toString())){
			
			result = datesStr.toString();
			result = result.substring(0,result.lastIndexOf(","));
		}
		return result;
	}
	
	/**
	 * 获得假期字符串(考虑到手动设置的日期)
	 * @param week 星期（1-7）依次为星期一、二...等.数组
	 * @param year 年份
	 * @param month 月份(1-12)一月、二月....等
	 * @return 返回某年某月的节假日字符串,用','分割
	 * @return
	 */
	public String getHolidayContainSeted(int[] week,int year,int month,Long calendar_id){
		String hdnWwdata = getHolidays(week, year, month);
		String new_hdnWwdata = "";
		SysCalendarUtil util = new SysCalendarUtil();
		List<SysCalendarDetailInfoModel> list = sysworkcalendarservice.queryHolidaysBySeted(calendar_id, month,year);
		String[] dayHoliArray = new String[list.size()];
		String[] dayWorkArray = new String[list.size()];
		int countH = 0;
		int countW = 0;
		if(list!=null){
			//1工作  0休息
			for(SysCalendarDetailInfoModel model :list){
				if(model.getActualDayType()=="1"||"1".equals(model.getActualDayType())){
					dayWorkArray[countH] = util.getDayfromDate(model.getCDate());
					countH++;
				}
				if(model.getActualDayType()=="0"||"0".equals(model.getActualDayType())){
					dayHoliArray[countW] = util.getDayfromDate(model.getCDate());
					countW++;
				}
			}
		}
		//增加节假日和刨除工作日
		new_hdnWwdata = util.controlDays(hdnWwdata, dayHoliArray, dayWorkArray);
		return new_hdnWwdata;
	}
	/**
	 * 判断某天是星期几
	 * @param pTime
	 * @return 1：星期一、2:星期二、依次类推
	 * @throws Exception
	 */
	public  int getDayForWeek(String pTime) {
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
		   dayForWeek = 7;
		  }else{
		   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
		 }
	/**
	 * 判断某天是星期几
	 * @param pTime
	 * @return 1：星期一、2:星期二、依次类推
	 * @throws Exception
	 */
	public  int getDayForWeek(Date pTime) {
		  
		  Calendar c = Calendar.getInstance();
		  c.setTime(pTime);
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
		   dayForWeek = 7;
		  }else{
		   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
		 }
	/**
	 * 返回0则是正常的工作日,返回1则是正常的节假日
	 * @param day
	 * @return
	 */
	public int isWeekly(int day){
		int flag = 0;
		if(day>0&&day<6){
			flag = 0;
		}else{
			flag = 1;
		}
		return flag;
	}
	/**
	 * 获得节假日星期数组
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] getHolidaysArray(SysCalendarBaseInfoModel model){
		@SuppressWarnings("unused")
		int length = 0;
		HashMap map = new HashMap();
		String[] array = null;
		
		if(model!=null){
			String mon = model.getMon();//0为休息
			String tues = model.getTues();
			String wed = model.getWed();
			String turs = model.getTurs();
			String fri = model.getFri();
			String sat = model.getSat();
			String sun = model.getSun();
			String temp = "";
			if(mon=="0"||"0".equals(mon)||mon==null){
				temp = temp + SysCalendarConstants.MONDAY+",";
			}
			if(tues=="0"||"0".equals(tues)||tues==null){
				temp = temp + SysCalendarConstants.TUESDAY+",";
			}
			if(wed=="0"||"0".equals(wed)||wed==null){
				temp = temp + SysCalendarConstants.WEDSDAY+",";
			}
			if(turs=="0"||"0".equals(turs)||turs==null){
				temp = temp + SysCalendarConstants.TURSDAY+",";
			}
			if(fri=="0"||"0".equals(fri)||fri==null){
				temp = temp + SysCalendarConstants.FRIDAY+",";
			}
			if(sat=="0"||"0".equals(sat)||sat==null){
				temp = temp + SysCalendarConstants.SATDAY+",";
			}
			if(sun=="0"||"0".equals(sun)||sun==null){
				temp = temp + SysCalendarConstants.SUNDAY+",";
			}
			if(!"".equals(temp)){
				temp = temp.substring(0,temp.lastIndexOf(","));
			}
			if(temp!=null&&!"".equals(temp)){
				
				array = temp.split(",");
				length = array.length;
			}
		}
		
		int[] arrayInt = new int[length];
		for(int i=0;i<arrayInt.length;i++){
			arrayInt[i]=Integer.parseInt(array[i]);
		}
		return arrayInt;
	}
	/**
	 * 返回日期是几号
	 */
	public String getDayfromDate(Date date){
		String day = "0";
		Calendar c = Calendar.getInstance();
		if(date!=null){
			c.setTime(date);
			day = String.valueOf(c.get(Calendar.DATE));
		}
		return day;
	}
	/**
	 * 增加设置休息日和刨除休息日
	 * @param holidays
	 * @param workdays
	 * @return
	 */
	public String controlDays(String hdnWwdata,String[] holidays,String[] workdays){
		String str = "";
		String[] hdnWwdataArr = hdnWwdata.split(",");
		HashSet<String> set = new HashSet<String>();
		for(int j=0;j<hdnWwdataArr.length;j++){
			set.add(hdnWwdataArr[j]);
		}
		for(int i =0;i<holidays.length;i++){
			if(holidays[i]!=null&&!"".equals(holidays[i])){
				set.add(holidays[i]);
			}
		}
		for(String work:workdays){
			if(work!=null&&!"".equals(work)){
				if(set.contains(work)){
					set.remove(work);
				}
			}
		}
		Iterator<String> it = set.iterator();  
		while (it.hasNext()) {  
		  String strTemp = it.next();  
		  str = str + strTemp +",";
		}
		if(!"".equals(str)){
			str = str.substring(0,str.lastIndexOf(","));
		}
		return str;
	}
	/**
	 * 获得指定人的工作日历,若没有设置特殊日历,则用系统默认日历
	 * @return
	 */
	public Long getUserCalendar(String userid){
		Long calendarId = 0L;
		//获得系统默认日历
		SysCalendarBaseInfoModel modelDefault = new SysCalendarBaseInfoModel();
		//获得非系统默认日历
		List<SysCalendarBaseInfoModel> list = sysworkcalendarservice.queryCalendarListIsStatus();
		boolean flag = false;
		if(list!=null&&list.size()>0){
			Collections.sort(list,Collections.reverseOrder());
			for(SysCalendarBaseInfoModel model:list){
				if("0".equals(model.getCalendarType())){
					modelDefault = model;
				}else{
					//获得的list已经按照时间降序排列,保证获得的是最后建立的日历在前面
					flag = this.checkUserInPurview(userid,model.getGrantUsers());
					if(flag == true){
						calendarId = model.getId();
						break;
					}
				}
			}
			//若没有特殊日历,则赋给人员系统默认日历
			if(flag == false){
				calendarId = modelDefault.getId();
			}
		}else{
			calendarId = null;
		}
		
		return calendarId;
	}
	/**
	 * 根据日历授权字段获得所有被授权人员ID
	 * @param grantUsers
	 */
	public HashSet<String> getUsersAddressByCalendar(String grantUsers){
		PurviewCommonUtil addressBookUtil = new PurviewCommonUtil();
		String json = grantUsers;
		List<String> listResult = new ArrayList<String>();
		List<String> list = getJsonList(json);
		for(String jsonElement : list){
			
			List<String> listE = addressBookUtil.getUserListFromAddressCode(jsonElement);
			if(listE!=null&&listE.size()>0){
				
				listResult.addAll(listE);
			}
		}
		//去重复
		HashSet<String> setResult = new HashSet<String>(listResult);
		return setResult;
	}
	/**
	 * 获得部门、人员、公司、角色、分组JSON的集合 {DEPT{1,2},USER{LISI,ZHANGSAN}.....}
	 * @param code
	 * @return
	 */
	public List<String> getJsonList(String code){
		JSONObject jb = JSONObject.fromObject(code);
		JSONArray jsons = new JSONArray();
		String company = "";
		String user = "";
		String dept = "";
		String role = "";
		String group = "";
		List<String> list = new ArrayList<String>();
		if(code!=null&&!"".equals(code)){
			
			if(code.contains("COMPANY\":")){
				jsons = jb.getJSONArray("COMPANY");
				//DEPT:{6,7} ROLE:{1,2}
				String str = getElmentsJson(jsons);
				company = "COMPANY:{"+str+"}";
				list.add(company);
			}
			if(code.contains("USER\":")){
				jsons = jb.getJSONArray("USER");
				String str = getElmentsJson(jsons);
				user = "USER:{"+str+"}";
				list.add(user);
			}
			if(code.contains("DEPT\":")){
				jsons = jb.getJSONArray("DEPT");
				String str = getElmentsJson(jsons);
				dept = "DEPT:{"+str+"}";
				list.add(dept);
			}
			if(code.contains("ROLE\":")){
				jsons = jb.getJSONArray("ROLE");
				String str = getElmentsJson(jsons);
				role = "ROLE:{"+str+"}";
				list.add(role);
			}
			if(code.contains("GROUP\":")){
				jsons = jb.getJSONArray("GROUP");
				String str = getElmentsJson(jsons);
				group = "GROUP:{"+str+"}";
				list.add(group);
			}
		}
		return list;
		
	}
	/**
	 * 分别获得部门、人员、公司、角色、分组id 格式DEPT{1,2} USER{LISI,ZHANGSAN}.....
	 * @param jsons 字符串
	 * @return
	 */
	public String getElmentsJson(JSONArray jsons){
		String str = "";
		int jsonLength = jsons.size();
		 for (int i = 0; i < jsonLength; i++) {
	            JSONObject tempJson = JSONObject.fromObject(jsons.get(i));
	            String field = StringEscapeUtils.escapeSql(tempJson.getString("id"));
	            str = str +","+ field;
	     }
		 str = str.substring(1);
		return str;
	}
	/**
	 * 判断指定人是否有该日历权限
	 * @param userId
	 * @param code
	 * @return 
	 */
	public boolean checkUserInPurview(String userId, String code){
		boolean flag = false;
		if(code!=null){
			
			List<String> list = getJsonList(code);
			PurviewCommonUtil pUtil = new PurviewCommonUtil();
			if(list!=null&&list.size()>0){
				for(String str: list){
					flag = pUtil.checkUserInPurview(userId,str);
					if(flag==true){
						break;
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 获得系统默认日历
	 * @return
	 */
	public SysCalendarBaseInfoModel getCalendarDefault(){
		SysCalendarBaseInfoModel model =  sysworkcalendarservice.getCalendarDefault();
		return model;
	}
	/**
	 * 根据系统日历ID 获取休假日为星期几数组([7,6,0,0,0,0,0]代表休假日为星期日和星期六)
	 * @param calendar_id
	 * @return
	 */
	public int[] getHolidayToWeek(Long calendar_id){
		
		SysCalendarBaseInfoModel baseInfoModel  = sysworkcalendarservice.queryCalendarById(calendar_id);
		int[] week = new int[7];
		if(baseInfoModel!=null){
			int[] temp = this.getHolidaysArray(baseInfoModel);
			for(int i=0;i<temp.length;i++){
				week[i] = temp[i];
			}
		}
		return week;
	}
	/**
	 * 获得两个日期之间的节假日天数(包含系统日历和手动设置的节假日以及工作日)
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getHolidaysByBetweenDates(Date startDate,Date endDate,String userId){
		int countHDays = 0;//-111111111
		Calendar calendarFrom = Calendar.getInstance();
		Calendar calendarTo = Calendar.getInstance();
		calendarFrom.setTime(startDate);
		calendarTo.setTime(endDate);
		int startYear = calendarFrom.get(Calendar.YEAR);
		int startMonth = calendarFrom.get(Calendar.MONTH) + 1;
		int startDay = calendarFrom.get(Calendar.DATE);
		int endYear = calendarTo.get(Calendar.YEAR);
		int endMonth = calendarTo.get(Calendar.MONTH) + 1;
		int endDay = calendarTo.get(Calendar.DATE);
		String startMonthStr = "";
		String endMonthStr = "";
		Long carlendarId = this.getUserCalendar(userId);//获得日历ID
		if(carlendarId!=null){
			
			int week[] = new int[7];
			week = getHolidayToWeek(carlendarId);
			
			//若开始和结束是同一年
			if(startYear == endYear){
				for(int tempMonth = startMonth;tempMonth<=endMonth;tempMonth++){
					String strHolidays = getHolidayContainSeted(week, startYear, tempMonth, carlendarId);
					String[] temp = strHolidays.split(",");
					countHDays = countHDays + temp.length;
					if(tempMonth == startMonth){
						startMonthStr = strHolidays;
					}
					if(tempMonth == endMonth){
						endMonthStr = strHolidays;
					}
				}
			}else{//若开始和结束不是同一年
				int tempMonthStart = 1;//默认循环系数首次为1
				int tempMonthEnd = 12;//默认循环系数结束为12
				int tempYear = 0;//循环年份中间变量
				for(int i=startYear;i<=endYear;i++){
					tempYear = i;
					//若年份为开始日期的年份和结束日期的年份则要特殊计算,
					//如：2014-4-1 ~ 2016-5-6 那么循环的时候2014年计算假期天数要从4月份计起,同理2016计算假期天数要在5月结束
					if(i==startYear){
						tempMonthStart = startMonth;
					}
					if(i==endYear){
						tempMonthEnd = endMonth;
					}
					for(int m=tempMonthStart;m<=tempMonthEnd;m++){
						String strHolidays = getHolidayContainSeted(week, tempYear, m, carlendarId);
						String[] temp = strHolidays.split(",");
						countHDays = countHDays + temp.length;
						if(m == startMonth){
							startMonthStr = strHolidays;
						}
						if(m == endMonth){
							endMonthStr = strHolidays;
						}
					}
					//循环结束，重新赋值
					tempMonthStart = 1;//默认循环系数首次为1
					tempMonthEnd = 12;//默认循环系数结束为12
				}
			}
			//去掉开始月份和结束月份多余的天数
			if(startMonthStr!=null&&endMonthStr!=null&&!"".equals(startMonthStr)&&!"".equals(endMonthStr)){
				String[] startArray = startMonthStr.split(",");
				String[] endArray = endMonthStr.split(",");
				for(int i=0;i<startArray.length;i++){
					if(Integer.valueOf(startArray[i])<startDay && countHDays>0){
						countHDays --;
					}
				}
				for(int j=0;j<startArray.length;j++){
					if(Integer.valueOf(endArray[j])>endDay && countHDays>0){
						countHDays --;
					}
				}
			}
		}else{
			//若没查到,则返回错误编码
			countHDays = SysCalendarConstants.ERROR_DAYS;
		}
		return countHDays;
	}
	/**
	 * 获得设置的工作日和节假日 key:"0"为节假日,"1"为工作日
	 * @param calendarId
	 * @return
	 */
	public Map<String,List<SysCalendarDetailInfoModel>> getDatesSetedByCalendarId(Long calendarId,Date startDate,Date endDate){
		List<SysCalendarDetailInfoModel> list = sysworkcalendarservice.queryDatesSetedByCalendarId(calendarId,startDate,endDate);
		Map<String,List<SysCalendarDetailInfoModel>> map = new HashMap<String, List<SysCalendarDetailInfoModel>>();
		List<SysCalendarDetailInfoModel> listHol = new ArrayList<SysCalendarDetailInfoModel>();
		List<SysCalendarDetailInfoModel> listWork = new ArrayList<SysCalendarDetailInfoModel>();
		Iterator<SysCalendarDetailInfoModel> it = list.iterator(); 
		SysCalendarDetailInfoModel model = new SysCalendarDetailInfoModel();
		while(it.hasNext()){
			model = it.next();
			if("1".equals(model.getActualDayType())){
				listWork.add(model);
			}else{
				listHol.add(model);
			}
		}
		map.put("0", listHol);
		map.put("1", listWork);
		return map;
	}
	/**
	 * 获得时间戳
	 * @return
	 */
	public String getTimeStamp(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public Date strToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    logger.error(e,e);
	   }
	   return date;
	}
	/**
	 * 计算两个日期之间的天数(包含开始日期和结束日期天数)
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getDaysBetweenDates(Date startDate,Date endDate){
		int offset = 1;//日期偏移量
		int count = 0;
		if(startDate!=null&&endDate!=null){
			Long from = startDate.getTime();
			Long to = endDate.getTime();
			count = (int) ((to - from) / (1000 * 60 * 60 * 24));
			count = count + offset;
		}
		return count;
	}
	/**
	 * 判断星期是否包含于日期数组
	 */
	public boolean isContainWeek(int[]week,int dayOfWeek){
		boolean flag = false;
		if(week!=null){
			for(int i=0;i<week.length;i++){
				if(dayOfWeek == week[i]){
					flag = true;
					break;
				}else{
					flag = false;
				}
				
			}
		}
		return flag;
	}
	/**
	 * 判断是否在设置节假日或者工作的范围内
	 * @param list
	 * @return
	 */
	public boolean isContainDate(List<SysCalendarDetailInfoModel> list,Date _date){
		boolean flag = false;
		if(list.size()>0&&list!=null){
			for(SysCalendarDetailInfoModel model:list){
				if(model.getCDate().compareTo(_date) == 0){
					flag = true;
					break;
				}else{
					flag = false;
				}
			}
		}
		return flag;
	}
	/**
	 * 判断指定时间是否在两个时间区间内
	 * @param dateBefore
	 * @param dateAfter
	 * @param date
	 * @return true:在区间内  false: 不在区间内
	 */
	public boolean isBetweenTwoDates(Date dateBefore,Date dateAfter,Date date){
		boolean flag = false;
		if(dateBefore!=null&&dateAfter!=null&&date!=null){
			if(date.before(dateBefore) && date.after(dateAfter)){
				flag = true;
			}else{
				flag = false;
			}
		}
		//考虑相等的情况下
		if(flag == false){
			if(dateBefore.compareTo(date)==0 || dateAfter.compareTo(date)==0){
				flag = true;
			}
		}
		return flag ;
	}
	/**
	 * 日期格式转化为字符串
	 * @param date
	 * @return
	 */
	public String dateToStr(Date date){
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 String dateStr = "";
		 if(date!=null){
			 dateStr = format.format(date);
		 }
		 return dateStr;
	}
	
	
	public static void main(String[] args) throws ParseException{
		int offset = 1;//日期偏移量
		String startDate = "2014-1-10";
		String endDate = "2014-1-15";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Long from = df.parse(startDate).getTime();
		Long to = df.parse(endDate).getTime();
		int count = (int) ((to - from) / (1000 * 60 * 60 * 24));
		count = count + offset; //加一天
		
		//测试前十天
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(df.parse(endDate));
		calendar.add(Calendar.DATE, 1 + offset);//要减一天
	}
}
