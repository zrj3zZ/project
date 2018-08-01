<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>目录管理维护</title>
	<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/navigation/function_edit.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
	//==========================装载快捷键===============================//
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         $("#function_save").submit(); return false;
		     }
        }); //快捷键
        function doSubmit(){
  		  if($("#editForm_model_functionName").val()==""){
  			  art.dialog.tips("名称不允许为空",2);
  			  return ;
  		  }
  		  if($("#editForm_model_function_target").val()==""){
  			  art.dialog.tips("提交目标不允许为空",2);
  			  return ;
  		  }
  		  var url = "function_save.action";
  		    $.post(url,$("#editForm").serialize(),function(data){
  		    	parent.location.reload();
  		    });
  	      }
    function closeWin(){
    	api.close();
    }
</script>
</head>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<body>
<s:form name="editForm"  id="editForm"  action="function_save" validate="true" theme="simple">
<table width="500px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pagetitle"><img src="iwork_img/icon/system.gif" border="0">
        <s:if test="null == model">
            增加功能模块
        </s:if>
        <s:else>
            编辑功能模块
        </s:else>
	</td>
  </tr>

  <tr>
    <td >
    	<div region="center" style="padding: 3px; border: 0px">
			<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
  				<tr>
    				<td>
    					<table width="100%" border="0" cellpadding="2" cellspacing="0">
					        <tr>
						      	<td class="td_title">上级目录:</td>
						        <td class="td_data"><s:select label="上级目录" cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.directoryId" list="%{directoryList}" listKey="id" listValue="directoryName"></s:select>&nbsp;<span style='color:red'>*</span></td>
						    </tr>
						      <tr>
						      	<td class="td_title">模块名称:</td>
						        <td class="td_data">
						        	<s:textfield label="模块名称" cssClass="{validate:{required:true,maxlength:64,messages:{required:'必填字段',maxlength:'字段过长'}}}" name="model.functionName"/>&nbsp;<span style='color:red'>*</span></td>
						        </tr>
						      <tr>
						      	<td class="td_title">模块图标:</td>
						        <td class="td_data">
						        	<s:textfield label="模块图标" cssClass="{validate:{maxlength:256,messages:{maxlength:'字段过长'}}}" name="model.functionIcon"/></td>
						        </tr>
						      <tr>
						      	<td class="td_title">模块URL:</td>
						        <td class="td_data">
						        	<s:textfield label="模块URL" cssClass="{validate:{maxlength:256,messages:{maxlength:'字段过长'}}}" name="model.functionUrl"/></td>
						        </tr>
						      <tr>
						      	<td class="td_title">模块类型:</td>
						        <td class="td_data">
						        	<s:textfield label="模块类型" cssClass="{validate:{required:true,maxlength:64,messages:{required:'必填字段',maxlength:'字段过长'}}}" name="model.functionType"/>&nbsp;<span style='color:red'>*</span></td>
						     </tr>
						      <tr>
						      	<td class="td_title">提交目标:</td>
						        <td class="td_data">
						       	  <s:select label="" cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.functionTarget" value="model.functionTarget" list="#{'_blank':'弹出窗口【_blank】','_self':'当前窗口【_self】','_parent':'父窗口【_parent】','mainFrame':'主框架【mainFrame】','treeFrame':'左侧框架【mainFrame】'}" theme="simple" headerKey="" headerValue="--请选择--"></s:select>&nbsp;<span style='color:red'>*</span>
								</td>
						      </tr>
						      <tr>
						        <td class="td_title">模块描述:</td>
						        <td class="td_data">
									<s:textarea label="模块描述" cssClass="{validate:{maxlength:500,messages:{maxlength:'字段过长'}}}" name="model.functionDesc"></s:textarea>
								</td>
						      </tr>
    					</table>
				    </td>
				  </tr>
			</table>
		</td>
  	</tr>
  <tr>
  		<td>
  			<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px; padding-top: 5px; padding-right: 15px; margin-bottom: 20px;">
			<a href='javascript:doSubmit();' class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href='javascript:closeWin();' class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
			</div>
  		</td>
  </tr>
</table>
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
