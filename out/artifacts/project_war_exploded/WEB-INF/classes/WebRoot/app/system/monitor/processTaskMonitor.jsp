<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>IWORK综合应用管理系统</title>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/excanvas.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.highlighter.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.cursor.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.dateAxisRenderer.min.js"></script>
	<link rel="stylesheet" type="text/css" hrf="iwork_css/jquerycss/jquery.jqplot.min.css" />

	<script type="text/javascript">
	$(document).ready(function(){
		  var line1=<s:property value="list" escapeHtml="false"/>;
		  var plot1 = $.jqplot('chart1', [line1], {
		      title:'最近15天流程待办任务',
		      axes:{
		        xaxis:{
		          renderer:$.jqplot.DateAxisRenderer,
		          tickOptions:{
		            formatString:'%m月%#d日'
		          } 
		        },
		        yaxis:{
		          tickOptions:{
		            }
		        }
		      },
		      highlighter: {
		        show: true,
		        sizeAdjust: 7.5
		      },
		      cursor: {
		        show: true
		      }
		  });
		});
	</script>
	</head>
	<body >
	
		<div id="chart1" class="plot" style="width:300px;height:260px;"></div>
	</body>
</html>
