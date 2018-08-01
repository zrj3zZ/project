package com.iwork.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.iwork.core.organization.context.UserContext;

public class LoginFilter implements Filter {
	
	private UserContext uc;

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		String url=req.getRequestURI();
		if((!"/login.action".equals(url))
				&&(!"/console.action".equals(url))
				&&(!"/dologin.action".equals(url))
				&&(!"/showidentifyCode.action".equals(url))
				&&(!"/adminDoLogin.action".equals(url))
				&&(!"/getmobileyzm.action".equals(url))
				&&(!"/getPhoneYZ.action".equals(url))
				){
			HttpSession session=req.getSession();
			uc = (UserContext) session.getAttribute("USER_CONTEXT");
			if(uc==null){
				((HttpServletResponse) response).sendRedirect("/login.action");
				return;
			}
			if(req.getSession(true).isNew()){
				((HttpServletResponse) response).sendRedirect("/login.action");
				return;
			}
		}
		arg2.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
