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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		var mainFormValidator;
			$().ready(function() { 
			var setting2 = {
				async: {
						enable: true, 
						url:"sysEngineMetadataMap_treejson.action",
						dataType:"json",
						autoParam:["id","id"]
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
				$.fn.zTree.init($("#metadatatree"), setting2);
				mainFormValidator =  $("#editForm").validate({
					debug:true
				 });
				mainFormValidator.resetForm();
			});
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){ 
					return;
			}else{
				  var options = {
						error:errorFunc,
						success:successFunc 
			   	  };
				  $('#editForm').ajaxSubmit(options); 
			}
		}
      errorFunc=function(){
           alert("保存失败！");
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="success"){
               alert("保存成功！");
               api.close();
           }
           else if(responseText=="error"){
              alert("保存失败！");
           }else if(responseText=="ishave"){
              alert("当前[键/索引]已经存在，不能重复设置！");
           }else if(responseText=="noparam"){
              alert("参数不全，请联系管理员！");
           }   
      } 
		function autoCreateKey(){
			if($("#fieldname").val()==''){
				var title = $("#fieldtitle").val();
				var metadataid = $("#editForm_model_metadataid").val();
				if(title!=''&&metadataid!=''){
					var pageurl = 'sysEngineMetadataMap_fieldname_create.action?metadataid='+metadataid+'&metaMapTitle='+encodeURI(title);
						$.ajax({ 
					            type:'POST',
					            url:pageurl,
					            success:function(msg){ 
					               if(msg!=''){
					               		$("#fieldname").val(msg); 
					               } 
					            } 
					        });
				}
			}
		}
			//设置存储模型ID

		function setMetadataId(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("metadatatree");
			if(treeNode.isParent){
		 		zTree.expandNode(treeNode, true, null, null, true);
		 	}else{
				var v = "";
				var id = treeNode.id;
				var type =  treeNode.type;	
				
				if(type=='group'||type=='metadata'){
					return;
				}else{
					var v= treeNode.entityname;		
					alert(v);
					var node = treeNode.getParentNode();
					$("#metadataMapSel").val(treeNode.name);    
					$("#remetadataName").val(node.name);    
					$("#editForm_model_referTable").val(v);    
					$("#editForm_model_referField").val(id);   
					hideMetadataMenu(); 
				}
			}
		}

		function showMetadataMenu() {
				var cityObj = $("#metadataMenu"); 
				var metadataSelOffset = $("#remetadataName").offset();
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
		function check(obj){
			if(obj.value=='primarykey'){
				$("#fk_table").hide();
				$("#fk_field").hide();
				$("#remetadataName").attr("disabled",true); 
				$("#metadataMapSel").attr("disabled",true); 
				$("#remetadataName").rules("remove","required");//移除必填约束
				$("#metadataMapSel").rules("remove","required");//移除必填约束
			}else if(obj.value=='foreignkey'){
				$("#remetadataName").rules("add",{maxlength:32,required:true});//添加必填约束
				$("#metadataMapSel").rules("add",{maxlength:32,required:true});//添加必填约束
				$("#fk_table").show();
				$("#fk_field").show();
				$("#remetadataName").attr("disabled",false); 
				$("#metadataMapSel").attr("disabled",false); 
				
			}else if(obj.value=='unique'){
				$("#fk_table").hide();
				$("#fk_field").hide();
				$("#remetadataName").rules("remove","required"); //移除必填约束
				$("#metadataMapSel").rules("remove","required");//移除必填约束
				$("#remetadataName").attr("disabled",true); 
				$("#metadataMapSel").attr("disabled",true); 
			}else if(obj.value=='index'){
				$("#fk_table").hide();
				$("#fk_field").hide();
				$("#remetadataName").rules("remove","required"); //移除必填约束
				$("#metadataMapSel").rules("remove","required");//移除必填约束
				$("#remetadataName").attr("disabled",true); 
				$("#metadataMapSel").attr("disabled",true); 
			}
		}
	</script>
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
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <s:form name="editForm" id="editForm"  action="sysEngineMetadataKey_save.action"  theme="simple">
            <table width="100%" border="0" cellspacing="4" cellpadding="5">
            <tr>
			  <td class="form_title" width="30%"><span style="color:red">*</span>类型:</td>
			    <td class="form_data"  width="70%">
			    	<s:radio  id="fieldtype"  name="model.keyType" theme="simple" onclick="check(this)" cssStyle="width:20px;" list="#{'primarykey':'主键约束','foreignkey':'外键约束','unique':'唯一约束','index':'数据索引'}"></s:radio>
			    </td>
			  </tr>
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>字段列:</td>
			    <td class="form_data"><s:select  cssClass="{maxlength:32,required:true}"  name="model.keyField" headerKey="" headerValue="--选择列--" list="%{fieldlist}"  key="fieldname" listKey="fieldname"  listValue="fieldtitle"/></td>
			  </tr>
			  <tr id="fk_table" style="display:none">
			    <td class="form_title"><span style="color:red">*</span>关联主键表:</td>
			    <td class="form_data"> 
			    	<input id="remetadataName" type="text"   readonly="readonly"   value="" style="width:120px;background-color:#efefef;"/> 
			    	&nbsp;<a id="menuMetadataBtn" href="#" onclick="showMetadataMenu(); ">选择存储</a>
			    	 <s:hidden name="model.referTable" ></s:hidden>  
			    </td> 
			  </tr>
			  <tr id="fk_field" style="display:none">
			    <td class="form_title"><span style="color:red">*</span>关联主键列:</td>
			    <td class="form_data">
			    <input id="metadataMapSel" type="text"  readonly="readonly"    value="" style="width:120px;background-color:#efefef"/> 
			     <s:hidden name="model.referField" ></s:hidden> 
			    </td>
			  </tr>
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>是否允许级联删除</td>
			    <td class="form_data">
			    	<s:radio name="model.isCascade" list="#{'0':'是','1':'否'}" theme="simple"></s:radio>
			    </td>
			  </tr>
			  
			</table>	
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.metadataid"/>
                <s:hidden   name="model.c"/>
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:api.close();">取消</a>
            </div>
            <div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:220px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
            
</body>
</html>
