<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>召开会议</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
    
    
    
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
		.groupTitle{
			font-family:黑体;
			font-size:16px;
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
		.tips{
			margin:15px;
			padding:10px;
			background:#f6ffcb;
			border:1px solid #efefef;
			color:#666;
			line-height:16px;
		}
	</style>
	<script type="text/javascript">
		var api,W;
		var mainFormValidator;
		$().ready(function() {
		try{
			api= frameElement.api;
			W = api.opener;	
		}catch(e){}
		});
		
		function setStatus(){
		
			var pageUrl = "zqb_meeting_setStatus.action"; 
			var instanceid = $("#instanceid").val();
			var opreatorType = $("#opreatorType").val();
			var meettime = $("#meettime").val();
			if(meettime==''){
				alert("会议召开日期不允许为空");
				return;
			}
			/* alert(mainFormValidator);
			 var valid = mainFormValidator.form(); //执行校验操作
			 alert(valid);
			if(!valid){
					return false;
			} */
				$.post(pageUrl,{instanceid:instanceid,opreatorType:opreatorType,meettime:meettime},function(data){ 
	       			if(data=='success'){
	       				api.close();
	       			}else{
	       				alert("更新失败");
	       				api.close();
	       			}
	       		});
		}
		function close(){
			api.close();
		}
	</script>	
  </head> 
    <body class="easyui-layout">
      <div region="center"  border="false" >
      	<s:form id="editForm" name="editForm">
      	<div style="padding:10px">
      	<fieldset>
      		<legend>确认最终会议召开信息</legend>
      		<div class="groupTitle"><s:property value="hash.MEETNAME"/></div>
      		<div class="groupTitle">会议召开时间:<input type="text" name="meettime" id="meettime"  class = "{required:true}"  onfocus="WdatePicker()" value="<s:date name="hash.PLANTIME" format="yyyy-MM-dd"/>"/></div>
      		<div class="tips">点击“设置”后，信息将提交至持续督导人员，持续督导人员可根据提交的信息反馈需要的资料内容</div>
      	</fieldset>
     </div>
  		<s:hidden id="instanceid" name="instanceid"></s:hidden>
  		<s:hidden id="opreatorType" name="opreatorType"></s:hidden>
  		 </s:form>
  </div>
    <div  region="south" border="false" style="padding:5px;">
      	<div style="text-align:right;">
         	<a href="javascript:setStatus();" class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">设置</a>
			<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-remove">关闭</a>
        </div>
      </div>
 
  </body>
</html>
		
		
		
		
		
		
		
		
		
		
		
		
		