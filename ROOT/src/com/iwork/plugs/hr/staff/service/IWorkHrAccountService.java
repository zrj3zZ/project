package com.iwork.plugs.hr.staff.service;

import java.util.HashMap;
import java.util.List;

public class IWorkHrAccountService {
	/**
	 * 报销查询
	 * @return
	 */
	public String showAccountHtml(List<HashMap> list){
		StringBuffer html = new StringBuffer();
		int num = 0;
		for(HashMap hm:list){
			num++;
			html.append("<tr class=\"item\">");
			html.append("<td>").append(num).append("</td>");
			html.append("<td><a target=\"_blank\" href=\"loadProcessFormPage.action?actDefId=FYBXLC:1:71604&instanceId=134536&excutionId=134536&taskId=134666\">").append(hm.get("DJBH")).append("</a></td>");
			html.append("<td>").append(hm.get("SQR")).append("</td>");
			html.append("<td>").append(hm.get("SQRQ")).append("</td>");
//			html.append("<td>").append(hm.get("BXRZH")).append("</td>");
//			html.append("<td>").append(hm.get("SSGSMC")).append("</td>");
			html.append("<td>").append(hm.get("FYLBMC")).append("</td>");
//			html.append("<td>").append(hm.get("BXBM")).append("</td>");
//			html.append("<td>").append(hm.get("YSLX")).append("</td>");
//			html.append("<td>").append(hm.get("CBZXMC")).append("</td>");
			html.append("<td>").append(hm.get("JSFSBM")).append("</td>");
//			html.append("<td>").append(hm.get("JSF")).append("</td>");
			html.append("<td>").append(hm.get("DJZS")).append("</td>");
//			html.append("<td>").append(hm.get("SKDW")).append("</td>");
//			html.append("<td>").append(hm.get("KHX")).append("</td>");
//			html.append("<td>").append(hm.get("YXZH")).append("</td>");
//			html.append("<td>").append(hm.get("BXSY")).append("</td>");
			html.append("<td>").append(hm.get("SQBXJE")).append("</td>");
			html.append("<td>").append(hm.get("SJBXJE")).append("</td>");
			html.append("<td>").append(hm.get("JKYE")).append("</td>");
			html.append("<td>").append(hm.get("BCCXJE")).append("</td>");
			html.append("<td>").append(hm.get("SJZFJE")).append("</td>");
//			html.append("<td>").append(hm.get("JZRQ")).append("</td>");
//			html.append("<td>").append(hm.get("KJ")).append("</td>");
//			html.append("<td>").append(hm.get("CN")).append("</td>");
			html.append("</tr>");
		}
		return html.toString();
	}
}
