package com.iwork.plugs.syscalendar.action;
import com.iwork.core.util.ResponseUtil;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.apache.struts2.ServletActionContext;

import com.iwork.plugs.syscalendar.constans.SysCalendarConstants;
import com.iwork.plugs.syscalendar.model.SysCalendarBaseInfoModel;
import com.iwork.plugs.syscalendar.model.SysCalendarDetailInfoModel;
import com.iwork.plugs.syscalendar.service.SysWorkCalendarService;
import com.iwork.plugs.syscalendar.util.SysCalendarUtil;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 查询日历列表
 * @author WangJianhui
 *
 */
public class SysWorkCalendarAction extends ActionSupport{
	
	private SysWorkCalendarService sysworkcalendarservice;
	private List<SysCalendarBaseInfoModel> queryCalendarList;
	private SysCalendarBaseInfoModel calendar;
	private SysCalendarDetailInfoModel detailCalendar;
	private Long id;
	private String calendarName;
	private String describ;
	private String ts;
	private String sun;
	private String mon;
	private String tues;
	private String wed;
	private String turs;
	private String fri;
	private String sat;
	private Date expDateFrom;
	private Date expDateTo;
	private String status;
	private Long workTimeFrom;
	private Long workTimeTo;
	private String grantUsers;
	private String calendarType;
	private Long year;
	private HashMap mapHolidays;
	private String _date;//增加工作或节假日日期
	private String ids;
	
	public String list(){
		queryCalendarList = sysworkcalendarservice.queryCalendarList();
		return SUCCESS;
	}
	public String tab_list(){
		return SUCCESS;
	}
	
	
	
