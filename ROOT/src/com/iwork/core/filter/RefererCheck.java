package com.iwork.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RefererCheck implements Filter {
	
	private static String SERVER=null;
	
	//初始化过滤器
	public void init(FilterConfig config) throws ServletException{
		SERVER = config.getInitParameter("SERVER");
	}
    public void destroy(){}//过滤器的生命周期结束

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException{
    	HttpServletRequest req = (HttpServletRequest) request;
    	String a = req.getHeader("Referer").toString().substring(0, req.getHeader("Referer").toString().indexOf("/", 20));
    	String b = SERVER;
    	if(a.equals(b))
    		filter.doFilter(request, response);
    }
}
