<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>查看站内信</title>
    <!-- 此页暂时不用,不需要多人发送需求 -->
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/sysletters/sysletters.css">
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
   //点击回复文本框显示回复文本域和 回复按钮
   function showReply(){
   	var inputValue = document.getElementById('inputValue');
   	var textareaValue = document.getElementById('textareaValue');
   	inputValue.style.display = 'none';
   	textareaValue.style.display = 'block';
   	
   }
   //点击回复以下所有人,则隐藏多选地址簿
   function show_muti(){
    var replyType = document.getElementById('replyType');
    var replyTypeCreate = document.getElementById('replyTypeCreate');
    var replyTypeAdress = document.getElementById('replyTypeAdress');
   	var receiveUserName = document.getElementById('receiveUserName');
   	var radioBtnhtml = document.getElementById('radioBtnhtml');
   	var win = document.getElementById('hrygzz').contentWindow; 
	var checkValue = win.document.getElementById("tablespan");
	var objTrs = checkValue.getElementsByTagName("tr");
	var checkboxs = win.document.getElementsByName('sentUserId');
   	if(replyType.checked){
   	    replyTypeCreate.checked = false;
   	    replyTypeAdress.checked = false;
   		for(var i=0;i<checkboxs.length;i++){
   			checkboxs[i].style.display = 'block';
   		}
   		
   	}else{
   		for(var i=0;i<checkboxs.length;i++){
   			checkboxs[i].style.display = 'none';
   		}
   	}
   }
   //选择创建人为默认回复人
   function show_create_muti(){
   	 var replyType = document.getElementById('replyType');
   	 var replyTypeCreate = document.getElementById('replyTypeCreate');
   	 var replyTypeAdress = document.getElementById('replyTypeAdress');
   	 if(replyTypeCreate.checked){
   	 	replyType.checked = false;
   	 	replyTypeAdress.checked = false;
   	 	show_muti();
   	 }
   }
   //选择地址簿中为回复人
   function show_adress_muti(){
   	 var replyType = document.getElementById('replyType');
   	 var replyTypeCreate = document.getElementById('replyTypeCreate');
   	 var replyTypeAdress = document.getElementById('replyTypeAdress');
   	 if(replyTypeAdress.checked){
   	 	replyType.checked = false;
   	 	replyTypeCreate.checked = false;
   	 	show_muti();
   	 }
   }
   //回复操作
   function replyPage(){
   	  var letterId = document.getElementById("letterId").value;
      var replyType = document.getElementById('replyType');
      var replyTypeAdress = document.getElementById('replyTypeAdress');
      var replyTypeCreate = document.getElementById('replyTypeCreate');
      var receivePersonsIds = "";
      var receivePersonsNames = "";
      var createUserId = document.getElementById('createUserId').value;
      var content = document.getElementById('letterReply').value
      var flag = checkValidate();
      if(replyType.checked){
      	  var tempStr = getFrameCheck();
      	  var tempArr = tempStr.split("@@");
      	  receivePersonsIds = tempArr[0];
      	  receivePersonsNames = tempArr[1];
      	 //receivePersonsIds = document.getElementById('sentUserIdsGroup').value;
      }else if(replyTypeAdress.checked){
      	 receivePersonsIds = document.getElementById('receiveUserId').value;
      	 receivePersonsNames = document.getElementById('receiveUserName').value;
      }else{
      	 if(replyTypeCreate.checked){
      	 	receivePersonsIds = document.getElementById('createUserId').value;
      	 	receivePersonsNames = document.getElementById('createUserName').value;
      	 }else{
      	 	receivePersonsIds = "";
      	 }
      }
      if(flag == true){
      
      if(receivePersonsIds==''){
      	alert('请选择回复人!');
      	return false;
      }else{
   	    var url = "iwork_sys_letter_sent_reply.action";
				//var params = $('#form').serialize();
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
      }
      }
  }
  //获得要发送的人
  function getFrameCheck(){
  	var win = document.getElementById('hrygzz').contentWindow; 
	var sentUserIds_checkbox = win.document.getElementsByName("sentUserId");
	var userNames = [];
	var userIds = [];
	var userNameStr = '';//获得无重复的UserId 用,分割
	var userIdStr = '';//获得无重复的UserName 用,分割
	var userId = '';
	for(var i=0;i<sentUserIds_checkbox.length;i++){
		if(sentUserIds_checkbox[i].checked){
			userId = sentUserIds_checkbox[i].value;
			var objTr = sentUserIds_checkbox[i].parentNode.parentNode;
			var sentUserName = objTr.getElementsByTagName("td")[2].childNodes[3].value;//获得是该td下第4个位置的值,也就是<input id="sentUserName">的value
			var flag = compareArray(userIds,userId);
			if(flag == false){
			    userIds[i] = userId;
			    userNames[i] = sentUserName;
				userIdStr = userIdStr + userId +',';
				userNameStr = userNameStr + sentUserName + ','
			}
		}
	}
	if(userIdStr.indexOf(',')>0){
		userIdStr = userIdStr.substring(0,userIdStr.lastIndexOf(','));
		userNameStr = userNameStr.substring(0,userNameStr.lastIndexOf(','));
	}
	return userIdStr+"@@"+ userNameStr;
  }
  //比较是否存在
  function compareArray(ObjArr,objStr){
  	var flag = false;
  	for(var j=0;j<ObjArr.length;j++){
		if(objStr == ObjArr[j]){
			flag = true;
		}
	}
	return flag;
  }
  //收取回复
  function refresh(){
    var letterId = document.getElementById("letterId").value;
    var obj=document.getElementById("hrygzz");
    var changeType = "1"//标记更改未读/已读状态,是从收取回复按钮发起
 	obj.src = "iwork_sys_letter_look_for_reply.action"+"?letterId="+letterId+"&changeType="+changeType;
  }
  function goBack(){
  	var form = document.getElementById('form');
  	form.action = 'iwork_sys_letter_list.action'
  	form.submit();
  }
  
  function checkValidate(){
  	var letterReply = document.getElementById('letterReply');
  	if(letterReply.value==''){
  		alert('请填写回复内容!');
  		return false;
  	}else{
  		return true;
  	}
  }
  </script>
  </head>
  
  <body>
  <form action="" id="form" name="form">
  	
  	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  	<tr>
    <td height="3">
    	<table width="100%" height="39" border="0" align="center" cellpadding="0" cellspacing="0">
      	  	<tr>
        		<td width="51%" align="right" valign="top" style="margin:10px;">	
        			<fieldset style="padding:8px;width:90%;border:1px solid #ACBCC9; margin-left:10px; margin-right:10px;line-height:2.0; text-align:left;" >
        				<legend style="vertical-align:middle; text-align:left">
        					<img src="iwork_img/sysletter/letter_sent.png" width="16" height="16" border="0">站内信详细信息
        				</legend>
  						<table  width='90%'>
 							<tr>
   								<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">创建站内信人：</td>
   								<td class="td_data1"  align="left">
   									<input type="text" id="createUserName" name="sysletterscontent.createUserName" readonly="readonly" value="<s:property value="sysletterscontent.createUserName"/>">
   								</td>
  							</tr>
  							<tr>
   								<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">首次发信主要内容：</td>
   								<td class="td_data1"><textarea  name="letterContent" id="letterContent"  style="width:300px;height:80px;hoverflow-y:visible" readonly="readonly" ><s:property value="sysletterscontent.letterContent"/></textarea></td>
  							</tr>
  							<tr>
  								<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">消息优先级：</td>
  								<td class="td_data1"> <s:radio list="#{1:'高', 2:'中', 3:'低' }" theme="simple" name="letterLevel" value="sysletterscontent.letterLevel"></s:radio></td>
  							</tr>
  								<input type="hidden" id="createUserId" name="sysletterscontent.createUserId" value="<s:property value="sysletterscontent.createUserId"/>"/>
								<input type="hidden" id="letterId" name="sysletterscontent.id" value="<s:property value="sysletterscontent.id"/>"/>
						</table>
						<table width='100%'>
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
							 <tr>
							    <td width="15%" class="td_title2">
							 		<input type="checkbox" id="replyTypeCreate" onclick="javascript:show_create_muti();" checked="checked"/>默认创建人为回复人
							 	</td>
							 	<td width="13%" class="td_title2">
							 		<input type="checkbox" id="replyType" onclick="javascript:show_muti();"/>选择历史记录为回复人
							 	</td>
							 	<td width="15%" class="td_title2">
							 		<input type="checkbox" id="replyTypeAdress" onclick="javascript:show_adress_muti();"/>选择地址簿为回复人
							 	</td>
							 	<td class="td_title2">
							 		<input type="text" id="receiveUserName" name="receiveUserName" readonly="readonly" style="background-color: #E5E5E5"/>
							 		<input type="hidden" id="receiveUserId" name="receiveUserId"/>
							 		<input type="hidden" id="receiveUser" name="receiveUser"></input>
							 		<a id='radioBtnhtml'  id="radioBtnhtml" href="###" title="多选地址薄"  onclick="multi_book('','','','receiveUserId','','receiveUserName','','','receiveUser');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a>
							 	</td>
							 	
							 </tr>
							</tbody>
							 <tr>
							 	<td colspan="4">
							 		<input type="button" value="收取回复" onclick="refresh();"/>
							 		<input type="button" value="返回" onclick="javascript:goBack();">
							 	</td>
							 </tr>
						</table>
					</fieldset>
	   		   </td>
   	 	   </tr>
        </table>
      </td>
    </tr>
 	<tr>
   		<td>
   			<iframe name="hrygzz" id="hrygzz"  marginheight="0" marginwidth="0" frameborder="0"  src="iwork_sys_letter_look_for_reply.action?letterId=<s:property value="sysletterscontent.id"/>" align=center width=100% onload="document.all['hrygzz'].style.height=hrygzz.document.body.scrollHeight+20;">
   			</iframe>
  		</td>
 	</tr>  
</form>
</body>
</html>
