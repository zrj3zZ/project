package com.iwork.plugs.hr.staff.service;

import java.util.HashMap;
import java.util.List;

public class IWorkHrWorkService {
	/**
	 * 加班
	 * @return
	 */
	public String showWorkHtml(List<HashMap> list){
		StringBuffer html = new StringBuffer();
		int num = 0;
		for(HashMap hm:list){
			num++;
			html.append("<tr class=\"item\">");
			html.append("<td>").append(num).append("</td>");
			html.append("<td><a target=\"_blank\" href=\"processRuntimeStartInstance.action?actDefId=JBSQLC:1:79707&instanceId=132972&excutionId=132972&taskId=133030\">").append(hm.get("FROMNO")).append("</a></td>");
			html.append("<td>").append(hm.get("APPLYDATE")).append("</td>");
			html.append("<td>").append(hm.get("STARTDATE")).append("</td>");
			html.append("<td>").append(hm.get("FINISHDATE")).append("</td>");
			html.append("<td>").append(hm.get("DATEEXTENS")).append("</td>");
			html.append("<td>").append(hm.get("WORKCAUSE")).append("</td>");
			html.append("</tr>");
		}
		return html.toString();
	}
}
