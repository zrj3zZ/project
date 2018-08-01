function getType() {
	var objs = document.getElementsByTagName("input"); 
	var ret = '';
	for(var i=0; i<objs.length; i++)   
	{
		if(objs[i].type=="checkbox" && objs[i].checked) 
		{ 
			ret = ret + ' ' + objs[i].value;
			
		} 
	}

	return ret;
}
function querylog(){
var startdate=$('#startDate').datebox("getValue");
	var enddate=$('#endDate').datebox("getValue");
	var starthour=document.getElementsByName("startHour");
	var endhour=document.getElementsByName("endHour");
	var startmin=document.getElementsByName("startMin");
	var endmin=document.getElementsByName("endMin");
	if(startdate!=""&&enddate==""){
		alert("请填写时间段!");
		return false;
	}
	
	if(startdate>enddate){
		alert("请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value>endhour[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value==endhour[0].value&&startmin[0].value>endmin[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	var content=document.getElementById("keywords");
	var oprator=document.getElementById("sender");
	document.forms[0].checktype.value = getType();
	 document.forms[0].hvalue.value=content.value;
	 document.forms[0].hoprator.value=oprator.value;
	 document.forms[0].hstartd.value=startdate;
	 document.forms[0].hstarth.value=starthour[0].value;
	 document.forms[0].hstartm.value=startmin[0].value;
	 document.forms[0].hendd.value=enddate;
	 document.forms[0].hendh.value=endhour[0].value;
	 document.forms[0].hendm.value=endmin[0].value;
            var url='loginj.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data"             
            document.forms[0].submit();
            return false;
}

function querylog2(currentPage1,pagerMethod1){
	var typec=document.getElementById("typepage");
	var startdate=$('#startDate').datebox("getValue");
	var enddate=$('#endDate').datebox("getValue");
	var starthour=document.getElementsByName("startHour");
	var endhour=document.getElementsByName("endHour");
	var startmin=document.getElementsByName("startMin");
	var endmin=document.getElementsByName("endMin");
	if(startdate!=""&&enddate==""){
		alert("请填写时间段!");
		return false;
	}
	
	if(startdate>enddate){
		alert("请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value>endhour[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	if(startdate!=""&&startdate==enddate&&starthour[0].value==endhour[0].value&&startmin[0].value>endmin[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	var content=document.getElementById("keywords");
	var oprator=document.getElementById("sender");
	 document.forms[0].checktype.value = typec.value;
	 document.forms[0].hvalue.value=content.value;
	 document.forms[0].hoprator.value=oprator.value;
	 document.forms[0].hstartd.value=startdate;
	 document.forms[0].hstarth.value=starthour[0].value;
	 document.forms[0].hstartm.value=startmin[0].value;
	 document.forms[0].hendd.value=enddate;
	 document.forms[0].hendh.value=endhour[0].value;
	 document.forms[0].hendm.value=endmin[0].value;
            var url='loginj.action?pagerMethod='+pagerMethod1+'&currentPage='+currentPage1;
         
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data"             
            document.forms[0].submit();
           
}
function query(form,mycmd){
	if(form.startDate.value!="" && form.endDate.value==""){
		alert("请填写时间段！");
		return false;
	}
	if(form.startDate.value>form.endDate.value){
		alert("请输入正确的时间段");
		return false;
	}
	if(form.startDate.value!=""&&form.startDate.value==form.endDate.value&&form.startHour.value>form.endHour.value){
		alert("请输入正确的时间段");
		return false;
	}
	if(form.startDate.value!=""&&form.startDate.value==form.endDate.value&&form.startHour.value==form.endHour.value&&form.startMin.value>form.endMin.value){
		alert("请输入正确的时间段");
		return false;
	}
	form.cmd.value=mycmd;
	frmMain.target="MSG_PHONEBOOK_FRAME";
	document.getElementById('checktype').value = getType();
 	form.submit();
 	return false;
}
function gotoPage(form,mycmd,pageNow){ 
 	form.pageNow.value=pageNow;
 	form.target='_self';
 	return execMyCommand2(frmMain,mycmd,'_self');
 }
 
function execMyCommand2(form,mycmd,targetName){
	form.cmd.value=mycmd;
	form.target=targetName;

	form.submit(); 
	return false;
}