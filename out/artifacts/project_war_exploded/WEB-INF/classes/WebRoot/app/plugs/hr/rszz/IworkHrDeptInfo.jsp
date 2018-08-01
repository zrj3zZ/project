<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>部门信息</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<style type="text/css">
		.td_title1 {
					line-height: 30px;
					font-size: 12px;
					text-align: center;
					letter-spacing: 0.1em;
					padding-right:10px;
					white-space:nowrap;
					border-bottom:1px #999999 thick;
					vertical-align:middle;
					font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data1 {
				color:#0000FF;
				line-height: 23px;
				text-align: center;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				color:0000FF; 
			}
	</style>
  </head>
  
  <body>
<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>序号</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>开始日期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>结束日期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>职位</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='9%' align='center'>员工组</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>SAP部门</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='8%' align='center'>SAP成本中心代码</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>SAP成本中心</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>SAP公司代码</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='20%' align='center'>SAP公司名称</td>
	</tr>
	<tr height = '25'>
		<td class="td_data1" ><s:property value='1'/></td>
		<td class="td_data1" ><s:property value='startdate'/></td>
		<td class="td_data1" ><s:property value='endDate'/></td>
		<td class="td_data1" ><s:property value='postName'/></td>
		<td class="td_data1" ><s:property value='empgroup'/></td>
		<td class="td_data1" ><s:property value='deptName'/></td>
		<td class="td_data1" ><s:property value='sapCostCenterCode'/></td>
		<td class="td_data1" ><s:property value='costCenterName'/></td>
		<td class="td_data1" ><s:property value='companySAPno'/></td>
		<td class="td_data1" ><s:property value='companyName'/></td>
	</tr>
</table>

  </body>
</html>
