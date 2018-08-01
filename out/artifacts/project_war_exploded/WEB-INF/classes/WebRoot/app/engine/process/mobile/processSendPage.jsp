<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>操作提示</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script> 
	<script type="text/javascript" src="iwork_js/mobile/iOSJavascriptBridge.js"></script>
	<script type="text/javascript" src="iwork_js/mobile/sys_mobile_adapter.js"></script> 
<script type="text/javascript">    
		//执行发送动作
       function exexuteSendAction(){
       		//判断接收地址是否为空
       		var list = document.ifromMain.receiveUser;
       		if(typeof(list)=='undefined'){ 
       			showTip('未找到接收人,请联系系统管理员');
       			return;
       		}
       		if(typeof(list.length)=='undefined'){
       			if(list.value==""){
       				showTip('未找到接收人,请联系系统管理员');
           			return;
       			}
       		}else{
       			for (i=0;i<list.length;i++){ 
    				//alert(list[i].value);
    				if(list[i].value==''){
    				 alert(list);
    					continue;
    				}
    			}
       		}
			$("#sendBtn").addClass("ui-disabled");
			$.ajax({ 
				url: 'processRuntimeExecuteHandle.action', 
				data: $('form').serialize(), 
				type: "post", 
				cache : false, 
				success: function(data) 
				{ 
						if(data=='success'){
							showTip('办理完毕');
							backTodolist();
						}else{
							showTip('办理异常,请检查接收人');
						}
				}
			}); 
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
							if(data==""){
								$("#searchLister").html("<li>未发现账户地址</li>");
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}else{
								$("#searchLister").html(data);
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}
								
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
table td{
white-space: nowrap;
}
.ItemTitle{
	text-align:left;
	width:80px;
	color:#666;
}
.pageInfo{
	text-align:left;
}
.pageInfo ul{
	padding-left:0px;
}
.button{
	padding:2px;
}
</style>
</head>
<body >
<s:form name="ifromMain" id="ifromMain" method="post" action="processRuntimeExecuteHandle" theme="simple">
<!-- TOP区 -->
		<s:iterator value="sendList" status="status">
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep"><IMG alt="任务节点"  style="margin:3px;"  src="iwork_img/aol.png" border="0"/>发送至：【<s:property value="targetStepName"  escapeHtml="false"/>】</td>
			</tr>
			<tr>
				<td>
				<table width="100%"  border="0">
					<s:property value="addressHTML" escapeHtml="false"/> 
				</table>
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
						<td  class="pageInfo">
						<table>
							<tr>
								<td>优先级:</td>
								<td>
								<fieldset data-role="controlgroup" data-type="horizontal" >
						            <input type="radio" name="Priority" id="radio-choice-1" value="1" />
							     	<label for="radio-choice-1">加急</label>
							     	<input type="radio" name="Priority" id="radio-choice-0" value="0"  checked="checked" />
							     	<label for="radio-choice-0">普通</label>
						            </fieldset>
								</td>
							</tr>
						</table>
					</td>
					</tr>
					<s:if test="isOpinion==1">
						<tr> 
						<td >
						<fieldset data-role="controlgroup" >
							<legend>办理意见:</legend>
							<s:textarea name="opinion" id="opinion"  value="%{opinion1}" cssStyle="width:300px;height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
							</fieldset>
							</td>
						</tr>
					</s:if>
			</table>
			<div style="display:none">
			<!-- 办理参数 -->
			<s:textfield id ="targetStepId" name="targetStepId"/>
			<s:textfield id ="targetStepName" name="targetStepName"/>
			</div>
		</s:iterator>
		<div data-role="footer" data-position="fixed" style="padding:10px;text-align:center">
				<a id="sendBtn" style="width:250px;height:30px;font-size:20px"  href="javascript:exexuteSendAction();" data-role="button" data-icon="check"  data-theme="a">发送</a>
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
			<s:hidden id ="title" name="title"/>
			<s:hidden id ="deviceType" name="deviceType"/> 
			<s:hidden id ="action" name="action"/>
			<s:hidden name="opinion" value="%{opinion1}"></s:hidden> 
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
