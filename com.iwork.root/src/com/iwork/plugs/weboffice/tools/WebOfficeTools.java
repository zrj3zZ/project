package com.iwork.plugs.weboffice.tools;

import com.iwork.core.util.WebBrowserUtil;

public class WebOfficeTools {
	 private static WebOfficeTools instance;  
	 private static Object lock = new Object();  
	 public static WebOfficeTools getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new WebOfficeTools();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	 
	 public String getWebBrowserObjHtml(){
		 StringBuffer html = new StringBuffer();
		String webBrowserType =  WebBrowserUtil.getInstance().getWebBrowserType();		
		if(webBrowserType.equals(WebBrowserUtil.WEB_BROWSER_TYPE_FOR_IE)){
			html.append("<object id=\"WebOffice\" width=\"98%\"  classid=\"clsid:8B23EA28-2009-402F-92C4-59BE0E063499\" codebase=\"iwork_plugs/iWebOffice2009.cab#version=10,8,4,2\">").append("\n");
			html.append("</object>").append("\n");
		}else if(webBrowserType.equals(WebBrowserUtil.WEB_BROWSER_TYPE_FOR_CHROME)||webBrowserType.equals(WebBrowserUtil.WEB_BROWSER_TYPE_FOR_OPERA)||webBrowserType.equals(WebBrowserUtil.WEB_BROWSER_TYPE_FOR_FIREFOX)||webBrowserType.equals("Firefox")){
			html.append("<object id=\"WebOffice\" TYPE=\"application/kg-activex\" ALIGN=\"baseline\" BORDER=\"0\" WIDTH=\"100%\" HEIGHT=\"600px\" clsid=\"{8B23EA28-2009-402F-92C4-59BE0E063499}\" copyright=\"HNA Group CoLtd\" event_OnMenuClick=\"OnMenuClick\" event_OnToolsClick=\"OnToolsClick\">").append("\n");
		}else{
			html.append("<object id=\"WebOffice\" width=\"98%\" height=\"600\"  classid=\"clsid:8B23EA28-2009-402F-92C4-59BE0E063499\" codebase=\"iwork_plugs/iWebOffice2009.cab#version=10,8,4,2\">").append("\n");
			html.append("</object>").append("\n");
		} 
		 return html.toString();
	 }
}
