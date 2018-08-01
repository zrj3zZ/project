<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%java.util.HashMap map = new java.util.LinkedHashMap();
for(int i=0;i<24;i++){
	if(i<10){
		map.put("0"+i+":00","0"+i+":00");
		map.put("0"+i+":30","0"+i+":30");
	}else{
		map.put(i+":00",i+":00");
		map.put(i+":30",i+":30");
	}
}
request.setAttribute("timeMap",map);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.6.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_js/fullcalendar/fullcalendar/command.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/plugs/editschcalendar.css" />
	<script type="text/javascript">
		$(document).bind("contextmenu",function(){return false;});
		var api = frameElement.api, W = api.opener, D = document;
		$(document).ready(function(){
			$("#sch_create").bind("click", function(){//创建日程按钮
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					/* setSubmitVal();//根据事件时间设置是否全天事件 */
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
				}else{
					return false;
				}
			});
			$("#sch_update").bind("click", function(){//保存修改按钮
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					/* setSubmitVal();//根据事件时间设置是否全天事件 */
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
				}else{
					return false;
				}
			});
			$("#sch_delete").bind("click", function(){//删除日程按钮
				$('form').attr('action','deleteAppointment.action');
					var options = {
						error:showRequest,
						success:showResponse 
					};
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
			});
			<s:if test="null == appointment.id"></s:if>
			<s:else>
				var obj = api.data;
				//接收父页面的参数
				var titleVal = obj.editForm_appointment_title;
				var idVal = obj.editForm_appointment_id;
				var isalldayVal = obj.editForm_appointment_isallday;
				var useridVal = obj.editForm_appointment_userid;
				var startdateVal = obj.editForm_appointment_startdate;
				var starttimeVal = obj.editForm_appointment_starttime;
				var enddateVal = obj.editForm_appointment_enddate;
				var endtimeVal = obj.editForm_appointment_endtime;
				var isalertVal = obj.editForm_appointment_isalert;
				var alerttimeVal = obj.editForm_appointment_alerttime;
				var issharingVal = obj.editForm_appointment_issharing;
				var remarkVal = obj.editForm_appointment_remark;
				var reStartdateVal = obj.editForm_appointment_reStartdate;
				var reEnddateVal = obj.editForm_appointment_reEnddate;
				var reStarttimeVal = obj.editForm_appointment_reStarttime;
				var reEndtimeVal = obj.editForm_appointment_reEndtime;
				var reModeVal = obj.editForm_appointment_reMode;
				var reDayIntervalVal = obj.editForm_appointment_reDayInterval;
				var reWeekDateVal = obj.editForm_appointment_reWeekDate;
				var reMonthDaysVal = obj.editForm_appointment_reMonthDays;
				var reYearMonthVal = obj.editForm_appointment_reYearMonth;
				var reYearDaysVal = obj.editForm_appointment_reYearDays;
				var sfjy = obj.editForm_appointment_sfjy;
				var jyr = obj.editForm_appointment_jyr;
				//赋值
				$("#editForm_appointment_title").val(titleVal);
				$("#editForm_appointment_id").val(idVal);
				$("#editForm_appointment_isallday").val(isalldayVal);
				$("#editForm_appointment_userid").val(useridVal);
				$("#editForm_appointment_startdate").val(startdateVal);
				$("#editForm_appointment_starttime").val(starttimeVal);
				$("#editForm_appointment_enddate").val(enddateVal);
				$("#editForm_appointment_endtime").val(endtimeVal);
				$("#editForm_appointment_isalert").val(isalertVal);
				$("#editForm_appointment_alerttime").val(alerttimeVal);
				$("#editForm_appointment_issharing").val(issharingVal);
				$("#editForm_appointment_remark").val(remarkVal);
				$("#editForm_appointment_reStartdate").val(reStartdateVal);
				$("#editForm_appointment_reEnddate").val(reEnddateVal);
				$("#editForm_appointment_reStarttime").val(reStarttimeVal);
				$("#editForm_appointment_reEndtime").val(reEndtimeVal);
				$("#editForm_appointment_reMode").val(reModeVal);
				$("#editForm_appointment_reDayInterval").val(reDayIntervalVal);
				$("#editForm_appointment_reWeekDate").val(reWeekDateVal);
				$("#editForm_appointment_reMonthDays").val(reMonthDaysVal);
				$("#editForm_appointment_reYearMonth").val(reYearMonthVal);
				$("#editForm_appointment_reYearDays").val(reYearDaysVal);
				$("#editForm_appointment_szr").val(useridVal);
				$("#editForm_appointment_jyr").val(jyr);
				if(sfjy==1){
					document.getElementsByName('appointment.sfjy')[0].checked=true;
				}else{
					document.getElementsByName('appointment.sfjy')[1].checked=true;
				}
			</s:else>
			//初始化开始时间、结束时间
			if($("#editForm_appointment_reStartdate").val()!=null&&$("#editForm_appointment_reStartdate").val()!=""){
				$("#noRepeate_1").hide();$("#noRepeate_2").hide();
				$("#repeate_1").show();$("#repeate_2").show();
			}else{
				$("#noRepeate_1").show();$("#noRepeate_2").show();
				$("#repeate_1").hide();$("#repeate_2").hide();
			}
		});
			//提交表单失败
	function showRequest(formData, jqForm, options) {
		 W.$.dialog.alert("增加日志失败请联系管理员!");
		 return true; 
	}
	//成功提交表单
	function showResponse(responseText, statusText, xhr, $form)  { 
		window.parent.refetch();
		api.close();
	}
	//根据事件时间设置是否全天事件
	function setSubmitVal(){
		if($("#editForm_appointment_reStartdate").val()!=null&&$("#editForm_appointment_reStartdate").val()!=""){
			$("#editForm_appointment_startdate").val("");
			$("#editForm_appointment_enddate").val("");
			if($("#editForm_appointment_reStarttime").val()=="00:00"&&$("#editForm_appointment_reEndtime").val()=="00:00"){
				$("#editForm_appointment_isallday").val("1");
			}else{
				$("#editForm_appointment_isallday").val("0");
			}
		}else{
			if($("#editForm_appointment_starttime").val()=="00:00"&&$("#editForm_appointment_endtime").val()=="00:00"){
				$("#editForm_appointment_isallday").val("1");
			}else{
				$("#editForm_appointment_isallday").val("0");
			}
		}
	}
	//检测时间是否合法
	function checktime(start,end){
		var timeFalg = true;
		var startArr = start.split(":");
		var endArr = end.split(":");
		if(startArr[0]>endArr[0]){
			timeFalg = false;
			W.$.dialog.alert("您输入的时间不合法!");
		}else if(startArr[0]==endArr[0]&&startArr[0]!="00"){
			if(startArr[1]>endArr[1]){
				timeFalg = false;
				W.$.dialog.alert("您输入的时间不合法!");
			}else if(startArr[1]==endArr[1]){
				timeFalg = false;
				W.$.dialog.alert("您输入的时间不合法!");
			}
		}else if(startArr[0]==endArr[0]&&startArr[0]=="00"){
			if(startArr[1]>endArr[1]){
				timeFalg = false;
				W.$.dialog.alert("您输入的时间不合法!");
			}else if(startArr[1]==endArr[1]&&startArr[1]=="30"){
				timeFalg = false;
				W.$.dialog.alert("您输入的时间不合法!");
			}
		}
		return timeFalg;
	}
	//检测所有提交数据的合法性
	function checkSubmitVal(){
		var flag = true;
		var title = $.trim($("#editForm_appointment_title").val());
		/* if(title.length==0){
			flag = false;
			W.$.dialog.alert("请输入日志主题!");
		}else if(title.length>80){
			flag = false;
			W.$.dialog.alert("输入的主题请勿超过80个字符!");
		} */
		var startdate = $("#editForm_appointment_startdate").val();
		var starttime = $("#editForm_appointment_starttime").val();
		var enddate = $("#editForm_appointment_enddate").val();
		var endtime = $("#editForm_appointment_endtime").val();
		var restartdate = $("#editForm_appointment_reStartdate").val();
		var restarttime = $("#editForm_appointment_reStarttime").val();
		var reenddate = $("#editForm_appointment_reEnddate").val();
		var reendtime = $("#editForm_appointment_reEndtime").val(); 
		/* if(restartdate==null||restartdate==""){
			if(startdate==null||startdate==""){flag = false;W.$.dialog.alert("请输入开始日期");}
			if(enddate==null||enddate==""){flag = false;W.$.dialog.alert("请输入 结束日期");}
			if(starttime==null||starttime==""){flag = false;W.$.dialog.alert("请输入 开始时间");}
			if(endtime==null||endtime==""){flag = false;W.$.dialog.alert("请输入 结束时间");}
			if(startdate==enddate){
				if(checktime(starttime,endtime)){}else{flag=false;}
			}
		}else{
			if(restartdate==null||restartdate==""){flag = false;W.$.dialog.alert("请输入开始日期");}
			if(restarttime==null||restarttime==""){flag = false;W.$.dialog.alert("请输入 开始时间");}
			if(reendtime==null||reendtime==""){flag = false;W.$.dialog.alert("请输入 结束时间");}
			if(restartdate==reenddate){
				if(checktime(restarttime,reendtime)){}else{flag=false;}
			}
		} */
		return flag;
	}
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="width:300px;height:100px;padding:3px;border:0px;padding-left:10px;padding-top:10px;padding-right:10px;">
            	<s:form id ="editForm" name="editForm" action="saveAppoinment"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td class="td_data" width="83%">
	            				<s:radio list="#{'1':'休息','0':'工作'}" name="appointment.sfjy"/>&nbsp;&nbsp;
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title" width="17%"></td>
	            			<td class="td_data" width="83%"></td>
	            		</tr>
	            	<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td class="td_data" width="83%">
	            				<input id="sch_update" name="sch_update" class="button blue" type="button" value="保存" />&nbsp;&nbsp;
	            				<input id="sch_delete" name="sch_delete" class="button blue" type="button" value="删除" />&nbsp;&nbsp;
	            				<!-- <a id="sch_delete" href="javasccript:void(0)"><font style="color: blue">删除</font></a>&nbsp;&nbsp; -->
	            			</td>
	            		</tr>
	            	</table>
	                <s:hidden name="appointment.id"/>
	                <s:hidden name="appointment.isallday"/>
	                <s:hidden name="appointment.userid"/>
	                <s:hidden name="appointment.isalert"/>
	                <s:hidden name="appointment.alerttime"/>
	                <s:hidden name="appointment.issharing"/>
	                <s:hidden name="appointment.remark"/>
	                <s:hidden name="appointment.reMode"/>
	                <s:hidden name="appointment.reDayInterval"/>
	                <s:hidden name="appointment.reWeekDate"/>
	                <s:hidden name="appointment.reMonthDays"/>
	                <s:hidden name="appointment.reYearMonth"/>
	                <s:hidden name="appointment.reYearDays"/>
	                <s:hidden name="appointment.szr"/>
	                <s:hidden name="appointment.jyr"/>
	          </s:form> 
	     </div>
</body>
</html>