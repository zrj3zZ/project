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
						url:"sysEngineMetadata!openjson.action",
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
           alert("保存失败！");
        }
        
	    successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText!="0"){
	              alert("保存成功,请点击'表单布局'设置外观布局样式！");
	               	cancel();
	           }
	           else if(responseText=="error"){
	              alert("保存失败！");
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
					$("#editForm_model_metadataid").val(id);   
					hideMetadataMenu(); 
				}else{
					alert('请选择存储');
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
            <s:form name="editForm" id="editForm" action="sysEngineIForm!save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
            		
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>表单名称:</td>
            			<td class="form_data"><s:textfield name="model.iformTitle" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            		</tr> 
            		<tr>   
            			<td class="form_title"><span style="color:red;">*</span>映射存储:</td>
            			<td class="form_data">
            			<input id="metadataSel" type="text"  disabled value="<s:property value="metadataName"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuMetadataBtn" href="#" onclick="showMetadataMenu(); ">选择存储</a>
            			<s:hidden name="model.metadataid" cssClass="{maxlength:32,required:true}" theme="simple"/>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>表单类型:</td>
            			<td  class="form_data">  
            					<s:radio  name="model.metadataType" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'数据表单','1':'流程表单','2':'行项目子表'}" value="model.metadataType" theme="simple"/>
            			</td>
            		</tr>
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>目录分组:</td>
            			<td class="form_data">
								<input id="citySel" type="text"  disabled value="<s:property value="groupname"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); ">选择</a>
										<s:hidden  id="groupid"  name="model.groupid" theme="simple"/> 
						</td>
            		</tr> 
            		
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>访问方式:</td>
            			<td class="form_data">
            					<s:radio  name="model.visitType" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'登录验证','1':'匿名访问'}" value="model.metadataType" theme="simple"/>
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
            			<td class="form_title"><span style="color:red;">*</span>选择表单模板:</td>
            			<td class="form_data">
            				<table width="100%">
            				<tr>
	            				<s:iterator  value="templateList" status="num"> 
	            						<td> 
	            							<input type="radio" name="templateRadio" id="templateRadio<s:property value="id"/>"  value="<s:property value="filename"/>" class="{maxlength:32,required:true}" style="border:0px;"/><label for="templateRadio<s:property value="id"/>">
	            							<div><img src='<s:property value="perview"/>' border="0"></div>
	            							<div><s:property value="title"/></div> 
	            							</label>
	            						</td>
	            				</s:iterator>
            				</tr>
            				</table>
            			</td>
            		</tr> 
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.createdate"/>
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
        <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;height:240px;width:190px;overflow:auto;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
		<div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
