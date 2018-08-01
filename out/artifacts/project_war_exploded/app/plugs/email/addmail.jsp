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
   	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/plugs/emaileditor.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.leftDiv{ 
			background:#f5f5f5;
			width:150px;
		} 
		.rigetDiv{
			width:230px;
			border-left:1px solid #efefef;
			vertical-align:top
		} 
		input[type~=text] {
			width:500px;
			height:20px;
			padding:2px;
			padding-left:5px;
			color:#666;
			font-family:微软雅黑;
		}
		.headTitle{
			text-align:right;
			padding:2px;
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:#285586;
			font-werght:bold;
			line-height:28px;
		}
		.headData{
			text-align:left;
			padding:2px;
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:red;
			font-werght:bold;
			line-height:28px;
		}
		.attachItem{
			background-color: #f6f6f6;
			border: 1px solid #efefef;
			font: 11px Verdana, Geneva, sans-serif;
			padding: 5px;width: 200px;
			margin:2px;
		}
		.uploadBtn{
			padding:5px;
		}
		.uploadBtn a{
			padding:5px;
			padding-left:20px;
			background-color: #f6f6f6;
			border: 1px solid #efefef;
			 background-repeat:no-repeat;
			 background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			 background-position:2px 2px;
			 
		}
		.uploadBtns span{
			padding:2px;
			padding-left:0px;
			background-color: #f6f6f6;
			border: 1px solid #efefef;
			 background-repeat:no-repeat;
			background-repeat:no-repeat;
			 background-position:2px 2px;
		}
	</style>
	<script type="text/javascript">
	//发送动作
	function sentLetter(){
   		var flag = true;
  		var ccName = document.getElementById('_cc').value;
	  	var toName = $('#_to').val();
	  	   $.post("iwork_mail_ToAndCc.action",{to:toName,cc:ccName},function(data){
	  	   			if(data!="OK"){
	  	   				flag=false;
	  	   				art.dialog.tips('请填写有效名称!',2,'alert.gif');
	  	   			}else{
	  	   				flag = checkValidate();
	  	   				var form =  document.getElementById('editForm');
   						editor.sync();
   						if(flag==true){
   							form.action ="iwork_email_dosend.action";
   							form.submit();
   							// window.opener.location.href="app/plugs/email/save_tips.jsp";
							// window.close();
   						}
	  	   			}
	  	    });
   }
   	//存草稿箱
   	var editor;
	KindEditor.ready(function(K) {
		// editor = K.create('#htmlEditor', {});
		// editor = K.create('textarea[name="model._content"]');
		editor = K.init();
	});
   
 function savetounsend(){
 		
 	    var flag;
  		var ccName = document.getElementById('_cc').value;
	  	var toName = document.getElementById('_to').value;
	  	  
	  	   				var form =  document.getElementById('editForm');
   						editor.sync();
   							form.action ="iwork_mail_savetounsend.action";
   							form.submit();
   							window.opener.location.href="app/plugs/email/save_tips.jsp";
							window.close();
   				
 }
	
	   //校验输入内容
   function checkValidate(){
	  	var letterContent = document.getElementById('htmlEditor');
	  	var letterTitle = document.getElementById('_title');
	  	var receiveUserName = document.getElementById('_to');
       
	  	if(receiveUserName.value==''){
	  		art.dialog.tips('请选择收件人!',2,'alert.gif'); 
	  		return false;
	  	}
	  	if(letterTitle.value==''){
	  		art.dialog.tips('请填写邮件标题!',2,'alert.gif'); 
	  		return false;
	  	}
	  	if(letterContent.value==''){
	  		art.dialog.tips('请填写邮件正文!',2,'alert.gif'); 
	  		return false;
	  	}
  	return true;
  }
  //关闭提示是否保存
  function message(){
	  art.dialog.confirm( '确认是否保存?',function(r){ 
	  if(r){ 
	 	 savetounsend();
	  }
	  }); 
	}

  function uploadFile(){
  	 var pargurl = "iwork_email_upload.action";
  	 			art.dialog.open(pargurl,{
				 	id:"uploadFile",
					title: '上传附件',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width: 500,
					height: 400
				 });
  }

  	  function forwardReomve(uuid){
		$("#"+uuid).remove();
		var attachStr = $("#attachments").val();
		attachStr = attachStr.replace(uuid,'');
		attachStr = attachStr.replace(',,',',');
		$("#attachments").val(attachStr);
	  }
	  //取出发送人联想出来名字后面的“，”
	  function to_String(obj){
	  	var to=obj.value;
	  	if(to.substr(to.length-1,1)==","){
	  		$("#cn_to").val(to.substr(0,to.length-1));
	  	}
	  }
	  //取出抄送人联想出来名字后面的“，” 
	  function cc_String(obj){
	  	var cc=obj.value;
	  	if(cc.substr(cc.length-1,1)==","){
	  		$("#cn_cc").val(cc.substr(0,cc.length-1));
	  	}
	  }
	   //添加抄送
	  function addcs(){
	  	 $('#_cs').show();
	  	 $('#addcs').hide();
	  	 $('#delcs').show();
	  }
	    //删除抄送
	  function delcs(){
	  	 $('#_cs').hide();
	  	 $('#addcs').show();
	  	 $('#delcs').hide();
	  	// $('#_cc').val(null);
	  }
	  function showAddress(targetUserName,defaultField){
	  	 var url = "iwork_mail_addressbook.action?1=1";
	  	 if(targetUserName!=''){
			url+="&targetUserName="+targetUserName;
		}
		if(defaultField!=''){
			url+="&defaultField="+defaultField;
		}
		var v = document.getElementById(defaultField);
		if(v.value!=""){
			url+="&input="+v.value;
		}
		art.dialog.open(url,{
			id:"multiBookDialog",
			title: '邮件地址簿',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 650,
			height: 550,
			close:function(){
				
			}
		 });
	  
	  }
	</script>
	
