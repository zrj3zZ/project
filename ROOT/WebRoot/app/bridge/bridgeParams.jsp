<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 


 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/engine/conn_page.js"></script>  
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script>
		var api = art.dialog.open.api, W = api.opener;	
		var mainFormValidator;
			$().ready(function() {
					 var setting = {
						async: {
								enable: true, 
								url:"sysEngineGroup!openjson.action",
								dataType:"json"
							},
						view: {   
							dblClickExpand: false
						},
						data: {
							simpleData: {
								enable: true
							}
						},
						callback: {
							//beforeClick: beforeClick,
							onClick: onClick
						}
					};
					 setInterfaceType();
					$.fn.zTree.init($("#treeDemo"), setting);
					mainFormValidator =  $("#editForm").validate({
						debug:false,
						errorPlacement: function (error, element) { //指定错误信息位置
						      if (element.is(':radio') || element.is(':checkbox')) {
						          var eid = element.attr('name');
						          error.appendTo(element.parent());
						      } else {
						          error.insertAfter(element);
						     }
						 } 
					 });
					 mainFormValidator.resetForm();
					 
				});
	</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体; 
			font-size:14px;
			text-align:right;
			width:200px;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
</head>
<body class="easyui-layout">
<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" > 
	<tr>
		<td class="left-nav">
			<dl class="demos-nav">
				<dd><a id="sysConn_baseInfo" class="selected" href="conn_design_param_baseinfo.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">基本信息</a></dd>
				<dd><a id="sysConn_param_input" href="conn_design_param_input.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">输入参数设置</a></dd>
				<dd><a id="sysConn_param_output" href="conn_design_param_output.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">输出参数设置</a></dd>
				<dd><a id="sysConn_param_trigger" href="conn_design_trigger_index.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">接口扩展触发器</a></dd>
			</dl> 
		</td>
	</tr> 
</table> 
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <form id="editForm" name="editForm" action="/conn_design_save.action" method="post">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            	<tr>   
            			<td class="form_title"><span style="color:red;">*</span>目录分组1:</td>
            			<td class="form_data">
								<input id="citySel" type="text"  disabled value="系统平台" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); ">选择</a>
										<input type="hidden" name="model.groupId" value="10" id="groupid"/> 
						</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>接口描述:</td>
            			<td class="form_data">
            			<textarea name="model.inMemo" cols="" rows="" id="editForm_model_inMemo" class="{maxlength:200,required:true}" style="width:350px;height:50px;">ddddd</textarea></td>
            		</tr>
            		<tr>  
            			<td class="form_title" style="vertical-align:top"><span style="color:red;">*</span>接口名称:</td>
            			<td class="form_data"><input type="text" name="model.inName" value="ZBPM_INT_001" id="editForm_model_inName" class="{maxlength:32,required:true}" style="width:350px"/>
            			<div style="margin-top:10px;color:#ccc">注:SAP接口填写RFC接口名称，webservice填写调用的方法名称</div></td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>接口类型:</td>
            			<td  class="form_data">   
            					<input type="radio" name="model.inType" id="editForm_model_inTypesap" checked="checked" value="sap" class="{maxlength:32,required:true}" style="border:0px;" class="{maxlength:32,required:true}" style="border:0px;" onClick="setInterfaceType();"/><label for="editForm_model_inTypesap" class="{maxlength:32,required:true}" style="border:0px;">SAP接口</label>
<input type="radio" name="model.inType" id="editForm_model_inTypewebservice" value="webservice" class="{maxlength:32,required:true}" style="border:0px;" class="{maxlength:32,required:true}" style="border:0px;" onClick="setInterfaceType();"/><label for="editForm_model_inTypewebservice" class="{maxlength:32,required:true}" style="border:0px;">WebService接口</label>

            			</td>
            		</tr>
            		<tr id="in_addressTr">   
            			<td class="form_title" style="vertical-align:top">接口地址:</td>
            			<td class="form_data"><input type="text" name="model.inAddress" value="" id="editForm_model_inAddress" style="width:350px;"/><br/>
            			<div style="margin-top:10px;color:#ccc">注:适用于接口为webservice时，填写webservice地址，<br/>例如：http://mynamespace/test?wsdl</div>
            			</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>是否记录缓存:</td>
            			<td  class="form_data">   
            					<input type="radio" name="model.isCache" id="editForm_model_isCache1" value="1" class="{maxlength:32,required:true}" style="border:0px;" class="{maxlength:32,required:true}" style="border:0px;" onClick="setInterfaceType();"/><label for="editForm_model_isCache1" class="{maxlength:32,required:true}" style="border:0px;">是</label>
<input type="radio" name="model.isCache" id="editForm_model_isCache0" checked="checked" value="0" class="{maxlength:32,required:true}" style="border:0px;" class="{maxlength:32,required:true}" style="border:0px;" onClick="setInterfaceType();"/><label for="editForm_model_isCache0" class="{maxlength:32,required:true}" style="border:0px;">否</label>

            			</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>缓存时间:</td>
            			<td  class="form_data">  
            					<input type="text" name="model.cacheTime" value="0" id="editForm_model_cacheTime" style="width:50px"/> <span style="color:#ccc">注:默认缓存时间为3小时</span>  
            			</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>接口状态:</td>
            			<td class="form_data">
            					<input type="radio" name="model.status" id="editForm_model_status0" value="0" class="{maxlength:32,required:true}" style="border:0px;" class="{maxlength:32,required:true}" style="border:0px;"/><label for="editForm_model_status0" class="{maxlength:32,required:true}" style="border:0px;">关闭</label>
<input type="radio" name="model.status" id="editForm_model_status1" checked="checked" value="1" class="{maxlength:32,required:true}" style="border:0px;" class="{maxlength:32,required:true}" style="border:0px;"/><label for="editForm_model_status1" class="{maxlength:32,required:true}" style="border:0px;">开启</label>

            			</td>
            		</tr>
            		<tr>
            			<td class="form_title">唯一标识:</td>
            			<td class="form_data">e5d97057da244f139ff76bea4a55ba9b</td>
            		</tr>
            	</table>
                <input type="hidden" name="model.id" value="48" id="editForm_model_id"/>
                <input type="hidden" name="model.uuid" value="e5d97057da244f139ff76bea4a55ba9b" id="editForm_model_uuid"/>
                <input type="hidden" name="model.triggerPath" value="" id="editForm_model_triggerPath"/>
                <input type="hidden" name="groupid" value="" id="editForm_groupid"/> 
               </form>



  
            </div> 
            <div region="north" border="false" style="text-align: left; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
             <div region="north" border="false"  style="text-align:left;">
 			<div class="tools_nav"> 
				 <a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="javascript:doSubmit();" >保存</a> 
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	
			</div>			
		 </div>
               
            </div> 
        <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
		<div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
