<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>发送站内信</title>
    
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
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"  > </script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"  ></script>
	
  <script type="text/javascript">
  var mainFormValidator;
  $().ready(function() {
  	mainFormValidator =  $("#form").validate({});
  	mainFormValidator.resetForm();
  });
   function sentLetter(){
   	var form =  document.getElementById('form');
   	var flag = checkValidate();
   	if(flag==true){
   		 $.post("iwork_sys_letter_create_sent.action",$('form').serialize(),function(data){
   		 	if(data=='OK'){
   		 		window.opener.location.href="iwork_sys_letter_list.action";
				window.close();
   		 	}
   		 });
   		//form.action ="iwork_sys_letter_create_sent.action";
   		//form.submit();
   		//window.opener.location.href="iwork_sys_letter_list.action";
		//window.close();
   	}else{
   		return false;
   	}
   }
   //校验输入内容
   function checkValidate(){
  	var letterContent = document.getElementById('letterContent');
  	var letterTitle = document.getElementById('letterTitle');
  	var letterLevel = document.getElementsByName('letterLevel');
  	var receiveUserName = document.getElementById('receiveUserName');
  	var receiveUserId = $('#receiveUserId').val();
  	var sendUserId = $('#sentUserId').val();
  	if(receiveUserName.value==''){
  		alert('请选择收信人!');
  		return false;
  	}
  	if(sendUserId == receiveUserId){
  		alert('不能给自己发站内信,请选择收信人!');
  		return false;
  	}
  	if(letterTitle.value==''){
  		alert('请填写站内信标题!');
  		return false;
  	}
  	if(letterContent.value==''){
  		alert('请填写站内信内容!');
  		return false;
  	}
  	return true;
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
        				<img src="iwork_img/sysletter/letter_sent.png" width="16" height="16" border="0">发送站内信
        			</legend>
  		<table  width='90%'>
 			<tr>
   				<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">收信人：</td>
   				<td class="td_data1"  align="left">
   					<input type="text" id="receiveUserName" name="receiveUserName"/>
   					<a id='radioBtnhtml' href="###" title="多选地址薄"  onclick="radio_book('','','','receiveUserId','','receiveUserName','','','receiveUser');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a>
   				</td>
  			</tr>
  			<tr>
   				<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">标题：</td>
   				<td class="td_data1" align="left"><input type="text" id="letterTitle" name="letterTitle" width="%"/></td>
  			</tr>
  			<tr>
   				<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">内容：</td>
   				<td class="td_data1"><textarea  name="letterContent" id="letterContent"  style="width:300px;height:125px;hoverflow-y:visible"  ></textarea></td>
  			</tr>
  			<tr>
  				<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="22%" align="center">消息优先级：</td>
  				<td class="td_data1">
  				高<input type="radio"  name="letterLevel" value="1" />
  				中<input type="radio"  name="letterLevel" value="2" />
  				低<input type="radio"  name="letterLevel" value="3" checked="checked"/>
  				</td>
  			</tr>
  			<input type="hidden" id="sentUserId" name="sentUserId" value="${sentUserId}" class="{string:true}" />
  			<input type="hidden" id="receiveUserId" name="receiveUserId"/>
  			<input type="hidden" id="receiveUser" name="receiveUser"></input>
  			<tr>
  				<td><input type="button" onclick="javascript:sentLetter();"value="发送"/>
  				</td>
  				<td>
  				</td>
  			</tr>
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

<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
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
