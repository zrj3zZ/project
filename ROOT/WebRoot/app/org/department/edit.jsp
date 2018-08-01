<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>

<html>
<head> 
	<title>部门信息管理维护</title>
	<s:head/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link href="iwork_css/org/department_edit.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	var mainFormValidator;
	$().ready(function() { 
			 var setting = {
				async: {
						enable: true, 
						url:"department_tree_Json.action",
						dataType:"json",
						autoParam:["id","nodeType","companyId"]
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
			$.fn.zTree.init($("#departmenttree"), setting);
			   mainFormValidator =  $("#editForm").validate({
				   errorPlacement: function (error, element) {
		                error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
				   }
			   });
			 mainFormValidator.resetForm();
	});
		function onClick(e, treeId, treeNode) {
			var groupTree = $.fn.zTree.getZTreeObj("departmenttree");
			var nodes = groupTree.getSelectedNodes(); 
			v = "";
			var id = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
				if(nodes[i].nodeType=='company'){
					id+= 0 + ",";
				}else{ 
					id+= nodes[i].id + ",";
				}
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (id.length > 0 ) id = id.substring(0, id.length-1);
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
			$("#parentdepartmentid").val(id); 
			hideMenu();
		}
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
		function doDisabled(){
			var id = $("#editForm_model_id").val();
			 $.post("department_disable.action",{id:id},function(data){
			 		if(data=='success'){
			 			alert("注销成功");
			 		}else if(data=='no purview'){
			 			alert("当前用户无注销权限");
			 		}
	                setTimeout("closeWin()",1000);
		     });
			//$("#editForm").attr("action", "department_disable.action");
			//$("#editForm").submit();
		}
		function doActive(){
			var id = $("#editForm_model_id").val();
			 $.post("department_active.action",{id:id},function(data){
			 		if(data=='success'){
			 			alert("注销成功");
			 		}else if(data=='no purview'){
			 			alert("当前用户无注销权限");
			 		}
	                setTimeout("closeWin()",1000);
		     });	
		}
		
		 
		function closeWin(){
			api.close();
		}
		function doSubmit(){
			/* var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				alert("表单验证失败，请检查信息项是否合法",2);
				return;
			} */
			 var obj = $('#editForm').serialize(); 
		     $.post("department_save.action",obj,function(data){
		           if(data=="success"){ 
	                	alert("保存成功");
	                	closeWin();
	            	}else if(data=="zLength"){
	            		alert("部门名称长度为2-64！");
	            	}else if(data=="zh"){
	            		alert("部门名称 只能是 汉字，中文的（），数字，字母！");
	            	}
		     });
		}
	</script>
	<script type="text/javascript">
	window.onbeforeunload = function() {
		if (is_form_changed()) {
			
		}
	}
	function is_form_changed() {
		var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

		if (t_save.length > 0) { //检测到保存按钮,继续检测元素是否修改
			var is_changed = false;
			jQuery("#border input, #border textarea, #border select").each(
					function() {
						var _v = jQuery(this).attr('_value');
						if (typeof (_v) == 'undefined')
							_v = '';
						if (_v != jQuery(this).val())
							is_changed = true;
					});
			return is_changed;
		}
		return false;
	}
	jQuery(document).ready(
			function() {
				jQuery("#border input, #border textarea, #border select").each(
						function() {
							jQuery(this).attr('_value', jQuery(this).val());
						});
			});
</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:15px;
			text-align:right;
			width:350px;
			line-height:20px;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
			padding:3px;
		}
	</style>
</head>
<body class="easyui-layout">
<div region="center" border="false" class="winBackground" style="padding: 15px;padding-top:40px;">
<s:form name="editForm" id="editForm" action="department_save" validate="true" theme="simple">
<table width="100%" border="0" cellpadding="2" cellspacing="0">
     
      <tr>
      	<td align="right" class="form_title">部门名称</td>
        <td align="left"><s:textfield  label="部门名称" cssClass="{maxlength:64,required:true}" name="model.departmentname"/>&nbsp;<span style='color:red'>*</span></td>
      </tr>
       <tr>
      	<td align="right" width='30%' class="form_title">部门编码</td>
        <td align="left" width='70%'><s:textfield label="部门编号" cssClass="{maxlength:32,required:true}" name="model.departmentno"/>&nbsp;<span style='color:red'>*</span></td>
      </tr>
      <tr>
       <td align="right" class="form_title">上级部门编号</td>
        <td align="left">
        <input id="citySel" type="text"  disabled value="<s:property value="parentdeptname"/>" style="width:120px;background-color:#efefef;color:#333"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择</a>
										<s:hidden  id="parentdepartmentid"  name="model.parentdepartmentid" value="%{parentdeptid}" theme="simple"/> 
      </tr>
      <tr>
      	<td align="right" class="form_title">组织单位</td>
        <td align="left">
        <s:hidden cssClass="{maxlength:64,required:true}" value="%{company.id}" name="model.companyid"></s:hidden><s:textfield  readonly="true" cssClass="{maxlength:64,required:true,width:300px,background-color:#efefef,color:#333}" name="company.companyname"/>
		 	&nbsp;<span style='color:red'>*</span> 
        </td>
      </tr>
      <tr>
      	<td align="right" class="form_title">地区编号</td>
        <td align="left"><s:textfield cssClass="{maxlength:64}" label="地区编号" name="model.zoneno"/></td>
      </tr>
      <tr>
      	<td align="right" class="form_title">地区名称</td>
        <td align="left"><s:textfield label="地区名称" cssClass="{maxlength:64}" name="model.zonename"/>&nbsp;</td>
      </tr>
      <tr>
      	<td align="right" class="form_title">部门描述</td>
        <td align="left"><s:textarea label="部门描述" cssClass="{maxlength:256}" cssStyle="width:300px;height:100px" name="model.departmentdesc"></s:textarea></td>
      </tr>
      <tr>
      	<td align="right" class="form_title">成本中心编号</td>
        <td align="left"><s:textfield label="扩展项一"  cssClass="{maxlength:64}"  name="model.extend1"/></td>
      </tr>
      <tr>
      	<td align="right" class="form_title">成本中心名称</td>
        <td align="left"><s:textfield label="扩展项二"  cssClass="{maxlength:64}" name="model.extend2"/></td>
      </tr>
      <tr>
      	<td align="right" class="form_title">扩展项三</td>
        <td align="left"><s:textfield label="扩展项三"  cssClass="{maxlength:64}" name="model.extend3"/></td>
     </tr> 
       <tr>
       	<td align="right" class="form_title">扩展项四</td>
        <td align="left"><s:textfield label="扩展项四"  cssClass="{maxlength:64}" name="model.extend4"/></td>
       </tr>
       <tr>
       	<td align="right" class="form_title">扩展项五</td>
        <td align="left"><s:textfield label="扩展项五"  cssClass="{maxlength:64}" name="model.extend5"/></td>
        </tr>
    </table>
  		
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 	<s:hidden name="model.orderindex" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 	<s:hidden name="model.orderindex"/>
		 	<s:hidden name="model.departmentstate"/>
		 </s:else>
		 <s:hidden name="queryName" />
		 <s:hidden name="queryValue" />	
		 <s:hidden name="model.layer" value="%{layer}"/>
</s:form>
</div> 
          <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;border-top:1px solid #efefef">
              <a id="btnEp" class="easyui-linkbutton" icon="icon-save" href="javascript:doSubmit();" >保存</a>
              <!--               <s:if test="model.departmentstate==0">
              <a href="javascript:doDisabled();" class="easyui-linkbutton"  plain="false" iconCls="icon-remove" >注销</a>
              </s:if>
              <s:else>
              	<a href="javascript:doActive();" class="easyui-linkbutton"  plain="false" iconCls="icon-add" >激活</a>
              </s:else>
               -->
       		  <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:closeWin()">关闭</a> 
          </div> 
          <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;overflow:auto;width:300px;height:300px;position: absolute;"> 
								<ul id="departmenttree" class="ztree" style="margin-top:0; width:220px;"></ul> 
				</div>
</body>
</html>
