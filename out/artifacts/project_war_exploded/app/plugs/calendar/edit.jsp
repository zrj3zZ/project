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
			
			$("#editForm_iworkSchCalendar_title").focus();
			$("#sch_create").bind("click", function(){//创建日程按钮
			    this.disabled = true;
  				var me = this;
  				setTimeout(function() { me.disabled = false; }, 5000); 
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					setSubmitVal();//根据事件时间设置是否全天事件
        			$("#editForm").ajaxSubmit(options); 
        			return false; 
				}else{
					return false;
				}
			});
			$("#sch_update").bind("click", function(){//保存修改按钮
				this.disabled = true;
  				var me = this;
  				setTimeout(function() { me.disabled = false; }, 5000);
				
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					setSubmitVal();//根据事件时间设置是否全天事件
        			$("#editForm").ajaxSubmit(options); 
        			setInterval(function(){
        				pageClose();
        			}, 250);
        			return false; 
				}else{
					return false;
				}
			});
			$("#sch_delete").bind("click", function(){//删除日程按钮
				
				if($("#editForm_iworkSchCalendar_id").val()!=null  && $("#editForm_iworkSchCalendar_id").val!=null){
					$.post('rctxxg.action',{id:$("#editForm_iworkSchCalendar_id").val()},function(data){
						if(data!="0"){
							document.getElementById("sch_delete").disabled =true;
							document.getElementById("sch_update").disabled =true;
							alert("该日程不能删除和编辑，请联系管理员");
							
						}else{
							if(confirm("确认删除当前[日程]吗？")){
								$('form').attr('action','deleteSchCalendar.action');
								var options = {
									error:showRequest,
									success:showResponse 
								};
			        			$("#editForm").ajaxSubmit(options); 
			        			return false; 
							}
						}
						
		       		});
				}
				
				
				
			});
			if($("#iworkSchCalendar_isalert").val()=="1"){
				document.getElementById("editForm_iworkSchCalendar_isalert1").checked = true;
				$("#editForm_iworkSchCalendar_isalert1").val("1");
			}else{
				document.getElementById("editForm_iworkSchCalendar_isalert1").checked = false;
				$("#editForm_iworkSchCalendar_isalert1").val("0");
			}
			$("#editForm_iworkSchCalendar_isalert1").bind("click", function(){
				if(document.getElementById("editForm_iworkSchCalendar_isalert1").checked){
					document.getElementById("editForm_iworkSchCalendar_alerttime").disabled =false;
					document.getElementById("editForm_iworkSchCalendar_alerttime").value = "15";
				}else{
					document.getElementById("editForm_iworkSchCalendar_alerttime").disabled =true;
					document.getElementById("editForm_iworkSchCalendar_alerttime").value = "0";
				}
			});
			<s:if test="null == iworkSchCalendar.id"></s:if>
				$("#editForm_iworkSchCalendar_starttime").val("08:00");
				$("#editForm_iworkSchCalendar_endtime").val("17:00");
			<s:else>
				var obj = api.data;
				//接收父页面的参数
				var titleVal = obj.editForm_iworkSchCalendar_title;
				var idVal = obj.editForm_iworkSchCalendar_id;
				var isalldayVal = obj.editForm_iworkSchCalendar_isallday;
				var useridVal = obj.editForm_iworkSchCalendar_userid;
				var startdateVal = obj.editForm_iworkSchCalendar_startdate;
				var starttimeVal = obj.editForm_iworkSchCalendar_starttime;
				var enddateVal = obj.editForm_iworkSchCalendar_enddate;
				var endtimeVal = obj.editForm_iworkSchCalendar_endtime;
				var issharingVal = obj.editForm_iworkSchCalendar_issharing;
				var remarkVal = obj.editForm_iworkSchCalendar_remark;
				var reStartdateVal = obj.editForm_iworkSchCalendar_reStartdate;
				var reEnddateVal = obj.editForm_iworkSchCalendar_reEnddate;
				var reStarttimeVal = obj.editForm_iworkSchCalendar_reStarttime;
				var reEndtimeVal = obj.editForm_iworkSchCalendar_reEndtime;
				var reModeVal = obj.editForm_iworkSchCalendar_reMode;
				var reDayIntervalVal = obj.editForm_iworkSchCalendar_reDayInterval;
				var reWeekDateVal = obj.editForm_iworkSchCalendar_reWeekDate;
				var reMonthDaysVal = obj.editForm_iworkSchCalendar_reMonthDays;
				var reYearMonthVal = obj.editForm_iworkSchCalendar_reYearMonth;
				var reYearDaysVal = obj.editForm_iworkSchCalendar_reYearDays;
				
				var wcztVal = obj.editForm_iworkSchCalendar_wczt;
				
				var wcqkVal = obj.editForm_iworkSchCalendar_wcqk;
				$("#editForm_iworkSchCalendar_wczt").val(wcztVal);
				$("#editForm_iworkSchCalendar_wcqk").val(wcqkVal);
				//赋值
				$("#editForm_iworkSchCalendar_title").val(titleVal);
				$("#editForm_iworkSchCalendar_id").val(idVal);
				$("#editForm_iworkSchCalendar_isallday").val(isalldayVal);
				$("#editForm_iworkSchCalendar_userid").val(useridVal);
				$("#editForm_iworkSchCalendar_startdate").val(startdateVal);
				$("#editForm_iworkSchCalendar_starttime").val(starttimeVal);
				$("#editForm_iworkSchCalendar_enddate").val(enddateVal);
				$("#editForm_iworkSchCalendar_endtime").val(endtimeVal);
				$("#editForm_iworkSchCalendar_issharing").val(issharingVal);
				$("#editForm_iworkSchCalendar_remark").val(remarkVal);
				$("#editForm_iworkSchCalendar_reStartdate").val(reStartdateVal);
				$("#editForm_iworkSchCalendar_reEnddate").val(reEnddateVal);
				$("#editForm_iworkSchCalendar_reStarttime").val(reStarttimeVal);
				$("#editForm_iworkSchCalendar_reEndtime").val(reEndtimeVal);
				$("#editForm_iworkSchCalendar_reMode").val(reModeVal);
				$("#editForm_iworkSchCalendar_reDayInterval").val(reDayIntervalVal);
				$("#editForm_iworkSchCalendar_reWeekDate").val(reWeekDateVal);
				$("#editForm_iworkSchCalendar_reMonthDays").val(reMonthDaysVal);
				$("#editForm_iworkSchCalendar_reYearMonth").val(reYearMonthVal);
				$("#editForm_iworkSchCalendar_reYearDays").val(reYearDaysVal);
				if($("#editForm_iworkSchCalendar_id").val()!=null && $("#editForm_iworkSchCalendar_id").val()!=""){
					 if($("#editForm_iworkSchCalendar_id").val()!=null  && $("#editForm_iworkSchCalendar_id").val!=null){
						$.post('rctxxg.action',{id:idVal},function(data){
								if(data!="0"){
								//	document.getElementById("sch_delete").disabled =true;
									document.getElementById("sch_update").disabled =true;
								//	alert("该日程不能删除和编辑，请联系管理员");
								}
								
				       		});
						}
					}
			</s:else>
			
			//初始化开始时间、结束时间
			if($("#editForm_iworkSchCalendar_reStartdate").val()!=null&&$("#editForm_iworkSchCalendar_reStartdate").val()!=""){
				$("#noRepeate_1").hide();$("#noRepeate_2").hide();
				$("#repeate_1").show();$("#repeate_2").show();
			}else{
				$("#noRepeate_1").show();$("#noRepeate_2").show();
				$("#repeate_1").hide();$("#repeate_2").hide();
			}
		});
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
	//根据事件时间设置是否全天事件
	function setSubmitVal(){
		if($("#editForm_iworkSchCalendar_reStartdate").val()!=null&&$("#editForm_iworkSchCalendar_reStartdate").val()!=""){
			$("#editForm_iworkSchCalendar_startdate").val("");
			$("#editForm_iworkSchCalendar_enddate").val("");
			if($("#editForm_iworkSchCalendar_reStarttime").val()=="00:00"&&$("#editForm_iworkSchCalendar_reEndtime").val()=="00:00"){
				$("#editForm_iworkSchCalendar_isallday").val("1");
			}else{
				$("#editForm_iworkSchCalendar_isallday").val("0");
			}
		}else{
			if($("#editForm_iworkSchCalendar_starttime").val()=="00:00"&&$("#editForm_iworkSchCalendar_endtime").val()=="00:00"){
				$("#editForm_iworkSchCalendar_isallday").val("1");
			}else{
				$("#editForm_iworkSchCalendar_isallday").val("0");
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
		var title = $.trim($("#editForm_iworkSchCalendar_title").val());
		if(title.length==0){
			flag = false;
			W.$.dialog.alert("请输入日程主题!");
		}else if(title.length>50){
			flag = false;
			W.$.dialog.alert("输入的主题请勿超过50个字符!");
		}
		var startdate = $("#editForm_iworkSchCalendar_startdate").val();
		var starttime = $("#editForm_iworkSchCalendar_starttime").val();
		var enddate = $("#editForm_iworkSchCalendar_enddate").val();
		var endtime = $("#editForm_iworkSchCalendar_endtime").val();
		var restartdate = $("#editForm_iworkSchCalendar_reStartdate").val();
		var restarttime = $("#editForm_iworkSchCalendar_reStarttime").val();
		var reenddate = $("#editForm_iworkSchCalendar_reEnddate").val();
		var reendtime = $("#editForm_iworkSchCalendar_reEndtime").val(); 
		if(restartdate==null||restartdate==""){
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
		}
		return flag;
	}
	function pageClose(){ 
		if(typeof(api) =="undefined"){
			window.close();
		}else{ 
			api.close(); 
		}
	}
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="width:545px;height:350px;padding:3px;border:0px;padding-left:10px;padding-top:10px;padding-right:10px;">
            	<s:form id ="editForm" name="editForm" action="saveSchCalendar"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	            			<td class="td_title" width="17%">主题:</td>
	            			<td class="td_data" width="83%" > 
	            				<s:textarea name="iworkSchCalendar.title" cssStyle="min-width:395px;min-height:40px;" theme="simple" maxlength="100"></s:textarea>
	            			</td>
	            		</tr>
	            		<tr id="noRepeate_1">
	            			<td class="td_title" width="17%">开始时间:</td>
	            			<td class="td_data" width="83%">
	            				<s:textfield name="iworkSchCalendar.startdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:200px;" theme="simple">
	            					<s:param name="value"><s:date name="iworkSchCalendar.startdate" format="yyyy-MM-dd"/></s:param> 
	            				</s:textfield>
	            				<s:select name="iworkSchCalendar.starttime" list="#request.timeMap" cssStyle="width:190px;" headerKey="" headerValue=""></s:select>
	            			</td>
	            		</tr>
	            		<tr id="noRepeate_2">
	            			<td class="td_title" width="17%">结束时间:</td>
	            			<td class="td_data" width="83%">
	            				<s:textfield name="iworkSchCalendar.enddate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:200px;" theme="simple">
	            					<s:param name="value"><s:date name="iworkSchCalendar.enddate" format="yyyy-MM-dd"/></s:param>
	            				</s:textfield>
	            				<s:select name="iworkSchCalendar.endtime" list="#request.timeMap" cssStyle="width:190px;" headerKey="" headerValue=""></s:select>
	            			</td>
	            		</tr>
	            		 <tr id="repeate_1">
	            			<td class="td_title" width="17%">开始时间:</td>
	            			<td class="td_data" width="83%">
	            				<s:textfield name="iworkSchCalendar.reStartdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:200px;" theme="simple">
	            					<s:param name="value"><s:date name="iworkSchCalendar.reStartdate" format="yyyy-MM-dd"/></s:param> 
	            				</s:textfield>
	            				<s:select name="iworkSchCalendar.reStarttime" list="#request.timeMap" cssStyle="width:190px;" headerKey="" headerValue=""></s:select>
	            			</td>
	            		</tr>
	            		<tr id="repeate_2">
	            			<td class="td_title" width="17%">结束时间:</td>
	            			<td class="td_data" width="83%">
	            				<s:textfield name="iworkSchCalendar.reEnddate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:200px;" theme="simple">
	            					<s:param name="value"><s:date name="iworkSchCalendar.reEnddate" format="yyyy-MM-dd"/></s:param>
	            				</s:textfield>
	            				<s:select name="iworkSchCalendar.reEndtime" list="#request.timeMap" cssStyle="width:190px;" headerKey="" headerValue=""></s:select>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title" width="17%">
	            				提前:
	            			</td>
	            			<td class="td_data" width="83%">
	            				&nbsp;<s:select name="iworkSchCalendar.alerttime" list="#{'0':'0分钟','5':'5分钟','10':'10分钟','15':'15分钟','30':'30分钟','45':'45分钟','60':'1小时','120':'2小时','180':'3小时','360':'6小时','720':'12小时','1080':'18小时','1440':'1天','2880':'2天','10080':'1周'}" cssStyle="width:100px;"></s:select>
	            				<input type="checkbox" name="iworkSchCalendar.isalert" id="editForm_iworkSchCalendar_isalert1" onclick="setCheck(this)" value="0">提醒
	            			</td>
	            			<!-- 
	           				<td width="27%">
	           					<input type="checkbox" name="iworkSchCalendar.issharing" id="editForm_iworkSchCalendar_issharing1" onclick="setCheck(this)" value="1">共享
	           				</td> -->
           				</tr>
           				
           				
           				<tr>
	            			<td class="td_title" width="17%">
	            				完成状态:
	            			</td>
	            			<td class="td_data" width="83%">
	            				&nbsp;<s:select name="iworkSchCalendar.wczt" list="#{'未完成':'未完成','已完成':'已完成'}" cssStyle="width:100px;"></s:select>
	            			</td>
           				</tr>
           				<tr>
	            			<td class="td_title" width="17%">
	            				具体工作内容:
	            			</td>
	            			<td class="td_data" width="83%">
	            				<s:textarea name="iworkSchCalendar.wcqk" cssStyle="min-width:395px;min-height:140px;" theme="simple" maxlength="512"></s:textarea>
	            			</td>
           				</tr>
           				
           				
	            	<s:if test="null == iworkSchCalendar.id">
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td class="td_data" width="83%">
	            				<input id="sch_create" name="sch_create" class="button blue" type="button" value="创建日程" />&nbsp;&nbsp;
	            				<a href="javascript:void(0)" onclick="addAdvance();"><font style="color: blue">高级选项>></font></a>
	            			</td>
	            		</tr>
	            	</s:if>
	            	<s:else>
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td class="td_data" width="83%">
	            				<input id="sch_update" name="sch_update" class="button blue" type="button" value="保存" />
	            				<input id="sch_delete" name="sch_delete" class="button blue" type="button" value="删除" />
	            				<a href="javascript:void(0)" onclick="editAdvance();"><font style="color: blue">高级选项>></font></a>
	            			</td>
	            		</tr>
	            	</s:else>
	            	</table>
	                <s:hidden name="iworkSchCalendar.id"/>
	                <s:hidden name="iworkSchCalendar.isallday"/>
	                <s:hidden name="iworkSchCalendar.userid"/>
	                <s:hidden name="iworkSchCalendar.issharing"/>
	                <s:hidden name="iworkSchCalendar.remark"/>
	                <s:hidden name="iworkSchCalendar.reMode"/>
	                <s:hidden name="iworkSchCalendar.reDayInterval"/>
	                <s:hidden name="iworkSchCalendar.reWeekDate"/>
	                <s:hidden name="iworkSchCalendar.reMonthDays"/>
	                <s:hidden name="iworkSchCalendar.reYearMonth"/>
	                <s:hidden name="iworkSchCalendar.reYearDays"/>
	          </s:form> 
	          <s:hidden name="iworkSchCalendar.isalert"/>
	     </div>
	     <script type="text/javascript">
	     function setCheck(obj){
				if(obj.checked){
				    obj.value=1;
				}else{
				    obj.value=0;  
				}
	     	}
		     //跳转创建日程高级选项
		function addAdvance(){
			var title=$("#editForm_iworkSchCalendar_title").val();
			var startDate = $("#editForm_iworkSchCalendar_startdate").val();
			var endDate = $("#editForm_iworkSchCalendar_enddate").val();
			var startTime = $("#editForm_iworkSchCalendar_starttime").val();
			var endTime = $("#editForm_iworkSchCalendar_endtime").val();
			var allDay = $("#editForm_iworkSchCalendar_isallday").val();
			
			var wczt = $("#editForm_iworkSchCalendar_wczt").val();
			var wcqk = $("#editForm_iworkSchCalendar_wcqk").val();
			if(startTime==endTime&&startTime=="00:00"){
				allDay = 1;
			}else{
				allDay = 0;
			}
			window.location.href="addSchCalendar_Advance.action?startDate="+startDate+"&endDate="+endDate+"&startTime="+startTime+"&endTime="+endTime+"&allDay="+allDay+"&title="+encodeURI(encodeURI(title))+"&wczt="+encodeURI(encodeURI(wczt))+"&wcqk="+encodeURI(encodeURI(wcqk));
		}
		//跳转编辑日程高级选项
		function editAdvance(){
			if($("#editForm_iworkSchCalendar_id").val()!=null  && $("#editForm_iworkSchCalendar_id").val!=null){
				$.post('rctxxg.action',{id:$("#editForm_iworkSchCalendar_id").val()},function(data){
					if(data!="0"){
						document.getElementById("sch_delete").disabled =true;
						document.getElementById("sch_update").disabled =true;
						alert("该日程不能删除和编辑，请联系管理员");
						
					}else{
						var id = $("#editForm_iworkSchCalendar_id").val();
						window.location.href="editSchCalendar_Advance.action?id="+id;
					}
					
	       		});
			}
		
		
		}
		</script>	     
</body>
</html>