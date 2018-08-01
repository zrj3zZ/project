<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-节点基本信息</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>	
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style> 
		<!--
			#header { background:#6cf;}
			#title { height:20px; background:#EFEFEF; border-bottom:1px solid #990000; font:12px; font-family:宋体; padding-left:5px; padding-top:5px;}
			#baseframe { margin:0px;background:#FFFFFF; border:1px solid #CCCCCC;}
			#baseinfo {background:#FFFFFF; padding:5px;font:12px; font-family:宋体;}
			.toobar{
				 border-bottom:1px solid #990000; 
				 padding-bottom:5px; 
			}
			/*只读数据样式*/
			.readonly_data {
				vertical-align:bottom;
				font-size: 12px;
				line-height: 20px;
				color: #888888;
				padding-right:10px;
				border-bottom:1px #999999 dotted;
				font-family:"宋体";
				line-height:15px;
			}
			.table_form{
				font-family:"宋体";
				font-size: 12px;
			}
			legend{
			    font-family:"宋体";
				font-size: 12px;
			}
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				border-bottom:1px solid #efefef;
				vertical-align:top;
				font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data {
				color:#0000FF;
				line-height: 23px;
				text-align: left;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px solid #efefef;
				vertical-align:top;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				
			}
			*{font-family:Verdana;font-size:96%;}
           label.error{float:none;color:red;padding-left:.5em;}
           p{clear:both;}
           .submit{margin-left:12em;}
           em{font-weight:bold;}
		-->
