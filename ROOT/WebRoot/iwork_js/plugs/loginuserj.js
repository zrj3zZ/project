function userQuery(){
	var startdate=$('#startdate').datebox("getValue");
	var enddate=$('#enddate').datebox("getValue");
	
     var oprator=document.getElementById("sender");
	 document.forms[0].hoprator.value=oprator.value;
	 document.forms[0].hstartd.value=startdate;
	 
	 document.forms[0].hendd.value=enddate;
            var url='loginuserj.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data"             
            document.forms[0].submit();
            return false;
}
//设置
function dataEdit(cid){
		   document.forms[0].hcid.value=cid;
				$('#typeedit').window('open');
            }
		