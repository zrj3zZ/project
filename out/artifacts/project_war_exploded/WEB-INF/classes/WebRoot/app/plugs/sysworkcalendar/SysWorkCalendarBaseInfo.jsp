<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% 
  SimpleDateFormat  sdf=new  SimpleDateFormat("yyyy-MM-dd");
  String isModify = (String)request.getParameter("isModify");//修改标识
  String mon = (String)request.getAttribute("mon");
  String tues = (String)request.getAttribute("tues");
  String wed = (String)request.getAttribute("wed");
  String turs = (String)request.getAttribute("turs");
  String fri = (String)request.getAttribute("fri");
  String sat = (String)request.getAttribute("sat");
  String sun = (String)request.getAttribute("sun");
  Long workTimeFrom = (Long)request.getAttribute("workTimeFrom");
  Long workTimeTo = (Long)request.getAttribute("workTimeTo");
  String status = (String)request.getAttribute("status"); 
  String calendarType = (String)request.getAttribute("calendarType"); 
  if(workTimeFrom==null){
  	workTimeFrom=0L;
  }
   if(workTimeTo==null){
  	workTimeTo=0L;
  }
  if(mon==null){
  	mon="";
  }
    if(tues==null){
  	tues="";
  }
    if(wed==null){
  	wed="";
  }
    if(turs==null){
  	turs="";
  }
    if(fri==null){
  	fri="";
  }
    if(sat==null){
  	sat="";
  }
    if(sun==null){
  	sun="";
  }

 %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<title>Insert title here</title>
<style type="text/css">
	textarea{
		color:#0000FF;
		}
	.td_title {
				letter-spacing: 0.1em;
				white-space:nowrap;
				border-bottom:1px #999999 thick;
				vertical-align:middle;
				font-family:"黑体";
				font-family:黑体;
				font-size:14px;
				font-color:"#2a5caa";
				text-align:right;
			}
	
}
</style>
<script type="text/javascript">
	var api = frameElement.api, W = api.opener;
    var mainFormValidator;
    $().ready(function() {
    	mainFormValidator =  $("#form1").validate({});
    	mainFormValidator.resetForm();
    });
	function fun_checkOfficePay(){
		var option={
				type: "post",
				url: "iwork_sys_calendar_insert.action",
				data: "",
				cache:false,
				success: function(msg){
					if(flag=='succ'){
						alert('保存成功');
					}else{
						alert('保存失败');
					}
				}
			}
		$.ajax(option);
	}
	//更新和创建日历
	function submitTo(){

		var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return false;
					}
		var calendarName = $('#calendarName');
		if (calendarName.val()==""){
			alert("请填写日历名称！");
			calendarName.focus();
			return false;
		}
		var expDateFrom = $('#expDateFrom');
		if (expDateFrom.val()==""){
			alert("请填写有效期开始日期！");
			expDateFrom.focus();
			return false;
		}
		var expDateTo = $('#expDateTo');
		if (expDateTo.val()==""){
			alert("请填写有效期结束日期！");
			expDateTo.focus();
			return false;
		}
		if(<%=isModify%>==1){
		    var params = $('#form1').serialize();
		 	var option={
				type: "post",
				url: "iwork_sys_calendar_insert.action",
				data: params,
				cache:false,
				success: function(msg){
					if(msg=="succ"){
						alert('保存成功');
						parent.window.close();					
						window.opener.location.href="iwork_sys_calendar.action";
						
					}else{
						if(msg=="exsits"){
							alert("已存在系统默认日历,请重新设置!");
						}else{
							alert('保存失败');
						}
						return false;
					}
				}
			}
			$.ajax(option);
		}else{	
			var params = $('#form1').serialize();
		 	var option={
				type: "post",
				url: "iwork_sys_calendar_update_byid.action",
				data: params,
				cache:false,
				success: function(msg){
					if(msg=="succ"){
						alert('保存成功');			
						parent.window.opener.location.reload();
					   	parent.window.close();
					}else{
						if(msg=="exsits"){
							alert("已存在系统默认日历,请重新设置!");
						}else{
							alert('保存失败');
						}
						return false;
					}
				}
			}
			$.ajax(option);
			
		}
	}
	
	$(function(){
		var timeFrom = document.getElementById('workTimeFrom');
		var timeTo = document.getElementById('workTimeTo');
		var status = document.getElementsByName('calendar.status');
		var calendarType = document.getElementsByName('calendar.calendarType');
		
		for(var i=0;i<timeFrom.length;i++){
			if(timeFrom.options[i].value==<%=workTimeFrom%>){
				timeFrom.options[i].selected = true;
			}
		}
		for(var j=0;j<timeTo.length;j++){
			if(timeTo.options[j].value==<%=workTimeTo%>){
				timeTo.options[j].selected = true;
			}
		}
		for(var k=0;k<status.length;k++){
			if(status[k].value==<%=status%>){
				status[k].checked = true;
			}
		}
		for(var g=0;g<status.length;g++){
			if(calendarType[g].value==<%=calendarType%>){
				calendarType[g].checked = true;
			}
		}
		
	})
	
	
	
    //清空授權
	function resetGrantUsers(){
		var obj = document.getElementById('grantUsers');
		var gnl=confirm("确定要清空吗?");
		if(gnl==true){
			obj.value = '';
			return true;
		}else{
			return false;
		}
	}	

