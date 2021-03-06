<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_js/fullcalendar/fullcalendar/fullcalendar.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/fullcalendar/fullcalendar/fullcalendar.print.css" media='print' />
<link rel="stylesheet" type="text/css" href="iwork_js/fullcalendar/theme/theme.css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.6.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery-ui-1.8.16.custom.js"></script>
<script type="text/javascript" src="iwork_js/fullcalendar/fullcalendar/fullcalendar.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=blue"></script>
<link  type="text/css"  rel="stylesheet" href="iwork_css/plugs/schcalendaraction.css" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#editForm").hide();//隐藏表格
		var date = '${currentTime}';//设定日历当前日期
		dateArr = date.split("-");
		var d = dateArr[2];
		var m = dateArr[1]-1;
		var y = dateArr[0];
		
		var calendar = $('#calendar').fullCalendar({
			theme:false,
			height: 520,
			defaultView:'month',
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"], 
			dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
			today: ["今天"],
			buttonText: {
				today: "今天",
				ohter: "其他",
				month: "月视图",
			    week: "周视图",
				day: "日视图"				
			},
			firstHour:9,
			selectable: true,
			selectHelper: true,
			select: function(start, end, allDay) {//选择日期范围添加日程
				var isWriter = $("#isWriter").val();
				if(isWriter!=1){
					$.dialog.tips('权限不足',2);
					return;
				}
				var startArr = start.format('yyyy-MM-dd hh:mm').split(" ");
				var endArr = end.format('yyyy-MM-dd hh:mm').split(" ");
				var dg = $.dialog({ 
                    id:'dg_select',  
                    title:'创建日程',
                    resize : false,
                    iconTitle:false, 
                    content:"url:addSchCalendar.action?visitor=<s:property value="visitor"/>&startDate="+startArr[0]+"&startTime="+startArr[1]+"&endDate="+endArr[0]+"&endTime="+endArr[1]+"&allDay="+allDay,
                    max:false
				});
				calendar.fullCalendar('unselect');
			},
			eventClick:function(calEvent, jsEvent, view){//点击日程
			var isWriter = $("#isWriter").val();
			if(isWriter!=1){
				$.dialog.tips('权限不足',2);
				return;
			}
				var startArr = calEvent.start.format('yyyy-MM-dd hh:mm').split(" ");
				var endArr = new Array();
				if(null!=calEvent.end&&""!=calEvent.end){
					endArr = calEvent.end.format('yyyy-MM-dd hh:mm').split(" ");
				}else{
					endArr = startArr;
				}
				var isAllDay = 0;
				if(calEvent.allDay){
					isAllDay = 1;
				}else{
					isAllDay = 0;
				}
				
				var obj = new Object();
				obj.editForm_iworkSchCalendar_id = calEvent.id;
				obj.editForm_iworkSchCalendar_isallday = calEvent.isAllDay;
				obj.editForm_iworkSchCalendar_userid = calEvent.userid;
				obj.editForm_iworkSchCalendar_title = calEvent.title;
				obj.editForm_iworkSchCalendar_startdate = startArr[0];
				obj.editForm_iworkSchCalendar_starttime = startArr[1];
				obj.editForm_iworkSchCalendar_enddate = endArr[0];
				obj.editForm_iworkSchCalendar_endtime = endArr[1];
				obj.editForm_iworkSchCalendar_isalert = calEvent.isalert;
				obj.editForm_iworkSchCalendar_alerttime = calEvent.alerttime;
				obj.editForm_iworkSchCalendar_issharing = calEvent.issharing;
				obj.editForm_iworkSchCalendar_remark = calEvent.remark;
				obj.editForm_iworkSchCalendar_reStartdate = calEvent.reStartdate ;
				obj.editForm_iworkSchCalendar_reEnddate=calEvent.reEnddate ;
				obj.editForm_iworkSchCalendar_reStarttime=calEvent.reStarttime ;
				obj.editForm_iworkSchCalendar_reEndtime=calEvent.reEndtime ;
				obj.editForm_iworkSchCalendar_reMode=calEvent.reMode ;
				obj.editForm_iworkSchCalendar_reDayInterval=calEvent.reDayInterval ;
				obj.editForm_iworkSchCalendar_reWeekDate=calEvent.reWeekDate ;
				obj.editForm_iworkSchCalendar_reMonthDays=calEvent.reMonthDays ;
				obj.editForm_iworkSchCalendar_reYearMonth=calEvent.reYearMonth ;
				obj.editForm_iworkSchCalendar_reYearDays=calEvent.reYearDays ;
				
				var dg = $.dialog({
					id:'dg_eventClick',
					title:'编辑日程',
					resize : false,
					iconTitle:false,
					content:"url:editSchCalendar.action",
					max:false,
					data:obj
				});
			},
			eventMouseover:function(event, jsEvent, view ) {
		    	$(this).css('border-color','white');
		    	$(this).attr("title",event.start.format('yyyy-MM-dd hh:mm')+event.title);
		    },
		    
		    eventMouseout:function(event, jsEvent, view ) {
		    	$(this).css('border-color','blue');
		    },
			eventDrop:function(calEvent, dayDelta, minuteDelta, allDay, revertFunc,jsEvent, ui, view) {//拖动日程
			var isWriter = $("#isWriter").val();
			if(isWriter!=1){
				$.dialog.tips('权限不足',2);
				return;
			}
				var startArr = calEvent.start.format('yyyy-MM-dd hh:mm').split(" ");
				var endArr = new Array();
				if(null!=calEvent.end&&""!=calEvent.end){
					endArr = calEvent.end.format('yyyy-MM-dd hh:mm').split(" ");
				}else{
					endArr = startArr;
				}
				var tip = "";
				if(Math.abs(dayDelta)!=0){tip =tip+Math.abs(dayDelta)+"天";}
				if(Math.floor(Math.abs(minuteDelta)/60)!=0){tip=tip+Math.floor(Math.abs(minuteDelta)/60)+"小时"}
				if(Math.floor(Math.abs(minuteDelta)%60)!=0){tip=tip+Math.floor(Math.abs(minuteDelta)%60)+"分钟"}
				var msg = "您移动了"+tip+",确定修改吗?";
		  		if (confirm(msg) == true) {
		  			$("#editForm_iworkSchCalendar_id").val(calEvent.id);
			        $("#editForm_iworkSchCalendar_userid").val(calEvent.userid);
			        $("#editForm_iworkSchCalendar_title").val(calEvent.title);
			        $("#editForm_iworkSchCalendar_startdate").val(startArr[0]);
			        $("#editForm_iworkSchCalendar_enddate").val(endArr[0]);
			        $("#editForm_iworkSchCalendar_starttime").val(startArr[1]);
			        $("#editForm_iworkSchCalendar_endtime").val(endArr[1]);
			        $("#editForm_iworkSchCalendar_isallday").val(calEvent.isallday);
			        $("#editForm_iworkSchCalendar_isalert").val(calEvent.isalert);
			        $("#editForm_iworkSchCalendar_alerttime").val(calEvent.alerttime);
			        $("#editForm_iworkSchCalendar_issharing").val(calEvent.issharing);
			        $("#editForm_iworkSchCalendar_remark").val(calEvent.remark);
			        var options = {
						error:revertFunc,
						success:showResponse 
					};
        			$("#editForm").ajaxSubmit(options);
		  		} else {
		   			revertFunc();
		  		}
			},
			eventResize:function( calEvent, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ) {//改变日程日期范围
				var isWriter = $("#isWriter").val();
				if(isWriter!=1){
					$.dialog.tips('权限不足',2);
					return;
				}
				var startArr = calEvent.start.format('yyyy-MM-dd hh:mm').split(" ");
				var endArr = new Array();
				if(null!=calEvent.end&&""!=calEvent.end){
					endArr = calEvent.end.format('yyyy-MM-dd hh:mm').split(" ");
				}else{
					endArr = startArr;
				}
				var tip = "";
				if(Math.abs(dayDelta)!=0){tip =tip+Math.abs(dayDelta)+"天";}
				if(Math.floor(Math.abs(minuteDelta)/60)!=0){tip=tip+Math.floor(Math.abs(minuteDelta)/60)+"小时"}
				if(Math.floor(Math.abs(minuteDelta)%60)!=0){tip=tip+Math.floor(Math.abs(minuteDelta)%60)+"分钟"}
				var msg = "您移动了"+tip+",确定修改吗?";
		  		if (confirm(msg) == true) {
		  			$("#editForm_iworkSchCalendar_id").val(calEvent.id);
			        $("#editForm_iworkSchCalendar_userid").val(calEvent.userid);
			        $("#editForm_iworkSchCalendar_title").val(calEvent.title);
			        $("#editForm_iworkSchCalendar_startdate").val(startArr[0]);
			        $("#editForm_iworkSchCalendar_enddate").val(endArr[0]);
			        $("#editForm_iworkSchCalendar_starttime").val(startArr[1]);
			        $("#editForm_iworkSchCalendar_endtime").val(endArr[1]);
			        $("#editForm_iworkSchCalendar_isallday").val(calEvent.isallday);
			        $("#editForm_iworkSchCalendar_isalert").val(calEvent.isalert);
			        $("#editForm_iworkSchCalendar_alerttime").val(calEvent.alerttime);
			        $("#editForm_iworkSchCalendar_issharing").val(calEvent.issharing);
			        $("#editForm_iworkSchCalendar_remark").val(calEvent.remark);
			        var options = {
						error:revertFunc,
						success:showResponse 
					};
        			$("#editForm").ajaxSubmit(options);
		  		} else {
		   			revertFunc();
		  		}
			},
			editable: true,
			eventSources: [//日程源
        		{
            		url: 'schShowJason.action?visitor=<s:property value="visitor"/>',
            		type: 'POST',
            		error: function() {
                	alert('加载时发生错误,请联系管理员');
            		}
        		},
        		{
            		url: 'schShowJason_Repeate.action?visitor=<s:property value="visitor"/>',
            		type: 'POST',
            		error: function() {
                	alert('加载周期日程时发生错误,请联系管理员');
            		}
        		}
    		]
		});
	});
	
	showResponse = function(){
		var a = $.dialog({
			id: "eventDrop",
			title: "系统消息",
			content: "日程修改成功!",
			icon:"succeed.png",
			time:1	
		});	
	}
	
	//格式化时间的方法
	Date.prototype.format = function(format)
    {
        var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(),    //day
        "h+" : this.getHours(),   //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
        "S" : this.getMilliseconds() //millisecond
        }
        if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
        (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)if(new RegExp("("+ k +")").test(format))
        format = format.replace(RegExp.$1,
        RegExp.$1.length==1 ? o[k] :
        ("00"+ o[k]).substr((""+ o[k]).length));
        return format;
    }
    //重新抓取日程
    function refetch(){
		$('#calendar').fullCalendar('refetchEvents');
	}
	//弹出增加周期模式选取框
	function popAddRepeateMode(start,end){
		var isWriter = $("#isWriter").val();
		if(isWriter!=1){
			$.dialog.tips('权限不足',2);
			return;
		}
			var a = $.dialog({
			id:'popRepeateMode',
			title:'编辑日程周期',
			resize : false,
			iconTitle:false,
			content:"url:addRepeateMode.action?visitor=<s:property value="visitor"/>&startDate="+start+"&endDate="+end,
			left:'70%',
			max:false
		});
	}
	//弹出更改周期模式选取框
	function popEditRepeateMode(id){
		var isWriter = $("#isWriter").val();
		if(isWriter!=1){
			$.dialog.tips('权限不足',2);
			return;
		}
			var a = $.dialog({
			id:'popRepeateMode',
			title:'编辑日程周期',
			resize : false,
			iconTitle:false,
			content:"url:editRepeateMode.action?visitor=<s:property value="visitor"/>&id="+id,
			left:'70%',
			max:false
			});
	}
	//关闭模式周期选取框
	function closeRepeateMode(){
		lhgdialog.list["popRepeateMode"].close();
	}
	//将循环模式页面的信息传给主页再传给高级选项页面
	function getRepeateMode(reStartdate,reEnddate,reMode,reDayInterval,reWeekDate,reMonthDays,reYearMonth,reYearDays){
		$("#editForm_iworkSchCalendar_reStartdate").val(reStartdate);
		$("#editForm_iworkSchCalendar_reEnddate").val(reEnddate);
		$("#editForm_iworkSchCalendar_reMode").val(reMode);
		$("#editForm_iworkSchCalendar_reDayInterval").val(reDayInterval);
		$("#editForm_iworkSchCalendar_reWeekDate").val(reWeekDate);
		$("#editForm_iworkSchCalendar_reMonthDays").val(reMonthDays);
		$("#editForm_iworkSchCalendar_reYearMonth").val(reYearMonth);
		$("#editForm_iworkSchCalendar_reYearDays").val(reYearDays);
		if(typeof(lhgdialog.list['dg_eventClick'])=="object"){//判断编辑窗口是否打开
			lhgdialog.list["dg_eventClick"].get("dg_eventClick","window").setRepeateMode();
		}
		if(typeof(lhgdialog.list['dg_select'])=="object"){//判断新建窗口是否打开
			lhgdialog.list["dg_select"].get("dg_select","window").setRepeateMode();
		}
	}
	
	function showSetting(){
		var a = $.dialog({
			id:'CalendarSettingMode',
			title:'日程设置',
			cover:true, 
			bgcolor:'#999',
			lock: true,
			esc: true,
			resize : false,
			iconTitle:false,
			top:10,
			width:450,
			height:300,
			content:"url:schCalendarSettingIndex.action",
			max:false
		});
	
	}
	function showMyView(){
		this.location.href="schCalendarAction.action";
	}
