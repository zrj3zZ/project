<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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
			font-family:宋体;
			font-size:12px;
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
					onClick: onClick
				}
			};
			$.fn.zTree.init($("#grouptree"), setting);
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();
			  
			checkLogSel();//判断修改日志选项是否加载
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
	           }
	           else if(responseText=="error"){
	              art.dialog.tips("保存失败!");
	           } 
	    }
	
		function beforeClick(treeId, treeNode) {
			var check = (treeNode && !treeNode.isParent);
			if (!check) art.dialog.tips("请选择目录...");
			return check;
		}
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("grouptree"),
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
		function checkLogSel(){
		 //判断修改日志选项是否加载
				var islog=$('input:radio[name="model.isLog"]:checked').val();
				if(islog==1){ 
					$("#tr_isShowLog").show(600);
				}else{
					$("#tr_isShowLog").hide(600);
				}
		}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false" > 
		 	<div class="tools_nav">
		 	  <a class="easyui-linkbutton" icon="icon-ok"  plain="true"  href="javascript:doSubmit();" >保存</a> 
                <a  class="easyui-linkbutton" icon="icon-reload"  plain="true"  href="javascript:this.location.reload();">刷新</a>
		 	</div>
        </div>  
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="sysDem_save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            		
            		 <tr>  
            			<td class="form_title"><span style="color:red;">*</span>映射表单:</td>
            			<td class="form_data">
            			<input id="metadataSel" type="text"  disabled value="<s:property value="formName"/>" style="width:120px;background-color:#efefef"/> 
									<!-- &nbsp;<a id="menuMetadataBtn" href="#" onclick="showMetadataMenu(); return false;">选择表单</a> -->
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
            			<td class="form_title"><span style="color:red;">*</span>默认显示行数:</td>
            			<td class="form_data"><s:select name="model.rowcount" list="#{'10':'10行','20':'20行','30':'30行','40':'40行'}" theme="simple"></s:select></td>
            		</tr> 
            		 <tr>  
            			<td class="form_title">访问地址:</td>
            			<td class="form_data">
            				<img src="iwork_img/link.png" border="0">&nbsp;[sysDem_DataList.action?demUUID=<s:property value="model.uuid"/>]
            				<s:hidden  id="uuid"  name="model.uuid" theme="simple"/>  
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
            		<tr>
            			<td colspan="2">
            				<fieldset style="color:#ff0000;padding:10px">
            				<legend class="td_memo_title">属性设置</legend>
            					<table width="100%">
            						<tr>
			            			<td class="form_title"><span style="color:red;">*</span>是否记录修改日志:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isLog" cssStyle="border:0px;" onclick="checkLogSel()" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            			<td class="form_title"><span style="color:red;">*</span>是否允许EXCEL导出:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isExp" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            		</tr> 
			            		<tr>
			            		<td class="form_title"><span style="color:red;">*</span>是否显示修改日志:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isShowLog" cssStyle="border:0px;" cssClass="{maxlength:32}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            		
			            			<td class="form_title"><span style="color:red;">*</span>是否允许EXCEL导入:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isImp" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            		</tr> 
			            		<tr>
			            			<td class="form_title"><span style="color:red;">*</span>是否允许新增:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isAdd" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            			<td class="form_title"><span style="color:red;">*</span>是否是主数据:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isMdm" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            		</tr> 
			            		<tr>
			            			<td class="form_title"><span style="color:red;">*</span>是否允许删除:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isDel" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            			<td class="form_title"><span style="color:red;">*</span>是否在主数据管理中心显示:</td>
			            			<td class="form_data">
			            				<s:radio  name="model.isMdmCenter" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>
			            			</td>
			            		</tr> 
            					</table>
            				</fieldset>
            			</td>
            		</tr>
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
           <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="grouptree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
           
</body>
</html>
