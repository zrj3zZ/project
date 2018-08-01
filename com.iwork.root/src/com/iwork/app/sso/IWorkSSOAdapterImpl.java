package com.iwork.app.sso;

import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.client.authentication.AttributePrincipal;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.server.ge.control.sso.SSOAdapterInterface;
import com.iwork.core.server.util.UserUtil;

public class IWorkSSOAdapterImpl implements SSOAdapterInterface {
	  
	
	/**
	 * 检查登陆验证条件 
	 */
	public boolean check( HttpServletRequest request ,Object action) {
    	//获得response对象
		boolean flag = false;
		UserContext uc = UserUtil.getCurrentUser();
		if(uc!=null){
			flag = true;
		}
		return flag;
	}
	 
	/**
	 * 返回验证用户
	 */
	public String getCheckUserId( HttpServletRequest request ,Object action){
		AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
		String userid = "";
    	if(principal!=null){
    		userid = principal.getName().toUpperCase();
    	}
		return userid; 
	}
	
	 
	/**
	 * 返回验证用户
	 */
	public boolean logout(HttpServletRequest request ,Object action){
		AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
		String userid = "";
		if(principal!=null){
			userid = principal.getName().toUpperCase();
		}
		return false;
	} 
}
