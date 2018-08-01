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
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
        $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
             // return false;   
           });
       });
		  function addCCUsers(){ 
				var ccUsers = $("#ccUsers").val();
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
       function exexuteArchives(){
       			 var obj = $('#ifromMain').serialize();
			     $.post("processBatchArchiveSend.action",obj,function(data){
			            var arr=data;
			             try{ 
			            	showSysTips();
			            }catch(e){}
						if(arr!=null){
							if(arr==""){
								art.dialog.tips("已成功归档",2);
								//刷新办理列表
								try{
									close();
								}catch(err){} 
							}else{
							art.dialog.tips(arr,5); 
							}
						}else{
							art.dialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
						}
			     });
       		//$('form').ajaxSubmit(submitOption);
       }
       //添加审核意见
		function addAuditOpinion(){
				var pageUrl='processBatchShowOpinionList.action?code=1';
				art.dialog.open(pageUrl,{
						id:'ccDialog',
						title:"办理意见", 
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:600,
						height:410
					 });
		}
     	function showOpinionContent(){
     		var obj={actDefId:$("#actDefId").val(),prcDefId:$("#prcDefId").val(),taskId:$("#taskId").val()};
     		 $.post("processRuntimegetOpinionContent.action",obj,function(data){ 
		            if(data!=""){ 
		            	$("#opinion").val(data);
		            }
		     });
     	}
       //调用父页面脚本，关闭办理窗口
        function close(){
        	api.close();
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
				<td>
				<table width="100%"  border="0">
					<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					
					<!-- 判断是否允许抄送 -->
					<%-- <s:if test="cslc=='DZSBNH'||cslc=='DZEJDGZFKWT'||cslc=='ZZYJDGZFKHF'||cslc=='ZZEJDGZFKWT'||cslc=='SGGZFKWTTZ'"> --%>
					<tr>
					<td  class="ItemTitle">通知人	:</td><td  class="pageInfo"><input type="text" name="ccUsers" id="ccUsers" cssStyle="width:300px" theme="simple"
					
					value="<s:property value="ccUsers" />"/></td>
					<%-- </s:if><s:else> --%>
					
					
					<s:if test="ccinstal==1">
						<tr>
							<td  class="ItemTitle">通知人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;"  class="easyui-linkbutton"  plain="true" iconCls="icon-multibook"></a></td>
						</tr> 
						<s:if test="RemindTypeList!=null">
						<tr>
							<td class="ItemTitle">提醒方式:	</td><td  class="pageInfo">
								 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" value="remindTypeList" 
						         labelposition="top"
						         listKey="key"
						         listValue="value" >
						        </s:checkboxlist>
							</td>
						</tr>
						</s:if> 
					</s:if>
					<tr>
						<td class="ItemTitle">优先级:</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr> 
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo"><s:textarea name="opinion" id="opinion" cssStyle="width:300px;height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>&nbsp;<a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></td>
						</tr>
					</s:if>
					<%-- </s:else> --%>
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
			<s:hidden id ="targetStepName" name="targetStepName"/>
			<s:hidden id ="action" name="action"/> 
		<div class="button">
						<a href="#" onclick="exexuteArchives();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">完成</a>
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
