<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script> 
<script type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/engine/process_page.js"></script>
<script type="text/javascript">
       function exexuteSendAction(){
    	   if($("#opinion").val()==""){
    		   art.dialog.tips("驳回时请填写您的驳回意见",2);
    		   $("#opinion").focus();
    		   return; 
    	   }
       		var obj = $('#ifromMain').serialize();
			     $.post("processRuntimeExecuteBackUp.action",obj,function(data){
			            var responseText=data;
			            try{
					    		showSysTips();
						 }catch(e){}
						if(responseText=="success"){
							art.dialog.tips("驳回成功");
							//刷新办理列表
							try{
								window.parent.opener.reloadWorkList();
							}catch(err){}
							setTimeout('window.parent.closeWin();',1000); 
						}else if(responseText=="ERROR-10005"){
							art.dialog.tips("参数异常，请联系管理员(错误号:ERROR-10005)");
						}else if(responseText=="ERROR-10006"){
							art.dialog.tips("驳回异常，请联系管理员(错误号:ERROR-10006)");
						}else{
							art.dialog.tips("驳回异常，请联系管理员(错误号:ERROR-10006)");
						}
			     });
       }
    
		//设置抄送列表
		function setCCList(str){
			 $("#ccUsers").val(str);
		}
       //调用父页面脚本，关闭办理窗口
        function close(){
        	parent.hiddenSenWindow();
       }
       function setParam(obj){
       		var value = obj.value;
       		var list = value.split("|");
       		if(list.length>0 ) {
       		 	$("#targetStepId").val(list[0]);
       		 	$("#reciverUserTip").text(list[1]);
       		 	$("#receiveUser").val(list[2]); 
       		 }
       }
</script>
</head>
<body > 
<s:form name="ifromMain" id="ifromMain" method="post" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep">
				<fieldset data-role="controlgroup" >
							<legend>请选择驳回至：</legend>
				<s:property value="backStepList" escapeHtml="false" />
				</fieldset>
				</td>
			</tr>
			<tr>
				<td>
				<table width="100%"  border="0" cellpadding="5" cellspacing="0">
					<tr style="display:none">
						<td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					<tr>
					<td  class="pageInfo">
							驳回给:<span id="reciverUserTip" style="font-size:20px;"></span>
						</td> 
					
					<!-- 判断是否允许抄送 -->
					<s:if test="ccinstal==1">
						<tr>
							<td>
							<table width="100%"  border="0"> 
								<tr>
									<td  class="ItemTitle">抄  送  人:</td> 
									<td  class="pageInfo"><s:textfield  name="ccUsers" id="ccUsers"  theme="simple"></s:textfield></td>
									<td  class="pageInfo"><a href="javascript:radio_book('ccUsers');" data-role="button"  data-inline="true" data-icon="search">地址簿</a></td>
								</tr>
							</table>
							</td>
						</tr>
					</s:if>
					
					<s:if test="opinionFlag==1">
						<tr>
						<td  class="pageInfo">
							<fieldset data-role="controlgroup"  >
					   		 <legend>办理意见:</legend>
								<s:textarea name="opinion" id="opinion" cssStyle="height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
								
							</fieldset>
							</td>
						</tr>
					</s:if>
					
					<s:if test="RemindTypeList!=null">
					<tr>
						<td  class="pageInfo">
					<fieldset data-role="controlgroup"   data-type="horizontal" data-mini="true">
					    <legend>是否通知所有已办理的用户</legend>
						<s:radio  value="1" list="#{'1':'是','0':'否'}"  id="isRemindHistoricUser" name="isRemindHistoricUser" theme="simple"/>
						</fieldset>
						</td>
					</tr>  
					<tr> 
						<td  class="pageInfo">
							<fieldset data-role="controlgroup" >
					    <legend>提醒方式:</legend>
							 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
					         labelposition="top"
					         listKey="key"
					         listValue="value" 
					         value="remindTypeList"
					         >
					        </s:checkboxlist>
					        </fieldset>
						</td>
					</tr>
					</s:if>
					<tr>
						<td  class="pageInfo">
							<fieldset data-role="controlgroup"  data-type="horizontal" data-mini="true" >
					   		 <legend>优先级:</legend>
						<s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/>
						</fieldset>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId"/>
			<s:hidden id ="receiveUser" name="receiveUser"/>
			<s:hidden id ="action" name="action"/> 
			<div data-role="footer" data-position="fixed" style="text-align:center;padding:10px;">
				<a id="sendBtn"   href="javascript:exexuteSendAction();"  data-iconpos="left" data-inline="true" data-role="button" data-icon="check"  data-theme="b"><span style="font-size:20px;height:30px;">　　　发　　　送　　　</span></a>
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
