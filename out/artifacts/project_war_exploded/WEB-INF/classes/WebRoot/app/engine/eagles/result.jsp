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
	function changeTab(tabkey){
			$("#searchType").val(tabkey);
			//如果检索内容不为空，则直接查询
				doSearch();
	}
	
	function doSearch(){
		var searchTxt = $('#searcherTxt').val();
		if(searchTxt==null){
			art.dialog.tips("请填写您要查询的内容",2);
			return false;
		}
			$('form').attr('action','eaglesSearch_Do.action');
			$("#editForm").submit();
	}
	 function enterKey(){
				if (window.event.keyCode==13){
					doSearch();
					return;
				}
	} 
</script>
</head> 
<body class="easyui-layout">
<div region="north" style="text-align:left;border:0px">
<s:form name="editForm" id="editForm">
	<div class="container">
				<div class="mini_search_box">
					 <s:textfield  id = "searcherTxt" name="searcherTxt" theme="simple" onkeydown="enterKey();"  cssClass="mini_search_input"></s:textfield><input type="button" onclick='doSearch()' title='Ctrl+Enter'  class="mini_search_button" name="searchBtn" value="搜索一下"/>
				</div>
			<div class="searchAreaTab">
					<s:if test="searchType=='ALL'||searchType=='all'">
					    <a href="#" onclick="changeTab('ALL')" class="unSelectTab"  >全部</a>
			      	</s:if> 
			      	<s:else> 
			      		<a href="#" onclick="changeTab('ALL')" class="selectTab"  >全部</a>
			      	</s:else>
			      		<s:iterator  value="typeList" status="status">
				      	<s:if test="esType==searchType">
				      		<a href="javascript:changeTab('<s:property value="esType"/>');"  id='<s:property value="esType"/>'   class="unSelectTab" ><s:property value="title"/></a>
				      	</s:if>
				      	<s:else>
				      		<a href="javascript:changeTab('<s:property value="esType"/>');" id='<s:property value="esType"/>'  class="selectTab" ><s:property value="title"/></a>
				      	</s:else>
			  	</s:iterator>
			</div>
			<s:hidden name="searchType" id="searchType"></s:hidden>
	</div>
			</s:form> 
</div>
	<div region="center" style="text-align:left;border:0px">
					<s:if test="resultList==null||resultList.size<=0">
						<div class="nofind">
						<img alt="查询信息未找到"  border="0" src="iwork_img/nondynamic.gif">未找到与“<s:property value="searcherTxt"/>”相关的内容!
						</div>
					</s:if> 
							<s:iterator  value="resultList" status="status">
								<div  class="result_Item">
								<div class="result_title">[<s:property value="type"  escapeHtml="false"/>]<s:property value="title"  escapeHtml="false"/></div>
					 			 <div class="result_article"><s:property value="content" escapeHtml="false"/>...</div>
					 			 </div>
						  	</s:iterator> 
		</div>
				
</body>
</html>
