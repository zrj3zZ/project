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
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
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
<script type="text/javascript" src="iwork_js/engine/process_page.js"></script>
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
				//alert(list[i].value);
				if(list[i].value==''){
					continue;
				}
			} 
			 var obj = $('#ifromMain').serialize();
			     $.post("processRuntimeExecuteHandle.action",obj,function(data){
			            var arr=data;
			             try{
			            	showSysTips();
			            }catch(e){}
						if(arr!=null){
							if(arr=="success"){
								art.dialog.tips("发送成功",1);
								//刷新办理列表
								try{
									window.parent.opener.reloadWorkList();
								}catch(err){
									setTimeout('closeParentWin();',1000); 
								}
								setTimeout('closeParentWin();',1000); 
							}else if(arr=="ERROR-10014"){ 
								art.dialog.tips("发送异常,任务接收人中包含了非法账号<br/>(错误编号:ERROR-00014)",3);
							}else if(arr=="ERROR-10015"){
								art.dialog.tips("发送异常,任务接收人地址异常<br/>(错误编号:ERROR-00015)",3); 
							}else if(arr=="ERROR-10005"){ 
								art.dialog.tips("发送异常,系统参数异常<br/>(错误编号:ERROR-00005)",3);
							}else if(arr=="ERROR-10017"){  
								art.dialog.tips("发送异常,接收人地址未填写完整，请检查<br/>(错误编号:ERROR-00017)",3);
							}else{
								art.dialog.tips("发送异常(错误编号:ERROR-00018)",3);
							}
						}else{
							art.dialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
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
        	parent.hiddenSenWindow();
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
	.item_memo{
		text-align:left;
		padding-left:60px;
		font-size:14px;
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
					<td class="nextStep" colspan="2"><img src="iwork_img/parallel.gateway.png"  height="25" alt="发送汇合节点" />任务流转至并签汇聚</td>
				</tr> 
				<tr>
					<td colspan="2">
					
					</td>
				</tr>  
				 	<s:property value="addressHTML" escapeHtml="false"/> 
					
					<!-- 办理参数 -->
				<s:hidden id ="targetStepId" name="targetStepId"/>
				<s:hidden id ="targetStepName" name="targetStepName"/>
					</s:iterator>
					<s:if test="ccinstal==1">
						<tr>
							<td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px;color:#0000FF;" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-multibook">地址簿</a></td>
						</tr>
					</s:if>
					<s:if test="isOpinion==1">
						<tr>
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo"><s:textarea name="opinion" id="opinion" cssStyle="width:300px;height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>&nbsp;<a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></td>
						</tr>
					</s:if>
				</table>
				</td>
			</tr>
			<tr>
					<td class="item_memo" colspan="2">
					<img src="iwork_img/bullet2.gif" border="0" alt="提示" />当前任务发送后，需等待最后的并签办理人，一并发送至目标办理人
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
			<s:hidden id ="Priority" name="Priority" value="0"/>
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