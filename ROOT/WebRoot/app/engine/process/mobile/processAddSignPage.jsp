<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script> 
<script type="text/javascript" src="iwork_js/mobile/iOSJavascriptBridge.js"></script>
<script type="text/javascript" src="iwork_js/mobile/sys_mobile_adapter.js"></script> 
<script type="text/javascript">
       $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
              return false;   
           });
        });
         
		
       function exexuteAddSign(){
       		var receiveUser = $("#receiveUser").val();
       		if(receiveUser == ''){
       			showTip("请选择您要加签人员信息",2);
       			return false;
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
								showTip('加签成功');
								backTodolist();
							}else if(data=='ERROR-10014'){
								showTip('加签地址异常');
									$("#sendBtn").removeClass("ui-disabled");
							}else if(data=='ERROR-10002'){
								showTip('系统无法将任务加签给自己');
									$("#sendBtn").removeClass("ui-disabled");
							}else{
								showTip('加签异常,请联系管理员');
								$("#sendBtn").removeClass("ui-disabled");
							}
							
						}
				}
       		);
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
								$("#searchLister").append(data);
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
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
<s:form name="ifromMain" id="ifromMain" method="post" action="processRuntimeExecuteAddSign" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
			<tr>
				<td>
				<table width="100%"  border="0">
					<tr>
						<td  class="pageInfo">
						<table width="100%"  border="0"> 
								<tr>
									<td  class="ItemTitle">加签人	:</td>
									<td  class="pageInfo"></td>
								</tr>
								<tr>
									<td  class="pageInfo"><s:textfield  name="receiveUser" id="receiveUser"  theme="simple"></s:textfield></td>
									<td  class="pageInfo"><a href="ifrom_mb_radio_address_show.action?fieldName=receiveUser" data-role="button" data-rel="dialog" data-transition="slide" data-inline="true">地址簿</a></td>
								</tr>
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
						<fieldset data-role="controlgroup">
							<legend>优先级:</legend>
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
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId" value="999999"/>
			<s:hidden id ="targetStepName" name="targetStepName" value="加签"/>
			<div data-role="footer" data-position="fixed" style="padding:10px;text-align:center">
				<a id="sendBtn" style="width:250px;height:30px;font-size:20px"   href="javascript:exexuteAddSign();" data-role="button" data-icon="check"  data-theme="a">发送</a>
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
			<s:hidden id ="action" name="action" value="送加签"/> 
			<s:hidden id ="deviceType" name="deviceType"/>
			<s:hidden name="opinion" value="%{opinion1}"></s:hidden>  
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
