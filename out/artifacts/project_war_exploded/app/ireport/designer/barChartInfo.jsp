<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <title>数据源设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript">
		function save(){
			alert("测试系统...");
		}
	</script>
    <style type="text/css">
		.td_title {
		        width:15%;
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";
				height:15px;			
			}
		.td_data{
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:12px;
			padding-top:5px;
			font-family:"微软雅黑";
			height:15px;
		}
   </style>  
  </head>
  
  <body class="easyui-layout">
     <div region="center" style="padding:3px;border-top:0px;">					
		<div style="border-bottom:1px solid #efefef;margin-bottom:3px;text-align:left;padding-right:20px;backgroud-color:#efefef">
			<a href="javascript:save();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>						
		</div>
		<div>
			<s:form name="chartForm" id="chartForm" action="ireport_designer_baseInfo_dbSourse_save.action" theme="simple">
				<table class = "table_form">
					<tr>
							<td class = "td_title">子标题:</td>
							<td class = "td_data"><s:textfield  name="baseModel.rpName" theme="simple"></s:textfield></td>
					</tr>
					<tr>
							<td class = "td_title">右边距:</td>
							<td class = "td_data"><s:textfield  name="baseModel.rowNum" cssStyle="width:30px" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">下边距:</td>
							<td class = "td_data"><s:radio  name="baseModel.condition"  listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="baseModel.condition" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">标题左移:</td>
							<td class = "td_data"><s:radio  name="baseModel.groupCount"  listKey="key" listValue="value"  list="#{'1':'一列','2':'二列','3':'三列'}" value="baseModel.groupCount" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">子标题左移:</td>
							<td class = "readonly_data"><s:radio  name="baseModel.status"  listKey="key" listValue="value"  list="#{'1':'开启','0':'关闭'}" value="baseModel.status" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">X轴数据:</td>
							<td class = "td_data"><s:textfield  name="baseModel.groupid" cssStyle="width:80px" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">Y轴数据:</td>
							<td class = "td_data"><s:textfield  name="baseModel.master" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					
					<tr>
							<td class = "td_title">图解:</td>
							<td class = "td_data"><s:textarea name="baseModel.memo" cssStyle="width:500px;height:100px" theme="simple"/></td>
					</tr>
					<s:hidden name="baseModel.id"></s:hidden> 
					<s:hidden name="baseModel.rpType"></s:hidden> 
				</table>
			</s:form>
		</div>
	</div>
  </body>
</html>
