<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.iwork.app.conf.SystemConfig"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>ShiYanLou Chat</title>
<!-- Set render engine for 360 browser -->
<meta name="renderer" content="webkit">
<!-- No Baidu Siteapp-->
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="alternate icon" href="../../../favicon.ico">
<link rel="stylesheet" href="../../../assets/css/amazeui.min.css">
<link rel="stylesheet" href="../../../assets/css/app.css">
<!-- umeditor css -->
<%-- <link href="../../../ueditor/third-party/SyntaxHighlighter/shCoreDefault.css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="../../../ueditor/third-party/SyntaxHighlighter/shCore.js"></script> --%>
<script type="text/javascript" charset="utf-8" src="../../../ueditor/ueditor.parse.js"></script>
<script type="text/javascript" charset="utf-8" src="../../../ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="../../../ueditor/ueditor.all.js"> </script>
<script type="text/javascript" charset="utf-8" src="../../../ueditor/ueditor.all.min.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="../../../ueditor/lang/zh-cn/zh-cn.js"></script>
<!--[if (gte IE 9)|!(IE)]>
<script type="text/javascript" src="../../../assets/js/jquery.min.js"></script>
<![endif]-->
<!--[if lte IE 8 ]>
<script type="text/javascript" src="../../../iwork_js/jquery.min.js"></script>
<![endif]-->
<script type="text/javascript" src="../../../iwork_js/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8">
        window.UEDITOR_HOME_URL = location.protocol + '//'+ document.domain + (location.port ? (":" + location.port):"") + "/ueditor/";
