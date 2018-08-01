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
		     $.post("org_station_save.action",obj,function(data){
		           if(data=="add"){ 
	                	art.dialog.tips("岗位添加成功",3);
	                	setTimeout("_cancel()",1000);
	            	}else if(data=='update'){
	                	art.dialog.tips("岗位更新成功",3);
	                	setTimeout("_cancel()",1000);
	            	}
		     });
		}
		function deleteItem(){
			 art.dialog.confirm('确认是否删除当前岗位', function(){
			 		var id = $("#id").val();
					var pageurl = "org_station_delete.action";
					 $.post(pageurl,{id:id},function(msg){ 
						 if(msg=='success'){
							 location.reload(); 
						 }else{
							 alert('权限不足，删除失败!');
						 }
						 }); 
					return;
			}); 
		}
		function _cancel(){
			api.close();
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
<s:form name="editForm"  id="editForm" action="company_save" validate="true" theme="simple">
<table border="0" align="center" cellpadding="5" cellspacing="5">
 	 <tr>
   		<td >
   		<table>
   			<tr>
   				<td align="right" class="form_title">岗位名称：</td>
   				<td class="form_data">
   					  <s:textfield theme="simple" cssClass="{maxlength:32,required:true}"  cssStyle="width:120px" name="model.stationName"/><span style="color:red">*</span>
   				</td>
   			</tr>
   			
   			<tr>
   				<td align="right" class="form_title">岗位描述：</td><td class="form_data"><s:textarea theme="simple" cssClass="{maxlength:256}"  cssStyle="width:300px;height=100" name="model.memo"></s:textarea></td>
   			</tr>    		</table>
    </td>
  </tr>
  
</table>
		 	<s:hidden id="id" name="model.id"/>
		 	<s:hidden id="uuid" name="model.uuid"/>
</s:form>
</div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;border-top:1px solid #efefef">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-save" href="javascript:doSubmit();" >
                    保存</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:_cancel()">关闭</a>
            </div> 

</body>
</html>
