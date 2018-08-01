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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<SCRIPT type="text/javascript">
		<!--
		var setting1 = {
			async: {
						enable: true, 
						url:"processDeploy_showtree.action",
						dataType:"json" 
					},
			check: {
				enable: true,
				chkboxType: {"Y":"", "N":""}
			}, 
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				onCheck: onCheck,
				onAsyncSuccess: onAsyncSuccess
			}
		};
		var setting = {
			check: {
				enable: true,
				chkboxType: {"Y":"", "N":""}
			}, 
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				onCheck: onCheck
			}
		};
		
		function onAsyncSuccess(event, treeId, treeNode, msg){
	                     var treeObj = $.fn.zTree.getZTreeObj(treeId);
	                    alert(treeNode.type);
	             }
		function beforeClick(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}
		
		function onCheck(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getCheckedNodes(true),
			v = "";
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
		}

		function showMenu() {
			var cityObj = $("#citySel");
			var cityOffset = $("#citySel").offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityObj.outerHeight()-25 + "px"}).slideDown("fast");

			$("body").bind("mousedown", onBodyDown);
		}
		function showMenu1() {
			var cityObj = $("#analyticsType");
			var cityOffset = $("#analyticsType").offset();
			$("#menuContent1").css({left:cityOffset.left + "px", top:cityObj.outerHeight()-25 + "px"}).slideDown("fast"); 

			$("body").bind("mousedown", onBodyDown);
		}
		
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("#menuContent1").fadeOut("fast");
			$("#menuContent2").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		
		function onBodyDown(event) {
			if (!(event.target.id == "citySel" || event.target.id == "orgType" || event.target.id == "analyticsType" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0|| event.target.id == "menuContent1" || $(event.target).parents("#menuContent1").length>0|| event.target.id == "menuContent2" || $(event.target).parents("#menuContent2").length>0)) {
				hideMenu();
			}
		}
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting1); 
				  var plot4 = $.jqplot('chart1', <s:property value="data2" escapeHtml="false"/>, {
				   animate: true,
					   animateReplot: true,
				      stackSeries: true, 
				       axesDefaults: {
					        tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
					        tickOptions: {
					          angle: 20
					        }
					    },
					    
				      seriesDefaults: {
				          renderer: $.jqplot.BarRenderer,
				          rendererOptions:{barMargin: 25}, 
				          pointLabels:{show:true, stackedValue: true}
				      },
				      axes: {
				          xaxis:{renderer:$.jqplot.CategoryAxisRenderer}
				      }
				  });
    // from their vertical bar counterpart.
					  var plot2 = $.jqplot('chart2',<s:property value="data" escapeHtml="false"/>, {
					  animate: true,
					   animateReplot: true,
					    series:[{renderer:$.jqplot.BarRenderer}, {xaxis:'x2axis', yaxis:'y2axis'}],
					    axesDefaults: {
					        tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
					        tickOptions: {
					         showGridLine: false,
					        fontSize:'6px', 
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
			var pageUrl = "process_analytics_process_chart.action?actDefId="+actDefid;	
			art.dialog.open(pageUrl,{ 
					id:'analytics_process_chart',
					cover:true,
					title:title,
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1000,
					cache:false,
					lock: true,
					height:600, 
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
		.chartTitle{
			padding:5px;
			font-size:22px; 
			font-family:黑体;
			font-weight:bold;
			text-align:center;
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
			border:1px solid #ccc;
			border-top:0px;;
			padding-bottom:30px;
			width:1000px;
			margin-left:auto;
			margin-right:auto;
			padding-top:5px;
			background-color:#fff; 
			
		}
		.Tb_analytics{
			border:1px solid #efefef;
			width:900px;
			margin-left:50px;
		}
		.Tb_analytics td{
			height:25px;
			text-align:center;
			padding-left:5px;
		}
	</style>    
</head>
<body  class="easyui-layout" >
	<div region="north" border="false" style="height:76px;overflow-y:hidden" split="false"  id="layoutNorth">
		<div class="home_navigation_bg">
  <div class="home_navigation">
    <div class="home_navigation_logo"><img style="height:35px" src="iwork_img/analytics/analytics.png"></div>
    <div class="home_navigation_tab"> 
    	<s:property value="toolbar" escapeHtml="false"/>
    </div>
    <div class="home_navigation_icon">
      <ul id="jsddm">
        <li><a href="#">操   作</a>
          <ul>
            <li><a href="#">创建面板</a></li>
            <li><a href="#">帮助</a></li>
            <li><a href="#">论坛</a></li>
            <li><a href="#">反馈</a></li>
            <li><a href="#">退出</a></li>
          </ul>
        </li> 
      </ul>
      <span class="icon_red_point" onclick="$('#sysQtip').show();return false;"></span> </div>
   
    <div class="gn_tips" style="top: 43px;	left: 860px; display:none" id="sysQtip"> <a href="#" class="W_ico12 icon_close" onclick="$('#sysQtip').hide();return false;"></a>
      <ul class="tips_list">
        <li id="dblc">待办流程，<a target="_top" href="#"><span id="tiao1">2</span>条</a></li>
        <li id="wdxx">未读消息，<a target="_top" href="#"><span id="tiao2">3</span>条</a></li>
      </ul>
    </div>
  </div>
</div> 
<div class="mainToolbar">
	<table width="100%" >
		<tr>
			<td style="padding-left:15px"> 
			<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择流程</a>&nbsp;<input id="citySel" type="text" readonly value="" style="width:220px;height:20px;" onclick="showMenu();" />
			时间区间&nbsp;<input id="start" type="text"  value="" style="width:120px;height:20px;"/>至<input id="end" type="text"  value="" style="width:120px;height:20px;"/>&nbsp;<input type="button" value="查询" /></td>
			<td>保存查询方案</td>
		</tr>
	</table>
</div> 
	</div>
<div region="center" border="false" style="border-left:1px solid #efefef;background-color:#ccc" id="layoutCenter" >
	<div class="process_header">
  <div class="process_head_tab_box"> 
 	 <a class="process_head_tab_active"  href="process_analytics_process.action?type=process"><img src="iwork_img/km/chart.gif">流程实例概览</a>
     <a class="process_head_tab"    href="process_analytics_process_time.action?type=process"><img src="iwork_img/km/chart.gif">办理时长分布</a>
     <a class="process_head_tab"  href="process_analytics_process_top.action?type=process">TOP10</a>
   <!--    <a class="process_head_tab"  title="显示当前用户已办理，所有办理过的任务历史及审批意见"  href="process_desk_history.action"> 办理日志</a> -->
     <!--  <a class="process_head_tab"  title="显示所有当前用户发起的流程单据跟踪查询"  href="process_desk_createlog.action">我发起的流程</a> -->
</div>
</div>
<div class="content">
	<table width="100%">
	<tr>
			<td class="chartTitle">在办及归档比例柱图</td>
			<td class="chartTitle">流程办理时间分布图</td>
		</tr>
		<tr>
			<td style="padding:10px;"><div id="chart1" ></div></td>
			<td style="padding:10px;"><div id="chart2" ></div></td>
		</tr> 
		
	</table>
	
	<table width="100%" border="1"  class="Tb_analytics">
		<tr>
			<td class="mainToolbar">序号</td>
			<td class="mainToolbar">流程标题</td>
			<!-- <td class="mainToolbar">待办实例数</td> -->
			<td class="mainToolbar">在办流程数</td>
			<td class="mainToolbar">归档流程数</td>
			<td class="mainToolbar">平均办理时长</td>
			<td class="mainToolbar">最长办理时长</td>
			<td class="mainToolbar">最短办理时长</td>
		</tr>
		<s:property value="tableHtml" escapeHtml="false"/>
	</table>
<div id="menuContent" class="menuContent" style="display:none;border:1px solid #efefef;background-color:#ccc;overflow:auto;position: absolute;">
	<ul id="treeDemo" class="ztree" style="background-color:#FFFF99;margin-top:0; width:220px; height: 450px;"></ul>
</div>
</div>

</div>
</html>
	