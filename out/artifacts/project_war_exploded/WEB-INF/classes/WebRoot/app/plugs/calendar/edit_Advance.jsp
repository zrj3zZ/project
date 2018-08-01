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
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<base href="<%=basePath%>">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.6.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_js/fullcalendar/fullcalendar/command.css" />
	
	<link rel="stylesheet" type="text/css" href="iwork_css/plugs/editschcalendar_advance.css" />
		
	<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
		var api = frameElement.api, W = api.opener, D = document;
		$(document).ready(function(){
			$("#editForm_iworkSchCalendar_title").focus();
			//初始化是否周期模式
			if($("#editForm_iworkSchCalendar_startdate").val()!=""&&$("#editForm_iworkSchCalendar_startdate").val()!=null){
				document.getElementById("choseDate0").checked=true;
			}else{
				var re_Startdate = $("#editForm_iworkSchCalendar_reStartdate").val();
				var re_Enddate = $("#editForm_iworkSchCalendar_reEnddate").val();
				var re_Mode = $("#editForm_iworkSchCalendar_reMode").val();
				var re_DayInterval = $("#editForm_iworkSchCalendar_reDayInterval").val();
				var re_WeekDate = $("#editForm_iworkSchCalendar_reWeekDate").val();
				var re_MonthDays = $("#editForm_iworkSchCalendar_reMonthDays").val();
				var re_YearMonth = $("#editForm_iworkSchCalendar_reYearMonth").val();
				var re_YearDays = $("#editForm_iworkSchCalendar_reYearDays").val();
				$("#RepeateModeInfo").html(getRepeateModeInfo(re_Startdate,re_Enddate,re_Mode,re_DayInterval,re_WeekDate,re_MonthDays,re_YearMonth,re_YearDays));
				document.getElementById("noneRepeateMode1").style.display = "none";
				document.getElementById("noneRepeateMode2").style.display = "none";
				document.getElementById("RepeateMode1").style.display = "block";
				document.getElementById("RepeateMode2").style.display = "block";
			}
			//初始化是否全天日程
			if($("#iworkSchCalendar_isallday").val()=="1"){
				document.getElementById("editForm_iworkSchCalendar_isallday1").checked = true;
			}else{
				document.getElementById("editForm_iworkSchCalendar_isallday0").checked = true;
			}
			<s:if test="null == iworkSchCalendar.id"></s:if>
			<s:else>//编辑事件的时候初始化
			//初始化是否提醒
			if($("#iworkSchCalendar_isalert").val()=="1"){
				document.getElementById("editForm_iworkSchCalendar_isalert1").checked = true;
			}else{
				document.getElementById("editForm_iworkSchCalendar_isalert1").checked = false;
			}
			
			//初始化是否共享
			/*if($("#iworkSchCalendar_issharing").val()=="1"){
				document.getElementById("editForm_iworkSchCalendar_issharing1").checked = true;
			}else{
				document.getElementById("editForm_iworkSchCalendar_issharing1").checked = false;
			}*/
			</s:else>
			//创建日程按钮
			$("#sch_create").bind("click", function(){
				this.disabled = true;
  				var me = this;
  				setTimeout(function() { me.disabled = false; }, 5000);
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					$("#editForm_iworkSchCalendar_reStarttime").val($("#editForm_iworkSchCalendar_starttime").val());
					$("#editForm_iworkSchCalendar_reEndtime").val($("#editForm_iworkSchCalendar_endtime").val());
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
				}else{
					return false; 
				}
			});
			//保存修改按钮
			$("#sch_update").bind("click", function(){
				this.disabled = true;
  				var me = this;
  				setTimeout(function() { me.disabled = false; }, 5000);
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					$("#editForm_iworkSchCalendar_reStarttime").val($("#editForm_iworkSchCalendar_starttime").val());
					$("#editForm_iworkSchCalendar_reEndtime").val($("#editForm_iworkSchCalendar_endtime").val());
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
				}else{
					return false; 
				}
			});
			//删除日程按钮
			$("#sch_delete").bind("click", function(){
				if(confirm("确认删除当前[日程]吗？")){
					$('form').attr('action','deleteSchCalendar.action');
					var options = {
						error:showRequest,
						success:showResponse 
					};
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
				}
			});
			//点击设置周期模式按钮
			$("#choseDate1").bind("click", function(){
				window.parent.popAddRepeateMode($("#editForm_iworkSchCalendar_startdate").val(),$("#editForm_iworkSchCalendar_enddate").val());//弹出设置周期窗口
				document.getElementById("editForm_iworkSchCalendar_startdate").disabled =true;
				$("#editForm_iworkSchCalendar_startdate").val("");
				document.getElementById("editForm_iworkSchCalendar_enddate").disabled =true;
				$("#editForm_iworkSchCalendar_enddate").val("");
				$("#editForm_iworkSchCalendar_enddate").val("");
			});
			//点击设置非周期模式按钮
			$("#choseDate0").bind("click", function(){
				document.getElementById("editForm_iworkSchCalendar_startdate").disabled =false;
				document.getElementById("editForm_iworkSchCalendar_enddate").disabled =false;
				window.parent.closeRepeateMode();//关闭设置周期窗口
			});
			//点击开始日期自动选择非周期模式
			$("#editForm_iworkSchCalendar_startdate").bind("click", function(){
				document.getElementById("choseDate0").checked = true;
			});
			//点击结束日期自动选择非周期模式
			$("#editForm_iworkSchCalendar_enddate").bind("click", function(){
				document.getElementById("choseDate0").checked = true;
			});
			//点击设置非全天日程
			$("#editForm_iworkSchCalendar_isallday0").bind("click", function(){
				document.getElementById("editForm_iworkSchCalendar_starttime").disabled =false;
				document.getElementById("editForm_iworkSchCalendar_endtime").disabled =false;
			});
			//点击设置全天日程
			$("#editForm_iworkSchCalendar_isallday1").bind("click", function(){
				document.getElementById("editForm_iworkSchCalendar_starttime").disabled =true;
				document.getElementById("editForm_iworkSchCalendar_starttime").value = "00:00";
				document.getElementById("editForm_iworkSchCalendar_endtime").disabled =true;
				document.getElementById("editForm_iworkSchCalendar_endtime").value = "00:00";
			});
			//点击开始时间自动选择非全天
			$("#editForm_iworkSchCalendar_starttime").bind("click", function(){
				document.getElementById("editForm_iworkSchCalendar_isallday0").checked = true;
			});
			//点击结束 时间自动选择非全天
			$("#editForm_iworkSchCalendar_endtime").bind("click", function(){
				document.getElementById("editForm_iworkSchCalendar_isallday0").checked = true;
			});
			//点击设置提醒
			$("#editForm_iworkSchCalendar_isalert1").bind("click", function(){
				if(document.getElementById("editForm_iworkSchCalendar_isalert1").checked){
					document.getElementById("editForm_iworkSchCalendar_alerttime").disabled =false;
					document.getElementById("editForm_iworkSchCalendar_alerttime").value = "15";
				}else{
					document.getElementById("editForm_iworkSchCalendar_alerttime").disabled =true;
					document.getElementById("editForm_iworkSchCalendar_alerttime").value = "0";
				}
			});
		});
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
		var title = $.trim($("#editForm_iworkSchCalendar_title").val());
		if(title.length==0){
			flag = false;
			W.$.dialog.alert("请输入日程主题!");
		}else if(title.length>32){
			flag = false;
			W.$.dialog.alert("输入的主题请勿超过32个字符!");
		}
		var startdate = $("#editForm_iworkSchCalendar_startdate").val();
		var starttime = $("#editForm_iworkSchCalendar_starttime").val();
		var enddate = $("#editForm_iworkSchCalendar_enddate").val();
		var endtime = $("#editForm_iworkSchCalendar_endtime").val();
		var restartdate = $("#editForm_iworkSchCalendar_reStartdate").val();
		if(restartdate==null||restartdate==""){
			if(startdate==null||startdate==""){flag = false;W.$.dialog.alert("请输入开始日期");}
			if(enddate==null||enddate==""){flag = false;W.$.dialog.alert("请输入 结束日期");}
			if(starttime==null||starttime==""){flag = false;W.$.dialog.alert("请输入 开始时间");}
			if(endtime==null||endtime==""){flag = false;W.$.dialog.alert("请输入 结束时间");}
			if(startdate==enddate){
				if(checktime(starttime,endtime)){}else{flag=false;}
			}
		}
		var remark = $("#editForm_iworkSchCalendar_remark").val();
		if(remark.length>200){
			flag = false;
			W.$.dialog.alert("输入的备注请勿超过200个字符!");
		}
		return flag;
	}
	//提交表单失败
	function showRequest(formData, jqForm, options) {
		 W.$.dialog.alert("创建日程失败请联系管理员!");
		 return true; 
	}
	//成功提交表单
	function showResponse(responseText, statusText, xhr, $form)  { 
		window.parent.refetch();
		api.close();
	}
	//接收父页面传来的周期模式参数
	function setRepeateMode(){
		var reStartdateVal = W.document.getElementById('editForm_iworkSchCalendar_reStartdate').value;
		var reEnddateVal = W.document.getElementById('editForm_iworkSchCalendar_reEnddate').value;
		var reModeVal = W.document.getElementById('editForm_iworkSchCalendar_reMode').value;
		var reDayIntervalVal = W.document.getElementById('editForm_iworkSchCalendar_reDayInterval').value;
		var reWeekDateVal = W.document.getElementById('editForm_iworkSchCalendar_reWeekDate').value;
		var reMonthDaysVal = W.document.getElementById('editForm_iworkSchCalendar_reMonthDays').value;
		var reYearMonthVal = W.document.getElementById('editForm_iworkSchCalendar_reYearMonth').value;
		var reYearDaysVal = W.document.getElementById('editForm_iworkSchCalendar_reYearDays').value;
		
		var wcztVal = W.document.getElementById('editForm_iworkSchCalendar_wczt').value;
		var wcqkVal = W.document.getElementById('editForm_iworkSchCalendar_wcqk').value;
		$("#editForm_iworkSchCalendar_wczt").val(wcztVal);
		$("#editForm_iworkSchCalendar_wcqk").val(wcqkVal);
		
		$("#editForm_iworkSchCalendar_reStartdate").val(reStartdateVal);
		$("#editForm_iworkSchCalendar_reEnddate").val(reEnddateVal);
		$("#editForm_iworkSchCalendar_reMode").val(reModeVal);
		$("#editForm_iworkSchCalendar_reDayInterval").val(reDayIntervalVal);
		$("#editForm_iworkSchCalendar_reWeekDate").val(reWeekDateVal);
		$("#editForm_iworkSchCalendar_reMonthDays").val(reMonthDaysVal);
		$("#editForm_iworkSchCalendar_reYearMonth").val(reYearMonthVal);
		$("#editForm_iworkSchCalendar_reYearDays").val(reYearDaysVal);
		//将周期模式信息 替换非周期模式信息
		$("#RepeateModeInfo").html(getRepeateModeInfo(reStartdateVal,reEnddateVal,reModeVal,reDayIntervalVal,reWeekDateVal,reMonthDaysVal,reYearMonthVal,reYearDaysVal));
		document.getElementById("noneRepeateMode1").style.display = "none";
		document.getElementById("noneRepeateMode2").style.display = "none";
		document.getElementById("RepeateMode1").style.display = "block";
		document.getElementById("RepeateMode2").style.display = "block";
	}
	//将周期信息拼成字符串
	function getRepeateModeInfo(reStartdateVal,reEnddateVal,reModeVal,reDayIntervalVal,reWeekDateVal,reMonthDaysVal,reYearMonthVal,reYearDaysVal){
		var RepeateModeInfo = " <B>日程周期:</B>";//周期模式信息字符串
		var reWeekDateArr = reWeekDateVal.split("_");
		var   weekDateMap   =   {
		"1"   :  "星期天",
		"2"   :  "星期一",
		"3"   :  "星期二",
		"4"   :  "星期三",
		"5"   :  "星期四",
		"6"   :  "星期五",
		"7"   :  "星期六"
		}
		if(reModeVal==0){
			RepeateModeInfo = RepeateModeInfo + "每" +reDayIntervalVal +"天,";
		}
		if(reModeVal==1){
			for(i=0;i<reWeekDateArr.length;i++){
				RepeateModeInfo = RepeateModeInfo + weekDateMap[reWeekDateArr[i]] +","
			}
		}
		if(reModeVal==2){
			RepeateModeInfo = RepeateModeInfo + "每月第"+reMonthDaysVal+"日,";
		}
		if(reModeVal==3){
			RepeateModeInfo = RepeateModeInfo + "每年"+reYearMonthVal+"月"+reYearDaysVal+"日,";
		}
		if(reEnddateVal!=""&&reEnddateVal!=null){
			RepeateModeInfo = RepeateModeInfo + "<B>有效期：</B>从" + reStartdateVal+"到"+reEnddateVal;
		}else{
			RepeateModeInfo = RepeateModeInfo + "<B>有效期：</B>从" + reStartdateVal+"开始";
		}
		return RepeateModeInfo;
	}
	//清除周期模式按钮
	function showNoneRepeateMode(){
		document.getElementById("noneRepeateMode1").style.display = "block";
		document.getElementById("noneRepeateMode2").style.display = "block";
		document.getElementById("RepeateMode1").style.display = "none";
		document.getElementById("RepeateMode2").style.display = "none";
		$("#editForm_iworkSchCalendar_reStartdate").val("");
		$("#editForm_iworkSchCalendar_reEnddate").val("");
		$("#editForm_iworkSchCalendar_reMode").val("");
		$("#editForm_iworkSchCalendar_reDayInterval").val("");
		$("#editForm_iworkSchCalendar_reWeekDate").val("");
		$("#editForm_iworkSchCalendar_reMonthDays").val("");
		$("#editForm_iworkSchCalendar_reYearMonth").val("");
		$("#editForm_iworkSchCalendar_reYearDays").val("");
	}
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="width:530px;height:460px;padding:3px;border:0px;">
            	<s:form id ="editForm" name="editForm" action="saveSchCalendar"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	            			<td class="td_title" width="17%">主题:</td>
	            			<td class="td_data" width="83%" > 
	            				<s:textarea name="iworkSchCalendar.title" cssStyle="min-width:420px;min-height:40px;" theme="simple" maxlength="32"></s:textarea>
	            			</td>
	            		</tr>
	            		<tr>
	           				<td colspan="2">
	           				<fieldset>
		           				<legend id="dateTitle"><font color="808080" >日期设置</font></legend>
		            				<table width="100%">
		            					<tr>
		            						<td>
		            						  <div id="noneRepeateMode1" style="display: block;">
			            						<input type="radio" name="choseDate" id="choseDate0" value="0">
			            						<label>开始日期:
				            					<s:textfield name="iworkSchCalendar.startdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.startdate" format="yyyy-MM-dd"/></s:param> 
			            						</s:textfield>
		            							</label>
			            						<label>结束日期:
				            					<s:textfield name="iworkSchCalendar.enddate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.enddate" format="yyyy-MM-dd"/></s:param>
				            					</s:textfield>
			            						</label>
			            					 </div>
			            					 <div id="RepeateMode1" style="display: none;">
			            					 	<span id="RepeateModeInfo" style="width: 280px;color: #808080;font-size: 11px;"></span>
			            					 </div>
			            					</td>
		            					</tr>
		            					<tr>
		            						<td>
		            						 <div id="noneRepeateMode2" style="display: block;">
		            							<input type="radio" name="choseDate" id="choseDate1" value="1">&nbsp;周期模式
		            						 </div>
		            						 <div id="RepeateMode2" style="display: none;">
		            							<input type="button" value="清除循环周期" onclick="showNoneRepeateMode()"/>
		            							<input type="button" value="重新设置周期" onclick="resetRepeateMode()"/>
		            						 </div>
		            						</td>
		            					</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		<tr>
	           				<td colspan="2">
	           				<fieldset>
		           				<legend id="timeTitle"><font color="808080" >时间设置</font></legend>
		            				<table width="100%">
		            					<tr>
		            						<td>
			            						<input type="radio" name="iworkSchCalendar.isallday" id="editForm_iworkSchCalendar_isallday0" value="0">
			            						<label>开始时间:
			            						<s:select name="iworkSchCalendar.starttime" list="#request.timeMap" cssStyle="width:100px;" headerKey="" headerValue=""></s:select>
		            							</label>
			            						<label>结束时间:
				            					<s:select name="iworkSchCalendar.endtime" list="#request.timeMap" cssStyle="width:100px;" headerKey="" headerValue=""></s:select>
			            						</label>
		            						</td>
		            					</tr>
		            					<tr>
		            						<td>
		            							<input type="radio" name="iworkSchCalendar.isallday" id="editForm_iworkSchCalendar_isallday1" value="1">&nbsp;全天
		            						</td>
		            					</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		<tr>
	            			<td colspan="2">
	           				<fieldset>
		           				<legend id="alertTitle"><font color="808080" >提醒时间</font></legend>
		            				<table width="100%">
		            					<tr>
			            					<td width="46%">
			            						提前:&nbsp;<s:select name="iworkSchCalendar.alerttime" list="#{'0':'0分钟','5':'5分钟','10':'10分钟','15':'15分钟','30':'30分钟','45':'45分钟','60':'1小时','120':'2小时','180':'3小时','360':'6小时','720':'12小时','1080':'18小时','1440':'1天','2880':'2天','10080':'1周'}" cssStyle="width:100px;" headerKey="15" headerValue="15分钟"></s:select>
			            					</td>
			            					<td width="27%">
			            						<input type="checkbox" name="iworkSchCalendar.isalert" id="editForm_iworkSchCalendar_isalert1" onclick="setCheck(this)" value="1" checked="checked">提醒
			            					</td>
			            					<!-- 
			           						<td width="27%">
			           							<input type="checkbox" name="iworkSchCalendar.issharing" id="editForm_iworkSchCalendar_issharing1" onclick="setCheck(this)" value="1">共享
			           						</td> -->
		           						</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		
	            		<tr>
	           				<td colspan="2">
	           				<fieldset>
		           				<legend id="remarkTitle"><font color="808080" >完成情况</font></legend>
		            				<table width="100%">
		            					<tr>
			            					<td >
			            						完成状态:&nbsp;<s:select name="iworkSchCalendar.wczt" list="#{'未完成':'未完成','已完成':'已完成'}" cssStyle="width:100px;" ></s:select>
			            					</td>
			            					<tr><td><s:textarea name="iworkSchCalendar.wcqk" cssStyle="width:360px;height:60px;"  maxlength="256"/></td></tr>
			            					
		           						</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		
	            		<tr>
	           				<td colspan="2">
	           				<fieldset>
		           				<legend id="remarkTitle"><font color="808080" >备注</font></legend>
		            				<table width="100%">
		            					<tr><td><s:textarea name="iworkSchCalendar.remark" cssStyle="width:360px;height:60px;"  maxlength="256"/></td></tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            	<s:if test="null == iworkSchCalendar.id">
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td style="text-align:right" class="td_data" width="83%">
	            				<input id="sch_create" name="sch_create" class="button blue" type="button" value="创建日程" />&nbsp;&nbsp;
	            			</td>
	            		</tr>
	            	</s:if>
	            	<s:else>
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td style="text-align:right" class="td_data" width="83%">
	            				<input id="sch_update" name="sch_update" class="button blue" type="button" value="保存" />
	            				<input id="sch_delete" name="sch_delete" class="button blue" type="button" value="删除" />
	            			</td>
	            		</tr>
	            	</s:else>
	            	</table>
	                <s:hidden name="iworkSchCalendar.id"/>
	                <s:hidden name="iworkSchCalendar.userid"/>
	                <!-- 周期模式的参数 -->
	                <div style="display: none;">
		                <s:textfield name="iworkSchCalendar.reStartdate" theme="simple">
	    					<s:param name="value"><s:date name="iworkSchCalendar.reStartdate" format="yyyy-MM-dd"/></s:param> 
	     				</s:textfield>
		                <s:textfield name="iworkSchCalendar.reEnddate" theme="simple">
	    					<s:param name="value"><s:date name="iworkSchCalendar.reEnddate" format="yyyy-MM-dd"/></s:param> 
	     				</s:textfield>
     				</div>
	                <s:hidden name="iworkSchCalendar.reStarttime"/>
	                <s:hidden name="iworkSchCalendar.reEndtime"/>
	                <s:hidden name="iworkSchCalendar.reMode"/>
	                <s:hidden name="iworkSchCalendar.reDayInterval"/>
	                <s:hidden name="iworkSchCalendar.reWeekDate"/>
	                <s:hidden name="iworkSchCalendar.reMonthDays"/>
	                <s:hidden name="iworkSchCalendar.reYearMonth"/>
	                <s:hidden name="iworkSchCalendar.reYearDays"/>
	          </s:form>
	          		<s:hidden name="iworkSchCalendar.isallday"/>
	          		<s:hidden name="iworkSchCalendar.isalert"/>
	          		<s:hidden name="iworkSchCalendar.issharing"/>
	     </div>
	     <script type="text/javascript">
	     function setCheck(obj){
				if(obj.checked){
				    obj.value=1;
				}else{
				    obj.value=0;  
				}
	     	}
		function resetRepeateMode(){
	     		window.parent.popEditRepeateMode($("#editForm_iworkSchCalendar_id").val());
	     	}
	     </script>
</body>
</html>