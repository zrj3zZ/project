<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<META http-equiv="Last-Modified" content="Sat, 10 Nov 1997 09:08:07 GMT"/>
<title><s:text name="system.name"/></title>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_layout/default/css/icon.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
<%-- <script type="text/javascript" src="iwork_layout/skins2015/js/showtipswin.js"></script> --%>
<%-- <script type="text/javascript" src="iwork_js/index.js"></script> --%>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
			$(function() {
				$.ajaxSetup({timeout:8000});
				setWebBrowser();
				$('#mainFrameTab').tabs({});
				$("#syiframe").attr("src","pt_person_index.action");
				$("#deskframe").attr("src","processManagement_batch_index.action");
				$("#fspframe").attr("src","fsp_zydd_index.action");
				$("#sysframe").attr("src","sysmsg_index.action");
			});
			function setWebBrowser(){
				var Sys = {}; 
		        var ua = navigator.userAgent.toLowerCase(); 
		        var s; 
		        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
		        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : 
		        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : 
		        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : 
		        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		        var browserType = 'IE';
		        //以下进行测试
		        if (Sys.firefox) browserType = 'Firefox'; 
		        if (Sys.ie)browserType = 'IE'; 
		        if (Sys.chrome)browserType = 'Chrome'; 
		        if (Sys.opera)browserType = 'Opera'; 
		        if (Sys.safari)browserType = 'Safari';  
		        var url = "setWebBrowserType.action";
		        $.post(url,{browserType:browserType},function(response){});
			}
			function showUrl(url, title, icon, type) {
				if (type == "_blank") {
					window.open(url, '_blank', '');
				} else {
					addTab(title, url, icon);
				}
			}
			function openWin(title, height, width, pageurl, location, dialogId) {
				if (dialogId == 'undefined')
					dialogId = "mainDialogWin";
				art.dialog.open(pageurl,{
					id : dialogId,
					cover : true,
					title : title,
					loadingText : '正在加载中,请稍后...',
					bgcolor : '#999',
					width : width,
					cache : false,
					lock : true,
					esc : true,
					height : height,
					iconTitle : false,
					extendDrag : true,
					autoSize : true,
					close : function() {
						if (location != null) {
							location.reload();
						}
					}

				});
			}

			function unlogin() {
				if (confirm('您确定要退出本次登录吗?')) {
					$.ajax({
						url : 'logout.action', //后台处理程序
						type : 'post', //数据发送方式
						dataType : 'json'
					});
					location.href = 'login.action';
				}
			}
			//添加页签
			function addTab(subtitle, url, icon) {
				if (!$('#mainFrameTab').tabs('exists', subtitle)) {
					$('#mainFrameTab').tabs('add', {
						title : subtitle,
						singleSelect : true,
						closable : true,
						content : createFrame(url),
						icon : icon
					});
				} else {
					$('#mainFrameTab').tabs('select', subtitle);
				}
				return;
			}
			function createFrame(url) {
				var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
						+ '" style="width:100%;height:100%;"></iframe>';
				return s;
			}
			function setPWD() {
				var url = "syspersion_pwd_update.action";
				addTab("修改口令", url, "icon-user");
			}
		</script>
		<style type="text/css">
