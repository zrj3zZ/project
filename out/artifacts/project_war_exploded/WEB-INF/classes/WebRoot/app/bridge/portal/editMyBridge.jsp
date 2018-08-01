<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.memo span {
			display:block;
			width:350px;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			}
		.tabcss{
		     margin-left:160px;
		     margin-top:50px;
			 border: 1px solid #efefef;
    		 width: 50%;
    		 line-height: 30px;
    		 
		}
		.tabcss tr th{
			background: #efefef;
			border-bottom: 1px solid #efefef;
			height: 15px;
			padding-left: 5px;
			font-size: 14px;
			color:  #585858;
		}
		.tbone{
			width: 230px;
			text-align:center;
			border-bottom: 1px solid #efefef;
			height: 27px;
			font-size: 14px;
			color:  #585858;
		}
		.tbtwo{
			width: 230px;
			text-align:center;
			border-bottom: 1px solid #efefef;
			height: 27px;
			font-size: 14px;
			color:  #585858;
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
		function del(uuid){
			alert(uuid);
		}
		function doSub(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				 art.dialog.tips("表单验证失败，请确认表单是否填写完整！");
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
			//alert(responseText);
		        if(responseText=="success"){
		     	   art.dialog.tips("保存成功",1);
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
	<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav"> 
		
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td> 
					<a id="btnEp" class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSub();" >保存</a>
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	
				</td>
				<td>
				 
		  		</td>
			</tr>
		</table> 
		 
		 </div>
	</div>
	<div region="center" style="padding:3px;border:0px;">
		<form action="syspersion_editMyBridge_save.action" id="editForm">
			<table class="tabcss">
				<s:property value="html" escapeHtml="false"/>
			</table>
		</form>
	</div>
</body>
</html>
