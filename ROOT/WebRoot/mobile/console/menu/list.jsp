<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>子系统管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/navigation/sysindex.css">
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script language="javascript" src="iwork_js/commons.js"></script>
	    <script type="text/javascript" src="iwork_js/navigation/sysindex.js"></script>
	    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	    <script type="text/javascript">

 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 add(); return false;
				}
}); //快捷键

	function edit(id){
		var pageUrl = "system_edit.action?id="+id;
		 art.dialog.open(pageUrl,{
						id:'system_edit',
						title:'编辑',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:510
					 });
		}
	function changePageSize(currentPage,pagerMethod){
		var pageSize = $("#pageSize option:selected").val()
		var url = "sysindex.action?currentPage="+currentPage+"&pagerMethod="+pagerMethod+"&pageSize="+pageSize;
		window.location.href=url;
	}

	function moveup(id){
		$.post("system_moveup.action",{id:id},function(data){
			if(data=='success'){
				window.location.reload();
			}else{
				art.dialog.alert("操作失败");
			}
	    });
	}
	function movedown(id){
		$.post("system_movedown.action",{id:id},function(data){
			if(data=='success'){
				window.location.reload();
			}else{
				art.dialog.alert("操作失败");
			}
	    });
	}
	</script>
		<style type="text/css">
		.header th{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					font-size:12px;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
	</style>
	</head>
<body  class="easyui-layout">
<div region="north" border="false" style="height:40px;border-bottom:1px;">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
					<tr  >
						<td align="left" class="tools_nav">
							<a href="javascript:add();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
						</td>
					</tr>
		</table>
</div>
<div region="center" style="padding:3px;border:0px;border-left:1px solid #efefef">
	<table  width="100%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
    <tr class="header" >
        <th  width="39" >序号</th>
        <th width="220" align="left" >子系统名称</th>
        <th width="80" align="center" >ICON</th>
        <th width="240" style="display:none">URL</th>
        <th width="169"  >提交目标</th>
        <th width="163"  >操作</th>
    </tr>
    <s:iterator value="availableItems" status="status">
        <tr class="cell">
	        <td class="RowData2">
	         <s:property value="#status.count"/>
	        </td>
            <td class="RowData2">
	            <a href='javascript:edit(<s:property value="id"/>);'>
	            <img alt="子系统" src="iwork_img/domain.gif" border="0">
	            <s:property value="sysName"/>
	            </a>
            </td>
            <td class="RowData2"><img src='iwork_img/adds.gif' border="0" onerror="this.src='iwork_img/adds.gif'"></td>
            <td class="RowData2"><s:property value="sysUrl"/></td>
            <td class="RowData2" style="display:none"><s:property value="sysTarget" /></td>
            <td align="left" class="RowData2">
            	<a href='javascript:edit(<s:property value="id"/>);'> <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            	
            	<s:if test="#status.first==true">
            	    <a href='javascript:void(0);' onclick='movedown(<s:property value="id" />)'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true">
            		<a href='javascript:void(0);' onclick='moveup(<s:property value="id" />)'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='javascript:void(0);' onclick='moveup(<s:property value="id" />)'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='javascript:void(0);' onclick='movedown(<s:property value="id" />)'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else>
            	<a href='javascript:confirm1(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a> 
            </td>
        </tr>
    </s:iterator>
    <tr align="right">
    	<td colspan="5">
    		共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<select id="pageSize" value="<s:property value='pageSize' />" onchange="changePageSize(<s:property value='currentPage'/>,'first')">
    			<option value="10" <s:if test="pageSize==10">selected</s:if>>10</option>
    			<option value="20" <s:if test="pageSize==20">selected</s:if>>20</option>
    			<option value="30" <s:if test="pageSize==30">selected</s:if>>30</option>
    			<option value="50" <s:if test="pageSize==50">selected</s:if>>50</option>
    		</select>
    		<a href="javascript:changePageSize(<s:property value='currentPage'/>,'first')">首页</a>
    		<a href="javascript:changePageSize(<s:property value='currentPage'/>,'previous')">上一页</a>
    		<a href="javascript:changePageSize(<s:property value='currentPage'/>,'next')">下一页</a>
    		<a href="javascript:changePageSize(<s:property value='currentPage'/>,'last')">尾页</a>
    	</td>
    </tr>
    </tbody>
</table>
</div>
</body>
</html>
