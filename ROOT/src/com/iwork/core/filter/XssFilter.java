package com.iwork.core.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;  
import javax.servlet.FilterChain;  
import javax.servlet.FilterConfig;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRequest;  
import javax.servlet.ServletResponse;  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibpmsoft.project.zqb.util.ConfigUtil;  
  
/** 
 * @Author hithedy 
 * @Date 2016年2月2日 
 * @Time 下午2:01:53 
 */  
public class XssFilter implements Filter {  
  
    public void destroy() {
    	
    }  
  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       		XssRequestWrapper xssRequest = new XssRequestWrapper((HttpServletRequest) request);  
    		
    		String uri0 = xssRequest.getRequestURI();
       		if(uri0.contains(".")){
       			String uri = uri0.substring(0, uri0.indexOf(".")).replace("/", "");
       			//xlj 后台页面脚本设置，表单中的行项目子表配置，生成表单,数据选择器的基本设置（编辑和添加），这6个地方不进行过滤
       			if(uri.equals("sysEngineIForm_formjs_save") || uri.equals("sysEngineSubform_save") || uri.equals("sysEngineIformDesginer_save") 
       					|| uri.equals("sys_dictionary_design_create") || uri.equals("sys_dictionary_design_saveBaseInfo") || uri.equals("wfprocdefprocessDeploy_saveProcessXML")  ){
       				chain.doFilter(request, response);
       			}else{
       				chain.doFilter(xssRequest, response);
       			}
       		}else{
       			chain.doFilter(xssRequest, response);
       		}
    }  
  
    public void init(FilterConfig arg0) throws ServletException {  
    	
    }  
  
}