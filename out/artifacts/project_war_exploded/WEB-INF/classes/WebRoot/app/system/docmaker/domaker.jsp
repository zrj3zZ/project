<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>文档制作</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">	
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <style type="text/css">
    .panel-title{
        color:#3F3F3F;
        font-size:18px;
        font-family:黑体;
        padding:10px;
        border-bottom:1px solid #efefef;
     } 
	body {  
    	 font-family: 宋体,Geneva, Arial, Helvetica, sans-serif;  
     	 font-size:   100%;  
    	 *font-size:  80%;
    	 margin-left: 0px;
		 margin-top: 0px;
		 margin-right: 0px;
		 margin-bottom: 0px;  
 	}
 	.optionSet{
 		width:600px;
 		border:1px solid #efefef;
 		margin-left:auto;
 		margin-right:auto;
 		padding:10px;
 		background:#FFFFCC;
 	}
 	.optionSet table td{
 		padding:5px;
 		font-size:14px;
 		font-family:微软雅黑;
 	} 
 	.item_title{
 		text-align:right;
 	}
 	.item_data{
 	}
 	.btn{
 		margin-left:auto;
 		margin-right:auto;
 		 text-align:center;
 		 padding:10px;
 		 margin-top:10px;
    
 	}
 	
 	.btn a{
 	  padding:10px;
 	  font-size:20px;
 	  text-decoration:none;
 	   
	    color:#333;
	     
	      background: -moz-linear-gradient(#c4c4c4,#f5f5f5); /* FF 3.6+ */  
	     background: -ms-linear-gradient(#c4c4c4,#f5f5f5); /* IE10 */  
	     background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#c4c4c4 ), color-stop(100%, #f5f5f5)); /* Safari 4+, Chrome 2+ */  
	     background: -webkit-linear-gradient(#c4c4c4,#f5f5f5); /* Safari 5.1+, Chrome 10+ */  
	     background: -o-linear-gradient(#c4c4c4,#f5f5f5); /* Opera 11.10 */  
	     filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#c4c4c4', endColorstr='#f5f5f5'); /* IE6 & IE7 */  
	     -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr='#c4c4c4', endColorstr='#f5f5f5')"; /* IE8+ */  
	     background: linear-gradient(#c4c4c4, #f5f5f5); /* the standard */ 
	      cursor: pointer;
		  -moz-border-radius: 5px 5px 5px 5px;
		  -webkit-border-radius: 5px 5px 5px 5px;
		  border-radius: 5px 5px 5px 5px;
		  font-family:微软雅黑;
 	}
 	.btn a:hover{
 		background: -moz-linear-gradient(#f5f5f5, #c4c4c4); /* FF 3.6+ */  
	     background: -ms-linear-gradient(#f5f5f5, #c4c4c4); /* IE10 */  
	     background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #f5f5f5), color-stop(100%, #c4c4c4)); /* Safari 4+, Chrome 2+ */  
	     background: -webkit-linear-gradient(#f5f5f5, #c4c4c4); /* Safari 5.1+, Chrome 10+ */  
	     background: -o-linear-gradient(#f5f5f5, #c4c4c4); /* Opera 11.10 */  
	     filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#f5f5f5', endColorstr='#c4c4c4'); /* IE6 & IE7 */  
	     -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr='#f5f5f5', endColorstr='#c4c4c4')"; /* IE8+ */  
	     background: linear-gradient(#f5f5f5, #c4c4c4); /* the standard */ 
	     color:#999;
 	     
 	}
 	.btn a:active {
	    color:#666;
 	}
</style>
<script type="text/javascript">
	function doExcute(){
		var url = "";
		$("#frmMain").attr("action",url);
	}
</script> 
</head>

<body class="easyui-layout">
	<div region="north" style="height:110px" border="false">
	<div class="panel-title">文档制作</div>
		
	</div>
	<div region="center"  style="border:0px;padding:0px;background:#gegege" >
			<div class="optionSet">
			<s:form name="frmMain" id="frmMain" theme="simple">
				<table width="100%">
				<tr>
					<td class="item_title">是否包含存储模型</td>
					<td class="item_data">
						<s:radio name="isMetaData" id="isMetaData" value="0" listKey="key" listValue="value" cssStyle="border:0px;" theme="simple" list="#{'0':'是','1':'否'}"></s:radio>
					</td>
					</tr>
				<tr>
					<td class="item_title">是否包含表单模型</td>
					<td class="item_data">
						<s:radio name="isFormMap" id="isFormMap"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" theme="simple" list="#{'0':'是','1':'否'}"></s:radio>
					</td>
					</tr>
				<tr>
					<td class="item_title">是否包含流程模型</td>
					<td class="item_data">
						<s:radio name="isProcess" id="isProcess"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
					</td>
				</tr>
				<tr>
					<td class="item_title">是否包含主数据模型</td>
					<td class="item_data">
						<s:radio name="isDem" id="isDem"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
					</td>
				</tr>
				<tr>
					<td class="item_title">是否包含接口模型</td>
					<td class="item_data">
						<s:radio name="isReport" id="isReport"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
					</td>
				</tr>
				<tr>
					<td class="item_title">是否包含接口模型</td>
					<td class="item_data">
						<s:radio name="isConn" id="isConn"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
					</td>
				</tr>
				<tr>
					<td class="item_title">是否包含组织模型</td>
					<td class="item_data">
						<s:radio name="isConn" id="isConn"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
					</td>
				</tr>
				<tr>
					<td class="item_title">是否包含外部数据源模型</td>
					<td class="item_data"> 
						<s:radio name="isConn" id="isConn"  value="0" listKey="key" listValue="value"  cssStyle="border:0px;" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
					</td>
				</tr>
				</table>
				</s:form>
			</div>
			<div class="btn">
				<a href="javascript:doExcute()">生成系统文档</a>
			</div>
	</div>
</body> 
 
</html>
