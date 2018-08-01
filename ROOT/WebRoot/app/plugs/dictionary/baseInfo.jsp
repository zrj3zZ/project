<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
  //全局变量
   
  var mainFormValidator;
	$().ready(function() {
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();
			 setEnumTitle();
			 setDemSet();
		});

	function setDemSet(){
		var d = $("input[name='model.isDem']:checked").val();
		if(d=="1"){
			$("#demUUID").rules("add",{required:true});
			$("#demUUID_Tr").show();
		}else{
			
			$("#demUUID_Tr").hide();
			$("#demUUID").rules("remove","required");
		}
	}
	//执行保存动作
	 function saveBaseInfo(){
          var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return false;
				}
             var options = {
			error:errorFunc,
			success:successFunc 
		   };
			$('#editForm').ajaxSubmit(options);
      }
      errorFunc=function(){
           art.dialog.tips("保存失败！");
      }
      
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="success"){
               art.dialog.tips("保存成功!");
           }
           else if(responseText=="error"){
              art.dialog.tips("保存失败！");
           } 
      }
     //关闭窗口
		function cancel(){
			api.close();
		}
     function setEnumTitle(){
    	 var id = $("#editForm_model_dsId").val();
    	 if(id=='999999'){
    		 $("#enumTitle").html("集成模型唯一标识");
    	 }else{
    		 $("#enumTitle").html("选择器SQL");
    	 }
     }
</script>
<style> 
		<!--
			#header { background:#6cf;}
			#title { height:20px; background:#EFEFEF; border-bottom:1px solid #990000; font:12px; font-family:宋体; padding-left:5px; padding-top:5px;}
			#baseframe { margin:0px;background:#FFFFFF;}
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
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				width:140px;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				padding-left:10px;
				white-space:nowrap;
				border-bottom:1px #999999 thick;
				vertical-align:middle;
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
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				
			}
			.memo{
				padding:5px;
			}
		-->
</style>
</head>
<body class="easyui-layout">
 <div region="north" border="false"  style="text-align:left;">
 			<div class="tools_nav">
		        <a href="javascript:saveBaseInfo()" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	
			</div>			
		 </div> 
	<div region="center" style="padding:0px;padding-top:10px;border:0px;">
				<div id="baseframe">
				<s:form name="editForm" id="editForm" action="sys_dictionary_design_saveBaseInfo.action" theme="simple">
							<table class = "table_form"> 
								<tr>
										<td class = "td_title"><span style='color:red;'>*</span>选择器名称:</td> 
										<td class = "td_data"><s:textfield cssClass="{maxlength:32,required:true}"   name="model.dicName" theme="simple"></s:textfield>&nbsp;</td>
								</tr>
								<tr>
										<td class = "td_title">选择器数据类型:</td>
										<td class = "td_data"><s:radio  name="model.dicType"  listKey="key" listValue="value"  list="#{'1':'表单选择器(单选)','2':'表单选择器(复选)','3':'行项目选择器'}" value="model.dicType" theme="simple"/></td>
								</tr> 
								<tr>
										<td class = "td_title">数据源选择:</td> 
										<td class = "td_data"><s:select onchange="setEnumTitle()"  name="model.dsId" listKey="id" listValue="dsrcTitle" theme="simple" list="%{datasourceList}"/></td>
								</tr>  
								<tr> 
										<td class = "td_title"><span style='color:red;'>*</span><span id="enumTitle">选择器SQL:</span></td>
										<td class = "td_data"><s:textarea name="model.dsSql" cssClass="{maxlength:1000,required:true}" cssStyle="width:400px;height:100px" theme="simple"/></td>
								</tr>
								<tr>
										<td class = "td_title"><span style='color:red;'>*</span>显示行数:</td>
										<td class = "readonly_data"><s:textfield cssClass="{maxlength:32,required:true}"   name="model.rowNum" theme="simple"></s:textfield></td>
								</tr> 
								<tr>
										<td class = "td_title">是否自动加载数据:</td>
										<td class = "td_data"><s:radio  name="model.isAutoShow"  listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="model.isAutoShow" theme="simple"/></td>
								</tr> 
								<tr>
										<td class = "td_title">是否允许维护字典数据:</td>
										<td class = "td_data"><s:radio  name="model.isDem" cssClass="{maxlength:32,required:true}"   onclick="setDemSet()" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="model.isDem" theme="simple"/></td>
								</tr> 
								<tr id="demUUID_Tr" style="display:none">
										<td class = "td_title">主数据维护UUID</td>
										<td class = "td_data"><s:textfield   name="model.demUUID"  id="demUUID" theme="simple"></s:textfield><div class="memo">注：设置该选项后，"数据选择设置"中须添加主数据维护中的ID字段</div></td>
								</tr> 
								<tr style="display:none">
										<td class = "td_title"><span style='color:red;'>*</span>字典管理员:</td>
										<td class = "td_data"><s:textfield  cssClass="{maxlength:32,required:true}"  name="model.master" theme="simple"></s:textfield></td>
								</tr>
								
								<tr style="display:none">
										<td class = "td_title">应用范围描述:</td>
										<td class = "td_data"><s:textarea name="model.memo" cssStyle="width:400px;height:60px" theme="simple"/></td>
								</tr>
								<tr>
										<td class = "td_title">唯一标识:</td>
										<td class = "td_data"><s:property value="model.uuid"/><s:hidden name="model.uuid" /></td>
								</tr>
								<s:hidden name="model.id"></s:hidden> 
								<s:hidden name="model.groupid"></s:hidden> 
							</table>
							</s:form>
				</div>
	</div>
	 <div region="south" border="false"  style="text-align:center;height:40px;padding:5px;padding-left:35px;color:#666">
								注： 【选择器名称】当类型为“行项目选择器”时，名称做为按钮名称使用</div> 
</body>
</html>
