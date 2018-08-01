<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
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
		function doSubmit(){
			var info = "确认传输当前模型吗?"
			if(confirm(info)){
			var spaceId = $("#spaceId").val();
			var isCover =  $("input[name='isCover']:checked").val(); 
			if(isCover=="undefined"){alert("请选择");return false;}
				var url='sysImp_doExecute.action';
			    $.post(url,{spaceId:spaceId,isCover:isCover},function(data){
			    	if(data=='error'){
			    		alert('传输模型参数异常');
					}else{
						alert(data);
						cancel();
					}
			    });
			} 
			//document.forms[0].submit();
		}
		//关闭窗口
		function cancel(){
			api.close();
		}
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            	<form action="sysImp_doExecute.action">
           				<table width="100%">
           					<tr>
           						<td>如果模型已存在则</td>
           						<td>
           							<s:radio name="isCover" list="#{'cover':'覆盖更新','ignore':'跳过'}" listKey="key" listValue="value"  theme="simple"></s:radio>
           							<s:hidden name="spaceId" id="spaceId"></s:hidden>
           						</td>
           					</tr>
           				</table>
           				</form>
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定传输</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
</body>
</html>
