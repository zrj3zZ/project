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
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pieRenderer.min.js"></script>
	<script language="javascript" type="text/javascript" src="iwork_js/jqueryjs/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script type="text/javascript" src="iwork_js/analytics_index.js"></script>
  		<style type="text/css"> 
  			#chart2 .jqplot-point-label {
			  border: 1.5px solid #aaaaaa;
			  padding: 1px 3px;
			  background-color: #eeccdd;
			}
			.title{
				font-size:30px;
				text-align:center;
			}
			.grid{
				width:90%;
				margin-left:auto;
				margin-right:auto;
				border:1px solid #CCCCCC;
			}
			.grid th{
				height:22px;
				background-color:#efefef;
				padding:5px;
			}
			.grid td{
				height:22px;
				padding:5px;
				border-bottom:1px solid #efefef;
			}
  		.ownername{
				font-size:40px;
				margin-left:auto;
				margin-right:auto;
				width:90%;
			}
			.ownername span{
				font-size:14px;
			}
  		</style>
	</style>	
  </head>
    <body class="easyui-layout">
    <div region="north" border="false" split="false" >
      <div class="title" width="100%">项目组成员绩效统计</div>
      <div class="ownername" width="100%"><span style="color:red;">姓名：<s:property value="username"/></span></div>
      </div>
      <div split="false" border="false" region="center">
      	<table width="100%" class="grid">
      	<tr align="center">
				<th>序号</th>
				<!--  <th >姓名</th>-->
				<th align="left">项目名称</th> 
				<!-- <th >完成百分比</th> -->
				<th align="right">合同金额</th>
				<th align="right">实收金额</th>
				<th >项目开始时间</th>
				<th >项目截止时间</th>
      		</tr>
      	<s:iterator value="runList"  status="status"> 
      		<tr align="center">
				<td ><s:property value="#status.count"/></td>
				<%--<td ><s:property value="MANAGER"/></td> --%>
				<td align="left"><s:property value="PROJECTNAME"/></td> 
				<%-- <td ><s:property value="SCALE"/></td> --%>
				<td align="right"><s:property value="HTJE"/></td>
				<td align="right"><s:property value="SSJE"/></td>
				<td ><s:property value="STARTDATE"/></td>
				<td ><s:property value="ENDDATE"/></td>
      		</tr> 
      	</s:iterator>	
      	</table>
		</div>
  </body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>		