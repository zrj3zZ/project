

<%@ page language="java" import="com.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>


		<%
			FileSaver fs= new FileSaver(request, response);
			
		
			Object newfilepath1=request.getSession().getAttribute("startpath");
			Object endpath1=request.getParameter("endpath");
			String id=null;
			if(newfilepath1!=null){
			id=newfilepath1.toString();
			};
			String endpath=null;
			if(endpath1!=null){
			endpath=endpath1.toString();
			endpath=endpath.replace("/", "\\");
			}; 
			
			fs.saveToFile(request.getSession().getServletContext().getRealPath(id+endpath));
			
 			fs.close();
 			
			
		 %>

