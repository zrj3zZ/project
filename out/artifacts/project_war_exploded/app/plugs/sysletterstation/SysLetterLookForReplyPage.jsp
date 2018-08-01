<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>查看站内信</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="/iwork_js/jqueryjs/jquery.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/sysletters/sysletters.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/sysletters/sysletterpaopao.css"/>
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"  > </script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"  ></script>
	
  <script type="text/javascript">
          longPolling = function () {
                 	 var win = document.getElementById('hrygzz').contentWindow; 
                 	 var beforeId = document.getElementById('beforeId').value;
                 	 var receiveUserId = document.getElementById('receiveUserId').value;
                 	 var letterId = document.getElementById('letterId').value;
                 	 var obj=document.getElementById("hrygzz");
                     $.ajax({
                         url: "iwork_sys_letter_long_request.action?beforeId="+beforeId+"&receiveUserId="+receiveUserId+"&letterId="+letterId,
                         data: "",
                         dataType: "text",
                         timeout: 60000,//超过1分钟时走error
                         error: function (XMLHttpRequest, textStatus, errorThrown) {
                             if (textStatus == "timeout") { // 请求超时
                                      longPolling(); // 递归调用
                                 
                                 // 其他错误，如网络错误等
                                 } else { 
                                      longPolling();
                                 }
                             },
                         success: function (data, textStatus) {
                             if(data!=null&&data!=''){
                             	document.getElementById('beforeId').value = data;
 	    						obj.src = "iwork_sys_letter_look_for_reply.action"+"?letterId="+letterId;
                             }
                             if (textStatus == "success") { // 请求成功
                                longPolling();
                             }
                         }
                     });
                 } 
           
           $(function(){
    			longPolling();
			});
           
  
  
   //点击回复文本框显示回复文本域和 回复按钮
   function showReply(){
   	var inputValue = document.getElementById('inputValue');
   	var textareaValue = document.getElementById('textareaValue');
   	inputValue.style.display = 'none';
   	textareaValue.style.display = 'block';
   	
   }
   //点击回复,则隐藏单选地址簿
   function show_muti(){
    var replyType = document.getElementById('replyType');
   	var no_reply_all = document.getElementById('no_reply_all');
   	if(replyType.checked){
   		no_reply_all.style.display = 'none';
   	}else{
   		no_reply_all.style.display = 'block';
   	}
   }
   
    //回复操作
   function replyPage(){
   	  var letterId = document.getElementById("letterId").value;
      var receivePersonsIds = document.getElementById('toUserId').value;
      var receivePersonsNames = document.getElementById('toUserName').value;
      var createUserId = document.getElementById('createUserId').value;
      var content = document.getElementById('letterReply').value;
 	  var win = document.getElementById('hrygzz').contentWindow; 
   	  var url = "iwork_sys_letter_sent_reply.action";
	  var option={
			type: "post",
			url: url,
			data: "letterId="+letterId+"&receivePersonsIds="+receivePersonsIds+"&receivePersonsNames="+receivePersonsNames+"&content="+content+"&createUserId="+createUserId,
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			cache:false,	
			success: function(msg){
				if(msg=="succ"){
					win.location.reload();
					//window.location.reload();
				}else{
					alert('发送失败');
					return false;
				}
			}
		}
		$.ajax(option);
 	    var obj=document.getElementById("hrygzz");
 	    obj.src = "iwork_sys_letter_look_for_reply.action"+"?letterId="+letterId;
  	}
  	//休眠函数
   function sleep(n) {
　　var start = new Date().getTime();
　　while(true)　if(new Date().getTime()-start > n) break;
　　}
	
	//收取回复
  function refresh(){
    var letterId = document.getElementById("letterId").value;
    var obj=document.getElementById("hrygzz");
    var changeType = "1"//标记更改未读/已读状态,是从收取回复按钮发起
 	obj.src = "iwork_sys_letter_look_for_reply.action"+"?letterId="+letterId+"&changeType="+changeType;
  }
  function go_close(){
  	window.close();
  	window.opener.location.reload();
  }
  </script>
  </head>
  
  <body>
  <form action="" id="form" name="form">
  	
  	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
 	<tr>
   		<td>
   		    <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
      	  		<tr>
        			<td width="51%" align="right" valign="top" style="margin:10px;">	
        				<fieldset style="padding:8px;width:90%;border:1px solid #ACBCC9; margin-left:10px; margin-right:10px;line-height:2.0; text-align:left;height:300px; overflow:auto;" >
        					<legend style="vertical-align:middle; text-align:left;background:#FFFFFF" >
        						<img src="iwork_img/sysletter/letter_sent.png" width="16" height="16" border="0">历史回复内容
        					</legend>
    						<iframe name="hrygzz" id="hrygzz"  marginheight="0" marginwidth="0" frameborder="0"  src="iwork_sys_letter_look_for_reply.action?letterId=<s:property value="sysletterscontent.id"/>" align=center width=100% onload="document.all['hrygzz'].style.height=hrygzz.document.body.scrollHeight+20;">
   							</iframe>
   						</fieldset>
	   				</td>
   	 	 		</tr>
  			</table>
  		</td>
 	</tr>
 	<tr>
 		<td>
    		<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
      	  		<tr>
        			<td width="51%" align="right" valign="top" style="margin:10px;">	
        				<fieldset style="padding:8px;width:90%;border:1px solid #ACBCC9; margin-left:10px; margin-right:10px;line-height:2.0; text-align:left;overflow:auto;" >
        					<legend style="vertical-align:middle; text-align:left;background:#FFFFFF" >
        						<img src="iwork_img/sysletter/letter_sent.png" width="16" height="16" border="0">回复区域
        					</legend>
        					<table  width="95%" >
        						<tr>
							 			<td colspan="4">
							 				<input type="button" value="收取回复" onclick="refresh();"/>
							 				<input type="button" value="关闭" onclick="javascript:go_close();">
							 				<input type="hidden" id="toUserId" value="<s:property value="toUserId"/>"/>
							 				<input type="hidden" id="createUserId" name="sysletterscontent.createUserId" value="<s:property value="sysletterscontent.createUserId"/>"/>
											<input type="hidden" id="letterId" name="sysletterscontent.id" value="<s:property value="sysletterscontent.id"/>"/>	
							 				<input type="hidden" id="toUserName" name="toUserName" value="<s:property value="toUserName"/>"/>
							 				<input type="hidden" id="beforeId" value="<s:property value="beforeId"/>"/>
							 				<input type="hidden" id="receiveUserId" value="<s:property value="receiveUserId"/>"/>
							 			</td>
							 	</tr>	
        						<tbody id="inputValue">
									<tr>
										<td width="100%" colspan="4">
											<input type="text" style="width:490px;height:23px;font-style: italic;color: 888888" value="点击回复..." onclick="showReply();"></input>
										</td>
									</tr>
								</tbody>
								<tbody id="textareaValue" style="display: none">
							 		<tr>
							 			<td colspan="4">
							 				<textarea  name="letterReply" id="letterReply"  style="width:490px;height:80px;"  ></textarea> <span style="color:red">*</span>
											<input type="button" onclick="javascript:replyPage();" value="回复"/>
										</td>
							 		</tr>
							 		
								</tbody>
        					</table>
        				</fieldset>
	   				</td>
   	 	 		</tr>
  			</table>
  		</td>
 	</tr>
 	
 </table>  
</form>
</body>
</html>
