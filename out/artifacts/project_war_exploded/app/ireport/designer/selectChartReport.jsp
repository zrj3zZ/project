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
		$("#chartType").val(type);
		$('form').attr('action','ireport_designer_CreateChartReport.action');
		$("#editForm").submit();
	}
	
	function selectTR(obj){
		obj.class="report_tr";
	}
	function unselectTR(obj){
		obj.class="";
	}
	</script>
	<style type="text/css">
		.report_table{
			magin:5px;
		}
		.report_tr{
			 background-color:#CCCCCC";
		}
		.UserTips{
			font-family:微软雅黑;
			font-size:14px;
			color:#999;
			padding-top:10px;
			padding-left:5px;
			padding-bottom:25px;
		}
		
		.report_logo{
			font-family:微软雅黑;
			font-size:14px;
			cursor:pointer;
			padding-left:100px;
		}
		.report_title{
			font-family:微软雅黑;
			font-size:14px;
			cursor:pointer
		}
		.report_memo{
			font-family:宋体;
			font-size:12px;
			color:#999;
			cursor:pointer
		}
		.report_img{
			border:1px solid #efefef;
			padding:3px; 
		}
	</style>
</head>
<body class="easyui-layout">
	<div region="center" style="padding:10px;border-top:0px;" >
			<s:form name="editForm" id="editForm" >
			
				<table width="100%" class="report_table" border=0 >
					<tr>
						<td colspan="2" class="UserTips">
							<img src="iwork_img/nondynamic.gif"border="0">请选择您要创建的图标类型
						</td>
					</tr> 
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('table')">
						<td class="report_logo"><img src="iwork_img/menulogo_gongdan.gif" class="report_img"  border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">表格统计报表</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于清晰标注统计报表各项参数，支持excel格式导出</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('pie')">
						<td class="report_logo"><img src="iwork_img/menulogo_chanpingku.gif" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">饼图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示数据仅有一个要绘制的数据系列并且没有负值及零值时，显示各个部分需要占用比例</td>
								</tr>
							</table>
						</td>
					</tr>
					<!--
					
					
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('basicBar')">
						<td class="report_logo"><img src="iwork_img/menulogo_zidian.gif" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">横向柱形图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示一段时间内的数据变化或显示各项之间的比较情况，排列在工作表的列或行中的数据可以绘制到柱形图中
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('stackedBar')">
						<td class="report_logo"><img src="iwork_img/menulogo_zidian.gif" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">横向堆叠柱形图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示一段时间内的数据变化或显示各项之间的比较情况，排列在工作表的列或行中的数据可以绘制到柱形图中
									</td>
								</tr>
							</table>
						</td>
					</tr>
					-->
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('basicColumn')">
						<td class="report_logo"><img src="iwork_img/menulogo_zidian.gif" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">柱形图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示一段时间内的数据变化或显示各项之间的比较情况，多列数据横向排列
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('stackedColumn')">
						<td class="report_logo"><img src="iwork_img/menulogo_zidian.gif" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">堆叠柱形图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示一段时间内的数据变化或显示各项之间的比较情况，多列数据堆叠排列
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('line')">
						<td class="report_logo"><img src="iwork_img/menulogo_yewu.gif" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">折线图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示在相等时间间隔下数据的趋势。在折线图中，类别数据沿水平轴均匀分布，所有值数据沿垂直轴均匀分布</td>
								</tr>
							</table>
						</td>
					</tr>
					<!-- 
					<tr class="report_tr"  onmouseover="selectTR(this)"  onmouseout="unselectTR(this)" onclick="createReport('')">
						<td><img src="iwork_img/report4.png" class="report_img" border="0"></td>
						<td>
							<table>
								<tr>
									<td class="report_title">散点图</td>
								</tr>
								<tr>
									<td class ="report_memo">适用于显示和比较数值，比较大量数据点时，请使用散点图。散点图中包含的数据越多，比较的效果就越好</td>
								</tr>
							</table>
						</td>
					</tr> -->
				</table>
				<s:hidden name="groupid" id="groupid"></s:hidden>
				<s:hidden name="reportType" id="reportType"></s:hidden>
				<s:hidden name="chartType" id="chartType"></s:hidden>
			</s:form>
		</div>
		
</body>
</html>
