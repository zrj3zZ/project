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
		$("#FloatDiv").css("top", $('#typelist').height() + bodyTop) // 设置层的CSS样式中的top属性, 注意要是小写，要符合“标准”      
		}); 
		
		function myShopCar(id){
			$("#id").val(id);
			$("#editForm").attr("target","myLeCar");
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
     
        
      <div region="center" border="false" >
      		   <s:property value="html" escape="fase"/> 
	</div>
 
  </body>
</html>
		