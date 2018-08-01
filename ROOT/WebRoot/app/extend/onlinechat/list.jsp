<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>在线互动-消息历史记录</title>
<!-- Set render engine for 360 browser -->
<meta name="renderer" content="webkit">
<!-- No Baidu Siteapp-->
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link rel="alternate icon" href="../../../favicon.ico">
<link rel="stylesheet" href="../../../assets/css/amazeui.min.css">
<link rel="stylesheet" href="../../../assets/css/app.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<!-- <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"> -->
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"  ></script>
<style>
#getContent {
    background-image: url("../../../images/linkbutton_bg.png");
    background-repeat: no-repeat;
    border-radius: 5px;
    color: #444;
}
#getContent {
    background-position: right 0;
    cursor: pointer;
    display: inline-block;
    height: 24px;
    outline: medium none;
    padding-right: 18px;
    text-decoration: none;
}
.title {
	text-align: center;
}

.chat-content-container {
	height: 29rem;
	overflow-y: scroll;
	border: 1px solid silver;
}

a.lm_addr,a.lm_addr:link,a.lm_addr:visited {
	background: #fff none repeat scroll 0 0;
	color: #494949;
	cursor: pointer;
	display: block;
	height: 20px;
	line-height: 20px;
	outline: medium none;
	overflow: hidden;
	text-align: left;
	text-decoration: none;
	text-overflow: ellipsis;
	white-space: nowrap;
	width: auto;
}

a.lm_addr:hover {
	background: #ffeec2 none repeat scroll 0 0;
}

.dialog_mode a.lm_addr:hover {
	background: #fff6df none repeat scroll 0 0;
}
.searchtitle{
	width: 50px;
}
.searchdata{
	width: 250px;
}
td{
	padding-top: 10px;
}
</style>
</head>
<body>
<div style="width: 100%;min-width: 800px;">
<div style="min-width:800px;width:50%;margin:auto;overflow:hidden;font-family: serif;font-size: 16px;color: #9933CC;border: solid 1px #ccc; background-color: #FFFFEE;">
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr>
  <td style='padding-top:10px;padding-bottom:10px;width: 80%'> 
	<table width='100%' border='0' style="margin: auto;" cellpadding='0' cellspacing='0'> 
		<tr> 
	        <td class="searchtitle">时间:</td>
			<td>
				<input type='text' onfocus="WdatePicker()" class = "{required:true}"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='<s:property value='startdate'/>' >
				到 <input type='text' onfocus="WdatePicker()" class = "{required:true}" onchange="checkRQ()" style="width:100px" name='ENDDATE' id='ENDDATE'  value='<s:property value='enddate'/>' >
			</td>
		</tr>
		<tr>
			<td class="searchtitle">内容:</td>
			<td class= "searchdata">
				<input type='text' class = '{maxlength:128,required:false}'  style="width:224px" name='CONTENT' id='CONTENT'  value='<s:property value='content'/>' > 
			</td>
		</tr>
		<tr>
			<td class="searchtitle">接收人:</td>
			<td class= "searchdata">
				<select id="selecttedvalue" onclick="setCompanyDisabled();">
					<option value="all">--空--</option>
					<s:iterator value="allName" status="ll">
						<option value="<s:property value="viewname"/>"><s:property value="viewname"/></option>
					</s:iterator>
				</select>
			</td>
		</tr>
		<tr>
			<td class="searchtitle">公司名:</td>
			<td class= "searchdata">
				<select id="selecttedcompany" onclick="setPersonDisabled();"></select>
			</td>
			<td><button id="getContent" icon="icon-search">查询</button></td>
		</tr>
	</table> 
  <td> 
  <td valign='bottom' style='padding-bottom:5px;'>
  	<button id="expContent" style="margin-top: -4px;" onclick="expLTJL();" >导出</button>
  	<button id="delContent" style="margin-top: -4px;" onclick="deleteLTJL();" >删除</button>
  </td>
<tr> 
</table> 
</div>
</div>
	<div id="content"></div>
	<input id="nickname" name="nickname" type="hidden" value="<s:property value='nickname' />" />
	<input id="chatName" name="chatName" type="hidden" value="<s:property value='chatName' />" />
