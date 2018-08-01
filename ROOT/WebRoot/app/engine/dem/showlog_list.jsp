
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link href="iwork_skins/_def1/css/base.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.messager.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(function(){
			$('a.basic').cluetip({
				cluetipClass: 'rounded', 
				dropShadow: false, 
				positionBy: 'mouse',
				showTitle: false,
				width:580,
				cursor:''
			});
	});
		function showDialog(logid){
			var pageUrl = "sysForm_showlogItem.action?dataid="+logid;
			 art.dialog.open(pageUrl,{
			    	id:'showlogDialog',  
			    	title:"操作日志",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:700,
				    height:410
				 });
		}
		function dellog(logid){
		 	art.dialog.confirm('你确定要删除当前日志吗？', function(){
				var pageUrl = "sysForm_deleteLogItem.action";
				 $.post(pageUrl,{dataid:logid},function(data){
			         if(data=='success'){
			             window.location.reload();
			         }
		   		 });
	     });
		}
	</script>
	<style type="text/css">
		.time{
			padding:2px;
			padding-left:30px;
			color:#999;
			font-size:10px;
			width:80px;
			border-bottom:1px dotted #efefef;
		}
		.username{
			padding:5px;
			padding-left:10px;
			color:#666;
			font-size:10px;
			border-bottom:1px dotted #efefef;
		}
		.action{
			padding:2px;
			padding-left:10px;
			color:#666;
			font-size:10px;
			width:15px;
			border-bottom:1px dotted #efefef;
		}
		.opra{
			padding:2px;
			padding-left:10px;
			color:#666;
			font-size:10px;
			width:30px;
			border-bottom:1px dotted #efefef;
		}
		.title{
			height:30px;
			font-size:16px;color:#333;font-family:黑体;
			border-bottom:1px solid #999;
			padding-left:10px;
		}
		.searchArea{
			text-align:right;
			font-size:12px;color:#333;font-family:宋体;
			border-bottom:1px solid #999;
			vertical-align:bottom;
			padding-bottom:5px;
			padding-right:10px;
		}
		.Itemtitle{
			height:25px;
			line-height:25px;
			font-size:12px;
			border-bottom:1px solid #efefef;
			background:#f5f5f5;
			padding-left:5px;
			border-right:1px solid #efefef;
		}
		.itemdata{
			height:25px;
			line-height:25px;
			font-size:12px; 
			border-bottom:1px solid #efefef;
			padding-left:5px;
			color:#0000FF;
			border-right:1px solid #efefef;
		}
		#contentTable{
			width:98%;
		}
		#contentTable td{
			padding-left:10px;
			padding-right:5px;
			border-bottom:1px dotted #efefef;
		}
		#contentTable tr{
			line-height:30px;
			border-bottom:1px dotted #efefef;
		}
		#contentTable tr:hover{
			background-color:#fffff5;
			color:#0000FF; 
			CURSOR: pointer;
		}
	</style>
</head> 
<body class="easyui-layout" >  
<div region="north" border="false" split="false" style="height:40px">
<table width="100%">
	<tr>
		<td  class="title">	<img src="iwork_img/search_btn.png" border="0">数据维护变更日志</td>
		<td class="searchArea" style="display:none"><input type="text" name="startDate">&nbsp;至&nbsp;<input type="text" name="endDate">&nbsp;<a id="btnEp" class="easyui-linkbutton" icon="icon-search" href="javascript:doSearch();" >查询</a></td>
	</tr>
</table>
</div>
<div id="content" region="center" border="false" >
<s:form name="editForm" id="editForm">
	<s:property value="loadLogHTML" escapeHtml="false"/>
	<s:if test="pager.getTotalRows()!=0">
	<table width="90%" style="margin-top:5px;margin-bottom:10px;">
	<tr align="right">
    	<td>
    		共<s:property value="pager.getTotalRows()"/>行&nbsp;
    		第<s:property value="pager.getCurrentPage()"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<s:if test="pager.getCurrentPage()!=1">
    		<a href="<s:url value="sysForm_showlog.action">
    			<s:param name="demId"  value="demId"/>
    			<s:param name="modelId" value="modelId"/> 
    			<s:param name="currentPage" value="curPage"/>
    			<s:param name="pagerMethod" value="'first'"/>
    		</s:url>">首页</a> 
    		<a href="<s:url value="sysForm_showlog.action">
    		<s:param name="demId"  value="demId"/>
    		<s:param name="modelId" value="modelId"/>
    			<s:param name="curPage" value="pager.getCurrentPage()"/>
    			<s:param name="pagerMethod" value="'previous'"/>
    		</s:url>">上一页</a>
    		</s:if>
    		<s:if test="pager.getCurrentPage()!=pager.getTotalPages()">
	    		<a href="<s:url value="sysForm_showlog.action">
	    			<s:param name="demId"  value="demId"/>
	    			<s:param name="modelId" value="modelId"/>
	    			<s:param name="curPage" value="pager.getCurrentPage()"/>
	    			<s:param name="pagerMethod" value="'next'"/>
	    		</s:url>">下一页</a>
    		<a href="<s:url value="sysForm_showlog.action">
    			<s:param name="demId"  value="demId"/>
    			<s:param name="modelId" value="modelId"/>
    			<s:param name="curPage" value="pager.getCurrentPage()"/>
    			<s:param name="pagerMethod" value="'last'"/>
    		</s:url>">尾页</a>
    		</s:if>
    		
    	</td>
    </tr>	
    </table>
			</s:if>
    		<s:else>
    			<div style="font-size:16px;color:#999;text-align:center;padding-top:30px;">未发现变更记录</div>
    		</s:else>
			<s:hidden name="startNum"/>
			<s:hidden name="modelId"/>
			</s:form>
</div>
</body>
</html>
