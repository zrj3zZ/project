<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/prc_ru_dialog.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<link href="iwork_css/button.css" rel="stylesheet" type="text/css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/engine/process_page.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
	$.ajaxSetup({
        async: false
    });
	mainFormValidator =  $("#ifromMain").validate({});
	mainFormValidator.resetForm();
});
var api = art.dialog.open.api, W = api.opener;
	   $(document).ready(function(){
	   		var actDefId = $("#actDefId").val();
	   	 var prcDefId = $("#prcDefId").val();
		 var actStepDefId = $("#actStepDefId").val();
		 var taskId = $("#taskId").val();
		 var backType = $("#backType").val();
		 var instanceId = $("#instanceId").val();
	   		
	   		$("#KFLX").hide();
	   		$.ajax({
				type: "POST",
				url: "sx_zqb_isShowKF.action",
				data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType},
				success: function(data){
					if(data=="false"){
						$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
					}else{
						sxkf(actDefId,prcDefId,actStepDefId,taskId,backType,instanceId);
					}
				}
		 	});
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
		 	if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0||actDefId.indexOf("RCYWCB")==0){
	   			$("#KFBS").hide();
				$.ajax({
					type: "POST",
					url: "sx_zqb_KFLX.action",
					data: {},
					success: function(data){
					$("#checkKFLX").html(data);
					}
				});
			}else{
			
				/* document.getElementById("KFLX").style.display="none"; */$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
			}
	   });
	   var flag =0;
	   function sxkf(actDefId,prcDefId,actStepDefId,taskId,backType,instanceId){
    	   $.ajax({
				type: "POST",
				url: "sxkf_isShowKF.action",//董秘、督导提交公告提示的几个阶段不显示扣分信息
				data: {actDefId:actDefId},
				success: function(data){
					if(data=="jffs"){
						flag=1;
						$("#KFBS").show();$("#KFLX").hide();$("#BKFR").show();
					}else if(data=="jflx"){
						flag=1;
						$("#KFBS").hide();$("#KFLX").show();$("#BKFR").show();
					}
				}
		 	});
       }
       function exexuteSendAction(){
    	   var valid = mainFormValidator.form(); //执行校验操作
	   		if(!valid){
	   		return;
	   		}
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
							art.dialog.tips("驳回成功",2);
							 //获取被驳回人信息
				    		   var actDefId = $("#actDefId").val();
				    		   var instanceId = $("#instanceId").val();
				    		   var value = $("select").find("option:selected").val();
				          		var list = value.split("|");
				          		 var userid=list[2];
				          		 //回传被驳回人账号
					           $.ajax({
					   	          type: "GET",
					   	          url: "sx_zqb_obtainUserid.action",
					   	          data: {userid:userid,actDefId:actDefId,instanceid:instanceId},
					   	          dataType: "json",
					   	          success: function(data){ }
					   	         });
					   	        var id = $("#dataid").val();							 
								var kfbs = $("input[name='KFBS']:checked").val();
								var s=''; 
								var qttext ="";//其他扣分类型的输入框
								var obj=document.getElementsByName('Fruit'); //选择所有name="'test'"的对象，返回数组
				          		//执行扣分操作  
								if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0||actDefId.indexOf("RCYWCB")==0){
								 	qttext =$("#text").val();//其他扣分类型的输入框
									for(var i=0; i<obj.length; i++){ 
										if(obj[i].checked) 
										s+=obj[i].value+','; //如果选中，将value添加到变量s中 
									}
									s = s.substring(0, s.length-1);									
								}
									var seachUrl = encodeURI("zqb_sx_tjscoreset.action?id="+id+"&countdownscore="+kfbs+"&instanceid="+instanceId+"&actDefId="+actDefId+"&type="+s+"&memo="+qttext+"&bkfr="+userid);
									$.post(seachUrl,function(result){});
							//刷新办理列表
							
							try{
								window.parent.opener.reloadWorkList();
							}catch(err){}
							setTimeout('window.parent.closeWin();',1000);
						}else if(responseText=="ERROR-10005"){
							art.dialog.tips("参数异常，请联系管理员(错误号:ERROR-10005)",2);
						}else if(responseText=="ERROR-10006"){
							art.dialog.tips("驳回异常，请联系管理员(错误号:ERROR-10006)",2);
						}else{
							art.dialog.tips("驳回异常，请联系管理员(错误号:ERROR-10006)",2);
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
       function setParam(obj){
    	   var actDefId = $("#actDefId").val();
       		var value = obj.value;
       		var list = value.split("|");
       		if(list.length>0 ) {
       		 	$("#targetStepId").val(list[0]);
       		 	$("#reciverUserTip").text(list[1]);
       		 	$("#receiveUser").val(list[2]); 
       		    var receiveUser = list[2]; 
       		    if(flag==0){
	    		    $.ajax({
	    				type: "POST",
	    				url: "zqb_bh_isShowKF.action",
	    				data: {receiveUser:receiveUser},
	    				success: function(data){
	    					if(data=="false"){
	    						//$("#KFBS").hide();
	    						$("#KFLX").hide();//$("#BKFR").hide();
	    					}else{
	    						//$("#KFBS").show();
	    						$("#KFLX").show();//$("#BKFR").show();
	    					}
	    				}
	    		 	});
       		    }else{
       		    	$.ajax({
        				type: "POST",
        				url: "zqb_bh_isShowKF.action",
        				data: {receiveUser:receiveUser,actDefId:actDefId},
        				success: function(data){
        					if(data=="false"){
        						$("#KFBS").hide();
        						$("#BKFR").hide();
        						
        					}else{
        						sxkfs();
        					}
        				}
        		 	});
       		    }
       		 }
       }
  function sxkfs(){
    	   
    	   $.ajax({
				type: "POST",
				url: "sxkf_isShowKF.action",//董秘、督导提交公告提示的几个阶段不显示扣分信息
				data: {actDefId:actDefId},
				success: function(data){
					if(data=="jffs"){
						$("#KFBS").show();$("#KFLX").hide();$("#BKFR").show();
					}else if(data=="jflx"){
						$("#KFBS").hide();$("#KFLX").show();$("#BKFR").show();
					}
				}
		 	});
       }
       
</script>
<style type="text/css">
.ItemTitle {
    color: #0000ff;
    font-family: "宋体";
    font-size: 12px;
    font-weight: 800;
    line-height: 12px;
    padding-left: 10px;
    text-align: right;
    vertical-align: middle;
    white-space: nowrap;
    word-break: break-all;
    word-wrap: break-word;
    width:30%;
}
.pageInfo {
    color: #0000ff;
    font-family: "宋体";
    font-size: 12px;
    font-weight: 500;
    line-height: 12px;
    padding-left: 10px;
    padding-right: 20px;
    text-align: left;
    vertical-align: middle;
    word-break: break-all;
    word-wrap: break-word;
    width:70%;
}
</style>
</head>
 
<body > 
<s:form name="ifromMain" id="ifromMain" method="post" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep"><IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>驳回至：【<s:property value="backStepList" escapeHtml="false" />】</td>
			</tr>
			<tr>
				<td>
				<table width="100%"  border="0" cellpadding="5" cellspacing="0">
					<tr style="display:none">
						<td  class="ItemTitle" style="width:300px;">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					<tr>
						<td  class="ItemTitle" style="width:300px;">驳回人	</td><td  class="pageInfo">
							<span id="reciverUserTip"></span>
						</td> 
					
					<!-- 判断是否允许抄送 -->
					<s:if test="ccinstal==1">
						<tr>
							<td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px;color:#0000FF;" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-multibook">添加抄送人</a></td>
						</tr>
					</s:if>
					
					<s:if test="RemindTypeList!=null">
					<tr>
						<td  class="ItemTitle" >是否通知所有已办理的用户	</td><td  class="pageInfo">
						<s:radio  value="1" list="#{'1':'是','0':'否'}"  id="isRemindHistoricUser" name="isRemindHistoricUser" theme="simple"/>
						</td>
					</tr>  
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
						<td class="ItemTitle">优先级	</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr> 
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo">
							<table width="100%">
								<tr>
									<td width="5%">
										<s:textarea name="opinion" id="opinion" class='{string:true,maxlength:2000}' cssStyle="width:300px;height:80px;border:1px solid #ccc;overflow:auto;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
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
						<tr id="KFLX">
							<td class="ItemTitle">减分类型:</td><td class="pageInfo"><span id="checkKFLX"></td>
						</tr>
						<tr id="KFBS">
							<td  class="ItemTitle" >减分:</td><td  class="pageInfo">
							0.1<input type="radio" name="KFBS" value="0.1">
							0.3<input type="radio" name="KFBS" value="0.3">
							0.5<input type="radio" name="KFBS" value="0.5">
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
				</table>
				</td>
			</tr>
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId"/>
			<s:hidden id ="receiveUser" name="receiveUser"/>
			<s:hidden id ="action" name="action"/> 
		<div class="button">
						<a href="#" onclick="exexuteSendAction();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">发送</a>
						<a href="javascript:_close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
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
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
    	  var patrn=/[`~!#$^&*+<>?"{};'[\]\\]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>