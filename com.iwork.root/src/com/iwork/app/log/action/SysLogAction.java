package com.iwork.app.log.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.log.service.SysLogService;
import com.iwork.app.log.common.LogConstant;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 操作日志Action
 * @author LuoChuan
 *
 */
public class SysLogAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3944898509310725177L;
	private SysLogService sysLogService;
	protected String id;								//ID
	protected String operateUser;						//操作人
	protected String operateDateStart;					//开始时间
	protected String operateDateEnd;					//结束时间
	protected String operateType;						//操作类型
	protected String operateTable;						//操作表
	protected String functionName;						//功能名称
	protected String isDoSearch;						//是否是进行查询操作
	protected String logType;							//日志类型，分为操作日志、错误日志两种
	protected Page page = new Page(); 					//分页类
	protected int totalPages;							//总页数
	protected int curPage;								//显示第几页
	protected int totalRecords;							//总记录数
	private String infolist;
	
	/**
	 * 显示操作日志
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	public String showOperationLog() throws UnsupportedEncodingException {
		
		String logType = LogConstant.OPERATIONLOG;//日志类型，"0"代表操作日志,"1"代表错误日志
		String isDoSearch = this.getIsDoSearch();
		String user = this.getOperateUser();
		String startDate = this.getOperateDateStart();
		String endDate = this.getOperateDateEnd();
		String operateType = this.getOperateType();
		String table = this.getOperateTable();
		String functionName = this.getFunctionName();
		String infoList=sysLogService.getGridScript(logType,isDoSearch,user,startDate,endDate,operateType,table,functionName);
        this.setInfolist(infoList);
        
		return SUCCESS;
	}
	
	/**
	 * 获得操作日志列表
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	public void getOperationLogList() throws UnsupportedEncodingException {
		
		String user = "";
		String table = "";
		String function = "";

		if (null != this.getOperateUser() && !"".equals(this.getOperateUser())) {
			user = this.getOperateUser().trim();
		}
		if (null != this.getOperateTable() && !"".equals(this.getOperateTable())) {
			table = this.getOperateTable().trim();
		}
		if (null != this.getFunctionName() && !"".equals(this.getFunctionName())) {
			function = this.getFunctionName().trim();
		}
		String logType = this.getLogType();//日志类型，"0"代表操作日志,"1"代表错误日志
		String startDateTime = this.getOperateDateStart();
		String endDateTime = this.getOperateDateEnd();
		String operateType = this.getOperateType();//操作类型
		Page page = this.getPage();
		
		Boolean isDoSearch = Boolean.parseBoolean(this.getIsDoSearch());
		String operateLogList = sysLogService.getLog(logType,isDoSearch,URLDecoder.decode(user, "UTF-8"),startDateTime,endDateTime,operateType,table,URLDecoder.decode(function, "UTF-8"),page);
		String json = operateLogList.substring(1, operateLogList.length()-1);
		ResponseUtil.write(json);
	}
	
	/**
	 * 删除选中日志记录
	 * @return String
	 */
	public String deleteLog() {
		
		String id = this.getId();
		if (null != id && !"".equals(id)) {
			sysLogService.deleteLog(id);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 显示错误日志
	 * @return String 
	 * @throws UnsupportedEncodingException 
	 */
	public String showErrorLog() throws UnsupportedEncodingException {
		
		String logType = LogConstant.ERRORLOG;//日志类型，"0"代表操作日志,"1"代表错误日志
		String isDoSearch = this.getIsDoSearch();
		String user = this.getOperateUser();
		String startDate = this.getOperateDateStart();
		String endDate = this.getOperateDateEnd();
		String operateType = this.getOperateType();
		String table = this.getOperateTable();
		String functionName = this.getFunctionName();
		
		String infoList=sysLogService.getGridScript(logType,isDoSearch,user,startDate,endDate,operateType,table,functionName);
        this.setInfolist(infoList);
        
		return SUCCESS;
	}
	
	/**
	 * 获得错误日志列表
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getErrorLogList() {

		String logType = this.getLogType();//日志类型，"0"代表操作日志,"1"代表错误日志
		String user = this.getOperateUser().trim();
		String startDateTime = this.getOperateDateStart();
		String endDateTime = this.getOperateDateEnd();
		String function = this.getFunctionName().trim();
		Boolean isDoSearch = Boolean.parseBoolean(this.getIsDoSearch());
		Page page = this.getPage();
		
		String errorLogList = sysLogService.getLog(logType,isDoSearch,user,startDateTime,endDateTime,"","",function,page);//错误日志,此处不需要操作类型、操作表两个参数
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("sysErrorLogJson",errorLogList.substring(1, errorLogList.length()-1));
		
		return SUCCESS;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(SysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public String getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOperateTable() {
		return operateTable;
	}

	public void setOperateTable(String operateTable) {
		this.operateTable = operateTable;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getIsDoSearch() {
		return isDoSearch;
	}

	public void setIsDoSearch(String isDoSearch) {
		this.isDoSearch = isDoSearch;
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

	public String getInfolist() {
		return infolist;
	}

	public void setInfolist(String infolist) {
		this.infolist = infolist;
	}

	public String getOperateDateStart() {
		return operateDateStart;
	}

	public void setOperateDateStart(String operateDateStart) {
		this.operateDateStart = operateDateStart;
	}

	public String getOperateDateEnd() {
		return operateDateEnd;
	}

	public void setOperateDateEnd(String operateDateEnd) {
		this.operateDateEnd = operateDateEnd;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}
	
}
