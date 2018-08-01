<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

 


 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	 <script type="text/javascript" src="iwork_js/bridge/conn_bridge.js"></script>    
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	 <script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	 </script>
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
			color:0000FF; 
		}
	</style>	
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form id="editForm" name="editForm" action="/conn_bridge_save.action" method="post">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            	
            		<tr>  
            			<td class="form_title" style="vertical-align:top"><span style="color:red;">*</span>连接名称:</td>
            			<td class="form_data"><input type="text" name="model.bridgeName" value="<s:property value="model.bridgeName"/>" id="editForm_model_bridgeName" class="{maxlength:32,required:true}" style="width:350px"/></td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>验证地址:</td>
            			<td  class="form_data">  
            				<input type="text" name="model.validateAddress" value="<s:property value="model.validateAddress"/>" id="editForm_model_validateAddress" class="{required:true}" style="width:350px"/>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>转向延时:</td>
            			<td  class="form_data"> 
            				<input type="text" name="model.delay" value="<s:property value="model.delay"/>" id="editForm_model_delay" style="width:250px" class="{maxlength:32,required:true}"/> <font color="#000000;">毫秒</font>
            			</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>连接状态:</td>
            			<td class="form_data">
            				<c:if test="${empty model.status || '开启' == model.status}">
            					<input type="radio" name="model.status" style="border:0px;" value="关闭"/>关闭
								<input type="radio" name="model.status" checked="checked" style="border:0px;" value="开启"/>开启
							</c:if>
							<c:if test="${ '关闭' == model.status}">
            					<input type="radio" name="model.status" checked="checked" style="border:0px;" value="关闭"/>关闭
								<input type="radio" name="model.status" style="border:0px;" value="开启"/>开启
							</c:if>
							
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title">唯一标识:</td>
            			<td class="form_data"><s:property value="model.uuid"/></td>
            		</tr>
            	</table>
                <input type="hidden" name="model.id" value="<s:property value="model.id"/>" id="editForm_model_id"/>
                <input type="hidden" name="model.uuid" value="<s:property value="model.uuid"/>" id="editForm_model_uuid"/>
               </s:form>
            </div> 
            <div region="north" border="false" style="text-align: left; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
             <div region="north" border="false"  style="text-align:left;">
 			<div class="tools_nav"> 
				 <a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="javascript:doSubmit();" >保存</a> 
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	
			</div>	
			</div>
        <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
		<div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
