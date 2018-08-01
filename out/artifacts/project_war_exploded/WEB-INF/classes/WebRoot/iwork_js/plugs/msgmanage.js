function mu(){
	var t = document.getElementsByName("chk");
	for(var i=0;i<t.length;i++){   
	if(t[i].checked){
	document.forms[0].tvalue.value+=t[i].value+" ";	
		}
	}
	if(document.forms[0].tvalue.value==""||document.forms[0].tvalue.value==null){
	alert("请选择要修改的短信!");
	return false;
	}
	var upchane=document.getElementsByName("updatechanel");
	var upchanev=upchane[0].value;
	var upstatus=document.getElementsByName("updatestatus");
	var upstatusv=upstatus[0].value;
	if(upchanev==""&&upstatusv==""){
	  alert("请选择要修改的状态或通道!");
	  return false;
	}
	var url='updatemsg.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
           
            document.forms[0].submit(); 
            

}
function query2(currentPage1,pagerMethod1){
	
            var url='msgmanage1.action?pagerMethod='+pagerMethod1+'&currentPage='+currentPage1;
         
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data"             
            document.forms[0].submit();
           
}