</script>
<script type="text/javascript">
var socket;
function initWebSocket(){
	if (window.WebSocket) {
        if (window.WebSocket){
        	<%
        	//获取isHTTPS
        	boolean isHttps = SystemConfig._iworkServerConf.getIsHttps().equals("on");
        	if(isHttps){%>
        		socket = new WebSocket('wss://${pageContext.request.getServerName()}:${pageContext.request.getServerPort()}${pageContext.request.contextPath}/websocket');
        	<%}else{%>
        		socket = new WebSocket('ws://${pageContext.request.getServerName()}:${pageContext.request.getServerPort()}${pageContext.request.contextPath}/websocket');
        	<%}
        	%>
	    	// 处理服务器端发送的数据  
	    	socket.onmessage = function(event) {
	    	    addMessage(event.data);  
	    	};
        };
    }
}
function send(content,nickname,username,chatRecordName) {
    if (!socket || socket.readyState != 1) {
        initWebSocket();
    }
    socket.send(JSON.stringify({
            content : content,  
            nickname : nickname,
            onlyChat : username,
            chatRecordName : chatRecordName
        }));
}
/* function expLTJL() {
	var username = $('#username').val();
	var url = "zqb_ltjl_know.action?senduser="+encodeURI(username);
		var r = confirm("您确定要导出与'" + username + "'的聊天记录吗？");
		if (r == true) {
			window.location.href = url;
	}
}
function deleteLTJL() {
	var username = $('#username').val();
	var url = "zqb_ltjl_delete.action?senduser="+encodeURI(username);
	var r = confirm("您确定要删除与'" + username + "'的聊天记录吗？");
		if (r == true) {
			window.location.href = url;
		}
} */
</script>
<style>
.title {
  text-align: center;
}
.chat-content-container {
  height: 29rem;
  overflow-y: scroll;
  border: 1px solid silver;
}
a.lm_addr, a.lm_addr:link, a.lm_addr:visited {
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
</style>
</head>
<body>
<div style="margin: auto;width: 1100px;">
<div style="width: 800px;float: left;margin-top:10px;">
<!-- <div class="title">
    <div class="am-g am-g-fixed">
      <div class="am-u-sm-12">
        <h1 class="am-text-primary">
        
        </h1>
      </div>
    </div>
  </div> -->
  <!-- title end -->
  <!-- chat content start -->
  <div class="chat-content">
		<div class="am-g am-g-fixed chat-content-container">
			<div class="am-u-sm-12">
				<ul id="message-list" class="am-comments-list am-comments-list-flip"></ul>
			</div>
		</div>
  </div>
  <!-- chat content start -->
  <!-- message input start -->
  <div class="message-input am-margin-top">
    <div class="am-g am-g-fixed">
      <div>
        <form class="am-form">
          <div class="am-form-group">
          <script type="text/plain" id="myEditor"
							style="width: 100%;height: 15rem;"></script>
          </div>
        </form>
      </div>
    </div>
    <div class="am-g am-g-fixed am-margin-top" style="width: 100%;">
      <div class="am-u-sm-6" style="display: none;">
				<div id="message-input-nickname" 
					class="am-input-group am-input-group-primary">
					<span class="am-input-group-label"><i class="am-icon-user"></i></span>
					<input id="nickname" type="text" class="am-form-field"
						placeholder="Please enter nickname" value="<s:property value='sendUserName' />" readonly="readonly"/>
				</div>
			</div>
			<div class="am-u-sm-6" style="width:100%;">
				<!-- <button id="clear" type="button" class="am-btn am-btn-primary">
					清空发送对象
				</button> -->
				发送对象：<%-- <s:select list="sendUserList" headerKey="" headerValue="-发送所有人-" listKey="viewname" listValue="viewname" name="username" id="username"></s:select> --%>
				<input name="username" id="username" disabled="disabled" value="--所有人--" ></input>
				<button id="send" type="button" class="am-btn am-btn-primary">
					<i class="am-icon-send"></i> 发送
				</button>
				<button id="chatRecord" type="button" class="am-btn am-btn-primary">
					<i class="am-icon-send"></i> 消息记录
				</button>
				<!-- <button id="expRecord" type="button" onclick="expLTJL();" class="am-btn am-btn-primary" style="padding-left:5px;padding-right:5px;margin-left:30px;">
					</i> 导出消息记录
				</button>
				<button id="deleteRecord" type="button" onclick="deleteLTJL();" class="am-btn am-btn-primary" style="padding-left:5px;padding-right:5px;">
					</i> 删除消息记录
				</button> -->
			</div>
    </div>
  </div>
</div>
<input id="chatRecordName" type="hidden" value="<s:property value='chatRecordName' />"/>
<div style="width: 260px;margin-left: 10px;margin-top: 10px;float: left;" id="view">
			<div style="height:500px;overflow:auto;border:1px solid #cccccc;font-family:Arial,Helvetica,sans-serif;font-size:100%;line-height:1em;" id="onlinename" >
			</div>
</div>
<div style="clear: both;"></div>
<script id="container" type="text/plain" style="width:1024px;height:500px;"></script>
</div>
<!-- 实例化编辑器 -->
<script type="text/javascript">
	var ue = UE.getEditor("myEditor");
	var height=document.body.scrollHeight;
	var o=document.getElementById('view');//获得元素
	var onlinename=document.getElementById('onlinename');//获得元素
	o.style.height=height+'px';//设置宽度
	onlinename.style.height=height+'px';//设置宽度
	//使昵称框获取焦点
	$('#container')[0].focus();
	initWebSocket();
	// 点击Send按钮时的操作  
	$('#send').on('click', function() {  
	    var nickname = $('#nickname').val();
	    var username = $('#username').val();
	    var chatRecordName = $('#chatRecordName').val();
	    if (!ue.hasContents()) {    // 判断消息输入框是否为空  
	        // 消息输入框获取焦点  
	        ue.focus();  
	        // 添加抖动效果  
	        $('.edui-container').addClass('am-animation-shake');  
	        setTimeout("$('.edui-container').removeClass('am-animation-shake')", 1000);  
	    } else if (nickname == '') {    // 判断昵称框是否为空  
	        //昵称框获取焦点  
	        $('#nickname')[0].focus();  
	        // 添加抖动效果  
	        $('#message-input-nickname').addClass('am-animation-shake');  
	        setTimeout("$('#message-input-nickname').removeClass('am-animation-shake')", 1000);  
	    } else {  
	        // 发送消息  
	        send(ue.getContent(),nickname,username,chatRecordName);
	        /* socket.send(JSON.stringify({
	            content : ue.getContent(),  
	            nickname : nickname,
	            onlyChat : username,
	            chatRecordName : chatRecordName
	        }));   */
	        // 清空消息输入框  
	        ue.setContent('');  
	        // 消息输入框获取焦点  
	        ue.focus();  
	    }  
	});
	
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
	    if((message.nickname!=undefined)&&(message.date!=undefined)&&(message.content!=undefined)){
		    var messageItem = '<li class="am-comment '  
		            + (message.isSelf ? 'am-comment-flip' : 'am-comment')  
		            + '">'  
		            + '<a href="javascript:void(0)" ><img src="../../../'  
		            + (message.isSelf ? 'iwork_img/default_userImg.jpg' : 'iwork_img/default_userImg.jpg')  
		            + '" alt="" class="am-comment-avatar" width="48" height="48"/></a>'  
		            + '<div class="am-comment-main"><header class="am-comment-hd"><div class="am-comment-meta">' 
		            + '<a href="javascript:void(0)" '
		            + (message.isSelf?' ' :'onmousedown=getSelectOnName(this.innerHTML);return false;')
		            + 'class="am-comment-author">'  
		            + message.nickname + '</a> <time>' + message.date  
		            + '</time></div></header>'  
		            + '<div class="am-comment-bd">' + message.content  
		            + '</div></div></li>';  
		    $(messageItem).appendTo('#message-list');
		    // 把滚动条滚动到底部  
		    $(".chat-content-container").scrollTop($(".chat-content-container")[0].scrollHeight);
	    }
	}
	
	function getSelectName(object){
		if(object.indexOf("]")==-1){
			$("#username").val("--所有人--");
		}else{
			$("#username").val(object.substr(0,object.indexOf("]")+1));
		}
	}
	
	function getSelectOnName(object){
		$("#username").val(object);
	}
	
	$('#chatRecord').on('click', function() {  
		var username=$("#nickname").val();
		var chatName=$("#chatRecordName").val();
		var url = "zqb_onlinechat_record.action?nickname="+encodeURI(username)+"&chatName="+encodeURI(chatName);
		var target = "_blank";
		var iTop = (window.screen.availHeight-30-800)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-800)/2; //获得窗口的水平位置;
		var page = window.open('form/loader_frame.html',target,'width=800,height=800,top='+iTop+',left='+iLeft+',location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	});
</script>
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