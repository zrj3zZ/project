<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>操作提示</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/ui/base/jquery.ui.autocomplete.css">
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery-ui-1.8.16.custom.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js" ></script> 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
        $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
             // return false;   
           }); 
           /*
           if(document.addEventListener){//如果是Firefox
				document.addEventListener("keypress",execHandler, true);
		   }else{
		   		document.attachEvent("onkeypress",execHandler);
		   }*/
		   //录入提示
		   	function split( val ) {
				return val.split( /,\s*/ );
			}
			function extractLast( term ) {
				return split( term ).pop();
			}
		   $("#ccUsers").bind( "keydown", function( event ) {
				if ( event.keyCode === $.ui.keyCode.TAB &&
						$( this ).data( "autocomplete" ).menu.active ) {
					event.preventDefault();
				}
			}).autocomplete({
				source: function( request, response ) {
					$.getJSON( "user_load_autocomplete_json.action", {
						term: extractLast( request.term )
					}, response );
				},
				search: function() {
					var term = extractLast( this.value );
					if ( term.length < 2 ) {
						return false;
					}
				},
				focus: function() {
					return false;
				},
				select: function( event, ui ) {
					var terms = split( this.value );
					terms.pop();
					terms.push( ui.item.value );
					terms.push( "" );
					this.value = terms.join( ", " );
					return false;
				}
			});
       });
       function execHandler(evt){
		  if(evt.keyCode==13){
			exexuteSendAction();
			return false;
		  }
	    }
	    
		//执行发送动作
       function exexuteSendAction(){
       		//判断接收地址是否为空
       		var list = document.ifromMain.receiveUser;
       		if(typeof(list)=='undefined'){
       			alert('未找到接收人,请联系系统管理员');
       		}
			for (i=0;i<list.length;i++){ 
				if(list[i].value==''){
					continue;
				}
			} 
			 var obj = $('#ifromMain').serialize();
			     $.post("processBatchHandleSend.action",obj,function(data){
			            var arr=data;
			             try{
			            	showSysTips();
			            }catch(e){}
						if(arr!=null){
							if(arr==""){
								art.dialog.tips("发送成功",1);
								//刷新办理列表
								try{
									close();
								}catch(err){
								}
							}else{
								art.dialog.tips(arr,5);
							}
						}else{ 
							art.dialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
						}
			     });
       }
		//添加审核意见
		function addAuditOpinion(){
			    var pageUrl='processBatchShowOpinionList.action?code=1';
			     art.dialog.open(pageUrl,{
		    		id: "dg_addAuditOpinion" ,
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
		
		//设置参数
		function setParams(str,val){
			document.getElementById(str).value = val;
			try{
				document.getElementById(str).onchange();
			}catch(e){}
			return true;
			//alert(document.getElementById("receiveUser").value);
		}
       //调用父页面脚本，关闭办理窗口
        function close(){
        	api.close();
       }
        function showAddress(fieldname){
				var pageUrl = "processRuntimeRadioAddress_show.action?inputField="+fieldname;
					art.dialog.open(pageUrl,{
						id:'addressDialog', 
						title:"地址簿",
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width: 500,
						height: 510,
						close:function(){
							$("#sendbtn").focus();
						}
					 });
       }
</script>
<style type="text/css">
body{ 
		 	margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		 }
		.ui-menu-item{
			font-size:10px;
			color:#0000FF;
			background-color:#FFF;
			border-left:1px solid #f2f2f2;
			border-right:1px solid #f2f2f2;
			border-bottom:1px solid #f2f2f2;
			text-decoration:none;
			display:block;
			padding:.2em .4em;
			line-height:1.5;
			zoom:1;
			float: left;
			clear: left;
			width: 100%;
		}
		.ui-state-hover{
			font-size:10px;
			color:#0000FF;
			background-color:#EEEEEE;
			text-decoration:none;
			display:block;
			padding:.2em .4em;
			line-height:1.5;
			zoom:1;
			float: left;
			clear: left;
			width: 100%;
		}
	
	.ui-menu {
		list-style:none;
		padding: 2px;
		margin: 0;
		display:block;
		float: left;
	}
	.nextStep{
		font-size:16px;
		color:#0000FF;
		text-align: left;
		padding-left: 3px;
		font-family:"黑体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-bottom:5px;
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
		padding: 10px;
		font-family:"宋体";
		vertical-align:top;
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
	.task_title{
		font-size:12px;
		color:#666;
	}
</style>
</head>

<body >
<s:form name="ifromMain" id="ifromMain" method="post"  theme="simple">
<!-- TOP区 -->
<table width="100%"  border="0">
			<tr>
				<td>
				<table width="100%"  border="0">
				<s:iterator value="sendList" status="status">
				<tr>
				<td class="nextStep" colspan="2"><IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>任务流转至：【<IMG alt="任务节点" src="iwork_img/domain.gif" border="0"/><s:property value="targetStepName"  escapeHtml="false"/>】</td>
			</tr> 
						<s:property value="addressHTML" escapeHtml="false"/> 
					<!-- 办理参数 -->
				<s:hidden id ="targetStepId" name="targetStepId"/>
				<s:hidden id ="targetStepName" name="targetStepName"/>
					</s:iterator>
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
					<tr>
						<td class="ItemTitle">优先级:</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr>
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo"><textarea name="opinion" cols="" rows="" id="opinion" style="width:300px;height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></textarea>&nbsp;<a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></td>
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
			<tr style="display:none">
				<td> 
				
				<s:hidden id ="action" name="action"/>
				</td>
			</tr>
		
		</table>
		<div class="button">
						<a id="sendbtn" href="#" onclick="exexuteSendAction();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">发送</a>
						<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
					</div>
					<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="title" name="title"/>
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/> 
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
s			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
<script>
	$("#sendbtn").focus();
</script>