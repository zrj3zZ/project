function phonebookQuery(phone,content){

	if(document.getElementById("name").value!=""&&document.getElementById("name").value.length>10){
	alert("姓名长度过长");
	return false;
	}
	if(document.getElementById("mobilenum").value!=""&&document.getElementById("mobilenum").value.length>11){
	alert("手机号码长度过长");
	return false;
	}
	if(document.getElementById("extend1").value!=""&&document.getElementById("extend1").value.length>20){
	alert("属性一长度过长");
	return false;
	}
	if(document.getElementById("extend2").value!=""&&document.getElementById("extend2").value.length>20){
	alert("属性二长度过长");
	return false;
	}
	if(document.getElementById("extend3").value!=""&&document.getElementById("extend3").value.length>20){
	alert("属性三长度过长");
	return false;
	}
	        content=content.replace(/\%/g,'%25');
	        var url='qselectnum.action?phone='+phone+'&content='+content;	       
            document.forms[0].action=url;  
            document.forms[0].method="post";  
           // document.forms[0].enctype="multipart/form-data"  
            document.forms[0].target="MSG_PHONEBOOK_FRAME";                    
            document.forms[0].submit();
            return false;

}
/*function phonebookback(){
history.go(-1);
return false;
}*/
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