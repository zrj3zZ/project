package com.iwork.app.schedule.log.model;

import com.iwork.core.db.ObjectModel;

/**
 * 系统计划任务模型
 * @author LuoChuan
 *
 */
public class SysScheduleLog implements java.io.Serializable,ObjectModel {

	private static final long serialVersionUID = 1L;
	public static String DATABASE_ENTITY = "SysScheduleLog";
	// Fields
	private String id;
	private String scheduleId;
	private String executeTime;
	private String endTime;
	private String status;
	private String logMemo;
	private String executeType;	// 执行方式： 0 自动执行， 1 手动执行
	private String logType;

	
	
	public static String getDATABASE_ENTITY() {
		return DATABASE_ENTITY;
	}
	public static void setDATABASE_ENTITY(String database_entity) {
		DATABASE_ENTITY = database_entity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogMemo() {
		return logMemo;
	}
	public void setLogMemo(String logMemo) {
		this.logMemo = logMemo;
	}
	public String getExecuteType() {
		return executeType;
	}
	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	
}