.nav {
	font-family: Trebuchet MS, sans-serif;
	font-size: 0;
	padding: 5px 5px 5px 0;
	width: 100%;
	background: -moz-linear-gradient(#f5f5f5, #c4c4c4); /* FF 3.6+ */
	background: -ms-linear-gradient(#f5f5f5, #c4c4c4); /* IE10 */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #f5f5f5),
		color-stop(100%, #c4c4c4)); /* Safari 4+, Chrome 2+ */
	background: -webkit-linear-gradient(#f5f5f5, #c4c4c4);
	/* Safari 5.1+, Chrome 10+ */
	background: -o-linear-gradient(#f5f5f5, #c4c4c4); /* Opera 11.10 */
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#f5f5f5',
		endColorstr='#c4c4c4'); /* IE6 & IE7 */
	-ms-filter:
		"progid:DXImageTransform.Microsoft.gradient(startColorstr='#f5f5f5', endColorstr='#c4c4c4')";
	/* IE8+ */
	background: linear-gradient(#f5f5f5, #c4c4c4); /* the standard */
}

.top_head {
	border-bottom: 0px solid #777;
	vertical-align: bottom;
	background-image: url(iwork_layout/skins2015/img/logo.png);
	background-repeat: no-repeat;
	height: 70px;
}

#fo {
	color: red;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false" split="false" class="nav"
		id="layoutNorth">
		<div class="top_head">
			<div style="padding:5px;text-align:right;">
				<span style="margin-right:20px"><a class="easyui-linkbutton"
					iconCls="icon-user" data-options="plain:true"><s:property
							value="currentUserStr" escapeHtml="false" /></a></span>
					<!-- <a
					href="javascript:addTab('我的日程','schCalendarAction.action','icon-calendar')"
					class="easyui-linkbutton" data-options="plain:true"
					iconCls="icon-calendar">工作日志</a> <a
					href="javascript:addTab('知识与培训','kmdoc_Index.action','icon-km')"
					class="easyui-linkbutton" data-options="plain:true"
					iconCls="icon-km">知识与培训</a><a
					href="javascript:addTab('问答','know_index.action','icon-model')"
					class="easyui-linkbutton" data-options="plain:true"
					iconCls="icon-model">问答</a> -->
					<a class="easyui-linkbutton">
						<span style="font-size: 11px;">上次登录IP/时间：<s:property value="ipaddress"/>&nbsp;<s:property value="lastTime"/></span>
					</a>
					<s:if test="roleid==3">
						<a href="iwork_file/BIGTXT_FILE/董秘用户手册.pdf" class="easyui-linkbutton" data-options="plain:true" iconCls="icon-sysbtn" target="_blank">帮助</a>
					</s:if>
					<s:else>
						<a href="iwork_file/BIGTXT_FILE/券商用户手册.pdf" class="easyui-linkbutton" data-options="plain:true" iconCls="icon-sysbtn" target="_blank">帮助</a>
					</s:else>
					<a href="javascript:addTab('个人设置','syspersion_info.action','icon-sysbtn');" 
					class="easyui-linkbutton" data-options="plain:true" iconCls="icon-sysbtn">设置</a> 
					<a href="javascript:unlogin()" class="easyui-linkbutton" data-options="plain:true" 
					id="loginOut" iconCls="icon-exit">退出</a>
			</div>
			<div style="padding:5px;padding-left:100px;">
				<a href="mainAction.action" class="easyui-linkbutton"
					iconCls="icon-home" data-options="plain:false">首页</a>
				<s:property value="topMenuHtml" escapeHtml="false" />
			</div>
		</div>
	</div>
	<div region="center" border="false" style="border:0px;" id="layoutCenter">
		<div id="mainFrameTab" class="easyui-tabs" fit="true" border="false">
			<div title="首页">
				<iframe id="syiframe" width="100%" height="98%" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0"></iframe>
			</div>
			<div title="待审核(<font id='fo'><span id='workflow'><s:property  value="dbCount"/></span></font>)">
				<iframe id="deskframe" width="100%" height="98%" name="deskframe" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0"></iframe>
			</div>
			<div title="待办事宜(<font id='fo'><s:property  value="fspCount"/></font>)">
				<iframe id="fspframe" width="100%" height="98%" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0"></iframe>
			</div>
			<div title="系统消息(<s:property  value="sysCount"/>)">
				<iframe id="sysframe" width="100%" height="98%" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0"></iframe>
			</div>
		</div>
	</div>
	<div region="south" id="layoutSouth" border="false" style="border-top:1px solid #efefef;padding:2px;height:15px;padding-left:10px;display:none">
	</div>
</body>
</html>
