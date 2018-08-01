<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:property value="targetStepName"  escapeHtml="false"/></title>
 <meta name="viewport" content="width=device-width, initial-scale=0.8, user-scalable=0">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script type="text/javascript">
        $(document).ready(function(){
        	showSysTips();
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
       		$("#sendbtn").attr("disabled",true);
       		if($("#opinion").val()==""){
    		   alert("请填写会签意见");
    		   $("#opinion").focus();
    		   $("#sendbtn").attr("disabled",false);
    		   return; 
    	   }
       		 var obj = $('#ifromMain').serialize();
			     $.post("processRuntimedoSign.action",obj,function(data){
			            var arr=data;
			             try{
			            	showSysTips();
			            }catch(e){}
						if(arr!=null){
							if(arr=="success"){
								alert("签批成功"); 
								//刷新办理列表 
								try{
									window.parent.opener.reloadWorkList();
								}catch(err){
									setTimeout('window.parent.closeWin();',1000); 
								}
								setTimeout('window.parent.closeWin();',1000); 
							}else{
								$("#sendbtn").attr("disabled",false);
								alert("发送失败,返回值异常(错误编号:ERROR-10016)");
							}
						}else{
							$("#sendbtn").attr("disabled",false);
							alert("发送失败,返回值异常(错误编号:ERROR-10016)");
						}
			     });
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
	.trans_tip{
		padding:5px;
		border:1px solid #999999;
		background-color:#FFFFCC;
		margin-left:auto;
		margin-right:auto;
		color:red;
		height:260px;
		font-size:12px;
	}
	.trans_tip_title{
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
					<tr>
					<td class="nextStep">
						<IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>流程会签
					</td>
				</tr>
					<s:if test="RemindTypeList!=null">
					<tr>
						<td class="ItemTitle">
						<fieldset data-role="controlgroup" >
							<legend>提醒方式:</legend>
						     	 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
								         labelposition="top"
								         listKey="key"
								         listValue="value" 
								         value="remindTypeList">
								        </s:checkboxlist>
						</fieldset>
						</td>
					</tr>
					</s:if>
					<tr style="display:none"> 
						<td class="ItemTitle">优先级:</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
						<tr> 
							<td  class="ItemTitle">
								<fieldset data-role="controlgroup" > 
								<legend>办理意见:</legend>
								<s:textarea name="opinion" id="opinion"  value="%{opinion1}" cssStyle="height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
								</fieldset>
							</td>
						</tr>
						<tr>
							<td></td><td style="text-align:left;padding-left:10px;">
								<div id='DIVATTACHMENT'>
									<s:property value="opinionAttachHtml" escapeHtml="false"/>
								</div>
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
			<tr>
				<td style="padding-right:10px">
					<input id="sendbtn" type="button" onclick="exexuteSendAction()" value="确认会签意见"   class="button_ send"/>
					<input type="button"  onClick="_close()" value="取消"  class="button_ close"/>
				</td>
			</tr>
		
		</table>
						
					<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="title" name="title"/>
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
<script>
	$("#sendbtn").focus();
</script>