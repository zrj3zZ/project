<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <title>显示分类专家</title>
    <style type="text/css">
    #rightbox {
		font-weight:bold; font-family:微软雅黑; font-size:12px; text-align:left;
		width:250px;
		height:auto;    	
		margin-right:20px;
		overflow:hidden;
    }
    </style>
  </head>
  
  <body>

    	<div id="rightbox">
	        <fieldset>
	            	<legend><b><font color="808080" >分类专家列表</font></b></legend>
	            	<div style="padding-left:5px; width:auto; height:400px; overflow-y:auto; border:1px #C0C0C0 solid;">
	            			<s:property value='selectHtml'  escapeHtml='false'/>
	                </div>	
	       </fieldset>
	   </div>
	 
  </body>
</html>
