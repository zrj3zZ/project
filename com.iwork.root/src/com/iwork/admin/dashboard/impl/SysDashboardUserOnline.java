package com.iwork.admin.dashboard.impl;

import java.util.List;

import com.iwork.admin.dashboard.SysConsoleDashboardInterface;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.session.UserOnlineManage;

public class SysDashboardUserOnline implements SysConsoleDashboardInterface {

	/**
	 * 获得在线用户列表
	 */
	public String getContent() {
		StringBuffer html = new StringBuffer();
		List<UserContext> list = UserOnlineManage.getInstance().getOnLineList();
		html.append("<div style=\"padding:5px;text-align:left;padding-left:10px;font-size:20px;border-bottom:1px solid #efefef\">在线用户数为<span style=\"font-size:30px;color:red\">").append(list.size()).append("</span>人</div>");
		for(UserContext context:list){
			html.append("<div  style=\"line-height:18px;margin:3px;font-size:10px;color:#666\">").append(context.get_userModel().getUsername()).append("[").append(context.get_deptModel().getDepartmentname()).append("]").append("</div>").append("\n");
		}
		return html.toString(); 
	}

}
