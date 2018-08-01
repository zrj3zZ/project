<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<script type="text/javascript" src="iwork_js/sys_procdef.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<style type="text/css">
		.title{
			font-size:16px;
			font-weight:bold;
			padding:20px 10px;
			background:#eee;
			overflow:hidden;
			border-bottom:1px solid #ccc;
		}
		.t-list{
			padding:5px;
		}
		.formdata{
			padding-top:3px;
			padding-bottom:3px;
		}
		label.error{float:none;color:red;padding-left:.5em;}
	</style>
	<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新建流程模型
					addSysProcDef(); return false;
				}
		  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
				}		
}); //快捷键
	</script>
	<script type="text/javascript">
	$(function(){
				//加载导航树 
				$('#processtree').tree({   
	                 url: 'sysProcessDefinition!openjson.action',   
	               	onClick:function(node){
	               		if(node.attributes.type=='process'){
	               			var url = 'sysProcessDefinition!displayProcess.action?processId='+node.id;
	               			//var url = "sysProcessDefinition";
	               			
							window.parent.addTab(node.text,url,'');
	               		}else if(node.attributes.type=='group'){
	               			
	               			document.location.href = "sysProcessDefinition!index.action?groupId="+node.id;
	               		}
	               },
	               onLoadSuccess:function(node,data){
	               		var groupId = "<s:property value='groupId'  escapeHtml='false'/>";
	               		var tnode = $('#processtree').tree('find',groupId);
	               		if(tnode!=null){
	               			$('#processtree').tree('select',tnode.target);
	               		}
	               }
	             });
	             $('#process_grid').datagrid({
	             	url:"sysProcessDefinition!load.action?groupId=<s:property value='groupId'  escapeHtml='false'/>",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					singleSelect:true,
					columns:[[
						{field:'ID',title:'序号',width:30},
						{field:'TITLE',title:'流程标题',width:100},
						{field:'VERSION',title:'流程版本',width:80,align:'left'},
						{field:'STATE',title:'流程状态',width:80,align:'left'},
						{field:'MASTER',title:'模型管理员',width:150}
						
					]],
					idField:'ID',
					onDblClickRow:function(rowIndex){
						var row = $('#process_grid').datagrid('getSelected');
						var url = 'sysProcessDefinition!displayProcess.action?processId='+row.ID;
						parent.frames['processDefListFrame'].location = url; 
					}
				});				
		
		
		})
		
$(document).ready(function(){
              $('#editForm').validate({meta:"validate"});
           });		
		
		
	
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef">
		<a href="javascript:addSysProcDef();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新建流程模型</a>
		<!--<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>-->
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		
	</div>
	</div>
	<div region="center" style="padding:3px;border:0px;border-left:1px solid #efefef">
			<table id="process_grid" style="margin:2px;"></table>
	</div>
	
		<!--添加分类窗口-->
    <div id="sys_proc_def_window" class="easyui-window" title="新建流程" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false" icon="icon-save"  style="width: 400px; height: 350px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <s:form id="editForm" name="editForm" action="sysProcessDefinition!save.action"  theme="simple"  enctype="multipart/form-data">
            	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            		<tr>
            			<td>流程标题：</td>
            			<td><input class="{validate:{required:true,maxlength:50,messages:{required:'必填字段',maxlength:'字段过长'}}}"  type="text" name="model.title" id ="title"  style="width:190px">
            			 <span style="color:red;">*</span>
            			</td>
            		</tr>
            		<tr>
            			<td>流程状态：</td>
            			<td> <s:radio cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.state" list="%{#{'1':'开启','0':'关闭'}}" label="流程状态" listKey="key" key="model.state"/>
            			 <span style="color:red;">*</span>
            			</td>
            		</tr>
            		<tr>
            			<td>模型管理员：</td>
            			<td> <input class="{validate:{maxlength:20,messages:{maxlength:'字段过长'}}}" type="text" name="model.master" style="width:190px"></input></td>
            		</tr>
            		<tr>
            			<td>模型描述：</td>
            			<td> <textarea name="model.context" class="{validate:{maxlength:500,messages:{maxlength:'字段过长'}}}" style="height:100px;width:190px;" ></textarea></td>
            		</tr>
            		<tr>
            			<td>上传部署文件：</td>
            			<td> <s:file cssClass="{validate:{maxlength:200,messages:{maxlength:'字段过长'}}}" name="upload"  label="上传的文件" /></td>
            		</tr>
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden name="model.groupId"/>
                <s:hidden name="groupId"/>
                <s:hidden name="fileName"/>
                <s:hidden name="uploader"/>
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
            </div>
        </div>
    </div>
</body>
</html>
