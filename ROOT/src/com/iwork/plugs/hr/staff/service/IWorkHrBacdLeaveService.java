package com.iwork.plugs.hr.staff.service;

import java.util.HashMap;
import java.util.List;

public class IWorkHrBacdLeaveService {
	/**
	 * 销假
	 * @return
	 */
	public String showBacdLeaveHtml(List<HashMap> list){
		StringBuffer html = new StringBuffer();
		int num = 0;
		for(HashMap hm:list){
			num++;
			html.append("<tr class=\"item\">");
			html.append("<td>").append(num).append("</td>");
			html.append("<td><a target=\"_blank\" href=\"processRuntimeStartInstance.action?actDefId=XJSQ:1:133104&instanceId=133341&excutionId=133341&taskId=133397\">").append(hm.get("DJBH")).append("</a></td>");
			html.append("<td>").append(hm.get("SQRQ")).append("</td>");
			html.append("<td>").append(hm.get("XJQSRQ")).append("</td>");
			html.append("<td>").append(hm.get("XJJSRQ")).append("</td>");
			html.append("<td>").append(hm.get("XJGJXSS")).append("</td>");
			html.append("<td>").append(hm.get("XJYY")).append("</td>");
			html.append("</tr>");
		}
		return html.toString();
	}
}
