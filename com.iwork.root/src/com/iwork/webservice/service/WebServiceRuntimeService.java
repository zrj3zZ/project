package com.iwork.webservice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import com.iwork.plugs.meeting.util.UtilDate;
import com.iwork.webservice.dao.WebServiceRuntimeDAO;
import com.iwork.webservice.model.SysWsRuLog;
import com.iwork.webservice.model.SysWsRuParams;
import org.apache.log4j.Logger;
public class WebServiceRuntimeService {
	private static Logger logger = Logger.getLogger(WebServiceRuntimeService.class);
	private WebServiceRuntimeDAO webServiceRuntimeDAO;
	private WebServiceService webServiceService;
	
	
	/**
	 * 创建接口调用日志
	 * @param log
	 * @param inParams
	 * @param outParams
	 */
	public void createWsRuLog(SysWsRuLog log,List<SysWsRuParams> inParams,List<SysWsRuParams> outParams){
		if(log!=null){ 
			log.setCreatedate(new Date());
			webServiceRuntimeDAO.saveLogInfo(log);
		}else{
			return ;
		}
		//输入参数
		if(inParams!=null&&inParams.size()>0){
			for(SysWsRuParams param:inParams){
				param.setLogId(log.getId());
				param.setInOrOut("input");
				webServiceRuntimeDAO.saveLogParams(param);
			}
		}
		//输出参数
		if(outParams!=null&&outParams.size()>0){
			for(SysWsRuParams param:outParams){
				param.setLogId(log.getId());
				param.setInOrOut("output");
				webServiceRuntimeDAO.saveLogParams(param);
			}
		}
		//设置接口执行状态
	}
	
	/**
	 * 获得日志数据信息
	 * @param pid
	 * @return
	 */
	public HashMap getExecuteInfo(int pid,String startdate,String enddate){
		int sumNum = 0;
		int successNum = 0;
		int failureNum = 0;
		HashMap root = new HashMap();
		List<HashMap> datalist = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date s;
		Date d;
		try {
			s = sdf.parse(startdate);
			d = sdf.parse(enddate);
		} catch (ParseException e) {logger.error(e,e);
			 return null;
		}
		List<SysWsRuLog> list = webServiceRuntimeDAO.getLogList(pid,s,d);
		for(SysWsRuLog log:list){
			int status = log.getStatus();
			if(status==1){
				successNum++;
			}else if(status==0){
				failureNum++;
			}
			sumNum++;
		}
		root.put("SUM_NUM",sumNum);
		root.put("SUCCESS_NUM",successNum);
		root.put("FAILURE_NUM",failureNum);
		return root;
	}
	/**
	 * 获得日志数据信息
	 * @param pid
	 * @return
	 */
	public String getLogJSON(int pid,String startdate,String enddate){
		StringBuffer jsonstr = new StringBuffer();
		HashMap root = new HashMap();
		List<HashMap> datalist = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date s;
		Date d;
		try {
			s = sdf.parse(startdate);
			d = sdf.parse(enddate);
		} catch (ParseException e) {logger.error(e,e);
			return "";
		}
		List<SysWsRuLog> list = webServiceRuntimeDAO.getLogList(pid,s,d);
		for(SysWsRuLog log:list){
			HashMap hash = new HashMap();
			hash.put("ID",log.getId());
			String createdate = UtilDate.datetimeFormat2(log.getCreatedate());
			hash.put("CREATEDATE",createdate);
			long showtime = 0;
			if(log.getShowtime()!=0){
				showtime = log.getShowtime()/1000;
			}
			hash.put("SHOWTIME", showtime);
			if(log.getLoginfo()==null){
				log.setLoginfo("");
			}
			hash.put("LOGINFO",log.getLoginfo());
			String status = "";
			String icon = "";
			String oprate = "";
			if(log.getStatus()==0){
				status = "失败";
				icon = "<img src='iwork_img/button-red.png'>";
//				oprate="执行";
				oprate="<a href='javascript:showinfo("+log.getId()+")'>查看</a>";
			}else if(log.getStatus()==1){
				status = "成功";
				icon = "<img src='iwork_img/button-green.png'>";
				oprate="<a href='javascript:showinfo("+log.getId()+")'>查看</a>";
			}
			hash.put("EDIT",oprate);
			hash.put("ICON",icon);
			hash.put("STATUS",status);
			datalist.add(hash);
		}
		root.put("total", datalist.size());
		root.put("rows", datalist);
		JSONArray json = JSONArray.fromObject(root);
		jsonstr.append(json.toString());
		return jsonstr.toString();
	}
	
	/**
	 * 添加运行时接口调用日志
	 * @param log
	 */
	public void saveLogInfo(SysWsRuLog log){
		webServiceRuntimeDAO.saveLogInfo(log);
	}
	
	/**
	 * 添加或更新运行时接口调用日志
	 * @param log
	 */
	public void saveOrUpdateLogInfo(SysWsRuLog log){
		webServiceRuntimeDAO.saveOrUpdateLogInfo(log);
	}
	
	/**
	 *添加运行时输入输出参数
	 */
	public void saveLogParams(SysWsRuParams model){
		webServiceRuntimeDAO.saveLogParams(model);
	}
	
	public WebServiceRuntimeDAO getWebServiceRuntimeDAO() {
		return webServiceRuntimeDAO;
	}

	public void setWebServiceRuntimeDAO(WebServiceRuntimeDAO webServiceRuntimeDAO) {
		this.webServiceRuntimeDAO = webServiceRuntimeDAO;
	}

	public WebServiceService getWebServiceService() {
		return webServiceService;
	}

	public void setWebServiceService(
			WebServiceService webServiceService) {
		this.webServiceService = webServiceService;
	}

	
	
}
