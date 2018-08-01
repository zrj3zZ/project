package com.ibpmsoft.project.zqb.util;

import com.ibpmsoft.project.zqb.common.ZQBConstants;
import com.iwork.core.organization.tools.UserContextUtil;

public class CheckCurrentUserTypeUtil {
	 private static Object lock = new Object();  
	 private static CheckCurrentUserTypeUtil instance;  
	 public static CheckCurrentUserTypeUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new CheckCurrentUserTypeUtil();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	 /**
	  * R1  场外市场部总经理
	  * R2 项目负责人
	  * R3 持续督导
	  * R4 项目组成员
	  * R5 管理员
	  * @return
	  */
	 public String getCurrentRoleType(){
		String roleid =  UserContextUtil.getInstance().getCurrentUserContext()._orgRole.getId();
		String type = "";
		if(roleid.equals("5")){
			type = ZQBConstants.ISPURVIEW_ROLE_TYPE_CHANG;
		}else if(roleid.equals("6")){
			type = ZQBConstants.ISPURVIEW_ROLE_TYPE_PROJECTMANAGE;
		}else if(roleid.equals("4")){
			type = ZQBConstants.ISPURVIEW_ROLE_TYPE_DUDAO;
		}else if(roleid.equals("7")){
			type = ZQBConstants.ISPURVIEW_ROLE_TYPE_PROJECTUSER;
		}else if(roleid.equals("3")){  //董秘
			type = ZQBConstants.ISPURVIEW_ROLE_TYPE_CUSTORMER;
		}else if(roleid.equals("1")){  //董秘
			type = ZQBConstants.ISPURVIEW_ROLE_TYPE_MANAGER;
		}  
		return type;
	 }
	
	 
	 
	 
	 
	 
	 
	 
	 
	 

}
