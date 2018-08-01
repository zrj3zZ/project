<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>组织机构管理</title> 
	<s:head/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/org/company_edit.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/org/company_edit.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		var mainFormValidator; 
		$().ready(function() {
		   mainFormValidator =  $("#editForm").validate({
			   errorPlacement: function (error, element) {
	               error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
			   }
		   });
		   mainFormValidator.resetForm();
		});
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){	
					return;
			}
			 var obj = $('#editForm').serialize(); 
		     $.post("company_save.action",obj,function(data){
		           if(data=="add"){ 
	                	art.dialog.tips("组织添加成功",3);
	                	setTimeout("_cancel()",1000);
	            	}else if(data=='update'){
	                	art.dialog.tips("组织更新成功",3);
	                	setTimeout("_cancel()",1000);
	            	}else if(data=="companyName error"){
	            		art.dialog.tips("组织名称与许可不一致");
	            		$("#editForm_model_companyname").val("");
	            	}else if(data=="error-1001"){
	            		art.dialog.tips("组织名称父级名称与下属单位名称不能重复");
	            		$("#editForm_model_companyname").val("");
	            	}
		     });
		}
		function _cancel(){
			api.close();
		}
		function setCompanyInfo(){ 
			var title = $("#selCompany").find("option:selected").text();
			var key = $("#selCompany").find("option:selected").val();
			$("#companyname").val(title);
			$("#companyno").val(key);  
		}
	</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:15px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
			padding:3px;
		}
	</style>
</head>
<body class="easyui-layout">
<div region="center" border="false" class="winBackground" style="padding: 15px;padding-top:40px;">
<s:form name="editForm"  id="editForm" validate="true" theme="simple">
<table border="0" align="center" cellpadding="5" cellspacing="5">
 	 <tr>
   		<td >
   		<table>
   		<s:if test="parentname!=null">
   			<tr>
   				<td align="right" class="form_title">上级组织名称：</td>
   				<td class="form_data">  
   				<s:property value="parentname"/>
   				</td>
   			</tr>
   		</s:if>
   			<tr>
   				<td align="right" class="form_title">组织编号：</td>
   				<td class="form_data">  
   					<s:textfield theme="simple"  cssClass="{maxlength:500}"  cssStyle="width:300px" name="model.companyno"/>
   				<span style='color:red'>*</span>
   			</tr> 
   			<tr>
   				<td align="right" class="form_title">组织名称：</td>
   				<td class="form_data">  
   					<s:textfield theme="simple"  cssClass="{maxlength:500}"  cssStyle="width:300px" name="model.companyname"/>
   				<span style='color:red'>*</span>
   			</tr> 
   			<tr> 
   				<td align="right" class="form_title">组织类型：</td><td class="form_data"><s:radio theme="simple" cssClass="{maxlength:64,required:true}"  value="model.companytype" list="#{'1':'集团','5':'子公司','2':'分公司','3':'事业部','4':'办事处'}" name="model.companytype"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">组织描述：</td><td class="form_data"><s:textarea theme="simple" cssClass="{maxlength:256}"  cssStyle="width:300px;height=100" name="model.companydesc"></s:textarea></td>
   			</tr> 
   			<tr> 
   				<td align="right" class="form_title">地区编号：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.zoneno"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">地区名称：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}" cssStyle="width:120px" name="model.zonename"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">外观样式：</td><td class="form_data"><s:select cssClass="{maxlength:64,required:true}"  name="model.lookandfeel" list="#{'def':'金鹰BPM标准版样式'}" theme="simple"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">单位地址：</td><td class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:500}"  cssStyle="width:300px" name="model.orgadress"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">邮　　编：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}" cssStyle="width:120px" name="model.post"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">电　　话：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.tel"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">扩 展 一：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.extend1"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">扩 展 二：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.extend2"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">扩 展 三：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.extend3"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">扩 展 四：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.extend4"/></td>
   			</tr>
   			<tr>
   				<td align="right" class="form_title">扩 展 五：</td><td class="form_data"><s:textfield theme="simple" cssClass="{maxlength:32}"  cssStyle="width:120px" name="model.extend5"/></td>
   			</tr>
   		</table>
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
		 <s:hidden name="queryName" />
		 <s:hidden name="queryValue" />
		 <s:hidden name="model.parentid"/> 
		 <s:hidden name="parentname"/> 
</s:form>
</div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;border-top:1px solid #efefef">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-save" href="javascript:doSubmit();" >
                    保存</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:_cancel()">关闭</a>
            </div> 

</body>
</html>
