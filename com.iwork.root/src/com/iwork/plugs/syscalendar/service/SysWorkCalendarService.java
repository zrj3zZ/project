package com.iwork.plugs.syscalendar.service;

import java.util.Date;
import java.util.List;

import com.iwork.plugs.syscalendar.dao.SysCalendarDao;
import com.iwork.plugs.syscalendar.model.SysCalendarBaseInfoModel;
import com.iwork.plugs.syscalendar.model.SysCalendarDetailInfoModel;

public class SysWorkCalendarService {
	private SysCalendarDao syscalendardao;
	
	public List<SysCalendarBaseInfoModel> queryCalendarList(){
		List<SysCalendarBaseInfoModel> list = syscalendardao.queryCalendarList();
		return list;
	}
	
	public List<SysCalendarBaseInfoModel> queryCalendarListIsStatus(){
		List<SysCalendarBaseInfoModel> list = syscalendardao.queryCalendarListIsStatus();
		return list;
	}
	public List<SysCalendarDetailInfoModel> queryHolidaysBySeted(Long calendar_id ,int month,int year){
		List<SysCalendarDetailInfoModel> list = syscalendardao.queryHolidaysBySeted(calendar_id, month,year);
		return list;
	}
	public SysCalendarBaseInfoModel queryCalendarById(Long id){
		SysCalendarBaseInfoModel model = syscalendardao.queryCalendarById(id);
		return model;
	}
	public SysCalendarBaseInfoModel getCalendarDefault(){
		SysCalendarBaseInfoModel model = syscalendardao.getCalendarDefault();
		return model;
	}
	public List<SysCalendarDetailInfoModel> queryDatesSetedByCalendarId(Long calendarId,Date startDate,Date endDate){
		List<SysCalendarDetailInfoModel> list = syscalendardao.queryDatesSetedByCalendarId(calendarId,startDate,endDate);
		return list;
	}
	public String saveHolidaysOrWorkDays(SysCalendarDetailInfoModel detailCalendar){
		String msg = syscalendardao.saveHolidaysOrWorkDays(detailCalendar);
		return msg;
	}
	public int deleteCalendar(String[] ids){
		int count = syscalendardao.deleteCalendar(ids);
		return count;
	}
	public Long updateCalendarById(SysCalendarBaseInfoModel infoModel){
		Long id = syscalendardao.updateCalendarById(infoModel);
		return id;
	}
	public boolean isExsitsDefaultCal(Date dateFrom,Date dateTo){
		boolean flag = syscalendardao.isExsitsDefaultCal(dateFrom,dateTo);
		return flag;
	}
	public boolean isExsitsDefaultCal_update(Long self_id,SysCalendarBaseInfoModel model){
		boolean flag = syscalendardao.isExsitsDefaultCal_update(self_id,model);
		return flag;
	}
	public Long addCalendar(SysCalendarBaseInfoModel infoModel){
		Long id = syscalendardao.addCalendar(infoModel);
		return id;
	}
	public SysCalendarDao getSyscalendardao() {
		return syscalendardao;
	}
	public void setSyscalendardao(SysCalendarDao syscalendardao) {
		this.syscalendardao = syscalendardao;
	}
	
	
	
	
}
