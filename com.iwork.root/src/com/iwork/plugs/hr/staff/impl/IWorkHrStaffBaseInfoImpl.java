package com.iwork.plugs.hr.staff.impl;

import java.util.HashMap;
import java.util.List;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.hr.staff.model.ConfigModel;
import com.iwork.sdk.DemAPI;

public class IWorkHrStaffBaseInfoImpl extends IWorkHrStaffAbst {
	public IWorkHrStaffBaseInfoImpl(UserContext me,ConfigModel configModel){
		super(me,configModel);
	}
	/**
	 * 获得按钮
	 */
	public String getBtnHtml(){
		StringBuffer html = new StringBuffer();
		html.append("<a href=\"javascript:addItem();\" class=\"easyui-linkbutton\" plain=\"false\" iconCls=\"icon-edit\">更新信息</a>&nbsp;&nbsp;");
		html.append("<a href=\"javascript:location.reload();\" class=\"easyui-linkbutton\" plain=\"false\" iconCls=\"icon-reload\">刷新</a>");
		return html.toString();
		
	}
	public String getContent() {
		String content =  "";
		String demUUID = "8d3c8f39-f725-44c6-a865-3df14a887830";
		HashMap conditionMap = new HashMap();
		String userid = this.getUserContext()._userModel.getUserid();
		conditionMap.put("STAFFNO", userid);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID, conditionMap, null);
		HashMap data = null;
		if(list!=null&&list.size()>0){
			data = list.get(0);
			if(data!=null){
				StringBuffer html = new StringBuffer();
				html.append("<table class=\"mainTable\">").append("\n");
				html.append("	<tr>").append("\n");
				html.append("	<td class=\"groupTitle\">基本信息</td>").append("\n");
				html.append("	</tr>").append("\n");
				html.append("	<tr>").append("\n");
				html.append("	<td>").append("\n");
				html.append("		<table class=\"infoTable\">").append("\n");
				html.append("			<tr>").append("\n");
				html.append("				<td>").append("姓名：<span>").append(ObjectUtil.getString(data.get("NAME"))).append("</span></td>").append("\n");
				html.append("				<td>").append("性别：<span>").append(ObjectUtil.getString(data.get("SEX"))).append("</span></td>").append("\n");
				html.append("				<td>").append("出生日期：<span>").append(ObjectUtil.getString(data.get("BIRTHDAY"))).append("</span></td>").append("\n");
				html.append("			</tr>").append("\n");
				html.append("			<tr>").append("\n");
				html.append("				<td>").append("部门：<span>").append(ObjectUtil.getString(data.get("DEPTNAME"))).append("[").append(ObjectUtil.getString(data.get("DEPTNO"))).append("]</span></td>").append("\n");
				html.append("				<td>").append("籍贯：<span>").append(ObjectUtil.getString(data.get("JG"))).append("</span></td>").append("\n");
				html.append("				<td>").append("政治面貌：<span>").append(ObjectUtil.getString(data.get("ZZMM"))).append("</span></td>").append("\n");
				html.append("			</tr>").append("\n");
				html.append("			<tr>").append("\n");
				html.append("				<td>").append("电话：<span>").append(ObjectUtil.getString(data.get("HANDSET"))).append("</span></td>").append("\n");
				html.append("				<td>").append("邮箱：<span>").append(ObjectUtil.getString(data.get("E_MAIL"))).append("</span></td>").append("\n");
				html.append("				<td>").append("邮编：<span>").append(ObjectUtil.getString(data.get("ZIPCODEO"))).append("</span></td>").append("\n");
				html.append("			</tr>").append("\n");
				html.append("			<tr>").append("\n");
				html.append("				<td>").append("婚否：<span>").append(ObjectUtil.getString(data.get("ISMARRY"))).append("</span></td>").append("\n");
				html.append("				<td>").append("血型：<span>").append(ObjectUtil.getString(data.get("BLOODTYPE"))).append("</span></td>").append("\n");
				html.append("				<td>").append("学历：<span>").append(ObjectUtil.getString(data.get("EDUCATION"))).append("</span></td>").append("\n");
				html.append("			</tr>").append("\n");
//				html.append("			<tr>").append("\n");
//				html.append("				<td>").append("姓名：<span>").append(ObjectUtil.getString(data.get("NAME"))).append("</span></td>").append("\n");
//				html.append("				<td>").append("性别：<span>").append(ObjectUtil.getString(data.get("SEX"))).append("</span></td>").append("\n");
//				html.append("				<td>").append("出生日期：<span>").append(ObjectUtil.getString(data.get("BIRTHDAY"))).append("</span></td>").append("\n");
//				html.append("			</tr>").append("\n");
				html.append("		</table>").append("\n");
				html.append("	</td>").append("\n");
				html.append("	</tr>").append("\n");
				html.append("	<tr>").append("\n");
				html.append("	<td class=\"groupTitle\" >受教育记录</td>").append("\n");
				html.append("	</tr>").append("\n");
				html.append("	<tr>").append("\n");
				html.append("	<td>").append("\n");
				html.append("		<table class=\"infoTable\">").append("\n");
				html.append("			<tr>").append("\n");
				html.append("				<td class=\"data_title\" >").append("起始日期</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("截至日期</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("毕业院校</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("专业</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("学位</td>").append("\n");
				html.append("			</tr>").append("\n"); 
				html.append("			<tr  class=\"data_item\" >").append("\n");
				html.append("				<td>").append("<span>2002年9月</span></td>").append("\n");
				html.append("				<td>").append("<span>2006年7月</span></td>").append("\n");
				html.append("				<td>").append("<span>北京建筑工程学院</span></td>").append("\n");
				html.append("				<td>").append("<span>计算机应用</span></td>").append("\n");
				html.append("				<td>").append("<span>本科</span></td>").append("\n");
				html.append("			</tr>").append("\n");
				
//				html.append("			<tr>").append("\n");
//				html.append("				<td>").append("姓名：<span>").append(ObjectUtil.getString(data.get("NAME"))).append("</span></td>").append("\n");
//				html.append("				<td>").append("性别：<span>").append(ObjectUtil.getString(data.get("SEX"))).append("</span></td>").append("\n");
//				html.append("				<td>").append("出生日期：<span>").append(ObjectUtil.getString(data.get("BIRTHDAY"))).append("</span></td>").append("\n");
//				html.append("			</tr>").append("\n");
				html.append("		</table>").append("\n");
				html.append("	</td>").append("\n");
				html.append("	</tr>").append("\n");
				html.append("	<tr>").append("\n");
				html.append("	<td class=\"groupTitle\" >培训记录</td>").append("\n");
				html.append("	</tr>").append("\n");
				html.append("	<tr>").append("\n");
				html.append("	<td>").append("\n");
				html.append("		<table class=\"infoTable\">").append("\n");
				html.append("			<tr>").append("\n");
				html.append("				<td class=\"data_title\" >").append("起始日期</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("截至日期</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("毕业院校</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("专业</td>").append("\n");
				html.append("				<td class=\"data_title\" >").append("学位</td>").append("\n");
				html.append("			</tr>").append("\n"); 
				html.append("			<tr  class=\"data_item\" >").append("\n");
				html.append("				<td>").append("<span>2002年9月</span></td>").append("\n");
				html.append("				<td>").append("<span>2006年7月</span></td>").append("\n");
				html.append("				<td>").append("<span>北京建筑工程学院</span></td>").append("\n");
				html.append("				<td>").append("<span>计算机应用</span></td>").append("\n");
				html.append("				<td>").append("<span>本科</span></td>").append("\n");
				html.append("			</tr>").append("\n");
				
//				html.append("			<tr>").append("\n");
//				html.append("				<td>").append("姓名：<span>").append(ObjectUtil.getString(data.get("NAME"))).append("</span></td>").append("\n");
//				html.append("				<td>").append("性别：<span>").append(ObjectUtil.getString(data.get("SEX"))).append("</span></td>").append("\n");
//				html.append("				<td>").append("出生日期：<span>").append(ObjectUtil.getString(data.get("BIRTHDAY"))).append("</span></td>").append("\n");
//				html.append("			</tr>").append("\n");
				html.append("		</table>").append("\n");
				html.append("	</td>").append("\n");
				html.append("	</tr>").append("\n");
				html.append("</table>").append("\n");
				content = html.toString();
			}
		}
		return content;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return "url";
	}

}
