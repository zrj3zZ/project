package com.iwork.app.login.sso;

import javax.servlet.http.HttpServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;


public class SSOLoginServlet extends HttpServlet{
	private static final String CONTENT_TYPE = "text/html; charset=GBK";
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException{
		String ticket = req.getParameter("ticket");
			if(ticket!=null){
				resp.setContentType(CONTENT_TYPE);
				StringBuffer url = new StringBuffer();
				url.append("/login.action?ticket=").append(ticket);
				req.getRequestDispatcher(url.toString()).forward(req, resp);
			}
	} 
}
 