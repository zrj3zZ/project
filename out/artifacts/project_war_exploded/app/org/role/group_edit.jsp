<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
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
		//执行保存
		//表单提交
		function save(){ 
			var valid = mainFormValidator.form(); //执行校验操作 
			if(!valid){
					return false;
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
           if(responseText!="0"){
               alert("保存成功!");
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
		
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px;">
            <s:form name="editForm" id="editForm" action="role_savegroup.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>分组名称:</td>
            			<td  class="form_data">  
            				<s:textfield name="groupmodel.groupName" cssClass="{maxlength:32,required:true}" theme="simple"/>
         			</td> 
            		</tr>
            		<tr> 
            			<td class="form_title"><span style="color:red;">*</span>管理员:</td>
            			<td class="form_data"><s:textfield name="groupmodel.master" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            		</tr> 
            		
            		<tr>
            			<td class="form_title">描述:</td>
            			<td class="form_data">
            				<s:textarea cssStyle="width:300px;height:100px;" name="groupmodel.memo" cssClass="{maxlength:300}" ></s:textarea>
            			</td>
            		</tr>
            	</table>  
                <s:hidden name="groupmodel.id"></s:hidden>
               </s:form>  
            </div> 
            <div region="south" style="border:0px;padding:10px;height:50px;">
				<fieldset  class="td_memo" style="padding:5px;color:#666">
								<legend class="td_memo_title">使用说明</legend>
								
							</fieldset>
			</div>
         <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;border:0px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:save();" >确定</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
            </div> 
</body>
</html>
