function onload(){
	var value=document.getElementById("model_bv").value;
	if(value!=""&&value!=null){
	//$.messager.alert('系统提示',value,'info');
	alert(value);
	return false;
	}
}
  function sendmsg(){
	  var valid = mainFormValidator.form(); // 执行校验操作
	  if(!valid||!phoneflg||!contentflg){
		  return false;
	  }
	  var content11=document.getElementById("content").value;
	  content11=encodeURI(content11);
	  var phone11=document.getElementById("phone").value;
	  if(phone11.split(/[;,]/).length>50){
		  alert("接收号码过多!");
		  return false;
	  }
	  if(phone11==""){
		  alert("发送号码不能为空!");
		  return false;
	  }
	  if(content11==""){
		  alert("发送内容不能为空!");
		  return false;
	  }else{
		  var url = "messagesendbeforecontentcheck.action?content="+content11;
		  $.ajax({
			  type:"POST",
			  url:url,
			  success:function(msg){
				  if(msg!=""){
					  alert("发送内容包含敏感词,以下为敏感词汇:\n"+msg);
				  }else{
					  var url="messagesend.action";
					  $.post(url,{content:content11,phone:phone11},function(data){
						  var content = data.content;
						  var totalNum = data.totalNum;
						  var sNum = data.sNum;
						  var errNum = data.errNum;
						  var errForbid = data.errForbid;
						  var str= "发送：" + totalNum + "条，成功：" + sNum + "条，失败：" + errNum + "条。 "
						  if(errNum>0){
							  str += "\n发送失败的的手机号如下：";
							  var result=content.split(",");
							  for(var i=0;i<result.length;i++){
								  if(i%3==0){
									  str += "\n";
								  }
							      str += result[i];
							      if((i+1)%3!=0&&i!=result.length-1){
							    	  str += ",";
							      }
							  }
						  }
						  if(errForbid.length>0){
							  str += "\n其中五分钟内重复发送的手机号如下：";
							  var result=errForbid.split(",");
							  for(var i=0;i<result.length;i++){
								  if(i%3==0){
									  str += "\n";
								  }
							      str += result[i];
							      if((i+1)%3!=0&&i!=result.length-1){
							    	  str += ",";
							      }
							  }
						  }
						  alert(str);
						  return false;
					  },"json");
				  }
			  }
		  });
	  }
  }
  function phonebookSelect(){
   var content12=document.getElementById("content").value;
	var phone12=document.getElementById("phone").value;
	content12=content12.replace(/\%/g,'%25');
	var url = "phonebookSelectnum.action?content12="+content12+"&phone12="+phone12;
	
	var page = window.open('','_blank','width=700,height=650,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=no');
 	page.location=url;
 	return false; 	
}