<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>金鹰BPM流程绩效分析</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/analytics/home_page.css" rel="stylesheet" type="text/css" /> 
	<link href="iwork_css/analytics/menu.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/excanvas.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/jquery.jqplot.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.dateAxisRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.canvasTextRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=iblank"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<SCRIPT type="text/javascript">
		
		$(document).ready(function(){
    // from their vertical bar counterpart.
		     var line1 = [['Cup Holder Pinion Bob', 7], ['Generic Fog Lamp', 9], ['HDTV Receiver', 15], 
					  ['8 Track Control Module', 12], [' Sludge Pump Fourier Modulator', 3], 
					  ['Transcender/Spice Rack', 6], ['Hair Spray Danger Indicator', 18]];
					  var line2 = [['Nickle', 28], ['Aluminum', 13], ['Xenon', 54], ['Silver', 47], 
					  ['Sulfer', 16], ['Silicon', 14], ['Vanadium', 23]];
					 
					  var plot2 = $.jqplot('chart2',<s:property value="data" escapeHtml="false"/>, {
					    series:[{renderer:$.jqplot.BarRenderer}, {xaxis:'x2axis', yaxis:'y2axis'}],
					    axesDefaults: {
					        tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
					        tickOptions: {
					          angle: 30
					        }
					    },
					    axes: {
					      xaxis: {
					        renderer: $.jqplot.CategoryAxisRenderer
					      },
					      x2axis: {
					        renderer: $.jqplot.CategoryAxisRenderer
					      },
					      yaxis: {
					        autoscale:false
					      },
					      y2axis: {
					        autoscale:false
					      }
					    }
					  });
			    
		});
		
		function showProcessChart(actDefid,title){
			var pageUrl = "process_analytics_process_chart.action?actDefid="+actDefid;	
			art.dialog.open(pageUrl,{ 
					id:'analytics_process_chart',
					cover:true,
					title:title,
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:680,
					cache:false,
					lock: true,
					height:480, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false
				});
		}
		//-->
	</script>
	<style >
	html {
}
		.title{
			margin:10px;
			font-size:22px;
			font-family:黑体;
			font-weight:bold;
		}
		.mainToolbar{ 
			height:30px;
			background-color:#efefef;
			border-bottom:1px solid #ccc;
			text-align:left;
		}
		.crosshair{ 
			height:30px;
			background-color:#efefef;
			border-bottom:1px solid #ccc;
			text-align:left;
			
		}
		.content{
			width:800px;
			margin-left:auto;
			margin-right:auto;
			background-color:#fff; 
		}
		.Tb_analytics{
			border:1px solid #efefef;
		}
		.Tb_analytics td{
			height:25px;
			text-align:center;
			padding-left:5px;
		}
	</style>    
</head>
<body  class="easyui-layout" >
	<div region="north" border="false" style="height:50px;overflow-y:hidden" split="false"  id="layoutNorth">
		<div class="title"><s:property value="title"/>节点效率分析</div>
	</div>
<div region="center" border="false" id="layoutCenter" >
	<div class="content">
<div style="margin-left:auto;margin-right:auto">
	<table width="100%" border="1"  class="Tb_analytics">
		<tr>
			<td class="mainToolbar">序号</td>
			<td class="mainToolbar">节点名称</td>
			<!-- <td class="mainToolbar">待办任务</td> -->
			<td class="mainToolbar">已办任务</td>
			<td class="mainToolbar">平均待阅时长</td>
			<td class="mainToolbar">平均办理时长</td>
			<td class="mainToolbar">最长办理时长</td>
			<td class="mainToolbar">最短办理时长</td>
		</tr>
		<s:property value="tableHtml" escapeHtml="false"/>
	</table>
	
</div>
<div id="chart2" ></div>
</div>
</body>
</html>
	