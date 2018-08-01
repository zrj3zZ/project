<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="head_bg">
	<div class="head"> 
	  <div class="logo" onclick="showMainPage();"><img src="iwork_layout/bpm2013/img/logo.png" height="35"></div>
	  <div class="head_font">
	    <div class="head_font_top">
	    <a href="#"  id="createCenter"  target="_parent">发起中心</a> 
	    | <a href="#"  id="reportCenter">报表中心</a> 
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