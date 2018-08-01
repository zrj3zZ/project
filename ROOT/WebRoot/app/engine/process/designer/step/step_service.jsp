<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-服务节点基本信息</title>
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
			fieldset{
				border:1px solid #efefef;
				padding:10px; 
			}
			legend{
			    font-family:"宋体";
				font-size: 16px;
				color:#0000ff;
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
					var url = "process_step_service_save.action";
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
		<s:property value="selectHtml" escapeHtml="false"/>
	</div>
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			服务节点设置<img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
	<div region="center" style="border:0px;">
		<div class="tools_nav">
			<a href="javascript:saveSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	<s:form name="editForm" id="editForm" action="sysFlowDef_stepMap!save.action"  theme="simple" namespace="/">
		 <div>
		 	<fieldset>
			         <legend>服务类型设置:</legend>
							<s:radio value="model.stepType"  list="#{'1':'会签节点','2':'触发器节点'}" name="model.stepType" theme="simple"/>
									
		 	</fieldset>
		 </div>
		  <div>
		 	<fieldset>
			         <legend>下一个办理人:</legend>
			         <s:textfield name="model.nextStepId"></s:textfield>
			</fieldset>
		  	<fieldset>
			         <legend>下一个办理节点:</legend>
			         <s:textfield name="model.nextUser"></s:textfield>
			</fieldset>
		 
		 </div>
	 	<span style="display:none"><input name='submitbtn' id='submitbtn' type="submit" /></span>
						<s:hidden name="model.prcDefId"></s:hidden>
						<s:hidden name="model.actDefId"></s:hidden>
						<s:hidden name="model.actStepId"></s:hidden>
						<s:hidden name="model.id"></s:hidden>
			</s:form>		
	</div>
							
</body>
</html>