</script>
<style type="text/css">
	.toolbar{
		text-align:left;
		padding:5px;
		border-bottom:1px solid #efefef;
		margin:5px;
		font-size:18px;
	}
	.btn{
		padding:5px;
		padding-left:20px;
		border:1px solid #fff;
		text-decoration:none;
		color:#666;
		font-size:12px;
	}
	.otherBtnIcon{
		background:#FFF url(iwork_img/calendar.gif) no-repeat scroll 0px 3px;
	}
	.settingBtnIcon{
		background:#FFF url(iwork_img/cog1.png) no-repeat scroll 2px 3px;
	}
	.btn:hover{
		border:1px solid #ccc;
		background-color:#efefef;
	}
	
</style>
</head>
<body>
<div class="toolbar">
	<s:property value="title" escapeHtml="false"/>
	<span style="float:right">
	<a href="javascript:showMyView()" class="btn otherBtnIcon">返回我的日程</a>
	</span>
</div>
<div id='calendar'></div>
<s:form id ="editForm" name="editForm" action="saveSchCalendar"  theme="simple">
	id:<s:textfield name="iworkSchCalendar.id"></s:textfield>
	userid:<s:textfield name="iworkSchCalendar.userid"></s:textfield>
	title:<s:textfield name="iworkSchCalendar.title"></s:textfield>
	startdate:<s:textfield name="iworkSchCalendar.startdate"></s:textfield>
	enddate:<s:textfield name="iworkSchCalendar.enddate"></s:textfield>
	starttime:<s:textfield name="iworkSchCalendar.starttime"></s:textfield>
	endtime:<s:textfield name="iworkSchCalendar.endtime"></s:textfield>
	isallday:<s:textfield name="iworkSchCalendar.isallday"></s:textfield>
	isalert:<s:textfield name="iworkSchCalendar.isalert"></s:textfield>
	alerttime:<s:textfield name="iworkSchCalendar.alerttime"></s:textfield>
	issharing:<s:textfield name="iworkSchCalendar.issharing"></s:textfield>
	remark:<s:textfield name="iworkSchCalendar.remark"></s:textfield>
	
	<s:textfield name="editForm_iworkSchCalendar_reStartdate" id="editForm_iworkSchCalendar_reStartdate"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reEnddate" id="editForm_iworkSchCalendar_reEnddate"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reMode" id="editForm_iworkSchCalendar_reMode"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reDayInterval" id="editForm_iworkSchCalendar_reDayInterval"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reWeekDate" id="editForm_iworkSchCalendar_reWeekDate"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reMonthDays" id="editForm_iworkSchCalendar_reMonthDays"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reYearMonth" id="editForm_iworkSchCalendar_reYearMonth"></s:textfield>
	<s:textfield name="editForm_iworkSchCalendar_reYearDays" id="editForm_iworkSchCalendar_reYearDays"></s:textfield>
	
	<s:hidden name="visitor" id="visitor"></s:hidden>
	<s:hidden name="isShare" id="isShare"></s:hidden>
	<s:hidden name="isWriter" id="isWriter"></s:hidden> 
</s:form>
</body>
</html>
