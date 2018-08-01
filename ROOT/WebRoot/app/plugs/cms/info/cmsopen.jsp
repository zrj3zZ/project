﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title><s:property value='model.title' escapeHtml='false'/></title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/plugs/cmsopen.css" rel="stylesheet" type="text/css" />
<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script> 
<script type="text/javascript">
	$(document).ready(function(){
		//滚动条事件  
         $(window).scroll(function () {
			if ($(this).scrollTop() > 150) {
				$('#back-top').fadeIn();
			} else {
				$('#back-top').fadeOut();
			}
		});
		$('#back-top a').click(function () {
			$('body,html').animate({
				scrollTop: 0
			}, 200);
			return false;
		});

		
		changeNoName();
		
		var istalk = "<s:property value='model.istalk' escapeHtml='false'/>";
		if(istalk == "1"){
			$("#userTalk").css("display","none");
			$("#userOldTalks").css("display","none");
			$("#pageCount").css("display","none");
		}
	});
	
	function commentSubmit(){
		var infoid = "<s:property value='infoid' escapeHtml='false'/>";
		var talkName = $("#userName").html();
		var talkContent = $("#cmsComment").val();
		if($.trim(talkContent).length > 0){
			if($.trim(talkContent).length <= 500){
				$.post("addNewCmsComment.action",
				{contentId:infoid, talkName:talkName, talkContent:talkContent},
				function(data){
					window.location.reload();
				});
			}else{
				alert("评论内容500字以内");
			}
			
		}else{
			alert("请输入评论内容");
		}
	}
	function changeNoName(){
		var userName = "<s:property value='userName' escapeHtml='false'/>";
		var noNome = $("#noNome").is(':checked');
		if(noNome){
			$("#userName").html("匿名");
		}else{
			$("#userName").html(userName);
		}
	}
	
	function delCommmet(cId){
	    if(confirm('确定删除当前评论信息吗?')){
	    	$.post("delCmsComment.action",{commentId:cId},function(data){
				window.location.reload();
			});
	    }
	}
	function setNopic(obj){
		$(obj).attr("src","iwork_img/nopic1.gif");
	}
	
</script>
</head>
<body class="easyui-layout" >
<div  region="north" style="height:55px;padding:0px;overflow:hidden;border:0px;border-bottom:1px solid #efefef;background-color:#fff">
<div class="topbanner">
<div class="cc"> 
	<span class="page_title" style="font-family:黑体">业务流程平台</span>
</div>
</div>
</div>
<div  region="center" style="border:0px">
<p id="back-top"> <a href="#top"><span></span></a> </p>
	<div  class="cmsbody">
	<table  border="0" width="100%" align="center" cellpadding="0" cellspacing="0">		
      <tr>
        <td height="53" valign="bottom" class="news_title" style="font-family:微软雅黑"><s:property value='model.title' escapeHtml='false'/></td>
      </tr>
      <tr> 
        <td height="27" class="subtitle">发布日期: <s:property value='cmsdate' escapeHtml='false'/> 　发布人: <s:property value='model.releaseman' escapeHtml='false'/> 　 来源: <s:property value='model.source' escapeHtml='false'/></td>
      </tr>
      <s:if test="model.prepicture!=null&&model.prepicture!=\"\""> 
	    <tr>
        <td height="42" >
			<div align="center"><img id="cmsFileUploadUrl" onerror="setNopic(this)" src="<s:property value='model.prepicture' escapeHtml='false'/>" class="news_img"></img></div>
		</td>
      </tr>
      </s:if>
      <tr>
        <td  valign="top">
			<s:property value='precontent' escapeHtml='false'/>
		</td>
      </tr>  
      <tr>
        <td valign="top" >
			<s:property value='content' escapeHtml='false'/>
		</td>
      </tr>

	    <tr>
        <td valign="top">
			<s:property value='archives' escapeHtml='false'/>
		</td>
      </tr>
      <tr>
        <td valign="top"  class="border">
			&nbsp;
		</td>
      </tr>
		   
	  <tr>
	  	<td height="40" align="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td align="left" class="keyword">关键词：<s:property value='model.keyword' escapeHtml='false'/></td>
		<td align="right">【<a href="javascript:window.print()">打印</a>】【<a href="javascript:window.close()">关闭</a>】&nbsp;</td>
	  </tr>
	</table>
			<table width="95%" align="center" cellspacing="0" cellpadding="0" >
				<tbody>
				<s:if test="commentHtml!=\"\"">
					<tr class="nodragTR">
						<td>讨论区&nbsp;</td>
					</tr>
					</s:if>
					<tr bgcolor="#FFFFFF">
						<td>
							<table width="100%" align="center" cellspacing="0" cellpadding="0" bordercolor="#CCD7DF" border="0" style="border-collapse:collapse">
								<tbody>
									<s:property value='commentHtml' escapeHtml='false'/>
								</tbody>
							</table> 
						</td>
					</tr>
					<tr id="pageCount">
						<td style="padding-top: 10px; padding-bottom: 10px;">
							<table width="100%" align="center" cellspacing="0" cellpadding="3" >
								<tbody>
									<tr>
										<td  align="right"><s:property value='pagePagingHtml' escapeHtml='false'/></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				<tr id="userTalk">
			  	<td>
					<table width="100%" height="73" align="center" cellspacing="0" cellpadding="3" bordercolor="#CCD7DF" border="0" style="border-collapse:collapse">
						<tbody>
							<tr class="nodragTR">
								<td><b>我来说两句</b>（请控制在500字以内）</td>          
							</tr>
							<tr>            
								<td>
									<span id="userName"> <s:property value='userName' escapeHtml='false'/> </span> &nbsp;&nbsp;&nbsp;<input type="checkbox" id="noNome" onchange="changeNoName()"/>&nbsp;匿名
								</td>
							</tr>
							<tr>            
								<td>
									<textarea id="cmsComment"  style="width:99%;height:200px;"></textarea>
								</td>
							</tr>          
							<tr>            
								
								<td>
									<input type="button" value="提交" onclick="commentSubmit()" /> 
								</td>          
							</tr>        
						</tbody>
					</table>
				</td>
			</tr>
				</tbody>
			</table>
<input type='hidden' id='infoid' name='infoid' value='<s:property value='model.id' escapeHtml='false'/>'>
<input type='hidden' id='status' name='status'>
<input type='hidden' id='type' name='type'>
<iframe name='hidden_frame' id="hidden_frame" width= "0"  height= "0" style="VISIBILITY: hidden"></iframe>
</div>
</body>
</html>