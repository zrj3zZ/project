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
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
			api=  art.dialog.open.api;
			W = api.opener;	
		}catch(e){}
		});
		
		function setStatus(){
		
			var pageUrl = "zqb_meeting_setStatus.action"; 
			var instanceid = $("#instanceid").val();
			var opreatorType = $("#opreatorType").val();
			var meettime = $("#meettime").val();
			var meet = meettime.replace(/-/g,"/");//替换字符，变成标准格式   
			var today=new Date();//取今天的日期   
			var setdate = new Date(Date.parse(meet));   
			if(meettime!=''){
				if(setdate>today){  
					$.post(pageUrl,{instanceid:instanceid,opreatorType:opreatorType,meettime:meettime},function(data){ 
		       			if(data=='success'){
		       				api.close();
		       			}else{
		       				alert("更新失败");
		       				api.close();
		       			}
		       		});
				}else{
					$.messager.confirm('确认','设置日期小于当日日期，确认设置?',function(result){ 
					 	if(result){
					 		$.post(pageUrl,{instanceid:instanceid,opreatorType:opreatorType,meettime:meettime},function(data){ 
				       			if(data=='success'){
				       				api.close();
				       			}else{
				       				alert("更新失败");
				       				api.close();
				       			}
				       		});
					 	}
					});
				}
			}
			if(meettime==''){
				alert("会议召开日期不允许为空");
				return;
			}
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
      		<table>
      		</table>
      		<div class="groupTitle"><s:property value="hash.MEETNAME"/></div>
      		<div class="groupTitle">会议召开时间:<input type="text" name="meettime" id="meettime"  class = "{required:true}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="<s:date name="hash.PLANTIME" format="yyyy-MM-dd HH:mm:ss"/>"/></div>
     </div>
  		<s:hidden id="instanceid" name="instanceid"></s:hidden>
  		<s:hidden id="opreatorType" name="opreatorType"></s:hidden>
  		 </s:form>
  </div>
    <div  region="south" border="false" style="padding:5px;">
      	<div style="text-align:right;">
         	<a href="javascript:void(0);" onclick="setStatus();" class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">确认延期</a>
			<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-remove">关闭</a>
        </div>
      </div>
 
  </body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
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