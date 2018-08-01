function getWorkage(){
	
	var worktodate = new Date($("#WORKTODATE").val());// 参加工作日期
	var date1 = new Date(getDates());
	iDays = (Math.abs(date1 - worktodate))/1000/60/60/24;
	if(iDays<30){
		$("#labelWORKAGE").text(iDays+"天");
		$("#WORKAGE").val(iDays+"天");
	}else if(iDays>30&&iDays<=365){
		iDaysM = iDays/30;
		$("#labelWORKAGE").text(Math.floor(iDaysM)+"月");
		$("#WORKAGE").val(Math.floor(iDaysM)+"月");
	}else if(iDays>=365){
		iDaysY = iDays/365;
		$("#labelWORKAGE").text(Math.floor(iDaysY) +"年");
		$("#WORKAGE").val(Math.floor(iDaysY)+"年");
	}
}

function getSiling(){
	var hiredate = new Date($("#HIREDATE").val());// 入职日期
	var date1 = new Date(getDates());
	iDays = (Math.abs(date1 - hiredate))/1000/60/60/24;
	if(iDays<30){
		$("#labelSILING").text(iDays+"天");
		$("#SILING").val(iDays+"天");
	}else if(iDays>30&&iDays<=365){
		iDaysM = iDays/30;
		$("#labelSILING").text(Math.floor(iDaysM)+"月");
		$("#SILING").val(Math.floor(iDaysM)+"月");
	}else if(iDays>=365){
		iDaysY = iDays/365;
		$("#labelSILING").text(Math.floor(iDaysY) +"年");
		$("#SILING").val(Math.floor(iDaysY)+"年");
	}
}

function getDates(){
	var date = new Date();
	var datestr = date.getFullYear() +"-"+ (date.getMonth()+1) +"-"+ date.getDate() + " 08:00:00";
	var date1 = new Date(datestr);
	return date1.toString();
}

function getPositivedate(){
	var hiredate = new Date($("#HIREDATE").val());// 入职日期
	var positivedate = new Date($("#POSITIVEDATE").val());// 转正日期
	if(hiredate!=null&&positivedate!=null){
		iDays  = (Math.abs(positivedate - hiredate))/1000/60/60/24;
		if(iDays<30){
			$("#labelPROBATION").text(iDays+"天");
			$("#PROBATION").val(iDays+"天");
		}else if(iDays>30&&iDays<=365){
			iDaysM = iDays/30;
			$("#labelPROBATION").text(Math.floor(iDaysM)+"月");
			$("#PROBATION").val(Math.floor(iDaysM)+"月");
		}else if(iDays>=365){
			iDaysY = iDays/365;
			$("#labelPROBATION").text(Math.floor(iDaysY) +"年");
			$("#PROBATION").val(Math.floor(iDaysY)+"年");
		}
	}
	
}

function getQJGJXSS(){
	var qjqsrq = new Date($("#QJQSRQ").val());
	var qjjsrq = new Date($("#QJJSRQ").val());
	iDays  = (Math.abs(qjjsrq - qjqsrq))/1000/60/60/24;
	iDaysx = iDays*24;
	$("#labelQJGJXSS").text(Math.floor(iDaysx) +"小时");
	$("#QJGJXSS").val(Math.floor(iDaysx)+"小时");
}
/**
 * 获取出差申请的请假时间
 * @return
 */
function getDateextens(){
	var startdate = new Date($("#STARTDATE").val());
	var finishdate = new Date($("#FINISHDATE").val());
	iDays =  (Math.abs(finishdate - startdate))/1000/60/60/24;
	$("#labelDATEEXTENS").text(iDays+"天");
	$("#DATEEXTENS").val(iDays+"天");
}