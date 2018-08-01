<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:property value="title"/>-表单设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />   
	 <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.td_title {
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";
				height:15px;
				
			}
		.td_data{
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:14px;
			padding-top:5px;
			font-family:"微软雅黑";
			height:15px;
		}
		input {
		 color:#0000FF;
		}
		textarea{
		color:#0000FF;
		}
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
		.state{
		color:red;
		font-size:12px;
		}
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		//信息提示		
		$().ready(function() {
			 var setting = {
					async: { 
						enable: true, 
						url:"sysFlowDef_stepFormTreeJson.action",
						dataType:"json"
					},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: onClick
				} 
			};
			$.fn.zTree.init($("#grouptree"), setting);
			var arr=document.getElementsByName("model.bindType");
			for(var i=0;i<arr.length;i++){
				if(arr[i].checked){
					setBindType(arr[i].value);
				}
			}		
		})
		//修改路由策略
		function showForm(obj){
		    $('form').attr('action','sysFlowDef_stepFormSet!loadform.action');
		    $("#editForm").submit();
		}
		//选择不同的表单
		function setBindType(value){
			if(value==1){
				$("#def_form_span").hide();
				$("#def_url_span").show();
			}else{
				$("#def_form_span").show();
				$("#def_url_span").hide();
			}
		}
		//执行保存
		//表单提交
		function doSubmit(){ 
          var bool=regs();
          if(bool){  
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
           }
          else{
          		return ;
        	}
       }
      errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",2);
              setTimeout('cancel();',1000);
           }
           else if(responseText=="error"){
              art.dialog.tips("保存失败！",2);
           } 
      }
		//表单验证
		function regs(){
		var bindType = 0;
			var arr=document.getElementsByName("model.bindType");
			for(var i=0;i<arr.length;i++){
				if(arr[i].checked){
					bindType = arr[i].value;
				}
			}
			if(bindType==0){
				var formid= $("#editForm_model_formid").val();
				if(formid==''){
					art.dialog.tips("选择表单不能为空",2);
					return false;
				}
			}else{
				 var formname= $("#formname_temp").val();
				 var url= $("#editForm_model_url").val();
				 if(formname==''){
					 art.dialog.tips("表单名称不能为空",2);
					return false;
				 }
				 if(url==''){
				 	 art.dialog.tips("URL地址不能为空",2);
					return false;
				 }
				 //赋值操作，将表名临时域中得内容，添加到表名中
				 $("#editForm_model_formname").val($("#formname_temp").val());
			}
			return true;
		}
		//执行退出
		function cancel(){
			api.close();
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
		function onClick(e, treeId, treeNode) {
			var groupTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = groupTree.getSelectedNodes();
			v = "";
			var id = "";
			if(nodes.length>0){
				nodes.sort(function compare(a,b){return a.id-b.id;});
				var type = nodes[0].type;
				if(type=='group'){
						groupTree.expandNode(nodes[0], true, null, null, true);
						return false;
				}
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					id+= nodes[i].id + ",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if (id.length > 0 ) id = id.substring(0, id.length-1);
				var cityObj = $("#citySel");
				cityObj.attr("value", v);
				$("#editForm_model_formname").val(v); 
				$("#formId").val(id); 
				hideMenu();
			}
		}
		//================================================
		function showMenu() {
				var cityObj = $("#citySel"); 
				var cityOffset = $("#citySel").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
				return false;
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		} 
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
	</script>
</head>
<body  class="easyui-layout">
  <div region="north" border="false" >
  	<div class="tools_nav">
  	 <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >保存</a> 
  	 <a id="btnEp" class="easyui-linkbutton" icon="icon-reload" href="javascript:location.reload();" >刷新</a> 
		                    <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
  	</div>
	</div>
		<div region="center" style="padding:3px;border:0px">
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepFormSet!save.action"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	            			<td class="td_title" width="17%" >表单绑定类型：</td>
	            			<td class="td_data" width="83%" > 
	            				<s:radio onclick="setBindType(this.value);" value="model.bindType"  list="#{'0':'绑定表单','1':'绑定URL链接'}" id="mdoel_bindType" name="model.bindType" theme="simple"/>
	            				<a></a>
	            				<span style='color:red'>*</span>
	            			</td>
	            		</tr>
	            		<tr  id="def_form_span">
	            			<td class="td_title" width="17%">选择表单：</td>
	            			<td class="td_data" width="83%" id="formname_t_td">
	            				<s:hidden name="model.formid" id="formId"></s:hidden>
	            				<input id="citySel" type="text"  disabled value="<s:property value="model.formname"/>" style="width:120px;border:1px solid #999;background-color:#efefef;color:#333"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); ">选择</a>
	            				<s:hidden  name="model.formname"></s:hidden>
	            		    <span style='color:red'>*</span>
	            		</td>
	            		</tr>
	            		<tbody id="def_url_span">
		            		<tr  >
		            			<td class="td_title" width="17%">表单名称：</td>
		            			<td class="td_data" width="83%" id="url_td">
			            			<input type="text" name="formname_temp" id = "formname_temp"  value="<s:property value='model.formname'/>" theme="simple">
		            		     <span style='color:red'>*</span>
		            		     </td>
		            		</tr>
		            		<tr >
		            			<td class="td_title" width="17%">URL链接地址：</td>
		            			<td class="td_data" width="83%" id="url_td">
			            			<s:textfield name="model.url" cssStyle="width:180px;" theme="simple"></s:textfield>
			            			<span style='color:red'>*</span>
			            		</td>
		            		</tr>
	            		</tbody>
	            		<tr>
	            			<td class="td_title">设置为默认表单：</td>
	            			<td class="td_data"><s:radio value="model.isDef" list="#{'1':'是','0':'否'}" name="model.isDef" theme="simple"/><a></a><span style='color:red'>*</span></td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">是否允许编辑表单：</td>
	            			<td class="td_data"><s:radio value="model.isModify" list="#{'1':'是','0':'否'}" name="model.isModify" theme="simple"/><a></a><span style='color:red'>*</span></td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">使用权限设置：</td>
	            			<td class="td_data">
	            				<s:textarea readonly="true" id="formPurview" name="model.formPurview" cssStyle="width:240px;height:60px;"/>       				
	            				<a href="javascript:openAuthorityBook('formPurview');" class="easyui-linkbutton" plain="true" iconCls="icon-add">权限地址簿</a>	            				
	            			</td>
	            		</tr>
	            	</table>
	                <s:hidden name="model.actDefId"/>
	                <s:hidden name="model.actStepId"/>
	                <s:hidden name="model.prcDefId"/>
	                <s:hidden name="model.orderIndex"/>
	                <s:hidden name="model.id"/>
	                <s:hidden name="actDefId"/>
	                <s:hidden name="actStepDefId"/>
	                <s:hidden name="prcDefId"/>
	                <s:hidden name="id"/>
	                 </s:form> 
	     </div>
		            <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; height:260px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="grouptree" class="ztree" style="margin-top:0; width:180px;"></ul> 
				</div>
</body>
</html>
