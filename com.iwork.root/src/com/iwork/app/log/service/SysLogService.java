package com.iwork.app.log.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.log.common.IWorkLogUtil;
import com.iwork.app.log.common.LogConstant;
import com.iwork.app.log.dao.SysLogDAO;
import com.iwork.app.log.model.SysLogRecord;
import com.iwork.core.engine.iform.util.Page;

/**
 * 操作日志Service
 * @author LuoChuan
 *
 */
public class SysLogService {

	private SysLogDAO sysLogDAO;
	
	/**
	 * 获得日志
	 * @param type						日志类型
	 * @param isDoSearch					是否是执行查询操作
	 * @param operateUser				操作人
	 * @param operateDateStart			查询条件的开始时间
	 * @param operateDateEnd			查询条件的结束时间
	 * @param operateType				操作类型
	 * @param operateTable				操作表
	 * @param functionName				功能名称
	 */
	public String getLog(String type, Boolean isDoSearch, String operateUser,
						 String operateDateStart, String operateDateEnd, String operateType,
						 String operateTable, String functionName, Page page) {

		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		Map<String,Object> total = new HashMap<String,Object>();
		List logList;

		int totalRecord = 0;   //总记录行数
		int totalNum = 0;   //总记录页数
		int count = 0;
		
		if (isDoSearch == false) {
			logList = sysLogDAO.findByType(type);
		} else {
			logList = sysLogDAO.findByCondition(operateUser, operateDateStart, operateDateEnd, functionName, operateType, operateTable, type);
		}

		//获取总行数
		totalRecord = logList.size();
		BigDecimal b1 = new BigDecimal(totalRecord); 
		BigDecimal b2 = new BigDecimal(page.getPageSize()); 
		totalNum =  b1.divide(b2,0,BigDecimal.ROUND_UP).intValue();  //计算页数  向上取整
		int startRow =  page.getPageSize()*(page.getCurPageNo()-1);
		count = startRow;
		
		for (int i=0;i<logList.size();i++) {
			SysLogRecord log = (SysLogRecord) logList.get(i);
			Map<String,Object> rows = new HashMap<String,Object>();
			rows.put("id", log.getLogId());//主键
			rows.put("operateUser", log.getCreateUser());//日志创建人
//			rows.put("operateTime", UtilDate.dateFormat(log.getCreateTime()));//日志创建时间
			rows.put("operateTime", log.getCreateTime());//日志创建时间
			if (LogConstant.OPERATIONLOG.equals(type)) {//操作日志需要显示"操作类型","操作表"
				rows.put("operateType", IWorkLogUtil.filterString(String.valueOf(log.getOperateType()), LogConstant.OPERATETYPE) );//操作类型
				rows.put("operateTable", log.getTableName());//操作表名
			}
			rows.put("functionName", log.getFunctionName());//功能名称
			rows.put("logText", log.getLogText());//日志内容
			item.add(rows);
		}
		
		total.put("total", logList.size());
		total.put("curPage", page.getCurPageNo());
		total.put("pageSize", page.getPageSize());
		total.put("totalPages",totalNum);
		total.put("totalRecords", totalRecord);
		total.put("dataRows", item);
		
		JSONArray json = JSONArray.fromObject(total);
		jsonHtml.append(json.toString());
		return jsonHtml.toString();
	}

	/**
	 * 删除日志记录
	 */
	public void deleteLog(String id) {
		String[] ids = id.split(",");
		for (int i=0;i<ids.length;i++) {
			String tmpid = ids[i].trim();
			if(!tmpid.equals("")){
				sysLogDAO.deleteLog(Long.parseLong(tmpid));
			}
			
		}
	}

