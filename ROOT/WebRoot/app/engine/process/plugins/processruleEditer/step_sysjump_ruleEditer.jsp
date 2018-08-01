<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>编辑跳转规则</title>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
	.textinput {
		padding: 5px; 
		border: 1px solid #ABADB3;
		}
	.rule-design {
		width: 400px;
		height: 100px;
		resize: none;
		}
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	//将字符串转换为1010的字符串
	function parseString(inputStr){
	
		return true;
	}
	//IE下设置光标位置
	function setCaret(textObj){ 
		if(textObj.createTextRange){    
			textObj.caretPos=document.selection.createRange().duplicate();
		} 
	}
	//在光标位置插入
	function insertAtCaret(textObj,textFeildValue){ 
		if(document.all&&textObj.createTextRange&&textObj.caretPos){ 
			  var caretPos=textObj.caretPos;
			  caretPos.text=caretPos.text.charAt(caretPos.text.length-1)==''?textFeildValue+'':textFeildValue;
		}else if(textObj.setSelectionRange){     
			  var rangeStart=textObj.selectionStart;
			  var rangeEnd=textObj.selectionEnd;
			  var tempStr1=textObj.value.substring(0,rangeStart);      
			  var tempStr2=textObj.value.substring(rangeEnd);
			  textObj.value=tempStr1+textFeildValue+tempStr2;
			  textObj.focus();
			  var len=textFeildValue.length;
			  textObj.setSelectionRange(rangeStart+len,rangeStart+len);
			  textObj.blur();
		}else {
			textObj.value+=textFeildValue;
		} 
	} 
	
	$(document).ready(function(){
		var str =  $("#sjExpression").val();
		var str2 = str.replace(/_/g,"&");
		$("#rule-edit").val(str2);//初始化系统规则表达式
		
	   if($.browser.msie){//判断是否IE
	       $("#rule-edit")
	       .click(function(){
	         setCaret($(this).get(0));
	       })
	       .select(function(){
	         setCaret($(this).get(0));
	       })
	       .keyup(function(){
	         setCaret($(this).get(0));
	       });
	   }
	    $("a.easyui-linkbutton")
	     .click(function(){
	       var oldString = $("#rule-edit").val();
	       insertAtCaret($("#rule-edit").get(0),$(this).attr("name"));
	       var newString = $("#rule-edit").val();
	       if(parseString(newString)){
		   		$("#rule-edit").val(newString);
		   }else{
				art.dialog.tips("您的输入不合法");
				$("#rule-edit").val(oldString);
		   }
	     });
	   $("#funTree").tree({   
          url: 'stepSysJump_ruleEditer_JsonData.action?actDefId=<s:property value="actDefId"/>&prcDefId=<s:property value="prcDefId"/>&actStepDefId=<s:property value="actStepDefId"/>',   
          onClick:function(node){
             $(this).tree('toggle', node.target); 
         	 if(typeof(node.attributes)=="object"){
         	 	var oldString = $("#rule-edit").val();
          	 	insertAtCaret($("#rule-edit").get(0),node.attributes.fun);
          	 	var newString = $("#rule-edit").val();
	       		if(parseString(newString)){
		   			$("#rule-edit").val(newString);
		   		}else{
					art.dialog.tips("您的输入不合法"); 
					$("#rule-edit").val(oldString);
		   		}
          	 }else{
          	 	return false;
          	 }
		  } 
      });
      $("#rule-edit").focus();
	});
	</script>
</head>
<body >
<table width="450px" cellspacing="10">
	<tr>
		<td colspan="2">
			<table>
				<tr>
					<td><textarea name="rule-edit" id="rule-edit" class="rule-design" ></textarea></td>
					<td><a id="clearContent" href="javascript:clear();" style="width:50px;vertical-align:bottom;">清空</a></td>
				</tr>	
			</table>
			 <br><span style="color:#666;font-size:12px">例如：&#36;{印章使用申请单.SQR} == "张三"</span>
		</td>
	</tr>
	<tr>
		<td width="65%">
			<div class="textinput" style="height:310px;width:240px;overflow:auto;">
				<ul id="funTree"></ul>
			</div>
		</td>
		<td width="35%">
			<div class="textinput" style="height:300px;">
				<table cellspacing="5">
					<tr><td><a href="#" name=" + " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:13px">&nbsp;+</span></a></td><td><a href="#" name=" - " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:20px">&nbsp;-</span></a></td></tr>
					<tr><td><a href="#" name=" * " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">&nbsp;×</span></a></td><td><a href="#" name=" / " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">&nbsp;÷</span></a></td></tr>
					<tr><td><a href="#" name=" ( " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">&nbsp;(&nbsp;</span></a></td><td><a href="#" name=" ) " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">&nbsp;)&nbsp;</span></a></td></tr>
					<tr><td><a href="#" name=" > " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">>&nbsp;</span></a></td><td><a href="#" name=" < " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px"><&nbsp;</span></a></td></tr>
					<tr><td><a href="#" name=" >= " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">≥&nbsp;</span></a></td><td><a href="#" name=" <= " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">≤&nbsp;</span></a></td></tr>
					<tr><td><a href="#" name=" != " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:12px"><></span></a></td><td><a href="#" name=" == " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">=&nbsp;</span></a></td></tr>
					<tr><td><a href="#" name=" % " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:15px">%</span></a></td><td><a href="#" name=" ! " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:13px">非</span></a></td></tr>
					<tr><td><a href="#" name=" && " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:13px">且</span></a></td><td><a href="#" name=" || " style="width: 30px;" class="easyui-linkbutton"><span style="font-size:13px">或</span></a></td></tr>
				</table>
			</div>
			<div style="height:50px;">
			</div>
		</td>
	</tr>
</table>
		<div region="west" border="false" style="text-align: right; height: 30px; line-height: 30px;padding-top:5px;">
			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >验证</a> 
			<a id="btnEp" class="easyui-linkbutton" icon="icon-add" href="javascript:doSubmit();" >添加</a> 
			<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
		</div>
	<s:hidden name="sjExpression"></s:hidden>
</body>
<script type="text/javascript">
	function clear(){
		document.getElementById("rule-edit").value = "";
	}
	function cancel(){
			try{
				api.close(); 
			}catch(e){
				window.close();
			}
	}
	function doSubmit(){  
				var defaultField = "editForm_model_sjExpression";
				var origin = artDialog.open.origin; 
				var v = origin.document.getElementById(defaultField);
			if(v!=null){
				var rule = document.getElementById("rule-edit").value;
				v.value = rule;
				cancel();
			}
	}
</script>
</html>
