<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<html>
<head> 
	<script type="text/javascript" src="/jquery.min.js"></script>
	<script type="text/javascript" src="/pageoffice.js" id="po_js_main"></script>
	<script type="text/javascript" >
			function zhuozhengopen(){
				var NOTICEFILE=document.getElementById("NOTICEFILE").value;
			 	POBrowser.openWindowModeless("zhuozhengpass.action?id="+NOTICEFILE,"width=1200px;height =800px;frame=yes;"); 
				
				} 	
 			 	window.onload=function(){ 
 			 	/* if (window.navigator.userAgent.indexOf("MSIE")>=1){
 			 	var NOTICEFILE=document.getElementById("NOTICEFILE").value;	
 			 	var pageUrl=encodeURI("zhuozhengpass.action?id="+NOTICEFILE);
 			 	var target = "_blank";
		
				var page = window.open('form/loader_frame.html',target,'width=1200,height=00,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.location = pageUrl;
				setTimeout(function(){ 
					window.opener=null;    
					window.open("","_self");    
					window.close();
					}, 8000); 
					}else{ } */
 				zhuozhengopen();
 		  		setTimeout(function(){ 
					window.opener=null;    
					window.open("","_self");    
					window.close();
					}, 8000); 
					
					
					 
    	 
    	 		};    	  		
	</script>
    <title>卓正软件</title>
    
	

  </head>
  
  <body>
    		
    		
    		
    		
				 <s:hidden name="NOTICEFILE"/> 
  </body>
</html>
