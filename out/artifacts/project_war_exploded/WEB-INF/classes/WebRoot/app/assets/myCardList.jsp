<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>我的资产列表</title>
			<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
			<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
			<link rel="stylesheet" type="text/css"	href="iwork_themes/easyui/gray/easyui.css">
			<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
			<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.metadata.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.validate.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.form.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
			<script type="text/javascript"	src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>

	<script>							
	$(function(){
		$("#pp").pagination({
			    total:<s:property value="total"/>, 
			    pageNumber:<s:property value="pageNumber"/>,
			    pageSize:<s:property value="pageSize"/>,
			    onSelectPage:function(pageNumber, pageSize){
			    	var pageUrl="iwork_contract_query.action?pageSize="+pageSize+"&pageNumber="+pageNumber;
					window.location.href = pageUrl;
				}
			});
		
	  });
	  
	  $(function(){
		
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
	    });
	    trace();
	});
	  //开启流程
	  function openPage(id,actDefId){
			var url = 'iwork_gdzc_showPage.action?no='+id+"&actDefId="+actDefId;
			var target = "iform_"+id;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
} 	
	    
	</script>
							<style>
		.topHead{
			height:40px;
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.maintitle{
			font-size:25px;
			font-family:黑体;
			font-werght:bold;
			padding:8px;
			color:#304d79;
		}
		.maintitle span{
			font-size:26px;
			color:#304d79;
			padding:2px;
		}
		.maintitle span span{
			color:red;
		}
		.tablist{
			list-style-type:none;
			 padding:0px;
			 margin-left:5px;
			 margin-top:5px;
			  border-top:1px solid #ccc;
		}
		.tablist li{
			 line-height:30px;
			 border-bottom:1px solid #ccc;
			 border-right:1px solid #ccc;
			 border-left:1px solid #ccc;
			 padding-left:25px;
			 font-family:微软雅黑;
			  background:#f7f7f7;
			  color:#666;
			  font-weight:bold;
			  text-align:left;
		}
		.tablist li:hover{
			   background-color:#fff;
			   background-image:url(iwork_img/arrow.png);
			   background-repeat:no-repeat;
			   background-position:5px 10px;
			   color:#333;
			 cursor:pointer;
		}
		.tablist .current{
			  background:#fff;
			   color:#333;
			   background-image:url(iwork_img/zoom.png);
			   background-repeat:no-repeat;
			   background-position:5px 7px;
			   border-right:1px solid #fff;
		}
		.tablist .current:hover{
		 background-image:url(iwork_img/zoom.png);
		}
		.addItemBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:14px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addItemBtn span{
			background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			padding:5px;
			padding-left:40px;
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
		.mainTable{
			width:100%;
		}
		.infoTable{
		  width:100%;
		}
		.infoTable td{
			vertical-align:top;
			padding:3px;
			padding-left:20px;
			width:30xp;
			text-align:left;
			font-size:14px;
			font-family:微软雅黑;
			
		}
		.infoTable td span{
			color:#0000ff;
		}
		
		.groupTitle{
			background:#efefef;
			padding:5px;
			padding-left:25px;
			font-family:微软雅黑;
			font-size:14px;
			 background-image:url(iwork_img/orange_grid.png);
			   background-repeat:no-repeat;
			   background-position:5px 5px;
		}
	</style>
	</head>
	<body class="easyui-layout">
	 <div region="north" border="false"  style="overflow:hidden;" > 
		 <table width="100%" border="0" cellspacing="0" cellpadding="0"
					align="center">
					<tr height=50 class="tools_nav">
						<td align="left">
							<div class="maintitle">我的资产列表</div>
						</td>
					</tr>
				</table>
	</div>
	 <div region="center" border="false"> 
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<s:iterator value="list" id="missionStatus"> 
						<tr>
							<td>
								<table width=98% border=1 bordercolorlight=#CCCCCC	bordercolordark=#CCCCCC borderrules=rows cellspacing=5	cellpadding=0 align=center>
									<tr>
										<td>
											<table width=98% border=0 cellspacing=0 cellpadding=0 align=center>
												<tr>
													<td width=20% rowspan=2 valign=top align=middle>
															<img src=app/assets/img/nopicture.gif>
													</td>
													<td>  
													                     资产编号：<b><font color=gray><s:property value="NO"/></font></b><br>
															资产名称：<b><font color=gray><s:property value="NAME"/></font></b><br/>
															规格型号：<s:property value="SPECIFICATION"/><br/>
															参考配置：<s:property value="CONFIGURE"/><br/>
															管理状态：<s:property value="STATUS"/>
															<s:if test='STATUS=="已使用"'>
																					<tr>
																						<td align=right colspan=2>
																							【
																							<a href='' onclick="openPage('<s:property value="NO"/>','GDZCGHLC:1:135704')">资产归还</a>】&nbsp;&nbsp;【
																							<a href='' onclick="openPage('<s:property value="NO"/>','GDZCZYTRLC:1:135812')">转移他人</a>】&nbsp;&nbsp;【
																							<a href='' onclick="openPage('<s:property value="NO"/>','GDZCZQPZLC:1:135808')">增强配置</a>】&nbsp;&nbsp;【
																							<a href='' onclick="openPage('<s:property value="NO"/>','GDZCWXSQD:1:136104')">资产维修</a>】&nbsp;&nbsp;【
																							<a href='' onclick="openPage('<s:property value="NO"/>','GDZCLBFLC:1:136108')">资产报废</a>】&nbsp;&nbsp;【
																							<a href='' onclick="openPage('<s:property value="NO"/>','GDZCDSLC:1:136112')">资产丢失</a>】&nbsp;&nbsp;
																						</td>
																					</tr>
																					
															</s:if>
															
													</td>
												</tr>
												
											</table>
											<tr>
												
											</tr>
										</td>
									</tr>
								</table>

							</td>
						</tr>
					</s:iterator>
				</table>
			
		<s:if test='total==0'>
			<p
				style="display: block; width: 100%; background-color: #f6f6f6; text-align: center; font-size: 15px;">
				您暂时还有没有领用任何资产!!!
			</p>
		</s:if>
		<s:form name="editForm" theme="simple">
			<s:hidden name="baseinfo.demId" id="demId"></s:hidden>
			<s:hidden name="baseinfo.formId" id="formId"></s:hidden>
			<s:hidden name="itemname" id="itemname"></s:hidden>
			<s:hidden name="itemno" id="itemno"></s:hidden>
		</s:form>
	</div>
		<div region="south" border="false" style="height: 32px;background:red">
			<div id="pp"
				style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x; border: 1px solid #ccc;"></div>
		</div>
	</body>
</html>
