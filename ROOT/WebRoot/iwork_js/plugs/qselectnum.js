function phonebookAddnum(phone,content){
	
	var t = document.getElementsByName("chk");
	for(var i=0;i<t.length;i++){  
	if(t[i].checked){
	document.forms[0].tvalue.value+=t[i].value+",";	
		}
	}
	if(document.forms[0].tvalue.value==""||document.forms[0].tvalue.value==null){
	alert("请选择号码");
	return false;
	}
	//parent.opener.document.forms[0].phonebook_nums.value = document.forms[0].tvalue.value;
	content=content.replace(/\%/g,'%25');
    var url="selectednum.action?phone1="+phone+"&content1="+content;
    document.forms[0].action=url;  
    document.forms[0].method="post";  
    //document.forms[0].enctype="multipart/form-data" 
    document.forms[0].submit();
      //window.parent.opener.location="loginmsg.action?phone='"+<s:property value="model.bphone"/>+"'&content='"+<s:property value="model.bcontent"/>+"'";  
	 // parent.window.close();
	// parent.opener.document.forms[0].phone.focus();
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
var checkflag = "false";
function check(field) {
if (checkflag == "false") {
for (i = 0; i < field.length; i++) {
field[i].checked = true;}
checkflag = "true";
return "false"; }
else {
for (i = 0; i < field.length; i++) {
field[i].checked = false; }
checkflag = "false";
return "true"; }
}