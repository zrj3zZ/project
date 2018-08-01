package com.iwork.app.schedule.dao;

import java.util.List;

import com.iwork.app.schedule.model.SysSchedule;
/**
 * 系统计划任务数据操作接口
 * @author LuoChuan
 *
 */
public interface ISysScheduleDAO {
	@SuppressWarnings("unchecked")
	public List getAllData();

	public int update(String param1, String param2, String param3,
			String param4, String param5, String param6, String param7,
			String param8, String param9, String param10, String param11,
			String param12, String param13);

	public int delete(String id);

	public void add(SysSchedule model);
}
