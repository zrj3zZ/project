<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>

<script type="text/javascript" src="iwork_js/plugs/cmsinfoaction.js"  > </script>

<script type="text/javascript">	
	//发布
	function add(){
		//var url = "cmsRelease.action?portletid=<s:property value='portletid' escapeHtml='false'/>";
		var url = "cms_content_add.action?portletid=<s:property value='portletid' escapeHtml='false'/>"; 
		var newWin="newVikm";
		var page = window.open('form/waiting.html',newWin,'width=700,height=500,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location=url;
	}
	
	//删除
	function remove(){
		//根据表

	    var  rows = jQuery("#info_grid").jqGrid('getGridParam','selarrrow');		    
		if (rows.length>0){
		    $.messager.confirm('系统提示','确认删除?',function(result){  
		    if(result){
		        var s="";
		        for(var i=0;i<rows.length;i++){
		        var ret = jQuery("#info_grid").jqGrid('getRowData',rows[i]);
		         if (s != '') s += ',';
		         s += ret.INFOID;
				}	
				
				jQuery.ajax({
					url: 'cmsInfoRemove.action',
					data:{
						infoid:s,
						portletid:'<s:property value='portletid' escapeHtml='false'/>'
					},
					type: 'POST',
					beforeSend: function()
					{
					},
					success: function()
					{
							removeGridItem(rows);
					}
				});
		 	}
		 	})
		}else{
		$.messager.alert('提示信息','请选择您要删除的行项目!','info');  
		}
	}	
		
	//更改讨论状态
	function changeTalk(iid){
	    document.forms[0].portletid.value='<s:property value='portletid' escapeHtml='false'/>';
		var url = "cmsChangeXtTalk.action?contentid="+iid;
		document.forms[0].action=url;
		document.forms[0].submit();
		return false; 
	}
	
	//更改状态
	function changeStatus(iid){
	    document.forms[0].portletid.value='<s:property value='portletid' escapeHtml='false'/>';
		var url = "cmsChangeXtStatus.action?contentid="+iid;
		document.forms[0].action=url;
		document.forms[0].submit();
		return false; 
	}
	function generateRss(){
	    var  rows = jQuery("#info_grid").jqGrid('getGridParam','selarrrow');		    
		if (rows.length>0){
		    $.messager.confirm('系统提示','确认生成?',function(result){  
		    if(result){
		        var s="";
		        for(var i=0;i<rows.length;i++){
		        var ret = jQuery("#info_grid").jqGrid('getRowData',rows[i]);
		         if (s != '') s += ',';
		         s += ret.INFOID;
				}	
				
				jQuery.ajax({
					url: 'cmsInfoXtRss.action',
					data:{
						infoid:s,
						portletid:'<s:property value='portletid' escapeHtml='false'/>'
					},
					type: 'POST',
					beforeSend: function()
					{
					},
					success: function()
					{
							$.messager.alert("info","生成成功!");
					}
				});
		 	}
		 	})
		}else{
		$.messager.alert('提示信息','请选择您要生成的行项目!','info');  
		}
	}
</script>
<s:property value='pstrScript'  escapeHtml='false'/>
</head>
<body class="easyui-layout"> 
   <!-- TOP区 -->
	<div region="north" border="false" class="tools_nav" style="background-color:#efefef;padding:0px;overflow:no">
	<div style="padding:2px;">
		<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">发布</a>
		<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		<a href="javascript:generateRss();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">生成RSS</a>
	</div>
	</div>
    <!-- 内容区 -->
	<div region="center" style="padding:3px;">
	
		<table id='info_grid'></table>
		<div id='prowed_info_grid'></div>
		<s:property value='infolist'  escapeHtml='false'/>
    </div>
	<form id="contentform" method="post">	
	<input type='hidden' id='infoid' name='infoid' value=''>
    <input type='hidden' id='portletid' name='portletid'>
    </form>	
<iframe name='hidden_frame' id="hidden_frame" width= "0"  height= "0" style="VISIBILITY: hidden"></iframe>
</body>
</html>