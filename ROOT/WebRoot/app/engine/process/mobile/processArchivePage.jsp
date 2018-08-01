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
           $('form').submit(function() { 
					var alertMsg='';
					$.ajax({ 
						url: 'processRuntimeExecuteArchives.action', 
						data: $('form').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data)  
						{ 
							showTip('归档完毕');
							$("#sendBtn").removeClass("ui-disabled");
							backTodolist();
						}
					}
				); 
				return false; 
			}); 
       });
       function exexuteArchives(){
       		$("#sendBtn").addClass("ui-disabled");
       		$('form').attr('action','processRuntimeExecuteArchives.action');
       		$('form').submit();
       }
       //调用父页面脚本，关闭办理窗口
        function close(){
        	parent.hiddenSenWindow();
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
	.StepTitle{
		font-size:13px;
		color:#666;
		text-align: right;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		line-height:16px;
		font-weight:800;
		white-space:nowrap;
		background-color:#F5F8F7;
		 text-align:left;
		 border-bottom:1px solid #EFEFEF
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
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep"><IMG alt="任务节点"  style="margin:3px;"  src="iwork_img/aol.png" border="0"/>完成审批</td>
			</tr>
			<tr>
					<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td>
					</tr>
					<tr style="display:none">
						<td  class="pageInfo"><s:textfield name="title" theme="simple"/></td>
					</tr> 
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
					<td >
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
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId"/>
			<s:hidden id ="targetStepName" name="targetStepName"/> 
			
			<div data-role="footer" data-position="fixed" style="padding:10px;height:50px;text-align:center">
				<input type="button" value="　完　　成　" onClick="exexuteArchives()" style="width:50px;height:30px;font-size:20px" />
<!-- 				<a id="sendBtn" style="width:250px;height:30px;font-size:20px"   href="javascript:exexuteArchives();"   data-role="button" data-icon="check"  data-theme="a">发送</a>
 -->		</div>
		
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
			<s:hidden id ="deviceType" name="deviceType"/>
			<s:hidden id ="action" name="action"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
