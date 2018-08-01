package com.iwork.plugs.hr.staff.service;

import java.util.HashMap;
import java.util.List;

public class IWorkHrAttendanceService {
	/**
	 * 加班
	 * @return
	 */
	public String showAttendanceHtml(List<HashMap> list){
		StringBuffer html = new StringBuffer();
		int num = 0;
		for(HashMap hm:list){
			num++;
			html.append("<tr class=\"item\">");
			html.append("<td>").append(num).append("</td>");
			html.append("<td><a target=\"_blank\" href=\"processRuntimeStartInstance.action?actDefId=KQSMSQLC:1:47104&instanceId=133901&excutionId=133901&taskId=133959\">").append(hm.get("SQDJBH")).append("</a></td>");
			html.append("<td>").append(hm.get("SQRQ")).append("</td>");
			html.append("<td>").append(hm.get("USERNAME")).append("</td>");
			html.append("<td>").append(hm.get("DEPTNAME")).append("</td>");
			html.append("</tr>");
		}
		return html.toString();
	}
}
