<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
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
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	var mainFormValidator;
	$().ready(function() {
			mainFormValidator =  $("#editForm").validate({
				debug:false
			 });
			 mainFormValidator.resetForm();
		});
		
 //==========================装载快捷键===============================//快捷键
 		
 		jQuery(document).bind('keydown',function (evt){		
		   		if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
				 doSubmit(); return false;
		   }		
		}); //快捷键
		
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return;
				}
			var options = {
				error:errorFunc,
				success:successFunc 
			   }; 
			$('#editForm').ajaxSubmit(options);
		}
        errorFunc=function(){
           art.dialog.tips("保存失败！");
        }
	    successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText=="success"){
	            	art.dialog.tips("保存成功！");
	               cancel();
	           }
	           else if(responseText=="error"){
	              art.dialog.tips("保存失败！");
	           } 
	    }
		//关闭窗口
		function cancel(){
			api.close();
		}
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="pt_overall_save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
            		<tr>  
            			<td class="form_title">portlet名称:</td>
            			<td class="form_data"><s:textfield name="overallModel.title" cssClass="{maxlength:32,required:true}" theme="simple"/><span style="color:red;">*</span></td>
            		</tr> 
            		<tr>   
            			<td class="form_title">分组名称:</td>
            			<td class="form_data">
            			<s:select list="groupList" name="overallModel.groupName" cssClass="{required:true}" ></s:select><span style="color:red;">*</span>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title">门户类型:</td>
            			<td  class="form_data">  
            					<s:radio  name="overallModel.type" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'接口','1':'IFRAME','2':'外部数据源'}" value="overallModel.type" theme="simple"/><span style="color:red;">*</span>
            			</td>
            		</tr>
            		<tr>  
            			<td class="form_title">栏目高度:</td>
            			<td class="form_data">
								<s:textfield name="overallModel.height" cssClass="{maxlength:32,number:true,required:true}" theme="simple"/><span style="color:red;">*</span>
						</td>
            		</tr> 
            		
            		<tr>
            			<td class="form_title">接口:</td>
            			<td class="form_data">
            					<s:textfield name="overallModel.interface_" cssClass="{maxlength:128}" theme="simple"/><span style="color:red;">*</span>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title">IFRAME链接地址:</td>
            			<td class="form_data">
            				<s:textfield name="overallModel.urlLink" cssClass="{maxlength:128}" theme="simple"/><span style="color:red;">*</span>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title">更多连接地址</td>
            			<td class="form_data">	<s:textfield name="overallModel.moreLink" cssClass="{maxlength:128}" theme="simple"/></td>
            		</tr>
            		<tr>
            			<td class="form_title">状态:</td>
            			<td class="form_data">
            					<s:radio  name="overallModel.status" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'关闭','1':'开启'}" value="overallModel.status" theme="simple"/><span style="color:red;">*</span>
            			</td>
            		</tr> 
            	</table>
                <s:hidden name="overallModel.id"></s:hidden> 
               </s:form>  
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
</body>
</html>
