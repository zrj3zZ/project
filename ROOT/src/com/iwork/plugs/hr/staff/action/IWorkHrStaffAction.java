package com.iwork.plugs.hr.staff.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.engine.metadata.model.ConditionModel;
import com.iwork.core.engine.metadata.model.OrderByModel;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.hr.staff.service.IWorkHrStaffService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkHrStaffAction  extends ActionSupport{
	
	private IWorkHrStaffService iWorkHrStaffService;
	private int total; // 总页数
	private int totalNum; // 总页数
	private int pageNumber; // 当前页数
	private int startRow; // 
	private int currLogPage; // 
	private int totalLogPage; //
	private int pageSize = 10; // 
	private String html;
	private String processFrom1; //请假申请日期
	private String processFrom2;//请假申请日期
	private String title;//请假原因
	private String travel1; //出差申请日期
	private String travel2; //出差申请日期
	private String title1; //出差原因
	
	/**
	 * 请假历史查询
	 * @return
	 */
	public String showLeave(){
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
		String entityName = "BD_HR_QJSQB";
		List<ConditionModel> conditionList = new ArrayList();
		ConditionModel cm = new ConditionModel();
		cm.setFieldName("SQRZH");
		cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_EQUAL);
		cm.setFieldType(ConditionModel.FIELD_TYPE_CHAR);
		cm.setFieldData(UserContextUtil.getInstance().getCurrentUserId());
		conditionList.add(cm);
		
		if (processFrom1 != null && !processFrom1.equals("")) {
			
			cm = new ConditionModel();
			cm.setFieldName("SQRQ");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_MORE);
			cm.setFieldType(ConditionModel.FIELD_TYPE_DATE);
			cm.setFieldData(processFrom1);
			conditionList.add(cm);
		}
		if (processFrom2 != null && !processFrom2.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("SQRQ");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_LESS);
			cm.setFieldType(ConditionModel.FIELD_TYPE_DATE);
			cm.setFieldData(processFrom2);
			conditionList.add(cm);
		}
		if (title != null && !title.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("QJYY");
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
		html = iWorkHrStaffService.showLeaveHtml(list);
		total = ProcessAPI.getInstance().queryBDDataListSize(entityName, conditionList, orderByList);
		return SUCCESS;
	}
	/**
	 * 考勤申请记录
	 * @return
	 */
	public String showLeaveMemo(){
		
		return SUCCESS;
	}
	/**
	 * 出差申请记录
	 * @return
	 */
	public String showTravel(){
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
		String entityName = "BD_HR_ONBUSINESS";
		List<ConditionModel> conditionList = new ArrayList();
		ConditionModel cm = new ConditionModel();
		cm.setFieldName("APPLICANTNO");
		cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_EQUAL);
		cm.setFieldType(ConditionModel.FIELD_TYPE_CHAR);
		cm.setFieldData(UserContextUtil.getInstance().getCurrentUserId());
		conditionList.add(cm);
		if (travel1 != null && !travel1.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("APPLYDATE");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_MORE);
			cm.setFieldType(ConditionModel.FIELD_TYPE_DATE);
			cm.setFieldData(travel1);
			conditionList.add(cm);
		}
		if (travel2 != null && !travel2.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("APPLYDATE");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_LESS);
			cm.setFieldType(ConditionModel.FIELD_TYPE_DATE);
			cm.setFieldData(travel2);
			conditionList.add(cm);
		}
		if (title1 != null && !title1.equals("")) {
			cm = new ConditionModel();
			cm.setFieldName("CAUSE");
			cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_EQUAL);
			cm.setFieldType(ConditionModel.FIELD_TYPE_CHAR);
			cm.setFieldData(title1);
			conditionList.add(cm);
		}
		List<OrderByModel> orderByList = new ArrayList();
		OrderByModel obm = new OrderByModel();
		obm.setFieldName("ID");
		obm.setOrderType(OrderByModel.FIELD_TYPE_DESC);
		orderByList.add(obm);
		List<HashMap> list =  ProcessAPI.getInstance().queryBDDataList(entityName, conditionList, orderByList,startRow,pageSize);
		html = iWorkHrStaffService.showTravelHtml(list);
		total = ProcessAPI.getInstance().queryBDDataListSize(entityName, conditionList, orderByList);
		return SUCCESS;
	}
	public String getTravel1() {
		return travel1;
	}
	public void setTravel1(String travel1) {
		this.travel1 = travel1;
	}
	public String getTravel2() {
		return travel2;
	}
	public void setTravel2(String travel2) {
		this.travel2 = travel2;
	}
	public String getTitle1() {
		return title1;
	}
	public void setTitle1(String title1) {
		this.title1 = title1;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public IWorkHrStaffService getiWorkHrStaffService() {
		return iWorkHrStaffService;
	}
	public void setiWorkHrStaffService(IWorkHrStaffService iWorkHrStaffService) {
		this.iWorkHrStaffService = iWorkHrStaffService;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
	
}
