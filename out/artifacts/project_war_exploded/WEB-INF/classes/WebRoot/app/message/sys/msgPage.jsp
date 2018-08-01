<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/sys_procdef.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/message/sysmsgpage.js"></script>
	
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;height:50px">
		<div id="divsysmsgall" style="margin-top:5px;margin-left:15px;"> 
			<font color='red'>全部</font> <span class="cd">┊</span><a href="###" class="sl" onClick="getUnread();"> 未读 </a> 
			<!--<a href="###" class="sl" onClick="getMsgRead();">已读</a>--><span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgCommon();">系统消息</a> <span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgWorkFlow()"> 流程提醒</a> <span class="cd">
			┊</span> &nbsp;<a href="###" class="sl" onClick="getMsgBirth();">生日提醒</a> 
			</div> 
			<div id="divmsgunread" style="margin-top:5px;margin-left:15px;display:none;"> 
			<a href="###" class="sl" onClick="getMsgAll();">全部 </a><span class="cd">┊</span>
			<font color='red'>未读</font> 
			<!--<a href="###" class="sl" onClick="getMsgRead();">已读</a>--><span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgCommon();">系统消息</a> <span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgWorkFlow()"> 流程提醒</a> <span class="cd">
			┊</span> &nbsp;<a href="###" class="sl" onClick="getMsgBirth();">生日提醒</a> 
			</div> 
			<div id="divmsgread" style="margin-top:5px;margin-left:15px;display:none;"> 
			<a href="###" class="sl" onClick="getMsgAll();">全部</a><span class="cd">┊</span>
			<a href="###" class="sl" onClick="getUnread();">未读</a> 
			<font color='red'>已读</font><span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgCommon();">系统消息</a> <span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgWorkFlow()"> 流程提醒</a> <span class="cd">
			┊</span> &nbsp;<a href="###" class="sl" onClick="getMsgBirth();">生日提醒</a> 
			</div> 
			<div id="divsysmsgcommon" style="margin-top:5px;margin-left:15px;display:none;"> 
			<a href="###" class="sl" onClick="getMsgAll();">全部 </a><span class="cd">┊</span>
			<a href="###" class="sl" onClick="getUnread();">未读 </a> 
			<!--<a href="###" class="sl" onClick="getMsgRead();">已读</a>--><span class="cd">
			┊</span> <font color='red'>系统消息</font> <span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgWorkFlow()"> 流程提醒</a> <span class="cd">
			┊</span>  &nbsp;<a href="###" class="sl" onClick="getMsgBirth();">生日提醒</a> 
			</div>
			<div id="divsysmsgworkflow" style="margin-top:5px;margin-left:15px;display:none;"> 
			<a href="###" class="sl" onClick="getMsgAll();">全部 </a><span class="cd">┊</span>
			<a href="###" class="sl" onClick="getUnread();">未读 </a> 
			<!--<a href="###" class="sl" onClick="getMsgRead();">已读</a>--><span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgCommon()"> 系统消息</a> <span class="cd">
			┊</span><a> <font color='red'>流程提醒</font></a> <span class="cd">
			┊</span>  &nbsp;<a href="###" class="sl" onClick="getMsgBirth();">生日提醒</a> 
			</div>
			<div id="divsysmsgbirth" style="margin-top:5px;margin-left:15px;display:none;"> 
			<a href="###" class="sl" onClick="getMsgAll();">全部 </a><span class="cd">┊</span>
			<a href="###" class="sl" onClick="getUnread();">未读 </a> 
			<!--<a href="###" class="sl" onClick="getMsgRead();">已读</a>--><span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgCommon()"> 系统消息</a> <span class="cd">
			┊</span> <a href="###" class="sl" onClick="getMsgWorkFlow()"> 流程提醒</a> <span class="cd">
			┊</span> &nbsp;<font color='red'>生日提醒</font>
			</div>
			<div id="op" style="margin-top:5px;text-align:right;margin-left:15px;">
			<a href="###" class="sl" onClick="setAllRead()">&nbsp;全部标记已阅</a> 
			
			<span class="cd">┊</span> 
			&nbsp;<a href="###" class="sl" onClick="setAllDel();">清空全部消息</a> 
			</div>
	</div>
   
	<div style="padding:3px;border:0px;" region="center" >
			<iframe name=message_list id=message_list onload="ReSizeiFrame(this)" smarginheight="0" scrolling="auto" marginwidth="0" style="border-top:0px solid #990000;" frameborder="0"  src="sysmsg_list.action" align=center width=100%></iframe>
			<form action="sysMessageAction!list.action" method=post name=frmMain>
			<s:hidden name="status"></s:hidden>
			<s:hidden name="type"></s:hidden>
			<s:hidden name="id"></s:hidden>
			<s:hidden name="currentPage"></s:hidden>
			<s:hidden name="cmd"></s:hidden>
		</form>	
	</div>
</body>
</html>
