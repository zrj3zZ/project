<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WBS</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<style type="text/css">
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
		.TD_TITLE{
			padding:5px;
			width:200px;
			background-color:#efefef;
			text-align:right;
			
		}
		.TD_DATA{
			padding:5px;
			padding-left:15px;
			padding-right:30px;
			background-color:#fff;
			width:500px;
			text-align:left;
			border-bottom:1px solid #efefef;
		}
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		} 
		.cell:hover{
			background-color:#F0F0F0;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
	</style>
	<script type="text/javascript">
		
		// 全选、全清功能
		function selectAll(){
			if($("input[name='chk_list']").attr("checked")){
				$("input[name='chk_list']").attr("checked",true);
			}else{
				$("input[name='chk_list']").attr("checked",false);
			}
		}
		// 编辑模板
		function editTemplate(id,instanceid){
			var pageUrl = "openFormPage.action?formid=128&demId=76&instanceId="+instanceid;
					 art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'编辑模板',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:480, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
		}
		// 查看模板
		function readTemplate(id,instanceid){
			var pageUrl = "loadVisitPage.action?formid=128&demId=76&instanceId="+instanceid;
					 art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'查看模板',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:480, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
		}
		// 下载模板
		function downloadTemplate(id,fjid){
			// alert(fjid);
			var url = 'uploadifyDownload.action?fileUUID='+fjid;
			window.location.href = url;
			
		}
		// 删除模板
		function removeTemplate(id,instanceid){
			if(confirm("确定要删除模板吗？")){
				var pageUrl="hna_sanhui_template_delete.action";
				$.post(pageUrl,{id:id,instanceid:instanceid},function(data){
			         if(data=='success'){
			             window.location.reload();
			         }else{
			         	 alert("删除失败!");
			         }
			    });
			}
		}
	</script>
</head> 
<body class="easyui-layout">
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"><input type="checkbox" name="chk_list" onclick="selectAll()"></TD>
				<TD style="width:150px">公司名称</TD>
				<TD style="width:150px">模板名称</TD>
				<TD style="width:100px">是否为默认模板</TD>
				<TD style="width:60px">创建日期</TD>
				<TD style="width:60px">创建人</TD>
				<TD style="width:100px">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<TD><input type="checkbox" name="chk_list" value="<s:property value="ID"/>"></TD>
				<TD><s:property value="COMPANYNAME"/></TD>
				<TD><a href="javascript:readTemplate(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)"><s:property value="TEMPLATENAME"/></a></TD>
				<TD><s:property value="IS_DEF"/></TD>
				<TD><s:property value="CREATEDATE"/></TD>
				<TD><s:property value="CREATEUSER"/></TD>
				<TD>
					<a href="javascript:editTemplate(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">编辑</a> |
					<a href="javascript:downloadTemplate(<s:property value="ID"/>,'<s:property value="CONTENT"/>')">下载</a> |
					<a href="javascript:removeTemplate(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">删除</a>
				</TD>
			</TR>
			</s:iterator>
		</table>
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	</div>
	<s:hidden name="companyno" id="companyno"></s:hidden>
	<s:hidden name="companyname" id="companyname"></s:hidden>
</body>
</html>
