<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>目录管理维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
	var api = art.dialog.open.api, W = api.opener;
	  function doSubmit(){
		  if($("#editForm_model_directoryName").val()==""){
			  art.dialog.tips("名称不允许为空",2);
			  return ;
		  }
		  if($("#editForm_model_directoryTarget").val()==""){
			  art.dialog.tips("提交目标不允许为空",2);
			  return ;
		  }
		  var url = "directory_save.action";
		    $.post(url,$("#editForm").serialize(),function(data){
		    	parent.location.reload();
		    });
	      }
	    function closeWin(){
	   		api.close();
	    }    
	</script>
</head>
<body class="easyui-layout">
<s:form name="editForm" id="editForm" action="directory_save" theme="simple">
<div region="north" style="padding: 3px; border: 0px;padding-left:10px;font-size:18px;">
        <s:if test="null == model">
           	 新增系统目录管理菜单
        </s:if>
        <s:else>
           	 编辑系统目录管理菜单
        </s:else>
</div>
			<div region="center" style="padding: 3px; border: 0px">
				<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<table width="100%" border="0" cellpadding="2" cellspacing="0">
								<tr>
									<td class="td_title">目录名称:</td>
									<td class="td_data">
										<s:textfield label="目录名称"	cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.directoryName" />&nbsp;<span style='color: red'>*</span>
									</td>
								</tr>
								<tr>
									<td class="td_title">目录图标:</td>
									<td class="td_data">
										<s:textfield label="目录图标" name="model.directoryIcon" />
									</td>
								</tr>
								<tr>
									<td class="td_title">目录URL:</td>
									<td class="td_data">
										<s:textfield label="目录URL" name="model.directoryUrl" />
									</td>
								</tr>
								<tr>
									<td class="td_title">提交目标:</td>
									<td class="td_data">
										<s:select label="" cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.directoryTarget" value="model.directoryTarget"  list="#{'_blank':'弹出窗口【_blank】','_self':'当前窗口【_self】','_parent':'父窗口【_parent】','mainFrame':'主框架【mainFrame】','treeFrame':'左侧框架【mainFrame】'}" theme="simple" headerKey="" headerValue="--请选择--"></s:select>&nbsp;<span style='color: red'>*</span>
									</td>
								</tr>
								<tr>
									<td class="td_title">目录描述:</td>
									<td>
										<s:textarea label="目录描述" name="model.directoryDesc"></s:textarea>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		
		<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px; padding-top: 5px; padding-right: 15px; margin-bottom: 20px;">
			<a href='javascript:doSubmit()' class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href='javascript:closeWin();' class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 	<s:hidden name="model.orderindex" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 	<s:hidden name="model.orderindex"/>
		 </s:else>
</s:form>
</body>
</html>
