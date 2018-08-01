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
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
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
		.td_memo{
			text-align:left; 
			padding:12px;
			font-family:"宋体";
			border:1px solid #666;
			color:#666;
			font-size:12px;
		} 
		.td_memo_title{
			color:red;
			
		}
	</style>	
	<script type="text/javascript">
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
		
	//var api = frameElement.api, W = api.opener;	
	var mainFormValidator;
	var editor1 ;
	$().ready(function() {
			 var setting = {
				async: {
						enable: true, 
						url:"sysEngineGroup!openjson.action",
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
			mainFormValidator =  $("#editForm").validate({
				debug:false
			 });
			 mainFormValidator.resetForm();
		});
		KindEditor.ready(function(K) {
			editor1 = K.create('textarea[name="model.memo"]', { 
				cssPath : 'iwork_css/formstyle.css', 
				width : '450px', 
				items : [
					  'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
					'italic', 'underline',  'lineheight',
					'indent', 'outdent',  
					
					 'link', 'unlink'
				],
				themeType:'simple',
				newlineTag:'br',
				height : '100px',
				filterMode:false 
			});
			//prettyPrint();
		});
		//执行提交动作
		function doSubmit(){
			editor1.sync();//同步html编辑器
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return false;
				}
			var id = $("#editForm_model_id").val();
			
			if(id!=""){
				   var options = {
					error:errorFunc,
					success:successFunc 
				   };
				   $('#editForm').ajaxSubmit(options);
			}
		}
	      errorFunc=function(){
	           art.dialog.tips("保存失败！");
	      }
	      successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText!="0"){
	                art.dialog.tips("保存成功！"); 
	           }
	           else if(responseText=="error"){
	              art.dialog.tips("保存失败！");
	           } 
	      }
		function onClick(e, treeId, treeNode) {
			var groupTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = groupTree.getSelectedNodes();
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
<body class="easyui-layout"> 
            <div region="center" border="false">
            <div class="tools_nav" >
						  <a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true"  href="javascript:doSubmit();" >保存</a>
						  <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			</div>
            <s:form name="editForm" id="editForm" action="sysEngineIForm!save.action"  theme="simple"  method="post">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>表单类型:</td> 
            			<td  class="form_data">  
            					<s:radio  name="model.metadataType" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'数据表单','1':'流程表单','2':'行项目子表'}" value="model.metadataType" theme="simple"/>
            			</td>
            		</tr>
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>表单名称:</td>
            			<td class="form_data"><s:textfield name="model.iformTitle" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            		</tr> 
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>映射存储:</td>
            			<td class="form_data">
            			<s:property value="metadataName"/>[<s:property value="tableName"/>]
            			<s:hidden name="model.metadataid" cssClass="{maxlength:32,required:true}" theme="simple"/>
            			</td>
            		</tr>  
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>目录分组:</td>
            			<td class="form_data">
								<input id="citySel" type="text"  disabled value="<s:property value="groupname"/>" style="width:120px;background-color:#efefef;color:#333"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); ">选择</a>
										<s:hidden  id="groupid"  name="model.groupid" theme="simple"/> 
						</td>
            		</tr> 
            		
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>访问方式:</td>
            			<td class="form_data">
            					<s:radio  name="model.visitType" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'登录验证','1':'匿名访问'}" theme="simple"/>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>唯一识别码:</td>
            			<td class="form_data">
            					<s:property value="model.uuid"/>
            				<s:hidden  id="uuid"  name="model.uuid" theme="simple"/>  
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>表单路径:</td>
            			<td class="form_data">
            				<s:if test="model.iformTemplate==null||model.iformTemplate==''">
            					未发现模板  <a href="">[生成模板]</a>
            				</s:if>
            				<s:else>
            				%IWORK_HOME%\WEB-INF\templates\user_templates\<s:property value="model.iformTemplate"/>
            				</s:else>
            				
            				<s:hidden name="model.iformTemplate"   theme="simple"/>
            			</td>
            		</tr>
            		<tr style="display:none">
            			<td class="form_title"><span style="color:red;">*</span>管 理 员:</td>
            			<td class="form_data">
            				<s:textfield name="model.master"  cssClass="{maxlength:32,required:true}"  theme="simple"/>
            		</tr>
            		<tr>
            			<td class="form_title">表单说明:</td>
            			<td class="form_data"><textarea name="model.memo" cols="" rows="" id="editForm_model_memo"><s:property value='model.memo' escapeHtml="true"/></textarea></td>
            		</tr> 
            	</table>
            	<table width="80%"  border="0" cellpadding="5" cellspacing="0">
            		<tr>
            			<td class="form_data" style="padding: 15px;">
            				<fieldset  class="td_memo">
								<legend class="td_memo_title">使用说明</legend>
								 【表单类型】“数据表单”类型是当用户仅使用表单来存储、维护一般性数据时使用的单据，此类表单无需绑定流程，可直接应用。<br>
								 　　　　　　“流程表单”类型是流程引擎流转过程中使用的表单。<br><br>
								 【访问方式】“登录验证”表单填写时需要用户登录系统后填写表单<br>
								 　　　　　　“匿名访问”表单填写无需登录，系统外用户均可实现表单数据填报<br>
							</fieldset>
            			</td>
            		</tr>
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.createdate"/> 
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
            
        	 <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;height:240px;width:190px;overflow:auto;"> 
								<ul id="grouptree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
        		
</body>
</html>
