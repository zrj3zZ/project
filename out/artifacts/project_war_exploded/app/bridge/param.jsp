<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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
				debug:false,
				errorPlacement: function (error, element) { //指定错误信息位置
				      if (element.is(':radio') || element.is(':checkbox')) {
				          var eid = element.attr('name');
				          error.appendTo(element.parent());
				      } else {
				          error.insertAfter(element);
				     }
				 } 
			 });
			 mainFormValidator.resetForm();
		});
	
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
           alert("保存失败！");
        }
	    successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText=="success"){
	               cancel();
	           }
	           else if(responseText=="error"){
	              alert("保存失败！");
	           } 
	    }
		//关闭窗口
		function cancel(){
			api.close();
		}
	
		function showExtend(){
			$("#paramExtend").show();
		}
		function hideExtend(){
			$("#paramExtend").hide();
		}
		$(function(){
			var extend = $("#pExtend").val();
			if(extend != null && extend != ""){
				$("#paramExtend").show();
			}
		})
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="conn_bridge_param_save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            		<tr>
            			<td>
            				<td class="form_title"><span style="color:red;">*</span>参数名称:</td>
	            			<td class="form_data">
									<s:textfield name="param.paramName" cssClass="{required:true}"  ></s:textfield>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
            				<td class="form_title"><span style="color:red;">*</span>键:</td>
	            			<td class="form_data">
									<s:textfield name="param.paramKey" cssClass="{required:true}"  ></s:textfield>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
            				<td class="form_title">值:</td>
	            			<td class="form_data">
									<s:textfield name="param.paramValue"></s:textfield>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
            				<td class="form_title">类型:</td>
	            			<td class="form_data">
	            				<s:if test="param.paramType == null || param.paramType eq \"字符\" ">
									<input type="radio" name="param.paramType" checked="checked" value="字符"/>字符
		            				<input type="radio" name="param.paramType" value="密码"/>密码
		            			</s:if>
		            			<s:if test="param.paramType eq \"密码\"">
									<input type="radio" name="param.paramType" value="字符"/>字符
		            				<input type="radio" name="param.paramType" checked="checked" value="密码"/>密码
		            			</s:if>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
            				<td class="form_title">外观:</td>
	            			<td class="form_data">
	            				<s:if test="param.paramSurface eq \"文本框\" || param.paramSurface == null">
									<input type="radio" name="param.paramSurface" checked="checked" value="文本框" onclick="hideExtend();"/>文本框
		            				<input type="radio" name="param.paramSurface" value="下拉框" onclick="showExtend();"/>下拉框
		            			</s:if>
		            			<s:if test="param.paramSurface eq \"下拉框\"">
									<input type="radio" name="param.paramSurface" value="文本框" onclick="hideExtend();"/>文本框
		            				<input type="radio" name="param.paramSurface" checked="checked" value="下拉框" onclick="showExtend();"/>下拉框
		            			</s:if>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
	            			<td class="form_title">是否必填:</td>
	            			<td class="form_data">
	            				<s:if test="param.isRequired eq \"是\" || param.isRequired == null">
		            				<input type="radio" name="param.isRequired" checked="checked" value="是"/>是
		            				<input type="radio" name="param.isRequired" value="否"/>否
		            			</s:if>
		            			<s:if test="param.isRequired eq \"否\"">
		            				<input type="radio" name="param.isRequired" value="是"/>是
		            				<input type="radio" name="param.isRequired" checked="checked" value="否"/>否
		            			</s:if>
	            			</td>
            		</tr>
            		<tr style="display: none;" id="paramExtend">
            			<td>
            				<td class="form_title">参数扩展:</td>
	            			<td class="form_data">
									<s:textfield name="param.paramExtend"></s:textfield>
									<div><font style="color: #7B6899;">填写格式：1：A|2:B</font></div>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
            				<td class="form_title">默认值:</td>
	            			<td class="form_data">
									<s:textfield name="param.defaultValue"></s:textfield>
							</td>
            			</td>
            		</tr>
            		<tr>
            			<td>
            				<td class="form_title">提交方式:</td>
	            			<td class="form_data">
	            				<s:if test="param.submitMode == 'POST' || param.submitMode == null">
	            					<input type="radio" name="param.submitMode" checked="checked" value="POST"/>POST
	            					<input type="radio" name="param.submitMode" value="GET"/>GET
	            				</s:if>
	            				<s:if test="param.submitMode == 'GET'">
	            					<input type="radio" name="param.submitMode" value="POST"/>POST
	            					<input type="radio" name="param.submitMode" checked="checked" value="GET"/>GET
	            				</s:if>
							</td>
            			</td>
            		</tr>
            		
            </table>
                 <input type="hidden" name="param.id" value="<s:property value="param.id"/>"/>
                <input type="hidden" name="param.uuid" value="<s:property value="param.uuid"/>"/>
                  <input type="hidden" name="param.orderIndex" value="<s:property value="param.orderIndex"/>"/>
                  <input type="hidden" id="pExtend" value="<s:property value="param.paramExtend"/>"/>
                  
               </s:form>  
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
        <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
		<div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