</style>
	<script type="text/javascript">
		//提交保存
		$().ready(function() { 
			$("#editForm").validate({
			debug:false,meta:"validate",
				submitHandler:function(form){
					var url = "sysFlowDef_stepMap!save.action";
				    $.post(url,$("#editForm").serialize(),function(data){
				    	 art.dialog.tips("保存成功",2);
				    });
				} 
			});
			//判断会签
			checkSigns();
		});
		
		function checkSigns(){
			var signs = $('input:radio[name="model.isSigns"]:checked').val();
			var stepType = $('input:radio[name="model.stepType"]:checked').val();
			if(signs==1||stepType==2){ 
				$("#signUserDiv").show();
			}else{
				$("#signUserDiv").hide();
			}
		}
		//提交保存
		function saveSubmit(){
			//$("form").submit();
			document.getElementById("submitbtn").click();
		}
		//权限地址簿
		function openAuthorityBook(fieldName){
			var code = $("#"+fieldName).val();
			var pageUrl = "authorityAddressBookAction!index.action?target="+fieldName+"&code="+encodeURI(encodeURI(code));
			 art.dialog.open(pageUrl,{
						id:"selectrouteParam",
						title: '权限地址簿', 
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width: 650,
						height: 550
			});
		}
		
		$('#formid').combotree({
				    url:'sysFlowDef_stepFormSet!loadformTree.action',
				    onSelect:function(node){
				    	if(node.attributes.type=='group'){
				    		art.dialog.tips('请选择表单');
				    		this.open();
				    	}
				       
				    }
				});
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="west" border="false" style="width:180px;padding:3px;border-right:1px #efefef solid;">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			<s:property value="title"/><img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
	<div region="center" style="border:0px;">
		<div class="tools_nav">
			<a href="javascript:saveSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	<s:form name="editForm" id="editForm" action="sysFlowDef_stepMap!save.action"  theme="simple" namespace="/">
		 <div>
							<table>
								<tr>
										<td class = "td_title">流程节点ID：</td>
										<td class = "td_data"><s:property value="model.actStepId"/>
										<span style='color:red'>*</span>
										</td>
								</tr>
								<tr>
										<td class = "td_title">流程节点名称：</td>
										<td class = "td_data"><s:textfield cssStyle="width:350px" cssClass = "{validate:{required:true, maxlength:50,messages:{required:'必填字段',maxlength:'最长不能超过50个字符'}}}" name="model.stepTitle" theme='simple'></s:textfield>
										<span style='color:red'>*</span>
										</td>
								</tr>
							</table>
		</div>
		 <div>
		 	<fieldset>
			         <legend>页面功能设置:</legend>
			         <table width="100%" border="0">
								<tr>
								<td class = "td_title">
											是否显示提交按钮:
										</td>
										<td class = "td_data">
											<s:radio value="model.isTrans"  list="#{'1':'是','0':'否'}" name="model.isTrans" theme="simple"/>
										</td>
										
										<td class = "td_title">
											是否允许讨论:
										</td>
										<td class = "td_data">
											<s:radio value="model.isTalk" list="#{'1':'是','0':'否'}" name="model.isTalk" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">
											是否允许添加意见:
										</td> 
										<td class = "td_data">
											<s:radio value="model.isAddOpinion" list="#{'1':'是','0':'否'}" name="model.isAddOpinion" theme="simple"/>
										</td> 
										<td class = "td_title">
											是否允许显示跟踪列表:
										</td>
										<td class = "td_data">
											<s:radio value="model.isDisplayOpinion" list="#{'1':'是','0':'否'}" name="model.isDisplayOpinion" theme="simple"/>
										</td>
										
								</tr>
								<tr>
										<td class = "td_title">
											
										</td>
										<td class = "td_data">
											
										</td>
										<td class = "td_title">
										是否支持线下任务反馈:
										</td>
										<td class = "td_data">
										<s:radio value="model.isOffline" list="#{'1':'是','0':'否'}" name="model.isOffline" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title"> 
											是否允许转发:	
										</td>
										<td class = "td_data">
											<s:radio value="model.isForward" list="#{'1':'是','0':'否'}" name="model.isForward" theme="simple"/>
										</td>
										<td class = "td_title">
											是否允许任务挂起:
										</td>
										<td class = "td_data">
											<s:radio value="model.isWait" list="#{'1':'是','0':'否'}" name="model.isWait" theme="simple"/> 
										</td>
								</tr>
								<tr>
										<td class = "td_title">
											是否允许加签:
										</td>
										<td class = "td_data">
											<s:radio value="model.isAddsign" list="#{'1':'是','0':'否'}" name="model.isAddsign" theme="simple"/>
										</td>
										<td class = "td_title">
											是否允许打印:
										</td>
										<td class = "td_data">
											<s:radio value="model.isPrint" list="#{'1':'是','0':'否'}" name="model.isPrint" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">
											是否记录修改日志
										</td>
										<td class = "td_data">
											<s:radio value="model.isLog" list="#{'1':'是','0':'否'}" name="model.isLog" theme="simple"/>
										</td> 
										<td class = "td_title">是否允许驳回发送人:
										</td>
										<td class = "td_data">
											<s:radio value="model.isBack" list="#{'1':'是','0':'否'}" name="model.isBack" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">
											是否允许抄送:
										</td>
										<td class = "td_data">
											<s:radio value="model.isCc" list="#{'1':'是','0':'否'}" name="model.isCc" theme="simple"/>
										</td>
										<td class = "td_title" >是否允许驳回申请人:
										</td>
										<td class = "td_data">
											<s:radio value="model.isBackSrc" list="#{'1':'是','0':'否'}" name="model.isBackSrc" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title"  colspan="3">是否允许驳回申请人办理后返回当前办理人
										</td>
										<td class = "td_data"> 
											<s:radio value="model.isBackToBack" list="#{'1':'是','0':'否'}" name="model.isBackToBack" theme="simple"/> 
										</td>
								</tr>
								<tr>
								<td class = "td_title">
											是否允许会签:
										</td>
										<td class = "td_data">
											<s:radio value="model.isSigns" list="#{'1':'是','0':'否'}" onclick="checkSigns()" name="model.isSigns" theme="simple"/>
										</td>
										<td class = "td_title" >是否允许驳回任意节点:
										</td>
										<td class = "td_data">
											<s:radio value="model.isBackDiy" list="#{'1':'是','0':'否'}" name="model.isBackDiy" theme="simple"/>
										</td>
								</tr>
								
								<tr>
										<td class = "td_title">
											是否显示修改日志:
										</td>
										<td class = "td_data">
											<s:radio value="model.isShowLog" list="#{'1':'是','0':'否'}" name="model.isShowLog" theme="simple"/>
										</td> 
										<td class = "td_title">
											"提交"按钮显示名称
										</td>
										<td class = "td_data">
											<s:if test="model.stepAbstract==null||model.stepAbstract==''">
												<s:textfield cssStyle="width:150px" name="model.stepAbstract"  value="提交" theme='simple'></s:textfield>
											</s:if>
											<s:else>
												<s:textfield cssStyle="width:150px" name="model.stepAbstract" theme='simple'></s:textfield>
											</s:else>
											
										</td> 
								</tr>
								<tr>
										<td class = "td_title">
											是否支持意见附件:
										</td>
										<td class = "td_data">
											<s:radio value="model.isOpinionAttach" list="#{'1':'是','0':'否'}" name="model.isOpinionAttach" theme="simple"/>
										</td> 
										<td class = "td_title">
											是否支持单步撤销
										</td>
										<td class = "td_data">
											<s:radio value="model.isReback" list="#{'1':'是','0':'否'}" name="model.isReback" theme="simple"/>
											注：任务已被读取后，则无法执行收回动作
										</td> 
								</tr>
								
							</table>
		 	</fieldset>
		 </div>
		  <div>
		 	<fieldset>
			         <legend>流程办理设置:</legend>
			         <table>
								<tr >
										<td class = "td_title">流程节点类型：</td>
										<td class = "td_data">
										<s:radio value="model.stepType"  list="#{'0':'一般任务节点','2':'会签节点'}" onclick="checkSigns()"  name="model.stepType" theme="simple"/>
										<div style="color:#999">注：如果节点类型会签节点，则该节点必须要发送会签后，才允许继续办理</div>
										</td>
								</tr> 
								<tr id="signUserDiv" style='display:none'>
										<td class = "td_title">默认会签人：</td>
										<td class = "td_data">
										<s:textfield cssStyle="width:400px" id= "signsUsers" name="model.signsDefUser" theme='simple'></s:textfield><a href="javascript:openAuthorityBook('signsUsers');" class="easyui-linkbutton" plain="true" iconCls="icon-add"></a>	
										</td>
								</tr> 　　
								<tr>
										<td class = "td_title">节点办理人数限制：</td>
										<td class = "td_data">
										<s:select value="model.stepMax" list="#{'9999':'无限制','1':'1人','2':'2人','3':'3人','4':'4人','5':'5人','6':'6人','7':'7人','8':'8人','9':'9人'}" name="model.stepMax" theme="simple"/>
										</td> 
								</tr>
								<tr  style='display:none'>
										<td class = "td_title">节点身份限制：</td>
										<td class = "td_data">
											<s:textfield cssStyle="width:400px" id="stepPurview" name="model.stepPurview" theme='simple'></s:textfield><a href="javascript:openAuthorityBook('stepPurview');" class="easyui-linkbutton" plain="true" iconCls="icon-add"></a>
										</td>
								</tr> 
								<tr>
										<td class = "td_title">节点任务通知类型：</td>
										<td class = "td_data">
											系统消息：<s:radio value="model.sendSysmsg" list="#{'1':'开启','0':'关闭'}" name="model.sendSysmsg" theme="simple"/><br/>
											邮　　件：<s:radio value="model.sendEmail" list="#{'1':'开启','0':'关闭'}" name="model.sendEmail" theme="simple"/><br/>
											短　　信：<s:radio value="model.sendSms" list="#{'1':'开启','0':'关闭'}" name="model.sendSms" theme="simple"/><br/>
											即时通讯：<s:radio value="model.sendIm" list="#{'1':'开启','0':'关闭'}" name="model.sendIm" theme="simple"/><br/>
											微　　信：<s:radio value="model.sendWeixin" list="#{'1':'开启','0':'关闭'}" name="model.sendWeixin" theme="simple"/>
										</td>
								</tr>
							</table>
		 	</fieldset>
		 </div>
		 <div>
		 	<fieldset>
			         <legend>流程节点绩效设置:</legend>
			        <table>
								<tr>
										<td class = "td_title">合理流程时间设置：</td>
										<td class = "td_data">
											<s:textfield name="model.standardTime"></s:textfield>小时(工作日)
										</td>
								</tr>
								<tr>
										<td class = "td_title">流程办理预警时间设置：</td>
										<td class = "td_data">
											<s:textfield name="model.warningTime"></s:textfield>小时(工作日)
										</td>
								</tr>
							
							</table>
		 	</fieldset>
		 </div>
		 					<span style="display:none"><input name='submitbtn' id='submitbtn' type="submit" /></span>
							<s:hidden name="model.prcDefId"></s:hidden>
							<s:hidden name="model.actDefId"></s:hidden>
							<s:hidden name="model.actStepId"></s:hidden>
							<s:hidden name="model.id"></s:hidden>
							<s:hidden name="pageindex"></s:hidden>
							<s:hidden name="model.stepHelp"></s:hidden>
							
			</s:form>		
	</div>
							
</body>
</html>
