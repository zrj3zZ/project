<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>RSS订阅</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
<link href="iwork_css/rss/inettuts.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
        	function addRssInfo(){  
				parent.openWin("添加RSS订阅",450,380,"getAddRssPage.action",this.location);
        	} 
        	function deleteRssInfo(id){
				$.post('deleteRssSubscription.action',{id:id},function(){});
			}
			function resetRssUserProfile(){
				$.post('resetRssUserProfile.action',{},function(){alert("reload");location.reload();iNettuts.init();});
			}
			
        </script>
</head>
<body   class="easyui-layout" >
<div region="north" border="false" split="false" >
  <div id="head">
    <div class="toolbar"><a href="javascript:addRssInfo();">添加订阅</a>&nbsp;&nbsp;<a href="javascript:resetRssUserProfile();">恢复默认</a></div>
  </div>
</div>
<div region="center" border="false" split="false"  >
  <div id="columns">
    <s:property value="pageList" escapeHtml="false"/>
  </div>
</div>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.2.min.js"   ></script>
<script type="text/javascript" src="iwork_js/rss/jquery-ui-personalized-1.6rc2.min.js"></script>
<script type="text/javascript" src="iwork_js/rss/inettuts.js"></script>
</body>
</html>
