package com.iwork.app.log.operationlog.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.iwork.app.log.operationlog.dao.SysOperateLogDAO;
import com.iwork.app.log.operationlog.model.SysOperateLog;
import com.iwork.commons.util.UUIDUtil;
import org.apache.log4j.Logger;
public class SysOperateLogService {
	private static Logger logger = Logger.getLogger(SysOperateLogService.class);
	private SysOperateLogDAO sysOperateLogDAO;
	
	/**
	 * 获得指定用户指定操作最近一条记录产生的日期(除去当天和前一天以外最近一天记录的时间)
	 * @return
	 */
	public String getLastestSpecOperteLogTime(Date curDate,String userid,String logtype){
		if(null==userid||null==logtype){
			return null;
		}else{
			SimpleDateFormat mdhmf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(curDate);
			c.add(Calendar.DATE, -1);
			Date yestoday;
			try {
				yestoday = mdhmf.parse(mdhmf.format(c.getTime()));
				SysOperateLog sol = sysOperateLogDAO.getUserLatestSpecOperateLog(yestoday,userid, logtype);
				if(null!=sol){
					String latestDate = mdhmf.format(sol.getCreatedate());
					return latestDate;
				}else{
					return null;
				}
			} catch (ParseException e) {
				logger.error(e,e);
				return null;
			}
		}
	}
	
	/**
	 * 获得指定用的某项操作的最大INDEXID
	 * @param userid
	 * @param logtype
	 * @return
	 */
	public long getUserSpecOperateMaxIndexId(String userid,String logtype){
		String maxId = sysOperateLogDAO.getUserSpecOperateMaxIndexId(userid, logtype);
		return Long.parseLong(maxId);
	}
	
	/**
	 * 获得指定天全天的指定用户的某项操作的所有记录
	 * @param specDate
	 * @param userid
	 * @param logType
	 * @return
	 */
	public List<SysOperateLog> getSpecDaySysOperateLog(Date specDate,String userid,String logType){
		SimpleDateFormat mdhmf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(specDate);
		c.add(Calendar.DATE, 1);
		try {
			Date startdate = mdhmf.parse(mdhmf.format(specDate));
			Date enddate = mdhmf.parse(mdhmf.format(c.getTime()));
			List<SysOperateLog> list = sysOperateLogDAO.getPeriodList(startdate, enddate, userid, logType);
			return list;
		} catch (ParseException e) {
			logger.error(e,e);
			return null;
		}
	}
	
	/**
	 * 删除用户某天的全部记录
	 * @return
	 */
	public boolean deleteUserSpecDayLog(Date specDate,String userid,String logType){
		List<SysOperateLog> delList = getSpecDaySysOperateLog(specDate,userid,logType);
		return sysOperateLogDAO.batchDeleteLog(delList);
	}
	
	/**
	 * 删除用户的全部记录
	 * @return
	 */
	public boolean deleteUserAllLog(String userid,String logType){
		List<SysOperateLog> delList = sysOperateLogDAO.getUserSpecOperateList(userid, logType);
		return sysOperateLogDAO.batchDeleteLog(delList);
	}
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public SysOperateLog getModel(String id){
		long temp = Long.parseLong(id);
		SysOperateLog SysOperateLog =  sysOperateLogDAO.getBoData(temp);
		return SysOperateLog;
	}
	
	/**
	 * 执行更新操作
	 * @param SysOperateLog
	 * @return
	 */
	public void update(SysOperateLog SysOperateLog){
		if(SysOperateLog!=null&&SysOperateLog.getId()==null){
			SysOperateLog.setId(UUIDUtil.getUUID());
		}
		sysOperateLogDAO.updateBoData(SysOperateLog);
	}
	/**
	 * 执行保存动作
	 * @param SysOperateLog
	 * @return
	 */
	public void save(SysOperateLog SysOperateLog){
		if(SysOperateLog!=null&&SysOperateLog.getId()==null){
			SysOperateLog.setId(UUIDUtil.getUUID());
		}
		sysOperateLogDAO.addBoData(SysOperateLog);
	}
	/**
	 * 执行删除动作
	 * @param SysOperateLog
	 * @return
	 */
	public void delete(SysOperateLog SysOperateLog){
		sysOperateLogDAO.deleteBoData(SysOperateLog);
	}
//================POJO===============================
	public SysOperateLogDAO getSysOperateLogDAO() {
		return sysOperateLogDAO;
	}

	public void setSysOperateLogDAO(SysOperateLogDAO sysOperateLogDAO) {
		this.sysOperateLogDAO = sysOperateLogDAO;
	}
	
}
