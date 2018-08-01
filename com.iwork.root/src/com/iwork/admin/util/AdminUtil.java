package com.iwork.admin.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.opensymphony.xwork2.ActionContext;

public class AdminUtil {
	private static AdminUtil instance;  
    private static Object lock = new Object();  
	 public static AdminUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new AdminUtil();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	 /**
	  * 判断当前用户是否是管理员登陆
	  * @return
	  */
	 public boolean isManagerLogin(){
		 boolean flag = false;
		HttpSession httpSession = null;
		LoginContext loginContext = null;
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		httpSession = request.getSession();
		if(httpSession!=null){
			loginContext = (LoginContext)httpSession.getAttribute(AppContextConstant.LOGIN_CONTEXT_INFO);
		}
		if(loginContext!=null&&loginContext.getLoginType()!=null&&loginContext.getLoginType().equals(LoginConst.LOGIN_TYPE_ADMIN)){ 
			flag = true;
		}
		return flag;
	 }
	 
	
}