</script>
</head>
<body>

<s:form action="" method="post" enctype="multipart/form-data" name="form1" id="form1">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="3">
    	<table width="100%" height="39" border="0" align="center" cellpadding="0" cellspacing="0">
      	  <tr>
        	<td width="51%" align="right" valign="top" style="margin:10px;">	
        		<fieldset style="padding:8px;width:90%;border:1px solid #ACBCC9; margin-left:10px; margin-right:10px;line-height:2.0; text-align:left;" >
        			<legend style="vertical-align:middle; text-align:left">
        				<img src="iwork_img/rszz/table.png" width="16" height="16" border="0">日历基本设置
        			</legend>
<table width="100%" height="50%" border="0">
  <tr>
    <td class="td_title" height="41" colspan="2" align="right">
        <input class="btn1" type="button" onclick="javascript:submitTo();" value="保存" />
      
      <input class="btn1" type="button" name="sq" onclick="authority_chooser(document.getElementById('grantUsers'));" value="授权" />
	</td>
  </tr>
  <tr>
    <td class="td_title" width="97">
    <div align="right"><span style="color:red;">*</span>日历标题</div>
    </td>
    <td class="td_data" width="561">
      <input type="text" name="calendar.calendarName" id="calendarName" style="width: 300px" value="<s:property value="calendarName"/>"/>    </td>
  </tr>
  <tr>
    <td><div align="right" class="td_title">默认工作日</div></td>
    <td class="td_data">
    <input type="checkbox" <%if(mon.equals("1")){%> checked="checked"<%}%> name="calendar.mon" value="1" />
     	 周一
        <input type="checkbox" name="calendar.tues" <%if(tues.equals("1")){%> checked<%}%> value="1" />
		周二
		<input type="checkbox" name="calendar.wed" <%if(wed.equals("1")){%> checked<%}%> value="1" />
		周三
		<input type="checkbox" name="calendar.turs" <%if(turs.equals("1")){%> checked<%}%> value="1" />
		周四
		<input type="checkbox" name="calendar.fri" <%if(fri.equals("1")){%> checked<%}%> value="1"/>
		周五
		<input type="checkbox" name="calendar.sat" <%if(sat.equals("1")){%> checked<%}%> value="1" />
		周六
		<input type="checkbox" name="calendar.sun" <%if(sun.equals("1")){%> checked<%}%> value="1" />
		周日
	</td>
  </tr>
  <tr>
    <td class="td_title"><div align="right">默认工作时间</div></td>
    <td class="td_data">
    <select name="calendar.workTimeFrom" id="workTimeFrom">
    <option value='1'>01:00</option>
    <option value='2'>02:00</option>
    <option value='3'>03:00</option>
    <option value='4'>04:00</option>
    <option value='5'>05:00</option>
    <option value='6'>06:00</option>
    <option value='7'>07:00</option>
    <option value='8'>08:00</option>
    <option value='9' selected='selected'>09:00</option>
    <option value='10'>10:00</option>
    <option value='11'>11:00</option>
    <option value='12'>12:00</option>
    <option value='13'>13:00</option>
    <option value='14'>14:00</option>
    <option value='15'>15:00</option>
    <option value='16'>16:00</option>
    <option value='17'>17:00</option>
    <option value='18'>18:00</option>
    <option value='19'>19:00</option>
    <option value='20'>20:00</option>
    <option value='21'>21:00</option>
    <option value='22'>22:00</option>
    <option value='23'>23:00</option>
    <option value='24'>24:00</option>
    </select>
    到 
    <select name="calendar.workTimeTo" id="workTimeTo">
    <option value='1'>01:00</option>
    <option value='2'>02:00</option>
    <option value='3'>03:00</option>
    <option value='4'>04:00</option>
    <option value='5'>05:00</option>
    <option value='6'>06:00</option>
    <option value='7'>07:00</option>
    <option value='8'>08:00</option>
    <option value='9'>09:00</option>
    <option value='10'>10:00</option>
    <option value='11'>11:00</option>
    <option value='12'>12:00</option>
    <option value='13'>13:00</option>
    <option value='14'>14:00</option>
    <option value='15'>15:00</option>
    <option value='16'>16:00</option>
    <option value='17'>17:00</option>
    <option value='18'  selected='selected'>18:00</option>
    <option value='19'>19:00</option>
    <option value='20'>20:00</option>
    <option value='21'>21:00</option>
    <option value='22'>22:00</option>
    <option value='23'>23:00</option>
    <option value='24'>24:00</option>
    </select></td>
  </tr>
  <tr>
    <td class="td_title"><div align="right"><span style="color:red;">*</span>有效期</div></td>
    <td class="td_data">
    <input type="text" name="calendar.expDateFrom" id="expDateFrom" readonly="readonly" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'expDateTo\')}'})" value="<s:date name='expDateFrom'  format="yyyy-MM-dd"/>"/>&nbsp;&nbsp;到&nbsp;&nbsp;
    <input name="calendar.expDateTo" id="expDateTo" type="text" readonly="readonly"  onfocus="WdatePicker({minDate:'#F{$dp.$D(\'expDateFrom\')}'})" value="<s:date name='expDateTo'  format="yyyy-MM-dd"/>"/>
    </td>
  </tr>
  <tr>
    <td height="125" class="td_title"><div align="right">适用范围描述</div></td>
    <td class="td_data"><textarea  name="calendar.describ"  class = "{required:true,string:true}" style="width:300px;height:125px;hoverflow-y:visible"  >${describ}</textarea></td>
  </tr>
  <tr>
    <td class="td_title"><div align="right">使用状态</div></td>
    <td class="td_data"><input type="radio" checked="checked" name="calendar.status" id="status_on" value="1" />
      开启
        <input type="radio" name="calendar.status" id="status_off" value="0" />
      关闭</td>
  </tr>
  <tr>
    <td class="td_title">使用权限设置</td>
  	<td class="td_data">
  		<textarea readonly="readonly" name="grantUsers" id="grantUsers" class = "{required:true}" style="width:300px;height:125px;hoverflow-y:visible"  ><s:property value="grantUsers"/></textarea>
	 	<button type="button" onclick="resetGrantUsers();">清空</button>
	  <!--<s:textarea readonly="true" name="calendar.grantUsers" id="grantUsers" cssStyle="width:240px;height:60px;"/>-->       				
	  <!--<a href="javascript:openAuthorityBook(document.getElementById('editForm_model_formPurview'));" class="easyui-linkbutton" plain="true" iconCls="icon-add">权限地址簿</a>-->	            				
   </td>
  </tr>
  <tr>
  <tr>
    <td class="td_title"><div align="right">日历类型</div></td>
    <td class="td_data"><input type="radio"  name="calendar.calendarType" value="0" />
    系统默认日历
        <input type="radio" checked="checked" name="calendar.calendarType" value="1" />
      系统普通日历</td>
  </tr>
  	<td></td>
  	<td><input type="hidden" name="calendar.id" value="${id }" class="{string:true}"></input></td>
  </tr>
</table>
</fieldset>
	   		</td>
   	 	</tr>
     </table>
 	</td>
   </tr>
  </table>  
</s:form>
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