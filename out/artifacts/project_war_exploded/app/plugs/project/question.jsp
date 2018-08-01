<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>  
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_js/plugs/gantt2/jquery-ui-1.8.4.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/plugs/gantt2/reset.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/plugs/gantt2/jquery.ganttView.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
    <style type="text/css">
    pre {
    		overflow: auto; 
			white-space: pre-wrap; /* css-3 */
			white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
			white-space: -pre-wrap; /* Opera 4-6 */
			white-space: -o-pre-wrap; /* Opera 7 */
			word-wrap: break-word; /* Internet Explorer 5.5+ */
		}
    .header td{
			height:20px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #ccc;
			border-right:1px dotted #ccc;
			
		}
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	.other {
		width:620px;
		border:1px solid #c5c5c5;
		background-color:#fff;
		margin-top:9px;
		margin-left:9px;
		margin-bottom:10px;
	}
	.other_title {
		padding-left:17px;
		font-size:14px;
		font-weight:bold;
		height:33px;
		line-height:33px;
		background:url(other_bg.jpg) repeat-x;
	}
	.other_main {
		padding:20px 10px 20px 10px;
		font-size:14px;
		color:#737373;
		line-height:24px;
		border-top:1px solid #c5c5c5
	}
	.other_bottom {
		padding:10px 15px 0px 10px;
		color:#737373;
		margin-bottom:20px;
		line-height:14px;
		height:14px;
		font-size:14px;
	}	
	.other_bottom span{
		float:right;
	}
	.comment {
		background-color:#f8f8f8;
		padding:10px 10px 10px 10px;
		margin-left:40px;
		margin-bottom:10px;
		margin-right:10px;
		
	}
	.comment_title {
		padding-left:10px;
		font-size:14px;
		font-weight:bold;
		height:30px;
		line-height:30px;
		color:#737373;
	}
	.comment_main {
		line-height:20px;
		padding-top:5px;
		padding-left:10px;
		border-top:1px dashed #c5c5c5;
		vertical-align:top;
		color:#737373
	}
	.comment_bottom span{
	padding-left:5px;
		float:right;
		vertical-align:top;
	}
	.comment_bottom {
		color:#737373;
		margin-bottom:5px;
		padding-top:10px;
		line-height:14px;
		height:14px;
		text-align:right;
	}
	.ask_main_top {
		width:600px;
		padding:10px 10px 0px 0px;
		color:#000;
		height:24px;
		line-height:24px;
	}
	.ask_main {
		width:600px;
		padding:10px 10px 30px 30px;
		color:#737373;
		line-height:20px;
	}
	.reTalk{
		padding:5px;
	}
	 .reTalkBtn{
		text-align:right;
	}
	.reTalkInput{
		display:none;
	}
	</style>	
	<script type="text/javascript">
	var api,W;
	var mainFormValidator;
	$().ready(function() {mainFormValidator = $("#iformMain").validate({});
				mainFormValidator.resetForm();
			});
		function showReTalkInput(id){
			$("#reTalkInputDiv"+id).slideToggle("fast");
		}
		function commitTalk(id){
			var valid = mainFormValidator.form(); //执行校验操作
			if (!valid) {
				return false;
			}
			var projectNo = $("#projectNo").val();
			var taskid = $("#taskid").val();
			var content = $("#reTalkArea"+id).val();
			var count = $("#reCount"+id).text();
			var attach=$("#ATTACH"+id).val();
			if(content==''){
				alert('请填写回复正文');
				$("#reTalkArea"+id).focus();
				return ;
			}else{
			var pageUrl = "zqb_project_question_commit.action";
				$.post(pageUrl,{id:id,projectNo:projectNo,taskid:taskid,reTalk:content,attach:attach},function(data){
				var alertMsg = "";
       			if(data=='success'){
       				alertMsg =  "回复成功";
       				var item ="<div class=\"comment_main\"><img  src='iwork_img/comments.png'>&nbsp;"+content+"</div>";
       				item+="<div class=\"comment_bottom\">"+$("#username").val()+" - "+$("#createdate").val()+"</div>";
       				$("#commit"+id).find(".comment_bottom:last").after(item);
       				 $("#reTalkArea"+id).val("");
       				 count=Number(count)+1;
       				 $("#reCount"+id).text(count++);
       				$("#ATTACH"+id).val("");
       				window.location.reload();
       			}else{ 
       				alertMsg = "回复异常，请稍后再试";
       				alert(alertMsg);
       			}
     		});
			}
			
		}
		function removeQuestion(type,instanceid){
			if(confirm("确认删除操作吗？")){
				var pageUrl ="zqb_project_question_remove.action";
				$.post(pageUrl,{type:type,instanceid:instanceid},function(data){
					if(data=='success'){
       					window.location.reload();
	       			}else{ 
	       				alert("删除异常");
	       			}
     			});
			}
		
		}
		
		function removeAllQuestion(projectNo,taskid,questionId){
			
			if(confirm("确认删除操作吗？")){
				var aa=document.getElementsByName("sel"+questionId);
				var instanceId=null;
				for(var i=0;i<aa.length;i++){
					if(aa[i].checked==true){
						if(instanceId==null){
							instanceId=""+aa[i].id
						}else{
							instanceId=""+aa[i].id+","+instanceId;
						}
					}
		           }
				var pageUrl =null;
				if(instanceId==null){
					pageUrl ="zqb_project_question_all_remove.action?projectNo="+projectNo+"&taskid="+taskid+"&questionId="+questionId;
				}else{
					pageUrl ="zqb_project_question_all_remove.action?projectNo="+projectNo+"&taskid="+taskid+"&questionId="+questionId+"&instanceId="+instanceId;
				}
				$.post(pageUrl,function(data){
					if(data=='success'){
						var aa=document.getElementsByName("sel"+questionId);
				           for(var i=0;i<aa.length;i++){
				        	   aa[i].checked=false;
				           }
       					window.location.reload();
	       			}else{ 
	       				alert("删除异常");
	       			}
     			});
			} 
		
		}
		
		function allXuan(selid){
			var sel=document.getElementById("sel"+selid);
			var aa=document.getElementsByName("sel"+selid);
			for(var i=0;i<aa.length;i++){
			        if(sel.checked==true){
			        	aa[i].checked=true;
			        }else
			            aa[i].checked=false;
			}
		}
		function selFirst(selid)
		   {
			  var sel=document.getElementById("sel"+selid);
		      var o=document.getElementsByName("sel"+selid);
		      var count=0,num=0;
		      for(var i=0;i<o.length;i++){
		        if(o[i].checked==true)
		        {
		          count++;
		        }
		        if(o[i].checked==false)
		        {
		          num++;
		        }
		      }
		      if(count==o.length)
		      {
		        sel.checked=true;
		      }
		      if(num>0)
		      {
		        if(sel.checked==true)
		        {
		          sel.checked=false;
		        }
		      }
		   }
		function uploadifyDialog(dialogId, fieldName, divId, sizeLimit, multi,
				fileExt, fileDesc) {
			if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
				alert('参数不正确');
				return ;
			}
			$.dialog({
				id:dialogId,
				title: '上传附件',
				content: 'url:showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'',
				pading: 0,
				lock: true,
				width: 650,
				height: 500
			});
			return ;
		}

		function showUploadifyPageATTACH(parentColId,parentDivId,divId,flag) {
			uploadifyDialog(parentColId,parentDivId,divId,'',flag,'','');
		}
		function showQuestion(title,projectNo,taskid,questionid){
	    	var pageUrl = "url:zqb_project_question.action?projectNo="+projectNo+"&taskid="+taskid+"&questionId="+questionid;  
				$.dialog({
					id:'projectTask',
					cover:true,
					title:"[问题反馈]"+title, 
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:750,
					cache:false,
					lock: true,
					height:650, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					content:pageUrl
				});
	    	}
	</script>
  </head>
<body class="easyui-layout">
    <div region="north" border="false" ></div>
	<div region="center"  border="false" >
			<s:property value="html" escapeHtml="false"/>
			<s:hidden name="projectNo"></s:hidden>
			<s:hidden name="taskid"></s:hidden>
			<s:hidden name="username"></s:hidden>
			<s:hidden name="createdate"></s:hidden>
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+?<>"{},;'[\]]/im;
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