	/**
	 * 增加新的工作日历
	 * @return
	 */
	public void addCalendar(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String msg = "succ";
		boolean flag = false;
		calendar = changeNull(calendar);
		calendar.setTs(SysCalendarUtil.getInstance().getTimeStamp());
		calendar.setCreateTime(SysCalendarUtil.getInstance().getTimeStamp());
		calendar.setGrantUsers(this.getGrantUsers());
		Date dateFromD = calendar.getExpDateFrom();
		Date dateToD = calendar.getExpDateTo();
		String calendar_type = calendar.getCalendarType();
		if(calendar_type!=null){
			if(calendar_type=="0"||"0".equals(calendar_type)){
				flag = sysworkcalendarservice.isExsitsDefaultCal(dateFromD,dateToD);
			}
		}
		if(flag == true){
			msg = "exsits";
		}else{
			Long id = sysworkcalendarservice.addCalendar(calendar);
			if(id!=null){
				msg = "succ";
			}else{
				msg = "false";
			}
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 点击创建按钮跳转
	 * @return
	 */
	public String createCalendar(){
		return SUCCESS;
	}
	public String showCalendar(){
		return SUCCESS;
	}
	public String updateCalendar(){
		SysCalendarBaseInfoModel model = new SysCalendarBaseInfoModel();
		model = sysworkcalendarservice.queryCalendarById(id);
		if(model!=null){
			model = changeNull(model);
		}
		calendarName = model.getCalendarName();
		describ = model.getDescrib();
		sun = model.getSun();
		mon = model.getMon();
		tues = model.getTues();
		wed = model.getWed();
		turs = model.getTurs();
		fri = model.getFri();
		sat = model.getSat();
		expDateFrom = model.getExpDateFrom();
		expDateTo = model.getExpDateTo();
		status = model.getStatus();
		workTimeFrom = model.getWorkTimeFrom();
		workTimeTo = model.getWorkTimeTo();
		grantUsers = model.getGrantUsers();
		calendarType = model.getCalendarType();
		return SUCCESS;
	}
	/**
	 * 刪除日历方法
	 * @return
	 */
	public void deleteCalendar(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String idss = request.getParameter("check_id");
		String msg = "";
		int count = 0;
		if(idss!=null&&!"".equals(idss)){
			String[] _ids = idss.split(",");
			count = sysworkcalendarservice.deleteCalendar(_ids);
		}
		if(count>0){
			msg = "succ";
		}
		ResponseUtil.write(msg);
		
	}
	/**
	 * 更新工作日历
	 * @return
	 */
	public void updateCalendarById(){
		String msg = "";
		boolean flag = false;
		calendar.setGrantUsers(this.getGrantUsers());
		if(calendar!=null){
			calendar = changeNull(calendar);
		}
		calendar.setTs(SysCalendarUtil.getInstance().getTimeStamp());
		//true:存在系统日历
		String calendar_type = calendar.getCalendarType();
		if(calendar_type!=null){
			if(calendar_type=="0"||"0".equals(calendar_type)){
				flag = sysworkcalendarservice.isExsitsDefaultCal_update(calendar.getId(),calendar);
			}
		}
		if(flag == true){
			msg = "exsits";
		}else{
			
			Long id = sysworkcalendarservice.updateCalendarById(calendar);
			if(id!=null){
				msg="succ";
			}else{
				msg = "false";
			}
		}
		//SysCalendarAPI.getInstance().test_API();测试API的测试用例
		ResponseUtil.write(msg);
	}
	
	
	
	/**
	 * 初始化和点击上一年和下一年
	 * @return
	 */
	public String queryHolidaysByYear(){
		HttpServletRequest request = ServletActionContext.getRequest();
		mapHolidays = this.getHolidaysMap();
		request.setAttribute("mapHolidays", mapHolidays);
		return SUCCESS;
	}
	/**
	 * 增加节假日方法
	 */
	public void addHolidays(){
		String msg = "1";
		SysCalendarUtil util = new SysCalendarUtil();
		int dayOfWeek = 0;//星期几
		String workFlag = "0";//增加节假日,标识为 0
		String common_day_type = "0";//是否为周末 1为周末,0不为周末
		Date _meDate = new Date();
		if(_date!=null&&!"".equals(_date)){
			
			dayOfWeek = util.getDayForWeek(_date);
			common_day_type = String.valueOf(util.isWeekly(dayOfWeek));//判断是否为周末
			_meDate = util.strToDate(_date);
		}
		String tempDayOfWeek = String.valueOf(dayOfWeek);
		SysCalendarDetailInfoModel dCalendar = new SysCalendarDetailInfoModel();
		dCalendar.setDayOfWeek(tempDayOfWeek);
		dCalendar.setCalendarId(id);//主表ID
		dCalendar.setCommonDayType(common_day_type);
		dCalendar.setActualDayType(workFlag);
		dCalendar.setTs(util.getTimeStamp());//加入时间戳
		dCalendar.setCDate(_meDate);
		sysworkcalendarservice.saveHolidaysOrWorkDays(dCalendar);
		ResponseUtil.write(msg);
	}
	
	/**
	 * 增加工作日方法
	 */
	
	public void addWorkdays(){
		String msg = "";
		SysCalendarUtil util = new SysCalendarUtil();
		int dayOfWeek = 0;//星期几
		String workFlag = "1";//增加工作日,标识为 1
		String common_day_type = "0";//是否为周末 1为周末,0不为周末
		Date _meDate = new Date();
		if(_date!=null&&!"".equals(_date)){
			
			dayOfWeek = util.getDayForWeek(_date);
			common_day_type = String.valueOf(util.isWeekly(dayOfWeek));//判断是否为周末
			_meDate = util.strToDate(_date);
		}
		String tempDayOfWeek = String.valueOf(dayOfWeek);
		SysCalendarDetailInfoModel dCalendar = new SysCalendarDetailInfoModel();
		dCalendar.setDayOfWeek(tempDayOfWeek);
		dCalendar.setCalendarId(id);//主表ID
		dCalendar.setCommonDayType(common_day_type);
		dCalendar.setActualDayType(workFlag);
		dCalendar.setTs(util.getTimeStamp());//加入时间戳
		dCalendar.setCDate(_meDate);
		sysworkcalendarservice.saveHolidaysOrWorkDays(dCalendar);
		msg = "succ";
		ResponseUtil.write(msg);
	}
	
	/**
	 * 获得节假日MAP
	 * @return
	 */
	public HashMap<String, String> getHolidaysMap(){
		SysCalendarUtil calendarUtil = new SysCalendarUtil();
		//查询该日历的节假日设置
		int[] week = new int[7];
		week = SysCalendarUtil.getInstance().getHolidayToWeek(id);
        int tempYear = 0;
        String hdnWwdata1 = "";
        String hdnWwdata2 = "";
        String hdnWwdata3 = "";
        String hdnWwdata4 = "";
        String hdnWwdata5 = "";
        String hdnWwdata6 = "";
        String hdnWwdata7 = "";
        String hdnWwdata8 = "";
        String hdnWwdata9 = "";
        String hdnWwdata10 = "";
        String hdnWwdata11 = "";
        String hdnWwdata12 = "";
        String hdnWwdata = "";
        if(year==null){
        	Calendar cal = Calendar.getInstance();
            int yearTmp = cal.get(Calendar.YEAR);
        	year = Long.parseLong(String.valueOf(yearTmp));
        }
        if(year!=null){
        	tempYear = year.intValue();
        	hdnWwdata1 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.JANUARY, id);
        	hdnWwdata2 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.FEBRUARY, id);
        	hdnWwdata3 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.MARCH, id);
        	hdnWwdata4 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.APRIL, id);
        	hdnWwdata5 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.MAY, id);
        	hdnWwdata6 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.JUNE, id);
        	hdnWwdata7 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.JULY, id);
        	hdnWwdata8 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.AUGUST, id);
        	hdnWwdata9 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.SEPTEMBER, id);
        	hdnWwdata10 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.OCTOBER, id);
        	hdnWwdata11 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.NOVEBER, id);
        	hdnWwdata12 = calendarUtil.getHolidayContainSeted(week, tempYear, SysCalendarConstants.DECEBER, id);
        }
        //end
        HashMap<String, String> map = new HashMap<String,String>();
        map.put("hdnWwdata1", hdnWwdata1);
        map.put("hdnWwdata2", hdnWwdata2);
        map.put("hdnWwdata3", hdnWwdata3);
        map.put("hdnWwdata4", hdnWwdata4);
        map.put("hdnWwdata5", hdnWwdata5);
        map.put("hdnWwdata6", hdnWwdata6);
        map.put("hdnWwdata7", hdnWwdata7);
        map.put("hdnWwdata8", hdnWwdata8);
        map.put("hdnWwdata9", hdnWwdata9);
        map.put("hdnWwdata10", hdnWwdata10);
        map.put("hdnWwdata11", hdnWwdata11);
        map.put("hdnWwdata12", hdnWwdata12);
        return map;
	}
	
	/**
	 * 查询条件年的节假日
	 * @param model
	 * @return
	 */
	
	public void getHolidays(){
        HashMap<String, String> map = new HashMap<String,String>();
        map = this.getHolidaysMap();
        String json = getHolidaysJson(map);
        ResponseUtil.write(json);
	}
	/**
	 * 获得json字符串
	 * @return
	 */
	public String getHolidaysJson(HashMap<String, String> map){
		String json = "";
		if(map.size()>0&&map!=null){
			json = "[{id:'hdnWwdata1',value:'"+map.get("hdnWwdata1")+"'},"+"{id:'hdnWwdata2',value:'"+map.get("hdnWwdata2")+"'},"+
			"{id:'hdnWwdata3',value:'"+map.get("hdnWwdata3")+"'},"+"{id:'hdnWwdata4',value:'"+map.get("hdnWwdata4")+"'},"+
			"{id:'hdnWwdata5',value:'"+map.get("hdnWwdata5")+"'},"+"{id:'hdnWwdata6',value:'"+map.get("hdnWwdata6")+"'},"+
			"{id:'hdnWwdata7',value:'"+map.get("hdnWwdata7")+"'},"+"{id:'hdnWwdata8',value:'"+map.get("hdnWwdata8")+"'},"+
			"{id:'hdnWwdata9',value:'"+map.get("hdnWwdata9")+"'},"+"{id:'hdnWwdata10',value:'"+map.get("hdnWwdata10")+"'},"+
			"{id:'hdnWwdata11',value:'"+map.get("hdnWwdata11")+"'},"+"{id:'hdnWwdata12',value:'"+map.get("hdnWwdata12")+"'}]";
		}
		 return json;
	}
	public SysCalendarBaseInfoModel changeNull(SysCalendarBaseInfoModel model){
		if(model.getMon()==null||"".equals(model.getMon())){
			model.setMon("0");
		}
		if(model.getTues()==null||"".equals(model.getTues())){
			model.setTues("0");
		}
		if(model.getWed()==null||"".equals(model.getWed())){
			model.setWed("0");
		}
		if(model.getTurs()==null||"".equals(model.getTurs())){
			model.setTurs("0");
		}
		if(model.getFri()==null||"".equals(model.getFri())){
			model.setFri("0");
		}
		if(model.getSat()==null||"".equals(model.getSat())){
			model.setSat("0");
		}
		if(model.getSun()==null||"".equals(model.getSun())){
			model.setSun("0");
		}
		return model;
	}
	/**
	 * 获得最新的假期字符串(考虑到手动设置的日期)
	 * @param hdnWwdata
	 * @param year
	 * @param month
	 * @return
	 */
	public String cleanDays(String hdnWwdata,int year,int month,Long calendar_id){
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
	
	public SysWorkCalendarService getSysworkcalendarservice() {
		return sysworkcalendarservice;
	}
	public void setSysworkcalendarservice(
			SysWorkCalendarService sysworkcalendarservice) {
		this.sysworkcalendarservice = sysworkcalendarservice;
	}

	public List<SysCalendarBaseInfoModel> getQueryCalendarList() {
		return queryCalendarList;
	}

	public void setQueryCalendarList(
			List<SysCalendarBaseInfoModel> queryCalendarList) {
		this.queryCalendarList = queryCalendarList;
	}
	public SysCalendarBaseInfoModel getCalendar() {
		return calendar;
	}
	public void setCalendar(SysCalendarBaseInfoModel calendar) {
		this.calendar = calendar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getSun() {
		return sun;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public String getTues() {
		return tues;
	}

	public void setTues(String tues) {
		this.tues = tues;
	}

	public String getWed() {
		return wed;
	}

	public void setWed(String wed) {
		this.wed = wed;
	}

	public String getTurs() {
		return turs;
	}

	public void setTurs(String turs) {
		this.turs = turs;
	}

	public String getFri() {
		return fri;
	}

	public void setFri(String fri) {
		this.fri = fri;
	}

	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat = sat;
	}

	public Date getExpDateFrom() {
		return expDateFrom;
	}

	public void setExpDateFrom(Date expDateFrom) {
		this.expDateFrom = expDateFrom;
	}

	public Date getExpDateTo() {
		return expDateTo;
	}

	public void setExpDateTo(Date expDateTo) {
		this.expDateTo = expDateTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getWorkTimeFrom() {
		return workTimeFrom;
	}

	public void setWorkTimeFrom(Long workTimeFrom) {
		this.workTimeFrom = workTimeFrom;
	}

	public Long getWorkTimeTo() {
		return workTimeTo;
	}

	public void setWorkTimeTo(Long workTimeTo) {
		this.workTimeTo = workTimeTo;
	}

	public String getGrantUsers() {
		return grantUsers;
	}

	public void setGrantUsers(String grantUsers) {
		this.grantUsers = grantUsers;
	}

	public String getCalendarType() {
		return calendarType;
	}

	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public HashMap getMapHolidays() {
		return mapHolidays;
	}

	public void setMapHolidays(HashMap mapHolidays) {
		this.mapHolidays = mapHolidays;
	}

	public String get_date() {
		return _date;
	}

	public void set_date(String date) {
		_date = date;
	}

	public SysCalendarDetailInfoModel getDetailCalendar() {
		return detailCalendar;
	}

	public void setDetailCalendar(SysCalendarDetailInfoModel detailCalendar) {
		this.detailCalendar = detailCalendar;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	

	 
	
	
	
}
