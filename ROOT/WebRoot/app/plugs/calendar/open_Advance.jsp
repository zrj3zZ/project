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
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
	
		$(document).ready(function(){
			var oldParentUUIDs = $("#INSTANCEID",artDialog.open.origin.document).attr("value");
			document.getElementById("INSTANCEID1").value=oldParentUUIDs;
				document.getElementById("choseEndDate1").checked=true;
				document.getElementById("reEnddate").disabled  = true ;
			document.getElementById("zqmsdiv2").style.display = "none";
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
				//var re_YearDays = $("#editForm_iworkSchCalendar_reYearDays").val();zqdays
				var re_YearDays = $("#zqdays").val();
				//alert(re_YearDays);
				//alert(re_YearMonth);
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
			
			
			</s:else>
			//创建日程按钮
			$("#sch_create").bind("click", function(){
				 var temp = document.getElementsByName("choseDate");
				  for(var i=0;i<temp.length;i++)
				  {
				     if(temp[i].checked)
				    	
				    	 document.getElementById("choseDatev").value = temp[i].value;
				  }
				 
				  var temp1 = document.getElementsByName("iworkSchCalendar.isallday");
				  for(var i=0;i<temp1.length;i++)
				  {
				     if(temp1[i].checked)
				    	 document.getElementById("isalldayv").value = temp1[i].value;
				  }
				  var temp2 = document.getElementsByName("iworkSchCalendar.reMode");
				  for(var i=0;i<temp2.length;i++)
				  {
				     if(temp2[i].checked)
				    	 document.getElementById("reMode9").value=temp2[i].value;
				  }
					if(document.getElementById("editForm_iworkSchCalendar_reMode0").checked){
						 document.getElementById("reDayInterval1").value=$("#editForm_iworkSchCalendar_reDayInterval").val();
					}
				document.getElementById("title1").value=document.getElementById("title").value;
				document.getElementById("starttime2").value=document.getElementById("starttime1").value;
				document.getElementById("endtime2").value=document.getElementById("endtime1").value;
				document.getElementById("starttime3").value=document.getElementById("starttime1").value;
				document.getElementById("endtime3").value=document.getElementById("endtime1").value;
				document.getElementById("alerttime2").value=document.getElementById("alerttime1").value;
				document.getElementById("isalert1").value=document.getElementById("editForm_iworkSchCalendar_isalert1").value;
				document.getElementById("remark2").value=document.getElementById("remark1").value;
				document.getElementById("reStartdate1").value=document.getElementById("reStartdate").value;
				document.getElementById("reEnddate1").value=document.getElementById("reEnddate").value;
				
				document.getElementById("iworkSchCalendar.startdate1").value=$("input[name='iworkSchCalendar.startdate']")[1].value;
				document.getElementById("iworkSchCalendar.enddate1").value=$("input[name='iworkSchCalendar.enddate']")[1].value;
				if(document.getElementById("editForm_iworkSchCalendar_reMode1").checked){
					document.getElementById("reWeekDate1").value=$("#editForm_iworkSchCalendar_reWeekDate").val();
				}
				
				if(document.getElementById("editForm_iworkSchCalendar_reMode2").checked){
					document.getElementById("reMonthDays1").value=$("#editForm_iworkSchCalendar_reMonthDays").val();

				}
				
				if(document.getElementById("editForm_iworkSchCalendar_reMode3").checked){
					if($("#editForm_iworkSchCalendar_reYearMonth").val()=="" || $("#editForm_iworkSchCalendar_reYearMonth").val()==null){
						document.getElementById("reYearMonth1").value=$("#editForm_iworkSchCalendar_reYearMonth").val();
					}
					if($("#editForm_iworkSchCalendar_reYearDays").val()=="" || $("#editForm_iworkSchCalendar_reYearDays").val()==null){
						document.getElementById("reYearDays1").value=$("#editForm_iworkSchCalendar_reYearDays").val();
					}
				} 
				
				
				
				
				var checkResult = checkSubmitVal();
				if(checkResult){
					var options = {
						error:showRequest,
						success:showResponse 
					};
					$("#editForm_iworkSchCalendar_reStarttime").val($("#editForm_iworkSchCalendar_starttime").val());
					$("#editForm_iworkSchCalendar_reEndtime").val($("#editForm_iworkSchCalendar_endtime").val());
					$("#testzzz").ajaxSubmit(options); 
			
        			return false; 
				}else{
					return false; 
				}
			});
			//保存修改按钮
			$("#sch_update").bind("click", function(){
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
				//popAddRepeateMode($("#editForm_iworkSchCalendar_startdate").val(),$("#editForm_iworkSchCalendar_enddate").val());//弹出设置周期窗口
				document.getElementById("editForm_iworkSchCalendar_startdate").disabled =true;
				$("#editForm_iworkSchCalendar_startdate").val("");
				document.getElementById("editForm_iworkSchCalendar_enddate").disabled =true;
				$("#editForm_iworkSchCalendar_enddate").val("");
				//$("#editForm_iworkSchCalendar_enddate").val("");
				document.getElementById("zqmsdiv2").style.display = "block";
			});
			//点击设置非周期模式按钮
			$("#choseDate0").bind("click", function(){
				document.getElementById("editForm_iworkSchCalendar_startdate").disabled =false;
				document.getElementById("editForm_iworkSchCalendar_enddate").disabled =false;
				document.getElementById("zqmsdiv2").style.display = "none";
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
			$("#sch_save").bind("click", function(){
				var flag = true;
				if(document.getElementById("editForm_iworkSchCalendar_reMode0").checked){
					if($("#editForm_iworkSchCalendar_reDayInterval").val()=="" || $("#editForm_iworkSchCalendar_reDayInterval").val()==null){
						alert("您未输入完整的按天循环的信息!");
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
			
				if($("#reStartdate").val()=="" || $("#reStartdate").val()==null){
					alert("请选择开始日期!");
					flag = false;
				}
				if(!document.getElementById("reEnddate").disabled){
					if($("#reEnddate").val()=="" || $("#reEnddate").val()==null){
						alert("请选择结束日期!");
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
				
				if(flag){
				
					setRepeateMode(reStartdate,reEnddate,reMode,reDayInterval,reWeekDate,reMonthDays,reYearMonth,reYearDays);
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
		
		}else if(startArr[0]==endArr[0]&&startArr[0]!="00"){
			if(startArr[1]>endArr[1]){
				timeFalg = false;
			}else if(startArr[1]==endArr[1]){
				timeFalg = false;
			}
		}else if(startArr[0]==endArr[0]&&startArr[0]=="00"){
			if(startArr[1]>endArr[1]){
				timeFalg = false;
			}else if(startArr[1]==endArr[1]&&startArr[1]=="30"){
				timeFalg = false;
			}
		}
		return timeFalg;
	}
	//检测所有提交数据的合法性
	function checkSubmitVal(){
		var flag = true;
		var title = document.getElementById("title");
		if(title.value=="" || title.value==null){
			alert("请输入主题"); 
			title.focus();
			flag = false;
		}

		
		var startdate = $("#editForm_iworkSchCalendar_startdate").val();
		var starttime = $("#starttime1").val();
		var enddate = $("#editForm_iworkSchCalendar_enddate").val();
		var endtime = $("#endtime1").val();
		var restartdate = $("#editForm_iworkSchCalendar_reStartdate").val();
		if(restartdate==null||restartdate==""){
			if(document.getElementById("choseDate0").checked){
				if(startdate==null||startdate==""){flag = false;alert("请输入开始日期");}
				if(enddate==null||enddate==""){flag = false;alert("请输入 结束日期");}
			}
			
			
			if(starttime==null||starttime==""){flag = false;alert("请输入 开始时间");}
			if(endtime==null||endtime==""){flag = false;alert("请输入 结束时间");}
			if(startdate==enddate){
				if(checktime(starttime,endtime)){}else{flag=false;}
			}
		}
		var remark = $("#remark1").val();
		if(remark.length>200){
			flag = false;
		}
		return flag;
	}
	//提交表单失败
	function showRequest(formData, jqForm, options) {
		 alert("创建日程失败请联系管理员!");
		 return true; 
	}
	var api,W;
	try{
		api=art.dialog.open.api;
		W=api.opener;
	}catch(e){}
	//成功提交表单
	function showResponse(responseText, statusText, xhr, $form)  {
		alert("创建日程成功");
		api.close();
	
	}
	//接收父页面传来的周期模式参数
	function setRepeateMode(reStartdateVal,reEnddateVal,reModeVal,reDayIntervalVal,reWeekDateVal,reMonthDaysVal,reYearMonthVal,reYearDaysVal){
		//将周期模式信息 替换非周期模式信息
		$("#RepeateModeInfo").html(getRepeateModeInfo(reStartdateVal,reEnddateVal,reModeVal,reDayIntervalVal,reWeekDateVal,reMonthDaysVal,reYearMonthVal,reYearDaysVal));
		document.getElementById("noneRepeateMode1").style.display = "none";
		document.getElementById("noneRepeateMode2").style.display = "none";
		document.getElementById("RepeateMode1").style.display = "block";
		document.getElementById("RepeateMode2").style.display = "block";
		document.getElementById("zqmsdiv2").style.display = "none";
	}
	//将周期信息拼成字符串
	function getRepeateModeInfo(reStartdateVal,reEnddateVal,reModeVal,reDayIntervalVal,reWeekDateVal,reMonthDaysVal,reYearMonthVal,reYearDaysVal){
		//alert(reYearDaysVal);
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
			RepeateModeInfo = RepeateModeInfo + "每" +reDayIntervalVal +"转让日,";
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
		 
		var kssj=document.getElementById("reStartdate").value;
		 var jssj=document.getElementById("reEnddate").value;
		if(jssj!=""&&jssj!=null){
			RepeateModeInfo = RepeateModeInfo + "<B>有效期：</B>从" + kssj+"到"+jssj;
		}else{
			RepeateModeInfo = RepeateModeInfo + "<B>有效期：</B>从" + kssj+"开始";
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
		document.getElementById("zqmsdiv2").style.display = "block";
	}

	function popAddRepeateMode(start,end){

		var pageUrl ="addRepeateModezs.action?startDate="+start+"&endDate="+end;
			art.dialog.open(pageUrl,{
				title:'编辑日程周期',
				loadingText:'正在加载中,请稍后...',
				rang:true,
			
				cache:false,
				lock: true,
				height:300, 
				
				
			});
}
	//设置结束日期为永不过期或为有结束日期
	function selectEndDate(obj){
		if(obj.value==1){
			document.getElementById("reEnddate").disabled  = true ;
			document.getElementById("reEnddate").value = "";
		}else{
			document.getElementById("reEnddate").disabled  = false;
		}
	}
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
	</script>
</head>
<body  class="easyui-layout">
	<s:form id="testzzz" name="testzzz" action="saveSchCalendars" theme="simple">
		<s:hidden name="iworkSchCalendar.title" id="title1" />
		<s:hidden name="isShare" id="choseDatev" />
		<s:hidden name="iworkSchCalendar.isallday" id="isalldayv" />
		<s:hidden name="iworkSchCalendar.starttime" id="starttime2" />
		
		<s:hidden name="iworkSchCalendar.startdate" id="iworkSchCalendar.startdate1" />
		<s:hidden name="iworkSchCalendar.enddate" id="iworkSchCalendar.enddate1" />
		
		<s:hidden name="iworkSchCalendar.endtime" id="endtime2" />
		<s:hidden name="iworkSchCalendar.alerttime" id="alerttime2" />
		<s:hidden name="iworkSchCalendar.isalert" id="isalert1" />
		<s:hidden name="iworkSchCalendar.remark" id="remark2" />
		<s:hidden name="iworkSchCalendar.id" />
		<s:hidden name="iworkSchCalendar.userid" />
		<s:hidden name="iworkSchCalendar.reStartdate" id="reStartdate1" />
		<s:hidden name="iworkSchCalendar.reEnddate" id="reEnddate1" />
		<s:hidden name="iworkSchCalendar.reStarttime" id="starttime3" />
		<s:hidden name="iworkSchCalendar.reEndtime" id="endtime3" />
		<s:hidden name="iworkSchCalendar.reMode" id="reMode9" />
		<s:hidden name="iworkSchCalendar.reDayInterval" id="reDayInterval1" />
		<s:hidden name="iworkSchCalendar.reWeekDate" id="reYearDays1" />
		<s:hidden name="iworkSchCalendar.reMonthDays" id="reYearDays1" />
		<s:hidden name="iworkSchCalendar.reYearMonth" id="reYearDays1" />
		<s:hidden name="iworkSchCalendar.reYearDays" id="reYearDays1" />
		<s:hidden name="instanceId" id="INSTANCEID1" />
	</s:form>
	<div region="center" style="width:530px;height:460px;padding:3px;border:0px;">
            	<s:form id ="editForm" name="editForm" action="saveSchCalendar"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	            			<td class="td_title" width="17%">事项描述:</td>
	            				
	            			<td class="td_data" width="83%" > 
	            			<input  id="title" name="iworkSchCalendar.title" style="width: 95%;" value=" <s:property value="iworkSchCalendar.title" />"  disabled="disabled" maxlength="32"/>
	            			
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
			            						<input type="radio" name="choseDate" id="choseDate0" disabled="disabled" value="0">
			            						<label>开始日期:
				            					<s:textfield   name="iworkSchCalendar.startdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_enddate\\')||\\'2050-10-01\\'}'})" cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.startdate" format="yyyy-MM-dd"/></s:param> 
			            						</s:textfield>
		            							</label>
			            						<label>结束日期:
				            					<s:textfield  name="iworkSchCalendar.enddate" onclick="WdatePicker({minDate:'#F{$dp.$D(\\'editForm_iworkSchCalendar_startdate\\')}',maxDate:'2050-10-01'})" cssStyle="width:100px;" theme="simple">
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
		            							<input type="radio" name="choseDate" id="choseDate1" disabled="disabled" value="1">&nbsp;周期模式
		            						 </div>
		            						 <div id="RepeateMode2" style="display: none;">
		            							
		            						
		            						 </div>
		            						 <div id="zqmsdiv2" region="center" style="padding:3px;border:0px;width: 400px">
            	
	            	<table border="0"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	           				<td colspan="2">
	           				<fieldset>
		           				<legend id="dateTitle"><font color="808080" >周期模式</font></legend>
		            				<table width="100%">
		            					<tr>
		            					    <td width="25%" style="border-right:solid 1px #004080;">
		            							<table width="70px;"><tr><td><s:radio name="iworkSchCalendar.reMode" disabled="disabled" list="#{'0':'按天','1':'按周','2':'按月','3':'按年'}" value="0" onclick="selectTag(this);"></s:radio></td></tr></table>
			            					</td>
		            						<td width="75%">
		            							<div id="reMode0" style="display: block;padding-left: 10px;padding-top:10px">
		            								每<s:textfield onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" name="iworkSchCalendar.reDayInterval" cssStyle="width:20px;"></s:textfield>转让日
		            							</div>
		            							<div id="reMode1" style="display: none;">
		            								&nbsp;&nbsp;每周<br/>
		            								<s:checkboxlist name="reWeekMode" list="#{'1':'星期天','2':'星期一','3':'星期二','4':'星期三','5':'星期四','6':'星期五','7':'星期六'}"></s:checkboxlist>
		            							</div>
		            							<div id="reMode2" style="display: none;padding-left: 10px;padding-top:10px">
		            								每月&nbsp;第<s:textfield onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" name="iworkSchCalendar.reMonthDays" cssStyle="width:20px;" headerKey="" headerValue=""></s:textfield>天
		            							</div>
		            							<div id="reMode3" style="display: none;padding-left: 10px;padding-top:10px">
		            								每年&nbsp第<s:select name="iworkSchCalendar.reYearMonth" list="#{'1':'1月','2':'2月','3':'3月','4':'4月','5':'5月','6':'6月','7':'7月','8':'8月','9':'9月','10':'10月','11':'11月','12':'12月'}" headerKey="" headerValue=""></s:select>
		            								&nbsp第<s:select name="iworkSchCalendar.reYearDays" list="#request.timeMap" headerKey="" headerValue=""></s:select>天
		            							
		            							<input type="hidden" value="<s:property value="iworkSchCalendar.reYearDays" />" id="zqdays">
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
				            					<s:textfield disabled="disabled" id="reStartdate" name="iworkSchCalendar.reStartdate"  cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.reStartdate" format="yyyy-MM-dd"/></s:param> 
			            						</s:textfield>
		            							</label>
			            					</td>
			            					<td width="55%">
			            						<input type="radio" name="choseEndDate" id="choseEndDate0" onclick="selectEndDate(this)" value="0"/>
		            							<label>结束日期:
				            					<s:textfield id="reEnddate" disabled="disabled" name="iworkSchCalendar.reEnddate"  cssStyle="width:100px;" theme="simple">
		            								<s:param name="value"><s:date name="iworkSchCalendar.reEnddate"  format="yyyy-MM-dd"/></s:param>
				            					</s:textfield>
			            						</label>
			            					</td>
		            					</tr>
		            					<tr>
		            						<td width="45%"></td>
		            						<td width="55%">
		            							<input type="radio" disabled="disabled" name="choseEndDate" id="choseEndDate1" onclick="selectEndDate(this)" value="1"/>&nbsp;永不过期
		            						</td>
		            					</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		
	            	</table>
	                <s:hidden name="iworkSchCalendar.reWeekDate"/>
	           
	          	 <s:hidden name="iworkSchCalendar.id"/>
	     </div>
		            						</td>
		            					</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		<tr>
	           				<td colspan="2">
	           				<fieldset style="display: none;">
		           				<legend id="timeTitle"><font color="808080" >时间设置</font></legend>
		            				<table width="100%">
		            					<tr>
		            						<td>
			            						<input type="radio" disabled="disabled" name="iworkSchCalendar.isallday" id="editForm_iworkSchCalendar_isallday0" value="0">
			            						<label>开始时间:
			            						<s:select id="starttime1" name="iworkSchCalendar.starttime" list="#request.timeMap" cssStyle="width:100px;" headerKey="" headerValue=""></s:select>
		            							</label>
			            						<label>结束时间:
				            					<s:select id="endtime1" name="iworkSchCalendar.endtime" list="#request.timeMap" cssStyle="width:100px;" headerKey="" headerValue=""></s:select>
			            						</label>
		            						</td>
		            					</tr>
		            					<tr>
		            						<td>
		            							<input type="radio"  disabled="disabled" name="iworkSchCalendar.isallday" id="editForm_iworkSchCalendar_isallday1" value="1">&nbsp;全天
		            						</td>
		            					</tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            		<tr>
	            			<td colspan="2">
	           				<fieldset  style="display: none;">
		           				<legend id="alertTitle"><font color="808080" >提醒时间</font></legend>
		            				<table width="100%">
		            					<tr>
			            					<td width="46%">
			            						提前:&nbsp;<s:select disabled="disabled" id="alerttime1" name="iworkSchCalendar.alerttime" list="#{'0':'0分钟','5':'5分钟','10':'10分钟','15':'15分钟','30':'30分钟','45':'45分钟','60':'1小时','120':'2小时','180':'3小时','360':'6小时','720':'12小时','1080':'18小时','1440':'1天','2880':'2天','10080':'1周'}" cssStyle="width:100px;" headerKey="15" headerValue="15分钟"></s:select>
			            					</td>
			            					<td width="27%">
			            						<input type="checkbox" disabled="disabled" name="iworkSchCalendar.isalert" id="editForm_iworkSchCalendar_isalert1" onclick="setCheck(this)" value="1" checked="checked">提醒
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
	           				<fieldset >
		           				<legend id="remarkTitle"><font color="808080" >备注</font></legend>
		            				<table width="100%">
		            					<tr><td><s:textarea disabled="disabled" id="remark1" name="iworkSchCalendar.remark" cssStyle="width:360px;height:60px;"/></td></tr>
		           					</table>
		           				</fieldset>
	           				</td>
	            		</tr>
	            	<s:if test="null == iworkSchCalendar.id">
	            		<tr>
	            			<td class="td_title" width="17%">
	            			</td>
	            			
	            		</tr>
	            	</s:if>
	            	<s:else>
	            		<tr>
	            			<td class="td_title" width="17%">
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