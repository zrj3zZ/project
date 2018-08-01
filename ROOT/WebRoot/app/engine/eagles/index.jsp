<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/eagles_searcher.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
function changeTab(obj,tabkey){
		var className = obj.className;
		$('#searchType').val(tabkey);
		if(className==''){
			obj.className = "selected";
		}
		$('ul li').each(function(){
						if(tabkey != this.id){
							$(this).attr("class",""); 	
						}
		});	
	}
	function doSearch(){
		var searchTxt = $('#searcherTxt').val();
		if(searchTxt==null||searchTxt==""){
			//alert("请输入您要查询的内容");
			art.dialog.tips('请输入您要查询的内容...',2,'tips.gif'); 
			return false;
		}  
			art.dialog.tips('正在检索,请稍候...',100,'loading.gif'); 
			$('form').attr('action','eaglesSearch_Do.action');
			$("#editForm").submit();
	}
	//查询回车事件
	    function enterKey(){
			if (window.event.keyCode==13){
				 doSearch();
				return false;
			}
		} 
</script>
</head> 
<body  class="easyui-layout" >
<div region="north" border="false" split="false" >
<div class="tools_nav" style="text-align:right;padding-right:10px">
<img src="iwork_img/icon/eyeicon.gif" alt="鹰眼检索" />索引管理
</div>
</div>
<div region="center" border="false" style="border-left:1px solid #efefef">
<!-- TOP区 --> 
<s:form name="editForm" id="editForm">
	<table width="100%">
	<tr><td style="text-align:center; ">
				
					<div class="search">
					<div class="search_logo"></div>
					<div class="search_tab" > 
					  <ul id="search_tab">
					 	 <li id="all" class="selected"  onClick="changeTab(this,'all');" ><a href="#">全部</a></li>
						  	<s:iterator  value="typeList" status="status">
						  		 <li id='<s:property value="esType"/>' onclick="changeTab(this,'<s:property value="esType"/>');"><a href="#"><s:property value="title"/></a></li>
						  	</s:iterator>
					  </ul> 
					</div>  
					<div class="search_box">
					  <input  id="searcherTxt"  name="searcherTxt"  type="text" class="search_input"  maxlength="70" onKeyDown="enterKey();" />
					  <input name="" onclick="doSearch();" type="button" class="search_button"/>
					  <input type="hidden" id="searchType" name="searchType" value="all"/>
					</div>
					</div>
				
				</td></tr>
	</table>
	</s:form> 
	</div>
</body>
</html>
