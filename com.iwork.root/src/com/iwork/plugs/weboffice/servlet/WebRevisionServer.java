package com.iwork.plugs.weboffice.servlet;

import javax.servlet.http.HttpServlet;

import javax.servlet.*;
import javax.servlet.http.*;

import com.iwork.plugs.weboffice.service.IWorkPlugsWebRevisionService;

import java.io.*;

public class WebRevisionServer extends HttpServlet {
private static final String CONTENT_TYPE = "text/html; charset=GBK";
private IWorkPlugsWebRevisionService es;         //该类是上面创建的JavaBean类
protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
		resp.setContentType(CONTENT_TYPE);
		es = new IWorkPlugsWebRevisionService(); 
		es.executeRun(req,resp);
}
}
