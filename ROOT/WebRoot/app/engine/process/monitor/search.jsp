<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link href="iwork_css/eagles_searcher.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
	function doSearch(){
		var searchTxt = $('#searchkey').val();
		if(searchTxt==null){
			return false;
		}
			$('form').attr('target','resultFrame');
			$('form').attr('action','processInstanceManageSearch.action');
			$("#editForm").submit();
	}
	
	function selectSearchType(){
		var v = $('input[name="type"]:checked').val();
		if(v==1){
			$("#optionSearch").hide();
			$("#searchTitltStr").html("全文检索条件");
			
		}else if(v==0){
			$("#optionSearch").show();
			$("#searchTitltStr").html("流程标题");
		}
	}
</script>
<style type="text/css">
	.searchTitle{
		font-size:14px;
		font-family:微软雅黑;
		padding:2px;
		text-align:right:
	}
	.searchInput{
		font-size:16px;
		font-family:黑体;
		padding:2px;
	}
	.searchBtn{
		width:150px;
		font-size:16px;
		font-family:黑体;
		padding:2px;
	}
</style>
</head> 
<body class="easyui-layout">
	<div region="north" style="text-align:left;border:0px;border-bottom:1px solid #efefef;padding:10px;">
	<s:form id="editForm" name="editForm" theme="simple">
				<DIV><table width="100%"><TR><TD><h1>流程运行单据跟踪</h1> </TD><TD> <label><input  onClick="selectSearchType()" type=radio  id='type' checked="checked" name='type' value='0' >普通搜索</label>&nbsp;
<label><input type=radio  id='type' name='type' value='1' onClick="selectSearchType()" >全文检索</label></TD></TR></table></DIV>
				<div style="padding-top:10px;color:#333;padding-left:50px;">
				
				<div >
					<table>
						<tr id="optionSearch">
							<td class="searchTitle">流程发起人</td>
							<td><s:textfield name="owner" id="owner" cssStyle="width:150px;" cssClass="searchInput"/></td>
							<td  class="searchTitle">流程当前办理人</td>
							<td><s:textfield name="targetUser" id="targetUser" cssStyle="width:150px;" cssClass="searchInput"/></td>
						</tr>
						<tr>
							<td  class="searchTitle" id="searchTitltStr">流程标题:</td>
							<td colspan="2"><s:textfield name="searchkey" id="searchkey" cssStyle="width:450px;" cssClass="searchInput"/></td>
							<td> <input type="button" value="查询"  onclick="doSearch()" class="searchBtn"></td>
						</tr>
					</table>
				</div>
				</div>
				</s:form>
		</div>
	<div region="center" style="text-align:left;border:0px;padding:10px;">
				<iframe name="resultFrame"  id="resultFrame" iframe scrolling="auto" frameborder="0"  src="processInstanceManageSearch.action" style="width:100%;height:100%;"></iframe>
	</div>
				
</body>
</html>
