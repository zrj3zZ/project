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
   function sentLetter(){
   	var form =  document.getElementById('form');
   	form.action ="iwork_sys_letter_create_sent.action";
   	form.submit();
   	window.opener.location.href="iwork_sys_letter_list.action";
	window.close();
   }
   //删除回复内容
   function del(objThis){
    var objTr = objThis.parentNode.parentNode;
	var id = objTr.getElementsByTagName("td")[0].childNodes[0].value;
	var gnl=confirm("确定要删除?刪除后无法恢复!");
		if (gnl==true){
			var option={
				type: "post",
				url: "iwork_sys_letter_detail_delete.action?id="+id,
				data: "",
				cache:false,
				success: function(msg){
					if(msg=='succ'){
						alert('删除成功');
						window.location.reload();
					}else{
						alert('删除失败');
						window.location.reload();
					}
				}
			}
		$.ajax(option);
  			return true;
		}else{
  			return false;
		}
   }
   //点击回复文本框显示回复文本域和 回复按钮
   function showReply(){
   	var inputValue = document.getElementById('inputValue');
   	var textareaValue = document.getElementById('textareaValue');
   	inputValue.style.display = 'none';
   	textareaValue.style.display = 'block';
   	
   }
   //点击回复,则隐藏多选地址簿
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
   	  var url = "iwork_sys_letter_sent_reply.action";
	  var option={
			type: "post",
			url: url,
			data: "letterId="+letterId+"&receivePersonsIds="+receivePersonsIds+"&receivePersonsNames="+receivePersonsNames+"&content="+content+"&createUserId="+createUserId,
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			cache:false,	
			success: function(msg){
				if(msg=="succ"){
					alert('发送成功');
					window.location.reload();
				}else{
					alert('发送失败');
					return false;
				}
			}
		}
		$.ajax(option);
 	    var obj=document.getElementById("hrygzz");
 	    obj.src = "iwork_sys_letter_look_for_reply.action"+"?letterId="+letterId;
 	    window.location.reload();
 	    //window.opener.location.reload(); 
  	}
  </script>
  </head>
  
  <body>
  <form action="" id="form" name="form">
  	
  	<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
 		<tr>
   			<td>
        				<table  width="95%" id="tablespan">
        					<s:iterator value="queryLettersList" status="letters">
        						<s:if test="syslettersdetailreply!=null">
        						<tr>
        						    <td style="display: none"><input type="hidden" value="<s:property value="id"/>"/></td>
        						    <td width="3%" id="tdspan"><input type="checkbox"  style="display: none" name="sentUserId"  value="<s:property value="sentUserId"/>"/></td>
        							<!-- 注意更改此td下的顺序时,要更改sysletterlookfor.jsp的getFrameCheck()js方法,因为是按照文本位置来获取当前行的值 -->
        							<td class="td_title2" width="7%" >
        								<s:if test="sentUserId==ownerId"><img id="bflag" src="iwork_img/sysletter/sent.png"/></s:if>
        								<s:else><img id="bflag" src="iwork_img/sysletter/reply.png"/></s:else>
        								<s:property value="sentUserName"/>&nbsp;说:
        								<!--回复：<s:property value="receiveUserName"/>:-->
        								<input type="hidden" id="sentUserName" name="sentUserName" value="<s:property value="sentUserName"/>"/>
        							</td>
        							<td class="td_data1" width="55%">
        								<s:if test="sentUserId==ownerId">
        									<p class="triangle-isosceles left-self">
        										<s:property value="syslettersdetailreply.replyContent"/>
        									</p>
        								</s:if>
        								<s:else>
        									<p class="triangle-isosceles left">
        										<s:property value="syslettersdetailreply.replyContent"/>
        									</p>
        								</s:else>
        								
        							</td>
        							<td class="td_data_reply_time" width="22%">回复于:<s:property value="syslettersdetailreply.ts"/></td>
        							<td class="td_data1"><a href="####" onclick="javascript:del(this);">删除</a></td>
        						</tr>
        						</s:if>
        					</s:iterator>
        			    	<input type="hidden" id="beforeId" name="beforeId"  value="<s:property value="beforeId"/>"/>	
        				</table>
  			</td>
 	</tr>
 	
 	
 </table>  
</form>
</body>
</html>
