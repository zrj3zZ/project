<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>IWORK综合应用管理系统</title>
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script type="text/javascript" src="iwork_js/commons.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>		
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_js/highCharts/highcharts.js"></script>
		<script type="text/javascript" src="iwork_js/highCharts/modules/exporting.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 

	<script type="text/javascript">
		
		$(document).ready(function(){
			var tempStr = $("#editForm").html().length;
			if(tempStr < 1500){
				doSubmit();
			}
		});
	
		function doSubmit(){
			var options = {
				error:errorFunc,
				success:successFunc 
			};
			$('#editForm').ajaxSubmit(options);
		}
		
		String.prototype.replaceAll = function(s1,s2) { 
		    return this.replace(new RegExp(s1,"gm"),s2); 
		}
		
		successFunc=function(responseText){
			var resultStr = eval(responseText);
			var chartType = resultStr[0].chartType;
			if("pie" == chartType){
				chartPie(resultStr);
			}else if(chartType.indexOf("Bar")!=-1){
				var options = chartBar(resultStr);
				if("basicBar" == chartType){
					options.plotOptions.bar.dataLabels.enabled = true;
				}else if("stackedBar" == chartType){
					options.plotOptions.series.stacking = 'normal';
				}
				chart = new Highcharts.Chart(options);
			}else if(chartType.indexOf("Column")!=-1){
				var options = chartColumn(resultStr);
				if("basicColumn" == chartType){
				
				}else if("stackedColumn" == chartType){
					options.plotOptions.column.stacking = 'normal';
				}
				chart = new Highcharts.Chart(options);
			}else if(chartType.indexOf("line")!=-1){
				var options = chartLine(resultStr);
				chart = new Highcharts.Chart(options);
			}
			
      	}
      	errorFunc=function(){
           alert("error...");
     	}
     	
     	/**
     	  *	拼接饼图
     	  *	author	：zhousong
     	  * date	：2012-6-19
     	  */
     	function chartPie(resultStr){
     		var dataList = resultStr[0].dataList;
     		var resultArray = new Array();
     		
     		for (var i = 0; i < dataList.length; i ++){
				resultArray.push([dataList[i].name, Math.round(dataList[i].y*Math.pow(10,2))/Math.pow(10,2)]);
			}
			var chart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'container',
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false
	            },
	            title: { 
	                text: resultStr[0].reportName
	            },
	            tooltip: {
	                formatter: function() {
	                    return '<b>'+ this.point.name +'</b>: '+ Math.round(this.percentage*Math.pow(10,2))/Math.pow(10,2) +' %' + ' 共：' + this.point.y;
	                }
	            },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: true,
	                        color: '#000000',
	                        connectorColor: '#000000',
	                        formatter: function() {
	                            return '<b>'+ this.point.name +'</b>: '+ Math.round(this.percentage*Math.pow(10,2))/Math.pow(10,2) +' %' + ' 共：' + this.point.y;
	                        }
	                    }
	                }
	            },
	            series: [{
	                type: 'pie',
	                name: 'Browser share',
	                data: []
	            }]
	        });
	        chart.series[0].setData(resultArray);
	        chart.redraw();
     	}
     	
     	/**
     	  *	横向柱形图
     	  *	author	：zhousong
     	  * date	：2012-6-20
     	  */
     	function chartBar(resultStr){
     		var dataList = resultStr[0].dataList;
     		
     		var options = {
				chart: {
					renderTo: 'container',
					type: 'bar'
				},
				title: {
					text: resultStr[0].reportName
				},
				xAxis: {
					categories: resultStr[0].categories,
					title: {
						text: null
					}
				},
				yAxis: {
					min: 0,
					title: {
						text: '',
						align: 'high'
					},
					labels: {
						overflow: 'justify'
					}
				},
				tooltip: {
					formatter: function() {
						return ''+ this.series.name +': '+ this.y;
					}
				},
				plotOptions: {
					bar: {
						dataLabels: {
						}
					},
					series: {
					}
				},
				legend: {
					layout: 'vertical',
					align: 'right',
					verticalAlign: 'top',
					x: -100,
					y: 100,
					floating: true,
					borderWidth: 1,
					backgroundColor: '#FFFFFF',
					shadow: true
				},
				credits: {
					enabled: false
				},
				series: []
			};
			options.series = [];  
     		
     		//[{"dataList":[{"name":"abc","data":["111","112","113"]},{"name":"ABC","data":["441","442","443"]}]}]
	        for(var i=0; i<dataList.length; i++){//构造柱形图的数据
	        	var tmpArray = new Array();
	        	var tmpData = dataList[i].data.toString().split(",");
	            options.series[i] = {   
                  	name: dataList[i].name,  
                  	data: []  
               	};  
               	
               	for(var j=0; j<tmpData.length; j++){
               		tmpArray[j] = parseFloat(tmpData[j]);
               	}
               	options.series[i].data = tmpArray;
	        }  
	        return options;
     	}
     	
     	/**
     	  *	纵向柱形图
     	  *	author	：zhousong
     	  * date	：2012-6-20
     	  */
     	function chartColumn(resultStr){
     		var dataList = resultStr[0].dataList;
     		
     		var options = {
     			chart: {
					renderTo: 'container',
					type: 'column'
				},
				title: {
					text: resultStr[0].reportName
				},
				subtitle: {
					text: ''
				},
				xAxis: {
					categories: resultStr[0].categories
				},
				yAxis: {
					min: 0,
					title: {
						text: ''
					}
				},
				legend: {
					layout: 'vertical',
					backgroundColor: '#FFFFFF',
					align: 'left',
					verticalAlign: 'top',
					x: 100,
					y: 70,
					floating: true,
					shadow: true
				},
				tooltip: {
					formatter: function() {
						return ''+
							this.x +': '+ this.y +'';
					}
				},
				plotOptions: {
					column: {
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
				series: []
			}
			options.series = [];  
     		
	        for(var i=0; i<dataList.length; i++){//构造柱形图的数据
	        	var tmpArray = new Array();
	        	var tmpData = dataList[i].data.toString().split(",");
	            options.series[i] = {   
                  	name: dataList[i].name,  
                  	data: []  
               	};  
               	
               	for(var j=0; j<tmpData.length; j++){
               		tmpArray[j] = parseFloat(tmpData[j]);
               	}
               	options.series[i].data = tmpArray;
	        }  
	        return options;
     	}
		
		/**
     	  *	线性形图
     	  *	author	：zhousong
     	  * date	：2012-6-20
     	  */
     	function chartLine(resultStr){
     		var dataList = resultStr[0].dataList;
     		var options = {
     			chart: {
					renderTo: 'container',
					type: 'line'
				},
				title: {
					text: resultStr[0].reportName
				},
				subtitle: {
					text: ''
				},
				xAxis: {
					categories: resultStr[0].categories
				},
				yAxis: {
					min: 0,
					title: {
						text: ''
					}
				},
				legend: {
					layout: 'vertical',
					backgroundColor: '#FFFFFF',
					align: 'left',
					verticalAlign: 'top',
					x: 100,
					y: 70,
					floating: true,
					shadow: true
				},
				tooltip: {
					formatter: function() {
						return ''+
							this.x +': '+ this.y +'';
					}
				},
				plotOptions: {
					column: {
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
				series: []
			}
			
			options.series = [];
			for(var i=0; i<dataList.length; i++){//构造柱形图的数据
	        	var tmpArray = new Array();
	        	var tmpData = dataList[i].data.toString().split(",");
	            options.series[i] = {   
                  	name: dataList[i].name,  
                  	data: []  
               	};  
               	
               	for(var j=0; j<tmpData.length; j++){
               		tmpArray[j] = parseFloat(tmpData[j]);
               	}
               	options.series[i].data = tmpArray;
	        }
			
			
	        return options;
     	}
		
		
	</script>
	<style type="text/css">
		.searchtitle {
			text-align: right;
		}
		
		.ui-jqgrid tr.jqgrow td {
			white-space: normal !important;
			height: auto;
			vertical-align: text-top;
			padding-top: 2px;
		}
		
		.centerDiv{
			padding: 10px; 
			padding-top: 3px; 
			border: 0px;
		}
	</style>
	</head>
	<body class="easyui-layout">
		<!-- TOP区 -->
		<div region="north" border="false" style="padding: 10px;">
			<s:form name="editForm" id="editForm"
				action="ireport_rt_doPIESearch.action" theme="simple">
				<s:property value="searchArea" escapeHtml="false" />
				<s:hidden name="conditionStr" id="conditionStr"></s:hidden>
				<s:hidden name="reportId" id="reportId"></s:hidden>
			</s:form>
		</div>
		<div region="center" class="centerDiv">
			<div id="container" style="min-width:260px;border:1px solid #ccc;background:#FFF;height: 400px; margin: 0 auto"></div>
		</div>
	</body>
</html>
