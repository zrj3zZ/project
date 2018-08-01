package com.iwork.plugs.hr.staff.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.engine.metadata.model.ConditionModel;
import com.iwork.core.engine.metadata.model.OrderByModel;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.hr.staff.service.IWorkHrWorkService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;


public class IWorkHrWorkAction extends ActionSupport {
	private IWorkHrWorkService iWorkHrWorkService;
	private int total; // 总页数
	private int totalNum; // 总页数
	private int pageNumber; // 当前页数
	private int startRow; // 
	private int currLogPage; // 
	private int totalLogPage; //
	private int pageSize = 10; // 
	private String html;
	private String processFrom1; //申请日期
	private String processFrom2;//申请日期
	private String title;//请假原因
	/**
	 * 加班查询
	 * @return
	 */
	public String showWork(){
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		} 
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		String entityName = "BD_HR_WORKAPPLICATION";
		List<ConditionModel> conditionList = new ArrayList();
		ConditionModel cm = new ConditionModel();
		cm.setFieldName("APPLICANTNO");
		cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_EQUAL);
		cm.setFieldType(ConditionModel.FIELD_TYPE_CHAR);
		cm.setFieldData(UserContextUtil.getInstance().getCurrentUserId());
		conditionList.add(cm);
		if (processFrom1 != null && !processFrom1.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("APPLYDATE");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_MORE);
			cm.setFieldType(ConditionModel.FIELD_TYPE_DATE);
			cm.setFieldData(processFrom1);
			conditionList.add(cm);
		}
		if (processFrom2 != null && !processFrom2.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("APPLYDATE");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_LESS);
			cm.setFieldType(ConditionModel.FIELD_TYPE_DATE);
			cm.setFieldData(processFrom2);
			conditionList.add(cm);
		}
		if (title != null && !title.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("WORKCAUSE");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_EQUAL);
			cm.setFieldType(ConditionModel.FIELD_TYPE_CHAR);
			cm.setFieldData(title);
			conditionList.add(cm);
		}
		List<OrderByModel> orderByList = new ArrayList();
		OrderByModel obm = new OrderByModel();
		obm.setFieldName("ID");
		obm.setOrderType(OrderByModel.FIELD_TYPE_DESC);
		orderByList.add(obm);
		List<HashMap> list =  ProcessAPI.getInstance().queryBDDataList(entityName, conditionList, orderByList,startRow,pageSize);
		html = iWorkHrWorkService.showWorkHtml(list);
		total = ProcessAPI.getInstance().queryBDDataListSize(entityName, conditionList, orderByList);
		return SUCCESS;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProcessFrom1() {
		return processFrom1;
	}
	public void setProcessFrom1(String processFrom1) {
		this.processFrom1 = processFrom1;
	}
	public String getProcessFrom2() {
		return processFrom2;
	}
	public void setProcessFrom2(String processFrom2) {
		this.processFrom2 = processFrom2;
	}
	public IWorkHrWorkService getiWorkHrWorkService() {
		return iWorkHrWorkService;
	}
	public void setiWorkHrWorkService(IWorkHrWorkService iWorkHrWorkService) {
		this.iWorkHrWorkService = iWorkHrWorkService;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getCurrLogPage() {
		return currLogPage;
	}
	public void setCurrLogPage(int currLogPage) {
		this.currLogPage = currLogPage;
	}
	public int getTotalLogPage() {
		return totalLogPage;
	}
	public void setTotalLogPage(int totalLogPage) {
		this.totalLogPage = totalLogPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
}
