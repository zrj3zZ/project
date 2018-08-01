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
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.meterGaugeRenderer.min.js"></script>
	<link rel="stylesheet" type="text/css" hrf="iwork_css/jquerycss/jquery.jqplot.min.css" />

	<script type="text/javascript">
		$(document).ready(function(){
			   s1 = [<s:property value="freeM"/>];
			   plot4 = $.jqplot('chart4',[s1],{
				   seriesDefaults: {
			           renderer: $.jqplot.MeterGaugeRenderer,
			           rendererOptions: {
			               label:"MB",
			               min: 0,
			               max: <s:property value="maxM"/>,
			               intervals:[100,<s:property value="maxM"/>*0.9,<s:property value="maxM"/>],
			               intervalColors:['#66cc66', '#93b75f']
			           }
			       }
			   });  
			});
		function setNum(num){
			 s1 = [num];
			   plot4 = $.jqplot('chart4',[s1],{
				   seriesDefaults: {
			           renderer: $.jqplot.MeterGaugeRenderer,
			           rendererOptions: {
			        	   label:"MB",
			               min: 0,
			               max: <s:property value="maxM"/>,
			               intervals:[100,<s:property value="maxM"/>*0.9,<s:property value="maxM"/>],
			               intervalColors:['#66cc66', '#93b75f']
			           }
			       }
			   });
		}
	
	</script>
	</head>
	<body >
	<div id="calendar" style="color:#efefef">30</div>
		<div id="chart4" class="plot" style="width:350px;height:200px;"></div>
	</body>
	<script>
	setInterval(function() { 
		$.ajax({ url: "systemJVMMonitorParams.action",success: function(data){
			setNum(data);
	      }});
		}, 30000);
	setInterval(function() { 
		var count = $("#calendar").text();
		if(count==0){
			count=30;
		}
		$("#calendar").text(count-1);
		}, 1000);
	
</script>
</html>
