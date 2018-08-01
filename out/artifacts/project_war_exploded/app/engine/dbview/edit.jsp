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
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
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
	var mainFormValidator;
	var api = art.dialog.open.api, W = api.opener;
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
			$.fn.zTree.init($("#treeDemo"), setting);
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
					return false;
				}
			var id = $("#editForm_model_id").val();
				saveDBView(); 
		}
		
		//执行保存
		//表单提交
		function saveDBView(){ 
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
       }
      errorFunc=function(){
           alert("保存失败！");
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText==""){
               alert("保存成功！"); 
               cancel(); 
           } 
           else{
              alert(responseText);
           } 
      }
		//关闭窗口
		function cancel(){
			api.close();
		}
		function beforeClick(treeId, treeNode) {
			var check = (treeNode && !treeNode.isParent);
			if (!check) alert("请选择目录...");
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
			$("#groupid").val(id); 
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
	</script>
</head>
<body class="easyui-layout">
  <div region="north" border="false" style="text-align:left;line-height: 30px;padding-top:2px;padding-bottom:2px;">
  <div class="tools_nav">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-save"  plain="true" href="javascript:doSubmit();" >保存</a>
                <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
  </div>
            </div> 
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px;">
            <s:form name="editForm" id="editForm" action="sysEngineDbView_save.action"  theme="simple"> 
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
            		<tr> 
            			<td class="form_title"><span style="color:red;">*</span>视图标题:</td>
            			<td class="form_data"><s:textfield name="model.title" cssStyle="width:180px" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            		</tr> 
            		<tr> 
            			<td class="form_title"><span style="color:red;">*</span>视图名称:</td>
            			<td class="form_data">
            			<s:if test="model.id==null">
            			        <span style="color:red">VIEW_</span>&nbsp; <s:textfield name="model.viewName" cssStyle="border:0px;border-bottom:1px solid #efefef;" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            			</s:if>
            			<s:else>
            				    <span style="color:red">VIEW_ <s:property value="model.viewName"/></span>&nbsp;<s:hidden name="model.viewName" cssStyle="border:0px;border-bottom:1px solid #efefef;" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            			</s:else>
            		</tr> 
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>目录分组:</td>
            			<td class="form_data">  
            			<input id="citySel" type="text"  disabled value="<s:property value="groupname"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu();">选择</a> 
            			</td>
            		</tr> 
            		<tr>  
            		<td class="form_title">
            		<span style="color:red;">*</span>视图SQL:
            			</td>
            			<td class="form_data" >
            			<s:textarea name="model.sql"  cssClass="{maxlength:1000,required:true}"   cssStyle="width:450px;height:140px;font-size:14px;color:blue;padding:3px;"></s:textarea>
						</td> 
            		</tr>  
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>管 理 员:</td>
            			<td class="form_data"><s:textfield name="model.manager"  cssClass="{maxlength:32,required:true}"  theme="simple"/></td>
            		</tr>
            		<tr>
            			<td class="form_title">说　　明:</td>
            			<td class="form_data"><s:textarea name="model.remark" cols="45" rows="6"></s:textarea></td>
            		</tr>
            	</table>
            	<s:hidden name="model.groupid"/>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
           
         <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; overflow:auto;position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
</body>
</html>
