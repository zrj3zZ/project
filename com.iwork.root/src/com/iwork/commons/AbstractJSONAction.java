package com.iwork.commons;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>Title: struts2</p>
 *
 * <p>Description: 响应信息为JSON类型的Action抽象类</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Newland</p>
 *
 * @author yaoxj
 * @version 1.0  2008-12-29
 */
public abstract class AbstractJSONAction extends  ActionSupport{
	
	
	
	
	
	public String getRealyPath(String path){
		return getServletContent().getRealPath(path);
	}
	
	
	/**
	 * 获取上下文对象
	 * @return
	 */
	public ServletContext getServletContent(){
		return ServletActionContext.getServletContext();
	}
	
	/**
	 * 获取request对象
	 * @return
	 */
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 获取response对象
	 * @return
	 */
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}

	/**
	 * 获取session
	 * @return
	 */
	public HttpSession getSession(){
		return getRequest().getSession();
	}
	
	
	

}
