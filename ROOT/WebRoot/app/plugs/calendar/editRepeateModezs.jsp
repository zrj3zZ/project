<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%java.util.HashMap map = new java.util.LinkedHashMap();
for(int i=1;i<32;i++){
	map.put(i,i);
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
	
	<link rel="stylesheet" type="text/css" href="iwork_css/plugs/editrepeatemode.css" />
	
	<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
	//控制周期模式tab页签
	function selectTag(obj){
		for(m=0;m<4;m++){
			if(obj.value==m){
				for(i=0;j=document.getElementById("reMode"+i); i++){
					j.style.display = "none";
				}
				resetForm(m);
				document.getElementById("reMode"+m).style.display = "block";
			}
		}
	}
	//编辑的时候初始化TAB标签
	function initTag(obj){
		for(m=0;m<4;m++){
			if(obj==m){
				for(i=0;j=document.getElementById("reMode"+i); i++){
					j.style.display = "none";
				}
				document.getElementById("reMode"+m).style.display = "block";
			}
		}
	}
	//清空表格(obj为循环模式)
	function resetForm(obj){
		if(obj==0){
			for(i=1;i<8;i++){
				document.getElementById("editForm_reWeekMode-"+i).checked=false;
			}
			document.getElementById("editForm_iworkSchCalendar_reMonthDays").value="";
			document.getElementById("editForm_iworkSchCalendar_reYearMonth").value="";
			document.getElementById("editForm_iworkSchCalendar_reYearDays").value="";
		}else if(obj==1){
			document.getElementById("editForm_iworkSchCalendar_reDayInterval").value="";
			document.getElementById("editForm_iworkSchCalendar_reMonthDays").value="";
			document.getElementById("editForm_iworkSchCalendar_reYearMonth").value="";
			document.getElementById("editForm_iworkSchCalendar_reYearDays").value="";
		}else if(obj==2){
			document.getElementById("editForm_iworkSchCalendar_reDayInterval").value="";
			for(i=1;i<8;i++){
				document.getElementById("editForm_reWeekMode-"+i).checked=false;
			}
			document.getElementById("editForm_iworkSchCalendar_reYearMonth").value="";
			document.getElementById("editForm_iworkSchCalendar_reYearDays").value="";
		}else if(obj==3){
			document.getElementById("editForm_iworkSchCalendar_reDayInterval").value="";
			for(i=1;i<8;i++){
				document.getElementById("editForm_reWeekMode-"+i).checked=false;
			}
			document.getElementById("editForm_iworkSchCalendar_reMonthDays").value="";
		}
	}
	//设置结束日期为永不过期或为有结束日期
	function selectEndDate(obj){
		if(obj.value==1){
			document.getElementById("editForm_iworkSchCalendar_reEnddate").disabled  = true ;
			document.getElementById("editForm_iworkSchCalendar_reEnddate").value = "";
		}else{
			document.getElementById("editForm_iworkSchCalendar_reEnddate").disabled  = false;
		}
	}
	//拼接按周循环的周期模式
	function getReWeekDate(){
		var reWeekDate = "";
		for(i=1;i<8;i++){
			if(document.getElementById("editForm_reWeekMode-"+i).checked){
				reWeekDate = reWeekDate + i + "_" ;
			}
		}
		if(reWeekDate.length>1){reWeekDate = reWeekDate.substr(0,reWeekDate.length-1);}
		document.getElementById("editForm_iworkSchCalendar_reWeekDate").value = reWeekDate;
	}
	//判断输入的月份和天数是否合法
	function checkDate(m,d){
		var dateFlag = true;
		var b_Manth = [1,3,5,7,8,10,12];
		var s_Month = [4,6,9,11];
		for(i=0;i<7;i++){
			if(m==b_Manth[i]&&d>31){
				dateFlag =  false;
			}
		}
		for(i=0;i<4;i++){
			if(m==s_Month[i]&&d>30){
				dateFlag =  false;
			}
		}
		if(m==2&&d>29){
			dateFlag =  false;
		}
		return dateFlag;
	}
	
	//将周期信息传给高级选项
	$(document).ready(function(){
		//抓取周期模式信息，并初始化 
		<s:if test="null == iworkSchCalendar.id"></s:if>
		<s:else>
		var reStartdateVal = W.$.dialog.data('editForm_iworkSchCalendar_reStartdate');
		var reEnddateVal = W.$.dialog.data('editForm_iworkSchCalendar_reEnddate');
		var reModeVal = W.$.dialog.data('editForm_iworkSchCalendar_reMode');
		var reDayIntervalVal = W.$.dialog.data('editForm_iworkSchCalendar_reDayInterval');
		var reWeekDateVal = W.$.dialog.data('editForm_iworkSchCalendar_reWeekDate');
		var reMonthDaysVal = W.$.dialog.data('editForm_iworkSchCalendar_reMonthDays');
		var reYearMonthVal = W.$.dialog.data('editForm_iworkSchCalendar_reYearMonth');
		var reYearDaysVal = W.$.dialog.data('editForm_iworkSchCalendar_reYearDays');
		$("#editForm_iworkSchCalendar_reStartdate").val(reStartdateVal);
		$("#editForm_iworkSchCalendar_reEnddate").val(reEnddateVal);
		$("#editForm_iworkSchCalendar_reMode").val(reModeVal);
		$("#editForm_iworkSchCalendar_reDayInterval").val(reDayIntervalVal);
		$("#editForm_iworkSchCalendar_reWeekDate").val(reWeekDateVal);
		$("#editForm_iworkSchCalendar_reMonthDays").val(reMonthDaysVal);
		$("#editForm_iworkSchCalendar_reYearMonth").val(reYearMonthVal);
		$("#editForm_iworkSchCalendar_reYearDays").val(reYearDaysVal);
		if(reModeVal==0){document.getElementById("editForm_iworkSchCalendar_reMode0").checked=true;initTag(0);}
		else if(reModeVal==1){document.getElementById("editForm_iworkSchCalendar_reMode1").checked=true;initTag(1);}
		else if(reModeVal==2){document.getElementById("editForm_iworkSchCalendar_reMode2").checked=true;initTag(2);}
		else if(reModeVal==3){document.getElementById("editForm_iworkSchCalendar_reMode3").checked=true;initTag(3);}
		if(null!=reWeekDateVal&&""!=reWeekDateVal){
			var reWeekDateArr = reWeekDateVal.split("_");
			for(i=0;i<reWeekDateArr.length;i++){
			document.getElementById("reWeekMode-"+reWeekDateArr[i]).checked=true;
			}
		}
		</s:else>
		//初始化结束日期自动选择非永不过期
		if($("#editForm_iworkSchCalendar_reEnddate").val()!=""&&$("#editForm_iworkSchCalendar_reEnddate").val()!=null){
			document.getElementById("choseEndDate0").checked=true;
		}
		$("#editForm_iworkSchCalendar_reEnddate").bind("click", function(){
			document.getElementById("choseEndDate0").checked = true;
		});
		$("#editForm_iworkSchCalendar_reDayInterval").bind("click", function(){
			document.getElementById("editForm_iworkSchCalendar_reMode0").checked = true;
		});
		$("#sch_save").bind("click", function(){
			var flag = true;
			if(document.getElementById("editForm_iworkSchCalendar_reMode0").checked){
				if($("#editForm_iworkSchCalendar_reDayInterval").val()=="" || $("#editForm_iworkSchCalendar_reDayInterval").val()==null){
					alert("您未输入完整的按天循环的信息!");
					flag = false;
				}
				if($("#editForm_iworkSchCalendar_reDayInterval").val()===="0"){
					W.$.dialog.alert("请输入大于0的整数!");
					flag = false;
				}
			}
			if(document.getElementById("editForm_iworkSchCalendar_reMode1").checked){
				getReWeekDate();
				if($("#editForm_iworkSchCalendar_reWeekDate").val()=="" || $("#editForm_iworkSchCalendar_reWeekDate").val()==null){
					alert("您未输入完整的按周循环的信息!");
					flag = false;
				}
			}
			if(document.getElementById("editForm_iworkSchCalendar_reMode2").checked){
				if($("#editForm_iworkSchCalendar_reMonthDays").val()=="" || $("#editForm_iworkSchCalendar_reMonthDays").val()==null){
				alert("您未输入完整的按月循环的信息!");
					flag = false;
				}
			}
			if(document.getElementById("editForm_iworkSchCalendar_reMode3").checked){
				flag = checkDate($("#editForm_iworkSchCalendar_reYearMonth").val(),$("#editForm_iworkSchCalendar_reYearDays").val());
				if($("#editForm_iworkSchCalendar_reYearMonth").val()=="" || $("#editForm_iworkSchCalendar_reYearMonth").val()==null){
					alert("您未输入完整的按年循环的信息!");
					flag = false;
				}
				if($("#editForm_iworkSchCalendar_reYearDays").val()=="" || $("#editForm_iworkSchCalendar_reYearDays").val()==null){
					alert("您未输入完整的按年循环的信息!");
					flag = false;
				}
			}
			//取得循环模式中的信息传给父页面
			var reStartdate = $("#editForm_iworkSchCalendar_reStartdate").val();
			var reEnddate = $("#editForm_iworkSchCalendar_reEnddate").val();
			var reMode = "";
			var reModeArr = document.getElementsByName("iworkSchCalendar.reMode");
			for (i=0;i<reModeArr.length;i++){
				if(reModeArr[i].checked){
					reMode = reModeArr[i].value;
				}
			}
			var reDayInterval = $("#editForm_iworkSchCalendar_reDayInterval").val();
			var reWeekDate = $("#editForm_iworkSchCalendar_reWeekDate").val();
			var reMonthDays = $("#editForm_iworkSchCalendar_reMonthDays").val();
			var reYearMonth = $("#editForm_iworkSchCalendar_reYearMonth").val();
			var reYearDays = $("#editForm_iworkSchCalendar_reYearDays").val();
			if(reStartdate==""||reStartdate==null){
				flag = false;
			}
			if(reMode==""||reMode==null){
				flag = false;
			}
			if(flag){
		
				window.parent.getRepeateMode(reStartdate,reEnddate,reMode,reDayInterval,reWeekDate,reMonthDays,reYearMonth,reYearDays);
			}
		});
	});
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="padding:3px;border:0px;width: 400px">
            	<s:form id ="editForm" name="editForm" action="saveSchCalendar"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	           				<td colspan="2">
	           				<fieldset>
		           				<legend id="dateTitle"><font color="808080" >周期模式</font></legend>
		            				<table width="100%">
		            					<tr>
		            					    <td width="25%" style="border-right:solid 1px #004080;">
		            							<table width="70px;"><tr><td><s:radio name="iworkSchCalendar.reMode" list="#{'0':'按天','1':'按周','2':'按月','3':'按年'}" value="0" onclick="selectTag(this);"></s:radio></td></tr></table>
			            					</td>
		            						<td width="75%">
		            							<div id="reMode0" style="display: block;padding-left: 10px;padding-top:10px">
		            								每<s:textfield  onkeyup="value=(parseInt((value=value.replace(/\D/g,''))==''||parseInt((value=value.replace(/\D/g,''))==0)?'1':value,10))" onafterpaste="value=(parseInt((value=value.replace(/\D/g,''))==''||parseInt((value=value.replace(/\D/g,''))==0)?'1':value,10))"  name="iworkSchCalendar.reDayInterval" cssStyle="width:20px;"></s:textfield>天
		            							</div>
		            							<div id="reMode1" style="display: none;">
		            								&nbsp;&nbsp;每周<br/>
		            								<s:checkboxlist name="reWeekMode" list="#{'1':'星期天','2':'星期一','3':'星期二','4':'星期三','5':'星期四','6':'星期五','7':'星期六'}"></s:checkboxlist>
		            							</div>
		            							<div id="reMode2" style="display: none;padding-left: 10px;padding-top:10px">
		            								每月&nbsp;第<s:select name="iworkSchCalendar.reMonthDays" list="#request.timeMap" headerKey="" headerValue=""></s:select>天
		            							</div>
		            							<div id="reMode3" style="display: none;padding-left: 10px;padding-top:10px">
		            								每年&nbsp第<s:select name="iworkSchCalendar.reYearMonth" list="#{'1':'1月','2':'2月','3':'3月','4':'4月','5':'5月','6':'6月','7':'7月','8':'8月','9':'9月','10':'10月','11':'11月','12':'12月'}" headerKey="" headerValue=""></s:select>
		            								&nbsp第<s:select name="iworkSchCalendar.reYearDays" list="#request.timeMap" headerKey="" headerValue=""></s:select>天
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
		           				<legend id="dateTitle"><font color="808080" >重复范围</font></legend>
		            				<table width="100%">
		            					<tr>
		            						<td width="45%">
			            						<label>开始日期:
				            					<s:textfield name="iworkSchCalendar.reStartdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_reEnddate\\')||\\'2050-10-01\\'}'})" cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.reStartdate" format="yyyy-MM-dd"/></s:param> 
			            						</s:textfield>
		            							</label>
			            					</td>
			            					<td width="55%">
			            						<input type="radio" name="choseEndDate" id="choseEndDate0" onclick="selectEndDate(this)" value="0"/>
		            							<label>结束日期:
				            					<s:textfield name="iworkSchCalendar.reEnddate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_reStartdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.reEnddate" format="yyyy-MM-dd"/></s:param>
				            					</s:textfield>
			            						</label>
			            					</td>
		            					</tr>
		            					<tr>
		            						<td width="45%"></td>
		            						<td width="55%">
		            							<input type="radio" name="choseEndDate" id="choseEndDate1" onclick="selectEndDate(this)" value="1"/>&nbsp;永不过期
		            						</td>
		            					</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			<td class="td_data" width="83%">
	            				<input id="sch_save" name="sch_save" class="button blue" type="button" value="保存" />&nbsp;&nbsp;&nbsp;
	            			</td>
	            		</tr>
	            	</table>
	                <s:hidden name="iworkSchCalendar.reWeekDate"/>
	          </s:form> 
	          	 <s:hidden name="iworkSchCalendar.id"/>
	     </div>
	</body>
</html>