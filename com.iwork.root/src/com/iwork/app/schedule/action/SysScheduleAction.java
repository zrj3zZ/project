package com.iwork.app.schedule.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.quartz.SchedulerException;

import com.iwork.app.schedule.service.SysScheduleService;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 计划任务Action
 * @author LuoChuan
 *
 */
public class SysScheduleAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8617450798366766629L;
	private SysScheduleService sysScheduleService;
	protected String type;				//操作类型
	protected String id;				//计划任务ID
	protected String planName;			//计划任务名称
	protected String planPri;			//计划任务优先级
	protected String classz;			//计划任务执行类
	protected String usefulLife_start;	//有效期开始日期
	protected String usefulLife_end;	//有限期结束日期
	protected String planDesc;			//计划任务描述
	protected String repeatNum;			//失败补偿次数
	protected String ruleType;			//执行频率
	protected String executeTime;		//执行时间
	protected String executePoint;		//执行时间点
	protected String intervalMinutes;	//AWS执行的时间间隔
	protected String dayOfWeekStr;		//每周第几天
	protected String dayOfMonthStr;		//每月第几天
	protected String monthOfQuarter;	//每季度第几月
	protected String dayOfQuarterMonth;	//每季度第几月的第几天
	protected String monthOfYear;		//每年第几月
	protected String dayOfYearMonth;	//每年第几月的第几天
	protected String hour;				//执行小时
	protected String minute;			//执行分钟
	protected String flag;				//状态
	protected Page page = new Page(); 	//分页类
	protected int totalPages;			//总页数
	protected int curPage;				//显示第几页
	protected int totalRecords;			//总记录数
	private String infolist;
	protected List<Map<String,Object>> dataRows = new ArrayList<Map<String,Object>>();//保存实际的数据

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * 显示系统计划任务
	 * @return String
	 */
	public String showSchedule() {
		return SUCCESS;
	}
	
	/**
	 * 获得计划任务列表
	 * @return String
	 */
	public void getScheduleList() {
		String scheduleList = sysScheduleService.getSchedule();
		int lenght = scheduleList.length();
		if(lenght>3){
			ResponseUtil.write(scheduleList);
		}
		
	}
	
	/**
	 * 保存计划任务
	 * @return String
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public String saveSchedule() throws SchedulerException, ParseException {
		Map<String,Object> map = new HashMap<String,Object>();
		String executeTime = "";
		String executePoint = "";
		//根据页面上传过来的值，生成执行日期、执行时间
		map = generateExecuteTimeAndDate(ruleType, dayOfWeekStr, dayOfMonthStr, monthOfQuarter, dayOfQuarterMonth, monthOfYear, dayOfYearMonth, hour, minute);
		if (null != map && !map.isEmpty()) {
			executeTime = map.get("executeTime").toString();
			executePoint = map.get("executePoint").toString();
		}
		sysScheduleService.saveSchedule(type, id, planName, planPri, classz,
											usefulLife_start, usefulLife_end, planDesc, repeatNum,
											ruleType, intervalMinutes, executeTime, executePoint);
		return SUCCESS;
	}
	
	/**
	 * 改变计划任务状态
	 * 
	 * @return String
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public String changeStatus() throws SchedulerException, ParseException {
		
		Map<String,Object> map = new HashMap<String,Object>();
		String executeTime = "";
		String executePoint = "";
		map = generateExecuteTimeAndDate(ruleType, dayOfWeekStr, dayOfMonthStr, monthOfQuarter, dayOfQuarterMonth, monthOfYear, dayOfYearMonth, hour, minute);
		if (!map.isEmpty()) {
			executeTime = map.get("executeTime").toString();
			executePoint = map.get("executePoint").toString();
		}
		
		String id = this.getId();
		if (null != id && !"".equals(id)) {
			sysScheduleService.changeStatus(flag,id,ruleType,intervalMinutes,executeTime,executePoint);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 模拟使用
	 * @return
	 */
	public String simulate() {
		String executeType = "1";//手动执行
		sysScheduleService.simulate(id,flag,executeType,repeatNum);
		return SUCCESS;
	}
	
	/**
	 * 测试类
	 * @return
	 * @throws Exception 
	 */
	public void testClass() throws Exception {
		int isSuccess = sysScheduleService.testClass(classz);
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();		
		out.print("<script>window.location.href='sys_schedule.action'</script>");
		out.flush();  
        out.close();
	}
	
	/**
	 * 查看日志
	 * @return
	 */
	public String showLog() {
		String infoList=sysScheduleService.getGridScript(id);
        this.setInfolist(infoList);
		return SUCCESS;
	}
	
	/**
	 * 获得日志列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void getLogList() {
		Page page = this.getPage();
		String logList = "";
		String id = this.getId();
		if(id!=null && !"".equals(id)){
			 logList = sysScheduleService.showLog(id,page);
		}
		ResponseUtil.write(logList.substring(1, logList.length()-1));
		
	}
	
	/**
	 * 根据页面上传过来的值，生成执行日期、执行时间
	 * @return Map
	 */
	public Map<String,Object> generateExecuteTimeAndDate(String ruleType,
											   String dayOfWeekStr, String dayOfMonthStr, String monthOfQuarter,
											   String dayOfQuarterMonth, String monthOfYear,
											   String dayOfYearMonth, String hour, String minute) {
		Map<String,Object> map = new HashMap<String,Object>();
		String executeTime = "";
		String executePoint = "";
		if(hour!=null&&!hour.trim().equals("")&&minute!=null&&!minute.trim().equals("")){
			executeTime = hour + minute; // 组装执行时间 hhmm	
		}
		if (ruleType != null) {
			if (ruleType.trim().equals("1")) { // 周
				executePoint = dayOfWeekStr;
			}
			if (ruleType.trim().equals("2")) { // 月
				executePoint = dayOfMonthStr;
			}
			if (ruleType.trim().equals("3")) { // 季度
				executePoint = monthOfQuarter + "," + dayOfQuarterMonth;
			}
			if (ruleType.trim().equals("4")) { // 年
				executePoint = monthOfYear + "," + dayOfYearMonth;
			}
		}
		map.put("executeTime", executeTime);
		map.put("executePoint", executePoint);
		
		return map;
	}
	
	public SysScheduleService getSysScheduleService() {
		return sysScheduleService;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getPlanPri() {
		return planPri;
	}

	public void setPlanPri(String planPri) {
		this.planPri = planPri;
	}

	public String getClassz() {
		return classz;
	}

	public void setClassz(String classz) {
		this.classz = classz;
	}

	public String getUsefulLife_start() {
		return usefulLife_start;
	}

	public void setUsefulLife_start(String usefulLife_start) {
		this.usefulLife_start = usefulLife_start;
	}

	public String getUsefulLife_end() {
		return usefulLife_end;
	}

	public void setUsefulLife_end(String usefulLife_end) {
		this.usefulLife_end = usefulLife_end;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public String getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(String repeatNum) {
		this.repeatNum = repeatNum;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
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

	public void setSysScheduleService(SysScheduleService sysScheduleService) {
		this.sysScheduleService = sysScheduleService;
	}

	public String getDayOfWeekStr() {
		return dayOfWeekStr;
	}

	public void setDayOfWeekStr(String dayOfWeekStr) {
		this.dayOfWeekStr = dayOfWeekStr;
	}

	public String getDayOfMonthStr() {
		return dayOfMonthStr;
	}

	public void setDayOfMonthStr(String dayOfMonthStr) {
		this.dayOfMonthStr = dayOfMonthStr;
	}

	public String getMonthOfQuarter() {
		return monthOfQuarter;
	}

	public void setMonthOfQuarter(String monthOfQuarter) {
		this.monthOfQuarter = monthOfQuarter;
	}

	public String getDayOfQuarterMonth() {
		return dayOfQuarterMonth;
	}

	public void setDayOfQuarterMonth(String dayOfQuarterMonth) {
		this.dayOfQuarterMonth = dayOfQuarterMonth;
	}

	public String getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(String monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public String getDayOfYearMonth() {
		return dayOfYearMonth;
	}

	public void setDayOfYearMonth(String dayOfYearMonth) {
		this.dayOfYearMonth = dayOfYearMonth;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<Map<String, Object>> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<Map<String, Object>> dataRows) {
		this.dataRows = dataRows;
	}

	public String getInfolist() {
		return infolist;
	}

	public void setInfolist(String infolist) {
		this.infolist = infolist;
	}

}
