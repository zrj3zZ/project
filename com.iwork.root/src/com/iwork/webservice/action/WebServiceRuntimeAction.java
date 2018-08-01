package com.iwork.webservice.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.util.ResponseUtil;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsRuLog;
import com.iwork.webservice.model.SysWsRuParams;
import com.iwork.webservice.service.WebServiceRuntimeService;
import com.opensymphony.xwork2.ActionSupport;

public class WebServiceRuntimeAction extends ActionSupport {
	private WebServiceRuntimeService webServiceRuntimeService;

	private int id;
	private int pid;
	private int isChecked;
	private int sumNum = 0;
	private int successNum = 0;
	private int failureNum = 0;
	private String startdate;
	private String enddate;
	private SysWsRuLog log;
	private HashMap executeInfo;
	private List<SysWsRuParams> inparamList;
	private List<SysWsRuParams> outparamList;
	
	/**
	 * 加载日志列表
	 * @return
	 */
	public String showlog(){
		if(startdate==null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			startdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		}
		if(enddate==null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1); 
			 enddate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		}
		if(pid!=0&&startdate!=null&&enddate!=null){
			executeInfo = webServiceRuntimeService.getExecuteInfo(pid, startdate, enddate);
			if(executeInfo!=null){
				if(executeInfo.get("SUM_NUM")!=null){
					sumNum = Integer.parseInt(executeInfo.get("SUM_NUM").toString());
				}
				if(executeInfo.get("SUCCESS_NUM")!=null){
					successNum = Integer.parseInt(executeInfo.get("SUCCESS_NUM").toString());
				}
				if(executeInfo.get("FAILURE_NUM")!=null){
					failureNum = Integer.parseInt(executeInfo.get("FAILURE_NUM").toString());
				}
			} 
		}
		return SUCCESS;
	}
	
	/**
	 * 加载日志项
	 * @return
	 */
	public String showItem(){
		if(id!=0){
			log = webServiceRuntimeService.getWebServiceRuntimeDAO().getLogModel(id);
			if(log!=null){
				inparamList = webServiceRuntimeService.getWebServiceRuntimeDAO().getRuParamsList(id, WebServiceConstants.WS_PARAMS_TYPE_INPUT);
				outparamList = webServiceRuntimeService.getWebServiceRuntimeDAO().getRuParamsList(id, WebServiceConstants.WS_PARAMS_TYPE_OUTPUT);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 重新执行
	 */
	public void showJSON(){
		if(pid!=0&&startdate!=null&&enddate!=null){
			String json = webServiceRuntimeService.getLogJSON(pid,startdate,enddate);
			ResponseUtil.write( json.substring(1, json.length()-1));
		}
	}

	/**
	 * 重新执行
	 */
	public void reExcute(){
		
	}

	public void setwebServiceRuntimeService(
			WebServiceRuntimeService webServiceRuntimeService) {
		this.webServiceRuntimeService = webServiceRuntimeService;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SysWsRuLog getLog() {
		return log;
	}

	public void setLog(SysWsRuLog log) {
		this.log = log;
	}

	public List<SysWsRuParams> getInparamList() {
		return inparamList;
	}

	public List<SysWsRuParams> getOutparamList() {
		return outparamList;
	}

	public HashMap getExecuteInfo() {
		return executeInfo;
	}

	public int getSumNum() {
		return sumNum;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public int getFailureNum() {
		return failureNum;
	}
	
}
