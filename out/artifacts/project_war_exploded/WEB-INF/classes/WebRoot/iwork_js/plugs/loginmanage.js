function Query(){
var batchnump=document.getElementsByName("batchnum");
	if(batchnump[0].value!=""){
		var partn=/^[a-zA-Z0-9-]{0,30}$/;
		if(!partn.exec(batchnump[0].value)){
			alert("批号里只有小于30位的数字,字母和'-'！");
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
		    var url='msgmanage.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
            document.forms[0].target="MSG_PHONEBOOK_FRAME";
            document.forms[0].submit();
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
 function SetWinHeight(obj)
{
	var win=obj;
	if(document.getElementById)
	{
		if(win && !window.opera)
	  {
	  	if(win.contentDocument && win.contentDocument.body.offsetHeight) 
	    {
	    	win.width = "98%";
	    	//alert('win.width='+win.width);
	    	//alert('win.contentDocument.body.offsetWidth='+win.contentDocument.body.offsetWidth);
	      win.height = win.contentDocument.body.offsetHeight + 105; 
	      //win.width = win.contentDocument.body.offsetWidth;
	    }
	    else if(win.Document && win.Document.body.scrollHeight)
	    {
	    	win.width = "98%";
	    	//alert('win.width='+win.width);
	    	//alert('win.Document.body.scrollWidth='+win.Document.body.scrollWidth);
	      win.height = win.Document.body.scrollHeight + 105;
	     // win.width = win.Document.body.scrollWidth;
	    }
	  }
	}
}