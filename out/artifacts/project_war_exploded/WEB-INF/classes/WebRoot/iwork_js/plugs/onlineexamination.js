function toDate(str){
	    var sd=str.split("-");
	    return new Date(sd[0],sd[1],sd[2]);
	}
	function validate(){
	var beginDate=$("#BEGINDATE").val();
	var endDate=$("#ENDDATE").val();
	var d1=toDate(beginDate);
	var d2=toDate(endDate);
	if(d1>d2){
	alert("开始时间不可以大于结束时间");
	var endDate=$("#ENDDATE").val(null);
	}
	}
	