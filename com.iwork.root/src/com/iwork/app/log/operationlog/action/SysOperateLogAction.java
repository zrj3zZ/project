package com.iwork.app.log.operationlog.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.iwork.app.log.operationlog.model.SysOperateLog;
import com.iwork.app.log.operationlog.service.SysOperateLogService;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
public class SysOperateLogAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private SysOperateLogService sysOperateLogService;
	private String id;
	private String logType;
	private String loginfo;
	private String memo;
	private List<SysOperateLog> todayOperateLogList;//今天的操作记录
	private List<SysOperateLog> yestodayOperateLogList;//昨天的操作记录
	private List<SysOperateLog> latestOperateLogList;//最近的一天的操作记录
	private String specDate;//指定天的记录
	private String todayStr;//今天的日期
	private String yestodayStr;//昨天的日期
	private String latestdayStr;//最近的一天有记录的日期
	private static Logger logger = Logger.getLogger(SysOperateLogAction.class);
	/** 
	 * 删除用户指定天指定操作的全部记录
	 * @return
	 */
	public String deleteUserCurDayLog(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		if(null!=logType){
			SimpleDateFormat mdhmf = new SimpleDateFormat("yyyy-MM-dd");
			Date spec;
			try {
				spec = mdhmf.parse(specDate);
				boolean flag = sysOperateLogService.deleteUserSpecDayLog(spec, userid, logType);
				if(flag){
					ResponseUtil.writeTextUTF8("success");
				}else{
					ResponseUtil.writeTextUTF8("error");
				}
			} catch (ParseException e) {
				ResponseUtil.writeTextUTF8("error");
				logger.error(e,e);
			}
		}else{
			ResponseUtil.writeTextUTF8("error");
		}
		return null;
	}
	
	/**
	 * 删除用户的全部操作
	 * @return
	 */
	public String deleteUserAllLog(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		if(null!=logType){
			boolean flag = sysOperateLogService.deleteUserAllLog(userid, logType);
			if(flag){
				ResponseUtil.writeTextUTF8("success");
			}else{
				ResponseUtil.writeTextUTF8("error");
			}
		}else{
			ResponseUtil.writeTextUTF8("error");
		}
		return null;
	}
	
	/**
	 * 加载记录页
	 * @return
	 */
	public String index(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		SimpleDateFormat mdhmf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat chmf = new SimpleDateFormat("yyyy年MM月dd日");
		Date today = null;
		today = new Date();
		if(null!=logType){
			//没有指定天的记录
			if(null==specDate||specDate.equals(mdhmf.format(today))){
				specDate = mdhmf.format(today);
				//得到今天的事件
				todayStr = "今天 - "+chmf.format(today)+getWeekOfDate(today);
				todayOperateLogList = sysOperateLogService.getSpecDaySysOperateLog(today, userid, logType);
				//得到昨天的事件
				Calendar c = Calendar.getInstance();
				c.setTime(today);
				c.add(Calendar.DATE, -1);
				Date yestoday = c.getTime();
				yestodayStr = "昨天 - "+chmf.format(yestoday)+getWeekOfDate(yestoday);
				yestodayOperateLogList = sysOperateLogService.getSpecDaySysOperateLog(yestoday, userid, logType);
				//得到最近一天的事件
				String todayStrTemp = mdhmf.format(today);
				String yestodayStrTemp = mdhmf.format(yestoday);
				String latestStrTemp = sysOperateLogService.getLastestSpecOperteLogTime(new Date(),userid, logType);
				if(null==latestStrTemp||latestStrTemp.equals(todayStrTemp)||latestStrTemp.equals(yestodayStrTemp)){
					latestOperateLogList = null;
				}else{
					try {
						latestdayStr = chmf.format(mdhmf.parse(latestStrTemp))+getWeekOfDate(mdhmf.parse(latestStrTemp));
						latestOperateLogList = sysOperateLogService.getSpecDaySysOperateLog(mdhmf.parse(latestStrTemp), userid, logType);
					} catch (ParseException e) {
						latestOperateLogList = null;
						logger.error(e,e);
					}
				}
				return SUCCESS;
			}else{
				todayStr = null;
				todayOperateLogList = null;
				yestodayStr = null;
				yestodayOperateLogList = null;
				Date specDateTemp;
				try {
					specDateTemp = mdhmf.parse(specDate);
					latestOperateLogList = sysOperateLogService.getSpecDaySysOperateLog(specDateTemp, userid, logType);
					if(null!=latestOperateLogList&&latestOperateLogList.size()>0){
						SysOperateLog model = latestOperateLogList.get(0);
						latestdayStr = mdhmf.format(model.getCreatedate());
					}
				} catch (ParseException e) {
					latestdayStr = null;
					latestOperateLogList = null;
					logger.error(e,e);
				}
				return SUCCESS;
			}
		}else{
			return ERROR;
		}
	}
	/**
	 * 增加单条操作记录
	 * @return
	 */
	public String addOperateLog(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String currentUserId = uc.get_userModel().getUserid();
		if(null!=logType){
			String uuid = UUIDUtil.getUUID();
			Date createdate = new Date();
			SysOperateLog sol = new SysOperateLog();
			sol.setCreatedate(createdate);
			sol.setId(uuid);
			if(id==null){
				
			}else{
				sol.setIndexid(Long.parseLong(id));
			}
			sol.setLogtype(logType);
			sol.setUserid(currentUserId);
			if(null!=loginfo){
				sol.setLoginfo(loginfo);
			}
			if(null!=memo){
				try {
					sol.setMemo(java.net.URLDecoder.decode(memo,"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e,e);
				}
			}
			sysOperateLogService.save(sol);
			ResponseUtil.writeTextUTF8("success");
		}else{
			ResponseUtil.writeTextUTF8("error");
		}
		return null;
	}
	
	/**
	 * 删除单条记录
	 * @return
	 */
	public String deleteOperateLog(){
		if(null==id){
			ResponseUtil.writeTextUTF8("error");
		}else{
			SysOperateLog sol = sysOperateLogService.getModel(id);
			if(null==sol){
				ResponseUtil.writeTextUTF8("error");
			}else{
				sysOperateLogService.delete(sol);
				ResponseUtil.writeTextUTF8("success");
			}
		}
		return null;
	}
	
	/**
	 * 根据一个日期，返回是星期几的字符串
	 * 
	 * @param sdate
	 * @return
	 */
	private String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
//===============================POJO==============================================
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SysOperateLogService getSysOperateLogService() {
		return sysOperateLogService;
	}
	public void setSysOperateLogService(SysOperateLogService sysOperateLogService) {
		this.sysOperateLogService = sysOperateLogService;
	}
	public String getLoginfo() {
		return loginfo;
	}
	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public List<SysOperateLog> getTodayOperateLogList() {
		return todayOperateLogList;
	}

	public void setTodayOperateLogList(List<SysOperateLog> todayOperateLogList) {
		this.todayOperateLogList = todayOperateLogList;
	}

	public List<SysOperateLog> getYestodayOperateLogList() {
		return yestodayOperateLogList;
	}

	public void setYestodayOperateLogList(List<SysOperateLog> yestodayOperateLogList) {
		this.yestodayOperateLogList = yestodayOperateLogList;
	}

	public List<SysOperateLog> getLatestOperateLogList() {
		return latestOperateLogList;
	}

	public void setLatestOperateLogList(List<SysOperateLog> latestOperateLogList) {
		this.latestOperateLogList = latestOperateLogList;
	}

	public String getSpecDate() {
		return specDate;
	}

	public void setSpecDate(String specDate) {
		this.specDate = specDate;
	}

	public String getTodayStr() {
		return todayStr;
	}

	public void setTodayStr(String todayStr) {
		this.todayStr = todayStr;
	}

	public String getYestodayStr() {
		return yestodayStr;
	}

	public void setYestodayStr(String yestodayStr) {
		this.yestodayStr = yestodayStr;
	}

	public String getLatestdayStr() {
		return latestdayStr;
	}

	public void setLatestdayStr(String latestdayStr) {
		this.latestdayStr = latestdayStr;
	}
	
}
