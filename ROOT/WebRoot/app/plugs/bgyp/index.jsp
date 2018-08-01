<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>编辑</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript">
    	$(this).scroll(function() { // 页面发生scroll事件时触发  
		//alert($('#typelist').height());
		var bodyTop = 0;   
		if (typeof window.pageYOffset != 'undefined') {   
			bodyTop = window.pageYOffset;   
		}   
		else if (typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat')   
		{   
			bodyTop = document.documentElement.scrollTop;   
		}   
		else if (typeof document.body != 'undefined') {   
			bodyTop = document.body.scrollTop;   
		}   
		$("#FloatDiv").css("top", $('#typelist').height() + bodyTop); // 设置层的CSS样式中的top属性, 注意要是小写，要符合“标准”      
		}); 
		
		$(function(){
			$("#content").change().keyup(function(event){  
				   if(event.keyCode == 13){    
					   searchCategory();
				   }   
			 });
		});
		
		function myShopCar(id){
			$("#id").val(id);
			$("#editForm").attr("target","myLeCar");
			$(window.document).find("div#FloatDiv").css("visibility","visible");
			$("#editForm").attr("action","iwork_bgyp_shopAdd.action");
			$("#editForm").submit();
		}
		function selectTab(no){
			var url = "iwork_bgyp_index.action?lbbh="+no;
			this.location.href=url;
		}
		function selectTabs(){
			var url = "iwork_bgyp_selectTabs.action";
		//	window.location.href=url;
		    parent.addTab("办公用品领用记录",url);
		}
		// 领用品搜索
		function searchCategory(){
			var content = $("#content").val();
			var no = $("#lbbh").val();
			var url = "iwork_bgyp_search.action?lbbh="+no+"&content="+content;
			this.location.href=url;
		}
		
    </script>
    <style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			width:150px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
		.unselectTab{
			 color:#333;	
			 font-size:12px;
			 line-height:20px;
			 border-bottom:1px dotted #efefef;
			  padding-left:5px;
			  cursor:pointer;
		}
		.selectTab{
			font-weight:bold;
			 color:#000;	
			 font-size:12px;
			 line-height:20px;
			 border-bottom:1px dotted #efefef;	
			 background-color:#efefef;
			 padding-left:5px;
		}
		#small img{
		width:80px;
		height:80px;
		border:#9E9E9E 1px solid;
		margin:2px;
		}
	#FloatDiv{
		position:absolute;
		width:200px; 
		height:200px; 
		visibility:visible; 
		border: 1px;
		top:300px;   
		left:3px;   
		}
	.typeTitle{
		background-color:#F7F7F7;
		 border-left:1px solid #CCCCCC;
		 border-right:1px solid #CCCCCC;
		 border-top:1px solid #CCCCCC;
		 border-bottom:1px solid #FFFF;
		 font-size:14px; vertical-align:bottom; 
		 text-align:left; 
		 padding-left:5px;
		 font-weight:bold;
		 padding-bottom:2px;
		 padding-top:2px;
		 color:#990000;	
		}
	.le_pic{
		    border:1px solid #efefef;
			padding:1px;
		}
	.objItem{
		padding-left:5px;
		color:#798FB9;
		 font-size:12px; vertical-align:bottom; 
		}
	.fixedDiv{ 
	 overflow:auto; 
	 height:expression(window.screen.availHeight-295);
	}	
	.fixedHeaderTr { 
	 position:relative; 
	 top:expression(this.offsetParent.scrollTop); 
	 z-index:20;
	}
	 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
			
	</style>	
  </head>
    <body class="easyui-layout">
      <div region="north" border="false" >
      	<div class="tools_nav">
          <table style='position:absolute;top:-2px;right:15px;' border="0" cellspacing="0" width="100%" cellpadding="0" align="center">
			<tr height=30> 
				<td style="font-size:14px;text-align:left"></td>
				<td style="font-size:12px;text-align:right">领用品搜索&nbsp;
					<input type=text id='content' name='content' class='actionsoftInput' value='<s:property value="content"/>' />
					<input type=button value='搜 索'  class ='actionsoftButton' onClick="searchCategory();return false;" border='0'>&nbsp;&nbsp;
					<a href='#' onclick="myShopCar('2');">
					<img src=iwork_img/shopCar2.gif width=18 border=0>领用车
					</a>&nbsp;&nbsp;
				</td>
				<td><a href='#' onclick="selectTabs()"><img src=iwork_img/page_white_text.png width=15 border=0>领用记录</a></td>
			</tr>
		</table>
        </div>
      </div>
        <div region="west"  region="west" icon="icon-reload"  border="false"  split="true"  style="width:220px;padding-left:5px;overflow:hidden;border-right:1px soled #999">
        	<div>
        		<s:property value="typetab" escape="fase"/>
        	</div>
        	<div>
	        	<div id="FloatDiv">
					<iframe name="myLeCar" width="210"  src="iwork_bgyp_shopcar.action"  frameborder=auto ></iframe>
				</div>
        	</div>
        	<s:form name="editForm" id= "editForm" theme="simple" action="iwork_bgyp_shopAdd">
        		<input type="hidden" name="id" id="id"/>
		 	</s:form>
        </div>
      <div region="center" border="false" >
      		   <s:property value="html" escape="fase"/> 
	</div>
	<s:hidden name="lbbh" id="lbbh"></s:hidden>
  </body>
</html>
		