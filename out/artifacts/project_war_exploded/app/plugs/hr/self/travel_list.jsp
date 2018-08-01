<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.topHead{
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.title{
			background:#f2f4f6;
			border-bottom:1px solid #c6c9ca;
			border-right:1px solid #c6c9ca;
			padding:2px;
			padding-left:5px;
			font-family:微软雅黑;
			line-height:25px;
		}
		
		.searchtitle{
			font-size:14px;
			font-family:微软雅黑;
			font-werght:bold;
			padding:3px;
			color:#304d79;
			text-align:right;
		}
		.maintitle span{
			color:#666;
			font-size:12px;
			padding:2px;
		}
		.item{
			height:30px;
			border-bottom:1px solid #efefef;
		}
		.item td{
			font-family:微软雅黑;
			padding-left:5px;
		}
		.item .mailTitle span{
			color:#a0a0a0;
			font-weight:500;
		}
		.item:hover{
			height:30px;
			background:#f3f3f3;
			cursor:pointer;
		}
		.unread{
			font-weight:bold;
		}
	</style>
	<script type="text/javascript">
	$(function(){
		$("#pp").pagination({
			total:<s:property value="total"/>, 
			pageNumber:<s:property value="pageNumber"/>,
			pageSize:<s:property value="pageSize"/>,
			onSelectPage:function(pageNumber, pageSize){
				var pageUrl="iwork_hr_self_travel.action?pageSize="+pageSize+"&pageNumber="+pageNumber;
					window.location.href = pageUrl;
				}
			});
		});
		function addItem(){
			var pageUrl="processRuntimeStartInstance.action?actDefId=CCSQLC:1:79804";
			window.open(pageUrl);
		}
		function doQuery(){
			var travel1 = $('#travel1').val();
			var travel2 = $('#travel2').val();
			var title1 = $('#title1').val();
			window.location.href = "iwork_hr_self_travel.action?travel1=" + travel1 + "&travel2=" + travel2 + "&title1=" + title1;
		}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false" > 
		 <div class="tools_nav">
		 	<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">新建申请单</a>
		 </div>
		 	<div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;">
				<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
					<tr> 
						 <td style='padding-top:10px;padding-bottom:10px;'> 
						 <s:form name="mainFrm" id="mainFrm" action="iwork_hr_self_travel"> 
							<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
								<tr>
									<td class= "searchtitle">申请日期:</td>
									<td class= "searchdata">
										<input type="text" onfocus="WdatePicker()"  name="travel1" value="<s:property value="travel1"/>" id="travel1" style="width:100px;"/> 到 <input type="text" onfocus="WdatePicker()"  name="travel2" value="<s:property value="travel2"/>" id="travel2" style="width:100px;"/>
									</td>
									
									<td class= "searchtitle">出差原因:</td>
									<td class= "searchdata">
									<input type="text" name="title1" value="<s:property value="title1"/>" id="title1" class="" style="width:150px;"/>
									</td>
									<td align='right'>
											<a id="btnEp" class="easyui-linkbutton" icon="icon-search" href="javascript:doQuery();" >查询</a>
									</td>
								</tr>
							</table>
							</s:form> 
						</td>
					</tr>
				</table>
			</div>
        </div>  
            <div region="center" border="false" style="background: #fff;border:1px solid #fff">
            	<table width="100%" cellspacing="0" cellpadding="0" border="0">
            		<tr>
		 				<td class="title" style="width:30px;">序号</td>
		 				<td class="title" style="width:120px;" >单据编号</td>
		 				<td class="title" style="width:80px;">申请日期</td>
		 				<td class="title" style="width:80px;" >起始时间</td>
		 				<td class="title" style="width:80px;" >结束时间</td>
		 				<td class="title" style="width:80px;">目的地</td>
		 				<td class="title"  style="width:120px;" >出差原因</td>
		 				<td class="title"  style="width:120px;" >出差类型</td>
		 				<td class="title" style="width:80px;">随同人员</td>
		 			</tr>
		 			 <s:property value="html" escapeHtml="false"/>
		 		</table> 
            </div> 
            <div region="south" border="false" style="height: 32px;">
				<div id="pp" style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x;border:1px solid #ccc;"></div>
			</div>
</body>
</html>
