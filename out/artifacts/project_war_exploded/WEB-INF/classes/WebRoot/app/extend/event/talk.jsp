<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
		.groupTitle{
			font-family:黑体;
			font-size:12px;
			text-align:left;
			color:#666;
			height-line:20px;
			padding:5px;
			padding-left:15px;
		}
		.itemList{  
			font-family:宋体;
			font-size:12px;
			height:200px;
			padding-left:15px;
		}
		.itemList td{
			list-style:none;
			height:20px;
			padding:2px;
			padding-left:20px;
			
		}
		.itemList tr:hover{
			color:#0000ff;
			cursor:pointer;
		}
		.itemList  td{
			font-size:12px;
		}
		
		.itemicon{
			padding-left:25px;
			background:transparent url(iwork_img/pin.png) no-repeat scroll 0px 3px;
		}
		.selectBar{
			border:1px solid #efefef;
			margin:5px;
			background:#FDFDFD;
		}
		.selectBar td{
			vertical-align:middle;
			height:20px;
		}
		
		.selectBar td linkbtn{
			color:#0000FF;
			text-decoration: none;
		}
		.talkTitle{
			padding-top:5px;
			vertical-align:bottom; 
			padding-left:25px;
			background:transparent url(iwork_img/comment.gif) no-repeat scroll 5px 3px;
			padding-bottom:2px;
			background-color:#efefef;
			border:1px solid #fff;
			height:20px
		}
		.talkUser{
			padding-top:10px;
			vertical-align:bottom; 
			padding-left:20px;
			background:transparent url(iwork_img/user_comment.png) no-repeat scroll 5px 3px;
			padding-bottom:2px;
		}
		.talkContent{
			
			border:1px solid #efefef;
			background-color:#efefef;
			padding:5px;
			margin-left:40px;
			line-height:18px;
		}
	form.ifromMain label.error, label.error {
		/* remove the next line when you have trouble in IE6 with labels in list */
		color: red;
		font-style: italic
	}
	</style>	
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
	mainFormValidator =  $("#editForm").validate({
	 });
	 mainFormValidator.resetForm();
	});
	var submitFlag=0;
	function doSubmit(){
		var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
		return false;
		}
		var content = $("#talkTxt").val();
		if(content==""){
			alert('请填写留言');
			$("#talkTxt").focus();
			return false;
		}
		if (submitFlag == 1) {
	              alert('数据已经提交，请勿再次提交。');
	              return ;
	          }
	          else {
	              submitFlag = 1;               
	          }
		var username = $("#username").val();
		var instanceid = $("#instanceid").val();
		
		var pageUrl = "zqb_event_talk_commit.action"; 
			$.post(pageUrl,{instanceid:instanceid,content:content},function(data){
	      			var item = "<div>"+"<div class='talkUser'>"+username+"：</div><div class='talkContent'>"+content+"</div></div>";
				$("#talsList").append(item);
				 $("#talkTxt").val("");
				  submitFlag = 0; 
	      	});
		
	}
	
	</script>
  </head> 
    <body class="easyui-layout">
      <div region="north" border="false" style="height:200px" >
        <fieldset class="selectBar">
      		 <div style="font-weight:bold;padding-bottom:5px;font-size:14px;">
      		<s:property value="hash.SXMC"  escapeHtml="false"/>
      		</div> 
      		 <div style="padding-left:20px;">
      		<s:property value="hash.SXGY"  escapeHtml="false"/>
      		</div> 
      	</fieldset> 
      	<hr style="border:1px;"/>
      </div>
      <div region="center"  border="false"  >
      	<form id="editForm" name="editForm">
      	<div style="padding:10px">
      		<div class="talkTitle">
      		留言板
      		</div>
      		<div id="talsList">
      		<s:iterator value="talkList"> 
      		<div>
      			<div class="talkUser"><s:property value="FSR"/>：</div>
      			<div class="talkContent">
      				<s:property value="CONTENT"/>
      			</div>
      		</div>
      		</s:iterator>
      		</div>
      		<div style="padding:10px;padding-left:40px;">
      			<textarea name="talkTxt" id="talkTxt" rows="5" cols="70" class='{maxlength:512,string:true}'></textarea><span  style="padding-bottom:20px" ><input onClick="doSubmit()" type="button" value="提交"/></span>
      		</div>
      		<div style="text-align:right;padding-right:10px"></div>
      	</div>
      	<s:hidden name="instanceid"></s:hidden>
      	<s:hidden name="userid"></s:hidden>
      	<s:hidden name="username"></s:hidden> 
      	</form>
  </div>
  </body>
</html>
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
					flag = true;
					break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>