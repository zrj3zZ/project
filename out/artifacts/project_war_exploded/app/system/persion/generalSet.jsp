<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>
</head>	
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">

$(function(){
	if('<s:property value="rolelistHtml"/>'!=""){
		art.dialog.tips('<s:property value="rolelistHtml"/>',2);
	}
});
 	var mainFormValidator;
	//保存表格
	function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				/* var options = {
					error:errorFunc,
					success:showResponse 
				};
				$("#editForm").ajaxSubmit(options); */
				$("#editForm").submit();
			}
	}
	/* errorFunc = function(){
		art.dialog.tips("保存失败",2);
	}
	showResponse = function(){
		art.dialog.tips("保存成功",2);
	} */
	//设置工作状态
	$(document).ready(function(){
		 mainFormValidator =  $("#editForm").validate({
				   errorPlacement: function (error, element) {
		               error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面
				   }
			 }); 
		 mainFormValidator.resetForm(); 
		$("#workStatusOption").bind("click", function(){
			if($("#workStatusOption").val()=="其他"){
				$("#editForm_userModel_workStatus").val("");
				$("#editForm_userModel_workStatus").show();
			}else{
				$("#editForm_userModel_workStatus").val($("#workStatusOption").val());
				$("#editForm_userModel_workStatus").hide();
			}
		});
		if($("#editForm_userModel_workStatus").val()=="工作中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("工作中");}
		else if($("#editForm_userModel_workStatus").val()=="出差中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("出差中");}
		else if($("#editForm_userModel_workStatus").val()=="休假中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("休假中");}
		else if($("#editForm_userModel_workStatus").val()=="加班中"){$("#editForm_userModel_workStatus").hide();$("#workStatusOption").val("加班中");}
		else{
			$("#editForm_userModel_workStatus").show();
			$("#workStatusOption").val("其他");
		}
	});
	//弹出上传头像窗口
	function add_image(){
		var userid = $('#editForm_userModel_userid').val();
		var pageUrl = "syspersion_photo.action?userid="+userid;
		 art.dialog.open(pageUrl,{
						id:'dg_addImage',  
	       				title:'上传头像',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:510,
				        close:function(){
				        	window.location.reload();
				        }
					 });
	}
</script>
<style type="text/css">
	body{
		background:#fff;
	}
	
 </style>
<body>
<s:form id ="editForm" name="editForm" action="syspersion_update"  theme="simple">

<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
      <td>
		  		<div  class="tools_nav">
			  		<a id="btnEp" class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSubmit();" >保存</a>
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		  		</div>
	  </td>
    </tr>
    <tr>
      <td align="center" valign="top" height="100%" >
		<div id="editInfo" style="width:80%;">
			<div id="basicInfo">
				<table width="100%">
					<tr>
						<td colspan="2" style="border-bottom:1px solid #efefef;height:7px;">
							<h4><span style="float: left">基本资料</span></h4>
						</td>
					</tr>
					<tr>
						<td width="50%">
							<div id="basic-left">
								<table width="100%">
									<tr>
										<td class='td_title' width="10%">部门名称</td>
		    							<td class='td_data' width="40%"><s:property value="userModel.departmentname"/></td>
									</tr>
									<tr>
										<td class='td_title'>岗位名称</td>
										<td class='td_data'><s:property value="userModel.postsid"/>/<s:property value="userModel.postsname"/></td>
									</tr>
									<tr>
										<td class='td_title'>岗位职责</td>
										<td class='td_data'><s:textfield  theme="simple"   cssClass="true_input" name="userModel.postsresponsibility"/></td>
									</tr>
									<tr>
										<td class='td_title'>直接上级</td>
										<td class='td_data'><s:textfield  theme="simple"   cssClass="true_input" name="userModel.bossid"/></td>
									</tr>
									<tr>
										<td class='td_title'>我的状态</td>
										<td class='td_data'>
											<s:select cssClass="select" list="#{'工作中':'工作中','出差中':'出差中','休假中':'休假中','加班中':'加班中','其他':'其他'}"  name="workStatusOption" id="workStatusOption" theme="simple"></s:select><s:textfield  theme="simple"   cssClass="true_input" name="userModel.workStatus"/>
										</td>
									</tr>
									<tr>
										<td class='td_title'>签名 </td>
										<td class='td_data'><s:textarea theme="simple"  cssClass='{maxlength:56,required:true}' cssStyle="width:250px;resize: none;"  name="userModel.selfdesc"/></td>
									</tr>
								</table>
							</div>
						</td>
						<td width="50%">
							<div id="basic-right">
								<table width="100%">
									<tr>
										<td style="text-align: center;vertical-align:middle">
											<div id="user_image_div">
												<img width="150" id="user_image" src='<s:property value='userImgPath'/>' alt='用户照片' title='用户照片' name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
											</div>
										</td>
									</tr>
									<tr>
										<td style="text-align: center;"><a href="javascript:add_image();" class="easyui-linkbutton" iconCls="icon-add">修改头像</a></td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="contactInfo">
				<table width="100%">
					<tr>
						<td colspan="2" style="border-bottom:1px solid #efefef;height:7px;">
							<h4><span style="float: left">联系方式</span></h4>
						</td>
					</tr>
					<tr>
						<td width="50%">
							<div id="contact-left">
								<table width="100%">
									<tr>
										<td class='td_title'>手机</td>
		    							<td class='td_data'><s:textfield  theme="simple"   cssClass='{maxlength:56,required:true}'  name="userModel.mobile"/></td>
									</tr>
									<tr>
										<td class='td_title'>邮箱</td>
										<td class='td_data'><s:textfield  theme="simple" cssClass='{maxlength:56,required:true,email:true}'   name="userModel.email"/></td>
									</tr>
									<tr>
										<td class='td_title'>办公室电话</td>
		    							<td class='td_data'><s:textfield  theme="simple"   cssClass='{maxlength:56,required:false}'   name="userModel.officetel"/></td>
									</tr>
									<tr>
										<td class='td_title'>办公室传真</td>
		    							<td class='td_data'><s:textfield  theme="simple"  cssClass='{maxlength:56,required:false}' name="userModel.officefax"/></td>
									</tr>
									
								</table>
							</div>
						</td>
						<td width="50%" style="vertical-align:top">
							<div id="contact-right" >
								<table width="100%">
								<tr>
										<td class='td_title'>紧急联系人</td>
		    							<td class='td_data'><s:textfield  theme="simple"   cssClass="true_input"  name="userModel.jjlinkman"/></td>
									</tr>
									<tr>
										<td class='td_title'>紧急联系电话</td>
		    							<td class='td_data'><s:textfield   theme="simple"   cssClass="true_input"  name="userModel.jjlinktel"/></td>
									</tr>
									<tr>
										<td class='td_title'>QQ/MSN </td>
		    							<td class='td_data'><s:textfield   theme="simple"   cssClass="true_input" name="userModel.qqmsn"/></td>
									</tr>
									<tr>
										<td></td>
		    							<td ></td>
									</tr>
									
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
     </td>
    </tr>
  </table>
  	<s:hidden name="userModel.id"/>
  	<s:hidden name="userModel.userid"/>
  	<s:hidden  theme="simple"   name="userModel.extend1"/>
  	<s:hidden  theme="simple"  name="userModel.extend2"/>
  	<s:hidden  theme="simple" name="userModel.extend3"/>
  </s:form>
</body>	
</html>
