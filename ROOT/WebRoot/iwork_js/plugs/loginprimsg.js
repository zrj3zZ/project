function privateMSG(){
	var batchnump=document.getElementsByName("batchnum");
	if(batchnump[0].value!=""){
		var partn=/^[a-zA-Z0-9-]{0,30}$/;
		if(!partn.exec(batchnump[0].value)){
			$.messager.alert("警告","批号里只有小于30位的数字,字母和'-'！");
			return false;
		}
	}
	var startdate=$('#startDate').datebox("getValue");
	var enddate=$('#endDate').datebox("getValue");
	var starthour=document.getElementsByName("startHour");
	var endhour=document.getElementsByName("endHour");
	var startmin=document.getElementsByName("startMin");
	var endmin=document.getElementsByName("endMin");
	if(startdate!=""&&enddate==""){
		$.messager.alert("警告","请填写时间段!");
		return false;
	}
	
	if(startdate>enddate){
		$.messager.alert("警告","请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value>endhour[0].value){
		$.messager.alert("警告","请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value==endhour[0].value&&startmin[0].value>endmin[0].value){
		$.messager.alert("警告","请输入正确的时间段!");
		return false;
	}
	
	var mobile=document.getElementById("mobilenum");
	var content=document.getElementById("keywords");
	var status=document.getElementById("status");
	 document.forms[0].hbatch.value=batchnump[0].value;
     document.forms[0].hmobile.value=mobile.value;
	 document.forms[0].hcontent.value=content.value;
	 document.forms[0].hstatus.value=status.value;
	 document.forms[0].hstartd.value=startdate;
	 document.forms[0].hstarth.value=starthour[0].value;
	 document.forms[0].hstartm.value=startmin[0].value;
	 document.forms[0].hendd.value=enddate;
	 document.forms[0].hendh.value=endhour[0].value;
	 document.forms[0].hendm.value=endmin[0].value;
	 var url='loadq.action'	 
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
            document.forms[0].submit();
            
}
function privateMSG1(currentPage1,pagerMethod1){
	var batchnump=document.getElementsByName("batchnum");
	if(batchnump[0].value!=""){
		var partn=/^[a-zA-Z0-9-]{0,30}$/;
		if(!partn.exec(batchnump[0].value)){
			$.messager.alert("警告","批号里只有小于30位的数字,字母和'-'！");
			return false;
		}
	}
	
	var startdate=$('#startDate').datebox("getValue");
	var enddate=$('#endDate').datebox("getValue");
	var starthour=document.getElementsByName("startHour");
	var endhour=document.getElementsByName("endHour");
	var startmin=document.getElementsByName("startMin");
	var endmin=document.getElementsByName("endMin");
	if(startdate!=""&&enddate==""){
		$.messager.alert("警告","请填写时间段!");
		return false;
	}
	
	if(startdate>enddate){
		$.messager.alert("警告","请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value>endhour[0].value){
		$.messager.alert("警告","请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value==endhour[0].value&&startmin[0].value>endmin[0].value){
		$.messager.alert("警告","请输入正确的时间段!");
		return false;
	}
	
	var mobile=document.getElementById("mobilenum");
	var content=document.getElementById("keywords");
	var status=document.getElementById("status");
	 document.forms[0].hbatch.value=batchnump[0].value;
     document.forms[0].hmobile.value=mobile.value;
	 document.forms[0].hcontent.value=content.value;
	 document.forms[0].hstatus.value=status.value;
	 document.forms[0].hstartd.value=startdate;
	 document.forms[0].hstarth.value=starthour[0].value;
	 document.forms[0].hstartm.value=startmin[0].value;
	 document.forms[0].hendd.value=enddate;
	 document.forms[0].hendh.value=endhour[0].value;
	 document.forms[0].hendm.value=endmin[0].value;
	 var url='loadq.action?pagerMethod='+pagerMethod1+'&currentPage='+currentPage1;
	 
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
            document.forms[0].submit();
            
}