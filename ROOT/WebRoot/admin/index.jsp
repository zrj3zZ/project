<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">  
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>DSP应用开发管理平台-后台管理</title>   
	<link rel="stylesheet" type="text/css" href="admin/css/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/black/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>  
	  <script type="text/javascript"> 
	  var xmlhttp;
		if (window.XMLHttpRequest)
		{
			//  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
			xmlhttp=new XMLHttpRequest();
		}
		else
		{
			// IE6, IE5 浏览器执行代码
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange=function()
		{
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{
				if(xmlhttp.responseText=='login'){
				<%-- 	<%  response.sendRedirect(request.getContextPath()+"/console.action");  %>  --%>
					window.location="../login.jsp";
				}
			}
		}
		xmlhttp.open("GET","checkqx.action",true);
		xmlhttp.send();
	  $(function(){
			   $('#mainFrameTab').tabs({
			    });
	   });
            function showUrl(url,title,icon,type){ 
            	if(type=="_blank"){
            		window.open(url,'_blank',''); 
            	}else{
            		addTab(title,url,icon);
            	}
            }
            
            function openWin(title,height,width,pageurl,location,dialogId){
    			if(typeof(dialogId)=='undefined'){
					dialogId = "winDiv"
				}
				art.dialog.open(pageurl,{
					id:dialogId, 
					title:title,  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:width,
				    height:height,
				    close:function(){
				    	if(location!=null){
				    		location.reload();
				    	}
				    }
				 });
    		}
			function unlogin(){
				 if(confirm('您确定要退出本次登录吗?')){
		                        $.ajax({
		        	       			url:'logout.action', //后台处理程序
		        	       			type:'post',         //数据发送方式
		        	       			dataType:'json'
		             			});
		                    location.href = 'console.action';
		                }
			} 
			//添加页签
		function addTab(subtitle,url,icon){  
			if(!$('#mainFrameTab').tabs('exists',subtitle)){
				$('#mainFrameTab').tabs('add',{
					title:subtitle,
					singleSelect:true,
					closable:true,
					content:createFrame(url),
					icon:icon
				}); 
			}else{
				$('#mainFrameTab').tabs('select',subtitle);
			}
			return false;
		}
		function createFrame(url)
		{
			var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			return s;
		} 
		function setPWD(){
			var url = "syspersion_pwd_update.action";
			addTab("修改口令",url,"icon-user");
		}
        </script>
</head>
<body  class="easyui-layout" >
<div region="north" border="false" split="false"  style="" id="layoutNorth">
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td style="color:#fff;font-size:16px;padding-left:20px;font-family:微软雅黑">DSP应用开发管理后台</td>
		<td>
		<div style="padding:2px;border:0px;text-align:right">
        <a href="javascript:setPWD();" class="easyui-linkbutton" data-options="plain:true" style="coloc:#fff">管理员口令修改</a>
		<a href="mainAction.action" target="_blank" class="easyui-linkbutton" data-options="plain:true" style="coloc:#fff">登陆至前台</a>
		<a href="javascript:unlogin();" class="easyui-linkbutton" data-options="plain:true" style="coloc:#fff">退出</a>
			</div>
		</td>
	</tr>
		<td colspan="2" style="border-bottom:0px solid #777">
		 	<div style="padding:5px;padding-left:50px;">
		 		<a href="javascript:showUrl('admin_dashboard_index.action','首页面板')" class="easyui-linkbutton" iconCls="icon-home" data-options="plain:false" >首页面板</a>
		 			<s:property value="toolbar" escapeHtml="false"/>
			</div>
		</td>
	</tr>
</table> 
</div>
<div region="center" border="false" style="border:0px;" id="layoutCenter" >
		<div id="mainFrameTab"   class="easyui-tabs" fit="true">   
			<div title="首页面板"  border="false"  iconCls="icon-home" cache="false" > 
					<iframe id="mainFrame" width='100%' height='100%'  src='admin_dashboard_index.action?channelid=0' frameborder=0  scrolling=auto  marginheight=0 marginwidth=0></iframe>
				</div>
		</div> 
</div> 
<div region="south"  id="layoutSouth"  border="false" style="border-top:1px solid #efefef;padding:2px;height:15px;padding-left:10px;display:none">
		
	</div>
</body>
</html>
