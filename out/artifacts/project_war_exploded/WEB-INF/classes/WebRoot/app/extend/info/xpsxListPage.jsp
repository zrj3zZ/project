<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link href="iwork_css/common.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/process-icon.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/default/easyui.css" type="text/css" rel="stylesheet">
<link href="iwork_css/public.css" type="text/css" rel="stylesheet" >
<link href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" type="text/css" rel="stylesheet">
<link href="iwork_css/engine/iformpage.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/zTreeStyle.css" type="text/css" rel="stylesheet">
<link href="iwork_css/engine/sysenginemetadata.css" type="text/css" rel="stylesheet">
<link href="iwork_themes/easyui/gray/easyui.css" type="text/css" rel="stylesheet">
<script src="iwork_js/commons.js" language="javascript"></script>
<script src="iwork_js/jqueryjs/jquery-3.1.0.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.easyui.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/languages/grid.locale-cn.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8" type="text/javascript"></script>
<script src="iwork_js/engine/ifromworkbox.js" type="text/javascript"></script>
<script src="iwork_js/lhgdialog/lhgdialog.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	var api = frameElement.api, W = api.opener; 
	function addItem(id,sxmc){
		var formid= $("#xpsxformid").val();
		var demid= $("#xpsxdemid").val();
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&XPSXID="+id+"&XPSXNAME="+encodeURI(sxmc);
		art.dialog.open(pageUrl,{
			id:'iformPjrz',
			title:'添加信披事项',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:800,
			cache:false,
			lock: true,
			height:500, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			content:pageUrl,
			zIndex:3999,
			stack:true,
			modal:true,
			position:'absolute',
			close:function(){
				//pageClose();
			}
		});
	}
	function check(id,type) {
		var pageUrl = "zqb_xpsxt_type.action?nr="+encodeURI(type)+"&id="+id+"&type="+encodeURI(type);
		art.dialog.open(pageUrl,{ 
			title:type,
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:900,
			cache:false,
			lock: true,
			height:500, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			content:pageUrl,
			zIndex:3999,
			stack:true,
			modal:true,
			//position:'absolute',
			close:function(){}
		});
	}
	function pageClose() {
		if(typeof(api) =="undefined"){
			window.close();
		}else{ 
			api.close();
		}
	}
	</script>
<style type="text/css">
.searchtitle {text-align: right;padding: 5px;}
.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}
.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}
.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}
.cell:hover {background-color: #F0F0F0;}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<div style="padding:5px;text-align:center;">


			<table width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:25%;">信披事项</td>
					<td colspan="4" style="width:75%;">操作</td>
				</tr>
				<s:iterator value="xpsxlist" status="status">
					<tr class="cell">
						<td><s:property value="SXMC" /></td>
						<td><a href="javascript:check('<s:property value="ID"/>','适用规则')">适用规则</a></td>
						<td><a href="javascript:check('<s:property value="ID"/>','披露要求')">披露要求</a></td>
						<td><a href="javascript:check('<s:property value="ID"/>','步骤')">步骤</a></td>
						<td><a href="javascript:addItem('<s:property value="ID"/>','<s:property value="SXMC" />')">新增此事项</a></td>
					</tr>
				</s:iterator>
			</table>

			<form action="zqb_xpsx_list.action" method=post name=frmMain id=frmMain>
				<s:hidden name="xpsxdemid" id="xpsxdemid"></s:hidden>
				<s:hidden name="xpsxformid" id="xpsxformid"></s:hidden>		
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
</body>
</html>
