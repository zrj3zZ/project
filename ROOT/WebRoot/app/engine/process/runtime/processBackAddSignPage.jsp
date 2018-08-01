<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<link rel="stylesheet" type="text/css" href="iwork_css/button.css" />
<script language="javascript" src="iwork_js/commons.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/engine/process_page.js"></script>
<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	$(document).ready(function(){
		var taskId = $("#taskId").val();
	    $.ajax({
			type: "POST",
			url: "zqb_process_priority.action",//设置当前任务优先级
			data: {taskId:taskId},
			success: function(data){
				if(data!=null&&data!=""){
					if($("#Priority0").val()==data)
						$("#Priority0").attr("checked",true);
					if($("#Priority1").val()==data)
						$("#Priority1").attr("checked",true);
				}else{
					if($("#Priority0").val()==data)
						$("#Priority0").attr("checked",true);
				}
			}
	 	});
	});
       function lockPage(){
		}
       function unLockPage(){
			return true;	
	   }
       function failResponse(){
      	 art.dialog.tips("发送失败",3);
			return true;
		}
		function successResponse(responseText, statusText, xhr, $form){
			var arr=responseText;
		//	alert(responseText);
			art.dialog.tips("发送成功",1);
			
			if(arr!=null){
				//alert(responseText);
				if(arr=="success"){
					art.dialog.tips("发送成功",1);
					//刷新办理列表
					try{
						window.parent.opener.reloadWorkList();
					}catch(err){}
					setTimeout('parent.window.close();',1000);
				}else{
					art.dialog.tips("发送失败",1);
				}
			}else{
				art.dialog.tips("发送失败,请联系管理员",3);
			}
			return true;
		}
		//执行发送动作
       function exexuteSendAction(){
        $("#sendbtn").attr("disabled",true);
        var obj = $('#ifromMain').serialize();
			     $.post("processRuntimeExecuteBackUp.action",obj,function(data){
			            var arr=data;
			            try{
			            	showSysTips();
			            }catch(e){}
						art.dialog.tips("发送成功",1);
						if(arr!=null){
							if(arr=="success"){
								art.dialog.tips("发送成功",1);
								//获取加签人信息
								var s='';
								var value = document.getElementsByName('receiveUser');
								for(var i=0; i<value.length; i++){ 
									if(value[i].checked) 
									s+=value[i].value+","; //如果选中，将value添加到变量s中 
								} 
				          		var userid=s;
				          		var instanceid=$("#instanceId").val();
				          		var actDefId=$("#actDefId").val();
				          		var dxlx="1";
				          		//执行短信发送
				          		$.ajax({
					   	          type: "POST",
					   	          url: "sx_zqb_hqdx.action",
					   	          data: {dxlx:dxlx,userid:userid,instanceid:instanceid,actDefId:actDefId},
					   	          dataType: "json",
					   	          success: function(data){ }
					   	         });
								//刷新办理列表
								try{
									window.parent.opener.reloadWorkList();
								}catch(err){}
								setTimeout('closeParentWin();',1000); 
							}else{ 
							 $("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送失败",1);
							}
						}else{
							$("#sendbtn").attr("disabled",false);
							art.dialog.tips("发送失败,请联系管理员",3);
						}
			     });
       }
       function closeParentWin(){
			try{
				window.parent.opener = null;
				window.parent.close();
			}catch(err){
			}
		}
</script>
<style type="text/css">
		.nextStep{
		font-size:16px;
		color:#0000FF;
		text-align: left;
		padding-left: 3px;
		padding-bottom: 10px;
		font-family:"黑体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-top:5px;
	}
	.ItemTitle{
		font-size:12px;
		color:#0000FF;
		text-align: right;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:800;
		line-height:12px;
		 white-space:nowrap;
	}
	.pageInfo{
		font-size:12px;
		color:#0000FF;
		text-align: left;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-right:20px;
	}
	.button{
		margin-top:10px;
		border-top:1px solid #990000;
		text-align:right;
		padding-top:20px;
		padding-right:20px;
		height:60px;
		vertical-align:middle;
	}
	#div{ width:450px; padding-top:0px; }
	ul,li{ list-style:none; padding:0; }
	li{ width:80px; float:left; margin:-1px 0 0 -1px;padding:0 2px; border:0px solid #000;line-height:25px}
</style>
</head>
 
<body > 
<s:form name="ifromMain" id="ifromMain" method="post" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0" >
			<tr>
				<td class="nextStep"><IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>加签完成，返回至：【<IMG alt="任务节点" src="iwork_img/domain.gif" border="0"/><s:property value="targetStepName"/>】</td>
			</tr>
			<tr>
				<td>
				<table width="100%"  border="0"  cellpadding="10" cellspacing="5">
					<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					<tr>
						<td  class="ItemTitle">返回加签人</td><td  class="pageInfo">
						<s:checkboxlist name="receiveUser" title="返回加签操作，不能取消接收人选项"  onclick="return false" list="receiveUser" listKey="userid" value="backUser" listValue="username"  theme="simple"></s:checkboxlist>
						</td>
					</tr>
					<tr>
							<td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px;color:#0000FF;" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-multibook">添加抄送人</a></td>
						</tr>
					<s:if test="RemindTypeList!=null">
					<tr>
						<td class="ItemTitle">提醒方式:	</td><td  class="pageInfo">
							 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
					         labelposition="top"
					         listKey="key"
					         listValue="value" 
					         value="remindTypeList"
					         >
					        </s:checkboxlist>
						</td>
					</tr>
					</s:if>
					<tr style="display:none"> 
						<td class="ItemTitle">优先级	</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr> 
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo">
							<table width="100%">
								<tr>
									<td width="5%">
										<s:textarea name="opinion" id="opinion" cssStyle="width:300px;height:80px;border:1px solid #ccc;overflow:auto;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
									</td>
									<td style="text-align:left;vertical-align:bottom">
										<div><a href="javascript:addAttach('attach','DIVATTACHMENT');" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-attach">附件上传</a>
										<input type=hidden size=100 id='attach'  class = '{maxlength:512}'  name='attach' value=''/>
										</div>
										<div><a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td></td><td style="text-align:left;padding-left:10px;">
								<div id='DIVATTACHMENT'>
									<s:property value="opinionAttachHtml" escapeHtml="false"/>
								</div>
							</td>
						</tr>
					</s:if>
					<tr>
				<td style="padding-right:10px" colspan="2">
					<input id="sendbtn" type="button" onclick="exexuteSendAction()" value="发送"   class="button_ send"/>
					<input type="button"  onClick="_close()" value="取消"  class="button_ close"/>
				</td>
			</tr>
				</table>
				</td>
			</tr>
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId"/>
			<s:hidden id ="targetStepName" name="targetStepName"/>
			<s:hidden id ="action" name="action"/>
					<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/>
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<s:hidden id ="backType" name="backType"/>
			
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
<script>
	$("#sendbtn").focus();
</script>