</body>
<script type="text/javascript">
var socket;
function initWebSocket(){
	if (window.WebSocket) {
        if (window.WebSocket){
        	socket = new WebSocket('ws://${pageContext.request.getServerName()}:${pageContext.request.getServerPort()}${pageContext.request.contextPath}/websocket');
	    	// 处理服务器端发送的数据  
	    	socket.onmessage = function(event) {
	    	    addMessage(event.data);  
	    	};
        };
    }
}
// 把消息添加到聊天内容中  
function addMessage(message) {
    message = JSON.parse(message);
    var onlineName=message.onlineName;
    $.getJSON("zqb_onlinechat_getOnlineName.action",
			{onlineName:onlineName},
			function(data){
			$('#onlinename').empty();//清空resText里面的所有内容
			var html = '';
			html += "<div style=\"overflow:hidden;width:100%;\"><a class=\"lm_addr\" onmousedown=\"getSelectName(this.innerHTML);return false;\" nocheck=\"true\" unselectable=\"on\">--所有人--</a></div>";
			$.each(data.rows, function(commentIndex, comment){
				html += "<div style=\"overflow:hidden;width:100%;\"><a class=\"lm_addr\" onmousedown=\"getSelectName(this.innerHTML);return false;\" nocheck=\"true\" unselectable=\"on\">"+comment.viewname+""+comment.STATUS+"</a></div>";
				});
            $('#onlinename').html(html);
       });
}
function expLTJL() {
	var nickname = $("#nickname").val();
	var chatName = $("#chatName").val();
	var startdate = $("#STARTDATE").val();
	var enddate = $("#ENDDATE").val();
	var content = $("#CONTENT").val();
	var sendname = $("#selecttedvalue").val();
	var companyjc = $("#selecttedcompany").val();
	var url = "zqb_ltjl_know.action?nickname="+nickname+"&chatName="+chatName+"&startdate="+startdate+"&enddate="+enddate+"&content="+content+"&sendname="+sendname+"&companyjc="+companyjc;
	var encodeUrl = encodeURI(url);	
	if(confirm("确定要导出数据吗")){
		window.location.href = encodeUrl;
    }else{
	
    }
}
function deleteLTJL() {
	var nickname = $("#nickname").val();
	var chatName = $("#chatName").val();
	var startdate = $("#STARTDATE").val();
	var enddate = $("#ENDDATE").val();
	var content = $("#CONTENT").val();
	var sendname = $("#selecttedvalue").val();
	var companyjc = $("#selecttedcompany").val();
	var url = "zqb_ltjl_delete.action?nickname="+nickname+"&chatName="+chatName+"&startdate="+startdate+"&enddate="+enddate+"&content="+content+"&sendname="+sendname+"&companyjc="+companyjc;
	var encodeUrl = encodeURI(url);
	if(sendname=='all'&&startdate==''&&enddate==''&&content==''){
		if(confirm("确定要删除所有聊天数据吗？")){
			$.post(encodeUrl,function(data){
			    if(data=='删除失败'){
			    	alert("零条数据被删除！")
			    }else{
			    	alert("删除成功！")
			    	history.go(0);
			    }
			 });
	    }
	}else{
		if(confirm("确定要删除数据吗")){
			$.post(encodeUrl,function(data){
			    if(data=='删除失败'){
			    	alert("零条数据被删除！")
			    }else{
			    	alert("删除成功！")
			    	history.go(0);
			    }
			 });
	    }
	}
}
function setPersonDisabled() {
	var jsr = $("#selecttedvalue").val();
	var jsgs = $("#selecttedcompany").val();
	if(jsr == 'all' && jsgs != 'all'){
		$("#selecttedvalue").attr('disabled','disabled');
		$("#selecttedcompany").removeAttr('disabled');
	}else{
		$("#selecttedvalue").removeAttr('disabled');
		$("#selecttedcompany").removeAttr('disabled');
	}
}
function setCompanyDisabled() {
	var jsr = $("#selecttedvalue").val();
	var jsgs = $("#selecttedcompany").val();
	if(jsgs == 'all' && jsr != 'all'){
		$("#selecttedcompany").attr('disabled','disabled');
		$("#selecttedvalue").removeAttr('disabled');
	}else{
		$("#selecttedvalue").removeAttr('disabled');
		$("#selecttedcompany").removeAttr('disabled');
	}
}
</script>
<script type="text/javascript">
	function getOnlyChatContent() {
		var nickname = $("#nickname").val();
		var chatName = $("#chatName").val();
		var startdate = $("#STARTDATE").val();
		var enddate = $("#ENDDATE").val();
		var content = $("#CONTENT").val();
		var sendname = $("#selecttedvalue").val();
		var companyjc = $("#selecttedcompany").val();
		$.getJSON("zqb_onlinechat_recordContent.action",
				{nickname : nickname,
				chatName : chatName,
				startdate : startdate,
				enddate : enddate,
				content : content,
				sendname : sendname,
				companyjc : companyjc
				},function(data) {
				var html = '';
				$.each(data.rows,function(commentIndex, comment) {
						if(comment.ISME){
							if(comment.ONLYCHATNAME!=""){
								html += "<div style=\"margin:auto;overflow:hidden;width:50%;\"><table style=\"width:100%;\"><tr><td style=\"font-family: serif;font-size: 12px;color: #458B74;\">"+comment.CHATRECORDNAME+"&nbsp;对&nbsp;"+comment.ONLYCHATNAME+"&nbsp;"+comment.DATATIME+"</td></tr><tr><td style=\"font-family: serif;font-size: 12px;color: #000000;padding-left:10px;padding-top:3px;\">"+comment.CONTENT+"</td></tr></table></div>";
							}else{
								html += "<div style=\"margin:auto;overflow:hidden;width:50%;\"><table style=\"width:100%;\"><tr><td style=\"font-family: serif;font-size: 12px;color: #458B74;\">"+comment.CHATRECORDNAME+"&nbsp;"+comment.DATATIME+"</td></tr><tr><td style=\"font-family: serif;font-size: 12px;color: #000000;padding-left:10px;padding-top:3px;\">"+comment.CONTENT+"</td></tr></table></div>";
							}
						}else{
							if(comment.ONLYCHATNAME!=""){
								html += "<div style=\"margin:auto;overflow:hidden;width:50%;\"><table style=\"width:100%;\"><tr><td style=\"font-family: serif;font-size: 12px;color: #4876FF;\">"+comment.CHATRECORDNAME+"&nbsp;对&nbsp;"+comment.ONLYCHATNAME+"&nbsp;"+comment.DATATIME+"</td></tr><tr><td style=\"font-family: serif;font-size: 12px;color: #000000;padding-left:10px;padding-top:3px;\">"+comment.CONTENT+"</td></tr></table></div>";
							}else{
								html += "<div style=\"margin:auto;overflow:hidden;width:50%;\"><table style=\"width:100%;\"><tr><td style=\"font-family: serif;font-size: 12px;color: #4876FF;\">"+comment.CHATRECORDNAME+"&nbsp;"+comment.DATATIME+"</td></tr><tr><td style=\"font-family: serif;font-size: 12px;color: #000000;padding-left:10px;padding-top:3px;\">"+comment.CONTENT+"</td></tr></table></div>";
							}
						}
				});
				$('#content').html(html);
		});
	}
	$(function() {
		getOnlyChatContent();
		$("#getContent").click(function(){
			getOnlyChatContent();
	 	});
		var html="<option value='all'>--空--</option>";
		$.post("zqb_onlinechat_getAllCompany.action",function(data){
    		if(data != null || data != ''){
    			var values = data.split(",");
        	    for (var i = 0; i < values.length; i++) {
        	    	if(values[i] != null && values[i] != ''){
        	    		var valuessub = values[i].split("-");
        	    		html+="<option value='"+valuessub[1]+"'>"+valuessub[0]+"</option>";
        	    	}
    			}
        	    $("#selecttedcompany").html(html);
    		}
		});
	});
	function checkRQ(){
		if($("#ENDDATE").val()!=null&&$("#ENDDATE").val()!=""){
			if($("#ENDDATE").val()<$("#STARTDATE").val()){
				  alert("结束日期不能小于起始日期！");
				  $("#ENDDATE").val("");
				  return;
			}
		}
	}
</script>
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