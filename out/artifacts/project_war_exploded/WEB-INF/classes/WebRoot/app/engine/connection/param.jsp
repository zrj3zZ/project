<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
		function setParamNameStatus(){
			var chk = document.getElementById('editForm_checkParams');
			if(chk.checked){
				$("#editForm_param_name").val("NONE");
				$("#editForm_param_name").attr("readOnly",true);
				$("#editForm_param_name").attr("style","background-color:#efefef;");
			}else{
				$("#editForm_param_name").val("");
				$("#editForm_param_name").attr("readOnly",false);
				$("#editForm_param_name").attr("style","");
			}
			
		}
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="conn_design_param_save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            	<tr>  
            			<td class="form_title"><span style="color:red;">*</span>参数标题:</td>
            			<td class="form_data">
								<s:textfield name="param.title" cssClass="{maxlength:32,required:true}"  ></s:textfield>
						</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>参数名称:</td>
            			<td class="form_data">
            			
            			<s:if test="param.name=='NONE'">
            			<s:textfield name="param.name" readonly="true" cssStyle="background-color:#efefef" cssClass="{maxlength:32,required:true}" ></s:textfield>
            			<s:checkbox name="checkParams" value="true" label="无参数" onclick="setParamNameStatus()"></s:checkbox><label for="editForm_checkParams" class="checkboxLabel">无参数名称</label>
            			</s:if>
            			<s:else>
            			<s:textfield name="param.name" cssClass="{maxlength:32,required:true}" ></s:textfield>
            				<s:checkbox name="checkParams" value="false" label="无参数" onclick="setParamNameStatus()"></s:checkbox><label for="editForm_checkParams" class="checkboxLabel">无参数名称</label>
            			</s:else>
            		</tr>
            		<s:if test="param.inorout=='input'">
            		<tr>
            			<td class="form_title">参数值:</td>
            			<td class="form_data">
            			<s:textfield name="param.source"  ></s:textfield>
            		</tr>
            		<tr>
            			<td class="form_title">是否必填:</td>
            			<td class="form_data">
            			<s:radio  name="param.isCheck"   list="#{'1':'是','0':'否'}"></s:radio>
            		</tr>
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>参数类型:</td>
            			<td class="form_data">
            				<s:select name="param.type" list="#{'NUM':'数值','CHAR':'文本','DATE':'日期','STRUCTURE':'参数结构(适用SAP)','TABLE':'记录集(适用SAP)'}"></s:select>
            			</td>
            		</tr>
            		</s:if>
            		<s:else>
	            		<tr>
	            			<td class="form_title"><span style="color:red;">*</span>参数类型:</td>
	            			<td class="form_data">
	            				<s:select name="param.type" list="#{'NUM':'数值','CHAR':'文本','DATE':'日期','TABLE':'记录集'}"></s:select>
	            			</td>
	            		</tr>
            		</s:else>
            		
            	</table>
                <s:hidden name="param.id"></s:hidden>
                <s:hidden name="param.inorout"></s:hidden>
                <s:hidden name="param.pid"></s:hidden>
                <s:hidden name="param.orderIndex"></s:hidden>
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
