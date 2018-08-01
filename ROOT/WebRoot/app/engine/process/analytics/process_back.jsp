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
	<link href="admin/css/dashboard.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/excanvas.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/jquery.jqplot.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pieRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.donutRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
	<SCRIPT type="text/javascript">
		<!--
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

		var zNodes =[
			{id:1, pId:0, name:"北京"},
			{id:2, pId:0, name:"天津"},
			{id:3, pId:0, name:"上海"},
			{id:6, pId:0, name:"重庆"},
			{id:4, pId:0, name:"河北省", open:true, nocheck:true},
			{id:41, pId:4, name:"石家庄"},
			{id:42, pId:4, name:"保定"},
			{id:43, pId:4, name:"邯郸"},
			{id:44, pId:4, name:"承德"},
			{id:5, pId:0, name:"广东省", open:true, nocheck:true},
			{id:51, pId:5, name:"广州"},
			{id:52, pId:5, name:"深圳"},
			{id:53, pId:5, name:"东莞"},
			{id:54, pId:5, name:"佛山"},
			{id:6, pId:0, name:"福建省", open:true, nocheck:true},
			{id:61, pId:6, name:"福州"},
			{id:62, pId:6, name:"厦门"},
			{id:63, pId:6, name:"泉州"},
			{id:64, pId:6, name:"三明"}
		 ];
		var zNodes1 =[
			{id:1, pId:0, name:"待办实例分布"},
			{id:2, pId:0, name:"流程平均办理时长"},
			{id:2, pId:0, name:"流程待阅待阅时长"},
			{id:3, pId:0, name:"流程归档"},
			{id:6, pId:0, name:"重庆"}
			
		 ];
		var zNodes2 =[
			{id:1, pId:0, name:"信息技术部"},
			{id:2, pId:0, name:"人力资源部"},
			{id:3, pId:0, name:"品牌公关部 "},
			{id:6, pId:0, name:"重庆"}
			
		 ];

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
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$("body").bind("mousedown", onBodyDown);
		}
		function showMenu1() {
			var cityObj = $("#analyticsType");
			var cityOffset = $("#analyticsType").offset();
			$("#menuContent1").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast"); 

			$("body").bind("mousedown", onBodyDown);
		}
		function showMenu2() {
			var cityObj = $("#orgType");
			var cityOffset = $("#orgType").offset();
			$("#menuContent2").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast"); 

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
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$.fn.zTree.init($("#typeTree"), setting, zNodes1);
			$.fn.zTree.init($("#orgTree"), setting, zNodes1);
    // For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
		    var plot2 = $.jqplot('chart2', [
		        [[12,'OA权限申请流程'], [4,2], [6,3], [3,4]], 
		        [[5,'OA权限申请流程'], [1,2], [3,3], [4,4]], 
		        [[4,'OA权限申请流程'], [7,2], [1,3], [2,4]]], {
		         series:[  
	            {label:''},  
	            {label:'访问量'},  
	            {label:'访问人数'},  
	            {renderer:$.jqplot.CanvasAxisTickRenderer}  
	            ], 
		        seriesDefaults: {
		        rendererOptions: {  
		            barPadding: 8,      //设置同一分类两个柱状条之间的距离（px）  
		            barMargin: 10,      //设置不同分类的两个柱状条之间的距离（px）（同一个横坐标表点上）  
		            barDirection: 'vertical', //设置柱状图显示的方向：垂直显示和水平显示  
		                                 //，默认垂直显示 vertical or horizontal.  
		            barWidth: null,     // 设置柱状图中每个柱状条的宽度  
		            shadowOffset: 2,    // 同grid相同属性设置  
		            shadowDepth: 5,     // 同grid相同属性设置  
		            shadowAlpha: 0.8,   // 同grid相同属性设置  
		        }, 
		         
		         label: '',
		            renderer:$.jqplot.BarRenderer,
		            pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
		            shadowAngle: 135,
		            rendererOptions: {
		                barDirection: 'horizontal'
		            }
		        },
		        title: {
			        text: '流程实例统计图表',  //设置当前图的标题
			        show: true,//设置当前图的标题是否显示
			    },
		        legend: {
		            show: true,
		             location: 'se',
		            placement: 'outsideGrid'
		        },
		        axes: {
		            yaxis: {
		                renderer: $.jqplot.CategoryAxisRenderer
		            }
		        }
		    });
		});
		
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
		.Tb_analytics td{
			height:25px;
			text-align:center;
		}
	</style>    
</head>
<body style="overflow-y:hidden">
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
    <div class="homepage_head_tab_right">
      <div id="search" class="search">
        <input class="txt" type="text">
        </input>
        <button class="search-submit" ></button>
        <div class="search_xiala">
          <ul class="current_father">
            <li class="current"><a href="#">查找员工</a></li>
            <li><a href="#">搜索流程</a></li>
          </ul>
        </div>
      </div>
    </div>
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
			<a id="menuBtn1" href="#" onclick="showMenu1(); return false;">指标选择</a>&nbsp;<input id="analyticsType" type="text" readonly value="" style="width:120px;height:20px;" onclick="showMenu1();" />
			<a id="menuBtn2" href="#" onclick="showMenu2(); return false;">组织选择</a>&nbsp;<input id="orgType" type="text" readonly value="" style="width:120px;height:20px;" onclick="showMenu2();" />
			
			时间区间&nbsp;<input id="start" type="text" readonly value="" style="width:120px;height:20px;"/>至<input id="end" type="text" readonly value="" style="width:120px;height:20px;"/>&nbsp;<input type="button" value="查询" /></td>
			<td>保存查询方案</td>
		</tr>
	</table>
</div>
<div class="home_body">
	<div style="padding:10px;">
 	<div id="chart2" style="width:500px"></div>
 	</div>
	<table width="100%" border="1" class="Tb_analytics">
		<tr>
			<td>序号</td>
			<td>流程标题</td>
			<td>待办实例数</td>
			<td>历史实例数</td>
			<td>归档数</td>
			<td>平均办理时长</td>
			<td>最长办理时长</td>
			<td>最短办理时长</td>
		</tr>
		<s:property value="tableHtml" escapeHtml="false"/>
	</table>
	
</div>
<div id="menuContent" class="menuContent" style="display:none;border:1px solid #efefef;background-color:#ccc;overflow:auto;position: absolute;">
	<ul id="treeDemo" class="ztree" style="background-color:#FFFF99;margin-top:0; width:220px; height: 450px;"></ul>
</div>
<div id="menuContent1" class="menuContent1" style="display:none;border:1px solid #efefef;background-color:#ccc;overflow:auto;position: absolute;">
	<ul id="typeTree" class="ztree" style="background-color:#FFFF99;margin-top:0; width:220px; height: 100px;"></ul>
</div>
<div id="menuContent2" class="menuContent2" style="display:none;border:1px solid #efefef;background-color:#ccc;overflow:auto;position: absolute;">
	<ul id="orgTree" class="ztree" style="background-color:#FFFF99;margin-top:0; width:220px; height: 100px;"></ul>
</div>
</body>
</html>
	