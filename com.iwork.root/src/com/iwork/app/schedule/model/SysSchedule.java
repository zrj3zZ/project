package com.iwork.app.schedule.model;

import java.util.Date;

import com.iwork.app.schedule.log.model.SysScheduleLog;


/**
 * 系统计划任务模型
 * @author LuoChuan
 *
 */
public class SysSchedule implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static String DATABASE_ENTITY = "SysSchedule";  
	// Fields
	private String id;
	private String planName;
	private int isSystem;
	private int isDisabled;
	private String planDesc;
	private String classz;
	private int planPri;
	private Date addTime;
	private int ruleType;			// 规则类型 每天/每周/每月/每年/启动时
	private String executeTime;			// HH:MM::SS			
	private String usefulLifeStart;
	private String usefulLifeEnd;
	private String executePoint;
	private String intervalMinutes;
	private int repeatNum;
	private String taskLock;
	private String executeType;
	private SysScheduleLog SysScheduleLog;//运行日志
	
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
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}
	public String getClassz() {
		return classz;
	}
	public void setClassz(String classz) {
		this.classz = classz;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public String getUsefulLifeStart() {
		return usefulLifeStart;
	}
	public void setUsefulLifeStart(String usefulLifeStart) {
		this.usefulLifeStart = usefulLifeStart;
	}
	public String getUsefulLifeEnd() {
		return usefulLifeEnd;
	}
	public void setUsefulLifeEnd(String usefulLifeEnd) {
		this.usefulLifeEnd = usefulLifeEnd;
	}
	public String getExecutePoint() {
		return executePoint;
	}
	public void setExecutePoint(String executePoint) {
		this.executePoint = executePoint;
	}
	public String getIntervalMinutes() {
		return intervalMinutes;
	}
	public void setIntervalMinutes(String intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
	}
	public String getTaskLock() {
		return taskLock;
	}
	public void setTaskLock(String taskLock) {
		this.taskLock = taskLock;
	}
	public int getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(int isSystem) {
		this.isSystem = isSystem;
	}
	public int getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(int isDisabled) {
		this.isDisabled = isDisabled;
	}
	public int getPlanPri() {
		return planPri;
	}
	public void setPlanPri(int planPri) {
		this.planPri = planPri;
	}
	public int getRuleType() {
		return ruleType;
	}
	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}
	public int getRepeatNum() {
		return repeatNum;
	}
	public void setRepeatNum(int repeatNum) {
		this.repeatNum = repeatNum;
	}
	public String getExecuteType() {
		return executeType;
	}
	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	public SysScheduleLog getSysScheduleLog() {
		return SysScheduleLog;
	}
	public void setSysScheduleLog(SysScheduleLog sysScheduleLog) {
		SysScheduleLog = sysScheduleLog;
	}
}
