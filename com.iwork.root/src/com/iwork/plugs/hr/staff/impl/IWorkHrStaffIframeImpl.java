package com.iwork.plugs.hr.staff.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.engine.metadata.model.ConditionModel;
import com.iwork.core.engine.metadata.model.OrderByModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.hr.staff.model.ConfigModel;

public class IWorkHrStaffIframeImpl extends IWorkHrStaffAbst {
	
	public IWorkHrStaffIframeImpl(UserContext me,ConfigModel configModel){
		super(me,configModel);
	}
	/**
	 * 获得按钮
	 */
	public String getBtnHtml(){
		StringBuffer html = new StringBuffer();
//		html.append("<a href=\"javascript:addItem();\" class=\"easyui-linkbutton\" plain=\"false\" iconCls=\"icon-edit\">新建申请单</a>&nbsp;&nbsp;");
		html.append("<a href=\"javascript:location.reload();\" class=\"easyui-linkbutton\" plain=\"false\" iconCls=\"icon-reload\">刷新</a>");
		return html.toString(); 
		
	}
	public String getContent() {
		String content =  "";
				StringBuffer html = new StringBuffer();
				String url = getConfigModel().getUrl();
				html.append(" <iframe width='100%' height='99%' src ='").append(url).append("' frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\" frameborder=\"0\" scrolling=\"auto\" id=\"ifm\" name=\"ifm\"/>");
				content = html.toString(); 
		return content;
	}
//	public String getContent() {
//		String content =  "";
//		StringBuffer html = new StringBuffer();
//		html.append("<table class=\"mainTable\">").append("\n");
//		html.append("	<tr>").append("\n");
//		html.append("	<td class=\"groupTitle\">我的请假记录</td>").append("\n");
//		html.append("	</tr>").append("\n");
//		html.append("	<tr>").append("\n");
//		html.append("	<td>").append("\n");
//		html.append("		<table class=\"infoTable\">").append("\n");
//		html.append("			<tr>").append("\n");
//		html.append("			<th>单据编号</th>").append("\n");
//		html.append("			<th>申请日期</th>").append("\n");
//		html.append("			<th>请假起始日期</th>").append("\n");
//		html.append("			<th>请假结束日期</th>").append("\n");
//		html.append("			<th>请假时长（小时）</th>").append("\n");
//		html.append("			<th>请假请假原因</th>").append("\n");
//		html.append("			<th>备注</th>").append("\n");
//		html.append("			</tr>").append("\n");
//		List<HashMap> list = this.getList();
//		for(HashMap data:list){
//			html.append("			<tr>").append("\n");
//			html.append("				<td>").append(data.get("DJBH")).append("</td>").append("\n");
//			html.append("				<td>").append(data.get("SQRQ")).append("</td>").append("\n");
//			html.append("				<td>").append(data.get("QJQSRQ")).append("</td>").append("\n");
//			html.append("				<td>").append(data.get("QJJSRQ")).append("</td>").append("\n");
//			html.append("				<td>").append(data.get("QJGJXSS")).append("</td>").append("\n");
//			html.append("				<td>").append(data.get("QJYY")).append("</td>").append("\n");
//			html.append("			</tr>").append("\n");
//		}
//		html.append("		</table>").append("\n");
//		html.append("	</td>").append("\n");
//		html.append("	</tr>").append("\n");
//		
//		html.append("</table>").append("\n");
//		content = html.toString();
//		return null;
//	}
	
	private List<HashMap> getList(){
		String entityName = "BD_HR_QJSQB";
		List<ConditionModel> conditionList = new ArrayList();
		ConditionModel cm = new ConditionModel();
		cm.setFieldName("SQRZH");
		cm.setFieldExpression(ConditionModel.FIELD_EXPRESSION_EQUAL);
		cm.setFieldType(ConditionModel.FIELD_TYPE_CHAR);
		cm.setFieldData(UserContextUtil.getInstance().getCurrentUserId());
		conditionList.add(cm);
		List<OrderByModel> orderByList = new ArrayList();
		OrderByModel obm = new OrderByModel();
		obm.setFieldName("ID");
		obm.setOrderType(OrderByModel.FIELD_TYPE_DESC);
		orderByList.add(obm);
		List<HashMap> list =  null;
//		List<HashMap> list =  ProcessAPI.getInstance().queryBDDataList(entityName, conditionList, orderByList);
		return list;
	}
}