</head>
<body>

<s:hidden  id='attachHtml'  cssClass = '{maxlength:256}'  name='attachHtml' ></s:hidden>
<s:form name="eidtForm" id="editForm" theme="simple">
<table width="100%">
	<tr>
		<td colspan="2">
			<div class="tools_nav" style="padding-left:5px;">
		 		<input type="button" onclick="sentLetter()" name="removeBtn" value="发送"/>
		 		<input type="button" onclick = "savetounsend()" name="removeBtn" value="存草稿"/>
		 		<input type="button" onclick ="window.parent.location.reload();" name="removeBtn"  value="关闭"/>
		 	</div>
		</td>
	</tr>
	<tr>
		<td>
			<div>
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 		<tr>
		 			<td class="headTitle">收件人</td> 
		 			<td class="headData"><s:textfield tabindex="1" name="cn_to" id="cn_to" theme="simple" onblur="to_String('')" cssClass="{maxlength:1000,required:true}"></s:textfield> <a href="###" onclick="showAddress('cn_to','_to');" title="多选地址薄" class="easyui-linkbutton" plain="true"><img src="iwork_img/mans.gif"/></a></td>
		 		      
		 		</tr>  
		 		<tr id=addcs style="display:none;" >
		 		<td  class="headTitle"></td>
		 		<td  ><a href="#" onclick="addcs()">&nbsp;添加抄送</a></td>
		 		</tr> 
		 	
		 		<tr id="_cs">
		 			<td  class="headTitle">抄送</td>
		 			<td class="headData"><s:textfield tabindex="2"  name="cn_cc" id="cn_cc" theme="simple" onblur="cc_String(this)" readonly="readonly"  cssClass="{maxlength:1000,required:false}"></s:textfield> <a href="###"  onclick="showAddress('cn_cc','_cc');" class="easyui-linkbutton" plain="true"><img src="iwork_img/mans.gif"/></a></td>
		 		</tr>
		 		<tr id=delcs style="display:none;" >
		 		<td  class="headTitle"></td>
		 		<td  ><a href="#" onclick="delcs()">&nbsp;删除抄送</a></td>
		 		</tr> 
		 		<tr> 
		 			<td class="headTitle">主题</td>
		 			<td class="headData">
		 			<s:textfield tabindex="3"  name="model._title" id="_title" theme="simple" cssClass="{maxlength:200,required:true}"></s:textfield> 
		 			</td>
		 		</tr>
		 		<tr> 
		 			<td class="headTitle"></td>
		 			<td >
		 			<s:radio name="model._isImportant" list="#{'0':'一般邮件','1':'重要邮件','2':'非常重要'}" listKey="key" listValue="value" value="'0'"/>
		 			</td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle" rowspan="2">附件</td>
		 			<td class="headData" >
						<div class="uploadBtns" id='mailAttachDiv'>
							<s:property value="attachHtml" escapeHtml="false"/>
						</div>
						<div><s:hidden  id='attachments'  cssClass = '{maxlength:256}'  name='model._attachments' ></s:hidden></div>
		 		    </td>
		 		</tr>
		 		<tr>
		 			<td class="headData" >
						<div class="uploadBtn">
							<a href="#" onclick="uploadFile()">添加附件</a>
							</div>
		 		    </td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">正文</td>
		 			<td class="headData"> 
						<textarea style="width:99%;height:200px;visibility:hidden;" name="model._content" id="htmlEditor"  cols="80" rows="12" >
						<s:property value="model._content"/>
						</textarea>
					</td>
		 		</tr>
		 		</table>
		 	</div>
		</td>
		<s:hidden  name="model._cc"  id="_cc" ></s:hidden>
		<s:hidden  name="model._to"  id="_to"></s:hidden>
		<input type="hidden" name="sentUserId" value=""/>
  	    <input type="hidden" id="receiveUserId" name="receiveUserId"/>
  	    <input type="hidden" id="Ownerid" name="Ownerid" value="<s:property value="id"/>"/>
  	    <input type="hidden" id="receiveUser" name="receiveUser"></input> 
  	    <s:hidden id="_relatedId" name="model._relatedId"></s:hidden>
  	    <s:hidden id="_mailType" name="model._mailType"></s:hidden> 
  	    <script>
  	    	$(function(){
 				$("#cn_to").val('<s:property value="toName"/>');
 				$("#cn_cc").val('<s:property value="ccName"/>');
 			});
  	    </script> 
	</tr>
</table>
</s:form>
</body>

</html>
