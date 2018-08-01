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
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/form_template.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/commons.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript">
$(function(){
		$(document).bind("contextmenu",function(e){
              return false;   
        });
	});
	function createReport(type){
		var groupid = $("#groupid").val();
		var reportType ="";
		if(type==1){
			reportType ="表单查询报表";
			$("#reportType").val(type);
			$('form').attr('action','ireport_designer_createReport.action');
			$("#editForm").submit();
		}else if(type==2){
			reportType ="SQL脚本查询报表";
			$("#reportType").val(type);
			$('form').attr('action','ireport_designer_createReport.action');
			$("#editForm").submit();
		}else if(type==3){
			$("#reportType").val(type);
			$('form').attr('action','ireport_designer_selectChartReport.action');
			$("#editForm").submit();
			reportType ="图形统计报表";
		}else if(type==4){
			reportType ="三方报表查询";
			$("#reportType").val(type);
			$('form').attr('action','ireport_designer_createReport.action');
			$("#editForm").submit();
		}else if(type==5){
			reportType ="三方报表查询";
		}else if(type==6){
			reportType ="数据集成报表查询";
			$("#reportType").val(type);
			$('form').attr('action','ireport_designer_createReport.action');
			$("#editForm").submit();
		}
	}

	</script>
</head>
<body >
<div class="layout_bg">
  <div class="layout_title">请选择您要创建的报表</div>
  <div class="layout_box"  onclick="createReport(1)" onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" > 
      <tr>
        <td width="29%" rowspan="2"><img src="iwork_img/ireport.gif" width="82" height="82" /></td>
        <td width="71%" style="font-weight:bold">表单数据查询报表</td>
      </tr>
      <tr>
        <td valign="top" style="color:#b5b5b5">	
根据表单模型，自动生成查询报表数据绑定关系，选择相应的查询选项，生成报表
</td>
      </tr>
    </table>
  </div>
  <div class="layout_box"  onclick="createReport(2)"  onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="29%" rowspan="2"><img src="iwork_img/report2.png" width="82" height="82" /></td>
        <td width="71%" style="font-weight:bold">SQL脚本查询报表</td>
      </tr>
      <tr>
        <td valign="top" style="color:#b5b5b5">	根据指定的数据源及SQL脚本，执行查询操作，适用于非表单数据表的查询、外部数据源查询</td>
      </tr>
    </table>
  </div>
  <div class="layout_box"   onclick="createReport(3)"  onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="29%" rowspan="2"><img src="iwork_img/tongjireport.png" width="82" height="82" /></td>
        <td width="71%" style="font-weight:bold">图形统计报表查询</td>
      </tr>
      <tr>
        <td valign="top" style="color:#b5b5b5">	支持饼图、柱图及折线图实现图形统计报表查询显示</td>
      </tr>
    </table>
  </div>
    <div class="layout_box"  onclick="createReport(6)"  onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="29%" rowspan="2"><img src="iwork_img/node_icon7.jpg" width="82" height="82" /></td>
        <td width="71%" style="font-weight:bold">数据集成报表查询</td>
      </tr>
      <tr>
        <td valign="top" style="color:#b5b5b5">支持数据集成平台定义的记录集查询，根据集成平台接口返回的结果集进行显示，执行集成数据报表查询</td>
      </tr>
    </table>
  </div>
    <div class="layout_box"  onclick="createReport(4)"  onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="29%" rowspan="2"><img src="iwork_img/jasperreport.png" width="82" height="82" /></td>
        <td width="71%" style="font-weight:bold">三方报表查询</td>
      </tr> 
      <tr>
        <td valign="top" style="color:#b5b5b5">支持IReport报表工具定义的JasperReport报表文件，执行复杂报表查询</td>
      </tr>
    </table>
  </div>
  <div class="layout_box"   onclick="createReport(5)" onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="29%" rowspan="2"><img src="iwork_img/engine/formtemplate/layout4.png" width="82" height="82" /></td>
        <td width="71%" style="font-weight:bold">自定义开发报表</td>
      </tr>
      <tr>
        <td valign="top" style="color:#b5b5b5">	通过程序开发的报表，可通过此功能进行注册，注册后的报表可出现在报表中心，统一进行授权管理</td>
      </tr>
    </table>
  </div>
</div>
<s:form name="editForm" id="editForm" >	
<s:hidden name="groupid" id="groupid"></s:hidden>
			<s:hidden name="reportType" id="reportType"></s:hidden>
		</s:form>
</body>
</html>
