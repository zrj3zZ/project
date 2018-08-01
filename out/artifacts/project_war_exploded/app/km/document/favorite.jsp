<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>最近浏览</title>
  </head>
  
  <body>
    <div>
	    <s:iterator value='favList'>
	    	<div>
				<a href="#" onclick="showDetail(<s:property value='ftype'/>,<s:property value='fid'/>);return false;">    	
	    	    	<s:if test="ftype==0">
	    	    		<img src='iwork_img/page.png' border='0'/>
	    	    	</s:if>
	    	    	<s:else>
	    	    		<img src='iwork_img/ztree/diy/40.png' border='0'/>   
	    	    	</s:else>
	    	    	<s:property value='name'/>
	    	    </a>
	    	</div>
	    </s:iterator>
    </div>
  </body>
</html>
