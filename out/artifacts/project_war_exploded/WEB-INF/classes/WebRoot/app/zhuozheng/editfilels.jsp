<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java" import="com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<html>
<head> 
 

<title>控件选择</title>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/pageoffice.js" id="po_js_main"></script>
 
</head>	
  
  <body>
			<s:hidden name="NOTICEFILE"/> 
  </body>
  <script type="text/javascript" >
  function zhuoZhengOpenRead(){
				var NOTICEFILE=document.getElementById("NOTICEFILE").value;
				POBrowser.openWindowModeless("getLsZz.action?id="+NOTICEFILE,"width=1200px;height =800px;frame=yes;");
				} 
 		window.onload=function(){ 
 		
 		zhuoZhengOpenRead();
    	setTimeout(function(){ 
				window.opener=null;    
				window.open("","_self");    
				window.close();
				},8000);
				}
    	  
	</script> 			
</html>
