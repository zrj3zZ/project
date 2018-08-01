<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/engine/conn_page.js"></script>  
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.form_title{  
			font-family:黑体; 
			font-size:14px;
			text-align:right;
			width:200px;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:#666; 
			height:25px;
		}
	</style>	
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="conn_design_save.action"  theme="simple">
            	<table>
            		<tr>
            		<td class="form_title" ><span style="color:red;">*</span>触发器接口路径:</td>
            		<td><s:textfield name="model.triggerPath" cssStyle="width:450px" cssClass="{maxlength:400,required:true}" theme="simple"/></td>
            		</tr>
            		<tr>
            		<td></td>
            			<td  class="form_data" >
								说明:<s:property value="description"/>
            			</td>
            		</tr>
            	</table>
				<s:hidden  id="groupid"  name="model.groupId" /> 
       			<s:hidden name="model.inMemo"  ></s:hidden>
       			<s:hidden name="model.inName" />
       			<s:hidden name="model.inType"/>
       			<s:hidden name="model.inAddress"/>
       			<s:hidden name="model.isCache"/>
       			<s:hidden name="model.cacheTime"/>  
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.uuid"/>
                <s:hidden   name="model.status"/>
                 
               </s:form>  
            </div> 
            <div region="north" border="false" style="text-align: left; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
 			<div class="tools_nav"> 
				 <a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="javascript:doSubmit();" >保存</a> 
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	
			</div>			
		 </div>
</body>
</html>
