<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/analytics/home_page.css" rel="stylesheet" type="text/css" /> 
	<link href="iwork_css/analytics/menu.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link href="iwork_css/analytics/process.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/jquerycss/jquery.jqplot.min.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/excanvas.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/jquery.jqplot.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.dateAxisRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.canvasTextRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			 //var line1 =	[1,1]	  
		 	 // var line1 = [4, 2, 9, 16];           //子统计1数据
		  //    var line2 = [3, 7, 6.25, 3.125];     //子统计2数据
			  var line1 = <s:property value="typeBarData" escapeHtml="false"/>;
			  var plot2 = $.jqplot('chart2', line1, {
			    title: '持续督导客户及重大事项申报情况', 
			    animateReplot: true,
			    seriesDefaults: {
			    		pointLabels: { show: true },
                        shadow: true,
                         showMarker: true, // 是否强调显示图中的数据节点
                         renderer: $.jqplot.BarRenderer,
                         rendererOptions: {
                             barWidth: 10,
                             barMargin:10
                        }
			    },
			      legend: {
			            show: true,
			            location: 'ne',  
			            placement: 'outsideGrid',
			            showLabels:true,  
			            labels:['负责的公司数','重大事项申报数'],     
			              marginRight: '40px'
			        },
			    axes: {
			      xaxis:{
			      ticks:<s:property value="typeBarLabel" escapeHtml="false"/>,
			      tickOptions: {
                                 show: true,
                                fontSize: '14px',
                                 fontFamily: 'tahoma,arial,"Hiragino Sans GB",宋体b8b体,sans-serif',
                                 showLabel: true, //是否显示刻度线以及坐标轴上的刻度值
                                 showMark: false,//设置是否显示刻度
                                 showGridline: false // 是否在图表区域显示刻度值方向的网格
                             },
			      showTickMarks: true,
			      renderer:$.jqplot.CategoryAxisRenderer},
			      yaxis:{ min : 0,  
                    tickInterval : '1',
                    padMax:1.3}}
			      
			  });
			   });
		
	</script>
  		<style type="text/css">
  			#chart2 .jqplot-point-label {
			  border: 1.5px solid #aaaaaa;
			  padding: 1px 3px;
			  background-color: #eeccdd;
			}
  		
  		</style>
	</style>	
  </head>
    <body class="easyui-layout">
      <div region="north" border="false" >
      	<div class="tools_nav">
      	<span style="float:right;padding-right:10px">图表|<a href="zqb_project_report.action">表格</a></span>
      	</div>
      	 
      </div>
      <div region="center"  border="false" >
				<div id="chart2" ></div>
		</div>
  </body>
</html>
		