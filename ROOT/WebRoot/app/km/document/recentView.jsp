<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>最近浏览</title>
  </head>
  
  <body>
    <div id='results-display'>
    <s:iterator value='recentViewList'>
        <div class='day'><s:property value='readDate'/></div>
    	<div class='entry'>
    	    <div class='time'><s:property value='readTime'/></div>
    	    <div class='title' title='<s:property value='docName'/>'>
    	    	<a href="#" onclick="showDetail(0,<s:property value='docId'/>);return false;"><s:property value='docName'/></a>
    	    </div>
    	</div>
    </s:iterator>	
    </div>
  </body>
</html>
