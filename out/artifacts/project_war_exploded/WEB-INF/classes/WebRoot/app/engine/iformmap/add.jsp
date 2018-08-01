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
    <script type="text/javascript" src="iwork_js/tools/base64.js"></script>
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
		
	var api,W;
		try{
		 api = art.dialog.open.api;
		 W = api.opener;	
		}catch(e){}
	var mainFormValidator;
	
	$().ready(function() {
		 mainFormValidator =  $("#editForm").validate({
			   errorPlacement: function (error, element) {
			   			//$(element).attr("style","border:1p solid red");
	               error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面
			   }
		 }); 
			 var setting = {
				async:{
						enable: true, 
						url:"sysEngineIFormMap_loadDisplayTypeTree.action",
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
			$.fn.zTree.init($("#displayTree"), setting);
				mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();
			 //页面load时，默认打开外观列表
			 <s:if test='tipInfo==""'>
			  	showMenu();
			 </s:if>
			
		});
		//执行提交动作
		function doSubmit(){
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
				    $('#editForm').attr("action","sysEngineIFormMap_update.action");
				   $('#editForm').ajaxSubmit(options);
			}else{
				var options = {
					error:errorFunc,
					success:successFunc 
				   };
				    $('#editForm').attr("action","sysEngineIFormMap_save.action");
				   $('#editForm').ajaxSubmit(options);
			}
		}
	      errorFunc=function(){ 
	            $("#tips").text("保存失败!");
	            setTimeout("$('#tips').text('');",500);
	      }
	      successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText!="0"){
	               $("#tips").text("保存成功!");
	                setTimeout("$('#tips').text('');",500);
	           }
	           else if(responseText=="error"){
	              $("#tips").text("保存失败!");
	            setTimeout("$('#tips').text('');",500);
	           } 
	      }
		function onClick(e, treeId, treeNode) {
			var displayTree = $.fn.zTree.getZTreeObj("displayTree");
			var nodes = displayTree.getSelectedNodes();
			if(treeNode.isParent){
		 		displayTree.expandNode(treeNode, true, null, null, true);
			}else{
				v = "";
				var id = "";
				nodes.sort(function compare(a,b){return a.id-b.id;});
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					id+= nodes[i].id + ",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if (id.length > 0 ) id = id.substring(0, id.length-1);
				var cityObj = $("#displayTypeName");
				cityObj.attr("value", v);
				$("#displayType").val(id); 
				hideMenu();
			}
		}
		//================================================
		function showMenu() {
				var cityObj = $("#displayTypeName"); 
				var cityOffset = $("#displayTypeName").offset();
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
		function close(){
			try{
				api.close();
			}catch(e){
				parent.close(); 
			}
		}
		function doback(){
			$("#editForm").attr("action","sysEngineIFormMap_back.action");
			$("#editForm").attr("target","_self");
			document.editForm.submit(); 
		}
		function doNext(){
			$("#editForm").attr("action","sysEngineIFormMap_next.action");
			$("#editForm").attr("target","_self");
			document.editForm.submit(); 
		}
		//打开参数选择器
		function openDisplayParams(){
			var dstype = $("#displayType").val();
			alert(dstype); 
		}
		
		
		//公式编辑器
		function openExpressionIndex(){
			var eStr = $("#editForm_model_fieldDefault").val().replace("%","").replace("%","");
		//	$.post("getBase64Str.action",{encodeStr:eStr},function(data){
			var b = new Base64();  
            var str = b.encode(eStr);  
				var pageUrl = "expressionIndex.action?eId="+ str;
				try{
				  art.dialog.open(pageUrl,{
			    	id:'expressionIndexFrame',
			    	title:'表单域设置',  
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410
				 });
				}catch(e){	
					pageUrl="expressionIndex.action?eId="+ str;
					window.parent.showDialog("表单域设置",pageUrl,850,450);
				}
					
		}
		
		function insertExpressionStr(str){
			$("#editForm_model_fieldDefault").val($.trim(str));
		}
  
	</script>
</head>
<body class="easyui-layout"> 
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm"  action="sysEngineIFormMap_update.action"  theme="simple">
            <table width="100%" border="0" cellspacing="4" cellpadding="3">
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>域名称:</td>
			    <td class="form_data">
			    	<s:textfield name="model.fieldName" theme="simple" ></s:textfield>
			  </tr> 
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>标题:</td>
			    <td class="form_data">
			    	<s:textfield name="model.fieldTitle" theme="simple" ></s:textfield>
			    </td>
			  </tr>
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>外观类型:</td>
			    <td class="form_data">
					 <input type="text" name="displayTypeName" readonly="readonly" id="displayTypeName"  class = '{maxlength:256,required:true}'   value="<s:property value="displayTypeName"/>"><s:hidden id="displayType" name="model.displayType"></s:hidden> <a href="javascript:showMenu()">选择</a>
				</td> 
			  </tr>
			 <tr>
			    <td class="form_title">外观参数:</td>
			    <td class="form_data">
			    	<s:textfield name="model.displayEnum" cssStyle="width:260px;"  theme="simple" ></s:textfield> <a id="btnEp"  plain="true" class="easyui-linkbutton" icon="icon-help" href="javascript:openDisplayParams();" >帮助</a>
			    </td>
			  </tr>
			   <tr>
			    <td class="form_title">默认值:</td>
			    <td class="form_data">
			    	<s:textfield name="model.fieldDefault" cssStyle="width:220px;"  theme="simple"></s:textfield>
			    	<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"  plain="true" href="javascript:openExpressionIndex();" >公式</a> 
			    </td>
			  </tr>
			    <tr>
			    <td class="form_title">表单录入宽度/高度:</td>
			    <td class="form_data">
			    	<s:textfield name="model.inputWidth"  theme="simple" cssStyle="width:60px;"></s:textfield>
			    	&nbsp;/&nbsp;
			    	<s:textfield name="model.inputHeight"  theme="simple" cssStyle="width:60px;"></s:textfield>
			    </td>
			  </tr>
			   <tr>
			    <td class="form_title">显示宽度:</td>
			    <td class="form_data"><s:textfield name="model.displaywidth"  theme="simple" cssStyle="width:60px;"></s:textfield><span style="color:#999;padding-left:5px;">注：适用于表单子表、报表列宽度显示</span></td>
			  </tr>
			
			   <tr>
			    <td class="form_title">是否允许为空:</td>
			    <td class="form_data">
			    	<s:radio list="#{'0':'是','1':'否'}" name="model.fieldNotnull"></s:radio>
			    </td>
			  </tr>
			   <tr>
			    <td class="form_title">脚本扩展:</td>
			    <td class="form_data">
			    	<s:textarea rows="4" cols="30" name="model.htmlInner" ></s:textarea>
			    </td>
			  </tr>
			   <tr>
			    <td ></td>
			    <td >
			    	<span id="tips" style="color:red"><s:property value="tipInfo"  escapeHtml="false"/></span>
			    </td>
			  </tr>
			   
			</table>	 
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.iformId"/>
                <s:hidden   name="model.mapType" value="0"/>
               </s:form> 
               <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;height:280px;width:250px;overflow:auto;"> 
								<ul id="displayTree" class="ztree" style="margin-top:0; width:160px;height:260px;"></ul> 
				</div>
            </div>  
        		<div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;padding-right:15px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >确定</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:close();">关闭</a>
            </div>
</body>
</html>
