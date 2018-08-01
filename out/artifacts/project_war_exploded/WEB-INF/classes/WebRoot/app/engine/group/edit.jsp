<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysengineiformmap.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	 <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var mainFormValidator;
	var api = art.dialog.open.api, W = api.opener;
	$().ready(function() {
	
			 var setting = {
				async: {
						enable: true, 
						url:"sysEngineIForm!openjson.action",
						dataType:"json"
					}, 
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: onClick
				} 
			};
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 }); 
			 mainFormValidator.resetForm();
			
		});
		function onClick(){
			
		}
		//执行提交动作
		function doSubmit(){
			
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
	                alert("保存成功！");
	                parent.location.reload();
	           }
	           else if(responseText=="error"){
	              alert("保存失败！");
	           } 
	      }
		function close(){
			api.close(); 
		}
		//权限地址簿
		function openAuthorityBook(obj){
			var code = obj.value;	
			var url = "authorityAddressBookAction!index.action?code=" + code;
			window.showModalDialog(url, obj, 'dialogWidth:650px;dialogHeight:535px;help:no;resizable:no;status:no;location:no');
		}
		
	</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
			white-space:nowrap;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
		.td_memo{
			text-align:left; 
			padding:12px;
			font-family:"宋体";
			border:1px solid #666;
			color:#666;
			font-size:12px;
		} 
		.td_memo_title{
			color:red;
		}
		
	</style>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 10px; background: #fff; border:0px;">
	            <s:form name="editForm" id="editForm" action="sysEngineGroup!save.action"  theme="simple">
	                <table cellpadding="3" width="100%">
	                <s:if test="parentid!=null">
	                	<tr>
	                        <td class="form_title">上级分类：</td>
	                        <td  class="form_data"><s:textfield theme="simple"  readonly="true" name="parentname"/></td>
	                    </tr>
	                    </s:if>
	                    <tr>
	                        <td class="form_title"><span style="color:red">*</span>分类名称：</td>
	                        <td class="form_data"><s:textfield theme="simple"  cssClass="{maxlength:32,required:true}"  name="model.groupname"/>
	                        
	                        </td>
	                    </tr>
	                    <s:if test="parentid==null">
		                    <tr>
		                        <td class="form_title"><span style="color:red">*</span>分组键值：</td>
		                        <td class="form_data"><s:textfield  cssClass="{maxlength:4,required:true}"  theme="simple" name="model.groupKey"/>
		                        </td>
		                    </tr>
	                    </s:if>
	                    <s:else>
	                    	<tr>
		                        <td class="form_title"><span style="color:red">*</span>分组键值：</td>
		                        <td class="form_data"><s:property value="model.groupKey"/><s:hidden theme="simple" name="model.groupKey"/> 
		                        </td>
		                    </tr>
	                    </s:else>
	                    <tr>
	                        <td class="form_title"> <span style="color:red">*</span>管理员：</td>
	                        <td class="form_data">
	                        	<s:textarea name="model.master" id="master" cssClass="{maxlength:80,required:true}"  cols="45" rows="4"  theme="simple" ></s:textarea>
	                        	<a href="javascript:openAuthorityBook(document.getElementById('master'));" class="easyui-linkbutton" plain="true" iconCls="icon-add">权限地址簿</a>	            				
	                        </td> 
	                    </tr>
	                    <tr>
	                        <td class="form_title"　>备注：</td>
	                        <td class="form_data"><s:textarea name="model.groupmemo" cols="45" rows="4"  theme="simple" ></s:textarea></td>
	                    </tr>
	                    <tr>
	                        <td class="form_title"></td>
	                        <td class="form_data">
									<fieldset  class="td_memo" style="padding:5px;color:#666">
								<legend class="td_memo_title">使用说明</legend>
								【分组键值】 用于创建"存储模型","表单模型"时,用于对表名\表单模板<br>进行命名区分
								<br>　　例如：分组名称为“知识管理”的分组键值为“KM”,<br>　　则在知识管理目录中建立的数据表均以"BD_KM"开头<br>

							</fieldset>
									
							</td>
	                    </tr>
	                </table>
	                <s:hidden name="model.id"></s:hidden>
	                <s:hidden   name="model.parentid"/>
	                <s:hidden   name="model.orderIndex"/>
	              </s:form> 
	              
            </div>
            <div region="south" border="false" style="text-align: right; height: 45px; line-height: 30px;border-top:1px solid #ccc;padding:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:close()">取消</a>
            </div>
</body>
</html>
