package com.iwork.app.schedule.dao;

import java.util.List;

import com.iwork.app.schedule.log.model.SysScheduleLog;

public interface ISysScheduleLogDAO {
	
	public void add(SysScheduleLog model);
	
	@SuppressWarnings("unchecked")
	public List getLogByCondition(String id, String date, String startRow, String pageSize);
}
