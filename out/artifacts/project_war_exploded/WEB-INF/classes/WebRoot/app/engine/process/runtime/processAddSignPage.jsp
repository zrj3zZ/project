<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<link href="iwork_css/button.css" rel="stylesheet" type="text/css"/>
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
          var cache = {},
			lastXhr;
			$("#receiveUser").autocomplete({
				minLength: 2,
				source: function( request, response ) {
					var term = request.term;
					if ( term in cache ) {
						response( cache[ term ] );
						return;
					}
					lastXhr = $.getJSON("user_load_autocomplete_json.action", request, function( data, status, xhr ) {
						cache[ term ] = data;
						if ( xhr === lastXhr ) {
							response( data );
						}
					});
				},parse: function(data) {
			       var rows = [];
			       for(var i=0; i<data.length; i++){
			        rows[rows.length] = { 
			          data:data[i].label, 
			          value:data[i].label, 
			          result:data[i].label 
			          }; 
			        }
			     return rows;
			       },
			     formatItem: function(row, i, n) {
			        return row;      
			    }
			});
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
			var taskId = $("#taskId").val();
		    $.ajax({
				type: "POST",
				url: "zqb_process_priority.action",//设置当前任务优先级
				data: {taskId:taskId},
				success: function(data){
					if(data!=null&&data!=""){
						if($("#Priority0").val()==data)
							$("#Priority0").attr("checked",true);
						if($("#Priority1").val()==data)
							$("#Priority1").attr("checked",true);
					}else{
						if($("#Priority0").val()==data)
							$("#Priority0").attr("checked",true);
					}
				}
		 	});
       });
       
       		//录入提示
		   	function split( val ) {
				return val.split( /,\s*/ );
			}
			function extractLast( term ) {
				return split( term ).pop();
			}
       function exexuteAddSign(){
       		 var obj = $('#ifromMain').serialize();
       		 $("#sendbtn").attr("disabled",true);
			     $.post("processRuntimeExecuteHandle.action",obj,function(data){
			            var arr=data;
			            //获取消息
			            try{
						    showSysTips();
						 }catch(e){}        	
						if(arr!=null){
							if(arr=="success"){
								art.dialog.tips("发送成功",1);
								//刷新办理列表
								try{
									window.parent.opener.reloadWorkList();
								}catch(err){} 
								setTimeout('closeParentWin();',1000); 
							}else if(arr=="ERROR-10014"){
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips.tips("发送异常,任务接收人中包含了非法账号<br/>(错误编号:ERROR-00014)",3);
							}else if(arr=="ERROR-10015"){
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,任务接收人地址异常<br/>(错误编号:ERROR-00015)",3); 
							}else if(arr=="ERROR-10005"){ 
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,系统参数异常<br/>(错误编号:ERROR-00005)",3);
							}else if(arr=="ERROR-10017"){  
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,接收人地址未填写完整，请检查<br/>(错误编号:ERROR-00017)",3);
							}else{
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常(错误编号:ERROR-00018)",3);
							}
						}else{
							$("#sendbtn").attr("disabled",false);
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
        function radio_book(defaultField) {	  	
			var url = "radiobook_index.action?1=1"; 
			//获得input内容
			var v = document.getElementById(defaultField);
			if(v.value!=""){
				var val  = encodeURI(v.value);
				url+="&input="+val+""; 
			}
			if(defaultField!=''){
				url+="&defaultField="+defaultField;
			}
			 art.dialog.open(url,{
				 id:"radioBookDialog",
					title: '单选地址簿', 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width: 350,
					height: 550
				 });
		}
</script>
</head>
<body >
<s:form name="ifromMain" id="ifromMain" method="post"  theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep"><IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>请选择加签人</td>
			</tr>
			<tr>
				<td>
				<table width="100%"  border="0">
					<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					<tr>
						<td  class="ItemTitle">加签人	:</td><td  class="pageInfo"><s:textfield name="receiveUser" id="receiveUser" cssStyle="width:300px" theme="simple"/><a id='radioBtnhtml' href="###" title="单选地址薄" onclick="radio_book('receiveUser');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a>
</td>
					</tr>
					<tr>
							<td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px;color:#0000FF;" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-multibook">地址簿</a></td>
					</tr>
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
					<tr style="display:none"> 
						<td class="ItemTitle">优先级:	</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr> 
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo">
							<table width="100%">
								<tr>
									<td width="5%">
										<s:textarea name="opinion" id="opinion" cssStyle="width:300px;height:80px;border:1px solid #ccc;overflow:auto;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
									</td>
									<td style="text-align:left;vertical-align:bottom">
										<div><a href="javascript:addAttach('attach','DIVATTACHMENT');" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-attach">附件上传</a>
										<input type=hidden size=100 id='attach'  class = '{maxlength:512}'  name='attach' value=''/>
										</div>
										<div><a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td></td><td style="text-align:left;padding-left:10px;">
								<div id='DIVATTACHMENT'>
									<s:property value="opinionAttachHtml" escapeHtml="false"/>
								</div>
							</td>
						</tr>
					</s:if>
					<tr>
						<td style="padding-right:10px;text-align:right" colspan="2">
							<input id="sendbtn" type="button" onclick="exexuteAddSign()" value="发送"   class="button_ send"/>
							<input type="button"  onClick="_close()" value="取消"  class="button_ close"/>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId" value="999999"/>
			<s:hidden id ="targetStepName" name="targetStepName" value="加签"/>
			<s:hidden id ="action" name="action"/>
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
<script>
	$("#sendbtn").focus();
</script>