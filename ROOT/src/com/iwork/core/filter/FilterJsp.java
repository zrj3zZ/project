package com.iwork.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
 public class FilterJsp implements Filter{
     public void init(FilterConfig config) throws ServletException{} //初始化过滤器
     public void destroy(){}//过滤器的生命周期结束
 
     public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
         throws IOException, ServletException{
    	 HttpServletRequest req = (HttpServletRequest) request;
         String url = req.getRequestURI();
         if(url.indexOf(".")>0){
            if("login".equals(url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".")))
            		||"savefile".equals(url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".")))
            		){
            	filter.doFilter(request, response);
            }else{             
            	request.getRequestDispatcher("/app/public/err.jsp").forward(request, response);
            }
         }else{             
        	 request.getRequestDispatcher("/app/public/err.jsp").forward(request, response);
         }
     }
 }