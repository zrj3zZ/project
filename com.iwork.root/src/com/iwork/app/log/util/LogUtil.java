package com.iwork.app.log.util;

import java.util.Date;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.operationlog.model.SysOperateLog;
import com.iwork.app.log.operationlog.service.SysOperateLogService;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;

public class LogUtil {
	private static LogUtil instance;  
	private static SysOperateLogService sysOperateLogService;
	public final static String CN_FILENAME = "/common.properties";
    private static Object lock = new Object();  
	 public static LogUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new LogUtil();  
	                }
	            }  
	        }
	        if(sysOperateLogService==null){
	        	sysOperateLogService = (SysOperateLogService)SpringBeanUtil.getBean("sysOperateLogService");
	        }
	        return instance;  
	 }
	 
	 public void addLog(Long indexid,String loginfo,String memo){
		 String isLog = ConfigUtil.readValue(CN_FILENAME,"isLog");// 获取连接网址配置
		 if("1".equals(isLog)){
			 SysOperateLog log = new SysOperateLog();
			 String userid = UserContextUtil.getInstance().getCurrentUserId();
			 log.setId(UUIDUtil.getUUID());
			 log.setUserid(userid);
			 log.setCreatedate(new Date());
			 log.setIndexid(indexid);
			 log.setLoginfo(loginfo);
			 log.setMemo(memo);
			 sysOperateLogService.save(log);
		 }
	 }
	 
	 public void addLog(SysOperateLog solObj){
		 String isLog = ConfigUtil.readValue(CN_FILENAME,"isLog");// 获取连接网址配置
		 if("1".equals(isLog)){
			 sysOperateLogService.save(solObj);
		 }
	 }
}
