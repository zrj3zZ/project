package com.iwork.plugs.email.util;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;

public class EmailCommonTools {
	 private static EmailCommonTools instance;  
	 private static Object lock = new Object();  
	 public static EmailCommonTools getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new EmailCommonTools();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	/**
	 * 构建地址簿列表
	 * @param addressStr
	 * @return
	 */
	public String buildAddressList(String addressStr){
		StringBuffer recevieUser = new StringBuffer();
		String[] userlist = addressStr.split(",");
		StringBuffer to_addresslist = new StringBuffer(); 
		for(String user:userlist){
			String userid = UserContextUtil.getInstance().getUserId(user);
			if(userid==null){
				recevieUser.append(user);
				continue;
			}
			UserContext tocontext = UserContextUtil.getInstance().getUserContext(userid);
			if(tocontext!=null){
				String address = EmailCommonTools.buildUserAddress(tocontext,false);
				if(address!=null){
					recevieUser.append(address);
				}
			}
		}
		return recevieUser.toString();
		
	}
	
	/**
	 * 构建地址簿列表
	 * @param addressStr
	 * @return
	 */
	public String buildUserList(String addressStr){
		String[] userlist = addressStr.split(",");
		StringBuffer users = new StringBuffer();
		for(String useraddress:userlist){
			String userId = UserContextUtil.getInstance().getUserId(useraddress);
			UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
			if(uc!=null){
				if(users == null || "".equals(users) || users.length()==0){
					users.append(uc.get_userModel().getUsername());
				}else{
					users.append(",").append(uc.get_userModel().getUsername());
				}
			}
		}
		return users.toString();
	}
	
	 public static String buildUserAddress(UserContext uc,boolean isShowDept){
		 StringBuffer html = new StringBuffer();
		 StringBuffer title = new StringBuffer();
		 title.append(uc.get_deptModel().getDepartmentname()).append("\n");
		 title.append(uc.get_userModel().getUserid()).append("[").append(uc.get_userModel().getUsername()).append("]\n");
		 html.append("<span class=\"address\" id=\"").append(uc.get_userModel().getUserid()).append("\" title=\"").append(title).append("\">").append(uc.get_userModel().getUsername());
		 if(isShowDept){
			 html.append("[").append(uc.get_deptModel().getDepartmentname()).append("]");
		 }
		 html.append("</span>");
		 return html.toString();
	 }
	 
	 public static String buildUserAddress(UserContext uc,Long isRead,boolean isShowDept){
		 StringBuffer html = new StringBuffer();
		 StringBuffer title = new StringBuffer();
		 title.append(uc.get_deptModel().getDepartmentname()).append("\n");
		 title.append(uc.get_userModel().getUserid()).append("[").append(uc.get_userModel().getUsername()).append("]\n");
		 if(isShowDept){
			 html.append("<span class=\"address\" id=\"").append(uc.get_userModel().getUserid()).append("\" title=\"").append(title).append("\">").append(uc.get_userModel().getUsername());
			 html.append("[").append(uc.get_deptModel().getDepartmentname()).append("]");
		 }else{
			 if(isRead!=null && isRead==1){
				 html.append("<span class=\"read\" id=\"").append(uc.get_userModel().getUserid()).append("\" title=\"").append(title).append("\">").append(uc.get_userModel().getUsername());
			 }else{
				 html.append("<span class=\"unread\" id=\"").append(uc.get_userModel().getUserid()).append("\" title=\"").append(title).append("\">").append(uc.get_userModel().getUsername());
			 }
		 }
		 html.append("</span>");
		 return html.toString();
	 }
	 /**
	  * 离职人员
	  * @param userid
	  * @return
	  */
	 public static String buildDisableUserAddress(String userid){
		 StringBuffer html = new StringBuffer();
		 StringBuffer title = new StringBuffer();
		 html.append("<span class=\"address\" title=\"").append(userid).append("\">").append(userid);
		 html.append("[注销]</span>");
		 return html.toString();
	 }
}
