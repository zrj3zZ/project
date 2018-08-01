<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>小米SAP信息发布平台</title>
<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/portal.css">

<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
<script type="text/javascript" src="../iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
<script type="text/javascript" src="../iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.KinSlideshow-1.2.1.min.js"></script>
<link href="../iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="../iwork_css/base.css" rel="stylesheet" type="text/css" />
<link href="../iwork_css/eagle_search_box.css" rel="stylesheet" type="text/css">

<script type="text/javascript">

	$(document).ready(function(){

		$('#searchKeyWord').keydown(function(e){
			if(e.keyCode==13){
			  doSearch();
			}
		});
	
		$.post("getSearchTypeHtml.action",{},function(data){
			$("#tag_items").html(data);			
		});
	
	});


   function openCms(infoid){
	    var url = "cmsOpen.action?infoid="+infoid;
	    var newWin="newVikm"+infoid;
	    var win_width = window.screen.width-150;
	    var page = window.open('form/waiting.html',newWin,'width='+win_width+',height=650,top=50,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    page.location=url;
	}
	
	function doSearch(){
		var title = "首页搜索";
		var searcherTxt = $("#searchKeyWord").val();
		//var searchType = $('input:radio[name=searchType]:checked').val();
		var searchType = $('#searchType').val();
		if($.trim(searcherTxt).length == 0){
			alert("请输入相关的搜索内容");
			return;
		}
		var url = "eaglesSearch_Do.action?searchType=" + searchType + "&searcherTxt=" + searcherTxt;
		window.parent.addTab(title,url,''); 
	}
	
	function openMoreHtml(showId){
			var url = "getMoreCommonTypeHtmlStr.action?showId=" + showId + "&pageSize=50&pageNo=1";
	    var newWin="newVikm"+showId;
	    var win_width = window.screen.width-150;
	    var page = window.open('form/waiting.html',newWin,'width='+win_width+',height=650,top=50,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    page.location=url;
	}
	
	function drop_mouseover(pos){
	 try{window.clearTimeout(timer);}catch(e){}
	}
	function drop_mouseout(pos){
	 var posSel=document.getElementById(pos+"Sel").style.display;
	 if(posSel=="block"){
	  timer = setTimeout("drop_hide('"+pos+"')", 1000);
	 }
	}
	function drop_hide(pos){
	 document.getElementById(pos+"Sel").style.display="none";
	}
	function search_show(pos,searchType,href){
		document.getElementById(pos+"SearchType").value=searchType;
		document.getElementById(pos+"Sel").style.display="none";
		document.getElementById(pos+"Slected").innerHTML=href.innerHTML;
		document.getElementById(pos+'q').focus();
		var sE = document.getElementById("searchExtend");
		if(sE != undefined  &&  searchType == "bar"){
		 sE.style.display="block";
		}else if(sE != undefined){
		 sE.style.display="none";
		}
	 try{window.clearTimeout(timer);}catch(e){}
	 return false;
	}
	
</script>
<style>

	.lft_u_cnt {
		background: url("../iwork_img/user_img.jpg") no-repeat scroll 10px 5px #FFFFFF;
		height: 74px;
		padding: 4px 0 0 120px;
	}
	
	.lft_u_cnt span {
		display: block;
		float: left;
		height: 20px;
		margin-top: 4px;
		width: 85px;
	}

	.lft_item {
		padding-bottom: 3px;
	}
	.headTitle {
		font-size:22px;
		font-family:黑体;
		font-weight:blod;
		color:#ff9900;
		padding:5px;
		padding-left:10px;
		vertical-align:bottom;
	}
</style>
</head>
<body  style="margin:auto">
<div style="width:980px;margin:auto;text-align:center;padding:5px;">
<div>
<table>
	<tr>
		<td><img alt="" src="../iwork_img/login/default2/logo.png"></td>
		<td class="headTitle">信息发布平台</td>
	</tr>
</table>
</div>
    <!-- 内容区 -->
	<div id="context" style="text-align:left">
		<s:property value="channelContent" escapeHtml="false"/>
	</div>
	
<script type="text/javascript">
	$(document).ready(function(){
		$.post("getPicHtmlDivStr.action?key=FocusPic",function(data){
			$("#picHtmlDiv").html(data);
			$("#KinSlideshow").KinSlideshow();
			$("#KinSlideshow").css("padding-right",1);
			$("#KinSlideshow").css("height",205);
			$("#KinSlideshow").css("padding-top",1);
		});
	});
</script>
</div>
</body>
</html>
