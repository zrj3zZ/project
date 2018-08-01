<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN">
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:property value="systemTitle"/></title>
	<link rel="stylesheet" type="text/css" media="screen,projection" charset="utf-8" href="iwork_layout/ecom/css/style.css" />
	<link rel="stylesheet" type="text/css" media="screen,projection" charset="utf-8" href="iwork_layout/ecom/css/jquery.tmailsider.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_layout/ecom/css/base.css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.7.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>    
	<script type="text/javascript" src="iwork_layout/ecom/js/index.js"></script>  
	<script type="text/javascript" src="iwork_layout/ecom/js/jquery.tmailsider.js"></script>
	
</head>
<body  class="easyui-layout" >
<div data-options="region:'north',border:false" style="height:58px;">
	<div class="head_bg">
	<div class="head"> 
	  <div class="logo" onclick="showMainPage();"><img src="iwork_layout/bpm2013/img/logo.png" height="35"></div>
	  <div class="head_font">
	    <div class="head_font_top">
	    <a href="#"  id="createCenter"  target="_parent">发起中心</a> 
	    | <a href="#"  id="reportCenter"  target="_parent">报表中心</a> 
	    | <a href="#"  id="eaglesSearch"  target="_parent">鹰眼检索</a> 
	    | <a href="#"  id="userOnLine" onclick="showOnline();"  target="_parent">在线用户</a> 
	    | <a href="#"  id="personConfig"  target="_parent">设置</a> 
	    | <a href="#"  id="loginOut"  target="_parent">退出平台</a></div>
	  </div>
	  <div class="head_info_font"> 
	     <div class="head_font_bottom">
	     <span><img src="iwork_layout/bpm2013/img/hire-me.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;"/><a class="basic" href="#" rel="user_tip.action?userid=<s:property value='userid'  escapeHtml='false'/>"><s:property value="currentUserStr"  escapeHtml="false"/></a></span>
	     <img src="iwork_layout/bpm2013/img/exclamation_octagon_fram.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;"/><span id="workflow">(<s:property value="todoCount"  escapeHtml="false"/>)</span>
	     <span  id="sysmsg"><img src="iwork_layout/bpm2013/img/email.png" width="16" height="16" style="vertical-align:text-bottom; margin-bottom:2px; margin-bottom:-2px\9; margin:0px 3px 0px 10px;" />(<s:property value="unreadCount"  escapeHtml="false"/>)</span></div>
	  </div>
	</div>
	</div>
</div>
<div data-options="region:'center',border:false">
				<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
			<div title="<s:text name="portal.tab.Index.title"/>"  cache="false" > 
					<iframe width='100%' height='99%'  src='iworkMainPage.action?channelid=0' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
			<div title="<s:text name="portal.tab.process.title"/>"  cache="false" > 
					<iframe width='100%' height='99%'  src='process_desk_index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
				<div title="<s:text name="portal.tab.desk.title"/>"  cache="false" > 
				<iframe width='100%' height='100%'  src='pt_person_index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
				<div title="<s:text name="portal.tab.search.title"/>"  cache="true" > 
				<iframe width='100%' height='99%'  src='eaglesSearch_Index.action' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div> 
			</div>
		<div id="Z_SubList" style="display:none">
			<div class="subView">
				<ul id="menuList">
				</ul>
			</div>
			
		</div>	
	</div>	
<div data-options="region:'west',split:false" style="width:140px;padding:0px;overflow:hidden;border-top:0px;border-bottom:0px;border-left:0px;border-right:1px solid #efefef;">
	<div id="Z_TypeList" class="Z_TypeList">
		<h1 class="title">
			<a>导航目录</a>
		</h1>
		<div class="Z_MenuList">
			<ul id="nav_menu">
				
			</ul>
		</div>
	</div>
</div>
	<div data-options="region:'south',border:false"  style="border-top:1px solid #ccc;padding:2px;height:25px;padding-left:10px;">
		<table width="100%" border="0">
			<tr>
				<td><div id="bgclock"></div></td>
				<td style="text-align:right;padding-right:5px;"><a id="userOnLineLink" href="###" onclick="showOnline();"><img src="iwork_img/user.png" border="0"/>在线用户</a></td>
			</tr>
		</table>
	</div> 
	<script type="text/javascript">
		$('#Z_TypeList').Z_TMAIL_SIDER();
	</script>
	<!--[if IE 6]>
	<script type="text/javascript" src="js/DD_belatedPNG.js"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.Z_TypeList .Z_MenuList h3,.Z_TypeList .menuIcon');
	</script>
	<![endif]-->
</body>
</html>
