package com.iwork.plugs.email.util;

import java.io.File;

import org.apache.struts2.ServletActionContext;

import com.ibm.icu.util.Calendar;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.core.cache.BigTxtCache;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.BigTxtUtil;
import com.iwork.plugs.email.cache.EmailCache;

public class EmailUtil {

	 public static String buildUserAddress(UserContext uc,boolean isShowDept){
		 StringBuffer html = new StringBuffer();
		 StringBuffer title = new StringBuffer();
		 title.append(uc.get_deptModel().getDepartmentname()).append("\n");
		 title.append(uc.get_userModel().getUserid()).append("[").append(uc.get_userModel().getUsername()).append("]\n");
		 html.append("<span class=\"address\" title=\"").append(title).append("\">").append(uc.get_userModel().getUsername());
		 if(isShowDept){
			 html.append("[").append(uc.get_deptModel().getDepartmentname()).append("]");
		 }
		 html.append("<span>");
		 return html.toString();
	 }
	
	 
}
