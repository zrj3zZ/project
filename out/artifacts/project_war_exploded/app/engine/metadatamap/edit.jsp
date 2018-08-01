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
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		var mainFormValidator;
			$().ready(function() {
				mainFormValidator =  $("#editForm").validate({
					debug:false
				});
			 	mainFormValidator.resetForm(); 
			 	var fieldtype =  $("input[name='model.fieldtype']:checked").val();
			    selectType(fieldtype); 
			});
		function doSubmit(){ 
			//var fieldtype = $("#fieldtype").val();
			var fieldtype = $("input[name='model.fieldtype']:checked").val();
			selectType(fieldtype); 
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){ 
					return;
			}else{
				  var options = {
						error:function(){
				           alert("保存失败！");
				        },
						success:function(responseText, statusText, xhr, $form){
				           if(responseText=="success"){
				               alert("保存成功！",2);
				               api.close();
				           }
				           else if(responseText=="error"){
				              alert("保存失败！");
				           }else{
				          	 alert(responseText);
				           } 
				        }
			   	  };
				  $('#editForm').ajaxSubmit(options); 
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
		function selectType(val){
			if(val=='日期'||val=='日期时间'){ 
				$("#fieldLength").rules("remove","required");
				document.forms["editForm"].fieldLength.value = "";
				document.forms["editForm"].fieldLength.disabled=true;
			}else{
				//$("#fieldLength").rules("remove","required");
				//$("#fieldLength").rules("add","required:true");
				document.forms["editForm"].fieldLength.disabled="";
			}
		}
		function close(){
			try{
				api.close();
			}catch(e){
				parent.close(); 
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
            <s:form name="editForm" id="editForm"  action="sysEngineMetadataMap_updateinfo.action"  theme="simple">
            <table width="100%" border="0" cellspacing="4" cellpadding="5">
			  <tr>
			  <td class="form_title"><span style="color:red">*</span>字段标题:</td>
			    <td class="form_data"><s:textfield name="model.fieldtitle" id="fieldtitle" cssClass="{required:true}"  theme="simple"></s:textfield> <!--  <a href="javascript:autoCreateKey()">生成</a>--></td>
			  </tr>
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>字段名称:</td>
			    <td class="form_data"><s:property value="model.fieldname"/><s:hidden name="model.fieldname" id="fieldname"   theme="simple"></s:hidden></td>
			  </tr>
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>字段类型:</td>
			    <td class="form_data">
			    	<s:radio  id="fieldtype"  name="model.fieldtype" onclick="selectType(this.value)" theme="simple" cssStyle="width:20px;border:0px;" list="#{'字符':'字符','数值':'数值','日期':'日期','日期时间':'日期时间','文本':'文本'}"></s:radio>
			    </td> 
			  </tr>
			  <tr>
			    <td class="form_title">长　　度:</td>
			    <td class="form_data"><s:textfield name="model.fieldLength" id="fieldLength" cssStyle="width:40px;" cssClass="{required:true}"  theme="simple"></s:textfield></td>
			  </tr>
			  <tr>
			    <td class="form_title"><span style="color:red">*</span>是否允许为空:</td>
			    <td class="form_data">
			    	<s:radio name="model.fieldnotnull" cssStyle="border:0px;" list="#{'0':'是','1':'否'}"></s:radio>
			    </td>
			  </tr>
			  <tr>
			    <td class="form_title">默认值:</td> 
			    <td class="form_data">
					<s:textfield name="model.fielddefault"  theme="simple"></s:textfield>
			    </td>
			  </tr>
			</table>	
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.metadataid"/>
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:close();">取消</a>
            </div>
</body>
</html>
