package com.iwork.plugs.hr.staff.service;

import java.util.HashMap;
import java.util.List;


public class IWorkHrStaffService {
	
	/**
	 * 请假申请流程
	 * @return
	 */
	public String showLeaveHtml(List<HashMap> list){
		StringBuffer html = new StringBuffer();
		int num = 0;
		for(HashMap hm:list){
			num++;
			html.append("<tr class=\"item\">");
			html.append("<td>").append(num).append("</td>");
			html.append("<td><a target=\"_blank\" href=\"loadProcessFormPage.action?actDefId=QJSQLC:1:71304&instanceId=115101&excutionId=115101&taskId=115149\">").append(hm.get("DJBH")).append("</a></td>");
			html.append("<td>").append(hm.get("SQRQ")).append("</td>");
			html.append("<td>").append(hm.get("QJQSRQ")).append("</td>");
			html.append("<td>").append(hm.get("QJJSRQ")).append("</td>");
			html.append("<td>").append(hm.get("QJGJXSS")).append("</td>");
			html.append("<td>").append(hm.get("QJYY")).append("</td>");
			html.append("</tr>");
		}
		return html.toString();
	}
	/**
	 * 请假申请流程
	 * @return
	 */
	public String showTravelHtml(List<HashMap> list){
		StringBuffer html = new StringBuffer();
		int num = 0;
		for(HashMap hm:list){
			num++;
			html.append("<tr class=\"item\">");
			html.append("<td>").append(num).append("</td>");
			html.append("<td><a target=\"_blank\" href=\"processRuntimeStartInstance.action?actDefId=CCSQLC:1:79804&instanceId=133960&excutionId=133960&taskId=134040\">").append(hm.get("FROMNO")).append("</a></td>");
			html.append("<td>").append(hm.get("APPLYDATE")).append("</td>");
			html.append("<td>").append(hm.get("STARTDATE")).append("</td>");
			html.append("<td>").append(hm.get("FINISHDATE")).append("</td>");
			html.append("<td>").append(hm.get("TARGET")).append("</td>");
			html.append("<td>").append(hm.get("CAUSE")).append("</td>");
			html.append("<td>").append(hm.get("TYPE")).append("</td>");
			html.append("<td>").append(hm.get("ALLMAN")).append("</td>");
			html.append("</tr>");
		}
		return html.toString();
	}
	
	public List<HashMap> showLeaveMemo(){
			
			
			return null;
		}
	}
