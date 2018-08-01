<!--var abc=document.getElementById("MSG_PHONEBOOK_FRAME").src;-->
function onload(){
var retv=document.getElementsByName("returnvalue2");
var retvv=retv[0].value;
if(retvv!=""){
	alert(retvv);
}
}

function selectchange(selectvalue){
			
            document.forms[0].selectvalue.value=selectvalue;
            
            var url='queryconfig.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
            document.forms[0].target="MSG_PHONEBOOK_FRAME";           
            document.forms[0].submit();
            return false;
}
function add(){
	var parid=document.getElementsByName("parameterid");
	if(parid[0].value==""){
		alert("参数ID不能为空!");
		return false;
	}
	var par=document.getElementsByName("parameter");
	if(par[0].value==""){
		alert("参数值不能为空!");
		return false;
	}
	
	var reg=/^\d+$/;
       if(parid[0].value!=""&&!reg.test(parid[0].value)){
			alert("参数ID只能是整数!");       
			return false;
       }
	if(parid[0].value.length>25){
		alert("参数ID长度过长!最大长度是25，目前长度是"+parid[0].value.length+"。");
		return false;
		}
		if(par[0].value.length>25){
		alert("参数值长度过长!最大长度是25，目前长度是"+par[0].value.length+"。");
		return false;
		}
 	        var url='addconfig.action'
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