	/**
	 * 获取内容列表JS
	 * @param id
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String getGridScript(String type, String isDoSearch, String user, String startDate, String endDate, String operateType, String tableName, String functionName) throws UnsupportedEncodingException{
		StringBuffer script = new StringBuffer();
		String gridName = "";
		String height = "";
		if (LogConstant.OPERATIONLOG.equals(type)) {//操作日志
			gridName = "sysoperateloggrid";
			height = "270";
		} else if (LogConstant.ERRORLOG.equals(type)) {//错误日志
			gridName = "syserrorloggrid";
			height="295";
		}
		script.append("<script>").append("\n");
		script.append("jQuery(\"#").append(gridName).append("\").jqGrid({").append("\n");
		script.append("   	url:\"sys_operation_log_getList.action?isDoSearch=").append(isDoSearch).append("&logType=").append(type).append("&operateUser=\"+").append("encodeURI(encodeURI('").append(user).append("'))")
																				.append("+\"&operateDateStart=").append(startDate).append("&operateDateEnd=").append(endDate)
																				.append("&operateType=").append(operateType).append("&operateTable=").append(tableName)
																				.append("&functionName=\"+").append("encodeURI(encodeURI('").append(functionName).append("'))").append(",").append("\n");//获得数据URL
		script.append("		datatype: \"json\",").append("\n");
		script.append("		mtype: \"POST\",").append("\n");
		script.append("		autowidth:true,").append("\n");
		script.append("   	colNames:").append(this.getLogColumTitle(type)).append(",").append("\n");//获得列标题
		script.append("   	colModel:").append(this.getLogColumModel(type)).append(",").append("\n");
		script.append("   	rowNum:20,").append("\n");
		script.append("   	rowList:[20,40,60,80],").append("\n");
		script.append("   	multiselect: true,").append("\n");
		script.append("   	pager: '#prowed_info_grid',").append("\n");
		script.append("   	prmNames:{rows:\"page.pageSize\",page:\"page.curPageNo\",sort:\"page.orderBy\",order:\"page.order\"},").append("\n");
		script.append("     jsonReader: {").append("\n");
		script.append("     	root: \"dataRows\",").append("\n");
		script.append("     	page: \"curPage\",").append("\n");
		script.append("     	total: \"totalPages\",").append("\n");
		script.append("     	records: \"totalRecords\",").append("\n");
		script.append("     	repeatitems: false,").append("\n");
		script.append("     	userdata: \"userdata\"").append("\n");
		script.append("    },").append("\n");
		script.append("    	viewrecords: true,").append("\n");
		script.append("    	resizable:true,").append("\n");
//		script.append("     height: \"300\"").append("\n");
		script.append("     height: \"").append(height).append("\"\n");
		script.append("});").append("\n"); 
//		script.append("jQuery(\"#sysoperateloggrid\").jqGrid('navGrid',\"#prowed_info_grid").append("\",{edit:false,closeOnEscape:true,add:false,del:false,search:false});").append("\n");
		script.append("jQuery(\"#").append(gridName).append("\").jqGrid('navGrid',\"#prowed_info_grid").append("\",{edit:false,closeOnEscape:true,add:false,del:false,search:false});").append("\n");
		script.append("</script>").append("\n");
		return script.toString();
	}
	
	/**
	 * 列表标题
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getLogColumTitle(String type){
		
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		
		item.add("操作人");
		item.add("操作时间");
		if (LogConstant.OPERATIONLOG.equals(type)) {//操作日志
			item.add("操作类型");
			item.add("操作表");
		}
		item.add("功能名称");
		if (LogConstant.OPERATIONLOG.equals(type)) {
			item.add("日志内容");
		} else {
			item.add("错误信息");
		}
		
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 列表模型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getLogColumModel(String type){
		
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		
		//装载操作人
		Map operateUser = new HashMap();
		operateUser.put("name","operateUser");
		operateUser.put("index","1");
		operateUser.put("width","60");	
		item.add(operateUser);
		
		//装载操作日期
		Map operateTime = new HashMap();
		operateTime.put("name", "operateTime");
		operateTime.put("index","2"); 
		operateTime.put("width", "60");
		operateTime.put("align", "center");
		item.add(operateTime);
		
		if (LogConstant.OPERATIONLOG.equals(type)) {//操作日志才需要以下两列
			//装载操作类型
			Map operateType = new HashMap();
			operateType.put("name", "operateType");
			operateType.put("index","3"); 
			operateType.put("width", "40");
			item.add(operateType);
			
			//装载操作表	
			Map operateTable = new HashMap();
			operateTable.put("name", "operateTable");
			operateTable.put("index","4"); 
			operateTable.put("width", "60");
			item.add(operateTable);
		}
		
		//装载功能名称
		Map functionName = new HashMap();
		functionName.put("name", "functionName");
		 
		if (LogConstant.OPERATIONLOG.equals(type)) {
			functionName.put("index","5");
		} else {
			functionName.put("index","3");
		}
		functionName.put("width", "60");
		item.add(functionName);
		
		//装载日志内容		
		Map logText = new HashMap();
		logText.put("name", "logText");
		if (LogConstant.OPERATIONLOG.equals(type)) {
			logText.put("index","6");
		} else {
			logText.put("index","4");
		}
		logText.put("width", "200");
		item.add(logText);
		
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	public SysLogDAO getSysLogDAO() {
		return sysLogDAO;
	}

	public void setSysLogDAO(SysLogDAO sysLogDAO) {
		this.sysLogDAO = sysLogDAO;
	}
	
}
