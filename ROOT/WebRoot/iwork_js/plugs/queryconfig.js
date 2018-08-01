$('#hiddiv').dialog({
			    title:"保存设置"
			});
$('#hiddiv').dialog('close');

function dataEditc(cid){

	$.ajax({
		type: 'POST',
		url: 'dataeditc.action',
		data: {'id':cid},
		dataType: 'text',
		success: function(data,status){
		 $('#hiddiv').html(data);
		$('#hiddiv').dialog('open');
		}
	});
}


function datasave(cid){

var keychange = $('#keychange').val();
var valuechange=$('#valuechange').val();

        if(keychange==""){
		alert("参数ID不能为空!");
		return false;
		}
		if(valuechange==""){
		alert("参数值不能为空!");
		return false;
		}
		var reg=/^\d+$/;
       if(keychange!=""&&!reg.test(keychange)){
			alert('参数ID只能是整数!');       
			return false;
       }
		if(keychange.length>25){
		alert("参数ID长度过长!最大长度是25，目前长度是"+keychange.length+"。");
		return false;
		}
		if(valuechange.length>25){
		alert("参数值长度过长!最大长度是25，目前长度是"+valuechange.length+"。");
		return false;
		}

  $.ajax({
		type: 'POST',
		url: 'saveeditnumc.action',
		data: {'cid':cid,
		       'keychange':keychange,
		       'valuechange':valuechange       
		 },
		dataType: 'text',
		success: function(data,status){
		 $('#hiddiv').html(data); 
		 $('#hiddiv').dialog('close');
		}
	});
	
}
function onload(){
var retv=document.getElementsByName("returnvalue");
var retvv=retv[0].value;
if(retvv!=""){
	alert(retvv);
}
}
function dataEdit(form,mycmd,myid){
        form.cmd.value=mycmd;
		form.id.value=myid;
		form.target="AWS_AJAX_OPTER";
		form.submit();
		return false;
}
function savedata(form,mycmd,myid){
        form.cmd.value=mycmd;
		form.id.value=myid;
 		form.target="_self";
 		disableAll(form);
		if(form.keychange.value==""){
		alert("参数ID不能为空!");
		return false;
		}
		if(form.valuechange.value==""){
		alert("参数值不能为空!");
		return false;
		}
		var reg=/^\d+$/;
       if(form.keychange.value!=""&&!reg.test(form.keychange.value)){
			alert('参数ID只能是整数!');       
			return false;
       }
		if(form.keychange.value.length>25){
		alert("参数ID长度过长!最大长度是25，目前长度是"+form.keychange.value.length+"。");
		return false;
		}
		if(form.valuechange.value.length>25){
		alert("参数值长度过长!最大长度是25，目前长度是"+form.valuechange.value.length+"。");
		return false;
		}
 		form.submit();
 		return false; 	
}
function deleteItem(form,mycmd,myid){
		if ( false==confirm("确认要删除当前的条目吗？") ){
    		return false;
    	}
		form.id.value=myid;
		form.cmd.value=mycmd;
 		form.target="_self";
 		disableAll(form);
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