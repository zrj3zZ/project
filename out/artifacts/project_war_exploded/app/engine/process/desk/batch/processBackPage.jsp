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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/prc_ru_dialog.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
	  $(document).ready(function(){
           $(document).bind("contextmenu",function(e){   
              return false;   
           });
       });
       function exexuteSendAction(){
    	   if($("#opinion").val()==""){
    		   art.dialog.tips("驳回时请填写您的驳回意见",2);
    		   $("#opinion").focus();
    		   return; 
    	   }
       		var obj = $('#ifromMain').serialize();
			     $.post("processBatchBackSend.action",obj,function(data){
			            var responseText=data;
			            try{
					    		showSysTips();
						 }catch(e){}
						if(responseText==""){
							art.dialog.tips("驳回成功",2);
							//刷新办理列表
							try{
								close(); 
							}catch(err){}
						}else{
							art.dialog.tips(responseText,5);
						}
			     });
       }
     //添加审核意见
		function addAuditOpinion(){
				var pageUrl='processBatchShowOpinionList.action?code=1';
				 art.dialog.open(pageUrl,{
						id:'dg_addAuditOpinion',
						title:"办理意见", 
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:600,
					    height:410
					 });
		}
    	
        function addCCUsers(){ 
				var ccUsers = $("#ccUsers").val();
				art.dialog.data('ccUsers', ccUsers);// 存储数据
				var pageUrl = "processRuntimeCC.action?ccUsers="+encodeURI(ccUsers);
				 art.dialog.open(pageUrl,{
						id:'ccDialog',
						title:"添加抄送",
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:510
					 });
		}
		//设置抄送列表
		function setCCList(str){
			 $("#ccUsers").val(str);
		}
       //调用父页面脚本，关闭办理窗口
        function close(){
        	api.close();
       }
</script>
</head>
 
<body > 
<s:form name="ifromMain" id="ifromMain" method="post" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep"><IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>驳回至：【<IMG alt="任务节点" src="iwork_img/domain.gif" border="0"/><s:property value="targetStepName"/>】</td>
			</tr>
			<tr>
				<td>
				<table width="100%"  border="0" cellpadding="5" cellspacing="0">
					<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					<tr>
						<td  class="ItemTitle">驳回接收人	</td><td  class="pageInfo">
								<s:checkboxlist name="receiveUser" title="驳回操作，不能取消接收人选项" onclick="return false" list="backUserList" listKey="userid" value="backUser" listValue="username"  theme="simple"></s:checkboxlist>
						</td>
					
					<!-- 判断是否允许抄送 -->
					<s:if test="ccinstal==1">
						<tr>
							<td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px;color:#0000FF;" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-multibook">添加抄送人</a></td>
						</tr>
					</s:if>
					
					<s:if test="RemindTypeList!=null">
					<tr>
						<td  class="ItemTitle" >是否通知所有已办理的用户	</td><td  class="pageInfo">
						<s:radio  value="1" list="#{'1':'是','0':'否'}"  id="isRemindHistoricUser" name="isRemindHistoricUser" theme="simple"/>
						</td>
					</tr>  
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
					<tr>
						<td class="ItemTitle">优先级	</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr>
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo"><s:textarea name="opinion" id="opinion" cssStyle="width:300px;height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>&nbsp;<a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></td>
						</tr>
					</s:if>
					<tr> 
						<td colspan="2" class="task_title">
							<fieldset >
								<legend>批量办理任务</legend>
								<s:property value="content" escapeHtml="false"/>
							</fieldset>
						 </td>
					</tr>  
				</table>
				</td>
			</tr>
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId"/>
			<s:hidden id ="action" name="action"/> 
		<div class="button">
						<a href="#" onclick="exexuteSendAction();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">发送</a>
						<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
					</div>
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
			
			
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
