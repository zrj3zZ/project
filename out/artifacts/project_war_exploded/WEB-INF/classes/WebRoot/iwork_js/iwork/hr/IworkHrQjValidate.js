	function sentMeg(){
		var QJQSRQ =  $("#QJQSRQ").val();
		var QJJSRQ =  $("#QJJSRQ").val();
		var QSXS =  $("#QSXS").val();
		var JSXS =  $("#JSXS").val();
		var GJXSS ;
		var diffDay = DateDiff(QJQSRQ,QJJSRQ);
		var diffHour = hourDiff(QSXS,JSXS);
		GJXSS = diffDay * 8 + diffHour;
		document.getElementById('GJXSS').value= GJXSS;
	
	}
	//两日期之差

	function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式    
		var  aDate,  oDate1,  oDate2,  iDays;    
	       aDate  =  sDate1.split("-");  
	       oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);    //转换为12-18-2006格式    
	       aDate = sDate2.split("-");
	       oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);    
	       iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);    //把相差的毫秒数转换为天数   
	       return  iDays + 1;   
	   }
	//两时间之差
	function hourDiff(sHour,eHour){
		var diff ,
		diff = parseInt(Math.abs(sHour - eHour));
		return diff;
	}
	
	//销假小时数差
	function hoursDiffer(){
		//未休假小时数
		var noHisHour = document.getElementById('WXJXSS').value;
		//原始请假小时数
		var beforeHour = document.getElementById('YQJXSS').value;
		//已休假小时数
		var hasHisHour = beforeHour - noHisHour;
		if(hasHisHour<0){
			hasHisHour = 0;
		}
		document.getElementById('XJGJXSS').value = hasHisHour;
		
	}



