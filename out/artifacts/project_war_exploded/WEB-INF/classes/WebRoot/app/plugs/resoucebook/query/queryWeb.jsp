<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	   <title>资源预定信息查询</title>
	   <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	   <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/plugs/loadquery.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	   <!-- 数据字典的CSS JS -->
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	   <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	   <script>
	   function openDictionary(uuid,subformkey,subformid){
		var pageUrl = "sys_dictionary_runtime_show.action?dictionaryUUID="+uuid+"&subformkey="+subformkey+"&subformid="+subformid;
		art.dialog.open(pageUrl,{
				id:'DictionaryDialog',
				cover:true,
				title:"数据选择",
				width:800,
				height:520,
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true, 
				lock: true,
				iconTitle:false,
				extendDrag:true, 
				autoSize:false,
				resize:false
			});
		dg.ShowDialog();
	} 
	   </script>
			<style>
		.topCss{
			padding:5px;
		}
		.topCss table{
			border:1px solid #efefef;
		}
		.topCss tr{
			line-height:20px;
			padding:2px;
		}
		.topCss td{
			height:30px;
			padding:2px;
			padding-left:10px;
		}
		
		.tableList th{
			height:30px;
			padding:2px;
			padding-left:10px;
			background-color:#efefef;
			text-align:left;
		}
		.tableList tr{
			line-height:20px;
			padding:2px;
			border-bottom:1px solid #efefef;
		}
		.tableList tr:hover{
			background-color:#f6f6f6;
		}
		.tableList td{
			height:30px;
			padding:2px;
			padding-left:10px;
			text-align:left;
		}
	   .addItemBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:13px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addDeleBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:13px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addDeleBtn span{
			background-image:url(iwork_img/but_delete.gif);
			background-repeat:no-repeat;
			background-position:20px 5px;
			padding:5px;
			padding-left:40px;
			text-align:left;
		}
		.addDeleBtn:hover span{
			border:2px solid #efefef;
			color:red;
		}
		.addItemBtn span{
			background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			padding:5px;
			padding-left:40px;
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
		.attachDiv{
			list-style-type:none;
		}
		.attachDiv li a{
			color:#0000ff;
		}
	
	</style>
		</head>
	<body class="easyui-layout">

<!-- TOP区 -->
	<div region="north" border="false" class="topCss">
	<fieldset>
                  <legend>信息查询</legend>   
	<s:form>
<table width="100%"><tr><td >	
空间ID：<input type=text size='8' class="easyui-validatebox"  name='qspaceid' id='SPACEID' value='<s:property value='tqspaceid'  escapeHtml='false'/>'/><a href="##" onclick="openDictionary('4aec137bb3ba46ed87608799d82a88bc');" style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary"></a>
</td><td >空间名称：<input type=text size='8' class="easyui-validatebox" name='qspacename' id='SPACENAME' value='<s:property value='tqspacename'  escapeHtml='false'/>' />
</td><td >资源ID：<input type=text size='8' class="easyui-validatebox" name='qresouceid' id='RESOUCEID' value='<s:property value='tqresouceid'  escapeHtml='false'/>' />
</td><td >资源名称：<input type=text size='8' class="easyui-validatebox" name='qresoucename' id='RESOUCE' value='<s:property value='tqresoucename'  escapeHtml='false'/>' />
</td><td >预定人ID：<input type=text size='8' class="easyui-validatebox" name='quserid' id='USERID' value='<s:property value='tquserid'  escapeHtml='false'/>' />
</td></tr>
<tr><td >预定人姓名：<input type=text size='8' class="easyui-validatebox" name='qusername' id='USERNAME' value='<s:property value='tqusername'  escapeHtml='false'/>'/>
</td><td >预定日期：<input type=text  id='BEGINTIME' name='qdate'  style='width:100px;' class='easyui-datebox' editable='true' value='<s:property value='tqdate'  escapeHtml='false'/>'>
</td><td >预定状态：<input type="radio" value="1" name="qstatus" id="qstatus">有效
<input type="radio" value="2" name="qstatus" id="qstatus">作废</td><td></td><td colspan='2'>
<input type=button value='查 询'  onClick="query()" name='buttonquery'  border='0' class="font1">
 </td></tr></table>  </s:form></div> 
 
    <div region="center" border="false" style="padding:3px;">
		<table id="metadata_grid" style="margin:2px;"></table>
		<div style="text-align:right;padding-right:5px;">
		
			    </div>
			    </fieldset>
	</div>
</body>
<script>
		$(function(){	
		                  
		$('#metadata_grid').datagrid({
	             	url:"queryWeb.action?tqspaceid=<s:property value='tqspaceid'  escapeHtml='false'/>&tquserid=<s:property value='tquserid'  escapeHtml='false'/>&tqstatus=<s:property value='tqstatus'  escapeHtml='false'/>&tqdate=<s:property value='tqdate'  escapeHtml='false'/>&tqspacename="+encodeURI(encodeURI('<s:property value='tqspacename'  escapeHtml='false'/>'))+"&tqusername="+encodeURI(encodeURI('<s:property value='tqusername'  escapeHtml='false'/>'))+"&tqresouceid="+encodeURI(encodeURI('<s:property value='tqresouceid'  escapeHtml='false'/>'))+"&tqresoucename="+encodeURI(encodeURI('<s:property value='tqresoucename'  escapeHtml='false'/>')),					
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					singleSelect:true,	
					onLoadSuccess:function(){ 
					var statusc='<s:property value='tqstatus'  escapeHtml='false'/>';
					var ridaolen1=document.forms[0].qstatus.length;
		        		for(var i=0;i<ridaolen1;i++){
		            		if(statusc==document.forms[0].qstatus[i].value){
		                	document.forms[0].qstatus[i].checked=true
		            		}
		        		}
					},			
					columns:[[
						{field:'SPACEID',title:'空间ID',width:25,align:'center'},
						{field:'SPACENAME',title:'空间名称',width:40,align:'center'},
						{field:'RESOUCEID',title:'资源ID',width:25,align:'center'},
						{field:'RESOUCENAME',title:'资源名称',width:40,align:'center'},
						{field:'USERID',title:'预定人ID',width:30,align:'center'},
						{field:'USERNAME',title:'预定人姓名',width:40,align:'center'},
						{field:'BEGINTIME',title:'预定开始时间',width:62,align:'center'},
						{field:'ENDTIME',title:'预定结束时间',width:62,align:'center'},
						{field:'STATUS',title:'预定状态',width:35,align:'center'},
						{field:'MEMO',title:'备注',width:40,align:'center'}
					]]
				});	
		});	
</script>

<script type="text/javascript" src="iwork_js/plugs/loadquery.js"></script>

</html>