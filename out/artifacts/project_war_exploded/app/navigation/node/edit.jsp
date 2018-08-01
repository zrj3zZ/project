<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
<title>目录管理维护</title>
<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>

<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
*{font-family:Verdana;font-size:96%;}
label.error{float:none;color:red;padding-left:.5em;}
p{clear:both;}
.submit{margin-left:12em;}
em{font-weight:bold;}
</style>
<script>
  var api = art.dialog.open.api, W = api.opener;
	var mainFormValidator;
    $(document).ready(function(){
    	mainFormValidator =  $("#sysNode_save").validate({
			debug:false
		 });
		 mainFormValidator.resetForm();
         var setting = {
					async: {
						enable: true, 
						url:"sysNode_tree_Json.action",
						dataType:"json",
						autoParam:["id","nodeType"]
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
    });
    function closeWin(){
    	api.close();
    }
    function onClick(e, treeId, treeNode) {
			var groupTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = groupTree.getSelectedNodes();
			v = "";
			var id = "";
			var type = "";
			
			if(nodes.length>0){
				v = nodes[0].name;
				id = nodes[0].id;
				type = nodes[0].nodeType;
				if(type=='SYS'){
					$("#sysNode_save_model_systemId").val(id);
					$("#parentNodeId").val("");
				}else if(type="NODE"){
					$("#parentNodeId").val(id);
					$("#sysNode_save_model_systemId").val("");
				}
				var cityObj = $("#citySel");
				cityObj.attr("value", v);
				
				hideMenu();
			}
			
		}
		//================================================
		function showMenu() {
				var cityObj = $("#citySel"); 
				var cityOffset = $("#citySel").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
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
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return;
			}
			$("#sysNode_save").attr("action","sysNode_save.action");
			$("#sysNode_save").submit();
			
			
		}
	//==========================装载快捷键===============================//
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         $("#sysNode_save").submit(); return false;
		     }
        }); //快捷键
</script>
</head>
<body>
<s:form name="editForm" action="sysNode_save" validate="true" theme="simple">
<table width="500px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pagetitle"><img src="iwork_img/icon/system.gif" border="0">
        <s:if test="null == model.id">
            增加功能模块
        </s:if>
        <s:else>
            编辑功能模块
        </s:else>
	</td>
  </tr>

  <tr>
    <td >
    	<div region="center" style="padding: 3px; border: 0px">
			<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
  				<tr>
    				<td>
    					<table width="100%" border="0" cellpadding="2" cellspacing="0">
    						  <tr>
						      	<td class="td_title">上级目录1:</td>
						        <td class="td_data">
						        <input id="citySel" type="text"  disabled value="<s:property value="parentNodeName"/>" style="width:120px;background-color:#efefef;color:#333"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择</a>
										<s:hidden  id="parentNodeId"  name="model.parentNodeId" theme="simple"/> 
						        </td>
						      </tr>
    		                  <tr> 
						      	<td class="td_title">模块名称:</td>
						        <td class="td_data">
						        	<s:textfield label="模块名称" cssClass="{maxlength:32,required:true}" name="model.nodeName"/>&nbsp;<span style='color:red'>*</span></td>
						      </tr>
						      <tr>
						      	<td class="td_title">模块图标:</td>
						        <td class="td_data">
						        	<s:textfield label="模块图标" name="model.nodeIcon"  cssClass="{maxlength:128,required:true}"/></td>
						        </tr>
						      <tr>
						      	<td class="td_title">模块URL:</td>
						        <td class="td_data">
						        	<s:textfield label="模块URL"  cssClass="{maxlength:256,required:true}" name="model.nodeUrl"/>&nbsp;<span style='color:red'>*</span></td>
						        </tr>
						      <tr>
						      	<td class="td_title">模块类型:</td>
						        <td class="td_data">
						        	<s:select  name="model.nodeType"  cssClass="{required:true}"  headerKey="" headerValue="--请选择--" list="#{'sys':'系统菜单','global':'全局菜单','purview':'权限菜单'}"  label="模块类型" /> 
						        </td>
						     </tr>
						      <tr>
						      	<td class="td_title">提交目标:</td>
						        <td class="td_data">
						       	  <s:select label=""  cssClass="{required:true}" name="model.nodeTarget" value="model.nodeTarget" list="#{'_blank':'弹出窗口【_blank】','_self':'当前窗口【_self】','_parent':'父窗口【_parent】','mainFrame':'主框架【mainFrame】','treeFrame':'左侧框架【mainFrame】'}" theme="simple" headerKey="" headerValue="--请选择--"></s:select>&nbsp;<span style='color:red'>*</span>
								</td>
						      </tr>
						      <tr>
						        <td class="td_title">模块描述:</td>
						        <td class="td_data">
									<s:textarea label="模块描述"  cssClass="{maxlength:128}" name="model.nodeDesc" cssStyle="width:300px;height:50px;;"></s:textarea>
								</td>
						      </tr>
						      <tr>
						        <td class="td_title">唯一标识:</td>
						        <td class="td_data">
									<s:property value="model.nodeUuid"/>
									<s:hidden name="model.nodeUuid"></s:hidden>
								</td>
						      </tr>
						      <div style="display:none">
						      	<s:textfield name="model.systemId"></s:textfield>
						      	<s:textfield name="model.orderindex"></s:textfield>
						      	<s:textfield name="model.id"></s:textfield>
						      </div>
    					</table>
				    </td>
				  </tr>
			</table>
		</td>
  	</tr>
  <tr>
  		<td>
  			<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px; padding-top: 5px; padding-right: 15px; margin-bottom: 20px;">
			<a href='javascript:doSubmit();' class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href='javascript:closeWin();' class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
			</div>
  		</td>
  </tr>
</table>
</s:form>
<div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="grouptree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
