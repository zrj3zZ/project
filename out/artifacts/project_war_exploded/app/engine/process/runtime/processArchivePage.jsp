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
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<link href="iwork_css/button.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
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
           $(document).bind("contextmenu",function(e){
            //  return false;   
           });
			var actDefId = $("#actDefId").val();
			
			var prcDefId = $("#prcDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var backType = $("#backType").val();
			var instanceId = $("#instanceId").val();
			if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0||actDefId.indexOf("RCYWCB")==0){
	            $.ajax({
					type: "POST",
					url: "sx_zqb_XSBKFR.action",//获取被扣分人列表,操作dom使其显示在提交页面
					data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType,instanceId:instanceId},
					success: function(data){
						$("#checkBKFR").html(data);
					}
				});
			}else{
				$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
			}
		
			if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0||actDefId.indexOf("RCYWCB")==0){
				$("#KFBS").hide(); //董秘、督导提交公告不显示扣分项
				$.ajax({
					type: "POST",
					url: "sx_zqb_KFLX.action",//董秘、督导只显示扣分类型
					data: {},
					success: function(data){
					$("#checkKFLX").html(data);
					}
				});
			}else{
									
				/* document.getElementById("KFLX").style.display="none"; */$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
			}
				
				
		   if(actDefId.indexOf("ZZYJ")==0||actDefId.indexOf("ZZYJDGZFKHF")==0||actDefId.indexOf("ZZEJDGZFKHF")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isCcLastnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId},
					success: function(data){
						if(data!="false"){
							while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
						}
					}
			 	});
		   }
		 
		   if(actDefId.indexOf("GPECXXPL")==0||actDefId.indexOf("GPSCXXPL")==0||actDefId.indexOf("XMLXSPLC")==0||actDefId.indexOf("XMGGSH")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isGpLastnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId},
					success: function(data){
						if(data!="false"){
								while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
								var usercc= $("#ccUsers").val();
								if(usercc!=null&&usercc!=""){
								
								 $("#ccUsers").val(usercc+","+data);
								}else{
									while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							
							while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
							 }
						}
					}
			 	});
		   }
		   if(actDefId.indexOf("SGFAZLBS")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isSgLastnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId},
					success: function(data){
						if(data!="false"){
							while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
						}
					}
			 	});
		   }
		   if(actDefId.indexOf("XMLXNFX")==0||actDefId.indexOf("FAZLBS")==0||actDefId.indexOf("SBZL")==0||actDefId.indexOf("DZNHFK")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isDzLastnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId},
					success: function(data){
						if(data!="false"){
							while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
						}
					}
			 	});
		   }
		   if(actDefId.indexOf("DZXMLXEBYS")==0||actDefId.indexOf("GPFXEBYS")==0||actDefId.indexOf("SBNHEBYS")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isDzwLastnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId},
					success: function(data){
						if(data!="false"){
						while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
						}
					}
			 	});
		   }
		   
		   if(actDefId.indexOf("YBCWGZJDHB")==0||actDefId.indexOf("YBCWXMLX")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isYbcwLastnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId},
					success: function(data){
						if(data!="false"){
							while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
						}
					}
			 	});
		   }
		  
		    if(actDefId.indexOf("XMGGSH")==0||actDefId.indexOf("GPDJJGD")==0||actDefId.indexOf("XMNHSH")==0||actDefId.indexOf("GZFKJHF")==0||actDefId.indexOf("DZEJDGZFKHF")==0
				 ||actDefId.indexOf("SBNHEBYS")==0||actDefId.indexOf("ZZSBNH")==0){
		   	  
			   $.ajax({
					type: "POST",
					url: "isxmcsywbm.action", 
					data: {actDefId:actDefId},
					success: function(data){
						if(data!="false"){
								while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
								 var usercc= $("#ccUsers").val();
								if(usercc!=null&&usercc!=""){
								 $("#ccUsers").val(usercc+","+data);
								}else{
								while(true){
							var a=data.substring(0,1);
							if(a==","){
								data=data.substring(1,data.length);
							}else{
							break;
							}
							}
							 $("#ccUsers").val(data);
							 }
						}
					}
			 	});
		   }
			$.ajax({
				type: "POST",
				url: "sx_zqb_isShowKF.action",//董秘、督导提交公告提示的几个阶段不显示扣分信息
				data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType},
				success: function(data){
					if(data=="false"){
						$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
					}
				}
		 	});
       });
       function exexuteArchives(){
    	   var valid = mainFormValidator.form(); //执行校验操作
	   		if(!valid){
	   		return;
	   		}
    	     //根据流程判断是否执行扣分
    	    var actDef = $("#actDefId").val();
			if(actDef.indexOf("GGSPLC")==0||actDef.indexOf("CXDDYFQLC")==0||actDef.indexOf("RCYWCB")==0){
				 //查看被扣分人是否是空
				 var value = $("select").find("option:selected").val();
				 if(typeof(value)!= undefined  && value!=null){
				 var list = value.split("|"); 
	    		 var bkfr=list[1];
	    		 var id = $("#dataid").val();
				 var instanceId = $("#instanceId").val();
				 var actDefId = $("#actDefId").val();
	    		 if(!bkfr==null||!bkfr==""){
	    		    var kfbs = $("input[name='KFBS']:checked").val();
					var s=''; 
					var qttext ='';
					if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0){
						var obj=document.getElementsByName('Fruit'); //选择所有name="'test'"的对象，返回数组 
						//取到对象数组后，我们来循环检测它是不是被选中
						for(var i=0; i<obj.length; i++){ 
							if(obj[i].checked) 
							s+=obj[i].value+','; //如果选中，将value添加到变量s中 
						}
						s = s.substring(0, s.length-1);
						var qttext =  $("#text").val();
					}
					var seachUrl = encodeURI("zqb_sx_tjscoreset.action?id="+id+"&countdownscore="+kfbs+"&instanceid="+instanceId+"&actDefId="+actDefId+"&type="+s+"&memo="+qttext+"&bkfr="+bkfr);
					$.post(seachUrl,function(result){}); 
					}}
				}
       			 var obj = $('#ifromMain').serialize();
       				$("#sendbtn").attr("disabled",true);
			     $.post("processRuntimeExecuteArchives.action",obj,function(data){
			            var arr=data;
			             try{ 
			            	showSysTips();
			            }catch(e){}
						if(arr!=null){
							if(arr=="success"){
							    art.dialog.tips("发送成功",1); 
							   
								addDailyLog();
								
								//parent.lhgdialog.tips("已成功归档",2);
								//刷新办理列表
								try{
									
									window.parent.opener.reloadWorkList(); 
									
								}catch(err){
									/* setTimeout(closeParentWin(),1000);  */ 
									
									
									
									
								} 
								/* setTimeout(closeParentWin(),1000);  */ 
								setTimeout('window.parent.closeWin();',1000); 
								setTimeout('api.close();',1000);
								
							}else if(arr=="ERROR-10005"){ 
									$("#sendbtn").attr("disabled",false);
									art.dialog.tips("发送异常,系统参数异常<br/>(错误编号:ERROR-00005)",3);
								    //parent.lhgdialog.tips("发送异常,系统参数异常<br/>(错误编号:ERROR-00005)",3);
							}else{
									$("#sendbtn").attr("disabled",false);
								//parent.lhgdialog.tips("发送异常(错误编号:ERROR-00018)",3);
								art.dialog.tips("发送异常(错误编号:ERROR-00018)",3);
							}
						}else{
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
							//parent.lhgdialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
						}
			     });
       		//$('form').ajaxSubmit(submitOption);
       		
       }
       function addDailyLog(){
			var title = $("#ifromMain_title").val();
			var instanceId = $("#instanceId").val();
			var action = $("#action").val();
			var actStepDefId = $("#actStepDefId").val();
			var url = encodeURI("dg_zqb_process_addlog.action?title="+title+"&instanceId="+instanceId+"&action="+action+"&actStepDefId="+actStepDefId);
			$.post(url,function(data){
				
			});
		}
       function closeParentWin(){
			try{
				window.parent.opener = null;
				window.parent.close();
			}catch(err){
			}
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
	.trans_tip{
		padding:5px;
		border:1px solid #999999;
		background-color:#FFFFCC;
		margin-left:auto;
		margin-right:auto;
		color:red;
		height:250px;
		font-size:12px;
	}
	.trans_tip_title{
		font-size:12px;
		color:#666;
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
				<td  style="vertical-align:top;">
				<table width="100%"  border="0">
					<tr style="display:none">
						<td  class="ItemTitle">任务标题	</td><td  class="pageInfo"><s:textfield name="title" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					
					<!-- 判断是否允许抄送 -->
					 <s:if test="cslc=='DZSBNH'||cslc=='DZEJDGZFKWT'||cslc=='ZZYJDGZFKWT'||cslc=='ZZEJDGZFKWT'||cslc=='SGGZFKWTTZ'">  
					<tr>
					 <td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:500px;height:30px;" theme="simple"/></td>
				 </s:if><s:else>  
					<s:if test="ccinstal==1">
						<tr>
							<td  class="ItemTitle">抄送人	:</td><td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;"  class="easyui-linkbutton"  plain="true" iconCls="icon-multibook"></a></td>
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
				 </s:else> 
				<tr style="display:none"> 
						<td class="ItemTitle">优先级:</td><td  class="pageInfo"><s:radio  value="0" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
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
						<s:if test="backStepList!=null">
						<tr id="BKFR">
						<td class="ItemTitle">被减分人:</td><td class="pageInfo"><span id="checkBKFR"></td>
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
					</s:if>
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
				<s:if test="trans_tip!=''">
				<td style="vertical-align:top;padding:5px;padding-right:20px">
							<fieldset  class="trans_tip">
								<legend class="trans_tip_title">办理提示</legend>
									<s:property value="trans_tip" escapeHtml="false"/>
							</fieldset>
				</td>
				</s:if>
			</tr>
			
			<tr>
				<td style="padding-right:10px">
					<input id="sendbtn" type="button" onclick="exexuteArchives();" value="完成"   class="button_ archive"/>
					<input type="button"  onClick="_close()" value="取消"  class="button_ close"/>
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