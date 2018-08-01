<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
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
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pieRenderer.min.js"></script>
	<script language="javascript" type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
				
				/*   var data = <s:property value="typePieData" escapeHtml="false"/>;
					  var plot1 = jQuery.jqplot ('chart1', [data], 
					    { 
					    animate: true,
					    title:"项目类型统计",
					   animateReplot: true,
					   cursor: {  
						        style: 'help',     //当鼠标移动到图片上时，鼠标的显示样式，该属性值为css类  
						        show: true,              //是否显示光标  
						        showTooltip: true,      // 是否显示提示信息栏  
						    },  
					      seriesDefaults: {
					        // Make this a pie chart.
					        renderer: jQuery.jqplot.PieRenderer, 
					        rendererOptions: {
					          showDataLabels: true
					        }
					      }, 
					      legend: { show:true, location: 'e' },
					      
					    }
					  );
					  */
			 //var line1 =	[1,1]	  
			var line1 = <s:property value="typeBarData" escapeHtml="false"/>;
			var plot2 = jQuery.jqplot ('chart2', [line1], 
				    {
					animate: true,
				    title:"按项目阶段统计",
				  	animateReplot: true,
					seriesDefaults: {
						pointLabels: { show: true },
	                    shadow: false,
	                     showMarker: false, // 是否强调显示图中的数据节点
	                     renderer: $.jqplot.BarRenderer,
	                     rendererOptions: {
	                         barWidth: 30,
	                         barMargin: 30
	                    },
	                    
		    },
		   legend: {
		        show: true,
		        location: 'ne',  
		        placement: 'outsideGrid',
		        showLabels:true,
		        labels:['项目阶段统计'],
		          marginRight: '40px'
		    },
			axes: {
				xaxis:{
					ticks:<s:property value="typeBarLabel" escapeHtml="false"/>,
					renderer:$.jqplot.CategoryAxisRenderer},
				yaxis:{
					padMax:1.1,min : 0,tickInterval : '5'}
					}
			});
			  <s:if test="superman">
			   var data3 = <s:property value="typePieData2" escapeHtml="false"/>;
				var arrLabels =  $.makeArray($(data3).map(function(){return this[0]+":"+this[1]}));
			  var plot3 = jQuery.jqplot ('chart3', [<s:property value="typePieData2" escapeHtml="false"/>], 
					    { 
					    animate: true,
					    title:"项目完成率统计",
					  	animateReplot: true,
					      seriesDefaults: {
					        // Make this a pie chart.
					        renderer: jQuery.jqplot.PieRenderer, 
					        rendererOptions: {
					        dataLabels:arrLabels,
					          diameter: undefined, // 设置饼的直径    
				                padding: 20, // 饼距离其分类名称框或者图表边框的距离，变相该表饼的直径    
				                sliceMargin: 5, // 饼的每个部分之间的距离    
				                showDataLabels: true,
				                fill:true, // 设置饼的每部分被填充的状态    
				                shadow:true, //为饼的每个部分的边框设置阴影，以突出其立体效果    
				                shadowOffset: 2, //设置阴影区域偏移出饼的每部分边框的距离    
				                shadowDepth: 5, // 设置阴影区域的深度    
				                shadowAlpha: 0.07 // 设置阴影区域的透明度   
					        }
					      },   
					      legend: { show:true, location: 'e' }
					    }
					  );
		//	  var plot4 = jQuery.jqplot ('chart4', [<s:property value="typePieData3" escapeHtml="false"/>], 
		//			    { 
		//			    animate: true,
		//			    title:"项目负责人项目统计信息",
		//			   animateReplot: true,
			//		      seriesDefaults: {
					        // Make this a pie chart.
			//		        renderer: jQuery.jqplot.PieRenderer, 
				//	        rendererOptions: {
					//		    dataLabelsFormatString:'%s',
					  //        	diameter: undefined, // 设置饼的直径    
				        //        padding: 20, // 饼距离其分类名称框或者图表边框的距离，变相该表饼的直径    
				          //      sliceMargin: 5, // 饼的每个部分之间的距离    
				            //    showDataLabels: true,
				             //   fill:true, // 设置饼的每部分被填充的状态    
				               // shadow:true, //为饼的每个部分的边框设置阴影，以突出其立体效果    
				               // shadowOffset: 2, //设置阴影区域偏移出饼的每部分边框的距离    
				               // shadowDepth: 5, // 设置阴影区域的深度    
				               // shadowAlpha: 0.07 // 设置阴影区域的透明度   
					        //}
					     // },   
					  //    legend: { show:true, location: 'e' }
					  //  }
					//  );
			
			</s:if>		
				var data5 = <s:property value="typePieData4" escapeHtml="false"/>;
				var arrLabels =  $.makeArray($(data5).map(function(){return this[0]+":"+this[1]}));
			  var plot5 = jQuery.jqplot ('chart5', [data5], 
					    { 
					    animate: true,
					    title:"项目风险分数统计信息",
					   animateReplot: true,
					      seriesDefaults: {
					        // Make this a pie chart.
					        renderer: jQuery.jqplot.PieRenderer, 
					        rendererOptions: {
					    	    dataLabels:arrLabels,
							    dataLabelsFormatString:'%s',
					           diameter: undefined, // 设置饼的直径    
				                padding: 20, // 饼距离其分类名称框或者图表边框的距离，变相该表饼的直径    
				                sliceMargin: 5, // 饼的每个部分之间的距离    
				                showDataLabels: true,
				                fill:true, // 设置饼的每部分被填充的状态    
				                shadow:true, //为饼的每个部分的边框设置阴影，以突出其立体效果    
				                shadowOffset: 2, //设置阴影区域偏移出饼的每部分边框的距离    
				                shadowDepth: 5, // 设置阴影区域的深度    
				                shadowAlpha: 0.07 // 设置阴影区域的透明度   
					        }
					      },   
					      legend: { show:true, location: 'e' }
					    }
					  );
			$("#chart1").bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {  
						showProjectList("TYPENAME",data);
					 });  
			$("#chart2").bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {  
						var labels = <s:property value="typeBarLabel" escapeHtml="false"/>;
						var type = labels[pointIndex];
						showProjectList("XMJD",type);
					 });  
			$("#chart3").bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {  
						showProjectList("STATUS",data);
					 });  
		//	$("#chart4").bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {  
			//			showProjectList("OWNER",data);
			//		 });  
			$("#chart5").bind('jqplotDataClick', function (ev, seriesIndex, pointIndex, data) {  
						showProjectList("SCORE",data);
					 });  
		});
		
		function showProjectList(type, searchkey){
			var pageurl = encodeURI("zqb_project_search.action?type="+type+"&searchkey="+searchkey); 
			parent.openWin("项目列表",500,800,pageurl,this.location,"charinsertDlg");
		}
		
		function openWin(title,height,width,pageurl,location,dialogId){
		if(dialogId=='undefined')dialogId="mainDialogWin"; 
		$.dialog({ 
			   id:dialogId, 
				cover:true, 
				title:title,  
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				width:width,
				cache:false, 
				lock: false,
				esc: true,
				height:height,   
				iconTitle:false,  
				extendDrag:true,
				autoSize:true,
				content:pageurl
				
			});
	}
		
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
      	<table width="100%">
      		<tr>
      			<td style="padding:10px;"><div id="chart5" ></div></td>
				<td style="padding:10px;"><div id="chart2" ></div></td>
      		</tr>
      		<s:if test="superman">
      		
      		<tr>
      			<td><div id="chart3" ></div></td>
      			<!--<td><div id="chart4" ></div></td>  -->
      		</tr>
      		
      		</s:if>
      	</table>
		</div>
  </body>
</html>