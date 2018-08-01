<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>任务转发</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script> 
	<script type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
        $(document).ready(function(){
          
			$("#forwardUser").autocomplete({
				delay: 0,
				source:"user_load_autocomplete_json.action",
				minLength: 2
			});
       });
       
       function exexuteSendAction(){
       		var receiveUser = $("#receiveUser").val();
       		if(receiveUser == ''){
       			alert("请选择您要转发人员信息");
       			return false;
       		}
       		//
       		$("#sendBtn").addClass("ui-disabled");
       		$.ajax({
						url: 'processRuntimeExecuteForward.action', 
						data: $('form').serialize(), 
						type: "post",  
						cache : false, 
						success: function(data)  
						{
							if(data=='success'){
								alert('转发成功');
								backTodolist();
							}else if(data=='ERROR-10014'){
									alert('转发地址异常');
									$("#sendBtn").removeClass("ui-disabled");
							}else{
								alert('转发异常,请联系管理员');
								$("#sendBtn").removeClass("ui-disabled");
							}
							
						}
				}
       		);
       }
       //调用父页面脚本，关闭办理窗口
        function close(){
        	backTodolist();
       }
       	
		function dosearchAddress(fieldName,obj){
			if(obj.length>2){
				$.ajax({ 
						url: 'ifrom_mb_radio_address_search.action?fieldName='+fieldName+'&searchkey='+obj, 
						data: $('#iformMain').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data) 
						{
								$("#searchList").html( data );
						} 
					}
				);
			}
		}
		function setAddress(fieldname,obj){
			$('#'+fieldname).val(obj); 
			 $('.ui-dialog').dialog('close'); 
		}
</script>
<style type="text/css">
.pageInfo{
	text-align:left;
}
</style>
</head>

<body >
<s:form name="ifromMain" id="ifromMain" method="post" action="processRuntimeExecuteForward" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr>
			<td  class="pageInfo">
						<fieldset data-role="controlgroup">
							<legend>请选择您要转发的人:</legend>
						     	<table width="100%"><tr>
						     	<td><input type="text" name="receiveUser" id="receiveUser"></td>
						     	<td><a href="javascript:radio_book('receiveUser');" data-role="button"  data-inline="true" data-icon="search">地址簿</a></td>
						     	</tr></table> 
						</fieldset>
					</td>
			</tr>
	<!-- 判断是否允许抄送 --> 
					<s:if test="ccinstal==1">
						<tr>
							<td>
							<fieldset data-role="controlgroup">
							<legend>抄  送  人	:</legend>
							<table width="100%"  border="0"> 
								<tr>
									<td ><s:textfield  name="ccUsers" id="ccUsers"  theme="simple"></s:textfield></td>
									<td ><a href="ifrom_mb_radio_address_show.action?fieldName=ccUsers" data-role="button" data-rel="dialog" data-transition="slide" data-inline="true">地址簿</a></td>
								</tr>
							</table>
							</fieldset>
							
							</td>
						</tr>
						
					</s:if>
			<tr> 
						<td >
						<fieldset data-role="controlgroup" > 
							<legend>办理意见:</legend>
							<table width="100%">
								<tr> 
									<td>
										<s:textarea name="opinion" id="opinion"  value="%{opinion1}" cssStyle="height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
									</td>
									<td>
										<a href="weixin_process_opinionTemplate.action" data-role="button" data-rel="dialog" data-transition="slide" data-inline="true">意见</a>
									</td>
								</tr> 
							</table>
							
							</fieldset>
							</td>
						</tr>
			<!-- 判断提醒设置 --> 
					<s:if test="RemindTypeList!=null">
						<tr>
							<td>
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
				<td >
				<div data-role="footer" data-position="fixed" style="padding:10px;text-align:center">
				<a id="sendBtn" style="width:250px;height:30px;font-size:20px"   href="javascript:exexuteSendAction();" data-role="button" data-icon="check"  data-theme="a">发送</a>
		</div>
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
			<s:hidden id ="currentUser" name="currentUser"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<s:hidden id ="deviceType" name="deviceType"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
