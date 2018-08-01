<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>    
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
	<script type="text/javascript">
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
			 var setting2 = {
				async: {
						enable: true, 
						url:"sysEngineIForm!openjson.action?showtype=0",
						dataType:"json" 
					},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: setMetadataId
				} 
			};
			$.fn.zTree.init($("#treeDemo"), setting);
			$.fn.zTree.init($("#metadatatree"), setting2);
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();
		});
		
 //==========================装载快捷键===============================//快捷键
 		
 		jQuery(document).bind('keydown',function (evt){		
		   		 if(evt.ctrlKey&&evt.shiftKey){
				return false;
		   }else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			     this.location.reload(); return false;
		   }else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增存储
				 addMetaData(); return false;
		   }else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
				 doSubmit(); return false;
		   }		
		}); //快捷键
		
		
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return;
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
	               		cancel();
	           }
	           else if(responseText=="error"){
	              art.dialog.tips("保存失败!");
	           } 
	    }
		//关闭窗口
		function cancel(){
			api.close();
		}
		function beforeClick(treeId, treeNode) {
			var check = (treeNode && !treeNode.isParent);
			if (!check) art.dialog.tips("请选择目录...");
			return check;
		}
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			v = "";
			var id = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
				id+= nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (id.length > 0 ) id = id.substring(0, id.length-1);
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
			$("#groupId").val(id); 
			hideMenu();
		}
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
			//====================================================================================
			//设置存储模型ID
		function setMetadataId(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("metadatatree");
			if(treeNode.isParent){
		 		zTree.expandNode(treeNode, true, null, null, true);
			}else{
				nodes = zTree.getSelectedNodes();
				var v = "";
				var id = "";
				var type = "";
				nodes.sort(function compare(a,b){return a.id-b.id;});
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					id+= nodes[i].id + ",";
					type+=nodes[i].type;
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if (id.length > 0 ) id = id.substring(0, id.length-1);
				if(type!='group'){
					var cityObj = $("#metadataSel");
					cityObj.attr("value", v);
					$("#editForm_model_formid").val(id);   
					$("#editForm_model_title").val(v);   
					hideMetadataMenu(); 
				}else{
					art.dialog.tips('请选择表单模型');
					return false;
				} 
			}
		}
	
		function showMetadataMenu() {
				var cityObj = $("#metadataMenu"); 
				var metadataSelOffset = $("#metadataSel").offset();
				$("#metadataMenu").css({left:metadataSelOffset.left + "px", top:metadataSelOffset.top + 20 + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyMetadataDown); 
				return false;
			
		}  
		function hideMetadataMenu() {
			$("#metadataMenu").fadeOut("fast"); 
			$("body").unbind("mousedown", onBodyMetadataDown);
		}
		function onBodyMetadataDown(event) {
			if (!(event.target.id == "menuMetadataBtn" || event.target.id == "metadataMenu" || $(event.target).parents("#metadataMenu").length>0)) {
				hideMetadataMenu();
			}
		}
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="sysDem_save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            		 <tr>  
            			<td class="form_title"><span style="color:red;">*</span>映射表单:</td>
            			<td class="form_data">
            			<input id="metadataSel" type="text"  disabled value="<s:property value="formName"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuMetadataBtn" href="#" onclick="showMetadataMenu()">选择表单</a>
            			<s:hidden name="model.formid" cssClass="{maxlength:32,required:true}" theme="simple"/>
            			</td>
            		</tr>
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>数据维护标题:</td>
            			<td class="form_data"><s:textfield name="model.title" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            		</tr> 

            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>维护类型:</td>
            			<td  class="form_data">  
            					<s:radio  name="model.type" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'公共数据维护','1':'本人数据维护'}" theme="simple"/>
            			</td> 
            		</tr>
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>目录分组:</td>
            			<td class="form_data">
								<input id="citySel" type="text"  disabled value="<s:property value="groupName"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu()">选择</a>
								<s:hidden  id="groupId"  name="model.groupId" theme="simple"/> 
						</td>
            		</tr>  
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>管 理 员:</td>
            			<td class="form_data"><s:textfield name="model.master"  cssClass="{maxlength:32,required:true}"  theme="simple"/></td>
            		</tr> 
            		<tr>
            			<td class="form_title">说　　明:</td>
            			<td class="form_data"><s:textarea name="model.memo" cols="45" rows="6"></s:textarea></td>
            		</tr>
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden name="model.isImp"></s:hidden>
                <s:hidden name="model.isExp"></s:hidden> 
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
        <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; overflow:auto;position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
		<div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
